<template>
  <el-dialog
    title="转床"
    v-model="props.open"
    width="600px"
    append-to-body
    destroy-on-close
    @close="close"
  >
    <div>
      <el-form :model="form" ref="formRef">
        <el-row :gutter="24" class="mb8">
          <el-col :span="12">
            <el-form-item label="区域：" prop="organizationId_dictText" label-width="100">
              <el-input
                v-model="transferToBedInfo.organizationId_dictText"
                placeholder="请输入区域名称"
                clearable
                disabled
              />
              <!-- <el-select v-model="medRoute" clearable>
                <el-option
                  v-for="dict in method_code"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select> -->
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-table
        ref="table"
        :data="bedList"
        row-key="locationId"
        style="width: 100%; height: 100%"
        highlight-current-row
        show-overflow-tooltip
        max-height="400px"
        @row-click="selectBed"
      >
        <el-table-column label="床号" prop="locationId_dictText" min-width="80" align="center" />
      </el-table>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="submit">保存</el-button>
        <el-button @click="close">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, nextTick } from 'vue';
import { getEmptyBed, saveBedTransfer } from './api';

const { proxy } = getCurrentInstance();
const props = defineProps({
  open: {
    type: Boolean,
    default: false,
  },
  transferToBedInfo: {
    type: Object,
    default: () => {},
  },
});
const { method_code, unit_code, rate_code, distribution_category_code } = proxy.useDict(
  'method_code',
  'unit_code',
  'rate_code',
  'distribution_category_code'
);
const emit = defineEmits(['close','openTransferToBed']);
const form = ref({});
const bedList = ref([]);
const title = ref('');
const rowRules = ref({
  conditionDefinitionId: [{ required: true, message: '请选择诊断', trigger: 'change' }],
  dose: [{ required: true, message: '请输入单次剂量', trigger: 'change' }],
  doseQuantity: [{ required: true, message: '请输入单次剂量', trigger: 'change' }],
  quantity: [{ required: true, message: '请输入数量', trigger: 'change' }],
  dispensePerDuration: [{ required: true, message: '请输入用药天数', trigger: 'change' }],
});

/**
 * 取得床位号
 */
function show() {
  reset();
  title.value = '';
  title.value = props.title;
  form.value = props.transferToBedInfo;
  console.log(props, 'props', title.value, ' props.transferToBedInfo', props.transferToBedInfo);
  if (form.value) {
    getEmptyBed(form.value.organizationId).then((res) => {
      bedList.value = res.data;
      // loading.value = false;
      console.log(res, 'resqqqqqqqqqqqqqqqqqqqqqqq', form.value);
    });
  } else {
    bedList.value = [];
  }
}

/**
 * 选择床位
 *
 * @param row 要选择的床位信息对象
 */
function selectBed(row) {
  console.log(row, 'row 选择床位信息对象');
  form.value.newLocationId = row.locationId;
}
/**
 * 保存换床信息
 */
function submit() {
  console.log(form.value, 'form, prescriptionList.value新增处方');
  // console.log(row, ' 保存处方row 1234567890');
  // 新增的药品信息调用新增接口
  saveBedTransfer(form.value).then((res) => {
    if (res.code == 200) {
      emit('close', 'success');
      reset();
    }
  });
}

function close() {
  reset();
  emit('close');
}

/**
 * 重置表单
 */
function reset() {
  form.value = {
    organizationId: '',
    newLocationId: '',
    organizationId_dictText: '',
    patientId: '', // 处方号
    oldLocationId: '',
    busNo: '',
    encounterLocationId: '',
  };
  proxy.resetForm('formRef');
}

defineExpose({
  show,
});
</script>

<style scoped>
:deep(.pagination-container .el-pagination) {
  right: 20px !important;
}
</style>