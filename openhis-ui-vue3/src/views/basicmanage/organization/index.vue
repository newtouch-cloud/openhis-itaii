<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd"
          >新增</el-button
        >
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
        <el-button type="" plain icon="Download" @click="handleExport"
          >导出</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button type="" plain icon="Refresh" @click="getPageList"
          >刷新</el-button
        >
      </el-col>
      <right-toolbar
        v-model:showSearch="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>
    <el-table
      ref="orgTableRef"
      v-loading="loading"
      :data="organization"
      row-key="id"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" :selectable="selectable" width="55" />
      <el-table-column label="科室名称" align="left" prop="name" />
      <el-table-column label="科室类型" align="center" prop="typeEnum" />
      <el-table-column label="科室主任" align="center" prop="dictId" />
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="handelEdit(scope.row)"
            v-hasPermi="['system:dict:edit']"
            >修改</el-button
          >
          <el-button
            link
            type="primary"
            @click="handleDisabled(scope.row)"
            v-hasPermi="['system:dict:remove']"
            v-if="scope.row.status == '启用'"
            >停用</el-button
          >
          <el-button
            link
            type="primary"
            @click="handleDisabled(scope.row)"
            v-hasPermi="['system:dict:remove']"
            v-else
            >启用</el-button
          >
          <el-button
            link
            type="primary"
            @click="handleAdd(scope.row)"
            v-hasPermi="['system:dict:edit']"
            >添加下级</el-button
          >
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
    <el-dialog title="添加科室" v-model="open" width="600px" append-to-body>
      <el-form ref="orgRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="科室编号" prop="busNo">
          <el-input v-model="form.busNo" placeholder="请输入科室编号" />
        </el-form-item>
        <el-form-item label="科室名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入科室名称" />
        </el-form-item>
        <el-form-item label="科室类型" prop="typeEnum">
          <el-select
            v-model="form.typeEnum"
            placeholder="请选择科室类型"
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="dict in org_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-col>
          <el-form-item label="上级科室" prop="deptId">
            <el-tree-select
              style="width: 100%"
              v-model="form.deptId"
              :data="organization"
              :props="{ value: 'busNo', label: 'name', children: 'children' }"
              value-key="id"
              check-strictly
            />
          </el-form-item>
        </el-col>
        <!-- <el-form-item label="状态" prop="status"> </el-form-item> -->
        <!-- <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            placeholder="请输入内容"
          ></el-input>
        </el-form-item> -->
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

<script setup name="organization">
import {
  getList,
  deleteOrganization,
  addOrganization,
  updateOrganization,
  disableOrg,
  initOrgTypeOption,
  getOrgDetail,
} from "./components/api";

const { proxy } = getCurrentInstance();
const { org_type } = proxy.useDict("org_type");
const loading = ref(true);
const organization = ref([]);
const queryParams = ref({});
const open = ref(false);
const form = ref({});
const orgTableRef = ref();
const orgTypeOption = ref([{}]);
const selectRowIds = ref([]);
const rules = ref({
  busNo: [{ required: true, message: "请输入科室编号", trigger: "change" }],
  name: [
    { required: true, message: "请输入科室名称", trigger: "change" },
    { min: 2, max: 20, message: "长度在 2 到 20 个字符", trigger: "change" },
  ],
  typeEnum: [{ required: true, message: "请选择科室类型", trigger: "change" }],
});

getPageList();

function initOption() {
  if (!orgTypeOption.value) {
    initOrgTypeOption().then((res) => {
      orgTypeOption.value = res.data.records;
    });
  }
}

function getPageList() {
  loading.value = false;
  getList(queryParams.value).then((res) => {
    organization.value = res.data.records;
    console.log(res.data.records);

    loading.value = false;
  });
}

function handleAdd() {
  initOption();
  proxy.resetForm("orgRef");
  open.value = true;
}

function handelEdit(id) {
  initOption();
  getOrgDetail(id).then((res) => {
    form.value = res.data.records;
  });
  open.value = true;
}

function cancel() {
  open.value = false;
  proxy.resetForm("orgRef");
}

/**
 * 新增提交
 */
function submitForm() {
  proxy.$refs["orgRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != undefined) {
        addOrganization(form.value).then((res) => {
          proxy.$modal.msgSuccess(res.msg);
          open.value = false;
          getList();
        });
      } else {
        updateOrganization(form.value).then((res) => {
          proxy.$modal.msgSuccess(res.msg);
          open.value = false;
          getList();
        });
      }
    }
  });
}

function handleDelete() {
  loading.value = true;
  deleteOrganization({ ids: selectRowIds.value.join(",") }).then((res) => {
    proxy.$modal.msgSuccess(res.msg);
    loading.value = false;
    getList();
  });
}

function handleDisabled(id) {
  disableOrg(id).then((res) => {
    proxy.$modal.msgSuccess(res.msg);
    getList();
  });
}

function handleSelectionChange() {
  console.log(orgTableRef.value.getSelectionRows());

  selectRowIds.value = orgTableRef.value
    .getSelectionRows()
    .map((item) => item.id);
}
</script>