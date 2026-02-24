package com.hongguoyan.module.biz.controller.admin.adjustment;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentAdmitPageReqVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentAdmitPageRespVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentCascadeOptionsReqVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentCascadeOptionsRespVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentCreateReqVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentPageReqVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentPageRespVO;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.AppSchoolSpecialOptionsReqVO;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.AppSchoolSpecialOptionsRespVO;
import com.hongguoyan.module.biz.controller.app.school.vo.AppSchoolSimpleOptionRespVO;
import com.hongguoyan.module.biz.service.adjustment.AdjustmentAdminService;
import com.hongguoyan.module.biz.service.adjustment.SchoolSpecialOptionsService;
import com.hongguoyan.module.biz.service.school.SchoolService;
import com.hongguoyan.module.biz.enums.StudyModeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 调剂")
@RestController
@RequestMapping("/biz/adjustment")
@Validated
public class AdjustmentController {

    @Resource
    private AdjustmentAdminService adjustmentAdminService;
    @Resource
    private SchoolService schoolService;
    @Resource
    private SchoolSpecialOptionsService schoolSpecialOptionsService;

    @GetMapping("/page")
    @Operation(summary = "获得调剂分页（管理后台）")
    @PreAuthorize("@ss.hasPermission('biz:adjustment:query')")
    public CommonResult<PageResult<AdjustmentPageRespVO>> getAdjustmentPage(@Valid AdjustmentPageReqVO reqVO) {
        return success(adjustmentAdminService.getAdjustmentPage(reqVO));
    }

    @GetMapping("/year-list")
    @Operation(summary = "获得调剂年份下拉（管理后台）")
    @PreAuthorize("@ss.hasPermission('biz:adjustment:query')")
    public CommonResult<List<Integer>> getAdjustmentYearList() {
        return success(adjustmentAdminService.getYearList());
    }

    @GetMapping("/admit/page")
    @Operation(summary = "获得调剂录取名单分页（管理后台）")
    @PreAuthorize("@ss.hasPermission('biz:adjustment:query')")
    public CommonResult<PageResult<AdjustmentAdmitPageRespVO>> getAdjustmentAdmitPage(@Valid AdjustmentAdmitPageReqVO reqVO) {
        return success(adjustmentAdminService.getAdmitPage(reqVO));
    }

    @GetMapping("/cascade-options")
    @Operation(summary = "获得调剂联动选项（管理后台）") // 学校-学院-专业-学习方式-方向（固定 activeYear）
    @PreAuthorize("@ss.hasPermission('biz:adjustment:query')")
    public CommonResult<AdjustmentCascadeOptionsRespVO> getCascadeOptions(@Valid AdjustmentCascadeOptionsReqVO reqVO) {
        AdjustmentCascadeOptionsRespVO respVO = new AdjustmentCascadeOptionsRespVO();
        if (reqVO == null) {
            return success(respVO);
        }
        Long schoolId = reqVO.getSchoolId();
        if (schoolId == null) {
            respVO.setColleges(Collections.emptyList());
            respVO.setMajors(Collections.emptyList());
            respVO.setStudyModes(Collections.emptyList());
            respVO.setDirections(Collections.emptyList());
            return success(respVO);
        }

        // 1) colleges
        AppSchoolSpecialOptionsReqVO collegeReq = new AppSchoolSpecialOptionsReqVO();
        collegeReq.setType(1);
        collegeReq.setSchoolId(schoolId);
        AppSchoolSpecialOptionsRespVO collegeResp = schoolSpecialOptionsService.getOptions(collegeReq);
        if (collegeResp != null && collegeResp.getOptions() != null) {
            List<AdjustmentCascadeOptionsRespVO.CollegeOption> list = new ArrayList<>(collegeResp.getOptions().size());
            for (AppSchoolSpecialOptionsRespVO.Option o : collegeResp.getOptions()) {
                if (o == null || o.getId() == null) continue;
                AdjustmentCascadeOptionsRespVO.CollegeOption opt = new AdjustmentCascadeOptionsRespVO.CollegeOption();
                opt.setId(o.getId());
                opt.setName(o.getName());
                list.add(opt);
            }
            respVO.setColleges(list);
        } else {
            respVO.setColleges(Collections.emptyList());
        }

        Long collegeId = reqVO.getCollegeId();
        if (collegeId == null) {
            respVO.setMajors(Collections.emptyList());
            respVO.setStudyModes(Collections.emptyList());
            respVO.setDirections(Collections.emptyList());
            return success(respVO);
        }

        // 2) majors
        AppSchoolSpecialOptionsReqVO majorReq = new AppSchoolSpecialOptionsReqVO();
        majorReq.setType(2);
        majorReq.setSchoolId(schoolId);
        majorReq.setCollegeId(collegeId);
        AppSchoolSpecialOptionsRespVO majorResp = schoolSpecialOptionsService.getOptions(majorReq);
        if (majorResp != null && majorResp.getOptions() != null) {
            List<AdjustmentCascadeOptionsRespVO.MajorOption> list = new ArrayList<>(majorResp.getOptions().size());
            for (AppSchoolSpecialOptionsRespVO.Option o : majorResp.getOptions()) {
                if (o == null || o.getId() == null) continue;
                AdjustmentCascadeOptionsRespVO.MajorOption opt = new AdjustmentCascadeOptionsRespVO.MajorOption();
                opt.setMajorId(o.getId());
                opt.setCode(o.getCode());
                opt.setName(o.getName());
                opt.setDegreeType(null);
                list.add(opt);
            }
            respVO.setMajors(list);
        } else {
            respVO.setMajors(Collections.emptyList());
        }

        Long majorId = reqVO.getMajorId();
        if (majorId == null) {
            respVO.setStudyModes(Collections.emptyList());
            respVO.setDirections(Collections.emptyList());
            return success(respVO);
        }

        // 3) directions
        AppSchoolSpecialOptionsReqVO directionReq = new AppSchoolSpecialOptionsReqVO();
        directionReq.setType(3);
        directionReq.setSchoolId(schoolId);
        directionReq.setCollegeId(collegeId);
        directionReq.setMajorId(majorId);
        AppSchoolSpecialOptionsRespVO directionResp = schoolSpecialOptionsService.getOptions(directionReq);
        List<AppSchoolSpecialOptionsRespVO.DirectionOption> directions =
                directionResp != null ? directionResp.getDirections() : null;
        if (directions == null || directions.isEmpty()) {
            respVO.setStudyModes(Collections.emptyList());
            respVO.setDirections(Collections.emptyList());
            return success(respVO);
        }

        // study modes
        Set<Integer> modeSet = new HashSet<>();
        for (AppSchoolSpecialOptionsRespVO.DirectionOption d : directions) {
            if (d != null && d.getStudyMode() != null) {
                modeSet.add(d.getStudyMode());
            }
        }
        List<AdjustmentCascadeOptionsRespVO.StudyModeOption> modeOptions = new ArrayList<>();
        if (!modeSet.isEmpty()) {
            List<Integer> modes = new ArrayList<>(modeSet);
            modes.sort(Comparator.comparingInt(AdjustmentController::studyModeRank));
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
        for (AppSchoolSpecialOptionsRespVO.DirectionOption d : directions) {
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
        return success(respVO);
    }

    @GetMapping("/school/simple-all")
    @Operation(summary = "获得学校简单列表（管理后台）")
    @PreAuthorize("@ss.hasPermission('biz:adjustment:query')")
    public CommonResult<List<AppSchoolSimpleOptionRespVO>> getSchoolSimpleAll() {
        return success(schoolService.getSchoolSimpleAll());
    }

    @PostMapping("/create")
    @Operation(summary = "新增调剂（管理后台）") // 只传方向ID，其余字段按 activeYear 回填
    @PreAuthorize("@ss.hasPermission('biz:adjustment:create')")
    public CommonResult<Long> createAdjustment(@Valid @RequestBody AdjustmentCreateReqVO reqVO) {
        return success(adjustmentAdminService.createAdjustment(reqVO));
    }

    private static int studyModeRank(Integer mode) {
        if (mode != null && mode == 1) return 0;
        if (mode != null && mode == 2) return 1;
        return 10;
    }

}

