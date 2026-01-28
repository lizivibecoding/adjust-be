package com.hongguoyan.module.biz.dal.mysql.banner;

import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.dal.dataobject.banner.BannerDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 轮播图 Mapper
 *
 * @author hgy
 */
@Mapper
public interface BannerMapper extends BaseMapperX<BannerDO> {

    default List<BannerDO> selectAppList(Integer position, LocalDateTime now) {
        return selectList(new LambdaQueryWrapperX<BannerDO>()
                .eqIfPresent(BannerDO::getPosition, position)
                .eq(BannerDO::getStatus, 1)
                .and(w -> w.isNull(BannerDO::getStartTime).or().le(BannerDO::getStartTime, now))
                .and(w -> w.isNull(BannerDO::getEndTime).or().ge(BannerDO::getEndTime, now))
                .orderByDesc(BannerDO::getSort)
                .orderByDesc(BannerDO::getId));
    }

}

