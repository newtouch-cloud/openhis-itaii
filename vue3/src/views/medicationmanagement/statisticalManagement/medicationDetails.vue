<template>
  <div outpatientNo="app-container">
    <el-form
        style="margin-top:20px;margin-left:20px;"
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        label-width="90px"
    >
        <el-form-item label="发药时间：">
            <el-date-picker
                v-model="occurrenceTime"
                type="daterange"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                style="width: 300px"
                value-format="YYYY-MM-DD"
            />
        </el-form-item>
        <!-- <el-form-item label="开始时间" prop="startTime">
            <el-date-picker
                v-model="queryParams.startTime"
                type="datetime"
                placeholder="请选择开始时间"
                format="YYYY/MM/DD HH:mm:ss"
                value-format="YYYY/MM/DD HH:mm:ss"
            />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
            <el-date-picker
                v-model="queryParams.endTime"
                type="datetime"
                placeholder="请选择结束时间"
                format="YYYY/MM/DD HH:mm:ss"
                value-format="YYYY/MM/DD HH:mm:ss"
            />
        </el-form-item> -->
      <!-- <el-form-item label="商品编码：" prop="medicationDefId">
        <el-input
          v-model="queryParams.busNo"
          placeholder="商品编码："
          clearable
          style="width: 200px;"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品名称：" prop="medicationName">
        <el-input
          v-model="queryParams.name"
          placeholder="商品名称："
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="药房：" prop="locationId">
        <el-select
          v-model="queryParams.locationId"
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
      <!-- <el-form-item label="药品类型：" prop="categoryType">
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
      </el-form-item> -->
      <!-- <el-form-item label="病人姓名：" prop="patientName">
        <el-input
          v-model="queryParams.patientName"
          placeholder="请输入病人姓名"
          clearable
          style="width: 150px"
          @keyup.enter="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="发药人：" prop="practitionerId">
        <el-select
          v-model="queryParams.practitionerId"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="supplyStatus in practitionerNameOptions"
            :key="supplyStatus.id"
            :label="supplyStatus.name"
            :value="supplyStatus.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="项目查询/病人姓名：" prop="searchKey" label-width="150">
        <el-input
          v-model="queryParams.searchKey"
          placeholder="输入项目名称/编码/病人姓名后回车查询"
          clearable
          style="width: 270px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="统计类型：">
        <el-select
          v-model="inventoryScope"
          placeholder=""
          style="width: 220px"
          @change="inventoryChange(inventoryScope)"
        >
          <el-option
            v-for="supplyStatus in inventoryOptions"
            :key="supplyStatus.value"
            :label="supplyStatus.label"
            :value="supplyStatus.value"
          />
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="结算状态：" prop="settlementStatus">
        <el-select
          v-model="queryParams.settlementStatus"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="supplyStatus in purposeLocationIdList" 
            :key="supplyStatus.value"
            :label="supplyStatus.label"
            :value="supplyStatus.value"
          />
        </el-select>
      </el-form-item> -->
      <el-form-item label="出院状态：" prop="dischargeStatus">
        <el-select
          v-model="queryParams.dischargeStatus"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="supplyStatus in dischargeStatusList" 
            :key="supplyStatus.value"
            :label="supplyStatus.label"
            :value="supplyStatus.value"
          />
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="供应商：" prop="supplierId">
        <el-input
          v-model="queryParams.supplierId"
          placeholder="回车查询"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item> -->
    </el-form>

    <el-row :gutter="10" outpatientNo="mb8" style="margin-left:20px;margin-right:0px;margin-bottom:5px">
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
      style="padding:0 20px;width:100%"
      v-loading="loading"
      :data="purchaseinventoryList"
      @selection-change="handleSelectionChange"
      :span-method="arraySpanMethod"
     
    >
     <!-- :summary-method="getSummaries"
      show-summary 每页单独合计-->
      <el-table-column type="selection"  width="60px" align="center" />
      <el-table-column
        v-if="inventoryScope==1||inventoryScope==3||inventoryScope==5"
        label="发药人"
        align="center"
        key="practitionerName"
        prop="practitionerName"
        width="90px"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        v-if="inventoryScope==1||inventoryScope==3||inventoryScope==5"
        label="发药单号"
        align="center"
        key="dispenseNo"
        prop="dispenseNo"
        width="130px"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        v-if="inventoryScope==2||inventoryScope==4||inventoryScope==6"
        :label="inventoryScope==2?'门诊号':'住院号'"
        align="center"
        key="outpatientNo"
        prop="outpatientNo"
        width="120px"
        :show-overflow-tooltip="true"
      />
      <!-- <el-table-column label="门诊号" v-if="inventoryScope==2" width="180" key="outpatientNo" :show-overflow-tooltip="true"
        prop="outpatientNo">
        <template #default="{ row, column, $index }">
            <span v-if="$index === 0 || (purchaseinventoryList[$index - 1].outpatientNo !== row.outpatientNo)">{{ row.outpatientNo }}</span>
        </template>
      </el-table-column> -->
      <el-table-column
        v-if="inventoryScope==2||inventoryScope==4||inventoryScope==6"
        label="处方号"
        align="center"
        key="prescriptionNo"
        prop="prescriptionNo"
        width="120px"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        v-if="inventoryScope==2||inventoryScope==4||inventoryScope==6"
        label="病人"
        align="center"
        key="patientName"
        prop="patientName"
        width="90px"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        v-if="inventoryScope==2||inventoryScope==4||inventoryScope==6"
        label="发药人"
        align="center"
        key="practitionerName"
        prop="practitionerName"
        width="90px"
        :show-overflow-tooltip="true"
      />
    <el-table-column
        v-if="inventoryScope==2||inventoryScope==4||inventoryScope==6"
        label="发药单号"
        align="center"
        key="dispenseNo"
        prop="dispenseNo"
        width="130px"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="药品项目"
        align="center"
        key="medicationName"
        prop="medicationName"
        width="160px"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        v-if="inventoryScope==1||inventoryScope==3"
        label="药品编码"
        align="center"
        key="busNo"
        prop="busNo"
        width="140px"
        :show-overflow-tooltip="true"
      />
       <!-- <el-table-column
        v-if="inventoryScope==1||inventoryScope==3"
        label="医保项目"
        align="center"
        key="medicalName"
        prop="medicalName"
        width="140"
        :show-overflow-tooltip="true"
      /> -->
      <el-table-column
        v-if="inventoryScope==1||inventoryScope==3"
        label="医保编码"
        align="center"
        key="ybNo"
        prop="ybNo"
      
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="规格"
        align="center"
        key="totalVolume"
        prop="totalVolume"
   
        :show-overflow-tooltip="true"
      />
       <!-- <el-table-column
        label="零售价"
        align="center"
        key="salePrice"
        prop="salePrice"
        width="80"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        v-if="inventoryScope==1||inventoryScope==3||inventoryScope==5"
        label="采购价"
        align="center"
        key="price"
        prop="price"
         width="80"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        v-if="inventoryScope==1||inventoryScope==3||inventoryScope==5"
        label="采购金额"
        align="center"
        key="totalPrice"
        prop="totalPrice"
        width="120"
        :show-overflow-tooltip="true"
      /> -->
      <el-table-column
        label="发药数量"
        align="center"
        key="dispenseQuantity"
        prop="dispenseQuantity"
        width="100px"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="发药金额"
        align="center"
        key="dispensePrice"
        prop="dispensePrice"
        width="100px"
        :show-overflow-tooltip="true"
      /> 
      <el-table-column
        label="退药数量"
        align="center"
        key="refundQuantity"
        prop="refundQuantity"
        width="100px"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="退药金额"
        align="center"
        key="refundPrice"
        prop="refundPrice"
        width="100px"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="生产批号"
        align="center"
        key="lotNumber"
        prop="lotNumber"
        :show-overflow-tooltip="true"
        width="120px"
      />
      <el-table-column
        v-if="inventoryScope==2||inventoryScope==4||inventoryScope==6"
        label="发药时间"
        align="center"
        key="dispenseTime"
        prop="dispenseTime"
      
        :show-overflow-tooltip="true"
      >
        <template #default="scope">
          <span>{{ parseTime(scope.row.dispenseTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="厂家/产地"
        align="center"
        key="manufacturerText"
        prop="manufacturerText"
      
        :show-overflow-tooltip="true"
      /> 
      <el-table-column
        label="供应商"
        align="center"
        key="supplierName"
        prop="supplierName"
     
        :show-overflow-tooltip="true"
      /> 
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList(1)"
    />
    <el-row
      :gutter="10"
      outpatientNo="mb8"
      style="
        margin-top: 10px;
        display: flex;
        align-items: center;
        justify-content: flex-start;
        margin-left:0px;margin-right:0px;padding:0 20px;
      "
    >
      <el-col :span="3">
        <span>制单人：{{ userStore.name }}</span>
      </el-col>
      <!-- <el-col :span="6">
        <el-row
          :gutter="8"
          style="
            display: flex;
            align-items: center;
            justify-content: flex-start;
          "
        >
          <el-col :span="10">
            <span>合计金额：{{ totalAmount?totalAmount.toFixed(4):0 }}</span>
          </el-col>
        </el-row>
      </el-col> -->
    </el-row>
  </div>
</template>

<script setup name="medicationDetails">
import {
  getPharmacyCabinetList,
  getAmbMedicationDetail,
  getAmbPractitionerDetail,
  getMedicationDetailsInit
} from "./statisticalManagent";
import useUserStore from "@/store/modules/user";
import { watch } from "vue";
import { endsWith } from "lodash";
const userStore = useUserStore();

const router = useRouter();
const { proxy } = getCurrentInstance();
const totalAmount = ref(0);
const {
  item_type,
} = proxy.useDict(
  "item_type",
);
const purchaseinventoryListAll = ref([])
const xiaojiTotal = ref([])
const rowSpan = ref(1)
const purposeLocationIdList = ref([])
const dischargeStatusList = ref([])
const inventoryOptions = ref([])
const purchaseinventoryList = ref([]);
const loading = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const occurrenceTime = ref([]);
const practitionerNameOptions = ref([]); 
const inventoryScope = ref(1)
const locationIdList = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    searchKey: undefined, 
    practitionerId: undefined, 
    // busNo:undefined, 
    dischargeStatus: undefined,
    // settlementStatus:undefined,
    locationId:undefined,
    // patientName: undefined, 
    dispenseTimeSTime:undefined,
    dispenseTimeETime:undefined,
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
);
watch(
  () => inventoryScope.value,
  (newVlaue) => {
   inventoryScope.value = newVlaue
  },
  { immediate: true }
);
/**计算合计金额 */
function handleTotalAmount() {
  totalAmount.value = purchaseinventoryList.value.reduce(
    (accumulator, currentRow) => {
      return (accumulator + (Number(currentRow.totalPrice) || 0))
    },
    0
  );
}
function getTotals(row,i){
    let dispensePriceSums = Number(purchaseinventoryList.value[i].dispensePrice)
    let totalReturnPriceSums = Number(purchaseinventoryList.value[i].refundPrice)
    for (let j=1; i-j>=0; j++) {
        if (purchaseinventoryList.value[i].outpatientNo == purchaseinventoryList.value[i-j].outpatientNo) {
            dispensePriceSums += Number(purchaseinventoryList.value[i-j].dispensePrice)
            totalReturnPriceSums += Number(purchaseinventoryList.value[i-j].refundPrice)
        }
    }  
    xiaojiTotal.value.push({inde:i+1,outpatientNo:row.outpatientNo,dispensePrice:dispensePriceSums,refundPrice:totalReturnPriceSums})
    // var dispensePrice2 = 0
    // var refundPrice2 = 0
    purchaseinventoryList.value.splice(i+1, 0, {outpatientNo:row.outpatientNo,prescriptionNo:'小计',dispensePrice:dispensePriceSums.toFixed(4)||0,refundPrice:totalReturnPriceSums.toFixed(4)||0})
    // purchaseinventoryList.value.map(k=>{
    //     if(k.prescriptionNo!='小计'){
    //         dispensePrice2 += Number(k.dispensePrice)
    //         refundPrice2 += Number(k.refundPrice)
    //     }
    // })
    // dispensePrice2 = dispensePrice2?dispensePrice2.toFixed(4):dispensePrice2
    // refundPrice2 = refundPrice2?refundPrice2.toFixed(4):refundPrice2
    // purchaseinventoryList.value.push({prescriptionNo:'合计',dispensePrice:dispensePrice2,refundPrice:refundPrice2})
}
const arraySpanMethod = ({
  row,
  column,
  rowIndex,
  columnIndex,
}) => {
// 合并一样的列住院号 outpatientNumber属性
  if (columnIndex === 1&&(inventoryScope.value==2||inventoryScope.value==4||inventoryScope.value==6)) {
    if (rowIndex === 0 || (rowIndex > 0 && row.outpatientNo !== purchaseinventoryList.value[rowIndex - 1]?.outpatientNo)) {
      let rowspan = 1;
      let dispensePriceSum = 0
      let totalReturnPriceSum = 0
      for (let i = rowIndex + 1; i < purchaseinventoryList.value.length; i++) {
        if (purchaseinventoryList.value[i].outpatientNo === row.outpatientNo) {
          rowspan++;
          dispensePriceSum += Number(purchaseinventoryList.value[i].dispensePrice)
          totalReturnPriceSum += Number(purchaseinventoryList.value[i].refundPrice)
          if(i==purchaseinventoryList.value.length-1){
            let findIndexTotal = xiaojiTotal.value.findIndex(k=>k.outpatientNo==row.outpatientNo)
            if(findIndexTotal<0){
               getTotals(row,i)
            }
          }
        } else {
            dispensePriceSum += Number(row.dispensePrice)
            totalReturnPriceSum += Number(row.refundPrice)
            let findIndexTotal = xiaojiTotal.value.findIndex(k=>k.outpatientNo==row.outpatientNo)
            if(findIndexTotal<0){
                xiaojiTotal.value.push({inde:i,outpatientNo:row.outpatientNo,dispensePrice:dispensePriceSum,refundPrice:totalReturnPriceSum})
                purchaseinventoryList.value.splice(i, 0, {outpatientNo:row.outpatientNo,prescriptionNo:'小计',dispensePrice:dispensePriceSum.toFixed(4)||0,refundPrice:totalReturnPriceSum.toFixed(4)||0})
            }
            break;
        }
      }
      return { rowspan, colspan: 1 };
    } else {
      return { rowspan: 0, colspan: 0 };
    }
  }
}
const getSummaries = (param) => {
  const { columns, data } = param
  const sums = []
  columns.forEach((column, index) => {
    if (index === 0) {
      sums[index] = h('div', { style: { textDecoration: 'underline' } }, [
        '合计',
      ])
      return
    }
    const values = data.map((item) => (item.prescriptionNo!='小计'&&Number(item[column.property])))
    if (column.property=='refundPrice'||column.property=='dispensePrice'||column.property=='totalPrice') {
        // !values.every((value) => Number.isNaN(value))
        sums[index] = `${values.reduce((prev, curr) => {
            const value = Number(curr)
            if (!Number.isNaN(value)) {
            return (Number(prev) + Number(curr)).toFixed(4) || 0
            } else {
            return Number(prev)
            }
       }, 0)}`
    } else {
      sums[index] = ' '
    }
  })
  return sums
}

function inventoryChange(val){
    inventoryScope.value = val
    xiaojiTotal.value = []
    purchaseinventoryList.value = []
    getList()
}
function getPharmacyCabinetLists() {
  // 统计类型
  inventoryOptions.value = [
    {value:1,label:'门诊人员发药明细'},
    {value:2,label:'门诊发药明细流水账'},
    {value:3,label:'住院人员发药明细账'},
    {value:4,label:'住院发药明细流水账'},
    {value:5,label:'住院耗材记账领用明细'},
    {value:6,label:'住院耗材记账领用流水账'}
  ]
//   practitionerNameOptions.value = [{value:' ',label:'全部'},{value:"2",label:'超级管理员'},{value:"1925368521993523202",label:'郭鑫'}]
  purposeLocationIdList.value = [{value:' ',label:'全部'},{value:1,label:'已结算'},{value:2,label:'未结算'}]
  dischargeStatusList.value = [{value:' ',label:'全部'},{value:1,label:'已出院'},{value:2,label:'未出院'}]
  getPharmacyCabinetList().then((response) => {
    locationIdList.value = response.data
  })
  getMedicationDetailsInit().then((response) => {
    practitionerNameOptions.value = response.data.practitionerList
  })
  
}
/** 查询调拨管理项目列表 */
function getList(type) { 
    if(type){
      xiaojiTotal.value = []
      purchaseinventoryList.value = []
    }
    loading.value = true;
    // let queryParamsValues ={...queryParams.value}
    // delete queryParamsValues.inventoryScope
    if(inventoryScope.value==1||inventoryScope.value==3||inventoryScope.value==5){
        var dispensePrice = 0
        var refundPrice = 0
        getAmbPractitionerDetail(queryParams.value).then((res) => {
          purchaseinventoryList.value = res.data.records
          total.value = res.data.total;
          if(purchaseinventoryList.value.length==0){
            return
          }
          purchaseinventoryList.value.map(k=>{
            k.dispensePrice = k.dispensePrice || '0.00'
            k.refundPrice = k.refundPrice || '0.00'
            k.dispenseQuantity = k.dispenseQuantity?(k.dispenseQuantity+k.unitCode_dictText):('0.00'+ k.unitCode_dictText)
            k.refundQuantity = k.refundQuantity?(k.refundQuantity+k.refundUnitCode_dictText):('0.00'+ k.unitCode_dictText)
            if(total.value&&total.value<=queryParams.value.pageSize){
              dispensePrice += Number(k.dispensePrice)
              refundPrice += Number(k.refundPrice)
            }
          })
          // 1页数据
          if(total.value&&total.value<=queryParams.value.pageSize){
            dispensePrice = dispensePrice?dispensePrice.toFixed(4):dispensePrice
            refundPrice = refundPrice?refundPrice.toFixed(4):refundPrice
            let pageNoAll = total.value / queryParams.value.pageSize
            if(Math.ceil(pageNoAll)==queryParams.value.pageNo){
              purchaseinventoryList.value.push({practitionerName:'合计',dispensePrice:dispensePrice,refundPrice:refundPrice})
            }
          }
          loading.value = false;
            // 带分页数据
          if(total.value&&total.value>queryParams.value.pageSize){
            let queryParamsValue = {
              pageNo:1,
              pageSize:total.value
            }
            getAmbPractitionerDetail(queryParamsValue).then((res) => {
              purchaseinventoryListAll.value = res.data.records||[]
              if(purchaseinventoryListAll.value.length>0){
                purchaseinventoryListAll.value.map(k=>{
                  k.dispensePrice = k.dispensePrice || '0.00'
                  k.refundPrice = k.refundPrice || '0.00'
                  k.dispenseQuantity = k.dispenseQuantity?(k.dispenseQuantity+k.unitCode_dictText):('0.00'+ k.unitCode_dictText)
                  k.refundQuantity = k.refundQuantity?(k.refundQuantity+k.refundUnitCode_dictText):('0.00'+ k.unitCode_dictText)
                  dispensePrice += Number(k.dispensePrice)
                  refundPrice += Number(k.refundPrice)
                })
                dispensePrice = dispensePrice?dispensePrice.toFixed(4):dispensePrice
                refundPrice = refundPrice?refundPrice.toFixed(4):refundPrice
                let pageNoAll = total.value / queryParams.value.pageSize
                if(Math.ceil(pageNoAll)==queryParams.value.pageNo){
                  purchaseinventoryList.value.push({practitionerName:'合计',dispensePrice:dispensePrice,refundPrice:refundPrice})
                }
              }
            })
          }
        });
    }else if(inventoryScope.value==2||inventoryScope.value==4||inventoryScope.value==6){
        var dispensePrice2 = 0
        var refundPrice2 = 0
        getAmbMedicationDetail(queryParams.value).then((res) => {
            purchaseinventoryList.value = res.data.records
            total.value = res.data.total;
            if(purchaseinventoryList.value.length==0){
              return
            }
            purchaseinventoryList.value.map(k=>{
              k.dispensePrice = k.dispensePrice || '0.00'
              k.refundPrice = k.refundPrice || '0.00'
              k.dispenseQuantity = k.dispenseQuantity?(k.dispenseQuantity+k.unitCode_dictText):('0.00'+ k.unitCode_dictText)
              k.refundQuantity = k.refundQuantity?(k.refundQuantity+k.refundUnitCode_dictText):('0.00'+ k.unitCode_dictText)
              if(total.value&&total.value<=queryParams.value.pageSize){
                  dispensePrice2 += Number(k.dispensePrice)
                  refundPrice2 += Number(k.refundPrice)
              }
            })
            // 1页数据
            if(total.value&&total.value<=queryParams.value.pageSize){
                dispensePrice2 = dispensePrice2?dispensePrice2.toFixed(4):dispensePrice2
                refundPrice2 = refundPrice2?refundPrice2.toFixed(4):refundPrice2
                let pageNoAll = total.value / queryParams.value.pageSize
                if(Math.ceil(pageNoAll)==queryParams.value.pageNo){
                    purchaseinventoryList.value.push({prescriptionNo:'合计',dispensePrice:dispensePrice2,refundPrice:refundPrice2})
                }
            }
            loading.value = false;
            // 带分页数据
            if(total.value&&total.value>queryParams.value.pageSize){
                let queryParamsValue = {
                    pageNo:1,
                    pageSize:total.value
                }
                getAmbMedicationDetail(queryParamsValue).then((res) => {
                    purchaseinventoryListAll.value = res.data.records
                    if(purchaseinventoryListAll.value.length==0){
                      return
                    }
                    purchaseinventoryListAll.value.map((k,index)=>{
                      k.dispensePrice = k.dispensePrice || '0.00'
                      k.refundPrice = k.refundPrice || '0.00'
                      k.dispenseQuantity = k.dispenseQuantity?(k.dispenseQuantity+k.unitCode_dictText):('0.00'+ k.unitCode_dictText)
                      k.refundQuantity = k.refundQuantity?(k.refundQuantity+k.refundUnitCode_dictText):('0.00'+ k.unitCode_dictText)
                      dispensePrice2 += Number(k.dispensePrice)
                      refundPrice2 += Number(k.refundPrice)
                      //处理每页门诊号相同数据分开不在一页的问题start
                      for(let m = 1;m<queryParams.value.pageSize&&m<index;m++){
                        if (queryParams.value.pageNo>1&&index == (queryParams.value.pageSize*(queryParams.value.pageNo-1))&& 
                          k.outpatientNo == purchaseinventoryListAll.value[index - m]?.outpatientNo
                        ) {
                          purchaseinventoryList.value.unshift(purchaseinventoryListAll.value[index-m]) // 加
                          //修改计算的小计缺少加过来的部分
                          let dispenseNoIndex1 = purchaseinventoryList.value.findIndex(o=>o.prescriptionNo=='小计'&&o.outpatientNo==purchaseinventoryListAll.value[index-m].outpatientNo)
                          purchaseinventoryList.value[dispenseNoIndex1].dispensePrice = Number(purchaseinventoryList.value[dispenseNoIndex1].dispensePrice)+Number(purchaseinventoryListAll.value[index-m].dispensePrice)
                          purchaseinventoryList.value[dispenseNoIndex1].refundPrice = Number(purchaseinventoryList.value[dispenseNoIndex1].refundPrice)+Number(purchaseinventoryListAll.value[index-m].refundPrice)
                        } 

                        if ((index+m) == (queryParams.value.pageSize*queryParams.value.pageNo)&& k.outpatientNo == purchaseinventoryListAll.value[index + m]?.outpatientNo) {
                          let dispenseNoIndex = purchaseinventoryList.value.findIndex(o=>o.dispenseNo==k.dispenseNo)
                          purchaseinventoryList.value.splice(dispenseNoIndex,1) // 减
                         
                          if(purchaseinventoryList.value[dispenseNoIndex-1]&&purchaseinventoryList.value[dispenseNoIndex-1].prescriptionNo=='小计'){
                            purchaseinventoryList.value.splice(dispenseNoIndex+1,1) // 减掉带小计的
                          }
                        } 
                      }
                      // end
                    })
                    dispensePrice2 = dispensePrice2?dispensePrice2.toFixed(4):dispensePrice2
                    refundPrice2 = refundPrice2?refundPrice2.toFixed(4):refundPrice2
                    let pageNoAll = total.value / queryParams.value.pageSize
                    if(Math.ceil(pageNoAll)==queryParams.value.pageNo){
                      purchaseinventoryList.value.push({prescriptionNo:'合计',dispensePrice:dispensePrice2,refundPrice:refundPrice2})
                    }
                })
            }
        });
    }else{
        total.value =0
        purchaseinventoryList.value = [
            {outpatientNo:'M2505070009100304',prescriptionNo:'CF2505070009101361',dispenseTime:'2025-05-07 16:28',practitionerName:'张三',practitionerName:'王东收费',medicationName:'依折麦布片',busNo:'yp3065',medicalName:'依折麦布片',ybNo:'XC10AXY097A0010',totalVolume:'10mg*10',salePrice:'63.56',price:'63.56',totalPrice:'63.56',dispenseQuantity:'1盒',dispensePrice:'63.56',refundQuantity:'0.00盒',refundPrice:'0.00',lotNumber:'Y013201',manufacturerText:'杭州默沙东制药有限公司',supplierName:'旧系统批量'},
            {outpatientNo:'M2505070009100304',prescriptionNo:'CF2505070009101362',dispenseTime:'2025-05-07 16:28',practitionerName:'张三',practitionerName:'王东收费',medicationName:'布洛芬缓释胶囊',busNo:'yp3065',medicalName:'布洛芬缓释胶囊',ybNo:'XC10AXY097A0010',totalVolume:'0.3g*24',salePrice:'4.8',price:'4.8',totalPrice:'4.8',dispenseQuantity:'1盒',dispensePrice:'4.8',refundQuantity:'0.00盒',refundPrice:'0.00',lotNumber:'72240709',manufacturerText:'上海信谊天平药业有限公司',supplierName:'旧系统批量'},
            {outpatientNo:'M2505070009100305',prescriptionNo:'CF2505070009101363',dispenseTime:'2025-05-07 16:28',practitionerName:'张三',practitionerName:'王东收费',medicationName:'布洛芬缓释胶囊',busNo:'yp3065',medicalName:'布洛芬缓释胶囊',ybNo:'XC10AXY097A0010',totalVolume:'0.3g*24',salePrice:'4.8',price:'4.8',totalPrice:'4.8',dispenseQuantity:'1盒',dispensePrice:'4.8',refundQuantity:'0.00盒',refundPrice:'0.00',lotNumber:'72240709',manufacturerText:'上海信谊天平药业有限公司',supplierName:'旧系统批量'},
            {outpatientNo:'M2505070009100305',prescriptionNo:'CF2505070009101364',dispenseTime:'2025-05-07 16:28',practitionerName:'张三',practitionerName:'王东收费',medicationName:'依折麦布片',busNo:'yp3065',medicalName:'依折麦布片',ybNo:'XC10AXY097A0010',totalVolume:'10mg*10',salePrice:'63.56',price:'63.56',totalPrice:'63.56',dispenseQuantity:'1盒',dispensePrice:'63.56',refundQuantity:'0.00盒',refundPrice:'0.00',lotNumber:'Y013201',manufacturerText:'杭州默沙东制药有限公司',supplierName:'旧系统批量'},
            {outpatientNo:'M2505070009100305',prescriptionNo:'CF2505070009101365',dispenseTime:'2025-05-07 16:28',practitionerName:'张三',practitionerName:'王东收费',medicationName:'布洛芬缓释胶囊',busNo:'yp3065',medicalName:'布洛芬缓释胶囊',ybNo:'XC10AXY097A0010',totalVolume:'0.3g*24',salePrice:'4.8',price:'4.8',totalPrice:'4.8',dispenseQuantity:'1盒',dispensePrice:'4.8',refundQuantity:'0.00盒',refundPrice:'0.00',lotNumber:'72240709',manufacturerText:'上海信谊天平药业有限公司',supplierName:'旧系统批量'},
            {outpatientNo:'M2505070009100305',prescriptionNo:'CF2505070009101366',dispenseTime:'2025-05-07 16:28',practitionerName:'张三',practitionerName:'王东收费',medicationName:'布洛芬缓释胶囊',busNo:'yp3065',medicalName:'布洛芬缓释胶囊',ybNo:'XC10AXY097A0010',totalVolume:'0.3g*24',salePrice:'4.8',price:'4.8',totalPrice:'4.8',dispenseQuantity:'1盒',dispensePrice:'4.8',refundQuantity:'0.00盒',refundPrice:'0.00',lotNumber:'72240709',manufacturerText:'上海信谊天平药业有限公司',supplierName:'旧系统批量'},
            {outpatientNo:'M2505070009100306',prescriptionNo:'CF2505070009101367',dispenseTime:'2025-05-07 16:28',practitionerName:'张三',practitionerName:'王东收费',medicationName:'依折麦布片',busNo:'yp3065',medicalName:'依折麦布片',ybNo:'XC10AXY097A0010',totalVolume:'10mg*10',salePrice:'63.56',price:'63.56',totalPrice:'63.56',dispenseQuantity:'1盒',dispensePrice:'63.56',refundQuantity:'0.00盒',refundPrice:'0.00',lotNumber:'Y013201',manufacturerText:'杭州默沙东制药有限公司',supplierName:'旧系统批量'},
            {outpatientNo:'M2505070009100306',prescriptionNo:'CF2505070009101368',dispenseTime:'2025-05-07 16:28',practitionerName:'张三',practitionerName:'王东收费',medicationName:'布洛芬缓释胶囊',busNo:'yp3065',medicalName:'布洛芬缓释胶囊',ybNo:'XC10AXY097A0010',totalVolume:'0.3g*24',salePrice:'4.8',price:'4.8',totalPrice:'4.8',dispenseQuantity:'1盒',dispensePrice:'4.8',refundQuantity:'0.00盒',refundPrice:'0.00',lotNumber:'72240709',manufacturerText:'上海信谊天平药业有限公司',supplierName:'旧系统批量'},
            {outpatientNo:'M2505070009100306',prescriptionNo:'CF2505070009101369',dispenseTime:'2025-05-07 16:28',practitionerName:'张三',practitionerName:'王东收费',medicationName:'布洛芬缓释胶囊',busNo:'yp3065',medicalName:'布洛芬缓释胶囊',ybNo:'XC10AXY097A0010',totalVolume:'0.3g*24',salePrice:'4.8',price:'4.8',totalPrice:'4.8',dispenseQuantity:'1盒',dispensePrice:'4.8',refundQuantity:'0.00盒',refundPrice:'0.00',lotNumber:'72240709',manufacturerText:'上海信谊天平药业有限公司',supplierName:'旧系统批量'},
            {outpatientNo:'M2505070009100307',prescriptionNo:'CF2505070009101368',dispenseTime:'2025-05-07 16:28',practitionerName:'张三',practitionerName:'王东收费',medicationName:'布洛芬缓释胶囊',busNo:'yp3065',medicalName:'布洛芬缓释胶囊',ybNo:'XC10AXY097A0010',totalVolume:'0.3g*24',salePrice:'4.8',price:'4.8',totalPrice:'4.8',dispenseQuantity:'1盒',dispensePrice:'4.8',refundQuantity:'0.00盒',refundPrice:'0.00',lotNumber:'72240709',manufacturerText:'上海信谊天平药业有限公司',supplierName:'旧系统批量'},
            {outpatientNo:'M2505070009100307',prescriptionNo:'CF2505070009101369',dispenseTime:'2025-05-07 16:28',practitionerName:'张三',practitionerName:'王东收费',medicationName:'布洛芬缓释胶囊',busNo:'yp3065',medicalName:'布洛芬缓释胶囊',ybNo:'XC10AXY097A0010',totalVolume:'0.3g*24',salePrice:'2.8',price:'2.8',totalPrice:'2.8',dispenseQuantity:'1盒',dispensePrice:'2.8',refundQuantity:'0.00盒',refundPrice:'0.00',lotNumber:'72240709',manufacturerText:'上海信谊天平药业有限公司',supplierName:'旧系统批量'},
        ]
        loading.value = false;
    }
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.dispenseTimeSTime =
    occurrenceTime.value && occurrenceTime.value.length == 2
      ? occurrenceTime.value[0] + " 00:00:00"
      : "";
  queryParams.value.dispenseTimeETime =
    occurrenceTime.value && occurrenceTime.value.length == 2
      ? occurrenceTime.value[1] + " 23:59:59"
      : "";
  queryParams.value.pageNo = 1;
  getList(1);
}

/** 清空条件按钮操作 */
function handleClear() {
  // 清空查询条件
  occurrenceTime.value = ""
  queryParams.value.dispenseTimeSTime = ""
  queryParams.value.dispenseTimeETime = ""
  proxy.resetForm("queryRef");
  getList(1);
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

.pagination-container{
    margin-right: 20px;
}
</style>