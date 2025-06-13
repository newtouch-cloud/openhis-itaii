<template>
  <div class="app-container">
    <div class="table-header">
      <el-input
        class="table-header-search"
        placeholder="单据号"
        v-model="queryParams.searchKey"
      ></el-input>
      <el-select
        class="table-header-search"
        v-model="queryParams.statusEnum"
        placeholder="审批状态"
        clearable
      >
        <el-option
          v-for="item in supplyStatusOption"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-select
        class="table-header-search"
        v-model="queryParams.typeEnum"
        placeholder="单据类型"
        clearable
      >
        <el-option
          v-for="item in supplyTypeOption"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-date-picker
        v-model="queryParams.applyTime"
        placeholder="请选择申请日期"
        type="date"
        size="default"
        placement="bottom"
        @change="handleDateQuery"
        value-format="YYYY-MM-DD"
      />
      <el-button
        class="table-header-button"
        icon="Refresh"
        @click="
          () => {
            queryParams = {
              pageNo: 1,
              pageSize: 10,
              statusEnum: undefined,
              searchKey: undefined,
              typeEnum: undefined,
            };
            getList();
          }
        "
      >
        重置
      </el-button>
      <el-button
        class="table-header-button"
        type="primary"
        @click="getList"
        icon="Search"
      >
        搜索
      </el-button>
    </div>
    <el-table
      max-height="700"
      :data="receiptList"
      row-key="supplyBusNo"
      v-loading="loading"
    >
      <el-table-column
        label="单据号"
        align="center"
        prop="supplyBusNo"
        width="200"
      />
      <el-table-column
        label="审批状态"
        align="center"
        prop="statusEnum_enumText"
      />
      <el-table-column
        label="单据类型"
        align="center"
        prop="typeEnum_enumText"
      />
      <el-table-column
        label="供应商"
        align="center"
        prop="supplierId_dictText"
      />
      <el-table-column
        label="经手人"
        align="center"
        prop="practitionerId_dictText"
      />
      <el-table-column
        label="申请人"
        align="center"
        prop="applicantId_dictText"
      />
      <el-table-column label="申请时间" align="center" prop="applyTime">
        <template #default="scope">
          {{ formatDate(scope.row.applyTime) }}
        </template>
      </el-table-column>
      <el-table-column
        label="审批人"
        align="center"
        prop="approverId_dictText"
      />
      <el-table-column label="审批时间" align="center" prop="approvalTime">
        <template #default="scope">
          {{ formatDate(scope.row.approvalTime) }}
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="200"
        class-name="small-padding fixed-width"
      >
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="handelApplys(scope.row,'apply')"
            :disabled="scope.row.statusEnum == 3 || scope.row.statusEnum == 4"
          >
            审批
          </el-button>
          <el-button
            link
            type="primary"
            @click="handelApplys(scope.row,'view')"
          >
            查看
          </el-button>
          <!-- <el-button
            link
            type="primary"
            @click="handelApply(scope.row)"
            :disabled="scope.row.statusEnum == 3 || scope.row.statusEnum == 4"
          >
            审批通过
          </el-button>
          <el-button
            link
            type="primary"
            @click="handleReject(scope.row)"
            :disabled="scope.row.statusEnum == 4 || scope.row.statusEnum == 3"
          >
            驳回
          </el-button> -->
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
        
<script setup name="Billapproval">
import {
  getReceiptList,
  purchaseInventoryApproved,
  requisitionIssueApproved,
  returnIssueApproved,
  productTransferApproved,
  productStocktakingApproved,
  lossReportApproved,
  reject,
  init,
  getpurchaseInventoryDetail,
  getpurchaseInventoryDetailReturn
} from "./components/api";
import { useStore } from '@/store/store';
import { formatDate } from "@/utils/index";
import useTagsViewStore from '@/store/modules/tagsView';
const tagsViewStore = useTagsViewStore();
const router = useRouter();
const route = useRoute();
const store = useStore();
const { proxy } = getCurrentInstance();
const emit = defineEmits(["selectAdviceBase"]);
const total = ref(0);
const queryParams = ref({
  pageNo: 1,
  pageSize: 10,
});
const receiptList = ref([]);
const supplyTypeOption = ref([]);
const supplyStatusOption = ref([]);
const loading = ref(false);

watch(
  () => route.query.type,
  (newVlaue) => {
    if(newVlaue){
      getList()
    }
  },
  { immediate: true }
);

getList();
function getList() {
  loading.value = true;
  getReceiptList(queryParams.value).then((res) => {
    receiptList.value = res.data.records;
    total.value = res.data.total;
    loading.value = false;
  });
}

function handelApply(row) {
  if(row.typeEnum ==2 || row.typeEnum ==8){ //商品调拨  8 批量
    productTransferApproved(row.supplyBusNo).then((res) => {
      if (res.code == 200) {
        proxy.$modal.msgSuccess("操作成功");
        getList();
      }
    });
  }else if(row.typeEnum ==7 ){ //领用出库审批通过
    requisitionIssueApproved(row.supplyBusNo).then((res) => {
      if (res.code == 200) {
        proxy.$modal.msgSuccess("操作成功");
        getList();
      }
    });
  }else if(row.typeEnum == 9){ //领用退库审批通过
    returnIssueApproved(row.supplyBusNo).then((res) => {
      if (res.code == 200) {
        proxy.$modal.msgSuccess("操作成功");
        getList();
      }
    });
  }else if(row.typeEnum == 4||row.typeEnum == 10){ //盘点审批  批量盘点10通过
    productStocktakingApproved(row.supplyBusNo).then((res) => {
      if (res.code == 200) {
        proxy.$modal.msgSuccess("操作成功");
        getList();
      }
    });
  }else if(row.typeEnum == 6){ // 报损审批通过
    lossReportApproved(row.supplyBusNo).then((res) => {
      if (res.code == 200) {
        proxy.$modal.msgSuccess("操作成功");
        getList();
      }
    })
  }else{
    purchaseInventoryApproved(row.supplyBusNo).then((res) => {
      if (res.code == 200) {
        proxy.$modal.msgSuccess("操作成功");
        getList();
      }
    });
  }
  console.log(row,"typeEnum_enumText")
  
}

// 审批,查看
function handelApplys(row,view) {
  // if(view=='apply'){  // 审批
  //   tagsViewStore.delView(router.currentRoute.value)
  // }
  if(row.typeEnum == 2){//商品调拨 
    // 跳转到审核页面
    router.replace({path: '/medicationmanagement/transferManagement/transferManagent',query:{supplyBusNo:row.supplyBusNo,view:view}});
    // router.push({ path: '/medicationmanagement/transferManagement/transferManagent',query:{supplyBusNo:row.supplyBusNo,view:view}})
  }else if(row.typeEnum == 8){  //8 批量
    router.replace({ path: '/medicationmanagement/transferManagement/batchTransfer',query:{supplyBusNo:row.supplyBusNo,view:view}})
  }else if(row.typeEnum == 7 ){ //领用出库审批通过
    router.replace({ path: '/medicationmanagement/requisitionManagement/requisitionManagement',query:{supplyBusNo:row.supplyBusNo,view:view}})
  }else if(row.typeEnum == 9){ //领用退库审批通过
    router.replace({ path: '/medicationmanagement/requisitionManagement/returningInventory',query:{supplyBusNo:row.supplyBusNo,view:view}})
  }else if(row.typeEnum == 4){ //盘点审批  
    router.replace({ path: '/medicationmanagement/chkstock/chkstockPart',query:{supplyBusNo:row.supplyBusNo,view:view}})
  }else if(row.typeEnum == 10){ // 批量盘点
    router.replace({ path: '/medicationmanagement/chkstock/chkstockBatch',query:{supplyBusNo:row.supplyBusNo,view:view}})
  }else if(row.typeEnum == 6){ // 报损审批通过
    router.replace({ path: '/medicationmanagement/lossReportingManagement/lossReportingManagement',query:{supplyBusNo:row.supplyBusNo,view:view}})
  }else if(row.typeEnum == 5){ // 采购退货通过5
    // getpurchaseInventoryDetailReturn(row.supplyBusNo).then((response) => {
      // let currentData = response.data;
      // console.log(currentData,"退库详情")
      // store.setCurrentReturnData({ editRow: row, item: currentData });
      router.replace({ path: '/medicationmanagement/medicationmanagement/returnedPurchase',query:{originalSupplyBusNo:row.supplyBusNo,view:view}})  
    // });
    
  }else{     // 采购入库 1
    getpurchaseInventoryDetail(row.supplyBusNo).then((response) => {
      let currentData = response.data;
      store.setCurrentData({ editRow: row, item: currentData });
      router.replace({ path: '/medicationmanagement/medicationmanagement/purchaseDocument',query:{supplyBusNo:row.supplyBusNo,view:view}})  
    });
    
  }
  console.log(row,"typeEnum_enumText")
  
}

// 驳回
function handleReject(row) {
  reject(row.supplyBusNo).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess("操作成功");
      getList();
    }
  });
}

function handleDateQuery(value) {
  if (value) {
    queryParams.value.applyTimeSTime = value + " 00:00:00";
    queryParams.value.applyTimeETime = value + " 23:59:59";
  } else {
    queryParams.value.applyTimeSTime = undefined;
    queryParams.value.applyTimeETime = undefined;
  }
}

optionInit();
function optionInit() {
  init().then((res) => {
    supplyTypeOption.value = res.data.supplyTypeOptions;
    supplyStatusOption.value = res.data.supplyStatusOptions;
  });
}
</script>
      
<style scoped>
.table-header-search {
  width: 200px;
  float: left;
  margin-right: 15px;
}
.table-header {
  margin-top: 0px;
  margin-bottom: 15px;
  overflow: hidden;
}
.table-header-button {
  float: right;
  margin-left: 10px;
}
</style>