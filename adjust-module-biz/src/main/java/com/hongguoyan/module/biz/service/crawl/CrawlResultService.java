package com.hongguoyan.module.biz.service.crawl;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.controller.admin.crawl.vo.result.CrawlAdjustmentResultPageReqVO;
import com.hongguoyan.module.biz.controller.admin.crawl.vo.result.CrawlAdmissionResultPageReqVO;
import com.hongguoyan.module.biz.controller.admin.crawl.vo.result.CrawlRetestResultPageReqVO;
import com.hongguoyan.module.biz.dal.dataobject.crawl.CrawlAdjustmentResultDO;
import com.hongguoyan.module.biz.dal.dataobject.crawl.CrawlAdmissionResultDO;
import com.hongguoyan.module.biz.dal.dataobject.crawl.CrawlRetestResultDO;

/**
 * 爬虫结果 Service 接口
 *
 * @author hgy
 */
public interface CrawlResultService {

    /**
     * 获得爬虫复试名单分页
     *
     * @param pageReqVO 分页查询
     * @return 爬虫复试名单分页
     */
    PageResult<CrawlRetestResultDO> getCrawlRetestResultPage(CrawlRetestResultPageReqVO pageReqVO);

    /**
     * 获得爬虫录取名单分页
     *
     * @param pageReqVO 分页查询
     * @return 爬虫录取名单分页
     */
    PageResult<CrawlAdmissionResultDO> getCrawlAdmissionResultPage(CrawlAdmissionResultPageReqVO pageReqVO);

    /**
     * 获得爬虫调剂专业分页
     *
     * @param pageReqVO 分页查询
     * @return 爬虫调剂专业分页
     */
    PageResult<CrawlAdjustmentResultDO> getCrawlAdjustmentResultPage(CrawlAdjustmentResultPageReqVO pageReqVO);

}
