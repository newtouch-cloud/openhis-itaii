<template>
  <div
    style="display: flex; justify-content: space-between"
    class="app-container"
  >
    <div style="width: 30%">
      <el-table
        ref="patientListRef"
        height="700"
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
        <el-table-column
          label="收费状态"
          align="center"
          prop="statusEnum_enumText"
        />
      </el-table>
    </div>
    <div style="width: 65%">
      <div></div>
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
        height="400"
        :data="chargeList"
        row-key="encounterId"
        @cell-click="clickRow"
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
        <el-table-column label="处方号" align="center" prop="prescriptionNo" />
      </el-table>
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
::v-deep .el-table__row:hover > td {
  background-color: #cde5ff !important; /* 设置为透明或其他你想要的颜色 */
}
</style>