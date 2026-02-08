package com.hongguoyan.module.biz.service.recommend;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.admin.recommend.school.vo.UserRecommendSchoolPageReqVO;
import com.hongguoyan.module.biz.dal.dataobject.recommend.UserRecommendSchoolDO;
import com.hongguoyan.module.biz.dal.mysql.recommend.UserRecommendSchoolMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 推荐院校专业 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class UserRecommendSchoolServiceImpl implements UserRecommendSchoolService {

    @Resource
    private UserRecommendSchoolMapper userRecommendSchoolMapper;

    @Override
    public PageResult<UserRecommendSchoolDO> getUserRecommendSchoolPage(UserRecommendSchoolPageReqVO pageReqVO) {
        return userRecommendSchoolMapper.selectPage(pageReqVO, new LambdaQueryWrapperX<UserRecommendSchoolDO>()
                .eqIfPresent(UserRecommendSchoolDO::getUserId, pageReqVO.getUserId())
                .likeIfPresent(UserRecommendSchoolDO::getSchoolName, pageReqVO.getSchoolName())
                .likeIfPresent(UserRecommendSchoolDO::getMajorName, pageReqVO.getMajorName())
                .eqIfPresent(UserRecommendSchoolDO::getCategory, pageReqVO.getCategory())
                .orderByDesc(UserRecommendSchoolDO::getId));
    }

}
