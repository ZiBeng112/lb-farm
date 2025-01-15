<template>
  <div class="app-container">

    <el-row :gutter="20">

      <splitpanes :horizontal="appStore.device === 'mobile'" class="default-theme" :default-sizes="[30, 70]">
        <!--分类数据-->
        <pane >
          <el-col>
            <div class="head-container">
              <el-input v-model="categoryName" placeholder="请输入分类名称" clearable prefix-icon="Search" style="margin-bottom: 20px" />
            </div>
            <div class="head-container">
              <el-tree
                  :data="categoryOptions"
                  :props="{ label: 'label', children: 'children' }"
                  :expand-on-click-node="false"
                  :filter-node-method="filterNode"
                  ref="categoryTreeRef" node-key="id"
                  highlight-current default-expand-all
                  @node-click="handleNodeClick" />
            </div>
          </el-col>
        </pane>
        <!--用户数据-->
        <pane >
          111
        </pane>


      </splitpanes>



    </el-row>

  </div>
</template>
<script setup>
import {Pane, Splitpanes} from "splitpanes";
import useAppStore from "@/store/modules/app.js";

import {treeCategory} from "@/api/goods/equipment.js";

const {proxy} = getCurrentInstance();
const appStore = useAppStore();
const router = useRouter();



// 分类查询条件
const categoryName = ref("");
const categoryOptions = ref(undefined);


// 请求条件
const queryParams = ref({
  name: undefined,
  categoryId: undefined,
})


const showSearch = ref(true);
// 表格相关
const refreshTable = ref(true)
const loading = ref(false);
const equipmentList = ref([
  {
    id: 1,
    name: "设备1",
    imageUrl: "https://img0.baidu.com/it/u=3148308565,4219688661&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500",
  },
  {
    id: 2,
    name: "设备2",
    imageUrl: "https://img0.baidu.com/it/u=3148308565,4219688661&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500",
  }
]);

// 对话框相关
const open = ref(false);
const title = ref("");
// 对话框分类相关

const equipmentForm = ref({
});
const equipmentRules = ref({
  name: [
    {required: true, message: "分类名称不能为空", trigger: "blur"},
  ],
  sortOrder: [
    {required: true, message: "排序不能为空", trigger: "blur"},
  ],
  parentId: [
    {required: true, message: "上级分类不能为空", trigger: "blur"},]
})

function getCategoryTree(){

  treeCategory().then(response => {

    categoryOptions.value = response.data;

  });
}
/** 通过条件过滤节点  */
const filterNode = (value, data) => {
  if (!value) return true;
  return data.label.indexOf(value) !== -1;
};
/** 节点单击事件 */
function handleNodeClick(data) {
  queryParams.value.categoryId = data.id;
  // handleQuery();
};

/** 根据名称筛选分类树 */
watch(categoryName, val => {
  proxy.$refs["categoryTreeRef"].filter(val);
});

onMounted(
    () =>{
      getCategoryTree();
    }
)



</script>
<style lang="scss" scoped>
</style>
