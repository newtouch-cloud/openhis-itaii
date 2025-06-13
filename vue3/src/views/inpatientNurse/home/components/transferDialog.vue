<template>
  <el-dialog
    :title="props.title"
    v-model="props.open"
    width="480px"
    append-to-body
    destroy-on-close
    @close="close"
  >
    <div>
      <el-form :model="form" ref="formRef">
        <el-row :gutter="24" class="mb8">
          <el-col>
            <el-form-item label="病历号：" prop="patientNo" label-width="100">
              <el-input
                v-model="form.patientNo"
                placeholder="病历号"
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24" class="mb8">
          <el-col>
            <el-form-item label="患者姓名：" prop="patientName" label-width="100">
              <el-input
                v-model="form.patientName"
                placeholder="患者姓名"
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- <el-row :gutter="24" class="mb8" v-if="props.title == '出院'">
          <el-col>
            <el-form-item label="出院原因：" prop="dscgTrtRslt" label-width="100">
              <el-select v-model="form.dscgTrtRslt" clearable>
                <el-option
                  v-for="dict in dscg_trt_rslt_code"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row> -->
        <el-row :gutter="24" class="mb8" v-if="props.title == '出院'">
          <el-col>
            <el-form-item label="出院时间：" prop="DischargeHospitalYmd" label-width="100">
              <el-date-picker
                v-model="form.DischargeHospitalYmd"
                value-format="YYYY-MM-DD HH:mm:ss"
                type="datetime"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24" class="mb8" v-if="props.title == '转科'">
          <el-col>
            <el-form-item label="转科去向：" prop="organizationId" label-width="100">
              <el-select v-model="form.organizationId" clearable>
                <el-option
                  v-for="caty in catyList"
                  :key="caty.id"
                  :label="caty.name"
                  :value="caty.id"
                  :disabled="caty.id === form.organizationIdOld"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24" class="mb8" v-if="props.title == '转科'">
          <el-col>
            <el-form-item label="转科时间：" prop="DepartmentTransferYmd" label-width="100">
              <el-date-picker
                v-model="form.DepartmentTransferYmd"
                value-format="YYYY-MM-DD HH:mm:ss"
                type="datetime"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
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
import { getCaty, saveDepartmentTransfer, saveDischargeHospital } from './api';
const { proxy } = getCurrentInstance();
const props = defineProps({
  open: {
    type: Boolean,
    default: false,
  },
  title: {
    type: String,
    default: '',
  },
  transferInfo: {
    type: Object,
  },
});
const { dscg_trt_rslt_code, unit_code, rate_code, distribution_category_code } = proxy.useDict(
  'dscg_trt_rslt_code',
  'unit_code',
  'rate_code',
  'distribution_category_code'
);
const emit = defineEmits(['close']);
const selectRow = ref({});
const prescriptionTypeList = ref([]);
const catyList = ref([]);
const form = ref({});
const title = ref('');

const rowRules = ref({
  conditionDefinitionId: [{ required: true, message: '请选择诊断', trigger: 'change' }],
  dose: [{ required: true, message: '请输入单次剂量', trigger: 'change' }],
  doseQuantity: [{ required: true, message: '请输入单次剂量', trigger: 'change' }],
  quantity: [{ required: true, message: '请输入数量', trigger: 'change' }],
  dispensePerDuration: [{ required: true, message: '请输入用药天数', trigger: 'change' }],
});

getCatyList();
/**
 * 取得床位号
 */
function show() {
  reset();
  title.value = '';
  title.value = props.title;
  form.value = props.transferInfo;
  console.log(props, 'props', title.value);
}

/**
 * 查询科室信息
 *
 * @param row 要选择的床位信息对象
 */
function getCatyList() {
  getCaty().then((res) => {
    if (res.code == 200) {
      catyList.value = res.data;
    } else {
      catyList.value = [];
    }

    console.log(res, ' catyList.value');
  });
}

/**
 * 选择科室信息
 *
 * @param row 要选择科室信息对象
 */
function selectOrganizationId(value) {
  console.log(value, ' value');
  form.value.organizationId = value;
}
/**
 * 保存处方
 */
function submit() {
  console.log(form.value, 'form, prescriptionList.value新增处方');
  // console.log(row, ' 保存处方row 1234567890');
  // 新增的药品信息调用新增接口
  if (title.value == '转科') {
    saveDepartmentTransfer(form.value).then((res) => {
      if (res.code == 200) {
        emit('close', 'success');
        reset();
      }
    });
  }
  if (title.value == '出院') {
    saveDischargeHospital(form.value).then((res) => {
      if (res.code == 200) {
        emit('close', 'success');
        reset();
      }
    });
  }
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
    organizationIdOld: '',
    patientName: '',
    patientId: '', // 处方号
    hospitalNo: '',
    dscgTrtRslt: '',
    encounterId: '',
    encounterLocationId: '',
    locationId: '',
    DepartmentTransferYmd: undefined,
    DischargeHospitalYmd: undefined,
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