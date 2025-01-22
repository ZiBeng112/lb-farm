package com.farm.shop.domain.Dto;

import com.farm.shop.domain.FarmMerchant;
import lombok.Data;

@Data
public class MyFarmMerchantAddDto extends FarmMerchant {

    addressObjectDto addressObject;

}
