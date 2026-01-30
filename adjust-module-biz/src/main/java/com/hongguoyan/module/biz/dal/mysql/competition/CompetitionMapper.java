package com.hongguoyan.module.biz.dal.mysql.competition;

import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.dal.dataobject.competition.CompetitionDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Competition Mapper.
 */
@Mapper
public interface CompetitionMapper extends BaseMapperX<CompetitionDO> {

    default List<CompetitionDO> selectAll() {
        LambdaQueryWrapperX<CompetitionDO> qw = new LambdaQueryWrapperX<>();
        qw.select(CompetitionDO::getId, CompetitionDO::getName, CompetitionDO::getUrl);
        qw.orderByAsc(CompetitionDO::getId);
        return selectList(qw);
    }
}

