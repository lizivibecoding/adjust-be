package com.hongguoyan.module.biz.service.userprofile;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
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
import com.hongguoyan.module.biz.dal.dataobject.schoolrank.SchoolRankDO;
import com.hongguoyan.module.biz.dal.dataobject.undergraduatemajor.UndergraduateMajorDO;
import com.hongguoyan.module.biz.dal.dataobject.userprofile.UserProfileDO;
import com.hongguoyan.module.biz.dal.mysql.major.MajorMapper;
import com.hongguoyan.module.biz.dal.mysql.school.SchoolMapper;
import com.hongguoyan.module.biz.dal.mysql.schoolcollege.SchoolCollegeMapper;
import com.hongguoyan.module.biz.dal.mysql.schooldirection.SchoolDirectionMapper;
import com.hongguoyan.module.biz.dal.mysql.schoolrank.SchoolRankMapper;
import com.hongguoyan.module.biz.dal.mysql.undergraduatemajor.UndergraduateMajorMapper;
import com.hongguoyan.module.biz.dal.mysql.userprofile.UserProfileMapper;
import com.hongguoyan.module.biz.service.vipbenefit.VipBenefitService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.*;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;
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
    private VipBenefitService vipBenefitService;
    @Resource
    private UndergraduateMajorMapper undergraduateMajorMapper;

    @Resource
    private SchoolRankMapper schoolRankMapper;

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
        boolean shouldUpdateFirstChoice = existing == null || existing.getTargetDirectionId() == null;
        Long effectiveDirectionId = shouldUpdateFirstChoice ? reqVO.getTargetDirectionId() : existing.getTargetDirectionId();
        SchoolDirectionDO direction = effectiveDirectionId != null ? schoolDirectionMapper.selectById(effectiveDirectionId) : null;
        int subjectCount = resolveExamSubjectCount(direction, existing, reqVO);
        validateSubjectScores(reqVO, subjectCount);
        validateScoreTotal(reqVO, subjectCount);
        validateGraduateAverageScore(reqVO);
        validateCetScores(reqVO);

        UserProfileDO toSave = buildBaseToSave(userId, reqVO);
        // 已有一志愿则不再更新（避免客户端传错方向导致保存失败）
        if (shouldUpdateFirstChoice) {
            fillFirstChoiceFromDirection(toSave, direction, effectiveDirectionId);
        } else {
            copyFirstChoiceFromExisting(toSave, existing);
        }
        normalizeSubjectScoresBySubjectCount(toSave, subjectCount);

        if (existing == null) {
            toSave.setId(null);
            toSave.setEditNum(0);
            userProfileMapper.insert(toSave);
            // open major category (best-effort, idempotent)
            openMajorCategory(userId, toSave);
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

        // Non-first-choice info can only be modified once.
        boolean otherInfoChanged = hasOtherInfoChanged(existing, toSave);
        int used = existing.getEditNum() != null ? existing.getEditNum() : 0;
        if (otherInfoChanged) {
            // 除一志愿外信息仅允许修改 2 次；一志愿不可改逻辑保留
            if (used >= 2) {
                throw exception(USER_PROFILE_EDIT_EXCEEDED);
            }
            toSave.setEditNum(used + 1);
        } else {
            toSave.setEditNum(existing.getEditNum());
        }

        userProfileMapper.updateById(toSave);
        // open major category (best-effort, idempotent)
        openMajorCategory(userId, toSave);
        return existing.getId();
    }

    private void validateSubjectScores(AppUserProfileSaveReqVO reqVO, int subjectCount) {
        if (reqVO == null) {
            return;
        }
        BigDecimal s1 = reqVO.getSubjectScore1();
        BigDecimal s2 = reqVO.getSubjectScore2();
        BigDecimal s3 = reqVO.getSubjectScore3();
        BigDecimal s4 = reqVO.getSubjectScore4();

        // 不允许负数
        if (isNegative(s1) || isNegative(s2) || isNegative(s3) || isNegative(s4)) {
            throw exception(USER_PROFILE_SUBJECT_SCORE_NEGATIVE);
        }

        // 2/3 门：不应填写不存在的科目（兼容旧客户端传 0）
        if (subjectCount <= 3 && s4 != null && s4.compareTo(BigDecimal.ZERO) != 0) {
            throw exception(USER_PROFILE_SUBJECT_SCORE4_NOT_ALLOWED);
        }
        if (subjectCount <= 2) {
            if (s3 != null && s3.compareTo(BigDecimal.ZERO) != 0) {
                throw exception(USER_PROFILE_SUBJECT_SCORE3_NOT_ALLOWED);
            }
            if (s4 != null && s4.compareTo(BigDecimal.ZERO) != 0) {
                throw exception(USER_PROFILE_SUBJECT_SCORE4_NOT_ALLOWED);
            }
        }

        // 按“考几门”决定每科满分
        if (subjectCount == 2) {
            // 科目1 上限 200；科目2 上限 100
            if (s1 != null && s1.compareTo(BigDecimal.valueOf(200)) > 0) {
                throw exception(USER_PROFILE_SUBJECT_SCORE1_EXCEEDED_200);
            }
            if (s2 != null && s2.compareTo(BigDecimal.valueOf(100)) > 0) {
                throw exception(USER_PROFILE_SUBJECT_SCORE12_EXCEEDED_100);
            }
            return;
        }
        if (subjectCount == 3) {
            // 科目1/2 上限 100；科目3 上限 300
            if ((s1 != null && s1.compareTo(BigDecimal.valueOf(100)) > 0)
                    || (s2 != null && s2.compareTo(BigDecimal.valueOf(100)) > 0)) {
                throw exception(USER_PROFILE_SUBJECT_SCORE12_EXCEEDED_100);
            }
            if (s3 != null && s3.compareTo(BigDecimal.valueOf(300)) > 0) {
                throw exception(USER_PROFILE_SUBJECT_SCORE34_EXCEEDED_300);
            }
            return;
        }
        // 默认按 4 门处理：科目1/2=100，科目3/4=150
        if ((s1 != null && s1.compareTo(BigDecimal.valueOf(100)) > 0)
                || (s2 != null && s2.compareTo(BigDecimal.valueOf(100)) > 0)) {
            throw exception(USER_PROFILE_SUBJECT_SCORE12_EXCEEDED_100);
        }
        if (s3 != null && s3.compareTo(BigDecimal.valueOf(150)) > 0) {
            throw exception(USER_PROFILE_SUBJECT_SCORE3_EXCEEDED_150);
        }
        if (s4 != null && s4.compareTo(BigDecimal.valueOf(150)) > 0) {
            throw exception(USER_PROFILE_SUBJECT_SCORE4_EXCEEDED_150);
        }
    }

    private void validateScoreTotal(AppUserProfileSaveReqVO reqVO, int subjectCount) {
        if (reqVO == null) {
            return;
        }
        BigDecimal total = reqVO.getScoreTotal();
        if (total == null) {
            return;
        }
        if (isNegative(total)) {
            throw exception(USER_PROFILE_SCORE_TOTAL_NEGATIVE);
        }
        int maxTotal = subjectCount == 2 ? 300 : 500;
        if (total.compareTo(BigDecimal.valueOf(maxTotal)) > 0) {
            throw exception(subjectCount == 2 ? USER_PROFILE_SCORE_TOTAL_EXCEEDED_300 : USER_PROFILE_SCORE_TOTAL_EXCEEDED_500);
        }
    }

    private void validateGraduateAverageScore(AppUserProfileSaveReqVO reqVO) {
        if (reqVO == null) {
            return;
        }
        BigDecimal v = reqVO.getGraduateAverageScore();
        if (v == null) {
            return;
        }
        if (isNegative(v)) {
            throw exception(USER_PROFILE_GRADUATE_AVERAGE_SCORE_NEGATIVE);
        }
        if (v.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw exception(USER_PROFILE_GRADUATE_AVERAGE_SCORE_EXCEEDED_100);
        }
    }

    private void validateCetScores(AppUserProfileSaveReqVO reqVO) {
        if (reqVO == null) {
            return;
        }
        Integer cet4 = reqVO.getCet4Score();
        if (cet4 != null && (cet4 < 425 || cet4 > 710)) {
            throw exception(USER_PROFILE_CET4_SCORE_OUT_OF_RANGE);
        }
        Integer cet6 = reqVO.getCet6Score();
        if (cet6 != null && (cet6 < 425 || cet6 > 710)) {
            throw exception(USER_PROFILE_CET6_SCORE_OUT_OF_RANGE);
        }
    }

    private boolean isNegative(BigDecimal v) {
        return v != null && v.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * 规范化无效科目分数，避免旧客户端把不存在的科目落库为 0。
     */
    private void normalizeSubjectScoresBySubjectCount(UserProfileDO toSave, int subjectCount) {
        if (toSave == null) {
            return;
        }
        if (subjectCount <= 2) {
            toSave.setSubjectScore3(null);
            toSave.setSubjectScore4(null);
        } else if (subjectCount == 3) {
            toSave.setSubjectScore4(null);
        }
    }

    private int resolveExamSubjectCount(SchoolDirectionDO direction, UserProfileDO existing, AppUserProfileSaveReqVO reqVO) {
        int count = countFromDirectionSubjects(direction != null ? direction.getSubjects() : null);
        if (count >= 2) {
            return count;
        }
        count = countFromExistingProfile(existing);
        if (count >= 2) {
            return count;
        }
        count = countFromReqScores(reqVO);
        if (count >= 2) {
            return count;
        }
        // 兜底按 4 门处理（兼容历史脏数据/配置缺失）
        return 4;
    }

    private int countFromExistingProfile(UserProfileDO existing) {
        if (existing == null) {
            return 0;
        }
        int c = 0;
        if (StrUtil.isNotBlank(existing.getSubjectCode1()) || StrUtil.isNotBlank(existing.getSubjectName1())) c++;
        if (StrUtil.isNotBlank(existing.getSubjectCode2()) || StrUtil.isNotBlank(existing.getSubjectName2())) c++;
        if (StrUtil.isNotBlank(existing.getSubjectCode3()) || StrUtil.isNotBlank(existing.getSubjectName3())) c++;
        if (StrUtil.isNotBlank(existing.getSubjectCode4()) || StrUtil.isNotBlank(existing.getSubjectName4())) c++;
        return c;
    }

    private int countFromReqScores(AppUserProfileSaveReqVO reqVO) {
        if (reqVO == null) {
            return 0;
        }
        int c = 0;
        if (isPositive(reqVO.getSubjectScore1())) c++;
        if (isPositive(reqVO.getSubjectScore2())) c++;
        if (isPositive(reqVO.getSubjectScore3())) c++;
        if (isPositive(reqVO.getSubjectScore4())) c++;
        return c;
    }

    private boolean isPositive(BigDecimal v) {
        return v != null && v.compareTo(BigDecimal.ZERO) > 0;
    }

    private int countFromDirectionSubjects(String subjectsJson) {
        if (StrUtil.isBlank(subjectsJson)) {
            return 0;
        }
        try {
            JSONObject obj = JSONUtil.parseObj(subjectsJson);
            int c = 0;
            if (hasValidSubjectItem(obj.getJSONArray("s1"))) c++;
            if (hasValidSubjectItem(obj.getJSONArray("s2"))) c++;
            if (hasValidSubjectItem(obj.getJSONArray("s3"))) c++;
            if (hasValidSubjectItem(obj.getJSONArray("s4"))) c++;
            return c;
        } catch (Exception ignore) {
            return 0;
        }
    }

    private boolean hasValidSubjectItem(JSONArray arr) {
        if (arr == null || arr.isEmpty()) {
            return false;
        }
        Object first = arr.get(0);
        if (!(first instanceof JSONObject)) {
            return false;
        }
        JSONObject item = (JSONObject) first;
        return StrUtil.isNotBlank(item.getStr("code")) || StrUtil.isNotBlank(item.getStr("name"));
    }

    /**
     * "Other info" means all fields except the first-choice target* group.
     */
    private boolean hasOtherInfoChanged(UserProfileDO existing, UserProfileDO toSave) {
        if (existing == null || toSave == null) {
            return false;
        }
        // Graduate/basic
        if (!Objects.equals(existing.getGraduateSchoolId(), toSave.getGraduateSchoolId())) return true;
        if (!Objects.equals(existing.getGraduateMajorId(), toSave.getGraduateMajorId())) return true;
        if (!Objects.equals(existing.getGraduateAverageScore(), toSave.getGraduateAverageScore())) return true;
        if (!Objects.equals(existing.getUndergraduateGpa(), toSave.getUndergraduateGpa())) return true;
        if (!Objects.equals(existing.getGraduateMajorRank(), toSave.getGraduateMajorRank())) return true;
        if (!Objects.equals(existing.getCet4Score(), toSave.getCet4Score())) return true;
        if (!Objects.equals(existing.getCet6Score(), toSave.getCet6Score())) return true;
        if (!Objects.equals(existing.getUndergraduateAwards(), toSave.getUndergraduateAwards())) return true;
        if (!Objects.equals(existing.getAwardCount(), toSave.getAwardCount())) return true;
        if (!Objects.equals(existing.getIsNationalScholarship(), toSave.getIsNationalScholarship())) return true;
        if (!Objects.equals(existing.getIsSchoolScholarship(), toSave.getIsSchoolScholarship())) return true;

        // Scores & background
        if (!Objects.equals(existing.getSubjectScore1(), toSave.getSubjectScore1())) return true;
        if (!Objects.equals(existing.getSubjectScore2(), toSave.getSubjectScore2())) return true;
        if (!Objects.equals(existing.getSubjectScore3(), toSave.getSubjectScore3())) return true;
        if (!Objects.equals(existing.getSubjectScore4(), toSave.getSubjectScore4())) return true;
        if (!Objects.equals(existing.getScoreTotal(), toSave.getScoreTotal())) return true;

        if (!Objects.equals(existing.getSelfIntroduction(), toSave.getSelfIntroduction())) return true;
        if (!Objects.equals(existing.getPaperCount(), toSave.getPaperCount())) return true;
        if (!Objects.equals(existing.getCompetitionIds(), toSave.getCompetitionIds())) return true;
        if (!Objects.equals(existing.getCompetitionCount(), toSave.getCompetitionCount())) return true;
        if (!Objects.equals(existing.getSelfAssessedScore(), toSave.getSelfAssessedScore())) return true;

        return false;
    }

    private void openMajorCategory(Long userId, UserProfileDO toSave) {
        // Open major category by current profile's target major code (idempotent)
        String majorCode = StrUtil.blankToDefault(toSave.getTargetMajorCode(), "").trim();
        if (majorCode.isEmpty() || majorCode.length() < 2) {
            throw exception(new ErrorCode(400, "目标专业代码缺失，无法开通门类"));
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

    private UserProfileDO buildBaseToSave(Long userId, AppUserProfileSaveReqVO reqVO) {
        if (reqVO == null) {
            throw exception(new ErrorCode(400, "request body is required"));
        }
        UserProfileDO toSave = new UserProfileDO();
        toSave.setUserId(userId);

        // graduate school (id -> name snapshot)
        Long graduateSchoolId = reqVO.getGraduateSchoolId();
        SchoolRankDO schoolRankDO = schoolRankMapper.selectById(graduateSchoolId);

        if (schoolRankDO == null) {
            throw exception(new ErrorCode(400, "graduateSchoolId not exists: " + graduateSchoolId));
        }
        toSave.setGraduateSchoolId(graduateSchoolId);
        toSave.setGraduateSchoolName(StrUtil.blankToDefault(schoolRankDO.getSchoolName(), ""));

        // graduate major (id -> name snapshot)
        Long graduateMajorId = reqVO.getGraduateMajorId();
        UndergraduateMajorDO graduateMajor = undergraduateMajorMapper.selectById(graduateMajorId);
        if (graduateMajor == null || Boolean.TRUE.equals(graduateMajor.getDeleted())) {
            throw exception(new ErrorCode(400, "graduateMajorId not exists: " + graduateMajorId));
        }
        toSave.setGraduateMajorId(graduateMajorId);
        toSave.setGraduateMajorName(StrUtil.blankToDefault(graduateMajor.getName(), ""));

        // base score & background
        toSave.setGraduateAverageScore(reqVO.getGraduateAverageScore());
        toSave.setUndergraduateGpa(reqVO.getUndergraduateGpa());
        toSave.setGraduateMajorRank(reqVO.getGraduateMajorRank());
        toSave.setCet4Score(reqVO.getCet4Score());
        toSave.setCet6Score(reqVO.getCet6Score());
        toSave.setUndergraduateAwards(reqVO.getUndergraduateAwards());
        toSave.setAwardCount(reqVO.getAwardCount());
        toSave.setIsNationalScholarship(reqVO.getIsNationalScholarship());
        toSave.setIsSchoolScholarship(reqVO.getIsSchoolScholarship());

        toSave.setSubjectScore1(reqVO.getSubjectScore1());
        toSave.setSubjectScore2(reqVO.getSubjectScore2());
        toSave.setSubjectScore3(reqVO.getSubjectScore3());
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

    private void fillFirstChoiceFromDirection(UserProfileDO toSave, SchoolDirectionDO direction, Long targetDirectionId) {
        // target direction (id -> school/college/major + snapshots)
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

        // subjects: snapshot from direction.subjects JSON; each sx is an array, take the first item
        fillSubjectsFromDirectionSubjects(toSave, direction.getSubjects());
    }

    private void copyFirstChoiceFromExisting(UserProfileDO toSave, UserProfileDO existing) {
        if (toSave == null || existing == null) {
            return;
        }
        toSave.setTargetSchoolId(existing.getTargetSchoolId());
        toSave.setTargetSchoolName(existing.getTargetSchoolName());
        toSave.setTargetCollegeId(existing.getTargetCollegeId());
        toSave.setTargetCollegeName(existing.getTargetCollegeName());
        toSave.setTargetMajorId(existing.getTargetMajorId());
        toSave.setTargetMajorCode(existing.getTargetMajorCode());
        toSave.setTargetMajorName(existing.getTargetMajorName());
        toSave.setTargetDegreeType(existing.getTargetDegreeType());
        toSave.setTargetDirectionId(existing.getTargetDirectionId());
        toSave.setTargetDirectionCode(existing.getTargetDirectionCode());
        toSave.setTargetDirectionName(existing.getTargetDirectionName());

        toSave.setSubjectCode1(existing.getSubjectCode1());
        toSave.setSubjectName1(existing.getSubjectName1());
        toSave.setSubjectCode2(existing.getSubjectCode2());
        toSave.setSubjectName2(existing.getSubjectName2());
        toSave.setSubjectCode3(existing.getSubjectCode3());
        toSave.setSubjectName3(existing.getSubjectName3());
        toSave.setSubjectCode4(existing.getSubjectCode4());
        toSave.setSubjectName4(existing.getSubjectName4());
    }

    private void fillSubjectsFromDirectionSubjects(UserProfileDO toSave, String subjectsJson) {
        if (toSave == null) {
            return;
        }
        // Default to empty to avoid stale values
        toSave.setSubjectCode1("");
        toSave.setSubjectName1("");
        toSave.setSubjectCode2("");
        toSave.setSubjectName2("");
        toSave.setSubjectCode3("");
        toSave.setSubjectName3("");
        toSave.setSubjectCode4("");
        toSave.setSubjectName4("");

        if (StrUtil.isBlank(subjectsJson)) {
            return;
        }
        try {
            JSONObject obj = JSONUtil.parseObj(subjectsJson);
            fillOneSubject(toSave, 1, obj.getJSONArray("s1"));
            fillOneSubject(toSave, 2, obj.getJSONArray("s2"));
            fillOneSubject(toSave, 3, obj.getJSONArray("s3"));
            fillOneSubject(toSave, 4, obj.getJSONArray("s4"));
        } catch (Exception ignore) {
            // best-effort: if parsing fails, keep empty names/codes
        }
    }

    private void fillOneSubject(UserProfileDO toSave, int no, JSONArray arr) {
        if (toSave == null || arr == null || arr.isEmpty()) {
            return;
        }
        Object first = arr.get(0);
        if (!(first instanceof JSONObject)) {
            return;
        }
        JSONObject item = (JSONObject) first;
        String code = StrUtil.blankToDefault(item.getStr("code"), "");
        String name = StrUtil.blankToDefault(item.getStr("name"), "");
        switch (no) {
            case 1 -> {
                toSave.setSubjectCode1(code);
                toSave.setSubjectName1(name);
            }
            case 2 -> {
                toSave.setSubjectCode2(code);
                toSave.setSubjectName2(name);
            }
            case 3 -> {
                toSave.setSubjectCode3(code);
                toSave.setSubjectName3(name);
            }
            case 4 -> {
                toSave.setSubjectCode4(code);
                toSave.setSubjectName4(name);
            }
            default -> {
            }
        }
    }

}

