package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "调剂-响应")
@Data
@ExcelIgnoreUnannotated
public class AppAdjustmentRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20106")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "调剂年份", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("调剂年份")
    private Integer year;

    @Schema(description = "来源类型(1=研招网, 2=院校官网, 3=人工/第三方)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("来源类型(1=研招网, 2=院校官网, 3=人工/第三方)")
    private Integer sourceType;

    @Schema(description = "来源URL/原文链接", example = "https://www.iocoder.cn")
    @ExcelProperty("来源URL/原文链接")
    private String sourceUrl;

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    @ExcelProperty("学校ID")
    private Long schoolId;

    @Schema(description = "学校名称", example = "李四")
    @ExcelProperty("学校名称")
    private String schoolName;

    @Schema(description = "学院ID", example = "95")
    @ExcelProperty("学院ID")
    private Long collegeId;

    @Schema(description = "学院名称", example = "赵六")
    @ExcelProperty("学院名称")
    private String collegeName;

    @Schema(description = "专业ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2810")
    @ExcelProperty("专业ID")
    private Long majorId;

    @Schema(description = "专业代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("专业代码")
    private String majorCode;

    @Schema(description = "专业名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("专业名称")
    private String majorName;

    @Schema(description = "学位类型（0-不区分 1-学硕 2-专硕）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("学位类型（0-不区分 1-学硕 2-专硕）")
    private Integer degreeType;

    @Schema(description = "方向代码")
    @ExcelProperty("方向代码")
    private String directionCode;

    @Schema(description = "方向名称", example = "王五")
    @ExcelProperty("方向名称")
    private String directionName;

    @Schema(description = "学习方式：全日制/非全日制")
    @ExcelProperty("学习方式：全日制/非全日制")
    private String studyMode;

    @Schema(description = "调剂缺额人数", example = "20026")
    @ExcelProperty("调剂缺额人数")
    private Integer adjustCount;

    @Schema(description = "原计划招生人数(参考)")
    @ExcelProperty("原计划招生人数(参考)")
    private Integer adjustLeft;

    @Schema(description = "学制(年)")
    @ExcelProperty("学制(年)")
    private BigDecimal studyYears;

    @Schema(description = "学费说明")
    @ExcelProperty("学费说明")
    private String tuitionFee;

    @Schema(description = "复试比例(如: 1:1.2)")
    @ExcelProperty("复试比例(如: 1:1.2)")
    private String retestRatio;

    @Schema(description = "成绩权重(如: 初试50%+复试50%)")
    @ExcelProperty("成绩权重(如: 初试50%+复试50%)")
    private String retestWeight;

    @Schema(description = "复试参考书目")
    @ExcelProperty("复试参考书目")
    private String retestBooks;

    @Schema(description = "分数要求描述(如: 过国家线, 单科>50)")
    @ExcelProperty("分数要求描述(如: 过国家线, 单科>50)")
    private String requireScore;

    @Schema(description = "允许调入的一志愿专业范围")
    @ExcelProperty("允许调入的一志愿专业范围")
    private String requireMajor;

    @Schema(description = "科目1代码")
    @ExcelProperty("科目1代码")
    private String subjectCode1;

    @Schema(description = "科目1名称")
    @ExcelProperty("科目1名称")
    private String subjectName1;

    @Schema(description = "科目1说明")
    @ExcelProperty("科目1说明")
    private String subjectNote1;

    @Schema(description = "科目2代码")
    @ExcelProperty("科目2代码")
    private String subjectCode2;

    @Schema(description = "科目2名称")
    @ExcelProperty("科目2名称")
    private String subjectName2;

    @Schema(description = "科目2说明")
    @ExcelProperty("科目2说明")
    private String subjectNote2;

    @Schema(description = "科目3代码")
    @ExcelProperty("科目3代码")
    private String subjectCode3;

    @Schema(description = "科目3名称")
    @ExcelProperty("科目3名称")
    private String subjectName3;

    @Schema(description = "科目3说明")
    @ExcelProperty("科目3说明")
    private String subjectNote3;

    @Schema(description = "科目4代码")
    @ExcelProperty("科目4代码")
    private String subjectCode4;

    @Schema(description = "科目4名称")
    @ExcelProperty("科目4名称")
    private String subjectName4;

    @Schema(description = "科目4说明")
    @ExcelProperty("科目4说明")
    private String subjectNote4;

    @Schema(description = "科目组合JSON")
    @ExcelProperty("科目组合JSON")
    private String subjectCombinations;

    @Schema(description = "发布人用户ID(0代表系统爬虫)", requiredMode = Schema.RequiredMode.REQUIRED, example = "30375")
    @ExcelProperty("发布人用户ID(0代表系统爬虫)")
    private Long userId;

    @Schema(description = "联系方式")
    @ExcelProperty("联系方式")
    private String contact;

    @Schema(description = "备注", example = "你猜")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "招生状态(1=正常招生,0=已经停招)", example = "1")
    @ExcelProperty("招生状态(1=正常招生,0=已经停招)")
    private Integer adjustStatus;

    @Schema(description = "是否专项计划(1=专项计划,0=否)", example = "0")
    @ExcelProperty("是否专项计划(1=专项计划,0=否)")
    private Integer specialPlan;

    @Schema(description = "调剂类型(1=校内调剂,2=校外调剂)", example = "2")
    @ExcelProperty("调剂类型(1=校内调剂,2=校外调剂)")
    private Integer adjustType;

    @Schema(description = "状态(1=开放, 0=关闭)", example = "1")
    @ExcelProperty("状态(1=开放, 0=关闭)")
    private Integer status;

    @Schema(description = "审核状态(0=待审, 1=通过, 2=拒绝)", example = "2")
    @ExcelProperty("审核状态(0=待审, 1=通过, 2=拒绝)")
    private Integer reviewStatus;

    @Schema(description = "审核人ID")
    @ExcelProperty("审核人ID")
    private Long reviewer;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "浏览次数", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("浏览次数")
    private Integer viewCount;

}