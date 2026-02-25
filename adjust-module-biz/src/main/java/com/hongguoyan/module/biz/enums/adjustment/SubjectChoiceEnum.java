package com.hongguoyan.module.biz.enums.adjustment;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 可调剂公共课组合选项（前端提交 A/B/C/D）。
 *
 * <p>name 为组合 code：数学(301/302) + 英语(201/204)，格式如 "302_204"。</p>
 */
@Getter
@AllArgsConstructor
public enum SubjectChoiceEnum {

    A("A", "302_204", "数学二(302)+英语二(204)"),
    B("B", "302_201", "数学二(302)+英语一(201)"),
    C("C", "301_204", "数学一(301)+英语二(204)"),
    D("D", "301_201", "数学一(301)+英语一(201)");

    /**
     * 前端/DB 选项值（A/B/C/D）。
     */
    private final String option;

    /**
     * 组合 code（入库可用；若库里只存 A/B/C/D，也可用于展示/校验映射）。
     */
    private final String name;

    /**
     * 中文描述（用于日志/调试；前端若写死可不使用）。
     */
    private final String desc;

    private static final Map<String, SubjectChoiceEnum> OPTION_MAP;

    static {
        Map<String, SubjectChoiceEnum> m = new HashMap<>();
        for (SubjectChoiceEnum e : values()) {
            m.put(e.option, e);
        }
        OPTION_MAP = Collections.unmodifiableMap(m);
    }

    public static SubjectChoiceEnum fromOption(String option) {
        if (option == null) {
            return null;
        }
        return OPTION_MAP.get(option.trim().toUpperCase());
    }

    public static boolean isValidOption(String option) {
        return fromOption(option) != null;
    }

    public static String toName(String option) {
        SubjectChoiceEnum e = fromOption(option);
        if (e == null) {
            throw new IllegalArgumentException("非法公共课组合选项: " + option);
        }
        return e.name;
    }

    public static String toDesc(String option) {
        SubjectChoiceEnum e = fromOption(option);
        if (e == null) {
            throw new IllegalArgumentException("非法公共课组合选项: " + option);
        }
        return e.desc;
    }

    public static String[] allOptions() {
        return Arrays.stream(values()).map(SubjectChoiceEnum::getOption).toArray(String[]::new);
    }
}

