<template>
  <div style="margin-bottom: 15px">
    <el-button type="primary" @click="addEmr()">保存</el-button>
    <el-button type="primary" plain @click="handleEmrTemplate()" style="margin-left: 20px">
      模板
    </el-button>
    <el-button type="primary" plain @click="handleSaveTemplate()" style="margin-left: 20px">
      另存模板
    </el-button>
    <el-button type="primary" plain @click="handleEmrHistory()" style="margin-left: 20px">
      历史病历
    </el-button>
    <!-- <el-button type="primary" plain @click="handleReceive()" style="margin-left: 20px">
      打印病历
    </el-button> -->
  </div>
  <div style="height: 650px; overflow-y: auto; overflow-x: hidden">
    <el-form ref="emrRef" :model="form" :rules="rules" label-width="80px">
      <el-row :gutter="24">
        <el-col :span="6">
          <el-form-item label="身高" prop="height" style="width: 100%">
            <el-input placeholder="" v-model="form.height" class="input-with-bottom-border">
              <template #suffix>cm</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="体重" prop="weight" style="width: 100%">
            <el-input placeholder="" v-model="form.weight" class="input-with-bottom-border">
              <template #suffix>kg</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="体温" prop="temperature" style="width: 100%">
            <el-input placeholder="" v-model="form.temperature" class="input-with-bottom-border">
              <template #suffix>℃</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="脉搏" prop="pulse" style="width: 100%">
            <el-input placeholder="" v-model="form.pulse" class="input-with-bottom-border">
              <template #suffix>次/分</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="24">
        <!-- <el-col :span="6">
              <el-form-item
                label="血糖"
                prop="chiefComplaint"
                style="width: 100%"
              >
                <el-input placeholder="" class="input-with-bottom-border">
                  <template #suffix></template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item
                label="血糖"
                prop="chiefComplaint"
                style="width: 100%"
              >
                <el-input placeholder="" class="input-with-bottom-border">
                  <template #suffix>mmol/L</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item
                label="收缩压"
                prop="chiefComplaint"
                style="width: 100%"
              >
                <el-input placeholder="" class="input-with-bottom-border">
                  <template #suffix>mmHg</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item
                label="舒张压"
                prop="chiefComplaint"
                style="width: 100%"
              >
                <el-input placeholder="" class="input-with-bottom-border">
                  <template #suffix>mmHg</template>
                </el-input>
              </el-form-item>
            </el-col> -->
      </el-row>
      <el-row :gutter="24">
        <el-col :span="18">
          <el-form-item label="主诉" prop="chiefComplaint">
            <el-input v-model="form.chiefComplaint" type="textarea" :rows="1" placeholder="" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="发病日期" prop="onsetDate">
            <el-date-picker
              v-model="form.onsetDate"
              type="date"
              size="default"
              style="width: 100% !important"
              placement="bottom"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="现病史" prop="currentIllnessHistory">
        <el-input v-model="form.currentIllnessHistory" type="textarea" :rows="2" placeholder="" />
      </el-form-item>
      <el-form-item label="既往史" prop="pastMedicalHistory">
        <el-input v-model="form.pastMedicalHistory" type="textarea" :rows="2" placeholder="" />
      </el-form-item>
      <el-form-item label="月经史" prop="menstrualHistory">
        <el-input v-model="form.menstrualHistory" type="textarea" :rows="2" placeholder="" />
      </el-form-item>
      <el-form-item label="过敏史" prop="allergyHistory">
        <el-input v-model="form.allergyHistory" type="textarea" :rows="2" placeholder="" />
      </el-form-item>
      <el-form-item label="查体" prop="physicalExamination">
        <el-input v-model="form.physicalExamination" type="textarea" :rows="2" placeholder="" />
      </el-form-item>
      <el-form-item label="处理" prop="treatment">
        <el-input v-model="form.treatment" type="textarea" :rows="2" placeholder="" />
      </el-form-item>
      <el-form-item label="辅助检查" prop="auxiliaryExamination">
        <el-input v-model="form.auxiliaryExamination" type="textarea" :rows="2" placeholder="" />
      </el-form-item>
    </el-form>
    <el-dialog
      :title="emrTitle"
      v-model="openEmrTemplate"
      :width="showDialog == 'emrHistory'?'800px':'600px'"
      append-to-body
      destroy-on-close
    >
      <emrTemplate v-if="showDialog == 'emrTemplate'" @selectRow="templateSelect" />
      <emrhistory v-if="showDialog == 'emrHistory'" @selectRow="emrHistorySelect" />
      <div v-if="showDialog == 'saveTemplate'">
        <span> 模板名称： </span>
        <el-input
          v-model="templateName"
          style="width: 260px; margin-top: 10px; margin-right: 20px"
        />
        <el-radio-group v-model="radio">
          <el-radio-button :label="1">个人</el-radio-button>
          <el-radio-button :label="2">科室</el-radio-button>
          <el-radio-button :label="3">全院</el-radio-button>
        </el-radio-group>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submit">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { saveEmr, getEmrDetail, saveEmrTemplate } from './api';
import emrTemplate from './emrtemplate.vue';
import emrhistory from './emrhistory.vue';

const form = ref({});
const emrTitle = ref('');
const radio = ref(1);
const rules = ref({
  chiefComplaint: [{ required: true, message: '请输入主诉', trigger: 'blur' }],
});
const emits = defineEmits(['save']);
const { proxy } = getCurrentInstance();
const showDialog = ref('');
const openEmrTemplate = ref(false);
const templateName = ref('');
const props = defineProps({
  patientInfo: {
    type: Object,
    required: true,
  },
});

watch(
  () => form.value,
  () => {
    // 如果表单数据有变化，通知父组件保存
    emits('save', false);
  },
  { deep: true }
);

/**
 * 保存病历
 */
function addEmr() {
  proxy.$refs['emrRef'].validate((valid) => {
    if (valid) {
      saveEmr({
        patientId: props.patientInfo.patientId,
        encounterId: props.patientInfo.encounterId,
        contextJson: form.value,
      }).then((res) => {
        if (res.code == 200) {
          proxy.$modal.msgSuccess('病历已保存');
          emits('save', true);
        }
      });
    }
  });
}
/**
 * 病历模板
 */
function handleEmrTemplate() {
  emrTitle.value = '病历模板';
  showDialog.value = 'emrTemplate';
  openEmrTemplate.value = true;
}
/**
 * 选择病历模板
 */
function templateSelect(row) {
  form.value = row;
}
function emrHistorySelect(row) {
  form.value = row;
  openEmrTemplate.value = false;
}
/**
 * 历史病历
 */
function handleEmrHistory() {
  emrTitle.value = '历史病历';
  showDialog.value = 'emrHistory';
  openEmrTemplate.value = true;
  sessionStorage.setItem('patientId', props.patientInfo.patientId);
}

function getDetail(encounterId) {
  getEmrDetail(encounterId).then((res) => {
    if (res.data) {
      form.value = JSON.parse(res.data.contextJson);
      // 提交父组件刷新保存装填
      emits('save', true);
    } else {
      form.value = {};
    }
  });
}

/**
 * 保存病历模板
 */
function handleSaveTemplate() {
  emrTitle.value = '保存模板';
  showDialog.value = 'saveTemplate';
  openEmrTemplate.value = true;
}

/**
 * 弹窗确认操作，包括保存病历模板/选择病历模板/选择历史病历
 */
function submit() {
  switch (showDialog.value) {
    case 'saveTemplate':
      saveEmrTemplate({
        templateName: templateName.value,
        useScopeCode: radio.value,
        contextJson: form.value,
      }).then((res) => {
        if (res.code == 200) {
          openEmrTemplate.value = false;
          proxy.$modal.msgSuccess('保存成功');
        }
      });
      break;
    case 'emrTemplate':
      openEmrTemplate.value = false;
      break;
    case 'emrHistory':
      break;
  }
}
function cancel() {
  openEmrTemplate.value = false;
  // openDiagnosis.value = false;
}

defineExpose({ getDetail, addEmr });
</script>