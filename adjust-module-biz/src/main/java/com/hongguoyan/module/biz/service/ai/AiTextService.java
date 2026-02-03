package com.hongguoyan.module.biz.service.ai;

import com.hongguoyan.module.biz.service.ai.dto.AiTextRequest;
import com.hongguoyan.module.biz.service.ai.dto.AiTextResult;

/**
 * Common AI text generation service abstraction.
 *
 * <p>Business services should depend on this interface so that the underlying model/provider
 * can be swapped (e.g. Doubao -> other vendors) with minimal changes.
 */
public interface AiTextService {

    /**
     * Generate text by prompt.
     */
    AiTextResult generateText(AiTextRequest request);
}

