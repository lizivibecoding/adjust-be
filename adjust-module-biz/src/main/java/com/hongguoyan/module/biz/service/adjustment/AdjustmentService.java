package com.hongguoyan.module.biz.service.adjustment;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.*;
import com.hongguoyan.module.biz.controller.app.school.vo.AppSchoolAdjustmentPageReqVO;
import com.hongguoyan.module.biz.controller.app.school.vo.AppSchoolAdjustmentRespVO;
import com.hongguoyan.module.biz.dal.dataobject.adjustment.AdjustmentDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;

/**
 * 调剂 Service 接口
 *
 * @author hgy
 */
public interface AdjustmentService {

    /**
     * 创建调剂
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createAdjustment(@Valid AppAdjustmentSaveReqVO createReqVO);

    /**
     * 更新调剂
     *
     * @param updateReqVO 更新信息
     */
    void updateAdjustment(@Valid AppAdjustmentSaveReqVO updateReqVO);

    /**
     * 删除调剂
     *
     * @param id 编号
     */
    void deleteAdjustment(Long id);

    /**
    * 批量删除调剂
    *
    * @param ids 编号
    */
    void deleteAdjustmentListByIds(List<Long> ids);

    /**
     * 获得调剂
     *
     * @param id 编号
     * @return 调剂
     */
    AdjustmentDO getAdjustment(Long id);

    /**
     * 获得调剂分页
     *
     * @param pageReqVO 分页查询
     * @return 调剂分页
     */
    PageResult<AdjustmentDO> getAdjustmentPage(AppAdjustmentPageReqVO pageReqVO);

    /**
     * 调剂全局搜索（按 Tab 返回）
     *
     * @param userId 用户ID（可为空，表示未登录）
     * @param reqVO 搜索条件
     * @return 搜索结果
     */
    AppAdjustmentSearchTabRespVO getAdjustmentSearchPage(Long userId, AppAdjustmentSearchReqVO reqVO);

    /**
     * 调剂联想词
     *
     * @param keyword 关键词
     * @return 联想词
     */
    AppAdjustmentSuggestRespVO getAdjustmentSuggest(String keyword);

    /**
     * 调剂筛选配置
     *
     * @return 筛选配置
     */
    AppAdjustmentFilterConfigRespVO getAdjustmentFilterConfig(String majorCode);

    /**
     * 调剂详情切换选项(年份/学院)
     *
     * @param reqVO 条件
     * @return 选项
     */
    AppAdjustmentOptionsRespVO getAdjustmentOptions(@Valid AppAdjustmentOptionsReqVO reqVO);

    /**
     * 调剂详情(按方向聚合返回)
     *
     * @param userId 用户ID（可为空，表示未登录）
     * @param reqVO 条件
     * @return 详情
     */
    AppAdjustmentDetailRespVO getAdjustmentDetail(Long userId, @Valid AppAdjustmentDetailReqVO reqVO);

    /**
     * 调剂更新统计(默认2025年)
     *
     * @return 更新统计
     */
    AppAdjustmentUpdateStatsRespVO getAdjustmentUpdateStats();

    /**
     * 热门调剂专业排名
     *
     * @param reqVO 条件
     * @return 分页
     */
    PageResult<AppAdjustmentSearchRespVO> getHotRankingPage(@Valid AppAdjustmentHotRankingReqVO reqVO);

    /**
     * 院校调剂列表(调剂 Tab)
     *
     * @param reqVO 条件
     * @return 分页
     */
    PageResult<AppSchoolAdjustmentRespVO> getSchoolAdjustmentPage(@Valid AppSchoolAdjustmentPageReqVO reqVO);

}
