package com.hongguoyan.module.biz.dal.mysql.useradjustment;

import java.util.*;

import cn.hutool.core.util.StrUtil;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.useradjustment.UserAdjustmentDO;
import org.apache.ibatis.annotations.Mapper;
import com.hongguoyan.module.biz.controller.app.useradjustment.vo.*;

/**
 * 用户发布调剂 Mapper
 *
 * @author hgy
 */
@Mapper
public interface UserAdjustmentMapper extends BaseMapperX<UserAdjustmentDO> {

    default PageResult<UserAdjustmentDO> selectPage(AppUserAdjustmentPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UserAdjustmentDO>()
                .eqIfPresent(UserAdjustmentDO::getUserId, reqVO.getUserId())
                .eqIfPresent(UserAdjustmentDO::getTitle, reqVO.getTitle())
                .eqIfPresent(UserAdjustmentDO::getYear, reqVO.getYear())
                .eqIfPresent(UserAdjustmentDO::getSchoolId, reqVO.getSchoolId())
                .likeIfPresent(UserAdjustmentDO::getSchoolName, reqVO.getSchoolName())
                .eqIfPresent(UserAdjustmentDO::getCollegeId, reqVO.getCollegeId())
                .likeIfPresent(UserAdjustmentDO::getCollegeName, reqVO.getCollegeName())
                .eqIfPresent(UserAdjustmentDO::getMajorId, reqVO.getMajorId())
                .eqIfPresent(UserAdjustmentDO::getMajorCode, reqVO.getMajorCode())
                .likeIfPresent(UserAdjustmentDO::getMajorName, reqVO.getMajorName())
                .eqIfPresent(UserAdjustmentDO::getDegreeType, reqVO.getDegreeType())
                .eqIfPresent(UserAdjustmentDO::getDirectionId, reqVO.getDirectionId())
                .eqIfPresent(UserAdjustmentDO::getDirectionCode, reqVO.getDirectionCode())
                .likeIfPresent(UserAdjustmentDO::getDirectionName, reqVO.getDirectionName())
                .eqIfPresent(UserAdjustmentDO::getStudyMode, reqVO.getStudyMode())
                .eqIfPresent(UserAdjustmentDO::getAdjustCount, reqVO.getAdjustCount())
                .eqIfPresent(UserAdjustmentDO::getAdjustLeft, reqVO.getAdjustLeft())
                .eqIfPresent(UserAdjustmentDO::getContact, reqVO.getContact())
                .eqIfPresent(UserAdjustmentDO::getRemark, reqVO.getRemark())
                .eqIfPresent(UserAdjustmentDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(UserAdjustmentDO::getPublishTime, reqVO.getPublishTime())
                .eqIfPresent(UserAdjustmentDO::getViewCount, reqVO.getViewCount())
                .betweenIfPresent(UserAdjustmentDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(UserAdjustmentDO::getId));
    }

    /**
     * 公开分页：只支持搜索框 keyword + 默认只看开放(status=1)
     */
    default PageResult<UserAdjustmentDO> selectPublicPage(AppUserAdjustmentPublicPageReqVO reqVO) {
        LambdaQueryWrapperX<UserAdjustmentDO> query = new LambdaQueryWrapperX<UserAdjustmentDO>()
                .eq(UserAdjustmentDO::getStatus, 1);
        String keyword = reqVO != null ? reqVO.getKeyword() : null;
        if (StrUtil.isNotBlank(keyword)) {
            String kw = keyword.trim();
            query.and(w -> w.like(UserAdjustmentDO::getSchoolName, kw)
                    .or().like(UserAdjustmentDO::getCollegeName, kw)
                    .or().like(UserAdjustmentDO::getMajorName, kw)
                    .or().like(UserAdjustmentDO::getMajorCode, kw)
                    .or().like(UserAdjustmentDO::getDirectionName, kw)
                    .or().like(UserAdjustmentDO::getTitle, kw));
        }
        query.orderByDesc(UserAdjustmentDO::getId);
        return selectPage(reqVO, query);
    }

    /**
     * 我的发布：仅分页，不提供筛选
     */
    default PageResult<UserAdjustmentDO> selectMyPage(Long userId, AppUserAdjustmentMyPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UserAdjustmentDO>()
                .eq(UserAdjustmentDO::getUserId, userId)
                .orderByDesc(UserAdjustmentDO::getId));
    }

}