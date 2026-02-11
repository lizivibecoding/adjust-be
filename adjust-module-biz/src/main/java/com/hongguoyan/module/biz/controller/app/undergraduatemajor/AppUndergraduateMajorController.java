package com.hongguoyan.module.biz.controller.app.undergraduatemajor;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.module.biz.controller.app.undergraduatemajor.vo.AppUndergraduateMajorTreeRespVO;
import com.hongguoyan.module.biz.dal.dataobject.undergraduatemajor.UndergraduateMajorDO;
import com.hongguoyan.module.biz.service.undergraduatemajor.UndergraduateMajorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "API - 学科专业")
@RestController
@RequestMapping("/biz/undergraduate-major")
@Validated
public class AppUndergraduateMajorController {

    @Resource
    private UndergraduateMajorService undergraduateMajorService;

    @GetMapping("/tree")
    @Operation(summary = "获取学科专业树形列表")
    public CommonResult<List<AppUndergraduateMajorTreeRespVO>> getUndergraduateMajorTree() {
        List<UndergraduateMajorDO> list = undergraduateMajorService.getUndergraduateMajorList();
        return success(buildTree(list));
    }

    private List<AppUndergraduateMajorTreeRespVO> buildTree(List<UndergraduateMajorDO> list) {
        // Group by categoryName, preserving insertion order
        Map<String, List<UndergraduateMajorDO>> categoryMap = list.stream()
                .collect(Collectors.groupingBy(UndergraduateMajorDO::getCategoryName, LinkedHashMap::new, Collectors.toList()));

        List<AppUndergraduateMajorTreeRespVO> tree = new ArrayList<>();
        for (Map.Entry<String, List<UndergraduateMajorDO>> categoryEntry : categoryMap.entrySet()) {
            AppUndergraduateMajorTreeRespVO categoryNode = new AppUndergraduateMajorTreeRespVO();
            categoryNode.setCategoryName(categoryEntry.getKey());

            // Group by typeName within category
            Map<String, List<UndergraduateMajorDO>> typeMap = categoryEntry.getValue().stream()
                    .collect(Collectors.groupingBy(UndergraduateMajorDO::getTypeName, LinkedHashMap::new, Collectors.toList()));

            List<AppUndergraduateMajorTreeRespVO.TypeNode> typeNodes = new ArrayList<>();
            for (Map.Entry<String, List<UndergraduateMajorDO>> typeEntry : typeMap.entrySet()) {
                AppUndergraduateMajorTreeRespVO.TypeNode typeNode = new AppUndergraduateMajorTreeRespVO.TypeNode();
                typeNode.setTypeName(typeEntry.getKey());

                List<AppUndergraduateMajorTreeRespVO.MajorNode> majorNodes = typeEntry.getValue().stream()
                        .map(m -> {
                            AppUndergraduateMajorTreeRespVO.MajorNode majorNode = new AppUndergraduateMajorTreeRespVO.MajorNode();
                            majorNode.setId(m.getId());
                            majorNode.setCode(m.getCode());
                            majorNode.setName(m.getName());
                            return majorNode;
                        })
                        .collect(Collectors.toList());
                typeNode.setMajors(majorNodes);
                typeNodes.add(typeNode);
            }
            categoryNode.setTypes(typeNodes);
            tree.add(categoryNode);
        }
        return tree;
    }

}
