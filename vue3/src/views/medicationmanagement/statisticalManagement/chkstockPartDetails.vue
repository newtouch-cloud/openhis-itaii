<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryRef"
      :inline="true"
      v-show="showSearch"
      label-width="100px"
    >
     <el-form-item label="药品名称：" prop="searchKey" label-width="120px">
        <el-input
          v-model="queryParams.searchKey"
          placeholder="编码/项目名称"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="编码：" prop="busNo">
        <el-input
          v-model="queryParams.busNo"
          placeholder="编码："
          clearable
          style="width: 200px;"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目名称：" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="项目名称："
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item> -->
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
      </el-form-item> -->
      <el-form-item label="盘点日期：">
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
     <!-- <el-table-column
        label="药品名称"
        align="center"
        key="name"
        prop="name"
        :show-overflow-tooltip="true"
        width="110"
      /> -->
      <el-table-column
        label="编码"
        align="center"
        key="busNo"
        prop="busNo"
 
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="项目名称"
        align="center"
        key="name"
        prop="name"
    
        :show-overflow-tooltip="true"
      />
        <el-table-column
        label="规格"
        align="center"
        key="totalVolume"
        prop="totalVolume"
 
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="厂家/产地"
        align="center"
        key="manufacturerText"
        prop="manufacturerText"
  
        :show-overflow-tooltip="true"
      />
      <!-- <el-table-column
        label="类型"
        align="center"
        key="itemTableText"
        prop="itemTableText"
  
        :show-overflow-tooltip="true"
      /> -->
     <el-table-column
        label="生产批号"
        align="center"
        key="lotNumber"
        prop="lotNumber"

        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="仓库"
        align="center"
        key="locationName"
        prop="locationName"
    
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
        label="盈亏数量"
        align="center"
        key="itemQuantity"
        prop="itemQuantity"
     
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="盘点日期"
        align="center"
        key="approvalTime"
        prop="approvalTime"
  
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

<script setup name="chkstockPartDetails">
import {
  getReportStocktakingPage,
  getInit,
  getPharmacyCabinetList,
  getBusNoInit,
  cancelSupply,
  stopSupply,
} from "./statisticalManagent";

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

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    searchKey: undefined, 
    // busNo: undefined, 
    // name:undefined, 
    categoryType: undefined,
    // occurrenceTime:undefined,
    approvalTimeSTime:undefined,
    approvalTimeETime:undefined,
  },
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);

function getBusNoInitList() {
  getBusNoInit().then((response) => {
    console.log(response,'response',response.data)
    busNoAdd.value = response.data.busNo; // 单据号新增
  });
}
function getPharmacyCabinetLists() {
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
  // // queryParams.value.locationId = +queryParams.value.locationId
  // proxy.addoccurrenceTime(queryParams.value, occurrenceTime.value)
  getReportStocktakingPage(queryParams.value).then((res) => {
    console.log(res,"res----------------")
    loading.value = false;
    purchaseinventoryList.value = res.data.records
    
    if(purchaseinventoryList.value&& purchaseinventoryList.value.length>0){
    //   purchaseinventoryList.value.map((k,index)=>{
    //     k.inventoryStatusEnum_enumText = k.inventoryStatusEnum==2?'未停供':(k.inventoryStatusEnum==3?'已停供':'')
    //     k.quantityUnit = k.quantity / k.partPercent
    //     const integerPart1 = Math.floor(k.quantityUnit); // 获取整数部分
    //     const decimalPart1 = k.quantityUnit - integerPart1; // 获取小数部分

    //     if(decimalPart1){
    //       k.quantityUnit = integerPart1 + k.unitCode_dictText +
    //       (decimalPart1*k.partPercent).toFixed(0) + k.minUnitCode_dictText
    //     }
    //   })
    }
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

// 取消停供  停供
function handlestopcancelSupply(row) {
  if(row.inventoryStatusEnum == 2){
    stopSupply(row.id).then((response) => {
      proxy.$modal.msgSuccess("停供成功");
      open.value = false
      getList()
    })
  }
  if(row.inventoryStatusEnum == 3){
    cancelSupply(row.id).then((response) => {
      proxy.$modal.msgSuccess("取消停供成功");
      open.value = false
      getList()
    })
  }
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