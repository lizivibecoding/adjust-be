package com.hongguoyan.module.biz.service.schooldirection;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.app.schooldirection.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.schooldirection.SchoolDirectionDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.schooldirection.SchoolDirectionMapper;
import com.hongguoyan.module.biz.framework.config.AdjustProperties;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 院校研究方向 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class SchoolDirectionServiceImpl implements SchoolDirectionService {

    @Resource
    private SchoolDirectionMapper schoolDirectionMapper;
    @Resource
    private AdjustProperties adjustProperties;

    @Override
    public Long createSchoolDirection(AppSchoolDirectionSaveReqVO createReqVO) {
        // 插入
        SchoolDirectionDO schoolDirection = BeanUtils.toBean(createReqVO, SchoolDirectionDO.class);
        schoolDirectionMapper.insert(schoolDirection);

        // 返回
        return schoolDirection.getId();
    }

    @Override
    public void updateSchoolDirection(AppSchoolDirectionSaveReqVO updateReqVO) {
        // 校验存在
        validateSchoolDirectionExists(updateReqVO.getId());
        // 更新
        SchoolDirectionDO updateObj = BeanUtils.toBean(updateReqVO, SchoolDirectionDO.class);
        schoolDirectionMapper.updateById(updateObj);
    }

    @Override
    public void deleteSchoolDirection(Long id) {
        // 校验存在
        validateSchoolDirectionExists(id);
        // 删除
        schoolDirectionMapper.deleteById(id);
    }

    @Override
        public void deleteSchoolDirectionListByIds(List<Long> ids) {
        // 删除
        schoolDirectionMapper.deleteByIds(ids);
        }


    private void validateSchoolDirectionExists(Long id) {
        if (schoolDirectionMapper.selectById(id) == null) {
            throw exception(SCHOOL_DIRECTION_NOT_EXISTS);
        }
    }

    @Override
    public SchoolDirectionDO getSchoolDirection(Long id) {
        return schoolDirectionMapper.selectById(id);
    }

    @Override
    public PageResult<SchoolDirectionDO> getSchoolDirectionPage(AppSchoolDirectionPageReqVO pageReqVO) {
        return schoolDirectionMapper.selectPage(pageReqVO);
    }

    @Override
    public List<SchoolDirectionDO> getSchoolDirectionList(Long schoolId, Long collegeId, Long majorId, Integer year) {
        Integer queryYear = year != null ? year : adjustProperties.getActiveYear();
        return schoolDirectionMapper.selectListByBizKey(schoolId, collegeId, majorId, queryYear);
    }

}