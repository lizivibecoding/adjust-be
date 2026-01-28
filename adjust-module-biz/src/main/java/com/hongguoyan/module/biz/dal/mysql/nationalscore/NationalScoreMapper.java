package com.hongguoyan.module.biz.dal.mysql.nationalscore;

import java.util.*;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.nationalscore.NationalScoreDO;
import org.apache.ibatis.annotations.Mapper;
import com.hongguoyan.module.biz.controller.admin.nationalscore.vo.*;

/**
 * 国家线 Mapper
 *
 * @author hgy
 */
@Mapper
public interface NationalScoreMapper extends BaseMapperX<NationalScoreDO> {

    default PageResult<NationalScoreDO> selectPage(NationalScorePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<NationalScoreDO>()
                .eqIfPresent(NationalScoreDO::getYear, reqVO.getYear())
                .eqIfPresent(NationalScoreDO::getDegreeType, reqVO.getDegreeType())
                .eqIfPresent(NationalScoreDO::getArea, reqVO.getArea())
                .eqIfPresent(NationalScoreDO::getScoreType, reqVO.getScoreType())
                .eqIfPresent(NationalScoreDO::getMajorId, reqVO.getMajorId())
                .eqIfPresent(NationalScoreDO::getMajorCode, reqVO.getMajorCode())
                .likeIfPresent(NationalScoreDO::getMajorName, reqVO.getMajorName())
                .eqIfPresent(NationalScoreDO::getTotal, reqVO.getTotal())
                .eqIfPresent(NationalScoreDO::getSingle100, reqVO.getSingle100())
                .eqIfPresent(NationalScoreDO::getSingle150, reqVO.getSingle150())
                .betweenIfPresent(NationalScoreDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(NationalScoreDO::getId));
    }

}