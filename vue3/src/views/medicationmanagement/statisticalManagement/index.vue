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
          placeholder="项目编号/项目品名"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="编码：" prop="medicationDefId">
        <el-input
          v-model="queryParams.medicationDefId"
          placeholder="编码："
          clearable
          style="width: 200px;"
          @keyup.enter="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="剩余过期天数：" prop="remainingDays" label-width="120px">
        <el-input
          v-model="queryParams.remainingDays"
          placeholder="查询 ≤ X 天的记录"
          clearable
          style="width: 200px;"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
     
      <el-form-item label="项目类型：" prop="categoryCode">
        <el-select
          v-model="queryParams.categoryCode"
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
      <el-form-item label="医保等级：" prop="chrgitmLv" label-width="120px">
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
      </el-form-item>
      <el-form-item label="仓库药房：" prop="locationId" label-width="100px">
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
      <el-form-item label="库存范围：" prop="warehouseScope" label-width="100px">
        <el-select
          v-model="queryParams.warehouseScope"
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
       <!-- <el-form-item label="医院：" prop="hospital" label-width="100px">
        <el-select
          v-model="queryParams.hospital"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="supplyStatus in supplyTypeOptions"
            :key="supplyStatus.value"
            :label="supplyStatus.label"
            :value="supplyStatus.value"
          />
        </el-select>
      </el-form-item> -->
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
        label="项目编号"
        align="center"
        key="busNo"
        prop="busNo"
         width="140"
        :show-overflow-tooltip="true"
      />
      <!-- itemTable -->
      <el-table-column
        label="项目品名"
        align="center"
        key="medicineName"
        prop="medicineName"
         width="140"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="仓库"
        align="center"
        key="locationName"
        prop="locationName"
         width="140"
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
        label="医保等级"
        align="center"
        key="chrgitmLv_enumText"
        prop="chrgitmLv_enumText"
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
        label="生产批号"
        align="center"
        key="lotNumber"
        prop="lotNumber"
        width="120px"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="库存（常规单位）"
        align="center"
        key="quantityUnit"
        prop="quantityUnit"
         width="140px"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="采购单位"
        align="center"
        key="unitCode_dictText"
        prop="unitCode_dictText"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="库存数量"
        align="center"
        key="quantity"
        prop="quantity"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        label="库存单位"
        align="center"
        key="minUnitCode_dictText"
        prop="minUnitCode_dictText"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="拆零比"
        align="center"
        key="partPercent"
        prop="partPercent"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        label="采购价格"
        align="center"
        key="price"
        prop="price"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="生产日期"
        align="center"
        key="productionDate"
        prop="productionDate"
        width="160"
        :show-overflow-tooltip="true"
      >
        <template #default="scope">
          <span>{{ parseTime(scope.row.productionDate) }}</span>
        </template>
      </el-table-column>
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
       <el-table-column
        label="剩余过期天数"
        align="center"
        key="remainingDays"
        prop="remainingDays"
         width="140px"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        label="药品追溯码"
        align="center"
        key="traceNo"
        prop="traceNo"
        width="120px"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        label="药品停用"
        align="center"
        key="statusEnum_enumText"
        prop="statusEnum_enumText"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        label="货位"
        align="center"
        key="locationStoreName"
        prop="locationStoreName"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        label="仓库类型"
        align="center"
        key="formEnum_enumText"
        prop="formEnum_enumText"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        label="供应商"
        align="center"
        key="supplyId_dictText"
        prop="supplyId_dictText"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        label="停供状态"
        align="center"
        key="inventoryStatusEnum_enumText"
        prop="inventoryStatusEnum_enumText"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="操作"
        align="center"
        width="100"
        fixed="right"
        class-name="small-padding fixed-width"
      >
       <!-- 停供/停用3，取消停供/启用2 -->
        <template #default="scope">
          <!-- v-hasPermi="['system:user:remove']" -->
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handlestopcancelSupply(scope.row)"
            
            v-if="scope.row.inventoryStatusEnum == 2"
            >停供</el-button
          >
          <!-- v-hasPermi="['system:user:remove']" -->
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handlestopcancelSupply(scope.row)"
            
            v-if="scope.row.inventoryStatusEnum == 3"
            >取消停供</el-button
          >
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

<script setup name="statisticalManagement">
import {
  getproductReturnPage,
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
    hospital:undefined, 
    hospital: undefined,
    categoryCode:undefined,
    chrgitmLv:undefined,
    warehouseScope: undefined,
    locationId: undefined, 
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
  getproductReturnPage(queryParams.value).then((res) => {
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
/** 备份 */
function openAddaddTransferProductDialog() {
  // getTransferProductTypeList();
  // getBusNoInitList()
  // nextTick(() => {
    // proxy.$refs["transferProductRef"].show();
  // });
//   const partItem = {partFlg: 'add',rowData: []}
  // item: JSON.stringify(partItem)
  // ,query:{item: JSON.stringify(partItem)}
//   router.push({ path: '/medicationmanagement/transferManagement/transferManagent'})
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

// 停供
function handlestopSupply(row) {
 
}


// getTransferProductTypeList();
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