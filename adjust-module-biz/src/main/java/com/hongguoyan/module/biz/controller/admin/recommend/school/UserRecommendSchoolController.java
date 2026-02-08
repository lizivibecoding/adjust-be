package com.hongguoyan.module.biz.controller.admin.recommend.school;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.module.biz.controller.admin.recommend.school.vo.UserRecommendSchoolPageReqVO;
import com.hongguoyan.module.biz.controller.admin.recommend.school.vo.UserRecommendSchoolRespVO;
import com.hongguoyan.module.biz.dal.dataobject.recommend.UserRecommendSchoolDO;
import com.hongguoyan.module.biz.service.recommend.UserRecommendSchoolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 推荐院校专业")
@RestController
@RequestMapping("/biz/recommend-school")
@Validated
public class UserRecommendSchoolController {

    @Resource
    private UserRecommendSchoolService userRecommendSchoolService;

    @GetMapping("/page")
    @Operation(summary = "获得推荐院校专业分页")
    @PreAuthorize("@ss.hasPermission('biz:recommend-school:query')")
    public CommonResult<PageResult<UserRecommendSchoolRespVO>> getUserRecommendSchoolPage(@Valid UserRecommendSchoolPageReqVO pageReqVO) {
        PageResult<UserRecommendSchoolDO> pageResult = userRecommendSchoolService.getUserRecommendSchoolPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, UserRecommendSchoolRespVO.class));
    }

}
