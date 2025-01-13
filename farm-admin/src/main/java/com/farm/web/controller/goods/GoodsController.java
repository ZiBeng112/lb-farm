package com.farm.web.controller.goods;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.farm.common.core.controller.BaseController;
import com.farm.common.core.domain.AjaxResult;
import com.farm.goods.domain.dto.EquipmentListDto;
import com.farm.goods.service.FarmEquipmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "商品管理")
@RequestMapping("/goods")
@Data
public class GoodsController extends BaseController {

    private final FarmEquipmentService farmEquipmentService;

    // todo 查询商品列表
    // todo 查询商品详情
    // todo 添加商品
    // todo 编辑商品
    // todo 删除商品
    // todo 上下架商品


    @ApiOperation("查询商品列表")
    @PreAuthorize("@ss.hasPermi('admin:goods:list')")
    @GetMapping("/list")
    public AjaxResult getList(IPage iPage, EquipmentListDto equipmentListDto){

        equipmentListDto.setOwnerId(this.getUserId());


        List list = farmEquipmentService.getList(iPage, equipmentListDto);

        return AjaxResult.success(list);
    }




}
