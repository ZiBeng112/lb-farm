package com.farm.goods.service.impl;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.farm.common.utils.bean.BeanUtils;
import com.farm.goods.domain.CategoryTreeSelect;
import com.farm.goods.domain.FarmCategories;
import com.farm.goods.domain.FarmEquipment;
import com.farm.goods.domain.dto.CategoryListDto;
import com.farm.goods.domain.dto.EquipmentListDto;
import com.farm.goods.domain.vo.EquipmentListVo;
import com.farm.goods.service.FarmCategoriesService;
import com.farm.goods.service.FarmEquipmentService;
import com.farm.goods.mapper.FarmEquipmentMapper;
import lombok.Data;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xxplhl
 * @description 针对表【farm_equipment(农业设备表)】的数据库操作Service实现
 * @createDate 2025-01-13 17:37:55
 */
@Service
@Data
public class FarmEquipmentServiceImpl extends ServiceImpl<FarmEquipmentMapper, FarmEquipment>
        implements FarmEquipmentService {

    private final FarmCategoriesService farmCategoriesService;


    @Override
    public List getList(IPage iPage, EquipmentListDto equipmentListDto) {

        // 返回本店的所有商品,设备,但是分页
        LambdaQueryWrapper<FarmEquipment> eq = Wrappers.lambdaQuery(FarmEquipment.class)
                .eq(FarmEquipment::getOwnerId, equipmentListDto.getOwnerId());

        List<FarmEquipment> list = this.list(iPage, eq);

        // 补充其全部种类,从树结构开始
        ArrayList<EquipmentListVo> equipmentListVos = new ArrayList<>();

        list.forEach(
                equipment -> {
                    EquipmentListVo equipmentListVo = new EquipmentListVo();
                    BeanUtils.copyProperties(equipment, equipmentListVo);

                    String All = farmCategoriesService.selectAllCategoryById(equipment.getCategoryId());
                    equipmentListVo.setCategoryAll(All);
                    equipmentListVos.add(equipmentListVo);


                }
        );


                    return equipmentListVos;

                }

    @Override
    public List<CategoryTreeSelect> getCategoryTree(CategoryListDto categoryListDto) {

        List<FarmCategories> list = farmCategoriesService.selectCategoryTree(categoryListDto);

        //获取集合后,将其变为树结构
        List<FarmCategories> farmCategories = FarmCategories.buildTree(list);


        return farmCategories.stream().map(CategoryTreeSelect::new).collect(Collectors.toList());

    }


}




