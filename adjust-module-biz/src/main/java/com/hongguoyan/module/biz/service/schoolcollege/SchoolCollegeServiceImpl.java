package com.hongguoyan.module.biz.service.schoolcollege;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.app.schoolcollege.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.schoolcollege.SchoolCollegeDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.schoolcollege.SchoolCollegeMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 学院 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class SchoolCollegeServiceImpl implements SchoolCollegeService {

    @Resource
    private SchoolCollegeMapper schoolCollegeMapper;

    @Override
    public Long createSchoolCollege(AppSchoolCollegeSaveReqVO createReqVO) {
        // 插入
        SchoolCollegeDO schoolCollege = BeanUtils.toBean(createReqVO, SchoolCollegeDO.class);
        schoolCollegeMapper.insert(schoolCollege);

        // 返回
        return schoolCollege.getId();
    }

    @Override
    public void updateSchoolCollege(AppSchoolCollegeSaveReqVO updateReqVO) {
        // 校验存在
        validateSchoolCollegeExists(updateReqVO.getId());
        // 更新
        SchoolCollegeDO updateObj = BeanUtils.toBean(updateReqVO, SchoolCollegeDO.class);
        schoolCollegeMapper.updateById(updateObj);
    }

    @Override
    public void deleteSchoolCollege(Long id) {
        // 校验存在
        validateSchoolCollegeExists(id);
        // 删除
        schoolCollegeMapper.deleteById(id);
    }

    @Override
        public void deleteSchoolCollegeListByIds(List<Long> ids) {
        // 删除
        schoolCollegeMapper.deleteByIds(ids);
        }


    private void validateSchoolCollegeExists(Long id) {
        if (schoolCollegeMapper.selectById(id) == null) {
            throw exception(SCHOOL_COLLEGE_NOT_EXISTS);
        }
    }

    @Override
    public SchoolCollegeDO getSchoolCollege(Long id) {
        return schoolCollegeMapper.selectById(id);
    }

    @Override
    public PageResult<SchoolCollegeDO> getSchoolCollegePage(AppSchoolCollegePageReqVO pageReqVO) {
        return schoolCollegeMapper.selectPage(pageReqVO);
    }

}