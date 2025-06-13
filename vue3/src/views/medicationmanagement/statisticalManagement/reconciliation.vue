<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-form
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        label-width="100px"
      >
        <el-form-item label="结算日期：">
          <el-date-picker
            v-model="occurrenceTime"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 300px"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="清算类型：" prop="clrType">
          <el-select
            v-model="queryParams.clrType"
            placeholder="请选择清算类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="categoryCode in clrTypeOptions"
              :key="categoryCode.value"
              :label="categoryCode.label"
              :value="categoryCode.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="险种类型：" prop="insuType">
          <el-select
            v-model="queryParams.insuType"
            placeholder="请选择险种类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="insuType in insutype"
              :key="insuType.value"
              :label="insuType.label"
              :value="insuType.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="合同：" prop="contractNo">
          <el-select
            v-model="queryParams.contractNo"
            placeholder="请选择合同"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="insuType in contractNoOptions"
              :key="insuType.busNo"
              :label="insuType.contractName"
              :value="insuType.busNo"
            />
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="医院" prop="orgId">
          <el-input
            v-model="queryParams.orgId"
            placeholder="请输入医院id"
            clearable
            style="width: 200px"
            @keyup.enter="handleQuery"
          />
        </el-form-item> -->
      </el-form>
      <!-- <el-col :span="1.5"> -->
      <!-- v-hasPermi="['system:user:import']" -->
      <!-- <el-button type="primary" plain icon="Edit" @click="getList">查询</el-button> -->
      <el-button type="primary" plain icon="Edit" @click="handleQuery">结算</el-button>
      <!-- </el-col> -->
      <!-- <el-col :span="1.5"> -->
      <!-- v-hasPermi="['system:user:export']" -->
      <el-button type="primary" plain icon="Edit" @click="handleReconciliation" :disabled="ids.length==0">
        医保对总账
      </el-button>
      <!-- </el-col> -->
    </el-row>
    <!-- <div style="margin-bottom: 5px">
      定点医药机构结算笔数：{{ counts }}（其中 医疗费总额：{{ totalPrice1 }}，基金支付总额：{{
        totalPrice2
      }}，个人账户支付金额：{{ totalPrice3 }}，现金支付金额：{{ totalPrice4 }}，认可费用总额：{{
        totalPrice5
      }}
      ）
    </div> -->
    <el-table
      v-loading="loading"
      :data="purchaseinventoryList"
      border
      @select="clickRow" 
      ref="refTables"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="清算类别" align="center" prop="contractNo_dictText" />
      <el-table-column label="险种" align="center" prop="insutype_dictText" />
      <el-table-column label="是否异地" align="center" key="locationName" prop="locationName" />
      <el-table-column label="医疗费用总额" align="right" prop="medFeeSumAmt" header-align="center">
        <template #default="scope">
          {{ formatNumber(scope.row.medFeeSumAmt) + ' 元' }}
        </template>
      </el-table-column>
      <el-table-column label="基金支付金额" align="right" prop="fundPaySumAmt" header-align="center">
        <template #default="scope">
          {{ formatNumber(scope.row.fundPaySumAmt) + ' 元' }}
        </template>
      </el-table-column>
      <el-table-column label="个人账户支付总额" align="right" prop="acctPay" header-align="center">
        <template #default="scope">
          {{ formatNumber(scope.row.acctPay) + ' 元' }}
        </template>
      </el-table-column>
      <el-table-column label="账户共济总额" align="right" prop="acctGjPay" header-align="center">
        <template #default="scope">
          {{ formatNumber(scope.row.acctGjPay) + ' 元' }}
        </template>
      </el-table-column>
      <el-table-column label="结算笔数" align="center" prop="fixMedInsSetlCnt">
        <template #default="scope">
          {{ scope.row.fixMedInsSetlCnt + ' 笔' }}
        </template>
      </el-table-column>
    </el-table>
    <!-- <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    /> -->
    <el-form :inline="true" label-width="90px" style="width: 100%; margin-top: 30px">
      <el-form-item label="对账结果：" style="width: 100%">
        <el-input
          v-model="reconciliation"
          :autosize="{ minRows: 4, maxRows: 10 }"
          type="textarea"
          disabled
          placeholder=""
        />
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup name="reconciliation">
import {
  ybRequestReconcile, // 医保对账
  getReconcileList, // 对账列表 结算
  getContractList, // 合同
} from './statisticalManagent';
const { proxy } = getCurrentInstance();
const { insutype } = 
proxy.useDict( "insutype");
// 险种类型
const purchaseinventoryList = ref([]);
const loading = ref(false);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const occurrenceTime = ref([]);
const contractNoOptions = ref([]);
const clrTypeOptions = ref([]);
const selectedRow = ref(null);
const reconciliation = ref('');
const counts = ref(0);
const totalPrice1 = ref(0);
const totalPrice2 = ref(0);
const totalPrice3 = ref(0);
const totalPrice4 = ref(0);
const totalPrice5 = ref(0);

const data = reactive({
  form: {},
  queryParams: {
    // pageNo: 1,
    // pageSize: 10,
    // searchKey: undefined,
    insuType: undefined, // 险种
    clrType: undefined, //住院或门诊
    contractNo: undefined,
    orgId: undefined,
    stmtBegnDate: undefined,
    stmtEndDate: undefined,
  },
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);

function clickRow(row) {
  if (row.length > 1) {
    let del_row = row.shift(); // 删除选中的第一项
    proxy.$refs["refTables"].toggleRowSelection(del_row, false); //改变table勾选状态
  }
}
function getContractLists() {
  getContractList().then((response) => {
    contractNoOptions.value = response.data;
  });
  clrTypeOptions.value = [
    { value: 11, label: '门诊' },
    { value: 21, label: '住院' },
  ];
  // insuTypeOptions.value = [
  //   { value: 310, label: '职工基本医疗保险' },
  //   { value: 390, label: '城乡居民基本医疗保险' },
  //   { value: 391, label: '城镇居民基本医疗保险' },
  //   { value: 399, label: '其他特殊人员医疗保障' },
  // ];
}
/** 查询列表 */
function getList() {
  loading.value = true;
  getReconcileList(queryParams.value).then((res) => {
    loading.value = false;
    purchaseinventoryList.value = res.data.record ? res.data.record : res.data;
    // [{insutype:"410",clrType: 11,contractNo:"229900"},{insutype:"410",clrType: 21,contractNo:"229900"}]
    counts.value = res.data.fixMedInsSetlCnt | 0;
    totalPrice1.value = res.data.medFeeSumAmt | 0;
    totalPrice2.value = res.data.fundPaySumAmt | 0;
    totalPrice3.value = res.data.acctPay | 0;
    totalPrice4.value = res.data.acctGjPay | 0;
    totalPrice5.value =
      totalPrice1.value + totalPrice2.value + totalPrice3.value + totalPrice4.value;
    total.value = res.data.total;
  });
}

function formatNumber(value) {
  return value ? value.toFixed(2) : '0.00';
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.stmtBegnDate =
    occurrenceTime.value && occurrenceTime.value.length == 2
      ? occurrenceTime.value[0] + ' 00:00:00'
      : '';
  queryParams.value.stmtEndDate =
    occurrenceTime.value && occurrenceTime.value.length == 2
      ? occurrenceTime.value[1] + ' 23:59:59'
      : '';
  // queryParams.value.pageNo = 1;
  getList();
}

/** 按钮操作 */
function handleReconciliation() {
  // 调用接口返回值
  // loading.value = true;
  reconciliation.value = '';
  let queryParams = {
    insuType:selectedRow.value.insutype.toString(),
    clrType:selectedRow.value.clrType,
    contractNo:selectedRow.value.contractNo.toString(),
    stmtBegnDate:occurrenceTime.value && occurrenceTime.value.length == 2
      ? occurrenceTime.value[0] + ' 00:00:00'
      : '',
    stmtEndDate:occurrenceTime.value && occurrenceTime.value.length == 2
      ? occurrenceTime.value[1] + ' 23:59:59'
      : '',
  }
  ybRequestReconcile(queryParams).then((res) => {
    // loading.value = false;
    reconciliation.value = res.data;
  });
}

/** 选择条数  */
function handleSelectionChange(selection) {
  selectedRow.value = selection[selection.length-1]
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

getList();
getContractLists();
</script>
<style scoped>
.custom-tree-node {
  display: flex;
  align-items: center;
}


::v-deep .el-textarea.is-disabled .el-textarea__inner {
  resize: none !important;
}
</style>