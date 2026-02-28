package com.hongguoyan.module.biz.service.usercustomreport;

import com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil;
import com.hongguoyan.module.biz.service.projectconfig.ProjectConfigService;
import com.hongguoyan.module.biz.service.vipbenefit.VipBenefitService;
import com.hongguoyan.module.biz.service.vipbenefit.model.VipResolvedBenefit;
import com.hongguoyan.module.member.api.user.MemberUserApi;
import com.hongguoyan.module.member.api.user.dto.MemberUserRespDTO;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertSet;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.admin.recommend.report.vo.UserCustomReportPageReqVO;
import com.hongguoyan.module.biz.dal.dataobject.userprofile.UserProfileDO;
import com.hongguoyan.module.biz.dal.dataobject.usercustomreport.UserCustomReportDO;
import com.hongguoyan.module.biz.dal.mysql.userprofile.UserProfileMapper;
import com.hongguoyan.module.biz.dal.mysql.usercustomreport.UserCustomReportMapper;
import com.hongguoyan.module.biz.enums.ErrorCodeConstants;
import com.hongguoyan.module.biz.service.recommend.NationalLineContext;
import com.hongguoyan.module.biz.service.recommend.NationalLineEligibilityService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.VIP_BENEFIT_QUOTA_EXCEEDED;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.BENEFIT_KEY_USER_REPORT;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.BENEFIT_TYPE_QUOTA;

/**
 * 用户AI调剂定制报告 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class UserCustomReportServiceImpl implements UserCustomReportService {

    @Resource
    private UserCustomReportMapper userCustomReportMapper;
    @Resource
    private MemberUserApi memberUserApi;
    @Resource
    private UserProfileMapper userProfileMapper;
    @Resource
    private NationalLineEligibilityService nationalLineEligibilityService;

    @Resource
    private ProjectConfigService projectConfigService;

    @Resource
    private VipBenefitService vipBenefitService;

    @Override
    public UserCustomReportDO getLatestByUserId(Long userId) {
        UserCustomReportDO report = userCustomReportMapper.selectLatestByUserId(userId);
        fillDefaultReportNameIfMissing(report);
        return report;
    }

    @Override
    public UserCustomReportDO getByUserIdAndId(Long userId, Long reportId) {
        UserCustomReportDO report = userCustomReportMapper.selectByUserIdAndId(userId, reportId);
        fillDefaultReportNameIfMissing(report);
        return report;
    }

    @Override
    public List<UserCustomReportDO> listByUserId(Long userId) {
        List<UserCustomReportDO> list = userCustomReportMapper.selectListByUserId(userId);
        if (list != null) {
            list.forEach(this::fillDefaultReportNameIfMissing);
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateReportName(Long userId, Long reportId, String reportName) {
        if (userId == null || reportId == null) {
            throw exception(ErrorCodeConstants.CANDIDATE_CUSTOM_REPORTS_NOT_EXISTS);
        }
        int updated = userCustomReportMapper.updateReportNameByUserIdAndId(userId, reportId, reportName);
        if (updated <= 0) {
            throw exception(ErrorCodeConstants.CANDIDATE_CUSTOM_REPORTS_NOT_EXISTS);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateReportPdfUrl(Long userId, Long reportId, String pdfUrl) {
        if (userId == null || reportId == null) {
            return;
        }
        userCustomReportMapper.update(null, new LambdaUpdateWrapper<UserCustomReportDO>()
                .eq(UserCustomReportDO::getUserId, userId)
                .eq(UserCustomReportDO::getId, reportId)
                .set(UserCustomReportDO::getReportPdfUrl, pdfUrl));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createNewVersionByUserId(Long userId) {
        validateUserQualifiedForReport(userId);
        // 最终还是冲突，交由上层处理（会返回 500）
        UserCustomReportDO report = new UserCustomReportDO();
        report.setId(null);
        report.setUserId(userId);
        int reportNo = userCustomReportMapper.selectMaxReportNoByUserId(userId) + 1;
        report.setReportNo(reportNo);
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE); // yyyyMMdd
        report.setReportName(String.format("%s - 智能推荐调剂报告 - %02d", date, reportNo));
        report.setGenerateStatus(0); // 0-生成中
        userCustomReportMapper.insert(report);
        return report.getId();
    }

    private void validateUserQualifiedForReport(Long userId) {
        nationalLineEligibilityService
            .resolveContextOrThrow(userId, projectConfigService.getAdjustYear());
    }

    @Override
    public void updateGenerateStatus(Long reportId, Integer generateStatus) {
        userCustomReportMapper.update(null, new LambdaUpdateWrapper<UserCustomReportDO>()
                .eq(UserCustomReportDO::getId, reportId)
                .set(UserCustomReportDO::getGenerateStatus, generateStatus));
    }

    @Override
    public void deleteUserCustomReport(Long id) {
        if (userCustomReportMapper.selectById(id) == null) {
            throw exception(ErrorCodeConstants.CANDIDATE_CUSTOM_REPORTS_NOT_EXISTS);
        }
        userCustomReportMapper.deleteById(id);
    }

    @Override
    public PageResult<UserCustomReportDO> getUserCustomReportPage(UserCustomReportPageReqVO pageReqVO) {
        PageResult<UserCustomReportDO> pageResult = userCustomReportMapper.selectPage(pageReqVO, new LambdaQueryWrapperX<UserCustomReportDO>()
                .eqIfPresent(UserCustomReportDO::getUserId, pageReqVO.getUserId())
                .likeIfPresent(UserCustomReportDO::getReportName, pageReqVO.getReportName())
                .orderByDesc(UserCustomReportDO::getId));
        if (pageResult != null && CollUtil.isNotEmpty(pageResult.getList())) {
            // Fill default report name if missing
            pageResult.getList().forEach(this::fillDefaultReportNameIfMissing);

            // Fill user name
            Set<Long> userIds = convertSet(pageResult.getList(), UserCustomReportDO::getUserId);
            Map<Long, MemberUserRespDTO> userMap = memberUserApi.getUserMap(userIds);
            pageResult.getList().forEach(report -> {
                if (report.getUserId() != null && userMap.containsKey(report.getUserId())) {
                    report.setUserName(userMap.get(report.getUserId()).getNickname());
                }
            });
        }
        return pageResult;
    }

    private void fillDefaultReportNameIfMissing(UserCustomReportDO report) {
        if (report == null) {
            return;
        }
        if (report.getReportName() != null && !report.getReportName().isBlank()) {
            return;
        }
        LocalDate date = report.getCreateTime() != null ? report.getCreateTime().toLocalDate() : LocalDate.now();
        String ds = date.format(DateTimeFormatter.BASIC_ISO_DATE);
        int no = report.getReportNo() != null ? report.getReportNo() : 0;
        report.setReportName(String.format("%s - 调剂报告 - %02d", ds, no));
    }

}
