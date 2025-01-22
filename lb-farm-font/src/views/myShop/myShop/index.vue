<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="8" :xs="24">
        <el-card class="box-card">
          <template v-slot:header>
            <div class="clearfix">
              <span>店铺信息</span>
            </div>
          </template>
          <div>
            <div class="text-center">
              <image-preview :src="shop.showImage"></image-preview>
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <svg-icon icon-class="user" />店铺名称
                <div class="pull-right">{{ shop.merchantName }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="user" />负责人姓名
                <div class="pull-right">{{ shop.contactPerson }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="user" />手机号码
                <div class="pull-right">{{ shop.contactPhone }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="user" />店铺描述
                <div class="pull-right">{{ shop.description }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="user" />店铺地址
                <div class="pull-right">{{ shop.provinceName }}{{ shop.cityName }}{{ shop.districtName }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="user" />店铺详细地址
                <div class="pull-right">{{ shop.address }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="date" />创建日期
                <div class="pull-right">{{ parseTime(shop.createdAt) }}</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16" :xs="24">
        <el-card>
          <template v-slot:header>
            <div class="clearfix">
              <span>店铺基本资料</span>
            </div>
          </template>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="shopinfo">
              <shopInfo :shop="shop" @update-shop="updateShop"/>
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">

            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import shopInfo from "@/views/myShop/myShop/profile/shopInfo.vue"
import ImagePreview from "@/components/ImagePreview/index.vue";
import {getMyShopInfo} from "@/api/shop/shop.js"

const activeTab = ref("shopinfo");

const shop = ref({})


const getShopInfo = () => {

  getMyShopInfo().then((res)=>{


    shop.value = res.data
    shop.value.addressObject = {
      province: res.data.province,
      city: res.data.city,
      district: res.data.district,
    };
    console.log(shop.value)

  })
}
const updateShop = (newShopData) => {
  getShopInfo()
};

onMounted(
    ()=>{
      getShopInfo()
    }
)



</script>
