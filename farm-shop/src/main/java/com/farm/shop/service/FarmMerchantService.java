package com.farm.shop.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.farm.shop.domain.Dto.MyFarmMerchantAddDto;
import com.farm.shop.domain.Dto.MyFarmMerchantDto;

import com.farm.shop.domain.FarmMerchant;
import com.farm.shop.domain.Vo.MyFarmMerchantVo;

/**
* @author xxplhl
* @description 针对表【farm_merchant(商户信息表)】的数据库操作Service
* @createDate 2025-01-20 16:34:21
*/
public interface FarmMerchantService extends IService<FarmMerchant> {

    MyFarmMerchantDto getInfo(Long userId);

    void addOrUpdateShopInfo(MyFarmMerchantAddDto myFarmMerchantAddDto);
}
