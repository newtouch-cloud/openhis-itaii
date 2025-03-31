<template>
  <div>
    <el-table
      height="400"
      :data="patientList"
      row-key="id"
      @cell-click="clickRow"
    >
      <el-table-column label="姓名" align="center" prop="name" />
      <el-table-column label="性别" align="center" prop="genderEnum_enumText" />
      <el-table-column label="证件号" align="center" prop="idCard" />
      <el-table-column label="联系电话" align="center" prop="phone" />
      <el-table-column label="年龄" align="center" prop="age" />
    </el-table>
  </div>
</template>
        
<script setup>
import { getOutpatientRegistrationList } from "./outpatientregistration";
const props = defineProps({
  searchkey: {
    type: String,
    default: "",
  },
});
const emit = defineEmits(["selsectPatient"]);
const total = ref(0);
const queryParams = ref({
  pageNum: 1,
  pageSize: 50,
});
const patientList = ref([]);

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
  getOutpatientRegistrationList(queryParams.value).then((res) => {
    patientList.value = res.data.records;
    total.value = res.data.total;
  });
}

function clickRow(row) {
  emit("selsectPatient", row);
}
</script>
      
<style scoped>
::v-deep .el-table__row:hover > td {
  background-color: #cde5ff !important; /* 设置为透明或其他你想要的颜色 */
}
</style>