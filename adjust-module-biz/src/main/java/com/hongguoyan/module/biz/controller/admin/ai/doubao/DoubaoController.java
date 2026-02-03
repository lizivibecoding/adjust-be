package com.hongguoyan.module.biz.controller.admin.ai.doubao;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.module.biz.controller.admin.ai.doubao.vo.DoubaoResponsesReqVO;
import com.hongguoyan.module.biz.controller.admin.ai.doubao.vo.DoubaoResponsesRespVO;
import com.hongguoyan.module.biz.service.ai.doubao.DoubaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - AI - 豆包 Responses")
@RestController
@RequestMapping("/ai/doubao")
@Validated
public class DoubaoController {

    @Resource
    private DoubaoService doubaoResponsesService;

    @PostMapping("/responses")
    @Operation(summary = "创建模型响应（非流式）")
    @PermitAll
    public CommonResult<DoubaoResponsesRespVO> create(@Valid @RequestBody DoubaoResponsesReqVO reqVO) {
        return success(doubaoResponsesService.generate(reqVO));
    }
}

