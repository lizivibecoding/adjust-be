package com.hongguoyan.module.biz.service.projectconfig;

import com.hongguoyan.module.biz.controller.app.projectconfig.vo.AppProjectConfigRespVO;
import com.hongguoyan.module.biz.dal.mysql.adjustment.AdjustmentMapper;
import com.hongguoyan.module.biz.framework.config.AdjustProperties;
import jakarta.annotation.Resource;
import java.time.Year;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ProjectConfigServiceImpl implements ProjectConfigService {

    @Resource
    private AdjustProperties adjustProperties;
    @Resource
    private AdjustmentMapper adjustmentMapper;

    @Override
    public AppProjectConfigRespVO getProjectConfig() {
        Integer activeYear = adjustProperties.getActiveYear();
        if (activeYear == null) {
            activeYear = Year.now().getValue();
        }

        List<Integer> adjustmentYears = adjustmentMapper.selectYearList();
        if (adjustmentYears == null || adjustmentYears.isEmpty()) {
            adjustmentYears = List.of(activeYear);
        }

        int sameScoreUpperYear = activeYear - 1;
        List<Integer> sameScoreYears = adjustmentYears.stream()
                .filter(Objects::nonNull)
                .filter(y -> y <= sameScoreUpperYear)
                .toList();
        if (sameScoreYears.isEmpty()) {
            sameScoreYears = List.of(sameScoreUpperYear);
        }

        AppProjectConfigRespVO respVO = new AppProjectConfigRespVO();
        respVO.setStatYear(adjustmentYears.get(0));
        respVO.setAdjustmentYears(adjustmentYears);
        respVO.setSameScoreYears(sameScoreYears);
        return respVO;
    }
}

