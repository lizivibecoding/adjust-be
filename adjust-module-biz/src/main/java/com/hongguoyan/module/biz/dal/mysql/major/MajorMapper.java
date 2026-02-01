package com.hongguoyan.module.biz.dal.mysql.major;

import java.util.*;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.major.MajorDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.hongguoyan.module.biz.controller.app.major.vo.*;

/**
 * 专业 Mapper
 *
 * @author hgy
 */
@Mapper
public interface MajorMapper extends BaseMapperX<MajorDO> {

    default PageResult<MajorDO> selectPage(AppMajorPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MajorDO>()
                .eqIfPresent(MajorDO::getLevel, reqVO.getLevel())
                .eqIfPresent(MajorDO::getExtId, reqVO.getExtId())
                .eqIfPresent(MajorDO::getParentId, reqVO.getParentId())
                .eqIfPresent(MajorDO::getParentCode, reqVO.getParentCode())
                .eqIfPresent(MajorDO::getCode, reqVO.getCode())
                .likeIfPresent(MajorDO::getName, reqVO.getName())
                .eqIfPresent(MajorDO::getDegreeType, reqVO.getDegreeType())
                .betweenIfPresent(MajorDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MajorDO::getId));
    }

    List<AppMajorLevel1RespVO> selectLevel1List();

    List<String> selectSuggestMajorCodes(@Param("keyword") String keyword,
                                         @Param("limit") Integer limit);

    List<String> selectSuggestMajorNames(@Param("keyword") String keyword,
                                         @Param("limit") Integer limit);

    default List<MajorDO> selectListByLevelAndParentCode(@Param("parentCode") String parentCode,
                                                         @Param("level") Integer level,
                                                         @Param("degreeType") Integer degreeType) {
        // NOTE: do not chain eqIfPresent after select(), because select() returns MP wrapper type
        LambdaQueryWrapperX<MajorDO> qw = new LambdaQueryWrapperX<>();
        qw.select(MajorDO::getId, MajorDO::getCode, MajorDO::getName, MajorDO::getLevel, MajorDO::getDegreeType);
        qw.eqIfPresent(MajorDO::getParentCode, parentCode);
        qw.eq(MajorDO::getLevel, level);
        // degreeType: null means no filter; when specified, include "both/not applicable"(0) as well
        if (degreeType != null) {
            qw.and(w -> w.eq(MajorDO::getDegreeType, 0).or().eq(MajorDO::getDegreeType, degreeType));
        }
        qw.isNotNull(MajorDO::getCode);
        qw.ne(MajorDO::getCode, "");
        qw.eq(MajorDO::getDeleted, false);
        qw.orderByAsc(MajorDO::getCode);
        return selectList(qw);
    }

}
