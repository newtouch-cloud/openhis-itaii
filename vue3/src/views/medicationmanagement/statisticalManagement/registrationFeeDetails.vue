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
      <el-form-item label="医生：" prop="doctorId">
        <el-select
          v-model="queryParams.doctorId"
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
      style="padding:0 20px;width:100%"
      v-loading="loading"
      :data="purchaseinventoryList"
      @selection-change="handleSelectionChange"
      :span-method="arraySpanMethod"
     
    >
     <!-- :summary-method="getSummaries"
      show-summary 每页单独合计-->
      <el-table-column type="selection"  align="center" />
      <el-table-column
        label='门诊号'
        align="center"
        key="busNo"
        prop="busNo"

        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="姓名"
        align="center"
        key="name"
        prop="name"
    
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
        label="项目"
        align="center"
        key="clinicalName"
        prop="clinicalName"

        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="挂号医生"
        align="center"
        key="doctorName"
        prop="doctorName"
      
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
        label="时间"
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
      @pagination="getList()"
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

<script setup name="outPatientRegister">
import {
  getReportRegisterPage,
  getDepartmentList,
  getReportRegisterInit
} from "./statisticalManagent";
import useUserStore from "@/store/modules/user";
import { watch } from "vue";
const userStore = useUserStore();

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

const currentCategory =  ref(null);
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
const payeeNameOptions = ref([]); 
const getDepartmentOptions = ref([])

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    departmentId: undefined, 
    doctorId:undefined, 
    registerTimeSTime:undefined,
    registerTimeETime:undefined,
  },
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);


function getPharmacyCabinetLists() {
  getReportRegisterInit().then((response)=>{
    issuerOptions.value = response.data.doctorOptions
  })
  getDepartmentList().then((response) => {
    getDepartmentOptions.value = response.data // 科室
  })
}
/** 查询调拨管理项目列表 */
function getList() { 
  loading.value = true;
  console.log(queryParams.value,"queryParams.value")
  getReportRegisterPage(queryParams.value).then((res) => {
    purchaseinventoryList.value = res.data.records||[]
    purchaseinventoryList.value.map(k=>{
      k.number = k.number?(k.number+k.quantityUnit_dictText):0
      k.price = k.price?k.price.toFixed(2):"0.00"
      // k.totalPrice = k.totalPrice?("¥"+k.totalPrice.toFixed(2)):"¥0.00"
      k.totalPrice = k.totalPrice?k.totalPrice.toFixed(2):"0.00"
    })
    total.value = res.data.total;
    loading.value = false
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
  getList();
}

/** 清空条件按钮操作 */
function handleClear() {
  // 清空查询条件
  occurrenceTime.value = ""
  queryParams.value.chargeTimeSTime = ""
  queryParams.value.chargeTimeETime = ""
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

.pagination-container{
    margin-right: 20px;
}
</style>