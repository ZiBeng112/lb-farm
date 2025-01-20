package com.farm.web.controller.shop;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.farm.common.core.controller.BaseController;
import com.farm.common.core.domain.AjaxResult;
import com.farm.myShop.domain.FarmMerchant;
import com.farm.myShop.service.FarmMerchantService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "我的店铺", tags = "我的店铺")
@RestController
@RequestMapping("/myShop")
@Data
public class MyShopController extends BaseController {


    private final FarmMerchantService farmMerchantService;

    // todo 获取店铺信息
    // todo 添加or修改店铺信息

    @GetMapping("/getShopInfo")
    @ApiOperation("获取店铺信息")
    @PreAuthorize("@ss.hasPermi('myshop:myshop:list')")
    public AjaxResult getShopInfo(){



        return AjaxResult.success(
                farmMerchantService.getOne(
                        Wrappers.lambdaQuery(FarmMerchant.class)
                                .eq(FarmMerchant::getUserId, this.getUserId())
                ));
    }

    @GetMapping("/addShopInfo")
    @ApiOperation("添加or修改店铺信息")
    @PreAuthorize("@ss.hasPermi('myshop:myshop:list')")
    public AjaxResult addShopInfo(){

        

        return AjaxResult.success();
    }





}
