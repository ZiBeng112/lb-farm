package com.farm.goods.domain.dto;

import lombok.Data;

import java.util.List;
@Data
public class EquipmentBatchDto {
    private List<Long> ids;
    private Integer status;
}
