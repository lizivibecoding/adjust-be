package com.hongguoyan.module.biz.service.crawl;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.admin.crawl.vo.result.CrawlAdjustmentResultPageReqVO;
import com.hongguoyan.module.biz.controller.admin.crawl.vo.result.CrawlAdmissionResultPageReqVO;
import com.hongguoyan.module.biz.controller.admin.crawl.vo.result.CrawlRetestResultPageReqVO;
import com.hongguoyan.module.biz.dal.dataobject.crawl.CrawlAdjustmentResultDO;
import com.hongguoyan.module.biz.dal.dataobject.crawl.CrawlAdmissionResultDO;
import com.hongguoyan.module.biz.dal.dataobject.crawl.CrawlRetestResultDO;
import com.hongguoyan.module.biz.dal.mysql.crawl.CrawlAdjustmentResultMapper;
import com.hongguoyan.module.biz.dal.mysql.crawl.CrawlAdmissionResultMapper;
import com.hongguoyan.module.biz.dal.mysql.crawl.CrawlRetestResultMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 爬虫结果 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class CrawlResultServiceImpl implements CrawlResultService {

    @Resource
    private CrawlRetestResultMapper crawlRetestResultMapper;

    @Resource
    private CrawlAdmissionResultMapper crawlAdmissionResultMapper;

    @Resource
    private CrawlAdjustmentResultMapper crawlAdjustmentResultMapper;

    @Override
    public PageResult<CrawlRetestResultDO> getCrawlRetestResultPage(CrawlRetestResultPageReqVO pageReqVO) {
        return crawlRetestResultMapper.selectPage(pageReqVO, new LambdaQueryWrapperX<CrawlRetestResultDO>()
                .eqIfPresent(CrawlRetestResultDO::getTaskId, pageReqVO.getTaskId())
                .likeIfPresent(CrawlRetestResultDO::getSchoolName, pageReqVO.getSchoolName())
                .likeIfPresent(CrawlRetestResultDO::getCandidateName, pageReqVO.getCandidateName())
                .orderByDesc(CrawlRetestResultDO::getId));
    }

    @Override
    public PageResult<CrawlAdmissionResultDO> getCrawlAdmissionResultPage(CrawlAdmissionResultPageReqVO pageReqVO) {
        return crawlAdmissionResultMapper.selectPage(pageReqVO, new LambdaQueryWrapperX<CrawlAdmissionResultDO>()
                .eqIfPresent(CrawlAdmissionResultDO::getTaskId, pageReqVO.getTaskId())
                .likeIfPresent(CrawlAdmissionResultDO::getSchoolName, pageReqVO.getSchoolName())
                .likeIfPresent(CrawlAdmissionResultDO::getCandidateName, pageReqVO.getCandidateName())
                .orderByDesc(CrawlAdmissionResultDO::getId));
    }

    @Override
    public PageResult<CrawlAdjustmentResultDO> getCrawlAdjustmentResultPage(CrawlAdjustmentResultPageReqVO pageReqVO) {
        return crawlAdjustmentResultMapper.selectPage(pageReqVO, new LambdaQueryWrapperX<CrawlAdjustmentResultDO>()
                .eqIfPresent(CrawlAdjustmentResultDO::getTaskId, pageReqVO.getTaskId())
                .likeIfPresent(CrawlAdjustmentResultDO::getSchoolName, pageReqVO.getSchoolName())
                .likeIfPresent(CrawlAdjustmentResultDO::getMajorName, pageReqVO.getMajorName())
                .orderByDesc(CrawlAdjustmentResultDO::getId));
    }

}
