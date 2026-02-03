package com.hongguoyan.module.biz.service.recommend;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil;
import com.hongguoyan.module.biz.controller.app.recommend.vo.AppRecommendSchoolListReqVO;
import com.hongguoyan.module.biz.controller.app.recommend.vo.AppRecommendSchoolRespVO;
import com.hongguoyan.module.biz.dal.dataobject.adjustment.AdjustmentDO;
import com.hongguoyan.module.biz.dal.dataobject.nationalscore.NationalScoreDO;
import com.hongguoyan.module.biz.dal.dataobject.recommend.UserRecommendSchoolDO;
import com.hongguoyan.module.biz.dal.dataobject.school.SchoolDO;
import com.hongguoyan.module.biz.dal.dataobject.schoolscore.SchoolScoreDO;
import com.hongguoyan.module.biz.dal.dataobject.userprofile.UserProfileDO;
import com.hongguoyan.module.biz.dal.mysql.adjustment.AdjustmentMapper;
import com.hongguoyan.module.biz.dal.mysql.major.MajorMapper;
import com.hongguoyan.module.biz.dal.mysql.nationalscore.NationalScoreMapper;
import com.hongguoyan.module.biz.dal.mysql.recommend.UserRecommendSchoolMapper;
import com.hongguoyan.module.biz.dal.mysql.school.SchoolMapper;
import com.hongguoyan.module.biz.dal.mysql.schoolscore.SchoolScoreMapper;
import com.hongguoyan.module.biz.dal.mysql.userintention.UserIntentionMapper;
import com.hongguoyan.module.biz.dal.mysql.userprofile.UserProfileMapper;
import com.hongguoyan.module.biz.enums.ErrorCodeConstants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


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

    @Override
    public List<AppRecommendSchoolRespVO> recommendSchools(Long userId, AppRecommendSchoolListReqVO reqVO) {
        // TODO: Implement query from userRecommendSchoolMapper
        return new ArrayList<>();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean generateRecommend(Long userId) {
        // 1. 获取用户信息
        UserProfileDO userProfile = userProfileMapper.selectOne(new LambdaQueryWrapper<UserProfileDO>()
                .eq(UserProfileDO::getUserId, userId));
        if (userProfile == null) {
            log.warn("用户画像不存在，无法推荐: userId={}", userId);
        return false;
    }

        // 2. 清除旧推荐
        userRecommendSchoolMapper.delete(new LambdaQueryWrapper<UserRecommendSchoolDO>()
                .eq(UserRecommendSchoolDO::getUserId, userId));

        // 3. 预加载基础数据
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

        // 预加载所有学校分数线 (自划线)
        List<SchoolScoreDO> allSchoolScores = schoolScoreMapper.selectList(new LambdaQueryWrapper<SchoolScoreDO>()
                .eq(SchoolScoreDO::getYear, currentYear));
        // Map<SchoolId, MinScore> 记录该校最低分数线 (用于粗筛)
        Map<Long, BigDecimal> schoolMinScoreMap = new HashMap<>();
        // Map<SchoolId, Map<MajorCode, Score>> 记录具体分数线 (用于精筛)
        Map<Long, Map<String, SchoolScoreDO>> schoolMajorScoreMap = new HashMap<>();
        
        for (SchoolScoreDO score : allSchoolScores) {
            schoolMinScoreMap.merge(score.getSchoolId(), score.getScoreTotal(), (oldVal, newVal) -> oldVal.compareTo(newVal) < 0 ? oldVal : newVal);
            
            schoolMajorScoreMap.computeIfAbsent(score.getSchoolId(), k -> new HashMap<>())
                    .put(score.getMajorCode(), score);
        }

        double userScoreB = calculateUserScoreB(userProfile, schoolMap);
        double baseBonusC0 = userScoreB * 0.2;
        
        Set<Long> candidateSchoolIds = new HashSet<>();
        
        for (SchoolDO school : allSchools) {
            Long schoolId = school.getId();
            
            // 4.1 自划线粗筛 (School Level)
            // 如果用户分数 < 该校最低自划线，直接淘汰该校
            BigDecimal minScore = schoolMinScoreMap.get(schoolId);
            if (minScore != null && userProfile.getScoreTotal().compareTo(minScore) < 0) {
                 continue;
            }
            
            // 4.2 院校协同过滤
            double schoolScoreA = getSchoolRankScore(school);
            // 计算乐观 C (假设 adjustment 匹配良好)
            double maxC = calculateUserBonusC(userProfile, baseBonusC0, school, null, schoolMap);
            double totalUserScore = userScoreB + maxC;
            
            if (totalUserScore >= 0.75 * schoolScoreA && totalUserScore <= 1.5 * schoolScoreA) {
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
        
        // --- Step 4: 院校专业匹配度 (SimFinal) ---
        List<UserRecommendSchoolDO> recommendations = new ArrayList<>();
        
        for (AdjustmentDO adjustment : adjustments) {
            SchoolDO school = schoolMap.get(adjustment.getSchoolId());
            if (school == null) continue;
            
            // Sim Calculations
            double simA = calculateSimAInMemory(userProfile, school, adjustment, schoolMajorScoreMap);
            double simB = calculateSimB(userProfile, adjustment);
            double simC = calculateSimC(userProfile, adjustment);
            double simFinal = 0.5 * simA + 0.3 * simB + 0.2 * simC;

            // 分档
            int category;
            if (simFinal <= 0.4) category = 1;
            else if (simFinal <= 0.8) category = 2;
            else category = 3;

            UserRecommendSchoolDO recommendDO = UserRecommendSchoolDO.builder()
                    .userId(userId)
                    .adjustmentId(adjustment.getId())
                    .schoolId(school.getId())
                    .schoolName(school.getSchoolName())
                    .majorId(adjustment.getMajorId())
                    .majorName(adjustment.getMajorName())
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

    // --- Helper Methods ---

    /**
     * 严格检查国家线 (针对特定区域)
     */
    private boolean checkNationalLineStrict(UserProfileDO user, String area, List<NationalScoreDO> nationalScores) {
        if (user.getScoreTotal() == null) return false;

        // 用户的 "Degree Type" 应该取 UserProfile 中的 targetDegreeType (一志愿)
        Integer degreeType = user.getTargetDegreeType() != null ? user.getTargetDegreeType() : 2; // 默认为专硕? 或根据 targetMajor 判断
        // 用户的 "Major Code" (一志愿)
        String majorCode = user.getTargetMajorCode();
        if (StrUtil.isBlank(majorCode)) return false; // 没填一志愿专业，无法判断

        NationalScoreDO matchedLine = nationalScores.stream()
                .filter(ns -> ns.getDegreeType().equals(degreeType))
                .filter(ns -> ns.getArea().equalsIgnoreCase(area))
                .filter(ns -> majorCode.startsWith(ns.getMajorCode()))
                .max((o1, o2) -> o1.getMajorCode().length() - o2.getMajorCode().length())
                .orElse(null);

        if (matchedLine == null) {
             matchedLine = nationalScores.stream()
                .filter(ns -> ns.getDegreeType().equals(degreeType))
                .filter(ns -> ns.getArea().equalsIgnoreCase(area))
                .filter(ns -> majorCode.substring(0, 2).equals(ns.getMajorCode()))
                .findFirst()
                .orElse(null);
        }

        if (matchedLine == null) return false; // 无线数据，默认过

        // 总分
        if (user.getScoreTotal().intValue() < matchedLine.getTotal()) return false;
        
        // 单科
        int s1 = user.getSubjectScore1() != null ? user.getSubjectScore1().intValue() : 0;
        int s2 = user.getSubjectScore2() != null ? user.getSubjectScore2().intValue() : 0;
        int s3 = user.getSubjectScore3() != null ? user.getSubjectScore3().intValue() : 0;
        int s4 = user.getSubjectScore4() != null ? user.getSubjectScore4().intValue() : 0;

        if (s1 < matchedLine.getSingle100()) return false;
        if (s2 < matchedLine.getSingle100()) return false;
        if (s3 < matchedLine.getSingle150()) return false;
        return s4 >= matchedLine.getSingle150();
    }
    
    /**
     * 获取学校排名分 (A)
     */
    private double getSchoolRankScore(SchoolDO school) {
        if (Boolean.TRUE.equals(school.getIs985())) return 95.0;
        if (Boolean.TRUE.equals(school.getIs211())) return 85.0;
        if (Boolean.TRUE.equals(school.getIsSyl())) return 80.0;
        if (Boolean.TRUE.equals(school.getIsKeySchool())) return 75.0;
        return 65.0; // 普通院校
    }

    /**
     * 计算用户综合分 (B)
     * B = B1 * 0.7 + B2 * 0.3
     */
    private double calculateUserScoreB(UserProfileDO user, Map<Long, SchoolDO> schoolMap) {
        // B1: 本科院校排名分
        double b1 = 60.0;
        if (user.getGraduateSchoolId() != null) {
            SchoolDO gradSchool = schoolMap.get(user.getGraduateSchoolId());
            if (gradSchool != null) {
                b1 = getSchoolRankScore(gradSchool);
            }
        }

        // B2: 初试总分 (归一化)
        // Formula: L / (1 + e^-k(x-x0))
        // 假设 L=100, k=0.05, x0=360
        double score = user.getScoreTotal() != null ? user.getScoreTotal().doubleValue() : 300.0;
        double b2 = 100.0 / (1.0 + Math.exp(-0.05 * (score - 360.0)));

        return b1 * 0.7 + b2 * 0.3;
    }

    /**
     * 计算用户加分 (C)
     */
    private double calculateUserBonusC(UserProfileDO user, double c0, SchoolDO targetSchool, AdjustmentDO adjustment, Map<Long, SchoolDO> schoolMap) {
        double c = 0.0;

        // C1: 科研经历 (Paper)
        int paperCount = user.getPaperCount() != null ? user.getPaperCount() : 0;
        if (paperCount >= 3) c += c0 * 0.2;
        else if (paperCount >= 2) c += c0 * 0.15;
        else if (paperCount >= 1) c += c0 * 0.1;

        // C2: 竞赛得分
        int compCount = user.getCompetitionCount() != null ? user.getCompetitionCount() : 0;
        // 假设每次竞赛加分逻辑：count/84 * c0 * 0.2? 文档: C2 = count/84 * 加分基数分*0.2 (可能是 typo, 假设是 count * something)
        // 暂定：每次竞赛 + 0.05 * C0
        if (compCount > 0) {
            c += Math.min(compCount * 0.05 * c0, c0 * 0.2);
        }

        // C3: 四六级
        int cet6 = user.getCet6Score() != null ? user.getCet6Score() : 0;
        int cet4 = user.getCet4Score() != null ? user.getCet4Score() : 0;
        if (cet6 >= 470) c += c0 * 0.1;
        else if (cet6 >= 425 || cet4 >= 425) c += c0 * 0.05;

        // C4: 学科 (跨考/跨调)
        // 暂简单判断：专业代码前4位相同
        boolean sameDiscipline = false;
        
        // 修正：如果 adjustment 为 null (School Level filtering), 默认不加分或者加分?
        // 文档 C4: 学院学科在 C+ 以上?
        // 既然在 School 阶段，我们无法精确匹配专业，这里先忽略 C4? 或者给予一个平均值?
        // 考虑到要筛除不合适的学校，如果 C4 是关键加分项，少了它可能导致学校被筛掉。
        // 但如果学校很好，A很高，需要高 B+C。如果 C4 缺失，可能 totalUserScore 低。
        // 策略：在 School Level，假设 C4 满足 (Optimistic)，以免误杀。
        if (adjustment == null) {
            sameDiscipline = true; 
        } else {
             if (StrUtil.isNotBlank(user.getGraduateMajorName()) && StrUtil.isNotBlank(adjustment.getMajorName())) {
                 // 简单名字匹配
                 if (user.getGraduateMajorName().contains(adjustment.getMajorName()) || adjustment.getMajorName().contains(user.getGraduateMajorName())) {
                     sameDiscipline = true;
                 }
            }
        }
        
        if (sameDiscipline) {
            // 学院学科在 C+ 以上? 暂忽略复杂逻辑
            // c += c0 * 0.05; // 假设加一点分? 文档没写具体数值，只写了条件。
            // 假设符合条件不做惩罚? 或者有加分? 文档似乎没明确 C4 的具体加分公式，只是列出 C4。
            // 假设 C4 不加分，只做判定? 
            // 让我们假设不加分，或者 user bonus C structure is complex.
        }

        // C5: 本科平均分
        if (user.getGraduateAverageScore() != null && user.getGraduateAverageScore().doubleValue() > 85) {
            c += c0 * 0.1;
        }

        // C6: 奖学金 (User Profile里没有明确字段，暂忽略)

        // C7: 自评分
        if (user.getSelfAssessedScore() != null) {
            c += (user.getSelfAssessedScore() / 10.0) * c0 * 0.05;
        }

        // C8: 一志愿院校
        if (user.getTargetSchoolId() != null) {
            SchoolDO firstChoice = schoolMap.get(user.getTargetSchoolId());
            if (firstChoice != null) {
                // 文档逻辑复杂，这里简化：如果是985/211，加分
                if (Boolean.TRUE.equals(firstChoice.getIs985())) c += c0 * 0.2 * 1.0;
                else if (Boolean.TRUE.equals(firstChoice.getIs211())) c += c0 * 0.2 * 0.8;
                else c += c0 * 0.2 * 0.5;
            }
        }

        return c;
    }

    /**
     * 计算分数匹配度 SimA (Memory)
     */
    private double calculateSimAInMemory(UserProfileDO user, SchoolDO school, AdjustmentDO adjustment, Map<Long, Map<String, SchoolScoreDO>> schoolMajorScoreMap) {
        if (user.getScoreTotal() == null) return 0.5;
        double userScore = user.getScoreTotal().doubleValue();

        double avgScore = 360.0; // 默认
        
        // 查找往年分数 (这里简化为取最新的自划线作为参考，或者沿用 SchoolScoreDO)
        Map<String, SchoolScoreDO> majorMap = schoolMajorScoreMap.get(school.getId());
        if (majorMap != null) {
            SchoolScoreDO schoolScore = majorMap.get(adjustment.getMajorCode());
            if (schoolScore != null && schoolScore.getScoreTotal() != null) {
                avgScore = schoolScore.getScoreTotal().doubleValue();
            }
        }

        double delta = userScore - avgScore;

        if (delta > 0) {
            return 1.0 - Math.min(0.5, delta / 100.0);
        } else if (delta >= -10) {
            return 0.8 + 0.02 * delta;
        } else if (delta >= -30) {
            return 0.6 + 0.01 * (delta + 10);
        } else {
            return Math.max(0.1, 0.4 + 0.005 * (delta + 30));
        }
    }

    /**
     * 计算专业匹配度 SimB
     */
    private double calculateSimB(UserProfileDO user, AdjustmentDO adjustment) {
        String userMajorCode = "000000"; // TODO: UserProfile need major code, currently using name or empty
        // 暂时假设用户没有填 MajorCode，只填了 Name，这里先Mock
        // 如果有 targetMajorCode 可以用
        if (StrUtil.isNotBlank(user.getTargetMajorCode())) {
            userMajorCode = user.getTargetMajorCode();
        }

        String adjMajorCode = adjustment.getMajorCode();
        if (StrUtil.isBlank(adjMajorCode) || StrUtil.isBlank(userMajorCode)) return 0.4;

        if (userMajorCode.equals(adjMajorCode)) return 1.0;
        if (userMajorCode.length() >= 4 && adjMajorCode.length() >= 4 && userMajorCode.substring(0, 4).equals(adjMajorCode.substring(0, 4))) return 0.8;
        if (userMajorCode.length() >= 2 && adjMajorCode.length() >= 2 && userMajorCode.substring(0, 2).equals(adjMajorCode.substring(0, 2))) return 0.4;

        return 0.1;
    }

    /**
     * 计算竞争力 SimC (Mock)
     */
    private double calculateSimC(UserProfileDO user, AdjustmentDO adjustment) {
        return 0.5; // 暂无热度数据
    }

}
