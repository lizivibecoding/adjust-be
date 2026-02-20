package com.hongguoyan.module.biz.service.userpreference;

import cn.hutool.core.util.StrUtil;
import com.hongguoyan.module.biz.controller.app.userpreference.vo.AppUserPreferenceExportRespVO;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil;
import com.hongguoyan.module.biz.enums.ErrorCodeConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户志愿 PDF 导出 Service 实现类
 */
@Service
@Slf4j
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
            return addWatermarks(baos.toByteArray());
        } catch (Exception e) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.PDF_GENERATE_ERROR);
        }
    }

    /**
     * 重新打开已生成的 PDF，逐页添加水印图片
     */
    private byte[] addWatermarks(byte[] srcPdfBytes) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = getClass().getClassLoader();
            }
            ImageData centerImgData = ImageDataFactory.create(
                    Objects.requireNonNull(classLoader.getResource("logo/hgy_shuban_mid.png"), "Center watermark not found"));
            ImageData topRightImgData = ImageDataFactory.create(
                    Objects.requireNonNull(classLoader.getResource("logo/hgy_hengban_top.png"), "Top-right watermark not found"));

            ByteArrayOutputStream destBaos = new ByteArrayOutputStream();
            PdfDocument pdfDoc = new PdfDocument(new PdfReader(new java.io.ByteArrayInputStream(srcPdfBytes)), new PdfWriter(destBaos));

            PdfExtGState gs = new PdfExtGState().setFillOpacity(1f);

            int numberOfPages = pdfDoc.getNumberOfPages();
            for (int i = 1; i <= numberOfPages; i++) {
                PdfPage page = pdfDoc.getPage(i);
                Rectangle pageSize = page.getPageSize();
                PdfCanvas canvas = new PdfCanvas(page);
                canvas.saveState();
                canvas.setExtGState(gs);

                // 1. 居中水印（竖版 logo）
                float centerImgWidth = 200;
                float centerImgHeight = centerImgWidth * centerImgData.getHeight() / centerImgData.getWidth();
                float centerX = (pageSize.getWidth() - centerImgWidth) / 2;
                float centerY = (pageSize.getHeight() - centerImgHeight) / 2;
                canvas.addImageFittedIntoRectangle(centerImgData,
                        new Rectangle(centerX, centerY, centerImgWidth, centerImgHeight), false);

                // 2. 右上角水印（横版 logo，10pt 边距）
                float topRightImgWidth = 80;
                float topRightImgHeight = topRightImgWidth * topRightImgData.getHeight() / topRightImgData.getWidth();
                float topRightX = pageSize.getWidth() - topRightImgWidth - 40;
                float topRightY = pageSize.getHeight() - topRightImgHeight - 10;
                canvas.addImageFittedIntoRectangle(topRightImgData,
                        new Rectangle(topRightX, topRightY, topRightImgWidth, topRightImgHeight), false);

                canvas.restoreState();
            }
            pdfDoc.close();
            return destBaos.toByteArray();
        } catch (Exception e) {
            log.warn("添加水印失败，返回无水印 PDF", e);
            return srcPdfBytes;
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

