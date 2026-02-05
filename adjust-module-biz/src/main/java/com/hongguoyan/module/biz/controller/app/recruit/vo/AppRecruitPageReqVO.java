package com.hongguoyan.module.biz.controller.app.recruit.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "招生-分页-请求")
@Data
public class AppRecruitPageReqVO extends PageParam {

    @Schema(description = "招生年份")
    private Integer year;

    @Schema(description = "学校ID", example = "5")
    private Long schoolId;

    @Schema(description = "学校名称", example = "赵六")
    private String schoolName;

    @Schema(description = "学院ID", example = "95")
    private Long collegeId;

    @Schema(description = "学院名称", example = "芋艿")
    private String collegeName;

    @Schema(description = "专业ID", example = "2810")
    private Long majorId;

    @Schema(description = "专业代码")
    private String majorCode;

    @Schema(description = "专业名称", example = "芋艿")
    private String majorName;

    @Schema(description = "学位类型（0-不区分 1-学硕 2-专硕）", example = "1")
    private Integer degreeType;

    @Schema(description = "方向代码(来自CSV方向列括号内)")
    private String directionCode;

    @Schema(description = "方向名称", example = "赵六")
    private String directionName;

    @Schema(description = "学习方式：全日制/非全日制")
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

    @Schema(description = "退役计划")
    private Boolean retiredPlan;

    @Schema(description = "少骨计划")
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

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "浏览次数")
    private Long viewCount;

}