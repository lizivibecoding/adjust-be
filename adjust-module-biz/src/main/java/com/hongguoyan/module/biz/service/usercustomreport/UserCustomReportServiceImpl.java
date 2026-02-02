package com.hongguoyan.module.biz.service.usercustomreport;

import com.hongguoyan.module.biz.dal.dataobject.usercustomreport.UserCustomReportDO;
import com.hongguoyan.module.biz.dal.mysql.usercustomreport.UserCustomReportMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * 用户AI调剂定制报告 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class UserCustomReportServiceImpl implements UserCustomReportService {

    @Resource
    private UserCustomReportMapper userCustomReportMapper;

    @Override
    public UserCustomReportDO getLatestByUserId(Long userId) {
        return userCustomReportMapper.selectLatestByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createNewVersionByUserId(Long userId) {
        // 最终还是冲突，交由上层处理（会返回 500）
        UserCustomReportDO report = new UserCustomReportDO();
        report.setId(null);
        report.setUserId(userId);
        report.setReportNo(userCustomReportMapper.selectMaxReportNoByUserId(userId) + 1);
        userCustomReportMapper.insert(report);
        return report.getId();
    }

}

