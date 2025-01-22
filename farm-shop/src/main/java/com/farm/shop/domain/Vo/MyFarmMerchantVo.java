package com.farm.shop.domain.Vo;

import com.farm.shop.domain.FarmMerchant;
import lombok.Data;

@Data
public class MyFarmMerchantVo extends FarmMerchant {


    private String provinceName;
    private String cityName;
    private String districtName;

}
