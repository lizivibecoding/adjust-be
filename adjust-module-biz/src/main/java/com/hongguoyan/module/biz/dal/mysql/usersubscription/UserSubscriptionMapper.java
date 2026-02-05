package com.hongguoyan.module.biz.dal.mysql.usersubscription;

import java.util.*;
import java.time.LocalDateTime;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.util.MyBatisUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hongguoyan.module.biz.dal.dataobject.usersubscription.UserSubscriptionDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.hongguoyan.module.biz.controller.app.usersubscription.vo.*;

/**
 * 用户调剂订阅 Mapper
 *
 * @author hgy
 */
@Mapper
public interface UserSubscriptionMapper extends BaseMapperX<UserSubscriptionDO> {

    default UserSubscriptionDO selectByUserIdAndSchoolIdAndMajorId(Long userId, Long schoolId, Long majorId) {
        return selectOne(new LambdaQueryWrapperX<UserSubscriptionDO>()
                .eq(UserSubscriptionDO::getUserId, userId)
                .eq(UserSubscriptionDO::getSchoolId, schoolId)
                .eq(UserSubscriptionDO::getMajorId, majorId));
    }

    default int deleteByUserIdAndSchoolIdAndMajorId(Long userId, Long schoolId, Long majorId) {
        return delete(new LambdaQueryWrapperX<UserSubscriptionDO>()
                .eq(UserSubscriptionDO::getUserId, userId)
                .eq(UserSubscriptionDO::getSchoolId, schoolId)
                .eq(UserSubscriptionDO::getMajorId, majorId));
    }

    default PageResult<AppUserSubscriptionPageRespVO> selectMySchoolPage(Long userId, AppUserSubscriptionPageReqVO reqVO) {
        Page<AppUserSubscriptionPageRespVO> page = MyBatisUtils.buildPage(reqVO);
        List<AppUserSubscriptionPageRespVO> records = selectMySchoolPage(page, userId, reqVO);
        page.setRecords(records);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    List<AppUserSubscriptionPageRespVO> selectMySchoolPage(IPage<AppUserSubscriptionPageRespVO> page,
                                                           @Param("userId") Long userId,
                                                           @Param("reqVO") AppUserSubscriptionPageReqVO reqVO);

    /**
     * 我的订阅 - 学校列表（不分页）
     */
    List<AppUserSubscriptionPageRespVO> selectMySchoolList(@Param("userId") Long userId,
                                                           @Param("reqVO") AppUserSubscriptionPageReqVO reqVO);

    List<AppUserSubscriptionPageMajorRespVO> selectMyMajorList(@Param("userId") Long userId,
                                                               @Param("schoolIds") Collection<Long> schoolIds,
                                                               @Param("reqVO") AppUserSubscriptionPageReqVO reqVO);

    /**
     * 是否存在未读订阅更新（1=有，0=无）
     */
    Integer selectHasUnread(@Param("userId") Long userId);

    /**
     * 批量标记用户订阅为已读（进入我的订阅页即更新）
     */
    default int updateLastReadTimeByUserId(Long userId, LocalDateTime now) {
        if (userId == null || now == null) {
            return 0;
        }
        return update(null, new LambdaUpdateWrapper<UserSubscriptionDO>()
                .eq(UserSubscriptionDO::getUserId, userId)
                .set(UserSubscriptionDO::getLastReadTime, now));
    }

}