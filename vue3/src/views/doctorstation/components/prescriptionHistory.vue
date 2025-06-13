<template>
  <el-drawer v-model="drawer" title="历史医嘱" direction="ltr">
    <div style="margin: 10px 0px">
      <el-input
        v-model="queryParams.searchKey"
        placeholder="请输入医嘱信息"
        clearable
        style="width: 50%; margin-bottom: 10px"
        @keyup.enter="getList"
      >
        <template #append>
          <el-button icon="Search" @click="getList" />
        </template>
      </el-input>
    </div>
    <el-table :data="orderList">
      <el-table-column label="医嘱项" align="center" prop="adviceName" width="150" />
      <!-- <el-table-column label="组套类型" align="center" prop="typeEnum_enumText" /> -->
      <el-table-column label="单次剂量" align="center" prop="rangeCode_dictText">
        <template #default="scope">
          {{
            scope.row.dose
              ? formatNumber(scope.row.dose) + ' ' + scope.row.doseUnitCode_dictText
              : ''
          }}
        </template>
      </el-table-column>
      <el-table-column label="总量" align="center" prop="rangeCode_dictText">
        <template #default="scope">
          {{ scope.row.quantity ? scope.row.quantity + ' ' + scope.row.unitCode_dictText : '' }}
        </template>
      </el-table-column>
      <el-table-column label="频次/用法" align="center" prop="rangeCode_dictText" width="200">
        <template #default="scope">
          {{
            scope.row.rateCode_dictText
              ? scope.row.rateCode_dictText +
                ' ' +
                scope.row.dispensePerDuration +
                '天' +
                ' ' +
                scope.row.methodCode_dictText
              : ''
          }}
        </template>
      </el-table-column>
      <el-table-column label="注射药品" align="center" prop="rangeCode_dictText">
        <template #default="scope">
          {{ scope.row.injectFlag_enumText || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="皮试" align="center" prop="rangeCode_dictText">
        <template #default="scope">
          {{ scope.row.skinTestFlag_enumText || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="诊断" align="center" prop="rangeCode_dictText">
        <template #default="scope">
          {{ scope.row.diagnosisName || scope.row.conditionDefinitionName }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" fixed="right">
        <template #default="scope">
          <el-button type="primary" link @click="handleUseOrderGroup(scope.row)">选择</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-drawer>
</template>

<script setup>
import { getAdviceHistoryInfo } from './api';
import { formatNumber } from '@/utils/his';

const props = defineProps({
  patientInfo: {
    type: Object,
    required: true,
  },
  diagnosis: {
    type: Object,
    required: true,
  },
});

const drawer = ref(false);
const orderList = ref([]);
const emit = defineEmits(['userPrescriptionHistory']);
const queryParams = ref({
  typeEnum: 1,
});

function handleOpen() {
  drawer.value = true;
  getList();
}

function handleUseOrderGroup(row) {
  row = {
    ...row,
    conditionId: props.diagnosis.conditionId,
    conditionDefinitionId: props.diagnosis.definitionId,
  };
  // value.conditionId = props.diagnosis.conditionId;
  // value.conditionDefinitionId = props.diagnosis.definitionId;
  emit('userPrescriptionHistory', row);
  drawer.value = false;
}

function getList() {
  getAdviceHistoryInfo({ patientId: props.patientInfo.patientId, encounterId: props.patientInfo.encounterId }).then((res) => {
    orderList.value = res.data;
  });
}

defineExpose({
  handleOpen,
});
</script>