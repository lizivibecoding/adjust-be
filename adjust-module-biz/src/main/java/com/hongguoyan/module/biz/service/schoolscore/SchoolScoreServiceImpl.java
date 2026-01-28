package com.hongguoyan.module.biz.service.schoolscore;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.admin.schoolscore.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.schoolscore.SchoolScoreDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.schoolscore.SchoolScoreMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 自划线 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class SchoolScoreServiceImpl implements SchoolScoreService {

    @Resource
    private SchoolScoreMapper schoolScoreMapper;

    @Override
    public Long createSchoolScore(SchoolScoreSaveReqVO createReqVO) {
        // 插入
        SchoolScoreDO schoolScore = BeanUtils.toBean(createReqVO, SchoolScoreDO.class);
        schoolScoreMapper.insert(schoolScore);

        // 返回
        return schoolScore.getId();
    }

    @Override
    public void updateSchoolScore(SchoolScoreSaveReqVO updateReqVO) {
        // 校验存在
        validateSchoolScoreExists(updateReqVO.getId());
        // 更新
        SchoolScoreDO updateObj = BeanUtils.toBean(updateReqVO, SchoolScoreDO.class);
        schoolScoreMapper.updateById(updateObj);
    }

    @Override
    public void deleteSchoolScore(Long id) {
        // 校验存在
        validateSchoolScoreExists(id);
        // 删除
        schoolScoreMapper.deleteById(id);
    }

    @Override
        public void deleteSchoolScoreListByIds(List<Long> ids) {
        // 删除
        schoolScoreMapper.deleteByIds(ids);
        }


    private void validateSchoolScoreExists(Long id) {
        if (schoolScoreMapper.selectById(id) == null) {
            throw exception(SCHOOL_SCORE_NOT_EXISTS);
        }
    }

    @Override
    public SchoolScoreDO getSchoolScore(Long id) {
        return schoolScoreMapper.selectById(id);
    }

    @Override
    public PageResult<SchoolScoreDO> getSchoolScorePage(SchoolScorePageReqVO pageReqVO) {
        return schoolScoreMapper.selectPage(pageReqVO);
    }

}