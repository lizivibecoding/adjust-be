package com.hongguoyan.module.biz.controller.admin.publisher;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.framework.security.core.util.SecurityFrameworkUtils;
import com.hongguoyan.module.biz.controller.admin.publisher.vo.PublisherAuditReqVO;
import com.hongguoyan.module.biz.controller.admin.publisher.vo.PublisherPageReqVO;
import com.hongguoyan.module.biz.controller.admin.publisher.vo.PublisherRespVO;
import com.hongguoyan.module.biz.dal.dataobject.publisher.PublisherDO;
import com.hongguoyan.module.biz.service.publisher.PublisherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 发布者资质审核")
@RestController
@RequestMapping("/biz/publisher")
@Validated
public class PublisherController {

    @Resource
    private PublisherService publisherService;

    @GetMapping("/get")
    @Operation(summary = "获得发布者资质详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('biz:publisher:query')")
    public CommonResult<PublisherRespVO> getPublisher(@RequestParam("id") Long id) {
        PublisherDO publisher = publisherService.getPublisher(id);
        return success(BeanUtils.toBean(publisher, PublisherRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得发布者资质分页")
    @PreAuthorize("@ss.hasPermission('biz:publisher:query')")
    public CommonResult<PageResult<PublisherRespVO>> getPublisherPage(@Valid PublisherPageReqVO pageReqVO) {
        PageResult<PublisherDO> pageResult = publisherService.getPublisherPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, PublisherRespVO.class));
    }

    @PutMapping("/approve")
    @Operation(summary = "审核通过发布者资质")
    @PreAuthorize("@ss.hasPermission('biz:publisher:approve')")
    public CommonResult<Boolean> approvePublisher(@Valid @RequestBody PublisherAuditReqVO reqVO) {
        publisherService.approvePublisher(reqVO.getId(), SecurityFrameworkUtils.getLoginUserId(), reqVO.getReason());
        return success(true);
    }

    @PutMapping("/reject")
    @Operation(summary = "审核拒绝发布者资质")
    @PreAuthorize("@ss.hasPermission('biz:publisher:reject')")
    public CommonResult<Boolean> rejectPublisher(@Valid @RequestBody PublisherAuditReqVO reqVO) {
        publisherService.rejectPublisher(reqVO.getId(), SecurityFrameworkUtils.getLoginUserId(), reqVO.getReason());
        return success(true);
    }

    @PutMapping("/disable")
    @Operation(summary = "禁用发布者资质")
    @PreAuthorize("@ss.hasPermission('biz:publisher:disable')")
    public CommonResult<Boolean> disablePublisher(@Valid @RequestBody PublisherAuditReqVO reqVO) {
        publisherService.disablePublisher(reqVO.getId(), SecurityFrameworkUtils.getLoginUserId(), reqVO.getReason());
        return success(true);
    }

    @PutMapping("/enable")
    @Operation(summary = "启用发布者资质")
    @PreAuthorize("@ss.hasPermission('biz:publisher:enable')")
    public CommonResult<Boolean> enablePublisher(@Valid @RequestBody PublisherAuditReqVO reqVO) {
        publisherService.enablePublisher(reqVO.getId(), SecurityFrameworkUtils.getLoginUserId(), reqVO.getReason());
        return success(true);
    }
}
