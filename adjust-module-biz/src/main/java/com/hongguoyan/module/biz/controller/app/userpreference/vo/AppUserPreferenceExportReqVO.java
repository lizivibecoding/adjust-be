package com.hongguoyan.module.biz.controller.app.userpreference.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Schema(description = "志愿导出-请求")
@Data
public class AppUserPreferenceExportReqVO {

    @Schema(description = "要导出的志愿序号（不传则导出全部已选）", example = "[1,2]")
    @Size(max = 3, message = "志愿序号最多 3 个")
    private List<@Min(value = 1, message = "志愿序号必须在 1~3 之间")
            @Max(value = 3, message = "志愿序号必须在 1~3 之间") Integer> preferenceNos;
}

