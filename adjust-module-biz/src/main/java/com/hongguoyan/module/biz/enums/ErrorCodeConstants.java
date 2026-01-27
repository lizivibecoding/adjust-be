package com.hongguoyan.module.biz.enums;

import com.hongguoyan.framework.common.exception.ErrorCode;

public interface ErrorCodeConstants {
    ErrorCode ADJUSTMENT_NOT_EXISTS = new ErrorCode(1, "调剂不存在");
    ErrorCode RECRUIT_NOT_EXISTS = new ErrorCode(2, "招生不存在");
    ErrorCode SCHOOL_NOT_EXISTS = new ErrorCode(3, "学校不存在");
    ErrorCode MAJOR_NOT_EXISTS = new ErrorCode(4, "专业不存在");}
