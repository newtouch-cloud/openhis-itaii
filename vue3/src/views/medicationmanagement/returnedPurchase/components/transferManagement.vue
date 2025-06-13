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
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="项目类型"
        align="center"
        prop="itemType_enumText"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="规格" 
        align="center" 
        prop="volume" 
        :show-overflow-tooltip="true"
      />
        <el-table-column
        label="产品批号"
        align="center"
        prop="lotNumber"
        :show-overflow-tooltip="true"
      />
        <el-table-column
          label="包装单位"
          align="center"
          prop="unitCode_dictText"
         :show-overflow-tooltip="true"
      />
      <el-table-column
        label="最小单位"
        align="center"
        prop="minUnitCode_dictText"
        :show-overflow-tooltip="true"
      />
      <!-- <el-table-column label="用法" align="center" prop="methodCode_dictText" />
      <el-table-column label="单次剂量" align="center" prop="dose" />
      <el-table-column
        label="剂量单位"
        align="center"
        prop="doseUnitCode_dictText"
      /> -->
      <el-table-column label="生产厂家" align="center" prop="manufacturer" :show-overflow-tooltip="true"/>
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
});
const emit = defineEmits(["selectRow"]);
const queryParams = ref({
  pageNum: 1,
  pageSize: 50,
  itemType: props.itemType,
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
    queryParams.value.searchKey = newValue.searchKey;
    queryParams.value.itemType = newValue.itemType;
    throttledGetList();
  },
  { immdiate: true, deep: true }
);

getList();
function getList() {
  queryParams.value.purchaseFlag = 1
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