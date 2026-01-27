package com.hongguoyan.module.biz.service.major;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.app.major.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.major.MajorDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.major.MajorMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 专业 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class MajorServiceImpl implements MajorService {

    @Resource
    private MajorMapper majorMapper;

    @Override
    public Long createMajor(AppMajorSaveReqVO createReqVO) {
        // 插入
        MajorDO major = BeanUtils.toBean(createReqVO, MajorDO.class);
        majorMapper.insert(major);

        // 返回
        return major.getId();
    }

    @Override
    public void updateMajor(AppMajorSaveReqVO updateReqVO) {
        // 校验存在
        validateMajorExists(updateReqVO.getId());
        // 更新
        MajorDO updateObj = BeanUtils.toBean(updateReqVO, MajorDO.class);
        majorMapper.updateById(updateObj);
    }

    @Override
    public void deleteMajor(Long id) {
        // 校验存在
        validateMajorExists(id);
        // 删除
        majorMapper.deleteById(id);
    }

    @Override
        public void deleteMajorListByIds(List<Long> ids) {
        // 删除
        majorMapper.deleteByIds(ids);
        }


    private void validateMajorExists(Long id) {
        if (majorMapper.selectById(id) == null) {
            throw exception(MAJOR_NOT_EXISTS);
        }
    }

    @Override
    public MajorDO getMajor(Long id) {
        return majorMapper.selectById(id);
    }

    @Override
    public PageResult<MajorDO> getMajorPage(AppMajorPageReqVO pageReqVO) {
        return majorMapper.selectPage(pageReqVO);
    }

    @Override
    public List<AppMajorLevel1RespVO> getMajorLevel1List() {
        return majorMapper.selectLevel1List();
    }

}