package com.hongguoyan.module.biz.framework.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hongguoyan.module.biz.enums.StudyModeEnum;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Serialize {@code Map<Long, List<Integer>>} studyMode codes as Chinese names list.
 */
public class StudyModeListMapNameSerializer extends JsonSerializer<Map<Long, List<Integer>>> {

    @Override
    public void serialize(Map<Long, List<Integer>> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        gen.writeStartObject();
        for (Map.Entry<Long, List<Integer>> entry : value.entrySet()) {
            String key = entry.getKey() != null ? String.valueOf(entry.getKey()) : "null";
            gen.writeFieldName(key);
            List<Integer> list = entry.getValue();
            if (list == null) {
                gen.writeNull();
                continue;
            }
            gen.writeStartArray();
            for (Integer code : list) {
                String name = StudyModeEnum.getName(code);
                if (name == null) {
                    gen.writeNull();
                } else {
                    gen.writeString(name);
                }
            }
            gen.writeEndArray();
        }
        gen.writeEndObject();
    }
}

