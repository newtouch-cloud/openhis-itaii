<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryRef"
      :inline="true"
      v-show="showSearch"
      label-width="100px"
    >
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
      <el-form-item label="开单时间：">
        <el-date-picker
          v-model="occurrenceTime"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 300px"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>
      <el-form-item label="项目查询：" prop="searchKey">
        <el-input
          v-model="queryParams.searchKey"
          placeholder="输入名称/编码后回车查询"
          clearable
          style="width: 220px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="统计类型：">
        <el-select
          v-model="inventoryScope"
          placeholder=""
          clearable
          style="width: 150px"
          disabled
        >
          <el-option
            v-for="supplyStatus in inventoryOptions"
            :key="supplyStatus.value"
            :label="supplyStatus.label"
            :value="supplyStatus.value"
          />
        </el-select>
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
        label="药品编码"
        align="center"
        key="medicineNo"
        prop="medicineNo"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        label="药品名称"
        align="center"
        key="medicineName"
        prop="medicineName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="待发数量"
        align="center"
        key="dispenseQuantity"
        prop="dispenseQuantity"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="单位"
        align="center"
        key="unitCode_dictText"
        prop="unitCode_dictText"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="处方类型"
        align="center"
        key="dispenseEnum_enumText"
        prop="dispenseEnum_enumText"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        label="病人"
        align="center"
        key="patientName"
        prop="patientName"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        label="处方号"
        align="center"
        key="prescriptionNo"
        prop="prescriptionNo"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="门诊号"
        align="center"
        key="outpatientNo"
        prop="outpatientNo"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="住院号"
        align="center"
        key="admissionNo"
        prop="admissionNo"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="开单时间"
        align="center"
        key="createTime"
        prop="createTime"
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
    <!-- <el-row
      :gutter="10"
      class="mb8"
      style="
        margin-top: 15px;
        display: flex;
        align-items: center;
        justify-content: flex-start;
      "
    >
      <el-col :span="3">
        <span>制单人：{{ userStore.name }}</span>
      </el-col>
      <el-col :span="6">
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
      </el-col>
    </el-row> -->
  </div>
</template>

<script setup name="awaitingMedicineDetails">
import {
  getAwaitingPendingMedicationPageList,
  getInit,
  getPharmacyCabinetList,
  getBusNoInit,
  cancelSupply,
  stopSupply,
} from "./statisticalManagent";
import useUserStore from "@/store/modules/user";
import { ref } from "vue";
const userStore = useUserStore();

const router = useRouter();
const { proxy } = getCurrentInstance();

const inventoryOptions = ref([])
const totalAmount = ref(0);
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
const categoryCodeListOptions = ref(undefined); // 源仓库
const chrgitmLv_enumTextListOptions = ref(undefined); //目的仓库
const supplyStatusOptions = ref(undefined);
const editRow = ref({});
// 使用 ref 定义当前编辑的采购
const currentData = ref({});
// 是否停用
const statusFlagOptions = ref(undefined);
const chrgitmLv_enumTextOptions = ref([]);
const locationIdList = ref([]);
const inventoryScope = ref("")

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    searchKey: undefined, 
    // startTime:undefined, 
    // endTime: undefined,
    // inventoryScope: undefined,
    createTimeSTime:undefined,
    createTimeETime:undefined,
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
/**计算合计金额 */
function handleTotalAmount() {
  totalAmount.value = purchaseinventoryList.value.reduce(
    (accumulator, currentRow) => {
      return (accumulator + (Number(currentRow.totalPrice) || 0))
    },
    0
  );
}

function getPharmacyCabinetLists() {
  // 统计类型
  inventoryOptions.value = [
    {value:1,label:'待发药明细账'},
  ]
  inventoryScope.value = 1
  getPharmacyCabinetList().then((response) => {
    console.log(response,'response',response.data)
    locationIdList.value = response.data
  });
  getInit().then((response) => {
    chrgitmLv_enumTextOptions.value = response.data.chrgitmLvOptions 
  })
}
/** 查询调拨管理项目列表 */
function getList() {
  loading.value = true;
  getAwaitingPendingMedicationPageList(queryParams.value).then((res) => {
    console.log(res,"res----------------")
    loading.value = false;
    purchaseinventoryList.value = res.data.records
    
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
  queryParams.value.createTimeSTime = ""
  queryParams.value.createTimeETime = ""
  occurrenceTime.value = ""
  // inventoryScope.value = ""
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