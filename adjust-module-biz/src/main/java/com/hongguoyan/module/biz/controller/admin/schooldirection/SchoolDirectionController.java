package com.hongguoyan.module.biz.controller.admin.schooldirection;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.module.biz.controller.admin.schooldirection.vo.SchoolDirectionListRespVO;
import com.hongguoyan.module.biz.dal.dataobject.schooldirection.SchoolDirectionDO;
import com.hongguoyan.module.biz.service.schooldirection.SchoolDirectionService;
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

@Tag(name = "管理后台 - 院校研究方向")
@RestController
@RequestMapping("/biz/school-direction")
@Validated
public class SchoolDirectionController {

    @Resource
    private SchoolDirectionService schoolDirectionService;

    @GetMapping("/list")
    @Operation(summary = "获得院校研究方向列表") // 用于学院列表（树表格-展开专业节点）
    @Parameter(name = "schoolId", description = "学校ID", required = true, example = "5")
    @Parameter(name = "collegeId", description = "学院ID", required = true, example = "95")
    @Parameter(name = "majorId", description = "专业ID(biz_major.id)", required = true, example = "2810")
    @Parameter(name = "year", description = "年份（默认有效年）", example = "2026")
    @PreAuthorize("@ss.hasPermission('biz:school:query')")
    public CommonResult<List<SchoolDirectionListRespVO>> getSchoolDirectionList(
            @RequestParam("schoolId") @NotNull Long schoolId,
            @RequestParam("collegeId") @NotNull Long collegeId,
            @RequestParam("majorId") @NotNull Long majorId,
            @RequestParam(value = "year", required = false) Integer year) {
        List<SchoolDirectionDO> list = schoolDirectionService.getSchoolDirectionList(schoolId, collegeId, majorId, year);
        return success(BeanUtils.toBean(list, SchoolDirectionListRespVO.class));
    }
}

