package com.hongguoyan.module.biz.dal.mysql.recommend;

import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.recommend.RecommendRuleDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 推荐算法规则参数 Mapper
 *
 * @author hgy
 */
@Mapper
public interface RecommendRuleMapper extends BaseMapperX<RecommendRuleDO> {
}
