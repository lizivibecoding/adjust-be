package com.hongguoyan.module.biz.service.schoolmajor;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.hongguoyan.module.biz.controller.app.schoolmajor.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.schoolmajor.SchoolMajorDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.schoolmajor.SchoolMajorMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 院校专业 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class SchoolMajorServiceImpl implements SchoolMajorService {

    @Resource
    private SchoolMajorMapper schoolMajorMapper;

    @Override
    public Long createSchoolMajor(SchoolMajorSaveReqVO createReqVO) {
        // 插入
        SchoolMajorDO schoolMajor = BeanUtils.toBean(createReqVO, SchoolMajorDO.class);
        schoolMajorMapper.insert(schoolMajor);

        // 返回
        return schoolMajor.getId();
    }

    @Override
    public void updateSchoolMajor(SchoolMajorSaveReqVO updateReqVO) {
        // 校验存在
        validateSchoolMajorExists(updateReqVO.getId());
        // 更新
        SchoolMajorDO updateObj = BeanUtils.toBean(updateReqVO, SchoolMajorDO.class);
        schoolMajorMapper.updateById(updateObj);
    }

    @Override
    public void deleteSchoolMajor(Long id) {
        // 校验存在
        validateSchoolMajorExists(id);
        // 删除
        schoolMajorMapper.deleteById(id);
    }

    @Override
        public void deleteSchoolMajorListByIds(List<Long> ids) {
        // 删除
        schoolMajorMapper.deleteByIds(ids);
        }


    private void validateSchoolMajorExists(Long id) {
        if (schoolMajorMapper.selectById(id) == null) {
            throw exception(SCHOOL_MAJOR_NOT_EXISTS);
        }
    }

    @Override
    public SchoolMajorDO getSchoolMajor(Long id) {
        return schoolMajorMapper.selectById(id);
    }

    @Override
    public PageResult<SchoolMajorDO> getSchoolMajorPage(SchoolMajorPageReqVO pageReqVO) {
        return schoolMajorMapper.selectPage(pageReqVO);
    }

}