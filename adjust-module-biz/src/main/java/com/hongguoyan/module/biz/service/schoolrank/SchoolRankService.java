package com.hongguoyan.module.biz.service.schoolrank;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.admin.schoolrank.vo.*;
import com.hongguoyan.module.biz.controller.app.schoolrank.vo.AppSchoolRankSimpleRespVO;
import com.hongguoyan.module.biz.dal.dataobject.schoolrank.SchoolRankDO;
import com.hongguoyan.framework.common.pojo.PageResult;

/**
 * 软科排名 Service 接口
 *
 * @author hgy
 */
public interface SchoolRankService {

    /**
     * 创建软科排名
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSchoolRank(@Valid SchoolRankSaveReqVO createReqVO);

    /**
     * 更新软科排名
     *
     * @param updateReqVO 更新信息
     */
    void updateSchoolRank(@Valid SchoolRankSaveReqVO updateReqVO);

    /**
     * 删除软科排名
     *
     * @param id 编号
     */
    void deleteSchoolRank(Long id);

    /**
     * 批量删除软科排名
     *
     * @param ids 编号
     */
    void deleteSchoolRankListByIds(List<Long> ids);

    /**
     * 获得软科排名
     *
     * @param id 编号
     * @return 软科排名
     */
    SchoolRankDO getSchoolRank(Long id);

    /**
     * 获得软科排名分页
     *
     * @param pageReqVO 分页查询
     * @return 软科排名分页
     */
    PageResult<SchoolRankDO> getSchoolRankPage(SchoolRankPageReqVO pageReqVO);

    /**
     * 获得学院排名简单列表
     *
     * @param schoolName 学校名称（模糊搜索，可为空）
     * @return 简单列表
     */
    List<AppSchoolRankSimpleRespVO> getSchoolRankSimpleList(String schoolName);

}
