package com.hongguoyan.module.biz.controller.admin.crawl;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.module.biz.controller.admin.crawl.vo.task.CrawlTaskPageReqVO;
import com.hongguoyan.module.biz.controller.admin.crawl.vo.task.CrawlTaskRespVO;
import com.hongguoyan.module.biz.dal.dataobject.crawl.CrawlAttachmentDO;
import com.hongguoyan.module.biz.service.crawl.CrawlTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 爬虫任务")
@RestController
@RequestMapping("/biz/crawl-task")
@Validated
public class CrawlTaskController {

    @Resource
    private CrawlTaskService crawlTaskService;

    @GetMapping("/page")
    @Operation(summary = "获得爬虫任务分页")
    @PreAuthorize("@ss.hasPermission('biz:crawl-task:query')")
    public CommonResult<PageResult<CrawlTaskRespVO>> getCrawlTaskPage(@Valid CrawlTaskPageReqVO pageReqVO) {
        PageResult<CrawlAttachmentDO> pageResult = crawlTaskService.getCrawlTaskPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CrawlTaskRespVO.class));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除爬虫任务")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:crawl-task:delete')")
    public CommonResult<Boolean> deleteCrawlTask(@RequestParam("id") Long id) {
        crawlTaskService.deleteCrawlTask(id);
        return success(true);
    }

}
