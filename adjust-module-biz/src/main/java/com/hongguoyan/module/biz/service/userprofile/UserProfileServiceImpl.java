package com.hongguoyan.module.biz.service.userprofile;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hongguoyan.framework.common.exception.ErrorCode;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.admin.userprofile.vo.UserProfilePageReqVO;
import com.hongguoyan.module.biz.controller.admin.userprofile.vo.UserProfileSaveReqVO;
import com.hongguoyan.module.biz.controller.app.userprofile.vo.AppUserProfileSaveReqVO;
import com.hongguoyan.module.biz.dal.dataobject.major.MajorDO;
import com.hongguoyan.module.biz.dal.dataobject.school.SchoolDO;
import com.hongguoyan.module.biz.dal.dataobject.schoolcollege.SchoolCollegeDO;
import com.hongguoyan.module.biz.dal.dataobject.schooldirection.SchoolDirectionDO;
import com.hongguoyan.module.biz.dal.dataobject.userprofile.UserProfileDO;
import com.hongguoyan.module.biz.dal.dataobject.userpreference.UserPreferenceDO;
import com.hongguoyan.module.biz.dal.mysql.major.MajorMapper;
import com.hongguoyan.module.biz.dal.mysql.school.SchoolMapper;
import com.hongguoyan.module.biz.dal.mysql.schoolcollege.SchoolCollegeMapper;
import com.hongguoyan.module.biz.dal.mysql.schooldirection.SchoolDirectionMapper;
import com.hongguoyan.module.biz.dal.mysql.userpreference.UserPreferenceMapper;
import com.hongguoyan.module.biz.dal.mysql.userprofile.UserProfileMapper;
import com.hongguoyan.module.biz.service.vipbenefit.VipBenefitService;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.BENEFIT_KEY_MAJOR_CATEGORY_OPEN;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.REF_TYPE_MAJOR_CATEGORY_OPEN;

/**
 * 用户基础档案表(含成绩与软背景) Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class UserProfileServiceImpl implements UserProfileService {

    @Resource
    private UserProfileMapper userProfileMapper;
    @Resource
    private SchoolMapper schoolMapper;
    @Resource
    private MajorMapper majorMapper;
    @Resource
    private SchoolDirectionMapper schoolDirectionMapper;
    @Resource
    private SchoolCollegeMapper schoolCollegeMapper;
    @Resource
    private UserPreferenceMapper userPreferenceMapper;
    @Resource
    private VipBenefitService vipBenefitService;

    @Override
    public Long createUserProfile(UserProfileSaveReqVO createReqVO) {
        UserProfileDO userProfile = BeanUtils.toBean(createReqVO, UserProfileDO.class);
        userProfileMapper.insert(userProfile);
        return userProfile.getId();
    }

    @Override
    public void updateUserProfile(UserProfileSaveReqVO updateReqVO) {
        validateUserProfileExists(updateReqVO.getId());
        UserProfileDO updateObj = BeanUtils.toBean(updateReqVO, UserProfileDO.class);
        userProfileMapper.updateById(updateObj);
    }

    @Override
    public void deleteUserProfile(Long id) {
        validateUserProfileExists(id);
        userProfileMapper.deleteById(id);
    }

    private void validateUserProfileExists(Long id) {
        if (userProfileMapper.selectById(id) == null) {
            throw exception(new ErrorCode(404, "用户基础档案不存在"));
        }
    }

    @Override
    public UserProfileDO getUserProfile(Long id) {
        return userProfileMapper.selectById(id);
    }

    @Override
    public PageResult<UserProfileDO> getUserProfilePage(UserProfilePageReqVO pageReqVO) {
        return userProfileMapper.selectPage(pageReqVO, new LambdaQueryWrapperX<UserProfileDO>()
                .eqIfPresent(UserProfileDO::getUserId, pageReqVO.getUserId())
                .likeIfPresent(UserProfileDO::getGraduateSchoolName, pageReqVO.getGraduateSchoolName())
                .likeIfPresent(UserProfileDO::getTargetSchoolName, pageReqVO.getTargetSchoolName()));
    }

    @Override
    public UserProfileDO getUserProfileByUserId(Long userId) {
        return userProfileMapper.selectOne(new LambdaQueryWrapperX<UserProfileDO>()
                .eq(UserProfileDO::getUserId, userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveUserProfileByUserId(Long userId, AppUserProfileSaveReqVO reqVO) {
        UserProfileDO existing = getUserProfileByUserId(userId);
        UserProfileDO toSave = buildToSave(userId, reqVO);

        if (existing == null) {
            toSave.setId(null);
            userProfileMapper.insert(toSave);
            // no1 empty then write + open major category
            syncNo1AndOpenMajorCategory(userId, toSave, reqVO);
            return toSave.getId();
        }

        // 覆盖更新：按 userId 唯一行覆盖
        toSave.setId(existing.getId());
        // 兼容旧客户端未传新字段：不覆盖原值
        if (reqVO.getIsNationalScholarship() == null) {
            toSave.setIsNationalScholarship(existing.getIsNationalScholarship());
        }
        if (reqVO.getIsSchoolScholarship() == null) {
            toSave.setIsSchoolScholarship(existing.getIsSchoolScholarship());
        }
        // 目前不处理锁定/提交逻辑，这两个字段保持原值，避免被覆盖为 null
        toSave.setBasicLocked(existing.getBasicLocked());
        toSave.setSubmitTime(existing.getSubmitTime());
        userProfileMapper.updateById(toSave);
        // no1 empty then write + open major category
        syncNo1AndOpenMajorCategory(userId, toSave, reqVO);
        return existing.getId();
    }

    private void syncNo1AndOpenMajorCategory(Long userId, UserProfileDO toSave, AppUserProfileSaveReqVO reqVO) {
        // 1) Ensure preferenceNo=1 exists (only when empty)
        List<UserPreferenceDO> no1List = userPreferenceMapper.selectListByUserIdAndPreferenceNo(userId, 1);
        UserPreferenceDO no1First = (no1List != null && !no1List.isEmpty()) ? no1List.get(0) : null;
        if (no1First == null) {
            // Build a preference row from profile snapshot (directionId comes from targetDirectionId)
            Long directionId = toSave.getTargetDirectionId();
            if (directionId == null) {
                throw exception(new ErrorCode(400, "一志愿方向ID缺失，无法写入志愿表"));
            }
            SchoolDirectionDO direction = schoolDirectionMapper.selectById(directionId);
            if (direction == null) {
                throw exception(new ErrorCode(400, "一志愿方向不存在，无法写入志愿表"));
            }
            UserPreferenceDO pref = new UserPreferenceDO();
            pref.setUserId(userId);
            pref.setPreferenceNo(1);
            pref.setSchoolId(toSave.getTargetSchoolId());
            pref.setSchoolName(StrUtil.blankToDefault(toSave.getTargetSchoolName(), ""));
            pref.setCollegeId(toSave.getTargetCollegeId());
            pref.setCollegeName(StrUtil.blankToDefault(toSave.getTargetCollegeName(), ""));
            pref.setMajorId(toSave.getTargetMajorId());
            pref.setMajorCode(StrUtil.blankToDefault(toSave.getTargetMajorCode(), ""));
            pref.setMajorName(StrUtil.blankToDefault(toSave.getTargetMajorName(), ""));
            pref.setDirectionId(directionId);
            pref.setDirectionCode(StrUtil.blankToDefault(toSave.getTargetDirectionCode(), ""));
            pref.setDirectionName(StrUtil.blankToDefault(toSave.getTargetDirectionName(), ""));
            pref.setStudyMode(direction.getStudyMode());
            pref.setSourceAdjustmentId(null);
            userPreferenceMapper.insert(pref);
            no1First = pref;
        }

        // 2) Open major category for the first row of preferenceNo=1
        String majorCode = no1First.getMajorCode() != null ? no1First.getMajorCode().trim() : "";
        if (majorCode.isEmpty() || majorCode.length() < 2) {
            throw exception(new ErrorCode(400, "一志愿专业代码缺失，无法开通门类"));
        }
        String majorCategoryCode = majorCode.substring(0, 2);
        // Idempotency safeguard: avoid repeated consumption on profile re-save
        try {
            Set<String> opened = vipBenefitService.getConsumedUniqueKeys(userId, BENEFIT_KEY_MAJOR_CATEGORY_OPEN);
            // If user has opened ANY major category before, do NOT auto-open on profile save.
            if (opened != null && !opened.isEmpty()) {
                return;
            }
        } catch (Exception ignore) {
            // best-effort: fall through to consume
        }
        vipBenefitService.consumeQuotaOrThrowReturnConsumed(userId, BENEFIT_KEY_MAJOR_CATEGORY_OPEN, 1,
                REF_TYPE_MAJOR_CATEGORY_OPEN, majorCategoryCode, majorCategoryCode);
    }

    private UserProfileDO buildToSave(Long userId, AppUserProfileSaveReqVO reqVO) {
        if (reqVO == null) {
            throw exception(new ErrorCode(400, "request body is required"));
        }
        UserProfileDO toSave = new UserProfileDO();
        toSave.setUserId(userId);

        // graduate school (id -> name snapshot)
        Long graduateSchoolId = reqVO.getGraduateSchoolId();
        SchoolDO graduateSchool = schoolMapper.selectById(graduateSchoolId);
        if (graduateSchool == null) {
            throw exception(new ErrorCode(400, "graduateSchoolId not exists: " + graduateSchoolId));
        }
        toSave.setGraduateSchoolId(graduateSchoolId);
        toSave.setGraduateSchoolName(StrUtil.blankToDefault(graduateSchool.getSchoolName(), ""));

        // graduate major (id -> name snapshot)
        Long graduateMajorId = reqVO.getGraduateMajorId();
        MajorDO graduateMajor = majorMapper.selectById(graduateMajorId);
        if (graduateMajor == null || Boolean.TRUE.equals(graduateMajor.getDeleted())) {
            throw exception(new ErrorCode(400, "graduateMajorId not exists: " + graduateMajorId));
        }
        toSave.setGraduateMajorId(graduateMajorId);
        toSave.setGraduateMajorName(StrUtil.blankToDefault(graduateMajor.getName(), ""));
        // best-effort: fill major class by parent major name (if exists), otherwise empty
        String majorClass = "";
        if (StrUtil.isNotBlank(graduateMajor.getParentCode())) {
            MajorDO parent = majorMapper.selectOne(new LambdaQueryWrapperX<MajorDO>()
                    .select(MajorDO::getName)
                    .eq(MajorDO::getCode, graduateMajor.getParentCode())
                    .eq(MajorDO::getDeleted, false)
                    .last("LIMIT 1"));
            if (parent != null) {
                majorClass = StrUtil.blankToDefault(parent.getName(), "");
            }
        }
        toSave.setGraduateMajorClass(majorClass);

        // base score & background
        toSave.setGraduateAverageScore(reqVO.getGraduateAverageScore());
        toSave.setUndergraduateGpa(reqVO.getUndergraduateGpa());
        toSave.setCet4Score(reqVO.getCet4Score());
        toSave.setCet6Score(reqVO.getCet6Score());
        toSave.setUndergraduateAwards(reqVO.getUndergraduateAwards());
        toSave.setAwardCount(reqVO.getAwardCount());
        toSave.setIsNationalScholarship(reqVO.getIsNationalScholarship());
        toSave.setIsSchoolScholarship(reqVO.getIsSchoolScholarship());

        // target direction (id -> school/college/major + snapshots)
        Long targetDirectionId = reqVO.getTargetDirectionId();
        SchoolDirectionDO direction = schoolDirectionMapper.selectById(targetDirectionId);
        if (direction == null) {
            throw exception(new ErrorCode(400, "targetDirectionId not exists: " + targetDirectionId));
        }
        toSave.setTargetDirectionId(targetDirectionId);
        toSave.setTargetDirectionCode(StrUtil.blankToDefault(direction.getDirectionCode(), ""));
        toSave.setTargetDirectionName(StrUtil.blankToDefault(direction.getDirectionName(), ""));
        toSave.setTargetSchoolId(direction.getSchoolId());
        toSave.setTargetCollegeId(direction.getCollegeId());
        toSave.setTargetMajorId(direction.getMajorId());

        SchoolDO targetSchool = direction.getSchoolId() != null ? schoolMapper.selectById(direction.getSchoolId()) : null;
        toSave.setTargetSchoolName(targetSchool != null ? StrUtil.blankToDefault(targetSchool.getSchoolName(), "") : "");
        SchoolCollegeDO targetCollege = direction.getCollegeId() != null ? schoolCollegeMapper.selectById(direction.getCollegeId()) : null;
        toSave.setTargetCollegeName(targetCollege != null ? StrUtil.blankToDefault(targetCollege.getName(), "") : "");

        MajorDO targetMajor = direction.getMajorId() != null ? majorMapper.selectById(direction.getMajorId()) : null;
        if (targetMajor != null) {
            toSave.setTargetMajorCode(StrUtil.blankToDefault(targetMajor.getCode(), ""));
            toSave.setTargetMajorName(StrUtil.blankToDefault(targetMajor.getName(), ""));
            toSave.setTargetDegreeType(targetMajor.getDegreeType() != null ? targetMajor.getDegreeType() : 0);
        } else {
            toSave.setTargetMajorCode("");
            toSave.setTargetMajorName("");
            toSave.setTargetDegreeType(0);
        }

        // subjects: only keep scores, clear codes/names
        toSave.setSubjectCode1("");
        toSave.setSubjectName1("");
        toSave.setSubjectScore1(reqVO.getSubjectScore1());
        toSave.setSubjectCode2("");
        toSave.setSubjectName2("");
        toSave.setSubjectScore2(reqVO.getSubjectScore2());
        toSave.setSubjectCode3("");
        toSave.setSubjectName3("");
        toSave.setSubjectScore3(reqVO.getSubjectScore3());
        toSave.setSubjectCode4("");
        toSave.setSubjectName4("");
        toSave.setSubjectScore4(reqVO.getSubjectScore4());
        toSave.setScoreTotal(reqVO.getScoreTotal());

        toSave.setSelfIntroduction(StrUtil.blankToDefault(reqVO.getSelfIntroduction(), ""));
        toSave.setPaperCount(reqVO.getPaperCount());

        // competitions: ids json + count computed on backend
        List<Long> competitionIds = reqVO.getCompetitionIds();
        if (competitionIds == null) {
            toSave.setCompetitionIds(null);
            toSave.setCompetitionCount(reqVO.getCompetitionCount());
        } else {
            List<Long> cleaned = new ArrayList<>();
            Set<Long> dedup = new HashSet<>();
            for (Long id : competitionIds) {
                if (id == null) {
                    continue;
                }
                if (dedup.add(id)) {
                    cleaned.add(id);
                }
            }
            toSave.setCompetitionIds(cleaned.isEmpty() ? null : JSONUtil.toJsonStr(cleaned));
            toSave.setCompetitionCount(dedup.isEmpty() ? null : dedup.size());
        }

        toSave.setSelfAssessedScore(reqVO.getSelfAssessedScore() != null ? reqVO.getSelfAssessedScore() : 0);
        return toSave;
    }

}

