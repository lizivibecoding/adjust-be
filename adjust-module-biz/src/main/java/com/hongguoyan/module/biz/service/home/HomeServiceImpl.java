package com.hongguoyan.module.biz.service.home;

import com.hongguoyan.module.biz.controller.admin.home.vo.HomeStatsRespVO;
import com.hongguoyan.module.biz.dal.mysql.home.HomeStatsMapper;
import com.hongguoyan.module.biz.dal.mysql.home.dto.HomeDateCountDTO;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 首页统计 Service 实现类（只读聚合）
 */
@Service
@Validated
public class HomeServiceImpl implements HomeService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Resource
    private HomeStatsMapper homeStatsMapper;

    @Override
    public HomeStatsRespVO getHomeStats() {
        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDate beginDate = today.minusDays(6);
        LocalDateTime beginTime = beginDate.atStartOfDay();

        HomeStatsRespVO respVO = new HomeStatsRespVO();
        respVO.setTodayNewUserCount(nvl(homeStatsMapper.selectTodayNewUserCount(todayStart)));
        respVO.setTodayUpdatedAdjustmentCount(nvl(homeStatsMapper.selectTodayUpdatedAdjustmentCount(todayStart)));
        respVO.setPendingPublisherAuditCount(nvl(homeStatsMapper.selectPendingPublisherAuditCount()));
        respVO.setTodayVipPaidAmount(nvl(homeStatsMapper.selectTodayVipPaidAmount(todayStart)));

        // trend: new users
        Map<String, Long> newUserDailyMap = toMap(homeStatsMapper.selectDailyNewUserCount(beginTime));
        List<HomeStatsRespVO.DateCount> growthList = new ArrayList<>(7);
        for (int i = 0; i < 7; i++) {
            LocalDate d = beginDate.plusDays(i);
            String key = DATE_FMT.format(d);
            HomeStatsRespVO.DateCount item = new HomeStatsRespVO.DateCount();
            item.setDate(key);
            item.setCount(nvl(newUserDailyMap.get(key)));
            growthList.add(item);
        }
        respVO.setUserGrowthList(growthList);

        // conversion: paid orders / new users
        Map<String, Long> paidNewUserDailyMap = toMap(homeStatsMapper.selectDailyPaidNewUserCount(beginTime));
        List<HomeStatsRespVO.ConversionPoint> conversionList = new ArrayList<>(7);
        for (int i = 0; i < 7; i++) {
            LocalDate d = beginDate.plusDays(i);
            String key = DATE_FMT.format(d);
            long newUsers = nvl(newUserDailyMap.get(key));
            long paidNewUsers = nvl(paidNewUserDailyMap.get(key));
            BigDecimal rate = BigDecimal.ZERO;
            if (newUsers > 0) {
                rate = BigDecimal.valueOf(paidNewUsers)
                        .multiply(BigDecimal.valueOf(100))
                        .divide(BigDecimal.valueOf(newUsers), 2, RoundingMode.HALF_UP);
            }

            HomeStatsRespVO.ConversionPoint item = new HomeStatsRespVO.ConversionPoint();
            item.setDate(key);
            item.setNewUserCount(newUsers);
            item.setPaidNewUserCount(paidNewUsers);
            item.setConversionRate(rate);
            conversionList.add(item);
        }
        respVO.setConversionList(conversionList);

        return respVO;
    }

    private Map<String, Long> toMap(List<HomeDateCountDTO> list) {
        Map<String, Long> map = new HashMap<>();
        if (list == null || list.isEmpty()) {
            return map;
        }
        for (HomeDateCountDTO item : list) {
            if (item == null || item.getDate() == null) {
                continue;
            }
            map.put(item.getDate(), nvl(item.getCount()));
        }
        return map;
    }

    private long nvl(Long v) {
        return v != null ? v : 0L;
    }
}

