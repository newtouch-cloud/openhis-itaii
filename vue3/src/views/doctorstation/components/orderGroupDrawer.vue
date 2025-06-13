<template>
  <el-drawer v-model="drawer" title="组套信息" direction="ltr">
    <div style="margin: 10px 0px">
      <el-input
        v-model="queryParams.searchKey"
        placeholder="请输入组套信息"
        clearable
        style="width: 45%; margin-bottom: -6px; margin-right: 50px"
        @keyup.enter="getList"
      >
        <template #append>
          <el-button icon="Search" @click="getList" />
        </template>
      </el-input>
      <el-radio-group v-model="queryParams.rangeCode" @change="getList">
        <el-radio-button :label="1">个人</el-radio-button>
        <el-radio-button :label="2">科室</el-radio-button>
        <el-radio-button :label="3">全院</el-radio-button>
      </el-radio-group>
    </div>
    <el-table :data="orderList">
      <el-table-column label="组套名称" align="center" prop="name" />
      <!-- <el-table-column label="组套类型" align="center" prop="typeEnum_enumText" /> -->
      <el-table-column label="使用范围" align="center" prop="rangeCode_dictText" />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button type="primary" link @click="handleUseOrderGroup(scope.row)">选择</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-drawer>
</template>

<script setup>
import { getOrderGroupList } from './api';

const props = defineProps({
  diagnosis: {
    type: Object,
    required: true,
  },
});

const drawer = ref(false);
const orderList = ref([]);
const emit = defineEmits(['useOrderGroup']);
const queryParams = ref({
  typeEnum: 1,
  rangeCode: 3,
});

function handleOpen() {
  drawer.value = true;
  getList();
}

function handleUseOrderGroup(row) {
  let value = JSON.parse(row.groupJson);
  value = value.map((item) => {
    return {
      ...item,
      conditionId: props.diagnosis.conditionId,
      conditionDefinitionId: props.diagnosis.definitionId,
    };
  });
  // value.conditionId = props.diagnosis.conditionId;
  // value.conditionDefinitionId = props.diagnosis.definitionId;
  emit('useOrderGroup', value);
  drawer.value = false;
}

function getList() {
  getOrderGroupList(queryParams.value).then((res) => {
    orderList.value = res.data.records;
  });
}

defineExpose({
  handleOpen,
});
</script>