package com.hongguoyan.module.biz.controller.admin.schoolmajor;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.module.biz.controller.admin.schoolmajor.vo.SchoolMajorListRespVO;
import com.hongguoyan.module.biz.dal.dataobject.schoolmajor.SchoolMajorDO;
import com.hongguoyan.module.biz.service.schoolmajor.SchoolMajorService;
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

@Tag(name = "管理后台 - 院校专业")
@RestController("adminSchoolMajorController")
@RequestMapping("/biz/school-major")
@Validated
public class SchoolMajorController {

    @Resource
    private SchoolMajorService schoolMajorService;

    @GetMapping("/list")
    @Operation(summary = "获得院校专业列表") // 用于学院列表（树表格-展开学院节点）
    @Parameter(name = "schoolId", description = "学校ID", required = true, example = "5")
    @Parameter(name = "collegeId", description = "学院ID", required = true, example = "95")
    @Parameter(name = "year", description = "年份（默认有效年）", example = "2026")
    @PreAuthorize("@ss.hasPermission('biz:school:query')")
    public CommonResult<List<SchoolMajorListRespVO>> getSchoolMajorList(
            @RequestParam("schoolId") @NotNull Long schoolId,
            @RequestParam("collegeId") @NotNull Long collegeId,
            @RequestParam(value = "year", required = false) Integer year) {
        List<SchoolMajorDO> list = schoolMajorService.getSchoolMajorList(schoolId, collegeId, year);
        return success(BeanUtils.toBean(list, SchoolMajorListRespVO.class));
    }
}

