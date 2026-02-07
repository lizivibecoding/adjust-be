package com.hongguoyan.module.biz.service.crawl;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.controller.admin.crawl.vo.config.CrawlSourceConfigPageReqVO;
import com.hongguoyan.module.biz.controller.admin.crawl.vo.config.CrawlSourceConfigSaveReqVO;
import com.hongguoyan.module.biz.dal.dataobject.crawl.CrawlSourceConfigDO;

import jakarta.validation.Valid;

/**
 * 爬虫配置 Service 接口
 *
 * @author hgy
 */
public interface CrawlSourceConfigService {

    /**
     * 创建爬虫配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCrawlSourceConfig(@Valid CrawlSourceConfigSaveReqVO createReqVO);

    /**
     * 更新爬虫配置
     *
     * @param updateReqVO 更新信息
     */
    void updateCrawlSourceConfig(@Valid CrawlSourceConfigSaveReqVO updateReqVO);

    /**
     * 删除爬虫配置
     *
     * @param id 编号
     */
    void deleteCrawlSourceConfig(Long id);

    /**
     * 获得爬虫配置
     *
     * @param id 编号
     * @return 爬虫配置
     */
    CrawlSourceConfigDO getCrawlSourceConfig(Long id);

    /**
     * 获得爬虫配置分页
     *
     * @param pageReqVO 分页查询
     * @return 爬虫配置分页
     */
    PageResult<CrawlSourceConfigDO> getCrawlSourceConfigPage(CrawlSourceConfigPageReqVO pageReqVO);

}
