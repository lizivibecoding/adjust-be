package com.hongguoyan.module.biz.cache;

import com.hongguoyan.module.biz.dal.dataobject.adjustment.AdjustmentDO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 缓存失效门面（聚合主动失效逻辑；TTL 足够的缓存不在此处理）。
 */
@Service
@Validated
public class CacheEvictFacade {

    @Caching(evict = {
            @CacheEvict(cacheNames = CacheNames.ADJUSTMENT_DETAIL_ROWS,
                    key = "'year:' + #row.year"
                            + " + ':school:' + #row.schoolId + ':college:' + #row.collegeId + ':major:' + #row.majorId"
                            + " + ':study:' + #row.studyMode",
                    condition = "#row != null && #row.year != null && #row.schoolId != null && #row.collegeId != null && #row.majorId != null && #row.studyMode != null"),
            @CacheEvict(cacheNames = CacheNames.ADJUSTMENT_UPDATE_STATS, key = "'default'", condition = "#row != null")
    })
    public void evictAfterAdjustmentChanged(AdjustmentDO row) {
        // no-op (used for cache eviction)
    }

    @CacheEvict(cacheNames = CacheNames.ADJUSTMENT_ADMIT_LIST,
            key = "'year:' + #year"
                    + " + ':school:' + #schoolId + ':college:' + #collegeId + ':major:' + #majorId"
                    + " + ':study:' + #studyMode",
            condition = "#year != null && #schoolId != null && #collegeId != null && #majorId != null && #studyMode != null")
    public void evictAdjustmentAdmitList(Integer year, Long schoolId, Long collegeId, Long majorId, Integer studyMode) {
        // no-op (used for cache eviction)
    }
}

