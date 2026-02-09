package com.hongguoyan.module.biz.dal.mysql.userintention;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.app.userintention.vo.AppUserIntentionPageReqVO;
import com.hongguoyan.module.biz.dal.dataobject.userintention.UserIntentionDO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户调剂意向与偏好设置 Mapper
 *
 * @author hgy
 */
@Mapper
public interface UserIntentionMapper extends BaseMapperX<UserIntentionDO> {

    default PageResult<UserIntentionDO> selectPage(AppUserIntentionPageReqVO reqVO) {
        LambdaQueryWrapperX<UserIntentionDO> query = new LambdaQueryWrapperX<UserIntentionDO>()
                .eqIfPresent(UserIntentionDO::getUserId, reqVO.getUserId())
                .eqIfPresent(UserIntentionDO::getProvinceCodes, reqVO.getProvinceCodes())
                .eqIfPresent(UserIntentionDO::getExcludeProvinceCodes, reqVO.getExcludeProvinceCodes())
                .eqIfPresent(UserIntentionDO::getMajorIds, reqVO.getMajorIds())
                .eqIfPresent(UserIntentionDO::getStudyMode, reqVO.getStudyMode())
                .eqIfPresent(UserIntentionDO::getDegreeType, reqVO.getDegreeType())
                .eqIfPresent(UserIntentionDO::getIsSpecialPlan, reqVO.getIsSpecialPlan())
                .eqIfPresent(UserIntentionDO::getIsAcceptResearchInst, reqVO.getIsAcceptResearchInst())
                .eqIfPresent(UserIntentionDO::getIsAcceptCrossMajor, reqVO.getIsAcceptCrossMajor())
                .eqIfPresent(UserIntentionDO::getIsAcceptCrossExam, reqVO.getIsAcceptCrossExam())
                .eqIfPresent(UserIntentionDO::getAdjustPriority, reqVO.getAdjustPriority())
                .betweenIfPresent(UserIntentionDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(UserIntentionDO::getId);

        // schoolLevel is stored as JSON array in DB; filter by "any match"
        List<String> schoolLevels = reqVO.getSchoolLevels();
        if (CollUtil.isNotEmpty(schoolLevels)) {
            query.and(w -> {
                for (int i = 0; i < schoolLevels.size(); i++) {
                    String level = schoolLevels.get(i);
                    if (i == 0) {
                        w.apply("JSON_CONTAINS(school_level, {0})", JSONUtil.toJsonStr(List.of(level)));
                    } else {
                        w.or().apply("JSON_CONTAINS(school_level, {0})", JSONUtil.toJsonStr(List.of(level)));
                    }
                }
            });
        }

        return selectPage(reqVO, query);
    }

}

