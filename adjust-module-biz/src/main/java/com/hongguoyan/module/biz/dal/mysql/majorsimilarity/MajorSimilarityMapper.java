package com.hongguoyan.module.biz.dal.mysql.majorsimilarity;

import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.majorsimilarity.MajorSimilarityDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 专业相似度（相似专业桶） Mapper
 *
 * @author hgy
 */
@Mapper
public interface MajorSimilarityMapper extends BaseMapperX<MajorSimilarityDO> {
}
