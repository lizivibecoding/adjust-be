package com.hongguoyan.module.biz.controller.app.school.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "学校树(按分区/省份)-响应")
@Data
public class AppSchoolTreeAreaRespVO {

    @Schema(description = "考研分区：A/B", requiredMode = Schema.RequiredMode.REQUIRED, example = "A")
    private String area;

    @Schema(description = "省份节点列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<ProvinceNode> provinces;

    @Schema(description = "省份节点")
    @Data
    public static class ProvinceNode {

        @Schema(description = "省份名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "北京")
        private String name;

        @Schema(description = "学校列表", requiredMode = Schema.RequiredMode.REQUIRED)
        private List<AppSchoolSimpleOptionRespVO> schools;
    }
}

