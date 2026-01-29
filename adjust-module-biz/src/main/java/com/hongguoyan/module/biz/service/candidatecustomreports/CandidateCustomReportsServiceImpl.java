package com.hongguoyan.module.biz.service.candidatecustomreports;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import com.hongguoyan.module.biz.controller.app.candidatecustomreports.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.candidatecustomreports.CandidateCustomReportsDO;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.candidatecustomreports.CandidateCustomReportsMapper;
import org.springframework.dao.DuplicateKeyException;

/**
 * 考生AI调剂定制报告 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class CandidateCustomReportsServiceImpl implements CandidateCustomReportsService {

    @Resource
    private CandidateCustomReportsMapper candidateCustomReportsMapper;

    @Override
    public CandidateCustomReportsDO getLatestByUserId(Long userId) {
        return candidateCustomReportsMapper.selectLatestByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createNewVersionByUserId(Long userId, AppCandidateCustomReportsSaveReqVO reqVO) {
        // 尽量避免并发下 report_no 冲突：冲突则重试一次
        for (int i = 0; i < 2; i++) {
            int maxReportNo = candidateCustomReportsMapper.selectMaxReportNoByUserId(userId);
            int nextReportNo = maxReportNo + 1;

            CandidateCustomReportsDO report = BeanUtils.toBean(reqVO, CandidateCustomReportsDO.class);
            report.setId(null);
            report.setUserId(userId);
            report.setReportNo(nextReportNo);

            try {
                candidateCustomReportsMapper.insert(report);
                return report.getId();
            } catch (DuplicateKeyException ignore) {
                // retry
            }
        }
        // 最终还是冲突，交由上层处理（会返回 500）
        CandidateCustomReportsDO report = BeanUtils.toBean(reqVO, CandidateCustomReportsDO.class);
        report.setId(null);
        report.setUserId(userId);
        report.setReportNo(candidateCustomReportsMapper.selectMaxReportNoByUserId(userId) + 1);
        candidateCustomReportsMapper.insert(report);
        return report.getId();
    }

}