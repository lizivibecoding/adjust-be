package com.hongguoyan.module.biz.dal.mysql.schoolmajor;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.schoolmajor.SchoolMajorDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import com.hongguoyan.module.biz.controller.app.schoolmajor.vo.*;

/**
 * 院校专业 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface SchoolMajorMapper extends BaseMapperX<SchoolMajorDO> {

    /**
     * Increment hot_score by (schoolId, collegeId, majorId).
     *
     * MySQL: use COALESCE to handle legacy NULL values safely.
     */
    @Update("""
            UPDATE biz_school_major
            SET hot_score = COALESCE(hot_score, 0) + #{delta}
            WHERE school_id = #{schoolId}
              AND college_id = #{collegeId}
              AND major_id = #{majorId}
            """)
    int incrHotScoreByBizKey(@Param("schoolId") Long schoolId,
                             @Param("collegeId") Long collegeId,
                             @Param("majorId") Long majorId,
                             @Param("delta") long delta);

    /**
     * Increment view_count and hot_score by (schoolId, collegeId, majorId).
     */
    @Update("""
            UPDATE biz_school_major
            SET view_count = COALESCE(view_count, 0) + #{viewDelta},
                hot_score = COALESCE(hot_score, 0) + #{hotDelta}
            WHERE school_id = #{schoolId}
              AND college_id = #{collegeId}
              AND major_id = #{majorId}
            """)
    int incrViewAndHotByBizKey(@Param("schoolId") Long schoolId,
                               @Param("collegeId") Long collegeId,
                               @Param("majorId") Long majorId,
                               @Param("viewDelta") int viewDelta,
                               @Param("hotDelta") long hotDelta);

    default PageResult<SchoolMajorDO> selectPage(SchoolMajorPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SchoolMajorDO>()
                .eqIfPresent(SchoolMajorDO::getSchoolId, reqVO.getSchoolId())
                .eqIfPresent(SchoolMajorDO::getCollegeId, reqVO.getCollegeId())
                .eqIfPresent(SchoolMajorDO::getMajorId, reqVO.getMajorId())
                .eqIfPresent(SchoolMajorDO::getCode, reqVO.getCode())
                .likeIfPresent(SchoolMajorDO::getName, reqVO.getName())
                .eqIfPresent(SchoolMajorDO::getDegreeType, reqVO.getDegreeType())
                .betweenIfPresent(SchoolMajorDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(SchoolMajorDO::getViewCount, reqVO.getViewCount())
                .orderByDesc(SchoolMajorDO::getId));
    }

}