<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryRef"
      :inline="true"
      v-show="showSearch"
      label-width="90px"
    >
      <el-form-item label="单据号" prop="busNo">
        <el-input
          v-model="queryParams.searchKey"
          placeholder="单据号："
          clearable
          style="width: 150px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审批状态：" prop="statusEnum" label-width="100px">
        <el-select
          v-model="queryParams.statusEnum"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="supplyStatus in supplyStatusOptions"
            :key="supplyStatus.value"
            :label="supplyStatus.label"
            :value="supplyStatus.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="供应商：" prop="supplierId">
        <el-select
          v-model="queryParams.supplierId"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="supplier in supplierListOptions"
            :key="supplier.value"
            :label="supplier.label"
            :value="supplier.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="经手人：" prop="practitionerId" label-width="120px">
        <el-select
          v-model="queryParams.practitionerId"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="practitioner in practitionerListOptions"
            :key="practitioner.value"
            :label="practitioner.label"
            :value="practitioner.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="查询时间">
        <el-date-picker
          v-model="dateRange"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="openAddInventoryReceiptDialog"
          v-hasPermi="['system:user:add']"
          >添加记录</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['monitor:job:remove']"
          >删除</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Search"
          @click="handleQuery"
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
          >重置</el-button
        >
      </el-col>
    </el-row>

    <el-table
      v-loading="loading"
      :data="purchaseinventoryList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column
        label="单据号"
        align="center"
        key="supplyBusNo"
        prop="supplyBusNo"
      />
      <el-table-column
        label="审批状态"
        align="center"
        key="statusEnum_enumText"
        prop="statusEnum_enumText"
      />
      <el-table-column
        label="供应商"
        align="center"
        key="supplierId_dictText"
        prop="supplierId_dictText"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="经手人"
        align="center"
        key="practitionerId_dictText"
        prop="practitionerId_dictText"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="制单人"
        align="center"
        key="applicantId_dictText"
        prop="applicantId_dictText"
      />
      <el-table-column
        label="审核人"
        align="center"
        key="approverId_dictText"
        prop="approverId_dictText"
      />
      <el-table-column
        label="制单日期"
        align="center"
        key="occurrenceTime"
        prop="occurrenceTime"
        width="160"
      >
        <template #default="scope">
          <span>{{ parseTime(scope.row.occurrenceTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="审核日期 "
        align="center"
        key="approvalTime"
        prop="approvalTime"
        width="160"
      >
        <template #default="scope">
          <span>{{ parseTime(scope.row.approvalTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="180"
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
            @click="handleSubmitApproval(scope.row)"
            v-hasPermi="['system:user:remove']"
            v-if="scope.row.statusEnum == '1'"
            >提交审批</el-button
          >
          <el-button
            link
            type="primary"
            icon="View"
            @click="handleWithdrawApproval(scope.row)"
            v-hasPermi="['system:user:remove']"
            v-if="scope.row.statusEnum == '2'"
            >撤销审批</el-button
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
    <inventory-receipt-dialog
      ref="inventoryReceiptRef"
      :practitionerListOptions="practitionerListOptions"
      :itemTypeOptions="itemTypeOptions"
      :supplierListOptions="supplierListOptions"
      :busNoAdd="busNoAdd"
      :item="currentData"
      @new-item-added="getList"
    />
  </div>
</template>

<script setup name="Purchaseinventory">
import {
  getPurchaseinventoryList,
  addPurchaseinventory,
  getpurchaseInventoryDetail,
  getInit,
  submitApproval,
  withdrawApproval,
  delPurchaseinventory,
} from "./components/purchaseinventory";

import inventoryReceiptDialog from "./components/inventoryReceiptDialog";

const router = useRouter();
const { proxy } = getCurrentInstance();
const purchaseinventoryRef = ref(null); // 初始化 ref

const purchaseinventoryList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const dateRange = ref([]);
const busNoAdd = ref(""); // 单据号新增
const itemTypeOptions = ref(undefined); // 入库项目类型
const practitionerListOptions = ref(undefined); // 查询经手人列表
const supplierListOptions = ref(undefined); // 供应商列表
const supplyStatusOptions = ref(undefined); // 审批状态
// 使用 ref 定义当前编辑的采购
const currentData = ref({});
// 是否停用
const statusFlagOptions = ref(undefined);

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    searchKey: undefined, // 供应商名称
    busNo: undefined, // 编码
    practitionerId: undefined,
    supplierId: undefined,
    statusEnum: undefined, // 审批状态
  },
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);

/** 采购入库查询下拉树结构 */
function getPurchaseinventoryTypeList() {
  getInit().then((response) => {
    console.log(response, "response采购入库查询下拉树结构");
    busNoAdd.value = response.data.busNo; // 单据号新增
    itemTypeOptions.value = response.data.itemTypeOptions; // 活动标记
    practitionerListOptions.value = response.data.practitionerListOptions; // 预约必填标记
    supplierListOptions.value = response.data.supplierListOptions; // 供应商列表
    supplyStatusOptions.value = response.data.supplyStatusOptions; // 供应状态
  });
}

/** 查询采购入库项目列表 */
function getList() {
  loading.value = true;
  // // queryParams.value.statusEnum = +queryParams.value.statusEnum
  console.log(queryParams.value, "queryParams.value");
  // proxy.addDateRange(queryParams.value, dateRange.value)
  getPurchaseinventoryList(queryParams.value).then((res) => {
    loading.value = false;
    console.log(res, "res");
    purchaseinventoryList.value = res.data.records;
    total.value = res.data.total;
    console.log(total.value, "total.value");
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.occurrenceTimeSTime =
    dateRange.value && dateRange.value.length == 2
      ? dateRange.value[0] + " 00:00:00"
      : "";
  queryParams.value.occurrenceTimeETime =
    dateRange.value && dateRange.value.length == 2
      ? dateRange.value[1] + " 23:59:59"
      : "";
  queryParams.value.pageNo = 1;
  getList();
}

/** 清空条件按钮操作 */
function handleClear() {
  // 清空查询条件
  proxy.resetForm("queryRef");
  getList();
}

/** 选择条数  */
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 打开新增弹窗 */
function openAddInventoryReceiptDialog() {
  getPurchaseinventoryTypeList();
  nextTick(() => {
    proxy.$refs["inventoryReceiptRef"].show();
  });
}

/** 修改按钮操作 */
function handleUpdate(row) {
  console.log(typeof row.supplyBusNo, "row", row);
  getpurchaseInventoryDetail(row.supplyBusNo).then((response) => {
    currentData.value = response.data;
    console.log(response, "response采购入库编辑按钮", currentData.value);
    nextTick(() => {
      proxy.$refs["inventoryReceiptRef"].edit();
    });
    getList();
  });
}
/** 提交审核按钮 */
function handleSubmitApproval(row) {
  submitApproval(row.supplyBusNo).then((response) => {
    proxy.$modal.msgSuccess("提交审批成功");
    open.value = false;
    getList();
  });
}

/** 撤回审批按钮 */
function handleWithdrawApproval(row) {
  withdrawApproval(row.supplyBusNo).then((response) => {
    proxy.$modal.msgSuccess("撤销审批成功");
    open.value = false;
    getList();
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const delId = row.id || ids.value;
  proxy.$modal
    .confirm("是否确认删除以上数据?")
    .then(function () {
      return delPurchaseinventory({ ids: delId.join(",") });
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}

getPurchaseinventoryTypeList();
getList();
</script>
<style scoped>
.custom-tree-node {
  display: flex;
  align-items: center;
}

.title {
  font-weight: bold;
  font-size: large;
  margin-bottom: 10px;
}
</style>