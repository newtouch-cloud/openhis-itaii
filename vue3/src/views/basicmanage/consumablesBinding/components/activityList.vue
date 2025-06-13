<template>
  <div>
    <el-table ref="medicineRef" height="400" :data="activityList" @cell-click="clickRow" border>
      <el-table-column label="项目名称" align="center" prop="name" width="300" />
      <el-table-column label="类型" align="center" prop="typeEnum_enumText" />
      <el-table-column label="价格" align="right" prop="retailPrice">
        <template #default="scope">
          {{ scope.row.retailPrice.toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column label="说明" align="center" prop="descriptionText" />
    </el-table>
  </div>
</template>
        
<script setup>
import { getActivityList } from './api';
import { watch } from 'vue';
import { throttle } from 'lodash-es';

const props = defineProps({
  searchKey: {
    type: String,
    default: '',
  },
});
const emit = defineEmits(['selectRow']);
const queryParams = ref({
  pageNum: 1,
  pageSize: 50,
  itemType: props.itemType,
});
const activityList = ref([]);

// 节流函数
const throttledGetList = throttle(
  () => {
    getList();
  },
  300,
  { leading: true, trailing: true }
);

watch(
  () => props,
  (newValue) => {
    queryParams.value.searchKey = newValue.searchKey;
    throttledGetList();
  },
  { immdiate: true, deep: true }
);

getList();
function getList() {
  getActivityList(queryParams.value).then((res) => {
    activityList.value = res.data.records;
  });
}

function clickRow(row) {
  emit('selectRow', row);
}
</script>
      
<style scoped>
</style>