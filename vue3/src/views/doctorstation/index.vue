<template>
  <div style="display: flex; justify-content: space-between; height: 90vh">
    <div style="width: 15%; height: 100%; border: 1px solid #eee; border-right: 0">
      <div style="padding: 10px; border: 1px solid #eee; height: 50px; border-right: 0">
        <span>现诊患者</span>
        <el-badge
          :value="waitCount > 0 ? waitCount : ''"
          :max="10"
          style="float: right; color: #409eff; cursor: pointer; margin-right: 10px"
        >
          <span @click="openDrawer"> 患者队列 </span>
        </el-badge>
      </div>
      <div style="width: 100%; padding: 10px">
        <el-input
          v-model="queryParams.searchKey"
          placeholder="请输入患者名"
          clearable
          style="width: 100%; margin-bottom: 10px"
          @keyup.enter="getPatientList"
        >
          <template #append>
            <el-button icon="Search" @click="getPatientList" />
          </template>
        </el-input>
        <el-date-picker
          v-model="registerTime"
          @change="handleTimeChange"
          type="date"
          style="width: 100%; margin-bottom: 10px"
          placeholder="挂号时间"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
        />
        <el-scrollbar height="700px">
          <div
            v-for="(item, index) in patientList"
            :class="item.active ? 'patient-card actived' : 'patient-card'"
            :key="item.id"
            @click="handleCardClick(item, index)"
          >
            <div class="main-info-container">
              <!-- <el-avatar
                :size="30"
                src="\src\assets\images\man.png"
                fit="scale-down"
              /> -->
              <div class="name-container">
                <!-- 患者姓名 -->
                <div class="name" style="max-width: 90px">
                  <el-text tclass="name" width="auto">{{ item.patientName || '未知' }}</el-text>
                </div>
              </div>
              <div class="name-container">
                <!-- 患者性别/年龄 -->
                <div class="age">
                  <el-text tclass="name" width="auto">
                    {{ item.genderEnum_enumText }}/{{ item.age }}/{{ item.typeCode_dictText }}
                  </el-text>
                </div>
              </div>
            </div>
            <div class="doctor-parent-line" />
            <div class="personal-info-container">
              <div class="name-container">
                <div class="name">
                  <el-text tclass="name" width="auto">挂号时间：</el-text>
                  <el-text tclass="name" width="auto">
                    {{ item.registerTime ? formatDate(item.registerTime) : '-' }}
                  </el-text>
                </div>
              </div>
            </div>
          </div>
        </el-scrollbar>
      </div>
    </div>
    <div class="disabled-wrapper" style="width: 85%; border: 1px solid #eee; position: relative">
      <div style="padding: 10px; border: 1px solid #eee; height: 50px; border-left: 0">
        <el-descriptions :column="4">
          <el-descriptions-item label="患者信息:" width="150">
            {{
              Object.keys(patientInfo).length !== 0
                ? patientInfo.patientName +
                  ' / ' +
                  patientInfo.age +
                  ' / ' +
                  patientInfo.genderEnum_enumText +
                  ' / ' +
                  patientInfo.contractName
                : '-'
            }}
          </el-descriptions-item>
          <el-descriptions-item label="挂号时间：" width="150">
            {{ Object.keys(patientInfo).length !== 0 ? formatDate(patientInfo.registerTime) : '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="医生：" width="150">{{
            userStore.name
          }}</el-descriptions-item>
          <el-descriptions-item label="" width="150">
            <el-button type="primary" plain @click.stop="handleFinish(patientInfo.encounterId)">
              完诊
            </el-button>
            <el-button type="primary" plain @click.stop="handleLeave(patientInfo.encounterId)">
              暂离
            </el-button>
            <el-button type="primary" plain @click.stop="handleRefund(patientInfo.encounterId)">
              退费
            </el-button>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <div style="padding: 10px">
        <el-tabs
          type="card"
          style="width: 100%; height: 100%"
          v-loading="loading"
          v-model="activeTab"
          @tab-change="handleClick(activeTab)"
        >
          <el-tab-pane label="病历" name="emr">
            <Emr
              :patientInfo="patientInfo"
              ref="emrRef"
              @save="
                (value) => {
                  saveStatus = value;
                }
              "
            />
          </el-tab-pane>
          <el-tab-pane label="诊断" name="diagnosis">
            <Diagnosis
              :patientInfo="patientInfo"
              ref="diagnosisRef"
              @diagnosisSave="
                (value) => {
                  saveStatus = value;
                }
              "
            />
          </el-tab-pane>
          <el-tab-pane label="医嘱" name="prescription">
            <prescriptionlist
              :patientInfo="patientInfo"
              ref="prescriptionRef"
              :activeTab="activeTab"
            />
          </el-tab-pane>
          <el-tab-pane label="中医" name="tcm">
            <tcmAdvice :patientInfo="patientInfo" ref="tcmRef" />
          </el-tab-pane>
          <el-tab-pane label="电子处方" name="eprescription">
            <eprescriptionlist :patientInfo="patientInfo" ref="eprescriptionRef" />
          </el-tab-pane>
        </el-tabs>
        <div class="overlay" v-if="disabled"></div>
      </div>
    </div>
    <el-drawer v-model="drawer" title="患者队列" direction="ltr" @open="handleOpen">
      <Patientlist ref="patientDrawerRef" @toCurrent="handleReceive" />
    </el-drawer>
    <RefundListDialog
      :open="openRefundListDialog"
      :encounterId="currentEncounterId"
      @close="openRefundListDialog = false"
    />
  </div>
</template>
<script setup>
import Emr from './components/emr.vue';
import { getList, leaveEncounter, completeEncounter } from './components/api.js';
import prescriptionlist from './components/prescriptionlist.vue';
import RefundListDialog from './components/refundListDialog.vue';
import Patientlist from './components/patientlist.vue';
import Diagnosis from './components/diagnosis.vue';
import eprescriptionlist from './components/eprescriptionlist.vue';
import tcmAdvice from './components/tcm/tcmAdvice.vue';
import { formatDate, formatDateStr } from '@/utils/index';
import useUserStore from '@/store/modules/user';
import { nextTick } from 'vue';
import { onBeforeRouteLeave } from 'vue-router';

// // 监听路由离开事件
// onBeforeRouteLeave((to, from, next) => {
//   // 弹出确认框
//   const confirmLeave = window.confirm('确定要离开吗？未保存的数据可能丢失！');
//   if (confirmLeave) {
//     next(); // 允许离开
//   } else {
//     next(false); // 取消离开
//   }
// });
defineOptions({
  name: 'PatientParentCard',
});

const userStore = useUserStore();
const bedfont = 'bed-font';
const queryParams = ref({
  registerTimeSTime: formatDateStr(new Date(), 'YYYY-MM-DD') + ' 00:00:00',
  registerTimeETime: formatDateStr(new Date(), 'YYYY-MM-DD') + ' 23:59:59',
});
const drawer = ref(false);
const openRefundListDialog = ref(false);
const saveStatus = ref(false);
const currentEncounterId = ref('');
const emits = defineEmits(['click']);
const activeTab = ref('emr');
const patientList = ref([]);
const patientInfo = ref({});
const registerTime = ref(formatDate(new Date()));
const patientDrawerRef = ref();
const prescriptionRef = ref();
const tcmRef = ref();
const emrRef = ref();
const diagnosisRef = ref();
const waitCount = ref(0);
const loading = ref(false);
const { proxy } = getCurrentInstance();
const disabled = computed(() => {
  return Object.keys(patientInfo.value).length === 0;
});
const eprescriptionRef = ref();
onMounted(() => {
  getWaitPatient();
});

getPatientList();
// 获取现诊患者列表
function getPatientList() {
  queryParams.value.statusEnum = 2;
  getList(queryParams.value).then((res) => {
    patientList.value = res.data.records.map((item) => {
      return {
        ...item,
        active: currentEncounterId.value ? item.encounterId == currentEncounterId.value : false,
      };
    });
  });
}

function getWaitPatient() {
  queryParams.value.registerTimeSTime = formatDateStr(new Date(), 'YYYY-MM-DD') + ' 00:00:00';
  queryParams.value.registerTimeETime = formatDateStr(new Date(), 'YYYY-MM-DD') + ' 23:59:59';
  queryParams.value.statusEnum = 1;
  getList(queryParams.value).then((res) => {
    waitCount.value = res.data.total;
  });
}

function handleClick(tab) {
  switch (tab) {
    case 'emr':
      break;
    case 'diagnosis':
      diagnosisRef.value.getDetail(patientInfo.value.encounterId);
      break;
    case 'prescription':
      prescriptionRef.value.getDiagnosisInfo();
    case 'eprescription':
      eprescriptionRef.value.getList();
      break;
  }
  // if (tab != 'emr') {
  //   if (!saveStatus.value) {
  //     emrRef.value.addEmr();
  //   }
  // }
  // if (tab != 'diagnosis') {
  //   if (!saveStatus.value) {
  //     let valid= diagnosisRef.value.handleSaveDiagnosis();
  //     console.log(valid);

  //   }
  // }
}

function handleQuery() {}

function handleRefund(encounterId) {
  currentEncounterId.value = encounterId;
  openRefundListDialog.value = true;
}

function handleOpen() {
  patientDrawerRef.value.refreshList();
}

function handleCardClick(item, index) {
  currentEncounterId.value = '';
  // if (item.active) {
  //   patientList.value[index].active = false;
  //   return;
  // }
  loading.value = true;
  patientList.value.forEach((patient) => {
    patient.active = patient.encounterId === item.encounterId;
  });
  patientInfo.value = item;
  activeTab.value = 'emr';
  nextTick(() => {
    prescriptionRef.value.getListInfo();
    tcmRef.value.getListInfo();
    diagnosisRef.value.getList();
    eprescriptionRef.value.getList();
    emrRef.value.getDetail(item.encounterId);
    setTimeout(() => {
      loading.value = false;
    }, 200);
  });
}

function handleLeave(encounterId) {
  leaveEncounter(encounterId).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess('操作成功');
      patientInfo.value = {};
      getPatientList();
    }
  });
}

function handleFinish(encounterId) {
  completeEncounter(encounterId).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess('操作成功');
      patientInfo.value = {};
      getPatientList();
    }
  });
}

function handleTimeChange(value) {
  queryParams.value.registerTimeSTime = value + ' 00:00:00';
  queryParams.value.registerTimeETime = value + ' 23:59:59';
  getPatientList();
}

// 接诊回调
function handleReceive(row) {
  // patientInfo.value = row;
  handleCardClick(row);
  currentEncounterId.value = row.encounterId;
  drawer.value = false;
  getPatientList();
  getWaitPatient();
}

function openDrawer() {
  drawer.value = true;
}
</script>

<style lang="scss" scoped>
.patient-card {
  width: 100%;
  overflow: hidden;
  background-color: #fff;
  border: 1px solid;
  border-color: #eee;
  border-radius: 4px;
  box-shadow: 0 2px 2px 0 rgba(57.55, 69.04, 86.28, 20%);
  margin-bottom: 10px;
  cursor: pointer;

  &.actived {
    background-color: rgb(7, 155, 140, 5%);
    border-color: var(--el-color-primary);
  }

  .cross-dept {
    height: 24px;
    padding: 0 16px;
    color: #fff;
    font-size: 14px;
    line-height: 24px;
    background-color: #256d95;
  }

  .main-info-container {
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: 32px;
    margin: 7px 0;
    padding: 0 16px;

    .bed-container {
      display: flex;
      flex: 1;
      align-items: center;
      min-width: 0;

      .bed {
        flex-grow: 0;
        flex-shrink: 1;
        min-width: 0;

        :deep(.bed-font) {
          color: #333;
          font-weight: 600;
          font-size: 16px;
        }
      }

      .bed_new {
        flex-shrink: 0;
        width: 10px;
        height: 10px;
        margin-left: 4px;
        background: #29af6f;
        border-radius: 50%;
      }
    }

    .indepatient-code-container {
      display: flex;
      flex-shrink: 0;
      align-items: center;
      padding-left: 6px;
      color: #666;
      font-size: 14px;

      .sign {
        width: 24px;
        height: 24px;
        color: white;
        line-height: 24px;
        text-align: center;
        border-radius: 50%;
        user-select: none;
      }
    }
  }

  .doctor-parent-line {
    margin: 0 16px;
    border-bottom: 1px dashed #ddd;
  }

  .personal-info-container {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin: 8px 0;
    padding: 0 16px;

    .name-container {
      display: flex;
      align-items: center;
      height: 18px;

      .name {
        color: #333;
        font-size: 14px;
      }

      .age {
        margin-left: 10px;
        color: #666;
        font-size: 14px;
      }
    }

    .change-department {
      width: 58px;
      height: 24px;
      color: #5585e3;
      font-size: 14px;
      line-height: 24px;
      text-align: center;
      background: #e6edfb;
      border-radius: 4px;
    }
  }

  .dept {
    margin-bottom: 4px;
    padding: 0 16px;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .doctor {
      display: flex;
      align-items: center;
      height: 32px;
      line-height: 32px;

      .doctor_name {
        display: flex;
        align-items: center;
        margin-left: 4px;
        color: #333;
      }
    }

    .deptNurseName {
      display: flex;
      align-items: center;
      height: 32px;
      color: #256d95;
      line-height: 32px;
    }
  }
}
:deep(.el-tabs__header) {
  padding: 0;
  position: relative;
  margin: 0 0 5px !important;
}

:deep(.el-drawer__header) {
  margin-bottom: 15px !important;
}
:deep(.el-drawer__body) {
  padding: 10px !important;
}
.el-badge {
  --el-badge-padding: 6px;
}
.disabled-wrapper .overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 999; /* 确保覆盖在内容上方 */
  cursor: not-allowed;
}
</style>
