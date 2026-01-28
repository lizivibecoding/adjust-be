package com.hongguoyan.module.biz.service.nationalscore;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.admin.nationalscore.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.nationalscore.NationalScoreDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;

/**
 * 国家线 Service 接口
 *
 * @author hgy
 */
public interface NationalScoreService {

    /**
     * 创建国家线
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createNationalScore(@Valid NationalScoreSaveReqVO createReqVO);

    /**
     * 更新国家线
     *
     * @param updateReqVO 更新信息
     */
    void updateNationalScore(@Valid NationalScoreSaveReqVO updateReqVO);

    /**
     * 删除国家线
     *
     * @param id 编号
     */
    void deleteNationalScore(Long id);

    /**
    * 批量删除国家线
    *
    * @param ids 编号
    */
    void deleteNationalScoreListByIds(List<Long> ids);

    /**
     * 获得国家线
     *
     * @param id 编号
     * @return 国家线
     */
    NationalScoreDO getNationalScore(Long id);

    /**
     * 获得国家线分页
     *
     * @param pageReqVO 分页查询
     * @return 国家线分页
     */
    PageResult<NationalScoreDO> getNationalScorePage(NationalScorePageReqVO pageReqVO);

}