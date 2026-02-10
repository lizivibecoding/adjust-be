package com.hongguoyan.module.biz.service.adjustmentadmit;

import cn.hutool.core.collection.CollUtil;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.AppAdjustmentAnalysisReqVO;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.AppAdjustmentAnalysisRespVO;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.AppSameScoreAxisRespVO;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.AppSameScoreItemRespVO;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.AppSameScorePageReqVO;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.AppSameScoreStatItemRespVO;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.AppSameScoreStatReqVO;
import com.hongguoyan.module.biz.controller.app.adjustmentadmit.vo.AppAdjustmentAdmitListItemRespVO;
import com.hongguoyan.module.biz.controller.app.adjustmentadmit.vo.AppAdjustmentAdmitListReqVO;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import com.hongguoyan.module.biz.controller.app.adjustmentadmit.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.adjustmentadmit.AdjustmentAdmitDO;
import com.hongguoyan.module.biz.dal.dataobject.school.SchoolDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.adjustmentadmit.AdjustmentAdmitMapper;
import com.hongguoyan.module.biz.dal.mysql.school.SchoolMapper;
import com.hongguoyan.module.biz.dal.dataobject.userprofile.UserProfileDO;
import com.hongguoyan.module.biz.service.userprofile.UserProfileService;
import com.hongguoyan.module.biz.service.vipbenefit.VipBenefitService;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.*;

/**
 * 调剂录取名单 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class AdjustmentAdmitServiceImpl implements AdjustmentAdmitService {

    @Resource
    private AdjustmentAdmitMapper adjustmentAdmitMapper;
    @Resource
    private SchoolMapper schoolMapper;
    @Resource
    private UserProfileService userProfileService;
    @Resource
    private VipBenefitService vipBenefitService;

    @Override
    public Long createAdjustmentAdmit(AppAdjustmentAdmitSaveReqVO createReqVO) {
        // 插入
        AdjustmentAdmitDO adjustmentAdmit = BeanUtils.toBean(createReqVO, AdjustmentAdmitDO.class);
        adjustmentAdmitMapper.insert(adjustmentAdmit);

        // 返回
        return adjustmentAdmit.getId();
    }

    @Override
    public void updateAdjustmentAdmit(AppAdjustmentAdmitSaveReqVO updateReqVO) {
        // 校验存在
        validateAdjustmentAdmitExists(updateReqVO.getId());
        // 更新
        AdjustmentAdmitDO updateObj = BeanUtils.toBean(updateReqVO, AdjustmentAdmitDO.class);
        adjustmentAdmitMapper.updateById(updateObj);
    }

    @Override
    public void deleteAdjustmentAdmit(Long id) {
        // 校验存在
        validateAdjustmentAdmitExists(id);
        // 删除
        adjustmentAdmitMapper.deleteById(id);
    }

    @Override
        public void deleteAdjustmentAdmitListByIds(List<Long> ids) {
        // 删除
        adjustmentAdmitMapper.deleteByIds(ids);
        }


    private void validateAdjustmentAdmitExists(Long id) {
        if (adjustmentAdmitMapper.selectById(id) == null) {
            throw exception(ADJUSTMENT_ADMIT_NOT_EXISTS);
        }
    }

    @Override
    public AdjustmentAdmitDO getAdjustmentAdmit(Long id) {
        return adjustmentAdmitMapper.selectById(id);
    }

    @Override
    public PageResult<AdjustmentAdmitDO> getAdjustmentAdmitPage(AppAdjustmentAdmitPageReqVO pageReqVO) {
        return adjustmentAdmitMapper.selectPage(pageReqVO);
    }

    @Override
    public List<AppAdjustmentAdmitListItemRespVO> getAdmitList(Long userId, AppAdjustmentAdmitListReqVO reqVO) {
        vipBenefitService.checkEnabledOrThrow(userId, BENEFIT_KEY_VIEW_ADMIT_LIST);
        LambdaQueryWrapperX<AdjustmentAdmitDO> wrapper = new LambdaQueryWrapperX<>();
        // 注意：LambdaQueryWrapperX 未重写 select 的返回类型，因此这里不要链式赋值
        wrapper.select(AdjustmentAdmitDO::getCandidateName,
                AdjustmentAdmitDO::getFirstSchoolName,
                AdjustmentAdmitDO::getFirstScore,
                AdjustmentAdmitDO::getRetestScore,
                AdjustmentAdmitDO::getTotalScore);
        wrapper.eq(AdjustmentAdmitDO::getDeleted, false)
                .eq(AdjustmentAdmitDO::getSchoolId, reqVO.getSchoolId())
                .eq(AdjustmentAdmitDO::getCollegeId, reqVO.getCollegeId())
                .eq(AdjustmentAdmitDO::getMajorId, reqVO.getMajorId())
                .eq(AdjustmentAdmitDO::getYear, reqVO.getYear())
                .eq(AdjustmentAdmitDO::getStudyMode, reqVO.getStudyMode());
        // TODO 先忽略
        // wrapper.eqIfPresent(AdjustmentAdmitDO::getDirectionId, reqVO.getDirectionId());
        wrapper.orderByDesc(AdjustmentAdmitDO::getTotalScore)
                .orderByDesc(AdjustmentAdmitDO::getFirstScore)
                .orderByDesc(AdjustmentAdmitDO::getId);
        List<AdjustmentAdmitDO> list = adjustmentAdmitMapper.selectList(wrapper);
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        List<AppAdjustmentAdmitListItemRespVO> resp = new ArrayList<>(list.size());
        for (AdjustmentAdmitDO item : list) {
            AppAdjustmentAdmitListItemRespVO vo = new AppAdjustmentAdmitListItemRespVO();
            vo.setCandidateName(item.getCandidateName());
            vo.setFirstSchoolName(item.getFirstSchoolName());
            vo.setFirstScore(item.getFirstScore());
            vo.setRetestScore(item.getRetestScore());
            vo.setTotalScore(item.getTotalScore());
            resp.add(vo);
        }
        return resp;
    }

    @Override
    public AppAdjustmentAnalysisRespVO getAnalysis(Long userId, AppAdjustmentAnalysisReqVO reqVO) {
        vipBenefitService.checkEnabledOrThrow(userId, BENEFIT_KEY_VIEW_ANALYSIS);
        LambdaQueryWrapperX<AdjustmentAdmitDO> wrapper = new LambdaQueryWrapperX<>();
        wrapper.select(AdjustmentAdmitDO::getFirstSchoolId,
                AdjustmentAdmitDO::getFirstScore);
        wrapper.eq(AdjustmentAdmitDO::getDeleted, false)
                .eq(AdjustmentAdmitDO::getSchoolId, reqVO.getSchoolId())
                .eq(AdjustmentAdmitDO::getCollegeId, reqVO.getCollegeId())
                .eq(AdjustmentAdmitDO::getMajorId, reqVO.getMajorId())
                .eq(AdjustmentAdmitDO::getYear, reqVO.getYear())
                .eq(AdjustmentAdmitDO::getStudyMode, reqVO.getStudyMode());
        // TODO: 先忽略
        // wrapper.eqIfPresent(AdjustmentAdmitDO::getDirectionId, reqVO.getDirectionId());
        List<AdjustmentAdmitDO> list = adjustmentAdmitMapper.selectList(wrapper);

        AppAdjustmentAnalysisRespVO respVO = new AppAdjustmentAnalysisRespVO();
        if (list == null || list.isEmpty()) {
            respVO.setSection(Collections.emptyList());
            respVO.setLevel(Collections.emptyList());
            respVO.setDetail(null);
            return respVO;
        }

        Long schoolId = reqVO.getSchoolId();

        // split first-choice vs adjust
        List<BigDecimal> firstChoiceScores = new ArrayList<>();
        List<BigDecimal> adjustScores = new ArrayList<>();
        LinkedHashSet<Long> adjustFirstSchoolIds = new LinkedHashSet<>();
        for (AdjustmentAdmitDO item : list) {
            Long firstSchoolId = item.getFirstSchoolId();
            BigDecimal firstScore = item.getFirstScore();
            boolean isFirstChoice = firstSchoolId != null && firstSchoolId.equals(schoolId);
            if (isFirstChoice) {
                if (firstScore != null) {
                    firstChoiceScores.add(firstScore);
                } else {
                    firstChoiceScores.add(null);
                }
            } else {
                if (firstScore != null) {
                    adjustScores.add(firstScore);
                }
                if (firstSchoolId != null) {
                    adjustFirstSchoolIds.add(firstSchoolId);
                }
            }
        }

        // detail
        AppAdjustmentAnalysisRespVO.Detail detail = new AppAdjustmentAnalysisRespVO.Detail();
        long firstChoiceCount = countNotNullOrAll(firstChoiceScores);
        long adjustCount = list.size() - firstChoiceCount;
        detail.setFirstChoiceCount(firstChoiceCount);
        detail.setFirstChoiceMaxScore(maxScore(firstChoiceScores));
        detail.setFirstChoiceMinScore(minScore(firstChoiceScores));
        detail.setAdjustCount(adjustCount);
        detail.setAdjustMinScore(minScore(adjustScores));
        detail.setAdjustMedianScore(medianScore(adjustScores));
        detail.setAdjustMaxScore(maxScore(adjustScores));
        respVO.setDetail(detail);

        // section distribution (only adjust group)
        respVO.setSection(buildScoreSection(adjustScores));

        // level distribution (distinct first school count in adjust group)
        respVO.setLevel(buildLevel(adjustFirstSchoolIds));

        return respVO;
    }

    @Override
    public PageResult<AppSameScoreItemRespVO> getSameScorePage(Long userId, AppSameScorePageReqVO reqVO) {
        vipBenefitService.checkEnabledOrThrow(userId, BENEFIT_KEY_VIEW_SAME_SCORE);
        return adjustmentAdmitMapper.selectSameScorePage(reqVO);
    }

    @Override
    public AppSameScoreAxisRespVO getSameScoreAxis(Long userId) {
        vipBenefitService.checkEnabledOrThrow(userId, BENEFIT_KEY_VIEW_SAME_SCORE);
        UserProfileDO profile = userProfileService.getUserProfileByUserId(userId);
        if (profile == null) {
            throw exception(CANDIDATE_PROFILES_NOT_EXISTS);
        }
        BigDecimal scoreTotal = profile.getScoreTotal();
        if (scoreTotal == null) {
            throw exception(CANDIDATE_SCORE_TOTAL_NOT_EXISTS);
        }

        int base = scoreTotal.setScale(0, RoundingMode.FLOOR).intValue();
        int min = Math.max(0, base - 20);
        int max = base + 20;

        AppSameScoreAxisRespVO respVO = new AppSameScoreAxisRespVO();
        respVO.setBaseScore(base);
        respVO.setMinScore(min);
        respVO.setMaxScore(max);
        respVO.setBeginScore(min);
        respVO.setEndScore(max);
        return respVO;
    }

    @Override
    public List<AppSameScoreStatItemRespVO> getSameScoreStat(Long userId, AppSameScoreStatReqVO reqVO) {
        vipBenefitService.checkEnabledOrThrow(userId, BENEFIT_KEY_VIEW_SAME_SCORE);
        List<AppSameScoreStatItemRespVO> list = adjustmentAdmitMapper.selectSameScoreStat(reqVO);
        return list != null ? list : Collections.emptyList();
    }

    private long countNotNullOrAll(List<BigDecimal> scores) {
        // list elements are always added; treat size as count
        return scores != null ? scores.size() : 0L;
    }

    private BigDecimal minScore(List<BigDecimal> scores) {
        if (scores == null || scores.isEmpty()) {
            return null;
        }
        BigDecimal min = null;
        for (BigDecimal v : scores) {
            if (v == null) {
                continue;
            }
            if (min == null || v.compareTo(min) < 0) {
                min = v;
            }
        }
        return min;
    }

    private BigDecimal maxScore(List<BigDecimal> scores) {
        if (scores == null || scores.isEmpty()) {
            return null;
        }
        BigDecimal max = null;
        for (BigDecimal v : scores) {
            if (v == null) {
                continue;
            }
            if (max == null || v.compareTo(max) > 0) {
                max = v;
            }
        }
        return max;
    }

    private BigDecimal medianScore(List<BigDecimal> scores) {
        if (scores == null || scores.isEmpty()) {
            return null;
        }
        List<BigDecimal> list = new ArrayList<>();
        for (BigDecimal v : scores) {
            if (v != null) {
                list.add(v);
            }
        }
        if (list.isEmpty()) {
            return null;
        }
        list.sort(BigDecimal::compareTo);
        int n = list.size();
        if ((n & 1) == 1) {
            return list.get(n / 2);
        }
        BigDecimal a = list.get(n / 2 - 1);
        BigDecimal b = list.get(n / 2);
        return a.add(b).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
    }

    private List<AppAdjustmentAnalysisRespVO.NameValue> buildScoreSection(List<BigDecimal> scores) {
        if (scores == null || scores.isEmpty()) {
            return Collections.emptyList();
        }
        // convert to integer range by floor
        List<Integer> ints = new ArrayList<>();
        for (BigDecimal v : scores) {
            if (v == null) {
                continue;
            }
            ints.add(v.setScale(0, RoundingMode.FLOOR).intValue());
        }
        if (ints.isEmpty()) {
            return Collections.emptyList();
        }
        int min = Collections.min(ints);
        int max = Collections.max(ints);
        int start = (min / 5) * 5;
        int span = (max + 1) - start;
        int step = (int) Math.ceil(span / 5.0);
        if (step <= 0) {
            step = 1;
        }
        List<AppAdjustmentAnalysisRespVO.NameValue> result = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            int left = start + i * step;
            int right = start + (i + 1) * step;
            if (i == 4) {
                right = max + 1;
            }
            long count = 0L;
            for (int v : ints) {
                if (v >= left && v < right) {
                    count++;
                }
            }
            AppAdjustmentAnalysisRespVO.NameValue item = new AppAdjustmentAnalysisRespVO.NameValue();
            item.setName(left + "-" + right);
            item.setValue(count);
            result.add(item);
        }
        return result;
    }

    private List<AppAdjustmentAnalysisRespVO.LevelItem> buildLevel(Set<Long> firstSchoolIds) {
        if (firstSchoolIds == null || firstSchoolIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> ids = new ArrayList<>(firstSchoolIds);
        List<SchoolDO> schools = schoolMapper.selectBatchIds(ids);
        Map<Long, SchoolDO> map = new HashMap<>();
        if (schools != null) {
            for (SchoolDO s : schools) {
                map.put(s.getId(), s);
            }
        }

        long c985 = 0L;
        long c211 = 0L;
        long csyl = 0L;
        long cOther = 0L;
        for (Long id : ids) {
            SchoolDO s = map.get(id);
            boolean is985 = s != null && Boolean.TRUE.equals(s.getIs985());
            boolean is211 = s != null && Boolean.TRUE.equals(s.getIs211());
            boolean isSyl = s != null && Boolean.TRUE.equals(s.getIsSyl());
            if (is985) {
                c985++;
            } else if (is211) {
                c211++;
            } else if (isSyl) {
                csyl++;
            } else {
                cOther++;
            }
        }

        List<AppAdjustmentAnalysisRespVO.LevelItem> result = new ArrayList<>(4);
        result.add(levelItem("985工程", "", c985));
        result.add(levelItem("211工程", "不含985", c211));
        result.add(levelItem("双一流", "不含985、211", csyl));
        result.add(levelItem("普通院校", "", cOther));
        return result;
    }

    private AppAdjustmentAnalysisRespVO.LevelItem levelItem(String name, String subName, long value) {
        AppAdjustmentAnalysisRespVO.LevelItem item = new AppAdjustmentAnalysisRespVO.LevelItem();
        item.setName(name);
        item.setSubName(subName);
        item.setValue(value);
        return item;
    }

}
