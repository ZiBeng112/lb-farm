<template>
  <div class="location-selector">
    <!-- 省选择 -->
    <el-select
        v-model="selectedProvince"
        placeholder="请选择省"
        @change="onProvinceChange"
        clearable
    >
      <el-option
          v-for="province in provinces"
          :key="province.districtId"
          :label="province.district"
          :value="province.districtId"
      ></el-option>
    </el-select>

    <!-- 市选择 -->
    <el-select
        v-model="selectedCity"
        placeholder="请选择市"
        @change="onCityChange"
        clearable
        :disabled="!selectedProvince"
    >
      <el-option
          v-for="city in cities"
          :key="city.districtId"
          :label="city.district"
          :value="city.districtId"
      ></el-option>
    </el-select>

    <!-- 区选择 -->
    <el-select
        v-model="selectedDistrict"
        placeholder="请选择区"
        @change="onDistrictChange"
        clearable
        :disabled="!selectedCity"
    >
      <el-option
          v-for="district in districts"
          :key="district.districtId"
          :label="district.district"
          :value="district.districtId"
      ></el-option>
    </el-select>
  </div>
</template>

<script setup>
import {ref, watch, onMounted} from "vue";
import {defineProps, defineEmits} from "vue";
import {listByParentId,} from "@/api/common/provinces.js"

// 接收父组件传递的 props
const props = defineProps({
  modelValue: {
    type: Object,
    default: () => ({
      province: null,
      city: null,
      district: null,
    }),
  },
});

// 定义 emits
const emit = defineEmits(["update:modelValue", "location-change"]);

// 数据
const provinces = ref([]);
const cities = ref([]);
const districts = ref([]);

const selectedProvince = ref(null);
const selectedCity = ref(null);
const selectedDistrict = ref(null);

// 加载省数据
const loadProvinces = async () => {

  await listByParentId({id: 1}).then(res => {

    provinces.value = res.data;
  })

};

// 加载市数据
const loadCities = async (provinceId) => {

  await listByParentId({id: provinceId}).then(res => {
    cities.value = res.data;
  })

  districts.value = [];
  selectedCity.value = null;
  selectedDistrict.value = null;
};

// 加载区数据
const loadDistricts = async (cityId) => {
  await listByParentId({id: cityId}).then(res => {
    districts.value = res.data;
  })
  selectedDistrict.value = null;
};

// 省变化
const onProvinceChange = (value) => {
  loadCities(value);
  // emit("update:modelValue", {
  //   province: value,
  //   city: null,
  //   district: null,
  // });
};

// 市变化
const onCityChange = (value) => {
  console.log(value)
  loadDistricts(value);
  console.log(selectedCity)
  // emit("update:modelValue", {
  //   province: selectedProvince.value,
  //   city: value,
  //   district: null,
  // });
};

// 区变化
const onDistrictChange = (value) => {
  emit("update:modelValue", {
    province: selectedProvince.value,
    city: selectedCity.value,
    district: value,
  });
};

console.log(props.modelValue)

// 监听 props 数据变化，用于回显
watch(
    () => props.modelValue,
    (newValue) => {


      if (newValue.province) {
        loadCities(newValue.province).then(() => {
          if (newValue.city) {
            loadDistricts(newValue.city).then(
                () => {
                  selectedProvince.value = newValue.province;
                  selectedCity.value = newValue.city;
                  selectedDistrict.value = newValue.district;
                }
            );
          }
        });
      }


      console.log(newValue)


    },
    {immediate: true}
);

// 初始化加载省数据
onMounted(() => {
  loadProvinces();
});
</script>

<style scoped>
.location-selector {
  display: flex;
  width: 300px;
  gap: 10px;
}
</style>
