package com.hongguoyan.module.biz.service.ai.doubao;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongguoyan.module.biz.controller.admin.ai.doubao.vo.DoubaoResponsesReqVO;
import com.hongguoyan.module.biz.controller.admin.ai.doubao.vo.DoubaoResponsesRespVO;
import com.hongguoyan.module.biz.service.ai.AiTextService;
import com.hongguoyan.module.biz.service.ai.dto.AiTextRequest;
import com.hongguoyan.module.biz.service.ai.dto.AiTextResult;
import com.hongguoyan.module.biz.service.ai.doubao.config.DoubaoProperties;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

@Service
@Validated
@Slf4j
public class DoubaoServiceImpl implements DoubaoService, AiTextService {

    private static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(10);

    @Resource
    private DoubaoProperties properties;
    @Resource
    private ObjectMapper objectMapper;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(DEFAULT_CONNECT_TIMEOUT)
            .version(HttpClient.Version.HTTP_1_1)
            .build();

    @Override
    public DoubaoResponsesRespVO generate(DoubaoResponsesReqVO reqVO) {
        String apiKey = properties.getApiKey();
        if (StrUtil.isBlank(apiKey)) {
            throw exception(DOUBAO_CONFIG_MISSING);
        }

        String model = StrUtil.blankToDefault(reqVO.getModel(), properties.getDefaultModel());
        if (StrUtil.isBlank(model)) {
            throw exception(DOUBAO_CONFIG_MISSING);
        }

        Long timeoutMs = reqVO.getTimeoutMs() != null ? reqVO.getTimeoutMs() : properties.getDefaultTimeoutMs();
        if (timeoutMs == null || timeoutMs < 1000) {
            timeoutMs = 60_000L;
        }

        String url = normalizeBaseUrl(properties.getBaseUrl()) + "/v3/responses";
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model", model);
        body.put("input", buildInput(reqVO));

        String requestJson;
        try {
            requestJson = objectMapper.writeValueAsString(body);
        } catch (Exception e) {
            log.error("Doubao request serialize failed", e);
            throw exception(DOUBAO_REQUEST_FAILED);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMillis(timeoutMs))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestJson, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (HttpTimeoutException e) {
            log.warn("Doubao request timeout, timeoutMs={}, model={}", timeoutMs, model);
            throw exception(DOUBAO_TIMEOUT);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Doubao request interrupted, model={}", model);
            throw exception(DOUBAO_REQUEST_FAILED);
        } catch (Exception e) {
            log.error("Doubao request failed, model={}", model, e);
            throw exception(DOUBAO_REQUEST_FAILED);
        }

        int status = response.statusCode();
        if (status < 200 || status >= 300) {
            String respBody = response.body();
            log.warn("Doubao non-2xx response, status={}, body={}", status, truncate(respBody, 2000));
            throw exception(DOUBAO_REQUEST_FAILED);
        }

        JsonNode root;
        try {
            root = objectMapper.readTree(response.body());
        } catch (Exception e) {
            log.warn("Doubao response parse failed, body={}", truncate(response.body(), 2000), e);
            throw exception(DOUBAO_REQUEST_FAILED);
        }

        DoubaoResponsesRespVO respVO = new DoubaoResponsesRespVO();
        respVO.setResponseId(textOrNull(root.get("id")));
        respVO.setOutputText(extractOutputText(root));

        Map<String, Object> raw = new LinkedHashMap<>();
        raw.put("id", respVO.getResponseId());
        raw.put("status", textOrNull(root.get("status")));
        raw.put("model", textOrNull(root.get("model")));
        respVO.setRaw(raw);
        return respVO;
    }

    @Override
    public AiTextResult generateText(AiTextRequest request) {
        if (request == null || StrUtil.isBlank(request.getPrompt())) {
            throw exception(DOUBAO_REQUEST_FAILED);
        }
        DoubaoResponsesReqVO reqVO = new DoubaoResponsesReqVO();
        reqVO.setModel(request.getModel());
        reqVO.setText(request.getPrompt());
        reqVO.setImageUrls(request.getImageUrls());
        reqVO.setFileUrls(request.getFileUrls());
        reqVO.setTimeoutMs(request.getTimeoutMs());

        DoubaoResponsesRespVO respVO = generate(reqVO);
        return AiTextResult.builder()
                .text(respVO.getOutputText())
                .raw(respVO.getRaw())
                .build();
    }

    /**
     * input mapping:
     * - text-only: string
     * - multimodal: array with a single user message (role + content parts)
     */
    private Object buildInput(DoubaoResponsesReqVO reqVO) {
        String text = buildTextWithFileUrls(reqVO.getText(), reqVO.getFileUrls());
        if (CollUtil.isEmpty(reqVO.getImageUrls())) {
            return text;
        }

        List<Map<String, Object>> content = new ArrayList<>();
        for (String imageUrl : reqVO.getImageUrls()) {
            if (StrUtil.isBlank(imageUrl)) {
                continue;
            }
            Map<String, Object> imagePart = new LinkedHashMap<>();
            imagePart.put("type", "input_image");
            imagePart.put("image_url", imageUrl);
            content.add(imagePart);
        }

        Map<String, Object> textPart = new LinkedHashMap<>();
        textPart.put("type", "input_text");
        textPart.put("text", text);
        content.add(textPart);

        Map<String, Object> msg = new LinkedHashMap<>();
        msg.put("role", "user");
        msg.put("content", content);
        return List.of(msg);
    }

    private String buildTextWithFileUrls(String text, List<String> fileUrls) {
        if (CollUtil.isEmpty(fileUrls)) {
            return text;
        }
        StringBuilder sb = new StringBuilder(StrUtil.nullToEmpty(text));
        sb.append("\n\n附件链接：");
        for (String url : fileUrls) {
            if (StrUtil.isBlank(url)) {
                continue;
            }
            sb.append("\n- ").append(url);
        }
        return sb.toString();
    }

    private String normalizeBaseUrl(String baseUrl) {
        String v = StrUtil.blankToDefault(baseUrl, "https://ark.cn-beijing.volces.com/api");
        // remove trailing slashes
        while (v.endsWith("/")) {
            v = v.substring(0, v.length() - 1);
        }
        return v;
    }

    /**
     * Best-effort extraction across possible response formats.
     */
    private String extractOutputText(JsonNode root) {
        if (root == null || root.isNull()) {
            return null;
        }

        // 1) OpenAI Responses compatible convenience field
        String outputText = textOrNull(root.get("output_text"));
        if (StrUtil.isNotBlank(outputText)) {
            return outputText;
        }

        // 2) responses output[].content[].text
        JsonNode output = root.get("output");
        if (output != null && output.isArray()) {
            StringBuilder sb = new StringBuilder();
            for (JsonNode item : output) {
                JsonNode content = item.get("content");
                if (content != null && content.isArray()) {
                    for (JsonNode part : content) {
                        String t = textOrNull(part.get("text"));
                        if (StrUtil.isNotBlank(t)) {
                            if (sb.length() > 0) {
                                sb.append('\n');
                            }
                            sb.append(t);
                        }
                    }
                }
            }
            if (sb.length() > 0) {
                return sb.toString();
            }
        }

        // 3) chat.completions compatible: choices[0].message.content
        JsonNode choices = root.get("choices");
        if (choices != null && choices.isArray() && choices.size() > 0) {
            JsonNode first = choices.get(0);
            String content = textOrNull(first.at("/message/content"));
            if (StrUtil.isNotBlank(content)) {
                return content;
            }
        }

        // 4) fallback: try common nodes
        String text = textOrNull(root.at("/data/output_text"));
        if (StrUtil.isNotBlank(text)) {
            return text;
        }
        return null;
    }

    private String textOrNull(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (node.isTextual()) {
            return node.asText();
        }
        return node.toString();
    }

    private String truncate(String s, int max) {
        if (s == null) {
            return null;
        }
        if (s.length() <= max) {
            return s;
        }
        return s.substring(0, max) + "...(truncated)";
    }
}

