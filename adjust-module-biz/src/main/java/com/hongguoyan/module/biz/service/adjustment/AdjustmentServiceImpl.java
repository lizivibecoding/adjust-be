package com.hongguoyan.module.biz.service.adjustment;

import cn.hutool.core.util.StrUtil;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.*;
import com.hongguoyan.module.biz.controller.app.school.vo.AppSchoolAdjustmentPageReqVO;
import com.hongguoyan.module.biz.controller.app.school.vo.AppSchoolAdjustmentRespVO;
import com.hongguoyan.module.biz.dal.dataobject.area.AreaDO;
import com.hongguoyan.module.biz.dal.dataobject.adjustment.AdjustmentDO;
import com.hongguoyan.module.biz.dal.dataobject.school.SchoolDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.dal.mysql.area.AreaMapper;
import com.hongguoyan.module.biz.dal.mysql.adjustment.AdjustmentMapper;
import com.hongguoyan.module.biz.dal.mysql.major.MajorMapper;
import com.hongguoyan.module.biz.dal.mysql.school.SchoolMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 调剂 Service 实现类
 *
 * @author hgy
 */
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

    private static final Pattern SPLIT_PATTERN = Pattern.compile("[\\n;,，；]+");
    private static final int DEFAULT_STATS_YEAR = 2025;

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
    public AppAdjustmentSearchTabRespVO getAdjustmentSearchPage(AppAdjustmentSearchReqVO reqVO) {
        if (reqVO != null && StrUtil.isNotBlank(reqVO.getKeyword())) {
            reqVO.setKeyword(buildBooleanKeyword(reqVO.getKeyword()));
        }
        String tabType = reqVO != null ? reqVO.getTabType() : null;
        boolean schoolTab = "school".equalsIgnoreCase(tabType);
        AppAdjustmentSearchTabRespVO respVO = new AppAdjustmentSearchTabRespVO();
        if (schoolTab) {
            PageResult<AppAdjustmentSearchSchoolRespVO> pageResult = adjustmentMapper.selectSearchSchoolPage(reqVO);
            respVO.setTotal(pageResult.getTotal());
            respVO.setSchoolList(pageResult.getList());
            respVO.setMajorList(Collections.emptyList());
        } else {
            PageResult<AppAdjustmentSearchRespVO> pageResult = adjustmentMapper.selectSearchMajorPage(reqVO);
            List<AppAdjustmentSearchRespVO> majorList = pageResult.getList();
            for (AppAdjustmentSearchRespVO item : majorList) {
                Integer viewCount = item.getViewCount();
                // Keep consistent with UI: heat in [0, 100], same as viewCount capped by SQL
                item.setHeat(viewCount != null ? viewCount : 0);
            }
            respVO.setTotal(pageResult.getTotal());
            respVO.setMajorList(majorList);
            respVO.setSchoolList(Collections.emptyList());
        }
        return respVO;
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
    public AppAdjustmentFilterConfigRespVO getAdjustmentFilterConfig() {
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

        // 2) studyMode
        groups.add(group("studyMode", "学习方式", Arrays.asList(
                option("全日制", "全日制"),
                option("非全日制", "非全日制")
        )));

        // 3) degreeType
        groups.add(group("degreeType", "学术类型", Arrays.asList(
                option("2", "学硕"),
                option("1", "专硕")
        )));

        // 4) schoolFeature (985/211/双一流/其他)
        groups.add(group("schoolFeature", "学校属性", Arrays.asList(
                option("985", "985"),
                option("211", "211"),
                option("syl", "双一流"),
                option("other", "其他")
        )));

        // 5) adjustType
        groups.add(group("adjustType", "调剂类型", Arrays.asList(
                option("1", "校内调剂"),
                option("2", "校外调剂")
        )));

        // 6) specialPlan
        groups.add(group("specialPlan", "专项计划", Collections.singletonList(
                option("1", "只看专项计划")
        )));

        // 7) adjustStatus
        groups.add(group("adjustStatus", "招生状态", Arrays.asList(
                option("0", "已经停招"),
                option("1", "正常招生")
        )));

        // 8) mathSubject
        groups.add(group("mathSubject", "数学科目", Arrays.asList(
                option("301", "数学（一）"),
                option("302", "数学（二）"),
                option("303", "数学（三）")
        )));

        // 9) foreignSubject
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
    public AppAdjustmentDetailRespVO getAdjustmentDetail(AppAdjustmentDetailReqVO reqVO) {
        List<AdjustmentDO> list = adjustmentMapper.selectList(new LambdaQueryWrapperX<AdjustmentDO>()
                .eq(AdjustmentDO::getSchoolId, reqVO.getSchoolId())
                .eq(AdjustmentDO::getMajorId, reqVO.getMajorId())
                .eq(AdjustmentDO::getCollegeId, reqVO.getCollegeId())
                .eq(AdjustmentDO::getYear, reqVO.getYear())
                .eq(AdjustmentDO::getStudyMode, reqVO.getStudyMode()));
        if (list == null || list.isEmpty()) {
            throw exception(ADJUSTMENT_NOT_EXISTS);
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
        for (AppAdjustmentSearchRespVO item : list) {
            Integer viewCount = item.getViewCount();
            item.setHeat(viewCount != null ? viewCount : 0);
        }
        return pageResult;
    }

    @Override
    public PageResult<AppSchoolAdjustmentRespVO> getSchoolAdjustmentPage(@Valid AppSchoolAdjustmentPageReqVO reqVO) {
        return adjustmentMapper.selectSchoolAdjustmentPage(reqVO);
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
