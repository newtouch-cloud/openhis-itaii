<template>
  <div>
    <el-table
      ref="medicineRef"
      height="400"
      :data="medicineList"
      @cell-click="clickRow"
    >
      <el-table-column
        label="项目名称"
        align="center"
        prop="name"
        width="300"
      />
      <el-table-column
        label="项目类型"
        align="center"
        prop="itemType_enumText"
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
      <!-- <el-table-column label="用法" align="center" prop="methodCode_dictText" />
      <el-table-column label="单次剂量" align="center" prop="dose" />
      <el-table-column
        label="剂量单位"
        align="center"
        prop="doseUnitCode_dictText"
      /> -->
      <el-table-column label="生产厂家" align="center" prop="manufacturer" />
       <el-table-column
        label="编码"
        align="center"
        prop="ybNo"
      />
    </el-table>
  </div>
</template>
        
<script setup>
import { getMedicineList } from "./api";
import { watch } from "vue";
import { throttle } from "lodash-es";

const props = defineProps({
  searchKey: {
    type: String,
    default: "",
  },
  itemType: {
    type: String,
    default: "",
  },
  purposeLocationId:{
    type: String,
    default: "",
  },
  purposeLocationId1:{
    type: String,
    default: "",
  },
});
const emit = defineEmits(["selectRow"]);
const queryParams = ref({
  itemType: props.itemType,
  orgLocationId:props.purposeLocationId,
  orgLocationId1:props.purposeLocationId1,
  purchaseFlag:0
});
const medicineList = ref([]);

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
    queryParams.value.searchKey = newValue.searchKey
    queryParams.value.itemType = newValue.itemType
    queryParams.value.orgLocationId = newValue.sourceLocationId
    queryParams.value.orgLocationId1 = newValue.sourceLocationId1
    throttledGetList();
  },
  { immdiate: true, deep: true }
);

getList();
function getList() {
  if(route.query.supplyBusNo){ // 编辑
    queryParams.value.itemType =  queryParams.value.itemType;
    queryParams.value.orgLocationId = queryParams.value.orgLocationId1
  }
  delete queryParams.value.orgLocationId1
  getMedicineList(queryParams.value).then((res) => {
    medicineList.value = res.data;
  });
}

function clickRow(row) {
  emit("selectRow", row);
}
</script>
      
<style scoped>
</style>