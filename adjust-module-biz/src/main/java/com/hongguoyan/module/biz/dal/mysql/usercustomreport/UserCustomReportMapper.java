package com.hongguoyan.module.biz.dal.mysql.usercustomreport;

import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.dal.dataobject.usercustomreport.UserCustomReportDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户AI调剂定制报告 Mapper
 *
 * @author hgy
 */
@Mapper
public interface UserCustomReportMapper extends BaseMapperX<UserCustomReportDO> {

    default UserCustomReportDO selectLatestByUserId(Long userId) {
        return selectOne(new LambdaQueryWrapperX<UserCustomReportDO>()
                .eq(UserCustomReportDO::getUserId, userId)
                .orderByDesc(UserCustomReportDO::getId)
                .last("LIMIT 1"));
    }

    /**
     * @return max(report_no) for the user; 0 if none
     */
    default int selectMaxReportNoByUserId(Long userId) {
        UserCustomReportDO row = selectOne(new LambdaQueryWrapperX<UserCustomReportDO>()
                .select(UserCustomReportDO::getReportNo)
                .eq(UserCustomReportDO::getUserId, userId)
                .orderByDesc(UserCustomReportDO::getReportNo)
                .last("LIMIT 1"));
        return row == null || row.getReportNo() == null ? 0 : row.getReportNo();
    }

}

