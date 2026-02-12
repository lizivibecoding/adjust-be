package com.hongguoyan.module.biz.service.usercustomreport;

import com.hongguoyan.module.biz.dal.dataobject.usercustomreport.UserCustomReportDO;

import java.util.List;

/**
 * 用户AI调剂定制报告 Service 接口
 *
 * @author hgy
 */
public interface UserCustomReportService {

    /**
     * 获取用户最新一份报告
     */
    UserCustomReportDO getLatestByUserId(Long userId);

    /**
     * 按 reportId 获取报告（仅允许获取自己的报告）
     */
    UserCustomReportDO getByUserIdAndId(Long userId, Long reportId);

    /**
     * 获取用户报告列表（按 reportNo 倒序）
     */
    List<UserCustomReportDO> listByUserId(Long userId);

    /**
     * 修改报告名称（仅允许修改自己的报告）
     */
    void updateReportName(Long userId, Long reportId, String reportName);

    /**
     * 更新报告的 PDF 链接
     */
    void updateReportPdfUrl(Long userId, Long reportId, String pdfUrl);

    /**
     * 创建新版本报告（按 userId 自动递增 reportNo）
     */
    Long createNewVersionByUserId(Long userId);

    /**
     * 更新报告生成状态
     *
     * @param reportId       报告ID
     * @param generateStatus 生成状态：0-生成中，1-已完成
     */
    void updateGenerateStatus(Long reportId, Integer generateStatus);

    /**
     * 删除报告
     */
    void deleteUserCustomReport(Long id);

    /**
     * 获得报告分页
     */
    com.hongguoyan.framework.common.pojo.PageResult<UserCustomReportDO> getUserCustomReportPage(com.hongguoyan.module.biz.controller.admin.recommend.report.vo.UserCustomReportPageReqVO pageReqVO);

}

