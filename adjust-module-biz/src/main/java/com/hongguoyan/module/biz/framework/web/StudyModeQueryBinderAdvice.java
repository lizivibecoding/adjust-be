package com.hongguoyan.module.biz.framework.web;

import com.hongguoyan.module.biz.enums.StudyModeEnum;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;

/**
 * Query/Form parameter binder for {@code studyMode}.
 *
 * <p>
 * Only affects properties named {@code studyMode} with target type {@link Integer}.
 * Accepts: {@code 1}/{@code 2} or Chinese names ({@code 全日制}/{@code 非全日制}).
 * </p>
 */
@ControllerAdvice
public class StudyModeQueryBinderAdvice {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Integer.class, "studyMode", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                // Only allow "0" (不限) for user intention query models; other APIs only accept 1/2.
                Object target = binder.getTarget();
                boolean allowZero = target != null
                        && target.getClass().getName().contains(".userintention.")
                        && target.getClass().getSimpleName().contains("UserIntention");
                Integer code = StudyModeEnum.parseCode(text, allowZero);
                // If not provided, keep null. If provided but invalid, fail binding.
                if (text != null && !text.trim().isEmpty() && code == null) {
                    throw new IllegalArgumentException("studyMode must be 1/2 or 全日制/非全日制");
                }
                setValue(code);
            }
        });
    }
}

