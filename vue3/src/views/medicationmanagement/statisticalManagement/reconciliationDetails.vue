<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-form
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        label-width="85px"
      >
        <el-form-item label="结算经办机构" prop="setlOptins" label-width="100px">
          <el-input
            v-model="queryParams.setlOptins"
            placeholder="请输入结算经办机构"
            clearable
            style="width: 150px"
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="结算日期：">
          <el-date-picker
            v-model="occurrenceTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 300px"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="清算类型：" prop="clrType">
          <el-select
            v-model="queryParams.clrType"
            placeholder="请选择清算类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="categoryCode in clrTypeOptions"
              :key="categoryCode.value"
              :label="categoryCode.label"
              :value="categoryCode.value"
            />
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="医院：" prop="orgId"  label-width="70px">
          <el-input
            v-model="queryParams.orgId"
            placeholder="请输入医院id"
            clearable
            style="width: 150px"
            @keyup.enter="handleQuery"
          />
        </el-form-item> -->
        <el-form-item label="文件查询号：" prop="fileQuryNo" label-width="100px">
          <el-input
            v-model="queryParams.fileQuryNo"
            placeholder="请输入文件查询号"
            clearable
            style="width: 150px"
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="本地文件路径：" prop="filePath" label-width="110px">
          <el-input
            v-model="queryParams.filePath"
            placeholder="请输入本地文件路径"
            clearable
            style="width: 150px"
            @keyup.enter="handleQuery"
          />
        </el-form-item>
      </el-form>
    <!-- <el-row :gutter="10" class="mb8"> -->
      <!-- <el-col :span="1.5"> -->
        <!-- v-hasPermi="['system:user:import']" -->
        <el-button
          type="primary"
          plain
          icon="Edit"
          @click="handleQuery"
          >统计</el-button
        >
        <el-button
          type="primary"
          plain
          icon="Edit"
          @click="handleExport"
          >生成txt文件</el-button
        >
      <!-- </el-col> -->
      <!-- <el-col :span="1.5"> -->
        <!-- v-hasPermi="['system:user:export']" -->
        <el-button
          type="primary"
          plain
          icon="Edit"
          @click="handleReconciliation"
          >开始明细对账</el-button
        >
      <!-- </el-col> -->
    </el-row>
    <div style="margin-bottom:5px">结算笔数：{{counts}}（其中 总费用：{{totalPrice1}}元， 基金支付金额：{{totalPrice2}}元， 账户支付金额：{{totalPrice3}}元，现金支付金额：{{totalPrice4}}元）</div>
    <el-table
      v-loading="loading"
      :data="purchaseinventoryList"
      @selection-change="handleSelectionChange"
      @row-click="handleRowClick"
    >
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column
        label="个人编码"
        align="center"
        key="psnNo"
        prop="psnNo"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="就诊ID"
        align="center"
        key="mdtrtId"
        prop="mdtrtId"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="结算ID"
        align="center"
        key="setlId"
        prop="setlId"
        :show-overflow-tooltip="true"
      />
       <!-- <el-table-column
        label="姓名"
        align="center"
        key="locationName"
        prop="locationName"
         width="140"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="医保结算日期"
        align="center"
        key="productionDate"
        prop="productionDate"
        width="160"
        :show-overflow-tooltip="true"
      >
        <template #default="scope">
          <span>{{ parseTime(scope.row.productionDate) }}</span>
        </template>
      </el-table-column> -->
      <el-table-column
        label="总费用"
        align="center"
        key="medfeeSumamt"
        prop="medfeeSumamt"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="基金支付金额"
        align="center"
        key="fundPaySumamt"
        prop="fundPaySumamt"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="个人账户金额"
        align="center"
        key="acctPay"
        prop="acctPay"
        :show-overflow-tooltip="true"
      />
      <!-- <el-table-column
        label="现金支付金额"
        align="center"
        key="unitCode_dictText"
        prop="unitCode_dictText"
        :show-overflow-tooltip="true"
      /> -->
      <!-- <el-table-column
        label="共计支付金额"
        align="center"
        key="quantity"
        prop="quantity"
        :show-overflow-tooltip="true"
      /> -->
       <el-table-column
        label="是否退费"
        align="center"
        key="refdSetlFlag"
        prop="refdSetlFlag"
        :show-overflow-tooltip="true"
      />
       <!-- <el-table-column
        label="报销类型"
        align="center"
        key="minUnitCode_dictText"
        prop="minUnitCode_dictText"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        label="身份证"
        align="center"
        key="minUnitCode_dictText"
        prop="minUnitCode_dictText"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        label="收据编号"
        align="center"
        key="busNo"
        prop="busNo"
        :show-overflow-tooltip="true"
      /> -->
    </el-table>
    <!-- <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    /> -->
    <!-- v-if="purchaseinventoryReturnList&&purchaseinventoryReturnList.length>0" -->
    <!-- <el-table
     v-if="purchaseinventoryList.length>0"
      style="height:190px;margin-top:32px;"
      v-loading="loadingReturn"
      :data="purchaseinventoryReturnList"
      @selection-change="handleSelectionChangeReturn"
    >
     <el-table-column type="selection" width="50" align="center" />
      <el-table-column
        label="人员ID"
        align="center"
        key="busNo"
        prop="busNo"
         width="140"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="就诊ID"
        align="center"
        key="medicineName"
        prop="medicineName"
         width="140"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="结算ID"
        align="center"
        key="locationName"
        prop="locationName"
         width="140"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="对账结果"
        align="center"
        key="locationName"
        prop="locationName"
         width="140"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="备注"
        align="center"
        key="locationName"
        prop="locationName"
         width="140"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="总费用"
        align="center"
        key="manufacturerText"
        prop="manufacturerText"
        width="180px"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="医疗费总额"
        align="center"
        key="lotNumber"
        prop="lotNumber"
        width="120px"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="基金支付总额"
        align="center"
        key="quantityUnit"
        prop="quantityUnit"
         width="140px"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="个人账户支付总额"
        align="center"
        key="unitCode_dictText"
        prop="unitCode_dictText"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="发送方报文ID"
        align="center"
        key="quantity"
        prop="quantity"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        label="退费结算标志"
        align="center"
        key="minUnitCode_dictText"
        prop="minUnitCode_dictText"
        :show-overflow-tooltip="true"
      />
    </el-table> -->
  </div>
</template>

<script setup name="reconciliationDetails">
import {
  reconcileDetailList,
  reconcileDetailTxt,
  reconcileGeneral,
  getPharmacyCabinetList,
} from "./statisticalManagent";
import { formatDate } from '@/utils/index';
const { proxy } = getCurrentInstance();
const purchaseinventoryList = ref([]);
const loading = ref(false);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const occurrenceTime = ref([formatDate(new Date()),formatDate(new Date())]);
const clrTypeOptions = ref([]);
const filePath = ref("")
const purchaseinventoryReturnList = ref([])
const loadingReturn = ref(false)
const selectedRow = ref([])
const counts = ref(0)
const totalPrice1 = ref(0)
const totalPrice2 = ref(0)
const totalPrice3 = ref(0)
const totalPrice4 = ref(0)

const data = reactive({
  form: {},
  queryParams: {
    // pageNo: 1,
    // pageSize: 10,
    setlOptins: '229900', 
    filePath: undefined, 
    orgId:undefined,
    clrType: undefined,
    fileQuryNo: undefined, 
    stmtBegnDate:undefined,
    stmtEndDate:undefined,
  },
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);

watch(
  () => purchaseinventoryList.value,
  (newVlaue) => {
    if(newVlaue&&newVlaue.length>0){
      handleTotalAmount()
    }
  },
  { immediate: true }
)

/**计算合计金额 */
function handleTotalAmount() {
  counts.value = purchaseinventoryList.value.length
  totalPrice1.value = purchaseinventoryList.value.reduce(
    (accumulator, currentRow) => {
      return (accumulator + (Number(currentRow.medfeeSumamt) || 0))
    },
    0
  );
  totalPrice2.value = purchaseinventoryList.value.reduce(
    (accumulator, currentRow) => {
      return (accumulator + (Number(currentRow.fundPaySumamt) || 0))
    },
    0
  );
  totalPrice3.value = purchaseinventoryList.value.reduce(
    (accumulator, currentRow) => {
      return (accumulator + (Number(currentRow.acctPay) || 0))
    },
    0
  );
  // 现金支付金额
  // totalPrice4.value = purchaseinventoryList.value.reduce(
  //   (accumulator, currentRow) => {
  //     return (accumulator + (Number(currentRow.profitAmount) || 0))
  //   },
  //   0
  // );
}
function getPharmacyCabinetLists() {
  clrTypeOptions.value = [
    { value: 11, label: '门诊' },
    { value: 21, label: '住院' },
  ];
}
function handleSelectionChangeReturn(selection) {
  idsReturn.value = selection.map((item) => item.id);
  singleReturn.value = selection.length != 1;
  multipleReturn.value = !selection.length;
}

function getList() {
  reconcileDetailList(queryParams.value).then((res) => {
    console.log(res,"res----------------")
    // loading.value = false;
    // purchaseinventoryList.value = [{psnNo:"123",mdtrtId:"121",setlId:"1213",medfeeSumamt:"122",fundPaySumamt:"12",acctPay:"12",insutype:"410",clrType: 11,contractNo:"229900"},{psnNo:"123",mdtrtId:"121",setlId:"1213",medfeeSumamt:"122",fundPaySumamt:"12",acctPay:"12",insutype:"410",clrType: 11,contractNo:"229900"}]
    res.data.record ? res.data.record : res.data;
    total.value = res.data.total;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.stmtBegnDate =
    occurrenceTime.value && occurrenceTime.value.length == 2
      ? occurrenceTime.value[0] + " 00:00:00"
      : "";
  queryParams.value.stmtEndDate =
    occurrenceTime.value && occurrenceTime.value.length == 2
      ? occurrenceTime.value[1] + " 23:59:59"
      : "";
  // queryParams.value.pageNo = 1;
  getList();
}

/** 按钮操作 */
function handleReconciliation() {
  // 调用接口返回值
  if(selectedRow.value.length==0){
    proxy.$message.error('请选择一行数据开始明细对账');
    return
  }
  loading.value = true;
  let queryParamsss = {
    settlementIdList:ids.value,
    filePath:queryParams.value.filePath?queryParams.value.filePath:"", 
    fileQuryNo:queryParams.value.fileQuryNo?queryParams.value.fileQuryNo:"", 
    setlOptins:queryParams.value.setlOptins?queryParams.value.setlOptins:"", 
    clrType:queryParams.value.clrType?queryParams.value.clrType:""
  }
  reconcileGeneral(queryParamsss).then((res) => {
    console.log(res.data, 'res----------------');
    loading.value = false;
  });
}
function handleExport(){
  if(selectedRow.value.length==0){
    proxy.$message.error('请选择一行数据进行生成txt文件');
    return
  }
  loading.value = true;
  reconcileDetailTxt({settlementIdList:ids.value}).then((res) => {
    console.log(res.data, 'res----------------');
    loading.value = false;
    if(res.data){
      queryParams.value.filePath = res.data.split("生成txt文件成功，文件路径：")?res.data.split("生成txt文件成功，文件路径：")[1]:""
    }
  });
}

/** 选择条数  */
function handleSelectionChange(selection) {
  selectedRow.value = selection
  console.log(selectedRow.value,"selectedRow.value")
  ids.value = selection.map((item) => item.setlId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}
//点击列表当前行
function handleRowClick(row) {
  // editRowTK.value = row
  console.log(row,"row")
  // if(row.returnStatus==1){
  //   loadingReturn.value = true
  //   generatedReturnDetail(row.supplyBusNo).then((response) => {
  //     purchaseinventoryReturnList.value = response.data
  //     purchaseinventoryReturnList.value.map(k=>{
  //       // k.returnStatus_text =  k.returnStatus==1?'已退库':''
  //       k.originalSupplyBusNo = k.originalSupplyBusNo?k.originalSupplyBusNo:row.supplyBusNo
  //     }) 
    
  //   })
  //   loadingReturn.value = false
  // }
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