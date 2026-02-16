package com.hongguoyan.module.biz.service.school;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.app.school.vo.*;
import com.hongguoyan.module.biz.controller.admin.school.vo.SchoolPageReqVO;
import com.hongguoyan.module.biz.controller.admin.school.vo.SchoolDetailRespVO;
import com.hongguoyan.module.biz.controller.admin.school.vo.SchoolRespVO;
import com.hongguoyan.module.biz.controller.admin.school.vo.SchoolUpdateReqVO;
import com.hongguoyan.module.biz.dal.dataobject.school.SchoolDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.school.SchoolMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import cn.hutool.core.util.StrUtil;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.infra.api.file.FileApi;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 院校 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class SchoolServiceImpl implements SchoolService {

    @Resource
    private SchoolMapper schoolMapper;
    @Resource
    private FileApi fileApi;

    @Override
    public Long createSchool(AppSchoolSaveReqVO createReqVO) {
        // 插入
        SchoolDO school = BeanUtils.toBean(createReqVO, SchoolDO.class);
        schoolMapper.insert(school);

        // 返回
        return school.getId();
    }

    @Override
    public void updateSchool(AppSchoolSaveReqVO updateReqVO) {
        // 校验存在
        validateSchoolExists(updateReqVO.getId());
        // 更新
        SchoolDO updateObj = BeanUtils.toBean(updateReqVO, SchoolDO.class);
        schoolMapper.updateById(updateObj);
    }

    @Override
    public void deleteSchool(Long id) {
        // 校验存在
        validateSchoolExists(id);
        // 删除
        schoolMapper.deleteById(id);
    }

    @Override
        public void deleteSchoolListByIds(List<Long> ids) {
        // 删除
        schoolMapper.deleteByIds(ids);
        }


    private void validateSchoolExists(Long id) {
        if (schoolMapper.selectById(id) == null) {
            throw exception(SCHOOL_NOT_EXISTS);
        }
    }

    @Override
    public SchoolDO getSchool(Long id) {
        return schoolMapper.selectById(id);
    }

    @Override
    public AppSchoolOverviewRespVO getSchoolOverview(Long schoolId) {
        SchoolDO school = schoolMapper.selectById(schoolId);
        if (school == null) {
            throw exception(SCHOOL_NOT_EXISTS);
        }
        return BeanUtils.toBean(school, AppSchoolOverviewRespVO.class);
    }

    @Override
    public PageResult<SchoolDO> getSchoolPage(AppSchoolPageReqVO pageReqVO) {
        return schoolMapper.selectPage(pageReqVO);
    }

    @Override
    public PageResult<SchoolRespVO> getSchoolAdminPage(SchoolPageReqVO pageReqVO) {
        PageResult<SchoolDO> pageResult = schoolMapper.selectPage(pageReqVO);
        if (pageResult == null || pageResult.getList() == null || pageResult.getList().isEmpty()) {
            return new PageResult<>(Collections.emptyList(), pageResult != null ? pageResult.getTotal() : 0);
        }

        List<SchoolRespVO> list = new ArrayList<>(pageResult.getList().size());
        for (SchoolDO school : pageResult.getList()) {
            if (school == null) {
                continue;
            }
            SchoolRespVO vo = BeanUtils.toBean(school, SchoolRespVO.class);
            // Build static URL from stored path/key (no presign)
            vo.setSchoolLogoUrl(fileApi.buildStaticUrl(school.getSchoolLogo()));
            list.add(vo);
        }
        return new PageResult<>(list, pageResult.getTotal());
    }

    @Override
    public SchoolDetailRespVO getSchoolAdmin(Long id) {
        SchoolDO school = schoolMapper.selectById(id);
        if (school == null) {
            throw exception(SCHOOL_NOT_EXISTS);
        }
        SchoolDetailRespVO vo = BeanUtils.toBean(school, SchoolDetailRespVO.class);
        vo.setSchoolLogoUrl(fileApi.buildStaticUrl(school.getSchoolLogo()));
        return vo;
    }

    @Override
    public void updateSchoolAdmin(SchoolUpdateReqVO updateReqVO) {
        validateSchoolExists(updateReqVO.getId());
        SchoolDO update = new SchoolDO();
        update.setId(updateReqVO.getId());
        update.setSchoolLogo(updateReqVO.getSchoolLogo());
        update.setSchoolType(updateReqVO.getSchoolType());
        update.setIsAcademy(updateReqVO.getIsAcademy());
        update.setIs985(updateReqVO.getIs985());
        update.setIs211(updateReqVO.getIs211());
        update.setIsSyl(updateReqVO.getIsSyl());
        update.setIsOrdinary(updateReqVO.getIsOrdinary());
        update.setIsZihuaxian(updateReqVO.getIsZihuaxian());
        update.setIntro(updateReqVO.getIntro());
        update.setSchoolAddress(updateReqVO.getSchoolAddress());
        update.setCreateYear(updateReqVO.getCreateYear());
        update.setSchoolSite(updateReqVO.getSchoolSite());
        update.setSchoolPhone(updateReqVO.getSchoolPhone());
        update.setSchoolEmail(updateReqVO.getSchoolEmail());
        schoolMapper.updateById(update);
    }

    @Override
    public List<AppSchoolSimpleOptionRespVO> getSchoolSimpleAll() {
        List<SchoolDO> list = schoolMapper.selectList(new LambdaQueryWrapper<SchoolDO>()
                .select(SchoolDO::getId, SchoolDO::getSchoolName)
                .eq(SchoolDO::getDeleted, false)
                .orderByAsc(SchoolDO::getId));
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        List<AppSchoolSimpleOptionRespVO> result = new ArrayList<>(list.size());
        for (SchoolDO item : list) {
            if (item == null) {
                continue;
            }
            AppSchoolSimpleOptionRespVO opt = new AppSchoolSimpleOptionRespVO();
            opt.setId(item.getId());
            opt.setName(StrUtil.blankToDefault(item.getSchoolName(), ""));
            result.add(opt);
        }
        return result;
    }

    @Override
    public List<AppSchoolTreeAreaRespVO> getSchoolTree() {
        LambdaQueryWrapperX<SchoolDO> qw = new LambdaQueryWrapperX<>();
        qw.select(SchoolDO::getId, SchoolDO::getSchoolName, SchoolDO::getProvinceArea,
                SchoolDO::getProvinceCode, SchoolDO::getProvinceName);
        // soft delete disabled: do NOT enforce deleted = 0
        qw.orderByAsc(SchoolDO::getProvinceArea)
                .orderByAsc(SchoolDO::getProvinceCode) // province sort by code
                .orderByAsc(SchoolDO::getId);
        List<SchoolDO> list = schoolMapper.selectList(qw);
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        // area(A/B) -> provinceCode -> provinceNode(name + schools)
        Map<String, Map<String, AppSchoolTreeAreaRespVO.ProvinceNode>> areaProvinceNodes = new LinkedHashMap<>();
        areaProvinceNodes.put("A", new LinkedHashMap<>());
        areaProvinceNodes.put("B", new LinkedHashMap<>());

        for (SchoolDO item : list) {
            if (item == null || item.getId() == null) {
                continue;
            }
            String area = normalizeArea(item.getProvinceArea());
            if (area == null) {
                continue;
            }
            String provinceCode = StrUtil.blankToDefault(item.getProvinceCode(), "");
            if (StrUtil.isBlank(provinceCode)) {
                provinceCode = "UNKNOWN";
            }
            String provinceName = StrUtil.blankToDefault(item.getProvinceName(), "");
            if (StrUtil.isBlank(provinceName)) {
                provinceName = "未知省份";
            }

            Map<String, AppSchoolTreeAreaRespVO.ProvinceNode> provinceNodes =
                    areaProvinceNodes.computeIfAbsent(area, k -> new LinkedHashMap<>());
            AppSchoolTreeAreaRespVO.ProvinceNode provinceNode = provinceNodes.get(provinceCode);
            if (provinceNode == null) {
                provinceNode = new AppSchoolTreeAreaRespVO.ProvinceNode();
                provinceNode.setName(provinceName);
                provinceNode.setSchools(new ArrayList<>());
                provinceNodes.put(provinceCode, provinceNode);
            }

            AppSchoolSimpleOptionRespVO opt = new AppSchoolSimpleOptionRespVO();
            opt.setId(item.getId());
            opt.setName(StrUtil.blankToDefault(item.getSchoolName(), ""));
            provinceNode.getSchools().add(opt);
        }

        List<AppSchoolTreeAreaRespVO> result = new ArrayList<>(areaProvinceNodes.size());
        for (Map.Entry<String, Map<String, AppSchoolTreeAreaRespVO.ProvinceNode>> areaEntry : areaProvinceNodes.entrySet()) {
            AppSchoolTreeAreaRespVO areaNode = new AppSchoolTreeAreaRespVO();
            areaNode.setArea(areaEntry.getKey());

            List<AppSchoolTreeAreaRespVO.ProvinceNode> provinces = new ArrayList<>(areaEntry.getValue().size());
            provinces.addAll(areaEntry.getValue().values());
            areaNode.setProvinces(provinces);
            result.add(areaNode);
        }
        return result;
    }

    private static String normalizeArea(String provinceArea) {
        if (StrUtil.isBlank(provinceArea)) {
            return null;
        }
        String s = provinceArea.trim().toUpperCase(Locale.ROOT);
        // allow "A", "A区", "A ZONE" etc.
        if ("A".equals(s) || s.contains("A")) {
            return "A";
        }
        if ("B".equals(s) || s.contains("B")) {
            return "B";
        }
        return null;
    }

}
