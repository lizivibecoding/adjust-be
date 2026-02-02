package com.hongguoyan.module.biz.service.recommend;

import com.hongguoyan.module.biz.controller.app.recommend.vo.AppRecommendSchoolListReqVO;
import com.hongguoyan.module.biz.controller.app.recommend.vo.AppRecommendSchoolRespVO;

import java.util.List;

/**
 * 智能推荐 Service 接口
 *
 * @author hgy
 */
public interface RecommendService {

    /**
     * 获取用户的智能推荐院校列表
     *
     * @param userId 用户ID
     * @param reqVO 请求参数
     * @return 推荐列表
     */
    List<AppRecommendSchoolRespVO> recommendSchools(Long userId, AppRecommendSchoolListReqVO reqVO);


    /**
     * 生成用户推荐院校专业和 AI 报告
     * @param userId 用户ID
     * @return b
     */
    boolean generateRecommend(Long userId);


}
