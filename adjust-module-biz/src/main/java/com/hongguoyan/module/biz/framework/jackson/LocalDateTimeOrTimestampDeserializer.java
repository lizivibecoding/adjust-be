package com.hongguoyan.module.biz.framework.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * LocalDateTime 反序列化器：兼容时间戳（毫秒）或字符串（yyyy-MM-dd HH:mm:ss / ISO）
 */
public class LocalDateTimeOrTimestampDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken token = p.currentToken();
        if (token == JsonToken.VALUE_NUMBER_INT) {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(p.getLongValue()), ZoneId.systemDefault());
        }
        if (token == JsonToken.VALUE_STRING) {
            String text = p.getText();
            if (text == null) {
                return null;
            }
            String value = text.trim();
            if (value.isEmpty()) {
                return null;
            }
            // ISO_LOCAL_DATE_TIME: 2026-02-16T00:00:00
            if (value.contains("T")) {
                return LocalDateTime.parse(value);
            }
            return LocalDateTime.parse(value, FORMATTER);
        }
        // fallback：尽量按 long 处理（兼容 p.getValueAsLong）
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(p.getValueAsLong()), ZoneId.systemDefault());
    }
}

