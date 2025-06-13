<template>
  <div>
    <el-table
      ref="diagnosisDefinitionRef"
      :data="diagnosisDefinitionList"
      row-key="patientId"
      @cell-click="clickRow"
      max-height="400"
    >
      <el-table-column label="诊断名称" align="center" prop="name" />
      <el-table-column label="医保编码" align="center" prop="ybNo" />
    </el-table>
  </div>
</template>
    
<script setup>
import { getDiagnosisDefinitionList } from './api';

const props = defineProps({
  diagnosisSearchkey: {
    type: String,
    default: '',
  },
});
const emit = defineEmits(['selectDiagnosis']);
const total = ref(0);
const queryParams = ref({
  pageSize: 1000,
  pageNo: 1,
});
const diagnosisDefinitionList = ref([]);

watch(
  () => props.diagnosisSearchkey,
  (newValue) => {
    queryParams.value.searchKey = newValue;
    getList();
  },
  { immdiate: true }
);

getList();
function getList() {
  getDiagnosisDefinitionList(queryParams.value).then((res) => {
    diagnosisDefinitionList.value = res.data.records;
    total.value = res.data.total;
  });
}

function clickRow(row) {
  emit('selectDiagnosis', row);
}
</script>
  
<style scoped>
</style>