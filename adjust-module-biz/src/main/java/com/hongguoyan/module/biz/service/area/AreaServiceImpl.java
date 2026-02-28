package com.hongguoyan.module.biz.service.area;

import com.hongguoyan.module.biz.controller.app.area.vo.AppAreaRespVO;
import com.hongguoyan.module.biz.dal.dataobject.area.AreaDO;
import com.hongguoyan.module.biz.dal.mysql.area.AreaMapper;
import com.hongguoyan.module.biz.cache.CacheNames;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.cache.annotation.Cacheable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Area Service Implementation.
 */
@Service
@Validated
public class AreaServiceImpl implements AreaService {

    @Resource
    private AreaMapper areaMapper;

    @Override
    @Cacheable(cacheNames = CacheNames.AREA_LIST, key = "'all'")
    public List<AppAreaRespVO> getAreaList() {
        List<AreaDO> list = areaMapper.selectAllOrderByAreaAndCode();
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        List<AppAreaRespVO> result = new ArrayList<>(list.size());
        for (AreaDO item : list) {
            if (item == null) {
                continue;
            }
            AppAreaRespVO vo = new AppAreaRespVO();
            vo.setCode(item.getCode());
            vo.setName(item.getName());
            vo.setArea(item.getArea());
            result.add(vo);
        }
        return result;
    }
}

