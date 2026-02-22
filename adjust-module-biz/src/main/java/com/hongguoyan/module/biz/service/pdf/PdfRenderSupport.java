package com.hongguoyan.module.biz.service.pdf;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * PDF 渲染公共支持：字体缓存 + 水印事件处理。
 */
public final class PdfRenderSupport {

    private static final String FONT_NAME = "fonts/NotoSansSC-Regular.ttf";
    private static final String FONT_ENCODING = "Identity-H";
    private static volatile byte[] CACHED_FONT_BYTES;
    private static volatile ImageData CACHED_CENTER_WATERMARK_IMG;
    private static volatile ImageData CACHED_TOP_RIGHT_WATERMARK_IMG;

    private PdfRenderSupport() {
    }

    public static PdfFont createReportFont() throws IOException {
        return PdfFontFactory.createFont(getOrLoadFontBytes(), FONT_ENCODING, PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);
    }

    public static void registerWatermarkHandler(PdfDocument pdfDoc) {
        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE,
                new WatermarkPageEventHandler(getCenterWatermarkImage(), getTopRightWatermarkImage()));
    }

    private static byte[] getOrLoadFontBytes() throws IOException {
        byte[] localCache = CACHED_FONT_BYTES;
        if (localCache != null) {
            return localCache;
        }
        synchronized (PdfRenderSupport.class) {
            if (CACHED_FONT_BYTES == null) {
                try (InputStream is = PdfRenderSupport.class.getClassLoader().getResourceAsStream(FONT_NAME)) {
                    if (is == null) {
                        throw new RuntimeException("Font file not found: " + FONT_NAME);
                    }
                    CACHED_FONT_BYTES = is.readAllBytes();
                }
            }
            return CACHED_FONT_BYTES;
        }
    }

    private static ImageData getCenterWatermarkImage() {
        ImageData localCache = CACHED_CENTER_WATERMARK_IMG;
        if (localCache != null) {
            return localCache;
        }
        synchronized (PdfRenderSupport.class) {
            if (CACHED_CENTER_WATERMARK_IMG == null) {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                if (classLoader == null) {
                    classLoader = PdfRenderSupport.class.getClassLoader();
                }
                CACHED_CENTER_WATERMARK_IMG = ImageDataFactory.create(
                        Objects.requireNonNull(classLoader.getResource("logo/hgy_shuban_mid.png"), "Center watermark not found"));
            }
            return CACHED_CENTER_WATERMARK_IMG;
        }
    }

    private static ImageData getTopRightWatermarkImage() {
        ImageData localCache = CACHED_TOP_RIGHT_WATERMARK_IMG;
        if (localCache != null) {
            return localCache;
        }
        synchronized (PdfRenderSupport.class) {
            if (CACHED_TOP_RIGHT_WATERMARK_IMG == null) {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                if (classLoader == null) {
                    classLoader = PdfRenderSupport.class.getClassLoader();
                }
                CACHED_TOP_RIGHT_WATERMARK_IMG = ImageDataFactory.create(
                        Objects.requireNonNull(classLoader.getResource("logo/hgy_hengban_top.png"), "Top-right watermark not found"));
            }
            return CACHED_TOP_RIGHT_WATERMARK_IMG;
        }
    }

    private static class WatermarkPageEventHandler implements IEventHandler {
        private final ImageData centerImgData;
        private final ImageData topRightImgData;

        private WatermarkPageEventHandler(ImageData centerImgData, ImageData topRightImgData) {
            this.centerImgData = centerImgData;
            this.topRightImgData = topRightImgData;
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfPage page = docEvent.getPage();
            Rectangle pageSize = page.getPageSize();
            PdfCanvas canvas = new PdfCanvas(page.newContentStreamAfter(), page.getResources(), docEvent.getDocument());
            canvas.saveState();
            canvas.setExtGState(new PdfExtGState().setFillOpacity(1f));

            float centerImgWidth = 200;
            float centerImgHeight = centerImgWidth * centerImgData.getHeight() / centerImgData.getWidth();
            float centerX = (pageSize.getWidth() - centerImgWidth) / 2;
            float centerY = (pageSize.getHeight() - centerImgHeight) / 2;
            canvas.addImageFittedIntoRectangle(centerImgData,
                    new Rectangle(centerX, centerY, centerImgWidth, centerImgHeight), false);

            float topRightImgWidth = 80;
            float topRightImgHeight = topRightImgWidth * topRightImgData.getHeight() / topRightImgData.getWidth();
            float topRightX = pageSize.getWidth() - topRightImgWidth - 40;
            float topRightY = pageSize.getHeight() - topRightImgHeight - 10;
            canvas.addImageFittedIntoRectangle(topRightImgData,
                    new Rectangle(topRightX, topRightY, topRightImgWidth, topRightImgHeight), false);
            canvas.restoreState();
        }
    }
}
