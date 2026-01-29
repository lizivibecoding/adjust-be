package com.hongguoyan.module.biz.service.candidatecustomreports;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.candidatecustomreports.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.candidatecustomreports.CandidateCustomReportsDO;

/**
 * 考生AI调剂定制报告 Service 接口
 *
 * @author hgy
 */
public interface CandidateCustomReportsService {

    /**
     * 获取用户最新一份报告
     */
    CandidateCustomReportsDO getLatestByUserId(Long userId);

    /**
     * 创建新版本报告（按 userId 自动递增 reportNo）
     */
    Long createNewVersionByUserId(Long userId, @Valid AppCandidateCustomReportsSaveReqVO reqVO);

}