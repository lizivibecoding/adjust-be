package com.hongguoyan.module.biz.controller.app.userpreference.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 APP - 志愿导出 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AppUserPreferenceExportRespVO {

    @ExcelProperty("志愿序号")
    private Integer preferenceNo;

    @ExcelProperty("学校名称")
    private String schoolName;

    @ExcelProperty("学院名称")
    private String collegeName;

    @ExcelProperty("专业代码")
    private String majorCode;

    @ExcelProperty("专业名称")
    private String majorName;

    @ExcelProperty("方向名称")
    private String directionName;

    @ExcelProperty("学习方式(1全日制 2非全日制)")
    private Integer studyMode;
}

