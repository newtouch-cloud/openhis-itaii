<template>
  <div>
    <el-table
      ref="adviceBaseRef"
       height="400"
      :data="adviceBaseList"
      row-key="patientId"
      @cell-click="clickRow"
    >
      <el-table-column label="名称" align="center" prop="productName" />
      <el-table-column
        label="类型"
        align="center"
        prop="activityType_enumText"
      />
      <el-table-column
        label="包装单位"
        align="center"
        prop="unitCode_dictText"
      />
      <el-table-column
        label="最小单位"
        align="center"
        prop="minUnitCode_dictText"
      />
      <el-table-column label="规格" align="center" prop="volume" />
      <el-table-column label="用法" align="center" prop="methodCode_dictText" />
      <el-table-column label="频次" align="center" prop="rateCode_dictText" />
      <el-table-column label="单次剂量" align="center" prop="dose" />
      <el-table-column
        label="剂量单位"
        align="center"
        prop="doseUnitCode_dictText"
      />
      <el-table-column label="生产厂家" align="center" prop="manufacturer" />
      <el-table-column label="诊断名称" align="center" prop="name" />
    </el-table>
  </div>
</template>
      
<script setup>
import { getAdviceBaseInfo } from "./api";

const props = defineProps({
  searchkey: {
    type: String,
    default: "",
  },
});
const emit = defineEmits(["selsectAdviceBase"]);
const total = ref(0);
const queryParams = ref({});
const adviceBaseList = ref([]);

watch(
  () => props.searchkey,
  (newValue) => {
    queryParams.value.searchKey = newValue; 
    getList();
  },
  { immdiate: true }
);

getList();
function getList() {
  getAdviceBaseInfo(queryParams.value).then((res) => {
    adviceBaseList.value = res.data.records;
    total.value = res.data.total;
  });
}

function clickRow(row) {
  emit("selsectAdviceBase", row);
}
</script>
    
<style scoped>
::v-deep .el-table__row:hover > td {
  background-color: #cde5ff !important; /* 设置为透明或其他你想要的颜色 */
}
</style>