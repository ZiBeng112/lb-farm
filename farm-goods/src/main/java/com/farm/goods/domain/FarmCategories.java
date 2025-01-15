package com.farm.goods.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.*;


/**
 * 
 * @TableName farm-categories
 */
@TableName(value ="farm_categories")
@Data
@ToString(exclude = {"children"})
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

    /**
     *
     */
    @TableField(exist = false)
    private List<FarmCategories> children = new ArrayList<>();

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    public static List<FarmCategories> buildTree(List<FarmCategories> list) {

        Map<Long, FarmCategories> nodeMap = new HashMap<>(); // 用于快速查找节点
        List<FarmCategories> rootNodes = new ArrayList<>();     // 存储根节点

        // 将所有节点存入 Map，键为节点 ID
        for (FarmCategories farmCategories : list) {
            nodeMap.put(farmCategories.getId(), farmCategories);
        }

        // 遍历节点，建立父子关系
        for (FarmCategories farmCategories : list) {

            Long parentId = farmCategories.getParentId();
            if (parentId == null || parentId == -1) {
                // 如果没有父节点，说明是根节点
                rootNodes.add(farmCategories);
            } else {
                // 找到父节点并添加到父节点的 children 列表中
                FarmCategories parentFarmCategories = nodeMap.get(parentId);
                if (parentFarmCategories != null) {
                    parentFarmCategories.getChildren().add(farmCategories);
                }
            }
        }

        return rootNodes; // 返回根节点列表


    }

}