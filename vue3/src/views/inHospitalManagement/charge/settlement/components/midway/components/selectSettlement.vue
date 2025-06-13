<template>
  <el-drawer ref="elDrawerComRef" title="选择中途结算费用" :style="{
    maxWidth: '580px',
    minWidth: '580px',
  }" v-model="showDrawer" :showFoot="false" :showFootLine="false" body-class="selectSettlement-container"
    @open="initData">
    <el-space class="operate-line" alignment="center" :size="16">
      <div>收费时间</div>
      <el-date-picker v-model="searchDateRange" type="daterange" range-separator="至" start-placeholder="开始时间"
        end-placeholder="结束时间" value-format="YYYY-MM-DD" />
      <el-button style="margin-right: 0px" @click="search">查找</el-button>
    </el-space>
    <div class="table-container">
      <el-table :data="selectSettlementData" style="width: 100%" height="100%" align="center" show-overflow-tooltip
        ref="selectSettlementDataRef">
        <el-table-column type="selection" width="38" fixed />
        <el-table-column prop="chargingDate" label="收费日期" width="108" align="center"
          :formatter="(row: any) => { return row.chargingDate && dayjs(row.chargingDate).format('YYYY/MM/DD') }" />
        <el-table-column prop="costsSum" label="实收总金额" width="100" align="center" />
        <el-table-column class-name="nameRight" prop="amountSum" label="应付总金额" width="100" align="center" />
        <el-table-column class-name="nameRight" prop="settleAmountSum" label="已结算金额" width="100" align="center" />
        <el-table-column prop="unSettleAmountSum" label="未结算金额" width="100" align="center" />
      </el-table>
    </div>
    >
    <template #tool>
      <el-button type="primary" style="margin: 8px" @click="selectOk">确定</el-button>
    </template>
  </el-drawer>
</template>
<script setup >
import {
  getCurrentInstance,
  onBeforeMount,
  onMounted,
  reactive,
  ref,
} from 'vue';
// const { proxy } = getCurrentInstance();
import dayjs from 'dayjs';
const emits = defineEmits(['selectOk']);
const showDrawer = defineModel('showDrawer', { type: Boolean, default: false });
const props = defineProps<{
  visitCode: string;
}>();
const state = reactive({});
// onBeforeMount(() => {

// })
// onMounted(() => {

// })

const searchDateRange = ref([
  dayjs(new Date()).format('YYYY-MM-DD'),
  dayjs(new Date()).format('YYYY-MM-DD'),
]);
const search = () => {
  if (searchDateRange.value) {
  }
};
const selectSettlementData = ref<Array<any>>([]);
const selectSettlementDataRef = ref();
const initData = () => {
  search();
};
const selectOk = () => {
  emits('selectOk', selectSettlementDataRef.value.getSelectionRows());
};
defineExpose({ state });
</script>
<style lang="scss" scoped>
.selectSettlement-container {
  height: 100%;
  display: flex;
  flex-direction: column;

  .operate-line {
    padding-bottom: 16px;
    flex: none;
  }

  .table-container {
    width: 100%;
    height: calc(100% - 50px);
    padding-bottom: 16px;
    flex: auto;
  }
}
</style>
