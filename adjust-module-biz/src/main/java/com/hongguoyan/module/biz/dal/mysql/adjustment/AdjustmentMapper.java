package com.hongguoyan.module.biz.dal.mysql.adjustment;

import java.util.*;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.framework.mybatis.core.util.MyBatisUtils;
import com.hongguoyan.module.biz.dal.dataobject.adjustment.AdjustmentDO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.*;

/**
 * 调剂 Mapper
 *
 * @author hgy
 */
@Mapper
public interface AdjustmentMapper extends BaseMapperX<AdjustmentDO> {

    default PageResult<AdjustmentDO> selectPage(AppAdjustmentPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AdjustmentDO>()
                .eqIfPresent(AdjustmentDO::getYear, reqVO.getYear())
                .eqIfPresent(AdjustmentDO::getSourceType, reqVO.getSourceType())
                .eqIfPresent(AdjustmentDO::getSourceUrl, reqVO.getSourceUrl())
                .eqIfPresent(AdjustmentDO::getSchoolId, reqVO.getSchoolId())
                .likeIfPresent(AdjustmentDO::getSchoolName, reqVO.getSchoolName())
                .eqIfPresent(AdjustmentDO::getCollegeId, reqVO.getCollegeId())
                .likeIfPresent(AdjustmentDO::getCollegeName, reqVO.getCollegeName())
                .eqIfPresent(AdjustmentDO::getMajorId, reqVO.getMajorId())
                .eqIfPresent(AdjustmentDO::getMajorCode, reqVO.getMajorCode())
                .likeIfPresent(AdjustmentDO::getMajorName, reqVO.getMajorName())
                .eqIfPresent(AdjustmentDO::getDegreeType, reqVO.getDegreeType())
                .eqIfPresent(AdjustmentDO::getDirectionCode, reqVO.getDirectionCode())
                .likeIfPresent(AdjustmentDO::getDirectionName, reqVO.getDirectionName())
                .eqIfPresent(AdjustmentDO::getStudyMode, reqVO.getStudyMode())
                .eqIfPresent(AdjustmentDO::getBalanceCount, reqVO.getBalanceCount())
                .eqIfPresent(AdjustmentDO::getBalanceLeft, reqVO.getBalanceLeft())
                .eqIfPresent(AdjustmentDO::getStudyYears, reqVO.getStudyYears())
                .eqIfPresent(AdjustmentDO::getTuitionFee, reqVO.getTuitionFee())
                .eqIfPresent(AdjustmentDO::getRetestRatio, reqVO.getRetestRatio())
                .eqIfPresent(AdjustmentDO::getRetestWeight, reqVO.getRetestWeight())
                .eqIfPresent(AdjustmentDO::getRetestBooks, reqVO.getRetestBooks())
                .eqIfPresent(AdjustmentDO::getRequireScore, reqVO.getRequireScore())
                .eqIfPresent(AdjustmentDO::getRequireMajor, reqVO.getRequireMajor())
                .eqIfPresent(AdjustmentDO::getSubjectCode1, reqVO.getSubjectCode1())
                .eqIfPresent(AdjustmentDO::getSubjectName1, reqVO.getSubjectName1())
                .eqIfPresent(AdjustmentDO::getSubjectNote1, reqVO.getSubjectNote1())
                .eqIfPresent(AdjustmentDO::getSubjectCode2, reqVO.getSubjectCode2())
                .eqIfPresent(AdjustmentDO::getSubjectName2, reqVO.getSubjectName2())
                .eqIfPresent(AdjustmentDO::getSubjectNote2, reqVO.getSubjectNote2())
                .eqIfPresent(AdjustmentDO::getSubjectCode3, reqVO.getSubjectCode3())
                .eqIfPresent(AdjustmentDO::getSubjectName3, reqVO.getSubjectName3())
                .eqIfPresent(AdjustmentDO::getSubjectNote3, reqVO.getSubjectNote3())
                .eqIfPresent(AdjustmentDO::getSubjectCode4, reqVO.getSubjectCode4())
                .eqIfPresent(AdjustmentDO::getSubjectName4, reqVO.getSubjectName4())
                .eqIfPresent(AdjustmentDO::getSubjectNote4, reqVO.getSubjectNote4())
                .eqIfPresent(AdjustmentDO::getSubjectCombinations, reqVO.getSubjectCombinations())
                .eqIfPresent(AdjustmentDO::getUserId, reqVO.getUserId())
                .eqIfPresent(AdjustmentDO::getContact, reqVO.getContact())
                .eqIfPresent(AdjustmentDO::getRemark, reqVO.getRemark())
                .eqIfPresent(AdjustmentDO::getAdjustStatus, reqVO.getAdjustStatus())
                .eqIfPresent(AdjustmentDO::getSpecialPlan, reqVO.getSpecialPlan())
                .eqIfPresent(AdjustmentDO::getAdjustType, reqVO.getAdjustType())
                .eqIfPresent(AdjustmentDO::getStatus, reqVO.getStatus())
                .eqIfPresent(AdjustmentDO::getReviewStatus, reqVO.getReviewStatus())
                .eqIfPresent(AdjustmentDO::getReviewer, reqVO.getReviewer())
                .betweenIfPresent(AdjustmentDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(AdjustmentDO::getViewCount, reqVO.getViewCount())
                .orderByDesc(AdjustmentDO::getId));
    }

    default PageResult<AppAdjustmentSearchRespVO> selectSearchMajorPage(AppAdjustmentSearchReqVO reqVO) {
        Page<AppAdjustmentSearchRespVO> page = MyBatisUtils.buildPage(reqVO);
        List<AppAdjustmentSearchRespVO> records = selectSearchMajorPage(page, reqVO);
        page.setRecords(records);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    List<AppAdjustmentSearchRespVO> selectSearchMajorPage(IPage<AppAdjustmentSearchRespVO> page,
                                                          @Param("reqVO") AppAdjustmentSearchReqVO reqVO);

    default PageResult<AppAdjustmentSearchSchoolRespVO> selectSearchSchoolPage(AppAdjustmentSearchReqVO reqVO) {
        Page<AppAdjustmentSearchSchoolRespVO> page = MyBatisUtils.buildPage(reqVO);
        List<AppAdjustmentSearchSchoolRespVO> records = selectSearchSchoolPage(page, reqVO);
        page.setRecords(records);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    List<AppAdjustmentSearchSchoolRespVO> selectSearchSchoolPage(IPage<AppAdjustmentSearchSchoolRespVO> page,
                                                                 @Param("reqVO") AppAdjustmentSearchReqVO reqVO);

    List<Integer> selectOptionYears(@Param("schoolId") Long schoolId,
                                    @Param("majorId") Long majorId);

    List<AppAdjustmentCollegeOptionRespVO> selectOptionColleges(@Param("schoolId") Long schoolId,
                                                                @Param("majorId") Long majorId);

}