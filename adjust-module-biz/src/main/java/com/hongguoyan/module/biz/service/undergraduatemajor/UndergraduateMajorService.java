package com.hongguoyan.module.biz.service.undergraduatemajor;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.admin.undergraduatemajor.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.undergraduatemajor.UndergraduateMajorDO;
import com.hongguoyan.framework.common.pojo.PageResult;

/**
 * 学科专业 Service 接口
 *
 * @author hgy
 */
public interface UndergraduateMajorService {

    /**
     * 创建学科专业
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createUndergraduateMajor(@Valid UndergraduateMajorSaveReqVO createReqVO);

    /**
     * 更新学科专业
     *
     * @param updateReqVO 更新信息
     */
    void updateUndergraduateMajor(@Valid UndergraduateMajorSaveReqVO updateReqVO);

    /**
     * 删除学科专业
     *
     * @param id 编号
     */
    void deleteUndergraduateMajor(Long id);

    /**
     * 批量删除学科专业
     *
     * @param ids 编号列表
     */
    void deleteUndergraduateMajorListByIds(List<Long> ids);

    /**
     * 获得学科专业
     *
     * @param id 编号
     * @return 学科专业
     */
    UndergraduateMajorDO getUndergraduateMajor(Long id);

    /**
     * 获得学科专业分页
     *
     * @param pageReqVO 分页查询
     * @return 学科专业分页
     */
    PageResult<UndergraduateMajorDO> getUndergraduateMajorPage(UndergraduateMajorPageReqVO pageReqVO);

    /**
     * 获得全部学科专业列表（状态正常）
     *
     * @return 学科专业列表
     */
    List<UndergraduateMajorDO> getUndergraduateMajorList();

}
