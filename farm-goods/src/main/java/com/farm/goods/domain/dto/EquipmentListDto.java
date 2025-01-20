package com.farm.goods.domain.dto;

import com.farm.goods.domain.FarmEquipment;
import lombok.Data;

import java.util.List;

@Data
public class EquipmentListDto extends FarmEquipment {
    private List<Long> ids;
}
