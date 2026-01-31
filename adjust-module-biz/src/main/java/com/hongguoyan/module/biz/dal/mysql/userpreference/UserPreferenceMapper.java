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

    default UserPreferenceDO selectByUserIdAndPreferenceNo(Long userId, Integer preferenceNo) {
        return selectOne(new LambdaQueryWrapperX<UserPreferenceDO>()
                .eq(UserPreferenceDO::getUserId, userId)
                .eq(UserPreferenceDO::getPreferenceNo, preferenceNo));
    }

    default List<UserPreferenceDO> selectListByUserId(Long userId) {
        return selectList(new LambdaQueryWrapperX<UserPreferenceDO>()
                .eq(UserPreferenceDO::getUserId, userId)
                .orderByAsc(UserPreferenceDO::getPreferenceNo));
    }

}