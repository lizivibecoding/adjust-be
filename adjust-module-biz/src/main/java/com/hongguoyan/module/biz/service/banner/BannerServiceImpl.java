package com.hongguoyan.module.biz.service.banner;

import cn.hutool.core.util.StrUtil;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.controller.admin.banner.vo.BannerCreateReqVO;
import com.hongguoyan.module.biz.controller.admin.banner.vo.BannerPageReqVO;
import com.hongguoyan.module.biz.controller.admin.banner.vo.BannerRespVO;
import com.hongguoyan.module.biz.controller.admin.banner.vo.BannerUpdateReqVO;
import com.hongguoyan.module.biz.controller.app.banner.vo.AppBannerListReqVO;
import com.hongguoyan.module.biz.controller.app.banner.vo.AppBannerRespVO;
import com.hongguoyan.module.biz.dal.dataobject.banner.BannerDO;
import com.hongguoyan.module.biz.dal.mysql.banner.BannerMapper;
import com.hongguoyan.module.infra.api.file.FileApi;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.BANNER_NOT_EXISTS;

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
    @Resource
    private FileApi fileApi;

    @Override
    public List<AppBannerRespVO> getAppBannerList(@Valid AppBannerListReqVO reqVO) {
        if (reqVO == null || reqVO.getPosition() == null) {
            return Collections.emptyList();
        }
        List<BannerDO> list = bannerMapper.selectAppList(reqVO.getPosition(), LocalDateTime.now());
        return BeanUtils.toBean(list, AppBannerRespVO.class);
    }

    @Override
    public PageResult<BannerRespVO> getBannerPage(@Valid BannerPageReqVO pageReqVO) {
        PageResult<BannerDO> pageResult = bannerMapper.selectPage(pageReqVO);
        PageResult<BannerRespVO> result = BeanUtils.toBean(pageResult, BannerRespVO.class);
        if (result.getList() != null) {
            result.getList().forEach(this::fillAdminPicUrl);
        }
        return result;
    }

    @Override
    public BannerRespVO getBanner(Long id) {
        BannerDO banner = bannerMapper.selectById(id);
        if (banner == null) {
            throw exception(BANNER_NOT_EXISTS);
        }
        BannerRespVO respVO = BeanUtils.toBean(banner, BannerRespVO.class);
        respVO.setPicPath(banner.getPicUrl());
        respVO.setPicUrl(fileApi.buildStaticUrl(banner.getPicUrl()));
        return respVO;
    }

    @Override
    public Long createBanner(@Valid BannerCreateReqVO reqVO) {
        BannerDO banner = new BannerDO();
        banner.setPosition(reqVO.getPosition());
        banner.setTitle(reqVO.getTitle());
        banner.setDescription(reqVO.getDescription());
        banner.setPicUrl(normalizePicPath(reqVO.getPicPath()));
        banner.setLinkType(reqVO.getLinkType());
        banner.setLinkUrl(reqVO.getLinkUrl());
        banner.setCtaText(reqVO.getCtaText());
        banner.setSort(reqVO.getSort());
        banner.setStatus(reqVO.getStatus());
        banner.setStartTime(reqVO.getStartTime());
        banner.setEndTime(reqVO.getEndTime());
        bannerMapper.insert(banner);
        return banner.getId();
    }

    @Override
    public void updateBanner(@Valid BannerUpdateReqVO reqVO) {
        if (bannerMapper.selectById(reqVO.getId()) == null) {
            throw exception(BANNER_NOT_EXISTS);
        }
        BannerDO update = new BannerDO();
        update.setId(reqVO.getId());
        update.setPosition(reqVO.getPosition());
        update.setTitle(reqVO.getTitle());
        update.setDescription(reqVO.getDescription());
        update.setPicUrl(normalizePicPath(reqVO.getPicPath()));
        update.setLinkType(reqVO.getLinkType());
        update.setLinkUrl(reqVO.getLinkUrl());
        update.setCtaText(reqVO.getCtaText());
        update.setSort(reqVO.getSort());
        update.setStatus(reqVO.getStatus());
        update.setStartTime(reqVO.getStartTime());
        update.setEndTime(reqVO.getEndTime());
        bannerMapper.updateById(update);
    }

    @Override
    public void deleteBanner(Long id) {
        if (bannerMapper.selectById(id) == null) {
            throw exception(BANNER_NOT_EXISTS);
        }
        bannerMapper.deleteById(id);
    }

    private void fillAdminPicUrl(BannerRespVO item) {
        if (item == null) {
            return;
        }
        String stored = item.getPicUrl(); // BeanUtils 会把 DO.picUrl 映射过来
        item.setPicPath(stored);
        item.setPicUrl(fileApi.buildStaticUrl(stored));
    }

    /**
     * 统一入库为 path/key（兼容前端误传完整 URL 的场景）
     */
    private String normalizePicPath(String picPathOrUrl) {
        if (StrUtil.isBlank(picPathOrUrl)) {
            return picPathOrUrl;
        }
        String value = picPathOrUrl.trim();
        // URL -> path
        if (StrUtil.startWithAny(value, "http://", "https://")) {
            try {
                URI uri = URI.create(value);
                String path = uri.getPath();
                if (StrUtil.isBlank(path)) {
                    return value;
                }
                return StrUtil.removePrefix(path, StrUtil.SLASH);
            } catch (Exception ignore) {
                return value;
            }
        }
        return StrUtil.removePrefix(value, StrUtil.SLASH);
    }
}

