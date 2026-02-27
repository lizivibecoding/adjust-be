package com.hongguoyan.module.biz.service.userprofile;

import com.hongguoyan.framework.test.core.ut.BaseMockitoUnitTest;
import com.hongguoyan.module.biz.controller.app.userprofile.vo.AppUserProfileSaveReqVO;
import com.hongguoyan.module.biz.dal.dataobject.major.MajorDO;
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
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Set;

import static com.hongguoyan.framework.test.core.util.AssertUtils.assertServiceException;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

/**
 * profile 初试分数校验相关单测
 *
 * 说明：这里直接覆盖 {@link UserProfileServiceImpl#saveUserProfileByUserId(Long, AppUserProfileSaveReqVO)}
 * 的分数校验逻辑（不走 controller 鉴权/参数校验），保证错误码稳定。
 */
class UserProfileServiceImplScoreValidationTest extends BaseMockitoUnitTest {

    private static final Logger log = LoggerFactory.getLogger(UserProfileServiceImplScoreValidationTest.class);

    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    @Mock
    private UserProfileMapper userProfileMapper;
    @Mock
    private SchoolDirectionMapper schoolDirectionMapper;
    @Mock
    private SchoolRankMapper schoolRankMapper;
    @Mock
    private UndergraduateMajorMapper undergraduateMajorMapper;
    @Mock
    private MajorMapper majorMapper;
    @Mock
    private SchoolMapper schoolMapper;
    @Mock
    private SchoolCollegeMapper schoolCollegeMapper;
    @Mock
    private VipBenefitService vipBenefitService;

    @Test
    void test2Subjects_subject3NotAllowed() {
        mockNoExistingProfile();
        when(schoolDirectionMapper.selectById(4L)).thenReturn(mockDirection2Subjects(4L, 10001L));

        AppUserProfileSaveReqVO reqVO = baseReq(4L);
        reqVO.setSubjectScore3(BigDecimal.ONE); // 2 门不允许填科目三（非 0）

        assertServiceException(() -> userProfileService.saveUserProfileByUserId(1L, reqVO),
                USER_PROFILE_SUBJECT_SCORE3_NOT_ALLOWED);
        log.info("[OK] 2门-科目3不允许填写：命中 {}", USER_PROFILE_SUBJECT_SCORE3_NOT_ALLOWED.getMsg());
    }

    @Test
    void test3Subjects_subject4NotAllowed() {
        mockNoExistingProfile();
        when(schoolDirectionMapper.selectById(53L)).thenReturn(mockDirection3Subjects(53L, 10001L));

        AppUserProfileSaveReqVO reqVO = baseReq(53L);
        reqVO.setSubjectScore4(BigDecimal.TEN); // 3 门不允许填科目四（非 0）

        assertServiceException(() -> userProfileService.saveUserProfileByUserId(1L, reqVO),
                USER_PROFILE_SUBJECT_SCORE4_NOT_ALLOWED);
        log.info("[OK] 3门-科目4不允许填写：命中 {}", USER_PROFILE_SUBJECT_SCORE4_NOT_ALLOWED.getMsg());
    }

    @Test
    void test4Subjects_subject3Exceeded150() {
        mockNoExistingProfile();
        when(schoolDirectionMapper.selectById(1L)).thenReturn(mockDirection4Subjects(1L, 10001L));

        AppUserProfileSaveReqVO reqVO = baseReq(1L);
        reqVO.setSubjectScore3(new BigDecimal("151"));

        assertServiceException(() -> userProfileService.saveUserProfileByUserId(1L, reqVO),
                USER_PROFILE_SUBJECT_SCORE3_EXCEEDED_150);
        log.info("[OK] 4门-科目3超过150：命中 {}", USER_PROFILE_SUBJECT_SCORE3_EXCEEDED_150.getMsg());
    }

    @Test
    void testScoreTotalNegative() {
        mockNoExistingProfile();
        when(schoolDirectionMapper.selectById(1L)).thenReturn(mockDirection4Subjects(1L, 10001L));

        AppUserProfileSaveReqVO reqVO = baseReq(1L);
        reqVO.setScoreTotal(new BigDecimal("-1"));

        assertServiceException(() -> userProfileService.saveUserProfileByUserId(1L, reqVO),
                USER_PROFILE_SCORE_TOTAL_NEGATIVE);
        log.info("[OK] 总分为负数：命中 {}", USER_PROFILE_SCORE_TOTAL_NEGATIVE.getMsg());
    }

    @Test
    void test2Subjects_scoreTotalExceeded300() {
        mockNoExistingProfile();
        when(schoolDirectionMapper.selectById(4L)).thenReturn(mockDirection2Subjects(4L, 10001L));

        AppUserProfileSaveReqVO reqVO = baseReq(4L);
        reqVO.setScoreTotal(new BigDecimal("301"));

        assertServiceException(() -> userProfileService.saveUserProfileByUserId(1L, reqVO),
                USER_PROFILE_SCORE_TOTAL_EXCEEDED_300);
        log.info("[OK] 2门-总分超过300：命中 {}", USER_PROFILE_SCORE_TOTAL_EXCEEDED_300.getMsg());
    }

    @Test
    void test2Subjects_subject1Exceeded200() {
        mockNoExistingProfile();
        when(schoolDirectionMapper.selectById(4L)).thenReturn(mockDirection2Subjects(4L, 10001L));

        AppUserProfileSaveReqVO reqVO = baseReq(4L);
        reqVO.setSubjectScore1(new BigDecimal("201"));

        assertServiceException(() -> userProfileService.saveUserProfileByUserId(1L, reqVO),
                USER_PROFILE_SUBJECT_SCORE1_EXCEEDED_200);
        log.info("[OK] 2门-科目1超过200：命中 {}", USER_PROFILE_SUBJECT_SCORE1_EXCEEDED_200.getMsg());
    }

    @Test
    void test2Subjects_subject2Exceeded100() {
        mockNoExistingProfile();
        when(schoolDirectionMapper.selectById(4L)).thenReturn(mockDirection2Subjects(4L, 10001L));

        AppUserProfileSaveReqVO reqVO = baseReq(4L);
        reqVO.setSubjectScore2(new BigDecimal("101"));

        assertServiceException(() -> userProfileService.saveUserProfileByUserId(1L, reqVO),
                USER_PROFILE_SUBJECT_SCORE12_EXCEEDED_100);
        log.info("[OK] 2门-科目2超过100：命中 {}", USER_PROFILE_SUBJECT_SCORE12_EXCEEDED_100.getMsg());
    }

    @Test
    void test3Subjects_subject3Exceeded300() {
        mockNoExistingProfile();
        when(schoolDirectionMapper.selectById(53L)).thenReturn(mockDirection3Subjects(53L, 10001L));

        AppUserProfileSaveReqVO reqVO = baseReq(53L);
        reqVO.setSubjectScore3(new BigDecimal("301"));

        assertServiceException(() -> userProfileService.saveUserProfileByUserId(1L, reqVO),
                USER_PROFILE_SUBJECT_SCORE34_EXCEEDED_300);
        log.info("[OK] 3门-科目3超过300：命中 {}", USER_PROFILE_SUBJECT_SCORE34_EXCEEDED_300.getMsg());
    }

    @Test
    void test4Subjects_subject4Exceeded150() {
        mockNoExistingProfile();
        when(schoolDirectionMapper.selectById(1L)).thenReturn(mockDirection4Subjects(1L, 10001L));

        AppUserProfileSaveReqVO reqVO = baseReq(1L);
        reqVO.setSubjectScore4(new BigDecimal("151"));

        assertServiceException(() -> userProfileService.saveUserProfileByUserId(1L, reqVO),
                USER_PROFILE_SUBJECT_SCORE4_EXCEEDED_150);
        log.info("[OK] 4门-科目4超过150：命中 {}", USER_PROFILE_SUBJECT_SCORE4_EXCEEDED_150.getMsg());
    }

    @Test
    void test4Subjects_scoreTotalExceeded500() {
        mockNoExistingProfile();
        when(schoolDirectionMapper.selectById(1L)).thenReturn(mockDirection4Subjects(1L, 10001L));

        AppUserProfileSaveReqVO reqVO = baseReq(1L);
        reqVO.setScoreTotal(new BigDecimal("501"));

        assertServiceException(() -> userProfileService.saveUserProfileByUserId(1L, reqVO),
                USER_PROFILE_SCORE_TOTAL_EXCEEDED_500);
        log.info("[OK] 4门-总分超过500：命中 {}", USER_PROFILE_SCORE_TOTAL_EXCEEDED_500.getMsg());
    }

    @Test
    void testSubjectScoreNegative() {
        mockNoExistingProfile();
        when(schoolDirectionMapper.selectById(1L)).thenReturn(mockDirection4Subjects(1L, 10001L));

        AppUserProfileSaveReqVO reqVO = baseReq(1L);
        reqVO.setSubjectScore1(new BigDecimal("-0.1"));

        assertServiceException(() -> userProfileService.saveUserProfileByUserId(1L, reqVO),
                USER_PROFILE_SUBJECT_SCORE_NEGATIVE);
        log.info("[OK] 科目分数为负数：命中 {}", USER_PROFILE_SUBJECT_SCORE_NEGATIVE.getMsg());
    }

    @Test
    void test2Subjects_subject3ZeroShouldBeIgnored_andSaveSuccess() {
        mockNoExistingProfile();
        when(schoolDirectionMapper.selectById(4L)).thenReturn(mockDirection2Subjects(4L, 10001L));
        mockSaveDependencies();

        AppUserProfileSaveReqVO reqVO = baseReq(4L);
        reqVO.setSubjectScore3(BigDecimal.ZERO); // 兼容旧端传 0：不应报错
        reqVO.setSubjectScore1(new BigDecimal("199"));
        reqVO.setSubjectScore2(new BigDecimal("99"));
        reqVO.setScoreTotal(new BigDecimal("298"));

        Long id = userProfileService.saveUserProfileByUserId(1L, reqVO);
        org.junit.jupiter.api.Assertions.assertNotNull(id);
        log.info("[OK] 2门-科目3传0兼容：保存成功 id={}", id);
    }

    private void mockNoExistingProfile() {
        when(userProfileMapper.selectOne(any())).thenReturn(null);
    }

    private void mockSaveDependencies() {
        SchoolRankDO schoolRank = new SchoolRankDO();
        schoolRank.setId(3320L);
        schoolRank.setSchoolName("mock-school");
        when(schoolRankMapper.selectById(3320L)).thenReturn(schoolRank);

        UndergraduateMajorDO major = new UndergraduateMajorDO();
        major.setId(7856L);
        major.setName("mock-major");
        major.setDeleted(false);
        when(undergraduateMajorMapper.selectById(7856L)).thenReturn(major);

        MajorDO targetMajor = new MajorDO();
        targetMajor.setId(10001L);
        targetMajor.setCode("12XX");
        targetMajor.setName("mock-target-major");
        targetMajor.setDegreeType(1);
        when(majorMapper.selectById(10001L)).thenReturn(targetMajor);

        // 避免 openMajorCategory 走真实权益消耗
        when(vipBenefitService.getConsumedUniqueKeys(any(), any())).thenReturn(Set.of("already-opened"));

        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);
            if (arg0 instanceof UserProfileDO userProfileDO) {
                userProfileDO.setId(999L);
            }
            return 1;
        }).when(userProfileMapper).insert(any(UserProfileDO.class));
    }

    private AppUserProfileSaveReqVO baseReq(Long targetDirectionId) {
        AppUserProfileSaveReqVO reqVO = new AppUserProfileSaveReqVO();
        reqVO.setGraduateSchoolId(3320L);
        reqVO.setGraduateMajorId(7856L);
        reqVO.setTargetDirectionId(targetDirectionId);
        reqVO.setSelfAssessedScore(5);
        reqVO.setSelfIntroduction("x");
        return reqVO;
    }

    private SchoolDirectionDO mockDirection2Subjects(Long directionId, Long majorId) {
        SchoolDirectionDO direction = new SchoolDirectionDO();
        direction.setId(directionId);
        direction.setMajorId(majorId);
        direction.setSubjects("""
                {"s1":[{"code":"199","name":"管理类综合能力"}],
                 "s2":[{"code":"204","name":"英语（二）"}],
                 "s3":[],
                 "s4":[]}
                """);
        return direction;
    }

    private SchoolDirectionDO mockDirection3Subjects(Long directionId, Long majorId) {
        SchoolDirectionDO direction = new SchoolDirectionDO();
        direction.setId(directionId);
        direction.setMajorId(majorId);
        direction.setSubjects("""
                {"s1":[{"code":"101","name":"思想政治理论"}],
                 "s2":[{"code":"204","name":"英语（二）"}],
                 "s3":[{"code":"347","name":"心理学专业综合"}],
                 "s4":[]}
                """);
        return direction;
    }

    private SchoolDirectionDO mockDirection4Subjects(Long directionId, Long majorId) {
        SchoolDirectionDO direction = new SchoolDirectionDO();
        direction.setId(directionId);
        direction.setMajorId(majorId);
        direction.setSubjects("""
                {"s1":[{"code":"101","name":"思想政治理论"}],
                 "s2":[{"code":"201","name":"英语（一）"}],
                 "s3":[{"code":"303","name":"数学（三）"}],
                 "s4":[{"code":"431","name":"金融学综合"}]}
                """);
        return direction;
    }
}

