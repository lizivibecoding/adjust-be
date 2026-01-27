package com.hongguoyan.module.biz.controller.app.recruit.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "用户 APP - 招生 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AppRecruitRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3690")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "招生年份", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("招生年份")
    private Integer year;

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15423")
    @ExcelProperty("学校ID")
    private Long schoolId;

    @Schema(description = "学校名称", example = "赵六")
    @ExcelProperty("学校名称")
    private String schoolName;

    @Schema(description = "学院ID", example = "23464")
    @ExcelProperty("学院ID")
    private Long collegeId;

    @Schema(description = "学院名称", example = "芋艿")
    @ExcelProperty("学院名称")
    private String collegeName;

    @Schema(description = "专业ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11344")
    @ExcelProperty("专业ID")
    private Long majorId;

    @Schema(description = "专业代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("专业代码")
    private String majorCode;

    @Schema(description = "专业名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("专业名称")
    private String majorName;

    @Schema(description = "学位类型(0=未知/不区分,1=专硕,2=学硕)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("学位类型(0=未知/不区分,1=专硕,2=学硕)")
    private Integer degreeType;

    @Schema(description = "方向代码(来自CSV方向列括号内)")
    @ExcelProperty("方向代码(来自CSV方向列括号内)")
    private String directionCode;

    @Schema(description = "方向名称", example = "赵六")
    @ExcelProperty("方向名称")
    private String directionName;

    @Schema(description = "学习方式(全日制/非全日制)")
    @ExcelProperty("学习方式(全日制/非全日制)")
    private String studyMode;

    @Schema(description = "考试方式")
    @ExcelProperty("考试方式")
    private String examMode;

    @Schema(description = "major=专业人数,direction=方向人数", example = "2")
    @ExcelProperty("major=专业人数,direction=方向人数")
    private String recruitType;

    @Schema(description = "拟招生人数")
    @ExcelProperty("拟招生人数")
    private Integer recruitNumber;

    @Schema(description = "人数描述", example = "随便")
    @ExcelProperty("人数描述")
    private String recruitDescription;

    @Schema(description = "指导老师", example = "张三")
    @ExcelProperty("指导老师")
    private String mentorName;

    @Schema(description = "退役计划", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("退役计划")
    private Boolean retiredPlan;

    @Schema(description = "少骨计划", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("少骨计划")
    private Boolean shaoGuPlan;

    @Schema(description = "科目1代码")
    @ExcelProperty("科目1代码")
    private String subjectCode1;

    @Schema(description = "科目1名称")
    @ExcelProperty("科目1名称")
    private String subjectName1;

    @Schema(description = "科目1说明/参考书/链接(剥离自<>，不含<见招生简章>)")
    @ExcelProperty("科目1说明/参考书/链接(剥离自<>，不含<见招生简章>)")
    private String subjectNote1;

    @Schema(description = "科目2代码")
    @ExcelProperty("科目2代码")
    private String subjectCode2;

    @Schema(description = "科目2名称")
    @ExcelProperty("科目2名称")
    private String subjectName2;

    @Schema(description = "科目2说明/参考书/链接")
    @ExcelProperty("科目2说明/参考书/链接")
    private String subjectNote2;

    @Schema(description = "科目3代码")
    @ExcelProperty("科目3代码")
    private String subjectCode3;

    @Schema(description = "科目3名称")
    @ExcelProperty("科目3名称")
    private String subjectName3;

    @Schema(description = "科目3说明/参考书/链接")
    @ExcelProperty("科目3说明/参考书/链接")
    private String subjectNote3;

    @Schema(description = "科目4代码")
    @ExcelProperty("科目4代码")
    private String subjectCode4;

    @Schema(description = "科目4名称")
    @ExcelProperty("科目4名称")
    private String subjectName4;

    @Schema(description = "科目4说明/参考书/链接")
    @ExcelProperty("科目4说明/参考书/链接")
    private String subjectNote4;

    @Schema(description = "科目组合JSON")
    @ExcelProperty("科目组合JSON")
    private String subjectCombinations;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "点击次数", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("点击次数")
    private Long clicks;

}