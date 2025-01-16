package com.farm.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.farm.goods.domain.FarmCategories;
import com.farm.goods.domain.dto.CategoryListDto;

import java.util.List;


/**
* @author xxplhl
* @description 针对表【farm-categories】的数据库操作Service
* @createDate 2025-01-11 17:06:49
*/
public interface FarmCategoriesService extends IService<FarmCategories> {

    List<FarmCategories> selectCategoryTree(CategoryListDto categoryListDto);

    FarmCategories qureyCategory(Long id);

    String selectAllCategoryById(Long id);

    List<FarmCategories> selectCategoryListById(Long id);
}
