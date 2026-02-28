package com.hongguoyan.module.biz.service.recommend;

import com.hongguoyan.module.biz.dal.dataobject.nationalscore.NationalScoreDO;
import com.hongguoyan.module.biz.dal.dataobject.userprofile.UserProfileDO;
import java.util.List;

public interface NationalLineEligibilityService {

    /**
     * 解析国家线上下文：年份回退、一志愿分区、匹配国家线。
     */
    NationalLineContext resolveContextOrThrow(Long userId, Integer preferredYear);

    /**
     * 按单科规则判断用户是否通过命中国家线。
     */
    boolean checkQualified(UserProfileDO userProfile, NationalScoreDO matchedLine);

    /**
     * 批量获取国家线（带年份回退机制）。
     * 优先查询该年数据；若无数据，则查询该年-1的数据。
     */
    List<NationalScoreDO> getNationalScoresWithFallback(Integer year);

    /**
     * 查找匹配的国家线（根据区域和专业代码前缀匹配）。
     */
    NationalScoreDO findMatchedNationalLine(List<NationalScoreDO> nationalScores, String area, String majorCode);
}

