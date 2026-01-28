package com.hongguoyan.module.biz.service.nationalscore;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.admin.nationalscore.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.nationalscore.NationalScoreDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.nationalscore.NationalScoreMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 国家线 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class NationalScoreServiceImpl implements NationalScoreService {

    @Resource
    private NationalScoreMapper nationalScoreMapper;

    @Override
    public Long createNationalScore(NationalScoreSaveReqVO createReqVO) {
        // 插入
        NationalScoreDO nationalScore = BeanUtils.toBean(createReqVO, NationalScoreDO.class);
        nationalScoreMapper.insert(nationalScore);

        // 返回
        return nationalScore.getId();
    }

    @Override
    public void updateNationalScore(NationalScoreSaveReqVO updateReqVO) {
        // 校验存在
        validateNationalScoreExists(updateReqVO.getId());
        // 更新
        NationalScoreDO updateObj = BeanUtils.toBean(updateReqVO, NationalScoreDO.class);
        nationalScoreMapper.updateById(updateObj);
    }

    @Override
    public void deleteNationalScore(Long id) {
        // 校验存在
        validateNationalScoreExists(id);
        // 删除
        nationalScoreMapper.deleteById(id);
    }

    @Override
        public void deleteNationalScoreListByIds(List<Long> ids) {
        // 删除
        nationalScoreMapper.deleteByIds(ids);
        }


    private void validateNationalScoreExists(Long id) {
        if (nationalScoreMapper.selectById(id) == null) {
            throw exception(NATIONAL_SCORE_NOT_EXISTS);
        }
    }

    @Override
    public NationalScoreDO getNationalScore(Long id) {
        return nationalScoreMapper.selectById(id);
    }

    @Override
    public PageResult<NationalScoreDO> getNationalScorePage(NationalScorePageReqVO pageReqVO) {
        return nationalScoreMapper.selectPage(pageReqVO);
    }

}