package com.farm.web.controller.goods;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.farm.common.core.controller.BaseController;
import com.farm.common.core.domain.AjaxResult;
import com.farm.common.core.domain.entity.SysUser;
import com.farm.common.core.page.TableDataInfo;
import com.farm.common.utils.poi.ExcelUtil;
import com.farm.goods.domain.FarmEquipment;
import com.farm.goods.domain.dto.CategoryListDto;
import com.farm.goods.domain.dto.EquipmentBatchDto;
import com.farm.goods.domain.dto.EquipmentListDto;
import com.farm.goods.domain.vo.EquipmentListVo;
import com.farm.goods.service.FarmEquipmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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

        TableDataInfo dataTable = this.getDataTable(list);

        return AjaxResult.success(dataTable);

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

    @ApiOperation("添加或修改商品")
    @PreAuthorize("@ss.hasPermi('admin:goods:list')")
    @PostMapping("/saveOrUpdate")
    public AjaxResult addEquipment(@RequestBody FarmEquipment farmEquipment) {

      farmEquipment.setOwnerId(this.getUserId());

      if( farmEquipment.getId() != null){
          farmEquipment.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
      }
      farmEquipment.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

      return AjaxResult.success(
                farmEquipmentService.saveOrUpdate(farmEquipment)
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

    // todo 导入 导出 批量删除 批量上下架

    @ApiOperation("批量删除商品")
    @PreAuthorize("@ss.hasPermi('admin:goods:list')")
    @DeleteMapping("/deleteByIds")
    public AjaxResult deleteByIds(@RequestBody EquipmentBatchDto data){
        return AjaxResult.success(
                farmEquipmentService.removeByIds(data.getIds())
        );
    }

    @ApiOperation("批量上下架商品")
    @PreAuthorize("@ss.hasPermi('admin:goods:list')")
    @PutMapping("/updateStatusByIds")
    public AjaxResult updateStatusByIds(@RequestBody EquipmentBatchDto data){

        LambdaUpdateWrapper<FarmEquipment> set = Wrappers.lambdaUpdate(FarmEquipment.class)
                .in(FarmEquipment::getId, data.getIds())
                .set(FarmEquipment::getStatus, data.getStatus());
        farmEquipmentService.update(set);

        return AjaxResult.success();


    }

    @ApiOperation("导出商品")
    @PreAuthorize("@ss.hasPermi('admin:goods:list')")
    @PostMapping ("/export")
    public void export(HttpServletResponse response, EquipmentListDto equipmentListDto) {

        equipmentListDto.setOwnerId(this.getUserId());

        List<EquipmentListVo> list = farmEquipmentService.getList(null, equipmentListDto);
        ExcelUtil<EquipmentListVo> util = new ExcelUtil<EquipmentListVo>(EquipmentListVo.class);
        util.exportExcel(response, list, "用户数据");

    }





}
