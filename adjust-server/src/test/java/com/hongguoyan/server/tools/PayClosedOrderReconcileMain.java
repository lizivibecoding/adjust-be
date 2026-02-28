package com.hongguoyan.server.tools;

import com.hongguoyan.module.pay.enums.order.PayOrderStatusEnum;
import com.hongguoyan.module.pay.framework.pay.core.client.PayClient;
import com.hongguoyan.module.pay.framework.pay.core.client.dto.order.PayOrderRespDTO;
import com.hongguoyan.module.pay.service.channel.PayChannelService;
import com.hongguoyan.server.AdjustServerApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 本地只读对账脚本：拉取指定时间段内已关闭支付单，按 out_trade_no 调渠道查单并打印结果。
 *
 * 注意：仅做“查单 + 控制台输出”，不更新任何数据库数据。
 *
 * 运行参数（可选）：
 * - --profile=local|dev|prod
 * - --from=yyyy-MM-dd HH:mm:ss
 * - --to=yyyy-MM-dd HH:mm:ss
 * - --threads=5
 * - --limit=0  (0 表示不限制)
 */
public class PayClosedOrderReconcileMain {

  private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public static void main(String[] args) throws Exception {
    Args a = Args.parse(args);
    LocalDateTime from = a.from != null ? a.from : LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
    LocalDateTime to = a.to != null ? a.to : LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 0));
    int threads = a.threads > 0 ? a.threads : 5;

    System.out.println("=== PayClosedOrderReconcileMain ===");
    System.out.println("profile=" + (a.profile == null ? "<default>" : a.profile));
    System.out.println("from=" + from.format(DT) + ", to=" + to.format(DT));
    System.out.println("threads=" + threads + ", limit=" + a.limit);

    ConfigurableApplicationContext ctx = new SpringApplicationBuilder(AdjustServerApplication.class)
        // 使用完整 Web 应用启动，避免安全相关自动配置缺失（例如 AuthenticationConfiguration）
        // 同时将端口设置为 0（随机端口）避免本地端口冲突
        .web(WebApplicationType.SERVLET)
        .profiles(a.profile == null ? new String[]{} : new String[]{a.profile})
        .properties(
            "server.port=0",
            "spring.main.lazy-initialization=true",
            "spring.main.banner-mode=off"
        )
        .run(args);
    try {
      run(ctx, from, to, threads, a.limit);
    } finally {
      ctx.close();
    }
  }

  private static void run(ConfigurableApplicationContext ctx,
                          LocalDateTime from,
                          LocalDateTime to,
                          int threads,
                          int limit) throws Exception {
    JdbcTemplate jdbcTemplate = ctx.getBean(JdbcTemplate.class);
    PayChannelService payChannelService = ctx.getBean(PayChannelService.class);

    List<OrderRow> rows = queryClosedOrders(jdbcTemplate, from, to, limit);
    Map<Long, OrderAgg> aggMap = aggregateByPayOrder(rows);
    System.out.println("待对账支付单数=" + aggMap.size() + "，extension数=" + rows.size());
    if (rows.isEmpty()) {
      return;
    }

    AtomicInteger successCnt = new AtomicInteger();
    AtomicInteger waitingCnt = new AtomicInteger();
    AtomicInteger closedCnt = new AtomicInteger();
    AtomicInteger refundCnt = new AtomicInteger();
    AtomicInteger unknownCnt = new AtomicInteger();
    AtomicInteger errorCnt = new AtomicInteger();

    ExecutorService pool = Executors.newFixedThreadPool(threads);
    Semaphore sem = new Semaphore(Math.max(1, threads));
    List<OrderAgg> aggs = new ArrayList<>(aggMap.values());
    List<CompletableFuture<Void>> futures = new ArrayList<>(aggs.size());
    for (OrderAgg agg : aggs) {
      futures.add(CompletableFuture.runAsync(() -> {
        try {
          sem.acquire();
          reconcileOneOrder(payChannelService, agg, successCnt, waitingCnt, closedCnt, refundCnt, unknownCnt, errorCnt);
        } catch (Throwable e) {
          errorCnt.incrementAndGet();
          System.out.println("[ERROR] payOrderId=" + agg.payOrderId
              + " vipUserId=" + nullToEmpty(agg.vipUserId)
              + " vipOrderNo=" + nullToEmpty(agg.vipOrderNo)
              + " msg=" + safeMsg(e));
        } finally {
          sem.release();
        }
      }, pool));
    }

    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    pool.shutdown();
    pool.awaitTermination(30, TimeUnit.SECONDS);

    System.out.println("=== 对账结果汇总 ===");
    System.out.println("SUCCESS(10)=" + successCnt.get());
    System.out.println("WAITING(0)=" + waitingCnt.get());
    System.out.println("CLOSED(30)=" + closedCnt.get());
    System.out.println("REFUND(20)=" + refundCnt.get());
    System.out.println("UNKNOWN=" + unknownCnt.get());
    System.out.println("ERROR=" + errorCnt.get());
  }

  private static void reconcileOneOrder(PayChannelService payChannelService,
                                        OrderAgg agg,
                                        AtomicInteger successCnt,
                                        AtomicInteger waitingCnt,
                                        AtomicInteger closedCnt,
                                        AtomicInteger refundCnt,
                                        AtomicInteger unknownCnt,
                                        AtomicInteger errorCnt) {
    if (agg.exts.isEmpty()) {
      unknownCnt.incrementAndGet();
      System.out.println("[SKIP] payOrderId=" + agg.payOrderId + " 无 out_trade_no 可查");
      return;
    }

    boolean sawWaiting = false;
    boolean sawClosed = false;
    boolean sawRefund = false;
    boolean sawUnknown = false;
    boolean sawError = false;

    ExtRow hitExt = null;
    PayOrderRespDTO hitResp = null;

    for (ExtRow ext : agg.exts.values()) {
      if (ext.channelId == null || ext.outTradeNo == null || ext.outTradeNo.isBlank()) {
        sawUnknown = true;
        continue;
      }
      try {
        PayClient<?> client = payChannelService.getPayClient(ext.channelId);
        PayOrderRespDTO resp = client.getOrder(ext.outTradeNo);
        Integer st = resp != null ? resp.getStatus() : null;
        if (Objects.equals(st, PayOrderStatusEnum.SUCCESS.getStatus())) {
          hitExt = ext;
          hitResp = resp;
          break; // 任意一笔 out_trade_no 成功，则该 pay_order 视为成功
        }
        if (Objects.equals(st, PayOrderStatusEnum.REFUND.getStatus())) {
          sawRefund = true;
          continue;
        }
        if (Objects.equals(st, PayOrderStatusEnum.CLOSED.getStatus())) {
          sawClosed = true;
          continue;
        }
        if (Objects.equals(st, PayOrderStatusEnum.WAITING.getStatus())) {
          sawWaiting = true;
          continue;
        }
        sawUnknown = true;
      } catch (Throwable e) {
        sawError = true;
        System.out.println("[EXT_ERROR] payOrderId=" + agg.payOrderId
            + " extId=" + ext.extId
            + " outTradeNo=" + nullToEmpty(ext.outTradeNo)
            + " channelId=" + nullToEmpty(ext.channelId)
            + " msg=" + safeMsg(e));
      }
    }

    if (hitExt != null) {
      successCnt.incrementAndGet();
      System.out.println("[SUCCESS] payOrderId=" + agg.payOrderId
          + " vipUserId=" + nullToEmpty(agg.vipUserId)
          + " vipOrderNo=" + nullToEmpty(agg.vipOrderNo)
          + " extCount=" + agg.exts.size()
          + " hitExtId=" + hitExt.extId
          + " outTradeNo=" + hitExt.outTradeNo
          + " channelId=" + nullToEmpty(hitExt.channelId)
          + " channelOrderNo=" + (hitResp == null ? "" : nullToEmpty(hitResp.getChannelOrderNo()))
          + " successTime=" + (hitResp == null || hitResp.getSuccessTime() == null ? "" : hitResp.getSuccessTime())
          + " closeTime=" + (agg.closeTime == null ? "" : agg.closeTime));
      return;
    }

    if (sawRefund) {
      refundCnt.incrementAndGet();
      return;
    }
    if (sawClosed) {
      closedCnt.incrementAndGet();
      return;
    }
    if (sawWaiting) {
      waitingCnt.incrementAndGet();
      return;
    }
    if (sawUnknown) {
      unknownCnt.incrementAndGet();
      return;
    }
    if (sawError) {
      errorCnt.incrementAndGet();
    } else {
      unknownCnt.incrementAndGet();
    }
  }

  private static Map<Long, OrderAgg> aggregateByPayOrder(List<OrderRow> rows) {
    Map<Long, OrderAgg> map = new LinkedHashMap<>();
    for (OrderRow row : rows) {
      if (row.payOrderId == null) {
        continue;
      }
      OrderAgg agg = map.get(row.payOrderId);
      if (agg == null) {
        agg = new OrderAgg(row.payOrderId, row.closeTime, row.vipUserId, row.vipOrderNo);
        map.put(row.payOrderId, agg);
      }
      agg.addExt(row.extId, row.outTradeNo, row.channelId);
    }
    return map;
  }

  private static List<OrderRow> queryClosedOrders(JdbcTemplate jdbcTemplate,
                                                  LocalDateTime from,
                                                  LocalDateTime to,
                                                  int limit) {
    // 只对账“会员相关 + 当前无有效订阅”的关单支付单：
    // 1) pay_order.status=30 且 update_time 在时间窗内
    // 2) 必须能关联到 biz_vip_order（会员下单）
    // 3) 用户当前无有效订阅（biz_vip_subscription.end_time < now 或不存在）
    // 4) 必须有 out_trade_no（pay_order_extension.no），同一个 pay_order 取所有 extension 逐个查渠道
    String pickedSql = """
        SELECT
          po.id AS pay_order_id,
          po.update_time AS close_time,
          vo.user_id AS vip_user_id,
          vo.order_no AS vip_order_no
        FROM pay_order po
        JOIN vip_pick vp ON vp.pay_order_id = po.id
        JOIN biz_vip_order vo ON vo.id = vp.vip_order_id
        LEFT JOIN biz_vip_subscription s
          ON s.user_id = vo.user_id AND s.end_time >= NOW()
        WHERE po.status = 30
          AND po.update_time >= ?
          AND po.update_time < ?
          AND s.id IS NULL
        ORDER BY po.update_time DESC
        """;
    if (limit > 0) {
      pickedSql = pickedSql + " LIMIT " + limit;
    }

    String sql = """
        WITH vip_pick AS (
          SELECT pay_order_id, MAX(id) AS vip_order_id
          FROM biz_vip_order
          GROUP BY pay_order_id
        )
        SELECT
          picked.pay_order_id,
          picked.close_time,
          picked.vip_user_id,
          picked.vip_order_no,
          poe.id AS ext_id,
          poe.no AS out_trade_no,
          poe.channel_id AS channel_id
        FROM (
        """ + pickedSql + """
        ) picked
        JOIN pay_order_extension poe
          ON poe.order_id = picked.pay_order_id
         AND poe.no IS NOT NULL AND poe.no <> ''
        ORDER BY picked.close_time DESC, poe.id DESC
        """;

    return jdbcTemplate.query(sql, ps -> {
      ps.setObject(1, from);
      ps.setObject(2, to);
    }, (rs, rowNum) -> new OrderRow(
        rs.getLong("pay_order_id"),
        rs.getTimestamp("close_time") != null ? rs.getTimestamp("close_time").toLocalDateTime() : null,
        rs.getObject("vip_user_id") != null ? rs.getLong("vip_user_id") : null,
        rs.getString("vip_order_no"),
        rs.getObject("ext_id") != null ? rs.getLong("ext_id") : null,
        rs.getString("out_trade_no"),
        rs.getObject("channel_id") != null ? rs.getLong("channel_id") : null
    ));
  }

  private static String nullToEmpty(String s) {
    return s == null ? "" : s;
  }

  private static String nullToEmpty(Long v) {
    return v == null ? "" : String.valueOf(v);
  }

  private static String safeMsg(Throwable e) {
    String msg = e.getMessage();
    if (msg != null && msg.length() > 300) {
      return msg.substring(0, 300);
    }
    return msg == null ? e.getClass().getSimpleName() : msg;
  }

  private record OrderRow(Long payOrderId,
                          LocalDateTime closeTime,
                          Long vipUserId,
                          String vipOrderNo,
                          Long extId,
                          String outTradeNo,
                          Long channelId) {}

  private static class OrderAgg {
    final Long payOrderId;
    final LocalDateTime closeTime;
    final Long vipUserId;
    final String vipOrderNo;
    final Map<String, ExtRow> exts = new LinkedHashMap<>();

    OrderAgg(Long payOrderId, LocalDateTime closeTime, Long vipUserId, String vipOrderNo) {
      this.payOrderId = payOrderId;
      this.closeTime = closeTime;
      this.vipUserId = vipUserId;
      this.vipOrderNo = vipOrderNo;
    }

    void addExt(Long extId, String outTradeNo, Long channelId) {
      if (outTradeNo == null || outTradeNo.isBlank()) {
        return;
      }
      // 同一个 out_trade_no 去重，避免重复请求渠道
      exts.putIfAbsent(outTradeNo, new ExtRow(extId, outTradeNo, channelId));
    }
  }

  private record ExtRow(Long extId, String outTradeNo, Long channelId) {}

  private static class Args {
    String profile;
    LocalDateTime from;
    LocalDateTime to;
    int threads = 5;
    int limit = 0;

    static Args parse(String[] args) {
      Args a = new Args();
      if (args == null) {
        return a;
      }
      for (String s : args) {
        if (s == null) {
          continue;
        }
        if (s.startsWith("--profile=")) {
          a.profile = s.substring("--profile=".length()).trim();
        } else if (s.startsWith("--from=")) {
          a.from = LocalDateTime.parse(s.substring("--from=".length()).trim(), DT);
        } else if (s.startsWith("--to=")) {
          a.to = LocalDateTime.parse(s.substring("--to=".length()).trim(), DT);
        } else if (s.startsWith("--threads=")) {
          a.threads = Integer.parseInt(s.substring("--threads=".length()).trim());
        } else if (s.startsWith("--limit=")) {
          a.limit = Integer.parseInt(s.substring("--limit=".length()).trim());
        }
      }
      return a;
    }
  }
}

