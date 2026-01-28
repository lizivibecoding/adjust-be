package com.hongguoyan.module.biz.service.banner;

import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.module.biz.controller.app.banner.vo.AppBannerListReqVO;
import com.hongguoyan.module.biz.controller.app.banner.vo.AppBannerRespVO;
import com.hongguoyan.module.biz.dal.dataobject.banner.BannerDO;
import com.hongguoyan.module.biz.dal.mysql.banner.BannerMapper;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 轮播图 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class BannerServiceImpl implements BannerService {

    @Resource
    private BannerMapper bannerMapper;

    @Override
    public List<AppBannerRespVO> getAppBannerList(@Valid AppBannerListReqVO reqVO) {
        if (reqVO == null || reqVO.getPosition() == null) {
            return Collections.emptyList();
        }
        List<BannerDO> list = bannerMapper.selectAppList(reqVO.getPosition(), LocalDateTime.now());
        return BeanUtils.toBean(list, AppBannerRespVO.class);
    }

}

