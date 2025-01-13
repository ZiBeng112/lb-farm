package com.farm.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.farm.common.core.domain.entity.SysUser;
import com.farm.common.exception.GlobalException;
import com.farm.goods.domain.FarmCategories;
import com.farm.goods.mapper.FarmCategoriesMapper;
import com.farm.goods.service.FarmCategoriesService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

/**
* @author xxplhl
* @description 针对表【farm-categories】的数据库操作Service实现
* @createDate 2025-01-11 17:06:49
*/
@Service
public class FarmCategoriesServiceImpl
        extends ServiceImpl<FarmCategoriesMapper, FarmCategories>
        implements FarmCategoriesService {


    /**
     * 获取对应用户的商品分类
     * @param userId
     */
    @Override
    public List<FarmCategories> selectCategoryTree(Long userId) {

        LambdaQueryWrapper<FarmCategories> eq = Wrappers.lambdaQuery(FarmCategories.class).eq(FarmCategories::getUserId, userId);

        List<FarmCategories> list = this.list(eq);

        return list;


    }

    @Override
    public FarmCategories qureyCategory(Long id) {

        FarmCategories byId = this.getById(id);
        if (byId != null) {
            return byId;
        }else {
            throw  new GlobalException("分类不存在");
        }
    }
}




