package com.hongguoyan.module.biz.service.competition;

import com.hongguoyan.module.biz.controller.app.competition.vo.AppCompetitionRespVO;

import java.util.List;

/**
 * Competition Service.
 */
public interface CompetitionService {

    /**
     * Get competition list.
     */
    List<AppCompetitionRespVO> getCompetitionList();
}

