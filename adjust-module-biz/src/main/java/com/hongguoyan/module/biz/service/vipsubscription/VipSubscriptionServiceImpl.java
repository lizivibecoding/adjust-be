package com.hongguoyan.module.biz.service.vipsubscription;

import cn.hutool.core.util.StrUtil;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.admin.vipsubscription.vo.VipSubscriptionPageReqVO;
import com.hongguoyan.module.biz.controller.admin.vipsubscription.vo.VipSubscriptionRespVO;
import com.hongguoyan.module.biz.controller.admin.vipsubscription.vo.VipSubscriptionSummaryRespVO;
import com.hongguoyan.module.biz.dal.dataobject.vipsubscription.VipSubscriptionDO;
import com.hongguoyan.module.biz.dal.mysql.vipsubscription.VipSubscriptionMapper;
import com.hongguoyan.module.infra.api.file.FileApi;
import com.hongguoyan.module.member.api.user.MemberUserApi;
import com.hongguoyan.module.member.api.user.dto.MemberUserRespDTO;
import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.VIP_SUBSCRIPTION_NOT_EXISTS;

/**
 * 用户会员订阅 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class VipSubscriptionServiceImpl implements VipSubscriptionService {

    private static final int MEMBER_STATUS_EFFECTIVE = 1;
    private static final int MEMBER_STATUS_EXPIRED = 2;

    @Resource
    private VipSubscriptionMapper vipSubscriptionMapper;
    @Resource
    private MemberUserApi memberUserApi;
    @Resource
    private FileApi fileApi;

    @Override
    public VipSubscriptionRespVO getVipSubscription(Long id) {
        VipSubscriptionDO subscription = vipSubscriptionMapper.selectById(id);
        if (subscription == null) {
            throw exception(VIP_SUBSCRIPTION_NOT_EXISTS);
        }
        VipSubscriptionRespVO respVO = BeanUtils.toBean(subscription, VipSubscriptionRespVO.class);
        setMemberStatus(respVO);
        fillUserInfo(List.of(respVO));
        return respVO;
    }

    @Override
    public PageResult<VipSubscriptionRespVO> getVipSubscriptionPage(VipSubscriptionPageReqVO pageReqVO) {
        applyKeywordFilter(pageReqVO);
        PageResult<VipSubscriptionDO> pageResult = vipSubscriptionMapper.selectPage(pageReqVO);
        PageResult<VipSubscriptionRespVO> result = BeanUtils.toBean(pageResult, VipSubscriptionRespVO.class);
        result.getList().forEach(this::setMemberStatus);
        fillUserInfo(result.getList());
        return result;
    }

    @Override
    public VipSubscriptionSummaryRespVO getVipSubscriptionSummary() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime monthStart = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime monthEnd = LocalDate.now().plusMonths(1).withDayOfMonth(1).atStartOfDay().minusSeconds(1);

        Long vipCount = vipSubscriptionMapper.selectCount(new LambdaQueryWrapperX<VipSubscriptionDO>()
                .eq(VipSubscriptionDO::getPlanCode, "VIP")
                .ge(VipSubscriptionDO::getEndTime, now));
        Long svipCount = vipSubscriptionMapper.selectCount(new LambdaQueryWrapperX<VipSubscriptionDO>()
                .eq(VipSubscriptionDO::getPlanCode, "SVIP")
                .ge(VipSubscriptionDO::getEndTime, now));
        Long monthNewCount = vipSubscriptionMapper.selectCount(new LambdaQueryWrapperX<VipSubscriptionDO>()
                .between(VipSubscriptionDO::getCreateTime, monthStart, monthEnd));

        VipSubscriptionSummaryRespVO respVO = new VipSubscriptionSummaryRespVO();
        respVO.setVipCount(vipCount == null ? 0L : vipCount);
        respVO.setSvipCount(svipCount == null ? 0L : svipCount);
        respVO.setMonthNewCount(monthNewCount == null ? 0L : monthNewCount);
        return respVO;
    }

    private void setMemberStatus(VipSubscriptionRespVO respVO) {
        if (respVO == null) {
            return;
        }
        LocalDateTime endTime = respVO.getEndTime();
        if (endTime != null && endTime.isBefore(LocalDateTime.now())) {
            respVO.setMemberStatus(MEMBER_STATUS_EXPIRED);
        } else {
            respVO.setMemberStatus(MEMBER_STATUS_EFFECTIVE);
        }
    }

    private void applyKeywordFilter(VipSubscriptionPageReqVO pageReqVO) {
        if (StrUtil.isBlank(pageReqVO.getKeyword())) {
            return;
        }
        String keyword = pageReqVO.getKeyword().trim();
        Set<Long> userIds = new LinkedHashSet<>();

        if (keyword.matches("\\d+")) {
            userIds.add(Long.valueOf(keyword));
        }
        MemberUserRespDTO userByMobile = memberUserApi.getUserByMobile(keyword);
        if (userByMobile != null) {
            userIds.add(userByMobile.getId());
        }
        List<MemberUserRespDTO> usersByNickname = memberUserApi.getUserListByNickname(keyword);
        if (usersByNickname != null) {
            usersByNickname.forEach(item -> userIds.add(item.getId()));
        }

        if (userIds.isEmpty()) {
            pageReqVO.setUserIds(List.of(-1L));
        } else {
            pageReqVO.setUserIds(new ArrayList<>(userIds));
        }
    }

    private void fillUserInfo(List<VipSubscriptionRespVO> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        Set<Long> userIds = new LinkedHashSet<>();
        list.forEach(item -> {
            if (item.getUserId() != null) {
                userIds.add(item.getUserId());
            }
        });
        if (userIds.isEmpty()) {
            return;
        }

        Map<Long, MemberUserRespDTO> userMap = memberUserApi.getUserMap(userIds);
        list.forEach(item -> {
            MemberUserRespDTO user = userMap.get(item.getUserId());
            if (user != null) {
                item.setUserNickname(user.getNickname());
                item.setUserAvatar(fileApi.buildStaticUrl(user.getAvatar()));
            }
        });
    }
}