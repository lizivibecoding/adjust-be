package com.hongguoyan.module.biz.cache.adjustment;

import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.cache.CacheNames;
import com.hongguoyan.module.biz.dal.dataobject.adjustment.AdjustmentDO;
import com.hongguoyan.module.biz.dal.mysql.adjustment.AdjustmentMapper;
import jakarta.annotation.Resource;
import java.util.Collections;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 调剂详情-缓存。
 */
@Service
@Validated
public class AdjustmentDetailCache {

    @Resource
    private AdjustmentMapper adjustmentMapper;

    @Cacheable(cacheNames = CacheNames.ADJUSTMENT_DETAIL_ROWS,
            key = "'year:' + #year"
                    + " + ':school:' + #schoolId + ':college:' + #collegeId + ':major:' + #majorId"
                    + " + ':study:' + #studyMode",
            sync = true)
    public List<AdjustmentDO> listDetailRows(Long schoolId, Long majorId, Long collegeId, Integer year, Integer studyMode) {
        if (schoolId == null || majorId == null || collegeId == null || year == null || studyMode == null) {
            return Collections.emptyList();
        }
        return adjustmentMapper.selectList(new LambdaQueryWrapperX<AdjustmentDO>()
                .eq(AdjustmentDO::getSchoolId, schoolId)
                .eq(AdjustmentDO::getMajorId, majorId)
                .eq(AdjustmentDO::getCollegeId, collegeId)
                .eq(AdjustmentDO::getYear, year)
                .eq(AdjustmentDO::getStudyMode, studyMode));
    }

    @CacheEvict(cacheNames = CacheNames.ADJUSTMENT_DETAIL_ROWS,
            key = "'year:' + #year"
                    + " + ':school:' + #schoolId + ':college:' + #collegeId + ':major:' + #majorId"
                    + " + ':study:' + #studyMode")
    public void evictDetailRows(Long schoolId, Long majorId, Long collegeId, Integer year, Integer studyMode) {
        // no-op (used for cache eviction)
    }
}

