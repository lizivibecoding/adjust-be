package com.hongguoyan.module.biz.dal.mysql.usercustomreport;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.dal.dataobject.usercustomreport.UserCustomReportDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    default UserCustomReportDO selectByUserIdAndId(Long userId, Long reportId) {
        if (userId == null || reportId == null) {
            return null;
        }
        return selectOne(new LambdaQueryWrapperX<UserCustomReportDO>()
                .eq(UserCustomReportDO::getUserId, userId)
                .eq(UserCustomReportDO::getId, reportId)
                .last("LIMIT 1"));
    }

    default List<UserCustomReportDO> selectListByUserId(Long userId) {
        if (userId == null) {
            return List.of();
        }
        // list page only needs light fields; keep payload small
        return selectList(new LambdaQueryWrapperX<UserCustomReportDO>()
                .select(UserCustomReportDO::getId, UserCustomReportDO::getUserId,
                        UserCustomReportDO::getReportNo, UserCustomReportDO::getReportName,
                        UserCustomReportDO::getCreateTime,UserCustomReportDO::getGenerateStatus)
                .eq(UserCustomReportDO::getUserId, userId)
                .orderByDesc(UserCustomReportDO::getReportNo));
    }

    default int updateReportNameByUserIdAndId(Long userId, Long reportId, String reportName) {
        if (userId == null || reportId == null) {
            return 0;
        }
        return update(null, new LambdaUpdateWrapper<UserCustomReportDO>()
                .eq(UserCustomReportDO::getUserId, userId)
                .eq(UserCustomReportDO::getId, reportId)
                .set(UserCustomReportDO::getReportName, reportName));
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

