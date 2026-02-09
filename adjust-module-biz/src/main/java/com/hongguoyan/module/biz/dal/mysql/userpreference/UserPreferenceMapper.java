package com.hongguoyan.module.biz.dal.mysql.userpreference;

import java.util.*;

import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.userpreference.UserPreferenceDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户志愿 Mapper
 *
 * @author hgy
 */
@Mapper
public interface UserPreferenceMapper extends BaseMapperX<UserPreferenceDO> {

    /**
     * Get the first row for (userId, preferenceNo) by stable ordering.
     * NOTE: the table supports multiple rows per preferenceNo after schema change.
     */
    default UserPreferenceDO selectByUserIdAndPreferenceNo(Long userId, Integer preferenceNo) {
        return selectOne(new LambdaQueryWrapperX<UserPreferenceDO>()
                .eq(UserPreferenceDO::getUserId, userId)
                .eq(UserPreferenceDO::getPreferenceNo, preferenceNo)
                .orderByAsc(UserPreferenceDO::getMajorCode)
                .orderByAsc(UserPreferenceDO::getDirectionCode)
                .orderByAsc(UserPreferenceDO::getStudyMode)
                .orderByAsc(UserPreferenceDO::getId)
                .last("LIMIT 1"));
    }

    default List<UserPreferenceDO> selectListByUserId(Long userId) {
        return selectList(new LambdaQueryWrapperX<UserPreferenceDO>()
                .eq(UserPreferenceDO::getUserId, userId)
                .orderByAsc(UserPreferenceDO::getPreferenceNo)
                .orderByAsc(UserPreferenceDO::getMajorCode)
                .orderByAsc(UserPreferenceDO::getDirectionCode)
                .orderByAsc(UserPreferenceDO::getStudyMode)
                .orderByAsc(UserPreferenceDO::getId));
    }

    default List<UserPreferenceDO> selectListByUserIdAndPreferenceNo(Long userId, Integer preferenceNo) {
        return selectList(new LambdaQueryWrapperX<UserPreferenceDO>()
                .eq(UserPreferenceDO::getUserId, userId)
                .eq(UserPreferenceDO::getPreferenceNo, preferenceNo)
                .orderByAsc(UserPreferenceDO::getMajorCode)
                .orderByAsc(UserPreferenceDO::getDirectionCode)
                .orderByAsc(UserPreferenceDO::getStudyMode)
                .orderByAsc(UserPreferenceDO::getId));
    }

    default UserPreferenceDO selectByUserIdPreferenceNoAndDirectionId(Long userId, Integer preferenceNo, Long directionId) {
        return selectOne(new LambdaQueryWrapperX<UserPreferenceDO>()
                .eq(UserPreferenceDO::getUserId, userId)
                .eq(UserPreferenceDO::getPreferenceNo, preferenceNo)
                .eq(UserPreferenceDO::getDirectionId, directionId));
    }

}