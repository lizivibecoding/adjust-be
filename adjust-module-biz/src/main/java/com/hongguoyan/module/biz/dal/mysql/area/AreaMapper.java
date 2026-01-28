package com.hongguoyan.module.biz.dal.mysql.area;

import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.dal.dataobject.area.AreaDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Area Mapper
 *
 * @author hgy
 */
@Mapper
public interface AreaMapper extends BaseMapperX<AreaDO> {

    default List<AreaDO> selectAllOrderByAreaAndCode() {
        return selectList(new LambdaQueryWrapperX<AreaDO>()
                .orderByAsc(AreaDO::getArea)
                .orderByAsc(AreaDO::getCode));
    }
}

