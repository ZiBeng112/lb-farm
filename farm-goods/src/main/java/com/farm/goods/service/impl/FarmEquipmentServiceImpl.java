package com.farm.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.farm.goods.domain.FarmEquipment;
import com.farm.goods.domain.dto.EquipmentListDto;
import com.farm.goods.service.FarmEquipmentService;
import com.farm.goods.mapper.FarmEquipmentMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author xxplhl
* @description 针对表【farm_equipment(农业设备表)】的数据库操作Service实现
* @createDate 2025-01-13 17:37:55
*/
@Service
public class FarmEquipmentServiceImpl extends ServiceImpl<FarmEquipmentMapper, FarmEquipment>
    implements FarmEquipmentService{


    @Override
    public List getList(IPage iPage, EquipmentListDto equipmentListDto) {

        // 返回本店的所有商品,设备

        LambdaQueryWrapper<FarmEquipment> eq = Wrappers.lambdaQuery(FarmEquipment.class)
                .eq(FarmEquipment::getOwnerId, equipmentListDto.getOwnerId());

        List list = this.list(iPage, eq);

        return list;

    }
}




