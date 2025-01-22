<template>
  <el-form ref="userRef" :model="form" :rules="rules" label-width="80px">

    <el-form-item label="店铺名称" prop="merchantName">
      <el-input v-model="form.merchantName" maxlength="30"/>
    </el-form-item>
    <el-form-item label="展示图片" prop="showImage">
      <el-upload

          ref="upload"
          class="upload-demo"
          :action="uploadUrl"
          name="file"
          :headers="headers"
          list-type="picture-card"
          :file-list="fileList"
          :auto-upload="true"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :on-success="handleSuccess"
          :on-exceed="handleExceed"
          :limit="1"
      >
        <template #default>
          <el-icon>
            <Plus/>
          </el-icon>
        </template>
      </el-upload>
    </el-form-item>
    <el-form-item label="负责人姓名" prop="contactPerson">
      <el-input v-model="form.contactPerson" maxlength="11"/>
    </el-form-item>
    <el-form-item label="手机号码" prop="contactPhone">
      <el-input v-model="form.contactPhone" maxlength="11"/>
    </el-form-item>
    <el-form-item label="店铺描述" prop="description">
      <el-input type="textarea" v-model="form.description" maxlength="11"/>
    </el-form-item>
    <el-form-item label="店铺地址" prop="addressObject">
      <LocationSelector v-model="form.addressObject"/>
    </el-form-item>
    <el-form-item label="店铺详细地址" prop="address">
      <el-input v-model="form.address" maxlength="50"/>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="submit">保存</el-button>
      <el-button type="danger" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
import LocationSelector from "@/components/LocationSelector/index.vue"
import {addShopInfo} from "@/api/shop/shop.js"
import {Plus} from "@element-plus/icons-vue";
import {getToken} from "@/utils/auth.js";


const props = defineProps({
  shop: {
    type: Object
  }
});
const emit = defineEmits(['update-shop']);
const {proxy} = getCurrentInstance();

const form = ref({});
const rules = ref({
  merchantName: [{required: true, message: "店铺名称不能为空", trigger: "blur"}],
  contactPerson: [{required: true, message: "负责人姓名不能为空", trigger: "blur"}],
  description: [{required: true, message: "店铺描述不能为空", trigger: "blur"}],
  address: [{required: true, message: "店铺详细地址不能为空", trigger: "blur"}],
  contactPhone: [{
    required: true,
    message: "手机号码不能为空",
    trigger: "blur"
  }, {pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur"}],
  addressObject: [
    {
      required: true,
      validator: (rule, value, callback) => {
        if (!value || !value.province || !value.city || !value.district) {
          callback(new Error("店铺地址不能为空"));
        } else {
          callback(); // 验证通过
        }
      },
      trigger: "blur", // 可根据具体需求改为 "blur" 或其他
    },
  ]
});
const fileList = ref([]); // 上传文件列表
const headers = ref({
  Authorization: "Bearer " + getToken()
});
const uploadUrl = ref(import.meta.env.VITE_APP_BASE_API + "/common/upload");

const dialogVisible = ref(false); // 控制预览弹窗



// （当用户上传新图片时）
const handleSuccess = (response, file, fileListNew) => {
  fileList.value = fileListNew.slice(-1); // 确保只保留最新图片
  form.value.showImage = response.url

  console.log(fileList.value)
};

const handleExceed = () => {
  proxy.$modal.msgError('最多只能上传一张图片');

}



// 删除图片逻辑
const handleRemove = () => {
  form.value.showImage = "";
  fileList.value = [];
};

// 预览图片逻辑
const handlePreview = () => {
  dialogVisible.value = true;
};

/** 提交按钮 */
function submit() {
  proxy.$refs.userRef.validate(valid => {
    if (valid) {
      console.log(form.value)
      addShopInfo(unref(form)).then(response => {
        proxy.$modal.msgSuccess("保存成功");


        emit('update-shop', form.value); // 触发事件，传递数据给父组件


      })
    }
  });
};

/** 关闭按钮 */
function close() {
  proxy.$tab.closePage();
};

// 回显当前登录用户信息
watch(() => props.shop, shop => {
  if (shop) {
    console.log("hh: ",shop)
    form.value = {
      merchantId: shop.merchantId,
      showImage: shop.showImage,
      merchantName: shop.merchantName,
      contactPerson: shop.contactPerson,
      contactPhone: shop.contactPhone,
      description: shop.description,
      address: shop.address,
      addressObject: {
        province: shop.province,
        city: shop.city,
        district: shop.district
      }
    };
    fileList.value.pop()
    fileList.value.push({
      name: shop.name,
      url: shop.showImage
    })
  }
  console.log(form.value)
}, {immediate: true});
</script>
<style scoped>
.upload-demo {
  display: flex;
  align-items: center;
  justify-content: center;
}

</style>
