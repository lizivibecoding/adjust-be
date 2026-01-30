package com.hongguoyan.module.biz.dal.mysql.publisher;

import java.util.*;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.publisher.PublisherDO;
import org.apache.ibatis.annotations.Mapper;
import com.hongguoyan.module.biz.controller.app.publisher.vo.*;

/**
 * 发布者资质 Mapper
 *
 * @author hgy
 */
@Mapper
public interface PublisherMapper extends BaseMapperX<PublisherDO> {

    default PageResult<PublisherDO> selectPage(AppPublisherPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PublisherDO>()
                .eqIfPresent(PublisherDO::getUserId, reqVO.getUserId())
                .eqIfPresent(PublisherDO::getIdentityType, reqVO.getIdentityType())
                .eqIfPresent(PublisherDO::getSchoolId, reqVO.getSchoolId())
                .likeIfPresent(PublisherDO::getSchoolName, reqVO.getSchoolName())
                .eqIfPresent(PublisherDO::getStatus, reqVO.getStatus())
                .likeIfPresent(PublisherDO::getRealName, reqVO.getRealName())
                .eqIfPresent(PublisherDO::getIdNo, reqVO.getIdNo())
                .likeIfPresent(PublisherDO::getOrgName, reqVO.getOrgName())
                .eqIfPresent(PublisherDO::getMobile, reqVO.getMobile())
                .eqIfPresent(PublisherDO::getFiles, reqVO.getFiles())
                .eqIfPresent(PublisherDO::getReviewerId, reqVO.getReviewerId())
                .betweenIfPresent(PublisherDO::getReviewTime, reqVO.getReviewTime())
                .eqIfPresent(PublisherDO::getRejectReason, reqVO.getRejectReason())
                .betweenIfPresent(PublisherDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(PublisherDO::getId));
    }

}