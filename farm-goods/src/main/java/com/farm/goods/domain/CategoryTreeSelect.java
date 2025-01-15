package com.farm.goods.domain;

import com.farm.common.core.domain.TreeSelect;

import java.util.stream.Collectors;

public class CategoryTreeSelect extends TreeSelect {

    public CategoryTreeSelect(FarmCategories farmCategories) {
        this.setId(farmCategories.getId());
        this.setLabel(farmCategories.getName());
        this.setChildren(farmCategories.getChildren().stream().map(CategoryTreeSelect::new).collect(Collectors.toList()));
    }

}
