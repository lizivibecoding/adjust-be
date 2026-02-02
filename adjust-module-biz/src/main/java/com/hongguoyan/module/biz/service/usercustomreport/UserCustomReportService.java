package com.hongguoyan.module.biz.service.usercustomreport;

import com.hongguoyan.module.biz.dal.dataobject.usercustomreport.UserCustomReportDO;

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
     * 创建新版本报告（按 userId 自动递增 reportNo）
     */
    Long createNewVersionByUserId(Long userId);

}

