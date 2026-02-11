package com.hongguoyan.module.biz.service.schoolrank;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;
import com.hongguoyan.module.biz.controller.admin.schoolrank.vo.*;
import com.hongguoyan.module.biz.controller.app.schoolrank.vo.AppSchoolRankSimpleRespVO;
import com.hongguoyan.module.biz.dal.dataobject.schoolrank.SchoolRankDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.schoolrank.SchoolRankMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 软科排名 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class SchoolRankServiceImpl implements SchoolRankService {

    @Resource
    private SchoolRankMapper schoolRankMapper;

    @Override
    public Long createSchoolRank(SchoolRankSaveReqVO createReqVO) {
        // 插入
        SchoolRankDO schoolRank = BeanUtils.toBean(createReqVO, SchoolRankDO.class);
        schoolRankMapper.insert(schoolRank);

        // 返回
        return schoolRank.getId();
    }

    @Override
    public void updateSchoolRank(SchoolRankSaveReqVO updateReqVO) {
        // 校验存在
        validateSchoolRankExists(updateReqVO.getId());
        // 更新
        SchoolRankDO updateObj = BeanUtils.toBean(updateReqVO, SchoolRankDO.class);
        schoolRankMapper.updateById(updateObj);
    }

    @Override
    public void deleteSchoolRank(Long id) {
        // 校验存在
        validateSchoolRankExists(id);
        // 删除
        schoolRankMapper.deleteById(id);
    }

    @Override
    public void deleteSchoolRankListByIds(List<Long> ids) {
        // 删除
        schoolRankMapper.deleteByIds(ids);
    }

    private void validateSchoolRankExists(Long id) {
        if (schoolRankMapper.selectById(id) == null) {
            throw exception(SCHOOL_RANK_NOT_EXISTS);
        }
    }

    @Override
    public SchoolRankDO getSchoolRank(Long id) {
        return schoolRankMapper.selectById(id);
    }

    @Override
    public PageResult<SchoolRankDO> getSchoolRankPage(SchoolRankPageReqVO pageReqVO) {
        return schoolRankMapper.selectPage(pageReqVO);
    }

    @Override
    public List<AppSchoolRankSimpleRespVO> getSchoolRankSimpleList(String schoolName) {
        List<SchoolRankDO> list = schoolRankMapper.selectListBySchoolName(schoolName);
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream().map(item -> {
            AppSchoolRankSimpleRespVO vo = new AppSchoolRankSimpleRespVO();
            vo.setId(item.getId());
            vo.setSchoolName(item.getSchoolName());
            return vo;
        }).collect(Collectors.toList());
    }

}
