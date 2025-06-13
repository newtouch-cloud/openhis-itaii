<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryRef"
      :inline="true"
      v-show="showSearch"
      label-width="90px"
    >
      <!-- <el-form-item label="单据号：" prop="busNo">
        <el-input
          v-model="queryParams.busNo"
          placeholder="单据号："
          clearable
          style="width: 200px;"
          @keyup.enter="handleQuery"
        />
     </el-form-item>
     <el-form-item label="药品名称：" prop="medicineName">
        <el-input
          v-model="queryParams.medicineName"
          placeholder="项目编号/项目品名"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="编码：" prop="medicationDefId">
        <el-input
          v-model="queryParams.medicationDefId"
          placeholder="编码："
          clearable
          style="width: 200px;"
          @keyup.enter="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="项目名称：" prop="searchKey">
        <el-input
          v-model="queryParams.searchKey"
          placeholder="编码/单据号/药品名称"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="库房：" prop="purposeLocationId">
        <el-select
          v-model="queryParams.purposeLocationId"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="supplyStatus in purposeLocationIdList" 
            :key="supplyStatus.value"
            :label="supplyStatus.name"
            :value="supplyStatus.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="审核日期：">
        <el-date-picker
          v-model="occurrenceTime"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 300px"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>
      <el-form-item label="科室：" prop="sourceLocationId">
        <el-select
          v-model="queryParams.sourceLocationId"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="issueDepartment in issueDepartmentDto"
            :key="issueDepartment.id"
            :label="issueDepartment.name"
            :value="issueDepartment.id"
          />
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="项目类型：" prop="categoryType">
        <el-select
          v-model="queryParams.categoryType"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="categoryCode in item_type"
            :key="categoryCode.value"
            :label="categoryCode.label"
            :value="categoryCode.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="单据类型：" prop="typeEnum">
        <el-select
          v-model="queryParams.typeEnum"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="categoryCode in item_type"
            :key="categoryCode.value"
            :label="categoryCode.label"
            :value="categoryCode.value"
          />
        </el-select>
      </el-form-item> -->
      <el-form-item label="供应商：" prop="supplierId">
        <el-select
          v-model="queryParams.supplierId"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="supplierList in supplierListOptions"
            :key="supplierList.value"
            :label="supplierList.label"
            :value="supplierList.value"
          />
        </el-select>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- 添加记录 -->
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="openAddaddTransferProductDialog"
          >备份</el-button
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
        key="supplyBusno"
        prop="supplyBusno"
         width="200"
        :show-overflow-tooltip="true"
      />
       <!-- <el-table-column
        label="出库类型"
        align="center"
        key="statusEnum"
        prop="statusEnum"
         width="90"
        :show-overflow-tooltip="true"
      /> -->
      <!-- itemTable -->
      <el-table-column
        label="药品名称"
        align="center"
        key="name"
        prop="name"
         width="140"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="编码"
        align="center"
        key="busNo"
        prop="busNo"
        width="140"
        :show-overflow-tooltip="true"
      />
      <!-- <el-table-column
        label="药品类型"
        align="center"
        key="categoryCode_dictText"
        prop="categoryCode_dictText"
        width="90"
        :show-overflow-tooltip="true"
      /> -->
      <el-table-column
        label="生产批号"
        align="center"
        key="lotNumber"
        prop="lotNumber"
        width="120px"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="库房"
        align="center"
        key="purposeLocationName"
        prop="purposeLocationName"
         width="140"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="货位"
        align="center"
        key="purposeLocationStoreName"
        prop="purposeLocationStoreName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="计量单位"
        align="center"
        key="unitCode_dictText"
        prop="unitCode_dictText"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="退库数量"
        align="center"
        key="itemQuantity"
        prop="itemQuantity"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="采购单价"
        align="center"
        key="purchasePrice"
        prop="purchasePrice"
        :show-overflow-tooltip="true"
      />
      <!-- <el-table-column
        label="采购金额"
        align="center"
        key="purchasePriceSum"
        prop="purchasePriceSum"
        :show-overflow-tooltip="true"
      /> -->
      <el-table-column
        label="退库单价"
        align="center"
        key="price"
        prop="price"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="金额"
        align="center"
        key="totalPrice"
        prop="totalPrice"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="科室"
        align="center"
        key="sourceLocationName"
        prop="sourceLocationName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="供应商"
        align="center"
        key="supplierId_dictText"
        prop="supplierId_dictText"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="审核人"
        align="center"
        key="approverId_dictText"
        prop="approverId_dictText"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="审核日期"
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
        label="备注"
        align="center"
        key="remake"
        prop="remake"
        :show-overflow-tooltip="true"
      />
        
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script setup name="returnOrutboundDetails">
import {
  getRreportReturnIssue,
  getReturnIssueInit,
  getPharmacyCabinetList,
  getBusNoInit,
  cancelSupply,
  getDepartmentList,
  stopSupply,
} from "./statisticalManagent";

// import Dialog from "./components/Dialog";

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
const occurrenceTime = ref([]);
const busNoAdd = ref(""); // 单据号新增
const purposeTypeListOptions = ref(undefined);
const sourceTypeListOptions = ref(undefined)
const supplyTypeOptions = ref(undefined); 
const categoryCodeListOptions = ref(undefined);
const supplierListOptions = ref(undefined); 
const issueDepartmentDto = ref([]);
const editRow = ref({});
// 使用 ref 定义当前编辑的采购
const currentData = ref({});
// 是否停用
const statusFlagOptions = ref(undefined);
const chrgitmLv_enumTextOptions = ref([]);
const purposeLocationIdList = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    searchKey: undefined,  
    purposeLocationId:undefined, 
    sourceLocationId:undefined,
    supplierId:undefined,
    approvalTimeSTime:undefined,
    approvalTimeETime:undefined,
  },
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);


function getPharmacyCabinetLists() {
  getDepartmentList().then((response) => {
    issueDepartmentDto.value = response.data
  })
  getPharmacyCabinetList().then((response) => {
    console.log(response,'response',response.data)
    purposeLocationIdList.value = response.data
  });
  getReturnIssueInit().then((response) => {
    supplierListOptions.value = response.data.supplierListOptions 
  })
}
/** 查询调拨管理项目列表 */
function getList() {
  loading.value = true;
  getRreportReturnIssue(queryParams.value).then((res) => {
    loading.value = false;
    purchaseinventoryList.value = res.data.records
    total.value = res.data.total;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.approvalTimeSTime =
    occurrenceTime.value && occurrenceTime.value.length == 2
      ? occurrenceTime.value[0] + " 00:00:00"
      : "";
  queryParams.value.approvalTimeETime =
    occurrenceTime.value && occurrenceTime.value.length == 2
      ? occurrenceTime.value[1] + " 23:59:59"
      : "";
  queryParams.value.pageNo = 1;
  getList();
}

/** 清空条件按钮操作 */
function handleClear() {
  // 清空查询条件
  queryParams.value.approvalTimeSTime = ""
  queryParams.value.approvalTimeETime = ""
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

getList();
getPharmacyCabinetLists()
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