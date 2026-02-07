package com.hongguoyan.module.biz.service.crawl;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.admin.crawl.vo.task.CrawlTaskPageReqVO;
import com.hongguoyan.module.biz.dal.dataobject.crawl.CrawlAttachmentDO;
import com.hongguoyan.module.biz.dal.mysql.crawl.CrawlAttachmentMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.CRAWL_TASK_NOT_EXISTS;

/**
 * 爬虫任务（文件） Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class CrawlTaskServiceImpl implements CrawlTaskService {

    @Resource
    private CrawlAttachmentMapper crawlAttachmentMapper;

    @Override
    public PageResult<CrawlAttachmentDO> getCrawlTaskPage(CrawlTaskPageReqVO pageReqVO) {
        return crawlAttachmentMapper.selectPage(pageReqVO, new LambdaQueryWrapperX<CrawlAttachmentDO>()
                .eqIfPresent(CrawlAttachmentDO::getTaskId, pageReqVO.getTaskId())
                .eqIfPresent(CrawlAttachmentDO::getConfigId, pageReqVO.getConfigId())
                .likeIfPresent(CrawlAttachmentDO::getFileName, pageReqVO.getFileName())
                .orderByDesc(CrawlAttachmentDO::getId));
    }

    @Override
    public void deleteCrawlTask(Long id) {
        if (crawlAttachmentMapper.selectById(id) == null) {
            throw exception(CRAWL_TASK_NOT_EXISTS);
        }
        crawlAttachmentMapper.deleteById(id);
    }
}
