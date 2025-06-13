<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryRef"
      :inline="true"
      label-width="100px"
    >
     <el-form-item label="药品名称：" prop="searchKey">
        <el-input
          v-model="queryParams.searchKey"
          placeholder="编码/项目名称"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="源仓库：" prop="sourceLocationId" label-width="80px">
        <el-select
          v-model="queryParams.sourceLocationId"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="supplyStatus in locationIdList" 
            :key="supplyStatus.value"
            :label="supplyStatus.name"
            :value="supplyStatus.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="目的仓库：" prop="purposeLocationId" label-width="100px">
        <el-select
          v-model="queryParams.purposeLocationId"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="supplyStatus in locationIdList" 
            :key="supplyStatus.value"
            :label="supplyStatus.name"
            :value="supplyStatus.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="制单日期：">
        <el-date-picker
          v-model="occurrenceTime"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 300px"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
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
      <el-table-column
        label="单据号"
        align="center"
        key="supplyBusNo"
        prop="supplyBusNo"
         width="200"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="药品"
        align="center"
        key="name"
        prop="name"
         width="140"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="是否调拨"
        align="center"
        key="statusEnum_enumText"
        prop="statusEnum_enumText"
        width="90"
        :show-overflow-tooltip="true"
      />
     <el-table-column
        label="生产批号"
        align="center"
        key="lotNumber"
        prop="lotNumber"
        width="120px"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="源仓库"
        align="center"
        key="sourceLocationName"
        prop="sourceLocationName"
         width="140"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        label="源货位"
        align="center"
        key="sourceLocationStoreName"
        prop="sourceLocationStoreName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="目的仓库"
        align="center"
        key="purposeLocationName"
        prop="purposeLocationName"
         width="140"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        label="目的货位"
        align="center"
        key="purposeLocationStoreName"
        prop="purposeLocationStoreName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="调拨数量"
        align="center"
        key="itemQuantity"
        prop="itemQuantity"
        width="80"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="计量单位"
        align="center"
        key="unitCode_dictText"
        prop="unitCode_dictText"
        width="80"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="单价"
        align="center"
        key="price"
        prop="price"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="合计金额"
        align="center"
        key="totalPrice"
        prop="totalPrice"
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
        label="审核人"
        align="center"
        key="approverId_dictText"
        prop="approverId_dictText"
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
        label="申请日期"
        align="center"
        key="applyTime"
        prop="applyTime"
        width="160"
        :show-overflow-tooltip="true"
      >
        <template #default="scope">
          <span>{{ parseTime(scope.row.applyTime) }}</span>
        </template>
      </el-table-column>
       <el-table-column
        label="调拨日期"
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

<script setup name="transferManagentDetails">
import {
  getReportTransferPage,
  getPharmacyCabinetList,
} from "./statisticalManagent";

const { proxy } = getCurrentInstance();
const purchaseinventoryList = ref([]);
const loading = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const occurrenceTime = ref([]);
const locationIdList = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    searchKey: undefined, 
    sourceLocationId:undefined,
    purposeLocationId:undefined,
    createTimeSTime:undefined,
    createTimeETime:undefined,
  },
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);

function getPharmacyCabinetLists() {
  getPharmacyCabinetList().then((response) => {
    locationIdList.value = response.data
  });
}
/** 查询调拨管理项目列表 */
function getList() {
  loading.value = true;
  getReportTransferPage(queryParams.value).then((res) => {
    loading.value = false;
    purchaseinventoryList.value = res.data.records
    
    if(purchaseinventoryList.value&& purchaseinventoryList.value.length>0){
      purchaseinventoryList.value.map((k,index)=>{
        k.statusEnum_enumText = k.statusEnum_enumText=='同意'?'已调拨':''
      })
    }
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


getList();
getPharmacyCabinetLists()
</script>
<style scoped>
.custom-tree-node {
  display: flex;
  align-items: center;
}

</style>