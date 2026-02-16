package com.hongguoyan.module.biz.service.vipcoupon;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.admin.vipcoupon.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.vipcouponbatch.VipCouponBatchDO;
import com.hongguoyan.module.biz.dal.dataobject.vipcouponcode.VipCouponCodeDO;
import com.hongguoyan.module.biz.dal.mysql.vipcouponbatch.VipCouponBatchMapper;
import com.hongguoyan.module.biz.dal.mysql.vipcouponcode.VipCouponCodeMapper;
import com.hongguoyan.module.biz.enums.vip.VipCouponBatchStatusEnum;
import com.hongguoyan.module.biz.enums.vip.VipCouponStatusEnum;
import com.hongguoyan.module.infra.api.file.FileApi;
import com.hongguoyan.module.member.api.user.MemberUserApi;
import com.hongguoyan.module.member.api.user.dto.MemberUserRespDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.invalidParamException;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.VIP_COUPON_CODE_NOT_EXISTS;

@Service
@Validated
public class VipCouponServiceImpl implements VipCouponService {

    private static final DateTimeFormatter YYMMDD = DateTimeFormatter.ofPattern("yyMMdd");
    private static final int BATCH_SEQ_WIDTH = 3;

    @Resource
    private VipCouponCodeMapper vipCouponCodeMapper;
    @Resource
    private VipCouponBatchMapper vipCouponBatchMapper;
    @Resource
    private MemberUserApi memberUserApi;
    @Resource
    private FileApi fileApi;

    @Override
    public VipCouponSummaryRespVO getVipCouponSummary() {
        LocalDateTime now = LocalDateTime.now();
        Long total = vipCouponCodeMapper.selectCount(new LambdaQueryWrapperX<VipCouponCodeDO>());
        Long used = vipCouponCodeMapper.selectCount(new LambdaQueryWrapperX<VipCouponCodeDO>()
                .eq(VipCouponCodeDO::getStatus, VipCouponStatusEnum.USED.getCode()));
        Long expired = vipCouponCodeMapper.selectCount(new LambdaQueryWrapperX<VipCouponCodeDO>()
                .and(qw -> qw.eq(VipCouponCodeDO::getStatus, VipCouponStatusEnum.EXPIRED.getCode())
                        .or(w -> w.eq(VipCouponCodeDO::getStatus, VipCouponStatusEnum.UNUSED.getCode())
                                .lt(VipCouponCodeDO::getValidEndTime, now))));
        Long unused = vipCouponCodeMapper.selectCount(new LambdaQueryWrapperX<VipCouponCodeDO>()
                .eq(VipCouponCodeDO::getStatus, VipCouponStatusEnum.UNUSED.getCode())
                .ge(VipCouponCodeDO::getValidEndTime, now));
        VipCouponSummaryRespVO resp = new VipCouponSummaryRespVO();
        resp.setTotalCount(total == null ? 0L : total);
        resp.setUnusedCount(unused == null ? 0L : unused);
        resp.setUsedCount(used == null ? 0L : used);
        resp.setExpiredCount(expired == null ? 0L : expired);
        return resp;
    }

    @Override
    public String previewVipCouponBatchNo(String planCode) {
        String plan = StrUtil.trim(planCode).toUpperCase();
        if (StrUtil.isBlank(plan)) {
            throw invalidParamException("套餐类型不能为空");
        }
        return generateBatchNo(plan);
    }

    @Override
    public PageResult<VipCouponRespVO> getVipCouponPage(VipCouponPageReqVO pageReqVO) {
        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapperX<VipCouponCodeDO> wrapper = new LambdaQueryWrapperX<VipCouponCodeDO>()
                .eqIfPresent(VipCouponCodeDO::getPlanCode, pageReqVO.getPlanCode());

        // status（兜底：未使用 + 已过期时间，也算过期）
        if (pageReqVO.getStatus() != null) {
            if (Objects.equals(pageReqVO.getStatus(), VipCouponStatusEnum.EXPIRED.getCode())) {
                wrapper.and(qw -> qw.eq(VipCouponCodeDO::getStatus, VipCouponStatusEnum.EXPIRED.getCode())
                        .or(w -> w.eq(VipCouponCodeDO::getStatus, VipCouponStatusEnum.UNUSED.getCode())
                                .lt(VipCouponCodeDO::getValidEndTime, now)));
            } else if (Objects.equals(pageReqVO.getStatus(), VipCouponStatusEnum.UNUSED.getCode())) {
                wrapper.eq(VipCouponCodeDO::getStatus, VipCouponStatusEnum.UNUSED.getCode())
                        .ge(VipCouponCodeDO::getValidEndTime, now);
            } else {
                wrapper.eq(VipCouponCodeDO::getStatus, pageReqVO.getStatus());
            }
        }

        // keyword（券码 / 批次号）
        if (StrUtil.isNotBlank(pageReqVO.getKeyword())) {
            String kw = pageReqVO.getKeyword().trim();
            wrapper.and(qw -> qw.like(VipCouponCodeDO::getCode, kw)
                    .or()
                    .like(VipCouponCodeDO::getBatchNo, kw));
        }

        wrapper.orderByDesc(VipCouponCodeDO::getId);
        PageResult<VipCouponCodeDO> pageResult = vipCouponCodeMapper.selectPage(pageReqVO, wrapper);
        PageResult<VipCouponRespVO> result = BeanUtils.toBean(pageResult, VipCouponRespVO.class);
        normalizeExpiredStatus(result.getList(), now);
        fillUserInfo(result.getList());
        return result;
    }

    @Override
    public VipCouponRespVO getVipCoupon(Long id) {
        VipCouponCodeDO coupon = vipCouponCodeMapper.selectById(id);
        if (coupon == null) {
            throw exception(VIP_COUPON_CODE_NOT_EXISTS);
        }
        VipCouponRespVO resp = BeanUtils.toBean(coupon, VipCouponRespVO.class);
        normalizeExpiredStatus(List.of(resp), LocalDateTime.now());
        fillUserInfo(List.of(resp));
        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String generateVipCouponBatch(VipCouponGenerateReqVO reqVO) {
        validateValidTimeRange(reqVO.getValidStartTime(), reqVO.getValidEndTime());
        int totalCount = reqVO.getTotalCount() != null ? reqVO.getTotalCount() : 0;
        if (totalCount <= 0) {
            throw invalidParamException("数量必须大于 0");
        }

        String planCode = StrUtil.trim(reqVO.getPlanCode());
        String batchNo = generateBatchNo(planCode);

        VipCouponBatchDO batch = new VipCouponBatchDO();
        batch.setBatchNo(batchNo);
        batch.setPlanCode(planCode);
        batch.setValidStartTime(reqVO.getValidStartTime());
        batch.setValidEndTime(reqVO.getValidEndTime());
        batch.setTotalCount(totalCount);
        batch.setUsedCount(0);
        batch.setStatus(VipCouponBatchStatusEnum.ENABLE.getCode());
        batch.setRemark(reqVO.getRemark());
        vipCouponBatchMapper.insert(batch);

        LocalDateTime now = LocalDateTime.now();
        List<VipCouponCodeDO> codes = new ArrayList<>(totalCount);
        for (int i = 0; i < totalCount; i++) {
            VipCouponCodeDO code = new VipCouponCodeDO();
            code.setCode(generateCouponCode(planCode, now));
            code.setBatchNo(batchNo);
            code.setPlanCode(planCode);
            code.setStatus(VipCouponStatusEnum.UNUSED.getCode());
            code.setValidStartTime(reqVO.getValidStartTime());
            code.setValidEndTime(reqVO.getValidEndTime());
            code.setRemark(reqVO.getRemark());
            codes.add(code);
        }
        // 批量插入，遇到唯一冲突时逐条重试
        try {
            vipCouponCodeMapper.insertBatch(codes, 1000);
        } catch (Exception ex) {
            // fallback：逐条插入并重试 code 冲突
            for (VipCouponCodeDO item : codes) {
                insertWithRetry(item, planCode);
            }
        }

        return batchNo;
    }

    @Override
    public void updateVipCoupon(VipCouponUpdateReqVO reqVO) {
        validateValidTimeRange(reqVO.getValidStartTime(), reqVO.getValidEndTime());
        VipCouponCodeDO coupon = vipCouponCodeMapper.selectById(reqVO.getId());
        if (coupon == null) {
            throw exception(VIP_COUPON_CODE_NOT_EXISTS);
        }
        validateCouponEditable(coupon);
        VipCouponCodeDO update = new VipCouponCodeDO();
        update.setId(coupon.getId());
        update.setValidStartTime(reqVO.getValidStartTime());
        update.setValidEndTime(reqVO.getValidEndTime());
        update.setRemark(reqVO.getRemark());
        vipCouponCodeMapper.updateById(update);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteVipCoupon(Long id) {
        VipCouponCodeDO coupon = vipCouponCodeMapper.selectById(id);
        if (coupon == null) {
            throw exception(VIP_COUPON_CODE_NOT_EXISTS);
        }
        validateCouponEditable(coupon);
        int deleted = vipCouponCodeMapper.deleteById(id);
        if (deleted > 0) {
            // 同步减少批次数量，避免 total_count 与实际码数量偏离
            vipCouponBatchMapper.update(null, new LambdaUpdateWrapper<VipCouponBatchDO>()
                    .setSql("total_count = total_count - 1")
                    .eq(VipCouponBatchDO::getBatchNo, coupon.getBatchNo()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int expireCoupon() {
        LocalDateTime now = LocalDateTime.now();
        return vipCouponCodeMapper.update(null, new LambdaUpdateWrapper<VipCouponCodeDO>()
                .set(VipCouponCodeDO::getStatus, VipCouponStatusEnum.EXPIRED.getCode())
                .eq(VipCouponCodeDO::getStatus, VipCouponStatusEnum.UNUSED.getCode())
                .lt(VipCouponCodeDO::getValidEndTime, now));
    }

    private void normalizeExpiredStatus(List<VipCouponRespVO> list, LocalDateTime now) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        for (VipCouponRespVO item : list) {
            if (item == null) {
                continue;
            }
            if (VipCouponStatusEnum.isUnused(item.getStatus())
                    && item.getValidEndTime() != null
                    && item.getValidEndTime().isBefore(now)) {
                item.setStatus(VipCouponStatusEnum.EXPIRED.getCode());
            }
        }
    }

    private void fillUserInfo(List<VipCouponRespVO> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        Set<Long> userIds = new LinkedHashSet<>();
        for (VipCouponRespVO item : list) {
            if (item != null && item.getUserId() != null) {
                userIds.add(item.getUserId());
            }
        }
        if (userIds.isEmpty()) {
            return;
        }
        Map<Long, MemberUserRespDTO> userMap = memberUserApi.getUserMap(userIds);
        for (VipCouponRespVO item : list) {
            if (item == null || item.getUserId() == null) {
                continue;
            }
            MemberUserRespDTO user = userMap.get(item.getUserId());
            if (user != null) {
                item.setUserNickname(user.getNickname());
                item.setUserMobile(user.getMobile());
                item.setUserAvatar(fileApi.buildStaticUrl(user.getAvatar()));
            }
        }
    }

    private void validateCouponEditable(VipCouponCodeDO coupon) {
        LocalDateTime now = LocalDateTime.now();
        if (!VipCouponStatusEnum.isUnused(coupon.getStatus())) {
            throw new IllegalStateException("仅未使用券码允许操作");
        }
        if (coupon.getValidEndTime() != null && coupon.getValidEndTime().isBefore(now)) {
            throw new IllegalStateException("券码已过期");
        }
    }

    private void validateValidTimeRange(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || !start.isBefore(end)) {
            throw invalidParamException("有效期不合法");
        }
    }

    private String generateBatchNo(String planCode) {
        String plan = StrUtil.trim(planCode).toUpperCase();
        Integer maxSeq = vipCouponBatchMapper.selectMaxSeqByPlanCode(plan);
        int nextSeq = (maxSeq == null ? 0 : maxSeq) + 1;
        String seq = String.format("%0" + BATCH_SEQ_WIDTH + "d", nextSeq);
        String date = YYMMDD.format(LocalDateTime.now());
        // BATCH_{PLAN}_{NNN}_{yyMMdd}
        return "BATCH_" + plan + "_" + seq + "_" + date;
    }

    private String generateCouponCode(String planCode, LocalDateTime now) {
        String plan = StrUtil.trim(planCode).toUpperCase();
        String prefix = Objects.equals(plan, "SVIP") ? "S" : "V";
        String date = YYMMDD.format(now);
        String rand = UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
        return prefix + date + rand;
    }

    private void insertWithRetry(VipCouponCodeDO item, String planCode) {
        int retry = 0;
        LocalDateTime now = LocalDateTime.now();
        while (true) {
            try {
                vipCouponCodeMapper.insert(item);
                return;
            } catch (Exception ex) {
                if (++retry >= 3) {
                    throw ex;
                }
                item.setCode(generateCouponCode(planCode, now));
            }
        }
    }
}

