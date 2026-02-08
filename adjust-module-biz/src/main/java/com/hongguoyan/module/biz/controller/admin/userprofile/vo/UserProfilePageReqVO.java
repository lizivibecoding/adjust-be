package com.hongguoyan.module.biz.controller.admin.userprofile.vo;

import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 用户基础档案分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserProfilePageReqVO extends PageParam {

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "本科毕业院校名称", example = "北京大学")
    private String graduateSchoolName;

    @Schema(description = "一志愿学校名称", example = "清华大学")
    private String targetSchoolName;

}
