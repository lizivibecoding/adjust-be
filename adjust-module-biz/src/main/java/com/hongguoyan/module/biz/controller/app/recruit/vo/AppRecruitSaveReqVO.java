package com.hongguoyan.module.biz.controller.app.recruit.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "用户 APP - 招生新增/修改 Request VO")
@Data
public class AppRecruitSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3690")
    private Long id;

    @Schema(description = "招生年份", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "招生年份不能为空")
    private Integer year;

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15423")
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @Schema(description = "学校名称", example = "赵六")
    private String schoolName;

    @Schema(description = "学院ID", example = "23464")
    private Long collegeId;

    @Schema(description = "学院名称", example = "芋艿")
    private String collegeName;

    @Schema(description = "专业ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11344")
    @NotNull(message = "专业ID不能为空")
    private Long majorId;

    @Schema(description = "专业代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "专业代码不能为空")
    private String majorCode;

    @Schema(description = "专业名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "专业名称不能为空")
    private String majorName;

    @Schema(description = "学位类型(0=未知/不区分,1=专硕,2=学硕)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "学位类型(0=未知/不区分,1=专硕,2=学硕)不能为空")
    private Integer degreeType;

    @Schema(description = "方向代码(来自CSV方向列括号内)")
    private String directionCode;

    @Schema(description = "方向名称", example = "赵六")
    private String directionName;

    @Schema(description = "学习方式(全日制/非全日制)")
    private String studyMode;

    @Schema(description = "考试方式")
    private String examMode;

    @Schema(description = "major=专业人数,direction=方向人数", example = "2")
    private String recruitType;

    @Schema(description = "拟招生人数")
    private Integer recruitNumber;

    @Schema(description = "人数描述", example = "随便")
    private String recruitDescription;

    @Schema(description = "指导老师", example = "张三")
    private String mentorName;

    @Schema(description = "退役计划", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "退役计划不能为空")
    private Boolean retiredPlan;

    @Schema(description = "少骨计划", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "少骨计划不能为空")
    private Boolean shaoGuPlan;

    @Schema(description = "科目1代码")
    private String subjectCode1;

    @Schema(description = "科目1名称")
    private String subjectName1;

    @Schema(description = "科目1说明/参考书/链接(剥离自<>，不含<见招生简章>)")
    private String subjectNote1;

    @Schema(description = "科目2代码")
    private String subjectCode2;

    @Schema(description = "科目2名称")
    private String subjectName2;

    @Schema(description = "科目2说明/参考书/链接")
    private String subjectNote2;

    @Schema(description = "科目3代码")
    private String subjectCode3;

    @Schema(description = "科目3名称")
    private String subjectName3;

    @Schema(description = "科目3说明/参考书/链接")
    private String subjectNote3;

    @Schema(description = "科目4代码")
    private String subjectCode4;

    @Schema(description = "科目4名称")
    private String subjectName4;

    @Schema(description = "科目4说明/参考书/链接")
    private String subjectNote4;

    @Schema(description = "科目组合JSON")
    private String subjectCombinations;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "点击次数", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "点击次数不能为空")
    private Long clicks;

}