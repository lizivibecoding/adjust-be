package com.hongguoyan.module.biz.service.recommend;

import com.hongguoyan.module.biz.dal.dataobject.nationalscore.NationalScoreDO;
import com.hongguoyan.module.biz.dal.dataobject.school.SchoolDO;
import com.hongguoyan.module.biz.dal.dataobject.userprofile.UserProfileDO;
import java.util.Map;

public interface NationalLineEligibilityService {

    /**
     * 解析国家线上下文：年份回退、一志愿分区、匹配国家线。
     */
    NationalLineContext resolveContextOrThrow(UserProfileDO userProfile, Integer preferredYear, Map<Long, SchoolDO> schoolMap);

    /**
     * 按单科规则判断用户是否通过命中国家线。
     */
    boolean checkQualified(UserProfileDO userProfile, NationalScoreDO matchedLine);
}

