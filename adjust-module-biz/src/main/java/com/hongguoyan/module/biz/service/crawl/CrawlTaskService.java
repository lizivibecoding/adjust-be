package com.hongguoyan.module.biz.service.crawl;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.controller.admin.crawl.vo.task.CrawlTaskPageReqVO;
import com.hongguoyan.module.biz.dal.dataobject.crawl.CrawlAttachmentDO;

/**
 * 爬虫任务（文件） Service 接口
 *
 * @author hgy
 */
public interface CrawlTaskService {

    /**
     * 获得爬虫任务分页
     *
     * @param pageReqVO 分页查询
     * @return 爬虫任务分页
     */
    PageResult<CrawlAttachmentDO> getCrawlTaskPage(CrawlTaskPageReqVO pageReqVO);

    /**
     * 删除爬虫任务
     *
     * @param id 编号
     */
    void deleteCrawlTask(Long id);

}
