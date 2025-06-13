<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryRef"
      :inline="true"
      v-show="showSearch"
      label-width="90px"
    >
      <el-form-item label="单据号：" prop="busNo">
        <el-input
          v-model="queryParams.searchKey"
          placeholder="单据号："
          clearable
          style="width: 220px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审批状态：" prop="statusEnum">
        <el-select v-model="queryParams.statusEnum" placeholder="" clearable style="width: 150px">
          <el-option
            v-for="supplyStatus in supplyStatusOptions"
            :key="supplyStatus.value"
            :label="supplyStatus.label"
            :value="supplyStatus.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="单据类型：" prop="typeEnum">
        <el-select v-model="queryParams.typeEnum" placeholder="" clearable style="width: 150px">
          <el-option
            v-for="item in supplyTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="供应商：" prop="supplierId">
        <el-select v-model="queryParams.supplierId" placeholder="" clearable style="width: 150px">
          <el-option
            v-for="supplier in supplierListOptions"
            :key="supplier.value"
            :label="supplier.label"
            :value="supplier.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="经手人：" prop="practitionerId">
        <el-select
          v-model="queryParams.practitionerId"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="practitioner in practitionerListOptions"
            :key="practitioner.value"
            :label="practitioner.label"
            :value="practitioner.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="查询时间：">
        <el-date-picker
          v-model="dateRange"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 300px"
        ></el-date-picker>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <!-- v-hasPermi="['system:user:add']" -->
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="openAddInventoryReceiptDialog"
          
          >添加记录</el-button
        >
      </el-col>
      <el-col :span="1.5">
         <!-- v-hasPermi="['monitor:job:remove']" -->
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
         
          >删除</el-button
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
        <!-- v-hasPermi="['system:user:export']" -->
      <el-col :span="1.5">
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
      @row-click="handleRowClick"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column
        label="单据号"
        align="center"
        key="supplyBusNo"
        prop="supplyBusNo"
        width="200"
      />
      <el-table-column
        label="退库状态"
        align="center"
        key="returnStatus_text"
        prop="returnStatus_text"
      />
      <el-table-column
        label="审批状态"
        align="center"
        key="statusEnum_enumText"
        prop="statusEnum_enumText"
      />
      <el-table-column
        label="单据类型"
        align="center"
        key="typeEnum_enumText"
        prop="typeEnum_enumText"
      />
      <el-table-column
        label="供应商"
        align="center"
        key="supplierId_dictText"
        prop="supplierId_dictText"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="经手人"
        align="center"
        key="practitionerId_dictText"
        prop="practitionerId_dictText"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="制单人"
        align="center"
        key="applicantId_dictText"
        prop="applicantId_dictText"
      />
      <el-table-column
        label="审核人"
        align="center"
        key="approverId_dictText"
        prop="approverId_dictText"
      />
      <el-table-column
        label="制单日期"
        align="center"
        key="createTime"
        prop="createTime"
        width="180"
      >
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="审核日期 "
        align="center"
        key="approvalTime"
        prop="approvalTime"
        width="180"
      >
        <template #default="scope">
          <span>{{ parseTime(scope.row.approvalTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="230"
        class-name="small-padding fixed-width"
      >
        <template #default="scope">
            <!-- v-hasPermi="['system:user:edit']" -->
          <el-button
            link
            type="primary"
            icon="View"
            @click.stop="handleUpdate(scope.row,'view')"
            >详情</el-button
          >
          <el-button
            link
            type="primary"
            icon="Edit"
            @click.stop="handleUpdate(scope.row)"
          
            :disabled="scope.row.statusEnum != '1' && scope.row.statusEnum != '9'"
            >编辑</el-button
          >
           <!-- v-hasPermi="['system:user:edit']" -->
          <el-button
            v-if="scope.row.statusEnum_enumText=='同意'"
            link
            type="primary"
            icon="Edit"
            @click.stop="handleUpdateTK(scope.row)"
           
            >退库</el-button
          >
          <!-- v-hasPermi="['system:user:remove']" -->
          <el-button
            link
            type="primary"
            icon="View"
            @click.stop="handleSubmitApproval(scope.row)"
      
            v-if="scope.row.statusEnum == '1' || scope.row.statusEnum == '9'"
            >提交审批</el-button
          >
           <!-- v-hasPermi="['system:user:remove']" -->
          <el-button
            link
            type="primary"
            icon="View"
            @click.stop="handleWithdrawApproval(scope.row)"
           
            v-if="scope.row.statusEnum == '2'"
            >撤销审批</el-button
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
    <!-- 退货列表 -->
    <el-table
      v-if="purchaseinventoryReturnList&&purchaseinventoryReturnList.length>0"
      style="height:190px;margin-top:32px;"
      v-loading="loadingReturn"
      :data="purchaseinventoryReturnList"
      @selection-change="handleSelectionChangeReturn"
    >
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column
        label="单据号"
        align="center"
        key="supplyBusNo"
        prop="supplyBusNo"
        width="200"
      />
      <el-table-column
        label="审批状态"
        align="center"
        key="statusEnum_enumText"
        prop="statusEnum_enumText"
      />
      <el-table-column
        label="单据类型"
        align="center"
        key="typeEnum_enumText"
        prop="typeEnum_enumText"
      />
      <el-table-column
        label="供应商"
        align="center"
        key="supplierId_dictText"
        prop="supplierId_dictText"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="经手人"
        align="center"
        key="practitionerId_dictText"
        prop="practitionerId_dictText"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="制单人"
        align="center"
        key="applicantId_dictText"
        prop="applicantId_dictText"
      />
      <el-table-column
        label="审核人"
        align="center"
        key="approverId_dictText"
        prop="approverId_dictText"
      />
      <el-table-column
        label="制单日期"
        align="center"
        key="createTime"
        prop="createTime"
        width="180"
      >
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="审核日期 "
        align="center"
        key="approvalTime"
        prop="approvalTime"
        width="180"
      >
        <template #default="scope">
          <span>{{ parseTime(scope.row.approvalTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="230"
        class-name="small-padding fixed-width"
      >
        <template #default="scope">
          <!-- v-hasPermi="['system:user:edit']" -->
          <el-button
            link
            type="primary"
            icon="View"
            @click.stop="handleUpdateReturn(scope.row,'view')"
            >详情</el-button
          >
          <el-button
            link
            type="primary"
            icon="Edit"
            @click.stop="handleUpdateReturn(scope.row)"
            
            :disabled="scope.row.statusEnum != '1' && scope.row.statusEnum != '9'"
            >编辑</el-button
          >
          <!-- v-hasPermi="['system:user:remove']" -->
          <el-button
            link
            type="primary"
            icon="View"
            @click.stop="handleSubmitApprovalReturn(scope.row)"
            
            v-if="scope.row.statusEnum == '1' || scope.row.statusEnum == '9'"
            >提交审批</el-button
          >
           <!-- v-hasPermi="['system:user:remove']" -->
          <el-button
            link
            type="primary"
            icon="View"
            @click.stop="handleWithdrawApprovalReturn(scope.row)"
           
            v-if="scope.row.statusEnum == '2'"
            >撤销审批</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <inventory-receipt-dialog
      ref="inventoryReceiptRef"
      :practitionerListOptions="practitionerListOptions"
      :itemTypeOptions="itemTypeOptions"
      :supplierListOptions="supplierListOptions"
      :busNoAdd="busNoAdd"
      :item="currentData"
      :editRow="editRow"
      @refresh="getList"
    />
  </div>
</template>

<script setup name="Purchaseinventory">
import {
  getPurchaseinventoryList,
  addPurchaseinventory,
  getpurchaseInventoryDetail,
  getInit,
  submitApproval,
  withdrawApproval,
  delPurchaseinventory,
  generatedReturnDetail,
  getpurchaseInventoryDetailReturn,
  submitApprovalReturn,
  withdrawApprovalReturn
} from './components/purchaseinventory';
import { useStore } from '@/store/store';
import inventoryReceiptDialog from './components/inventoryReceiptDialog';
import { ref } from 'vue';

const router = useRouter();
const store = useStore();
const { proxy } = getCurrentInstance();
const purchaseinventoryRef = ref(null); // 初始化 ref
const purchaseinventoryReturnList = ref([])
const purchaseinventoryList = ref([]);
const open = ref(false);
const loading = ref(true);
const loadingReturn = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const idsReturn = ref([]);
const singleReturn = ref(true);
const multipleReturn = ref(true);
const total = ref(0);
const title = ref('');
const dateRange = ref([]);
const busNoAdd = ref(''); // 单据号新增
const itemTypeOptions = ref(undefined); // 入库项目类型
const supplyTypeOptions = ref(undefined); // 入库单据类型
const practitionerListOptions = ref(undefined); // 查询经手人列表
const supplierListOptions = ref(undefined); // 供应商列表
const supplyStatusOptions = ref(undefined); // 审批状态
const editRow = ref({});
const editRowTK = ref({});
// 使用 ref 定义当前编辑的采购
const currentData = ref({});
const selectedRows = ref([])
// 是否停用
const statusFlagOptions = ref(undefined);

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    searchKey: undefined, // 供应商名称
    busNo: undefined, // 编码
    practitionerId: undefined,
    supplierId: undefined,
    statusEnum: undefined, // 审批状态
  },
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);

/** 采购入库查询下拉树结构 */
function getPurchaseinventoryTypeList() {
  getInit().then((response) => {
    busNoAdd.value = response.data.busNo; // 单据号新增
    itemTypeOptions.value = response.data.itemTypeOptions; // 活动标记
    supplyTypeOptions.value = response.data.supplyTypeOptions; // 活动标记
    practitionerListOptions.value = response.data.practitionerListOptions; // 预约必填标记
    supplierListOptions.value = response.data.supplierListOptions; // 供应商列表
    supplyStatusOptions.value = response.data.supplyStatusOptions; // 供应状态
  });
}

/** 查询采购入库项目列表 */
function getList() {
  loading.value = true;
  // // queryParams.value.statusEnum = +queryParams.value.statusEnum
  // proxy.addDateRange(queryParams.value, dateRange.value)
  getPurchaseinventoryList(queryParams.value).then((res) => {
    loading.value = false;
    purchaseinventoryList.value = res.data.records
    purchaseinventoryList.value.map(k=>{
      k.returnStatus_text =  k.returnStatus==1?'已退库':''
    })
    total.value = res.data.total;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.createTimeSTime =
    dateRange.value && dateRange.value.length == 2 ? dateRange.value[0] + ' 00:00:00' : '';
  queryParams.value.createTimeETime =
    dateRange.value && dateRange.value.length == 2 ? dateRange.value[1] + ' 23:59:59' : '';
  queryParams.value.pageNo = 1;
  getList();
}

/** 清空条件按钮操作 */
function handleClear() {
  // 清空查询条件
  queryParams.value.createTimeSTime = ""
  queryParams.value.createTimeETime = ""
  dateRange.value = ""
  proxy.resetForm('queryRef');
  getList();
}

/** 打开新增弹窗 */
function openAddInventoryReceiptDialog() {
  store.clearCurrentData();
  router.push({
    path: 'purchaseDocument',
  });
  // getPurchaseinventoryTypeList();
  // nextTick(() => {
  //   proxy.$refs["inventoryReceiptRef"].show();
  // });
}
function handleUpdateTK(row) {
  // store.clearCurrentReturnData();
  editRow.value = row;
    router.push({
    path: 'returnedPurchase',
    query:{originalSupplyBusNo:editRow.value.supplyBusNo}
  });
  // getpurchaseInventoryDetail(row.supplyBusNo).then((response) => {
  //   currentData.value = response.data;
  //   // nextTick(() => {
  //   //   proxy.$refs['inventoryReceiptRef'].edit();
  //   // });
  //   store.setCurrentData({ editRow: row, item: currentData.value });
  //   router.push({
  //     path: 'purchaseDocument',
  //   });
  //   getList();
  // });
}
/** 退库修改按钮操作 */
function handleUpdateReturn(row,view) {
  if(view){
    router.replace({ path: 'returnedPurchase',query:{originalSupplyBusNo:row.supplyBusNo,view:view}})  
  }else{
    router.push({
      path: 'returnedPurchase',query:{originalSupplyBusNo:row.originalSupplyBusNo,supplyBusNo:row.supplyBusNo}
    });
  }
  handleRowClick(editRowTK.value)
}
/** 退库提交审核按钮 */
function handleSubmitApprovalReturn(row) {
  submitApprovalReturn(row.supplyBusNo).then((response) => {
    proxy.$modal.msgSuccess('提交审批成功');
    // open.value = false;
    handleRowClick(editRowTK.value)
    // loadingReturn.value = false
  });
}

/** 退库撤回审批按钮 */
function handleWithdrawApprovalReturn(row) {
  withdrawApprovalReturn(row.supplyBusNo).then((response) => {
    proxy.$modal.msgSuccess('撤销审批成功');
    // open.value = false;
    handleRowClick(editRowTK.value);
    // loadingReturn.value = false
  });
}
/** 退库修改按钮操作 */
function handleUpdate(row,view) {
  if(row.typeEnum==5){ //采购退货
    // editRow.value = row;
    // getpurchaseInventoryDetail(row.supplyBusNo).then((response) => {
    //   currentData.value = response.data;
    //   store.setCurrentData({ editRow: row, item: currentData.value });
      router.push({
        path:'returnedPurchase',query:{supplyBusNo:row.supplyBusNo}
      });
      // getList();
    // })
  }else{ // 采购入库
    editRow.value = row;
    getpurchaseInventoryDetail(row.supplyBusNo).then((response) => {
      currentData.value = response.data;
      // nextTick(() => {
      //   proxy.$refs['inventoryReceiptRef'].edit();
      // });
      store.setCurrentData({ editRow: row, item: currentData.value });
      if(view){ // 详情
        router.push({
          path: 'purchaseDocument',query:{supplyBusNo:row.supplyBusNo,view:view}
        });
      }else{
        router.push({
          path: 'purchaseDocument',
        });
      }
      
      getList();
    })
  }
  
}
/** 提交审核按钮 */
function handleSubmitApproval(row) {
  submitApproval(row.supplyBusNo).then((response) => {
    proxy.$modal.msgSuccess('提交审批成功');
    open.value = false;
    getList();
  });
}

/** 撤回审批按钮 */
function handleWithdrawApproval(row) {
  withdrawApproval(row.supplyBusNo).then((response) => {
    proxy.$modal.msgSuccess('撤销审批成功');
    open.value = false;
    getList();
  });
}

/** 选择条数  */
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id);
  selectedRows.value = selection;
  console.log(selectedRows.value,"!21212")
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 选择退货条数  */
function handleSelectionChangeReturn(selection) {
  idsReturn.value = selection.map((item) => item.id);
  singleReturn.value = selection.length != 1;
  multipleReturn.value = !selection.length;
}
/** 删除按钮操作 */
function handleDelete(row) {
  let length = selectedRows.value.length;
  let ids  = []
  if(selectedRows.value[0].id){
     ids = selectedRows.value.map((item) => {
      return item.id
    });
  } 
  console.log(ids, "ids");
  proxy.$modal
    .confirm('是否确认删除以上数据?')
    .then(function () {
      return delPurchaseinventory(ids);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess('删除成功');
    })
    .catch(() => {});
}
//点击列表当前行
function handleRowClick(row) {
  editRowTK.value = row
  console.log(row,"row")
  if(row.returnStatus==1){
    loadingReturn.value = true
    generatedReturnDetail(row.supplyBusNo).then((response) => {
      purchaseinventoryReturnList.value = response.data
      purchaseinventoryReturnList.value.map(k=>{
        // k.returnStatus_text =  k.returnStatus==1?'已退库':''
        k.originalSupplyBusNo = k.originalSupplyBusNo?k.originalSupplyBusNo:row.supplyBusNo
      }) 
    
    })
    loadingReturn.value = false
  }
}

getPurchaseinventoryTypeList();
getList();
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