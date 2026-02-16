package com.hongguoyan.module.biz.service.banner;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.controller.admin.banner.vo.BannerCreateReqVO;
import com.hongguoyan.module.biz.controller.admin.banner.vo.BannerPageReqVO;
import com.hongguoyan.module.biz.controller.admin.banner.vo.BannerRespVO;
import com.hongguoyan.module.biz.controller.admin.banner.vo.BannerUpdateReqVO;
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

    /**
     * 获得轮播图分页（管理后台）
     */
    PageResult<BannerRespVO> getBannerPage(@Valid BannerPageReqVO pageReqVO);

    /**
     * 获得轮播图详情（管理后台）
     */
    BannerRespVO getBanner(Long id);

    /**
     * 创建轮播图（管理后台）
     */
    Long createBanner(@Valid BannerCreateReqVO reqVO);

    /**
     * 更新轮播图（管理后台）
     */
    void updateBanner(@Valid BannerUpdateReqVO reqVO);

    /**
     * 删除轮播图（管理后台）
     */
    void deleteBanner(Long id);

}

