package com.hongguoyan.module.biz.service.crawl;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.admin.crawl.vo.config.CrawlSourceConfigPageReqVO;
import com.hongguoyan.module.biz.controller.admin.crawl.vo.config.CrawlSourceConfigSaveReqVO;
import com.hongguoyan.module.biz.dal.dataobject.crawl.CrawlSourceConfigDO;
import com.hongguoyan.module.biz.dal.mysql.crawl.CrawlSourceConfigMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.CRAWL_SOURCE_CONFIG_NOT_EXISTS;

/**
 * 爬虫配置 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class CrawlSourceConfigServiceImpl implements CrawlSourceConfigService {

    @Resource
    private CrawlSourceConfigMapper crawlSourceConfigMapper;

    @Override
    public Long createCrawlSourceConfig(CrawlSourceConfigSaveReqVO createReqVO) {
        CrawlSourceConfigDO crawlSourceConfig = BeanUtils.toBean(createReqVO, CrawlSourceConfigDO.class);
        crawlSourceConfigMapper.insert(crawlSourceConfig);
        return crawlSourceConfig.getId();
    }

    @Override
    public void updateCrawlSourceConfig(CrawlSourceConfigSaveReqVO updateReqVO) {
        // 校验存在
        validateCrawlSourceConfigExists(updateReqVO.getId());
        // 更新
        CrawlSourceConfigDO updateObj = BeanUtils.toBean(updateReqVO, CrawlSourceConfigDO.class);
        crawlSourceConfigMapper.updateById(updateObj);
    }

    @Override
    public void deleteCrawlSourceConfig(Long id) {
        // 校验存在
        validateCrawlSourceConfigExists(id);
        // 删除
        crawlSourceConfigMapper.deleteById(id);
    }

    private void validateCrawlSourceConfigExists(Long id) {
        if (crawlSourceConfigMapper.selectById(id) == null) {
            throw exception(CRAWL_SOURCE_CONFIG_NOT_EXISTS);
        }
    }

    @Override
    public CrawlSourceConfigDO getCrawlSourceConfig(Long id) {
        return crawlSourceConfigMapper.selectById(id);
    }

    @Override
    public PageResult<CrawlSourceConfigDO> getCrawlSourceConfigPage(CrawlSourceConfigPageReqVO pageReqVO) {
        return crawlSourceConfigMapper.selectPage(pageReqVO, new LambdaQueryWrapperX<CrawlSourceConfigDO>()
                .likeIfPresent(CrawlSourceConfigDO::getSchoolName, pageReqVO.getSchoolName())
                .eqIfPresent(CrawlSourceConfigDO::getIsActive, pageReqVO.getIsActive())
                .orderByDesc(CrawlSourceConfigDO::getId));
    }

}
