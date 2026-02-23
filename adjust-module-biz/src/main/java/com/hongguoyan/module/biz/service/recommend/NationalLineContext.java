package com.hongguoyan.module.biz.service.recommend;

import com.hongguoyan.module.biz.dal.dataobject.nationalscore.NationalScoreDO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NationalLineContext {

    /**
     * 实际命中的国家线年份（优先 preferredYear，若无数据回退到 preferredYear-1）。
     */
    private Integer nationalScoreYear;

    /**
     * 一志愿学校所属分区（A/B）。
     */
    private String firstChoiceArea;

    /**
     * 命中的国家线记录。
     */
    private NationalScoreDO matchedLine;

    /**
     * 命中年份的国家线列表（便于上层复用）。
     */
    private List<NationalScoreDO> nationalScores;
}

