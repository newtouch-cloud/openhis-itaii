<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true">
      <el-form-item label="" prop="searchKey">
        <el-input
          v-model="queryParams.searchKey"
          placeholder="仓库名称"
          clearable
          style="width: 150px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd"> 新增 </el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-tooltip
          :content="selectRowIds.length == 0 ? '至少选择一条数据' : ''"
          placement="top"
          :disabled="selectRowIds.length != 0"
        >
          <el-button
            type=""
            plain
            icon="Delete"
            :disabled="selectRowIds.length == 0"
            @click="handleDelete"
            >删除</el-button
          >
        </el-tooltip>
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button type="" plain icon="Download" @click="handleExport">
          导出
        </el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button type="" plain icon="Refresh" @click="getPageList">刷新</el-button>
      </el-col>
      <!-- <right-toolbar
        v-model:showSearch="showSearch"
        @queryTable="getList"
      ></right-toolbar> -->
    </el-row>
    <el-table
      ref="orgTableRef"
      v-loading="loading"
      :data="organization"
      row-key="id"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column label="仓库名称" align="left" prop="name" />
      <el-table-column label="仓库类型" align="center" prop="formEnum_enumText" />
      <!-- <el-table-column
        label="存放药品类型"
        align="center"
        prop="classEnum_dictText"
      /> -->
      <el-table-column label="是否使用" align="center" prop="operationalEnum_enumText" />
      <el-table-column label="停用状态" align="center" prop="statusEnum_enumText" />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button link type="primary" @click="handelEdit(scope.row)">编辑</el-button>
          <!-- <el-button
            link
            type="primary"
            @click="handleDisabled(scope.row.id)"
            v-if="scope.row.activeFlag == '1'"
            >停用</el-button
          >
          <el-button
            link
            type="primary"
            @click="handelEnable(scope.row.id)"
            v-else
            >启用</el-button
          > -->
          <el-button
            type="danger"
            link
            icon="Delete"
            :disabled="scope.row.busNo == ''"
            @click="handelDelete(scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" @close="cancel" append-to-body>
      <el-form ref="orgRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="id" prop="busNo" v-show="false">
          <el-input v-model="form.id" placeholder="请输入科室编号" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="类型" prop="formEnum">
          <el-select
            v-model="form.formEnum"
            placeholder="请选择类型"
            clearable
            style="width: 100%"
            :disabled="editShow"
          >
            <el-option
              v-for="dict in warehous_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
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

<script setup name="Organization">
import {
  getList,
  addWarehouse,
  updateWarehouse,
  deleteWarehouse,
  // disableOrg,
  init,
  // enableOrg,
} from './components/api';

const { proxy } = getCurrentInstance();
const loading = ref(true);
const organization = ref([]);
const queryParams = ref({
  locationFormList: [11, 16],
});
const open = ref(false);
const form = ref({
  id: undefined,
  name: undefined,
  formEnum: undefined,
});
const orgTableRef = ref();
const orgRef = ref();
const orgTypeOption = ref([]);
const classEnumOption = ref([]);
const selectRowIds = ref([]);
const total = ref(0);
const title = ref('');
const editShow = ref(false);
const { warehous_type } = proxy.useDict('warehous_type');
const rules = ref({
  busNo: [{ required: false, message: '请输入科室编号', trigger: 'change' }],
  name: [
    { required: true, message: '请输入仓库名称', trigger: 'change' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'change' },
  ],
  formEnum: [{ required: true, message: '请选择仓库类型', trigger: 'change' }],
});

getPageList();
initOption();
function initOption() {
  if (orgTypeOption.value.length == 0) {
    init().then((res) => {
      orgTypeOption.value = res.data.organizationTypeOptions;
      classEnumOption.value = res.data.organizationClassOptions;
    });
  }
}

function reset() {
  form.value = {
    id: undefined,
    name: undefined,
    formEnum: undefined,
  };
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNo = 1;
  getPageList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm('queryRef');
  handleQuery();
}

function getPageList() {
  loading.value = true;
  getList(queryParams.value).then((res) => {
    organization.value = res.data.records.map((record) => {
      return {
        ...record,
        formEnum: record.formEnum.toString(), // 确保 formEnum 是字符串
      };
    });
    total.value = res.data.total;
    loading.value = false;
  });
}

function handleAdd() {
  title.value = '添加药库药房';
  open.value = true;
  editShow.value = false;
  reset();
}

function handelEdit(row) {
  console.log(warehous_type);
  title.value = '编辑药库药房';
  open.value = true;
  editShow.value = true;
  setTimeout(() => {
    form.value.id = row.id;
    form.value.name = row.name;
    form.value.formEnum = row.formEnum.toString();
  }, 10);
}

function cancel() {
  open.value = false;
  reset();
  console.log(form.value);
}

// 新增/编辑
function submitForm() {
  proxy.$refs['orgRef'].validate((valid) => {
    if (valid) {
      if (form.value.busNoParent) {
        form.value.busNo = form.value.busNoParent;
      }
      if (form.value.id == undefined) {
        addWarehouse(form.value).then((res) => {
          proxy.$modal.msgSuccess('操作成功');
          open.value = false;
          getPageList();
        });
      } else {
        alert('456789');
        updateWarehouse(form.value).then((res) => {
          proxy.$modal.msgSuccess('操作成功');
          open.value = false;
          getPageList();
        });
      }
    }
  });
}

// 删除
function handelDelete(data) {
  loading.value = true;
  deleteWarehouse({ locationId: data.id }).then((res) => {
    proxy.$modal.msgSuccess('操作成功');
    loading.value = false;
    getPageList();
  });
}
// // 停用
// function handleDisabled(id) {
//   disableOrg(id).then((res) => {
//     proxy.$modal.msgSuccess("操作成功");
//     getPageList();
//   });
// }

// // 启用
// function handelEnable(id) {
//   enableOrg(id).then((res) => {
//     proxy.$modal.msgSuccess("操作成功");
//     getPageList();
//   });
// }

function handleSelectionChange() {
  selectRowIds.value = orgTableRef.value.getSelectionRows().map((item) => item.id);
}
</script>