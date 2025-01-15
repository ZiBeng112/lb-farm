package com.farm.web.controller.goods;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.farm.common.core.controller.BaseController;
import com.farm.common.core.domain.AjaxResult;
import com.farm.goods.domain.FarmEquipment;
import com.farm.goods.domain.dto.CategoryListDto;
import com.farm.goods.domain.dto.EquipmentListDto;
import com.farm.goods.service.FarmEquipmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@Api(tags = "商品管理")
@RequestMapping("/equipment")
@Data
public class equipmentController extends BaseController {

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
    public AjaxResult getList(IPage iPage, EquipmentListDto equipmentListDto) {

        equipmentListDto.setOwnerId(this.getUserId());


        List list = farmEquipmentService.getList(iPage, equipmentListDto);

        return AjaxResult.success(list);

    }

    /**
     * 详情里面都有啥东西,有多表么
     * 暂时没有
     *
     * @param id
     * @return
     */
    @ApiOperation("查询商品详情")
    @PreAuthorize("@ss.hasPermi('admin:goods:list')")
    @GetMapping("/detail")
    public AjaxResult getEquipmentDetail(Long id) {

        return AjaxResult.success(
                farmEquipmentService.getById(id)
        );
    }

    @ApiOperation("添加商品")
    @PreAuthorize("@ss.hasPermi('admin:goods:list')")
    @PostMapping("/add")
    public AjaxResult addEquipment(@RequestBody FarmEquipment farmEquipment) {

      farmEquipment.setOwnerId(this.getUserId());
      farmEquipment.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
      farmEquipment.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

      return AjaxResult.success(
                farmEquipmentService.save(farmEquipment)
        );

    }

    @ApiOperation("编辑商品")
    @PreAuthorize("@ss.hasPermi('admin:goods:list')")
    @PutMapping("/edit")
    public AjaxResult editEquipment(@RequestBody FarmEquipment farmEquipment) {
        farmEquipment.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        return AjaxResult.success(
                farmEquipmentService.updateById(farmEquipment)
        );

    }

    @ApiOperation("删除商品")
    @PreAuthorize("@ss.hasPermi('admin:goods:list')")
    @DeleteMapping("/delete")
    public AjaxResult deleteEquipment(Long id) {
        return AjaxResult.success(
                farmEquipmentService.removeById(id)
        );
    }

    /**
     * 需要新接口么,可不可以与编辑共用
     * @param farmEquipment
     * @return
     */
    @ApiOperation("上下架商品")
    @PreAuthorize("@ss.hasPermi('admin:goods:list')")
    @PutMapping("/upDown")
    public AjaxResult upDownEquipment(@RequestBody FarmEquipment farmEquipment) {
        return AjaxResult.success(
                farmEquipmentService.updateById(farmEquipment)
        );
    }

    @ApiOperation("获取分类树")
    @PreAuthorize("@ss.hasPermi('admin:goods:list')")
    @GetMapping("/categoryTree")
    public AjaxResult categoryTree() {

        CategoryListDto categoryListDto = new CategoryListDto();
        categoryListDto.setUserId(this.getUserId());

        return AjaxResult.success(
                farmEquipmentService.getCategoryTree(categoryListDto)
        );

    }


}
