package com.farm.web.controller.shop;

import com.farm.common.core.controller.BaseController;
import com.farm.common.core.domain.AjaxResult;
import com.farm.common.utils.bean.BeanUtils;
import com.farm.shop.domain.Dto.MyFarmMerchantAddDto;
import com.farm.shop.domain.Vo.MyFarmMerchantVo;
import com.farm.shop.service.FarmMerchantService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(value = "我的店铺", tags = "我的店铺")
@RestController
@RequestMapping("/shop/myShop")
@Data
public class MyShopController extends BaseController {


    private final FarmMerchantService farmMerchantService;

    // todo 获取店铺信息
    // todo 添加or修改店铺信息

    @GetMapping("/getShopInfo")
    @ApiOperation("获取店铺信息")
    @PreAuthorize("@ss.hasPermi('myshop:myshop:list')")
    public AjaxResult getShopInfo(){


        MyFarmMerchantVo myFarmMerchantVo = new MyFarmMerchantVo();
        BeanUtils.copyProperties(farmMerchantService.getInfo(this.getUserId()),myFarmMerchantVo);

        return AjaxResult.success(
                myFarmMerchantVo
        );
    }

    @PostMapping("/addShopInfo")
    @ApiOperation("添加or修改店铺信息")
    @PreAuthorize("@ss.hasPermi('myshop:myshop:list')")
    public AjaxResult addShopInfo(@RequestBody MyFarmMerchantAddDto myFarmMerchantAddDto){

        myFarmMerchantAddDto.setUserId(this.getUserId());

        farmMerchantService.addOrUpdateShopInfo(myFarmMerchantAddDto);


        return AjaxResult.success();
    }





}
