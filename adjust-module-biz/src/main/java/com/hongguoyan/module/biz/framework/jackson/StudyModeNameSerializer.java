package com.hongguoyan.module.biz.framework.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hongguoyan.module.biz.enums.StudyModeEnum;

import java.io.IOException;

/**
 * Serialize studyMode code (1/2) as Chinese name.
 * <p>
 * Used to keep backward compatibility for old response contracts that expect
 * {@code studyMode} to be a Chinese string (全日制/非全日制).
 * </p>
 */
public class StudyModeNameSerializer extends JsonSerializer<Integer> {

    @Override
    public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        String name = StudyModeEnum.getName(value);
        if (name == null) {
            gen.writeNull();
            return;
        }
        gen.writeString(name);
    }
}

