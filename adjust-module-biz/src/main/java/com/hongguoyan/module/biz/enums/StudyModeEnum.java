package com.hongguoyan.module.biz.enums;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Study mode enum.
 *
 * <p>
 * DB / backend internal representation uses numeric codes.
 * </p>
 *
 * <ul>
 *   <li>1 - Full-time (全日制)</li>
 *   <li>2 - Part-time (非全日制)</li>
 * </ul>
 */
public enum StudyModeEnum {

    NONE(0, "不限"),
    FULL_TIME(1, "全日制"),
    PART_TIME(2, "非全日制");

    private final Integer code;
    private final String name;

    StudyModeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private static final Map<Integer, StudyModeEnum> CODE_MAP;
    private static final Map<String, Integer> NAME_TO_CODE_MAP;

    static {
        Map<Integer, StudyModeEnum> codeMap = new HashMap<>();
        Map<String, Integer> nameToCode = new HashMap<>();
        for (StudyModeEnum e : values()) {
            if (e.code != null) {
                codeMap.put(e.code, e);
            }
            if (e.name != null && !e.name.isEmpty()) {
                nameToCode.put(e.name, e.code);
            }
        }
        CODE_MAP = Collections.unmodifiableMap(codeMap);
        NAME_TO_CODE_MAP = Collections.unmodifiableMap(nameToCode);
    }

    public static StudyModeEnum fromCode(Integer code) {
        return code == null ? null : CODE_MAP.get(code);
    }

    public static String getName(Integer code) {
        StudyModeEnum e = fromCode(code);
        return e != null ? e.name : null;
    }

    /**
     * Parse query text to code.
     * Accepts: "1"/"2" or Chinese names.
     */
    public static Integer parseCode(String raw) {
        return parseCode(raw, false);
    }

    /**
     * Parse query text to code.
     * Accepts: "1"/"2" (and optionally "0") or Chinese names.
     */
    public static Integer parseCode(String raw, boolean allowZero) {
        if (raw == null) {
            return null;
        }
        String s = raw.trim();
        if (s.isEmpty()) {
            return null;
        }
        // numeric
        if (allowZero && "0".equals(s)) {
            return 0;
        }
        if ("1".equals(s)) {
            return 1;
        }
        if ("2".equals(s)) {
            return 2;
        }
        // Chinese
        Integer code = NAME_TO_CODE_MAP.get(s);
        if (code != null) {
            if (!allowZero && code == 0) {
                return null;
            }
            return code;
        }
        // try parse integer string
        try {
            int n = Integer.parseInt(s);
            if (n == 1 || n == 2) {
                return n;
            }
            if (allowZero && n == 0) {
                return 0;
            }
            return null;
        } catch (NumberFormatException ignore) {
            return null;
        }
    }
}

