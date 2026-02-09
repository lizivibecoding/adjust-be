package com.hongguoyan.module.biz.service.userpreference;

import cn.hutool.core.util.StrUtil;
import com.hongguoyan.module.biz.controller.app.userpreference.vo.AppUserPreferenceExportRespVO;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil;
import com.hongguoyan.module.biz.enums.ErrorCodeConstants;
import java.io.ByteArrayOutputStream;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 用户志愿 PDF 导出 Service 实现类
 */
@Service
public class UserPreferencePdfServiceImpl implements UserPreferencePdfService {

    // Same as RecommendPdfServiceImpl
    private static final String FONT_NAME = "STSong-Light";
    private static final String FONT_ENCODING = "UniGB-UCS2-H";

    @Override
    public byte[] generatePdf(List<AppUserPreferenceExportRespVO> rows) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);

            PdfFont font = PdfFontFactory.createFont(FONT_NAME, FONT_ENCODING);
            document.setFont(font);

            Paragraph title = new Paragraph("志愿表")
                    .setFontSize(16)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(16);
            document.add(title);

            // Columns: 志愿, 学校, 学院, 专业代码, 专业名称, 方向, 学习方式
            Table table = new Table(UnitValue.createPercentArray(new float[]{1.2f, 2.2f, 2.4f, 1.6f, 2.2f, 2.2f, 1.4f}))
                    .useAllAvailableWidth();

            addHeaderCell(table, "志愿");
            addHeaderCell(table, "学校");
            addHeaderCell(table, "学院");
            addHeaderCell(table, "专业代码");
            addHeaderCell(table, "专业名称");
            addHeaderCell(table, "方向");
            addHeaderCell(table, "学习方式");

            if (rows != null) {
                for (AppUserPreferenceExportRespVO row : rows) {
                    if (row == null) {
                        continue;
                    }
                    table.addCell(normalCell(preferenceNoName(row.getPreferenceNo())));
                    table.addCell(normalCell(row.getSchoolName()));
                    table.addCell(normalCell(row.getCollegeName()));
                    table.addCell(normalCell(row.getMajorCode()));
                    table.addCell(normalCell(row.getMajorName()));
                    table.addCell(normalCell(row.getDirectionName()));
                    table.addCell(normalCell(row.getStudyModeName()));
                }
            }

            document.add(table);
            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.PDF_GENERATE_ERROR);
        }
    }

    private void addHeaderCell(Table table, String text) {
        Cell cell = new Cell().add(new Paragraph(StrUtil.blankToDefault(text, "")))
                .setBold()
                .setBackgroundColor(new DeviceRgb(240, 240, 240))
                .setFontColor(ColorConstants.BLACK);
        table.addHeaderCell(cell);
    }

    private Cell normalCell(String text) {
        return new Cell().add(new Paragraph(StrUtil.blankToDefault(text, "")));
    }

    private String preferenceNoName(Integer preferenceNo) {
        if (preferenceNo == null) {
            return "";
        }
        return switch (preferenceNo) {
            case 1 -> "第一志愿";
            case 2 -> "第二志愿";
            case 3 -> "第三志愿";
            default -> String.valueOf(preferenceNo);
        };
    }
}

