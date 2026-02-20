package com.hongguoyan.module.biz.service.adjustment;

import cn.hutool.core.util.StrUtil;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentAdmitPageReqVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentAdmitPageRespVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentPageReqVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentPageRespVO;
import com.hongguoyan.module.biz.dal.mysql.adjustment.AdjustmentMapper;
import com.hongguoyan.module.biz.dal.mysql.adjustmentadmit.AdjustmentAdmitMapper;
import com.hongguoyan.module.infra.api.file.FileApi;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Validated
public class AdjustmentAdminServiceImpl implements AdjustmentAdminService {

    @Resource
    private AdjustmentMapper adjustmentMapper;

    @Resource
    private AdjustmentAdmitMapper adjustmentAdmitMapper;
    @Resource
    private FileApi fileApi;

    @Override
    public PageResult<AdjustmentPageRespVO> getAdjustmentPage(AdjustmentPageReqVO reqVO) {
        if (reqVO != null && StrUtil.isNotBlank(reqVO.getKeyword())) {
            reqVO.setKeyword(reqVO.getKeyword().trim());
        }
        if (reqVO != null && StrUtil.isNotBlank(reqVO.getMajorCode())) {
            reqVO.setMajorCode(reqVO.getMajorCode().trim());
        }
        if (reqVO != null && StrUtil.isNotBlank(reqVO.getProvinceCode())) {
            reqVO.setProvinceCode(reqVO.getProvinceCode().trim());
        }
        PageResult<AdjustmentPageRespVO> pageResult = adjustmentMapper.selectAdminAdjustmentPage(reqVO);
        if (pageResult == null || pageResult.getList() == null || pageResult.getList().isEmpty()) {
            return pageResult != null ? pageResult : new PageResult<>(Collections.emptyList(), 0L);
        }
        List<AdjustmentPageRespVO> list = new ArrayList<>(pageResult.getList().size());
        for (AdjustmentPageRespVO item : pageResult.getList()) {
            if (item == null) {
                continue;
            }
            item.setSchoolLogoUrl(fileApi.buildStaticUrl(item.getSchoolLogo()));
            // do not expose stored path in admin list response
            item.setSchoolLogo(null);
            list.add(item);
        }
        return new PageResult<>(list, pageResult.getTotal());
    }

    @Override
    public List<Integer> getYearList() {
        return adjustmentMapper.selectAdminYearList();
    }

    @Override
    public PageResult<AdjustmentAdmitPageRespVO> getAdmitPage(AdjustmentAdmitPageReqVO reqVO) {
        if (reqVO != null && StrUtil.isNotBlank(reqVO.getKeyword())) {
            reqVO.setKeyword(reqVO.getKeyword().trim());
        }
        PageResult<AdjustmentAdmitPageRespVO> pageResult = adjustmentAdmitMapper.selectAdminAdmitPage(reqVO);
        if (pageResult == null || pageResult.getList() == null || pageResult.getList().isEmpty()) {
            return pageResult != null ? pageResult : new PageResult<AdjustmentAdmitPageRespVO>(Collections.emptyList(), 0L);
        }
        List<AdjustmentAdmitPageRespVO> list = new ArrayList<>(pageResult.getList().size());
        for (AdjustmentAdmitPageRespVO item : pageResult.getList()) {
            if (item == null) {
                continue;
            }
            item.setCandidateName(maskCandidateName(item.getCandidateName()));
            list.add(item);
        }
        return new PageResult<>(list, pageResult.getTotal());
    }

    /**
     * Mask candidate name for display.
     * <p>
     * Rules:
     * - 2 chars: mask last char (e.g. 张三 -> 张*)
     * - 3 chars: mask middle char (e.g. 王小明 -> 王*明)
     * - 4 chars: mask middle two chars (e.g. 欧阳娜娜 -> 欧**娜)
     * - other lengths: keep first & last, mask the rest
     */
    private String maskCandidateName(String name) {
        if (name == null) {
            return null;
        }
        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            return trimmed;
        }
        int[] cps = trimmed.codePoints().toArray();
        int n = cps.length;
        if (n <= 0) {
            return trimmed;
        }
        if (n == 1) {
            return "*";
        }
        String first = new String(cps, 0, 1);
        String last = new String(cps, n - 1, 1);
        if (n == 2) {
            return first + "*";
        }
        if (n == 3) {
            return first + "*" + last;
        }
        if (n == 4) {
            return first + "**" + last;
        }
        return first + "*".repeat(n - 2) + last;
    }

}

