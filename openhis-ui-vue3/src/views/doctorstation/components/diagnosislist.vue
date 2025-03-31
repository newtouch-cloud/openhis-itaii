<template>
  <div>
    <el-table
      ref="diagnosisDefinitionRef"
      :data="diagnosisDefinitionList"
      row-key="patientId"
      @cell-click="clickRow"
      @selection-change="handleSelectionChange"
    >
      <el-table-column label="诊断名称" align="center" prop="name" />
      <el-table-column label="医保编码" align="center" prop="ybNo" />
    </el-table>
  </div>
</template>
    
<script setup>
import { getDiagnosisDefinitionList } from "./api";

const props = defineProps({
  diagnosisSearchkey: {
    type: String,
    default: "",
  },
});
const emit = defineEmits(["selsectDiagnosis"]);
const total = ref(0);
const queryParams = ref({});
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
  emit("selsectDiagnosis", row);
}
</script>
  
<style scoped>
::v-deep .el-table__row:hover > td {
  background-color: #cde5ff !important; /* 设置为透明或其他你想要的颜色 */
}
</style>