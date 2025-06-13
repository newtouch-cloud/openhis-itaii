<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" v-show="showSearch" :inline="true" label-width="68px">
      <el-form-item label="租户ID" prop="tenantId">
        <el-input v-model="queryParams.tenantId" placeholder="请输入租户ID" clearable style="width: 240px"
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="租户编码" prop="tenantCode">
        <el-input v-model="queryParams.tenantCode" placeholder="请输入租户编码" clearable style="width: 240px"
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="租户名称" prop="tenantName">
        <el-input v-model="queryParams.tenantName" placeholder="请输入租户名称" clearable style="width: 240px"
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="状态" clearable style="width: 240px">
          <el-option v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="CircleCheck" :disabled="multiple" @click="handleEnable">启用</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="CircleClose" :disabled="multiple" @click="handleDisable">停用</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 表格数据 -->
    <el-table v-loading="loading" :data="tenantList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="租户ID" prop="id" width="200" />
      <el-table-column label="租户编码" prop="tenantCode" :show-overflow-tooltip="true" width="250" />
      <el-table-column label="租户名称" prop="tenantName" :show-overflow-tooltip="true" width="250" />
      <el-table-column label="状态" align="center" width="100">
        <template #default="scope">
          <el-switch v-model="scope.row.status" active-value="0" inactive-value="1"
            @change="handleStatusChange(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" prop="remark" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-tooltip content="修改" placement="top">
            <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"></el-button>
          </el-tooltip>
          <el-tooltip content="删除" placement="top">
            <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"></el-button>
          </el-tooltip>
          <el-tooltip content="所属用户" placement="top">
            <el-button link type="primary" icon="User" @click="handleSetUser(scope.row)"></el-button>
          </el-tooltip>
          <el-tooltip content="个性化配置" placement="top">
            <el-button link type="primary" icon="Setting" @click="handleSetOption(scope.row)"></el-button>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改租户对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="tenantRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="租户编码" prop="tenantCode">
          <el-input v-model="form.tenantCode" placeholder="请输入租户编码" />
        </el-form-item>
        <el-form-item label="租户名称" prop="tenantName">
          <el-input v-model="form.tenantName" placeholder="请输入租户名称" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label
            }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
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

<script setup name="Tenant">
import { getTenantPage, getTenantDetail, addTenant, editTenant, delTenant, enableTenant, disableTenant } from "@/api/system/tenant";

const router = useRouter();
const { proxy } = getCurrentInstance();
const { sys_normal_disable } = proxy.useDict("sys_normal_disable");


const tenantList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const dateRange = ref([]);


const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    tenantId: undefined,
    tenantName: undefined,
    status: undefined
  },
  rules: {
    tenantCode: [
      { required: true, message: "租户编码不能为空", trigger: "blur" },
      { min: 1, max: 50, message: '长度需在1到50个字符之间', trigger: 'blur' }
    ],
    tenantName: [
      { required: true, message: "租户名称不能为空", trigger: "blur" },
      { min: 1, max: 50, message: '长度需在1到50个字符之间', trigger: 'blur' }
    ],
    remark: [
      { max: 300, message: '备注不能超过300个字符', trigger: 'blur' }
    ]
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 查询租户列表 */
function getList() {
  loading.value = true;
  getTenantPage(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
    tenantList.value = response.data.records;
    total.value = response.data.total;
    loading.value = false;
  });
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}
/** 删除按钮操作 */
function handleDelete(row) {
  const tenantIds = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除租户ID为"' + tenantIds + '"的数据项?').then(function () {
    return delTenant(tenantIds);
  }).then((response) => {
    getList();
    if (response.code != 0) {
      proxy.$modal.msgSuccess(response.msg);
    } else {
      proxy.$modal.msgSuccess("删除成功");
    }
  }).catch(() => { });
}
/** 启用按钮操作 */
function handleEnable() {
  const tenantIds = ids.value;
  proxy.$modal.confirm('是否确认启用租户ID为"' + tenantIds + '"的数据项?').then(function () {
    return enableTenant(ids.value);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("启用成功");
  }).catch(() => { });
}
/** 停用按钮操作 */
function handleDisable() {
  const tenantIds = ids.value;
  proxy.$modal.confirm('是否确认停用租户ID为"' + tenantIds + '"的数据项?').then(function () {
    return disableTenant(tenantIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("停用成功");
  }).catch(() => { });
}
/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}
/** 租户状态修改 */
function handleStatusChange(row) {
  let text = row.status === "0" ? "启用" : "停用";
  proxy.$modal.confirm('确认要' + text + '"' + row.id + '"租户吗?').then(function () {
    if (row.status === "0") {
      return enableTenant(Array.of(row.id));
    } else {
      return disableTenant(Array.of(row.id));
    }
  }).then(() => {
    proxy.$modal.msgSuccess(text + "成功");
  }).catch(function () {
    row.status = row.status === "0" ? "1" : "0";
  });
}
/** 所属用户 */
function handleSetUser(row) {
  router.push("/system/tenant-user/set/" + row.id);
}
/** 个性化配置 */
function handleSetOption(row) {
  router.push("/system/tenant-option/set/" + row.id);
}
/** 重置新增的表单以及其他数据  */
function reset() {
  form.value = {
    id: undefined,
    tenantName: undefined,
    status: "0",
    remark: undefined
  };
  proxy.resetForm("tenantRef");
}
/** 添加租户 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加租户";
}
/** 修改租户 */
function handleUpdate(row) {
  reset();
  const tenantId = row.id || ids.value;
  getTenantDetail(tenantId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改租户";
  });
}
/** 提交按钮 */
function submitForm() {
  proxy.$refs["tenantRef"].validate(valid => {
    if (valid) {
      if (form.value.id != undefined) {
        editTenant(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addTenant(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}

getList();
</script>
