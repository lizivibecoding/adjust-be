package com.hongguoyan.module.biz.service.undergraduatemajor;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.hongguoyan.module.biz.controller.admin.undergraduatemajor.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.undergraduatemajor.UndergraduateMajorDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;

import com.hongguoyan.module.biz.dal.mysql.undergraduatemajor.UndergraduateMajorMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 学科专业 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class UndergraduateMajorServiceImpl implements UndergraduateMajorService {

    @Resource
    private UndergraduateMajorMapper undergraduateMajorMapper;

    @Override
    public Long createUndergraduateMajor(UndergraduateMajorSaveReqVO createReqVO) {
        UndergraduateMajorDO undergraduateMajor = BeanUtils.toBean(createReqVO, UndergraduateMajorDO.class);
        undergraduateMajorMapper.insert(undergraduateMajor);
        return undergraduateMajor.getId();
    }

    @Override
    public void updateUndergraduateMajor(UndergraduateMajorSaveReqVO updateReqVO) {
        validateUndergraduateMajorExists(updateReqVO.getId());
        UndergraduateMajorDO updateObj = BeanUtils.toBean(updateReqVO, UndergraduateMajorDO.class);
        undergraduateMajorMapper.updateById(updateObj);
    }

    @Override
    public void deleteUndergraduateMajor(Long id) {
        validateUndergraduateMajorExists(id);
        undergraduateMajorMapper.deleteById(id);
    }

    @Override
    public void deleteUndergraduateMajorListByIds(List<Long> ids) {
        undergraduateMajorMapper.deleteByIds(ids);
    }

    private void validateUndergraduateMajorExists(Long id) {
        if (undergraduateMajorMapper.selectById(id) == null) {
            throw exception(UNDERGRADUATE_MAJOR_NOT_EXISTS);
        }
    }

    @Override
    public UndergraduateMajorDO getUndergraduateMajor(Long id) {
        return undergraduateMajorMapper.selectById(id);
    }

    @Override
    public PageResult<UndergraduateMajorDO> getUndergraduateMajorPage(UndergraduateMajorPageReqVO pageReqVO) {
        return undergraduateMajorMapper.selectPage(pageReqVO);
    }

    @Override
    public List<UndergraduateMajorDO> getUndergraduateMajorList() {
        LambdaQueryWrapperX<UndergraduateMajorDO> wrapper = new LambdaQueryWrapperX<>();
        wrapper.eq(UndergraduateMajorDO::getStatus, 0)
               .orderByAsc(UndergraduateMajorDO::getSort);
        return undergraduateMajorMapper.selectList(wrapper);
    }

}
