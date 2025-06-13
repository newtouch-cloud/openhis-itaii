<!--
 * @Author: hezhibo@bjgoodwill.com
 * @Date: 2025-02-19 11:23:31
 * @Description:
-->
<template>
  <el-drawer ref="elDrawerComRef" title="患者列表" :style="{
    maxWidth: '477px',
    minWidth: '477px',
  }" v-model="showDrawer" :showFoot="false" :showFootLine="false" body-class="patientList-container"
    @after-enter="initData">
    <div class="operate">
      <el-space class="operate-line" direction="vertical" alignment="flex-start">
        <el-space class="">
          <span>病区</span>
          <div>
            <el-select v-model="searchForm.deptNurseCode" style="width: 168px" placeholder="请选择信息">
            </el-select>
          </div>

          <el-checkbox v-model="searchForm.rcptPayFlag" label="未缴费" />
        </el-space>
        <el-space class="operate-line">
          <span>患者</span>

          <el-input v-model="searchForm.inpatientCode" style="width: 168px" placeholder="住院号" />
          <el-input v-model="searchForm.patName" style="width: 139px" placeholder="姓名" />
          <el-button style="margin-right: 0px; margin-left: 8px" @click="search">查找</el-button>
        </el-space>
      </el-space>
    </div>
    <div class="table-container">
      <el-table :data="patientListData" height="100%" show-overflow-tooltip :style="{ height: '100%' }">
        <el-table-column prop="deptNurseName" label="病区" width="127" show-overflow-tooltip />
        <el-table-column prop="bedName" label="床号" width="77" />
        <el-table-column prop="name" label="姓名" width="78" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" min-width="108" show-overflow-tooltip :formatter="statusFormatter">
        </el-table-column>
        <el-table-column prop="type" label="操作" width="55">
          <template #default="{ row }">
            <el-button link type="warning" style="margin-right: 0px" @click="settle(row)">结算</el-button>
            <el-button link type="warning" style="margin-right: 0px" @click="pay(row)">缴费</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </el-drawer>
</template>
<script setup >
import {
  getCurrentInstance,
  onBeforeMount,
  onMounted,
  reactive,
  ref,
  h,
} from 'vue';
// const { proxy } = getCurrentInstance();

const emits = defineEmits(['settling', 'paying']);
const props = defineProps({});
const state = reactive({});
const showDrawer = defineModel('showDrawer', { type: Boolean, default: false });
// onBeforeMount(() => {

// })

const searchForm = reactive({
  deptNurseCode: '',
  rcptPayFlag: false,
  inpatientCode: '',
  patName: '',
});
const patientListData = ref<Array<any>>([]);
const search = () => {

};
const hipWorkGroupSelTabelRef = ref();
onMounted(() => {
  initData();
});
const initData = () => {
};

const statusFormatter = (row: any) => {
  switch (row.status) {
    case '2':
      return h(
        'div',
        {
          style: { color: '#5585E3' },
        },
        '待结算'
      );
    case '3':
      return h(
        'div',
        {
          style: { color: '#945D3D' },
        },
        '待缴费'
      );
    case '4':
      return h('div', {}, '已支付');
    default:
      break;
  }
};
const settle = (row: any) => {
  emits('settling', row);
};
const pay = (row: any) => {
  emits('paying', row);
};
defineExpose({ state });
</script>
<style lang="scss">
.patientList-container {
  height: 100%;
  display: flex;
  flex-direction: column;

  .operate {
    flex: none;
    padding-left: 12px;
  }

  .table-container {
    padding: 8px 0;
    height: 100px;
    flex: auto;
  }
}
</style>
