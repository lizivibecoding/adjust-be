package com.hongguoyan.module.biz.framework.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Accepts JSON array of strings: ["a","b"] OR comma-separated string: "a,b".
 */
public class StringListOrCommaSeparatedDeserializer extends JsonDeserializer<List<String>> {

    @Override
    public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken token = p.currentToken();
        if (token == null) {
            token = p.nextToken();
        }
        if (token == JsonToken.VALUE_NULL) {
            return null;
        }
        if (token == JsonToken.START_ARRAY) {
            List<String> result = new ArrayList<>();
            while (p.nextToken() != JsonToken.END_ARRAY) {
                if (p.currentToken() == JsonToken.VALUE_NULL) {
                    continue;
                }
                result.add(p.getValueAsString());
            }
            return result;
        }
        if (token == JsonToken.VALUE_STRING) {
            String raw = p.getValueAsString();
            if (raw == null || raw.trim().isEmpty()) {
                return new ArrayList<>();
            }
            String[] parts = raw.split(",");
            List<String> result = new ArrayList<>(parts.length);
            for (String part : parts) {
                if (part == null) {
                    continue;
                }
                String s = part.trim();
                if (!s.isEmpty()) {
                    result.add(s);
                }
            }
            return result;
        }
        if (token.isScalarValue()) {
            List<String> result = new ArrayList<>(1);
            result.add(p.getValueAsString());
            return result;
        }
        throw JsonMappingException.from(p, "value must be an array or a comma-separated string");
    }
}

