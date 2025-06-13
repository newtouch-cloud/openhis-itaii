<template>
  <div style="height: 500px;padding-bottom: 120px">
    <el-table ref="medicineRef" height="400" :data="medicineList" @cell-click="clickRow">
      <el-table-column label="药品名称" align="center" prop="registeredName" width="300" />
      <el-table-column label="药品规格" align="center" prop="drugSpecification" />
      <el-table-column label="生产厂家" align="center" prop="manufacturerName" />
      <el-table-column label="国药准字号" align="center" prop="approvalNo"/>
      <el-table-column label="包装单位" align="center" prop="minPackageUnit" />
      <el-table-column label="剂量单位" align="center" prop="minPreparationUnit" />
      <!-- <el-table-column
        label="最小单位"
        align="center"
        prop="minUnitCode_dictText"
      />
      <el-table-column label="规格" align="center" prop="volume" /> -->
      <!-- <el-table-column label="用法" align="center" prop="methodCode_dictText" />
      <el-table-column label="单次剂量" align="center" prop="dose" />
<<<<<<< HEAD
      <el-table-column
        label="剂量单位"
        align="center"
        prop="doseUnitCode_dictText"
      /> -->
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>
        
<script setup>
import { getAllMedicationInfo } from './api';
import { watch } from 'vue';
import { throttle } from 'lodash-es';

const props = defineProps({
  searchKey: {
    type: String,
    default: '',
  },
  itemType: {
    type: String,
    default: '',
  },
});
const emit = defineEmits(['selectRow']);
const queryParams = ref({
  pageNum: 1,
  pageSize: 50,
});
const medicineList = ref([]);
const total = ref(0);

// 节流函数
const throttledGetList = throttle(
  () => {
    console.log('节流执行了', queryParams.value);
    getList();
  },
  300,
  { leading: true, trailing: true }
);

watch(
  () => props.searchKey,
  (newValue) => {
    queryParams.value.searchKey = newValue;
    throttledGetList();
  },
  { deep: true }
);

getList();
function getList() {
  getAllMedicationInfo(queryParams.value).then((res) => {
    console.log(res, 'wwwwwwwwwwwwwwwww药品列表', queryParams.value, 'queryParams.value');
    medicineList.value = res.data.records;
    total.value = res.data.total;
  });
}

function clickRow(row) {
  emit('selectRow', row);
}
</script>
      
<style scoped>
</style>