package com.hongguoyan.module.biz.service.recommend;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil;
import com.hongguoyan.module.biz.controller.app.recommend.vo.AppRecommendSchoolRespVO;
import com.hongguoyan.module.biz.dal.dataobject.major.MajorDO;
import com.hongguoyan.module.biz.dal.dataobject.nationalscore.NationalScoreDO;
import com.hongguoyan.module.biz.dal.dataobject.school.SchoolDO;
import com.hongguoyan.module.biz.dal.dataobject.schoolscore.SchoolScoreDO;
import com.hongguoyan.module.biz.dal.dataobject.userintention.UserIntentionDO;
import com.hongguoyan.module.biz.dal.dataobject.userprofile.UserProfileDO;
import com.hongguoyan.module.biz.dal.mysql.major.MajorMapper;
import com.hongguoyan.module.biz.dal.mysql.nationalscore.NationalScoreMapper;
import com.hongguoyan.module.biz.dal.mysql.school.SchoolMapper;
import com.hongguoyan.module.biz.dal.mysql.schoolscore.SchoolScoreMapper;
import com.hongguoyan.module.biz.dal.mysql.userintention.UserIntentionMapper;
import com.hongguoyan.module.biz.dal.mysql.userprofile.UserProfileMapper;
import com.hongguoyan.module.biz.enums.ErrorCodeConstants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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

    @Override
    public List<AppRecommendSchoolRespVO> recommendSchools(Long userId) {
        // 1. 获取用户档案与意向
        UserProfileDO userProfile = userProfileMapper.selectOne(UserProfileDO::getUserId, userId);
        if (userProfile == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.USER_PROFILE_NOT_EXISTS);
        }
        UserIntentionDO userIntention = userIntentionMapper.selectOne(UserIntentionDO::getUserId, userId);
        if (userIntention == null) {
            // 如果没有意向，可以返回空或者基于默认逻辑，这里暂抛异常或返回空
            return Collections.emptyList();
        }

        // 2. 准备基础数据
        // 2.1 获取所有院校 (实际场景可能需要先根据意向省份、层次初筛，避免全量计算)
        // 这里为了演示逻辑，假设先获取符合意向省份的学校，若意向省份为空则全量
        List<SchoolDO> candidateSchools = fetchCandidateSchools(userIntention);
        if (CollUtil.isEmpty(candidateSchools)) {
            return Collections.emptyList();
        }

        // 2.2 获取用户分数 B 和加分 C (部分 C 需要依赖目标院校，先算不依赖的部分)
        BigDecimal scoreB = calculateUserComprehensiveScore(userProfile);
        BigDecimal baseBonusC0 = scoreB.multiply(new BigDecimal("0.2"));
        BigDecimal scoreC_Base = calculateUserBaseBonusScore(userProfile, baseBonusC0); // C1+C2+C3+C5+C6+C7

        List<AppRecommendSchoolRespVO> resultList = new ArrayList<>();

        // 3. 遍历院校进行过滤与打分
        for (SchoolDO school : candidateSchools) {
            // 3.1 硬性过滤 (国家线)
            // 获取该学校所在区的国家线 (需要知道用户报考的专业门类，这里假设用户的一志愿专业或者意向专业)
            // 简化逻辑：使用用户一志愿专业对应的国家线进行判定
            // 注意：实际调剂可能跨专业，这里应当遍历该学校所有招收调剂的专业。
            // 由于需求是“推荐院校”，我们需要找到该院校下适合用户的专业。
            // 假设我们推荐该学校下与用户意向匹配的专业。

            // 获取该学校所有可能匹配的专业 (这里简化为获取该学校所有有分数的专业)
            // 实际应查询 biz_school_major 或 biz_adjustment
            List<SchoolScoreDO> schoolMajors = schoolScoreMapper.selectList(SchoolScoreDO::getSchoolId, school.getId());
            // 如果没有具体分数线数据，可能无法进行精确推荐，跳过
            if (CollUtil.isEmpty(schoolMajors)) {
                continue;
            }

            for (SchoolScoreDO majorScore : schoolMajors) {
                // 3.1.1 检查是否满足国家线
                if (!checkNationalLine(userProfile, school, majorScore)) {
                    continue;
                }
                // 3.1.2 检查是否满足院校线
                if (!checkSchoolLine(userProfile, majorScore)) {
                    continue;
                }

                // 3.2 院校协同过滤
                BigDecimal scoreA = calculateSchoolRankingScore(school);
                // 计算依赖目标院校的加分 C4, C8
                BigDecimal scoreC_Target = calculateUserTargetBonusScore(userProfile, baseBonusC0, school, majorScore);
                BigDecimal totalC = scoreC_Base.add(scoreC_Target);
                BigDecimal totalUserScore = scoreB.add(totalC);

                // 过滤逻辑: 0.75 * A <= B + C <= 1.5 * A
                BigDecimal minLimit = scoreA.multiply(new BigDecimal("0.75"));
                BigDecimal maxLimit = scoreA.multiply(new BigDecimal("1.5"));

                if (totalUserScore.compareTo(minLimit) < 0 || totalUserScore.compareTo(maxLimit) > 0) {
                    continue;
                }

                // 3.3 院校专业匹配度计算
                // SimA: 分数匹配
                double simA = calculateSimA(userProfile.getScoreTotal(), majorScore.getScoreTotal());
                // SimB: 专业匹配
                double simB = calculateSimB(userProfile, majorScore);
                // SimC: 竞争力 (暂无数据，设默认值)
                double simC = 0.5;

                // SimFinal 计算 (文档未给出具体公式，假设加权平均或乘积，这里采用加权)
                // 假设 SimFinal = 0.6*SimA + 0.3*SimB + 0.1*SimC
                double simFinal = 0.6 * simA + 0.3 * simB + 0.1 * simC;

                // 3.4 封装结果
                AppRecommendSchoolRespVO respVO = new AppRecommendSchoolRespVO();
                respVO.setSchoolId(school.getId());
                respVO.setSchoolName(school.getSchoolName());
                respVO.setSchoolLogo(school.getSchoolLogo());
                respVO.setProvinceName(school.getProvinceName());
                respVO.setTags(buildTags(school));
                respVO.setMajorId(majorScore.getMajorId());
                respVO.setMajorCode(majorScore.getMajorCode());
                respVO.setMajorName(majorScore.getMajorName());
                respVO.setMatchProbability(simFinal);
                respVO.setProbabilityLabel(getProbabilityLabel(simFinal));
                respVO.setSimFinal(simFinal);
                respVO.setSimA(simA);
                respVO.setSimB(simB);
                respVO.setSimC(simC);
                respVO.setLastYearAvgScore(majorScore.getScoreTotal()); // 暂用分数线代替平均分

                // 新增字段填充
                respVO.setDifficultyLabel(getDifficultyLabel(simFinal));
                respVO.setYear(majorScore.getYear() != null ? Integer.valueOf(majorScore.getYear()) : null);
                respVO.setCollegeName(majorScore.getCollegeName());
                // 学习方式: 暂无直接字段，根据 degreeType 简单推断或给默认值，实际应查 biz_recruit / biz_adjustment
                respVO.setStudyMode(getStudyMode(majorScore.getDegreeType()));
                // 招生人数: 暂无直接字段，给默认值或留空
                respVO.setPlanCount(null); 
                // 分数详情: 暂用总分填充最低分，其他留空
                respVO.setMinScore(majorScore.getScoreTotal());
                respVO.setMedianScore(null);
                respVO.setMaxScore(null);
                // 排名差距
                respVO.setRankingGapLabel(calculateRankingGapLabel(userProfile.getGraduateSchoolId(), school.getId()));

                resultList.add(respVO);
            }
        }

        // 4. 排序与返回 (按匹配概率倒序)
        resultList.sort((o1, o2) -> Double.compare(o2.getMatchProbability(), o1.getMatchProbability()));
        return resultList;
    }

    // ================== 辅助方法 ==================

    private List<SchoolDO> fetchCandidateSchools(UserIntentionDO intention) {
        // 简单实现：根据意向省份筛选，若无意向则全量
        // 实际应结合 UserIntention 的 provinceCodes (JSON数组)
        // 这里仅做全量查询演示，实际需优化
        return schoolMapper.selectList();
    }

    /**
     * 计算用户综合分 B
     * B = B1 * 0.7 + B2 * 0.3
     */
    private BigDecimal calculateUserComprehensiveScore(UserProfileDO user) {
        // B1: 本科院校排名分 (Ranking/100)
        // 暂无排名数据，根据学校层次模拟
        BigDecimal b1 = getSchoolRankingScoreById(user.getGraduateSchoolId());

        // B2: 初试总分 (Sigmoid)
        // f(x) = L / (1 + e^-k(x-x0))
        // 假设 L=100, k=0.05, x0=300 (需根据实际总分分布调整)
        double x = user.getScoreTotal() != null ? user.getScoreTotal().doubleValue() : 0;
        double l = 100.0;
        double k = 0.05;
        double x0 = 300.0;
        double b2Val = l / (1 + Math.exp(-k * (x - x0)));
        BigDecimal b2 = new BigDecimal(b2Val);

        return b1.multiply(new BigDecimal("0.7")).add(b2.multiply(new BigDecimal("0.3")));
    }

    /**
     * 计算用户基础加分 C (不依赖目标院校的部分)
     * C1, C2, C3, C5, C6, C7
     */
    private BigDecimal calculateUserBaseBonusScore(UserProfileDO user, BigDecimal c0) {
        BigDecimal total = BigDecimal.ZERO;

        // C1: 科研经历 (Paper Count)
        int paperCount = user.getPaperCount() != null ? user.getPaperCount() : 0;
        if (paperCount >= 3) total = total.add(c0.multiply(new BigDecimal("0.2")));
        else if (paperCount >= 2) total = total.add(c0.multiply(new BigDecimal("0.15")));
        else if (paperCount >= 1) total = total.add(c0.multiply(new BigDecimal("0.1")));

        // C2: 竞赛得分 (Competition)
        // 假设 competitionCount 是省二级以上数量
        int compCount = user.getCompetitionCount() != null ? user.getCompetitionCount() : 0;
        // 公式: count/84 * C0 * 0.2 (84可能是归一化因子)
        BigDecimal c2 = new BigDecimal(compCount).divide(new BigDecimal("84"), 4, RoundingMode.HALF_UP)
                .multiply(c0).multiply(new BigDecimal("0.2"));
        total = total.add(c2);

        // C3: 英语得分
        int cet6 = user.getCet6Score() != null ? user.getCet6Score() : 0;
        int cet4 = user.getCet4Score() != null ? user.getCet4Score() : 0;
        if (cet6 >= 470) {
            total = total.add(c0.multiply(new BigDecimal("0.1")));
        } else if (cet6 >= 425 || cet4 >= 425) {
            total = total.add(c0.multiply(new BigDecimal("0.05")));
        }

        // C5: 本科平均分
        if (user.getGraduateAverageScore() != null && user.getGraduateAverageScore().compareTo(new BigDecimal("85")) > 0) {
            total = total.add(c0.multiply(new BigDecimal("0.1")));
        }

        // C6: 奖学金 (暂无字段，跳过或假设)

        // C7: 自评分
        if (user.getSelfAssessedScore() != null) {
            // score/10 * C0 * 0.05
            BigDecimal c7 = new BigDecimal(user.getSelfAssessedScore()).divide(BigDecimal.TEN)
                    .multiply(c0).multiply(new BigDecimal("0.05"));
            total = total.add(c7);
        }

        return total;
    }

    /**
     * 计算依赖目标院校的加分 C (C4, C8)
     */
    private BigDecimal calculateUserTargetBonusScore(UserProfileDO user, BigDecimal c0, SchoolDO targetSchool, SchoolScoreDO targetMajor) {
        BigDecimal total = BigDecimal.ZERO;

        // C4: 用户所属学科分 (跨考/跨调)
        // 简化：假设不跨考不跨调加分 (需比对 user.graduateMajorCode 和 targetMajor.majorCode)
        // 暂略复杂逻辑，给个默认值
        boolean isCross = false; // 需实现判断逻辑
        if (!isCross) {
             // total = total.add(...)
        }

        // C8: 一志愿报考院校
        // 逻辑：报考院校 ranking > 3.3 (需换算)
        // 这里简化：如果一志愿就是该目标院校，加分
        if (user.getTargetSchoolId() != null && user.getTargetSchoolId().equals(targetSchool.getId())) {
             total = total.add(c0.multiply(new BigDecimal("0.2"))); // 简化系数
        }

        return total;
    }

    /**
     * 计算院校排名分 A (Ranking/100)
     * 模拟实现
     */
    private BigDecimal calculateSchoolRankingScore(SchoolDO school) {
        double score = 60.0;
        if (Boolean.TRUE.equals(school.getIs985())) score += 30;
        else if (Boolean.TRUE.equals(school.getIs211())) score += 20;
        else if (Boolean.TRUE.equals(school.getIsSyl())) score += 10;
        
        return new BigDecimal(score).divide(new BigDecimal("100")); // 归一化到 0-1 左右? 
        // 文档公式: Ranking/100. 假设 Ranking 是 0-100分。
        // 如果 Ranking 是排名(1,2,3)，则需要反转。假设是评分。
    }

    /**
     * 模拟通过ID获取学校排名分
     */
    private BigDecimal getSchoolRankingScoreById(Long schoolId) {
        if (schoolId == null) return new BigDecimal("0.5");
        SchoolDO school = schoolMapper.selectById(schoolId);
        return school != null ? calculateSchoolRankingScore(school) : new BigDecimal("0.5");
    }

    /**
     * 检查国家线
     */
    private boolean checkNationalLine(UserProfileDO user, SchoolDO school, SchoolScoreDO majorScore) {
        // 1. 确定分区 (A/B)
        String area = school.getProvinceArea(); // "A" or "B"
        if (StrUtil.isBlank(area)) area = "A"; // 默认A区

        // 2. 获取国家线
        // 需根据 majorScore.getMajorCode() (前2位或4位) 匹配 NationalScoreDO
        // 尝试匹配 4位，若无匹配 2位
        String majorCode = majorScore.getMajorCode();
        if (StrUtil.isBlank(majorCode)) return true;

        NationalScoreDO nationalLine = null;
        
        // 尝试 4位匹配
        if (majorCode.length() >= 4) {
             nationalLine = nationalScoreMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<NationalScoreDO>()
                        .eq(NationalScoreDO::getYear, majorScore.getYear())
                        .eq(NationalScoreDO::getArea, area)
                        .eq(NationalScoreDO::getDegreeType, majorScore.getDegreeType())
                        .eq(NationalScoreDO::getMajorCode, majorCode.substring(0, 4))
                        .last("LIMIT 1")
            );
        }
        
        // 尝试 2位匹配
        if (nationalLine == null && majorCode.length() >= 2) {
             nationalLine = nationalScoreMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<NationalScoreDO>()
                        .eq(NationalScoreDO::getYear, majorScore.getYear())
                        .eq(NationalScoreDO::getArea, area)
                        .eq(NationalScoreDO::getDegreeType, majorScore.getDegreeType())
                        .eq(NationalScoreDO::getMajorCode, majorCode.substring(0, 2))
                        .last("LIMIT 1")
            );
        }
        
        if (nationalLine == null) return true; // 无国家线数据，默认通过或跳过

        // 3. 比较分数
        if (user.getScoreTotal() != null && nationalLine.getTotal() != null) {
            if (user.getScoreTotal().compareTo(new BigDecimal(nationalLine.getTotal())) < 0) return false;
        }
        // 单科比较略
        return true;
    }

    /**
     * 检查院校线
     */
    private boolean checkSchoolLine(UserProfileDO user, SchoolScoreDO majorScore) {
        if (majorScore.getScoreTotal() == null) return true;
        return user.getScoreTotal().compareTo(majorScore.getScoreTotal()) >= 0;
    }

    /**
     * SimA: 分数匹配
     */
    private double calculateSimA(BigDecimal userScore, BigDecimal avgScore) {
        if (userScore == null || avgScore == null) return 0.5;
        double user = userScore.doubleValue();
        double avg = avgScore.doubleValue();
        double gap = avg - user;

        if (user > avg) {
            // 总分 > 平均分
            return 1.0 - Math.min(0.5, (user - avg) / 100.0);
        } else {
            // 总分 <= 平均分
            if (gap <= 10) return 0.8 - gap * 0.02;
            else if (gap <= 30) return 0.6 - (gap - 10) * 0.01;
            else return Math.max(0.1, 0.4 - (gap - 30) * 0.005);
        }
    }

    /**
     * SimB: 专业匹配
     */
    private double calculateSimB(UserProfileDO user, SchoolScoreDO targetMajor) {
        double simB1 = 0.0;
        double simB2 = 0.0;

        // 本科专业匹配 (简化：代码前4位相同)
        String graduateMajorCode = null;
        if (user.getGraduateMajorId() != null) {
            MajorDO graduateMajor = majorMapper.selectById(user.getGraduateMajorId());
            if (graduateMajor != null) {
                graduateMajorCode = graduateMajor.getCode();
            }
        }

        if (graduateMajorCode != null && targetMajor.getMajorCode() != null
                && graduateMajorCode.length() >= 4 && targetMajor.getMajorCode().length() >= 4
                && graduateMajorCode.substring(0, 4).equals(targetMajor.getMajorCode().substring(0, 4))) {
            simB1 = 0.5;
        }

        // 一志愿专业匹配
        if (user.getTargetMajorCode() != null && targetMajor.getMajorCode() != null
                && user.getTargetMajorCode().equals(targetMajor.getMajorCode())) {
            simB2 = 0.5;
        }
        
        return simB1 + simB2;
    }

    private String getProbabilityLabel(double simFinal) {
        if (simFinal <= 0.4) return "冲刺";
        if (simFinal <= 0.8) return "稳妥";
        return "保底";
    }
    
    private String getDifficultyLabel(double simFinal) {
        if (simFinal <= 0.4) return "难";
        if (simFinal <= 0.8) return "中";
        return "易";
    }
    
    private String getStudyMode(Integer degreeType) {
        // 简单映射，实际应查表
        if (degreeType == null) return "全日制";
        return "全日制"; // 默认
    }
    
    private String calculateRankingGapLabel(Long userSchoolId, Long targetSchoolId) {
        BigDecimal userScore = getSchoolRankingScoreById(userSchoolId);
        BigDecimal targetScore = getSchoolRankingScoreById(targetSchoolId);
        
        // 假设分数范围 0.5 - 1.0 (50-100分归一化)
        // 差距 > 0.3 较大, > 0.1 适中, else 较小
        // 注意方向：如果是逆向考 (双非->985)，target > user，差距为正
        BigDecimal gap = targetScore.subtract(userScore);
        
        if (gap.compareTo(new BigDecimal("0.3")) > 0) return "较大";
        if (gap.compareTo(new BigDecimal("0.1")) > 0) return "适中";
        return "较小";
    }

    private List<String> buildTags(SchoolDO school) {
        List<String> tags = new ArrayList<>();
        if (Boolean.TRUE.equals(school.getIs985())) tags.add("985");
        if (Boolean.TRUE.equals(school.getIs211())) tags.add("211");
        if (Boolean.TRUE.equals(school.getIsSyl())) tags.add("双一流");
        return tags;
    }
}
