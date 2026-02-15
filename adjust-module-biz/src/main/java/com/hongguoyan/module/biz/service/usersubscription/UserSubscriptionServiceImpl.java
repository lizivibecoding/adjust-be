package com.hongguoyan.module.biz.service.usersubscription;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.time.LocalDateTime;
import com.hongguoyan.module.biz.dal.dataobject.usersubscription.UserSubscriptionDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.controller.app.usersubscription.vo.AppUserSubscriptionPageMajorRespVO;
import com.hongguoyan.module.biz.controller.app.usersubscription.vo.AppUserSubscriptionPageReqVO;
import com.hongguoyan.module.biz.controller.app.usersubscription.vo.AppUserSubscriptionPageRespVO;
import com.hongguoyan.module.biz.controller.app.usersubscription.vo.AppUserSubscriptionSubscribeReqVO;
import com.hongguoyan.module.biz.controller.app.usersubscription.vo.AppUserSubscriptionUnreadRespVO;
import com.hongguoyan.module.biz.controller.app.usersubscription.vo.AppUserSubscriptionUnsubscribeReqVO;

import com.hongguoyan.module.biz.dal.dataobject.adjustment.AdjustmentDO;
import com.hongguoyan.module.biz.dal.mysql.adjustment.AdjustmentMapper;
import com.hongguoyan.module.biz.dal.mysql.usersubscription.UserSubscriptionMapper;
import com.hongguoyan.module.biz.service.vipbenefit.VipBenefitService;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.BENEFIT_KEY_USER_SUBSCRIPTION;

/**
 * 用户调剂订阅 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

    private static final long HOT_SCORE_SUBSCRIBE_DELTA = 5L;

    @Resource
    private UserSubscriptionMapper userSubscriptionMapper;
    @Resource
    private VipBenefitService vipBenefitService;

    @Override
    public void subscribe(Long userId, AppUserSubscriptionSubscribeReqVO reqVO) {
        vipBenefitService.checkEnabledOrThrow(userId, BENEFIT_KEY_USER_SUBSCRIPTION);
        Long adjustmentId = reqVO != null ? reqVO.getAdjustmentId() : null;
        AdjustmentDO adjustment = adjustmentMapper.selectById(adjustmentId);
        if (adjustment == null) {
            throw exception(ADJUSTMENT_NOT_EXISTS);
        }

        Long schoolId = adjustment.getSchoolId();
        Long majorId = adjustment.getMajorId();
        Long collegeId = adjustment.getCollegeId();
        if (schoolId == null || majorId == null || collegeId == null) {
            throw exception(ADJUSTMENT_NOT_EXISTS);
        }

        UserSubscriptionDO existing = userSubscriptionMapper.selectByUserIdAndSchoolIdAndMajorId(userId, schoolId, majorId);
        if (existing == null) {
            LocalDateTime now = LocalDateTime.now();
            UserSubscriptionDO toCreate = new UserSubscriptionDO();
            toCreate.setUserId(userId);
            toCreate.setSchoolId(schoolId);
            toCreate.setCollegeId(collegeId);
            toCreate.setMajorId(majorId);
            // New subscription is treated as read at subscribe time
            toCreate.setLastReadTime(now);
            userSubscriptionMapper.insert(toCreate);
            // hot_score +5 for new subscription (only from subscription behavior)
            adjustmentMapper.incrHotScoreById(adjustmentId, HOT_SCORE_SUBSCRIBE_DELTA);
            return;
        }

        if (!Objects.equals(existing.getCollegeId(), collegeId)) {
            UserSubscriptionDO toUpdate = new UserSubscriptionDO();
            toUpdate.setId(existing.getId());
            toUpdate.setCollegeId(collegeId);
            userSubscriptionMapper.updateById(toUpdate);
        }
    }

    @Override
    public Boolean unsubscribe(Long userId, AppUserSubscriptionUnsubscribeReqVO reqVO) {
        int deleted = userSubscriptionMapper.deleteByUserIdAndSchoolIdAndMajorId(userId, reqVO.getSchoolId(), reqVO.getMajorId());
        return deleted > 0;
    }

    @Override
    public PageResult<AppUserSubscriptionPageRespVO> getMyPage(Long userId, AppUserSubscriptionPageReqVO reqVO) {
        vipBenefitService.checkEnabledOrThrow(userId, BENEFIT_KEY_USER_SUBSCRIPTION);
        LocalDateTime readAt = LocalDateTime.now();
        // Keep API response shape unchanged (PageResult), but query all data without pagination
        List<AppUserSubscriptionPageRespVO> schools = userSubscriptionMapper.selectMySchoolList(userId, reqVO);
        if (schools == null || schools.isEmpty()) {
            return new PageResult<>(List.of(), 0L);
        }

        List<Long> schoolIds = new ArrayList<>(schools.size());
        for (AppUserSubscriptionPageRespVO school : schools) {
            if (school != null && school.getSchoolId() != null) {
                schoolIds.add(school.getSchoolId());
            }
        }
        if (schoolIds.isEmpty()) {
            return new PageResult<>(schools, (long) schools.size());
        }

        List<AppUserSubscriptionPageMajorRespVO> majorList = userSubscriptionMapper.selectMyMajorList(userId, schoolIds, reqVO);
        Map<Long, List<AppUserSubscriptionPageMajorRespVO>> majorMap = new HashMap<>();
        for (AppUserSubscriptionPageMajorRespVO item : majorList) {
            if (item == null || item.getSchoolId() == null) {
                continue;
            }
            majorMap.computeIfAbsent(item.getSchoolId(), k -> new ArrayList<>()).add(item);
        }

        for (AppUserSubscriptionPageRespVO school : schools) {
            if (school == null || school.getSchoolId() == null) {
                continue;
            }
            List<AppUserSubscriptionPageMajorRespVO> majors = majorMap.get(school.getSchoolId());
            school.setMajors(majors != null ? majors : List.of());
        }
        // Entering "My Subscription" page marks all as read
        userSubscriptionMapper.updateLastReadTimeByUserId(userId, readAt);
        return new PageResult<>(schools, (long) schools.size());
    }

    @Override
    public AppUserSubscriptionUnreadRespVO getUnread(Long userId) {
        Integer has = userSubscriptionMapper.selectHasUnread(userId);
        AppUserSubscriptionUnreadRespVO respVO = new AppUserSubscriptionUnreadRespVO();
        respVO.setHasUnread(has != null && has > 0);
        return respVO;
    }

    @Resource
    private AdjustmentMapper adjustmentMapper;

}