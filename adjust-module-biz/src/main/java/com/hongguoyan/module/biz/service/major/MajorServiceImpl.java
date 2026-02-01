package com.hongguoyan.module.biz.service.major;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.app.major.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.major.MajorDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.major.MajorMapper;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.common.exception.ErrorCode;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 专业 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class MajorServiceImpl implements MajorService {

    @Resource
    private MajorMapper majorMapper;

    @Override
    public Long createMajor(AppMajorSaveReqVO createReqVO) {
        // 插入
        MajorDO major = BeanUtils.toBean(createReqVO, MajorDO.class);
        majorMapper.insert(major);

        // 返回
        return major.getId();
    }

    @Override
    public void updateMajor(AppMajorSaveReqVO updateReqVO) {
        // 校验存在
        validateMajorExists(updateReqVO.getId());
        // 更新
        MajorDO updateObj = BeanUtils.toBean(updateReqVO, MajorDO.class);
        majorMapper.updateById(updateObj);
    }

    @Override
    public void deleteMajor(Long id) {
        // 校验存在
        validateMajorExists(id);
        // 删除
        majorMapper.deleteById(id);
    }

    @Override
        public void deleteMajorListByIds(List<Long> ids) {
        // 删除
        majorMapper.deleteByIds(ids);
        }


    private void validateMajorExists(Long id) {
        if (majorMapper.selectById(id) == null) {
            throw exception(MAJOR_NOT_EXISTS);
        }
    }

    @Override
    public MajorDO getMajor(Long id) {
        return majorMapper.selectById(id);
    }

    @Override
    public PageResult<MajorDO> getMajorPage(AppMajorPageReqVO pageReqVO) {
        return majorMapper.selectPage(pageReqVO);
    }

    @Override
    public List<AppMajorLevel1RespVO> getMajorLevel1List() {
        return majorMapper.selectLevel1List();
    }

    @Override
    public List<AppMajorChildRespVO> getMajorList(String parentCode, Integer level, Integer degreeType) {
        if (level == null || level < 1 || level > 3) {
            throw exception(new ErrorCode(400, "level must be 1, 2 or 3"));
        }
        if (level > 1 && StrUtil.isBlank(parentCode)) {
            throw exception(new ErrorCode(400, "parentCode is required when level > 1"));
        }

        String pc = level == 1 ? null : parentCode;
        // degreeType filter applies only to level=2/3, and 0/null means "no filter"
        Integer degreeTypeFilter = null;
        if ((level == 2 || level == 3) && degreeType != null && degreeType != 0) {
            degreeTypeFilter = degreeType;
        }
        List<MajorDO> children = majorMapper.selectListByLevelAndParentCode(pc, level, degreeTypeFilter);
        if (children == null || children.isEmpty()) {
            return Collections.emptyList();
        }

        // build hasChildren for level=1/2 only (level=3 is leaf)
        Set<String> hasChildParentCodes = Collections.emptySet();
        if (level < 3) {
            List<String> codes = new ArrayList<>(children.size());
            for (MajorDO item : children) {
                if (item != null && item.getCode() != null) {
                    codes.add(item.getCode());
                }
            }
            if (!codes.isEmpty()) {
                List<MajorDO> level3 = majorMapper.selectList(new LambdaQueryWrapperX<MajorDO>()
                        .select(MajorDO::getParentCode)
                        .in(MajorDO::getParentCode, codes)
                        .eq(MajorDO::getLevel, level + 1)
                        .eq(MajorDO::getDeleted, false)
                        .groupBy(MajorDO::getParentCode));
                if (level3 != null && !level3.isEmpty()) {
                    Set<String> set = new HashSet<>();
                    for (MajorDO item : level3) {
                        if (item != null && item.getParentCode() != null) {
                            set.add(item.getParentCode());
                        }
                    }
                    hasChildParentCodes = set;
                }
            }
        }

        List<AppMajorChildRespVO> result = new ArrayList<>(children.size());
        for (MajorDO item : children) {
            if (item == null) {
                continue;
            }
            AppMajorChildRespVO vo = new AppMajorChildRespVO();
            vo.setId(item.getId());
            vo.setCode(item.getCode());
            vo.setName(item.getName());
            vo.setLevel(level);
            vo.setDegreeType(item.getDegreeType());
            vo.setHasChildren(level < 3 && item.getCode() != null && hasChildParentCodes.contains(item.getCode()));
            result.add(vo);
        }
        return result;
    }

    @Override
    public List<AppMajorTreeNodeRespVO> getMajorTree() {
        // Only majors with code; sorted by code asc to keep stable tree order
        LambdaQueryWrapperX<MajorDO> qw = new LambdaQueryWrapperX<>();
        qw.select(MajorDO::getId, MajorDO::getCode, MajorDO::getName, MajorDO::getLevel,
                MajorDO::getDegreeType, MajorDO::getParentCode);
        qw.isNotNull(MajorDO::getCode);
        qw.ne(MajorDO::getCode, "");
        qw.eq(MajorDO::getDeleted, false);
        qw.orderByAsc(MajorDO::getCode);
        List<MajorDO> list = majorMapper.selectList(qw);
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        // build nodes by code
        Map<String, AppMajorTreeNodeRespVO> nodeByCode = new HashMap<>(list.size() * 2);
        Map<String, List<AppMajorTreeNodeRespVO>> childrenByParentCode = new HashMap<>();

        for (MajorDO item : list) {
            if (item == null || StrUtil.isBlank(item.getCode())) {
                continue;
            }
            AppMajorTreeNodeRespVO node = new AppMajorTreeNodeRespVO();
            node.setId(item.getId());
            node.setCode(item.getCode());
            node.setName(item.getName());
            node.setLevel(item.getLevel());
            node.setDegreeType(item.getDegreeType());
            node.setChildren(Collections.emptyList());
            nodeByCode.put(item.getCode(), node);

            String parentCode = item.getParentCode();
            if (StrUtil.isNotBlank(parentCode)) {
                childrenByParentCode.computeIfAbsent(parentCode, k -> new ArrayList<>()).add(node);
            }
        }

        // attach children
        for (Map.Entry<String, List<AppMajorTreeNodeRespVO>> entry : childrenByParentCode.entrySet()) {
            AppMajorTreeNodeRespVO parent = nodeByCode.get(entry.getKey());
            if (parent == null) {
                continue;
            }
            parent.setChildren(entry.getValue());
        }

        // roots: level=1, plus fallback nodes whose parent not found
        List<AppMajorTreeNodeRespVO> roots = new ArrayList<>();
        Set<String> nonRootCodes = new HashSet<>();
        for (List<AppMajorTreeNodeRespVO> children : childrenByParentCode.values()) {
            for (AppMajorTreeNodeRespVO child : children) {
                if (child != null && child.getCode() != null) {
                    nonRootCodes.add(child.getCode());
                }
            }
        }
        for (MajorDO item : list) {
            if (item == null || StrUtil.isBlank(item.getCode())) {
                continue;
            }
            AppMajorTreeNodeRespVO node = nodeByCode.get(item.getCode());
            if (node == null) {
                continue;
            }
            if (Integer.valueOf(1).equals(item.getLevel())) {
                roots.add(node);
                continue;
            }
            // if parent missing, treat as root to avoid losing nodes
            if (!nonRootCodes.contains(item.getCode()) && StrUtil.isNotBlank(item.getParentCode())
                    && !nodeByCode.containsKey(item.getParentCode())) {
                roots.add(node);
            }
        }
        return roots;
    }

}
