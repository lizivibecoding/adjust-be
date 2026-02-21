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
import org.apache.ibatis.annotations.Update;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.*;
import com.hongguoyan.module.biz.dal.mysql.adjustment.dto.BizMajorKeyDTO;
import com.hongguoyan.module.biz.dal.mysql.adjustment.dto.RecruitSnapshotRowDTO;
import com.hongguoyan.module.biz.controller.app.school.vo.AppSchoolAdjustmentPageReqVO;
import com.hongguoyan.module.biz.controller.app.school.vo.AppSchoolAdjustmentRespVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentPageReqVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentPageRespVO;

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
                .eqIfPresent(AdjustmentDO::getAdjustCount, reqVO.getAdjustCount())
                .eqIfPresent(AdjustmentDO::getAdjustLeft, reqVO.getAdjustLeft())
                .eqIfPresent(AdjustmentDO::getStudyYears, reqVO.getStudyYears())
                .eqIfPresent(AdjustmentDO::getTuitionFee, reqVO.getTuitionFee())
                .eqIfPresent(AdjustmentDO::getRetestRatio, reqVO.getRetestRatio())
                .eqIfPresent(AdjustmentDO::getRetestWeight, reqVO.getRetestWeight())
                .eqIfPresent(AdjustmentDO::getRetestBooks, reqVO.getRetestBooks())
                .eqIfPresent(AdjustmentDO::getRequireScore, reqVO.getRequireScore())
                .eqIfPresent(AdjustmentDO::getRequireMajor, reqVO.getRequireMajor())
                .eqIfPresent(AdjustmentDO::getRemark, reqVO.getRemark())
                .eqIfPresent(AdjustmentDO::getAdjustStatus, reqVO.getAdjustStatus())
                .eqIfPresent(AdjustmentDO::getSpecialPlan, reqVO.getSpecialPlan())
                .eqIfPresent(AdjustmentDO::getAdjustType, reqVO.getAdjustType())
                .eqIfPresent(AdjustmentDO::getStatus, reqVO.getStatus())
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

    default PageResult<AppAdjustmentSearchRespVO> selectHotRankingPage(AppAdjustmentHotRankingReqVO reqVO) {
        Page<AppAdjustmentSearchRespVO> page = MyBatisUtils.buildPage(reqVO);
        List<AppAdjustmentSearchRespVO> records = selectHotRankingPage(page, reqVO);
        page.setRecords(records);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    List<AppAdjustmentSearchRespVO> selectHotRankingPage(IPage<AppAdjustmentSearchRespVO> page,
                                                         @Param("reqVO") AppAdjustmentHotRankingReqVO reqVO);

    /**
     * Batch load recruit snapshot (current year: latest by publish_time, id).
     */
    List<RecruitSnapshotRowDTO> selectRecruitSnapshotLatest(@Param("keys") List<BizMajorKeyDTO> keys);

    /**
     * Batch load recruit snapshot (history years: earliest by publish_time, id).
     */
    List<RecruitSnapshotRowDTO> selectRecruitSnapshotEarliest(@Param("keys") List<BizMajorKeyDTO> keys);

    /**
     * Batch load majors that have history adjust (year < currentYear, adjust_count > 0).
     * Only (schoolId, collegeId, majorId) are meaningful in results.
     */
    List<BizMajorKeyDTO> selectMajorsHasHistoryAdjust(@Param("triplets") List<BizMajorKeyDTO> triplets,
                                                      @Param("currentYear") Integer currentYear);

    List<Integer> selectOptionYears(@Param("schoolId") Long schoolId,
                                    @Param("collegeId") Long collegeId,
                                    @Param("majorId") Long majorId,
                                    @Param("studyMode") Integer studyMode);

    List<AppAdjustmentCollegeOptionRespVO> selectOptionColleges(@Param("schoolId") Long schoolId,
                                                                @Param("majorId") Long majorId);

    List<Integer> selectOptionStudyModes(@Param("schoolId") Long schoolId,
                                        @Param("collegeId") Long collegeId,
                                        @Param("majorId") Long majorId);

    AppAdjustmentUpdateStatsRespVO selectUpdateStats(@Param("year") Integer year);

    default PageResult<AppSchoolAdjustmentRespVO> selectSchoolAdjustmentPage(AppSchoolAdjustmentPageReqVO reqVO) {
        Page<AppSchoolAdjustmentRespVO> page = MyBatisUtils.buildPage(reqVO);
        List<AppSchoolAdjustmentRespVO> records = selectSchoolAdjustmentPage(page, reqVO);
        page.setRecords(records);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    List<AppSchoolAdjustmentRespVO> selectSchoolAdjustmentPage(IPage<AppSchoolAdjustmentRespVO> page,
                                                               @Param("reqVO") AppSchoolAdjustmentPageReqVO reqVO);

    /**
     * Increment view_count and hot_score by adjustment id.
     */
    @Update("""
            UPDATE biz_adjustment
            SET view_count = COALESCE(view_count, 0) + #{viewDelta},
                hot_score = COALESCE(hot_score, 0) + #{hotDelta}
            WHERE id = #{id}
            """)
    int incrViewAndHotById(@Param("id") Long id,
                           @Param("viewDelta") int viewDelta,
                           @Param("hotDelta") long hotDelta);

    /**
     * Increment hot_score by adjustment id.
     */
    @Update("""
            UPDATE biz_adjustment
            SET hot_score = COALESCE(hot_score, 0) + #{delta}
            WHERE id = #{id}
            """)
    int incrHotScoreById(@Param("id") Long id,
                         @Param("delta") long delta);

    /**
     * Year list of adjustment data (distinct, desc).
     */
    List<Integer> selectYearList();

    // ==================== admin queries ====================

    default PageResult<AdjustmentPageRespVO> selectAdminAdjustmentPage(AdjustmentPageReqVO reqVO) {
        Page<AdjustmentPageRespVO> page = MyBatisUtils.buildPage(reqVO);
        List<AdjustmentPageRespVO> records = selectAdminAdjustmentPage(page, reqVO);
        page.setRecords(records);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    List<AdjustmentPageRespVO> selectAdminAdjustmentPage(IPage<AdjustmentPageRespVO> page,
                                                         @Param("reqVO") AdjustmentPageReqVO reqVO);

    List<Integer> selectAdminYearList();

}
