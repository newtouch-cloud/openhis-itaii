<!--
 * @Author: sjjh
 * @Date: 2025-04-21 14:50:04
 * @Description:
-->
<template>
  <div class="inpatientNurseHome-inOut-container">
    <el-tabs v-model="activeTabName" type="card" class="inOut-tabs">
      <el-tab-pane label="待出院" name="first">
        <WaitForDischarge />
      </el-tab-pane>
      <el-tab-pane label="已出院" name="second">
        <Discharged />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script setup >
import { getCurrentInstance, onBeforeMount, onMounted, reactive, ref } from 'vue'

import { WaitForDischarge, Discharged } from './components/index.ts'
// const { proxy } = getCurrentInstance()
// const emits = defineEmits([])
// const props = defineProps({})
const state = reactive({})
onBeforeMount(() => { })
onMounted(() => { })
defineExpose({ state })

const activeTabName = ref('first')
</script>
<style lang="scss" scoped>
.inpatientNurseHome-inOut-container {
  width: 100%;
  height: 100%;
  display: flex;
  padding: 0 8px 8px;

  :deep(.inOut-tabs) {
    height: 100%;
    width: 100%;

    .el-tabs__header {
      margin: 0;
    }

    .el-tab-pane {
      height: 100%;
    }

    .el-tabs__content {
      height: calc(100% - 40px);
    }
  }
}
</style>
