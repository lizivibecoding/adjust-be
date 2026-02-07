package com.hongguoyan.module.biz.dal.mysql.crawl;

import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.crawl.CrawlAttachmentDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 爬虫原始文件 Mapper
 *
 * @author hgy
 */
@Mapper
public interface CrawlAttachmentMapper extends BaseMapperX<CrawlAttachmentDO> {

}
