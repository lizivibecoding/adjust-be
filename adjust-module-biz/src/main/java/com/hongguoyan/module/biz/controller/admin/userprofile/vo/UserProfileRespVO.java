package com.hongguoyan.module.biz.controller.admin.userprofile.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 用户基础档案 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserProfileRespVO extends UserProfileSaveReqVO {

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    /**
     * 科目1代码
     */
    private String subjectCode1;
    /**
     * 科目1名称
     */
    private String subjectName1;

    /**
     * 科目2代码
     */
    private String subjectCode2;
    /**
     * 科目2名称
     */
    private String subjectName2;

    /**
     * 科目3代码
     */
    private String subjectCode3;
    /**
     * 科目3名称
     */
    private String subjectName3;

    /**
     * 科目4代码
     */
    private String subjectCode4;
    /**
     * 科目4名称
     */
    private String subjectName4;


}
