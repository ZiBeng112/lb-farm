package com.farm.web.controller.common;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.farm.common.core.controller.BaseController;
import com.farm.common.core.domain.AjaxResult;
import com.farm.common.web.domain.RcDistrict;
import com.farm.common.web.service.RcDistrictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "省市区管理")
@RestController
@RequestMapping("/common/province")
@Data
public class ProvinceController extends BaseController {


    private final RcDistrictService rcDistrictService;



    // TODO: 通过父获得全部子
    // todo: 通过id获取详细

    @GetMapping("/list")
    @ApiOperation(value = "获取省市区列表")
    public AjaxResult getList(Long id){

        return AjaxResult.success(rcDistrictService.list(
                Wrappers.lambdaQuery(RcDistrict.class)
                        .eq(RcDistrict::getPid, id)
        ));
    }



}
