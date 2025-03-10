<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryRef"
      :inline="true"
      v-show="showSearch"
      label-width="90px"
    >
      <el-form-item label="厂家编码：" prop="busNo">
        <el-input
          v-model="queryParams.busNo"
          placeholder="厂家编码"
          clearable
          style="width: 240px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="厂家名称：" prop="searchKey">
        <el-input
          v-model="queryParams.searchKey"
          placeholder="厂家名称"
          clearable
          style="width: 240px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="厂商种类：" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="生产商/供应商"
          clearable
          style="width: 240px"
        >
          <el-option
            v-for="dict in supplierTypeOptions"
            :key="dict.value"
            :label="dict.info"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['system:user:add']"
          >添加</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Remove"
          :disabled="multiple"
          @click="handleClose"
          v-hasPermi="['system:user:edit']"
          >停用</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="CirclePlus"
          :disabled="multiple"
          @click="handleStart"
          v-hasPermi="['system:user:remove']"
          >启用</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Search"
          @click="getList"
          v-hasPermi="['system:user:import']"
          >查询</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="CircleClose"
          @click="handleClear"
          v-hasPermi="['system:user:export']"
          >清空条件</el-button
        >
      </el-col>
    </el-row>

    <el-table
      v-loading="loading"
      :data="supplierList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="编号" align="center" key="busNo" prop="busNo" />
      <el-table-column
        label="名称"
        align="center"
        key="name"
        prop="name"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="拼音助记码"
        align="center"
        key="pyStr"
        prop="pyStr"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="类型 "
        align="center"
        key="typeEnum"
        prop="typeEnum"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="地址"
        align="center"
        key="address"
        prop="address"
        width="120"
      />
      <el-table-column
        label="联系人电话"
        align="center"
        key="phone"
        prop="phone"
      />
      <el-table-column
        label="联系人邮箱"
        align="center"
        key="email"
        prop="email"
        width="160"
      />
      <el-table-column
        label="活动标识"
        align="center"
        key="activeFlag_enumText"
        prop="activeFlag_enumText"
        width="160"
      />
      <el-table-column
        label="机构编号"
        align="center"
        key="orgId"
        prop="orgId"
        width="160"
      />
      <el-table-column
        label="操作"
        align="center"
        width="150"
        class-name="small-padding fixed-width"
      >
        <template #default="scope">
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:user:edit']"
            >编辑</el-button
          >
          <el-button
            link
            type="primary"
            icon="View"
            @click="handleView(scope.row)"
            v-hasPermi="['system:user:remove']"
            >查看</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form
        :model="form"
        :rules="rules"
        ref="supplierRef"
        label-width="110px"
      >
        <el-row>
          <el-col :span="12">
            <el-form-item label="名称" prop="name">
              <el-input
                v-model="form.name"
                placeholder="请输入名称"
                :disabled="form.id != undefined"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="编码" prop="busNo">
              <el-input
                v-model="form.busNo"
                placeholder="请输入编码"
                :disabled="form.id != undefined"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="拼音" prop="pyStr">
              <el-input v-model="form.pyStr" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="五笔码" prop="wbStr">
              <el-input v-model="form.wbStr" maxlength="11" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="类型" prop="typeEnum">
              <el-input v-model="form.typeEnum" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="地址" prop="address">
              <el-input v-model="form.address" maxlength="11" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="联系人电话" prop="phone">
              <el-input v-model="form.phone" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系人邮箱" prop="email">
              <el-input v-model="form.email" maxlength="11" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="活动标识" prop="activeFlag">
              <el-input v-model="form.activeFlag" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机构编号" prop="orgId">
              <el-input v-model="form.orgId" maxlength="11" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer v-if="title != '查看'">
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Supplier">
import {
  getSupplierList,
  editSupplier,
  addSupplier,
  getSupplierOne,
  stopSupplier,
  startSupplier,
  getSupplierInit,
} from "./components/supplier";

const router = useRouter();
const { proxy } = getCurrentInstance();
const { sys_normal_disable, sys_user_sex } = proxy.useDict(
  "sys_normal_disable",
  "sys_user_sex"
);

const supplierList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const supplierTypeOptions = ref(undefined);
// 是否停用
const statusFlagOptions = ref(undefined);
// const initPassword = ref(undefined);
// const postOptions = ref([]);
// const roleOptions = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 50,
    searchKey: undefined, // 供应商名称
    busNo: undefined, // 编码
    statusEnum: undefined, // 状态（包括 1：预置，2：启用，3：停用）
    sourceEnum: undefined, // 来源（包括 1：厂商/产地目录分类，2：自定义）
  },
  rules: {
    busNo: [{ required: true, message: "编码不能为空", trigger: "blur" }],
    name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
    pyStr: [{ required: true, message: "拼音不能为空", trigger: "blur" }],
    wbStr: [{ required: true, message: "五笔拼音不能为空", trigger: "blur" }],
    typeEnum: [{ required: true, message: "类型不能为空", trigger: "blur" }],
    address: [{ required: true, message: "地址不能为空", trigger: "blur" }],
    phone: [{ required: true, message: "联系人电话不能为空", trigger: "blur" }],
    email: [{ required: true, message: "联系人邮箱不能为空", trigger: "blur" }],
    activeFlag: [
      { required: true, message: "活动标识不能为空", trigger: "blur" },
    ],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 厂商种类查询下拉树结构 */
function getsupplierTypeList() {
  getSupplierInit().then((response) => {
    console.log(response, "response");
    supplierTypeOptions.value = response.data.supplierTypeOptions;
  });
}

/** 查询厂商/产地目录列表 */
function getList() {
  loading.value = true;
  // queryParams.value.statusEnum = +queryParams.value.statusEnum
  console.log(queryParams.value, "queryParams.value");
  getSupplierList(queryParams.value).then((res) => {
    loading.value = false;
    console.log(res, "res");
    supplierList.value = res.data.records;
    total.value = res.data.total;
    console.log(total.value, "total.value");
  });
}
/** 节点单击事件 */
function handleNodeClick(data) {
  queryParams.value.sourceEnum = data.id;
  handleQuery();
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNo = 1;
  getList();
}

/** 启用按钮操作 */
function handleStart(row) {
  const stardIds = row.id || ids.value;
  proxy.$modal
    .confirm("是否确定启用数据！")
    .then(function () {
      return startSupplier(stardIds);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("启用成功");
    })
    .catch(() => {});
}
/** 停用按钮操作 */
function handleClose(row) {
  const stopIds = row.id || ids.value;
  proxy.$modal
    .confirm("是否确认停用数据！")
    .then(function () {
      return stopSupplier(stopIds);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("停用成功");
    })
    .catch(() => {});
}
/** 清空条件按钮操作 */
function handleClear() {
  // queryParams.value.pageNo = 1;
  // queryParams.value.searchKey = undefined;
  // queryParams.value.statusEnum = undefined;
  // queryParams.value.sourceEnum = undefined;
  // queryParams.value.busNo = undefined;
  // 清空查询条件
  proxy.resetForm("queryRef");
  getList();
}

/** 选择条数  */
function handleSelectionChange(selection) {
  console.log(selection, "selection");
  // selectedData.value = selection.map((item) => ({ ...item })); // 存储选择的行数据
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `user_template_${new Date().getTime()}.xlsx`
  );
}
/** 重置操作表单 */
function reset() {
  form.value = {
    id: undefined,
    conditionCode: undefined,
    pyStr: undefined,
    status: undefined,
    statusEnum: undefined,
  };
  proxy.resetForm("supplierRef");
}
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}
/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增";
}
/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  console.log(row, "row");
  form.value = JSON.parse(JSON.stringify(row));
  open.value = true;
  title.value = "厂商/产地编辑";
}
/** 提交按钮 */
function submitForm() {
  proxy.$refs["supplierRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != undefined) {
        console.log(form.value, "editSupplier", form.value.statusEnum);
        editSupplier(form.value).then((response) => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addSupplier(form.value).then((response) => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 详细按钮操作 */
function handleView(row) {
  reset();
  title.value = "查看";
  open.value = true;
  getSupplierOne(row.id).then((response) => {
    console.log(response, "responsebbbb", row.id);
    form.value = response.data;
  });
}
getsupplierTypeList();
getList();
</script>
<style scoped>
.custom-tree-node {
  display: flex;
  align-items: center;
}
</style>