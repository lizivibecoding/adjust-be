package com.hongguoyan.module.biz.service.major;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.major.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.major.MajorDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;

/**
 * 专业 Service 接口
 *
 * @author hgy
 */
public interface MajorService {

    /**
     * 创建专业
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createMajor(@Valid AppMajorSaveReqVO createReqVO);

    /**
     * 更新专业
     *
     * @param updateReqVO 更新信息
     */
    void updateMajor(@Valid AppMajorSaveReqVO updateReqVO);

    /**
     * 删除专业
     *
     * @param id 编号
     */
    void deleteMajor(Long id);

    /**
    * 批量删除专业
    *
    * @param ids 编号
    */
    void deleteMajorListByIds(List<Long> ids);

    /**
     * 获得专业
     *
     * @param id 编号
     * @return 专业
     */
    MajorDO getMajor(Long id);

    /**
     * 获得专业分页
     *
     * @param pageReqVO 分页查询
     * @return 专业分页
     */
    PageResult<MajorDO> getMajorPage(AppMajorPageReqVO pageReqVO);

    /**
     * 获得一级学科列表
     *
     * @return 一级学科列表
     */
    List<AppMajorLevel1RespVO> getMajorLevel1List();

}