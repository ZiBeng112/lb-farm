package com.farm.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.farm.common.core.domain.entity.SysUser;
import com.farm.common.exception.GlobalException;
import com.farm.goods.domain.FarmCategories;
import com.farm.goods.domain.dto.CategoryListDto;
import com.farm.goods.mapper.FarmCategoriesMapper;
import com.farm.goods.service.FarmCategoriesService;
import org.springframework.stereotype.Service;

import java.util.*;

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
     *
     * @param categoryListDto
     */
    @Override
    public List<FarmCategories> selectCategoryTree(CategoryListDto categoryListDto) {

        // 先查出来所有匹配的分类
        LambdaQueryWrapper<FarmCategories> eq = Wrappers.lambdaQuery(FarmCategories.class)
                .eq(FarmCategories::getUserId, categoryListDto.getUserId())
                .like(categoryListDto.getName() != null, FarmCategories::getName, categoryListDto.getName())
                .orderByAsc(FarmCategories::getSortOrder);

        List<FarmCategories> list = this.list(eq);

        // 查询匹配的分类所有子类
        // todo 优化 可以其父类也加入进去
        HashSet<FarmCategories> farmCategoriesSet = new HashSet<>();

        if (categoryListDto.getName() != null&&!(categoryListDto.getName().isEmpty())) {
            list.forEach(
                    item -> {
                        HashSet<FarmCategories> farmCategories = selectListByParentId(item.getId());
                        farmCategoriesSet.addAll(farmCategories);
                    }
            );

            // 找到其父类 加入列表

            FarmCategories farmCategories = new FarmCategories();


            Iterator<FarmCategories> iterator = farmCategoriesSet.iterator();
            while (iterator.hasNext()) {
                FarmCategories next = iterator.next();
                break;
            }


            while(farmCategories.getParentId() != 0){
                farmCategories = this.getById(farmCategories.getParentId());
                farmCategoriesSet.add(farmCategories);
            }

        }
        list.addAll(farmCategoriesSet);


        return list;


    }

    @Override
    public FarmCategories qureyCategory(Long id) {

        FarmCategories byId = this.getById(id);
        if (byId != null) {
            return byId;
        } else {
            throw new GlobalException("分类不存在");
        }
    }

    @Override
    public String selectAllCategoryById(Long id) {

        StringJoiner sj = new StringJoiner("->");

        FarmCategories farmCategory = this.getById(id);

        if(farmCategory.getParentId()!=0){
            sj.add(this.selectAllCategoryById(farmCategory.getParentId()));
            sj.add(farmCategory.getName());
        }else {
            return farmCategory.getName();
        }

        return sj.toString();
    }

    private HashSet<FarmCategories> selectListByParentId(Long parentId) {

        HashSet<FarmCategories> farmCategories = new HashSet<>();

        List<FarmCategories> list = this.list(Wrappers.lambdaQuery(FarmCategories.class).eq(FarmCategories::getParentId, parentId));

        list.forEach(
                item -> {
                    farmCategories.add(item);
                    this.selectListByParentId(item.getId());
                }
        );

        return farmCategories;


    }

}




