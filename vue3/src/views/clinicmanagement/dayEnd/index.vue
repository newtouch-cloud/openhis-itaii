<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryRef"
      :inline="true"
      v-show="showSearch"
      label-width="90px"
    >
      <el-form-item label="查询日期：">
        <el-date-picker
          v-model="queryTime"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 300px; margin-right: 20px"
          value-format="YYYY-MM-DD"
        />
        <el-button type="primary" plain icon="Search" @click="getValue">查询</el-button>
      </el-form-item>
      <!-- <el-form-item label="科室：" prop="sourceLocationId">
        <el-select
          v-model="queryParams.sourceLocationId"
          placeholder=""
          clearable
          style="width: 150px"
        >
          <el-option
            v-for="issueDepartment in issueDepartmentDto"
            :key="issueDepartment.id"
            :label="issueDepartment.name"
            :value="issueDepartment.id"
          />
        </el-select>
      </el-form-item> -->
    </el-form>

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Search" @click="getValue">查询</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="CircleClose" @click="handleClear">重置</el-button>
      </el-col>
    </el-row> -->
    <el-row
      :gutter="10"
      outpatientNo="mb8"
      style="
        margin: 20px 0;
        display: flex;
        align-items: center;
        justify-content: flex-start;
        padding: 0 20px;
      "
    >
      <!-- <el-col :span="3">
        <span>经办人编号：</span>
      </el-col> -->
      <el-col :span="3">
        <span class="label">经办人姓名：</span>
        <span class="value"> {{ userStore.nickName }}</span>
      </el-col>
      <el-col :span="3">
        <span class="label">科室：</span>
        <span class="value">{{ userStore.orgName }}</span>
      </el-col>
      <el-col :span="4.5">
        <span class="label">时间：</span>
        <span class="value"> {{ queryTime[0] + '~' + queryTime[1] }} </span>
      </el-col>
    </el-row>
    <!-- <el-table
      v-loading="loading"
      :data="purchaseinventoryList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column
        label="科目"
        align="center"
        key="name"
        prop="name"
        width="140"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="应收"
        align="center"
        key="purchasePrice"
        prop="purchasePrice"
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
        label="优惠"
        align="center"
        key="price"
        prop="price"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="科目"
        align="center"
        key="name"
        prop="name"
        width="140"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="应收"
        align="center"
        key="purchasePrice"
        prop="purchasePrice"
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
        label="优惠"
        align="center"
        key="price"
        prop="price"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="科目"
        align="center"
        key="name"
        prop="name"
        width="140"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="应收"
        align="center"
        key="purchasePrice"
        prop="purchasePrice"
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
        label="优惠"
        align="center"
        key="price"
        prop="price"
        :show-overflow-tooltip="true"
      />
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    /> -->
    <el-row
      :gutter="10"
      outpatientNo="mb8"
      style="
        margin: 20px 0;
        display: flex;
        align-items: center;
        justify-content: flex-start;
        padding: 0 20px;
      "
    >
      <el-col :span="3">
        <span class="label">实际现金收入：</span>
        <span class="value"> {{ formatValue(reportValue.cashSum) }}</span>
      </el-col>
      <el-col :span="3">
        <span class="label">现金：</span>
        <span class="value">{{ formatValue(reportValue.rmbCashSum) }}</span>
      </el-col>
      <el-col :span="3">
        <span class="label">微信：</span>
        <span class="value">{{ formatValue(reportValue.vxCashSum) }}</span>
      </el-col>
      <el-col :span="3">
        <span class="label">支付宝：</span>
        <span class="value">{{ formatValue(reportValue.aliCashSum) }}</span>
      </el-col>
    </el-row>
    <el-row
      :gutter="10"
      outpatientNo="mb8"
      style="
        margin: 20px 0;
        display: flex;
        align-items: center;
        justify-content: flex-start;
        padding: 0 20px;
      "
    >
      <el-col :span="3">
        <span class="label">统筹支付：</span>
        <span class="value">{{ formatValue(reportValue.tcSum) }}</span>
      </el-col>
      <el-col :span="3">
        <span class="label">账户支付：</span>
        <span class="value">{{ formatValue(reportValue.zhSum) }}</span>
      </el-col>
      <el-col :span="3">
        <span class="label">基金支付总额：</span>
        <span class="value">{{ formatValue(reportValue.fundSum) }}</span>
      </el-col>
      <!-- <el-col :span="3">
        <span>医保人次：{{ reportValue.aliCashSum }}</span>
      </el-col> -->
    </el-row>
    <el-row
      :gutter="10"
      outpatientNo="mb8"
      style="
        margin: 20px 0;
        display: flex;
        align-items: center;
        justify-content: flex-start;
        padding: 0 20px;
      "
    >
      <el-col :span="3">
        <span class="label">诊查费：</span>
        <span class="value">{{ formatValue(reportValue.DIAGNOSTIC_FEE) }}</span>
      </el-col>
      <el-col :span="3">
        <span class="label">检查费：</span>
        <span class="value">{{ formatValue(reportValue.CHECK_FEE) }}</span>
      </el-col>
      <el-col :span="3">
        <span class="label">化验费：</span>
        <span class="value">{{ formatValue(reportValue.DIAGNOSTIC_TEST_FEE) }}</span>
      </el-col>
      <el-col :span="3">
        <span class="label">治疗费：</span>
        <span class="value">{{ formatValue(reportValue.MEDICAL_EXPENSE_FEE) }}</span>
      </el-col>
    </el-row>
    <el-row
      :gutter="10"
      outpatientNo="mb8"
      style="
        margin: 20px 0;
        display: flex;
        align-items: center;
        justify-content: flex-start;
        padding: 0 20px;
      "
    >
      <el-col :span="3">
        <span class="label">西药费：</span>
        <span class="value">{{ formatValue(reportValue.WEST_MEDICINE) }}</span>
      </el-col>
      <el-col :span="3">
        <span class="label">中药饮片费：</span>
        <span class="value">{{ formatValue(reportValue.CHINESE_MEDICINE_SLICES_FEE) }}</span>
      </el-col>
      <el-col :span="3">
        <span class="label">中成药费：</span>
        <span class="value">{{ formatValue(reportValue.CHINESE_MEDICINE_FEE) }}</span>
      </el-col>
    </el-row>
    <el-row
      :gutter="10"
      outpatientNo="mb8"
      style="
        margin: 20px 0;
        display: flex;
        align-items: center;
        justify-content: flex-start;
        padding: 0 20px;
      "
    >
      <el-col :span="3">
        <span class="label">诊疗费：</span>
        <span class="value">{{ formatValue(reportValue.GENERAL_CONSULTATION_FEE) }}</span>
      </el-col>
      <el-col :span="3">
        <span class="label">挂号费：</span>
        <span class="value">{{ formatValue(reportValue.REGISTRATION_FEE) }}</span>
      </el-col>
      <el-col :span="3">
        <span class="label">其他费用：</span>
        <span class="value">{{ formatValue(reportValue.OTHER_FEE) }}</span>
      </el-col>
      <!-- <el-col :span="3">
        <span>现金：</span>
      </el-col> -->
    </el-row>
    <!-- <el-row
      :gutter="10"
      outpatientNo="mb8"
      style="
        margin: 20px 0;
        display: flex;
        align-items: center;
        justify-content: flex-start;
        padding: 0 20px;
      "
    >
      <el-col :span="3">
        <span>应收金额：0.00</span>
      </el-col>
      <el-col :span="3">
        <span>实收金额：</span>
      </el-col>
      <el-col :span="3">
        <span>优惠金额：</span>
      </el-col>
    </el-row> -->
  </div>
</template>

<script setup name="dayEnd">
import { getRreportReturnIssue, getTotal } from './component/api';
import dayjs from 'dayjs';
import useUserStore from '@/store/modules/user';
import { formatDate, formatDateStr } from '@/utils/index';

const userStore = useUserStore();
// import Dialog from "./components/Dialog";

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
const title = ref('');
const reportValue = ref({});
const queryTime = ref([
  formatDateStr(new Date(), 'YYYY-MM-DD'),
  formatDateStr(new Date(), 'YYYY-MM-DD'),
]);

const data = reactive({
  queryParams: {
    form: {},
    pageNo: 1,
    pageSize: 10,
    searchKey: undefined,
    purposeLocationId: undefined,
    sourceLocationId: undefined,
    supplierId: undefined,
    approvalTimeSTime: undefined,
    approvalTimeETime: undefined,
  },
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);

getValue();
function getValue() {
  getTotal({
    startTime: queryTime.value[0] + ' 00:00:00',
    endTime: queryTime.value[1] + ' 23:59:59',
    entererId: userStore.practitionerId,
  }).then((res) => {
    reportValue.value = res.data;
    console.log(reportValue.value, '123123');
  });
}

function getPharmacyCabinetLists() {
  // occurrenceTime.value =
  //   getDepartmentList().then((response) => {
  //     issueDepartmentDto.value = response.data
  //   })
}
/** 查询调拨管理项目列表 */
function getList() {
  loading.value = true;
  getRreportReturnIssue(queryParams.value).then((res) => {
    loading.value = false;
    purchaseinventoryList.value = res.data.records;
    total.value = res.data.total;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.approvalTimeSTime =
    occurrenceTime.value && occurrenceTime.value.length == 2
      ? occurrenceTime.value[0] + ' 00:00:00'
      : '';
  queryParams.value.approvalTimeETime =
    occurrenceTime.value && occurrenceTime.value.length == 2
      ? occurrenceTime.value[1] + ' 23:59:59'
      : '';
  queryParams.value.pageNo = 1;
  getList();
}

/** 清空条件按钮操作 */
function handleClear() {
  // 清空查询条件
  queryParams.value.approvalTimeSTime = '';
  queryParams.value.approvalTimeETime = '';
  occurrenceTime.value = '';
  proxy.resetForm('queryRef');
  getList();
}

/** 选择条数  */
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

function formatValue(value) {
  return value == null || value == undefined ? '0.00 元' : value.toFixed(2) + ' 元';
}

getList();
getPharmacyCabinetLists();
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
.label {
  display: inline-block;
  width: 120px !important;
}
.value {
  float: right;
}
.el-col {
  margin-right: 50px;
}
</style>