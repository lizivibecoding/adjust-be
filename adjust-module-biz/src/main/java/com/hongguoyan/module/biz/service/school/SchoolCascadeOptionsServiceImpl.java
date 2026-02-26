package com.hongguoyan.module.biz.service.school;

import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentCascadeOptionsReqVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentCascadeOptionsRespVO;
import com.hongguoyan.module.biz.dal.dataobject.schoolcollege.SchoolCollegeDO;
import com.hongguoyan.module.biz.dal.dataobject.schooldirection.SchoolDirectionDO;
import com.hongguoyan.module.biz.dal.dataobject.schoolmajor.SchoolMajorDO;
import com.hongguoyan.module.biz.enums.StudyModeEnum;
import com.hongguoyan.module.biz.service.projectconfig.ProjectConfigService;
import com.hongguoyan.module.biz.service.schoolcollege.SchoolCollegeService;
import com.hongguoyan.module.biz.service.schooldirection.SchoolDirectionService;
import com.hongguoyan.module.biz.service.schoolmajor.SchoolMajorService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 学校-调剂联动选项 Service 实现（管理后台）。
 */
@Service
@Validated
public class SchoolCascadeOptionsServiceImpl implements SchoolCascadeOptionsService {

    @Resource
    private SchoolCollegeService schoolCollegeService;
    @Resource
    private SchoolMajorService schoolMajorService;
    @Resource
    private SchoolDirectionService schoolDirectionService;
    @Resource
    private ProjectConfigService projectConfigService;

    @Override
    public AdjustmentCascadeOptionsRespVO getAdjustmentCascadeOptions(AdjustmentCascadeOptionsReqVO reqVO) {
        AdjustmentCascadeOptionsRespVO respVO = new AdjustmentCascadeOptionsRespVO();
        if (reqVO == null || reqVO.getSchoolId() == null) {
            return respVO;
        }
        Integer activeYear = projectConfigService.getActiveYear();

        Long schoolId = reqVO.getSchoolId();
        Long collegeId = reqVO.getCollegeId();
        Long majorId = reqVO.getMajorId();

        // 1) colleges
        List<SchoolCollegeDO> colleges = schoolCollegeService.getSchoolCollegeList(schoolId, activeYear);
        if (colleges != null && !colleges.isEmpty()) {
            List<AdjustmentCascadeOptionsRespVO.CollegeOption> list = new ArrayList<>(colleges.size());
            for (SchoolCollegeDO c : colleges) {
                if (c == null || c.getId() == null) continue;
                AdjustmentCascadeOptionsRespVO.CollegeOption opt = new AdjustmentCascadeOptionsRespVO.CollegeOption();
                opt.setId(c.getId());
                opt.setName(c.getName());
                list.add(opt);
            }
            respVO.setColleges(list);
        } else {
            respVO.setColleges(Collections.emptyList());
        }
        if (collegeId == null) {
            return respVO;
        }

        // 2) majors
        List<SchoolMajorDO> majors = schoolMajorService.getSchoolMajorList(schoolId, collegeId, activeYear);
        if (majors != null && !majors.isEmpty()) {
            List<AdjustmentCascadeOptionsRespVO.MajorOption> list = new ArrayList<>(majors.size());
            for (SchoolMajorDO m : majors) {
                if (m == null || m.getMajorId() == null) continue;
                AdjustmentCascadeOptionsRespVO.MajorOption opt = new AdjustmentCascadeOptionsRespVO.MajorOption();
                opt.setMajorId(m.getMajorId());
                opt.setCode(m.getCode());
                opt.setName(m.getName());
                opt.setDegreeType(m.getDegreeType());
                list.add(opt);
            }
            respVO.setMajors(list);
        } else {
            respVO.setMajors(Collections.emptyList());
        }
        if (majorId == null) {
            return respVO;
        }

        // 3) directions + study modes
        List<SchoolDirectionDO> directions = schoolDirectionService.getSchoolDirectionList(schoolId, collegeId, majorId, activeYear);
        if (directions == null || directions.isEmpty()) {
            respVO.setStudyModes(Collections.emptyList());
            respVO.setDirections(Collections.emptyList());
            return respVO;
        }

        Set<Integer> modeSet = new HashSet<>();
        for (SchoolDirectionDO d : directions) {
            if (d != null && d.getStudyMode() != null) {
                modeSet.add(d.getStudyMode());
            }
        }
        List<AdjustmentCascadeOptionsRespVO.StudyModeOption> modeOptions = new ArrayList<>();
        if (!modeSet.isEmpty()) {
            List<Integer> modes = new ArrayList<>(modeSet);
            modes.sort(Comparator.comparingInt(SchoolCascadeOptionsServiceImpl::studyModeRank));
            for (Integer code : modes) {
                AdjustmentCascadeOptionsRespVO.StudyModeOption opt = new AdjustmentCascadeOptionsRespVO.StudyModeOption();
                opt.setCode(code);
                opt.setName(StudyModeEnum.getName(code));
                modeOptions.add(opt);
            }
        }
        respVO.setStudyModes(modeOptions);

        Integer filterStudyMode = reqVO.getStudyMode();
        List<AdjustmentCascadeOptionsRespVO.DirectionOption> directionOptions = new ArrayList<>();
        for (SchoolDirectionDO d : directions) {
            if (d == null || d.getId() == null) continue;
            if (filterStudyMode != null && !filterStudyMode.equals(d.getStudyMode())) continue;
            AdjustmentCascadeOptionsRespVO.DirectionOption opt = new AdjustmentCascadeOptionsRespVO.DirectionOption();
            opt.setId(d.getId());
            opt.setDirectionCode(d.getDirectionCode());
            opt.setDirectionName(d.getDirectionName());
            opt.setStudyMode(d.getStudyMode());
            directionOptions.add(opt);
        }
        respVO.setDirections(directionOptions);
        return respVO;
    }

    /**
     * stable order: 1(全日制) -> 2(非全日制) -> others
     */
    private static int studyModeRank(Integer mode) {
        if (mode != null && mode == 1) return 0;
        if (mode != null && mode == 2) return 1;
        return 10;
    }
}

