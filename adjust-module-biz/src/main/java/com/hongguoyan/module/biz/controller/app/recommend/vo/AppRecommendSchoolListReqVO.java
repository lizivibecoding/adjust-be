package com.hongguoyan.module.biz.controller.app.recommend.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "智能推荐-请求")
@Data
public class AppRecommendSchoolListReqVO {

    @Schema(description = "英语科目", example = "英语一")
    private String englishSubject;

    @Schema(description = "数学科目", example = "数学一")
    private String mathSubject;

    @Schema(description = "专业课", example = "408")
    private String professionalSubject;

}
