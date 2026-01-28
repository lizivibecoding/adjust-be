package com.hongguoyan.module.biz.service.adjustment;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.adjustment.AdjustmentDO;
import com.hongguoyan.module.biz.dal.dataobject.school.SchoolDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
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

    private static final Pattern SPLIT_PATTERN = Pattern.compile("[\\n;,，；]+");

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
            int heatCap = 1000;
            for (AppAdjustmentSearchRespVO item : majorList) {
                Integer viewCount = item.getViewCount();
                int heat = 0;
                if (viewCount != null && viewCount > 0) {
                    heat = (int) Math.round(viewCount * 100.0 / heatCap);
                    if (heat > 100) {
                        heat = 100;
                    }
                }
                item.setHeat(heat);
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
    public AppAdjustmentOptionsRespVO getAdjustmentOptions(AppAdjustmentOptionsReqVO reqVO) {
        AppAdjustmentOptionsRespVO respVO = new AppAdjustmentOptionsRespVO();
        List<Integer> years = adjustmentMapper.selectOptionYears(reqVO.getSchoolId(), reqVO.getMajorId());
        List<AppAdjustmentCollegeOptionRespVO> colleges = adjustmentMapper.selectOptionColleges(reqVO.getSchoolId(), reqVO.getMajorId());
        respVO.setYearList(years != null ? years : Collections.emptyList());
        respVO.setCollegeList(colleges != null ? colleges : Collections.emptyList());
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
            respVO.setIs985(school.getIs_985());
            respVO.setIsSyl(school.getIsSyl());
            respVO.setIs211(school.getIs_211());
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
            direction.setBalanceCount(adjustment.getBalanceCount());
            direction.setBalanceLeft(adjustment.getBalanceLeft());
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