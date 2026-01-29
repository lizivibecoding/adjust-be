package com.hongguoyan.module.biz.service.candidatecustomreports;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.candidatecustomreports.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.candidatecustomreports.CandidateCustomReportsDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;

/**
 * 考生AI调剂定制报告 Service 接口
 *
 * @author hgy
 */
public interface CandidateCustomReportsService {

    /**
     * 创建考生AI调剂定制报告
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCandidateCustomReports(@Valid AppCandidateCustomReportsSaveReqVO createReqVO);

    /**
     * 更新考生AI调剂定制报告
     *
     * @param updateReqVO 更新信息
     */
    void updateCandidateCustomReports(@Valid AppCandidateCustomReportsSaveReqVO updateReqVO);

    /**
     * 删除考生AI调剂定制报告
     *
     * @param id 编号
     */
    void deleteCandidateCustomReports(Long id);

    /**
    * 批量删除考生AI调剂定制报告
    *
    * @param ids 编号
    */
    void deleteCandidateCustomReportsListByIds(List<Long> ids);

    /**
     * 获得考生AI调剂定制报告
     *
     * @param id 编号
     * @return 考生AI调剂定制报告
     */
    CandidateCustomReportsDO getCandidateCustomReports(Long id);

    /**
     * 获得考生AI调剂定制报告分页
     *
     * @param pageReqVO 分页查询
     * @return 考生AI调剂定制报告分页
     */
    PageResult<CandidateCustomReportsDO> getCandidateCustomReportsPage(AppCandidateCustomReportsPageReqVO pageReqVO);

}