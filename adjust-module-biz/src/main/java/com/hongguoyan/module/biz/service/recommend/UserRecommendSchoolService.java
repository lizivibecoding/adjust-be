package com.hongguoyan.module.biz.service.recommend;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.controller.admin.recommend.school.vo.UserRecommendSchoolPageReqVO;
import com.hongguoyan.module.biz.dal.dataobject.recommend.UserRecommendSchoolDO;

/**
 * 推荐院校专业 Service 接口
 *
 * @author hgy
 */
public interface UserRecommendSchoolService {

    /**
     * 获得推荐院校专业分页
     *
     * @param pageReqVO 分页查询
     * @return 推荐院校专业分页
     */
    PageResult<UserRecommendSchoolDO> getUserRecommendSchoolPage(UserRecommendSchoolPageReqVO pageReqVO);

}
