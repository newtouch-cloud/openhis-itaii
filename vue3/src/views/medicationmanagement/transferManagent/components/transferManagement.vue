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
        width="200"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="项目类型"
        align="center"
        prop="itemType_enumText"
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
      <el-table-column label="规格" 
        align="center" 
        prop="volume" 
        :show-overflow-tooltip="true"
      />
        <el-table-column
        label="产品批号"
        align="center"
        prop="lotNumber"
      />
        <el-table-column
          label="包装单位"
          align="center"
          prop="unitCode_dictText"
         :show-overflow-tooltip="true"
      />
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
import { getMedicineList } from "./transferManagement";
import { watch } from "vue";
import { throttle } from "lodash-es";

const router = useRouter();
const route = useRoute();

const props = defineProps({
  searchKey: {
    type: String,
    default: "",
  },
  itemType: {
    type: String,
    default: "",
  },
  sourceLocationId:{
    type: String,
    default: "",
  },
  purposeLocationId:{
    type: String,
    default: "",
  },
  sourceLocationId1:{
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
  // pageNum: 1,
  // pageSize: 50,
  itemType: props.itemType,
  // itemType1: props.itemType1,
  orgLocationId:props.sourceLocationId,
  // objLocationId:props.purposeLocationId,
  orgLocationId1:props.sourceLocationId1,
  objLocationId1:props.purposeLocationId1,
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
    console.log(newValue,"newValue")
    queryParams.value.searchKey = newValue.searchKey;
    queryParams.value.itemType = newValue.itemType;
    // queryParams.value.itemType1 = newValue.itemType1;
    queryParams.value.orgLocationId=newValue.sourceLocationId;
    // queryParams.value.objLocationId=newValue.purposeLocationId;
    queryParams.value.orgLocationId1=newValue.sourceLocationId1;
    // queryParams.value.objLocationId1=newValue.purposeLocationId1;
    queryParams.value.purchaseFlag = 0
    throttledGetList();
  },
  { immdiate: true, deep: true }
);

getList();
function getList() {
   console.log(queryParams.value,"queryParams.value1")
  if(route.query.supplyBusNo){ // 编辑
    queryParams.value.itemType =  queryParams.value.itemType;
    queryParams.value.orgLocationId = queryParams.value.orgLocationId1
    // queryParams.value.objLocationId = queryParams.value.objLocationId1
    console.log(queryParams.value,"queryParams.value1111111")
  }
  // delete queryParams.value.itemType
  delete queryParams.value.orgLocationId1
  delete queryParams.value.objLocationId1
  console.log(queryParams.value,"queryParams.value")
  getMedicineList(queryParams.value).then((res) => {
    medicineList.value = res.data;
    console.log(medicineList.value,"medicineList.value ")
  });
}

function clickRow(row) {
  console.log(row,"row--------------------")
  emit("selectRow", row);
}
</script>
      
<style scoped>
:deep( .hover_row){
  width: 100vw!important;
}
</style>