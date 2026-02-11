package com.hongguoyan.module.biz.dal.mysql.publisher;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.admin.publisher.vo.PublisherPageReqVO;
import com.hongguoyan.module.biz.dal.dataobject.publisher.PublisherDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 发布者资质 Mapper
 *
 * @author hgy
 */
@Mapper
public interface PublisherMapper extends BaseMapperX<PublisherDO> {

    default PageResult<PublisherDO> selectPage(PublisherPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PublisherDO>()
                .eqIfPresent(PublisherDO::getUserId, reqVO.getUserId())
                .likeIfPresent(PublisherDO::getRealName, reqVO.getRealName())
                .likeIfPresent(PublisherDO::getMobile, reqVO.getMobile())
                .eqIfPresent(PublisherDO::getIdentityType, reqVO.getIdentityType())
                .eqIfPresent(PublisherDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(PublisherDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(PublisherDO::getId));
    }
}