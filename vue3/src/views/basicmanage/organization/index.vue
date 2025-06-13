<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd"> 新增 </el-button>
      </el-col>
      <el-col :span="1.5">
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
      </el-col>
      <el-col :span="1.5">
        <el-button type="" plain icon="Download" @click="handleExport"> 导出 </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="" plain icon="Refresh" @click="getPageList">刷新</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <el-table
      ref="orgTableRef"
      v-loading="loading"
      :data="organization"
      row-key="id"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column label="科室名称" align="left" prop="name" />
      <el-table-column label="科室类型" align="center" prop="typeEnum_dictText" />
      <el-table-column label="科室分类" align="center" prop="classEnum_dictText" />
      <el-table-column label="医保码" align="center" prop="ybNo" />
      <el-table-column label="医保名称" align="center" prop="ybName" />
      <el-table-column label="状态" align="center" prop="activeFlag_dictText" />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button link type="primary" @click="handelEdit(scope.row)">编辑</el-button>
          <el-button
            link
            type="primary"
            @click="handleDisabled(scope.row.id)"
            v-if="scope.row.activeFlag == '1'"
            >停用</el-button
          >
          <el-button link type="primary" @click="handelEnable(scope.row.id)" v-else>启用</el-button>
          <el-button link type="primary" @click="handleAddInferior(scope.row)">
            添加下级
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    /> -->

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" @close="cancel" append-to-body>
      <el-form ref="orgRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="id" prop="id" v-show="false">
          <el-input v-model="form.id" placeholder="请输入科室编号" />
        </el-form-item>
        <el-form-item label="科室编号" prop="busNo" v-show="false">
          <el-input v-model="form.busNo" placeholder="请输入科室编号" />
        </el-form-item>
        <el-form-item label="科室名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入科室名称" />
        </el-form-item>
        <el-form-item label="医保码" prop="name">
          <el-input v-model="form.ybNo" placeholder="请输入医保码" />
        </el-form-item>
        <el-form-item label="医保名称" prop="name">
          <el-input v-model="form.ybName" placeholder="请输入医保名称" />
        </el-form-item>
        <el-form-item label="科室类型" prop="typeEnum">
          <el-select
            v-model="form.typeEnum"
            placeholder="请选择科室类型"
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="item in orgTypeOption"
              :key="item.value"
              :label="item.info"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="科室分类" prop="classEnum">
          <el-select
            v-model="form.classEnum"
            placeholder="请选择科室分类"
            clearable
            style="width: 100%"
            :disabled="form.typeEnum != 2"
          >
            <el-option
              v-for="item in classEnumOption"
              :key="item.value"
              :label="item.info"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-col>
          <el-form-item label="上级科室" prop="busNoParent">
            <el-tree-select
              clearable
              style="width: 100%"
              v-model="form.busNoParent"
              :data="organization"
              :props="{ value: 'busNo', label: 'name', children: 'children' }"
              value-key="id"
              check-strictly
            />
          </el-form-item>
        </el-col>
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
  deleteOrganization,
  addOrganization,
  updateOrganization,
  disableOrg,
  initOrgTypeOption,
  enableOrg,
} from './components/api';

const { proxy } = getCurrentInstance();
const loading = ref(true);
const organization = ref([]);
const queryParams = ref({});
const open = ref(false);
const form = ref({
  id: undefined,
  busNo: undefined,
  name: undefined,
  typeEnum: undefined,
  busNoParent: undefined,
});
const orgTableRef = ref();
const orgRef = ref();
const orgTypeOption = ref([]);
const classEnumOption = ref([]);
const selectRowIds = ref([]);
const total = ref(0);
const title = ref('');
const rules = ref({
  busNo: [{ required: false, message: '请输入科室编号', trigger: 'input' }],
  name: [
    { required: true, message: '请输入科室名称', trigger: 'change' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'change' },
  ],
  typeEnum: [{ required: true, message: '请选择科室类型', trigger: 'change' }],
});

getPageList();
initOption();
function initOption() {
  if (orgTypeOption.value.length == 0) {
    initOrgTypeOption().then((res) => {
      orgTypeOption.value = res.data.organizationTypeOptions;
      classEnumOption.value = res.data.organizationClassOptions;
    });
  }
}

function reset() {
  form.value.id = undefined;
  orgRef.value.resetFields();
}

function getPageList() {
  loading.value = false;
  getList(queryParams.value).then((res) => {
    organization.value = res.data.records;
    total.value = res.data.total;
    loading.value = false;
  });
}

function handleAdd() {
  title.value = '添加药库药房';
  open.value = true;
  reset();
  console.log(form.value);
}

function handelEdit(row) {
  console.log(row.busNo);

  title.value = '编辑科室';
  open.value = true;
  setTimeout(() => {
    form.value.id = row.id;
    form.value.busNo = row.busNo;
    form.value.name = row.name;
    form.value.ybNo = row.ybNo;
    form.value.ybName = row.ybName;
    form.value.typeEnum = row.typeEnum;
    form.value.classEnum = row.classEnum;
    form.value.busNoParent = row.busNo.split('.').length > 1 ? row.busNo.split('.')[0] : undefined;
  }, 50);
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
      if (form.value.id == undefined) {
        if (form.value.busNoParent) {
          form.value.busNo = form.value.busNoParent;
        }
        addOrganization(form.value).then((res) => {
          proxy.$modal.msgSuccess('操作成功');
          open.value = false;
          getPageList();
        });
      } else {
        updateOrganization(form.value).then((res) => {
          proxy.$modal.msgSuccess('操作成功');
          open.value = false;
          getPageList();
        });
      }
    }
  });
}

// 添加下级
function handleAddInferior(row) {
  title.value = '添加下级';
  open.value = true;
  form.value.busNoParent = row.busNo;
}

// 删除
function handleDelete() {
  loading.value = true;
  deleteOrganization(selectRowIds.value.join(',')).then((res) => {
    proxy.$modal.msgSuccess('操作成功');
    loading.value = false;
    getPageList();
  });
}
// 停用
function handleDisabled(id) {
  disableOrg(id).then((res) => {
    proxy.$modal.msgSuccess('操作成功');
    getPageList();
  });
}

// 启用
function handelEnable(id) {
  enableOrg(id).then((res) => {
    proxy.$modal.msgSuccess('操作成功');
    getPageList();
  });
}

function handleSelectionChange() {
  selectRowIds.value = orgTableRef.value.getSelectionRows().map((item) => item.id);
}
</script>