package com.hongguoyan.module.biz.cache.adjustment;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.cache.CacheNames;
import com.hongguoyan.module.biz.controller.app.school.vo.AppSchoolAdjustmentPageReqVO;
import com.hongguoyan.module.biz.controller.app.school.vo.AppSchoolAdjustmentRespVO;
import com.hongguoyan.module.biz.dal.mysql.adjustment.AdjustmentMapper;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 院校调剂列表-缓存。
 */
@Service
@Validated
public class SchoolAdjustmentCache {

    @Resource
    private AdjustmentMapper adjustmentMapper;

    @Cacheable(cacheNames = CacheNames.SCHOOL_ADJUSTMENT_PAGE,
            key = "'school:' + #reqVO.schoolId + ':y:' + #reqVO.beginYear + '-' + #reqVO.endYear"
                    + " + ':pn:' + #reqVO.pageNo + ':ps:' + #reqVO.pageSize")
    public PageResult<AppSchoolAdjustmentRespVO> getSchoolAdjustmentPage(AppSchoolAdjustmentPageReqVO reqVO) {
        return adjustmentMapper.selectSchoolAdjustmentPage(reqVO);
    }
}

