<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryRef"
      :inline="true"
      v-show="showSearch"
      label-width="100px"
    >
     <!-- <el-form-item label="药品名称：" prop="searchKey" label-width="120px">
        <el-input
          v-model="queryParams.searchKey"
          placeholder="项目编号/项目品名"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="商品编码：" prop="medicationDefId">
        <el-input
          v-model="queryParams.busNo"
          placeholder="商品编码："
          clearable
          style="width: 200px;"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品名称：" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="商品名称："
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="剩余过期天数：" prop="remainingDays" label-width="120px">
        <el-input
          v-model="queryParams.remainingDays"
          placeholder="查询 ≤ X 天的记录"
          clearable
          style="width: 200px;"
          @keyup.enter="handleQuery"
        />
      </el-form-item> -->
     
      <el-form-item label="项目类型：" prop="categoryType">
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
      <!-- <el-form-item label="医保等级：" prop="chrgitmLv" label-width="120px">
        <el-select
          v-model="queryParams.chrgitmLv"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="chrgitmLv_enumText in chrgitmLv_enumTextOptions"
            :key="chrgitmLv_enumText.value"
            :label="chrgitmLv_enumText.info"
            :value="chrgitmLv_enumText.value"
          />
        </el-select>
      </el-form-item> -->
      <el-form-item label="药房：" prop="purposeTypeEnum" label-width="80px">
        <el-select
          v-model="queryParams.purposeTypeEnum"
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
      <el-form-item label="库存范围：" prop="inventoryScope">
        <el-select
          v-model="queryParams.inventoryScope"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="supplyStatus in inventory_range"
            :key="supplyStatus.value"
            :label="supplyStatus.label"
            :value="supplyStatus.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="供应商：" prop="supplierId">
        <el-input
          v-model="queryParams.supplierId"
          placeholder="回车查询"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
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
        label="药品编码"
        align="center"
        key="busNo"
        prop="busNo"
         width="140"
        :show-overflow-tooltip="true"
      />
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
        label="生产批号"
        align="center"
        key="lotNumber"
        prop="lotNumber"
        width="120px"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="厂家/产地"
        align="center"
        key="manufacturerText"
        prop="manufacturerText"
        width="180px"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="药品类型"
        align="center"
        key="itemTableText"
        prop="itemTableText"
        width="90"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="库存数量"
        align="center"
        key="itemQuantity"
        prop="itemQuantity"
        width="80"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="计量单位"
        align="center"
        key="unitCode"
        prop="unitCode"
        width="80"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="小包装库存数"
        align="center"
        key="minPackageQuantity"
        prop="minPackageQuantity"
         width="110"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        label="小包装单位"
        align="center"
        key="minPackageUnit"
        prop="minPackageUnit"
        width="90"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        label="进价"
        align="center"
        key="price"
        prop="price"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        label="售价"
        align="center"
        key="salePrice"
        prop="salePrice"
        :show-overflow-tooltip="true"
      />
      <!-- <el-table-column
        label="规格"
        align="center"
        key="totalVolume"
        prop="totalVolume"
        :show-overflow-tooltip="true"
      />
      -->
      <el-table-column
        label="拆零进价"
        align="center"
        key="partPrice"
        prop="partPrice"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        label="拆零售价"
        align="center"
        key="partSalePrice"
        prop="partSalePrice"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="进价金额"
        align="center"
        key="totalPrice"
        prop="totalPrice"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        label="售价金额"
        align="center"
        key="totalSalePrice"
        prop="totalSalePrice"
        :show-overflow-tooltip="true"
      />
      
       <el-table-column
        label="仓库类型"
        align="center"
        key="purposeTypeEnum_enumText"
        prop="purposeTypeEnum_enumText"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="仓库名称"
        align="center"
        key="locationName"
        prop="locationName"
         width="140"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        label="货位名称"
        align="center"
        key="locationStoreName"
        prop="locationStoreName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="有效期至"
        align="center"
        key="expirationDate"
        prop="expirationDate"
        width="160"
        :show-overflow-tooltip="true"
      >
        <template #default="scope">
          <span>{{ parseTime(scope.row.expirationDate) }}</span>
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

<script setup name="inventoryProductDetails">
import {
  getReportProductPage,
  getInit,
  getPharmacyCabinetList,
  getBusNoInit,
  cancelSupply,
  stopSupply,
} from "./statisticalManagent";

// import Dialog from "./components/Dialog";

const router = useRouter();
const { proxy } = getCurrentInstance();

const {
  item_type,
  inventory_range,
} = proxy.useDict(
  "item_type",
  "inventory_range",
);
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
    // medicationDefId: undefined, 
    busNo:undefined, 
    name: undefined,
    categoryType:undefined,
    purposeTypeEnum:undefined,
    inventoryScope: undefined,
    supplierId: undefined, 
    // occurrenceTimeSTime:undefined,
    // occurrenceTimeETime:undefined,
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
  getReportProductPage(queryParams.value).then((res) => {
    console.log(res,"res----------------")
    loading.value = false;
    purchaseinventoryList.value = res.data.records
    
    if(purchaseinventoryList.value&& purchaseinventoryList.value.length>0){
      purchaseinventoryList.value.map((k,index)=>{
        k.inventoryStatusEnum_enumText = k.inventoryStatusEnum==2?'未停供':(k.inventoryStatusEnum==3?'已停供':'')
        k.quantityUnit = k.quantity / k.partPercent
        const integerPart1 = Math.floor(k.quantityUnit); // 获取整数部分
        const decimalPart1 = k.quantityUnit - integerPart1; // 获取小数部分

        if(decimalPart1){
          k.quantityUnit = integerPart1 + k.unitCode_dictText +
          (decimalPart1*k.partPercent).toFixed(0) + k.minUnitCode_dictText
        }
      })
    }
    total.value = res.data.total;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
//   queryParams.value.occurrenceTimeSTime =
//     occurrenceTime.value && occurrenceTime.value.length == 2
//       ? occurrenceTime.value[0] + " 00:00:00"
//       : "";
//   queryParams.value.occurrenceTimeETime =
//     occurrenceTime.value && occurrenceTime.value.length == 2
//       ? occurrenceTime.value[1] + " 23:59:59"
//       : "";
  queryParams.value.pageNo = 1;
  getList();
}

/** 清空条件按钮操作 */
function handleClear() {
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