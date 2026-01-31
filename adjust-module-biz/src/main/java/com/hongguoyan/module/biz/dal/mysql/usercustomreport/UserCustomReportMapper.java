package com.hongguoyan.module.biz.dal.mysql.usercustomreport;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.app.usercustomreport.vo.AppUserCustomReportPageReqVO;
import com.hongguoyan.module.biz.dal.dataobject.usercustomreport.UserCustomReportDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户AI调剂定制报告 Mapper
 *
 * @author hgy
 */
@Mapper
public interface UserCustomReportMapper extends BaseMapperX<UserCustomReportDO> {

    default UserCustomReportDO selectLatestByUserId(Long userId) {
        return selectOne(new LambdaQueryWrapperX<UserCustomReportDO>()
                .eq(UserCustomReportDO::getUserId, userId)
                .orderByDesc(UserCustomReportDO::getId)
                .last("LIMIT 1"));
    }

    /**
     * @return max(report_no) for the user; 0 if none
     */
    default int selectMaxReportNoByUserId(Long userId) {
        UserCustomReportDO row = selectOne(new LambdaQueryWrapperX<UserCustomReportDO>()
                .select(UserCustomReportDO::getReportNo)
                .eq(UserCustomReportDO::getUserId, userId)
                .orderByDesc(UserCustomReportDO::getReportNo)
                .last("LIMIT 1"));
        return row == null || row.getReportNo() == null ? 0 : row.getReportNo();
    }

    default PageResult<UserCustomReportDO> selectPage(AppUserCustomReportPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UserCustomReportDO>()
                .eqIfPresent(UserCustomReportDO::getUserId, reqVO.getUserId())
                .eqIfPresent(UserCustomReportDO::getReportNo, reqVO.getReportNo())
                .eqIfPresent(UserCustomReportDO::getReportVersion, reqVO.getReportVersion())
                .eqIfPresent(UserCustomReportDO::getSourceProfileId, reqVO.getSourceProfileId())
                .eqIfPresent(UserCustomReportDO::getSourceIntentionId, reqVO.getSourceIntentionId())
                .eqIfPresent(UserCustomReportDO::getDimBackgroundScore, reqVO.getDimBackgroundScore())
                .eqIfPresent(UserCustomReportDO::getDimLocationScore, reqVO.getDimLocationScore())
                .eqIfPresent(UserCustomReportDO::getDimEnglishScore, reqVO.getDimEnglishScore())
                .eqIfPresent(UserCustomReportDO::getDimTypeScore, reqVO.getDimTypeScore())
                .eqIfPresent(UserCustomReportDO::getDimTotalScore, reqVO.getDimTotalScore())
                .eqIfPresent(UserCustomReportDO::getAnalysisBackground, reqVO.getAnalysisBackground())
                .eqIfPresent(UserCustomReportDO::getAnalysisLocation, reqVO.getAnalysisLocation())
                .eqIfPresent(UserCustomReportDO::getAnalysisEnglish, reqVO.getAnalysisEnglish())
                .eqIfPresent(UserCustomReportDO::getAnalysisType, reqVO.getAnalysisType())
                .eqIfPresent(UserCustomReportDO::getAnalysisTotal, reqVO.getAnalysisTotal())
                .betweenIfPresent(UserCustomReportDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(UserCustomReportDO::getId));
    }

}

