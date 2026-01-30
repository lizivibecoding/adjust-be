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

    /**
     * 获得学科/专业列表（按层级懒加载）
     *
     * @param parentCode 父级专业代码（level>1 必传；level=2 传一级 code；level=3 传二级 code）
     * @param level      层级（1=一级学科,2=二级学科,3=三级学科）
     * @return 列表
     */
    List<AppMajorChildRespVO> getMajorList(String parentCode, Integer level);

}