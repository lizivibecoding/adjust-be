package com.hongguoyan.module.biz.service.adjustment;

import cn.hutool.core.util.StrUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.time.Year;
import java.util.regex.Pattern;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.*;
import com.hongguoyan.module.biz.controller.app.school.vo.AppSchoolAdjustmentPageReqVO;
import com.hongguoyan.module.biz.controller.app.school.vo.AppSchoolAdjustmentRespVO;
import com.hongguoyan.module.biz.dal.dataobject.area.AreaDO;
import com.hongguoyan.module.biz.dal.dataobject.adjustment.AdjustmentDO;
import com.hongguoyan.module.biz.dal.dataobject.major.MajorDO;
import com.hongguoyan.module.biz.dal.dataobject.school.SchoolDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.dal.mysql.area.AreaMapper;
import com.hongguoyan.module.biz.dal.mysql.adjustment.AdjustmentMapper;
import com.hongguoyan.module.biz.dal.mysql.adjustment.dto.BizMajorKeyDTO;
import com.hongguoyan.module.biz.dal.mysql.adjustment.dto.RecruitSnapshotRowDTO;
import com.hongguoyan.module.biz.dal.mysql.major.MajorMapper;
import com.hongguoyan.module.biz.dal.mysql.school.SchoolMapper;
import com.hongguoyan.module.biz.dal.mysql.schoolmajor.SchoolMajorMapper;
import com.hongguoyan.module.biz.service.vipbenefit.VipBenefitService;
import com.hongguoyan.module.biz.dal.dataobject.userprofile.UserProfileDO;
import com.hongguoyan.module.biz.service.userprofile.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.*;

/**
 * 调剂 Service 实现类
 *
 * @author hgy
 */
@Slf4j
@Service
@Validated
public class AdjustmentServiceImpl implements AdjustmentService {

    @Resource
    private AdjustmentMapper adjustmentMapper;
    @Resource
    private SchoolMapper schoolMapper;
    @Resource
    private MajorMapper majorMapper;
    @Resource
    private AreaMapper areaMapper;
    @Resource
    private SchoolMajorMapper schoolMajorMapper;
    @Resource
    private VipBenefitService vipBenefitService;
    @Resource
    private UserProfileService userProfileService;

    private static final Pattern SPLIT_PATTERN = Pattern.compile("[\\n;,，；]+");
    private static final int DEFAULT_STATS_YEAR = 2025;

    private static final int HEAT_MAX = 98;
    /**
     * Exponential saturation factor (tau). Smaller => faster to reach cap.
     */
    private static final double HEAT_TAU = 150D;

    private static int calcHeat(Long hotScore) {
        long score = hotScore != null ? hotScore : 0L;
        if (score <= 0) {
            return 0;
        }
        // heat = floor(HEAT_MAX * (1 - exp(-score / tau)))
        double heat = HEAT_MAX * (1D - Math.exp(-score / HEAT_TAU));
        int value = (int) Math.floor(heat);
        if (value < 0) {
            return 0;
        }
        return Math.min(HEAT_MAX, value);
    }

    @Override
    public Long createAdjustment(AppAdjustmentSaveReqVO createReqVO) {
        // 插入
        AdjustmentDO adjustment = BeanUtils.toBean(createReqVO, AdjustmentDO.class);
        adjustmentMapper.insert(adjustment);

        // 返回
        return adjustment.getId();
    }

    @Override
    public void updateAdjustment(AppAdjustmentSaveReqVO updateReqVO) {
        // 校验存在
        validateAdjustmentExists(updateReqVO.getId());
        // 更新
        AdjustmentDO updateObj = BeanUtils.toBean(updateReqVO, AdjustmentDO.class);
        adjustmentMapper.updateById(updateObj);
    }

    @Override
    public void deleteAdjustment(Long id) {
        // 校验存在
        validateAdjustmentExists(id);
        // 删除
        adjustmentMapper.deleteById(id);
    }

    @Override
        public void deleteAdjustmentListByIds(List<Long> ids) {
        // 删除
        adjustmentMapper.deleteByIds(ids);
        }


    private void validateAdjustmentExists(Long id) {
        if (adjustmentMapper.selectById(id) == null) {
            throw exception(ADJUSTMENT_NOT_EXISTS);
        }
    }

    @Override
    public AdjustmentDO getAdjustment(Long id) {
        return adjustmentMapper.selectById(id);
    }

    @Override
    public PageResult<AdjustmentDO> getAdjustmentPage(AppAdjustmentPageReqVO pageReqVO) {
        return adjustmentMapper.selectPage(pageReqVO);
    }

    @Override
    public AppAdjustmentSearchTabRespVO getAdjustmentSearchPage(Long userId, AppAdjustmentSearchReqVO reqVO) {
        validateMajorCategoryAccess(userId, reqVO != null ? reqVO.getMajorCode() : null);
        if (reqVO != null && StrUtil.isNotBlank(reqVO.getKeyword())) {
            reqVO.setKeyword(buildBooleanKeyword(reqVO.getKeyword()));
        }
        String tabType = reqVO != null ? reqVO.getTabType() : null;
        boolean schoolTab = "school".equalsIgnoreCase(tabType);
        AppAdjustmentSearchTabRespVO respVO = new AppAdjustmentSearchTabRespVO();
        if (schoolTab) {
            PageResult<AppAdjustmentSearchSchoolRespVO> pageResult = adjustmentMapper.selectSearchSchoolPage(reqVO);
            fillRecruitNumberForSchoolList(pageResult.getList());
            respVO.setTotal(pageResult.getTotal());
            respVO.setSchoolList(pageResult.getList());
            respVO.setMajorList(Collections.emptyList());
        } else {
            PageResult<AppAdjustmentSearchRespVO> pageResult = adjustmentMapper.selectSearchMajorPage(reqVO);
            List<AppAdjustmentSearchRespVO> majorList = pageResult.getList();
            fillRecruitNumberAndHighAdjustChance(majorList);
            for (AppAdjustmentSearchRespVO item : majorList) {
                item.setHeat(calcHeat(item.getHotScore()));
            }
            respVO.setTotal(pageResult.getTotal());
            respVO.setMajorList(majorList);
            respVO.setSchoolList(Collections.emptyList());
        }
        return respVO;
    }

    private void validateMajorCategoryAccess(Long userId, String majorCode) {
        // TODO VIP-BYPASS: restore major category access check (major_category_open)
        return;
        /*
        if (userId == null) {
            // allow anonymous access for now (no place to persist defaults)
            return;
        }
        if (StrUtil.isBlank(majorCode)) {
            return;
        }
        String code = majorCode.trim();
        Set<String> opened = vipBenefitService.getConsumedUniqueKeys(userId, BENEFIT_KEY_MAJOR_CATEGORY_OPEN);
        if (opened.contains(code)) {
            return;
        }
        throw exception(VIP_MAJOR_CATEGORY_NOT_OPENED);
         */
    }

    private String resolveDefaultMajorCategoryCode(Long userId) {
        UserProfileDO profile = userProfileService.getUserProfileByUserId(userId);
        if (profile == null || StrUtil.isBlank(profile.getTargetMajorCode())) {
            return null;
        }
        String s = profile.getTargetMajorCode().trim();
        if (s.length() >= 2) {
            return s.substring(0, 2);
        }
        return s;
    }

    @Override
    public AppAdjustmentSuggestRespVO getAdjustmentSuggest(String keyword) {
        AppAdjustmentSuggestRespVO respVO = new AppAdjustmentSuggestRespVO();
        if (StrUtil.isBlank(keyword)) {
            respVO.setSuggestions(Collections.emptyList());
            return respVO;
        }
        int limit = 10;
        LinkedHashSet<String> suggestions = new LinkedHashSet<>();
        suggestions.addAll(schoolMapper.selectSuggestSchoolNames(keyword, limit));
        suggestions.addAll(majorMapper.selectSuggestMajorCodes(keyword, limit));
        suggestions.addAll(majorMapper.selectSuggestMajorNames(keyword, limit));
        List<String> result = new ArrayList<>();
        for (String suggestion : suggestions) {
            result.add(suggestion);
            if (result.size() >= limit) {
                break;
            }
        }
        respVO.setSuggestions(result);
        return respVO;
    }

    @Override
    public AppAdjustmentFilterConfigRespVO getAdjustmentFilterConfig(String majorCode) {
        AppAdjustmentFilterConfigRespVO respVO = new AppAdjustmentFilterConfigRespVO();
        List<AppAdjustmentFilterConfigRespVO.Group> groups = new ArrayList<>();

        // 1) province (A/B)
        List<AreaDO> areas = areaMapper.selectAllOrderByAreaAndCode();
        AppAdjustmentFilterConfigRespVO.Group provinceGroup = new AppAdjustmentFilterConfigRespVO.Group();
        provinceGroup.setKey("province");
        provinceGroup.setName("目标省份");
        List<AppAdjustmentFilterConfigRespVO.Group> areaChildren = new ArrayList<>();
        areaChildren.add(buildProvinceAreaGroup("A", "A区", areas));
        areaChildren.add(buildProvinceAreaGroup("B", "B区", areas));
        provinceGroup.setChildren(areaChildren);
        groups.add(provinceGroup);

        // 2) target major (level2)
        AppAdjustmentFilterConfigRespVO.Group level2MajorGroup = new AppAdjustmentFilterConfigRespVO.Group();
        level2MajorGroup.setKey("level2MajorCodes");
        level2MajorGroup.setName("一级学科");
        List<AppAdjustmentFilterConfigRespVO.Option> level2MajorOptions = new ArrayList<>();
        if (StrUtil.isNotBlank(majorCode)) {
            List<MajorDO> level2List = majorMapper.selectListByLevelAndParentCode(majorCode, 2, null);
            if (level2List != null && !level2List.isEmpty()) {
                for (MajorDO item : level2List) {
                    if (item == null || StrUtil.isBlank(item.getCode()) || StrUtil.isBlank(item.getName())) {
                        continue;
                    }
                    level2MajorOptions.add(option(item.getCode(), item.getName()));
                }
            }
        }
        level2MajorGroup.setOptions(level2MajorOptions);
        groups.add(level2MajorGroup);

        // 3) studyMode
        groups.add(group("studyMode", "学习方式", Arrays.asList(
                option("全日制", "全日制"),
                option("非全日制", "非全日制")
        )));

        // 4) degreeType
        groups.add(group("degreeType", "学术类型", Arrays.asList(
                option("2", "学硕"),
                option("1", "专硕")
        )));

        // 5) schoolFeature (985/211/双一流/其他)
        groups.add(group("schoolFeature", "学校属性", Arrays.asList(
                option("985", "985"),
                option("211", "211"),
                option("syl", "双一流"),
                option("other", "其他")
        )));


        // 7) specialPlan
        groups.add(group("specialPlan", "专项计划", Collections.singletonList(
                option("1", "只看专项计划")
        )));


        // 9) mathSubject
        groups.add(group("mathSubject", "数学科目", Arrays.asList(
                option("301", "数学（一）"),
                option("302", "数学（二）"),
                option("303", "数学（三）")
        )));

        // 10) foreignSubject
        groups.add(group("foreignSubject", "外语科目", Arrays.asList(
                option("201", "英语（一）"),
                option("204", "英语（二）"),
                option("202", "俄语"),
                option("203", "日语"),
                option("261", "二外英语"),
                option("211", "翻译硕士（英语）")
        )));

        respVO.setGroups(groups);
        return respVO;
    }

    private AppAdjustmentFilterConfigRespVO.Group buildProvinceAreaGroup(String area,
                                                                        String areaName,
                                                                        List<AreaDO> areas) {
        AppAdjustmentFilterConfigRespVO.Group group = new AppAdjustmentFilterConfigRespVO.Group();
        group.setKey(area);
        group.setName(areaName);
        List<AppAdjustmentFilterConfigRespVO.Option> options = new ArrayList<>();
        // keep same as prototype: "A区全部"/"B区全部"
        options.add(option("", areaName + "全部"));
        for (AreaDO item : areas) {
            if (item == null || !area.equalsIgnoreCase(item.getArea())) {
                continue;
            }
            options.add(option(item.getCode(), item.getName()));
        }
        group.setOptions(options);
        return group;
    }

    private AppAdjustmentFilterConfigRespVO.Group group(String key,
                                                       String name,
                                                       List<AppAdjustmentFilterConfigRespVO.Option> options) {
        AppAdjustmentFilterConfigRespVO.Group group = new AppAdjustmentFilterConfigRespVO.Group();
        group.setKey(key);
        group.setName(name);
        group.setOptions(options);
        return group;
    }

    private AppAdjustmentFilterConfigRespVO.Option option(String code, String name) {
        AppAdjustmentFilterConfigRespVO.Option option = new AppAdjustmentFilterConfigRespVO.Option();
        option.setCode(code);
        option.setName(name);
        return option;
    }

    @Override
    public AppAdjustmentOptionsRespVO getAdjustmentOptions(AppAdjustmentOptionsReqVO reqVO) {
        AppAdjustmentOptionsRespVO respVO = new AppAdjustmentOptionsRespVO();
        List<Integer> years = adjustmentMapper.selectOptionYears(reqVO.getSchoolId(), reqVO.getMajorId());
        List<AppAdjustmentCollegeOptionRespVO> colleges = adjustmentMapper.selectOptionColleges(reqVO.getSchoolId(), reqVO.getMajorId());
        respVO.setYearList(years != null ? years : Collections.emptyList());
        respVO.setCollegeList(colleges != null ? colleges : Collections.emptyList());
        if (reqVO.getCollegeId() != null) {
            List<String> studyModes = adjustmentMapper.selectOptionStudyModes(reqVO.getSchoolId(), reqVO.getCollegeId(), reqVO.getMajorId());
            respVO.setStudyModeList(studyModes != null ? studyModes : Collections.emptyList());
        } else {
            respVO.setStudyModeList(Collections.emptyList());
        }
        return respVO;
    }

    @Override
    public AppAdjustmentDetailRespVO getAdjustmentDetail(Long userId, AppAdjustmentDetailReqVO reqVO) {
        List<AdjustmentDO> list = adjustmentMapper.selectList(new LambdaQueryWrapperX<AdjustmentDO>()
                .eq(AdjustmentDO::getSchoolId, reqVO.getSchoolId())
                .eq(AdjustmentDO::getMajorId, reqVO.getMajorId())
                .eq(AdjustmentDO::getCollegeId, reqVO.getCollegeId())
                .eq(AdjustmentDO::getYear, reqVO.getYear())
                .eq(AdjustmentDO::getStudyMode, reqVO.getStudyMode()));
        if (list == null || list.isEmpty()) {
            throw exception(ADJUSTMENT_NOT_EXISTS);
        }

        // best-effort: detail view should +1 view_count and +1 hot_score for the major
        try {
            schoolMajorMapper.incrViewAndHotByBizKey(reqVO.getSchoolId(), reqVO.getCollegeId(), reqVO.getMajorId(),
                    1, 1L);
        } catch (Exception e) {
            log.warn("Failed to incr view/hot score. schoolId={}, collegeId={}, majorId={}",
                    reqVO.getSchoolId(), reqVO.getCollegeId(), reqVO.getMajorId(), e);
        }

        // 按 directionCode 从小到大排序（兼容 00/01/02 以及 1/2/10）
        list.sort((a, b) -> compareDirectionCode(a.getDirectionCode(), b.getDirectionCode()));

        AdjustmentDO first = list.get(0);
        AppAdjustmentDetailRespVO respVO = new AppAdjustmentDetailRespVO();
        respVO.setSchoolId(reqVO.getSchoolId());
        respVO.setCollegeId(reqVO.getCollegeId());
        respVO.setMajorId(reqVO.getMajorId());
        respVO.setYear(reqVO.getYear());
        respVO.setStudyMode(reqVO.getStudyMode());

        // 公共信息：优先从学校表补充，其次取 adjustment 冗余字段
        SchoolDO school = schoolMapper.selectById(reqVO.getSchoolId());
        if (school != null) {
            respVO.setSchoolName(school.getSchoolName());
            respVO.setSchoolLogo(school.getSchoolLogo());
            respVO.setProvinceName(school.getProvinceName());
            respVO.setSchoolType(school.getSchoolType());
            respVO.setIs985(school.getIs985());
            respVO.setIsSyl(school.getIsSyl());
            respVO.setIs211(school.getIs211());
            respVO.setIsOrdinary(school.getIsOrdinary());
        } else {
            respVO.setSchoolName(first.getSchoolName());
        }
        respVO.setCollegeName(first.getCollegeName());
        respVO.setMajorCode(first.getMajorCode());
        respVO.setMajorName(first.getMajorName());
        respVO.setDegreeType(first.getDegreeType());

        List<AppAdjustmentDirectionDetailRespVO> directions = new ArrayList<>(list.size());
        for (AdjustmentDO adjustment : list) {
            AppAdjustmentDirectionDetailRespVO direction = new AppAdjustmentDirectionDetailRespVO();
            direction.setAdjustmentId(adjustment.getId());
            direction.setDirectionCode(adjustment.getDirectionCode());
            direction.setDirectionName(adjustment.getDirectionName());
            direction.setAdjustCount(adjustment.getAdjustCount());
            direction.setAdjustLeft(adjustment.getAdjustLeft());
            direction.setStudyYears(adjustment.getStudyYears());
            direction.setTuitionFee(adjustment.getTuitionFee());
            direction.setRetestRatio(adjustment.getRetestRatio());
            direction.setRetestWeight(adjustment.getRetestWeight());
            direction.setRetestBooks(splitToList(adjustment.getRetestBooks()));
            direction.setRequireScore(adjustment.getRequireScore());
            direction.setRequireMajor(adjustment.getRequireMajor());
            direction.setSubjectCode1(adjustment.getSubjectCode1());
            direction.setSubjectName1(adjustment.getSubjectName1());
            direction.setSubjectNote1(adjustment.getSubjectNote1());
            direction.setSubjectCode2(adjustment.getSubjectCode2());
            direction.setSubjectName2(adjustment.getSubjectName2());
            direction.setSubjectNote2(adjustment.getSubjectNote2());
            direction.setSubjectCode3(adjustment.getSubjectCode3());
            direction.setSubjectName3(adjustment.getSubjectName3());
            direction.setSubjectNote3(adjustment.getSubjectNote3());
            direction.setSubjectCode4(adjustment.getSubjectCode4());
            direction.setSubjectName4(adjustment.getSubjectName4());
            direction.setSubjectNote4(adjustment.getSubjectNote4());
            direction.setSubjectCombinations(adjustment.getSubjectCombinations());
            direction.setContact(adjustment.getContact());
            direction.setRemark(adjustment.getRemark());
            direction.setPublishTime(adjustment.getPublishTime());
            direction.setViewCount(adjustment.getViewCount());
            directions.add(direction);
        }
        respVO.setDirections(directions);
        return respVO;
    }

    @Override
    public AppAdjustmentUpdateStatsRespVO getAdjustmentUpdateStats() {
        AppAdjustmentUpdateStatsRespVO respVO = adjustmentMapper.selectUpdateStats(DEFAULT_STATS_YEAR);
        if (respVO == null) {
            respVO = new AppAdjustmentUpdateStatsRespVO();
            respVO.setYear(DEFAULT_STATS_YEAR);
            respVO.setTodayUpdateCount(0L);
            respVO.setTodayUpdateSchoolCount(0L);
            respVO.setTotalCount(0L);
            respVO.setTotalSchoolCount(0L);
        }
        return respVO;
    }

    @Override
    public PageResult<AppAdjustmentSearchRespVO> getHotRankingPage(@Valid AppAdjustmentHotRankingReqVO reqVO) {
        PageResult<AppAdjustmentSearchRespVO> pageResult = adjustmentMapper.selectHotRankingPage(reqVO);
        List<AppAdjustmentSearchRespVO> list = pageResult.getList();
        if (list == null || list.isEmpty()) {
            return pageResult;
        }
        fillRecruitNumberAndHighAdjustChance(list);
        for (AppAdjustmentSearchRespVO item : list) {
            item.setHeat(calcHeat(item.getHotScore()));
        }
        return pageResult;
    }

    @Override
    public PageResult<AppSchoolAdjustmentRespVO> getSchoolAdjustmentPage(@Valid AppSchoolAdjustmentPageReqVO reqVO) {
        return adjustmentMapper.selectSchoolAdjustmentPage(reqVO);
    }

    private void fillRecruitNumberAndHighAdjustChance(List<AppAdjustmentSearchRespVO> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        int currentYear = Year.now().getValue();

        // 1) recruit number (batch)
        List<BizMajorKeyDTO> keys = new ArrayList<>(list.size());
        for (AppAdjustmentSearchRespVO item : list) {
            if (item == null || item.getSchoolId() == null || item.getCollegeId() == null
                    || item.getMajorId() == null || item.getYear() == null) {
                continue;
            }
            BizMajorKeyDTO key = new BizMajorKeyDTO();
            key.setSchoolId(item.getSchoolId());
            key.setCollegeId(item.getCollegeId());
            key.setMajorId(item.getMajorId());
            key.setYear(item.getYear());
            keys.add(key);
        }
        Map<String, Integer> recruitMap = loadRecruitNumberByKeys(keys, currentYear);
        for (AppAdjustmentSearchRespVO item : list) {
            if (item == null) {
                continue;
            }
            String k = key(item.getSchoolId(), item.getCollegeId(), item.getMajorId(), item.getYear());
            if (k != null) {
                item.setRecruitNumber(recruitMap.get(k));
            }
        }

        // 2) high adjust chance (batch; only meaningful for current year)
        List<BizMajorKeyDTO> triplets = new ArrayList<>();
        for (AppAdjustmentSearchRespVO item : list) {
            if (item == null || item.getYear() == null || item.getYear() != currentYear) {
                continue;
            }
            if (item.getSchoolId() == null || item.getCollegeId() == null || item.getMajorId() == null) {
                continue;
            }
            BizMajorKeyDTO t = new BizMajorKeyDTO();
            t.setSchoolId(item.getSchoolId());
            t.setCollegeId(item.getCollegeId());
            t.setMajorId(item.getMajorId());
            triplets.add(t);
        }
        Set<String> hasHistory = new HashSet<>();
        if (!triplets.isEmpty()) {
            List<BizMajorKeyDTO> hits = adjustmentMapper.selectMajorsHasHistoryAdjust(triplets, currentYear);
            for (BizMajorKeyDTO hit : hits) {
                hasHistory.add(key(hit.getSchoolId(), hit.getCollegeId(), hit.getMajorId(), null));
            }
        }
        for (AppAdjustmentSearchRespVO item : list) {
            if (item == null || item.getYear() == null) {
                continue;
            }
            if (item.getYear() != currentYear) {
                item.setHighAdjustChance(false);
                continue;
            }
            String k = key(item.getSchoolId(), item.getCollegeId(), item.getMajorId(), null);
            item.setHighAdjustChance(k != null && hasHistory.contains(k));
        }
    }

    private void fillRecruitNumberForSchoolList(List<AppAdjustmentSearchSchoolRespVO> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        int currentYear = Year.now().getValue();
        List<BizMajorKeyDTO> keys = new ArrayList<>(list.size());
        for (AppAdjustmentSearchSchoolRespVO item : list) {
            if (item == null || item.getSchoolId() == null || item.getCollegeId() == null
                    || item.getMajorId() == null || item.getYear() == null) {
                continue;
            }
            BizMajorKeyDTO key = new BizMajorKeyDTO();
            key.setSchoolId(item.getSchoolId());
            key.setCollegeId(item.getCollegeId());
            key.setMajorId(item.getMajorId());
            key.setYear(item.getYear());
            keys.add(key);
        }
        Map<String, Integer> recruitMap = loadRecruitNumberByKeys(keys, currentYear);
        for (AppAdjustmentSearchSchoolRespVO item : list) {
            if (item == null) {
                continue;
            }
            String k = key(item.getSchoolId(), item.getCollegeId(), item.getMajorId(), item.getYear());
            if (k != null) {
                item.setRecruitNumber(recruitMap.get(k));
            }
        }
    }

    private Map<String, Integer> loadRecruitNumberByKeys(List<BizMajorKeyDTO> keys, int currentYear) {
        if (keys == null || keys.isEmpty()) {
            return Collections.emptyMap();
        }
        List<BizMajorKeyDTO> current = new ArrayList<>();
        List<BizMajorKeyDTO> history = new ArrayList<>();
        for (BizMajorKeyDTO k : keys) {
            if (k == null || k.getYear() == null) {
                continue;
            }
            if (k.getYear() == currentYear) {
                current.add(k);
            } else {
                history.add(k);
            }
        }

        Map<String, Integer> result = new HashMap<>();
        if (!current.isEmpty()) {
            List<RecruitSnapshotRowDTO> rows = adjustmentMapper.selectRecruitSnapshotLatest(current);
            for (RecruitSnapshotRowDTO row : rows) {
                result.put(key(row.getSchoolId(), row.getCollegeId(), row.getMajorId(), row.getYear()),
                        row.getRecruitNumber());
            }
        }
        if (!history.isEmpty()) {
            List<RecruitSnapshotRowDTO> rows = adjustmentMapper.selectRecruitSnapshotEarliest(history);
            for (RecruitSnapshotRowDTO row : rows) {
                result.put(key(row.getSchoolId(), row.getCollegeId(), row.getMajorId(), row.getYear()),
                        row.getRecruitNumber());
            }
        }
        return result;
    }

    private String key(Long schoolId, Long collegeId, Long majorId, Integer year) {
        if (schoolId == null || collegeId == null || majorId == null) {
            return null;
        }
        return schoolId + ":" + collegeId + ":" + majorId + ":" + (year != null ? year : "");
    }

    private List<String> splitToList(String text) {
        if (StrUtil.isBlank(text)) {
            return Collections.emptyList();
        }
        String normalized = text.trim();
        String[] parts = SPLIT_PATTERN.split(normalized);
        List<String> result = new ArrayList<>();
        for (String part : parts) {
            String item = StrUtil.trimToNull(part);
            if (item != null) {
                result.add(item);
            }
        }
        return result;
    }

    private int compareDirectionCode(String a, String b) {
        if (a == null && b == null) {
            return 0;
        }
        if (a == null) {
            return 1;
        }
        if (b == null) {
            return -1;
        }
        Integer ai = tryParseInt(a);
        Integer bi = tryParseInt(b);
        if (ai != null && bi != null) {
            return ai.compareTo(bi);
        }
        return a.compareTo(b);
    }

    private Integer tryParseInt(String s) {
        String t = s.trim();
        if (t.isEmpty()) {
            return null;
        }
        for (int i = 0; i < t.length(); i++) {
            if (!Character.isDigit(t.charAt(i))) {
                return null;
            }
        }
        try {
            return Integer.parseInt(t);
        } catch (NumberFormatException ignore) {
            return null;
        }
    }

    private String buildBooleanKeyword(String keyword) {
        List<String> tokens = StrUtil.splitTrim(keyword, ' ');
        if (tokens == null || tokens.isEmpty()) {
            return keyword;
        }
        StringBuilder builder = new StringBuilder();
        for (String token : tokens) {
            if (StrUtil.isBlank(token)) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append('+').append(token);
        }
        return builder.length() > 0 ? builder.toString() : keyword;
    }

}
