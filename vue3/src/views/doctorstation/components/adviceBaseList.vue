<template>
  <div @keyup="handleKeyDown" tabindex="0" ref="tableWrapper">
    <el-table
      ref="adviceBaseRef"
      height="400"
      :data="adviceBaseList"
      highlight-current-row
      @current-change="handleCurrentChange"
      row-key="patientId"
      @cell-click="clickRow"
    >
      <el-table-column label="名称" align="center" prop="adviceName" />
      <el-table-column label="类型" align="center" prop="activityType_enumText" />
      <el-table-column label="包装单位" align="center" prop="unitCode_dictText" />
      <el-table-column label="最小单位" align="center" prop="minUnitCode_dictText" />
      <el-table-column label="规格" align="center" prop="volume" />
      <el-table-column label="用法" align="center" prop="methodCode_dictText" />
      <el-table-column label="库存数量" align="center">
        <template #default="scope">{{ handleQuantity(scope.row) }}</template>
      </el-table-column>
      <el-table-column label="频次" align="center" prop="rateCode_dictText" />
      <!-- <el-table-column label="单次剂量" align="center" prop="dose" /> -->
      <!-- <el-table-column label="剂量单位" align="center" prop="doseUnitCode_dictText" /> -->
      <el-table-column label="注射药品" align="center" prop="injectFlag_enumText" />
      <el-table-column label="皮试" align="center" prop="skinTestFlag_enumText" />
    </el-table>
  </div>
</template>
      
<script setup>
import { nextTick } from 'vue';
import { getAdviceBaseInfo } from './api';
import { throttle } from 'lodash-es';

const props = defineProps({
  adviceQueryParams: {
    type: Object,
    default: '',
  },
  patientInfo: {
    type: Object,
    required: true,
  },
});
const emit = defineEmits(['selectAdviceBase']);
const total = ref(0);
const adviceBaseRef = ref();
const tableWrapper = ref();
const currentIndex = ref(0); // 当前选中行索引
const currentSelectRow = ref({});
const queryParams = ref({
  pageSize: 100,
  pageNum: 1,
});
const adviceBaseList = ref([]);
// 节流函数
const throttledGetList = throttle(
  () => {
    getList();
  },
  300,
  { leading: true, trailing: true }
);
watch(
  () => props.adviceQueryParams,
  (newValue) => {
    queryParams.value.searchKey = newValue.searchKey;
    queryParams.value.adviceType = newValue.adviceType;
    throttledGetList();
  },
  { deep: true }
);

getList();
function getList() {
  queryParams.value.organizationId = props.patientInfo.orgId;
  getAdviceBaseInfo(queryParams.value).then((res) => {
    adviceBaseList.value = res.data.records;
    total.value = res.data.total;
    nextTick(() => {
      currentIndex.value = 0;
      if (adviceBaseList.value.length > 0) {
        adviceBaseRef.value.setCurrentRow(adviceBaseList.value[0]);
      }
    });
  });
}

// 处理键盘事件
const handleKeyDown = (event) => {
  const key = event.key;
  const data = adviceBaseList.value;

  switch (key) {
    case 'ArrowUp': // 上箭头
      event.preventDefault(); // 阻止默认滚动行为
      if (currentIndex.value > 0) {
        currentIndex.value--;
        setCurrentRow(data[currentIndex.value]);
      }
      break;
    case 'ArrowDown': // 下箭头`
      event.preventDefault();
      if (currentIndex.value < data.length - 1) {
        currentIndex.value++;
        setCurrentRow(data[currentIndex.value]);
      }
      break;
    case 'Enter': // 回车键
      // const currentRow = adviceBaseRef.value.getSelectionRows();
      event.preventDefault();
      if (currentSelectRow.value) {
        // 这里可以触发自定义逻辑，如弹窗、跳转等
        emit('selectAdviceBase', currentSelectRow.value);
      }
      break;
  }
};

function handleQuantity(row) {
  if (row.inventoryList) {
    const totalQuantity = row.inventoryList.reduce((sum, item) => sum + (item.quantity || 0), 0);
    return totalQuantity.toString() + row.minUnitCode_dictText;
  }
  return 0;
}

// 设置选中行（带滚动）
const setCurrentRow = (row) => {
  adviceBaseRef.value.setCurrentRow(row);
  // 滚动到选中行
  const tableBody = adviceBaseRef.value.$el.querySelector('.el-table__body-wrapper');
  const currentRowEl = adviceBaseRef.value.$el.querySelector('.current-row');
  if (tableBody && currentRowEl) {
    currentRowEl.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
  }
};

// 当前行变化时更新索引
const handleCurrentChange = (currentRow) => {
  currentIndex.value = adviceBaseList.value.findIndex((item) => item === currentRow);
  currentSelectRow.value = currentRow;
};

function clickRow(row) {
  emit('selectAdviceBase', row);
}

defineExpose({
  handleKeyDown,
});
</script>
    
<style scoped>
.popover-table-wrapper:focus {
  outline: 2px solid #409eff; /* 聚焦时的高亮效果 */
}
</style>