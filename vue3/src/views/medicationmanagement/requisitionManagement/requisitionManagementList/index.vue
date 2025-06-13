<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryRef"
      :inline="true"
      v-show="showSearch"
      label-width="90px"
    >
    <!-- supplyBusNo searchKey-->
      <el-form-item label="单据号：" prop="searchKey">
        <el-input
          v-model="queryParams.searchKey"
          placeholder="单据号："
          clearable
          style="width: 200px;"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="药品" prop="searchKey" label-width="40">
        <el-input
          v-model="queryParams.searchKey"
          placeholder="品名/商品名/英文品名/编码/拼音"
          clearable
          style="width: 240px"
          @keyup.enter="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="领用部门：" prop="purposeLocationId">
        <el-select
          v-model="queryParams.purposeLocationId"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="purposeLocation in issueDepartmentDto"
            :key="purposeLocation.locationId"
            :label="purposeLocation.name"
            :value="purposeLocation.locationId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="部门经手人：" prop="practitionerId" label-width="120px">
        <el-select
          v-model="queryParams.practitionerId"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="purposeLocationId in practitionerListOptions"
            :key="purposeLocationId.value"
            :label="purposeLocationId.label"
            :value="purposeLocationId.value"
          />
        </el-select>
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
      <el-form-item label="制单日期：">
        <el-date-picker
          v-model="occurrenceTime"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: auto"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>
       <!-- <el-form-item label="单据类型：" prop="typeEnum" label-width="100px">
        <el-select
          v-model="queryParams.typeEnum"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="supplyStatus in supplyTypeOptions"
            :key="supplyStatus.value"
            :label="supplyStatus.label"
            :value="supplyStatus.value"
          />
        </el-select>
      </el-form-item> -->
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- 添加记录 -->
      <!-- v-hasPermi="['system:user:add']" -->
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="openAddaddTransferProductDialog"
          
          >新增领用出库单</el-button
        >
      </el-col>

      <el-col :span="1.5">
        <!-- v-hasPermi="['system:user:import']" -->
        <el-button
          type="primary"
          plain
          icon="Search"
          @click="handleQuery"
          
          >查询</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <!-- v-hasPermi="['system:user:export']" -->
        <el-button
          type="warning"
          plain
          icon="CircleClose"
          @click="handleClear"
          
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
     <!-- <el-table-column
        label="药品名称"
        align="center"
        key="name"
        prop="name"
        :show-overflow-tooltip="true"
        width="110"
      /> -->
      <el-table-column
        label="单据号"
        align="center"
        key="supplyBusNo"
        prop="supplyBusNo"
         width="200"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="审批状态"
        align="center"
        key="statusEnum_enumText"
        prop="statusEnum_enumText"
        :show-overflow-tooltip="true"
      />
      <!-- <el-table-column
        label="状态"
        align="center"
        key="typeEnum_enumText"
        prop="typeEnum_enumText"
      /> -->
      <el-table-column
        label="领用部门"
        align="center"
        key="locationName"
        prop="locationName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="部门经手人"
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
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="制单日期"
        align="center"
        key="createTime"
        prop="createTime"
        width="160"
        :show-overflow-tooltip="true"
      >
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="审核日期 "
        align="center"
        key="approvalTime"
        prop="approvalTime"
        width="160"
        :show-overflow-tooltip="true"
      >
        <template #default="scope">
          <span>{{ parseTime(scope.row.approvalTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="230"
        class-name="small-padding fixed-width"
      >
        <template #default="scope">
          <!-- v-hasPermi="['system:user:edit']" -->
          <el-button
            link
            type="primary"
            icon="View"
            @click="handleUpdate(scope.row,'view')"
            
            >详情</el-button
          >
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleUpdate(scope.row)"
            
            :disabled="
              scope.row.statusEnum != '1' && scope.row.statusEnum != '9'
            "
            >编辑</el-button
          >
          <!-- v-hasPermi="['system:user:remove']" -->
          <el-button
            link
            type="primary"
            icon="View"
            @click="handleSubmitApproval(scope.row)"
            
            v-if="scope.row.statusEnum == '1' || scope.row.statusEnum == '9'"
            >提交审批</el-button
          >
          <!-- v-hasPermi="['system:user:remove']" -->
          <el-button
            link
            type="primary"
            icon="View"
            @click="handleWithdrawApproval(scope.row)"
            
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
    <!-- <Dialog
      ref="transferProductRef"
      :supplyTypeOptions="supplyTypeOptions"
      :purposeTypeListOptions="purposeTypeListOptions"
      :sourceTypeListOptions="sourceTypeListOptions"
      :busNoAdd="busNoAdd"
      :item="currentData"
      :editRow="editRow"
      @refresh="getList"
    /> -->
  </div>
</template>

<script setup name="requisitionManagementList">
import {
  getTransferProductList,
  addTransferProduct,
  getTransferProductDetail,
  getInit,
  getBusNoInit,
  submitApproval,
  withdrawApproval,
  delTransferProduct,
} from '../components/api';
// import Dialog from "./components/Dialog";

import { formatDate } from "@/utils/index";
import useUserStore from "@/store/modules/user";

const router = useRouter();
const { proxy } = getCurrentInstance();

const {
  warehous_type,
  category_code,
  service_type_code,
  specialty_code,
  purchase_type,
} = proxy.useDict(
  "warehous_type",
  "category_code",
  "service_type_code",
  "specialty_code",
  "purchase_type"
);
const purchaseinventoryRef = ref(null); // 初始化 ref
const issueDepartmentDto = ref(undefined); 
const supplierListOptions = ref(undefined);
const practitionerListOptions = ref(undefined)
const purchaseinventoryList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const occurrenceTime = ref([]);
const busNoAdd = ref(""); // 单据号新增
const purposeTypeListOptions = ref(undefined);
const sourceTypeListOptions = ref(undefined)
const supplyTypeOptions = ref(undefined); 
const sourceLocationIdListOptions = ref(undefined); // 源仓库
const purposeLocationIdListOptions = ref(undefined); //目的仓库
const supplyStatusOptions = ref(undefined);
const editRow = ref({});
// 使用 ref 定义当前编辑的采购
const currentData = ref({});
// 是否停用
const statusFlagOptions = ref(undefined);

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    searchKey: undefined, 
    // SupplyBusNo: undefined, // 单据号
    typeEnum:undefined, // 单据类型
    // practitionerId: undefined,
    sourceLocationId:undefined,//源仓库
    purposeLocationId:undefined,// 目的仓库
    // supplierId: undefined,
    statusEnum: undefined, // 单据状态
    createTimeSTime:undefined,
    createTimeETime:undefined,
  },
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);

/** 调拨管理查询下拉树结构 */
function getTransferProductTypeList() {
  getInit().then((response) => {
    // busNoAdd.value = response.data.SupplyBusNo; // 单据号新增
    issueDepartmentDto.value = response.data.issueDepartmentDto; 
    practitionerListOptions.value = response.data.practitionerListOptions;
    supplierListOptions.value = response.data.supplierListOptions 
    supplyStatusOptions.value = response.data.supplyStatusOptions; 
  });
}
function getBusNoInitList() {
  getBusNoInit().then((response) => {
    busNoAdd.value = response.data.SupplyBusNo; // 单据号新增
  });
}


/** 查询管理项目列表 */
function getList() {
  loading.value = true;
  // // queryParams.value.statusEnum = +queryParams.value.statusEnum
  // proxy.addoccurrenceTime(queryParams.value, occurrenceTime.value)
  getTransferProductList(queryParams.value).then((res) => {
    loading.value = false;
    purchaseinventoryList.value = res.data.records;
    total.value = res.data.total;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.createTimeSTime =
    occurrenceTime.value && occurrenceTime.value.length == 2
      ? occurrenceTime.value[0] + " 00:00:00"
      : "";
  queryParams.value.createTimeETime =
    occurrenceTime.value && occurrenceTime.value.length == 2
      ? occurrenceTime.value[1] + " 23:59:59"
      : "";
  queryParams.value.pageNo = 1;
  getList();
}

/** 清空条件按钮操作 */
function handleClear() {
  // 清空查询条件
  queryParams.value.createTimeSTime = ""
  queryParams.value.createTimeETime = ""
  occurrenceTime.value = ""
  proxy.resetForm("queryRef");
  getList();
}

/** 选择条数  */
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 打开新增 */
function openAddaddTransferProductDialog() {
  // getTransferProductTypeList();
  // getBusNoInitList()
  // nextTick(() => {
  //   proxy.$refs["transferProductRef"].show();
  // });
  const partItem = {partFlg: 'add',rowData: []}
  // item: JSON.stringify(partItem)
  // ,query:{item: JSON.stringify(partItem)}
  router.push({ path: '/medicationmanagement/requisitionManagement/requisitionManagement'})
}

/** 修改按钮操作 */
function handleUpdate(row,view) {
  editRow.value = row;
  // const partItem = {partFlg: 'edit',rowData: editRow.value.supplyBusNo }
  if(view){
    router.replace({ path: '/medicationmanagement/requisitionManagement/requisitionManagement',query:{supplyBusNo:row.supplyBusNo,view:view}})
  }else{
    router.push({ path: '/medicationmanagement/requisitionManagement/requisitionManagement',query:{supplyBusNo:editRow.value.supplyBusNo}})
  }
    // getTransferProductDetail(row.supplyBusNo).then((response) => {
  //   currentData.value = response.data;
  //   nextTick(() => {
  //     proxy.$refs["transferProductRef"].edit();
  //   });
  //   getList();
  // });
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
      return delTransferProduct({ ids: delId.join(",") });
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}

getTransferProductTypeList();
// getBusNoInitList()
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