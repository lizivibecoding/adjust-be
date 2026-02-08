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
import org.apache.ibatis.annotations.Select;

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


    /**
     * 计算某学校、学院、专业（及方向）的初试平均分
     * @param schoolId 学校ID
     * @param collegeId 学院ID（可空）
     * @param majorCode 专业代码（可空）
     * @param year 年份（可空）
     * @return 平均初试分
     */
    @Select("<script>" +
        "SELECT AVG(first_score) FROM biz_adjustment_admit " +
        "WHERE deleted = 0 " +
        "AND school_id = #{schoolId} " +
        "<if test='collegeId != null'> AND college_id = #{collegeId} </if> " +
        "<if test='majorCode != null and majorCode != \"\"'> AND major_code = #{majorCode} </if> " +
        "<if test='year != null'> AND year = #{year} </if>" +
        "</script>")
    BigDecimal selectAvgFirstScore(@Param("schoolId") Long schoolId,
        @Param("collegeId") Long collegeId,
        @Param("majorCode") String majorCode,
        @Param("year") Integer year);

    Map<String, Object> selectAdmitStats(@Param("schoolId") Long schoolId,
                                         @Param("collegeId") Long collegeId,
                                         @Param("majorCode") String majorCode,
                                         @Param("year") Integer year);

    List<BigDecimal> selectAdmitScores(@Param("schoolId") Long schoolId,
                                       @Param("collegeId") Long collegeId,
                                       @Param("majorCode") String majorCode,
                                       @Param("year") Integer year);

}