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
          :key="province.id"
          :label="province.name"
          :value="province.id"
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
          :key="city.id"
          :label="city.name"
          :value="city.id"
      ></el-option>
    </el-select>

    <!-- 区选择 -->
    <el-select
        v-model="selectedDistrict"
        placeholder="请选择区"
        clearable
        :disabled="!selectedCity"
    >
      <el-option
          v-for="district in districts"
          :key="district.id"
          :label="district.name"
          :value="district.id"
      ></el-option>
    </el-select>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from "vue";
import { defineProps, defineEmits } from "vue";

// 接收父组件传递的 props
defineProps({
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
  provinces.value = [
    { id: "1", name: "北京" },
    { id: "2", name: "上海" },
  ];
};

// 加载市数据
const loadCities = async (provinceId) => {
  if (provinceId === "1") {
    cities.value = [{ id: "11", name: "北京市" }];
  } else if (provinceId === "2") {
    cities.value = [{ id: "21", name: "上海市" }];
  } else {
    cities.value = [];
  }
  districts.value = [];
  selectedCity.value = null;
  selectedDistrict.value = null;
};

// 加载区数据
const loadDistricts = async (cityId) => {
  if (cityId === "11") {
    districts.value = [
      { id: "111", name: "东城区" },
      { id: "112", name: "西城区" },
    ];
  } else if (cityId === "21") {
    districts.value = [
      { id: "211", name: "黄浦区" },
      { id: "212", name: "徐汇区" },
    ];
  } else {
    districts.value = [];
  }
  selectedDistrict.value = null;
};

// 省变化
const onProvinceChange = (value) => {
  loadCities(value);
  emit("update:modelValue", {
    province: value,
    city: null,
    district: null,
  });
};

// 市变化
const onCityChange = (value) => {
  loadDistricts(value);
  emit("update:modelValue", {
    province: selectedProvince.value,
    city: value,
    district: null,
  });
};

// 区变化
const onDistrictChange = (value) => {
  emit("update:modelValue", {
    province: selectedProvince.value,
    city: selectedCity.value,
    district: value,
  });
};

// 监听 props 数据变化，用于回显
watch(
    () => props.modelValue,
    (newValue) => {
      selectedProvince.value = newValue.province;
      selectedCity.value = newValue.city;
      selectedDistrict.value = newValue.district;

      if (newValue.province) {
        loadCities(newValue.province).then(() => {
          if (newValue.city) {
            loadDistricts(newValue.city);
          }
        });
      }
    },
    { immediate: true }
);

// 初始化加载省数据
onMounted(() => {
  loadProvinces();
});
</script>

<style scoped>
.location-selector {
  display: flex;
  gap: 10px;
}
</style>
