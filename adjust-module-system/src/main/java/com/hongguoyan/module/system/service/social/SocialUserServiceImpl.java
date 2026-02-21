package com.hongguoyan.module.system.service.social;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.hongguoyan.framework.common.exception.ServiceException;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.system.api.social.dto.SocialUserBindReqDTO;
import com.hongguoyan.module.system.api.social.dto.SocialUserRespDTO;
import com.hongguoyan.module.system.controller.admin.socail.vo.user.SocialUserPageReqVO;
import com.hongguoyan.module.system.dal.dataobject.social.SocialUserBindDO;
import com.hongguoyan.module.system.dal.dataobject.social.SocialUserDO;
import com.hongguoyan.module.system.dal.mysql.social.SocialUserBindMapper;
import com.hongguoyan.module.system.dal.mysql.social.SocialUserMapper;
import com.hongguoyan.module.system.enums.social.SocialTypeEnum;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertSet;
import static com.hongguoyan.framework.common.util.json.JsonUtils.parseObjectQuietly;
import static com.hongguoyan.framework.common.util.json.JsonUtils.toJsonString;
import static com.hongguoyan.module.system.enums.ErrorCodeConstants.SOCIAL_USER_NOT_FOUND;

/**
 * 社交用户 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class SocialUserServiceImpl implements SocialUserService {

    @Resource
    private SocialUserBindMapper socialUserBindMapper;
    @Resource
    private SocialUserMapper socialUserMapper;

    @Resource
    private SocialClientService socialClientService;

    @Override
    public List<SocialUserDO> getSocialUserList(Long userId, Integer userType) {
        // 获得绑定
        List<SocialUserBindDO> socialUserBinds = socialUserBindMapper.selectListByUserIdAndUserType(userId, userType);
        if (CollUtil.isEmpty(socialUserBinds)) {
            return Collections.emptyList();
        }
        // 获得社交用户
        return socialUserMapper.selectByIds(convertSet(socialUserBinds, SocialUserBindDO::getSocialUserId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String bindSocialUser(SocialUserBindReqDTO reqDTO) {
        // 获得社交用户
        SocialUserDO socialUser = authSocialUser(reqDTO.getSocialType(), reqDTO.getUserType(),
                reqDTO.getCode(), reqDTO.getState());
        Assert.notNull(socialUser, "社交用户不能为空");

        // 社交用户可能之前绑定过别的用户，需要进行解绑
        socialUserBindMapper.deleteByUserTypeAndSocialUserId(reqDTO.getUserType(), socialUser.getId());

        // 用户可能之前已经绑定过该社交类型，需要进行解绑
        socialUserBindMapper.deleteByUserTypeAndUserIdAndSocialType(reqDTO.getUserType(), reqDTO.getUserId(),
                socialUser.getType());

        // 绑定当前登录的社交用户
        SocialUserBindDO socialUserBind = SocialUserBindDO.builder()
                .userId(reqDTO.getUserId()).userType(reqDTO.getUserType())
                .socialUserId(socialUser.getId()).socialType(socialUser.getType()).build();
        socialUserBindMapper.insert(socialUserBind);
        return socialUser.getOpenid();
    }

    @Override
    public void unbindSocialUser(Long userId, Integer userType, Integer socialType, String openid) {
        // 获得 openid 对应的 SocialUserDO 社交用户
        SocialUserDO socialUser = socialUserMapper.selectByTypeAndOpenid(socialType, openid);
        if (socialUser == null) {
            throw exception(SOCIAL_USER_NOT_FOUND);
        }

        // 获得对应的社交绑定关系
        socialUserBindMapper.deleteByUserTypeAndUserIdAndSocialType(userType, userId, socialUser.getType());
    }

    @Override
    public SocialUserRespDTO getSocialUserByUserId(Integer userType, Long userId, Integer socialType) {
        // 获得绑定用户
        SocialUserBindDO socialUserBind = socialUserBindMapper.selectByUserIdAndUserTypeAndSocialType(userId, userType, socialType);
        if (socialUserBind == null) {
            return null;
        }
        // 获得社交用户
        SocialUserDO socialUser = socialUserMapper.selectById(socialUserBind.getSocialUserId());
        Assert.notNull(socialUser, "社交用户不能为空");
        return new SocialUserRespDTO(socialUser.getOpenid(), socialUser.getNickname(), socialUser.getAvatar(),
                socialUserBind.getUserId());
    }

    @Override
    public SocialUserRespDTO getSocialUserByCode(Integer userType, Integer socialType, String code, String state) {
        // 获得社交用户
        SocialUserDO socialUser = authSocialUser(socialType, userType, code, state);
        Assert.notNull(socialUser, "社交用户不能为空");

        // 获得绑定用户
        SocialUserBindDO socialUserBind = socialUserBindMapper.selectByUserTypeAndSocialUserId(userType,
                socialUser.getId());
        return new SocialUserRespDTO(socialUser.getOpenid(), socialUser.getNickname(), socialUser.getAvatar(),
                socialUserBind != null ? socialUserBind.getUserId() : null);
    }

    /**
     * 授权获得对应的社交用户
     * 如果授权失败，则会抛出 {@link ServiceException} 异常
     *
     * @param socialType 社交平台的类型 {@link SocialTypeEnum}
     * @param userType 用户类型
     * @param code     授权码
     * @param state    state
     * @return 授权用户
     */
    @NotNull
    public SocialUserDO authSocialUser(Integer socialType, Integer userType, String code, String state) {
        // 优先从 DB 中获取，因为 code 有且可以使用一次。
        // 在社交登录时，当未绑定 User 时，需要绑定登录，此时需要 code 使用两次
        SocialUserDO socialUser = socialUserMapper.selectByTypeAndCodeAnState(socialType, code, state);
        if (socialUser != null) {
            return socialUser;
        }

        // 请求获取
        AuthUser authUser = socialClientService.getAuthUser(socialType, userType, code, state);
        Assert.notNull(authUser, "三方用户不能为空");

        // 保存到 DB 中
        socialUser = socialUserMapper.selectByTypeAndOpenid(socialType, authUser.getUuid());
        if (socialUser == null) {
            socialUser = new SocialUserDO();
        }
        // 仅在「本次拿到 unionId」且「DB 尚未存 unionId」时进行补齐，避免影响既有业务逻辑
        String unionId = getUnionId(authUser);
        if (StrUtil.isNotBlank(unionId) && StrUtil.isBlank(socialUser.getUnionId())) {
            socialUser.setUnionId(unionId);
        }
        socialUser.setType(socialType).setCode(code).setState(state) // 需要保存 code + state 字段，保证后续可查询
                .setOpenid(authUser.getUuid()).setToken(authUser.getToken().getAccessToken()).setRawTokenInfo((toJsonString(authUser.getToken())))
                .setNickname(authUser.getNickname()).setAvatar(authUser.getAvatar()).setRawUserInfo(toJsonString(authUser.getRawUserInfo()));
        if (socialUser.getId() == null) {
            socialUserMapper.insert(socialUser);
        } else {
            socialUser.clean(); // 避免 updateTime 不更新：https://gitee.com/adjustcode/adjust-boot-mini/issues/ID7FUL
            socialUserMapper.updateById(socialUser);
        }
        return socialUser;
    }

    private String getUnionId(AuthUser authUser) {
        if (authUser == null) {
            return null;
        }
        // 1) 优先从 token 里取（JustAuth 的 AuthToken 常带 unionId 字段）
        if (authUser.getToken() != null) {
            Object token = authUser.getToken();
            Object value = invokeQuietly(token, "getUnionId");
            if (value == null) {
                value = getFieldQuietly(token, "unionId");
            }
            if (value != null && StrUtil.isNotBlank(String.valueOf(value))) {
                return String.valueOf(value);
            }
        }
        // 2) 再从 rawUserInfo 里取（不同渠道/实现可能会放在这里）
        Object rawUserInfo = authUser.getRawUserInfo();
        String unionId = getUnionIdFromRawUserInfo(rawUserInfo);
        return StrUtil.isBlank(unionId) ? null : unionId;
    }

    @SuppressWarnings("unchecked")
    private String getUnionIdFromRawUserInfo(Object rawUserInfo) {
        if (rawUserInfo == null) {
            return null;
        }
        if (rawUserInfo instanceof Map) {
            return getUnionIdFromMap((Map<String, Object>) rawUserInfo);
        }
        if (rawUserInfo instanceof String) {
            Map<String, Object> map = parseObjectQuietly((String) rawUserInfo,
                    new TypeReference<Map<String, Object>>() {});
            return map != null ? getUnionIdFromMap(map) : null;
        }
        // 兜底：尝试反射读取 unionId/unionid 字段
        Object value = getFieldQuietly(rawUserInfo, "unionId");
        if (value == null) {
            value = getFieldQuietly(rawUserInfo, "unionid");
        }
        return value != null ? String.valueOf(value) : null;
    }

    private String getUnionIdFromMap(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Object value = map.get("unionid");
        if (value == null) {
            value = map.get("unionId");
        }
        return value != null ? String.valueOf(value) : null;
    }

    private Object invokeQuietly(Object obj, String methodName) {
        try {
            return ReflectUtil.invoke(obj, methodName);
        } catch (Exception ignored) {
            return null;
        }
    }

    private Object getFieldQuietly(Object obj, String fieldName) {
        try {
            return ReflectUtil.getFieldValue(obj, fieldName);
        } catch (Exception ignored) {
            return null;
        }
    }

    // ==================== 社交用户 CRUD ====================

    @Override
    public SocialUserDO getSocialUser(Long id) {
        return socialUserMapper.selectById(id);
    }

    @Override
    public PageResult<SocialUserDO> getSocialUserPage(SocialUserPageReqVO pageReqVO) {
        return socialUserMapper.selectPage(pageReqVO);
    }

}
