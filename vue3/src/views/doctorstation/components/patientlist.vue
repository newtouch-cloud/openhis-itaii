<template>
  <div style="display: flex; align-items: center; gap: 10px; margin-bottom: 10px">
    <el-input
      v-model="queryParams.patientName"
      placeholder="请输入患者姓名"
      clearable
      style="flex: 1.4"
      @keyup.enter="handleQuery"
    >
      <template #append>
        <el-button icon="Search" @click="handleQuery" />
      </template>
    </el-input>

    <el-date-picker
      v-model="date"
      placeholder="请选择挂号日期"
      type="date"
      size="default"
      placement="bottom"
      style="flex: 1.1"
      @change="handleDateQuery"
      :clearable="false"
      value-format="YYYY-MM-DD"
    />
    <el-radio-group
      v-model="queryParams.statusEnum"
      @change="getPatientList"
      style="flex-shrink: 0"
    >
      <el-radio-button :label="1">待诊</el-radio-button>
      <el-radio-button :label="3">暂离</el-radio-button>
      <el-radio-button :label="4">完诊</el-radio-button>
    </el-radio-group>
  </div>
  <div style="justify-content: space-between; display: flex; margin-bottom: 10px">
    <div></div>
  </div>
  <el-table
    max-height="750"
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
        <div style="display: flex; justify-content: flex-start; align-items: center">
          <div style="padding-top: 5px">
            <el-avatar
              :size="40"
              src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
            />
          </div>
          <div style="font-size: 12px; text-align: left; padding-left: 20px">
            <div>
              <span style="margin-right: 10px">{{ scope.row.patientName }}</span>
              <span style="margin-right: 10px">年龄：{{ scope.row.age }}</span>
              <span>性别：{{ scope.row.genderEnum_enumText }}</span>
            </div>
            <div>
              <span>挂号时间：{{ formatDate(scope.row.registerTime) }}</span>
            </div>
          </div>
        </div>
      </template>
    </el-table-column>
    <el-table-column label="操作" align="center" prop="name" width="100">
      <template #default="scope">
        <el-button type="primary" link @click="handleReceive(scope.row)"> 接诊 </el-button>
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
import { getList, receiveEncounter, leaveEncounter, completeEncounter } from './api';
import { formatDate, formatDateStr } from '@/utils/index';

const date = ref(formatDate(new Date()));
const queryParams = ref({
  pageNum: 1,
  pageSize: 50,
  statusEnum: 1,
  registerTimeSTime: formatDateStr(new Date(), 'YYYY-MM-DD') + ' 00:00:00',
  registerTimeETime: formatDateStr(new Date(), 'YYYY-MM-DD') + ' 23:59:59',
});
const patient = ref([]);
const loading = ref(false);
const patientTableRef = ref();
const total = ref(0);
const emits = defineEmits(['cellClick', 'toCurrent']);
const props = defineProps({
  status: {
    type: Number,
    required: true,
  },
});
const { proxy } = getCurrentInstance();
const encounterId = ref();
onMounted(() => {
  getPatientList();
});

function getPatientList() {
  // queryParams.value.statusEnum = props.status;
  loading.value = true;
  // queryParams.value.registerTimeSTime = formatDateStr(new Date(), 'YYYY-MM-DD') + ' 00:00:00';
  // queryParams.value.registerTimeETime = formatDateStr(new Date(), 'YYYY-MM-DD') + ' 23:59:59';
  getList(queryParams.value).then((res) => {
    patient.value = res.data.records;
    total.value = res.data.total;
    loading.value = false;
  });
}

function clickRow(row) {
  encounterId.value = row.encounterId;
  emits('cellClick', row);
}

/**
 * 接诊并跳到现诊tab页
 */
function handleReceive(row) {
  // if (encounterId.value == undefined) {
  //   proxy.$modal.msgError('请选择患者');
  //   return;
  // }
  receiveEncounter(row.encounterId).then(() => {
    emits('toCurrent', row);
  });
}

/**
 * 暂离
 */
function handleDisabled() {
  if (encounterId.value == undefined) {
    proxy.$modal.msgError('请选择患者');
    return;
  }
  proxy.$modal
    .confirm('是否暂离该患者？')
    .then(() => {
      proxy.$modal.loading();
      leaveEncounter(encounterId.value).then(() => {
        proxy.$modal.closeLoading();
        proxy.$modal.msgSuccess('暂离成功');
        getPatientList();
      });
    })
    .catch(() => {
      proxy.$modal.closeLoading();
    });
}

/**
 * 完诊
 */
function handleComplete() {
  if (encounterId.value == undefined) {
    proxy.$modal.msgError('请选择患者');
    return;
  }
  proxy.$modal.confirm('是否完成该患者问诊？').then(() => {
    proxy.$modal.loading();
    completeEncounter(encounterId.value).then(() => {
      proxy.$modal.closeLoading();
      proxy.$modal.msgSuccess('完成问诊成功');
      getPatientList();
    });
  });
}

function handleQuery() {
  // 清空表格选中状态
  patientTableRef.value.setCurrentRow(null);
  getPatientList();
}

function handleDateQuery(value) {
  queryParams.value.registerTimeSTime = value + ' 00:00:00';
  queryParams.value.registerTimeETime = value + ' 23:59:59';
  handleQuery();
}

function handleSelectionChange() {}

// 添加刷新方法
function refreshList() {
  getPatientList();
}

// 暴露 refreshList 方法
defineExpose({
  refreshList,
});
</script>

<style scoped>
.custom-date-picker.el-picker__popper {
  width: 285px !important;
}
</style>