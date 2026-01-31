package com.hongguoyan.module.biz.service.userpreference;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.hongguoyan.module.biz.controller.app.userpreference.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.userpreference.UserPreferenceDO;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.userpreference.UserPreferenceMapper;
import com.hongguoyan.module.biz.dal.dataobject.schooldirection.SchoolDirectionDO;
import com.hongguoyan.module.biz.dal.dataobject.school.SchoolDO;
import com.hongguoyan.module.biz.dal.dataobject.schoolcollege.SchoolCollegeDO;
import com.hongguoyan.module.biz.dal.dataobject.schoolmajor.SchoolMajorDO;
import com.hongguoyan.module.biz.dal.mysql.schooldirection.SchoolDirectionMapper;
import com.hongguoyan.module.biz.dal.mysql.school.SchoolMapper;
import com.hongguoyan.module.biz.dal.mysql.schoolcollege.SchoolCollegeMapper;
import com.hongguoyan.module.biz.dal.mysql.schoolmajor.SchoolMajorMapper;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

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

    @Override
    public List<AppUserPreferenceRespVO> getMyList(Long userId) {
        List<UserPreferenceDO> list = userPreferenceMapper.selectListByUserId(userId);
        Map<Integer, UserPreferenceDO> map = new HashMap<>();
        for (UserPreferenceDO item : list) {
            map.put(item.getPreferenceNo(), item);
        }

        List<AppUserPreferenceRespVO> result = new ArrayList<>(3);
        for (int i = 1; i <= 3; i++) {
            UserPreferenceDO item = map.get(i);
            if (item == null) {
                AppUserPreferenceRespVO empty = new AppUserPreferenceRespVO();
                empty.setPreferenceNo(i);
                result.add(empty);
                continue;
            }
            result.add(BeanUtils.toBean(item, AppUserPreferenceRespVO.class));
        }
        return result;
    }

    @Override
    public void save(Long userId, AppUserPreferenceSaveReqVO reqVO) {
        validatePreferenceNo(reqVO.getPreferenceNo());

        SchoolDirectionDO direction = schoolDirectionMapper.selectById(reqVO.getDirectionId());
        if (direction == null) {
            throw exception(SCHOOL_DIRECTION_NOT_EXISTS);
        }

        SchoolDO school = schoolMapper.selectById(direction.getSchoolId());
        if (school == null) {
            throw exception(SCHOOL_NOT_EXISTS);
        }

        SchoolCollegeDO college = schoolCollegeMapper.selectById(direction.getCollegeId());
        if (college == null) {
            throw exception(SCHOOL_COLLEGE_NOT_EXISTS);
        }

        SchoolMajorDO major = schoolMajorMapper.selectOne(new LambdaQueryWrapperX<SchoolMajorDO>()
                .eq(SchoolMajorDO::getSchoolId, direction.getSchoolId())
                .eq(SchoolMajorDO::getCollegeId, direction.getCollegeId())
                .eq(SchoolMajorDO::getMajorId, direction.getMajorId()));
        if (major == null) {
            throw exception(SCHOOL_MAJOR_NOT_EXISTS);
        }

        UserPreferenceDO existing = userPreferenceMapper.selectByUserIdAndPreferenceNo(userId, reqVO.getPreferenceNo());
        UserPreferenceDO toSave = existing != null ? existing : new UserPreferenceDO();
        toSave.setUserId(userId);
        toSave.setPreferenceNo(reqVO.getPreferenceNo());
        toSave.setDirectionId(direction.getId());
        toSave.setDirectionCode(direction.getDirectionCode());
        toSave.setDirectionName(direction.getDirectionName());
        toSave.setSchoolId(school.getId());
        toSave.setSchoolName(school.getSchoolName());
        toSave.setCollegeId(college.getId());
        toSave.setCollegeName(college.getName());
        toSave.setMajorId(major.getMajorId());
        toSave.setMajorCode(major.getCode());
        toSave.setMajorName(major.getName());
        toSave.setStudyMode(convertStudyMode(direction.getStudyMode()));

        if (existing == null) {
            userPreferenceMapper.insert(toSave);
        } else {
            userPreferenceMapper.updateById(toSave);
        }
    }

    @Override
    public void clear(Long userId, Integer preferenceNo) {
        validatePreferenceNo(preferenceNo);

        UserPreferenceDO existing = userPreferenceMapper.selectByUserIdAndPreferenceNo(userId, preferenceNo);
        if (existing == null) {
            return;
        }

        UserPreferenceDO toUpdate = new UserPreferenceDO();
        toUpdate.setId(existing.getId());
        toUpdate.setSchoolId(null);
        toUpdate.setSchoolName("");
        toUpdate.setCollegeId(null);
        toUpdate.setCollegeName("");
        toUpdate.setMajorId(null);
        toUpdate.setMajorCode("");
        toUpdate.setMajorName("");
        toUpdate.setDirectionId(null);
        toUpdate.setDirectionCode("");
        toUpdate.setDirectionName("");
        toUpdate.setStudyMode(null);
        userPreferenceMapper.updateById(toUpdate);
    }

    @Resource
    private SchoolDirectionMapper schoolDirectionMapper;

    @Resource
    private SchoolMapper schoolMapper;

    @Resource
    private SchoolCollegeMapper schoolCollegeMapper;

    @Resource
    private SchoolMajorMapper schoolMajorMapper;

    private void validatePreferenceNo(Integer preferenceNo) {
        if (preferenceNo == null || preferenceNo < 1 || preferenceNo > 3) {
            throw exception(USER_PREFERENCE_NO_INVALID);
        }
    }

    private Integer convertStudyMode(String studyMode) {
        if (studyMode == null || studyMode.isEmpty()) {
            return null;
        }
        if ("全日制".equals(studyMode)) {
            return 1;
        }
        if ("非全日制".equals(studyMode)) {
            return 2;
        }
        return null;
    }

}