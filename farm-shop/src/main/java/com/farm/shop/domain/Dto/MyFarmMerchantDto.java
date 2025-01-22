package com.farm.shop.domain.Dto;

import com.farm.shop.domain.FarmMerchant;
import lombok.Data;

@Data
public class MyFarmMerchantDto extends FarmMerchant {


    private String provinceName;
    private String cityName;
    private String districtName;

}
