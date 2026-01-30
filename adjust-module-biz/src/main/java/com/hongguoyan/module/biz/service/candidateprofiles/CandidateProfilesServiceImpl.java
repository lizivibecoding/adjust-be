package com.hongguoyan.module.biz.service.candidateprofiles;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hongguoyan.module.biz.controller.app.candidateprofiles.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.candidateprofiles.CandidateProfilesDO;
import com.hongguoyan.module.biz.dal.dataobject.major.MajorDO;
import com.hongguoyan.module.biz.dal.dataobject.school.SchoolDO;
import com.hongguoyan.module.biz.dal.dataobject.schoolcollege.SchoolCollegeDO;
import com.hongguoyan.module.biz.dal.dataobject.schooldirection.SchoolDirectionDO;

import com.hongguoyan.module.biz.dal.mysql.candidateprofiles.CandidateProfilesMapper;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.dal.mysql.major.MajorMapper;
import com.hongguoyan.module.biz.dal.mysql.school.SchoolMapper;
import com.hongguoyan.module.biz.dal.mysql.schoolcollege.SchoolCollegeMapper;
import com.hongguoyan.module.biz.dal.mysql.schooldirection.SchoolDirectionMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hongguoyan.framework.common.exception.ErrorCode;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 考生基础档案表(含成绩与软背景) Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class CandidateProfilesServiceImpl implements CandidateProfilesService {

    @Resource
    private CandidateProfilesMapper candidateProfilesMapper;
    @Resource
    private SchoolMapper schoolMapper;
    @Resource
    private MajorMapper majorMapper;
    @Resource
    private SchoolDirectionMapper schoolDirectionMapper;
    @Resource
    private SchoolCollegeMapper schoolCollegeMapper;

    @Override
    public CandidateProfilesDO getCandidateProfilesByUserId(Long userId) {
        return candidateProfilesMapper.selectOne(new LambdaQueryWrapperX<CandidateProfilesDO>()
                .eq(CandidateProfilesDO::getUserId, userId));
    }

    @Override
    public Long saveCandidateProfilesByUserId(Long userId, AppCandidateProfilesSaveReqVO reqVO) {
        CandidateProfilesDO existing = getCandidateProfilesByUserId(userId);
        CandidateProfilesDO toSave = buildToSave(userId, reqVO);

        if (existing == null) {
            toSave.setId(null);
            candidateProfilesMapper.insert(toSave);
            return toSave.getId();
        }

        // 覆盖更新：按 userId 唯一行覆盖
        toSave.setId(existing.getId());
        // 目前不处理锁定/提交逻辑，这两个字段保持原值，避免被覆盖为 null
        toSave.setBasicLocked(existing.getBasicLocked());
        toSave.setSubmitTime(existing.getSubmitTime());
        candidateProfilesMapper.updateById(toSave);
        return existing.getId();
    }

    private CandidateProfilesDO buildToSave(Long userId, AppCandidateProfilesSaveReqVO reqVO) {
        if (reqVO == null) {
            throw exception(new ErrorCode(400, "request body is required"));
        }
        CandidateProfilesDO toSave = new CandidateProfilesDO();
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
        toSave.setUndergraduateGpa(reqVO.getUndergraduateGpa());
        toSave.setCet4Score(reqVO.getCet4Score());
        toSave.setCet6Score(reqVO.getCet6Score());
        toSave.setUndergraduateAwards(reqVO.getUndergraduateAwards());

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
        toSave.setPaperCount(reqVO.getPaperCount() != null ? reqVO.getPaperCount() : 0);

        // competitions: ids json + count computed on backend
        List<Long> competitionIds = reqVO.getCompetitionIds();
        if (competitionIds == null) {
            toSave.setCompetitionIds(null);
            toSave.setCompetitionCount(0);
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
            toSave.setCompetitionCount(dedup.size());
        }

        toSave.setSelfAssessedScore(reqVO.getSelfAssessedScore() != null ? reqVO.getSelfAssessedScore() : 0);
        return toSave;
    }

}