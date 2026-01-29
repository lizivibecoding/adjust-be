package com.hongguoyan.module.biz.service.adjustment;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hongguoyan.framework.common.exception.ErrorCode;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.AppSchoolSpecialOptionsReqVO;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.AppSchoolSpecialOptionsRespVO;
import com.hongguoyan.module.biz.dal.dataobject.major.MajorDO;
import com.hongguoyan.module.biz.dal.dataobject.nationalscore.NationalScoreDO;
import com.hongguoyan.module.biz.dal.dataobject.school.SchoolDO;
import com.hongguoyan.module.biz.dal.dataobject.schoolcollege.SchoolCollegeDO;
import com.hongguoyan.module.biz.dal.dataobject.schooldirection.SchoolDirectionDO;
import com.hongguoyan.module.biz.dal.dataobject.schoolmajor.SchoolMajorDO;
import com.hongguoyan.module.biz.dal.dataobject.schoolscore.SchoolScoreDO;
import com.hongguoyan.module.biz.dal.mysql.major.MajorMapper;
import com.hongguoyan.module.biz.dal.mysql.nationalscore.NationalScoreMapper;
import com.hongguoyan.module.biz.dal.mysql.school.SchoolMapper;
import com.hongguoyan.module.biz.dal.mysql.schoolcollege.SchoolCollegeMapper;
import com.hongguoyan.module.biz.dal.mysql.schooldirection.SchoolDirectionMapper;
import com.hongguoyan.module.biz.dal.mysql.schoolmajor.SchoolMajorMapper;
import com.hongguoyan.module.biz.dal.mysql.schoolscore.SchoolScoreMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.*;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

@Service
@Validated
public class SchoolSpecialOptionsServiceImpl implements SchoolSpecialOptionsService {

    private static final int TYPE_COLLEGE = 1;
    private static final int TYPE_MAJOR_AND_STUDY_MODE = 2;
    private static final int TYPE_DIRECTION = 3;
    private static final int TYPE_SCORE_LIMIT = 4;

    @Resource
    private SchoolCollegeMapper schoolCollegeMapper;
    @Resource
    private SchoolMajorMapper schoolMajorMapper;
    @Resource
    private SchoolDirectionMapper schoolDirectionMapper;
    @Resource
    private SchoolScoreMapper schoolScoreMapper;
    @Resource
    private NationalScoreMapper nationalScoreMapper;
    @Resource
    private SchoolMapper schoolMapper;
    @Resource
    private MajorMapper majorMapper;

    @Override
    public AppSchoolSpecialOptionsRespVO getOptions(AppSchoolSpecialOptionsReqVO reqVO) {
        Integer type = reqVO != null ? reqVO.getType() : null;
        if (type == null) {
            throw exception(new ErrorCode(400, "type is required"));
        }
        AppSchoolSpecialOptionsRespVO respVO = new AppSchoolSpecialOptionsRespVO();
        respVO.setType(type);
        if (TYPE_COLLEGE == type) {
            respVO.setOptions(buildCollegeOptions(reqVO));
            return respVO;
        }
        if (TYPE_MAJOR_AND_STUDY_MODE == type) {
            buildMajorAndStudyModeOptions(respVO, reqVO);
            return respVO;
        }
        if (TYPE_DIRECTION == type) {
            respVO.setOptions(buildDirectionOptions(reqVO));
            return respVO;
        }
        if (TYPE_SCORE_LIMIT == type) {
            respVO.setScoreLimit(buildScoreLimit(reqVO));
            return respVO;
        }
        throw exception(new ErrorCode(400, "unknown type: " + type));
    }

    private List<AppSchoolSpecialOptionsRespVO.Option> buildCollegeOptions(AppSchoolSpecialOptionsReqVO reqVO) {
        Long schoolId = reqVO.getSchoolId();
        if (schoolId == null) {
            throw exception(new ErrorCode(400, "schoolId is required"));
        }
        List<SchoolCollegeDO> list = schoolCollegeMapper.selectList(new LambdaQueryWrapper<SchoolCollegeDO>()
                .select(SchoolCollegeDO::getId, SchoolCollegeDO::getName)
                .eq(SchoolCollegeDO::getSchoolId, schoolId)
                .eq(SchoolCollegeDO::getDeleted, false)
                .orderByAsc(SchoolCollegeDO::getName));
        List<AppSchoolSpecialOptionsRespVO.Option> options = new ArrayList<>();
        for (SchoolCollegeDO item : list) {
            AppSchoolSpecialOptionsRespVO.Option opt = new AppSchoolSpecialOptionsRespVO.Option();
            opt.setId(item.getId());
            opt.setName(item.getName());
            options.add(opt);
        }
        return options;
    }

    private void buildMajorAndStudyModeOptions(AppSchoolSpecialOptionsRespVO respVO,
                                              AppSchoolSpecialOptionsReqVO reqVO) {
        Long schoolId = reqVO.getSchoolId();
        Long collegeId = reqVO.getCollegeId();
        if (schoolId == null) {
            throw exception(new ErrorCode(400, "schoolId is required"));
        }
        if (collegeId == null) {
            throw exception(new ErrorCode(400, "collegeId is required"));
        }

        List<SchoolMajorDO> majors = schoolMajorMapper.selectList(new LambdaQueryWrapper<SchoolMajorDO>()
                .select(SchoolMajorDO::getMajorId, SchoolMajorDO::getCode, SchoolMajorDO::getName)
                .eq(SchoolMajorDO::getSchoolId, schoolId)
                .eq(SchoolMajorDO::getCollegeId, collegeId)
                .eq(SchoolMajorDO::getDeleted, false)
                .orderByAsc(SchoolMajorDO::getCode));

        List<AppSchoolSpecialOptionsRespVO.Option> options = new ArrayList<>();
        List<Long> majorIds = new ArrayList<>();
        for (SchoolMajorDO item : majors) {
            if (item == null || item.getMajorId() == null) {
                continue;
            }
            majorIds.add(item.getMajorId());
            AppSchoolSpecialOptionsRespVO.Option opt = new AppSchoolSpecialOptionsRespVO.Option();
            opt.setId(item.getMajorId());
            opt.setCode(item.getCode());
            opt.setName(item.getName());
            options.add(opt);
        }
        respVO.setOptions(options);

        if (majorIds.isEmpty()) {
            respVO.setStudyModes(Collections.emptyMap());
            return;
        }

        // distinct (majorId, studyMode)
        List<SchoolDirectionDO> modes = schoolDirectionMapper.selectList(new LambdaQueryWrapper<SchoolDirectionDO>()
                .select(SchoolDirectionDO::getMajorId, SchoolDirectionDO::getStudyMode)
                .eq(SchoolDirectionDO::getSchoolId, schoolId)
                .eq(SchoolDirectionDO::getCollegeId, collegeId)
                .in(SchoolDirectionDO::getMajorId, majorIds)
                .eq(SchoolDirectionDO::getDeleted, false)
                .groupBy(SchoolDirectionDO::getMajorId, SchoolDirectionDO::getStudyMode));

        Map<Long, List<String>> map = new HashMap<>();
        for (SchoolDirectionDO item : modes) {
            if (item == null || item.getMajorId() == null) {
                continue;
            }
            String mode = StrUtil.blankToDefault(item.getStudyMode(), "");
            map.computeIfAbsent(item.getMajorId(), k -> new ArrayList<>()).add(mode);
        }
        // stable order: 全日制 -> 非全日制 -> others
        for (Map.Entry<Long, List<String>> entry : map.entrySet()) {
            List<String> list = entry.getValue();
            if (list == null) {
                continue;
            }
            list.sort((a, b) -> studyModeRank(a) - studyModeRank(b));
        }
        respVO.setStudyModes(map);
    }

    private List<AppSchoolSpecialOptionsRespVO.Option> buildDirectionOptions(AppSchoolSpecialOptionsReqVO reqVO) {
        Long schoolId = reqVO.getSchoolId();
        Long collegeId = reqVO.getCollegeId();
        Long majorId = reqVO.getMajorId();
        String studyMode = reqVO.getStudyMode();
        if (schoolId == null) {
            throw exception(new ErrorCode(400, "schoolId is required"));
        }
        if (collegeId == null) {
            throw exception(new ErrorCode(400, "collegeId is required"));
        }
        if (majorId == null) {
            throw exception(new ErrorCode(400, "majorId is required"));
        }
        if (StrUtil.isBlank(studyMode)) {
            throw exception(new ErrorCode(400, "studyMode is required"));
        }

        List<SchoolDirectionDO> list = schoolDirectionMapper.selectList(new LambdaQueryWrapper<SchoolDirectionDO>()
                .select(SchoolDirectionDO::getId, SchoolDirectionDO::getDirectionCode, SchoolDirectionDO::getDirectionName)
                .eq(SchoolDirectionDO::getSchoolId, schoolId)
                .eq(SchoolDirectionDO::getCollegeId, collegeId)
                .eq(SchoolDirectionDO::getMajorId, majorId)
                .eq(SchoolDirectionDO::getStudyMode, studyMode)
                .eq(SchoolDirectionDO::getDeleted, false)
                .orderByAsc(SchoolDirectionDO::getDirectionCode)
                .orderByAsc(SchoolDirectionDO::getDirectionName));

        List<AppSchoolSpecialOptionsRespVO.Option> options = new ArrayList<>();
        for (SchoolDirectionDO item : list) {
            AppSchoolSpecialOptionsRespVO.Option opt = new AppSchoolSpecialOptionsRespVO.Option();
            opt.setId(item.getId());
            opt.setName(buildDirectionDisplayName(item.getDirectionCode(), item.getDirectionName()));
            options.add(opt);
        }
        return options;
    }

    private AppSchoolSpecialOptionsRespVO.ScoreLimit buildScoreLimit(AppSchoolSpecialOptionsReqVO reqVO) {
        Long directionId = reqVO.getDirectionId();
        Long schoolId = reqVO.getSchoolId();
        Long majorId = reqVO.getMajorId();

        if (directionId != null) {
            SchoolDirectionDO direction = schoolDirectionMapper.selectById(directionId);
            if (direction == null) {
                throw exception(SCHOOL_DIRECTION_NOT_EXISTS);
            }
            schoolId = direction.getSchoolId();
            majorId = direction.getMajorId();
        }

        if (schoolId == null) {
            throw exception(new ErrorCode(400, "schoolId is required (or provide directionId)"));
        }
        if (majorId == null) {
            throw exception(new ErrorCode(400, "majorId is required (or provide directionId)"));
        }

        // 1) self-determined line first
        SchoolScoreDO schoolScore = schoolScoreMapper.selectOne(new LambdaQueryWrapper<SchoolScoreDO>()
                .eq(SchoolScoreDO::getSchoolId, schoolId)
                .eq(SchoolScoreDO::getMajorId, majorId)
                .eq(SchoolScoreDO::getDeleted, false)
                .orderByDesc(SchoolScoreDO::getYear)
                .last("LIMIT 1"));
        if (schoolScore != null) {
            return fromSchoolScore(schoolScore);
        }

        // 2) national line fallback (need area + degreeType)
        SchoolDO school = schoolMapper.selectById(schoolId);
        MajorDO major = majorMapper.selectById(majorId);
        String area = school != null ? school.getProvinceArea() : null;
        Integer degreeType = major != null ? major.getDegreeType() : null;
        if (StrUtil.isBlank(area) || degreeType == null) {
            // no enough info, return empty
            return new AppSchoolSpecialOptionsRespVO.ScoreLimit();
        }

        NationalScoreDO national = nationalScoreMapper.selectOne(new LambdaQueryWrapper<NationalScoreDO>()
                .eq(NationalScoreDO::getMajorId, majorId)
                .eq(NationalScoreDO::getArea, area)
                .eq(NationalScoreDO::getDegreeType, degreeType)
                .eq(NationalScoreDO::getScoreType, 1)
                .eq(NationalScoreDO::getDeleted, false)
                .orderByDesc(NationalScoreDO::getYear)
                .last("LIMIT 1"));
        if (national != null) {
            return fromNationalScore(national);
        }
        return new AppSchoolSpecialOptionsRespVO.ScoreLimit();
    }

    private AppSchoolSpecialOptionsRespVO.ScoreLimit fromSchoolScore(SchoolScoreDO score) {
        AppSchoolSpecialOptionsRespVO.ScoreLimit limit = new AppSchoolSpecialOptionsRespVO.ScoreLimit();
        limit.setPoliticsScoreLimit(toInt(score.getScoreSubject1()));
        limit.setEnglishScoreLimit(toInt(score.getScoreSubject2()));
        limit.setSpecialOneScoreLimit(toInt(score.getScoreSubject3()));
        limit.setSpecialTwoScoreLimit(toInt(score.getScoreSubject4()));
        limit.setTotalScoreLimit(toInt(score.getScoreTotal()));
        return limit;
    }

    private AppSchoolSpecialOptionsRespVO.ScoreLimit fromNationalScore(NationalScoreDO score) {
        AppSchoolSpecialOptionsRespVO.ScoreLimit limit = new AppSchoolSpecialOptionsRespVO.ScoreLimit();
        Integer single100 = score.getSingle100() != null ? score.getSingle100().intValue() : null;
        Integer single150 = score.getSingle150() != null ? score.getSingle150().intValue() : null;
        limit.setPoliticsScoreLimit(single100);
        limit.setEnglishScoreLimit(single100);
        limit.setSpecialOneScoreLimit(single150);
        limit.setSpecialTwoScoreLimit(single150);
        limit.setTotalScoreLimit(score.getTotal() != null ? score.getTotal().intValue() : null);
        return limit;
    }

    private Integer toInt(BigDecimal v) {
        if (v == null) {
            return null;
        }
        return v.intValue();
    }

    private int studyModeRank(String mode) {
        if ("全日制".equals(mode)) {
            return 0;
        }
        if ("非全日制".equals(mode)) {
            return 1;
        }
        if (mode == null) {
            return 99;
        }
        return 10;
    }

    private String buildDirectionDisplayName(String code, String name) {
        String n = StrUtil.blankToDefault(name, "");
        String c = StrUtil.blankToDefault(code, "");
        if (StrUtil.isBlank(c)) {
            return n;
        }
        if (n.startsWith("（")) {
            return n;
        }
        return "（" + c + "）" + n;
    }
}

