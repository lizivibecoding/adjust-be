package com.hongguoyan.module.biz.service.recommend;

import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.BENEFIT_KEY_MAJOR_CATEGORY_OPEN;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.BENEFIT_KEY_USER_REPORT;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.REF_TYPE_CUSTOM_REPORT;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.app.recommend.vo.AppRecommendSchoolListReqVO;
import com.hongguoyan.module.biz.controller.app.recommend.vo.AppRecommendSchoolRespVO;
import com.hongguoyan.module.biz.dal.dataobject.adjustment.AdjustmentDO;
import com.hongguoyan.module.biz.dal.dataobject.nationalscore.NationalScoreDO;
import com.hongguoyan.module.biz.dal.dataobject.recommend.RecommendRuleDO;
import com.hongguoyan.module.biz.dal.dataobject.recommend.RecommendRuleSimAItem;
import com.hongguoyan.module.biz.dal.dataobject.recommend.UserRecommendSchoolDO;
import com.hongguoyan.module.biz.dal.dataobject.school.SchoolDO;
import com.hongguoyan.module.biz.dal.dataobject.schoolrank.SchoolRankDO;
import com.hongguoyan.module.biz.dal.dataobject.schoolscore.SchoolScoreDO;
import com.hongguoyan.module.biz.dal.dataobject.usercustomreport.UserCustomReportDO;
import com.hongguoyan.module.biz.dal.dataobject.userintention.UserIntentionDO;
import com.hongguoyan.module.biz.dal.dataobject.userpreference.UserPreferenceDO;
import com.hongguoyan.module.biz.dal.dataobject.userprofile.UserProfileDO;
import com.hongguoyan.module.biz.dal.mysql.adjustment.AdjustmentMapper;
import com.hongguoyan.module.biz.dal.mysql.adjustmentadmit.AdjustmentAdmitMapper;
import com.hongguoyan.module.biz.dal.mysql.major.MajorMapper;
import com.hongguoyan.module.biz.dal.mysql.recommend.RecommendRuleMapper;
import com.hongguoyan.module.biz.dal.mysql.recommend.UserRecommendSchoolMapper;
import com.hongguoyan.module.biz.dal.mysql.school.SchoolMapper;
import com.hongguoyan.module.biz.dal.mysql.schoolrank.SchoolRankMapper;
import com.hongguoyan.module.biz.dal.mysql.schoolscore.SchoolScoreMapper;
import com.hongguoyan.module.biz.dal.mysql.usercustomreport.UserCustomReportMapper;
import com.hongguoyan.module.biz.dal.mysql.userintention.UserIntentionMapper;
import com.hongguoyan.module.biz.dal.mysql.userpreference.UserPreferenceMapper;
import com.hongguoyan.module.biz.dal.mysql.userprofile.UserProfileMapper;
import com.hongguoyan.module.biz.enums.ErrorCodeConstants;
import com.hongguoyan.module.biz.enums.adjustment.SubjectChoiceEnum;
import com.hongguoyan.module.biz.service.ai.AiTextService;
import com.hongguoyan.module.biz.service.ai.dto.AiTextRequest;
import com.hongguoyan.module.biz.service.ai.dto.AiTextResult;
import com.hongguoyan.module.biz.service.projectconfig.ProjectConfigService;
import com.hongguoyan.module.biz.service.school.SchoolService;
import com.hongguoyan.module.biz.service.schoolrank.SchoolRankService;
import com.hongguoyan.module.biz.service.schoolscore.SchoolScoreService;
import com.hongguoyan.module.biz.service.usercustomreport.UserCustomReportService;
import com.hongguoyan.module.biz.service.vipbenefit.VipBenefitService;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 智能推荐 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
@Slf4j
public class RecommendServiceImpl implements RecommendService {

    @Resource
    private UserProfileMapper userProfileMapper;
    @Resource
    private UserIntentionMapper userIntentionMapper;
    @Resource
    private SchoolMapper schoolMapper;
    @Resource
    private MajorMapper majorMapper;
    @Resource
    private AdjustmentMapper adjustmentMapper;
    @Resource
    private UserRecommendSchoolMapper userRecommendSchoolMapper;
    @Resource
    private SchoolRankMapper schoolRankMapper;
    @Resource
    private UserCustomReportService userCustomReportService;
    @Resource
    private UserCustomReportMapper userCustomReportMapper;
    @Resource
    private AiTextService aiTextService;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private VipBenefitService vipBenefitService;
    @Resource
    private AdjustmentAdmitMapper adjustmentAdmitMapper;
    @Resource
    private RecommendRuleMapper recommendRuleMapper;
    @Resource
    private NationalLineEligibilityService nationalLineEligibilityService;
    @Resource
    private UserPreferenceMapper userPreferenceMapper;
    @Resource
    private ProjectConfigService projectConfigService;
    @Resource
    private SchoolService schoolService;
    @Resource
    private SchoolRankService schoolRankService;
    @Resource
    private SchoolScoreService schoolScoreService;

    @Override
    public PageResult<AppRecommendSchoolRespVO> recommendSchools(Long userId, AppRecommendSchoolListReqVO reqVO) {
        // 1. Determine reportId
        Long reportId = reqVO.getReportId();
        if (reportId == null) {
            UserCustomReportDO latestReport = userCustomReportMapper.selectLatestByUserId(userId);
            if (latestReport != null) {
                reportId = latestReport.getId();
            } else {
                return PageResult.empty();
            }
        }
        // 2. Query recommendations (分页 + category 筛选)
        String keyword = reqVO.getKeyword();
        LambdaQueryWrapperX<UserRecommendSchoolDO> queryWrapper = new LambdaQueryWrapperX<UserRecommendSchoolDO>()
                .eq(UserRecommendSchoolDO::getUserId, userId)
                .eq(UserRecommendSchoolDO::getReportId, reportId)
                .eqIfPresent(UserRecommendSchoolDO::getCategory, reqVO.getCategory());
        if (StrUtil.isNotBlank(keyword)) {
            queryWrapper.and(w -> w.like(UserRecommendSchoolDO::getSchoolName, keyword)
                    .or().like(UserRecommendSchoolDO::getMajorName, keyword)
                    .or().like(UserRecommendSchoolDO::getMajorCode, keyword));
        }
        queryWrapper.orderByDesc(UserRecommendSchoolDO::getSimFinal);
        PageResult<UserRecommendSchoolDO> pageResult = userRecommendSchoolMapper.selectPage(reqVO, queryWrapper);

        if (CollUtil.isEmpty(pageResult.getList())) {
            return PageResult.empty(pageResult.getTotal());
        }
        List<UserRecommendSchoolDO> recommendations = pageResult.getList();

        // 3. Collect IDs and fetch related entities
        Set<Long> adjustmentIds = new HashSet<>();
        Set<Long> schoolIds = new HashSet<>();
        Set<Long> collegeIds = new HashSet<>();
        Set<Long> majorIds = new HashSet<>();
        for (UserRecommendSchoolDO rec : recommendations) {
            if (rec.getAdjustmentId() != null) {
                adjustmentIds.add(rec.getAdjustmentId());
            }
            if (rec.getSchoolId() != null) {
                schoolIds.add(rec.getSchoolId());
            }
            if (rec.getCollegeId() != null) {
                collegeIds.add(rec.getCollegeId());
            }
            if (rec.getMajorId() != null) {
                majorIds.add(rec.getMajorId());
            }
        }

        // 3.0 Fetch User Preferences (to check isJoinPreference)
        Set<Long> userPreferenceAdjustmentIds = new HashSet<>();
        if (CollUtil.isNotEmpty(adjustmentIds)) {
            List<UserPreferenceDO> userPreferences = userPreferenceMapper.selectList(new LambdaQueryWrapperX<UserPreferenceDO>()
                    .eq(UserPreferenceDO::getUserId, userId)
                    .in(UserPreferenceDO::getSourceAdjustmentId, adjustmentIds));
            if (CollUtil.isNotEmpty(userPreferences)) {
                userPreferenceAdjustmentIds = userPreferences.stream()
                        .map(UserPreferenceDO::getSourceAdjustmentId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());
            }
        }

        Map<Long, AdjustmentDO> adjustmentMap = adjustmentIds.isEmpty() ? Collections.emptyMap() :
                adjustmentMapper.selectBatchIds(adjustmentIds).stream()
                        .collect(Collectors.toMap(AdjustmentDO::getId, Function.identity()));

        Map<Long, SchoolDO> schoolMap = schoolIds.isEmpty() ? Collections.emptyMap() :
                schoolMapper.selectBatchIds(schoolIds).stream()
                        .collect(Collectors.toMap(SchoolDO::getId, Function.identity()));

        int currentYear = projectConfigService.getAdjustYear() - 1;
        Map<String, AdmitScoreAgg> statsMap = new HashMap<>();
        Map<String, List<BigDecimal>> scoresMap = new HashMap<>();
        Map<String, Boolean> remarkMap = new HashMap<>();

        if (CollUtil.isNotEmpty(schoolIds)) {
            // Batch fetch scores once, then derive min/max/avg/count + median in memory.
            List<Map<String, Object>> batchScores = adjustmentAdmitMapper.selectBatchAdmitScores(
                    schoolIds, collegeIds, majorIds, currentYear);

            // Fetch National Scores for filtering
            List<NationalScoreDO> nationalScores = nationalLineEligibilityService.getNationalScoresWithFallback(currentYear);

            // Group raw scores by key
            Map<String, List<Map<String, Object>>> groupedRows = buildScoreMap(batchScores);

            // Process each group
            for (Map.Entry<String, List<Map<String, Object>>> entry : groupedRows.entrySet()) {
                String key = entry.getKey();
                List<Map<String, Object>> rows = entry.getValue();
                List<BigDecimal> validScores = new ArrayList<>();
                int belowCount = 0;
                List<BigDecimal> belowScores = new ArrayList<>();

                for (Map<String, Object> row : rows) {
                    Object scoreObj = row.get("first_score");
                    if (scoreObj == null) continue;
                    BigDecimal score = new BigDecimal(scoreObj.toString());

                    // Check National Line
                    boolean isBelow = false;
                    String area = (String) row.get("first_choice_area");
                    String majorCode = (String) row.get("major_code");

                    if (StrUtil.isNotBlank(area) && StrUtil.isNotBlank(majorCode) && CollUtil.isNotEmpty(nationalScores)) {
                        NationalScoreDO line = nationalLineEligibilityService.findMatchedNationalLine(nationalScores, area, majorCode);
                        if (line != null && line.getTotal() != null && score.intValue() < line.getTotal()) {
                            isBelow = true;
                        }
                    }
                    if (isBelow) {
                        belowCount++;
                        belowScores.add(score);
                    } else {
                        validScores.add(score);
                    }
                }
                // Decision Rule
                if (belowCount > 0) {
                    // 如果全部小于国家线(validScores为空) 或 小于国家线的数量>=3，则认定为专业课不计入总分
                    if (validScores.isEmpty() || belowCount >= 3) {
                        // Include them back (认定为专业课不计入总分)
                        validScores.addAll(belowScores);
                        // Add remark
                        remarkMap.put(key, true);
                    }
                }

                // Add to statsMap and scoresMap
                for (BigDecimal s : validScores) {
                    scoresMap.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
                    statsMap.computeIfAbsent(key, k -> new AdmitScoreAgg()).add(s);
                }
            }
        }

        // 3.2 Fetch School Ranks (for Ranking Gap)
        UserProfileDO userProfile = userProfileMapper.selectOne(new LambdaQueryWrapperX<UserProfileDO>().eq(UserProfileDO::getUserId, userId));
        Long userSchoolId = userProfile != null ? userProfile.getGraduateSchoolId() : null;

        // 1. Fetch User School Rank Score explicitly (single query)
        BigDecimal userRankScore = null;
        if (userSchoolId != null) {
            List<SchoolRankDO> userRanks = schoolRankMapper.selectList(new LambdaQueryWrapperX<SchoolRankDO>()
                    .eq(SchoolRankDO::getSchoolId, userSchoolId)
                    .orderByDesc(SchoolRankDO::getYear)
                    .last("LIMIT 1"));
            if (CollUtil.isNotEmpty(userRanks) && userRanks.get(0).getNlScore() != null) {
                userRankScore = userRanks.get(0).getNlScore();
            }
        }

        // 2. Fetch Candidate Schools Ranks
        Map<Long, BigDecimal> schoolRankMap = new HashMap<>();
        if (CollUtil.isNotEmpty(schoolIds)) {
            List<SchoolRankDO> ranks = schoolRankMapper.selectList(new LambdaQueryWrapperX<SchoolRankDO>()
                    .in(SchoolRankDO::getSchoolId, schoolIds)
                    .orderByDesc(SchoolRankDO::getYear));

            for (SchoolRankDO r : ranks) {
                if (!schoolRankMap.containsKey(r.getSchoolId()) && r.getNlScore() != null) {
                    schoolRankMap.put(r.getSchoolId(), r.getNlScore());
                }
            }
        }

        // 4. Build response
        List<AppRecommendSchoolRespVO> result = new ArrayList<>(recommendations.size());
        for (UserRecommendSchoolDO rec : recommendations) {
            AppRecommendSchoolRespVO resp = new AppRecommendSchoolRespVO();
            resp.setSchoolId(rec.getSchoolId());
            resp.setAdjustment_id(rec.getAdjustmentId());
            resp.setSchoolName(rec.getSchoolName());

            SchoolDO school = schoolMap.get(rec.getSchoolId());
            if (school != null) {
                resp.setSchoolLogo(school.getSchoolLogo());
                resp.setProvinceName(school.getProvinceName());
            }

            resp.setMajorId(rec.getMajorId());
            resp.setMajorName(rec.getMajorName());
            resp.setCollegeName(rec.getCollegeName());
            resp.setCollegeId(rec.getCollegeId());
            resp.setDirectionId(rec.getDirectionId());
            resp.setDirectionCode(rec.getDirectionCode());
            resp.setDirectionName(rec.getDirectionName());

            if (rec.getSimFinal() != null) {
                resp.setMatchProbability(rec.getSimFinal().doubleValue());
            }
            Integer category = rec.getCategory();
            if (category != null) {
                if (category == 1) {
                    resp.setProbabilityLabel("冲刺");
                    resp.setDifficultyLabel("难");
                } else if (category == 2) {
                    resp.setProbabilityLabel("稳妥");
                    resp.setDifficultyLabel("中");
                } else {
                    resp.setProbabilityLabel("保底");
                    resp.setDifficultyLabel("易");
                }
            } else {
                resp.setProbabilityLabel("未知");
                resp.setDifficultyLabel("未知");
            }

            // Ranking Gap Calculation
            BigDecimal schoolRankScore = schoolRankMap.get(rec.getSchoolId());
            if (userRankScore != null && schoolRankScore != null) {
                double diff = schoolRankScore.subtract(userRankScore).doubleValue();
                // nl_score range 0-5. diff > 1.0 means significant gap.
                if (diff > 1.0) {
                    resp.setRankingGapLabel("较大");
                } else if (diff < -1.0) {
                    resp.setRankingGapLabel("较小");
                } else {
                    resp.setRankingGapLabel("适中");
                }
            } else {
                resp.setRankingGapLabel("适中");
            }

            AdjustmentDO adj = adjustmentMap.get(rec.getAdjustmentId());
            if (adj != null) {
                resp.setMajorCode(adj.getMajorCode());
                resp.setYear(adj.getYear());
                resp.setCollegeName(adj.getCollegeName());
                resp.setStudyMode(adj.getStudyMode());
                resp.setDegreeType(adj.getDegreeType());
                resp.setPlanCount(adj.getAdjustCount());

                // Fill Stats (Min, Max, Avg, Median)
                String majorCode = adj.getMajorCode();
                if (StrUtil.isNotBlank(majorCode)) {
                    Integer studyMode = rec.getStudyMode() != null ? rec.getStudyMode() : adj.getStudyMode();
                    // Try currentYear, then currentYear-1
                    AdmitScoreAgg stats = statsMap.get(rec.getSchoolId() + "_" + rec.getCollegeId() + "_" + majorCode + "_" + studyMode + "_" + currentYear);
                    Integer statsYear = currentYear;
                    if (stats == null) {
                        stats = statsMap.get(rec.getSchoolId() + "_" + rec.getCollegeId() + "_" + majorCode + "_" + studyMode + "_" + (currentYear - 1));
                        statsYear = currentYear - 1;
                    }

                    if (stats != null) {
                        // Fill Min/Max/Avg
                        resp.setMinScore(stats.getMinScore());
                        resp.setMaxScore(stats.getMaxScore());
                        resp.setLastYearAvgScore(stats.getAvgScore());

                        // Calculate Median (memory)
                        List<BigDecimal> scores = scoresMap.get(rec.getSchoolId() + "_" + rec.getCollegeId() + "_" + majorCode + "_" + studyMode + "_" + statsYear);
                        if (CollUtil.isNotEmpty(scores)) {
                            BigDecimal median;
                            int size = scores.size();
                            if (size % 2 == 0) {
                                median = scores.get(size / 2 - 1).add(scores.get(size / 2)).divide(BigDecimal.valueOf(2));
                            } else {
                                median = scores.get(size / 2);
                            }
                            resp.setMedianScore(median);
                        }

                        // Add Remark
                        String key = rec.getSchoolId() + "_" + rec.getCollegeId() + "_" + majorCode + "_" + studyMode + "_" + statsYear;
                        if (Boolean.TRUE.equals(remarkMap.get(key))) {
                            resp.setRemark("该院校复试时一志愿专业课不计入总分，请谨慎参考");
                        }
                    }
                }
            }
            // Check if joined preference
            if (rec.getAdjustmentId() != null && userPreferenceAdjustmentIds.contains(rec.getAdjustmentId())) {
                resp.setIsJoinPreference(true);
            } else {
                resp.setIsJoinPreference(false);
            }

            result.add(resp);
        }

        return new PageResult<>(result, pageResult.getTotal());
    }

    private Map<String, List<Map<String, Object>>> buildScoreMap(List<Map<String, Object>> batchScores) {
        Map<String, List<Map<String, Object>>> groupedRows = new HashMap<>();
        for (Map<String, Object> row : batchScores) {
            String key = row.get("school_id") + "_" + row.get("college_id") + "_" + row.get("major_code") + "_" + row.get("study_mode") + "_" + row.get("year");
            groupedRows.computeIfAbsent(key, k -> new ArrayList<>()).add(row);
        }
        return groupedRows;
    }

    private static class AdmitScoreAgg {

        private BigDecimal minScore;
        private BigDecimal maxScore;
        private BigDecimal sumScore = BigDecimal.ZERO;
        private int count;

        void add(BigDecimal score) {
            if (score == null) {
                return;
            }
            if (minScore == null || score.compareTo(minScore) < 0) {
                minScore = score;
            }
            if (maxScore == null || score.compareTo(maxScore) > 0) {
                maxScore = score;
            }
            sumScore = sumScore.add(score);
            count++;
        }

        BigDecimal getMinScore() {
            return minScore;
        }

        BigDecimal getMaxScore() {
            return maxScore;
        }

        BigDecimal getAvgScore() {
            if (count <= 0) {
                return null;
            }
            return sumScore.divide(BigDecimal.valueOf(count), 2, java.math.RoundingMode.HALF_UP);
        }
    }

    @Override
    public void generateRecommend(Long userId, Long reportId) {
        // 1. 获取用户信息
        UserProfileDO userProfile = userProfileMapper.selectOne(new LambdaQueryWrapper<UserProfileDO>()
                .eq(UserProfileDO::getUserId, userId));
        if (userProfile == null) {
            log.warn("用户画像不存在，无法推荐: userId={}", userId);
            return;
        }
        // 获取用户意向信息
        UserIntentionDO userIntention = userIntentionMapper.selectOne(new LambdaQueryWrapper<UserIntentionDO>()
                .eq(UserIntentionDO::getUserId, userId));

        if (Objects.isNull(userIntention)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.INTENT_NO_FOUND);
        }
        // 预加载基础数据
        List<SchoolDO> allSchools = schoolService.getSchoolList();
        Map<Long, SchoolDO> schoolMap = allSchools.stream()
                .collect(Collectors.toMap(SchoolDO::getId, Function.identity()));
        Integer currentYear = projectConfigService.getAdjustYear();
        // 4. 加载并过滤学校
        // 预加载软科排名 (用于协同过滤)
        List<SchoolRankDO> allRanks = schoolRankService.getSchoolRankList();
        Map<Long, Double> schoolRankIdMap = new HashMap<>();
        Map<Long, Double> schoolRankSchoolIdMap = new HashMap<>();
        for (SchoolRankDO rank : allRanks) {
            // 优先使用数据库已计算好的归一化分数
            double normalized;
            if (rank.getNlScore() != null) {
                normalized = rank.getNlScore().doubleValue();
            } else {
                continue; // 无分数则跳过
            }
            schoolRankIdMap.put(rank.getId(), normalized);
            if (rank.getSchoolId() != null) {
                schoolRankSchoolIdMap.put(rank.getSchoolId(), normalized);
            }
        }

        // 预加载所有学校分数线 (自划线)：优先当前年，无则回退到上一年
        Integer schoolScoreYear = currentYear;
        List<SchoolScoreDO> allSchoolScores = schoolScoreService.getSchoolScoreList(schoolScoreYear);
        if (CollUtil.isEmpty(allSchoolScores)) {
            schoolScoreYear = schoolScoreYear - 1;
            allSchoolScores = schoolScoreService.getSchoolScoreList(schoolScoreYear);
        }
        // Map<SchoolId, List<SchoolScoreDO>> 记录具体分数线 (用于精筛)
        Map<Long, List<SchoolScoreDO>> schoolMajorScoreMap = new HashMap<>();
        for (SchoolScoreDO score : allSchoolScores) {
            schoolMajorScoreMap.computeIfAbsent(score.getSchoolId(), k -> new ArrayList<>())
                    .add(score);
        }
        NationalLineContext nationalLineContext = nationalLineEligibilityService.resolveContextOrThrow(userId, currentYear);
        // 获取匹配的国家线
        NationalScoreDO matchedLine = nationalLineContext.getMatchedLine();
        double nationalLineTotal = matchedLine.getTotal().doubleValue();

        // 4.0 获取算法参数 (基于 TargetMajorCode)
        RecommendRuleDO rule = fetchRecommendRule(userProfile.getTargetMajorCode());

        double userScoreB = calculateUserScoreB(userProfile, schoolRankIdMap, nationalLineTotal, rule);
        double baseBonusC0 = 5;

        Set<Long> candidateSchoolIds = new HashSet<>();

        double filterMin = rule.getFilterMin() != null ? rule.getFilterMin().doubleValue() : 0.75;
        double filterMax = rule.getFilterMax() != null ? rule.getFilterMax().doubleValue() : 1.5;

        double maxC = calculateUserBonusC(userProfile, userIntention, baseBonusC0, schoolRankSchoolIdMap, rule);
        maxC = Math.min(maxC, 5);
        double totalUserScore = userScoreB * 0.8 + maxC * 0.2;

        for (SchoolDO school : allSchools) {
            Long schoolId = school.getId();
            // 4.1 自划线粗筛 (School Level)
            // 必须满足该校对用户一志愿专业的自划线要求（总分 + 单科）
            List<SchoolScoreDO> majorScores = schoolMajorScoreMap.get(schoolId);
            if (CollUtil.isNotEmpty(majorScores)) {
                SchoolScoreDO match = findBestMatchScore(majorScores, userProfile.getTargetMajorCode(), userProfile.getTargetDegreeType());
                if (match != null) {
                    if (!checkSchoolScore(userProfile, match)) {
                        continue;
                    }
                }
            }
            // 4.2 院校协同过滤
            // 修正：学校分数应在用户分数的合理浮动范围内 [min, max]
            double schoolScoreA = getRankScore(schoolId, schoolRankSchoolIdMap);
            if (schoolScoreA >= totalUserScore * filterMin && schoolScoreA <= totalUserScore * filterMax) {
                candidateSchoolIds.add(schoolId);
            }
        }

        if (candidateSchoolIds.isEmpty()) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.NO_MATCHING_SCHOOLS);
        }

        Set<String> xuekemenlei = vipBenefitService.getConsumedUniqueKeys(userId, BENEFIT_KEY_MAJOR_CATEGORY_OPEN);

        // 5. 获取候选学校的调剂信息（同时按已开通学科门类过滤，majorCode 前两位匹配）
        String xuekemenleiInSql = CollUtil.isNotEmpty(xuekemenlei)
                ? "LEFT(major_code, 2) IN (" + xuekemenlei.stream().map(s -> "'" + s + "'").collect(Collectors.joining(",")) + ")"
                : null;
        List<AdjustmentDO> adjustments = adjustmentMapper.selectList(new LambdaQueryWrapper<AdjustmentDO>()
                .in(AdjustmentDO::getSchoolId, candidateSchoolIds)
                .eq(AdjustmentDO::getYear, currentYear - 1)
                .apply(xuekemenleiInSql != null, xuekemenleiInSql));

        if (CollUtil.isEmpty(adjustments)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.NO_MATCHING_SCHOOLS_ADJUSTS);
        }

        // --- Step 3.1: 基于用户意向(UserIntention) 过滤调剂信息 ---
        adjustments = filterAdjustmentsByIntention(adjustments, userIntention, schoolMap);

        if (CollUtil.isEmpty(adjustments)) {
            // 过滤后无合适调剂，暂不报错，直接返回空或抛异常视业务而定，这里抛异常提示
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.NO_MATCHING_SCHOOLS_ADJUSTS);
        }

        // --- Step 4: 院校专业匹配度 (SimFinal) ---
        List<UserRecommendSchoolDO> recommendations = new ArrayList<>();
        // 记录“无录取均分”的推荐项，用于同分类内排序时置后
        Set<Long> nonScoreAggAdjustmentIds = new HashSet<>();

        // 批量预加载调剂录取分数统计，避免循环内逐条查库
        Map<String, AdmitScoreAgg> statsMap = new HashMap<>();
        Set<Long> adjustmentSchoolIds = adjustments.stream()
                .map(AdjustmentDO::getSchoolId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Set<Long> adjustmentCollegeIds = adjustments.stream()
                .map(AdjustmentDO::getCollegeId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Set<Long> adjustmentMajorIds = adjustments.stream()
                .map(AdjustmentDO::getMajorId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (!adjustmentSchoolIds.isEmpty()) {
            // 先拉取明细，再按“低于国家线人数”规则决定是否剔除后计算均值
            List<Map<String, Object>> batchRows = adjustmentAdmitMapper.selectBatchAdmitScores(
                    adjustmentSchoolIds, adjustmentCollegeIds, adjustmentMajorIds, currentYear - 1);
            List<NationalScoreDO> nationalScores = nationalLineEligibilityService.getNationalScoresWithFallback(currentYear - 1);

            Map<String, List<Map<String, Object>>> groupedRows = buildScoreMap(batchRows);

            // Process each group
            for (Map.Entry<String, List<Map<String, Object>>> entry : groupedRows.entrySet()) {
                String key = entry.getKey();
                List<Map<String, Object>> rows = entry.getValue();
                List<BigDecimal> validScores = new ArrayList<>();
                int belowCount = 0;
                List<BigDecimal> belowScores = new ArrayList<>();

                for (Map<String, Object> row : rows) {
                    Object scoreObj = row.get("first_score");
                    if (scoreObj == null) continue;
                    BigDecimal score = new BigDecimal(scoreObj.toString());

                    // Check National Line
                    boolean isBelow = false;
                    String area = (String) row.get("first_choice_area");
                    String majorCode = (String) row.get("major_code");

                    if (StrUtil.isNotBlank(area) && StrUtil.isNotBlank(majorCode) && CollUtil.isNotEmpty(nationalScores)) {
                        NationalScoreDO line = nationalLineEligibilityService.findMatchedNationalLine(nationalScores, area, majorCode);
                        if (line != null && line.getTotal() != null && score.intValue() < line.getTotal()) {
                            isBelow = true;
                        }
                    }
                    if (isBelow) {
                        belowCount++;
                        belowScores.add(score);
                    } else {
                        validScores.add(score);
                    }
                }
                // Decision Rule
                if (belowCount > 0) {
                    // 如果全部小于国家线(validScores为空) 或 小于国家线的数量>=3，则认定为专业课不计入总分
                    if (validScores.isEmpty() || belowCount >= 3) {
                        // Include them back (认定为专业课不计入总分)
                        validScores.addAll(belowScores);
                        // Add remark
                    }
                }
                // Add to statsMap and scoresMap
                for (BigDecimal s : validScores) {
                    statsMap.computeIfAbsent(key, k -> new AdmitScoreAgg()).add(s);
                }
            }
        }

        double weightSimA = rule.getWeightSimA() != null ? rule.getWeightSimA().doubleValue() : 0.5;
        double weightSimB = rule.getWeightSimB() != null ? rule.getWeightSimB().doubleValue() : 0.3;
        double weightSimC = rule.getWeightSimC() != null ? rule.getWeightSimC().doubleValue() : 0.2;
        double catThreshold1 = rule.getCatThreshold1() != null ? rule.getCatThreshold1().doubleValue() : 0.4;
        double catThreshold2 = rule.getCatThreshold2() != null ? rule.getCatThreshold2().doubleValue() : 0.8;
        boolean isUndergraduate985 = false;
        Double graduateSchoolScoreA = null;
        if (userProfile.getGraduateSchoolId() != null) {
            SchoolDO graduateSchool = schoolMap.get(userProfile.getGraduateSchoolId());
            isUndergraduate985 = graduateSchool != null && Boolean.TRUE.equals(graduateSchool.getIs985());
            graduateSchoolScoreA = getRankScore(userProfile.getGraduateSchoolId(), schoolRankIdMap);
        }

        for (AdjustmentDO adjustment : adjustments) {
            SchoolDO school = schoolMap.get(adjustment.getSchoolId());
            if (school == null) {
                continue;
            }

            boolean isNonScoreAgg = false;
            String key = adjustment.getSchoolId() + "_" + adjustment.getCollegeId() + "_" + adjustment.getMajorCode() + "_" + adjustment.getStudyMode() + "_" + (currentYear - 1);
            AdmitScoreAgg agg = statsMap.get(key);
            if (Objects.isNull(agg) || Objects.isNull(agg.getAvgScore())) {
                isNonScoreAgg = true;
                if (adjustment.getId() != null) {
                    nonScoreAggAdjustmentIds.add(adjustment.getId());
                }
            }
            // Sim Calculations
            double simA = calculateSimAInMemory(userProfile, rule, agg);
            double simB = calculateSimB(userProfile, adjustment, rule);
            if (simA == 0 || simB == 0) {
                continue;
            }
            double simC = calculateSimC(userProfile, adjustment);
            double simFinal = weightSimA * simA + weightSimB * simB + weightSimC * simC;

            // 分档
            int category;
            if (simFinal <= catThreshold1) {
                category = 1;
            } else if (simFinal <= catThreshold2) {
                category = 2;
            } else {
                category = 3;
            }
            // 本科985：目标院校是A区985 => 冲刺；本科非985：目标院校是A区985/A区211 => 冲刺。
            boolean isAZone985 = Boolean.TRUE.equals(school.getIs985()) && "A".equalsIgnoreCase(school.getProvinceArea());
            boolean isAZone211 = Boolean.TRUE.equals(school.getIs211()) && "A".equalsIgnoreCase(school.getProvinceArea());
            if (isUndergraduate985) {
                if (isAZone985) {
                    category = 1;
                }
            } else if (isAZone985 || isAZone211) {
                category = 1;
            }

            // 如果没有录取平均分，则判断 目标院校的排名分 - 用户的本科院校排名分  ，如果在 0.85 -1.1 之间是稳，低于 0.85 是保底，高于 1.1 是冲刺
            if (isNonScoreAgg && graduateSchoolScoreA != null && graduateSchoolScoreA > 0) {
                double targetSchoolScoreA = getRankScore(school.getId(), schoolRankSchoolIdMap);
                double rankRatio = targetSchoolScoreA / graduateSchoolScoreA;
                if (rankRatio < 0.85) {
                    category = 3; // 保底
                } else if (rankRatio <= 1.1) {
                    category = 2; // 稳妥
                } else {
                    category = 1; // 冲刺
                }
            }

            UserRecommendSchoolDO recommendDO = UserRecommendSchoolDO.builder()
                    .userId(userId)
                    .reportId(reportId)
                    .adjustmentId(adjustment.getId())
                    .schoolId(school.getId())
                    .schoolName(school.getSchoolName())
                    .collegeId(adjustment.getCollegeId())
                    .collegeName(adjustment.getCollegeName())
                    .majorId(adjustment.getMajorId())
                    .majorName(adjustment.getMajorName())
                    .majorCode(adjustment.getMajorCode())
                    .directionId(adjustment.getDirectionId())
                    .directionCode(adjustment.getDirectionCode())
                    .directionName(adjustment.getDirectionName())
                    .studyMode(adjustment.getStudyMode())
                    .simFinal(BigDecimal.valueOf(simFinal))
                    .simA(BigDecimal.valueOf(simA))
                    .simB(BigDecimal.valueOf(simB))
                    .simC(BigDecimal.valueOf(simC))
                    .userScoreB(BigDecimal.valueOf(userScoreB))
                    .maxC(BigDecimal.valueOf(maxC))
                    .schoolScoreA(BigDecimal.valueOf(getRankScore(school.getId(), schoolRankSchoolIdMap)))
                    .category(category)
                    .build();
            recommendations.add(recommendDO);
        }

        // 5. 保存结果
        if (CollUtil.isNotEmpty(recommendations)) {
            // 每个分档按 simFinal 降序，仅保留 Top100 入库
            List<UserRecommendSchoolDO> limitedRecommendations = new ArrayList<>();
            for (int category = 1; category <= 3; category++) {
                int finalCategory = category;
                List<UserRecommendSchoolDO> categoryList = recommendations.stream()
                        .filter(item -> Objects.equals(item.getCategory(), finalCategory))
                        .sorted((o1, o2) -> {
                            boolean o1NoScore = o1.getAdjustmentId() != null && nonScoreAggAdjustmentIds.contains(o1.getAdjustmentId());
                            boolean o2NoScore = o2.getAdjustmentId() != null && nonScoreAggAdjustmentIds.contains(o2.getAdjustmentId());
                            if (o1NoScore != o2NoScore) {
                                return o1NoScore ? 1 : -1; // 无录取均分的排在分类后面
                            }
                            return o2.getSimFinal().compareTo(o1.getSimFinal());
                        })
                        .limit(100)
                        .toList();
                limitedRecommendations.addAll(categoryList);
            }
            userRecommendSchoolMapper.insertBatch(limitedRecommendations);
        }
    }

    @Override
    @Async
    public void generateAssessmentReport(Long userId, Long reportId) {
        try {
            // 1. Load user profile + intention
            UserProfileDO userProfile = userProfileMapper.selectOne(new LambdaQueryWrapper<UserProfileDO>()
                    .eq(UserProfileDO::getUserId, userId));
            if (userProfile == null) {
                log.warn("用户画像不存在，无法生成报告: userId={}", userId);
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_PROFILE_NOT_EXISTS);
            }

            UserIntentionDO userIntention = userIntentionMapper.selectOne(new LambdaQueryWrapper<UserIntentionDO>()
                    .eq(UserIntentionDO::getUserId, userId));

            int currentYear = projectConfigService.getAdjustYear();

            // Graduate school rank (Ruanke)
            SchoolRankDO ruanke = null;
            if (userProfile.getGraduateSchoolId() != null) {
                ruanke = schoolRankMapper.selectById(userProfile.getGraduateSchoolId());
            }
            if (ruanke == null && StrUtil.isNotBlank(userProfile.getGraduateSchoolName())) {
                ruanke = schoolRankMapper.selectLatestBySchoolName(userProfile.getGraduateSchoolName());
            }

            Set<Long> neededSchoolIds = new HashSet<>();
            if (userProfile.getTargetSchoolId() != null) {
                neededSchoolIds.add(userProfile.getTargetSchoolId());
            }

            if (Objects.nonNull(ruanke) && Objects.nonNull(ruanke.getSchoolId())) {
                neededSchoolIds.add(ruanke.getSchoolId());
            }

            List<SchoolDO> schools = neededSchoolIds.isEmpty()
                    ? Collections.emptyList()
                    : schoolMapper.selectBatchIds(neededSchoolIds);
            Map<Long, SchoolDO> schoolMap = schools.stream()
                    .filter(s -> s.getId() != null)
                    .collect(Collectors.toMap(SchoolDO::getId, Function.identity(), (a, b) -> a));

            NationalLineContext nationalLineContext = nationalLineEligibilityService
                    .resolveContextOrThrow(userId, currentYear);
            String firstChoiceArea = nationalLineContext.getFirstChoiceArea();
            NationalScoreDO matchedLine = nationalLineContext.getMatchedLineA() != null ? nationalLineContext.getMatchedLineA() : nationalLineContext.getMatchedLineB();

            // Intention majors
            List<Long> intentionMajorIds = parseJsonLongList(userIntention != null ? userIntention.getMajorIds() : null);
            Map<Long, String> intentionMajorNameMap = new LinkedHashMap<>();
            if (CollUtil.isNotEmpty(intentionMajorIds)) {
                majorMapper.selectBatchIds(intentionMajorIds).forEach(m -> intentionMajorNameMap.put(m.getId(), m.getName()));
            }
            // Supply-side: count open adjustments by majorId (best-effort)
            Map<Long, Long> openAdjustmentCountByMajorId = new LinkedHashMap<>();
            if (CollUtil.isNotEmpty(intentionMajorIds)) {
                List<AdjustmentDO> rows = adjustmentMapper.selectList(new LambdaQueryWrapper<AdjustmentDO>()
                        .select(AdjustmentDO::getId, AdjustmentDO::getMajorId)
                        .eq(AdjustmentDO::getYear, currentYear - 1)
                        .in(AdjustmentDO::getMajorId, intentionMajorIds));
                Map<Long, Long> grouped = rows.stream()
                        .filter(r -> r.getMajorId() != null)
                        .collect(Collectors.groupingBy(AdjustmentDO::getMajorId, LinkedHashMap::new, Collectors.counting()));
                openAdjustmentCountByMajorId.putAll(grouped);
            }

            // 3. Ask AI to generate 5-dimension report JSON
            String prompt = buildAssessmentPrompt(currentYear, userProfile, userIntention, firstChoiceArea, matchedLine, ruanke,
                    schoolMap, intentionMajorNameMap, openAdjustmentCountByMajorId);

            AiTextResult aiResult = aiTextService.generateText(AiTextRequest.builder()
                    .provider("doubao")
                    .prompt(prompt)
                    .timeoutMs(60_000L)
                    .build());

            StudentAssessmentAiReport aiReport = parseAssessmentAiJson(Optional.of(aiResult).get().getText());

            // 4. Persist result
            UserCustomReportDO toUpdate = new UserCustomReportDO();
            toUpdate.setId(reportId);
            toUpdate.setUserId(userId);
            toUpdate.setReportVersion(String.valueOf(System.currentTimeMillis()));
            toUpdate.setSourceProfileId(userProfile.getId());
            toUpdate.setSourceIntentionId(userIntention != null ? userIntention.getId() : null);
            toUpdate.setSourceProfileJson(JSONUtil.toJsonStr(userProfile));
            toUpdate.setSourceIntentionJson(userIntention != null ? JSONUtil.toJsonStr(userIntention) : null);

            if (aiReport != null) {
                toUpdate.setDimBackgroundScore(aiReport.getDimBackgroundScore());
                toUpdate.setDimTotalScore(aiReport.getDimTotalScore());
                toUpdate.setDimTargetSchoolLevelScore(aiReport.getDimTargetSchoolLevelScore());
                toUpdate.setDimMajorCompetitivenessScore(aiReport.getDimMajorCompetitivenessScore());
                toUpdate.setDimSoftSkillsScore(aiReport.getDimSoftSkillsScore());
                toUpdate.setAnalysisBackground(aiReport.getAnalysisBackground());
                toUpdate.setAnalysisTotal(aiReport.getAnalysisTotal());
                toUpdate.setAnalysisTargetSchoolLevel(aiReport.getAnalysisTargetSchoolLevel());
                toUpdate.setAnalysisMajorCompetitiveness(aiReport.getAnalysisMajorCompetitiveness());
                toUpdate.setAnalysisSoftSkills(aiReport.getAnalysisSoftSkills());
            }
            toUpdate.setGenerateStatus(1); // 1-已完成
            userCustomReportMapper.updateById(toUpdate);
            generateRecommend(userId, reportId);
            // 5. Consume quota after success
            vipBenefitService.consumeQuotaOrThrow(userId, BENEFIT_KEY_USER_REPORT, 1,
                REF_TYPE_CUSTOM_REPORT, String.valueOf(reportId), null);
        } catch (Exception e) {
            log.error("异步生成报告失败: userId={}, reportId={}", userId, reportId, e);
            // 生成失败也标记为已完成，避免前端一直等待（可根据业务需要调整为"失败"状态）
            userCustomReportService.updateGenerateStatus(reportId, 1);
            throw e;
        }
    }

    // --- Helper Methods ---

    private List<Long> parseJsonLongList(String json) {
        if (StrUtil.isBlank(json)) {
            return Collections.emptyList();
        }
        try {
            return JSONUtil.parseArray(json).toList(Long.class);
        } catch (Exception e) {
            log.warn("parseJsonLongList error {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private String buildAssessmentPrompt(Integer year,
                                         UserProfileDO profile,
                                         UserIntentionDO intention,
                                         String nationalArea,
                                         NationalScoreDO nationalLine,
                                         SchoolRankDO ruankeRank,
                                         Map<Long, SchoolDO> schoolMap,
                                         Map<Long, String> intentionMajorNameMap,
                                         Map<Long, Long> openAdjustmentCountByMajorId) {
        SchoolDO gradSchool = ruankeRank.getSchoolId() != null ? schoolMap.get(ruankeRank.getSchoolId()) : null;
        SchoolDO targetSchool = profile.getTargetSchoolId() != null ? schoolMap.get(profile.getTargetSchoolId()) : null;

        Integer scoreTotal = profile.getScoreTotal() != null ? profile.getScoreTotal().intValue() : null;
        Integer nationalTotal = (nationalLine != null && nationalLine.getTotal() != null)
                ? nationalLine.getTotal().intValue()
                : null;
        Integer delta = (scoreTotal != null && nationalTotal != null) ? (scoreTotal - nationalTotal) : null;

        Map<String, Object> input = new LinkedHashMap<>();
        input.put("数据年份", year);
        // NOTE: Map.of does not allow null values, use LinkedHashMap for DB-driven payloads.
        Map<String, Object> graduateSchool = new LinkedHashMap<>();
        graduateSchool.put("id", profile.getGraduateSchoolId());
        graduateSchool.put("本科学校名称", StrUtil.blankToDefault(profile.getGraduateSchoolName(), gradSchool != null ? gradSchool.getSchoolName() : null));
        graduateSchool.put("是否985", gradSchool != null ? gradSchool.getIs985() : null);
        graduateSchool.put("是否211", gradSchool != null ? gradSchool.getIs211() : null);
        graduateSchool.put("是否双一流", gradSchool != null ? gradSchool.getIsSyl() : null);
        input.put("本科学校信息", graduateSchool);

        Map<String, Object> ruankeRankMap = new LinkedHashMap<>();
        ruankeRankMap.put("数据年份", ruankeRank.getYear());
        ruankeRankMap.put("学校排名", ruankeRank.getRanking());
        ruankeRankMap.put("学校排名分数", ruankeRank.getScore());
        input.put("本科大学全国排名情况", ruankeRankMap);

        Map<String, Object> firstScore = new LinkedHashMap<>();
        firstScore.put("初试总分", scoreTotal);
        firstScore.put("报考区域", nationalArea);
        firstScore.put("一志愿报考专业Code", profile.getTargetMajorCode());
        firstScore.put("一志愿报考专业名称", profile.getTargetMajorName());
        firstScore.put("一志愿报考学位类型", degreeTypeName(profile.getTargetDegreeType()));
        firstScore.put("国家线", nationalTotal);
        firstScore.put("与国家线差距", delta);
        input.put("一志愿报考信息", firstScore);

        Map<String, Object> targetSchoolMap = new LinkedHashMap<>();
        targetSchoolMap.put("学校名称",
                StrUtil.blankToDefault(profile.getTargetSchoolName(), targetSchool != null ? targetSchool.getSchoolName() : null));
        targetSchoolMap.put("是否985", targetSchool != null ? targetSchool.getIs985() : null);
        targetSchoolMap.put("是否211", targetSchool != null ? targetSchool.getIs211() : null);
        targetSchoolMap.put("是否双一流", targetSchool != null ? targetSchool.getIsSyl() : null);
        input.put("一志愿报考学校信息", targetSchoolMap);
        if (intention == null) {
            input.put("调剂意向信息", null);
        } else {
            Map<String, Object> intentionMap = new LinkedHashMap<>();
            List<String> schoolLevels = StrUtil.isNotBlank(intention.getSchoolLevel())
                    ? JSONUtil.toList(intention.getSchoolLevel(), String.class)
                    : Collections.emptyList();
            intentionMap.put("意向院校层次", schoolLevelNames(schoolLevels));
            intentionMap.put("意向学习方式", studyModeName(intention.getStudyMode()));
            intentionMap.put("意向学位类型", degreeTypeName(intention.getDegreeType()));
            intentionMap.put("是否跨考", intention.getIsAcceptCrossMajor());
            intentionMap.put("是否愿意调回本专业", intention.getIsAcceptCrossExam());
            input.put("调剂意向信息", intentionMap);
        }
        input.put("调剂意向专业信息", intentionMajorNameMap);
        input.put("调剂意向专业调剂数量", openAdjustmentCountByMajorId);
        Map<String, Object> softSkills = getStringObjectMap(profile);
        input.put("软实力", softSkills);

        String inputJson;
        try {
            inputJson = objectMapper.writeValueAsString(input);
        } catch (Exception e) {
            inputJson = "{}";
        }

        return """
                你是一名资深考研调剂专家，请基于输入数据，按照以下维度对学生进行精准评估。
                               \s
                ### 评估维度与逻辑
                               \s
                1. **院校背景 (dimBackgroundScore)**：综合软科排名、985/211/双一流标签，评估本科出身对调剂院校筛选的加持力。
                2. **初试成绩 (dimTotalScore)**：通过初试总分-国家线衡量分数，评估其在调剂池中的排位竞争力。
                3. **目标院校层次 (dimTargetSchoolLevelScore)**：结合一志愿与意向调剂学校的层次差异，评估“降级调剂”的可行性与溢价利用率。
                4. **专业竞争力 (dimMajorCompetitivenessScore)**：结合调剂意向专业调剂数量与该专业报考热度评估调剂难度。
                5. **软实力 (dimSoftSkillsScore)**：整合英语、科研、竞赛、GPA等亮点，评估复试面试中的胜算。
                               \s
                ### 输出约束
                               \s
                * **格式**：只输出一个 JSON 对象，**严禁使用 Markdown 代码块包裹**，严禁任何额外解释。
                * **分数**：0-100 的整数。
                * **评价语**：`analysis` 开头的字段必须使用中文，**每个字段的文案严禁超过 200 个汉字**（含标点），文词扮演专家的方式。
                * **兜底**：若数据缺失，基于常识给出稳健的中性评价，但不要直接说数据缺失。
                               \s
                ### JSON Schema (严格遵守字段名)
                               \s
                {
                "dimBackgroundScore": 0,
                "analysisBackground": "",
                "dimTotalScore": 0,
                "analysisTotal": "",
                "dimTargetSchoolLevelScore": 0,
                "analysisTargetSchoolLevel": "",
                "dimMajorCompetitivenessScore": 0,
                "analysisMajorCompetitiveness": "",
                "dimSoftSkillsScore": 0,
                "analysisSoftSkills": ""
                }
                               \s
                ### 输入数据 (JSON)
                %s
                    \s""".formatted(inputJson);
    }

    private static Map<String, Object> getStringObjectMap(UserProfileDO profile) {
        Map<String, Object> softSkills = new LinkedHashMap<>();
        softSkills.put("四级分数", profile.getCet4Score());
        softSkills.put("六级分数", profile.getCet6Score());
        softSkills.put("二作及以上论文数量", profile.getPaperCount());
        softSkills.put("省二级及以上竞赛获奖次数", profile.getCompetitionCount());
        softSkills.put("是否获得国家奖学金", profile.getIsNationalScholarship() ? "是" : "否");
        softSkills.put("是否获得学校级以上奖金", profile.getIsSchoolScholarship() ? "是" : "否");
        softSkills.put("本科平均分", profile.getGraduateAverageScore());
        softSkills.put("自评分（0-10）", profile.getSelfAssessedScore());
        softSkills.put("自我评价或介绍", profile.getSelfIntroduction());
        return softSkills;
    }

    /**
     * 获取推荐规则参数
     */
    private RecommendRuleDO fetchRecommendRule(String majorCode) {
        RecommendRuleDO rule = null;
        if (StrUtil.isNotBlank(majorCode)) {
            // 尝试按专业代码在 majorCodes 列表中匹配
            rule = recommendRuleMapper.selectOne(new LambdaQueryWrapperX<RecommendRuleDO>()
                    .apply("FIND_IN_SET({0}, major_codes) > 0", majorCode)
                    .last("LIMIT 1"));
        }

        if (rule == null) {
            // 尝试查询默认兜底配置
            rule = recommendRuleMapper.selectOne(new LambdaQueryWrapperX<RecommendRuleDO>()
                    .apply("FIND_IN_SET('000000', major_codes) > 0")
                    .last("LIMIT 1"));
        }
        if (rule == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.NO_RECOMMEND_RULE);
        }
        return rule;
    }

    private String studyModeName(Integer studyMode) {
        if (studyMode == null) {
            return null;
        }
        return switch (studyMode) {
            case 0 -> "不限";
            case 1 -> "全日制";
            case 2 -> "非全日制";
            default -> "未知";
        };
    }

    private String degreeTypeName(Integer degreeType) {
        if (degreeType == null) {
            return null;
        }
        return switch (degreeType) {
            case 0 -> "不限/不区分";
            case 1 -> "学硕";
            case 2 -> "专硕";
            default -> "未知";
        };
    }

    private String schoolLevelName(String schoolLevel) {
        if (StrUtil.isBlank(schoolLevel)) {
            return null;
        }
        return switch (schoolLevel) {
            case "985" -> "985";
            case "211" -> "211";
            case "syl" -> "双一流";
            case "ordinary" -> "普通院校";
            default -> "未知";
        };
    }

    private String schoolLevelNames(List<String> schoolLevels) {
        if (CollUtil.isEmpty(schoolLevels)) {
            return null;
        }
        return schoolLevels.stream()
                .map(this::schoolLevelName)
                .filter(StrUtil::isNotBlank)
                .distinct()
                .collect(Collectors.joining("、"));
    }

    private String adjustPriorityName(Integer adjustPriority) {
        if (adjustPriority == null) {
            return null;
        }
        return switch (adjustPriority) {
            case 1 -> "优先院校层次";
            case 2 -> "优先专业匹配度";
            default -> "未知";
        };
    }

    private StudentAssessmentAiReport parseAssessmentAiJson(String text) {
        if (StrUtil.isBlank(text)) {
            return null;
        }
        String json = extractFirstJsonObject(text);
        if (StrUtil.isBlank(json)) {
            return null;
        }
        try {
            JsonNode root = objectMapper.readTree(json);
            StudentAssessmentAiReport r = new StudentAssessmentAiReport();
            r.setDimBackgroundScore(intOrNull(root.get("dimBackgroundScore")));
            r.setAnalysisBackground(textOrNull(root.get("analysisBackground")));
            r.setDimTotalScore(intOrNull(root.get("dimTotalScore")));
            r.setAnalysisTotal(textOrNull(root.get("analysisTotal")));
            r.setDimTargetSchoolLevelScore(intOrNull(root.get("dimTargetSchoolLevelScore")));
            r.setAnalysisTargetSchoolLevel(textOrNull(root.get("analysisTargetSchoolLevel")));
            r.setDimMajorCompetitivenessScore(intOrNull(root.get("dimMajorCompetitivenessScore")));
            r.setAnalysisMajorCompetitiveness(textOrNull(root.get("analysisMajorCompetitiveness")));
            r.setDimSoftSkillsScore(intOrNull(root.get("dimSoftSkillsScore")));
            r.setAnalysisSoftSkills(textOrNull(root.get("analysisSoftSkills")));
            return r;
        } catch (Exception e) {
            log.warn("AI 报告 JSON 解析失败: {}", e.getMessage());
            return null;
        }
    }

    private String extractFirstJsonObject(String text) {
        int start = text.indexOf('{');
        int end = text.lastIndexOf('}');
        if (start < 0 || end < 0 || end <= start) {
            return null;
        }
        return text.substring(start, end + 1).trim();
    }

    private String textOrNull(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (node.isTextual()) {
            return node.asText();
        }
        return node.toString();
    }

    private Integer intOrNull(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (node.isInt() || node.isLong()) {
            return node.asInt();
        }
        if (node.isTextual()) {
            try {
                return Integer.parseInt(node.asText().trim());
            } catch (Exception ignore) {
                return null;
            }
        }
        return null;
    }

    private static class StudentAssessmentAiReport {

        private Integer dimBackgroundScore;
        private String analysisBackground;
        private Integer dimTotalScore;
        private String analysisTotal;
        private Integer dimTargetSchoolLevelScore;
        private String analysisTargetSchoolLevel;
        private Integer dimMajorCompetitivenessScore;
        private String analysisMajorCompetitiveness;
        private Integer dimSoftSkillsScore;
        private String analysisSoftSkills;

        public Integer getDimBackgroundScore() {
            return dimBackgroundScore;
        }

        public void setDimBackgroundScore(Integer v) {
            this.dimBackgroundScore = v;
        }

        public String getAnalysisBackground() {
            return analysisBackground;
        }

        public void setAnalysisBackground(String v) {
            this.analysisBackground = v;
        }

        public Integer getDimTotalScore() {
            return dimTotalScore;
        }

        public void setDimTotalScore(Integer v) {
            this.dimTotalScore = v;
        }

        public String getAnalysisTotal() {
            return analysisTotal;
        }

        public void setAnalysisTotal(String v) {
            this.analysisTotal = v;
        }

        public Integer getDimTargetSchoolLevelScore() {
            return dimTargetSchoolLevelScore;
        }

        public void setDimTargetSchoolLevelScore(Integer v) {
            this.dimTargetSchoolLevelScore = v;
        }

        public String getAnalysisTargetSchoolLevel() {
            return analysisTargetSchoolLevel;
        }

        public void setAnalysisTargetSchoolLevel(String v) {
            this.analysisTargetSchoolLevel = v;
        }

        public Integer getDimMajorCompetitivenessScore() {
            return dimMajorCompetitivenessScore;
        }

        public void setDimMajorCompetitivenessScore(Integer v) {
            this.dimMajorCompetitivenessScore = v;
        }

        public String getAnalysisMajorCompetitiveness() {
            return analysisMajorCompetitiveness;
        }

        public void setAnalysisMajorCompetitiveness(String v) {
            this.analysisMajorCompetitiveness = v;
        }

        public Integer getDimSoftSkillsScore() {
            return dimSoftSkillsScore;
        }

        public void setDimSoftSkillsScore(Integer v) {
            this.dimSoftSkillsScore = v;
        }

        public String getAnalysisSoftSkills() {
            return analysisSoftSkills;
        }

        public void setAnalysisSoftSkills(String v) {
            this.analysisSoftSkills = v;
        }
    }

    /**
     * 计算用户综合分 (B)
     * B = B1 * w1 + B2 * w2
     */
    private double calculateUserScoreB(UserProfileDO user,
                                       Map<Long, Double> rankIdMap, double nationalLineTotal, RecommendRuleDO rule) {
        double b1 = getRankScore(user.getGraduateSchoolId(), rankIdMap);

        // B2: 初试总分 (归一化)
        // Formula: L / (1 + e^-k(x-x0))
        if (user.getScoreTotal() == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CANDIDATE_SCORE_TOTAL_NOT_EXISTS);
        }
        double score = user.getScoreTotal().doubleValue();
        int L = rule.getCurveL() != null ? rule.getCurveL().intValue() : 5;
        double K = rule.getCurveK() != null ? rule.getCurveK().doubleValue() : 0.08;
        double x0 = rule.getCurveX0() != null ? rule.getCurveX0().doubleValue() : 56;
        double b2 = L / (1.0 + Math.exp(-K * (score - nationalLineTotal - x0)));

        double w1 = rule.getWeightB1() != null ? rule.getWeightB1().doubleValue() : 0.7;
        double w2 = rule.getWeightB2() != null ? rule.getWeightB2().doubleValue() : 0.3;

        return b1 * w1 + b2 * w2;
    }

    /**
     * 统一获取学校排名分
     */
    private double getRankScore(Long id, Map<Long, Double> idMap) {
        if (id != null && idMap.containsKey(id)) {
            return idMap.get(id);
        }
        return 0;
    }


    /**
     * 计算用户加分 (C)
     */
    private double calculateUserBonusC(UserProfileDO user, UserIntentionDO intention, double c0,
                                       Map<Long, Double> schoolRankSchoolIdMap, RecommendRuleDO rule) {
        // C1: 科研经历 (Paper)
        double c1 = 0.0;
        int paperCount = user.getPaperCount() != null ? user.getPaperCount() : 0;
        if (paperCount >= 3) {
            c1 = c0 * (rule.getC1PaperTop() != null ? rule.getC1PaperTop().doubleValue() : 0.2);
        } else if (paperCount >= 2) {
            c1 = c0 * (rule.getC1PaperMid() != null ? rule.getC1PaperMid().doubleValue() : 0.15);
        } else if (paperCount >= 1) {
            c1 = c0 * (rule.getC1PaperLow() != null ? rule.getC1PaperLow().doubleValue() : 0.1);
        }

        // C2: 竞赛得分
        double c2 = 0.0;
        int compCount = user.getCompetitionCount() != null ? user.getCompetitionCount() : 0;
        if (compCount > 0) {
            double base = rule.getC2CompBase() != null ? rule.getC2CompBase().doubleValue() : 84;
            c2 = compCount / base * c0;
        }

        // C3: 英语水平 (四六级)
        double c3 = 0.0;
        int cet6 = user.getCet6Score() != null ? user.getCet6Score() : 0;
        int cet4 = user.getCet4Score() != null ? user.getCet4Score() : 0;
        if (cet6 >= 470) {
            c3 = c0 * (rule.getC3CetHigh() != null ? rule.getC3CetHigh().doubleValue() : 0.1);
        } else if (cet6 >= 425 || cet4 >= 425) {
            c3 = c0 * (rule.getC3CetPass() != null ? rule.getC3CetPass().doubleValue() : 0.05);
        }

        // C4: 学科匹配度
        double c4 = 0.0;
        // 判断是否跨考
        if (!intention.getIsAcceptCrossExam()) {
            // 不跨考，按排名等级加分
            double c4Ap = rule.getC4RankAp() != null ? rule.getC4RankAp().doubleValue() : 1.0;
            String level = user.getGraduateMajorRank();
            if (StrUtil.isNotBlank(level)) {
                if ("A+".equalsIgnoreCase(level)) {
                    c4 = c0 * c4Ap;
                } else if ("A".equalsIgnoreCase(level)) {
                    c4 = c0 * (rule.getC4RankA() != null ? rule.getC4RankA().doubleValue() : 0.9);
                } else if ("A-".equalsIgnoreCase(level)) {
                    c4 = c0 * (rule.getC4RankAm() != null ? rule.getC4RankAm().doubleValue() : 0.8);
                } else if ("B+".equalsIgnoreCase(level)) {
                    c4 = c0 * (rule.getC4RankBp() != null ? rule.getC4RankBp().doubleValue() : 0.7);
                } else if ("B".equalsIgnoreCase(level)) {
                    c4 = c0 * (rule.getC4RankB() != null ? rule.getC4RankB().doubleValue() : 0.6);
                } else if ("B-".equalsIgnoreCase(level)) {
                    c4 = c0 * (rule.getC4RankBm() != null ? rule.getC4RankBm().doubleValue() : 0.5);
                } else if ("C+".equalsIgnoreCase(level)) {
                    c4 = c0 * (rule.getC4RankCp() != null ? rule.getC4RankCp().doubleValue() : 0.4);
                } else if ("C".equalsIgnoreCase(level)) {
                    c4 = c0 * (rule.getC4RankC() != null ? rule.getC4RankC().doubleValue() : 0.2);
                }
            }
        } else {
            if (intention.getIsAcceptCrossMajor()) {
                // 判断是否跨调
                c4 = 0;
            }
        }

        // C5: 本科成绩
        double c5 = 0.0;
        if (user.getGraduateAverageScore() != null && user.getGraduateAverageScore().doubleValue() > 85) {
            c5 = c0 * (rule.getC5Gpa() != null ? rule.getC5Gpa().doubleValue() : 0.1);
        }

        // C6: 奖学金
        double c6 = 0.0;
        if (Boolean.TRUE.equals(user.getIsNationalScholarship())) {
            c6 = c0 * (rule.getC6National() != null ? rule.getC6National().doubleValue() : 0.1);
        } else if (Boolean.TRUE.equals(user.getIsSchoolScholarship())) {
            c6 = c0 * (rule.getC6School() != null ? rule.getC6School().doubleValue() : 0.05);
        }

        // C7: 综合自评
        double c7 = 0.0;
        if (user.getSelfAssessedScore() != null) {
            // 分数范围通常 1-10，归一化后 * 权重
            double selfW = rule.getC7Self() != null ? rule.getC7Self().doubleValue() : 0.05;
            c7 = (user.getSelfAssessedScore() / 10.0) * c0 * selfW;
        }

        // C8: 一志愿院校背景加分
        double c8 = 0.0;
        if (user.getTargetSchoolId() != null) {
            SchoolDO firstChoice = schoolMapper.selectById(user.getTargetSchoolId());
            if (firstChoice != null && (Boolean.TRUE.equals(firstChoice.getIs985()) || Boolean.TRUE.equals(firstChoice.getIs211()))) {
                // 如果是985或211，根据软科分数区间加分
                double s = getRankScore(firstChoice.getId(), schoolRankSchoolIdMap);
                double ratio = 0.0;
                if (s >= 2.89) {
                    ratio = rule.getC8Ratio629() != null ? rule.getC8Ratio629().doubleValue() : 1.0;
                } else if (s >= 2.30) {
                    ratio = rule.getC8Ratio504() != null ? rule.getC8Ratio504().doubleValue() : 0.8;
                } else if (s >= 1.93) {
                    ratio = rule.getC8Ratio425() != null ? rule.getC8Ratio425().doubleValue() : 0.5;
                } else if (s >= 1.48) {
                    ratio = rule.getC8Ratio33() != null ? rule.getC8Ratio33().doubleValue() : 0.2;
                }
                c8 = c0 * ratio;
            }
        }
        return c1 + c2 + c3 + c4 + c5 + c6 + c7 + c8;
    }

    /**
     * 计算分数匹配度 SimA (Memory)
     */
    private double calculateSimAInMemory(UserProfileDO user,
                                         RecommendRuleDO rule, AdmitScoreAgg scoreAgg) {
        if (user.getScoreTotal() == null) {
            return 0;
        }
        double userScore = user.getScoreTotal().doubleValue();
        // 1. 从预加载的 Map 中查找调剂录取平均分 (优先 currentYear-1)
        if (Objects.isNull(scoreAgg)) {
            return 1;
        }
        BigDecimal admittedAvg = scoreAgg.getAvgScore();
        if (Objects.isNull(admittedAvg)) {
            return 1;
        }
        double delta = userScore - admittedAvg.doubleValue();
        List<RecommendRuleSimAItem> rules = rule.getSimARules();
        if (CollUtil.isNotEmpty(rules)) {
            for (RecommendRuleSimAItem item : rules) {
                boolean minMatch = item.getMin() == null || delta >= item.getMin();
                boolean maxMatch = item.getMax() == null || delta < item.getMax();
                if (minMatch && maxMatch) {
                    double base = item.getBase() != null ? item.getBase().doubleValue() : 0.0;
                    double slope = item.getSlope() != null ? item.getSlope().doubleValue() : 0.0;
                    // 如果未配置 reference，默认使用 0 (即 base + delta * slope)
                    // 但通常应该配置 reference，例如 delta > 10 时，reference=10，则 base + (delta-10)*slope
                    double reference = item.getReference() != null ? item.getReference().doubleValue() : 0.0;
                    double result = base + (delta - reference) * slope;
                    if (item.getMaxLimit() != null) {
                        result = Math.min(result, item.getMaxLimit().doubleValue());
                    }
                    if (item.getMinLimit() != null) {
                        result = Math.max(result, item.getMinLimit().doubleValue());
                    }
                    return result < 0 ? 0 : result;
                }
            }
            return 0;
        }
        throw ServiceExceptionUtil.invalidParamException("没有配置分数规则");
    }

    /**
     * 预加载所有规则桶，构建 List<Set<String>>，每个 Set 代表一个桶内的所有专业代码
     */
    private List<Set<String>> buildMajorBuckets() {
        List<RecommendRuleDO> allRules = recommendRuleMapper.selectList(new LambdaQueryWrapperX<RecommendRuleDO>());
        List<Set<String>> buckets = new ArrayList<>();
        for (RecommendRuleDO r : allRules) {
            if (StrUtil.isNotBlank(r.getMajorCodes())) {
                Set<String> codeSet = new HashSet<>(StrUtil.split(r.getMajorCodes(), ','));
                codeSet.remove("000000"); // 排除兜底标识
                if (codeSet.size() > 1) { // 至少两个代码才算桶
                    buckets.add(codeSet);
                }
            }
        }
        return buckets;
    }

    /**
     * 计算专业匹配度 SimB
     */
    private double calculateSimB(UserProfileDO user, AdjustmentDO adjustment, RecommendRuleDO rule) {
        // 如果有 targetMajorCode 可以用
        String userMajorCode = user.getTargetMajorCode();
        String adjMajorCode = adjustment.getMajorCode();
        double defaultScore = rule.getSimBDefault() != null ? rule.getSimBDefault().doubleValue() : 0.1;

        if (StrUtil.isBlank(adjMajorCode) || StrUtil.isBlank(userMajorCode)) {
            return defaultScore;
        }
        // 1. Code 匹配
        if (userMajorCode.equals(adjMajorCode)) {
            return rule.getSimBCode6() != null ? rule.getSimBCode6().doubleValue() : 1.0;
        }
        if (userMajorCode.length() >= 4 && adjMajorCode.length() >= 4 && userMajorCode.substring(0, 4).equals(adjMajorCode.substring(0, 4))) {
            return rule.getSimBCode4() != null ? rule.getSimBCode4().doubleValue() : 0.8;
        }
        if (userMajorCode.length() >= 2 && adjMajorCode.length() >= 2 && userMajorCode.substring(0, 2).equals(adjMajorCode.substring(0, 2))) {
            return rule.getSimBCode2() != null ? rule.getSimBCode2().doubleValue() : 0.2;
        }
        Set<String> bucket = Sets.newHashSet(rule.getMajorCodes().split(","));
        if (bucket.contains(userMajorCode) && bucket.contains(adjMajorCode)) {
            return rule.getSimBBucket() != null ? rule.getSimBBucket().doubleValue() : 0.6;
        }
        return defaultScore;
    }

    /**
     * 计算竞争力 SimC (Mock)
     */
    private double calculateSimC(UserProfileDO user, AdjustmentDO adjustment) {
        return 1; // 暂无热度数据
    }

    /**
     * 检查用户是否过该专业分数线
     */
    private boolean checkSchoolScore(UserProfileDO user, SchoolScoreDO schoolScore) {
        if (schoolScore == null) {
            return false;
        }

        // 总分
        if (schoolScore.getScoreTotal() != null) {
            if (user.getScoreTotal() == null || user.getScoreTotal().compareTo(schoolScore.getScoreTotal()) < 0) {
                return false;
            }
        }

        // 单科1 (政治)
        if (schoolScore.getScoreSubject1() != null) {
            if (user.getSubjectScore1() == null || user.getSubjectScore1().compareTo(schoolScore.getScoreSubject1()) < 0) {
                return false;
            }
        }
        // 单科2 (外语)
        if (schoolScore.getScoreSubject2() != null) {
            if (user.getSubjectScore2() == null || user.getSubjectScore2().compareTo(schoolScore.getScoreSubject2()) < 0) {
                return false;
            }
        }
        // 单科3 (业务课1)
        if (schoolScore.getScoreSubject3() != null) {
            if (user.getSubjectScore3() == null || user.getSubjectScore3().compareTo(schoolScore.getScoreSubject3()) < 0) {
                return false;
            }
        }
        // 单科4 (业务课2)
        if (schoolScore.getScoreSubject4() != null) {
            if (user.getSubjectScore4() == null || user.getSubjectScore4().compareTo(schoolScore.getScoreSubject4()) < 0) {
                return false;
            }
        }

        return true;
    }

    private SchoolScoreDO findBestMatchScore(List<SchoolScoreDO> scores, String majorCode, Integer degreeType) {
        if (StrUtil.isBlank(majorCode)) {
            return null;
        }

        // 1. 6-digit match
        for (SchoolScoreDO s : scores) {
            if ((degreeType == null || Objects.equals(s.getDegreeType(), degreeType))
                    && Objects.equals(s.getMajorCode(), majorCode)) {
                return s;
            }
        }
        // 2. 4-digit match
        if (majorCode.length() >= 4) {
            String prefix4 = majorCode.substring(0, 4);
            for (SchoolScoreDO s : scores) {
                if ((degreeType == null || Objects.equals(s.getDegreeType(), degreeType))
                        && s.getMajorCode() != null && s.getMajorCode().equals(prefix4)) {
                    return s;
                }
            }
        }
        // 3. 2-digit match
        if (majorCode.length() >= 2) {
            String prefix2 = majorCode.substring(0, 2);
            for (SchoolScoreDO s : scores) {
                if ((degreeType == null || Objects.equals(s.getDegreeType(), degreeType))
                        && s.getMajorCode() != null && s.getMajorCode().equals(prefix2)) {
                    return s;
                }
            }
        }
        return null;
    }

    /**
     * 根据用户意向过滤调剂信息
     */
    private List<AdjustmentDO> filterAdjustmentsByIntention(List<AdjustmentDO> adjustments, UserIntentionDO intention,
                                                            Map<Long, SchoolDO> schoolMap) {
        if (intention == null || CollUtil.isEmpty(adjustments)) {
            return adjustments;
        }

        List<AdjustmentDO> result = new ArrayList<>();

        // 1. Parse Intention Fields
        List<String> provinceCodes = StrUtil.isNotBlank(intention.getProvinceCodes())
                ? JSONUtil.toList(intention.getProvinceCodes(), String.class) : Collections.emptyList();
        List<Long> majorIds = parseJsonLongList(intention.getMajorIds()); // Use existing helper
        List<String> schoolLevels = StrUtil.isNotBlank(intention.getSchoolLevel())
                ? JSONUtil.toList(intention.getSchoolLevel(), String.class)
                : Collections.emptyList();
        Integer studyMode = intention.getStudyMode(); // 0-不限 1-全 2-非
        Integer degreeType = intention.getDegreeType(); // 0-不限 1-学 2-专
        Boolean isSpecialPlan = intention.getIsSpecialPlan();
        Set<String> allowedSubjectPairKeys = new HashSet<>();
        String subjectChoices = intention.getSubjectChoices();
        if (StrUtil.isNotBlank(subjectChoices)) {
            List<String> subjectChoicesList = JSONUtil.toList(subjectChoices, String.class);
            for (String subjectChoice : subjectChoicesList) {
                String pairKey = buildSubjectPairKeyFromOption(subjectChoice);
                if (StrUtil.isNotBlank(pairKey)) {
                    allowedSubjectPairKeys.add(pairKey);
                }
            }
        }

        for (AdjustmentDO adj : adjustments) {
            SchoolDO school = schoolMap.get(adj.getSchoolId());
            if (school == null) {
                continue;
            }

            // 1. Province Filter
            // If provinceCodes is set (whitelist), school must be in it.
            if (CollUtil.isNotEmpty(provinceCodes)) {
                if (StrUtil.isBlank(school.getProvinceCode()) || !provinceCodes.contains(school.getProvinceCode())) {
                    continue;
                }
            }
            // 2. School Level Filter (Intention: 1-985, 2-211, 3-Syl, 4-Ordinary)
            if (CollUtil.isNotEmpty(schoolLevels)) {
                String schoolLevel = inferSchoolLevelCode(school);
                if (StrUtil.isBlank(schoolLevel) || !schoolLevels.contains(schoolLevel)) {
                    continue;
                }
            }

            // 3. Major Filter
            if (CollUtil.isNotEmpty(majorIds)) {
                if (adj.getMajorId() == null || !majorIds.contains(adj.getMajorId())) {
                    continue;
                }
            }

            // 4. Study Mode Filter
            if (studyMode != null && studyMode != 0) {
                // Adjustment: 1-全, 2-非
                if (!studyMode.equals(adj.getStudyMode())) {
                    continue;
                }
            }

            // 5. Degree Type Filter
            if (degreeType != null && degreeType != 0) {
                // Adjustment: 1-学, 2-专
                if (adj.getDegreeType() != null && adj.getDegreeType() != 0 && !degreeType.equals(adj.getDegreeType())) {
                    continue;
                }
            }

            // 6. Special Plan Filter
            if (Boolean.TRUE.equals(isSpecialPlan)) {
                // User WANTS special plan -> Only keep special plan
                if (adj.getSpecialPlan() == null || adj.getSpecialPlan() == 1) {
                    continue;
                }
            }

            // 7. Subject Choices Filter: compare adj subjects.s2/s3 codes with user's selected pair.
            if (CollUtil.isNotEmpty(allowedSubjectPairKeys)) {
                String adjustmentPairKey = buildSubjectPairKeyFromAdjustmentSubjects(adj.getSubjects());
                if (StrUtil.isBlank(adjustmentPairKey) || !allowedSubjectPairKeys.contains(adjustmentPairKey)) {
                    continue;
                }
            }

            result.add(adj);
        }
        return result;
    }

    /**
     * Parse user option(A/B/C/D) -> pair key.
     * Example: 302_204 => 204_302 (normalized for order-insensitive compare).
     */
    private String buildSubjectPairKeyFromOption(String option) {
        if (StrUtil.isBlank(option)) {
            return null;
        }
        SubjectChoiceEnum subjectChoiceEnum = SubjectChoiceEnum.fromOption(option);
        if (subjectChoiceEnum == null || StrUtil.isBlank(subjectChoiceEnum.getName())) {
            return null;
        }
        String[] codes = subjectChoiceEnum.getName().split("_");
        if (codes.length != 2) {
            return null;
        }
        return normalizeSubjectPairKey(codes[0], codes[1]);
    }

    /**
     * Parse adjustment subjects JSON and extract s2/s3 code pair.
     */
    private String buildSubjectPairKeyFromAdjustmentSubjects(String subjectsJson) {
        if (StrUtil.isBlank(subjectsJson)) {
            return null;
        }
        try {
            JSONObject subjectsObj = JSONUtil.parseObj(subjectsJson);
            String s2Code = extractSubjectCode(subjectsObj, "s2");
            String s3Code = extractSubjectCode(subjectsObj, "s3");
            return normalizeSubjectPairKey(s2Code, s3Code);
        } catch (Exception e) {
            return null;
        }
    }

    private String extractSubjectCode(JSONObject subjectsObj, String field) {
        JSONArray arr = subjectsObj.getJSONArray(field);
        if (arr == null || arr.isEmpty()) {
            return null;
        }
        JSONObject item = arr.getJSONObject(0);
        if (item == null) {
            return null;
        }
        return item.getStr("code");
    }

    private String normalizeSubjectPairKey(String code1, String code2) {
        if (StrUtil.hasBlank(code1, code2)) {
            return null;
        }
        String a = code1.trim();
        String b = code2.trim();
        return a.compareTo(b) <= 0 ? a + "_" + b : b + "_" + a;
    }

    private String inferSchoolLevelCode(SchoolDO school) {
        if (school == null) {
            return null;
        }
        if (Boolean.TRUE.equals(school.getIs985())) {
            return "985";
        }
        if (Boolean.TRUE.equals(school.getIs211())) {
            return "211";
        }
        if (Boolean.TRUE.equals(school.getIsSyl())) {
            return "syl";
        }
        return "ordinary";
    }

}
