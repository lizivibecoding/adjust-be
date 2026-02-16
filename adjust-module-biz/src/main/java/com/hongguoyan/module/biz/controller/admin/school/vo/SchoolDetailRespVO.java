package com.hongguoyan.module.biz.controller.admin.school.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 院校详情 Response VO")
@Data
public class SchoolDetailRespVO {

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    private Long id;

    @Schema(description = "学校Logo path/key（仅存 path）", example = "biz/school/logo/xxx.png")
    private String schoolLogo;

    @Schema(description = "学校 Logo URL", example = "https://cdn.xxx.com/path/to/logo.png")
    private String schoolLogoUrl;

    @Schema(description = "学校类别", example = "综合类")
    private String schoolType;

    @Schema(description = "是否科研院所", example = "false")
    private Boolean isAcademy;

    @Schema(description = "是否985", example = "true")
    private Boolean is985;

    @Schema(description = "是否211", example = "true")
    private Boolean is211;

    @Schema(description = "是否双一流", example = "true")
    private Boolean isSyl;

    @Schema(description = "是否普通高校", example = "true")
    private Boolean isOrdinary;

    @Schema(description = "是否自主划线", example = "false")
    private Boolean isZihuaxian;

    @Schema(description = "学校简介")
    private String intro;

    @Schema(description = "学校详细地址")
    private String schoolAddress;

    @Schema(description = "建校年份", example = "1898")
    private Integer createYear;

    @Schema(description = "官方网站数组 JSON 字符串", example = "[\"https://xxx.edu.cn\"]")
    private String schoolSite;

    @Schema(description = "联系电话数组 JSON 字符串", example = "[\"010-xxxxxx\"]")
    private String schoolPhone;

    @Schema(description = "电子邮箱数组 JSON 字符串", example = "[\"xx@xxx.edu.cn\"]")
    private String schoolEmail;
}

