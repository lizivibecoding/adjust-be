package com.hongguoyan.module.biz.service.recommend;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil;
import com.hongguoyan.module.biz.controller.app.recommend.vo.AppRecommendSchoolListReqVO;
import com.hongguoyan.module.biz.controller.app.recommend.vo.AppRecommendSchoolRespVO;
import com.hongguoyan.module.biz.dal.dataobject.schoolrank.SchoolRankDO;
import com.hongguoyan.module.biz.dal.dataobject.majorrank.SchoolMajorRankDO;
import com.hongguoyan.module.biz.dal.dataobject.major.MajorDO;
import com.hongguoyan.module.biz.dal.dataobject.adjustment.AdjustmentDO;
import com.hongguoyan.module.biz.dal.dataobject.nationalscore.NationalScoreDO;
import com.hongguoyan.module.biz.dal.dataobject.recommend.UserRecommendSchoolDO;
import com.hongguoyan.module.biz.dal.dataobject.school.SchoolDO;
import com.hongguoyan.module.biz.dal.dataobject.schoolscore.SchoolScoreDO;
import com.hongguoyan.module.biz.dal.dataobject.usercustomreport.UserCustomReportDO;
import com.hongguoyan.module.biz.dal.dataobject.userintention.UserIntentionDO;
import com.hongguoyan.module.biz.dal.dataobject.userprofile.UserProfileDO;
import com.hongguoyan.module.biz.dal.mysql.adjustment.AdjustmentMapper;
import com.hongguoyan.module.biz.dal.mysql.schoolrank.SchoolRankMapper;
import com.hongguoyan.module.biz.dal.mysql.majorrank.SchoolMajorRankMapper;
import com.hongguoyan.module.biz.dal.mysql.major.MajorMapper;
import com.hongguoyan.module.biz.dal.mysql.nationalscore.NationalScoreMapper;
import com.hongguoyan.module.biz.dal.mysql.recommend.UserRecommendSchoolMapper;
import com.hongguoyan.module.biz.dal.mysql.school.SchoolMapper;
import com.hongguoyan.module.biz.dal.mysql.schoolscore.SchoolScoreMapper;
import com.hongguoyan.module.biz.dal.mysql.usercustomreport.UserCustomReportMapper;
import com.hongguoyan.module.biz.dal.mysql.userintention.UserIntentionMapper;
import com.hongguoyan.module.biz.dal.mysql.userprofile.UserProfileMapper;
import com.hongguoyan.module.biz.enums.ErrorCodeConstants;
import com.hongguoyan.module.biz.service.ai.AiTextService;
import com.hongguoyan.module.biz.service.ai.dto.AiTextRequest;
import com.hongguoyan.module.biz.service.ai.dto.AiTextResult;
import com.hongguoyan.module.biz.service.usercustomreport.UserCustomReportService;
import com.hongguoyan.module.biz.service.vipbenefit.VipBenefitService;
import com.hongguoyan.module.biz.service.vipbenefit.model.VipResolvedBenefit;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.Year;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.BENEFIT_KEY_USER_REPORT;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.BENEFIT_TYPE_QUOTA;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.REF_TYPE_CUSTOM_REPORT;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.VIP_BENEFIT_QUOTA_EXCEEDED;

import com.hongguoyan.module.biz.dal.dataobject.majorsimilarity.MajorSimilarityDO;
import com.hongguoyan.module.biz.dal.mysql.majorsimilarity.MajorSimilarityMapper;

import com.hongguoyan.module.biz.dal.dataobject.recommend.RecommendRuleDO;
import com.hongguoyan.module.biz.dal.mysql.recommend.RecommendRuleMapper;
import com.hongguoyan.module.biz.dal.mysql.adjustmentadmit.AdjustmentAdmitMapper;

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
    private NationalScoreMapper nationalScoreMapper;
    @Resource
    private SchoolScoreMapper schoolScoreMapper;
    @Resource
    private AdjustmentMapper adjustmentMapper;
    @Resource
    private UserRecommendSchoolMapper userRecommendSchoolMapper;
    @Resource
    private SchoolRankMapper schoolRankMapper;
    @Resource
    private SchoolMajorRankMapper schoolMajorRankMapper;
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
    private MajorSimilarityMapper majorSimilarityMapper;
    @Resource
    private AdjustmentAdmitMapper adjustmentAdmitMapper;
    @Resource
    private RecommendRuleMapper recommendRuleMapper;

    @Override
    public List<AppRecommendSchoolRespVO> recommendSchools(Long userId, AppRecommendSchoolListReqVO reqVO) {
        // 1. Determine reportId
        Long reportId = reqVO.getReportId();
        if (reportId == null) {
            UserCustomReportDO latestReport = userCustomReportMapper.selectLatestByUserId(userId);
            if (latestReport != null) {
                reportId = latestReport.getId();
            } else {
                return Collections.emptyList();
            }
        }
        // 2. Query recommendations
        List<UserRecommendSchoolDO> recommendations = userRecommendSchoolMapper.selectList(new LambdaQueryWrapperX<UserRecommendSchoolDO>()
                .eq(UserRecommendSchoolDO::getUserId, userId)
                .eq(UserRecommendSchoolDO::getReportId, reportId)
                .orderByDesc(UserRecommendSchoolDO::getSimFinal));

        if (CollUtil.isEmpty(recommendations)) {
            return Collections.emptyList();
        }

        // 3. Collect IDs and fetch related entities
        Set<Long> adjustmentIds = new HashSet<>();
        Set<Long> schoolIds = new HashSet<>();
        for (UserRecommendSchoolDO rec : recommendations) {
            if (rec.getAdjustmentId() != null) adjustmentIds.add(rec.getAdjustmentId());
            if (rec.getSchoolId() != null) schoolIds.add(rec.getSchoolId());
        }

        Map<Long, AdjustmentDO> adjustmentMap = adjustmentIds.isEmpty() ? Collections.emptyMap() :
                adjustmentMapper.selectBatchIds(adjustmentIds).stream()
                        .collect(Collectors.toMap(AdjustmentDO::getId, Function.identity()));

        Map<Long, SchoolDO> schoolMap = schoolIds.isEmpty() ? Collections.emptyMap() :
                schoolMapper.selectBatchIds(schoolIds).stream()
                        .collect(Collectors.toMap(SchoolDO::getId, Function.identity()));

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

            resp.setRankingGapLabel("适中");

            AdjustmentDO adj = adjustmentMap.get(rec.getAdjustmentId());
            if (adj != null) {
                resp.setMajorCode(adj.getMajorCode());
                resp.setYear(adj.getYear());
                resp.setCollegeName(adj.getCollegeName());
                resp.setStudyMode(adj.getStudyMode());
                resp.setDegreeType(adj.getDegreeType());
                resp.setPlanCount(adj.getAdjustCount());
            }

            result.add(resp);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean generateRecommend(Long userId,Long reportId) {
        // 1. 获取用户信息
        UserProfileDO userProfile = userProfileMapper.selectOne(new LambdaQueryWrapper<UserProfileDO>()
            .eq(UserProfileDO::getUserId, userId));
        if (userProfile == null) {
            log.warn("用户画像不存在，无法推荐: userId={}", userId);
            return false;
        }
        // 获取用户意向信息
        UserIntentionDO userIntention = userIntentionMapper.selectOne(new LambdaQueryWrapper<UserIntentionDO>()
            .eq(UserIntentionDO::getUserId, userId));

        // 预加载基础数据
        List<SchoolDO> allSchools = schoolMapper.selectList();
        Map<Long, SchoolDO> schoolMap = allSchools.stream()
            .collect(Collectors.toMap(SchoolDO::getId, Function.identity()));

        Integer currentYear = 2025;
        List<NationalScoreDO> nationalScores = nationalScoreMapper.selectList(new LambdaQueryWrapper<NationalScoreDO>()
            .eq(NationalScoreDO::getYear, currentYear));

        // --- Step 1: 硬性过滤 - 判断学生是否过国家线 (基于一志愿) ---
        // 确定一志愿学校所在区域
        String firstChoiceArea = "A";
        if (userProfile.getTargetSchoolId() != null) {
            SchoolDO firstChoiceSchool = schoolMap.get(userProfile.getTargetSchoolId());
            if (firstChoiceSchool != null && StrUtil.isNotBlank(firstChoiceSchool.getProvinceArea())) {
                firstChoiceArea = firstChoiceSchool.getProvinceArea();
            }
        }

        // 检查用户是否过一志愿区域的国家线 (作为基本资格)
        boolean qualified = checkNationalLineStrict(userProfile, firstChoiceArea, nationalScores);

        if (!qualified) {
            log.info("用户未过一志愿区域({})国家线，无法推荐: userId={}", firstChoiceArea, userId);
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_NOT_QUALIFIED);
        }

        // 4. 加载并过滤学校

        // 预加载软科排名 (用于协同过滤)
        List<SchoolRankDO> allRanks = schoolRankMapper.selectList(new LambdaQueryWrapper<SchoolRankDO>()
            .orderByAsc(SchoolRankDO::getYear));
        Map<Long, Double> schoolRankIdMap = new HashMap<>();
        Map<String, Double> schoolRankNameMap = new HashMap<>();
        for (SchoolRankDO rank : allRanks) {
            if (rank.getScore() != null) {
                double s = rank.getScore().doubleValue();
                if (rank.getSchoolId() != null) {
                    schoolRankIdMap.put(rank.getSchoolId(), s);
                }
                if (StrUtil.isNotBlank(rank.getSchoolName())) {
                    schoolRankNameMap.put(rank.getSchoolName(), s);
                }
            }
        }

        // 预加载所有学校分数线 (自划线)
        List<SchoolScoreDO> allSchoolScores = schoolScoreMapper.selectList(new LambdaQueryWrapper<SchoolScoreDO>()
            .eq(SchoolScoreDO::getYear, currentYear));
        // Map<SchoolId, List<SchoolScoreDO>> 记录具体分数线 (用于精筛)
        Map<Long, List<SchoolScoreDO>> schoolMajorScoreMap = new HashMap<>();
        for (SchoolScoreDO score : allSchoolScores) {
            schoolMajorScoreMap.computeIfAbsent(score.getSchoolId(), k -> new ArrayList<>())
                .add(score);
        }

        // 获取匹配的国家线
        NationalScoreDO matchedLine = findMatchedNationalLine(userProfile, firstChoiceArea, nationalScores);
        if (matchedLine == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.NATIONAL_SCORE_NOT_EXISTS);
        }
        double nationalLineTotal = matchedLine.getTotal().doubleValue();

        // 4.0 获取算法参数 (基于 TargetMajorCode)
        RecommendRuleDO rule = fetchRecommendRule(userProfile.getTargetMajorCode());

        double userScoreB = calculateUserScoreB(userProfile, schoolMap, schoolRankIdMap, schoolRankNameMap, nationalLineTotal, rule);
        double weightC0 = rule.getWeightC0() != null ? rule.getWeightC0().doubleValue() : 0.2;
        double baseBonusC0 = userScoreB * weightC0;

        Set<Long> candidateSchoolIds = new HashSet<>();

        double filterMin = rule.getFilterMin() != null ? rule.getFilterMin().doubleValue() : 0.75;
        double filterMax = rule.getFilterMax() != null ? rule.getFilterMax().doubleValue() : 1.5;

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
            double schoolScoreA = getRankScore(schoolId, school.getSchoolName(), schoolRankIdMap, schoolRankNameMap);

            // 计算乐观 C (假设 adjustment 匹配良好)
            double maxC = calculateUserBonusC(userProfile, userIntention, baseBonusC0, schoolRankIdMap, schoolRankNameMap, rule);
            double totalUserScore = userScoreB + maxC;

            if (totalUserScore >= filterMin * schoolScoreA && totalUserScore <= filterMax * schoolScoreA) {
                candidateSchoolIds.add(schoolId);
            }
        }

        if (candidateSchoolIds.isEmpty()) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.NO_MATCHING_SCHOOLS);
        }

        // 5. 获取候选学校的调剂信息
        List<AdjustmentDO> adjustments = adjustmentMapper.selectList(new LambdaQueryWrapper<AdjustmentDO>()
            .eq(AdjustmentDO::getStatus, 1) // 开放状态
            .eq(AdjustmentDO::getAdjustStatus, 1) // 正常招生
            .in(AdjustmentDO::getSchoolId, candidateSchoolIds)); // 限制在候选学校中

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

        double weightSimA = rule.getWeightSimA() != null ? rule.getWeightSimA().doubleValue() : 0.5;
        double weightSimB = rule.getWeightSimB() != null ? rule.getWeightSimB().doubleValue() : 0.3;
        double weightSimC = rule.getWeightSimC() != null ? rule.getWeightSimC().doubleValue() : 0.2;
        double catThreshold1 = rule.getCatThreshold1() != null ? rule.getCatThreshold1().doubleValue() : 0.4;
        double catThreshold2 = rule.getCatThreshold2() != null ? rule.getCatThreshold2().doubleValue() : 0.8;

        for (AdjustmentDO adjustment : adjustments) {
            SchoolDO school = schoolMap.get(adjustment.getSchoolId());
            if (school == null) {
                continue;
            }

            // Sim Calculations
            double simA = calculateSimAInMemory(userProfile, adjustment, schoolMajorScoreMap.get(school.getId()), currentYear, rule);
            double simB = calculateSimB(userProfile, adjustment, rule);
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
                .directionId(adjustment.getDirectionId())
                .directionCode(adjustment.getDirectionCode())
                .directionName(adjustment.getDirectionName())
                .simFinal(BigDecimal.valueOf(simFinal))
                .simA(BigDecimal.valueOf(simA))
                .simB(BigDecimal.valueOf(simB))
                .simC(BigDecimal.valueOf(simC))
                .category(category)
                .build();
            recommendations.add(recommendDO);
        }

        // 5. 保存结果
        if (CollUtil.isNotEmpty(recommendations)) {
            // 按 simFinal 降序排序
            recommendations.sort((o1, o2) -> o2.getSimFinal().compareTo(o1.getSimFinal()));
            userRecommendSchoolMapper.insertBatch(recommendations);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void generateAssessmentReport(Long userId) {
        // 0) Quick quota check (no consume yet). Consume after success to avoid charging on failure.
        vipBenefitService.checkEnabledOrThrow(userId, BENEFIT_KEY_USER_REPORT);
        VipResolvedBenefit quota = vipBenefitService.resolveBenefit(userId, BENEFIT_KEY_USER_REPORT);
        if (quota.getBenefitType() != null && quota.getBenefitType() != BENEFIT_TYPE_QUOTA) {
            // unexpected config type, let downstream throw consistent error
        } else {
            Integer v = quota.getBenefitValue();
            int used = quota.getUsedCount() != null ? quota.getUsedCount() : 0;
            if (v != null && v != -1 && used >= v) {
                throw ServiceExceptionUtil.exception(VIP_BENEFIT_QUOTA_EXCEEDED);
            }
        }

        // 1. Load user profile + intention
        UserProfileDO userProfile = userProfileMapper.selectOne(new LambdaQueryWrapper<UserProfileDO>()
            .eq(UserProfileDO::getUserId, userId));
        if (userProfile == null) {
            log.warn("用户画像不存在，无法生成报告: userId={}", userId);
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_PROFILE_NOT_EXISTS);
        }

        UserIntentionDO userIntention = userIntentionMapper.selectOne(new LambdaQueryWrapper<UserIntentionDO>()
            .eq(UserIntentionDO::getUserId, userId));

        // 2. Resolve basic context
        // use profile createTime year as report year
        int currentYear = userProfile.getCreateTime() != null
            ? userProfile.getCreateTime().getYear()
            : Year.now().getValue();
        List<NationalScoreDO> nationalScores = nationalScoreMapper.selectList(new LambdaQueryWrapper<NationalScoreDO>()
            .eq(NationalScoreDO::getYear, currentYear));

        // only load schools needed by report: graduate school + target school
        Set<Long> neededSchoolIds = new HashSet<>();
        if (userProfile.getGraduateSchoolId() != null) {
            neededSchoolIds.add(userProfile.getGraduateSchoolId());
        }
        if (userProfile.getTargetSchoolId() != null) {
            neededSchoolIds.add(userProfile.getTargetSchoolId());
        }
        List<SchoolDO> schools = neededSchoolIds.isEmpty()
            ? Collections.emptyList()
            : schoolMapper.selectBatchIds(neededSchoolIds);
        Map<Long, SchoolDO> schoolMap = schools.stream()
            .filter(s -> s.getId() != null)
            .collect(Collectors.toMap(SchoolDO::getId, Function.identity(), (a, b) -> a));

        String firstChoiceArea = resolveFirstChoiceArea(userProfile, schoolMap);
        NationalScoreDO matchedLine = findMatchedNationalLine(userProfile, firstChoiceArea, nationalScores);

        // Graduate school rank (Ruanke)
        SchoolRankDO ruanke = null;
        if (userProfile.getGraduateSchoolId() != null) {
            ruanke = schoolRankMapper.selectLatestBySchoolId(userProfile.getGraduateSchoolId());
        }
        if (ruanke == null && StrUtil.isNotBlank(userProfile.getGraduateSchoolName())) {
            ruanke = schoolRankMapper.selectLatestBySchoolName(userProfile.getGraduateSchoolName());
        }

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
                .select(AdjustmentDO::getMajorId)
                .eq(AdjustmentDO::getStatus, 1)
                .eq(AdjustmentDO::getAdjustStatus, 1)
                .eq(AdjustmentDO::getYear, currentYear)
                .in(AdjustmentDO::getMajorId, intentionMajorIds));
            Map<Long, Long> grouped = rows.stream()
                .filter(r -> r.getMajorId() != null)
                .collect(Collectors.groupingBy(AdjustmentDO::getMajorId, LinkedHashMap::new, Collectors.counting()));
            openAdjustmentCountByMajorId.putAll(grouped);
        }

        // 3. Create new report version row
        Long reportId = userCustomReportService.createNewVersionByUserId(userId);

        // 4. Ask AI to generate 5-dimension report JSON
        String prompt = buildAssessmentPrompt(currentYear, userProfile, userIntention, firstChoiceArea, matchedLine, ruanke,
            schoolMap, intentionMajorNameMap, openAdjustmentCountByMajorId);

        AiTextResult aiResult = aiTextService.generateText(AiTextRequest.builder()
            .provider("doubao")
            .prompt(prompt)
            .timeoutMs(60_000L)
            .build());

        StudentAssessmentAiReport aiReport = parseAssessmentAiJson(aiResult != null ? aiResult.getText() : null);

        // 5. Persist result
        UserCustomReportDO toUpdate = new UserCustomReportDO();
        toUpdate.setId(reportId);
        toUpdate.setUserId(userId);
        toUpdate.setReportVersion(String.valueOf(System.currentTimeMillis()));
        toUpdate.setSourceProfileId(userProfile.getId());
        toUpdate.setSourceIntentionId(userIntention != null ? userIntention.getId() : null);

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

        userCustomReportMapper.updateById(toUpdate);

        // 6. Consume quota after success
        vipBenefitService.consumeQuotaOrThrow(userId, BENEFIT_KEY_USER_REPORT, 1,
            REF_TYPE_CUSTOM_REPORT, String.valueOf(reportId), null);
        generateRecommend(userId, reportId);
    }

    // --- Helper Methods ---

    /**
     * 严格检查国家线 (针对特定区域)
     */
    private boolean checkNationalLineStrict(UserProfileDO user, String area, List<NationalScoreDO> nationalScores) {
        if (user.getScoreTotal() == null) {
            return false;
        }

        NationalScoreDO matchedLine = findMatchedNationalLine(user, area, nationalScores);

        if (matchedLine == null) {
            return false; // 无线数据，默认过
        }

        // 总分
        if (user.getScoreTotal().intValue() < matchedLine.getTotal()) {
            return false;
        }

        // 单科
        int s1 = user.getSubjectScore1() != null ? user.getSubjectScore1().intValue() : 0;
        int s2 = user.getSubjectScore2() != null ? user.getSubjectScore2().intValue() : 0;
        int s3 = user.getSubjectScore3() != null ? user.getSubjectScore3().intValue() : 0;
        int s4 = user.getSubjectScore4() != null ? user.getSubjectScore4().intValue() : 0;

        if (s1 < matchedLine.getSingle100()) {
            return false;
        }
        if (s2 < matchedLine.getSingle100()) {
            return false;
        }
        if (s3 < matchedLine.getSingle150()) {
            return false;
        }
        return s4 >= matchedLine.getSingle150();
    }

    private String resolveFirstChoiceArea(UserProfileDO userProfile, Map<Long, SchoolDO> schoolMap) {
        String firstChoiceArea = "A";
        if (userProfile.getTargetSchoolId() != null) {
            SchoolDO firstChoiceSchool = schoolMap.get(userProfile.getTargetSchoolId());
            if (firstChoiceSchool != null && StrUtil.isNotBlank(firstChoiceSchool.getProvinceArea())) {
                firstChoiceArea = firstChoiceSchool.getProvinceArea();
            }
        }
        return firstChoiceArea;
    }

    private NationalScoreDO findMatchedNationalLine(UserProfileDO user, String area, List<NationalScoreDO> nationalScores) {
        if (user == null || StrUtil.isBlank(area) || CollUtil.isEmpty(nationalScores)) {
            return null;
        }
        Integer degreeType = user.getTargetDegreeType() != null ? user.getTargetDegreeType() : 2;
        String majorCode = user.getTargetMajorCode();
        if (StrUtil.isBlank(majorCode)) {
            return null;
        }
        NationalScoreDO matchedLine = nationalScores.stream()
            .filter(ns -> ns.getDegreeType().equals(degreeType))
            .filter(ns -> ns.getArea().equalsIgnoreCase(area))
            .filter(ns -> majorCode.startsWith(ns.getMajorCode()))
            .max(Comparator.comparingInt(o -> o.getMajorCode().length()))
            .orElse(null);
        if (matchedLine != null) {
            return matchedLine;
        }
        if (majorCode.length() < 2) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.NATIONAL_SCORE_NOT_EXISTS);
        }
        return nationalScores.stream()
            .filter(ns -> ns.getDegreeType().equals(degreeType))
            .filter(ns -> ns.getArea().equalsIgnoreCase(area))
            .filter(ns -> majorCode.substring(0, 2).equals(ns.getMajorCode()))
            .findFirst()
            .orElseThrow(() -> ServiceExceptionUtil.exception(ErrorCodeConstants.NATIONAL_SCORE_NOT_EXISTS));
    }

    private List<Long> parseJsonLongList(String json) {
        if (StrUtil.isBlank(json)) {
            return Collections.emptyList();
        }
        try {
            List<Object> raw = JSONUtil.parseArray(json).toList(Object.class);
            if (raw == null || raw.isEmpty()) {
                return Collections.emptyList();
            }
            List<Long> result = new ArrayList<>(raw.size());
            for (Object item : raw) {
                if (item == null) {
                    continue;
                }
                try {
                    result.add(Long.parseLong(String.valueOf(item)));
                } catch (NumberFormatException ignore) {
                    // ignore invalid items
                }
            }
            return result;
        } catch (Exception ignore) {
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
        SchoolDO gradSchool = profile.getGraduateSchoolId() != null ? schoolMap.get(profile.getGraduateSchoolId()) : null;
        SchoolDO targetSchool = profile.getTargetSchoolId() != null ? schoolMap.get(profile.getTargetSchoolId()) : null;

        Integer scoreTotal = profile.getScoreTotal() != null ? profile.getScoreTotal().intValue() : null;
        Integer nationalTotal = (nationalLine != null && nationalLine.getTotal() != null)
            ? nationalLine.getTotal().intValue()
            : null;
        Integer delta = (scoreTotal != null && nationalTotal != null) ? (scoreTotal - nationalTotal) : null;

        Map<String, Object> input = new LinkedHashMap<>();
        input.put("year", year);
        // NOTE: Map.of does not allow null values, use LinkedHashMap for DB-driven payloads.
        Map<String, Object> graduateSchool = new LinkedHashMap<>();
        graduateSchool.put("id", profile.getGraduateSchoolId());
        graduateSchool.put("name", StrUtil.blankToDefault(profile.getGraduateSchoolName(), gradSchool != null ? gradSchool.getSchoolName() : null));
        graduateSchool.put("is985", gradSchool != null ? gradSchool.getIs985() : null);
        graduateSchool.put("is211", gradSchool != null ? gradSchool.getIs211() : null);
        graduateSchool.put("isSyl", gradSchool != null ? gradSchool.getIsSyl() : null);
        input.put("graduateSchool", graduateSchool);

        if (ruankeRank == null) {
            input.put("ruankeRank", null);
        } else {
            Map<String, Object> ruankeRankMap = new LinkedHashMap<>();
            ruankeRankMap.put("year", ruankeRank.getYear());
            ruankeRankMap.put("ranking", ruankeRank.getRanking());
            ruankeRankMap.put("score", ruankeRank.getScore());
            input.put("ruankeRank", ruankeRankMap);
        }

        Map<String, Object> firstScore = new LinkedHashMap<>();
        firstScore.put("total", scoreTotal);
        firstScore.put("nationalArea", nationalArea);
        firstScore.put("targetMajorCode", profile.getTargetMajorCode());
        firstScore.put("targetMajorName", profile.getTargetMajorName());
        firstScore.put("targetDegreeType", profile.getTargetDegreeType());
        firstScore.put("targetDegreeTypeName", degreeTypeName(profile.getTargetDegreeType()));
        firstScore.put("nationalLineTotal", nationalTotal);
        firstScore.put("delta", delta);
        input.put("firstScore", firstScore);

        Map<String, Object> targetSchoolMap = new LinkedHashMap<>();
        targetSchoolMap.put("id", profile.getTargetSchoolId());
        targetSchoolMap.put("name",
            StrUtil.blankToDefault(profile.getTargetSchoolName(), targetSchool != null ? targetSchool.getSchoolName() : null));
        targetSchoolMap.put("is985", targetSchool != null ? targetSchool.getIs985() : null);
        targetSchoolMap.put("is211", targetSchool != null ? targetSchool.getIs211() : null);
        targetSchoolMap.put("isSyl", targetSchool != null ? targetSchool.getIsSyl() : null);
        input.put("targetSchool", targetSchoolMap);
        if (intention == null) {
            input.put("intention", null);
        } else {
            Map<String, Object> intentionMap = new LinkedHashMap<>();
            intentionMap.put("schoolLevel", intention.getSchoolLevel());
            intentionMap.put("schoolLevelName", schoolLevelName(intention.getSchoolLevel()));
            intentionMap.put("studyMode", intention.getStudyMode());
            intentionMap.put("studyModeName", studyModeName(intention.getStudyMode()));
            intentionMap.put("degreeType", intention.getDegreeType());
            intentionMap.put("degreeTypeName", degreeTypeName(intention.getDegreeType()));
            intentionMap.put("isAcceptCrossMajor", intention.getIsAcceptCrossMajor());
            intentionMap.put("isAcceptCrossExam", intention.getIsAcceptCrossExam());
            intentionMap.put("adjustPriority", intention.getAdjustPriority());
            intentionMap.put("adjustPriorityName", adjustPriorityName(intention.getAdjustPriority()));
            input.put("intention", intentionMap);
        }
        input.put("intentionMajors", intentionMajorNameMap);
        input.put("openAdjustmentCountByMajorId", openAdjustmentCountByMajorId);
        // NOTE: Map.of supports up to 10 pairs only; use LinkedHashMap for larger payloads
        Map<String, Object> softSkills = new LinkedHashMap<>();
        softSkills.put("cet4", profile.getCet4Score());
        softSkills.put("cet6", profile.getCet6Score());
        softSkills.put("paperCount", profile.getPaperCount());
        softSkills.put("paperExperience", profile.getPaperExperience());
        softSkills.put("competitionCount", profile.getCompetitionCount());
        softSkills.put("competitionExperience", profile.getCompetitionExperience());
        softSkills.put("awardCount", profile.getAwardCount());
        softSkills.put("awards", profile.getUndergraduateAwards());
        softSkills.put("gpa", profile.getUndergraduateGpa());
        softSkills.put("avgScore", profile.getGraduateAverageScore());
        softSkills.put("selfAssessedScore", profile.getSelfAssessedScore());
        softSkills.put("selfIntroduction", profile.getSelfIntroduction());
        input.put("softSkills", softSkills);

        String inputJson;
        try {
            inputJson = objectMapper.writeValueAsString(input);
        } catch (Exception e) {
            inputJson = "{}";
        }

        return """
            你是一名考研调剂咨询顾问，请基于输入数据生成“学生评估报告（5个维度）”。
                            
            维度：
            1) 院校背景维度（软科排名/985/211/双一流等）
            2) 学生初试总分维度（与国家线离散程度：delta=总分-国家线总分）
            3) 目标院校层次维度（结合一志愿学校层次与调剂意向院校层次）
            4) 专业竞争力维度（调剂意向专业 + 市场供给：openAdjustmentCountByMajorId）
            5) 软实力维度（英语/科研/竞赛/获奖/GPA/自评等）
                            
            输出要求：
            - 只输出一个 JSON 对象，不要 Markdown，不要额外解释。
            - 分数字段为 0-100 的整数。
            - 文案字段使用中文，分段清晰，给出优势/风险/建议。
            - 字段必须完整，缺数据时也要给合理兜底说明。
                            
            输出 JSON Schema（必须严格遵守字段名）：
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
                            
            输入数据(JSON)：
            %s
            """.formatted(inputJson);
    }

    /**
     * 获取推荐规则参数
     */
    private RecommendRuleDO fetchRecommendRule(String majorCode) {
        RecommendRuleDO rule = null;
        if (StrUtil.isNotBlank(majorCode)) {
            // 尝试按专业代码查询
            rule = recommendRuleMapper.selectOne(new LambdaQueryWrapper<RecommendRuleDO>()
                .eq(RecommendRuleDO::getMajorCode, majorCode));
        }

        if (rule == null) {
            // 尝试查询默认兜底配置
            rule = recommendRuleMapper.selectOne(new LambdaQueryWrapper<RecommendRuleDO>()
                .eq(RecommendRuleDO::getMajorCode, "000000"));
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

    private String schoolLevelName(Integer schoolLevel) {
        if (schoolLevel == null) {
            return null;
        }
        return switch (schoolLevel) {
            case 1 -> "985";
            case 2 -> "211";
            case 3 -> "双一流";
            case 4 -> "普通";
            default -> "未知";
        };
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
    private double calculateUserScoreB(UserProfileDO user, Map<Long, SchoolDO> schoolMap,
        Map<Long, Double> rankIdMap, Map<String, Double> rankNameMap,
        double nationalLineTotal, RecommendRuleDO rule) {
        // B1: 本科院校排名分
        String gradSchoolName = user.getGraduateSchoolName();
        if (StrUtil.isBlank(gradSchoolName) && user.getGraduateSchoolId() != null) {
            SchoolDO s = schoolMap.get(user.getGraduateSchoolId());
            if (s != null) {
                gradSchoolName = s.getSchoolName();
            }
        }
        double b1 = getRankScore(user.getGraduateSchoolId(), gradSchoolName, rankIdMap, rankNameMap);

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
    private double getRankScore(Long schoolId, String schoolName, Map<Long, Double> idMap, Map<String, Double> nameMap) {
        if (schoolId != null && idMap.containsKey(schoolId)) {
            return idMap.get(schoolId) / 100;
        }
        if (StrUtil.isNotBlank(schoolName) && nameMap.containsKey(schoolName)) {
            return nameMap.get(schoolName) / 100;
        }
        return 1.0 / 100;
    }

    /**
     * 计算用户加分 (C)
     */
    private double calculateUserBonusC(UserProfileDO user, UserIntentionDO intention, double c0,
        Map<Long, Double> rankIdMap, Map<String, Double> rankNameMap,
        RecommendRuleDO rule) {
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
        SchoolMajorRankDO rankDO = null;
        if (user.getTargetSchoolId() != null && StrUtil.isNotBlank(user.getTargetMajorCode())) {
            // 尝试 6 位匹配
            rankDO = schoolMajorRankMapper.selectOne(new LambdaQueryWrapper<SchoolMajorRankDO>()
                .eq(SchoolMajorRankDO::getSchoolId, user.getTargetSchoolId())
                .eq(SchoolMajorRankDO::getMajorCode, user.getTargetMajorCode()));

            // 尝试 4 位匹配
            if (rankDO == null && user.getTargetMajorCode().length() >= 4) {
                rankDO = schoolMajorRankMapper.selectOne(new LambdaQueryWrapper<SchoolMajorRankDO>()
                    .eq(SchoolMajorRankDO::getSchoolId, user.getTargetSchoolId())
                    .eq(SchoolMajorRankDO::getMajorCode, user.getTargetMajorCode().substring(0, 4)));
            }
        }

        if (rankDO != null) {
            // 判断是否跨考
            boolean isCrossExam;
            if (intention != null && intention.getIsAcceptCrossExam() != null) {
                // 如果 intention 中有明确值，以 intention 为准
                isCrossExam = intention.getIsAcceptCrossExam();
            } else {
                // 兜底逻辑
                isCrossExam = true;
                String graduateMajorCode = null;
                if (user.getGraduateMajorId() != null) {
                    MajorDO graduateMajor = majorMapper.selectById(user.getGraduateMajorId());
                    if (graduateMajor != null) {
                        graduateMajorCode = graduateMajor.getCode();
                    }
                }
                // 如果 6 位代码完全一致，肯定不是跨考
                if (StrUtil.isNotBlank(graduateMajorCode) && StrUtil.isNotBlank(user.getTargetMajorCode())) {
                    if (graduateMajorCode.equals(user.getTargetMajorCode())) {
                        isCrossExam = false;
                    }
                }
            }

            if (!isCrossExam) {
                // 不跨考，按排名等级加分
                double c4Ap = rule.getC4RankAp() != null ? rule.getC4RankAp().doubleValue() : 1.0;
                String level = rankDO.getCalculatedLevel(); // A+, A ...
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
                // 跨考 (但可能是相近学科)
                String graduateMajorCode = null;
                if (user.getGraduateMajorId() != null) {
                    MajorDO graduateMajor = majorMapper.selectById(user.getGraduateMajorId());
                    if (graduateMajor != null) {
                        graduateMajorCode = graduateMajor.getCode();
                    }
                }

                // 使用抽取的方法判断是否相近
                boolean isSimilar = isSimilarMajor(user.getTargetMajorCode(), graduateMajorCode);

                if (isSimilar) {
                    double c4Cross = rule.getC4CrossSim() != null ? rule.getC4CrossSim().doubleValue() : 0.6;
                    c4 = c0 * c4Cross;
                }
            }
        }

        // C5: 本科成绩 (GPA/平均分)
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
                double s = getRankScore(firstChoice.getId(), firstChoice.getSchoolName(), rankIdMap, rankNameMap);
                // s 是 score/100
                double ratio = 0.0;
                if (s >= 6.29) {
                    ratio = rule.getC8Ratio629() != null ? rule.getC8Ratio629().doubleValue() : 1.0;
                } else if (s >= 5.04) {
                    ratio = rule.getC8Ratio504() != null ? rule.getC8Ratio504().doubleValue() : 0.8;
                } else if (s >= 4.25) {
                    ratio = rule.getC8Ratio425() != null ? rule.getC8Ratio425().doubleValue() : 0.5;
                } else if (s >= 3.3) {
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
    private double calculateSimAInMemory(UserProfileDO user, AdjustmentDO adjustment, List<SchoolScoreDO> schoolMajorScoreMap, Integer currentYear,
        RecommendRuleDO rule) {
        if (user.getScoreTotal() == null) {
            return 0.5;
        }
        double userScore = user.getScoreTotal().doubleValue();

        double avgScore = 0; // 默认

        // 1. 尝试从调剂录取名单计算平均分 (优先尝试 currentYear-1, 再 currentYear-2)
        BigDecimal admittedAvg = null;
        if (currentYear != null) {
            admittedAvg = adjustmentAdmitMapper.selectAvgFirstScore(
                adjustment.getSchoolId(),
                adjustment.getCollegeId(),
                adjustment.getMajorCode(),
                currentYear - 1
            );
            if (admittedAvg == null) {
                admittedAvg = adjustmentAdmitMapper.selectAvgFirstScore(
                    adjustment.getSchoolId(),
                    adjustment.getCollegeId(),
                    adjustment.getMajorCode(),
                    currentYear - 2
                );
            }
        } else {
            // 没传年份就泛查
            admittedAvg = adjustmentAdmitMapper.selectAvgFirstScore(
                adjustment.getSchoolId(),
                adjustment.getCollegeId(),
                adjustment.getMajorCode(),
                null
            );
        }

        if (admittedAvg != null) {
            avgScore = admittedAvg.doubleValue();
        } else {
            // 2. 降级：查找往年分数 (这里简化为取最新的自划线作为参考，或者沿用 SchoolScoreDO)
            if (CollUtil.isNotEmpty(schoolMajorScoreMap)) {
                SchoolScoreDO schoolScore = findBestMatchScore(schoolMajorScoreMap, adjustment.getMajorCode(), null);
                if (schoolScore != null && schoolScore.getScoreTotal() != null) {
                    avgScore = schoolScore.getScoreTotal().doubleValue();
                }
            }
        }

        double delta = userScore - avgScore;
        if (delta > 0) {
            double decay = rule.getSimADeltaPosDecay() != null ? rule.getSimADeltaPosDecay().doubleValue() : 0.5;
            double div = rule.getSimADeltaPosDiv() != null ? rule.getSimADeltaPosDiv().doubleValue() : 100.0;
            return 1.0 - Math.min(decay, delta / div);
        } else if (delta >= -10) {
            double base = rule.getSimADeltaNeg10Base() != null ? rule.getSimADeltaNeg10Base().doubleValue() : 0.8;
            double slope = rule.getSimADeltaNeg10Slope() != null ? rule.getSimADeltaNeg10Slope().doubleValue() : 0.02;
            return base + slope * delta;
        } else if (delta >= -30) {
            double base = rule.getSimADeltaNeg30Base() != null ? rule.getSimADeltaNeg30Base().doubleValue() : 0.6;
            double slope = rule.getSimADeltaNeg30Slope() != null ? rule.getSimADeltaNeg30Slope().doubleValue() : 0.01;
            return base + slope * (delta + 10);
        } else {
            double base = rule.getSimADeltaNegLowBase() != null ? rule.getSimADeltaNegLowBase().doubleValue() : 0.4;
            double slope = rule.getSimADeltaNegLowSlope() != null ? rule.getSimADeltaNegLowSlope().doubleValue() : 0.005;
            double min = rule.getSimADeltaNegLowMin() != null ? rule.getSimADeltaNegLowMin().doubleValue() : 0.1;
            return Math.max(min, base + slope * (delta + 30));
        }
    }

    /**
     * 计算专业匹配度 SimB
     */
    private double calculateSimB(UserProfileDO user, AdjustmentDO adjustment, RecommendRuleDO rule) {
        String userMajorCode = "000000"; // TODO: UserProfile need major code, currently using name or empty
        // 暂时假设用户没有填 MajorCode，只填了 Name，这里先Mock
        // 如果有 targetMajorCode 可以用
        if (StrUtil.isNotBlank(user.getTargetMajorCode())) {
            userMajorCode = user.getTargetMajorCode();
        }

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

        // 2. 相似专业桶匹配
        List<MajorSimilarityDO> buckets = majorSimilarityMapper.selectList(new LambdaQueryWrapper<MajorSimilarityDO>()
            .like(MajorSimilarityDO::getMajorCodes, userMajorCode));

        if (CollUtil.isNotEmpty(buckets)) {
            for (MajorSimilarityDO bucket : buckets) {
                if (StrUtil.isBlank(bucket.getMajorCodes())) {
                    continue;
                }
                List<String> codes = StrUtil.split(bucket.getMajorCodes(), ',');
                if (codes.contains(userMajorCode) && codes.contains(adjMajorCode)) {
                    // 取桶的系数
                    if (bucket.getSimilarityCoefficient() != null) {
                        return bucket.getSimilarityCoefficient().doubleValue();
                    }
                    return rule.getSimBBucket() != null ? rule.getSimBBucket().doubleValue() : 0.6;
                }
            }
        }
        return defaultScore;
    }

    /**
     * 计算竞争力 SimC (Mock)
     */
    private double calculateSimC(UserProfileDO user, AdjustmentDO adjustment) {
        return 0.5; // 暂无热度数据
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
     * 判断两个专业是否相近 (前4位或前2位一致，或在同一个相似专业桶中)
     */
    private boolean isSimilarMajor(String code1, String code2) {
        if (StrUtil.isBlank(code1) || StrUtil.isBlank(code2)) {
            return false;
        }
        // 4位匹配
        if (code1.length() >= 4 && code2.length() >= 4 && code1.substring(0, 4).equals(code2.substring(0, 4))) {
            return true;
        }
        // 2位匹配
        if (code1.length() >= 2 && code2.length() >= 2 && code1.substring(0, 2).equals(code2.substring(0, 2))) {
            return true;
        }

        // 查库判断是否在同一个桶
        // 优化：先查包含 code1 的所有桶，再在内存判断 code2
        List<MajorSimilarityDO> buckets = majorSimilarityMapper.selectList(new LambdaQueryWrapper<MajorSimilarityDO>()
            .like(MajorSimilarityDO::getMajorCodes, code1));

        if (CollUtil.isNotEmpty(buckets)) {
            for (MajorSimilarityDO bucket : buckets) {
                if (StrUtil.isBlank(bucket.getMajorCodes())) {
                    continue;
                }
                // 按逗号拆分精确比对
                List<String> codes = StrUtil.split(bucket.getMajorCodes(), ',');
                if (codes.contains(code1) && codes.contains(code2)) {
                    return true;
                }
            }
        }
        return false;
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
        Integer schoolLevel = intention.getSchoolLevel();
        Integer studyMode = intention.getStudyMode(); // 0-不限 1-全 2-非
        Integer degreeType = intention.getDegreeType(); // 0-不限 1-学 2-专
        Boolean isSpecialPlan = intention.getIsSpecialPlan();

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
            if (schoolLevel != null && schoolLevel > 0) {
                boolean match = false;
                switch (schoolLevel) {
                    case 1: // 985
                        match = Boolean.TRUE.equals(school.getIs985());
                        break;
                    case 2: // 211
                        match = Boolean.TRUE.equals(school.getIs211()) || Boolean.TRUE.equals(school.getIs985());
                        break;
                    case 3: // 双一流
                        match = Boolean.TRUE.equals(school.getIsSyl()) || Boolean.TRUE.equals(school.getIs211()) || Boolean.TRUE.equals(
                            school.getIs985());
                        break;
                    case 4: // 普通
                        match = true;
                        break;
                    default:
                        match = true;
                }
                if (!match) {
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
            result.add(adj);
        }
        return result;
    }

}
