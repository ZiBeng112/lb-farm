package com.farm.goods.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 * @TableName farm-categories
 */
@TableName(value ="farm_categories")
@Data
public class FarmCategories implements Serializable{
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;


    private long userId;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private Long parentId;



    /**
     * 
     */
    private Integer sortOrder;

    /**
     * 
     */
    private Date createdAt;

    /**
     * 
     */
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}