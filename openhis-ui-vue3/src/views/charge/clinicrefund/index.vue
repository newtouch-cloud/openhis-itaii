<template>
  <div
    style="display: flex; justify-content: space-between"
    class="app-container"
  >
    <el-card style="width: 34%">
      <template #header>
        <span style="vertical-align: middle">患者列表</span>
      </template>
      <div style="width: 100%">
        <el-input
          v-model="queryParams.patientName"
          placeholder="请输入患者名"
          clearable
          style="width: 49%; margin-bottom: 10px; margin-right: 10px"
          @keyup.enter="handleQuery"
        >
          <template #append>
            <el-button icon="Search" @click="handleQuery" />
          </template>
        </el-input>
        <el-select
          style="width: 49%; margin-bottom: 10px"
          placeholder="收费状态"
        >
        </el-select>
        <el-date-picker
          v-model="value1"
          type="daterange"
          range-separator="~"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          style="width: 100%; margin-bottom: 10px"
        />
        <el-table
          ref="patientListRef"
          height="630"
          :data="patientList"
          row-key="encounterId"
          @cell-click="clickRow"
          highlight-current
          width=""
        >
          <el-table-column label="病历号" align="center" prop="patientBusNo" />
          <el-table-column label="姓名" align="center" prop="patientName" />
          <el-table-column label="时间" align="center" prop="startTime">
            <template #default="scope">
              {{ formatDate(scope.row.startTime) }}
            </template>
          </el-table-column>
          <!-- <el-table-column
          label="收费状态"
          align="center"
          prop="statusEnum_enumText"
        /> -->
        </el-table>
      </div>
    </el-card>
    <div style="width: 65%">
      <el-card style="margin-bottom: 20px">
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
          <el-descriptions-item label="合同类型:">
            {{ patientInfo.categoryEnum_enumText }}
          </el-descriptions-item>
          <el-descriptions-item label="结算时间:">
            {{ patientInfo.billDate }}
          </el-descriptions-item>
          <el-descriptions-item label="账单总额:">
            {{ patientInfo.totalAmount }}
          </el-descriptions-item>
          <el-descriptions-item label="医保支付:">
            {{ patientInfo.insurancePrice }}
          </el-descriptions-item>
          <el-descriptions-item label="自费金额:">
            {{ patientInfo.selfPrice }}
          </el-descriptions-item>
          <el-descriptions-item label="支付方式:">
            {{ patientInfo.typeCode_dictText }}
          </el-descriptions-item>
          <el-descriptions-item label="发票号:">
            {{ patientInfo.idCard }}
          </el-descriptions-item>
          <!-- <el-descriptions-item label="手机号">{{ patientInfo.name }}</el-descriptions-item>
          <el-descriptions-item label="出生日期">{{ patientInfo.name }}</el-descriptions-item> -->
        </el-descriptions>
      </el-card>
      <el-card>
        <template #header>
          <span style="vertical-align: middle">退费单据</span>
        </template>
        <el-button
          type="primary"
          @click="handleRefund()"
          :disabled="buttonDisabled"
          >确认退费</el-button
        >
        <el-table
          ref="chargeListRef"
          height="475"
          :data="chargeList"
          row-key="encounterId"
          @cell-click="clickRow"
          v-loading="chargeLoading"
          width=""
        >
          <el-table-column label="单据号" align="center" prop="paymentNo" />
          <el-table-column
            label="付款总额"
            align="center"
            prop="displayAmount"
          />
          <el-table-column
            label="处方号"
            align="center"
            prop="prescriptionNo"
          />
          <el-table-column
            label="开单科室"
            align="center"
            prop="locationId_dictText"
          />
        </el-table>
      </el-card>
    </div>
  </div>
</template>
  
<script setup name="clinicCharge">
import { getList, getRefundList, refund } from "./components/api";
import { formatDate } from "@/utils/index";
const { proxy } = getCurrentInstance();
const queryParams = ref({
  pageNum: 1,
  pageSize: 50,
});

const patientList = ref([]);
const patientInfo = ref({});
const chargeList = ref([]);
const chargeLoading = ref(false);
const encounterId = ref("");
getPatientList();
/**
 * 患者列表
 */
function getPatientList() {
  getList(queryParams.value).then((res) => {
    patientList.value = res.data.data.records;
  });
}

/**
 * 点击患者列表行 获取处方列表
 */
function clickRow(row) {
  patientInfo.value = row;
  chargeLoading.value = true;
  encounterId.value = row.encounterId;
  getRefundList(row.encounterId).then((res) => {
    chargeList.value = res.data.data;
    setTimeout(() => {
      chargeLoading.value = false;
    }, 100);
  });
}

function handleRefund() {
  refund(
    chargeList.value.map((item) => {
      item.id;
    })
  ).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess("操作成功");
    }
    getPatientList();
  });
}
</script>
<style scoped>
::v-deep .el-table__row:hover > td {
  background-color: #cde5ff !important; /* 设置为透明或其他你想要的颜色 */
}
</style>