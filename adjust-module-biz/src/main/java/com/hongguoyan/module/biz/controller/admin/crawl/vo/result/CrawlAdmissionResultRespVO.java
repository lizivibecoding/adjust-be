package com.hongguoyan.module.biz.controller.admin.crawl.vo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 爬虫录取名单 Response VO")
@Data
public class CrawlAdmissionResultRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "任务ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    private Long taskId;

    @Schema(description = "学校名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "北京大学")
    private String schoolName;

    @Schema(description = "学院名称", example = "计算机学院")
    private String collegeName;

    @Schema(description = "专业名称", example = "计算机科学与技术")
    private String majorName;

    @Schema(description = "考生姓名", example = "李四")
    private String candidateName;

    @Schema(description = "考生编号", example = "100010002")
    private String candidateNo;

    @Schema(description = "初试总分", example = "390")
    private String initialScore;

    @Schema(description = "一志愿报考学校", example = "清华大学")
    private String firstChoiceSchool;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
