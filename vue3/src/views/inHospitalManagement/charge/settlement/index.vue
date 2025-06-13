<!--
 * @Author: sjjh
 * @Date: 2025-04-09 14:50:04
 * @Description:
-->
<template>
  <div class="settlement-container">
    <el-tabs v-model="activeTabName" type="card" class="inOut-tabs">
      <el-tab-pane label="住院结算" name="first">
        <SettlementHome />
      </el-tab-pane>
      <el-tab-pane label="结算取消" name="second">
        <SettlementCancel />
      </el-tab-pane>
      <el-tab-pane label="中途结算" name="third">
        <SettlementMidway />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script setup >
import { getCurrentInstance, onBeforeMount, onMounted, reactive, ref } from 'vue'

import SettlementHome from './components/home/index.vue'
import SettlementCancel from './components/cancel/index.vue'
import SettlementMidway from './components/midway/index.vue'
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
.settlement-container {
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
