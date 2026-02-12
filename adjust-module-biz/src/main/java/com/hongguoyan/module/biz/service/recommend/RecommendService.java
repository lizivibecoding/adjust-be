package com.hongguoyan.module.biz.service.recommend;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.controller.app.recommend.vo.AppRecommendSchoolListReqVO;
import com.hongguoyan.module.biz.controller.app.recommend.vo.AppRecommendSchoolRespVO;

/**
 * 智能推荐 Service 接口
 *
 * @author hgy
 */
public interface RecommendService {

    /**
     * 获取用户的智能推荐院校列表（分页）
     *
     * @param userId 用户ID
     * @param reqVO 请求参数（含分页、类别筛选）
     * @return 推荐分页结果
     */
    PageResult<AppRecommendSchoolRespVO> recommendSchools(Long userId, AppRecommendSchoolListReqVO reqVO);


    /**
     * 生成用户推荐院校专业和 AI 报告
     * @param userId 用户ID
     * @return b
     */
    boolean generateRecommend(Long userId,Long reportId);

    /**
     * Generate user's assessment report (5 dimensions) and persist to biz_user_custom_report.
     * Async: caller should create the report row first, then invoke this method.
     *
     * @param userId   用户ID
     * @param reportId 已创建的空报告ID
     */
     void generateAssessmentReport(Long userId, Long reportId);

}
