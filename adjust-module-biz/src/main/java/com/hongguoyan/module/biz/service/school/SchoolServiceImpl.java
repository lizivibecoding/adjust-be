package com.hongguoyan.module.biz.service.school;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.app.school.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.school.SchoolDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.school.SchoolMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import cn.hutool.core.util.StrUtil;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 院校 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class SchoolServiceImpl implements SchoolService {

    @Resource
    private SchoolMapper schoolMapper;

    @Override
    public Long createSchool(AppSchoolSaveReqVO createReqVO) {
        // 插入
        SchoolDO school = BeanUtils.toBean(createReqVO, SchoolDO.class);
        schoolMapper.insert(school);

        // 返回
        return school.getId();
    }

    @Override
    public void updateSchool(AppSchoolSaveReqVO updateReqVO) {
        // 校验存在
        validateSchoolExists(updateReqVO.getId());
        // 更新
        SchoolDO updateObj = BeanUtils.toBean(updateReqVO, SchoolDO.class);
        schoolMapper.updateById(updateObj);
    }

    @Override
    public void deleteSchool(Long id) {
        // 校验存在
        validateSchoolExists(id);
        // 删除
        schoolMapper.deleteById(id);
    }

    @Override
        public void deleteSchoolListByIds(List<Long> ids) {
        // 删除
        schoolMapper.deleteByIds(ids);
        }


    private void validateSchoolExists(Long id) {
        if (schoolMapper.selectById(id) == null) {
            throw exception(SCHOOL_NOT_EXISTS);
        }
    }

    @Override
    public SchoolDO getSchool(Long id) {
        return schoolMapper.selectById(id);
    }

    @Override
    public AppSchoolOverviewRespVO getSchoolOverview(Long schoolId) {
        SchoolDO school = schoolMapper.selectById(schoolId);
        if (school == null) {
            throw exception(SCHOOL_NOT_EXISTS);
        }
        return BeanUtils.toBean(school, AppSchoolOverviewRespVO.class);
    }

    @Override
    public PageResult<SchoolDO> getSchoolPage(AppSchoolPageReqVO pageReqVO) {
        return schoolMapper.selectPage(pageReqVO);
    }

    @Override
    public List<AppSchoolSimpleOptionRespVO> getSchoolSimpleAll() {
        List<SchoolDO> list = schoolMapper.selectList(new LambdaQueryWrapper<SchoolDO>()
                .select(SchoolDO::getId, SchoolDO::getSchoolName)
                .eq(SchoolDO::getDeleted, false)
                .orderByAsc(SchoolDO::getId));
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        List<AppSchoolSimpleOptionRespVO> result = new ArrayList<>(list.size());
        for (SchoolDO item : list) {
            if (item == null) {
                continue;
            }
            AppSchoolSimpleOptionRespVO opt = new AppSchoolSimpleOptionRespVO();
            opt.setId(item.getId());
            opt.setName(StrUtil.blankToDefault(item.getSchoolName(), ""));
            result.add(opt);
        }
        return result;
    }

}
