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
 * Accepts JSON array of numbers: [1,2,3] OR comma-separated string: "1,2,3".
 */
public class LongListOrCommaSeparatedDeserializer extends JsonDeserializer<List<Long>> {

    @Override
    public List<Long> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken token = p.currentToken();
        if (token == null) {
            token = p.nextToken();
        }
        if (token == JsonToken.VALUE_NULL) {
            return null;
        }
        if (token == JsonToken.START_ARRAY) {
            List<Long> result = new ArrayList<>();
            while (p.nextToken() != JsonToken.END_ARRAY) {
                if (p.currentToken() == JsonToken.VALUE_NULL) {
                    continue;
                }
                if (!p.currentToken().isNumeric()) {
                    throw JsonMappingException.from(p, "competitionIds array must contain numbers");
                }
                result.add(p.getLongValue());
            }
            return result;
        }
        if (token == JsonToken.VALUE_STRING) {
            String raw = p.getValueAsString();
            if (raw == null || raw.trim().isEmpty()) {
                return new ArrayList<>();
            }
            String[] parts = raw.split(",");
            List<Long> result = new ArrayList<>(parts.length);
            for (String part : parts) {
                if (part == null) {
                    continue;
                }
                String s = part.trim();
                if (s.isEmpty()) {
                    continue;
                }
                try {
                    result.add(Long.parseLong(s));
                } catch (NumberFormatException e) {
                    throw JsonMappingException.from(p, "competitionIds contains non-numeric value: " + s, e);
                }
            }
            return result;
        }
        if (token.isNumeric()) {
            List<Long> result = new ArrayList<>(1);
            result.add(p.getLongValue());
            return result;
        }
        throw JsonMappingException.from(p, "competitionIds must be an array, a comma-separated string, or a number");
    }
}

