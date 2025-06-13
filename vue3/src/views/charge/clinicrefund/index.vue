<template>
  <div style="display: flex; justify-content: space-between" class="app-container">
    <el-card style="width: 30%">
      <template #header>
        <span style="vertical-align: middle">患者列表</span>
      </template>
      <div style="width: 100%">
        <el-input
          v-model="queryParams.patientName"
          placeholder="请输入患者名"
          clearable
          style="width: 49%; margin-bottom: 10px; margin-right: 10px"
          @keyup.enter="getPatientList"
        >
          <template #append>
            <el-button icon="Search" @click="getPatientList" />
          </template>
        </el-input>
        <el-select
          v-model="queryParams.statusEnum"
          style="width: 49%; margin-bottom: 10px"
          placeholder="收费状态"
          @change="getPatientList"
        >
          <el-option
            v-for="item in chargeOption"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <el-date-picker
          v-model="receptionTime"
          type="daterange"
          range-separator="~"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          placement="bottom"
          value-format="YYYY-MM-DD"
          style="width: 84%; margin-bottom: 10px; margin-right: 10px"
        />
        <el-button type="primary" style="margin-bottom: 10px" @click="getPatientList">
          搜索
        </el-button>
        <el-table
          ref="patientListRef"
          height="630"
          :data="patientList"
          row-key="encounterId"
          @cell-click="clickRow"
          highlight-current-row
          width=""
        >
          <el-table-column label="病历号" align="center" prop="encounterBusNo" />
          <el-table-column label="姓名" align="center" prop="patientName" />
          <!-- <el-table-column label="时间" align="center" prop="startTime">
            <template #default="scope">
              {{ formatDate(scope.row.startTime) }}
            </template>
          </el-table-column> -->
          <el-table-column label="收费状态" align="center" prop="statusEnum_enumText" />
        </el-table>
      </div>
    </el-card>
    <div style="width: 69%">
      <el-card style="margin-bottom: 20px; height: 15%">
        <template #header>
          <span style="vertical-align: middle">基本信息</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item label="就诊号:">
            {{ patientInfo.encounterId }}
          </el-descriptions-item>
          <el-descriptions-item label="姓名:">
            {{ patientInfo.patientName }}
          </el-descriptions-item>
          <el-descriptions-item label="性别:">
            {{ patientInfo.genderEnum_enumText }}
          </el-descriptions-item>
          <el-descriptions-item label="年龄:">
            {{ patientInfo.age }}
          </el-descriptions-item>
          <!-- <el-descriptions-item label="合同类型:">
            {{ patientInfo.categoryEnum_enumText }}
          </el-descriptions-item>
          <el-descriptions-item label="结算时间:">
            {{ patientInfo.billDate ? formatDate(patientInfo.billDate) : '' }}
          </el-descriptions-item>
          <el-descriptions-item label="账单总额:">
            {{ patientInfo.totalAmount ? patientInfo.totalAmount.toFixed(2) + ' 元' : '' }}
          </el-descriptions-item>
          <el-descriptions-item label="医保支付:">
            {{ patientInfo.insurancePrice }}
          </el-descriptions-item>
          <el-descriptions-item label="自费金额:">
            {{ patientInfo.selfAmount ? patientInfo.selfAmount.toFixed(2) + ' 元' : '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="支付方式:">
            {{ patientInfo.typeCode_dictText }}
          </el-descriptions-item>
          <el-descriptions-item label="发票号:">
            {{ patientInfo.idCard }}
          </el-descriptions-item> -->
          <!-- <el-descriptions-item label="手机号">{{ patientInfo.name }}</el-descriptions-item>
          <el-descriptions-item label="出生日期">{{ patientInfo.name }}</el-descriptions-item> -->
        </el-descriptions>
      </el-card>
      <el-card style="height: 83%">
        <template #header>
          <span style="vertical-align: middle">退费单据</span>
        </template>
        <!-- <el-button type="primary" @click="handleRefund()" :disabled="buttonDisabled">
          确认退费
        </el-button> -->
        <el-table
          ref="chargeListRef"
          height="510"
          :data="chargeList"
          row-key="encounterId"
          v-loading="chargeLoading"
          :span-method="spanMethod"
          class="no-hover-table"
          border
          width=""
        >
          <!-- <el-table-column type="selection" :selectable="checkSelectable" width="55" /> -->
          <el-table-column label="操作" align="center">
            <template #default="scope">
              <el-button link type="primary" @click="handleRefund(scope.row)">退费</el-button>
            </template>
          </el-table-column>
          <el-table-column prop="paymentId" label="支付单据号" align="center" />
          <el-table-column label="项目单据号" align="center" prop="busNo" width="150" />
          <el-table-column label="项目名称" align="center" prop="itemName" />
          <el-table-column
            label="收费状态"
            align="center"
            prop="chargeStatus_enumText"
            width="100"
          />
          <!-- <el-table-column
            label="发药/执行状态"
            align="center"
            prop="dispenseStatus_enumText"
            width="130"
          >
            <template #default="scope">
              {{ scope.row.dispenseStatus_enumText || scope.row.serviceStatus_enumText }}
            </template>
          </el-table-column> -->
          <el-table-column label="数量" align="center" width="100">
            <template #default="scope">
              {{ scope.row.quantityValue + ' ' + scope.row.quantityUnit_dictText }}
            </template>
          </el-table-column>
          <el-table-column
            label="付款总额"
            align="right"
            prop="totalPrice"
            header-align="center"
            width="100"
          >
            <template #default="scope">
              {{ scope.row.totalPrice.toFixed(2) + ' 元' }}
            </template>
          </el-table-column>
          <!-- <el-table-column label="处方号" align="center" prop="prescriptionNo" /> -->
          <el-table-column label="收款人" align="center" prop="entererName" width="120" />
        </el-table>
      </el-card>
    </div>
    <RefundDialog
      :open="openDialog"
      @close="handleClose"
      :totalAmount="totalAmount"
      :patientInfo="patientInfo"
      :paymentId="paymentId"
      :chargeItemIds="chargeItemIdList"
    />
  </div>
</template>

<script setup name="ClinicCharge">
import {
  getList,
  getRefundList,
  refund,
  init,
  getChargeItemIds,
  validReturnDrug,
} from './components/api';
import { formatDate, formatDateStr } from '@/utils/index';
import RefundDialog from './components/refundDialog.vue';
import Decimal from 'decimal.js';

const { proxy } = getCurrentInstance();
const queryParams = ref({
  pageNum: 1,
  pageSize: 50,
  statusEnum: 7,
});
const spanMap = ref({});
const patientList = ref([]);
const patientInfo = ref({});
const chargeList = ref([]);
const totalAmount = ref(0);
const chargeOption = ref([]);
const chargeLoading = ref(false);
const openDialog = ref(false);
const chargeItemIdList = ref([]);
const encounterId = ref('');
const paymentId = ref('');
const receptionTime = ref([
  formatDateStr(new Date(), 'YYYY-MM-DD'),
  formatDateStr(new Date(), 'YYYY-MM-DD'),
]);
getPatientList();
initOptions();
/**
 * 患者列表
 */
function getPatientList() {
  // if (receptionTime.value.length > 0) {
  //   queryParams.value.receptionTimeSTime = receptionTime.value[0] + ' 00:00:00';
  //   queryParams.value.receptionTimeETime = receptionTime.value[1] + ' 23:59:59';
  // } else {
  //   queryParams.value.receptionTimeSTime = undefined;
  //   queryParams.value.receptionTimeETime = undefined;
  // }
  getList(queryParams.value).then((res) => {
    patientList.value = res.data.records;
  });
}

function initOptions() {
  init().then((res) => {
    chargeOption.value = res.data.chargeItemStatusOptions;
  });
}

// 生成合并行
const generateSpanMap = () => {
  spanMap.value = {};
  let currentId = null;
  let startIndex = 0;

  chargeList.value.forEach((row, index) => {
    if (row.paymentId !== currentId) {
      if (currentId !== null) {
        spanMap.value[currentId] = {
          start: startIndex,
          count: index - startIndex,
        };
      }
      currentId = row.paymentId;
      startIndex = index;
    }
  });

  // 处理最后一个分组
  if (currentId !== null) {
    spanMap.value[currentId] = {
      start: startIndex,
      count: chargeList.value.length - startIndex,
    };
  }
};

// 合并方法（同时处理多选列和paymentId列）
const spanMethod = ({ row, column, rowIndex, columnIndex }) => {
  if (columnIndex <= 1) {
    // 合并前两列
    const group = spanMap.value[row.paymentId];
    if (!group) return;

    if (rowIndex === group.start) {
      return { rowspan: group.count, colspan: 1 };
    } else {
      return { rowspan: 0, colspan: 0 };
    }
  }
};

/**
 * 点击患者列表行 获取处方列表
 */
function clickRow(row) {
  patientInfo.value = row;
  chargeLoading.value = true;
  encounterId.value = row.encounterId;
  getRefundList(row.encounterId).then((res) => {
    chargeList.value = res.data;
    spanMap.value = {};
    chargeList.value.sort((a, b) => a.paymentId.localeCompare(b.paymentId));
    console.log(chargeList.value);
    generateSpanMap();
    setTimeout(() => {
      chargeLoading.value = false;
    }, 100);
  });
}

function handleRefund(row) {
  totalAmount.value = chargeList.value
    .filter((item) => {
      return item.paymentId === row.paymentId;
    })
    .reduce((accumulator, currentRow) => {
      return new Decimal(accumulator).add(new Decimal(currentRow.totalPrice || 0));
    }, 0);
  paymentId.value = row.paymentId;
  patientInfo.value.patientId = row.patientId;
  getChargeItemIds(row.encounterId).then((res) => {
    chargeItemIdList.value = res.data;
    validReturnDrug(row.chargeItemIds.split(',')).then((res) => {
      if (res.code == 200) {
        openDialog.value = true;
      } else {
        proxy.$modal.msgWarning(res.msg);
      }
    });
  });
  // refund(
  //   chargeList.value.map((item) => {
  //     item.id;
  //   })
  // ).then((res) => {
  //   if (res.code == 200) {
  //     proxy.$modal.msgSuccess('操作成功');
  //   }
  //   getPatientList();
  // });
}

function handleClose(value) {
  openDialog.value = false;
  if (value == 'success') {
    proxy.$modal.msgSuccess('操作成功');
    clickRow(patientInfo.value);
  }
}
</script>
<style lang="scss" scoped>
:deep(.no-hover-table) .el-table__body tr:hover > td {
  background: inherit !important;
}
</style>