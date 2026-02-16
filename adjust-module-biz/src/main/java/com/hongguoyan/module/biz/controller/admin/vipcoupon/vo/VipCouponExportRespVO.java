package com.hongguoyan.module.biz.controller.admin.vipcoupon.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.annotation.write.style.ColumnWidth;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 会员券码导出 Response VO")
@Data
@ExcelIgnoreUnannotated
public class VipCouponExportRespVO {

    @Schema(description = "券码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("券码")
    @ColumnWidth(26)
    private String code;

    @Schema(description = "批次号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("批次号")
    @ColumnWidth(22)
    private String batchNo;

    @Schema(description = "套餐编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("类型")
    @ColumnWidth(10)
    private String planCode;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("开始时间")
    @ColumnWidth(20)
    private String validStartTime;

    @Schema(description = "截止时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("截止时间")
    @ColumnWidth(20)
    private String validEndTime;

}

