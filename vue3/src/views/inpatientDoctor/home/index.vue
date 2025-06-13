<!--
 * @Author: sjjh
 * @Date: 2025-04-07 11:49:37
 * @Description:
-->
<template>
  <div class="inpatientDoctor-home-container">
    <el-container>
      <!-- 患者列表 -->
      <el-aside minWidth="83px" :width="`${patientAside ? '254px' : '83px'}`">
        <PatientList v-model:expand="patientAside"
      /></el-aside>
      <!--  main-->
      <el-container class="inpatientDoctor-home-main">
        <el-header height="auto"><inPatientBarDoctorFold /></el-header>
        <el-main>
          <el-tabs v-model="activeTabName" type="card" class="patient-tabs">
            <el-tab-pane label="临床医嘱" name="first"><Advice /></el-tab-pane>
            <el-tab-pane label="诊断录入" name="second"><Diagnose /></el-tab-pane>
            <el-tab-pane label="住院病历" name="third">Role</el-tab-pane>
            <el-tab-pane label="医技报告" name="fourth">Task</el-tab-pane>
          </el-tabs>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<script setup >
import { getCurrentInstance, onBeforeMount, onMounted, reactive, ref } from 'vue'
// const { proxy } = getCurrentInstance()
// const emits = defineEmits([])
// const props = defineProps({})
import inPatientBarDoctorFold from '@/components/patientbar/inPatientBarDoctorFold.vue'
import { PatientList,Advice ,Diagnose} from './index.js'
const state = reactive({})
onBeforeMount(() => {})
onMounted(() => {})
defineExpose({ state })
const activeTabName = ref('first')
const patientAside = ref(false)
</script>
<style lang="scss" scoped>
.inpatientDoctor-home-container {
  height: 100%;
  height: calc(100vh - 84px);
  .el-container {
    height: 100%;
  }
  :deep(.el-aside ){
    padding: 0;
  }
  .inpatientDoctor-home-main {
    background-color: #ffffff;
    :deep(.el-header) {
      padding: 0px;
      margin-bottom: 0px;
    }
    .el-main {
      padding: 0px 8px;
    }
    :deep(.patient-tabs) {
      height: 100%;
      .el-tabs__header {
        margin: 0;
      }
      .el-tabs__content{
        height: calc(100% - 40px);
      }
      .el-tab-pane {
        height: 100%;
      }
    }
  }
}
</style>
