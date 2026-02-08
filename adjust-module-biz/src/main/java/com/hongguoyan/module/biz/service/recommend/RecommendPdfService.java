package com.hongguoyan.module.biz.service.recommend;

/**
 * 推荐报告 PDF 生成 Service 接口
 *
 * @author hgy
 */
public interface RecommendPdfService {

    /**
     * 生成调剂报告 PDF
     *
     * @param userId 用户ID
     * @param reportId 报告ID
     * @return PDF 文件字节数组
     */
    byte[] generateReportPdf(Long userId, Long reportId);

}
