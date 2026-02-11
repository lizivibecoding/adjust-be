package com.hongguoyan.module.biz.dal.mysql.undergraduatemajor;

import java.util.*;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.undergraduatemajor.UndergraduateMajorDO;
import org.apache.ibatis.annotations.Mapper;
import com.hongguoyan.module.biz.controller.admin.undergraduatemajor.vo.*;

/**
 * 学科专业 Mapper
 *
 * @author hgy
 */
@Mapper
public interface UndergraduateMajorMapper extends BaseMapperX<UndergraduateMajorDO> {

    default PageResult<UndergraduateMajorDO> selectPage(UndergraduateMajorPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UndergraduateMajorDO>()
                .likeIfPresent(UndergraduateMajorDO::getCategoryName, reqVO.getCategoryName())
                .likeIfPresent(UndergraduateMajorDO::getTypeName, reqVO.getTypeName())
                .eqIfPresent(UndergraduateMajorDO::getCode, reqVO.getCode())
                .likeIfPresent(UndergraduateMajorDO::getName, reqVO.getName())
                .eqIfPresent(UndergraduateMajorDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(UndergraduateMajorDO::getCreateTime, reqVO.getCreateTime())
                .orderByAsc(UndergraduateMajorDO::getSort));
    }

}
