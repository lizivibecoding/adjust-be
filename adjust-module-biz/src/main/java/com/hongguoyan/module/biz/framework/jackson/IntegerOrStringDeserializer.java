package com.hongguoyan.module.biz.framework.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;

/**
 * Accepts JSON number (1) OR numeric string ("1").
 */
public class IntegerOrStringDeserializer extends JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken token = p.currentToken();
        if (token == null) {
            token = p.nextToken();
        }
        if (token == JsonToken.VALUE_NULL) {
            return null;
        }
        if (token.isNumeric()) {
            return p.getIntValue();
        }
        if (token == JsonToken.VALUE_STRING) {
            String raw = p.getValueAsString();
            if (raw == null || raw.trim().isEmpty()) {
                return null;
            }
            String s = raw.trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                throw JsonMappingException.from(p, "value must be a number or numeric string", e);
            }
        }
        throw JsonMappingException.from(p, "value must be a number or numeric string");
    }
}

