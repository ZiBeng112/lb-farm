<template>

  <div class="app-container">

    <!-- 搜索 -->
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
      <el-form-item label="分类名称" prop="name">
        <el-input
            v-model="queryParams.name"
            placeholder="请输入分类名称"
            clearable
            style="width: 200px"
            @keyup.enter="getCategoryTree"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="getCategoryTree">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="Plus"
            @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="info"
            plain
            icon="Sort"
            @click="toggleExpandAll"
        >展开/折叠</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getCategoryTree"></right-toolbar>
    </el-row>

    <!-- 表格 -->
    <el-table
        v-if="refreshTable"
        v-loading="loading"
        :data="categoryList"
        row-key="id"
        :default-expand-all="isExpandAll"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <el-table-column prop="name" label="分类名称" width="260"></el-table-column>
      <el-table-column prop="sortOrder" label="排序" width="200"></el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="200">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createdAt) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button link type="primary" icon="Plus" @click="handleAdd(scope.row)">新增</el-button>
          <el-button v-if="scope.row.parentId != 0" link type="primary" icon="Delete" @click="handleDelete(scope.row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>


    <!-- 添加或修改分类对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="categoryRef" :model="categoryForm" :rules="categoryRules" label-width="80px">
        <el-row>
          <el-col :span="24" v-if="categoryForm.parentId !== 0">
            <el-form-item label="上级部门" prop="parentId">
              <el-tree-select
                  v-model="categoryForm.parentId"
                  :data="categoryOptions"
                  :props="{ value: 'id', label: 'name', children: 'children' }"
                  value-key="id"
                  placeholder="选择上级分类"
                  check-strictly
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>

          <el-col :span="12">
            <el-form-item label="分类名称" prop="name">
              <el-input v-model="categoryForm.name" placeholder="请输入分类名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="sortOrder">
              <el-input-number v-model="categoryForm.sortOrder" controls-position="right" :min="0"/>
            </el-form-item>
          </el-col>

        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

  </div>


</template>
<script setup name="categories">


import {listCategory,addCategory,deleteCategory,updateCategory,detailCategory  } from "@/api/goods/categories";

const {proxy} = getCurrentInstance();

// 请求条件
const queryParams = ref({
  name: undefined,
})
const showSearch = ref(true);
// 表格相关
const refreshTable = ref(true)
const loading = ref(true);
const categoryList = ref([]);
const isExpandAll = ref(true);

// 对话框相关
const open = ref(false);
const title = ref("");
const categoryOptions = ref([]);
const categoryForm = ref({
  id: undefined,
  name: undefined,
  sortOrder: 0,
  parentId: undefined,
  children: undefined,
});
const categoryRules = ref({
  name: [
    {required: true, message: "分类名称不能为空", trigger: "blur"},
  ],
  sortOrder: [
    {required: true, message: "排序不能为空", trigger: "blur"},
  ],
  parentId: [
    {required: true, message: "上级分类不能为空", trigger: "blur"},]
})

function getCategoryTree() {

  loading.value = true;

  listCategory(unref(queryParams)).then(response => {

    categoryList.value = proxy.handleTree(response.data);

    loading.value = false;
  });
}

function handleUpdate(row) {

  reset();

  listCategory().then(response => {
    categoryOptions.value = proxy.handleTree(response.data);

    if (row != undefined) {
      getCategoryDetail(row.id)
    }

  });

  open.value = true;
  title.value = "添加部门";
  // console.log(row)




  // updateCategory(row).then(
  //   response => {
  //     proxy.$modal.msgSuccess("修改成功");
  //     getCategoryTree();
  //   }
  // )

}



function handleDelete(row) {
  deleteCategory({id: row.id}).then(response => {
    proxy.$modal.msgSuccess("删除成功");
    getCategoryTree();
  })
}
/** 表单重置 */
function reset() {
  categoryForm.value = {
    id: undefined,
    name: undefined,
    sortOrder: 0,
    parentId: undefined,
    createdAt: undefined,
    updatedAt: undefined,
    deletedAt: undefined,
    children: undefined,
  };
  proxy.resetForm("categoryRef");
}
/** 展开/折叠操作 */
function toggleExpandAll() {
  refreshTable.value = false;
  isExpandAll.value = !isExpandAll.value;
  nextTick(() => {
    refreshTable.value = true;
  });
}

/** 新增按钮操作 */
function handleAdd(row) {
  reset();

  listCategory().then(response => {
    categoryOptions.value = proxy.handleTree(response.data);
  });
  // console.log(row)
  if (row != undefined) {
    categoryForm.value.parentId = row.id;
  }
  open.value = true;
  title.value = "添加分类";
}

function submitForm (){
  console.log(categoryForm.value)
  addCategory(categoryForm.value).then(response => {
    proxy.$modal.msgSuccess("新增成功");
    open.value = false;
    getCategoryTree();
  })

}


function getCategoryDetail (id) {

  detailCategory({'id': id}).then(response => {
    categoryForm.value = response.data;
  })

}
function resetQuery() {
  proxy.resetForm("queryRef");
  getCategoryTree();
}



function cancel (){
  open.value = false;
}


onMounted(() => {
  getCategoryTree();
})


</script>
<style lang="scss" scoped>
</style>
