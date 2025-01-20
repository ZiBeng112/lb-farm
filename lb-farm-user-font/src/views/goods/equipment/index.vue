<template>
  <div class="app-container">

    <el-row :gutter="20">
      <splitpanes :horizontal="appStore.device === 'mobile'" class="default-theme">
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
          <el-row :gutter="20">
            <el-col :span="14">

              <el-form ref="queryFormRef" :inline="true" :model="queryParams" class="demo-form-inline">
                <el-form-item label="设备名称" prop="name" style="width: 200px">
                  <el-input v-model="queryParams.name" clearable prefix-icon="Search"/>
                </el-form-item>
                <el-form-item label="设备状态" prop="status" style="width: 200px">
                  <el-select v-model="queryParams.status" clearable>
                    <el-option label="上架" value="1"/>
                    <el-option label="下架" value="0"/>
                  </el-select>
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :span="10">

              <div>
                <el-row :gutter="6" class="mb8">
                  <el-col :span="8">
                    <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                  </el-col>
                  <el-col :span="5">
                    <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
                  </el-col>
                  <el-col :span="5">
                    <el-button type="primary" plain icon="Plus" @click="betchUpdateStatus(1)">上架</el-button>
                  </el-col>
                  <el-col :span="5">
                    <el-button type="primary" plain icon="Plus" @click="handleAdd">导入</el-button>
                  </el-col>

                </el-row>
                <el-row :gutter="6" class="mb8">
                  <el-col :span="8">
                    <el-button icon="Refresh" @click="resetQuery">重置</el-button>
                  </el-col>
                  <el-col :span="5">
                    <el-button type="primary" plain icon="Plus" @click="handleDelete(null)">删除</el-button>

                  </el-col>
                  <el-col :span="5">
                    <el-button type="primary" plain icon="Plus" @click="betchUpdateStatus(0)">下架</el-button>
                  </el-col>
                  <el-col :span="5">
                    <el-button type="primary" plain icon="Plus" @click="handleDownload">导出</el-button>
                  </el-col>

                </el-row>
              </div>


            </el-col>


          </el-row>
          <!--          按钮-->


          <!--          表格-->

          <el-table v-loading="loading" :data="equipmentList" ref="equipmentTableRef" border
                    @selection-change="handleSelectionChange">
            <!-- 多选列 -->
            <el-table-column
                type="selection"
                width="55"
                align="center"
            />
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
            <el-table-column label="创建时间" align="center" prop="createdAt">
              <template #default="scope">
                {{ parseTime(scope.row.createdAt) }}
              </template>
            </el-table-column>
            <el-table-column label="设备状态" align="center" prop="status" width="100">
              <template #default="scope">
                <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
                  {{ scope.row.status === 1 ? '上架' : '下架' }}
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

    <!--    添加与修改对话框-->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form :model="form" :rules="rules" ref="equipmentRef" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="设备名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入设备名"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="归属部门" prop="deptId">
              <el-tree-select v-model="form.categoryId" :data="categoryOptions"
                              :props="{ value: 'id', label: 'label', children: 'children' }"
                              value-key="id"
                              placeholder="请选择归属部门"
                              check-strictly/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>

          <el-col :span="24">
            <el-form-item label="设备图片" prop="imageUrl">
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
                  :limit="1"
              >
                <template #default>
                  <el-icon>
                    <Plus/>
                  </el-icon>
                </template>
              </el-upload>


            </el-form-item>

          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="设备状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择设备状态">
                <el-option label="上架" value="1"></el-option>
                <el-option label="下架" value="0"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="价格每日" prop="price_per_day">
              <el-input-number v-model="form.pricePerDay" controls-position="right" :min="0"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="详细地址" prop="location">
              <el-input v-model="form.location" placeholder="请输入详细地址"/>
            </el-form-item>
          </el-col>

        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="设备描述" prop="description">
              <el-input type="textarea" v-model="form.description"></el-input>
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
    <el-dialog v-model="dialogVisible">
      <img :src="previewImage" alt="Preview Image"/>
    </el-dialog>


  </div>
</template>

<script setup name="Equipment">

import useAppStore from "@/store/modules/app.js";

import {treeCategory, listEquipment, addEquipment, batchUpdateStatusEquipment, deleteEquipment} from "@/api/goods/equipment.js";
import {Splitpanes, Pane} from "splitpanes"
import "splitpanes/dist/splitpanes.css"
import {useRouter} from "vue-router";

import {Plus,} from '@element-plus/icons-vue'
import {getToken} from "@/utils/auth.js";


const {proxy} = getCurrentInstance();
const appStore = useAppStore();
const router = useRouter();
// 图片文件上传
const headers = ref({
  Authorization: "Bearer " + getToken()
});

const uploadUrl = ref(import.meta.env.VITE_APP_BASE_API + "/common/upload"); // 上传的图片服务器地址

const upload = ref(null)

// 文件列表
const fileList = ref([]);

// 预览相关
const dialogVisible = ref(false);
const previewImage = ref("");

// 预览图片
const handlePreview = (file) => {
  previewImage.value = file.url;
  dialogVisible.value = true;
};

// 替换图片（当用户上传新图片时）
const handleSuccess = (response, file, fileListNew) => {
  fileList.value = fileListNew.slice(-1); // 确保只保留最新图片
  form.value.imageUrl = response.url

  console.log(fileList.value)
};

// 删除图片
const handleRemove = () => {
  fileList.value = [];
};


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
const loading = ref(false)
const equipmentList = ref([])
const selectedRows = ref([])

// 对话框相关
const title = ref("");
const open = ref(false);
const data = reactive({
  form: {
    pricePerDay: 0,
    imageUrl: "",
    name: "",
    description: "",
    status: "",
    location: "",
    categoryId: 0


  },
  rules: {

    name: [{required: true, message: "用户昵称不能为空", trigger: "blur"}],
    description: [{required: true, message: "设备描述不能为空", trigger: "blur"}],
    price_per_day: [{required: true, message: "设备价格不能为空", trigger: "blur"}],
    status: [{required: true, message: "设备状态不能为空", trigger: "blur"}],
    imgUrl: [{required: true, message: "设备图片不能为空", trigger: "blur"}],
    location: [{required: true, message: "设备地址不能为空", trigger: "blur"}],
    categoryId: [{required: true, message: "设备分不能为空", trigger: "blur"},]

  }
});
const {form, rules} = toRefs(data);


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

function handleSelectionChange(val) {
  selectedRows.value = val; // 更新选中的行数据

}

function handleAdd() {
  title.value = "添加设备";
  open.value = true;
}

function handleUpdate(row) {
  title.value = "修改设备";
  data.form = Object.assign({}, row);
  fileList.value.pop()
  fileList.value.push({
    name: row.name,
    url: row.imageUrl
  })
  console.log(fileList.value)
  open.value = true;
}

function handleDelete(row) {
  const data = {
    ids: []
  }
  console.log('row:',row)
  if (row !== null){
    data.ids.push(row.id)
  }else {
    data.ids = selectedRows.value.map(item => item.id);
  }
  console.log(data.ids)

  proxy.$confirm('是否确认删除设备编号为"' + data.ids + '"的数据项?', "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(function () {
    // return delEquipment(ids);
    return deleteEquipment(data);
  }).then(() => {
      handleQuery();
  }).catch(function () {
  });
}

function submitForm() {

  const requiredFields = ['name', 'imageUrl', 'description', 'status', 'location', 'categoryId']; // 必填字段列表

  const missingFields = requiredFields.filter((field) => !form.value[field]);

  if (missingFields.length > 0) {
    proxy.$modal.msgError(`以下字段不能为空: ${missingFields.join(', ')}`);
    return; // 阻止提交
  }


  console.log(form.value)


  addEquipment(unref(form)).then(response => {
    if (response.code === 200) {
      proxy.$modal.msgSuccess("操作成功");
      open.value = false;
      handleQuery();
    }
  });
}

function cancel() {
  open.value = false;
}

function betchUpdateStatus(status) {
  const data = {
    ids: [],
    status: status
  }
  data.ids = selectedRows.value.map(item => item.id);

  console.log("p:",data)

  batchUpdateStatusEquipment(data).then(response => {
    if (response.code === 200) {
      proxy.$modal.msgSuccess("操作成功");
      handleQuery();
    }
  });
}



function handleDownload() {

  proxy.download("/equipment/export", {
    ...queryParams.value,
    ids: selectedRows.value.map(item => item.id)
  },`Equipment_${new Date().getTime()}.xlsx`);

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
      console.log(uploadUrl.value)
    }
)


</script>

