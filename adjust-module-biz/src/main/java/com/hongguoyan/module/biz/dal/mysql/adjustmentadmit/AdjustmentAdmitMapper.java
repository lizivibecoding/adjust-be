package com.hongguoyan.module.biz.dal.mysql.adjustmentadmit;

import java.util.*;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.framework.mybatis.core.util.MyBatisUtils;
import com.hongguoyan.module.biz.dal.dataobject.adjustmentadmit.AdjustmentAdmitDO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.hongguoyan.module.biz.controller.app.adjustmentadmit.vo.*;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.AppSameScoreItemRespVO;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.AppSameScorePageReqVO;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.AppSameScoreStatItemRespVO;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.AppSameScoreStatReqVO;

/**
 * 调剂录取名单 Mapper
 *
 * @author hgy
 */
@Mapper
public interface AdjustmentAdmitMapper extends BaseMapperX<AdjustmentAdmitDO> {

    default PageResult<AdjustmentAdmitDO> selectPage(AppAdjustmentAdmitPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AdjustmentAdmitDO>()
                .eqIfPresent(AdjustmentAdmitDO::getSchoolId, reqVO.getSchoolId())
                .likeIfPresent(AdjustmentAdmitDO::getSchoolName, reqVO.getSchoolName())
                .eqIfPresent(AdjustmentAdmitDO::getCollegeId, reqVO.getCollegeId())
                .likeIfPresent(AdjustmentAdmitDO::getCollegeName, reqVO.getCollegeName())
                .eqIfPresent(AdjustmentAdmitDO::getMajorId, reqVO.getMajorId())
                .likeIfPresent(AdjustmentAdmitDO::getMajorName, reqVO.getMajorName())
                .eqIfPresent(AdjustmentAdmitDO::getMajorCode, reqVO.getMajorCode())
                .eqIfPresent(AdjustmentAdmitDO::getDirectionId, reqVO.getDirectionId())
                .likeIfPresent(AdjustmentAdmitDO::getDirectionName, reqVO.getDirectionName())
                .eqIfPresent(AdjustmentAdmitDO::getYear, reqVO.getYear())
                .eqIfPresent(AdjustmentAdmitDO::getStudyMode, reqVO.getStudyMode())
                .likeIfPresent(AdjustmentAdmitDO::getCandidateName, reqVO.getCandidateName())
                .eqIfPresent(AdjustmentAdmitDO::getFirstSchoolId, reqVO.getFirstSchoolId())
                .likeIfPresent(AdjustmentAdmitDO::getFirstSchoolName, reqVO.getFirstSchoolName())
                .eqIfPresent(AdjustmentAdmitDO::getFirstScore, reqVO.getFirstScore())
                .eqIfPresent(AdjustmentAdmitDO::getRetestScore, reqVO.getRetestScore())
                .eqIfPresent(AdjustmentAdmitDO::getTotalScore, reqVO.getTotalScore())
                .betweenIfPresent(AdjustmentAdmitDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(AdjustmentAdmitDO::getId));
    }

    default PageResult<AppSameScoreItemRespVO> selectSameScorePage(AppSameScorePageReqVO reqVO) {
        Page<AppSameScoreItemRespVO> page = MyBatisUtils.buildPage(reqVO);
        List<AppSameScoreItemRespVO> records = selectSameScorePage(page, reqVO);
        page.setRecords(records);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    List<AppSameScoreItemRespVO> selectSameScorePage(IPage<AppSameScoreItemRespVO> page,
                                                     @Param("reqVO") AppSameScorePageReqVO reqVO);

    List<AppSameScoreStatItemRespVO> selectSameScoreStat(@Param("reqVO") AppSameScoreStatReqVO reqVO);

}