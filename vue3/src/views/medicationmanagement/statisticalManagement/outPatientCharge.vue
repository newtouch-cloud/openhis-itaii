<template>
  <div busNo="app-container">
    <el-form
        style="margin-top:20px;margin-left:20px;"
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        label-width="90px"
    >
      <el-form-item label="收费时间：">
        <el-date-picker
          v-model="occurrenceTime"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 300px"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>
      <!-- @change="inventoryChange(queryParams.statisticsType)" -->
      <el-form-item label="统计类型：">
        <el-select
          v-model="queryParams.statisticsType"
          placeholder=""
          style="width: 220px"
          @change="inventoryChange(queryParams.statisticsType)"
        >
          <el-option
            v-for="supplyStatus in inventoryOptions"
            :key="supplyStatus.value"
            :label="supplyStatus.label"
            :value="supplyStatus.value"
          />
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="医保号：" prop="ybCode">
        <el-input
          v-model="queryParams.ybCode"
          placeholder=""
          clearable
          style="width: 150px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="门诊号：" prop="busNo">
        <el-input
          v-model="queryParams.busNo"
          placeholder=""
          clearable
          style="width: 150px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="病人姓名：" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder=""
          clearable
          style="width:150px"
          @keyup.enter="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="科室：" prop="departmentId">
        <el-select
          v-model="queryParams.departmentId"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="supplyStatus in getDepartmentOptions"
            :key="supplyStatus.id"
            :label="supplyStatus.name"
            :value="supplyStatus.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="开单人：" prop="issuerId">
        <el-select
          v-model="queryParams.issuerId"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="supplyStatus in issuerOptions"
            :key="supplyStatus.id"
            :label="supplyStatus.name"
            :value="supplyStatus.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="收费人：" prop="payeeId">
        <el-select
          v-model="queryParams.payeeId"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="supplyStatus in payeeOptions"
            :key="supplyStatus.id"
            :label="supplyStatus.name"
            :value="supplyStatus.id"
          />
        </el-select>
      </el-form-item> 
      <!-- 字典item_type -->
      <el-form-item label="项目类型：" prop="clinicalType">
        <el-select
          v-model="queryParams.clinicalType"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="supplyStatus in clinicalTypeOptions" 
            :key="supplyStatus.value"
            :label="supplyStatus.label"
            :value="supplyStatus.value"
          />
        </el-select>
      </el-form-item>
      <!-- 医保号，门诊号，患者姓名，项目名，项目编码 -->
      <el-form-item label="医保号/门诊号/患者姓名/项目名/项目编码：" prop="searchKey" label-width="295">
        <el-input
          v-model="queryParams.searchKey"
          placeholder="输入医保号/门诊号/患者姓名/项目名称/项目编码后回车查询"
          clearable
          style="width: 395px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
    </el-form>

    <el-row :gutter="10" busNo="mb8" style="margin-left:20px;margin-right:0px;margin-bottom:5px">
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
      style="padding:0 20px;width:100%;height:67vh"
      v-loading="loading"
      :data="purchaseinventoryList"
      @selection-change="handleSelectionChange"
      :span-method="arraySpanMethod"
     
    >
    <!-- :span-method="arraySpanMethod" -->
     <!-- :summary-method="getSummaries"
      show-summary 每页单独合计-->
      <el-table-column type="selection" width="50px" align="center" />
      <el-table-column
        label='门诊号'
        align="center"
        key="busNo"
        prop="busNo"
        width="110px"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="科室"
        align="center"
        key="departmentName"
        prop="departmentName"
  
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="姓名"
        align="center"
        key="name"
        prop="name"
        width="90px"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        label="医保号"
        align="center"
        key="ybCode"
        prop="ybCode"
        width="190px"
        :show-overflow-tooltip="true"
      />
      
      <el-table-column
        label="药品项目"
        align="center"
        key="clinicalName"
        prop="clinicalName"

        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="医保码"
        align="center"
        key="ybNo"
        prop="ybNo"
        width="220px"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="医保类别"
        align="center"
        key="type_dictText"
        prop="type_dictText"
   
        :show-overflow-tooltip="true"
      />
      
      <el-table-column
        label="开单人"
        align="center"
        key="issuerName"
        prop="issuerName"

        :show-overflow-tooltip="true"
      />
       <el-table-column
      
        label="收费人"
        align="center"
        key="payeeName"
        prop="payeeName"
       
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="规格"
        align="center"
        key="totalVolume"
        prop="totalVolume"
        width="135px"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="数量"
        align="center"
        key="number"
        prop="number"
        
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
        label="金额"
        align="center"
        key="totalPrice"
        prop="totalPrice"
     
        :show-overflow-tooltip="true"
      /> 
      <el-table-column
        label="医保等级"
        align="center"
        key="chrgitmLv_enumText"
        prop="chrgitmLv_enumText"
     
        :show-overflow-tooltip="true"
      /> 
      <el-table-column
        label="收费时间"
        align="center"
        key="chargeTime"
        prop="chargeTime"
       
        :show-overflow-tooltip="true"
      >
        <template #default="scope">
          <span>{{ parseTime(scope.row.chargeTime) }}</span>
        </template>
      </el-table-column>
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
      busNo="mb8"
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
    </el-row>
  </div>
</template>

<script setup name="outPatientCharge">
import {
  getReportChargePage,
  getAmbPractitionerDetail,
  // getMedicationDetailsInit,
  getDepartmentList,
  getReportChargeInit
} from "./statisticalManagent";
import useUserStore from "@/store/modules/user";
import { watch } from "vue";
const userStore = useUserStore();

const router = useRouter();
const { proxy } = getCurrentInstance();
const totalAmount = ref(0);
// const {
//   item_type,
//   hosp_lv
// } = proxy.useDict(
//   "item_type",
//   "hosp_lv"
// );
const purchaseinventoryListAll = ref([])
const xiaojiTotal = ref([])
const rowSpan = ref(1)
const issuerOptions = ref([])
const payeeOptions = ref([])
const inventoryOptions = ref([])
const clinicalTypeOptions = ref([]);
const purchaseinventoryList = ref([]);
const loading = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const occurrenceTime = ref([]);
const getDepartmentOptions = ref([])
// const queryParams.value.statisticsType = ref(1)

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    statisticsType: 1, 
    searchKey:undefined,
    // ybCode: undefined, 
    // busNo:undefined, 
    // name: undefined,
    departmentId:undefined,
    issuerId:undefined,
    payeeId: undefined, 
    clinicalType:undefined,
    // clinicalName:undefined,
    chargeTimeSTime:undefined,
    chargeTimeETime:undefined,
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
  () => queryParams.value.statisticsType,
  (newVlaue) => {
   queryParams.value.statisticsType = newVlaue
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
  console.log(row,i,"代码")
    let totalPriceSums = Number(purchaseinventoryList.value[i].totalPrice)
    // let totalReturnPriceSums = Number(purchaseinventoryList.value[i].refundPrice)
    for (let j=1; i-j>=0; j++) {
      if (purchaseinventoryList.value[i].busNo == purchaseinventoryList.value[i-j].busNo) {
        totalPriceSums += Number(purchaseinventoryList.value[i-j].totalPrice)
        // totalReturnPriceSums += Number(purchaseinventoryList.value[i-j].refundPrice)
      }
    }  
    xiaojiTotal.value.push({inde:i+1,busNo:row.busNo,genderEnum_enumText:row.genderEnum_enumText,totalPrice:totalPriceSums.toFixed(4)||0.0000})
    // var totalPrice2 = 0
    // var refundPrice2 = 0
    console.log(i+1,"i+1",xiaojiTotal)
    purchaseinventoryList.value.splice(i+1, 0, {busNo:row.busNo,genderEnum_enumText:row.genderEnum_enumText,departmentName:'小计',totalPrice:totalPriceSums.toFixed(4)||0.0000})
    // purchaseinventoryList.value.map(k=>{
    //     if(k.departmentName!='小计'){
    //         totalPrice2 += Number(k.totalPrice)
    //         refundPrice2 += Number(k.refundPrice)
    //     }
    // })
    // totalPrice2 = totalPrice2?totalPrice2.toFixed(4):totalPrice2
    // refundPrice2 = refundPrice2?refundPrice2.toFixed(4):refundPrice2
    // purchaseinventoryList.value.push({departmentName:'合计',totalPrice:totalPrice2,refundPrice:refundPrice2})
}
const arraySpanMethod = ({
  row,
  column,
  rowIndex,
  columnIndex,
}) => {
// 合并一样的列住院号 outpatientNumber属性&&(queryParams.value.statisticsType==1||queryParams.value.statisticsType==4||queryParams.value.statisticsType==6)
  if (columnIndex === 1&&purchaseinventoryList.value.length>0) {
    if (rowIndex === 0 || (rowIndex > 0 && row.busNo !== purchaseinventoryList.value[rowIndex - 1]?.busNo)) {
      let rowspan = 1;
      let totalPriceSum = 0
      // let totalReturnPriceSum = 0
      for (let i = rowIndex + 1; i < purchaseinventoryList.value.length; i++) {
        if (purchaseinventoryList.value[i].busNo === row.busNo) {
          rowspan++;
          totalPriceSum += Number(purchaseinventoryList.value[i].totalPrice)
          // totalReturnPriceSum += Number(purchaseinventoryList.value[i].refundPrice)
          console.log(purchaseinventoryList.value.length-1,i,xiaojiTotal.value,row.busNo,"!212122")
          if(i==purchaseinventoryList.value.length-1){
            
            let findIndexTotal = xiaojiTotal.value.findIndex(k=>k.busNo==row.busNo)
            if(findIndexTotal<0){
              getTotals(row,i)
            }
          }else{
            console.log(purchaseinventoryList.value.length-1,i,"!21212")
          }
        } else {
            console.log(purchaseinventoryList.value.length-1,i,"!212123")
            totalPriceSum += Number(row.totalPrice)
            // totalReturnPriceSum += Number(row.refundPrice)
            let findIndexTotal = xiaojiTotal.value.findIndex(k=>k.busNo==row.busNo)
            if(findIndexTotal<0){
              xiaojiTotal.value.push({inde:i,genderEnum_enumText:row.genderEnum_enumText,busNo:row.busNo,totalPrice:totalPriceSum})
              purchaseinventoryList.value.splice(i, 0, {busNo:row.busNo,genderEnum_enumText:row.genderEnum_enumText,departmentName:'小计',totalPrice:totalPriceSum.toFixed(4)||0.0000})
            }
            break;
        }
      }
      return { rowspan, colspan: 1 };
    } else {
      return { rowspan: 0, colspan: 0 };
    }
  // }else{ // 姓名列patientName
  //   // console.log(columnIndex,"columnIndex")
  //   if (columnIndex ===2&&purchaseinventoryList.value.length>0) {
  //     if (rowIndex === 0 || (rowIndex > 0 && row.name !== purchaseinventoryList.value[rowIndex - 1]?.name)) {
  //       let rowspan = 1;
  //       for (let i = rowIndex + 1; i < purchaseinventoryList.value.length; i++) {
  //         if (purchaseinventoryList.value[i].name === row.name) {
  //           rowspan++;
  //         } else {
  //           break;
  //         }
  //       }
  //       return { rowspan:rowspan, colspan: 1 };
  //     } else {
  //       return { rowspan: 0, colspan: 0 };
  //     }
  //   }
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
    const values = data.map((item) => (item.departmentName!='小计'&&Number(item[column.property])))
    if (column.property=='totalPrice') {
      sums[index] = `${values.reduce((prev, curr) => {
          const value = Number(curr)
          if (!Number.isNaN(value)) {
          return (Number(prev) + Number(curr)).toFixed(4) || 0.0000
          } else {
          return Number(prev)
          }
      }, 0.0000)}`
    } else {
      sums[index] = ' '
    }
  })
  return sums
}

function inventoryChange(val){
    queryParams.value.statisticsType = val
    xiaojiTotal.value = []
    purchaseinventoryList.value = []
    getList()
}
function getPharmacyCabinetLists() {
  getReportChargeInit().then((response)=>{
    inventoryOptions.value = response.data.statisticsTypeOptions
    queryParams.value.statisticsType = 1
    issuerOptions.value = response.data.issuerOptions
    payeeOptions.value = response.data.payeeOptions
    clinicalTypeOptions.value = response.data.clinicalTypeOptions
  })
  getDepartmentList().then((response) => {
    getDepartmentOptions.value = response.data // 科室
  })

}
/** 查询调拨管理项目列表 */
function getList(type) { 
  if(type){
    xiaojiTotal.value = []
    purchaseinventoryList.value = []
  }
  loading.value = true;
  // purchaseinventoryList.value = [
  //   {busNo:'M2505070009100304',name:"张三",departmentName:'CF2505070009101361',birthDate:'2025-05-07 16:28',practitionerName:'张三',practitionerName:'王东收费',clinicalName:'依折麦布片',busNo:'yp3065',medicalName:'依折麦布片',ybNo:'XC10AXY097A0010',totalVolume:'10mg*10',salePrice:'63.56',price:'63.56',totalPrice:'63.56',number:'1盒',totalPrice:'63.56',refundQuantity:'0.00盒',refundPrice:'0.00',lotNumber:'Y013201',manufacturerText:'杭州默沙东制药有限公司',supplierName:'旧系统批量'},
  //   {busNo:'M2505070009100304',name:"张三",departmentName:'CF2505070009101362',birthDate:'2025-05-07 16:28',practitionerName:'张三',practitionerName:'王东收费',clinicalName:'布洛芬缓释胶囊',busNo:'yp3065',medicalName:'布洛芬缓释胶囊',ybNo:'XC10AXY097A0010',totalVolume:'0.3g*24',salePrice:'4.8',price:'4.8',totalPrice:'4.8',number:'1盒',totalPrice:'4.8',refundQuantity:'0.00盒',refundPrice:'0.00',lotNumber:'72240709',manufacturerText:'上海信谊天平药业有限公司',supplierName:'旧系统批量'},
  //   {busNo:'M2505070009100305',name:"李四",departmentName:'CF2505070009101363',birthDate:'2025-05-07 16:28',practitionerName:'张三',practitionerName:'王东收费',clinicalName:'布洛芬缓释胶囊',busNo:'yp3065',medicalName:'布洛芬缓释胶囊',ybNo:'XC10AXY097A0010',totalVolume:'0.3g*24',salePrice:'4.8',price:'4.8',totalPrice:'4.8',number:'1盒',totalPrice:'4.8',refundQuantity:'0.00盒',refundPrice:'0.00',lotNumber:'72240709',manufacturerText:'上海信谊天平药业有限公司',supplierName:'旧系统批量'},
  //   {busNo:'M2505070009100305',name:"李四",departmentName:'CF2505070009101364',birthDate:'2025-05-07 16:28',practitionerName:'张三',practitionerName:'王东收费',clinicalName:'依折麦布片',busNo:'yp3065',medicalName:'依折麦布片',ybNo:'XC10AXY097A0010',totalVolume:'10mg*10',salePrice:'63.56',price:'63.56',totalPrice:'63.56',number:'1盒',totalPrice:'63.56',refundQuantity:'0.00盒',refundPrice:'0.00',lotNumber:'Y013201',manufacturerText:'杭州默沙东制药有限公司',supplierName:'旧系统批量'},
  //   {busNo:'M2505070009100305',name:"李四",departmentName:'CF2505070009101365',birthDate:'2025-05-07 16:28',practitionerName:'张三',practitionerName:'王东收费',clinicalName:'布洛芬缓释胶囊',busNo:'yp3065',medicalName:'布洛芬缓释胶囊',ybNo:'XC10AXY097A0010',totalVolume:'0.3g*24',salePrice:'4.8',price:'4.8',totalPrice:'4.8',number:'1盒',totalPrice:'4.8',refundQuantity:'0.00盒',refundPrice:'0.00',lotNumber:'72240709',manufacturerText:'上海信谊天平药业有限公司',supplierName:'旧系统批量'},
  //   {busNo:'M2505070009100305',name:"李四",departmentName:'CF2505070009101366',birthDate:'2025-05-07 16:28',practitionerName:'张三',practitionerName:'王东收费',clinicalName:'布洛芬缓释胶囊',busNo:'yp3065',medicalName:'布洛芬缓释胶囊',ybNo:'XC10AXY097A0010',totalVolume:'0.3g*24',salePrice:'4.8',price:'4.8',totalPrice:'4.8',number:'1盒',totalPrice:'4.8',refundQuantity:'0.00盒',refundPrice:'0.00',lotNumber:'72240709',manufacturerText:'上海信谊天平药业有限公司',supplierName:'旧系统批量'},
  //   {busNo:'M2505070009100306',name:"王五",departmentName:'CF2505070009101367',birthDate:'2025-05-07 16:28',practitionerName:'张三',practitionerName:'王东收费',clinicalName:'依折麦布片',busNo:'yp3065',medicalName:'依折麦布片',ybNo:'XC10AXY097A0010',totalVolume:'10mg*10',salePrice:'63.56',price:'63.56',totalPrice:'63.56',number:'1盒',totalPrice:'63.56',refundQuantity:'0.00盒',refundPrice:'0.00',lotNumber:'Y013201',manufacturerText:'杭州默沙东制药有限公司',supplierName:'旧系统批量'},
  //   {busNo:'M2505070009100306',name:"王五",departmentName:'CF2505070009101368',birthDate:'2025-05-07 16:28',practitionerName:'张三',practitionerName:'王东收费',clinicalName:'布洛芬缓释胶囊',busNo:'yp3065',medicalName:'布洛芬缓释胶囊',ybNo:'XC10AXY097A0010',totalVolume:'0.3g*24',salePrice:'4.8',price:'4.8',totalPrice:'4.8',number:'1盒',totalPrice:'4.8',refundQuantity:'0.00盒',refundPrice:'0.00',lotNumber:'72240709',manufacturerText:'上海信谊天平药业有限公司',supplierName:'旧系统批量'},
  //   {busNo:'M2505070009100306',name:"王五",departmentName:'CF2505070009101369',birthDate:'2025-05-07 16:28',practitionerName:'张三',practitionerName:'王东收费',clinicalName:'布洛芬缓释胶囊',busNo:'yp3065',medicalName:'布洛芬缓释胶囊',ybNo:'XC10AXY097A0010',totalVolume:'0.3g*24',salePrice:'4.8',price:'4.8',totalPrice:'4.8',number:'1盒',totalPrice:'4.8',refundQuantity:'0.00盒',refundPrice:'0.00',lotNumber:'72240709',manufacturerText:'上海信谊天平药业有限公司',supplierName:'旧系统批量'},
  //   {busNo:'M2505070009100307',name:"赵六",departmentName:'CF2505070009101368',birthDate:'2025-05-07 16:28',practitionerName:'张三',practitionerName:'王东收费',clinicalName:'布洛芬缓释胶囊',busNo:'yp3065',medicalName:'布洛芬缓释胶囊',ybNo:'XC10AXY097A0010',totalVolume:'0.3g*24',salePrice:'4.8',price:'4.8',totalPrice:'4.8',number:'1盒',totalPrice:'4.8',refundQuantity:'0.00盒',refundPrice:'0.00',lotNumber:'72240709',manufacturerText:'上海信谊天平药业有限公司',supplierName:'旧系统批量'},
  //   {busNo:'M2505070009100307',name:"赵六",departmentName:'CF2505070009101369',birthDate:'2025-05-07 16:28',practitionerName:'张三',practitionerName:'王东收费',clinicalName:'布洛芬缓释胶囊',busNo:'yp3065',medicalName:'布洛芬缓释胶囊',ybNo:'XC10AXY097A0010',totalVolume:'0.3g*24',salePrice:'2.8',price:'2.8',totalPrice:'2.8',number:'1盒',totalPrice:'2.8',refundQuantity:'0.00盒',refundPrice:'0.00',lotNumber:'72240709',manufacturerText:'上海信谊天平药业有限公司',supplierName:'旧系统批量'},
  // ]
  var totalPrice2 = 0
  // var refundPrice2 = 0
  // var purchaseinventoryLists = []
  // var purchaseinventoryListsArray = []
  console.log(queryParams.value,"queryParams.value")
  getReportChargePage(queryParams.value).then((res) => {
    // purchaseinventoryLists = res.data.records||[] //转换数据结构
    // if(purchaseinventoryLists.length>0){
    //   purchaseinventoryLists.map(p=>{
    //     // console.log(p.reportPageItemDto,"p.reportPageItemDto")
    //     purchaseinventoryListsArray = [...purchaseinventoryListsArray, ...p.reportPageItemDto]
    //     // purchaseinventoryList.value.concat(p.reportPageItemDto)
    //   }) 
    // }
    // console.log(purchaseinventoryList.value,"purchaseinventoryList.value")
    // purchaseinventoryList.value = purchaseinventoryListsArray
    purchaseinventoryList.value = res.data.records||[]
    total.value = res.data.total
    // console.log(purchaseinventoryList.value,"purchaseinventoryList.value")
    purchaseinventoryList.value.map(k=>{
      k.totalPrice = k.totalPrice?k.totalPrice.toFixed(4):'0.0000'
      k.price = k.price?k.price.toFixed(4):'0.0000'
      // k.refundPrice = k.refundPrice || '0.00'
      k.number = k.number?(k.number+(k.quantityUnit_dictText?k.quantityUnit_dictText:"")):('0.0000'+ k.quantityUnit_dictText?k.quantityUnit_dictText:"")
      // k.refundQuantity = k.refundQuantity?(k.refundQuantity+k.refundUnitCode_dictText):('0.00'+ k.quantityUnit_dictText)
      if(total.value&&total.value<=queryParams.value.pageSize){
          totalPrice2 += Number(k.totalPrice)
          // refundPrice2 += Number(k.refundPrice)
      }
    })
    if(total.value<=res.data.size){
      loading.value = false;
    }
    // 1页数据
    if(total.value&&total.value<=queryParams.value.pageSize){
      totalPrice2 = totalPrice2?totalPrice2.toFixed(4):totalPrice2
      // refundPrice2 = refundPrice2?refundPrice2.toFixed(4):refundPrice2
      let pageNoAll = total.value / queryParams.value.pageSize
      if(Math.ceil(pageNoAll)==queryParams.value.pageNo){
          purchaseinventoryList.value.push({departmentName:'合计',totalPrice:totalPrice2})
      }
    }
   
    // 带分页数据
    if(total.value&&total.value>queryParams.value.pageSize){
      // var purchaseinventoryListsAll = []
      // var purchaseinventoryListsArrayAll = []
      let queryParamsValue = {...queryParams.value}
      queryParamsValue.pageSize = total.value
      queryParamsValue.pageNo = 1
      getReportChargePage(queryParamsValue).then((res) => {
        // purchaseinventoryListsAll = res.data.records||[]
        // if(purchaseinventoryListsAll.length>0){
        //   purchaseinventoryListsAll.map(p=>{
        //     // console.log(p.reportPageItemDto,"p.reportPageItemDto")
        //     purchaseinventoryListsArrayAll = [...purchaseinventoryListsArrayAll, ...p.reportPageItemDto]
        //     // purchaseinventoryList.value.concat(p.reportPageItemDto)
        //   }) 
        // }
        // purchaseinventoryListAll.value = purchaseinventoryListsArrayAll
        purchaseinventoryListAll.value = res.data.records||[]
        if(purchaseinventoryListAll.value.length>0){
          purchaseinventoryListAll.value.map((k,index)=>{
            k.totalPrice = k.totalPrice?k.totalPrice.toFixed(4):'0.0000'
            k.price = k.price?k.price.toFixed(4):'0.0000'
            // k.refundPrice = k.refundPrice || '0.00'
            k.number = k.number?(k.number+k.quantityUnit_dictText?k.quantityUnit_dictText:""):('0.0000'+ k.quantityUnit_dictText?k.quantityUnit_dictText:"")
            // k.refundQuantity = k.refundQuantity?(k.refundQuantity+k.refundUnitCode_dictText):('0.00'+ k.quantityUnit_dictText)
            totalPrice2 += Number(k.totalPrice)
            // refundPrice2 += Number(k.refundPrice)
            //处理不同页门诊号相同小计在最后回显问题start
            // m<queryParams.value.pageSize&&
            for(let m = 1;m<index;m++){
              if (queryParams.value.pageNo>1&&index == (queryParams.value.pageSize*(queryParams.value.pageNo-1))&& 
                k.busNo == purchaseinventoryListAll.value[index - m]?.busNo
              ) {
              //   purchaseinventoryList.value.unshift(purchaseinventoryListAll.value[index-m]) // 加
              //   //修改计算的小计缺少加过来的部分
                let dispenseNoIndex1 = purchaseinventoryList.value.findIndex(o=>o.departmentName=='小计'&&o.busNo==purchaseinventoryListAll.value[index-m].busNo)
               
                if(dispenseNoIndex1>0){
                  purchaseinventoryList.value[dispenseNoIndex1].totalPrice = Number(purchaseinventoryList.value[dispenseNoIndex1].totalPrice)+Number(purchaseinventoryListAll.value[index-m].totalPrice)
                  purchaseinventoryList.value[dispenseNoIndex1].totalPrice = purchaseinventoryList.value[dispenseNoIndex1].totalPrice?purchaseinventoryList.value[dispenseNoIndex1].totalPrice.toFixed(4):'0.0000'
                }
              }
              if ((index+m) == (queryParams.value.pageSize*queryParams.value.pageNo)&& k.busNo == purchaseinventoryListAll.value[index + m]?.busNo) {
                if(purchaseinventoryList.value[purchaseinventoryList.value.length-1].departmentName=='小计'){
                  purchaseinventoryList.value.splice(purchaseinventoryList.value.length-1,1) // 减掉第二页还有相同门诊号的第一页的小计
                }
                // let dispenseNoIndex = purchaseinventoryList.value.findIndex(o=>o.paymentId==k.paymentId&&o.chargeId==k.chargeId) //通过唯一主键进行区分
                // purchaseinventoryList.value.splice(dispenseNoIndex,1) // 减
                // if(purchaseinventoryList.value[dispenseNoIndex-1]&&purchaseinventoryList.value[dispenseNoIndex-1].departmentName=='小计'){
                //   purchaseinventoryList.value.splice(dispenseNoIndex+1,1) // 减掉带小计的
                // }
              } 
            }
            // end
          })
          totalPrice2 = totalPrice2?totalPrice2.toFixed(4):totalPrice2
          // refundPrice2 = refundPrice2?refundPrice2.toFixed(4):refundPrice2
          loading.value = false;
          let pageNoAll = total.value / queryParams.value.pageSize
          if(Math.ceil(pageNoAll)==queryParams.value.pageNo){
            purchaseinventoryList.value.push({departmentName:'合计',totalPrice:totalPrice2})
          }
        }
      })
    }
  });
}
  
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.chargeTimeSTime =
    occurrenceTime.value && occurrenceTime.value.length == 2
      ? occurrenceTime.value[0] + " 00:00:00"
      : "";
  queryParams.value.chargeTimeETime =
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
  queryParams.value.chargeTimeSTime = ""
  queryParams.value.chargeTimeETime = ""
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