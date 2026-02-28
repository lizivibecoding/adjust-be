package com.hongguoyan.module.biz.service.adjustment;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.cache.CacheEvictFacade;
import com.hongguoyan.module.biz.cache.CacheNames;
import com.hongguoyan.module.biz.cache.adjustment.AdjustmentDetailCache;
import com.hongguoyan.module.biz.cache.adjustment.SchoolAdjustmentCache;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentUpsertByDirectionIdReqVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentUpsertByNameReqVO;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.*;
import com.hongguoyan.module.biz.controller.app.school.vo.AppSchoolAdjustmentPageReqVO;
import com.hongguoyan.module.biz.controller.app.school.vo.AppSchoolAdjustmentRespVO;
import com.hongguoyan.module.biz.dal.dataobject.adjustment.AdjustmentDO;
import com.hongguoyan.module.biz.dal.dataobject.area.AreaDO;
import com.hongguoyan.module.biz.dal.dataobject.major.MajorDO;
import com.hongguoyan.module.biz.dal.dataobject.school.SchoolDO;
import com.hongguoyan.module.biz.dal.dataobject.schoolcollege.SchoolCollegeDO;
import com.hongguoyan.module.biz.dal.dataobject.schooldirection.SchoolDirectionDO;
import com.hongguoyan.module.biz.dal.dataobject.useradjustment.UserAdjustmentDO;
import com.hongguoyan.module.biz.dal.dataobject.userprofile.UserProfileDO;
import com.hongguoyan.module.biz.dal.mysql.adjustment.AdjustmentMapper;
import com.hongguoyan.module.biz.dal.mysql.adjustment.dto.AdjustmentYearOptionRowDTO;
import com.hongguoyan.module.biz.dal.mysql.adjustment.dto.BizMajorKeyDTO;
import com.hongguoyan.module.biz.dal.mysql.adjustment.dto.BizMajorStudyKeyDTO;
import com.hongguoyan.module.biz.dal.mysql.adjustment.dto.RecruitSnapshotRowDTO;
import com.hongguoyan.module.biz.dal.mysql.area.AreaMapper;
import com.hongguoyan.module.biz.dal.mysql.major.MajorMapper;
import com.hongguoyan.module.biz.dal.mysql.school.SchoolMapper;
import com.hongguoyan.module.biz.dal.mysql.schoolcollege.SchoolCollegeMapper;
import com.hongguoyan.module.biz.dal.mysql.schooldirection.SchoolDirectionMapper;
import com.hongguoyan.module.biz.service.projectconfig.ProjectConfigService;
import com.hongguoyan.module.biz.service.userprofile.UserProfileService;
import com.hongguoyan.module.biz.service.vipbenefit.VipBenefitService;
import com.hongguoyan.framework.security.core.util.SecurityFrameworkUtils;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.ADJUSTMENT_NOT_EXISTS;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.VIP_MAJOR_CATEGORY_NOT_OPENED;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.BENEFIT_KEY_MAJOR_CATEGORY_OPEN;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.VIEW_SCHOOL_ADJUSTMENT_3Y;

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
    private VipBenefitService vipBenefitService;
    @Resource
    private UserProfileService userProfileService;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private ProjectConfigService projectConfigService;
    @Resource
    private AdjustmentDetailCache adjustmentDetailCache;
    @Resource
    private SchoolAdjustmentCache schoolAdjustmentCache;
    @Resource
    private SchoolDirectionMapper schoolDirectionMapper;
    @Resource
    private SchoolCollegeMapper schoolCollegeMapper;
    @Resource
    private CacheEvictFacade cacheEvictFacade;

    private static final Pattern SPLIT_PATTERN = Pattern.compile("[\\n;,，；]+");
    /**
     * Default opened major category code for new users with no opened codes.
     * "01" => 哲学
     */
    private static final String DEFAULT_MAJOR_CATEGORY_CODE = "01";

    private static final int HEAT_MAX = 98;
    /**
     * Exponential saturation factor (tau). Smaller => faster to reach cap.
     */
    private static final double HEAT_TAU = 50D;

    /**
     * Preview limit for users with no opened major categories.
     */
    private static final int DEFAULT_MAJOR_CATEGORY_PREVIEW_LIMIT = 10;

    private static final String HINT_CURRENT_YEAR_HAS_QUOTA = "今年有调剂名额";
    private static final String HINT_ADJUST_CHANCE_HIGH = "以往年分析今年调剂概率大";

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
    public Long upsertByDirectionId(@Valid AdjustmentUpsertByDirectionIdReqVO reqVO) {
        if (reqVO == null || reqVO.getDirectionId() == null) {
            throw exception(ADJUSTMENT_NOT_EXISTS);
        }
        Integer year = reqVO.getYear();
        SchoolDirectionDO direction = schoolDirectionMapper.selectById(reqVO.getDirectionId());
        if (direction == null) {
            throw exception(ADJUSTMENT_NOT_EXISTS);
        }
        AdjustmentDO incoming = buildIncomingByDirection(year, reqVO.getSourceType(), direction,
                reqVO.getAdjustCount(), reqVO.getAdjustLeft(),
                reqVO.getPublishTime(), reqVO.getSourceUrl(), reqVO.getRemark(), reqVO.getRetestBooks());
        // source_type=3 时，标记为运营写入，避免用户覆盖
        markThirdPartyWriterIfNeeded(incoming, true, SecurityFrameworkUtils.getLoginUserId());
        return upsertByBizKey(incoming);
    }

    @Override
    public Long upsertByName(@Valid AdjustmentUpsertByNameReqVO reqVO) {
        Integer activeYear = projectConfigService.getActiveYear();
        Long schoolId = resolveSchoolIdByCode(reqVO.getSchoolCode());
        SchoolDO school = schoolId != null ? schoolMapper.selectById(schoolId) : null;
        if (schoolId == null || school == null) {
            throw exception(ADJUSTMENT_NOT_EXISTS);
        }

        Long collegeId = resolveCollegeIdByName(schoolId, activeYear, reqVO.getCollegeName());
        SchoolCollegeDO college = collegeId != null ? schoolCollegeMapper.selectById(collegeId) : null;
        if (collegeId == null || college == null) {
            throw exception(ADJUSTMENT_NOT_EXISTS);
        }

        MajorDO major = resolveMajorByCodeAndName(activeYear, reqVO.getMajorCode(), reqVO.getMajorName());
        if (major == null || major.getId() == null) {
            throw exception(ADJUSTMENT_NOT_EXISTS);
        }

        SchoolDirectionDO direction = resolveDirectionByName(schoolId, collegeId, major.getId(), activeYear,
                reqVO.getStudyMode(), reqVO.getDirectionName());
        Long directionId = direction != null ? direction.getId() : 0L;
        String directionCode = direction != null ? direction.getDirectionCode() : "00";
        String subjects = direction != null ? direction.getSubjects() : null;

        AdjustmentDO incoming = new AdjustmentDO();
        incoming.setId(null);
        incoming.setYear(reqVO.getYear());
        incoming.setSourceType(reqVO.getSourceType());
        incoming.setSourceUrl(StrUtil.blankToDefault(StrUtil.trimToNull(reqVO.getSourceUrl()), ""));
        incoming.setSchoolId(schoolId);
        incoming.setSchoolName(StrUtil.blankToDefault(school.getSchoolName(), ""));
        incoming.setCollegeId(collegeId);
        incoming.setCollegeName(StrUtil.blankToDefault(college.getName(), ""));
        incoming.setMajorId(major.getId());
        incoming.setMajorCode(StrUtil.blankToDefault(major.getCode(), ""));
        incoming.setMajorName(StrUtil.blankToDefault(major.getName(), ""));
        incoming.setDegreeType(major.getDegreeType() != null ? major.getDegreeType() : 0);
        incoming.setStudyMode(reqVO.getStudyMode());
        incoming.setDirectionId(directionId);
        incoming.setDirectionCode(StrUtil.blankToDefault(directionCode, "00"));
        incoming.setDirectionName(StrUtil.blankToDefault(reqVO.getDirectionName(), ""));
        incoming.setSubjects(subjects);
        incoming.setAdjustCount(reqVO.getAdjustCount());
        incoming.setAdjustLeft(reqVO.getAdjustLeft());
        incoming.setRemark(StrUtil.blankToDefault(reqVO.getRemark(), ""));
        incoming.setPublishTime(reqVO.getPublishTime() != null ? reqVO.getPublishTime() : LocalDateTime.now());
        incoming.setRetestBooks(writeJsonArray(reqVO.getRetestBooks()));
        incoming.setStatus(1);
        incoming.setViewCount(0);
        incoming.setHotScore(0L);
        // source_type=3 时，标记为运营写入，避免用户覆盖
        markThirdPartyWriterIfNeeded(incoming, true, SecurityFrameworkUtils.getLoginUserId());
        return upsertByBizKey(incoming);
    }

    @Override
    public void syncFromUserAdjustment(UserAdjustmentDO userAdjustment) {
        if (userAdjustment == null || userAdjustment.getDirectionId() == null || userAdjustment.getYear() == null) {
            return;
        }
        SchoolDirectionDO direction = schoolDirectionMapper.selectById(userAdjustment.getDirectionId());
        if (direction == null) {
            return;
        }
        AdjustmentDO incoming = buildIncomingByDirection(userAdjustment.getYear(), 3, direction,
                userAdjustment.getAdjustCount(), userAdjustment.getAdjustLeft(),
                userAdjustment.getPublishTime(), userAdjustment.getSourceUrl(), userAdjustment.getRemark(), null);
        // 运营创建的 user_adjustment 会写 auditUserId；三方覆盖时运营 > 用户
        boolean fromAdmin = userAdjustment.getAuditUserId() != null;
        Long writerId = fromAdmin ? userAdjustment.getAuditUserId() : userAdjustment.getUserId();
        markThirdPartyWriterIfNeeded(incoming, fromAdmin, writerId);
        upsertByBizKey(incoming);
    }

    private AdjustmentDO buildIncomingByDirection(Integer year,
                                                  Integer sourceType,
                                                  SchoolDirectionDO direction,
                                                  Integer adjustCount,
                                                  Integer adjustLeft,
                                                  LocalDateTime publishTime,
                                                  String sourceUrl,
                                                  String remark,
                                                  List<String> retestBooks) {
        Long schoolId = direction.getSchoolId();
        Long collegeId = direction.getCollegeId();
        Long majorId = direction.getMajorId();
        Integer studyMode = direction.getStudyMode() != null ? direction.getStudyMode() : 1;

        SchoolDO school = schoolId != null ? schoolMapper.selectById(schoolId) : null;
        SchoolCollegeDO college = collegeId != null ? schoolCollegeMapper.selectById(collegeId) : null;
        MajorDO major = majorId != null ? majorMapper.selectById(majorId) : null;

        AdjustmentDO incoming = new AdjustmentDO();
        incoming.setId(null);
        incoming.setYear(year);
        incoming.setSourceType(sourceType);
        incoming.setSourceUrl(StrUtil.blankToDefault(StrUtil.trimToNull(sourceUrl), ""));
        incoming.setSchoolId(schoolId);
        incoming.setSchoolName(school != null ? StrUtil.blankToDefault(school.getSchoolName(), "") : "");
        incoming.setCollegeId(collegeId != null ? collegeId : 0L);
        incoming.setCollegeName(college != null ? StrUtil.blankToDefault(college.getName(), "") : "");
        incoming.setMajorId(majorId);
        incoming.setMajorCode(major != null ? StrUtil.blankToDefault(major.getCode(), "") : "");
        incoming.setMajorName(major != null ? StrUtil.blankToDefault(major.getName(), "") : "");
        incoming.setDegreeType(major != null && major.getDegreeType() != null ? major.getDegreeType() : 0);
        incoming.setDirectionId(direction.getId());
        incoming.setDirectionCode(StrUtil.blankToDefault(direction.getDirectionCode(), "00"));
        incoming.setDirectionName(StrUtil.blankToDefault(direction.getDirectionName(), ""));
        incoming.setStudyMode(studyMode);
        incoming.setSubjects(direction.getSubjects());
        incoming.setAdjustCount(adjustCount != null ? adjustCount : 0);
        incoming.setAdjustLeft(adjustLeft != null ? adjustLeft : 0);
        incoming.setRemark(StrUtil.blankToDefault(remark, ""));
        incoming.setPublishTime(publishTime != null ? publishTime : LocalDateTime.now());
        incoming.setRetestBooks(writeJsonArray(retestBooks));
        incoming.setStatus(1);
        incoming.setViewCount(0);
        incoming.setHotScore(0L);
        return incoming;
    }

    private Long resolveSchoolIdByCode(String schoolCode) {
        String code = StrUtil.trimToNull(schoolCode);
        if (code == null) {
            return null;
        }
        SchoolDO school = schoolMapper.selectOne(new LambdaQueryWrapperX<SchoolDO>()
                .eq(SchoolDO::getSchoolCode, code));
        return school != null ? school.getId() : null;
    }

    private Long resolveCollegeIdByName(Long schoolId, Integer activeYear, String collegeName) {
        String name = StrUtil.trimToNull(collegeName);
        if (schoolId == null || name == null) {
            return null;
        }
        SchoolCollegeDO college = schoolCollegeMapper.selectOne(new LambdaQueryWrapperX<SchoolCollegeDO>()
                .eq(SchoolCollegeDO::getSchoolId, schoolId)
                .eq(SchoolCollegeDO::getName, name)
                .eqIfPresent(SchoolCollegeDO::getYear, activeYear));
        return college != null ? college.getId() : null;
    }

    private MajorDO resolveMajorByCodeAndName(Integer activeYear, String majorCode, String majorName) {
        String code = StrUtil.trimToNull(majorCode);
        String name = StrUtil.trimToNull(majorName);
        if (code == null || name == null) {
            return null;
        }
        return majorMapper.selectOne(new LambdaQueryWrapperX<MajorDO>()
                .eq(MajorDO::getCode, code)
                .eq(MajorDO::getName, name)
                .eqIfPresent(MajorDO::getYear, activeYear));
    }

    private SchoolDirectionDO resolveDirectionByName(Long schoolId,
                                                     Long collegeId,
                                                     Long majorId,
                                                     Integer activeYear,
                                                     Integer studyMode,
                                                     String directionName) {
        String name = StrUtil.trimToNull(directionName);
        if (schoolId == null || collegeId == null || majorId == null || name == null) {
            return null;
        }
        List<SchoolDirectionDO> list = schoolDirectionMapper.selectList(new LambdaQueryWrapperX<SchoolDirectionDO>()
                .eq(SchoolDirectionDO::getSchoolId, schoolId)
                .eq(SchoolDirectionDO::getCollegeId, collegeId)
                .eq(SchoolDirectionDO::getMajorId, majorId)
                .eqIfPresent(SchoolDirectionDO::getStudyMode, studyMode)
                .eqIfPresent(SchoolDirectionDO::getYear, activeYear)
                .eq(SchoolDirectionDO::getDirectionName, name)
                .orderByAsc(SchoolDirectionDO::getId));
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    private Long upsertByBizKey(AdjustmentDO incoming) {
        if (incoming == null) {
            return null;
        }
        normalizeKeyFields(incoming);
        AdjustmentDO existing = adjustmentMapper.selectOne(new LambdaQueryWrapperX<AdjustmentDO>()
                .eq(AdjustmentDO::getYear, incoming.getYear())
                .eq(AdjustmentDO::getSchoolId, incoming.getSchoolId())
                .eq(AdjustmentDO::getCollegeId, incoming.getCollegeId())
                .eq(AdjustmentDO::getMajorId, incoming.getMajorId())
                .eq(AdjustmentDO::getStudyMode, incoming.getStudyMode())
                .eq(AdjustmentDO::getDirectionId, incoming.getDirectionId()));

        if (existing == null) {
            adjustmentMapper.insert(incoming);
            cacheEvictFacade.evictAfterAdjustmentChanged(incoming);
            return incoming.getId();
        }

        boolean updated = applyUpdateRule(existing, incoming);
        if (!updated) {
            return existing.getId();
        }
        adjustmentMapper.updateById(existing);
        cacheEvictFacade.evictAfterAdjustmentChanged(existing);
        return existing.getId();
    }

    private void normalizeKeyFields(AdjustmentDO incoming) {
        if (incoming.getCollegeId() == null) {
            incoming.setCollegeId(0L);
        }
        if (incoming.getDirectionId() == null) {
            incoming.setDirectionId(0L);
        }
        if (incoming.getStudyMode() == null) {
            incoming.setStudyMode(1);
        }
        if (incoming.getPublishTime() == null) {
            incoming.setPublishTime(LocalDateTime.now());
        }
        if (incoming.getStatus() == null) {
            incoming.setStatus(1);
        }
    }

    private boolean applyUpdateRule(AdjustmentDO existing, AdjustmentDO incoming) {
        Integer oldSource = existing.getSourceType() != null ? existing.getSourceType() : 3;
        Integer newSource = incoming.getSourceType() != null ? incoming.getSourceType() : 3;
        int oldP = priority(oldSource);
        int newP = priority(newSource);
        if (newP < oldP) {
            copyBusinessFields(existing, incoming);
            return true;
        }
        if (newP > oldP) {
            return false;
        }
        // same priority
        if (newSource == 3) {
            // 三方覆盖：运营写入永远大于用户写入
            boolean existingAdmin = isThirdPartyAdmin(existing.getUpdater());
            boolean incomingAdmin = isThirdPartyAdmin(incoming.getUpdater());
            if (incomingAdmin && !existingAdmin) {
                copyBusinessFields(existing, incoming);
                return true;
            }
            if (!incomingAdmin && existingAdmin) {
                return false;
            }
            LocalDateTime oldPt = existing.getPublishTime();
            LocalDateTime newPt = incoming.getPublishTime();
            if (oldPt == null || (newPt != null && newPt.isAfter(oldPt))) {
                copyBusinessFields(existing, incoming);
                return true;
            }
            return false;
        }
        // official / school site: special handle counts (official-style)
        mergeCountsOfficial(existing, incoming);
        // other fields: keep latest publish_time to reflect refresh
        LocalDateTime oldPt = existing.getPublishTime();
        LocalDateTime newPt = incoming.getPublishTime();
        if (oldPt == null || (newPt != null && newPt.isAfter(oldPt))) {
            existing.setPublishTime(newPt);
            existing.setSourceUrl(incoming.getSourceUrl());
            existing.setRemark(incoming.getRemark());
            if (StrUtil.isNotBlank(incoming.getRetestBooks())) {
                existing.setRetestBooks(incoming.getRetestBooks());
            }
            if (StrUtil.isNotBlank(incoming.getSubjects())) {
                existing.setSubjects(incoming.getSubjects());
            }
        }
        existing.setSourceType(newSource);
        existing.setStatus(1);
        return true;
    }

    private void copyBusinessFields(AdjustmentDO existing, AdjustmentDO incoming) {
        existing.setSourceType(incoming.getSourceType());
        existing.setSourceUrl(incoming.getSourceUrl());
        existing.setSchoolName(incoming.getSchoolName());
        existing.setCollegeName(incoming.getCollegeName());
        existing.setMajorCode(incoming.getMajorCode());
        existing.setMajorName(incoming.getMajorName());
        existing.setDegreeType(incoming.getDegreeType());
        existing.setDirectionCode(incoming.getDirectionCode());
        existing.setDirectionName(incoming.getDirectionName());
        existing.setSubjects(incoming.getSubjects());
        existing.setAdjustCount(incoming.getAdjustCount());
        existing.setAdjustLeft(incoming.getAdjustLeft());
        existing.setRemark(incoming.getRemark());
        existing.setPublishTime(incoming.getPublishTime());
        existing.setRetestBooks(incoming.getRetestBooks());
        existing.setStatus(1);
        if (incoming.getUpdater() != null) {
            existing.setUpdater(incoming.getUpdater());
        }
        if (incoming.getCreator() != null) {
            existing.setCreator(incoming.getCreator());
        }
        // keep viewCount/hotScore
    }

    private void mergeCountsOfficial(AdjustmentDO existing, AdjustmentDO incoming) {
        Integer oldTotal = existing.getAdjustCount();
        Integer newTotal = incoming.getAdjustCount();
        Integer mergedTotal = mergeMaxPreferPositive(oldTotal, newTotal);

        Integer oldLeft = existing.getAdjustLeft();
        Integer newLeft = incoming.getAdjustLeft();
        Integer mergedLeft = mergeMinPreferPositive(oldLeft, newLeft);
        if (mergedTotal != null && mergedTotal > 0 && mergedLeft != null && mergedLeft > mergedTotal) {
            mergedLeft = mergedTotal;
        }
        existing.setAdjustCount(mergedTotal != null ? mergedTotal : 0);
        existing.setAdjustLeft(mergedLeft != null ? mergedLeft : 0);
    }

    private Integer mergeMaxPreferPositive(Integer a, Integer b) {
        int av = a != null ? a : 0;
        int bv = b != null ? b : 0;
        if (av <= 0 && bv <= 0) {
            return 0;
        }
        return Math.max(av, bv);
    }

    private Integer mergeMinPreferPositive(Integer a, Integer b) {
        int av = a != null ? a : 0;
        int bv = b != null ? b : 0;
        if (av > 0 && bv > 0) {
            return Math.min(av, bv);
        }
        if (av > 0) {
            return av;
        }
        return bv;
    }

    private int priority(Integer sourceType) {
        if (sourceType == null) {
            return 3;
        }
        if (sourceType == 1) {
            return 1;
        }
        if (sourceType == 2) {
            return 2;
        }
        return 3;
    }

    private String writeJsonArray(List<String> list) {
        if (list == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            return null;
        }
    }

    private void markThirdPartyWriterIfNeeded(AdjustmentDO incoming, boolean admin, Long writerId) {
        if (incoming == null) {
            return;
        }
        Integer sourceType = incoming.getSourceType();
        if (sourceType == null || sourceType != 3) {
            return;
        }
        if (writerId == null) {
            return;
        }
        String marker = (admin ? "admin:" : "user:") + writerId;
        incoming.setUpdater(marker);
        if (incoming.getCreator() == null) {
            incoming.setCreator(marker);
        }
    }

    private boolean isThirdPartyAdmin(String updater) {
        return updater != null && updater.startsWith("admin:");
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
        // If user hasn't opened any major category yet, show default category data only (哲学) and preview 10 rows.
        boolean noOpenedMajorCategory = false;
        if (userId != null && reqVO != null) {
            Set<String> opened = vipBenefitService.getConsumedUniqueKeys(userId, BENEFIT_KEY_MAJOR_CATEGORY_OPEN);
            noOpenedMajorCategory = (opened == null || opened.isEmpty());
            if (noOpenedMajorCategory) {
                reqVO.setMajorCode(DEFAULT_MAJOR_CATEGORY_CODE);
                // enforce preview limit (avoid leaking full total)
                Integer pageNo = reqVO.getPageNo();
                if (pageNo != null && pageNo > 1) {
                    AppAdjustmentSearchTabRespVO resp = new AppAdjustmentSearchTabRespVO();
                    resp.setTotal((long) DEFAULT_MAJOR_CATEGORY_PREVIEW_LIMIT);
                    // Always return empty lists for page > 1 to avoid leaking full data.
                    resp.setMajorList(Collections.emptyList());
                    resp.setSchoolList(Collections.emptyList());
                    return resp;
                }
                Integer pageSize = reqVO.getPageSize();
                if (pageSize == null || pageSize > DEFAULT_MAJOR_CATEGORY_PREVIEW_LIMIT) {
                    reqVO.setPageSize(DEFAULT_MAJOR_CATEGORY_PREVIEW_LIMIT);
                }
            }
        }
        ResolvedMajorCode resolved = resolveMajorCodeForSearch(reqVO != null ? reqVO.getMajorCode() : null);
        if (reqVO != null) {
            // Compat: old client uses level2MajorId (single) instead of level2MajorCodes (list of code)
            if ((reqVO.getLevel2MajorCodes() == null || reqVO.getLevel2MajorCodes().isEmpty())
                    && reqVO.getLevel2MajorId() != null) {
                MajorDO level2 = majorMapper.selectById(reqVO.getLevel2MajorId());
                if (level2 != null && StrUtil.isNotBlank(level2.getCode())) {
                    reqVO.setLevel2MajorCodes(List.of(level2.getCode().trim()));
                }
            }
            reqVO.setResolvedMajorCodePrefix(resolved.prefix());
            reqVO.setResolvedMajorCodeExact(resolved.exact());
            ResolvedMajorCodeList resolvedLevel2 = resolveMajorCodesForSearch(reqVO.getLevel2MajorCodes());
            reqVO.setResolvedLevel2MajorCodePrefixes(resolvedLevel2.prefixes());
            reqVO.setResolvedLevel2MajorCodeExacts(resolvedLevel2.exacts());
        }
        validateMajorCategoryAccess(userId, resolved.majorCategoryCode());
        if (reqVO != null && StrUtil.isNotBlank(reqVO.getKeyword())) {
            reqVO.setKeyword(buildBooleanKeyword(reqVO.getKeyword()));
        }
        String tabType = reqVO != null ? reqVO.getTabType() : null;
        boolean schoolTab = "school".equalsIgnoreCase(tabType);
        AppAdjustmentSearchTabRespVO respVO = new AppAdjustmentSearchTabRespVO();
        if (schoolTab) {
            PageResult<AppAdjustmentSearchSchoolRespVO> pageResult = adjustmentMapper.selectSearchSchoolPage(reqVO);
            List<AppAdjustmentSearchSchoolRespVO> list = pageResult.getList();
            long total = pageResult.getTotal();
            if (noOpenedMajorCategory) {
                total = Math.min(total, (long) DEFAULT_MAJOR_CATEGORY_PREVIEW_LIMIT);
                if (list != null && list.size() > DEFAULT_MAJOR_CATEGORY_PREVIEW_LIMIT) {
                    list = list.subList(0, DEFAULT_MAJOR_CATEGORY_PREVIEW_LIMIT);
                }
            }
            respVO.setTotal(total);
            respVO.setSchoolList(list != null ? list : Collections.emptyList());
            respVO.setMajorList(Collections.emptyList());
        } else {
            PageResult<AppAdjustmentSearchRespVO> pageResult = adjustmentMapper.selectSearchMajorPage(reqVO);
            List<AppAdjustmentSearchRespVO> majorList = pageResult.getList();
            fillHighAdjustChance(majorList);
            fillAdjustHintText(majorList);
            for (AppAdjustmentSearchRespVO item : majorList) {
                item.setHeat(calcHeat(item.getHotScore()));
            }
            long total = pageResult.getTotal();
            if (noOpenedMajorCategory) {
                total = Math.min(total, (long) DEFAULT_MAJOR_CATEGORY_PREVIEW_LIMIT);
                if (majorList != null && majorList.size() > DEFAULT_MAJOR_CATEGORY_PREVIEW_LIMIT) {
                    majorList = majorList.subList(0, DEFAULT_MAJOR_CATEGORY_PREVIEW_LIMIT);
                }
            }
            respVO.setTotal(total);
            respVO.setMajorList(majorList != null ? majorList : Collections.emptyList());
            respVO.setSchoolList(Collections.emptyList());
        }
        return respVO;
    }

    private void validateMajorCategoryAccess(Long userId, String majorCode) {
        if (userId == null) {
            // allow anonymous access for now (no place to persist defaults)
            return;
        }
        if (StrUtil.isBlank(majorCode)) {
            return;
        }
        String code = majorCode.trim();
        Set<String> opened = vipBenefitService.getConsumedUniqueKeys(userId, BENEFIT_KEY_MAJOR_CATEGORY_OPEN);
        if (opened == null || opened.isEmpty()) {
            // New user: do NOT persist. Allow accessing hardcoded default category only.
            if (DEFAULT_MAJOR_CATEGORY_CODE.equals(code)) {
                return;
            }
            throw exception(VIP_MAJOR_CATEGORY_NOT_OPENED);
        }
        if (opened.contains(code)) {
            return;
        }
        throw exception(VIP_MAJOR_CATEGORY_NOT_OPENED);
    }

    private ResolvedMajorCode resolveMajorCodeForSearch(String majorCode) {
        if (StrUtil.isBlank(majorCode)) {
            return ResolvedMajorCode.empty();
        }
        String input = majorCode.trim();
        // Frontend codes are from biz_major: level1=2 digits, level2=4 digits, level3=6 digits.
        // Do NOT parse/translate; only decide exact vs prefix by length.
        if (input.matches("\\d{6}")) {
            return ResolvedMajorCode.exact(input);
        }
        if (input.matches("\\d{2}") || input.matches("\\d{4}")) {
            return ResolvedMajorCode.prefix(input);
        }
        // Fallback for unexpected formats: keep previous behavior (prefix match).
        return ResolvedMajorCode.prefix(input);
    }

    private ResolvedMajorCodeList resolveMajorCodesForSearch(List<String> codes) {
        if (codes == null || codes.isEmpty()) {
            return ResolvedMajorCodeList.empty();
        }
        List<String> prefixes = new ArrayList<>();
        List<String> exacts = new ArrayList<>();
        for (String code : codes) {
            String input = StrUtil.trimToNull(code);
            if (input == null) {
                continue;
            }
            if (input.matches("\\d{6}")) {
                exacts.add(input);
            } else {
                // expect 2/4; keep as prefix (also fallback)
                prefixes.add(input);
            }
        }
        // de-dup, keep order
        prefixes = prefixes.stream().distinct().collect(Collectors.toList());
        exacts = exacts.stream().distinct().collect(Collectors.toList());
        return new ResolvedMajorCodeList(prefixes, exacts);
    }

    private record ResolvedMajorCode(String prefix, String exact, String majorCategoryCode) {

        static ResolvedMajorCode empty() {
            return new ResolvedMajorCode(null, null, null);
        }

        static ResolvedMajorCode prefix(String prefix) {
            String p = StrUtil.trimToNull(prefix);
            String cat = p != null && p.length() >= 2 ? p.substring(0, 2) : null;
            return new ResolvedMajorCode(p, null, cat);
        }

        static ResolvedMajorCode exact(String exact) {
            String e = StrUtil.trimToNull(exact);
            String cat = e != null && e.length() >= 2 ? e.substring(0, 2) : null;
            return new ResolvedMajorCode(null, e, cat);
        }
    }

    private record ResolvedMajorCodeList(List<String> prefixes, List<String> exacts) {

        static ResolvedMajorCodeList empty() {
            return new ResolvedMajorCodeList(Collections.emptyList(), Collections.emptyList());
        }
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
        Integer activeYear = projectConfigService.getActiveYear();
        suggestions.addAll(majorMapper.selectSuggestMajorCodes(keyword, limit, activeYear));
        suggestions.addAll(majorMapper.selectSuggestMajorNames(keyword, limit, activeYear));
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

        // 2) degreeType
        groups.add(group("degreeType", "学位类型", Arrays.asList(
                option("1", "学硕"),
                option("2", "专硕")
        )));

        // 2) target major (level2)
        AppAdjustmentFilterConfigRespVO.Group level2MajorGroup = new AppAdjustmentFilterConfigRespVO.Group();
        level2MajorGroup.setKey("level2MajorCodes");
        level2MajorGroup.setName("一级学科");
        List<AppAdjustmentFilterConfigRespVO.Option> level2MajorOptions = new ArrayList<>();
        if (StrUtil.isNotBlank(majorCode)) {
            List<MajorDO> level2List = majorMapper.selectListByLevelAndParentCode(majorCode, 2, null, projectConfigService.getActiveYear());
            if (level2List != null && !level2List.isEmpty()) {
                for (MajorDO item : level2List) {
                    if (item == null || StrUtil.isBlank(item.getCode()) || StrUtil.isBlank(item.getName())) {
                        continue;
                    }
                    AppAdjustmentFilterConfigRespVO.Option opt = option(item.getCode(), item.getName());
                    opt.setDegreeType(item.getDegreeType());
                    level2MajorOptions.add(opt);
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
        List<AdjustmentYearOptionRowDTO> rows = adjustmentMapper.selectOptionYearOptions(
                reqVO.getSchoolId(), reqVO.getCollegeId(), reqVO.getMajorId(), reqVO.getStudyMode());
        if (rows == null || rows.isEmpty()) {
            respVO.setOptions(Collections.emptyList());
            return respVO;
        }
        final String officialText = "官方发布调剂";
        final String thirdPartyText = "第三方发布调剂";
        List<AppAdjustmentOptionsRespVO.Option> options = new ArrayList<>(rows.size());
        for (AdjustmentYearOptionRowDTO row : rows) {
            if (row == null || row.getYear() == null) {
                continue;
            }
            AppAdjustmentOptionsRespVO.Option opt = new AppAdjustmentOptionsRespVO.Option();
            opt.setYear(row.getYear());
            Integer p = row.getMinPriority();
            opt.setText(p != null && p == 3 ? thirdPartyText : officialText);
            options.add(opt);
        }
        respVO.setOptions(options);
        return respVO;
    }

    @Override
    public AppAdjustmentDetailRespVO getAdjustmentDetail(Long userId, AppAdjustmentDetailReqVO reqVO) {
        List<AdjustmentDO> list = adjustmentDetailCache.listDetailRows(
                reqVO.getSchoolId(), reqVO.getMajorId(), reqVO.getCollegeId(), reqVO.getYear(), reqVO.getStudyMode());
        if (list == null || list.isEmpty()) {
            throw exception(ADJUSTMENT_NOT_EXISTS);
        }

        // best-effort: detail view should +1 view_count and +1 hot_score for ONE representative adjustment row
        try {
            Long representativeId = pickRepresentativeAdjustmentId(list);
            if (representativeId != null) {
                adjustmentMapper.incrViewAndHotById(representativeId, 1, 1L);
            }
        } catch (Exception e) {
            log.warn("Failed to incr view/hot score. schoolId={}, collegeId={}, majorId={}, year={}, studyMode={}",
                    reqVO.getSchoolId(), reqVO.getCollegeId(), reqVO.getMajorId(), reqVO.getYear(), reqVO.getStudyMode(), e);
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
            direction.setRetestBooks(parseStringList(adjustment.getRetestBooks()));
            direction.setRequireScore(adjustment.getRequireScore());
            direction.setRequireMajor(adjustment.getRequireMajor());
            direction.setSubjects(parseSubjectsNamesOnly(adjustment.getSubjects()));
            direction.setRemark(adjustment.getRemark());
            direction.setPublishTime(adjustment.getPublishTime());
            direction.setViewCount(adjustment.getViewCount());
            directions.add(direction);
        }
        respVO.setDirections(directions);
        return respVO;
    }

    private Long pickRepresentativeAdjustmentId(List<AdjustmentDO> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        Long minId = null;
        for (AdjustmentDO item : list) {
            if (item == null || item.getId() == null) {
                continue;
            }
            // prefer "00" direction
            if ("00".equals(item.getDirectionCode())) {
                return item.getId();
            }
            if (minId == null || item.getId() < minId) {
                minId = item.getId();
            }
        }
        return minId;
    }

    @Override
    @Cacheable(cacheNames = CacheNames.ADJUSTMENT_UPDATE_STATS, key = "'default'")
    public AppAdjustmentUpdateStatsRespVO getAdjustmentUpdateStats() {
        Integer statYear = resolveStatsYear();
        AppAdjustmentUpdateStatsRespVO respVO = adjustmentMapper.selectUpdateStats(statYear);
        if (respVO == null) {
            respVO = new AppAdjustmentUpdateStatsRespVO();
            respVO.setYear(statYear);
            respVO.setTodayUpdateCount(0L);
            respVO.setThisYearUpdateCount(0L);
            respVO.setLastYearUpdateCount(0L);
            respVO.setHistoryUpdateCount(0L);
        }
        // TODO 临时口径：history 先写死 21w，待接入真实统计后移除
        respVO.setLastYearUpdateCount(respVO.getLastYearUpdateCount()+10000);
        respVO.setHistoryUpdateCount(212_665L);
        return respVO;
    }

    private Integer resolveStatsYear() {
//        List<Integer> years = adjustmentMapper.selectYearList();
//        if (years != null && !years.isEmpty() && years.get(0) != null) {
//            return years.get(0);
//        }
        return projectConfigService.getAdjustYear();
    }

    @Override
    @Cacheable(cacheNames = CacheNames.ADJUSTMENT_HOT_RANKING_PAGE,
            key = "'y:' + (#reqVO.year == null ? '' : #reqVO.year)"
                    + " + ':p:' + (#reqVO.provinceCode == null ? '' : #reqVO.provinceCode)"
                    + " + ':sl:' + (#reqVO.schoolLevel == null ? '' : #reqVO.schoolLevel)"
                    + " + ':sm:' + (#reqVO.studyMode == null ? '' : #reqVO.studyMode)"
                    + " + ':pn:' + #reqVO.pageNo + ':ps:' + #reqVO.pageSize")
    public PageResult<AppAdjustmentSearchRespVO> getHotRankingPage(@Valid AppAdjustmentHotRankingReqVO reqVO) {
        PageResult<AppAdjustmentSearchRespVO> pageResult = adjustmentMapper.selectHotRankingPage(reqVO);
        List<AppAdjustmentSearchRespVO> list = pageResult.getList();
        if (list == null || list.isEmpty()) {
            return pageResult;
        }
        fillHighAdjustChance(list);
        fillAdjustHintText(list);
        for (AppAdjustmentSearchRespVO item : list) {
            item.setHeat(calcHeat(item.getHotScore()));
        }
        return pageResult;
    }

    @Override
    public PageResult<AppSchoolAdjustmentRespVO> getSchoolAdjustmentPage(Long userId, @Valid AppSchoolAdjustmentPageReqVO reqVO) {
        // Non-member forbidden: controlled by VIP benefit config
        vipBenefitService.checkEnabledOrThrow(userId, VIEW_SCHOOL_ADJUSTMENT_3Y);

        // Force recent 3 years (currentYear, currentYear-1, currentYear-2)
        int currentYear = Year.now().getValue();
        if (reqVO != null) {
            reqVO.setBeginYear(currentYear - 2);
            reqVO.setEndYear(currentYear);
        }
        return schoolAdjustmentCache.getSchoolAdjustmentPage(reqVO);
    }

    private void fillAdjustHintText(List<AppAdjustmentSearchRespVO> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        int currentYear = Year.now().getValue();

        // collect prev-year keys for all history rows (year != currentYear)
        Map<String, BizMajorStudyKeyDTO> uniqPrevYearKeys = new HashMap<>();
        for (AppAdjustmentSearchRespVO item : list) {
            if (item == null || item.getYear() == null) {
                continue;
            }
            Integer y = item.getYear();
            if (y == currentYear) {
                continue;
            }
            if (item.getSchoolId() == null || item.getCollegeId() == null || item.getMajorId() == null
                    || item.getStudyMode() == null) {
                continue;
            }
            BizMajorStudyKeyDTO k = new BizMajorStudyKeyDTO();
            k.setSchoolId(item.getSchoolId());
            k.setCollegeId(item.getCollegeId());
            k.setMajorId(item.getMajorId());
            k.setStudyMode(item.getStudyMode());
            k.setYear(y - 1);
            String ks = key(k.getSchoolId(), k.getCollegeId(), k.getMajorId(), k.getStudyMode(), k.getYear());
            if (ks != null) {
                uniqPrevYearKeys.putIfAbsent(ks, k);
            }
        }

        Set<String> prevYearExists = new HashSet<>();
        if (!uniqPrevYearKeys.isEmpty()) {
            List<BizMajorStudyKeyDTO> hits = adjustmentMapper.selectExistsMajorStudyKeys(new ArrayList<>(uniqPrevYearKeys.values()));
            for (BizMajorStudyKeyDTO hit : hits) {
                prevYearExists.add(key(hit.getSchoolId(), hit.getCollegeId(), hit.getMajorId(), hit.getStudyMode(), hit.getYear()));
            }
        }

        for (AppAdjustmentSearchRespVO item : list) {
            if (item == null || item.getYear() == null) {
                continue;
            }
            Integer y = item.getYear();
            if (y == currentYear) {
                item.setAdjustHintText(HINT_CURRENT_YEAR_HAS_QUOTA);
                continue;
            }
            if (item.getSchoolId() == null || item.getCollegeId() == null || item.getMajorId() == null
                    || item.getStudyMode() == null) {
                continue;
            }
            String prevKey = key(item.getSchoolId(), item.getCollegeId(), item.getMajorId(), item.getStudyMode(), y - 1);
            if (prevKey != null && prevYearExists.contains(prevKey)) {
                item.setAdjustHintText(HINT_ADJUST_CHANCE_HIGH);
            }
        }
    }

    private void fillHighAdjustChance(List<AppAdjustmentSearchRespVO> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        int currentYear = Year.now().getValue();

        // high adjust chance (batch; only meaningful for current year)
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

    private String key(Long schoolId, Long collegeId, Long majorId, Integer studyMode, Integer year) {
        if (schoolId == null || collegeId == null || majorId == null || studyMode == null) {
            return null;
        }
        return schoolId + ":" + collegeId + ":" + majorId + ":" + studyMode + ":" + (year != null ? year : "");
    }

    private List<String> parseStringList(String value) {
        if (StrUtil.isBlank(value)) {
            return Collections.emptyList();
        }
        String normalized = value.trim();
        // Prefer JSON array (DB column is JSON)
        if (normalized.startsWith("[")) {
            try {
                JsonNode root = objectMapper.readTree(normalized);
                if (root != null && root.isArray()) {
                    List<String> list = new ArrayList<>();
                    for (JsonNode n : root) {
                        if (n == null || n.isNull()) {
                            continue;
                        }
                        String s = StrUtil.trimToNull(n.asText(null));
                        if (s != null) {
                            list.add(s);
                        }
                    }
                    return list;
                }
            } catch (Exception ignore) {
                // fall through to legacy split
            }
        }
        // Legacy: split by newline/semicolon/comma
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

    private AppAdjustmentSubjectsRespVO parseSubjectsNamesOnly(String subjectsJson) {
        AppAdjustmentSubjectsRespVO vo = new AppAdjustmentSubjectsRespVO();
        if (StrUtil.isBlank(subjectsJson)) {
            vo.setS1(Collections.emptyList());
            vo.setS2(Collections.emptyList());
            vo.setS3(Collections.emptyList());
            vo.setS4(Collections.emptyList());
            return vo;
        }
        try {
            JsonNode root = objectMapper.readTree(subjectsJson);
            vo.setS1(extractSubjectNames(root, "s1"));
            vo.setS2(extractSubjectNames(root, "s2"));
            vo.setS3(extractSubjectNames(root, "s3"));
            vo.setS4(extractSubjectNames(root, "s4"));
            return vo;
        } catch (Exception ignore) {
            // best-effort: avoid breaking detail if malformed
            vo.setS1(Collections.emptyList());
            vo.setS2(Collections.emptyList());
            vo.setS3(Collections.emptyList());
            vo.setS4(Collections.emptyList());
            return vo;
        }
    }

    private List<String> extractSubjectNames(JsonNode root, String key) {
        if (root == null || StrUtil.isBlank(key)) {
            return Collections.emptyList();
        }
        JsonNode arr = root.get(key);
        if (arr == null || !arr.isArray()) {
            return Collections.emptyList();
        }
        // Accept both ["name"] and [{"name": "...", "code": "..."}]
        List<String> names = new ArrayList<>();
        for (JsonNode item : arr) {
            if (item == null || item.isNull()) {
                continue;
            }
            String name = null;
            String code = null;
            if (item.isObject()) {
                name = StrUtil.trimToNull(item.path("name").asText(null));
                code = StrUtil.trimToNull(item.path("code").asText(null));
            } else {
                name = StrUtil.trimToNull(item.asText(null));
            }
            String display;
            if (name != null && code != null) {
                display = name + "（" + code + "）";
            } else if (name != null) {
                display = name;
            } else {
                display = code;
            }
            if (display != null) {
                names.add(display);
            }
        }
        // de-dup but keep order
        return names.stream().distinct().collect(Collectors.toList());
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
