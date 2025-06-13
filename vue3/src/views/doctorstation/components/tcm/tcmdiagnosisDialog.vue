<template>
  <el-dialog
    title="中医诊断"
    v-model="openDiagnosis"
    width="500px"
    append-to-body
    destroy-on-close
    @close="close"
    @open="open"
  >
    <div class="diagnosis-container">
      <div class="select-group">
        <span class="select-label">中医诊断：</span>
        <el-select
          v-model="condition"
          placeholder="请选择中医诊断"
          filterable
          clearable
          style="width: 300px"
        >
          <el-option
            v-for="item in conditionOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </div>

      <div class="select-group">
        <span class="select-label">中医证候：</span>
        <el-select
          v-model="syndrome"
          placeholder="请选择中医证候"
          filterable
          clearable
          style="width: 300px"
        >
          <el-option
            v-for="item in syndromeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="submit">确 定</el-button>
        <el-button @click="close">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref } from 'vue';
import { getTcmCondition, getTcmSyndrome } from '@/views/doctorstation/components/api';

const condition = ref('');
const syndrome = ref('');
const conditionOptions = ref([]);
const syndromeOptions = ref([]);
const diagnosisList = ref([]);
const openDiagnosis = ref(false);
const emit = defineEmits(['flush']);
const { proxy } = getCurrentInstance();
const props = defineProps({
  patientInfo: {
    type: Object,
    required: true,
  },
});
function open() {}

function submit() {
  // 提交逻辑
  if (!condition.value || !syndrome.value) {
    proxy.$modal.msgWarning('请选择诊断和证候');
    return; // 确保选择了诊断和证候
  }

  // 构建诊断对象
  const diagnosis = {
    id: Date.now(), // 使用时间戳作为唯一ID
    condition: conditionOptions.value.find((item) => item.value === condition.value)?.label || '',
    conditionCode: condition.value,
    syndrome: syndromeOptions.value.find((item) => item.value === syndrome.value)?.label || '',
    syndromeCode: syndrome.value,
  };
  const data = localStorage.getItem(`tcmDiagnosisList_${props.patientInfo.encounterId}`);
  diagnosisList.value = JSON.parse(data);
  // 添加到列表
  diagnosisList.value.push(diagnosis);
  localStorage.removeItem(`tcmDiagnosisList_${props.patientInfo.encounterId}`);
  // 保存到本地缓存
  localStorage.setItem(
    `tcmDiagnosisList_${props.patientInfo.encounterId}`,
    JSON.stringify(diagnosisList.value)
  );

  console.log('当前诊断列表:', diagnosisList.value);
  emit('flush')
  close();
}

function openDialog() {
  openDiagnosis.value = true;
  // 获取中医诊断选项
  getTcmCondition().then((res) => {
    conditionOptions.value = res.data.records.map((item) => ({
      value: item.ybNo,
      label: item.name,
    }));
  });

  // 获取中医证候选项
  getTcmSyndrome().then((res) => {
    syndromeOptions.value = res.data.records.map((item) => ({
      value: item.ybNo,
      label: item.name,
    }));
  });
}

function close() {
  // 关闭逻辑
  condition.value = '';
  syndrome.value = '';
  openDiagnosis.value = false;
}

defineExpose({ openDialog });
</script>

<style scoped>
.diagnosis-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  padding: 20px 0;
}

.select-group {
  display: flex;
  align-items: center;
  width: 100%;
  justify-content: center;
}

.select-label {
  width: 100px;
  text-align: right;
  margin-right: 10px;
  font-size: 14px;
}

:deep(.pagination-container .el-pagination) {
  right: 20px !important;
}
</style>