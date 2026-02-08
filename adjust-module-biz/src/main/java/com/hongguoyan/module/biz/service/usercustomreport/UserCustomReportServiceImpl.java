package com.hongguoyan.module.biz.service.usercustomreport;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.admin.recommend.report.vo.UserCustomReportPageReqVO;
import com.hongguoyan.module.biz.dal.dataobject.usercustomreport.UserCustomReportDO;
import com.hongguoyan.module.biz.dal.mysql.usercustomreport.UserCustomReportMapper;
import com.hongguoyan.module.biz.enums.ErrorCodeConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;

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
        UserCustomReportDO report = userCustomReportMapper.selectLatestByUserId(userId);
        fillDefaultReportNameIfMissing(report);
        return report;
    }

    @Override
    public UserCustomReportDO getByUserIdAndId(Long userId, Long reportId) {
        UserCustomReportDO report = userCustomReportMapper.selectByUserIdAndId(userId, reportId);
        fillDefaultReportNameIfMissing(report);
        return report;
    }

    @Override
    public List<UserCustomReportDO> listByUserId(Long userId) {
        List<UserCustomReportDO> list = userCustomReportMapper.selectListByUserId(userId);
        if (list != null) {
            list.forEach(this::fillDefaultReportNameIfMissing);
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateReportName(Long userId, Long reportId, String reportName) {
        if (userId == null || reportId == null) {
            throw exception(ErrorCodeConstants.CANDIDATE_CUSTOM_REPORTS_NOT_EXISTS);
        }
        int updated = userCustomReportMapper.updateReportNameByUserIdAndId(userId, reportId, reportName);
        if (updated <= 0) {
            throw exception(ErrorCodeConstants.CANDIDATE_CUSTOM_REPORTS_NOT_EXISTS);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createNewVersionByUserId(Long userId) {
        // 最终还是冲突，交由上层处理（会返回 500）
        UserCustomReportDO report = new UserCustomReportDO();
        report.setId(null);
        report.setUserId(userId);
        int reportNo = userCustomReportMapper.selectMaxReportNoByUserId(userId) + 1;
        report.setReportNo(reportNo);
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE); // yyyyMMdd
        report.setReportName(String.format("%s - 调剂报告 - %02d", date, reportNo));
        userCustomReportMapper.insert(report);
        return report.getId();
    }

    @Override
    public void deleteUserCustomReport(Long id) {
        if (userCustomReportMapper.selectById(id) == null) {
            throw exception(ErrorCodeConstants.CANDIDATE_CUSTOM_REPORTS_NOT_EXISTS);
        }
        userCustomReportMapper.deleteById(id);
    }

    @Override
    public PageResult<UserCustomReportDO> getUserCustomReportPage(UserCustomReportPageReqVO pageReqVO) {
        PageResult<UserCustomReportDO> pageResult = userCustomReportMapper.selectPage(pageReqVO, new LambdaQueryWrapperX<UserCustomReportDO>()
                .eqIfPresent(UserCustomReportDO::getUserId, pageReqVO.getUserId())
                .likeIfPresent(UserCustomReportDO::getReportName, pageReqVO.getReportName())
                .orderByDesc(UserCustomReportDO::getId));
        if (pageResult != null && pageResult.getList() != null) {
            pageResult.getList().forEach(this::fillDefaultReportNameIfMissing);
        }
        return pageResult;
    }

    private void fillDefaultReportNameIfMissing(UserCustomReportDO report) {
        if (report == null) {
            return;
        }
        if (report.getReportName() != null && !report.getReportName().isBlank()) {
            return;
        }
        LocalDate date = report.getCreateTime() != null ? report.getCreateTime().toLocalDate() : LocalDate.now();
        String ds = date.format(DateTimeFormatter.BASIC_ISO_DATE);
        int no = report.getReportNo() != null ? report.getReportNo() : 0;
        report.setReportName(String.format("%s - 调剂报告 - %02d", ds, no));
    }

}
