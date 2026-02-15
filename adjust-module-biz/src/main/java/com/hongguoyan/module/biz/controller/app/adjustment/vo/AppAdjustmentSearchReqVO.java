package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "调剂全局搜索-请求")
@Data
public class AppAdjustmentSearchReqVO extends PageParam {

    @Schema(description = "关键词(院校/专业/代码/科目)")
    private String keyword;

    @Schema(description = "搜索 Tab 类型(major/school)", requiredMode = Schema.RequiredMode.REQUIRED, example = "major")
    @NotBlank(message = "搜索 Tab 类型不能为空")
    private String tabType;

    @Schema(description = "学科/专业代码前缀", requiredMode = Schema.RequiredMode.REQUIRED, example = "02")
    @NotBlank(message = "学科/专业代码不能为空")
    private String majorCode;

    @Schema(hidden = true)
    private String resolvedMajorCodePrefix;

    @Schema(hidden = true)
    private String resolvedMajorCodeExact;

    @Schema(hidden = true)
    private List<String> resolvedLevel2MajorCodePrefixes;

    @Schema(hidden = true)
    private List<String> resolvedLevel2MajorCodeExacts;

    @Schema(description = "调剂年份")
    private Integer year;

    @Schema(description = "学习方式：1-全日制 2-非全日制", example = "1")
    private Integer studyMode;

    @Schema(description = "学位类型（0-不区分 1-学硕 2-专硕）")
    private Integer degreeType;

    @Schema(description = "省份代码(多选)")
    private List<String> provinceCodes;

    @Schema(description = "考研分区：A区/B区")
    private String provinceArea;

    @Schema(description = "目标专业二级 code(多选)")
    private List<String> level2MajorCodes;

    @Schema(description = "目标专业二级 ID（兼容旧客户端单选）", example = "249")
    private Long level2MajorId;

    @Schema(description = "是否985")
    private Boolean is985;

    @Schema(description = "是否211")
    private Boolean is211;

    @Schema(description = "是否双一流")
    private Boolean isSyl;

    @Schema(description = "院校标签(特性)")
    private String schoolFeature;

    @Schema(description = "发布时间区间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] publishTime;

    @Schema(description = "科目代码筛选(多选)")
    private List<String> subjectCodes;

    @Schema(description = "招生状态(1=正常招生,0=已经停招)")
    private Integer adjustStatus;

    @Schema(description = "是否专项计划(1=只看专项计划)")
    private Integer specialPlan;

    @Schema(description = "调剂类型(1=校内调剂,2=校外调剂)")
    private Integer adjustType;

}
