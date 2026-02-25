package com.hongguoyan.module.biz.dal.mysql.adjustmentadmit;

import java.math.BigDecimal;
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
import com.hongguoyan.module.biz.dal.mysql.adjustmentadmit.dto.SameScoreLevelStatDTO;
import org.apache.ibatis.annotations.Select;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentAdmitPageReqVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentAdmitPageRespVO;

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
        return selectSameScorePage(reqVO, null, null);
    }

    default PageResult<AppSameScoreItemRespVO> selectSameScorePage(AppSameScorePageReqVO reqVO, String targetMajorCode) {
        return selectSameScorePage(reqVO, targetMajorCode, null);
    }

    default PageResult<AppSameScoreItemRespVO> selectSameScorePage(AppSameScorePageReqVO reqVO, String targetMajorCode,
                                                                   List<String> openedMajorCodes) {
        Page<AppSameScoreItemRespVO> page = MyBatisUtils.buildPage(reqVO);
        // Do NOT use MP auto count (it wraps full SQL with ORDER BY into subquery), use custom count SQL instead.
        page.setSearchCount(false);
        List<AppSameScoreItemRespVO> records = selectSameScorePage(page, reqVO, targetMajorCode, openedMajorCodes);
        Long total = selectSameScoreCount(reqVO, openedMajorCodes);
        return new PageResult<>(records != null ? records : List.of(), total != null ? total : 0L);
    }

    List<AppSameScoreItemRespVO> selectSameScorePage(IPage<AppSameScoreItemRespVO> page,
                                                     @Param("reqVO") AppSameScorePageReqVO reqVO,
                                                     @Param("targetMajorCode") String targetMajorCode,
                                                     @Param("openedMajorCodes") List<String> openedMajorCodes);

    Long selectSameScoreCount(@Param("reqVO") AppSameScorePageReqVO reqVO,
                              @Param("openedMajorCodes") List<String> openedMajorCodes);

    List<AppSameScoreStatItemRespVO> selectSameScoreStat(@Param("reqVO") AppSameScoreStatReqVO reqVO,
                                                         @Param("openedMajorCodes") List<String> openedMajorCodes);

    SameScoreLevelStatDTO selectSameScoreStatAgg(@Param("reqVO") AppSameScoreStatReqVO reqVO,
                                                 @Param("openedMajorCodes") List<String> openedMajorCodes);


    /**
     * 批量查询（school_id, college_id, major_code, year）维度的初试平均分，
     * 用于替代循环内逐条调用 selectAvgFirstScore。
     */
    @Select("<script>" +
        "SELECT school_id, college_id, major_code, study_mode, year, AVG(first_score) as avg_score " +
        "FROM biz_adjustment_admit " +
        "WHERE school_id IN <foreach item='id' collection='schoolIds' open='(' separator=',' close=')'> #{id} </foreach> " +
        "AND year = #{year} " +
        "GROUP BY school_id, college_id, major_code, study_mode, year" +
        "</script>")
    List<Map<String, Object>> selectBatchAvgFirstScore(@Param("schoolIds") Collection<Long> schoolIds,
                                                       @Param("year") Integer year);

    Map<String, Object> selectAdmitStats(@Param("schoolId") Long schoolId,
                                         @Param("collegeId") Long collegeId,
                                         @Param("majorCode") String majorCode,
                                         @Param("year") Integer year);

    /**
     * 批量查询录取分数列表 (用于内存计算中位数)
     * 返回: school_id, college_id, major_code, study_mode, year, first_score
     */
    @Select("<script>" +
        "SELECT a.school_id, a.college_id, a.major_code, a.study_mode, a.year, a.first_score, s.province_area as first_choice_area " +
        "FROM biz_adjustment_admit a " +
        "LEFT JOIN biz_school s ON s.id = a.first_school_id " +
        "WHERE a.school_id IN <foreach item='id' collection='schoolIds' open='(' separator=',' close=')'> #{id} </foreach> " +
        "AND a.year = #{year} " +
        "ORDER BY a.first_score ASC" +
        "</script>")
    List<Map<String, Object>> selectBatchAdmitScores(@Param("schoolIds") Collection<Long> schoolIds,
                                                     @Param("year") Integer year);

    List<BigDecimal> selectAdmitScores(@Param("schoolId") Long schoolId,
                                       @Param("collegeId") Long collegeId,
                                       @Param("majorCode") String majorCode,
                                       @Param("year") Integer year);

    // ==================== admin queries ====================

    default PageResult<AdjustmentAdmitPageRespVO> selectAdminAdmitPage(AdjustmentAdmitPageReqVO reqVO) {
        Page<AdjustmentAdmitPageRespVO> page = MyBatisUtils.buildPage(reqVO);
        List<AdjustmentAdmitPageRespVO> records = selectAdminAdmitPage(page, reqVO);
        page.setRecords(records);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    List<AdjustmentAdmitPageRespVO> selectAdminAdmitPage(IPage<AdjustmentAdmitPageRespVO> page,
                                                         @Param("reqVO") AdjustmentAdmitPageReqVO reqVO);

}