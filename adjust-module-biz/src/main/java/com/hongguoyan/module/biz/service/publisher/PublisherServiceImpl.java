package com.hongguoyan.module.biz.service.publisher;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.app.publisher.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.publisher.PublisherDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.publisher.PublisherMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 发布者资质 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class PublisherServiceImpl implements PublisherService {

    @Resource
    private PublisherMapper publisherMapper;

    @Override
    public Long createPublisher(AppPublisherSaveReqVO createReqVO) {
        // 插入
        PublisherDO publisher = BeanUtils.toBean(createReqVO, PublisherDO.class);
        publisherMapper.insert(publisher);

        // 返回
        return publisher.getId();
    }

    @Override
    public void updatePublisher(AppPublisherSaveReqVO updateReqVO) {
        // 校验存在
        validatePublisherExists(updateReqVO.getId());
        // 更新
        PublisherDO updateObj = BeanUtils.toBean(updateReqVO, PublisherDO.class);
        publisherMapper.updateById(updateObj);
    }

    @Override
    public void deletePublisher(Long id) {
        // 校验存在
        validatePublisherExists(id);
        // 删除
        publisherMapper.deleteById(id);
    }

    @Override
        public void deletePublisherListByIds(List<Long> ids) {
        // 删除
        publisherMapper.deleteByIds(ids);
        }


    private void validatePublisherExists(Long id) {
        if (publisherMapper.selectById(id) == null) {
            throw exception(PUBLISHER_NOT_EXISTS);
        }
    }

    @Override
    public PublisherDO getPublisher(Long id) {
        return publisherMapper.selectById(id);
    }

    @Override
    public PageResult<PublisherDO> getPublisherPage(AppPublisherPageReqVO pageReqVO) {
        return publisherMapper.selectPage(pageReqVO);
    }

}