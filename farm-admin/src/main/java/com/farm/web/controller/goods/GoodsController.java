package com.farm.web.controller.goods;

import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "商品管理")
@PreAuthorize("hasAuthority('admin:goods:list')")
public class GoodsController {

}
