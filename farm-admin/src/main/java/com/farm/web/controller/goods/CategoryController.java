package com.farm.web.controller.goods;


import com.farm.common.core.controller.BaseController;
import com.farm.common.core.domain.AjaxResult;
import com.farm.common.utils.bean.BeanUtils;
import com.farm.goods.domain.FarmCategories;
import com.farm.goods.domain.dto.CategoryAddDto;
import com.farm.goods.domain.dto.CategoryListDto;
import com.farm.goods.service.FarmCategoriesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import org.apache.ibatis.annotations.Delete;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Api(tags = "商品分类")
@RestController
@RequestMapping("/goods/category")
@Data
public class CategoryController extends BaseController {

    //todo 缺失功能,1.获得分类树
    //todo 缺失功能,2.增加删除修改分类
    //todo 缺失功能,3.获得单独分类的子树
    //todo 缺失功能,4.获得单独分类详情

    private final FarmCategoriesService categoryService;


    @PreAuthorize("@ss.hasPermi('goods:category:list')")
    @GetMapping("list")
    @ApiOperation("获得分类树")
    public AjaxResult list(CategoryListDto categoryListDto) {

        // 当前登录用户的商品分类信息
        categoryListDto.setUserId(getUserId());
        List list = categoryService.selectCategoryTree(categoryListDto);

        return AjaxResult.success(list);
    }

    @PreAuthorize("@ss.hasPermi('goods:category:list')")
    @GetMapping ("get")
    @ApiOperation("获得分类详情")
    public AjaxResult get(Long id) {

        return AjaxResult.success(
                categoryService.qureyCategory(id)
        );
    }

    @PostMapping("add")
    @PreAuthorize("@ss.hasPermi('goods:category:list')")
    @ApiOperation("增加分类")
    public AjaxResult add(@RequestBody FarmCategories farmCategories ) {

        //赋予当前时间
        if(farmCategories.getId()!=null){
            farmCategories.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        }

        farmCategories.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        farmCategories.setUserId(this.getUserId());


        return AjaxResult.success(
                categoryService.saveOrUpdate(farmCategories)
        );
    }


    @PreAuthorize("@ss.hasPermi('goods:category:list')")
    @DeleteMapping("delete")
    @ApiOperation("删除分类")
    public AjaxResult delete(Long id) {
        return AjaxResult.success(
                categoryService.removeById(id)
        );
    }



}
