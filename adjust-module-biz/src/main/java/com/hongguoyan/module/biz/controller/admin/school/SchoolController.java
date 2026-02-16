package com.hongguoyan.module.biz.controller.admin.school;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.controller.admin.school.vo.SchoolPageReqVO;
import com.hongguoyan.module.biz.controller.admin.school.vo.SchoolDetailRespVO;
import com.hongguoyan.module.biz.controller.admin.school.vo.SchoolRespVO;
import com.hongguoyan.module.biz.controller.admin.school.vo.SchoolUpdateReqVO;
import com.hongguoyan.module.biz.service.school.SchoolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 院校")
@RestController
@RequestMapping("/biz/school")
@Validated
public class SchoolController {

    @Resource
    private SchoolService schoolService;

    @GetMapping("/page")
    @Operation(summary = "获得院校分页") // 用于学院列表（树表格根节点）
    @PreAuthorize("@ss.hasPermission('biz:school:query')")
    public CommonResult<PageResult<SchoolRespVO>> getSchoolPage(@Valid SchoolPageReqVO pageReqVO) {
        return success(schoolService.getSchoolAdminPage(pageReqVO));
    }

    @GetMapping("/get")
    @Operation(summary = "获得院校详情") // 用于编辑回显
    @Parameter(name = "id", description = "学校ID", required = true, example = "5")
    @PreAuthorize("@ss.hasPermission('biz:school:query')")
    public CommonResult<SchoolDetailRespVO> getSchool(@RequestParam("id") Long id) {
        return success(schoolService.getSchoolAdmin(id));
    }

    @PutMapping("/update")
    @Operation(summary = "更新院校") // 用于编辑保存
    @PreAuthorize("@ss.hasPermission('biz:school:update')")
    public CommonResult<Boolean> updateSchool(@Valid @RequestBody SchoolUpdateReqVO updateReqVO) {
        schoolService.updateSchoolAdmin(updateReqVO);
        return success(true);
    }
}

