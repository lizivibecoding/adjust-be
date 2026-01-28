package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "用户 APP - 调剂详情(方向维度) Response VO")
@Data
public class AppAdjustmentDirectionDetailRespVO {

    @Schema(description = "调剂ID(方向记录ID)", requiredMode = Schema.RequiredMode.REQUIRED, example = "20106")
    private Long adjustmentId;

    @Schema(description = "方向代码", example = "00")
    private String directionCode;

    @Schema(description = "方向名称", example = "不区分研究方向")
    private String directionName;

    @Schema(description = "调剂缺额人数", example = "3")
    private Integer adjustCount;

    @Schema(description = "原计划招生人数(参考)")
    private Integer adjustLeft;

    @Schema(description = "学制(年)")
    private BigDecimal studyYears;

    @Schema(description = "学费说明")
    private String tuitionFee;

    @Schema(description = "复试比例(如: 1:1.2)")
    private String retestRatio;

    @Schema(description = "成绩权重(如: 初试50%+复试50%)")
    private String retestWeight;

    @Schema(description = "复试参考书目列表")
    private List<String> retestBooks;

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

    @Schema(description = "联系方式")
    private String contact;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "发布时间")
    private LocalDateTime publishTime;

    @Schema(description = "浏览次数")
    private Integer viewCount;
}

