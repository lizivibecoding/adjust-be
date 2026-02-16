package com.hongguoyan.module.biz.controller.admin.user.vo;

import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.module.biz.dal.dataobject.userpreference.UserPreferenceDO;
import com.hongguoyan.module.biz.enums.StudyModeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 用户志愿条目 Response VO")
@Data
public class UserPreferenceItemRespVO {

    @Schema(description = "条目ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10001")
    private Long id;

    @Schema(description = "院校", example = "大连海洋大学")
    private String schoolName;

    @Schema(description = "学院", example = "信息工程学院")
    private String collegeName;

    @Schema(description = "专业代码", example = "085404")
    private String majorCode;

    @Schema(description = "专业", example = "计算机技术")
    private String majorName;

    @Schema(description = "方向编码", example = "01")
    private String directionCode;

    @Schema(description = "方向", example = "人工智能")
    private String directionName;

    @Schema(description = "学习方式：1-全日制 2-非全日制", example = "1")
    private Integer studyMode;

    @Schema(description = "学习方式名称", example = "全日制")
    private String studyModeName;

    @Schema(description = "添加时间")
    private LocalDateTime createTime;

    public static UserPreferenceItemRespVO from(UserPreferenceDO bean) {
        if (bean == null) {
            return null;
        }
        UserPreferenceItemRespVO vo = BeanUtils.toBean(bean, UserPreferenceItemRespVO.class);
        vo.setStudyModeName(StudyModeEnum.getName(bean.getStudyMode()));
        vo.setCreateTime(bean.getCreateTime());
        return vo;
    }
}

