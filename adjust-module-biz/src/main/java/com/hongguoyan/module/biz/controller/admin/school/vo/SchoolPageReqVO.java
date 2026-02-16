package com.hongguoyan.module.biz.controller.admin.school.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 院校分页 Request VO")
@Data
public class SchoolPageReqVO extends PageParam {

    @Schema(description = "学校名称（模糊）", example = "北京大学")
    private String schoolName;

}

