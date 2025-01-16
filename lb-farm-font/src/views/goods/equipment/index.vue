<template>
  <div class="app-container">

    <el-row :gutter="20">
      <splitpanes :horizontal="false" class="default-theme">
        <!--        分类数据-->
        <pane size="16">
          <el-col>
            <div class="head-container">
              <el-input v-model="categoryName" placeholder="请输入分类名称" clearable prefix-icon="Search"
                        style="margin-bottom: 20px"/>
            </div>
            <div class="head-container">
              <el-tree
                  :data="categoryOptions"
                  :props="{ label: 'label', children: 'children' }"
                  :expand-on-click-node="false"
                  :filter-node-method="filterNode"
                  ref="categoryTreeRef" node-key="id"
                  highlight-current default-expand-all
                  @node-click="handleNodeClick"/>
            </div>
          </el-col>
        </pane>

        <!--用户数据-->
        <pane size="84">
          <!--          搜索-->
          <el-row>

            <el-form ref="queryFormRef" :inline="true" :model="queryParams" class="demo-form-inline">
              <el-form-item label="设备名称" prop="name">
                <el-input v-model="queryParams.name" placeholder="请输入设备名称" clearable prefix-icon="Search"/>
              </el-form-item>
              <el-form-item label="设备状态" prop="status">
                <el-select v-model="queryParams.status" placeholder="请选择设备状态" clearable style="width: 100px">
                  <el-option label="上架" value="1"/>
                  <el-option label="下架" value="0"/>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
              </el-form-item>
            </el-form>

          </el-row>
          <!--          按钮-->
          <el-row>

          </el-row>
          <!--          表格-->

          <el-table v-loading="loading" :data="equipmentList" ref="equipmentTableRef" border>
            <el-table-column label="设备名称" align="center" prop="name"/>
            <el-table-column label="设备图片" align="center" prop="imageUrl" width="100">
              <template #default="scope">
                <el-image :src="scope.row.imageUrl" :preview-src-list="[scope.row.imageUrl]"/>
              </template>
            </el-table-column>
            <el-table-column label="设备描述" align="center" prop="description"/>
            <el-table-column label="设备价格(¥/天)" align="center" prop="pricePerDay"/>
            <el-table-column label="设备库存" align="center" prop="quantity"/>
            <el-table-column label="设备分类" align="center" prop="categoryAll"/>
            <el-table-column label="设备状态" align="center" prop="status" width="100">
              <template #default="scope">
                <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
                  {{ scope.row.status === 1 ? '正常' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template #default="scope">
                <el-button type="success" size="small" icon="View" @click="handleView(scope.row)">查看</el-button>
                <el-button type="success" size="small" icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
                <el-button v-if="scope.status === '0'" type="success" size="small" icon="Open"
                           @click="handleModifyStatus(scope.row, '1')">启用
                </el-button>
                <el-button v-if="scope.status === '1'" type="success" size="small" icon="Close"
                           @click="handleModifyStatus(scope.row, '0')">禁用
                </el-button>
                <el-button type="danger" size="small" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>

              </template>
            </el-table-column>
          </el-table>
          <pagination v-show="total > 0" :total="total" v-model:page="queryParams.current"
                      v-model:limit="queryParams.size" @pagination="handleQuery"/>

        </pane>

      </splitpanes>
    </el-row>


  </div>
</template>

<script setup name="Equipment">

import useAppStore from "@/store/modules/app.js";

import {treeCategory, listEquipment} from "@/api/goods/equipment.js";
import {Splitpanes, Pane} from "splitpanes"
import "splitpanes/dist/splitpanes.css"
import {useRouter} from "vue-router";


const {proxy} = getCurrentInstance();
const appStore = useAppStore();
const router = useRouter();


// 分类查询条件
const categoryName = ref("");
const categoryOptions = ref(undefined);

// 表格相关
// 请求条件
const total = ref(0)
const queryParams = ref({
  name: undefined,
  categoryId: undefined,
  status: undefined,
  current: 1,
  size: 10,

})

// 表格相关
const refreshTable = ref(true)
const loading = ref(false);
const equipmentList = ref([])


function getCategoryTree() {

  treeCategory().then(response => {

    categoryOptions.value = response.data
    console.log(appStore.device)

  });
}

/** 通过条件过滤节点  */
const filterNode = (value, data) => {
  if (!value) return true;
  return data.label.indexOf(value) !== -1;
}

/** 节点单击事件 */
function handleNodeClick(data) {
  queryParams.value.categoryId = data.id;
  handleQuery();
}

/** 查询按钮操作 equipmentList */
function handleQuery() {
  refreshTable.value = false;
  loading.value = true;
  listEquipment(queryParams.value).then(response => {
    equipmentList.value = response.data.rows;
    total.value = response.data.total;
    loading.value = false;
  })
}

/** 重置按钮操作 */
function resetQuery() {
  console.log(proxy)
  proxy.resetForm("queryFormRef");
  queryParams.value.categoryId = undefined;
  proxy.$refs.categoryTreeRef.setCurrentKey(null);
  handleQuery();
}


/** 根据名称筛选分类树 */
watch(
    () => categoryName.value,
    (val, categoryName) => {
      proxy.$refs["categoryTreeRef"].filter(val);
    },
);

onMounted(
    () => {
      getCategoryTree();
      handleQuery();
    }
)


</script>

