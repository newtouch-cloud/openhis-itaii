<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--药品目录-->
      <el-col :span="4" :xs="24">
        <!-- <div class="head-container">
               <el-input
                  v-model="deptName"
                  placeholder="请输入部门名称"
                  clearable
                  prefix-icon="Search"
                  style="margin-bottom: 20px"
               />
            </div> -->
        <div class="head-container">
          <el-tree
            :data="medicationOptions"
            :props="{ label: 'info', children: 'children' }"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="medicationTreeRef"
            node-key="id"
            highlight-current
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>
      <!--药品目录-->
      <el-col :span="20" :xs="24">
        <el-form
          :model="queryParams"
          ref="queryRef"
          :inline="true"
          v-show="showSearch"
          label-width="68px"
        >
          <el-row :gutter="24">
            <el-col :span="6">
              <el-form-item label="药品" prop="searchKey" label-width="40">
                <el-input
                  v-model="queryParams.searchKey"
                  placeholder="品名/商品名/英文品名/编码/拼音"
                  clearable
                  style="width: 240px"
                  @keyup.enter="handleQuery"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item
                label="是否系统预置"
                prop="status"
                label-width="100"
              >
                <el-select v-model="queryParams.statusEnum" clearable>
                  <el-option
                    v-for="dict in sys_normal_disable"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item
                label="医保是否对码"
                prop="status"
                label-width="100"
              >
                <el-select
                  v-model="queryParams.ybMatchFlag"
                  placeholder="用户状态"
                  clearable
                >
                  <el-option
                    v-for="dict in sys_normal_disable"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <!-- <el-col :span="4">
              <el-form-item label="已发生业务" prop="status" label-width="100">
                <el-select
                  v-model="queryParams.status"
                  placeholder="用户状态"
                  clearable
                >
                  <el-option
                    v-for="dict in sys_normal_disable"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                </el-select>
              </el-form-item>
            </el-col> -->
            <el-col :span="4">
              <el-form-item label="医保等级" prop="status" label-width="80">
                <el-select
                  v-model="queryParams.status"
                  placeholder="用户状态"
                  clearable
                >
                  <el-option
                    v-for="dict in sys_normal_disable"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <!-- <el-form-item>
                  <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                  <el-button icon="Refresh" @click="resetQuery">重置</el-button>
               </el-form-item> -->
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="Plus"
              @click="openAddMedicine"
              v-hasPermi="['system:user:add']"
              >添加新项目</el-button
            >
          </el-col>
          <!-- <el-col :span="1.5">
                  <el-button
                     type="primary"
                     plain
                     icon="Plus"
                     @click="handleAdd"
                     v-hasPermi="['system:user:add']"
                  >添加为本机构项目</el-button>
               </el-col> -->
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
          <!-- <el-col :span="1.5">
            <el-button
              type="success"
              plain
              icon="CirclePlus"
              :disabled="multiple"
              @click="handleStart"
              v-hasPermi="['system:user:remove']"
              >启用</el-button
            >
          </el-col> -->
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
              icon="Download"
              @click="handleExport"
              v-hasPermi="['system:user:export']"
              >导出Excel</el-button
            >
          </el-col>
        </el-row>

        <el-table
          v-loading="loading"
          :data="medicationList"
          @selection-change="handleSelectionChange"
          width="90%"
        >
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column
            label="编码"
            align="center"
            key="busNo"
            prop="busNo"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="项目"
            align="center"
            key="name"
            prop="name"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="规格"
            align="center"
            key="totalVolume"
            prop="totalVolume"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="厂家(产地)"
            align="center"
            key="manufacturerId"
            prop="manufacturerId"
            :show-overflow-tooltip="true"
            width="100"
          />
          <el-table-column
            label="单位"
            align="center"
            key="unitCode"
            prop="unitCode"
            :show-overflow-tooltip="true"
            width="50"
          />
          <el-table-column
            label="拆零单位"
            align="center"
            key="ybMatchflag"
            prop="ybMatchflag"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="单价"
            align="center"
            key="statusEnum"
            prop="statusEnum"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="拆零价"
            align="center"
            key="statusEnum"
            prop="statusEnum"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="拆零比"
            align="center"
            key="partPercent"
            prop="partPercent"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="组套标记"
            align="center"
            key="statusEnum"
            prop="statusEnum"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="医保编码"
            align="center"
            key="ybNo"
            prop="ybNo"
            :show-overflow-tooltip="true"
          />

          <el-table-column
            label="医保已对码"
            align="center"
            key="ybMatchFlag"
            prop="ybMatchFlag"
            :show-overflow-tooltip="true"
            width="100"
          />
          <el-table-column
            label="医保等级"
            align="center"
            key="statusEnum"
            prop="statusEnum"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="发票项目"
            align="center"
            key="statusEnum"
            prop="statusEnum"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="限制使用标记"
            align="center"
            key="restrictedFlag"
            prop="restrictedFlag"
            :show-overflow-tooltip="true"
            width="110"
          />
          <el-table-column
            label="限制使用范围"
            align="center"
            key="restrictedScope"
            prop="restrictedScope"
            :show-overflow-tooltip="true"
            width="110"
          />
          <el-table-column
            label="目录限价"
            align="center"
            key="statusEnum"
            prop="statusEnum"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="病案结算项"
            align="center"
            key="statusEnum"
            prop="statusEnum"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="已发生业务"
            align="center"
            key="statusEnum"
            prop="statusEnum"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="确认可用标记"
            align="center"
            key="statusEnum"
            prop="statusEnum"
            :show-overflow-tooltip="true"
            width="110"
          />
          <el-table-column
            label="停用"
            align="center"
            key="statusEnum"
            prop="statusEnum"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="取消批次号管理"
            align="center"
            key="statusEnum"
            prop="statusEnum"
            :show-overflow-tooltip="true"
            width="120"
          />
          <el-table-column
            label="用法煎法"
            align="center"
            key="statusEnum"
            prop="statusEnum"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="剂量单位换算比"
            align="center"
            key="statusEnum"
            prop="statusEnum"
            :show-overflow-tooltip="true"
            width="120"
          />
          <el-table-column
            label="采购价"
            align="center"
            key="statusEnum"
            prop="statusEnum"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="采购拆零价"
            align="center"
            key="statusEnum"
            prop="statusEnum"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="操作"
            align="center"
            width="150"
            class-name="small-padding fixed-width"
            fixed="right"
          >
            <template #default="scope">
              <el-button
                link
                type="primary"
                icon="Edit"
                @click="openEditMedicine(scope.row)"
                v-hasPermi="['system:user:edit']"
                >编辑</el-button
              >
              <el-button
                link
                type="primary"
                icon="View"
                @click="openViewMedicine(scope.row)"
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
      </el-col>
    </el-row>
    <medicine-dialog
      ref="medicineRef"
      :item="currentData"
      @submit="submitForm"
    />
    <medicine-view-dialog
      ref="medicineViewRef"
      :item="viewData"
      :viewFlg="viewFlg"
    />
    <!-- 添加或修改用户配置对话框 -->
    <!-- <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form :model="form" :rules="rules" ref="medicationRef" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="名称" prop="name">
              <el-input
                v-model="form.name"
                placeholder="请输入名称"
                maxlength="30"
                :disabled="form.id != undefined"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="编码" prop="conditionCode">
              <el-input
                v-model="form.conditionCode"
                placeholder="请输入编码"
                maxlength="30"
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
            <el-form-item label="停用" prop="status">
              <el-checkbox v-model="form.status"></el-checkbox>
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
    </el-dialog> -->
  </div>
</template>

<script setup name="Medication">
import {
  getMedicationList,
  editMedication,
  addMedication,
  getMedicationCategory,
  getMedicationOne,
} from "./components/medicine";
import medicineDialog from "./components/medicineDialog";
import medicineViewDialog from "./components/medicineViewDialog";
import { nextTick } from "vue";

const router = useRouter();
const { proxy } = getCurrentInstance();
const { sys_normal_disable, sys_user_sex } = proxy.useDict(
  "sys_normal_disable",
  "sys_user_sex"
);

const medicationList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const selectedData = ref([]); // 存储选择的行数据
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const medicationOptions = ref(undefined);
// 使用 ref 定义当前药品数据
const currentData = ref({});
// 使用 ref 定义当前查看药品数据
const viewData = ref({});
// const initPassword = ref(undefined);
// const postOptions = ref([]);
// const roleOptions = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 50,
    searchKey: undefined, // 品名/商品名/英文品名/编码/拼音
    statusEnum: undefined, // 状态（包括 1：预置，2：启用，3：停用）
    ybMatchFlag: undefined, // 是否医保匹配（包括 1：是，0：否）
    status: undefined, // 状态（包括 1：预置，2：启用，3：停用）
  },
  rules: {
    // name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
    // conditionCode: [
    //   { required: true, message: "编码不能为空", trigger: "blur" },
    // ],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 通过条件过滤节点  */
const filterNode = (value, data) => {
  if (!value) return true;
  return data.label.indexOf(value) !== -1;
};
// /** 根据名称筛选部门树 */
// watch(deptName, val => {
//   proxy.$refs["deptTreeRef"].filter(val);
// });
/** 病种目录分类查询下拉树结构 */
function getMedicationCategoryList() {
  getMedicationCategory().then((response) => {
    console.log(response, "response药品目录分类查询下拉树结构");
    medicationOptions.value = response.data;
  });
}
/** 查询病种目录列表 */
function getList() {
  loading.value = true;
  getMedicationList(queryParams.value).then((res) => {
    loading.value = false;
    console.log(res, "res");
    medicationList.value = res.data.records;
    total.value = res.data.total;
  });
}
/** 节点单击事件 */
function handleNodeClick(data) {
  queryParams.value.deptId = data.id;
  handleQuery();
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNo = 1;
  getList();
}

/** 启用按钮操作 */
function handleStart() {
  selectedData.value.forEach((item) => {
    item.statusEnum = "2";
  });
  const data = selectedData.value;
  //   selectedData
  console.log(data, "data");
  proxy.$modal
    .confirm("是否确定启用数据！")
    .then(function () {
      return editMedication(data);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("启用成功");
    })
    .catch(() => {});
}
/** 停用按钮操作 */
function handleClose() {
  selectedData.value.forEach((item) => {
    item.statusEnum = "3";
  });
  const data = selectedData.value;
  console.log(data, "data");
  proxy.$modal
    .confirm("是否确认停用数据！")
    .then(function () {
      return editMedication(data);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("停用成功");
    })
    .catch(() => {});
}
/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    "system/user/export",
    {
      ...queryParams.value,
    },
    `user_${new Date().getTime()}.xlsx`
  );
}

/** 选择条数  */
function handleSelectionChange(selection) {
  console.log(selection, "selection");
  selectedData.value = selection.map((item) => ({ ...item })); // 存储选择的行数据
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
  proxy.resetForm("medicationRef");
}
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}
/** 打开新增弹窗 */
function openAddMedicine() {
  proxy.$refs["medicineRef"].show();
}
/** 打开编辑弹窗 */
function openEditMedicine(row) {
  currentData.value = row;
  console.log(currentData.value, "currentData");
  // 确保子组件已经接收到最新的 props
  nextTick(() => {
    proxy.$refs["medicineRef"].edit();
  });
  // proxy.$refs["medicineRef"].edit();
}
/** 打开查看弹窗 */
function openViewMedicine(row) {
  // viewData.value = row;
  reset();
  getMedicationOne(row.id).then((response) => {
    viewData.value = response.data;
    nextTick(() => {
      proxy.$refs["medicineViewRef"].edit();
    });
    getList();
  });
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
  form.value = row;
  open.value = true;
  title.value = "病种编辑";
}
/** 提交按钮 */
function submitForm(formData) {
  if (formData.id != undefined) {
    // form.value.status
    //   ? (form.value.statusEnum = "3")
    //   : (form.value.statusEnum = "2");
    // console.log(form.value, "editMedication", form.value.statusEnum);
    editMedication(formData).then((response) => {
      proxy.$modal.msgSuccess("修改成功");
      open.value = false;
      getList();
    });
  } else {
    addMedication(formData).then((response) => {
      proxy.$modal.msgSuccess("新增成功");
      open.value = false;
      getList();
    });
  }
}

/** 详细按钮操作 */
function handleView(row) {
  reset();
  open.value = true;
  getMedicationOne(row.id).then((response) => {
    console.log(response, "responsebbbb", row.id);
    form.value = response.data;
    //  getList();
  });
}
getMedicationCategoryList();
getList();
</script>
<style scoped>
.el-form--inline .el-form-item {
  display: inline-flex;
  vertical-align: middle;
  margin-right: 10px !important;
}
</style>
