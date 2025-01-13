package com.farm.goods.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.farm.goods.domain.FarmEquipment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.farm.goods.domain.dto.EquipmentListDto;

import java.util.List;

/**
* @author xxplhl
* @description 针对表【farm_equipment(农业设备表)】的数据库操作Service
* @createDate 2025-01-13 17:37:55
*/
public interface FarmEquipmentService extends IService<FarmEquipment> {

    List getList(IPage iPage, EquipmentListDto equipmentListDto);
}
