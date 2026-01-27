package com.hongguoyan.module.biz.service.recruit;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.app.recruit.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.recruit.RecruitDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.recruit.RecruitMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 招生 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class RecruitServiceImpl implements RecruitService {

    @Resource
    private RecruitMapper recruitMapper;

    @Override
    public Long createRecruit(AppRecruitSaveReqVO createReqVO) {
        // 插入
        RecruitDO recruit = BeanUtils.toBean(createReqVO, RecruitDO.class);
        recruitMapper.insert(recruit);

        // 返回
        return recruit.getId();
    }

    @Override
    public void updateRecruit(AppRecruitSaveReqVO updateReqVO) {
        // 校验存在
        validateRecruitExists(updateReqVO.getId());
        // 更新
        RecruitDO updateObj = BeanUtils.toBean(updateReqVO, RecruitDO.class);
        recruitMapper.updateById(updateObj);
    }

    @Override
    public void deleteRecruit(Long id) {
        // 校验存在
        validateRecruitExists(id);
        // 删除
        recruitMapper.deleteById(id);
    }

    @Override
        public void deleteRecruitListByIds(List<Long> ids) {
        // 删除
        recruitMapper.deleteByIds(ids);
        }


    private void validateRecruitExists(Long id) {
        if (recruitMapper.selectById(id) == null) {
            throw exception(RECRUIT_NOT_EXISTS);
        }
    }

    @Override
    public RecruitDO getRecruit(Long id) {
        return recruitMapper.selectById(id);
    }

    @Override
    public PageResult<RecruitDO> getRecruitPage(AppRecruitPageReqVO pageReqVO) {
        return recruitMapper.selectPage(pageReqVO);
    }

}