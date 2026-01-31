package com.hongguoyan.module.infra.controller.app.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "用户 App - 上传文件 Response VO（返回 url + path）")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppFileUpload2RespVO {

    @Schema(description = "文件访问地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://example.com/xxx.png")
    private String url;

    @Schema(description = "文件存储路径（key）", requiredMode = Schema.RequiredMode.REQUIRED, example = "xxx/20260131/xxx_1700000000000.png")
    private String path;

}

