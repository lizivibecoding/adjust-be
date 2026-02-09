package com.hongguoyan.module.biz.service.userintention;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hongguoyan.framework.common.exception.ErrorCode;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.admin.userintention.vo.UserIntentionPageReqVO;
import com.hongguoyan.module.biz.controller.admin.userintention.vo.UserIntentionSaveReqVO;
import com.hongguoyan.module.biz.controller.app.userintention.vo.AppUserIntentionRespVO;
import com.hongguoyan.module.biz.controller.app.userintention.vo.AppUserIntentionSaveReqVO;
import com.hongguoyan.module.biz.dal.dataobject.userintention.UserIntentionDO;
import com.hongguoyan.module.biz.dal.mysql.userintention.UserIntentionMapper;
import com.hongguoyan.module.biz.service.vipbenefit.VipBenefitService;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.BENEFIT_KEY_USER_INTENTION;

/**
 * 用户调剂意向与偏好设置 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class UserIntentionServiceImpl implements UserIntentionService {

    @Resource
    private UserIntentionMapper userIntentionMapper;
    @Resource
    private VipBenefitService vipBenefitService;

    @Override
    public Long createUserIntention(UserIntentionSaveReqVO createReqVO) {
        UserIntentionDO userIntention = BeanUtils.toBean(createReqVO, UserIntentionDO.class);
        userIntention.setProvinceCodes(toJsonOrNullString(createReqVO.getProvinceCodes()));
        userIntention.setExcludeProvinceCodes(toJsonOrNullString(createReqVO.getExcludeProvinceCodes()));
        userIntention.setSchoolLevel(toJsonOrNullString(createReqVO.getSchoolLevels()));
        userIntention.setMajorIds(toJsonOrNullLong(createReqVO.getMajorIds()));
        userIntentionMapper.insert(userIntention);
        return userIntention.getId();
    }

    @Override
    public void updateUserIntention(UserIntentionSaveReqVO updateReqVO) {
        validateUserIntentionExists(updateReqVO.getId());
        UserIntentionDO updateObj = BeanUtils.toBean(updateReqVO, UserIntentionDO.class);
        updateObj.setProvinceCodes(toJsonOrNullString(updateReqVO.getProvinceCodes()));
        updateObj.setExcludeProvinceCodes(toJsonOrNullString(updateReqVO.getExcludeProvinceCodes()));
        updateObj.setSchoolLevel(toJsonOrNullString(updateReqVO.getSchoolLevels()));
        updateObj.setMajorIds(toJsonOrNullLong(updateReqVO.getMajorIds()));
        userIntentionMapper.updateById(updateObj);
    }

    @Override
    public void deleteUserIntention(Long id) {
        validateUserIntentionExists(id);
        userIntentionMapper.deleteById(id);
    }

    private void validateUserIntentionExists(Long id) {
        if (userIntentionMapper.selectById(id) == null) {
            throw exception(new ErrorCode(404, "用户调剂意向不存在"));
        }
    }

    @Override
    public UserIntentionDO getUserIntention(Long id) {
        return userIntentionMapper.selectById(id);
    }

    @Override
    public PageResult<UserIntentionDO> getUserIntentionPage(UserIntentionPageReqVO pageReqVO) {
        return userIntentionMapper.selectPage(pageReqVO, new LambdaQueryWrapperX<UserIntentionDO>()
                .eqIfPresent(UserIntentionDO::getUserId, pageReqVO.getUserId()));
    }

    @Override
    public UserIntentionDO getUserIntentionByUserId(Long userId) {
        return userIntentionMapper.selectOne(new LambdaQueryWrapperX<UserIntentionDO>()
                .eq(UserIntentionDO::getUserId, userId));
    }

    @Override
    public AppUserIntentionRespVO getMyUserIntention(Long userId) {
        vipBenefitService.checkEnabledOrThrow(userId, BENEFIT_KEY_USER_INTENTION);
        UserIntentionDO userIntention = getUserIntentionByUserId(userId);
        if (userIntention == null) {
            return null;
        }
        AppUserIntentionRespVO respVO = new AppUserIntentionRespVO();
        respVO.setId(userIntention.getId());
        respVO.setUserId(userIntention.getUserId());
        respVO.setProvinceCodes(parseJsonStringList(userIntention.getProvinceCodes()));
        respVO.setExcludeProvinceCodes(parseJsonStringList(userIntention.getExcludeProvinceCodes()));
        respVO.setSchoolLevels(parseJsonStringList(userIntention.getSchoolLevel()));
        respVO.setMajorIds(parseJsonLongList(userIntention.getMajorIds()));
        respVO.setStudyMode(userIntention.getStudyMode());
        respVO.setDegreeType(userIntention.getDegreeType());
        respVO.setIsSpecialPlan(userIntention.getIsSpecialPlan());
        respVO.setIsAcceptResearchInst(userIntention.getIsAcceptResearchInst());
        respVO.setIsAcceptCrossMajor(userIntention.getIsAcceptCrossMajor());
        respVO.setIsAcceptCrossExam(userIntention.getIsAcceptCrossExam());
        respVO.setAdjustPriority(userIntention.getAdjustPriority());
        respVO.setCreateTime(userIntention.getCreateTime());
        return respVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveUserIntentionByUserId(Long userId, AppUserIntentionSaveReqVO reqVO) {
        vipBenefitService.checkEnabledOrThrow(userId, BENEFIT_KEY_USER_INTENTION);
        UserIntentionDO existing = getUserIntentionByUserId(userId);
        UserIntentionDO toSave = new UserIntentionDO();
        toSave.setUserId(userId);
        toSave.setProvinceCodes(toJsonOrNullString(reqVO.getProvinceCodes()));
        toSave.setExcludeProvinceCodes(toJsonOrNullString(reqVO.getExcludeProvinceCodes()));
        toSave.setSchoolLevel(toJsonOrNullString(reqVO.getSchoolLevels()));
        toSave.setMajorIds(toJsonOrNullLong(reqVO.getMajorIds()));
        toSave.setStudyMode(reqVO.getStudyMode());
        toSave.setDegreeType(reqVO.getDegreeType());
        toSave.setIsSpecialPlan(reqVO.getIsSpecialPlan());
        toSave.setIsAcceptResearchInst(reqVO.getIsAcceptResearchInst());
        toSave.setIsAcceptCrossMajor(reqVO.getIsAcceptCrossMajor());
        toSave.setIsAcceptCrossExam(reqVO.getIsAcceptCrossExam());
        toSave.setAdjustPriority(reqVO.getAdjustPriority());

        if (existing == null) {
            toSave.setId(null);
            userIntentionMapper.insert(toSave);
            return toSave.getId();
        }

        toSave.setId(existing.getId());
        userIntentionMapper.updateById(toSave);
        return existing.getId();
    }

    private String toJsonOrNullLong(List<Long> list) {
        if (list == null) {
            return null;
        }
        List<Long> cleaned = new ArrayList<>(list.size());
        Set<Long> dedup = new HashSet<>();
        for (Long item : list) {
            if (item == null) {
                continue;
            }
            if (dedup.add(item)) {
                cleaned.add(item);
            }
        }
        if (cleaned.isEmpty()) {
            return null;
        }
        return JSONUtil.toJsonStr(cleaned);
    }

    private String toJsonOrNullString(List<String> list) {
        if (list == null) {
            return null;
        }
        List<String> cleaned = new ArrayList<>(list.size());
        Set<String> dedup = new HashSet<>();
        for (String item : list) {
            if (item == null) {
                continue;
            }
            String s = item.trim();
            if (s.isEmpty()) {
                continue;
            }
            if (dedup.add(s)) {
                cleaned.add(s);
            }
        }
        if (cleaned.isEmpty()) {
            return null;
        }
        return JSONUtil.toJsonStr(cleaned);
    }

    private List<String> parseJsonStringList(String json) {
        if (StrUtil.isBlank(json)) {
            return Collections.emptyList();
        }
        try {
            List<Object> raw = JSONUtil.parseArray(json).toList(Object.class);
            if (raw == null || raw.isEmpty()) {
                return Collections.emptyList();
            }
            List<String> result = new ArrayList<>(raw.size());
            for (Object item : raw) {
                if (item == null) {
                    continue;
                }
                String s = String.valueOf(item).trim();
                if (!s.isEmpty()) {
                    result.add(s);
                }
            }
            return result;
        } catch (Exception ignore) {
            return Collections.emptyList();
        }
    }

    private List<Long> parseJsonLongList(String json) {
        if (StrUtil.isBlank(json)) {
            return Collections.emptyList();
        }
        try {
            List<Object> raw = JSONUtil.parseArray(json).toList(Object.class);
            if (raw == null || raw.isEmpty()) {
                return Collections.emptyList();
            }
            List<Long> result = new ArrayList<>(raw.size());
            for (Object item : raw) {
                if (item == null) {
                    continue;
                }
                try {
                    result.add(Long.parseLong(String.valueOf(item)));
                } catch (NumberFormatException ignore) {
                    // ignore invalid items from historical data
                }
            }
            return result;
        } catch (Exception ignore) {
            return Collections.emptyList();
        }
    }

}

