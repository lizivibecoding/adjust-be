package com.hongguoyan.module.infra.service.file.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件创建结果
 *
 * 用于在 Service 层返回文件上传后的核心信息（path/url）。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileCreateRespBO {

    /**
     * 文件上传路径（存储 key）
     */
    private String path;

    /**
     * 文件访问地址
     */
    private String url;

}

