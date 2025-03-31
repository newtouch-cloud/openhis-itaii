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
          height="620"
          :data="patientList"
          row-key="encounterId"
          @cell-click="clickRow"
          highlight-current
        >
          <el-table-column label="病历号" align="center" prop="patientBusNo" />
          <el-table-column label="姓名" align="center" prop="patientName" />
          <el-table-column label="时间" align="center" prop="startTime">
            <template #default="scope">
              {{ formatDate(scope.row.startTime) }}
            </template>
          </el-table-column>
          <el-table-column
            label="收费状态"
            align="center"
            prop="statusEnum_enumText"
          />
        </el-table>
      </div>
    </el-card>
    <div style="width: 65%">
      <el-card style="margin-bottom: 20px">
        <template #header>
          <span style="vertical-align: middle">基本信息</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item label="姓名:">{{
            patientInfo.patientName
          }}</el-descriptions-item>
          <el-descriptions-item label="性别:">{{
            patientInfo.genderEnum_enumText
          }}</el-descriptions-item>
          <el-descriptions-item label="年龄:">{{
            patientInfo.age
          }}</el-descriptions-item>
          <el-descriptions-item label="合同类型:">{{
            patientInfo.categoryEnum_enumText
          }}</el-descriptions-item>
          <el-descriptions-item label="身份证号:">{{
            patientInfo.idCard
          }}</el-descriptions-item>
          <!-- <el-descriptions-item label="手机号">{{ patientInfo.name }}</el-descriptions-item>
          <el-descriptions-item label="出生日期">{{ patientInfo.name }}</el-descriptions-item> -->
        </el-descriptions>
      </el-card>
      <el-card style="min-width: 1100px">
        <template #header>
          <span style="vertical-align: middle">收费项目</span>
        </template>
        <el-button type="primary" @click="addEmr()" :disabled="buttonDisabled"
          >确认收费</el-button
        >
        <el-button
          type="primary"
          @click="payToSelt()"
          style="margin-left: 20px"
          :disabled="buttonDisabled"
          >医保转自费</el-button
        >
        <el-button
          type="primary"
          @click="patToMedicalInsurance()"
          style="margin-left: 20px"
          :disabled="buttonDisabled"
          >自费转医保</el-button
        >
        <el-table
          ref="chargeListRef"
          height="500"
          :data="chargeList"
          row-key="encounterId"
          v-loading="chargeLoading"
          width=""
        >
          <el-table-column
            label="收费项目"
            align="center"
            prop="contextEnum_enumText"
          />
          <el-table-column
            label="收费状态"
            align="center"
            prop="statusEnum_enumText"
          />
          <el-table-column label="总价" align="center" prop="totalPrice" />
          <el-table-column
            label="处方号"
            align="center"
            prop="prescriptionNo"
          />
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script setup name="clinicCharge">
import {
  getList,
  getChargeList,
  changeToSelfPay,
  changeToMedicalInsurance,
} from "./components/api";
import { formatDate } from "@/utils/index";

const { proxy } = getCurrentInstance();
const queryParams = ref({
  pageNum: 1,
  pageSize: 50,
  statusEnum: 1,
});

const patientList = ref([]);
const chargeList = ref([]);
const chargeLoading = ref(false);
const encounterId = ref("");
const patientInfo = ref({});
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
  getChargeList(row.encounterId).then((res) => {
    chargeList.value = res.data.data;
    setTimeout(() => {
      chargeLoading.value = false;
    }, 100);
  });
}

/**
 * 医保转自费
 */
function payToSelt() {
  changeToSelfPay(encounterId.value).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess("操作成功");
    }
  });
}

/**
 * 自费转医保
 */
function patToMedicalInsurance() {
  changeToMedicalInsurance(encounterId.value).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess("操作成功");
    }
  });
}
</script>
<style scoped>
/* ::v-deep .el-table__row:hover > td {
  background-color: #cde5ff !important; 
} */
/* ::v-deep .el-card__body {
	padding: 0 !important;
} */
</style>