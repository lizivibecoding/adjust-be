package com.hongguoyan.module.biz.service.candidatecustomreports;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.app.candidatecustomreports.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.candidatecustomreports.CandidateCustomReportsDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.candidatecustomreports.CandidateCustomReportsMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

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
    public Long createCandidateCustomReports(AppCandidateCustomReportsSaveReqVO createReqVO) {
        // 插入
        CandidateCustomReportsDO candidateCustomReports = BeanUtils.toBean(createReqVO, CandidateCustomReportsDO.class);
        candidateCustomReportsMapper.insert(candidateCustomReports);

        // 返回
        return candidateCustomReports.getId();
    }

    @Override
    public void updateCandidateCustomReports(AppCandidateCustomReportsSaveReqVO updateReqVO) {
        // 校验存在
        validateCandidateCustomReportsExists(updateReqVO.getId());
        // 更新
        CandidateCustomReportsDO updateObj = BeanUtils.toBean(updateReqVO, CandidateCustomReportsDO.class);
        candidateCustomReportsMapper.updateById(updateObj);
    }

    @Override
    public void deleteCandidateCustomReports(Long id) {
        // 校验存在
        validateCandidateCustomReportsExists(id);
        // 删除
        candidateCustomReportsMapper.deleteById(id);
    }

    @Override
        public void deleteCandidateCustomReportsListByIds(List<Long> ids) {
        // 删除
        candidateCustomReportsMapper.deleteByIds(ids);
        }


    private void validateCandidateCustomReportsExists(Long id) {
        if (candidateCustomReportsMapper.selectById(id) == null) {
            throw exception(CANDIDATE_CUSTOM_REPORTS_NOT_EXISTS);
        }
    }

    @Override
    public CandidateCustomReportsDO getCandidateCustomReports(Long id) {
        return candidateCustomReportsMapper.selectById(id);
    }

    @Override
    public PageResult<CandidateCustomReportsDO> getCandidateCustomReportsPage(AppCandidateCustomReportsPageReqVO pageReqVO) {
        return candidateCustomReportsMapper.selectPage(pageReqVO);
    }

}