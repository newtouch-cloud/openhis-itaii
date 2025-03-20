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
                  placeholder=""
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
            label="药品编号"
            align="center"
            key="busNo"
            prop="busNo"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="药品名称"
            align="center"
            key="name"
            prop="name"
            :show-overflow-tooltip="true"
            width="110"
          />
          <el-table-column
            label="药品编码"
            align="center"
            key="medicationDefId"
            prop="medicationDefId"
            :show-overflow-tooltip="true"
            width="200px"
          />
          <el-table-column
            label="药品状态"
            align="center"
            key="statusEnum"
            prop="statusEnum"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="药品名称拼音码"
            align="center"
            key="pyStr"
            prop="pyStr"
            :show-overflow-tooltip="true"
            width="120"
          />
          <el-table-column
            label="药品五笔码"
            align="center"
            key="wbStr"
            prop="wbStr"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="药品分类"
            align="center"
            key="categoryCode"
            prop="categoryCode"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="所属科室"
            align="center"
            key="orgId"
            prop="orgId"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="剂型"
            align="center"
            key="doseFormCode"
            prop="doseFormCode"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="规格"
            align="center"
            key="totalVolume"
            prop="totalVolume"
            :show-overflow-tooltip="true"
            width="200px"
          />
          <el-table-column
            label="成分"
            align="center"
            key="ingredientItem"
            prop="ingredientItem"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="是否为活性"
            align="center"
            key="activeFlag"
            prop="activeFlag"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="批次号"
            align="center"
            key="lotNumber"
            prop="lotNumber"
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
            label="生效日期"
            align="center"
            key="effectiveDate"
            prop="effectiveDate"
            :show-overflow-tooltip="true"
          >
            <template #default="scope">
              <span>{{ parseTime(scope.row.effectiveDate) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="到期日期"
            align="center"
            key="expirationDate"
            prop="expirationDate"
            :show-overflow-tooltip="true"
          >
            <template #default="scope">
              <span>{{ parseTime(scope.row.expirationDate) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="用法"
            align="center"
            key="methodCode"
            prop="methodCode"
            :show-overflow-tooltip="true"
          />

          <el-table-column
            label="用药频次"
            align="center"
            key="rateCode"
            prop="rateCode"
            :show-overflow-tooltip="true"
            width="100"
          />
          <el-table-column
            label="单次剂量"
            align="center"
            key="dose"
            prop="dose"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="剂量单位"
            align="center"
            key="doseUnitCode"
            prop="doseUnitCode"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="单次最大剂量"
            align="center"
            key="maxUnit"
            prop="maxUnit"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="药品定义"
            align="center"
            key="definition"
            prop="definition"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="适用范围"
            align="center"
            key="domainEnum"
            prop="domainEnum"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="药品版本"
            align="center"
            key="version"
            prop="version"
            :show-overflow-tooltip="true"
            width="120"
          />
          <el-table-column
            label="英文药名"
            align="center"
            key="nameEn"
            prop="nameEn"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="商品名称"
            align="center"
            key="merchandiseName"
            prop="merchandiseName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="商品名称拼音码"
            align="center"
            key="merchandisePyStr"
            prop="merchandisePyStr"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="商品五笔码"
            align="center"
            key="merchandiseWbStr"
            prop="merchandiseWbStr"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="药品单位"
            align="center"
            key="unitCode"
            prop="unitCode"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="最小单位"
            align="center"
            key="minUnitCode"
            prop="minUnitCode"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="所含耗材"
            align="center"
            key="comprisedText"
            prop="comprisedText"
            :show-overflow-tooltip="true"
            width="110"
          />
          <el-table-column
            label="成分"
            align="center"
            key="ingredient"
            prop="ingredient"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="拆零比"
            align="center"
            key="partPercent"
            prop="partPercent"
            :show-overflow-tooltip="true"
            width="120"
          />
          <el-table-column
            label="剂量形式"
            align="center"
            key="doseFrom"
            prop="doseFrom"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="批准文号"
            align="center"
            key="approvalNumber"
            prop="approvalNumber"
            :show-overflow-tooltip="true"
            width="120"
          />
          <el-table-column
            label="医保是否对码"
            align="center"
            key="ybMatchFlag"
            prop="ybMatchFlag"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="医保编码"
            align="center"
            key="ybNo"
            prop="ybNo"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="药理作用分类"
            align="center"
            key="pharmacologyCategoryCode"
            prop="pharmacologyCategoryCode"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="是否皮试"
            align="center"
            key="skinTestFlag"
            prop="skinTestFlag"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="是否为注射药物"
            align="center"
            key="injectFlag"
            prop="injectFlag"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="生产厂家"
            align="center"
            key="manufacturerId"
            prop="manufacturerId"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="供应商"
            align="center"
            key="supplyId"
            prop="supplyId"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="是否限制使用"
            align="center"
            key="restrictedFlag"
            prop="restrictedFlag"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="限制使用范围"
            align="center"
            key="restrictedScope"
            prop="restrictedScope"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="儿童用药标志"
            align="center"
            key="childrenFlag"
            prop="childrenFlag"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="产品特性"
            align="center"
            key="characteristic"
            prop="characteristic"
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
      :domainEnum="domainEnumOptions"
      :status="statusFlagOptions"
      @submit="submitForm"
    />
    <medicine-view-dialog
      ref="medicineViewRef"
      :item="viewData"
      :viewFlg="viewFlg"
    />
  </div>
</template>

<script setup name="Medication">
import {
  getMedicationList,
  editMedication,
  addMedication,
  getMedicationCategory,
  getMedicationOne,
  startMedication,
  stopMedication,
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
const ids = ref([]); // 存储选择的行数据
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const medicationOptions = ref(undefined);
const statusFlagOptions = ref(undefined);
const domainEnumOptions = ref(undefined);
// 使用 ref 定义当前药品数据
const currentData = ref({});
// 使用 ref 定义当前查看药品数据
const viewData = ref({});

// 使用 ref 定义当前查看药品数据
const viewFlg = ref(false);

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
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
    medicationOptions.value = response.data.medicationOptions;
    statusFlagOptions.value = response.data.statusFlagOptions;
    domainEnumOptions.value = response.data.domainFlagOptions;
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
  const startIds = ids.value;
  proxy.$modal
    .confirm("是否确定启用数据！")
    .then(function () {
      return startMedication(startIds);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("启用成功");
    })
    .catch(() => {});
}
/** 停用按钮操作 */
function handleClose() {
  const stopIds = ids.value;
  console.log(data, "data");
  proxy.$modal
    .confirm("是否确认停用数据！")
    .then(function () {
      return stopMedication(stopIds);
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
  currentData.value = JSON.parse(JSON.stringify(row));
  currentData.value.activeFlag == 1
    ? (currentData.value.activeFlag = true)
    : (currentData.value.activeFlag = false); //是否为活性
  currentData.value.ybMatchFlag == 1
    ? (currentData.value.ybMatchFlag = true)
    : (currentData.value.ybMatchFlag = false); //医保是否对码
  currentData.value.skinTestFlag == 1
    ? (currentData.value.skinTestFlag = true)
    : (currentData.value.skinTestFlag = false); //是否皮试
  currentData.value.injectFlag == 1
    ? (currentData.value.injectFlag = true)
    : (currentData.value.injectFlag = false); //是否为注射药物
  currentData.value.restrictedFlag == 1
    ? (currentData.value.restrictedFlag = true)
    : (currentData.value.restrictedFlag = false); //是否限制使用
  currentData.value.childrenFlag == 1
    ? (currentData.value.childrenFlag = true)
    : (currentData.value.childrenFlag = false); //儿童用药标志
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

/** 提交按钮 */
function submitForm(formData) {
  formData.activeFlag == true
    ? (formData.activeFlag = 1)
    : (formData.activeFlag = 0); //是否为活性
  formData.ybMatchFlag == true
    ? (formData.ybMatchFlag = 1)
    : (formData.ybMatchFlag = 0); //医保是否对码
  formData.skinTestFlag == true
    ? (formData.skinTestFlag = 1)
    : (formData.skinTestFlag = 0); //是否皮试
  formData.injectFlag == true
    ? (formData.injectFlag = 1)
    : (formData.injectFlag = 0); //是否为注射药物
  formData.restrictedFlag == true
    ? (formData.restrictedFlag = 1)
    : (formData.restrictedFlag = 0); //是否限制使用
  formData.childrenFlag == true
    ? (formData.childrenFlag = 1)
    : (formData.childrenFlag = 0); //儿童用药标志
  formData.status == true ? (formData.status = 1) : (formData.status = 0); //启用状态

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
