package com.hongguoyan.module.biz.service.userpreference;

import com.hongguoyan.module.biz.controller.app.userpreference.vo.AppUserPreferenceExportRespVO;
import java.util.List;

/**
 * User preference PDF export service.
 */
public interface UserPreferencePdfService {

    /**
     * Generate a PDF for user preferences export.
     *
     * @param rows flattened rows to export
     * @return pdf bytes
     */
    byte[] generatePdf(List<AppUserPreferenceExportRespVO> rows);
}

