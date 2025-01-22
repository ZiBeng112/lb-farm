package com.farm.shop.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.farm.common.utils.bean.BeanUtils;
import com.farm.shop.domain.Dto.MyFarmMerchantAddDto;
import com.farm.shop.domain.Dto.MyFarmMerchantDto;

import com.farm.shop.domain.FarmMerchant;
import com.farm.shop.domain.Vo.MyFarmMerchantVo;
import com.farm.shop.mapper.FarmMerchantMapper;
import com.farm.shop.service.FarmMerchantService;
import lombok.Data;
import org.springframework.stereotype.Service;

/**
* @author xxplhl
* @description 针对表【farm_merchant(商户信息表)】的数据库操作Service实现
* @createDate 2025-01-20 16:34:21
*/
@Service
@Data
public class FarmMerchantServiceImpl extends ServiceImpl<FarmMerchantMapper, FarmMerchant>
    implements FarmMerchantService {

    private final FarmMerchantMapper farmMerchantMapper;

    @Override
    public MyFarmMerchantDto getInfo(Long userId) {

        MyFarmMerchantDto myFarmMerchantDto = farmMerchantMapper.selectInfoById(userId);
        return myFarmMerchantDto;

    }

    @Override
    public void addOrUpdateShopInfo(MyFarmMerchantAddDto myFarmMerchantAddDto) {

        myFarmMerchantAddDto.setProvince(myFarmMerchantAddDto.getAddressObject().getProvince());
        myFarmMerchantAddDto.setCity(myFarmMerchantAddDto.getAddressObject().getCity());
        myFarmMerchantAddDto.setDistrict(myFarmMerchantAddDto.getAddressObject().getDistrict());


        FarmMerchant farmMerchant = new FarmMerchant();
        BeanUtils.copyBeanProp(farmMerchant,myFarmMerchantAddDto);

        this.saveOrUpdate(farmMerchant);
//        try {
//
//        }catch (Exception e){
//            throw new RuntimeException("店铺名字重复");
//        }


        return;


    }
}




