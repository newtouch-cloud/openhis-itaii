<template>
  <el-input
    v-model="queryParams.patientName"
    placeholder="请输入患者名"
    clearable
    style="width: 100%; margin-bottom: 10px"
    @keyup.enter="handleQuery"
  >
    <template #append>
      <el-button icon="Search" @click="handleQuery" />
    </template>
  </el-input>
  <div
    style="justify-content: space-between; display: flex; margin-bottom: 10px"
  >
    <div>
      <el-date-picker
        v-model="queryParams.createTime"
        placeholder="请选择挂号日期"
        type="date"
        size="default"
        placement="bottom"
        @change="handleQuery"
      />
    </div>
    <div>
      <el-button type="primary" @click="handleReceive()">接诊</el-button>
      <el-button type="primary" @click="handleDisabled(scope.row.id)">
        暂离
      </el-button>
    </div>
  </div>
  <el-table
    ref="patientTableRef"
    :data="patient"
    row-key="id"
    @cell-click="clickRow"
    v-loading="loading"
    highlight-current-row
    @selection-change="handleSelectionChange"
  >
    <el-table-column label="序号" type="index" width="50" />
    <el-table-column label="患者" align="center" prop="name">
      <template #default="scope">
        <div
          style="
            display: flex;
            justify-content: flex-start;
            align-items: center;
          "
        >
          <div style="padding-top: 5px">
            <el-avatar
              :size="40"
              src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
            />
          </div>
          <div style="font-size: 12px; text-align: left; padding-left: 20px">
            <div>
              <span style="margin-right: 10px">{{
                scope.row.patientName
              }}</span>
              <span style="margin-right: 10px">年龄：{{ scope.row.age }}</span>
              <span>性别：{{ scope.row.genderEnum_enumText }}</span>
            </div>
            <div>
              <span>挂号时间：{{ "2025-03-19 15:30:30" }}</span>
            </div>
          </div>
        </div>
      </template>
    </el-table-column>
  </el-table>
  <!-- <pagination
    v-show="total > 0"
    :total="total"
    v-model:page="queryParams.pageNum"
    v-model:limit="queryParams.pageSize"
    @pagination="getPatientList"
  /> -->
</template>

<script setup>
import { getList, receiveEncounter } from "./api";

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
});
const patient = ref([]);
const loading = ref(false);
const patientTableRef = ref();
const total = ref(0);
const emits = defineEmits(["cellClick", "toCurrent"]);
const props = defineProps({
  status: {
    type: Number,
    required: true,
  },
});
const encounterId = ref();
getPatientList();
function getPatientList() {
  queryParams.value.statusEnum = props.status;
  loading.value = true;
  console.log(queryParams.value.statusEnum);
  getList(queryParams.value).then((res) => {
    patient.value = res.data.records;
    total.value = res.data.total;
    loading.value = false;
  });
}

function clickRow(row, column) {
  encounterId.value = row.encounterId;
  emits("cellClick", row);
}

/**
 * 接诊并跳到现诊tab页
 */
function handleReceive() {
  receiveEncounter(encounterId.value).then(() => {
    emits("toCurrent");
  });
}

function handleQuery() {
  // 清空表格选中状态
  patientTableRef.value.setCurrentRow(null);
  getPatientList();
}

function handleSelectionChange() {}
</script>

<style scoped>
.custom-date-picker.el-picker__popper {
  width: 285px !important;
}
.el-table__body tr.current-row > td.el-table__cell {
  background-color: #cde5ff;
}
</style>