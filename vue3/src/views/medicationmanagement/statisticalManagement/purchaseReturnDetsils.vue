<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryRef"
      :inline="true"
      v-show="showSearch"
      label-width="100px"
    >
    <el-form-item label="药品名称：" prop="searchKey">
      <el-input
        v-model="queryParams.searchKey"
        placeholder="编码/单据号/药品名称"
        clearable
        style="width: 200px"
        @keyup.enter="handleQuery"
      />
    </el-form-item>
    <!-- <el-form-item label="单据号：" prop="busNo">
      <el-input
        v-model="queryParams.busNo"
        placeholder="单据号："
        clearable
        style="width: 200px;"
        @keyup.enter="handleQuery"
      />
    </el-form-item>
     <el-form-item label="药品名称：" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="项目编号/项目品名"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="编码：" prop="medicationDefId">
        <el-input
          v-model="queryParams.medicationDefId"
          placeholder="编码："
          clearable
          style="width: 200px;"
          @keyup.enter="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="审核日期：">
        <el-date-picker
          v-model="approvalTime"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 300px"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>
      <!-- <el-form-item label="科室：" prop="department">
        <el-select
          v-model="queryParams.department"
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
      <el-form-item label="存放仓库：" prop="purposeLocationId" >
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
      <el-form-item label="供应商：" prop="supplierId">
        <el-select
          v-model="queryParams.supplierId"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="supplierList in supplierListOptions"
            :key="supplierList.value"
            :label="supplierList.label"
            :value="supplierList.value"
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
     <!-- <el-table-column
        label="药品名称"
        align="center"
        key="name"
        prop="name"
        :show-overflow-tooltip="true"
        width="110"
      /> -->
      <el-table-column
        label="单据号"
        align="center"
        key="supplyBusno"
        prop="supplyBusno"
         width="200"
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
        label="编码"
        align="center"
        key="busNo"
        prop="busNo"
        width="140"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="采购单据号"
        align="center"
        key="originalBusNo"
        prop="originalBusNo"
        width="200"
        :show-overflow-tooltip="true"
      />
      <!-- <el-table-column
        label="药品类型"
        align="center"
        key="itemTableText"
        prop="itemTableText"
        width="140"
        :show-overflow-tooltip="true"
      /> -->
      <el-table-column
        label="生产批号"
        align="center"
        key="lotNumber"
        prop="lotNumber"
        width="140px"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="存放仓库"
        align="center"
        key="locationName"
        prop="locationName"
         width="140"
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
        label="计量单位"
        align="center"
        key="unitCode_dictText"
        prop="unitCode_dictText"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="退货数量"
        align="center"
        key="quantity"
        prop="quantity"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="采购单价"
        align="center"
        key="price"
        prop="price"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="退货单价"
        align="center"
        key="returnPrice"
        prop="returnPrice"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="退货金额"
        align="center"
        key="totalPrice"
        prop="totalPrice"
        :show-overflow-tooltip="true"
      />
      <!-- <el-table-column
        label="售价"
        align="center"
        key="salePrice"
        prop="salePrice"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="售价金额"
        align="center"
        key="totalSalePrice"
        prop="totalSalePrice"
        :show-overflow-tooltip="true"
      /> -->
      <!-- <el-table-column
        label="采购科室"
        align="center"
        key="department"
        prop="department"
        :show-overflow-tooltip="true"
      /> -->
       <el-table-column
        label="供应商"
        align="center"
        key="supplier"
        prop="supplier"
        width="100"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="审核人"
        align="center"
        key="approverId_dictText"
        prop="approverId_dictText"
        width="100"
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
        label="审核日期"
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

<script setup name="purchaseReturnDetsils">
import {
  getReportPurchaseReturn,
  getPharmacyCabinetList,
  cancelSupply,
  stopSupply,
  getPurchaseReturnInit
} from "./statisticalManagent";

// import Dialog from "./components/Dialog";

const router = useRouter();
const { proxy } = getCurrentInstance();

const {
  item_type
} = proxy.useDict(
  "item_type"
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
const approvalTime = ref([]);
const purposeTypeListOptions = ref(undefined);
const sourceTypeListOptions = ref(undefined)
const supplyTypeOptions = ref(undefined); 
const categoryCodeListOptions = ref(undefined); // 源仓库
const chrgitmLv_enumTextListOptions = ref(undefined); //目的仓库
const supplyStatusOptions = ref(undefined);
const supplierListOptions = ref([]);
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
    busNo: undefined, 
    name:undefined, 
    medicationDefId: undefined,
    // approvalTime:undefined,
    department:undefined,
    purposeLocationId: undefined,
    categoryType: undefined, 
    supplierId: undefined, 
    createTimeSTime:undefined,
    createTimeETime:undefined,
  },
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);

function getPharmacyCabinetLists() {
  getPharmacyCabinetList().then((response) => {
    console.log(response,'response',response.data)
    locationIdList.value = response.data
  });
  getPurchaseReturnInit().then((response) => {
    supplierListOptions.value = response.data.supplierListOptions 
  })
}
/** 查询列表 */
function getList() {
  loading.value = true;
  getReportPurchaseReturn(queryParams.value).then((res) => {
    loading.value = false;
    purchaseinventoryList.value = res.data.records
    total.value = res.data.total;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.createTimeSTime =
    approvalTime.value && approvalTime.value.length == 2
      ? approvalTime.value[0] + " 00:00:00"
      : "";
  queryParams.value.createTimeETime =
    approvalTime.value && approvalTime.value.length == 2
      ? approvalTime.value[1] + " 23:59:59"
      : "";
  queryParams.value.pageNo = 1;
  getList();
}

/** 清空条件按钮操作 */
function handleClear() {
  // 清空查询条件
  queryParams.value.createTimeSTime = ""
  queryParams.value.createTimeETime = ""
  approvalTime.value = ""
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