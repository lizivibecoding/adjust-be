package com.hongguoyan.module.biz.controller.admin.crawl;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.module.biz.controller.admin.crawl.vo.config.CrawlSourceConfigPageReqVO;
import com.hongguoyan.module.biz.controller.admin.crawl.vo.config.CrawlSourceConfigRespVO;
import com.hongguoyan.module.biz.controller.admin.crawl.vo.config.CrawlSourceConfigSaveReqVO;
import com.hongguoyan.module.biz.dal.dataobject.crawl.CrawlSourceConfigDO;
import com.hongguoyan.module.biz.service.crawl.CrawlSourceConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 爬虫配置")
@RestController
@RequestMapping("/biz/crawl-source-config")
@Validated
public class CrawlSourceConfigController {

    @Resource
    private CrawlSourceConfigService crawlSourceConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建爬虫配置")
    @PreAuthorize("@ss.hasPermission('biz:crawl-source-config:create')")
    public CommonResult<Long> createCrawlSourceConfig(@Valid @RequestBody CrawlSourceConfigSaveReqVO createReqVO) {
        return success(crawlSourceConfigService.createCrawlSourceConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新爬虫配置")
    @PreAuthorize("@ss.hasPermission('biz:crawl-source-config:update')")
    public CommonResult<Boolean> updateCrawlSourceConfig(@Valid @RequestBody CrawlSourceConfigSaveReqVO updateReqVO) {
        crawlSourceConfigService.updateCrawlSourceConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除爬虫配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:crawl-source-config:delete')")
    public CommonResult<Boolean> deleteCrawlSourceConfig(@RequestParam("id") Long id) {
        crawlSourceConfigService.deleteCrawlSourceConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得爬虫配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:crawl-source-config:query')")
    public CommonResult<CrawlSourceConfigRespVO> getCrawlSourceConfig(@RequestParam("id") Long id) {
        CrawlSourceConfigDO crawlSourceConfig = crawlSourceConfigService.getCrawlSourceConfig(id);
        return success(BeanUtils.toBean(crawlSourceConfig, CrawlSourceConfigRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得爬虫配置分页")
    @PreAuthorize("@ss.hasPermission('biz:crawl-source-config:query')")
    public CommonResult<PageResult<CrawlSourceConfigRespVO>> getCrawlSourceConfigPage(@Valid CrawlSourceConfigPageReqVO pageReqVO) {
        PageResult<CrawlSourceConfigDO> pageResult = crawlSourceConfigService.getCrawlSourceConfigPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CrawlSourceConfigRespVO.class));
    }

}
