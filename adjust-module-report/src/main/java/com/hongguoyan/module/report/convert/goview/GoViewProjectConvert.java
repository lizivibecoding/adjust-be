package com.hongguoyan.module.report.convert.goview;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.report.controller.admin.goview.vo.project.GoViewProjectCreateReqVO;
import com.hongguoyan.module.report.controller.admin.goview.vo.project.GoViewProjectRespVO;
import com.hongguoyan.module.report.controller.admin.goview.vo.project.GoViewProjectUpdateReqVO;
import com.hongguoyan.module.report.dal.dataobject.goview.GoViewProjectDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GoViewProjectConvert {

    GoViewProjectConvert INSTANCE = Mappers.getMapper(GoViewProjectConvert.class);

    GoViewProjectDO convert(GoViewProjectCreateReqVO bean);

    GoViewProjectDO convert(GoViewProjectUpdateReqVO bean);

    GoViewProjectRespVO convert(GoViewProjectDO bean);

    PageResult<GoViewProjectRespVO> convertPage(PageResult<GoViewProjectDO> page);

}
