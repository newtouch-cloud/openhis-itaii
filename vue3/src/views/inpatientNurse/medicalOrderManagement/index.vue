<!--
 * @Author: sjjh
 * @Date: 2025-04-09 17:14:59
 * @Description:
-->
<template>
  <el-tabs type="border-card" class="medicalOrderManagement-container">
    <el-tab-pane label="医嘱校对">
      <div class="check-wrapper">
        <!-- 患者列表 -->
        <CheckPatientList />
        <!-- main -->
        <CheckMedicalOrderManagement />
      </div>
    </el-tab-pane>
    <el-tab-pane label="医嘱执行">
      <div class="execute-wrapper">
        <!-- 患者列表 -->
        <ExecutePatientList />
        <!-- main -->
        <ExecuteMedicalOrderManagement />
      </div>
    </el-tab-pane>
    <el-tab-pane label="查询与打印">查询与打印</el-tab-pane>
  </el-tabs>
</template>
<script setup >
import { onBeforeMount, onMounted, reactive, ref } from 'vue'
import CheckPatientList from './components/check/patientList.vue'
import CheckMedicalOrderManagement from './components/check/MedicalOrderManagement.vue'
import ExecutePatientList from './components/execute/patientList.vue'
import ExecuteMedicalOrderManagement from './components/execute/MedicalOrderManagement.vue'
// const { proxy } = getCurrentInstance()
// const emits = defineEmits([])
// const props = defineProps({})

const transferInDialogVisible = ref(false)
const signEntryDialogVisible = ref(false)
const state = reactive({})
onBeforeMount(() => { })
onMounted(() => {
  query()
})
defineExpose({ state })
const allocationData = ref([{}, {}])
const query = () => {
  // 查询列表
}

const addSigns = (row: any) => {
  // TODO 新增入院体征
  signEntryDialogVisible.value = true
}

const selectBed = (row: any) => {
  // TODO 选床 入科
  transferInDialogVisible.value = true
}
</script>
<style lang="scss" scoped>
.medicalOrderManagement-container {

  height: 100%;
  width: 100%;

  :deep(.el-tabs__content) {
    padding: 0;
  }

  :deep(.el-tab-pane) {
    height: 100%;
    width: 100%;
  }

  .check-wrapper {
    height: 100%;
    width: 100%;
    display: flex;
  }

  .execute-wrapper {
    height: 100%;
    width: 100%;
    display: flex;
  }

  .patientList-container {
    flex: none;
  }
}
</style>
