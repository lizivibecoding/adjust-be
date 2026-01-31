package com.hongguoyan.module.biz.service.usercustomreport;

import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.module.biz.controller.app.usercustomreport.vo.AppUserCustomReportSaveReqVO;
import com.hongguoyan.module.biz.dal.dataobject.usercustomreport.UserCustomReportDO;
import com.hongguoyan.module.biz.dal.mysql.usercustomreport.UserCustomReportMapper;
import jakarta.annotation.Resource;
import org.springframework.dao.DuplicateKeyException;
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
    public Long createNewVersionByUserId(Long userId, AppUserCustomReportSaveReqVO reqVO) {
        // 尽量避免并发下 report_no 冲突：冲突则重试一次
        for (int i = 0; i < 2; i++) {
            int maxReportNo = userCustomReportMapper.selectMaxReportNoByUserId(userId);
            int nextReportNo = maxReportNo + 1;

            UserCustomReportDO report = BeanUtils.toBean(reqVO, UserCustomReportDO.class);
            report.setId(null);
            report.setUserId(userId);
            report.setReportNo(nextReportNo);

            try {
                userCustomReportMapper.insert(report);
                return report.getId();
            } catch (DuplicateKeyException ignore) {
                // retry
            }
        }
        // 最终还是冲突，交由上层处理（会返回 500）
        UserCustomReportDO report = BeanUtils.toBean(reqVO, UserCustomReportDO.class);
        report.setId(null);
        report.setUserId(userId);
        report.setReportNo(userCustomReportMapper.selectMaxReportNoByUserId(userId) + 1);
        userCustomReportMapper.insert(report);
        return report.getId();
    }

}

