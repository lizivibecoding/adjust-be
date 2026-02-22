package com.hongguoyan.module.biz.service.recommend;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.dfa.SensitiveUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.dal.dataobject.adjustment.AdjustmentDO;
import com.hongguoyan.module.biz.dal.dataobject.adjustmentadmit.AdjustmentAdmitDO;
import com.hongguoyan.module.biz.dal.dataobject.area.AreaDO;
import com.hongguoyan.module.biz.dal.dataobject.major.MajorDO;
import com.hongguoyan.module.biz.dal.dataobject.recommend.UserRecommendSchoolDO;
import com.hongguoyan.module.biz.dal.dataobject.usercustomreport.UserCustomReportDO;
import com.hongguoyan.module.biz.dal.dataobject.userintention.UserIntentionDO;
import com.hongguoyan.module.biz.dal.dataobject.userprofile.UserProfileDO;
import com.hongguoyan.module.biz.dal.mysql.adjustment.AdjustmentMapper;
import com.hongguoyan.module.biz.dal.mysql.adjustmentadmit.AdjustmentAdmitMapper;
import com.hongguoyan.module.biz.dal.mysql.area.AreaMapper;
import com.hongguoyan.module.biz.dal.mysql.major.MajorMapper;
import com.hongguoyan.module.biz.dal.mysql.recommend.UserRecommendSchoolMapper;
import com.hongguoyan.module.biz.dal.mysql.usercustomreport.UserCustomReportMapper;
import com.hongguoyan.module.biz.dal.mysql.userintention.UserIntentionMapper;
import com.hongguoyan.module.biz.dal.mysql.userprofile.UserProfileMapper;
import com.hongguoyan.module.biz.enums.ErrorCodeConstants;
import com.hongguoyan.module.biz.service.pdf.PdfRenderSupport;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import jakarta.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 推荐报告 PDF 生成 Service 实现类
 *
 * @author hgy
 */
@Service
@Slf4j
public class RecommendPdfServiceImpl implements RecommendPdfService {
    @Resource
    private UserCustomReportMapper userCustomReportMapper;
    @Resource
    private UserProfileMapper userProfileMapper;
    @Resource
    private UserIntentionMapper userIntentionMapper;
    @Resource
    private UserRecommendSchoolMapper userRecommendSchoolMapper;
    @Resource
    private AdjustmentAdmitMapper adjustmentAdmitMapper;
    @Resource
    private AdjustmentMapper adjustmentMapper;
    @Resource
    private MajorMapper majorMapper;
    @Resource
    private AreaMapper areaMapper;

    @Override
    public byte[] generateReportPdf(Long userId, Long reportId) {
        // 1. 获取报告数据
        UserCustomReportDO report = userCustomReportMapper.selectByUserIdAndId(userId, reportId);
        if (report == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CANDIDATE_CUSTOM_REPORTS_NOT_EXISTS);
        }

        // 2. 获取快照或实时数据
        UserProfileDO userProfile;
        if (StrUtil.isNotBlank(report.getSourceProfileJson())) {
            userProfile = JSONUtil.toBean(report.getSourceProfileJson(), UserProfileDO.class);
        } else if (report.getSourceProfileId() != null) {
            userProfile = userProfileMapper.selectById(report.getSourceProfileId());
        } else {
            userProfile = userProfileMapper.selectOne(new LambdaQueryWrapperX<UserProfileDO>().eq(UserProfileDO::getUserId, userId));
        }

        UserIntentionDO userIntention;
        if (StrUtil.isNotBlank(report.getSourceIntentionJson())) {
            userIntention = JSONUtil.toBean(report.getSourceIntentionJson(), UserIntentionDO.class);
        } else if (report.getSourceIntentionId() != null) {
            userIntention = userIntentionMapper.selectById(report.getSourceIntentionId());
        } else {
            userIntention = userIntentionMapper.selectOne(new LambdaQueryWrapperX<UserIntentionDO>().eq(UserIntentionDO::getUserId, userId));
        }

        // 3. 获取推荐列表
        List<UserRecommendSchoolDO> recommendations = userRecommendSchoolMapper.selectList(new LambdaQueryWrapperX<UserRecommendSchoolDO>()
                .eq(UserRecommendSchoolDO::getReportId, reportId)
                .orderByDesc(UserRecommendSchoolDO::getSimFinal));

        // 4. 生成 PDF
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            try {
                PdfRenderSupport.registerWatermarkHandler(pdf);
            } catch (Exception e) {
                log.warn("注册水印处理器失败，继续生成无水印版本", e);
            }
            
            // 设置中文字体（字体字节缓存复用，避免每次重复读取）
            PdfFont font = PdfRenderSupport.createReportFont();
            document.setFont(font);

            // --- 标题 ---
            Paragraph title = new Paragraph(DateUtil.thisYear() + " 年考研调剂智能评估报告")
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            // --- 1. 基本信息 ---
            addSectionTitle(document, "一、 基本信息");
            Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 3, 1, 3})).useAllAvailableWidth();
            addCell(infoTable, "本科院校", true);
            addCell(infoTable, userProfile != null ? userProfile.getGraduateSchoolName() : "-", false);
            addCell(infoTable, "本科专业", true);
            addCell(infoTable, userProfile != null ? userProfile.getGraduateMajorName() : "-", false);

            addCell(infoTable, "本科平均分", true);
            addCell(infoTable, userProfile != null && userProfile.getGraduateAverageScore() != null ? userProfile.getGraduateAverageScore().toString() : "-", false);

            addCell(infoTable, "英语四级", true);
            addCell(infoTable, userProfile != null && userProfile.getCet4Score() != null ? userProfile.getCet4Score().toString() : "-", false);
            addCell(infoTable, "英语六级", true);
            addCell(infoTable, userProfile != null && userProfile.getCet6Score() != null ? userProfile.getCet6Score().toString() : "-", false);

            addCell(infoTable, "省二级以上竞赛", true);
            addCell(infoTable, userProfile != null && userProfile.getCompetitionCount() != null ? userProfile.getCompetitionCount().toString() : "0", false);
            addCell(infoTable, "二作及以上论文", true);
            addCell(infoTable, userProfile != null && userProfile.getPaperCount() != null ? userProfile.getPaperCount().toString() : "0", false);

            addCell(infoTable, "国家级奖学金", true);
            addCell(infoTable, userProfile != null && Boolean.TRUE.equals(userProfile.getIsNationalScholarship()) ? "是" : "否", false);
            addCell(infoTable, "校级以上奖学金", true);
            addCell(infoTable, userProfile != null && Boolean.TRUE.equals(userProfile.getIsSchoolScholarship()) ? "是" : "否", false);
            addCell(infoTable, "", true);
            addCell(infoTable, "", false); // filler

            document.add(infoTable);
            document.add(new Paragraph("\n"));

            addSectionTitle(document, "二、 报考信息");
            Table targetTable = new Table(UnitValue.createPercentArray(new float[]{1, 3, 1, 3})).useAllAvailableWidth();
            addCell(targetTable, "一志愿院校", true);
            addCell(targetTable, userProfile != null ? userProfile.getTargetSchoolName() : "-", false);
            addCell(targetTable, "一志愿学院", true);
            addCell(targetTable, userProfile != null ? userProfile.getTargetCollegeName() : "-", false);

            addCell(targetTable, "一志愿专业", true);
            addCell(targetTable, userProfile != null ? userProfile.getTargetMajorName() : "-", false);
            addCell(targetTable, "一志愿方向", true);
            addCell(targetTable, userProfile != null ? userProfile.getTargetDirectionName() : "-", false);

            addCell(targetTable, "初试总分", true);
            addCell(targetTable, userProfile != null && userProfile.getScoreTotal() != null ? userProfile.getScoreTotal().toString() : "-", false);
            addCell(targetTable, "科目一", true);
            addCell(targetTable, userProfile != null && userProfile.getSubjectScore1() != null ? userProfile.getSubjectScore1().toString() : "-", false);

            addCell(targetTable, "科目二", true);
            addCell(targetTable, userProfile != null && userProfile.getSubjectScore2() != null ? userProfile.getSubjectScore2().toString() : "-", false);
            addCell(targetTable, "科目三", true);
            addCell(targetTable, userProfile != null && userProfile.getSubjectScore3() != null ? userProfile.getSubjectScore3().toString() : "-", false);

            addCell(targetTable, "科目四", true);
            addCell(targetTable, userProfile != null && userProfile.getSubjectScore4() != null ? userProfile.getSubjectScore4().toString() : "-", false);
            addCell(targetTable, "", true);
            addCell(targetTable, "", false); // filler

            document.add(targetTable);
            document.add(new Paragraph("\n"));

            // --- 2. 意向需求 ---
            addSectionTitle(document, "三、 调剂意向");
            if (userIntention != null) {
                Table intentTable = new Table(UnitValue.createPercentArray(new float[]{1, 4})).useAllAvailableWidth();
                
                // 意向地区 (Codes -> Names)
                String provinceNames = getProvinceNames(userIntention.getProvinceCodes());
                addCell(intentTable, "意向地区", true);
                addCell(intentTable, StrUtil.blankToDefault(provinceNames, "不限"), false);

                addCell(intentTable, "意向院校层次", true);
                addCell(intentTable, getSchoolLevelNames(userIntention.getSchoolLevel()), false);

                // 意向调剂专业 (JSON String of IDs -> Names)
                String majorNames = getMajorNames(userIntention.getMajorIds());
                addCell(intentTable, "意向专业", true);
                addCell(intentTable, StrUtil.blankToDefault(majorNames, "不限"), false);

                // 学习方式 (Integer -> Name)
                addCell(intentTable, "学习方式", true);
                addCell(intentTable, getStudyModeName(userIntention.getStudyMode()), false);

                // 学位类型 (Integer -> Name)
                addCell(intentTable, "学位类型", true);
                addCell(intentTable, getDegreeTypeName(userIntention.getDegreeType()), false);

                // 其他意向 (Booleans)
                addCell(intentTable, "专项计划", true);
                addCell(intentTable, Boolean.TRUE.equals(userIntention.getIsSpecialPlan()) ? "接受" : "不接受", false);

                addCell(intentTable, "跨考", true);
                addCell(intentTable, Boolean.TRUE.equals(userIntention.getIsAcceptCrossExam()) ? "是" : "否", false);

                addCell(intentTable, "跨专业调剂", true);
                addCell(intentTable, Boolean.TRUE.equals(userIntention.getIsAcceptCrossMajor()) ? "是" : "否", false);

                document.add(intentTable);
            } else {
                document.add(new Paragraph("未设置调剂意向"));
            }
            document.add(new Paragraph("\n"));

            // --- 3. 五维评估 ---
            addSectionTitle(document, "四、 综合实力评估");
            // 简单列表展示分数，雷达图需前端生成图片传给后端或后端使用绘图库生成（iText 绘制复杂图形较繁琐，这里暂以表格展示）
            Table scoreTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 3})).useAllAvailableWidth();
            scoreTable.addHeaderCell(new Cell().add(new Paragraph("维度").setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            scoreTable.addHeaderCell(new Cell().add(new Paragraph("评分").setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            scoreTable.addHeaderCell(new Cell().add(new Paragraph("分析").setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY));

            addScoreRow(scoreTable, "院校背景", report.getDimBackgroundScore(), report.getAnalysisBackground());
            addScoreRow(scoreTable, "初试成绩", report.getDimTotalScore(), report.getAnalysisTotal());
            addScoreRow(scoreTable, "目标层次", report.getDimTargetSchoolLevelScore(), report.getAnalysisTargetSchoolLevel());
            addScoreRow(scoreTable, "专业竞争", report.getDimMajorCompetitivenessScore(), report.getAnalysisMajorCompetitiveness());
            addScoreRow(scoreTable, "软实力", report.getDimSoftSkillsScore(), report.getAnalysisSoftSkills());
            
            document.add(scoreTable);
            document.add(new Paragraph("\n"));

            // --- 4. 推荐方案 ---
            addSectionTitle(document, "五、 调剂推荐方案");
            
            Map<Integer, List<UserRecommendSchoolDO>> grouped = recommendations.stream()
                    .collect(Collectors.groupingBy(r -> r.getCategory() != null ? r.getCategory() : 3));

            addRecommendationGroup(document, "5.1 冲刺方案 (机会较小，值得一试)", grouped.get(1));
            addRecommendationGroup(document, "5.2 稳妥方案 (匹配度高，重点关注)", grouped.get(2));
            addRecommendationGroup(document, "5.3 保底方案 (成功率高，确保有学上)", grouped.get(3));
            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("生成 PDF 失败", e);
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.PDF_GENERATE_ERROR);
        }
    }

    private void addSectionTitle(Document doc, String titleText) {
        Paragraph p = new Paragraph(titleText)
                .setFontSize(14)
                .setBold()
                .setBackgroundColor(new DeviceRgb(240, 240, 240))
                .setPadding(5)
                .setMarginBottom(10);
        doc.add(p);
    }

    private void addCell(Table table, String text, boolean isHeader) {
        // Sanitize text to remove unsupported characters
        String sanitized = sanitizeForPdf(StrUtil.blankToDefault(text, ""));
        Cell cell = new Cell().add(new Paragraph(sanitized));
        if (isHeader) {
            cell.setBold().setBackgroundColor(new DeviceRgb(250, 250, 250));
        }
        table.addCell(cell);
    }

    private void addScoreRow(Table table, String dim, Integer score, String analysis) {
        table.addCell(new Cell().add(new Paragraph(sanitizeForPdf(dim))));
        table.addCell(new Cell().add(new Paragraph(score != null ? score.toString() : "-")));
        table.addCell(new Cell().add(new Paragraph(sanitizeForPdf(StrUtil.blankToDefault(analysis, "-")))));
    }

    private void addRecommendationGroup(Document doc, String groupTitle, List<UserRecommendSchoolDO> list) {
        doc.add(new Paragraph(groupTitle).setFontSize(12).setBold().setMarginTop(10).setMarginBottom(5));
        if (CollUtil.isEmpty(list)) {
            doc.add(new Paragraph("暂无推荐院校").setFontSize(10).setItalic().setFontColor(ColorConstants.GRAY));
            return;
        }
        int targetYearInt = DateUtil.thisYear() - 1;
        // 分组 key: schoolId + collegeId + majorId + studyMode
        // 使用 Map 存储分组后的数据，key 为复合键
        // 这里为了简单，直接用拼接字符串作为 key
        Map<String, List<UserRecommendSchoolDO>> groupedRecs = list.stream()
                .collect(Collectors.groupingBy(r -> buildGroupKey(r.getSchoolId(),r.getCollegeId(),r.getMajorId(),r.getStudyMode())));
        // 按维度整体预取录取名单，循环内仅做 map 读取
        java.util.Set<Long> schoolIds = list.stream()
                .map(UserRecommendSchoolDO::getSchoolId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        java.util.Set<Long> majorIds = list.stream()
                .map(UserRecommendSchoolDO::getMajorId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        List<AdjustmentAdmitDO> allAdmitList = new ArrayList<>();
        if (CollUtil.isNotEmpty(schoolIds) && CollUtil.isNotEmpty(majorIds)) {
            LambdaQueryWrapperX<AdjustmentAdmitDO> allAdmitQuery = new LambdaQueryWrapperX<AdjustmentAdmitDO>()
                    .in(AdjustmentAdmitDO::getSchoolId, schoolIds)
                    .in(AdjustmentAdmitDO::getMajorId, majorIds)
                    .eq(AdjustmentAdmitDO::getYear, targetYearInt)
                    .orderByDesc(AdjustmentAdmitDO::getFirstScore);
            allAdmitList = adjustmentAdmitMapper.selectList(allAdmitQuery);
        }
        Map<String, List<AdjustmentAdmitDO>> groupedAdmitMap = allAdmitList.stream()
                .collect(Collectors.groupingBy(a -> buildGroupKey(a.getSchoolId(), a.getCollegeId(), a.getMajorId(), a.getStudyMode())));

        for (List<UserRecommendSchoolDO> group : groupedRecs.values()) {
            UserRecommendSchoolDO first = group.get(0);

            // 容器框
            Div card = new Div()
                    .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 1))
                    .setPadding(10)
                    .setMarginBottom(10);

            // 标题行：学校 | 学院 | 专业 | 学习方式
            String studyModeStr = getStudyModeName(first.getStudyMode());
            String titleLine = String.format("%s | %s | %s | %s", 
                    sanitizeForPdf(first.getSchoolName()), 
                    sanitizeForPdf(StrUtil.blankToDefault(first.getCollegeName(), "未分院")), 
                    sanitizeForPdf(first.getMajorName()),
                    studyModeStr);
            
            Paragraph pTitle = new Paragraph(titleLine).setBold().setFontSize(11);
            card.add(pTitle);

            // 方向列表
            String directions = group.stream()
                    .map(r -> sanitizeForPdf(StrUtil.blankToDefault(r.getDirectionName(), "不区分方向")))
                    .distinct()
                    .collect(Collectors.joining("，"));
            card.add(new Paragraph("研究方向: " + directions).setFontSize(10));

            // 推荐类型标签
            String categoryLabel = switch (first.getCategory() != null ? first.getCategory() : 3) {
                case 1 -> "冲刺推荐";
                case 2 -> "求稳推荐";
                default -> "兜底推荐";
            };
            Paragraph pCategory = new Paragraph(categoryLabel)
                    .setFontSize(10).setFontColor(ColorConstants.BLUE);
            card.add(pCategory);

            // 4. 从循环外预取结果中按维度取数据
            String groupKey = buildGroupKey(first.getSchoolId(), first.getCollegeId(), first.getMajorId(), first.getStudyMode());
            List<AdjustmentAdmitDO> admitList = groupedAdmitMap.getOrDefault(groupKey, new ArrayList<>());

            // 5. 统计数据由预取名单计算
            AdmitStats stats = queryAdmitStats(admitList);
            if (stats != null) {
                Table statTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1})).useAllAvailableWidth().setMarginTop(5);
                statTable.addHeaderCell(new Cell().add(new Paragraph("最低分").setFontSize(9)));
                statTable.addHeaderCell(new Cell().add(new Paragraph("最高分").setFontSize(9)));
                statTable.addHeaderCell(new Cell().add(new Paragraph("平均分").setFontSize(9)));
                statTable.addHeaderCell(new Cell().add(new Paragraph("中位数").setFontSize(9)));

                statTable.addCell(new Cell().add(new Paragraph(String.valueOf(stats.getMinScore())).setFontSize(9)));
                statTable.addCell(new Cell().add(new Paragraph(String.valueOf(stats.getMaxScore())).setFontSize(9)));
                statTable.addCell(new Cell().add(new Paragraph(String.format("%.1f", stats.getAvgScore())).setFontSize(9)));
                statTable.addCell(new Cell().add(new Paragraph(String.format("%.1f", stats.getMedianScore())).setFontSize(9)));
                card.add(statTable);
            }

            if (CollUtil.isNotEmpty(admitList)) {
                card.add(new Paragraph(targetYearInt+"年录取名单:").setFontSize(10).setBold().setMarginTop(5));
                Table listTable = new Table(UnitValue.createPercentArray(new float[]{2, 1, 3})).useAllAvailableWidth();
                listTable.setFontSize(9);
                listTable.addHeaderCell("姓名");
                listTable.addHeaderCell("初试");
                listTable.addHeaderCell("一志愿院校");
                for (AdjustmentAdmitDO admit : admitList) {
                    listTable.addCell(StrUtil.isBlank(admit.getCandidateName()) ? "-" : sanitizeForPdf(maskCandidateName(admit.getCandidateName())));
                    listTable.addCell(admit.getFirstScore() != null ? admit.getFirstScore().toString() : "-");
                    listTable.addCell(sanitizeForPdf(StrUtil.blankToDefault(admit.getFirstSchoolName(), "-")));
                }
                card.add(listTable);
            } else {
                card.add(new Paragraph("暂无往年录取数据").setFontSize(9).setFontColor(ColorConstants.GRAY).setMarginTop(5));
            }
            
            doc.add(card);
        }
    }

    private String getProvinceNames(String provinceCodesJson) {
        if (StrUtil.isBlank(provinceCodesJson)) {
            return "";
        }
        try {
            List<String> codes = Lists.newArrayList(provinceCodesJson.split(","));
            if (CollUtil.isEmpty(codes)) {
                return "";
            }
            List<AreaDO> areas = areaMapper.selectBatchIds(codes);
            if (CollUtil.isEmpty(areas)) {
                return "";
            }
            return areas.stream().map(AreaDO::getName).collect(Collectors.joining("、"));
        } catch (Exception e) {
            return provinceCodesJson; // Fallback to raw json if parse fails
        }
    }

    private String getMajorNames(String majorIdsJson) {
        if (StrUtil.isBlank(majorIdsJson)) {
            return "";
        }
        try {
            List<Long> ids = JSONUtil.toList(majorIdsJson, Long.class);
            if (CollUtil.isEmpty(ids)) {
                return "";
            }
            List<MajorDO> majors = majorMapper.selectBatchIds(ids);
            if (CollUtil.isEmpty(majors)) {
                return "";
            }
            return majors.stream().map(MajorDO::getName).collect(Collectors.joining("、"));
        } catch (Exception e) {
            return majorIdsJson; // Fallback to raw json if parse fails
        }
    }

    private String getDegreeTypeName(Integer type) {
        if (type == null) return "未知";
        return switch (type) {
            case 0 -> "不限";
            case 1 -> "学硕";
            case 2 -> "专硕";
            default -> "未知";
        };
    }

    private String getSchoolLevelNames(String schoolLevelsJson) {
        if (StrUtil.isBlank(schoolLevelsJson)) {
            return "不限";
        }
        try {
            List<String> levels = JSONUtil.toList(schoolLevelsJson, String.class);
            if (CollUtil.isEmpty(levels)) {
                return "不限";
            }
            return levels.stream().map(this::getSchoolLevelName).filter(StrUtil::isNotBlank).distinct().collect(Collectors.joining("、"));
        } catch (Exception ignore) {
            return "不限";
        }
    }

    private String getSchoolLevelName(String level) {
        if (StrUtil.isBlank(level)) {
            return null;
        }
        return switch (level) {
            case "985" -> "985";
            case "211" -> "211";
            case "syl" -> "双一流";
            case "ordinary" -> "普通院校";
            default -> "其他";
        };
    }

    private String getStudyModeName(Integer mode) {
        if (mode == null) return "未知";
        return mode == 1 ? "全日制" : (mode == 2 ? "非全日制" : "不限");
    }

    /**
     * Mask candidate name: keep first char, replace the rest with *.
     * e.g. 张三 -> 张*, 王老三 -> 王**
     */
    private String maskCandidateName(String name) {
        String trimmed = name.trim();
        int[] cps = trimmed.codePoints().toArray();
        int n = cps.length;
        if (n <= 1) {
            return "*";
        }
        return new String(cps, 0, 1) + "*".repeat(n - 1);
    }

    /**
     * Sanitize text for PDF generation.
     * Noto Sans SC supports most characters, but we still remove control characters.
     */
    private String sanitizeForPdf(String text) {
        if (text == null) {
            return "";
        }
        // Remove control characters but keep newlines
        return text.replaceAll("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F]", "");
    }

    private AdmitStats queryAdmitStats(List<AdjustmentAdmitDO> admitList) {
        if (CollUtil.isEmpty(admitList)) {
            return null;
        }
        List<BigDecimal> scores = admitList.stream()
                .map(AdjustmentAdmitDO::getFirstScore)
                .filter(Objects::nonNull)
                .sorted()
                .toList();
        if (CollUtil.isEmpty(scores)) {
            return null;
        }
        BigDecimal minScore = scores.get(0);
        BigDecimal maxScore = scores.get(scores.size() - 1);
        BigDecimal sum = scores.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal avgScore = sum.divide(BigDecimal.valueOf(scores.size()), 1, RoundingMode.HALF_UP);
        BigDecimal medianScore;
        int size = scores.size();
        if (size % 2 == 0) {
            medianScore = scores.get(size / 2 - 1)
                    .add(scores.get(size / 2))
                    .divide(BigDecimal.valueOf(2), 1, RoundingMode.HALF_UP);
        } else {
            medianScore = scores.get(size / 2);
        }
        AdmitStats stats = new AdmitStats();
        stats.setMinScore(minScore);
        stats.setMaxScore(maxScore);
        stats.setAvgScore(avgScore);
        stats.setMedianScore(medianScore);
        return stats;
    }

    private String buildGroupKey(Long schoolId, Long collegeId, Long majorId, Integer studyMode) {
        int sm = studyMode != null ? studyMode : 0;
        return schoolId + "_" + collegeId + "_" + majorId + "_" + sm;
    }

    @lombok.Data
    private static class AdmitStats {
        private BigDecimal minScore;
        private BigDecimal maxScore;
        private BigDecimal avgScore;
        private BigDecimal medianScore;
    }
}
