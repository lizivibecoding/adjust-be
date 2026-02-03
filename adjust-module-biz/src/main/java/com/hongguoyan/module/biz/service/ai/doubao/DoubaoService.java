package com.hongguoyan.module.biz.service.ai.doubao;

import com.hongguoyan.module.biz.controller.admin.ai.doubao.vo.DoubaoResponsesReqVO;
import com.hongguoyan.module.biz.controller.admin.ai.doubao.vo.DoubaoResponsesRespVO;

/**
 * Doubao Responses service.
 */
public interface DoubaoService {

    /**
     * Generate model response (non-streaming).
     */
    DoubaoResponsesRespVO generate(DoubaoResponsesReqVO reqVO);
}

