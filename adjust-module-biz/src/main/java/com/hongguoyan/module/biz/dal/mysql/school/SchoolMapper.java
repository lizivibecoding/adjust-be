package com.hongguoyan.module.biz.dal.mysql.school;

import java.util.*;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.school.SchoolDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.hongguoyan.module.biz.controller.app.school.vo.*;

/**
 * 院校 Mapper
 *
 * @author hgy
 */
@Mapper
public interface SchoolMapper extends BaseMapperX<SchoolDO> {

    default PageResult<SchoolDO> selectPage(AppSchoolPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SchoolDO>()
                .eqIfPresent(SchoolDO::getSchoolCode, reqVO.getSchoolCode())
                .likeIfPresent(SchoolDO::getSchoolName, reqVO.getSchoolName())
                .eqIfPresent(SchoolDO::getSchoolLogo, reqVO.getSchoolLogo())
                .eqIfPresent(SchoolDO::getProvinceCode, reqVO.getProvinceCode())
                .likeIfPresent(SchoolDO::getProvinceName, reqVO.getProvinceName())
                .eqIfPresent(SchoolDO::getProvinceArea, reqVO.getProvinceArea())
                .eqIfPresent(SchoolDO::getSchoolType, reqVO.getSchoolType())
                .eqIfPresent(SchoolDO::getFeature, reqVO.getFeature())
                .eqIfPresent(SchoolDO::getIsAcademy, reqVO.getIsAcademy())
                .eqIfPresent(SchoolDO::getIs_985, reqVO.getIs_985())
                .eqIfPresent(SchoolDO::getIs_211, reqVO.getIs_211())
                .eqIfPresent(SchoolDO::getIsSyl, reqVO.getIsSyl())
                .eqIfPresent(SchoolDO::getIsKeySchool, reqVO.getIsKeySchool())
                .eqIfPresent(SchoolDO::getIsOrdinary, reqVO.getIsOrdinary())
                .eqIfPresent(SchoolDO::getIsZihuaxian, reqVO.getIsZihuaxian())
                .eqIfPresent(SchoolDO::getIsTuimian, reqVO.getIsTuimian())
                .eqIfPresent(SchoolDO::getIsApply, reqVO.getIsApply())
                .eqIfPresent(SchoolDO::getIntro, reqVO.getIntro())
                .eqIfPresent(SchoolDO::getSchoolAddress, reqVO.getSchoolAddress())
                .eqIfPresent(SchoolDO::getBelongsTo, reqVO.getBelongsTo())
                .eqIfPresent(SchoolDO::getCreateYear, reqVO.getCreateYear())
                .eqIfPresent(SchoolDO::getSchoolSpace, reqVO.getSchoolSpace())
                .eqIfPresent(SchoolDO::getSchoolSite, reqVO.getSchoolSite())
                .eqIfPresent(SchoolDO::getSchoolPhone, reqVO.getSchoolPhone())
                .eqIfPresent(SchoolDO::getSchoolEmail, reqVO.getSchoolEmail())
                .betweenIfPresent(SchoolDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(SchoolDO::getId));
    }

    List<String> selectSuggestSchoolNames(@Param("keyword") String keyword,
                                          @Param("limit") Integer limit);

}