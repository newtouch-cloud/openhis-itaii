<template>
  <div style="width: 100%; padding: 0px 20px">
    <el-row  :gutter="20" v-if="cardList.length > 0" class="custom-row">
      <el-col
        v-for="o in cardList"
        :key="o.patientID"
        :span="4"
        style="margin-bottom: 10px; margin-right: 0px"
        class="custom-col"
      >
        <div class="pf_card" @click="clickAct">
          <el-card class="box-card" :body-style="{ padding: '0px' }">
            <div class="card-content" @dblclick="openPatientDetailDialog(o)">
              <div
                class="header"
                style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis"
              >
                <span v-if="o.priorityEnum == 1" class="label1">{{
                  o.locationId_dictText ? o.locationId_dictText : '暂无床位'
                }}</span>
                <span v-else-if="o.priorityEnum == 2" class="label2">
                  {{ o.locationId_dictText ? o.locationId_dictText : '暂无床位' }}
                </span>
                <span v-else-if="o.priorityEnum == 3" class="label3">
                  {{ o.locationId_dictText ? o.locationId_dictText : '暂无床位' }}
                </span>
                <span v-else-if="o.priorityEnum == 4" class="label4">
                  {{ o.locationId_dictText ? o.locationId_dictText : '暂无床位' }}
                </span>
                <span v-else-if="o.priorityEnum == '' || o.priorityEnum === null" class="label5">
                  {{ o.locationId_dictText ? o.locationId_dictText : '暂无床位' }}
                </span>
                <span v-else class="label5" />
                <!-- 左侧内容（优先级标签 + 姓名） -->
                <div style="flex: 1; overflow: hidden; text-overflow: ellipsis">
                  <span
                    class="header-label"
                    :title="o.patientName"
                    style="display: inline-block; max-width: 140px"
                    >{{ o.patientName }}</span
                  >
                </div>
                <span class="header-label" style="margin-right: 10px"
                  >{{ o.genderEnum_enumText }}/{{ o.ageString }}</span
                >
              </div>
              <div class="row">
                <div class="layui-inline">
                  <div>住院号</div>
                  <div>{{ o.hospitalNo }}</div>
                </div>
              </div>
              <div class="row">
                <div class="layui-inline">
                  <div>入院科室</div>
                  <div>{{ o.organizationId_dictText }}</div>
                </div>
              </div>
              <div class="row2">
                <div class="layui-inline">
                  <div>诊断</div>
                  <div class="patientDiagnosis" :title="o.mainDiagnosis">
                    {{ o.mainDiagnosis }}
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="layui-inline">
                  <div>入院</div>
                  <div>{{ o.admissionDate }}</div>
                </div>
              </div>
              <div class="row">
                <div class="layui-inline">
                  <div>入院天数</div>
                  <div>{{ o.hospitalizationDays }}</div>
                </div>
              </div>
              <!-- <div class="row">
                <div class="layui-inline">
                  <div>手术时间</div>
                  <div>{{ o.surgeryStartTime }}</div>
                </div>
              </div>
              <div class="row">
                <div class="layui-inline">
                  <div>手术天数</div>
                  <div>{{ o.postoperativeDays }}</div>
                </div>
              </div> -->
              <div class="row">
                <div class="layui-inline">
                  <div>医生</div>
                  <div>{{ o.responsibleDoctor }}</div>
                </div>
              </div>
              <div class="row">
                <div style="margin-left: 10px">
                  <!-- <div>操作</div> -->
                  <el-button type="primary" link>腕带</el-button>
                  <el-button type="primary" link>床头卡</el-button>
                  <el-button type="primary" link @click="openTransferCyDialog(o)">出院</el-button>
                  <el-button type="primary" link @click="openTransferZkDialog(o)">转科</el-button>
                  <el-button type="primary" link @click="openTransferToBedDialog(o)"
                    >转床</el-button
                  >
                </div>
              </div>
            </div>
            <!-- </div> -->
          </el-card>
        </div>
      </el-col>
    </el-row>
    <div v-else class="no-data">暂无患者数据</div>
    <patient-detial-dialog
      ref="patientDetialDialogRef"
      :open="openPatientDetail"
      @close="closePatientDetialDialog"
    />
    <transfer-to-bed-dialog
      ref="transferToBedRef"
      :open="openTransferToBed"
      @close="closeTransferToBedDialog"
    />
    <transfer-dialog
      ref="transferRef"
      :open="openTransfer"
      :title="title"
      @close="closeTransferDialog"
    />
  </div>
</template>
<script setup>
import patientDetialDialog from './patientDetialDialog.vue';
import transferToBedDialog from './transferToBedDialog.vue';
import transferDialog from './transferDialog.vue';
import { ref, nextTick } from 'vue';
const { proxy } = getCurrentInstance();
const radio = ref(1);
const props = defineProps({
  openPrescription: {
    type: Boolean,
    default: false,
  },
  cardList: {
    type: Object,
    required: true,
  },
});
const { method_code, unit_code, rate_code, distribution_category_code } = proxy.useDict(
  'method_code',
  'unit_code',
  'rate_code',
  'distribution_category_code'
);

const emit = defineEmits(
  'close',
  'openPatientDetail',
  'closePatientDetail',
  'submit',
  'openTransferToBed',
  'closeTransferToBed',
  'openTransferZk',
  'openTransferCy',
  'closeTransfer'
);
const total = ref(0);
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
});
const form = reactive({
  patientId: '', // 患者
  encounterId: '', // 就诊id
  prescriptionNo: '', // 处方号
  rxTypeCode: 1, // 处方类型编码
  validityDays: '', // 有效天数
  extensionReason: '', // 延长原因
  medicationInfoList: [],
});
// 患者详细对话框相关变量
const patientDetialDialogRef = ref();
const openPatientDetail = ref(false);
const dosageInputRefs = ref([]);
// 转床对话框相关变量
const transferToBedRef = ref();
const openTransferToBed = ref(false);
// 转科/出院对话框相关变量
const transferRef = ref();
const openTransfer = ref(false);
const title = ref('');

const rowRules = ref({
  conditionDefinitionId: [{ required: true, message: '请选择诊断', trigger: 'change' }],
  dose: [{ required: true, message: '请输入单次剂量', trigger: 'change' }],
  doseQuantity: [{ required: true, message: '请输入单次剂量', trigger: 'change' }],
  quantity: [{ required: true, message: '请输入数量', trigger: 'change' }],
  dispensePerDuration: [{ required: true, message: '请输入用药天数', trigger: 'change' }],
});
const unitMap = ref({
  dose: 'dose',
  minUnit: 'minUnit',
  unit: 'unit',
});

/**
 * 打开患者信息详细对话框
 */
function openPatientDetailDialog(row) {
  emit('openPatientDetail', row);
}

/**
 * 关闭新增处方弹窗
 */
function closePatientDetialDialog(str) {
  emit('closePatientDetail');
}
/**
 * 打开转床对话框
 */
function openTransferToBedDialog(row) {
  console.log(row, '卡片转床弹窗打开');
  emit('openTransferToBed', row);
}

/**
 * 关闭转床对话框
 */
function closeTransferToBedDialog(str) {
  emit('closeTransferToBed');
}

/**
 * 打开转科对话框
 */
function openTransferZkDialog(row) {
  emit('openTransferZk', row);
}

/**
 * 打开出院对话框
 */
function openTransferCyDialog(row) {
  emit('openTransferCy', row);
}

/**
 * 关闭转科/出院对话框
 */
function closeTransferDialog(str) {
  emit('closeTransfer');
}

function close() {
  reset();
  emit('close');
}

function reset() {
  form.medicationInfoList = [];
  form.patientId = '';
  form.encounterId = '';
  form.prescriptionNo = '';
  form.rxTypeCode = 1;
  form.validityDays = '';
  form.extensionReason = '';
}

function getBedBackColor(triageLevel) {
  const Level = triageLevel?.display ?? '0级';
  let backColor = '#808080';
  switch (Level) {
    case '1':
      backColor = this.colors[0];
      break;
    case '2':
      backColor = this.colors[1];
      break;
    case '3':
      backColor = this.colors[2];
      break;
    case '4':
      backColor = this.colors[3];
      break;
    default:
      backColor = '#C4C4C4';
  }
  return backColor;
}

function getDisplay(triageLevel) {
  return triageLevel?.display ?? '';
}
// defineExpose({
//   getPrescriptionNoInit,
// });
</script>

<style scoped>
.pf_card {
  outline-style: inherit;
  position: relative;
  border-radius: 5px;
  background-color: #ffffff;
  box-shadow: 1px 1px 4px #888888;
  border: 1px solid #e2dfdf;
  width: 100%;
  user-select: none;
  font-family: Microsoft YaHei;
  /* margin-left: 5px; */
}
.pf_card:hover {
  box-shadow: 4px 4px 16px #888888;
}

.layui-input-inline {
  display: inline-block;
}

.layui-inline {
  display: grid;
  grid-template-columns: 30% 60%;
  padding: 3% 0 3% 5%;
}

/* .box-card { */
.header {
  /* display: grid;
  grid-template-columns: 25% 75%;
  position: relative;
  height: 40px;
  grid-template-columns: 25% 75%;
  */
  border-bottom: 1px solid #d3d4d6;
  /* 可选: 设置行分隔线 */
  display: flex;
  align-items: center;
  white-space: nowrap; /* 禁止换行 */
  overflow: hidden; /* 隐藏溢出内容 */
  width: 100%; /* 根据需要设置宽度 */
  height: 36px; /* 固定高度 */
}

.label1 {
  background-color: #a61302;
  text-align: center;
  padding: 10px 5px;
  color: white;
  border-radius: 5px;
}

.label2 {
  background-color: #ec1500;
  text-align: center;
  padding: 10px 5px;
  color: white;
  border-radius: 5px;
}

.label3 {
  background-color: #10d45b;
  text-align: center;
  padding: 10px 5px;
  color: white;
  border-radius: 5px;
}

.label4 {
  background-color: #848d88;
  text-align: center;
  padding: 10px 5px;
  color: white;
  border-radius: 5px;
}

.label5 {
  text-align: center;
  padding: 10px 5px;
  border-radius: 5px;
}

.header-label {
  /* padding-left: 10px;
  padding-top: 8px;
  font-size: large;
  font-weight: bold; */
  display: inline-block;
  margin-left: 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 150px; /* 根据需要调整 */
  font-size: large;
  font-weight: bold;
}
/* } */

.card-content .row {
  font-size: 15px;
  display: grid;
  padding: 3px 0;
  /* 设置行间距 */
  border-bottom: 1px solid #e4e7ed;
  /* 可选: 设置行分隔线 */
}

.card-content .row2 {
  font-size: 15px;
  display: grid;
  padding: 3px 0;
  /* 设置行间距 */
  /* border-bottom: 2px solid #c6c7c7; */
  border-bottom: 1px solid #e4e7ed;
  /* 可选: 设置行分隔线 */
}

.card-content .row:last-child {
  font-size: 15px;
  border-bottom: none;
  /* 最后一行不显示分隔线 */
}

.exceptionalCare ::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 15px;
  height: 15px;
  border-radius: 50%;
  /* 使其成为圆形 */
  background-color: #a61302;
  /* 背景颜色 */
}

.primaryCare ::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 15px;
  height: 15px;
  border-radius: 50%;
  /* 使其成为圆形 */
  background-color: #ec1500;
  /* 背景颜色 */
}

.secondaryCare ::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 15px;
  height: 15px;
  border-radius: 50%;
  /* 使其成为圆形 */
  background-color: #10d45b;
  /* 背景颜色 */
}

.tertiaryCare ::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 15px;
  height: 15px;
  border-radius: 50%;
  /* 使其成为圆形 */
  background-color: #848d88;
  /* 背景颜色 */
}

.patientDiagnosis {
  white-space: nowrap;
  /* 不折行 */
  overflow: hidden;
  /* 隐藏溢出的内容 */
  text-overflow: ellipsis;
  /* 使用省略号表示溢出 */
  width: 150px;
  /* 设置宽度，控制显示效果 */
  display: block;
  /* 必须设置为块级元素或行内块元素 */
  margin-left: -3px;
}

.dialog-row {
  display: grid;
  grid-template-columns: 85% 15%;
  margin-left: 15px;
  margin-bottom: 2px;
}

.dialog-inline {
  display: flex;
}

.dialog-detaile-inline {
  font-size: 20px;
  display: grid;
  grid-template-columns: 40% 75%;
}

.dialog-line1 {
  text-align: right;
  margin-top: 10px;
}

.dialog-line2 {
  border-bottom: 1px solid black;
  width: 60%;
  text-align: center;
  margin-top: 10px;
}

.header-container {
  display: inline-flex;
  align-items: center;
}

.no-data {
  display: grid;
  place-items: center; /* 水平和垂直居中 */
  height: 100%; /* 确保父容器有高度 */
  width: 100%; /* 确保父容器有宽度 */
  margin-top: 20px;
}

::v-deep .el-dialog_header {
  font-size: 20px;
  /* 设置字体大小 */
  color: #ff4500;
  /* 设置字体颜色 */
}

::v-deep .el-card__body {
  padding: 0px !important;
}

.custom-row {
  display: flex;
  flex-wrap: wrap;
  margin-left: -10px !important; /* 抵消 gutter 的 margin */
  margin-right: -10px !important;
}
.custom-col {
  width: calc(20% - 20px) !important; /* 5列布局 */
  margin-left: 10px;
  margin-right: 10px;
}
</style>
