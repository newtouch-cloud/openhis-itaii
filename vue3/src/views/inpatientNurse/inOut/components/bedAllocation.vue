<!--
 * @Author: sjjh
 * @Date: 2025-04-09 17:14:59
 * @Description:
-->
<template>
  <div class="bedAllocation-container">
    <div class="bedAllocation-search">
      <el-space>
        <!-- <el-input v-model="input" style="width: 240px" placeholder="Please input" /> -->
      </el-space>
      <el-space>
        <el-button @click="query">刷新</el-button>
      </el-space>
    </div>
    <!-- 待入科列表 -->
    <div class="bedAllocation-table">
      <el-table
        :data="allocationData"
        border
        row-key="id"
        style="width: 100%; height: 100%"
        highlight-current-row
      >
        <el-table-column prop="date" label="姓名" width="180" sortable />
        <el-table-column prop="orderTypeName" label="住院号" width="180" />
        <el-table-column prop="address" label="诊断" />
        <el-table-column prop="address" label="性别" />
        <el-table-column prop="address" label="年龄" />
        <el-table-column prop="address" label="入院科室" />
        <el-table-column prop="address" label="费别" width="180" />
        <el-table-column prop="address" label="是否跨科收治" width="180" />
        <!-- 新入院还是其他科室转入 -->
        <el-table-column prop="address" label="入科来源" width="180" />

        <el-table-column fixed="right" label="操作" width="140">
          <template #default="props">
            <el-button link type="primary" size="small" @click="addSigns(props.row)">
              体征录入
            </el-button>
            <el-button link type="primary" size="small" @click="selectBed(props.row)"
              >分床</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
    <TransferInDialog v-model:visible="transferInDialogVisible" />
    <SignEntryDialog v-model:visible="signEntryDialogVisible" />
  </div>
</template>
<script setup >
import { getCurrentInstance, onBeforeMount, onMounted, reactive, ref } from 'vue'
import { TransferInDialog, SignEntryDialog } from './index'
// const { proxy } = getCurrentInstance()
// const emits = defineEmits([])
// const props = defineProps({})

const transferInDialogVisible = ref(false)
const signEntryDialogVisible = ref(false)
const state = reactive({})
onBeforeMount(() => {})
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
.bedAllocation-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  .bedAllocation-search {
    height: 44px;
    display: flex;
    align-items: center;
    flex-direction: row-reverse;
    text-align: right;
    flex: none;
    justify-content: space-between;
  }
  .bedAllocation-table {
    height: 200px;
    flex: auto;
  }
}
</style>
