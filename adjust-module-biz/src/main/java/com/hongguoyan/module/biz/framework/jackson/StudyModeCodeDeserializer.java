package com.hongguoyan.module.biz.framework.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.hongguoyan.module.biz.enums.StudyModeEnum;
import java.io.IOException;

/**
 * Deserialize studyMode from number / numeric string / Chinese name.
 *
 * <p>Used to make cached JSON (studyMode as "全日制/非全日制") readable back into Integer.</p>
 */
public class StudyModeCodeDeserializer extends JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken t = p.currentToken();
        if (t == JsonToken.VALUE_NUMBER_INT) {
            int v = p.getIntValue();
            return (v == 1 || v == 2 || v == 0) ? v : null;
        }
        if (t == JsonToken.VALUE_STRING) {
            String raw = p.getText();
            return StudyModeEnum.parseCode(raw, true);
        }
        if (t == JsonToken.VALUE_NULL) {
            return null;
        }
        // best-effort: coerce other types to string
        String raw = p.getValueAsString(null);
        return raw != null ? StudyModeEnum.parseCode(raw, true) : null;
    }
}

