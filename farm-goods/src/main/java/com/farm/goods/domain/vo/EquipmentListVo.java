package com.farm.goods.domain.vo;

import com.farm.common.annotation.Excel;
import com.farm.goods.domain.FarmEquipment;
import lombok.Data;

@Data
public class EquipmentListVo extends FarmEquipment {

    @Excel(name = "所属分类")
    private String categoryAll;
}
