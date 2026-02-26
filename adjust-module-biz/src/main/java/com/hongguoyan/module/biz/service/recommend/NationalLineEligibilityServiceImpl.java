package com.hongguoyan.module.biz.service.recommend;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hongguoyan.module.biz.dal.dataobject.nationalscore.NationalScoreDO;
import com.hongguoyan.module.biz.dal.dataobject.school.SchoolDO;
import com.hongguoyan.module.biz.dal.dataobject.userprofile.UserProfileDO;
import com.hongguoyan.module.biz.dal.mysql.nationalscore.NationalScoreMapper;
import com.hongguoyan.module.biz.dal.mysql.school.SchoolMapper;
import com.hongguoyan.module.biz.enums.ErrorCodeConstants;
import com.hongguoyan.module.biz.service.projectconfig.ProjectConfigService;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;

@Service
public class NationalLineEligibilityServiceImpl implements NationalLineEligibilityService {

    @Resource
    private NationalScoreMapper nationalScoreMapper;
    @Resource
    private SchoolMapper schoolMapper;
    @Resource
    private ProjectConfigService projectConfigService;

    @Override
    public NationalLineContext resolveContextOrThrow(UserProfileDO userProfile, Integer preferredYear, Map<Long, SchoolDO> schoolMap) {
        if (userProfile == null) {
            throw exception(ErrorCodeConstants.USER_PROFILE_NOT_EXISTS);
        }
        if (StrUtil.isBlank(userProfile.getTargetMajorCode())) {
            throw exception(ErrorCodeConstants.NATIONAL_SCORE_NOT_EXISTS);
        }
        int year = preferredYear != null ? preferredYear : projectConfigService.getAdjustYear();
        List<NationalScoreDO> nationalScores = nationalScoreMapper.selectList(new LambdaQueryWrapper<NationalScoreDO>()
            .eq(NationalScoreDO::getYear, year));
        if (CollUtil.isEmpty(nationalScores)) {
            year = year - 1;
            nationalScores = nationalScoreMapper.selectList(new LambdaQueryWrapper<NationalScoreDO>()
                .eq(NationalScoreDO::getYear, year));
        }

        String firstChoiceArea = resolveFirstChoiceArea(userProfile, schoolMap);
        NationalScoreDO matchedLine = findMatchedNationalLine(nationalScores, firstChoiceArea, userProfile.getTargetMajorCode());
        if (matchedLine == null) {
            throw exception(ErrorCodeConstants.NATIONAL_SCORE_NOT_EXISTS);
        }
        return NationalLineContext.builder()
            .nationalScoreYear(year)
            .firstChoiceArea(firstChoiceArea)
            .matchedLine(matchedLine)
            .nationalScores(nationalScores)
            .build();
    }

    @Override
    public boolean checkQualified(UserProfileDO userProfile, NationalScoreDO matchedLine) {
        if (userProfile == null || userProfile.getScoreTotal() == null || matchedLine == null) {
            return false;
        }
        if (userProfile.getScoreTotal().intValue() < matchedLine.getTotal()) {
            return false;
        }

        boolean hasS1 = hasSubjectData(userProfile.getSubjectName1(), userProfile.getSubjectScore1());
        boolean hasS2 = hasSubjectData(userProfile.getSubjectName2(), userProfile.getSubjectScore2());
        boolean hasS3 = hasSubjectData(userProfile.getSubjectName3(), userProfile.getSubjectScore3());
        boolean hasS4 = hasSubjectData(userProfile.getSubjectName4(), userProfile.getSubjectScore4());
        int subjectCount = (hasS1 ? 1 : 0) + (hasS2 ? 1 : 0) + (hasS3 ? 1 : 0) + (hasS4 ? 1 : 0);

        Integer s1 = userProfile.getSubjectScore1() != null ? userProfile.getSubjectScore1().intValue() : null;
        Integer s2 = userProfile.getSubjectScore2() != null ? userProfile.getSubjectScore2().intValue() : null;
        Integer s3 = userProfile.getSubjectScore3() != null ? userProfile.getSubjectScore3().intValue() : null;
        Integer s4 = userProfile.getSubjectScore4() != null ? userProfile.getSubjectScore4().intValue() : null;

        if (subjectCount >= 4) {
            return meetsSingleLine(s1, matchedLine.getSingle100())
                && meetsSingleLine(s2, matchedLine.getSingle100())
                && meetsSingleLine(s3, matchedLine.getSingle150())
                && meetsSingleLine(s4, matchedLine.getSingle150());
        }
        if (subjectCount == 3) {
            return meetsSingleLine(s1, matchedLine.getSingle100())
                && meetsSingleLine(s2, matchedLine.getSingle100())
                && meetsSingleLine(s3, matchedLine.getSingle150());
        }
        if (subjectCount == 2) {
            return meetsSingleLine(s1, matchedLine.getSingle150())
                && meetsSingleLine(s2, matchedLine.getSingle100());
        }
        return false;
    }

    @Override
    public List<NationalScoreDO> getNationalScoresWithFallback(Integer year) {
        if (year == null) {
            return CollUtil.newArrayList();
        }
        // 1. Try to fetch requested year
        List<NationalScoreDO> list = nationalScoreMapper.selectList(new LambdaQueryWrapper<NationalScoreDO>()
            .eq(NationalScoreDO::getYear, year));
        if (CollUtil.isNotEmpty(list)) {
            return list;
        }
        // 2. Fallback to year - 1
        return nationalScoreMapper.selectList(new LambdaQueryWrapper<NationalScoreDO>()
            .eq(NationalScoreDO::getYear, year - 1));
    }

    private String resolveFirstChoiceArea(UserProfileDO userProfile, Map<Long, SchoolDO> schoolMap) {
        String firstChoiceArea = "A";
        if (userProfile.getTargetSchoolId() == null) {
            return firstChoiceArea;
        }
        SchoolDO firstChoiceSchool = schoolMap != null ? schoolMap.get(userProfile.getTargetSchoolId()) : null;
        if (firstChoiceSchool == null) {
            firstChoiceSchool = schoolMapper.selectById(userProfile.getTargetSchoolId());
        }
        if (firstChoiceSchool != null && StrUtil.isNotBlank(firstChoiceSchool.getProvinceArea())) {
            return firstChoiceSchool.getProvinceArea();
        }
        return firstChoiceArea;
    }

    @Override
    public NationalScoreDO findMatchedNationalLine(List<NationalScoreDO> nationalScores, String area, String majorCode) {
        if (StrUtil.isBlank(area) || StrUtil.isBlank(majorCode) || CollUtil.isEmpty(nationalScores)) {
            return null;
        }
        NationalScoreDO matchedLine = nationalScores.stream()
            .filter(ns -> area.equalsIgnoreCase(ns.getArea()))
            .filter(ns -> ns.getMajorCode() != null && majorCode.startsWith(ns.getMajorCode()))
            .max(Comparator.comparingInt(o -> o.getMajorCode().length()))
            .orElse(null);
        if (matchedLine != null) {
            return matchedLine;
        }
        if (majorCode.length() < 2) {
            return null;
        }
        return nationalScores.stream()
            .filter(ns -> area.equalsIgnoreCase(ns.getArea()))
            .filter(ns -> ns.getMajorCode() != null && majorCode.substring(0, 2).equals(ns.getMajorCode()))
            .findFirst()
            .orElse(null);
    }

    private boolean hasSubjectData(String subjectName, BigDecimal subjectScore) {
        return StrUtil.isNotBlank(subjectName) || subjectScore != null;
    }

    private boolean meetsSingleLine(Integer score, Short baseLine) {
        if (score == null || baseLine == null) {
            return false;
        }
        return score >=  baseLine.intValue();
    }
}

