package com.hongguoyan.module.biz.dal.mysql.schoolrank;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.admin.schoolrank.vo.SchoolRankPageReqVO;
import com.hongguoyan.module.biz.dal.dataobject.schoolrank.SchoolRankDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 软科排名 Mapper
 *
 * @author hgy
 */
@Mapper
public interface SchoolRankMapper extends BaseMapperX<SchoolRankDO> {

    default PageResult<SchoolRankDO> selectPage(SchoolRankPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SchoolRankDO>()
                .eqIfPresent(SchoolRankDO::getYear, reqVO.getYear())
                .eqIfPresent(SchoolRankDO::getRanking, reqVO.getRanking())
                .likeIfPresent(SchoolRankDO::getSchoolName, reqVO.getSchoolName())
                .eqIfPresent(SchoolRankDO::getSchoolId, reqVO.getSchoolId())
                .eqIfPresent(SchoolRankDO::getScore, reqVO.getScore())
                .betweenIfPresent(SchoolRankDO::getCreateTime, reqVO.getCreateTime())
                .orderByAsc(SchoolRankDO::getId));
    }

    default SchoolRankDO selectLatestBySchoolId(Long schoolId) {
        if (schoolId == null) {
            return null;
        }
        return selectOne(new LambdaQueryWrapperX<SchoolRankDO>()
                .eq(SchoolRankDO::getSchoolId, schoolId)
                .orderByDesc(SchoolRankDO::getYear)
                .orderByAsc(SchoolRankDO::getRanking)
                .last("LIMIT 1"));
    }

    default SchoolRankDO selectLatestBySchoolName(String schoolName) {
        if (schoolName == null || schoolName.isBlank()) {
            return null;
        }
        return selectOne(new LambdaQueryWrapperX<SchoolRankDO>()
                .eq(SchoolRankDO::getSchoolName, schoolName)
                .orderByDesc(SchoolRankDO::getYear)
                .orderByAsc(SchoolRankDO::getRanking)
                .last("LIMIT 1"));
    }

    default SchoolRankDO selectByYearAndSchoolName(Integer year, String schoolName) {
        if (year == null || schoolName == null || schoolName.isBlank()) {
            return null;
        }
        return selectOne(new LambdaQueryWrapperX<SchoolRankDO>()
                .eq(SchoolRankDO::getYear, year)
                .eq(SchoolRankDO::getSchoolName, schoolName)
                .last("LIMIT 1"));
    }

    default List<SchoolRankDO> selectListBySchoolName(String schoolName) {
        LambdaQueryWrapperX<SchoolRankDO> wrapper = new LambdaQueryWrapperX<>();
        wrapper.select(SchoolRankDO::getId, SchoolRankDO::getSchoolName);
        wrapper.likeIfPresent(SchoolRankDO::getSchoolName, schoolName);
        wrapper.orderByAsc(SchoolRankDO::getId);
        return selectList(wrapper);
    }
}

