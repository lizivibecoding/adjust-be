package com.hongguoyan.module.biz.service.recommend;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.admin.recommend.rule.vo.RecommendRulePageReqVO;
import com.hongguoyan.module.biz.controller.admin.recommend.rule.vo.RecommendRuleSaveReqVO;
import com.hongguoyan.module.biz.dal.dataobject.recommend.RecommendRuleDO;
import com.hongguoyan.module.biz.dal.mysql.recommend.RecommendRuleMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.NO_RECOMMEND_RULE;

/**
 * 推荐规则 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class RecommendRuleServiceImpl implements RecommendRuleService {

    @Resource
    private RecommendRuleMapper recommendRuleMapper;

    @Override
    public Long createRecommendRule(RecommendRuleSaveReqVO createReqVO) {
        RecommendRuleDO recommendRule = BeanUtils.toBean(createReqVO, RecommendRuleDO.class);
        recommendRuleMapper.insert(recommendRule);
        return recommendRule.getId();
    }

    @Override
    public void updateRecommendRule(RecommendRuleSaveReqVO updateReqVO) {
        // 校验存在
        validateRecommendRuleExists(updateReqVO.getId());
        // 更新
        RecommendRuleDO updateObj = BeanUtils.toBean(updateReqVO, RecommendRuleDO.class);
        recommendRuleMapper.updateById(updateObj);
    }

    @Override
    public void deleteRecommendRule(Long id) {
        // 校验存在
        validateRecommendRuleExists(id);
        // 删除
        recommendRuleMapper.deleteById(id);
    }

    private void validateRecommendRuleExists(Long id) {
        if (recommendRuleMapper.selectById(id) == null) {
            throw exception(NO_RECOMMEND_RULE);
        }
    }

    @Override
    public RecommendRuleDO getRecommendRule(Long id) {
        return recommendRuleMapper.selectById(id);
    }

    @Override
    public PageResult<RecommendRuleDO> getRecommendRulePage(RecommendRulePageReqVO pageReqVO) {
        LambdaQueryWrapperX<RecommendRuleDO> wrapper = new LambdaQueryWrapperX<RecommendRuleDO>();
        wrapper.likeIfPresent(RecommendRuleDO::getName, pageReqVO.getName())
                .likeIfPresent(RecommendRuleDO::getMajorCodes, pageReqVO.getMajorCode())
                .likeIfPresent(RecommendRuleDO::getBucketName, pageReqVO.getBucketName())
                .orderByDesc(RecommendRuleDO::getId);
        return recommendRuleMapper.selectPage(pageReqVO, wrapper);
    }

}
