package com.hongguoyan.module.biz.service.recommend;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.controller.admin.recommend.rule.vo.RecommendRulePageReqVO;
import com.hongguoyan.module.biz.controller.admin.recommend.rule.vo.RecommendRuleSaveReqVO;
import com.hongguoyan.module.biz.dal.dataobject.recommend.RecommendRuleDO;
import jakarta.validation.Valid;

/**
 * 推荐规则 Service 接口
 *
 * @author hgy
 */
public interface RecommendRuleService {

    /**
     * 创建推荐规则
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createRecommendRule(@Valid RecommendRuleSaveReqVO createReqVO);

    /**
     * 更新推荐规则
     *
     * @param updateReqVO 更新信息
     */
    void updateRecommendRule(@Valid RecommendRuleSaveReqVO updateReqVO);

    /**
     * 删除推荐规则
     *
     * @param id 编号
     */
    void deleteRecommendRule(Long id);

    /**
     * 获得推荐规则
     *
     * @param id 编号
     * @return 推荐规则
     */
    RecommendRuleDO getRecommendRule(Long id);

    /**
     * 获得推荐规则分页
     *
     * @param pageReqVO 分页查询
     * @return 推荐规则分页
     */
    PageResult<RecommendRuleDO> getRecommendRulePage(RecommendRulePageReqVO pageReqVO);

}
