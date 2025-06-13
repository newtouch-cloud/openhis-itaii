<template>
  <div style="display: flex; justify-content: space-between; height: 90vh">
    <div style="width: 15%; height: 100%; border: 1px solid #eee; border-right: 0">
      <div style="padding: 10px; border: 1px solid #eee; height: 50px; border-right: 0">
        <span>现诊患者</span>
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
        <el-scrollbar height="700px">
          <div
            v-for="(item, index) in patientList"
            :class="item.active ? 'patient-card actived' : 'patient-card'"
            :key="item.id"
            @click="handleCardClick(item, index)"
          >
            <div class="main-info-container">
              <div class="bed-container">
                <div class="bed">
                  <el-text truncated :tclass="bedfont" width="auto">初诊</el-text>
                </div>
                <div class="bed_new" />
              </div>
            </div>
            <div class="doctor-parent-line" />
            <div class="personal-info-container">
              <div class="name-container">
                <!-- 患者姓名 -->
                <div class="name" style="max-width: 70px">
                  <el-text tclass="name" width="auto">{{ item.patientName || '未知' }}</el-text>
                </div>
                <!-- 患者性别/年龄 -->
                <div class="age">
                  {{ item.genderEnum_enumText }}/{{ item.age }}/{{ item.typeCode_dictText }}
                </div>
              </div>
            </div>
          </div>
        </el-scrollbar>
      </div>
    </div>
    <div style="width: 85%; border: 1px solid #eee; position: relative">
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
                  patientInfo.typeCode_dictText
                : '-'
            }}
          </el-descriptions-item>
          <el-descriptions-item label="挂号时间" width="150">
            {{ Object.keys(patientInfo).length !== 0 ? formatDate(patientInfo.registerTime) : '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="医生" width="150">{{ userStore.name }}</el-descriptions-item>
          <el-descriptions-item label="" width="150">
            <!-- {{ '' }} -->
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <div style="padding: 10px">
        <prescriptionlist :patientInfo="patientInfo" ref="prescriptionRef" />
        <div class="overlay" v-if="disabled"></div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { getList } from './component/api.js';
import { formatDate } from '@/utils/index';
import useUserStore from '@/store/modules/user';
import { nextTick } from 'vue';
import Prescriptionlist from './component/prescriptionlist.vue';
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
const queryParams = ref({});
const currentEncounterId = ref('');
const emits = defineEmits(['click']);
const patientList = ref([]);
const patientInfo = ref({});
const prescriptionRef = ref();
const waitCount = ref(0);
const { proxy } = getCurrentInstance();
const disabled = computed(() => {
  return Object.keys(patientInfo.value).length === 0;
});

getPatientList();
// 获取现诊患者列表
function getPatientList() {
  getList(queryParams.value).then((res) => {
    patientList.value = res.data.records.map((item) => {
      return {
        ...item,
        active: currentEncounterId.value ? item.encounterId == currentEncounterId.value : false,
      };
    });
  });
}

function handleQuery() {}

function handleCardClick(item, index) {
  currentEncounterId.value = '';
  // if (item.active) {
  //   patientList.value[index].active = false;
  //   return;
  // }
  patientList.value.forEach((patient) => {
    patient.active = patient.encounterId === item.encounterId;
  });
  patientInfo.value = item;
  nextTick(() => {
    prescriptionRef.value.getListInfo();
  });
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
    height: 24px;
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
    margin: 10px 0;
    padding: 0 16px;

    .name-container {
      display: flex;
      align-items: center;
      height: 24px;

      .name {
        color: #333;
        font-size: 16px;
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
::v-deep .el-tabs__header {
  padding: 0;
  position: relative;
  margin: 0 0 5px !important;
}

::v-deep .el-drawer__header {
  margin-bottom: 15px !important;
}
::v-deep .el-drawer__body {
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
