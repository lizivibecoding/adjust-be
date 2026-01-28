package com.hongguoyan.module.biz.service.banner;

import com.hongguoyan.module.biz.controller.app.banner.vo.AppBannerListReqVO;
import com.hongguoyan.module.biz.controller.app.banner.vo.AppBannerRespVO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 轮播图 Service 接口
 *
 * @author hgy
 */
public interface BannerService {

    /**
     * 获得 APP 轮播图列表
     *
     * @param reqVO 条件
     * @return 列表
     */
    List<AppBannerRespVO> getAppBannerList(@Valid AppBannerListReqVO reqVO);

}

