package com.hongguoyan.module.biz.controller.admin.schoolcollege;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.module.biz.controller.admin.schoolcollege.vo.SchoolCollegeListRespVO;
import com.hongguoyan.module.biz.dal.dataobject.schoolcollege.SchoolCollegeDO;
import com.hongguoyan.module.biz.service.schoolcollege.SchoolCollegeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 学院")
@RestController
@RequestMapping("/biz/school-college")
@Validated
public class SchoolCollegeController {

    @Resource
    private SchoolCollegeService schoolCollegeService;

    @GetMapping("/list")
    @Operation(summary = "获得学院列表") // 用于学院列表（树表格-展开学校节点）
    @Parameter(name = "schoolId", description = "学校ID", required = true, example = "5")
    @Parameter(name = "year", description = "年份（默认有效年）", example = "2026")
    @PreAuthorize("@ss.hasPermission('biz:school:query')")
    public CommonResult<List<SchoolCollegeListRespVO>> getSchoolCollegeList(
            @RequestParam("schoolId") @NotNull Long schoolId,
            @RequestParam(value = "year", required = false) Integer year) {
        List<SchoolCollegeDO> list = schoolCollegeService.getSchoolCollegeList(schoolId, year);
        return success(BeanUtils.toBean(list, SchoolCollegeListRespVO.class));
    }
}

