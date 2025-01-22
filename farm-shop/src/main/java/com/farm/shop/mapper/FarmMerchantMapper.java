package com.farm.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.farm.shop.domain.Dto.MyFarmMerchantDto;
import com.farm.shop.domain.FarmMerchant;


/**
* @author xxplhl
* @description 针对表【farm_merchant(商户信息表)】的数据库操作Mapper
* @createDate 2025-01-20 16:34:21
* @Entity generator.domain.FarmMerchant
*/
public interface FarmMerchantMapper extends BaseMapper<FarmMerchant> {

    MyFarmMerchantDto selectInfoById(Long userId);
}




