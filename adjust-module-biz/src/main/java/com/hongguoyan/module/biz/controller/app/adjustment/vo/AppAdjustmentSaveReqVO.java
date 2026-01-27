package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "用户 APP - 调剂新增/修改 Request VO")
@Data
public class AppAdjustmentSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20106")
    private Long id;

    @Schema(description = "调剂年份", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "调剂年份不能为空")
    private Integer year;

    @Schema(description = "来源类型(1=研招网, 2=院校官网, 3=人工/第三方)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "来源类型(1=研招网, 2=院校官网, 3=人工/第三方)不能为空")
    private Integer sourceType;

    @Schema(description = "来源URL/原文链接", example = "https://www.iocoder.cn")
    private String sourceUrl;

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9101")
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @Schema(description = "学校名称", example = "李四")
    private String schoolName;

    @Schema(description = "学院ID", example = "6746")
    private Long collegeId;

    @Schema(description = "学院名称", example = "赵六")
    private String collegeName;

    @Schema(description = "专业ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15937")
    @NotNull(message = "专业ID不能为空")
    private Long majorId;

    @Schema(description = "专业代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "专业代码不能为空")
    private String majorCode;

    @Schema(description = "专业名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "专业名称不能为空")
    private String majorName;

    @Schema(description = "学位类型(0=未知/不区分,1=专硕,2=学硕)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "学位类型(0=未知/不区分,1=专硕,2=学硕)不能为空")
    private Integer degreeType;

    @Schema(description = "方向代码")
    private String directionCode;

    @Schema(description = "方向名称", example = "王五")
    private String directionName;

    @Schema(description = "学习方式(全日制/非全日制)")
    private String studyMode;

    @Schema(description = "调剂缺额人数", example = "20026")
    private Integer balanceCount;

    @Schema(description = "原计划招生人数(参考)")
    private Integer balanceLeft;

    @Schema(description = "学制(年)")
    private BigDecimal studyYears;

    @Schema(description = "学费说明")
    private String tuitionFee;

    @Schema(description = "复试比例(如: 1:1.2)")
    private String retestRatio;

    @Schema(description = "成绩权重(如: 初试50%+复试50%)")
    private String retestWeight;

    @Schema(description = "复试参考书目")
    private String retestBooks;

    @Schema(description = "分数要求描述(如: 过国家线, 单科>50)")
    private String requireScore;

    @Schema(description = "允许调入的一志愿专业范围")
    private String requireMajor;

    @Schema(description = "科目1代码")
    private String subjectCode1;

    @Schema(description = "科目1名称")
    private String subjectName1;

    @Schema(description = "科目1说明")
    private String subjectNote1;

    @Schema(description = "科目2代码")
    private String subjectCode2;

    @Schema(description = "科目2名称")
    private String subjectName2;

    @Schema(description = "科目2说明")
    private String subjectNote2;

    @Schema(description = "科目3代码")
    private String subjectCode3;

    @Schema(description = "科目3名称")
    private String subjectName3;

    @Schema(description = "科目3说明")
    private String subjectNote3;

    @Schema(description = "科目4代码")
    private String subjectCode4;

    @Schema(description = "科目4名称")
    private String subjectName4;

    @Schema(description = "科目4说明")
    private String subjectNote4;

    @Schema(description = "科目组合JSON")
    private String subjectCombinations;

    @Schema(description = "发布人用户ID(0代表系统爬虫)", requiredMode = Schema.RequiredMode.REQUIRED, example = "30375")
    @NotNull(message = "发布人用户ID(0代表系统爬虫)不能为空")
    private Long userId;

    @Schema(description = "联系方式")
    private String contact;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = "状态(1=开放, 0=关闭)", example = "1")
    private Integer status;

    @Schema(description = "审核状态(0=待审, 1=通过, 2=拒绝)", example = "2")
    private Integer reviewStatus;

    @Schema(description = "审核人ID")
    private Long reviewer;

    @Schema(description = "点击次数", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "点击次数不能为空")
    private Integer clicks;

}