package com.hongguoyan.module.biz.controller.admin.crawl;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.module.biz.controller.admin.crawl.vo.result.*;
import com.hongguoyan.module.biz.dal.dataobject.crawl.CrawlAdjustmentResultDO;
import com.hongguoyan.module.biz.dal.dataobject.crawl.CrawlAdmissionResultDO;
import com.hongguoyan.module.biz.dal.dataobject.crawl.CrawlRetestResultDO;
import com.hongguoyan.module.biz.service.crawl.CrawlResultService;
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

@Tag(name = "管理后台 - 爬虫结果")
@RestController
@RequestMapping("/biz/crawl-result")
@Validated
public class CrawlResultController {

    @Resource
    private CrawlResultService crawlResultService;

    @GetMapping("/retest/page")
    @Operation(summary = "获得爬虫复试名单分页")
    @PreAuthorize("@ss.hasPermission('biz:crawl-result:query')")
    public CommonResult<PageResult<CrawlRetestResultRespVO>> getCrawlRetestResultPage(@Valid CrawlRetestResultPageReqVO pageReqVO) {
        PageResult<CrawlRetestResultDO> pageResult = crawlResultService.getCrawlRetestResultPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CrawlRetestResultRespVO.class));
    }

    @GetMapping("/admission/page")
    @Operation(summary = "获得爬虫录取名单分页")
    @PreAuthorize("@ss.hasPermission('biz:crawl-result:query')")
    public CommonResult<PageResult<CrawlAdmissionResultRespVO>> getCrawlAdmissionResultPage(@Valid CrawlAdmissionResultPageReqVO pageReqVO) {
        PageResult<CrawlAdmissionResultDO> pageResult = crawlResultService.getCrawlAdmissionResultPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CrawlAdmissionResultRespVO.class));
    }

    @GetMapping("/adjustment/page")
    @Operation(summary = "获得爬虫调剂专业分页")
    @PreAuthorize("@ss.hasPermission('biz:crawl-result:query')")
    public CommonResult<PageResult<CrawlAdjustmentResultRespVO>> getCrawlAdjustmentResultPage(@Valid CrawlAdjustmentResultPageReqVO pageReqVO) {
        PageResult<CrawlAdjustmentResultDO> pageResult = crawlResultService.getCrawlAdjustmentResultPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CrawlAdjustmentResultRespVO.class));
    }

}
