package com.hongguoyan.module.biz.dal.mysql.banner;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.admin.banner.vo.BannerPageReqVO;
import com.hongguoyan.module.biz.dal.dataobject.banner.BannerDO;
import com.hongguoyan.module.biz.enums.banner.BannerStatusEnum;
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

    default PageResult<BannerDO> selectPage(BannerPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BannerDO>()
                .eqIfPresent(BannerDO::getPosition, reqVO.getPosition())
                .likeIfPresent(BannerDO::getTitle, reqVO.getTitle())
                .eqIfPresent(BannerDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(BannerDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BannerDO::getSort)
                .orderByDesc(BannerDO::getId));
    }

    default List<BannerDO> selectListByPosition(Integer position) {
        return selectList(new LambdaQueryWrapperX<BannerDO>()
                .eqIfPresent(BannerDO::getPosition, position)
                .orderByDesc(BannerDO::getSort)
                .orderByDesc(BannerDO::getId));
    }

    default List<BannerDO> selectAppList(Integer position, LocalDateTime now) {
        return selectList(new LambdaQueryWrapperX<BannerDO>()
                .eqIfPresent(BannerDO::getPosition, position)
                .eq(BannerDO::getStatus, BannerStatusEnum.ENABLE.getCode())
                .and(w -> w.isNull(BannerDO::getStartTime).or().le(BannerDO::getStartTime, now))
                .and(w -> w.isNull(BannerDO::getEndTime).or().ge(BannerDO::getEndTime, now))
                .orderByDesc(BannerDO::getSort)
                .orderByDesc(BannerDO::getId));
    }

}

