<template>
  <div class="app-container" style="display: flex; gap: 10px; height: 90vh">
    <div style="width: 25%">
      <div style="height: 100px; padding: 10px 0">
        <div style="display: flex; gap: 50px">
          <el-input
            v-model="queryParams.searchKey"
            placeholder="请输入患者名"
            style="width: 220px"
            @keyup.enter="getEncounterList"
          >
            <template #append>
              <el-button icon="Search" @click="getEncounterList" />
            </template>
          </el-input>
          <el-select
            v-model="queryParams.departmentId"
            placeholder="请选择科室"
            @change="getEncounterList"
            clearable
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </div>
        <div style="display: flex; gap: 10px; margin-top: 15px">
          <el-date-picker
            v-model="encounterDate"
            type="daterange"
            range-separator="~"
            value-format="YYYY-MM-DD"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 80%"
            @change="getEncounterList"
          />
          <el-button type="primary" @click="getEncounterList">搜索</el-button>
        </div>
      </div>
      <el-table
        :data="encounterList"
        border
        style="width: 100%"
        @row-click="handleGetDisposalList"
        height="650"
        highlight-current-row
      >
        <el-table-column prop="departmentName" align="center" width="100" label="科室" />
        <el-table-column prop="patientName" align="center" label="姓名" />
        <el-table-column prop="genderEnum_enumText" align="center" width="70" label="性别" />
        <el-table-column prop="encounterDate" align="center" label="就诊日期" />
      </el-table>
      <pagination
        v-show="patientTotal > 0"
        :total="patientTotal"
        v-model:page="queryParams.pageNo"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </div>
    <div style="width: 40%">
      <div style="height: 100px">
        <el-card>
          <el-descriptions title="" :column="4">
            <el-descriptions-item label="姓名：">
              {{ patientInfo.patientName }}
            </el-descriptions-item>
            <el-descriptions-item label="性别：">
              {{ patientInfo.genderEnum_enumText }}
            </el-descriptions-item>
            <el-descriptions-item label="证件号：">
              {{ patientInfo.idCard }}
            </el-descriptions-item>
            <el-descriptions-item label="就诊科室：">
              {{ patientInfo.organizationName }}
            </el-descriptions-item>
            <el-descriptions-item label="就诊日期：">
              {{ patientInfo.encounterDate }}
            </el-descriptions-item>
            <el-descriptions-item label="门诊诊断：">
              {{ patientInfo.conditionName }}
            </el-descriptions-item>
            <el-descriptions-item label="总金额：">
              {{ patientInfo.totalPrice }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </div>

      <el-table
        :data="disposalList"
        border
        style="width: 100%"
        height="650"
        @row-click="handleGetExecuteList"
        highlight-current-row
      >
        <el-table-column prop="itemName" align="center" width="180" label="项目名称" />
        <el-table-column prop="unitPrice" align="center" label="单价" />
        <el-table-column prop="totalPrice" align="center" label="总价" />
        <el-table-column prop="quantity" align="center" label="数量" />
        <el-table-column prop="frequency" align="center" label="次数" />
        <el-table-column prop="chargeStatusEnum_enumText" align="center" label="收费状态" />
        <el-table-column prop="unitCode" align="center" label="单位" />
        <el-table-column label="操作" width="80" align="center" fixed="right">
          <template #default="scope">
            <el-button type="primary" link @click="handleExecute(scope.row)"> 执行 </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div style="width: 34%">
      <el-table :data="executeList" border style="width: 100%" height="750">
        <el-table-column prop="performerName" align="center" width="100" label="执行人" />
        <el-table-column prop="locationName" align="center" label="执行科室" />
        <el-table-column prop="deviceName" align="center" label="耗材名称" />
        <el-table-column prop="quantity" align="center" label="耗材数" />
        <el-table-column prop="occurrenceTime" align="center" label="执行时间" />
        <el-table-column prop="" label="操作" width="80" align="center" fixed="right">
          <template #default="scope">
            <el-button type="primary" link @click="handelCancel(scope.row)"> 取消 </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { getList, getDisposalList, getExecuteList, execute, cancel, init } from './components/api';

const queryParams = ref({
  pageSize: 50,
  pageNum: 1,
});
const patientInfo = ref({});
const patientTotal = ref(0);
const encounterList = ref([]);
const disposalList = ref([]);
const executeList = ref([]);
const encounterDate = ref([]);
const options = ref([]);
const { proxy } = getCurrentInstance();
getEncounterList();
function getEncounterList() {
  if (encounterDate.value) {
    queryParams.value.encounterDateSTime = encounterDate.value[0] + ' 00:00:00';
    queryParams.value.encounterDateETime = encounterDate.value[1] + ' 23:59:59';
  } else {
    queryParams.value.encounterDateSTime = undefined;
    queryParams.value.encounterDateETime = undefined;
  }
  getList(queryParams.value).then((res) => {
    patientTotal.value = res.data.total;
    encounterList.value = res.data.records;
  });
}

initOption();
function initOption() {
  init().then((res) => {
    options.value = res.data.departmentOptions;
  });
}

function handleGetDisposalList(row) {
  getDisposalList(row.encounterId).then((res) => {
    disposalList.value = res.data.outpatientDisposalActivityInfoDtoList;
    patientInfo.value = res.data.outpatientDisposalPatientInfoDto;
  });
}

function handleGetExecuteList(row) {
  getExecuteList({ busNo: row.busNo, activityId: row.activityId, type: row.type }).then((res) => {
    executeList.value = res.data;
  });
}

function handleExecute(row) {
  execute({ itemId: row.id, type: row.type }).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess('操作成功');
    }
  });
}

function handelCancel(row) {
  cancel({ busNo: row.busNo, activityId: row.activityId, type: row.type }).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess('操作成功');
    }
  });
}
</script>

<style lang="scss" scoped>
:deep( .el-card__body) {
  padding: 14px 20px 10px 20px !important;
}

.el-card.is-always-shadow {
  box-shadow: none;
}
</style>