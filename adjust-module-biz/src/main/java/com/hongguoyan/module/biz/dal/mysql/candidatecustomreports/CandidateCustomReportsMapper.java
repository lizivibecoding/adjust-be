package com.hongguoyan.module.biz.dal.mysql.candidatecustomreports;

import java.util.*;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.candidatecustomreports.CandidateCustomReportsDO;
import org.apache.ibatis.annotations.Mapper;
import com.hongguoyan.module.biz.controller.app.candidatecustomreports.vo.*;

/**
 * 考生AI调剂定制报告 Mapper
 *
 * @author hgy
 */
@Mapper
public interface CandidateCustomReportsMapper extends BaseMapperX<CandidateCustomReportsDO> {

    default PageResult<CandidateCustomReportsDO> selectPage(AppCandidateCustomReportsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CandidateCustomReportsDO>()
                .eqIfPresent(CandidateCustomReportsDO::getUserId, reqVO.getUserId())
                .eqIfPresent(CandidateCustomReportsDO::getReportNo, reqVO.getReportNo())
                .eqIfPresent(CandidateCustomReportsDO::getReportVersion, reqVO.getReportVersion())
                .eqIfPresent(CandidateCustomReportsDO::getSourceProfileId, reqVO.getSourceProfileId())
                .eqIfPresent(CandidateCustomReportsDO::getSourcePreferencesId, reqVO.getSourcePreferencesId())
                .eqIfPresent(CandidateCustomReportsDO::getDimBackgroundScore, reqVO.getDimBackgroundScore())
                .eqIfPresent(CandidateCustomReportsDO::getDimLocationScore, reqVO.getDimLocationScore())
                .eqIfPresent(CandidateCustomReportsDO::getDimEnglishScore, reqVO.getDimEnglishScore())
                .eqIfPresent(CandidateCustomReportsDO::getDimTypeScore, reqVO.getDimTypeScore())
                .eqIfPresent(CandidateCustomReportsDO::getDimTotalScore, reqVO.getDimTotalScore())
                .eqIfPresent(CandidateCustomReportsDO::getAnalysisBackground, reqVO.getAnalysisBackground())
                .eqIfPresent(CandidateCustomReportsDO::getAnalysisLocation, reqVO.getAnalysisLocation())
                .eqIfPresent(CandidateCustomReportsDO::getAnalysisEnglish, reqVO.getAnalysisEnglish())
                .eqIfPresent(CandidateCustomReportsDO::getAnalysisType, reqVO.getAnalysisType())
                .eqIfPresent(CandidateCustomReportsDO::getAnalysisTotal, reqVO.getAnalysisTotal())
                .betweenIfPresent(CandidateCustomReportsDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CandidateCustomReportsDO::getId));
    }

}