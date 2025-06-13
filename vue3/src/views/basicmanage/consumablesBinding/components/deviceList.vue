<template>
  <div>
    <el-table ref="medicineRef" height="400" :data="deviceList" @cell-click="clickRow" border>
      <el-table-column label="项目名称" align="center" prop="name" width="200" />
      <el-table-column label="分类" align="center" prop="categoryCode_dictText" width="150" />
      <el-table-column label="种类" align="center" prop="typeCode_dictText" />
      <el-table-column label="规格" align="center" prop="size" />
      <el-table-column label="价格" align="right" prop="retailPrice">
        <template #default="scope">
          {{ scope.row.retailPrice.toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column label="生产厂家" align="center" prop="supplyId_dictText" />
    </el-table>
  </div>
</template>
        
<script setup>
import { getDeviceList } from './api';
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
const deviceList = ref([]);

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
  getDeviceList(queryParams.value).then((res) => {
    deviceList.value = res.data.records;
  });
}

function clickRow(row) {
  emit('selectRow', row);
}
</script>
      
<style scoped>
</style>