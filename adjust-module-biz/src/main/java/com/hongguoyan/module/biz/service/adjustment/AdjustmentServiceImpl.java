package com.hongguoyan.module.biz.service.adjustment;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.adjustment.AdjustmentDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.adjustment.AdjustmentMapper;
import com.hongguoyan.module.biz.dal.mysql.major.MajorMapper;
import com.hongguoyan.module.biz.dal.mysql.school.SchoolMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 调剂 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class AdjustmentServiceImpl implements AdjustmentService {

    @Resource
    private AdjustmentMapper adjustmentMapper;
    @Resource
    private SchoolMapper schoolMapper;
    @Resource
    private MajorMapper majorMapper;

    @Override
    public Long createAdjustment(AppAdjustmentSaveReqVO createReqVO) {
        // 插入
        AdjustmentDO adjustment = BeanUtils.toBean(createReqVO, AdjustmentDO.class);
        adjustmentMapper.insert(adjustment);

        // 返回
        return adjustment.getId();
    }

    @Override
    public void updateAdjustment(AppAdjustmentSaveReqVO updateReqVO) {
        // 校验存在
        validateAdjustmentExists(updateReqVO.getId());
        // 更新
        AdjustmentDO updateObj = BeanUtils.toBean(updateReqVO, AdjustmentDO.class);
        adjustmentMapper.updateById(updateObj);
    }

    @Override
    public void deleteAdjustment(Long id) {
        // 校验存在
        validateAdjustmentExists(id);
        // 删除
        adjustmentMapper.deleteById(id);
    }

    @Override
        public void deleteAdjustmentListByIds(List<Long> ids) {
        // 删除
        adjustmentMapper.deleteByIds(ids);
        }


    private void validateAdjustmentExists(Long id) {
        if (adjustmentMapper.selectById(id) == null) {
            throw exception(ADJUSTMENT_NOT_EXISTS);
        }
    }

    @Override
    public AdjustmentDO getAdjustment(Long id) {
        return adjustmentMapper.selectById(id);
    }

    @Override
    public PageResult<AdjustmentDO> getAdjustmentPage(AppAdjustmentPageReqVO pageReqVO) {
        return adjustmentMapper.selectPage(pageReqVO);
    }

    @Override
    public AppAdjustmentSearchTabRespVO getAdjustmentSearchPage(AppAdjustmentSearchReqVO reqVO) {
        if (reqVO != null && StrUtil.isNotBlank(reqVO.getKeyword())) {
            reqVO.setKeyword(buildBooleanKeyword(reqVO.getKeyword()));
        }
        String tabType = reqVO != null ? reqVO.getTabType() : null;
        boolean schoolTab = "school".equalsIgnoreCase(tabType);
        AppAdjustmentSearchTabRespVO respVO = new AppAdjustmentSearchTabRespVO();
        if (schoolTab) {
            PageResult<AppAdjustmentSearchSchoolRespVO> pageResult = adjustmentMapper.selectSearchSchoolPage(reqVO);
            respVO.setTotal(pageResult.getTotal());
            respVO.setSchoolList(pageResult.getList());
            respVO.setMajorList(Collections.emptyList());
        } else {
            PageResult<AppAdjustmentSearchRespVO> pageResult = adjustmentMapper.selectSearchMajorPage(reqVO);
            List<AppAdjustmentSearchRespVO> majorList = pageResult.getList();
            int heatCap = 1000;
            for (AppAdjustmentSearchRespVO item : majorList) {
                Integer viewCount = item.getViewCount();
                int heat = 0;
                if (viewCount != null && viewCount > 0) {
                    heat = (int) Math.round(viewCount * 100.0 / heatCap);
                    if (heat > 100) {
                        heat = 100;
                    }
                }
                item.setHeat(heat);
            }
            respVO.setTotal(pageResult.getTotal());
            respVO.setMajorList(majorList);
            respVO.setSchoolList(Collections.emptyList());
        }
        return respVO;
    }

    @Override
    public AppAdjustmentSuggestRespVO getAdjustmentSuggest(String keyword) {
        AppAdjustmentSuggestRespVO respVO = new AppAdjustmentSuggestRespVO();
        if (StrUtil.isBlank(keyword)) {
            respVO.setSuggestions(Collections.emptyList());
            return respVO;
        }
        int limit = 10;
        LinkedHashSet<String> suggestions = new LinkedHashSet<>();
        suggestions.addAll(schoolMapper.selectSuggestSchoolNames(keyword, limit));
        suggestions.addAll(majorMapper.selectSuggestMajorCodes(keyword, limit));
        suggestions.addAll(majorMapper.selectSuggestMajorNames(keyword, limit));
        List<String> result = new ArrayList<>();
        for (String suggestion : suggestions) {
            result.add(suggestion);
            if (result.size() >= limit) {
                break;
            }
        }
        respVO.setSuggestions(result);
        return respVO;
    }

    private String buildBooleanKeyword(String keyword) {
        List<String> tokens = StrUtil.splitTrim(keyword, ' ');
        if (tokens == null || tokens.isEmpty()) {
            return keyword;
        }
        StringBuilder builder = new StringBuilder();
        for (String token : tokens) {
            if (StrUtil.isBlank(token)) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append('+').append(token);
        }
        return builder.length() > 0 ? builder.toString() : keyword;
    }

}