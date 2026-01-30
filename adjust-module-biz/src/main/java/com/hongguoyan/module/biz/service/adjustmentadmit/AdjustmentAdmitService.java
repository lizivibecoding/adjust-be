package com.hongguoyan.module.biz.service.adjustmentadmit;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.AppAdjustmentAnalysisReqVO;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.AppAdjustmentAnalysisRespVO;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.AppSameScorePageReqVO;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.AppSameScoreStatReqVO;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.AppSameScoreItemRespVO;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.AppSameScoreStatItemRespVO;
import com.hongguoyan.module.biz.controller.app.adjustmentadmit.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.adjustmentadmit.AdjustmentAdmitDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;

/**
 * 调剂录取名单 Service 接口
 *
 * @author hgy
 */
public interface AdjustmentAdmitService {

    /**
     * 创建调剂录取名单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createAdjustmentAdmit(@Valid AppAdjustmentAdmitSaveReqVO createReqVO);

    /**
     * 更新调剂录取名单
     *
     * @param updateReqVO 更新信息
     */
    void updateAdjustmentAdmit(@Valid AppAdjustmentAdmitSaveReqVO updateReqVO);

    /**
     * 删除调剂录取名单
     *
     * @param id 编号
     */
    void deleteAdjustmentAdmit(Long id);

    /**
    * 批量删除调剂录取名单
    *
    * @param ids 编号
    */
    void deleteAdjustmentAdmitListByIds(List<Long> ids);

    /**
     * 获得调剂录取名单
     *
     * @param id 编号
     * @return 调剂录取名单
     */
    AdjustmentAdmitDO getAdjustmentAdmit(Long id);

    /**
     * 获得调剂录取名单分页
     *
     * @param pageReqVO 分页查询
     * @return 调剂录取名单分页
     */
    PageResult<AdjustmentAdmitDO> getAdjustmentAdmitPage(AppAdjustmentAdmitPageReqVO pageReqVO);

    /**
     * 调剂录取名单列表(用于前端展示)
     *
     * @param reqVO 条件
     * @return 列表
     */
    List<AppAdjustmentAdmitListItemRespVO> getAdmitList(@Valid AppAdjustmentAdmitListReqVO reqVO);

    /**
     * 调剂分析(基于录取名单聚合)
     *
     * @param reqVO 条件
     * @return 分析数据
     */
    AppAdjustmentAnalysisRespVO getAnalysis(@Valid AppAdjustmentAnalysisReqVO reqVO);

    /**
     * 同分调剂去向列表(按初试分数区间聚合)
     *
     * @param reqVO 条件
     * @return 分页列表
     */
    PageResult<AppSameScoreItemRespVO> getSameScorePage(@Valid AppSameScorePageReqVO reqVO);

    /**
     * 同分调剂去向院校层次统计(按学校去重计数)
     *
     * @param reqVO 条件
     * @return 统计列表
     */
    List<AppSameScoreStatItemRespVO> getSameScoreStat(@Valid AppSameScoreStatReqVO reqVO);

}