package com.hongguoyan.module.biz.service.competition;

import com.hongguoyan.module.biz.controller.app.competition.vo.AppCompetitionRespVO;
import com.hongguoyan.module.biz.dal.dataobject.competition.CompetitionDO;
import com.hongguoyan.module.biz.dal.mysql.competition.CompetitionMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Competition Service Implementation.
 */
@Service
@Validated
public class CompetitionServiceImpl implements CompetitionService {

    @Resource
    private CompetitionMapper competitionMapper;

    @Override
    public List<AppCompetitionRespVO> getCompetitionList() {
        List<CompetitionDO> list = competitionMapper.selectAll();
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        List<AppCompetitionRespVO> result = new ArrayList<>(list.size());
        for (CompetitionDO item : list) {
            if (item == null) {
                continue;
            }
            AppCompetitionRespVO vo = new AppCompetitionRespVO();
            vo.setId(item.getId());
            vo.setName(item.getName());
            vo.setUrl(item.getUrl());
            result.add(vo);
        }
        return result;
    }
}

