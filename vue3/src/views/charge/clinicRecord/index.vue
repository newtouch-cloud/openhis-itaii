<template>
  <div class="app-continer">
    <div style="margin: 15px 0; padding: 0 20px">
      <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="90px">
        <el-form-item label="患者姓名：" prop="searchKey">
          <el-input
            v-model="queryParams.searchKey"
            placeholder="患者姓名"
            clearable
            style="width: 240px"
            @keyup.enter="getClinicRecord"
          />
        </el-form-item>
        <el-form-item label="发票号：" prop="invoiceNo" label-width="120px">
          <el-input
            v-model="queryParams.invoiceNo"
            placeholder="发票号"
            clearable
            style="width: 240px"
            @keyup.enter="getClinicRecord"
          />
        </el-form-item>
        <el-form-item label="结算时间：" prop="activeFlag">
          <el-date-picker
            v-model="occurrenceTime"
            type="daterange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 300px; margin-bottom: 10px; margin-left: 20px"
            value-format="YYYY-MM-DD"
            :clearable="false"
            @change="getClinicRecord"
          />
        </el-form-item>
        <div style="float: right">
          <el-button type="primary" plain @click="getClinicRecord">查询</el-button>
          <el-button type="warning" plain @click="handleReset">重置</el-button>
        </div>
      </el-form>
    </div>
    <el-table :data="clinicRecord" border>
      <!-- <el-table-column label="计算类型" align="center" prop="statusEnum_enumText" /> -->
      <el-table-column label="患者姓名" align="center" prop="patientName" :show-overflow-tooltip="true"/>
      <el-table-column label="支付状态" align="center" prop="statusEnum_dictText" :show-overflow-tooltip="true"/>
      <el-table-column label="费用类型" align="center" prop="paymentEnum_dictText" :show-overflow-tooltip="true"/>
      <el-table-column label="医保结算Id" align="center" prop="ybSettleIds" :show-overflow-tooltip="true"/>
      <el-table-column label="收费流水号" align="center" prop="paymentNo" width="280" :show-overflow-tooltip="true"/>
      <el-table-column label="发票号" align="center" prop="invoiceNo" :show-overflow-tooltip="true"/>
      <el-table-column
        label="结算金额"
        align="right"
        prop="tenderedAmount"
        header-align="center"
        width="100"
        :show-overflow-tooltip="true"
      >
        <template #default="scope">
          <span>{{ scope.row.tenderedAmount + ' 元' }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="支付金额"
        align="right"
        prop="displayAmount"
        header-align="center"
        width="100"
        :show-overflow-tooltip="true"
      >
        <template #default="scope">
          <span>{{ scope.row.displayAmount + ' 元' }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="结算时间"
        align="center"
        key="billDate"
        prop="billDate"
       
        :show-overflow-tooltip="true"
      >
        <template #default="scope">
          <span>{{ parseTime(scope.row.billDate) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="收款人" align="center" prop="entererName" :show-overflow-tooltip="true"/>
      <!-- <el-table-column label="医生" align="center" prop="paymentEnum_enumText" /> -->
      <el-table-column label="支付结果" align="center" prop="outcomeEnum_dictText" :show-overflow-tooltip="true"/>
      <el-table-column label="打印次数" align="center" prop="printCount" :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" prop="paymentEnum_enumText" width="270">
        <template #default="scope">
          <!-- <el-button type="primary" link @click="handlePrint(scope.row)">单据</el-button> -->
          <!-- <el-button type="primary" link @click="handleEdit(scope.row)">冲红</el-button> -->
          <el-button type="primary" link @click="handleOpen(scope.row,1)" :disabled="scope.row.invoiceNo">开具电子发票</el-button>
          <el-button type="primary" link @click="handleOpen(scope.row,2)">冲销发票</el-button>
          <el-button type="primary" link @click="handleOpen(scope.row,3)" :disabled="!scope.row.invoiceNo">调阅发票</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getLists"
    />
  </div>
</template>

<script setup name="ClinicRecord">
const { proxy } = getCurrentInstance();
import { getList ,invoiceMzInvoice,invoiceReissue,invoiceWriteoff,invoiceOpen} from './components/api.js';
import { formatDate } from '@/utils/index';
const occurrenceTime = ref([formatDate(new Date()),formatDate(new Date())]);
const total = ref(0);
const queryParams = ref({
  pageNo: 1,
  pageSize: 10,
  billDateSTime:"",
  billDateETime:"",
  searchKey:"",
});
const clinicRecord = ref([]);
getLists();
function getClinicRecord() {
  queryParams.value.billDateSTime =
  occurrenceTime.value && occurrenceTime.value.length == 2
    ? occurrenceTime.value[0] + " 00:00:00"
    : "";
  queryParams.value.billDateETime =
    occurrenceTime.value && occurrenceTime.value.length == 2
      ? occurrenceTime.value[1] + " 23:59:59"
      : "";
  queryParams.value.pageNo = 1;
  getLists()
}
function getLists(){
  getList(queryParams.value).then((res) => {
    total.value = res.data.total;
    clinicRecord.value = res.data.records;
  });
}
/** 清空条件按钮操作 */
function handleReset() {
  // 清空查询条件
  occurrenceTime.value = ""
  queryParams.value.billDateSTime = ""
  queryParams.value.billDateETime = ""
  proxy.resetForm("queryRef");
  getLists();
}
function handlePrint(row){

}
function handleEdit(row){
  
}
function handleOpen(row,type){
  if(type==1){
    invoiceReissue({paymentId:row.id,encounterId:row.encounterId?row.encounterId:""}).then((res) => {
      if (res.data) {
        // 门诊电子发票开具失败 住院电子发票开具失败 电子发票类型不明确
        if(res.data.includes(" 挂号电子发票开具失败")||res.data.includes(" 住院电子发票开具失败")||res.data.includes(" 门诊电子发票开具失败")||res.data.includes(" 电子发票类型不明确")){
          proxy.$message.error(res.data);
        }else{
          window.open(res.data)
        }
      }
    }).catch((err)=>{
    })
  }else if(type==2) {
    invoiceWriteoff({paymentId:row.id,encounterId:row.encounterId?row.encounterId:""}).then((res) => {
      if (res.data) {
        if(res.data.includes(" 电子票据红冲失败")){
          proxy.$message.error(res.data);
        }else{
          window.open(res.data)
        }
      }
    }).catch((err)=>{
    })
  }else{
    // 调阅电子发票
    invoiceOpen(row.invoiceId?row.invoiceId:"").then((res) => {
      if (res.data) {
        window.open(res.data)
      }
    }).catch((err)=>{
    })
  }
   
}
</script>