package com.hongguoyan.module.biz.service.userpreference;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.hongguoyan.module.biz.controller.app.userpreference.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.userpreference.UserPreferenceDO;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.dataobject.adjustment.AdjustmentDO;
import com.hongguoyan.module.biz.dal.mysql.adjustment.AdjustmentMapper;
import com.hongguoyan.module.biz.dal.mysql.userpreference.UserPreferenceMapper;
import com.hongguoyan.module.biz.dal.mysql.schooldirection.SchoolDirectionMapper;
import com.hongguoyan.module.biz.dal.mysql.school.SchoolMapper;
import com.hongguoyan.module.biz.dal.mysql.schoolcollege.SchoolCollegeMapper;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.service.vipbenefit.VipBenefitService;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.BENEFIT_KEY_USER_PREFERENCE;

/**
 * 用户志愿 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class UserPreferenceServiceImpl implements UserPreferenceService {

    @Resource
    private UserPreferenceMapper userPreferenceMapper;
    @Resource
    private AdjustmentMapper adjustmentMapper;
    @Resource
    private VipBenefitService vipBenefitService;

    @Override
    public List<AppUserPreferenceGroupRespVO> getMyList(Long userId) {
        vipBenefitService.checkEnabledOrThrow(userId, BENEFIT_KEY_USER_PREFERENCE);
        List<UserPreferenceDO> list = userPreferenceMapper.selectListByUserId(userId);
        Map<Integer, List<UserPreferenceDO>> map = new HashMap<>();
        for (UserPreferenceDO item : list) {
            if (item == null || item.getPreferenceNo() == null) {
                continue;
            }
            map.computeIfAbsent(item.getPreferenceNo(), k -> new ArrayList<>()).add(item);
        }

        List<AppUserPreferenceGroupRespVO> result = new ArrayList<>(3);
        for (int i = 1; i <= 3; i++) {
            AppUserPreferenceGroupRespVO group = new AppUserPreferenceGroupRespVO();
            group.setPreferenceNo(i);
            List<UserPreferenceDO> items = map.getOrDefault(i, Collections.emptyList());
            if (items.isEmpty()) {
                group.setItems(Collections.emptyList());
            } else {
                List<AppUserPreferenceItemRespVO> voList = new ArrayList<>(items.size());
                for (UserPreferenceDO item : items) {
                    if (item != null) {
                        voList.add(BeanUtils.toBean(item, AppUserPreferenceItemRespVO.class));
                    }
                }
                group.setItems(voList);
            }
            result.add(group);
        }
        return result;
    }

    @Override
    public void save(Long userId, AppUserPreferenceSaveReqVO reqVO) {
        vipBenefitService.checkEnabledOrThrow(userId, BENEFIT_KEY_USER_PREFERENCE);
        validatePreferenceNo(reqVO.getPreferenceNo());

        // directionId is deprecated: keep field in request but DO NOT use it for business logic.
        // (Old logic via school_direction/school/college/school_major is intentionally commented out.)
        // SchoolDirectionDO direction = schoolDirectionMapper.selectById(reqVO.getDirectionId());
        // ...

        Long adjustmentId = reqVO.getAdjustmentId();
        AdjustmentDO adjustment = adjustmentMapper.selectById(adjustmentId);
        if (adjustment == null) {
            throw exception(ADJUSTMENT_NOT_EXISTS);
        }
        if (adjustment.getDirectionId() == null) {
            throw exception(new com.hongguoyan.framework.common.exception.ErrorCode(400, "调剂缺少方向ID，无法加入志愿"));
        }

        Integer preferenceNo = reqVO.getPreferenceNo();

        // Prevent duplicates across 1/2/3 preferences: same directionId can only exist once per user
        UserPreferenceDO duplicate = userPreferenceMapper.selectFirstByUserIdAndDirectionId(userId, adjustment.getDirectionId());
        if (duplicate != null && duplicate.getPreferenceNo() != null && !duplicate.getPreferenceNo().equals(preferenceNo)) {
            throw exception(USER_PREFERENCE_DUPLICATE);
        }

        UserPreferenceDO existing = userPreferenceMapper.selectByUserIdPreferenceNoAndDirectionId(userId, preferenceNo, adjustment.getDirectionId());
        UserPreferenceDO toSave = existing != null ? existing : new UserPreferenceDO();
        toSave.setUserId(userId);
        toSave.setPreferenceNo(preferenceNo);
        toSave.setSourceAdjustmentId(adjustmentId);
        toSave.setSchoolId(adjustment.getSchoolId());
        toSave.setSchoolName(adjustment.getSchoolName());
        toSave.setCollegeId(adjustment.getCollegeId());
        toSave.setCollegeName(adjustment.getCollegeName());
        toSave.setMajorId(adjustment.getMajorId());
        toSave.setMajorCode(adjustment.getMajorCode());
        toSave.setMajorName(adjustment.getMajorName());
        toSave.setDirectionId(adjustment.getDirectionId());
        toSave.setDirectionCode(adjustment.getDirectionCode());
        toSave.setDirectionName(adjustment.getDirectionName());
        toSave.setStudyMode(adjustment.getStudyMode());

        if (existing == null) {
            userPreferenceMapper.insert(toSave);
        } else {
            userPreferenceMapper.updateById(toSave);
        }
    }

    @Override
    public void clear(Long userId, Integer preferenceNo) {
        vipBenefitService.checkEnabledOrThrow(userId, BENEFIT_KEY_USER_PREFERENCE);
        validatePreferenceNo(preferenceNo);
        userPreferenceMapper.delete(new LambdaQueryWrapperX<UserPreferenceDO>()
                .eq(UserPreferenceDO::getUserId, userId)
                .eq(UserPreferenceDO::getPreferenceNo, preferenceNo));
    }

    @Resource
    private SchoolDirectionMapper schoolDirectionMapper;

    @Resource
    private SchoolMapper schoolMapper;

    @Resource
    private SchoolCollegeMapper schoolCollegeMapper;

    private void validatePreferenceNo(Integer preferenceNo) {
        if (preferenceNo == null || preferenceNo < 1 || preferenceNo > 3) {
            throw exception(USER_PREFERENCE_NO_INVALID);
        }
    }

}