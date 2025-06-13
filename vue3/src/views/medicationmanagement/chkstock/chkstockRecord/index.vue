<template>
    <div class="app-container">
      <el-form
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        v-show="showSearch"
        label-width="90px"
      >
        <el-form-item label="单据号:" prop="searchKey">
          <el-input
            v-model="queryParams.searchKey"
            placeholder="单据号："
            clearable
            style="width: 220px"
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="审批状态：" prop="statusEnum" label-width="100px">
          <el-select
            v-model="queryParams.statusEnum"
            placeholder=""
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="supplyStatus in supplyStatusOptions"
              :key="supplyStatus.value"
              :label="supplyStatus.label"
              :value="supplyStatus.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="制单人：" prop="applicantId" label-width="120px">
          <el-select
            v-model="queryParams.applicantId"
            placeholder=""
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="practitioner in applicantListOptions"
              :key="practitioner.value"
              :label="practitioner.label"
              :value="practitioner.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="制单日期：">
          <el-date-picker
            v-model="occurrenceTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          ></el-date-picker>
        </el-form-item>
      </el-form>
  
      <el-row :gutter="10" class="mb8">
        <!-- 添加记录 -->
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="Plus"
            @click="openAddStockPart"
            
            >新增盘点单</el-button
          >
          <!-- v-hasPermi="['system:user:add']" -->
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="Plus"
            @click="openAddStockBatch"
            
            >新增批量盘点单</el-button
          >
          <!-- v-hasPermi="['system:user:add']" -->
        </el-col>
        <!-- 查询 -->
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="Search"
            @click="handleQuery"
           
            >查询</el-button
          >
           <!-- v-hasPermi="['system:user:import']" -->
        </el-col>
        <!-- 重置 -->
        <el-col :span="1.5">
          <el-button
            type="warning"
            plain
            icon="CircleClose"
            @click="handleClear"
           
            >重置</el-button
          >
           <!-- v-hasPermi="['system:user:export']" -->
        </el-col>
      </el-row>
  
      <el-table
        v-loading="loading"
        :data="stockinventoryList"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column
          label="单据号"
          align="center"
          key="supplyBusNo"
          prop="supplyBusNo"
          width="200"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="单据类型"
          align="center"
          key="typeEnum_enumText"
          prop="typeEnum_enumText"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="审批状态"
          align="center"
          key="statusEnum_enumText"
          prop="statusEnum_enumText"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="盘点仓库"
          align="center"
          key="purposeLocationId_dictText"
          prop="purposeLocationId_dictText"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="盈亏金额"
          align="center"
          key="breakevenPrice"
          prop="breakevenPrice"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="制单人"
          align="center"
          key="applicantId_dictText"
          prop="applicantId_dictText"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="审核人"
          align="center"
          key="approverId_dictText"
          prop="approverId_dictText"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="制单日期"
          align="center"
          key="createTime"
          prop="createTime"
          width="180"
          :show-overflow-tooltip="true"
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
          :show-overflow-tooltip="true"
        >
          <template #default="scope">
            <span>{{ parseTime(scope.row.approvalTime) }}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column
          label="备注"
          align="center"
          key="remake"
          prop="remake"
        /> -->
        <el-table-column
          label="操作"
          align="center"
          width="230"
          class-name="small-padding fixed-width"
        >
          <template #default="scope">
            <el-button
              link
              type="primary"
              icon="View"
              @click="handleUpdate(scope.row,'view')"
              
              >详情</el-button
            >
            <el-button
              link
              type="primary"
              icon="Edit"
              @click="handleUpdate(scope.row)"
              :disabled="scope.row.statusEnum != '1' && scope.row.statusEnum != '9'"
              >编辑</el-button
            >
            <!-- v-hasPermi="['system:user:edit']" -->
            <el-button
              link
              type="primary"
              icon="View"
              @click="handleSubmitApproval(scope.row)"
              
              v-if="scope.row.statusEnum == '1' || scope.row.statusEnum == '9'"
              >提交审批</el-button
            >
            <!-- v-hasPermi="['system:user:remove']" -->
            <el-button
              link
              type="primary"
              icon="View"
              @click="handleWithdrawApproval(scope.row)"
              
              v-if="scope.row.statusEnum == '2'"
              >撤销审批</el-button
            >
            <!-- v-hasPermi="['system:user:remove']" -->
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
      <!-- <stock-receipt-dialog
        ref="stockReceiptRef"
        :cabinetListOptions="cabinetListOptions"
        :categoryListOptions ="categoryListOptions"
        :profitReasonOptions = "profitReasonOptions"
        :busNoAdd="busNoAdd"
        :item="currentData"
        :editRow="editRow"
        @refresh="getList"
      /> -->
    </div>
  </template>
  
  <script setup name="chkstockRecord">
  import {
    getStockinventoryList,
    getstocktakingDetail,
    getInit,
    submitApproval,
    withdrawApproval,
    getDetailInit,
  } from "../components/api";
  
  // import stockReceiptDialog from "./components/stockReceiptDialog";
  
  const router = useRouter();
  const { proxy } = getCurrentInstance();
  const stockinventoryList = ref([]);
  const open = ref(false);
  const loading = ref(true);
  const showSearch = ref(true);
  const ids = ref([]);
  const single = ref(true);
  const multiple = ref(true);
  const total = ref(0);
  const occurrenceTime = ref([]);
  const busNoAdd = ref(""); // 单据号新增
  const applicantListOptions = ref(undefined); // 制单人列表
  const cabinetListOptions = ref(undefined); // 仓库列表
  const categoryListOptions = ref(undefined); // 药品类型
  const pharmacyListOptions = ref(undefined); // 药房列表
  const supplyStatusOptions = ref(undefined); // 审批状态
  const profitReasonOptions = ref(undefined); // 盈亏原因
  const editRow = ref({});
  // 使用 ref 定义当前编辑的采购
  const currentData = ref({});
  // 是否停用
  const statusFlagOptions = ref(undefined);
  
  const data = reactive({
    form: {},
    queryParams: {
        supplyBusNo: undefined, // 编码
        statusEnum: undefined, // 审批状态
        applicantId: undefined, // 制单人
        createTimeSTime:undefined,
        createTimeETime:undefined,
        pageNo:1,
        pageSize: 10,
        searchKey: undefined, // 供应商名称
    },
    rules: {},
  });
  
  const { queryParams, form, rules } = toRefs(data);
  
  /** 列表页查询下拉树结构 */
  function getStockinventoryTypeList() {
    getInit().then((response) => {
        console.log('列表页下拉树response1111111',response)
      busNoAdd.value = response.data.busNo; // 单据号新增
      applicantListOptions.value = response.data.applicantListOptions; // 制单人列表

      cabinetListOptions.value = response.data.cabinetListOptions; // 仓库列表
      categoryListOptions.value = response.data.categoryListOptions; //  药品类型列表
      pharmacyListOptions.value = response.data.pharmacyListOptions; // 药房列表
      profitReasonOptions.value = response.data.profitReasonOptions; // 盈亏类型列表

      supplyStatusOptions.value = response.data.supplyStatusOptions; // 审批状态
    });
  }
  /** 详情页查询下拉树结构 */
  
  /** 查询盘点列表 */
  function getList() {
    loading.value = true;
    getStockinventoryList(queryParams.value).then((res) => {
      console.log('查询盘点列表response1111111',res)
      loading.value = false;
      stockinventoryList.value = res.data.records;
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
    // 清空查询条件
    queryParams.value.createTimeSTime = ""
    queryParams.value.createTimeETime = ""
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
  
  /** 打开商品盘点 */
  function openAddStockPart() {
    // nextTick(() => {
    //   proxy.$refs["stockReceiptRef"].show();
    // });
    

    router.push({ path: '/medicationmanagement/chkstock/chkstockPart' })
  }
  /** 打开批量商品盘点 */
  function openAddStockBatch() {
    // nextTick(() => {
    //   proxy.$refs["stockReceiptRef"].show();
    // });
    router.push({ path: '/medicationmanagement/chkstock/chkstockBatch' })
  }
  
  /** 修改按钮操作 */
  function handleUpdate(row,view) {
    editRow.value = row;
    if(row.typeEnum==4){  // 盘点
      if(view){
        router.replace({ path: '/medicationmanagement/chkstock/chkstockPart',query:{supplyBusNo:row.supplyBusNo,view:view} })
      }else{
        router.push({ path: '/medicationmanagement/chkstock/chkstockPart',query:{supplyBusNo:editRow.value.supplyBusNo} })
      }
    }else{
      if(view){
        router.replace({ path: '/medicationmanagement/chkstock/chkstockBatch',query:{supplyBusNo:row.supplyBusNo,view:view} })
      }else{
        router.push({ path: '/medicationmanagement/chkstock/chkstockBatch',query:{supplyBusNo:editRow.value.supplyBusNo} })
      }
    }
  }
  /** 提交审核按钮 */
  function handleSubmitApproval(row) {
    submitApproval(row.supplyBusNo).then((response) => {
      proxy.$modal.msgSuccess("提交审批成功");
      open.value = false;
      getList();
    });
  }
  
  /** 撤回审批按钮 */
  function handleWithdrawApproval(row) {
    withdrawApproval(row.supplyBusNo).then((response) => {
      proxy.$modal.msgSuccess("撤销审批成功");
      open.value = false;
      getList();
    });
  }
  
  getStockinventoryTypeList();
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