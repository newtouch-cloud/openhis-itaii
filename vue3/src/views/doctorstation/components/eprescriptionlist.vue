<template>
  <div style="width: 100%; padding-bottom: 20px">
    <div style="margin-bottom: 5px">
      <el-button type="primary" @click="handleAddPrescription()" :disabled="selectDataStatus == 2">
        新增处方
      </el-button>
      <el-button type="primary" @click="handleSave()" :disabled="false"> 签发 </el-button>
      <el-button type="danger" plain @click="deletePrescription()" :disabled="false">
        删除
      </el-button>
    </div>

    <div>处方信息</div>
    <el-table
      max-height="650"
      ref="eprescriptionRef"
      :data="prescriptionList"
      row-key="prescriptionNo"
      border
      @row-dblclick="clickPrescriptionRow"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="50" align="center" :selectable="selectable" />
      <el-table-column label="处方号" align="center" prop="prescriptionNo" width="200" sortable>
        <template #default="scope">
          <span v-if="!scope.row.isEdit">
            {{ scope.row.prescriptionNo }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="门诊号" align="center" prop="iptOtpNo">
        <template #default="scope">
          <span v-if="!scope.row.isEdit">
            {{ scope.row.iptOtpNo }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="病区" align="center" prop="departmentWard">
        <template #default="scope">
          <span v-if="!scope.row.isEdit">
            {{ scope.row.departmentWard }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="有效天数" align="center" prop="validityDays" width="80">
        <template #default="scope">
          <span v-if="!scope.row.isEdit">
            {{ scope.row.validityDays }}
          </span>
          <el-input-number
            v-else
            :min="0"
            controls-position="right"
            :controls="false"
            v-model="scope.row.validityDays"
            placeholder=""
            @input="handleValidityDaysChange(scope.$index, $event)"
            style="width: 90%"
          />
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="statusEnum_enumText" width="80">
        <template #default="scope">
          <span v-if="!scope.row.isEdit">
            {{ scope.row.statusEnum_enumText }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        label="开方医师"
        align="center"
        prop="practitionerName"
        header-align="center"
        width="90"
      >
        <template #default="scope">
          <span v-if="!scope.row.isEdit">
            {{ scope.row.practitionerName }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="处方开立日期" align="center" prop="prscTime" width="170">
        <template #default="scope">
          <span v-if="!scope.row.isEdit">
            {{ parseTime(scope.row.prscTime) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="取药状态" align="center" prop="medStatus" width="80">
        <template #default="scope">
          <span v-if="!scope.row.isEdit">
            {{ scope.row.medStatus }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="延长原因" align="center" prop="extensionReason">
        <template #default="scope">
          <span v-if="!scope.row.isEdit">
            {{ scope.row.extensionReason }}
          </span>
          <el-input v-model="scope.row.extensionReason" placeholder="" v-else />
        </template>
      </el-table-column>
      <el-table-column label="撤销原因" align="center" prop="quashReason">
        <template #default="scope">
          <span v-if="!scope.row.isEdit">
            {{ scope.row.quashReason }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="诊断" align="center" prop="conditionName">
        <template #default="scope">
          <span v-if="!scope.row.isEdit">
            {{ scope.row.conditionName }}
          </span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="处方类别" align="center" prop="rxTypeCode">
          <template #default="scope">
            <span v-if="!scope.row.isEdit">
              {{ scope.row.rxTypeCode_enumText }}
            </span>
            <el-form-item
              v-else
              :prop="'prescriptionList.' + scope.$index + '.rxTypeCode'"
              :rules="rules.rxTypeCode"
            >
              <el-select v-model="scope.row.rxTypeCode" clearable>
                <el-option
                  v-for="prescriptionType in prescriptionTypeList"
                  :key="prescriptionType.value"
                  :label="prescriptionType.label"
                  :value="prescriptionType.value"
                />
              </el-select>
            </el-form-item>
          </template>
        </el-table-column> -->
      <el-table-column label="操作" align="center" width="220" fixed="right">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleView(scope.row)">查看</el-button>
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleEdit(scope.row)"
            :disabled="scope.row.statusEnum == 2"
            >编辑</el-button
          >
          <el-button
            link
            type="primary"
            icon="Plus"
            @click="savePrescriptionData(scope.row, scope.$index)"
            :disabled="!scope.row.isEdit"
            >保存</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
      style="margin-bottom: 5px"
    />

    <eprescriptiondialog
      ref="prescriptionDialogRef"
      :openPrescription="openPrescription"
      :patient="props.patientInfo"
      :prescriptionType="prescriptionTypeList"
      :medicationInfo="medicationInfoList"
      :prescriptionData="prescriptionInfo"
      :title="title"
      @close="closePrescriptionDialog"
    />
  </div>
</template>

<script setup>
import {
  elepPrescriptionInit,
  getEncounterDiagnosis,
  getPrescriptionList,
  getOrgTree,
  getPrescriptionInfo,
  updatePrescriptionInfo,
  getMedicationInfo,
  deletePrescriptionInfo,
  issuancePrescription,
} from './api';
import eprescriptiondialog from './eprescriptiondialog.vue';
import { getCurrentInstance } from 'vue';

const total = ref(0);
const queryParams = ref({
  pageNo: 1,
  pageSize: 10,
  patientId: undefined, // 门诊号/姓名
});
const totalMedication = ref(0);
const queryMedicationParams = ref({
  pageNo: 1,
  pageSize: 10,
  prescriptionNo: undefined, // 处方号
});
const prescriptionNoTemp = ref(undefined);
const dateRange = ref([]);
const openPrescription = ref(false);
const prescriptionList = ref([]);
const selectDataStatus = ref(0); // 选中数据的状态
const medicationInfoList = ref([]);
const form = ref({});
const eprescriptionFormRef = ref(null);
const title = ref('');
const prescriptionform = ref({});

const selectCurData = ref({});
const selectDataList = ref({});
const rowIndex = ref(-1);
const nextId = ref(1);
const unitCodeList = ref([]);
const organization = ref([]);
const clickTimer = ref(null);
const prescriptionDialogRef = ref();
// const rules = ref({
//   // validityDays: [{ required: true, message: '有效天数不能为空', trigger: 'blur' }],
//   // extensionReason: [{ required: true, message: '延长原因不能为空', trigger: 'blur' }],
// });
const rules = ref({
  validityDays: [
    { required: true, message: '有效天数不能为空', trigger: 'blur' },
    { type: 'number', message: '必须为数字值', transform: (value) => Number(value) },
    { type: 'number', min: 1, max: 30, message: '有效天数应在1-30之间' },
  ],
  extensionReason: [
    { required: true, message: '延长原因不能为空', trigger: 'blur' },
    { min: 5, max: 100, message: '长度在5到100个字符' },
  ],
});
const unitMap = ref({
  dose: 'dose',
  minUnit: 'minUnit',
  unit: 'unit',
});
const buttonDisabled = computed(() => {
  return !props.patientInfo;
});
const props = defineProps({
  patientInfo: {
    type: Object,
    required: true,
  },
});
const isAdding = ref(false);
const eprescriptionRef = ref();
const prescriptionInfo = ref({});
const prescriptionTypeList = ref([]);
const { proxy } = getCurrentInstance();
const { method_code, unit_code, rate_code, distribution_category_code } = proxy.useDict(
  'method_code',
  'unit_code',
  'rate_code',
  'distribution_category_code'
);

const ids = ref([]); // 存储选择的药品信息行数据
const prescriptionNos = ref([]); // 存储选择的处方行数据
const single = ref(true);
const multiple = ref(true);

const handleValidityDaysChange = (index, value) => {
  // 确保存储的是数字类型
  prescriptionList.value[index].validityDays = Number(value) || null;
  // 手动触发验证
  nextTick(() => {
    eprescriptionFormRef.value.validateField(`prescriptionList.${index}.validityDays`);
  });
};

// getList();
getElepPrescriptionInit();

/** 处方信息取得 */
function getList() {
  console.log(
    queryParams.value,
    'queryParams.value电子处方',
    props.patientInfo,
    'props.patientInfo'
  );
  prescriptionList.value = [];
  medicationInfoList.value = [];
  prescriptionNoTemp.value = undefined;
  queryParams.value.patientId = props.patientInfo.patientId;
  console.log(queryParams.value, 'queryParams.value电子处方');
  getPrescriptionInfo(queryParams.value).then((res) => {
    prescriptionList.value = res.data.records;
    console.log(res, '电子处方列表');
    total.value = res.data.total;
  });
}

/** 电子处方下拉选取得 */
function getElepPrescriptionInit() {
  elepPrescriptionInit().then((res) => {
    prescriptionTypeList.value = res.data.rxTypeCodeListOptions;
    console.log(res, '电子处方下拉框');
  });
}

/** 选择条数  */
function handleSelectionChange(selection) {
  console.log(selection, 'selection');
  selectDataList.value = selection.map((item) => ({ ...item })); // 存储选择的行数据
  prescriptionNos.value = selection.map((item) => item.prescriptionNo);
  ids.value = selection
    .filter((item) => item.id != null && item.id !== '') // 筛选 id 不为空的项
    .map((item) => item.id); // 提取 id 值
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

function getRowDisabled(row) {
  return row.isEdit;
}

/**
 * 新增处方按钮操作
 */
function handleAddPrescription() {
  console.log('handleAddPrescription新增处方按钮操作', prescriptionNoTemp.value);
  title.value = '新增处方';
  openPrescriptionDialog();
}

/**
 * @description 判断某行是否可选
 */
function selectable(row, index) {
  // 返回 true 表示该行可选，返回 false 表示该行不可选
  // console.log(row, 'selectable', rowIndex.value);
  return row.statusEnum !== 2;
}

/**
 * 单击行显示处方详细的药品信息
 */
function handleView(row) {
  // reset();
  title.value = '查看处方';
  open.value = true;

  queryMedicationParams.value.prescriptionNo = row.prescriptionNo;
  prescriptionNoTemp.value = JSON.stringify(row.prescriptionNo);
  prescriptionInfo.value.validityDays = row.validityDays;
  prescriptionInfo.value.extensionReason = row.extensionReason;
  prescriptionInfo.value.rxTypeCode = row.rxTypeCode;
  prescriptionInfo.value.prescriptionNo = row.prescriptionNo;
  console.log(queryMedicationParams.value, '处方详细的药品信息参数', row.prescriptionNo);
  getMedicationInfo(queryMedicationParams.value).then((response) => {
    medicationInfoList.value = response.data.records;
    medicationInfoList.value.forEach((medicationInfo) => {
      medicationInfo.isEdit = false;
    });
  
      openPrescriptionDialog();

    console.log(response, '处方详细的药品信息', medicationInfoList.value);
  });
}

/**
 * 单击行显示处方详细的药品信息
 */
function handleEdit(row) {
  title.value = '编辑处方';
  open.value = true;
  queryMedicationParams.value.prescriptionNo = row.prescriptionNo;
  prescriptionNoTemp.value = JSON.stringify(row.prescriptionNo);
  prescriptionInfo.value.validityDays = row.validityDays;
  prescriptionInfo.value.extensionReason = row.extensionReason;
  prescriptionInfo.value.rxTypeCode = row.rxTypeCode;
  prescriptionInfo.value.prescriptionNo = row.prescriptionNo;
  console.log(queryMedicationParams.value, '处方详细的药品信息参数', row.prescriptionNo);
  getMedicationInfo(queryMedicationParams.value).then((response) => {
    medicationInfoList.value = response.data.records;
    medicationInfoList.value.forEach((medicationInfo) => {
      medicationInfo.isEdit = false;
    });
    openPrescriptionDialog();
    console.log(response, '处方详细的药品信息', medicationInfoList.value);
  });
}

/**
 * 双击处方行
 */
function clickPrescriptionRow(row) {
  clearTimeout(clickTimer.value);
  if (row.statusEnum === 2) {
    row.isEdit = false;
    proxy.$modal.msgWarning('当前处方已签发，不可编辑');
    return;
  } else {
    row.isEdit = true;
  }
}

/**
 * 打开新增处方弹窗
 */
function openPrescriptionDialog() {
  openPrescription.value = true;
  nextTick(() => {
    proxy.$refs['prescriptionDialogRef'].getPrescriptionNoInit();
  });
  console.log(openPrescription.value, '打开新增处方弹窗');
}

/**
 * 关闭新增处方弹窗
 */
function closePrescriptionDialog(str) {
  if (str === 'success') {
    // getList();
    proxy.$modal.msgSuccess('操作成功');
  }
  getList();
  openPrescription.value = false;
}

/**
 * 保存处方
 */
async function savePrescriptionData(row, index) {
  // try {
  //   // 验证特定字段
  //   await eprescriptionFormRef.value.validateField([
  //     `prescriptionList.${index}.validityDays`,
  //     `prescriptionList.${index}.extensionReason`,
  //   ]);
  if (!row.validityDays) {
    proxy.$modal.msgWarning('请填写有效期天数！');
    return;
  }
  // 校验通过
  const updateParam = {
    prescriptionNo: row.prescriptionNo,
    validityDays: row.validityDays,
    extensionReason: row.extensionReason,
    rxTypeCode: row.rxTypeCode,
  };

  console.log(updateParam, ' 保存处方updateParam');
  updatePrescriptionInfo(updateParam).then((response) => {
    if (response.code == 200) {
      console.log(response, '保存成功');
      proxy.$modal.msgSuccess('保存成功');
      getList();
    } else {
      proxy.$modal.console.error(response.msg);
    }
  });
  // } catch (error) {
  //   console.log('验证失败:', error);
  //   // 验证失败，不执行保存
  // }
}
/**
 * 签发处方
 */
function handleSave() {
  if (prescriptionNos.value.length == 0) {
    proxy.$modal.msgWarning('请选择想要签发的处方');
    return;
  }
  const checkList = selectDataList.value
    .filter((item) => item.statusEnum === 2) // 筛选 id 不为空的项
    .map((item) => ({ ...item })); //
  if (checkList.length > 0) {
    proxy.$modal.msgWarning('选择的处方中包含已签发处方');
    return;
  }
  const prescriptionNoList = prescriptionNos.value;
  issuancePrescription(prescriptionNoList).then((res) => {
    if (res.code === 200) {
      proxy.$modal.msgSuccess('签发成功');
      getList();
      // nextId.value == 1;
    }
  });
}

// 删除处方

/**
 * 删除处方/药品信息
 *
 * @param index - 要删除的处方在列表中的索引
 */
function deletePrescription(index) {
  const prescriptionNo = prescriptionNoTemp.value;
  if (ids.value.length == 0 && prescriptionNos.value.length == 0) {
    proxy.$modal.msgWarning('请选择要删除的数据信息！');
    return;
  }
  const data = {
    idList: ids.value,
    prescriptionNoList: prescriptionNos.value,
  };
  console.log('deletePrescription删除', data);
  proxy.$modal
    .confirm('是否确认删除以上数据！')
    .then(function () {
      return deletePrescriptionInfo(data);
    })
    .then(() => {
      if (prescriptionNos.value.length > 0) {
        getList();
      }
      // if (ids.value.length > 0) {
      //   queryMedicationParams.value.prescriptionNo = prescriptionNo;
      //   getMedicationInfo(queryMedicationParams.value).then((response) => {
      //     form.value.medicationInfoList = response.data.records;
      //     form.value.medicationInfoList.forEach((medicationInfo) => {
      //       medicationInfo.isEdit = false;
      //     });
      //     console.log(response, '处方详细的药品信息', form.value.medicationInfoList);
      //     totalMedication.value = response.data.total;
      //   });
      // }
      proxy.$modal.msgSuccess('删除成功');
    })
    .catch(() => {});
}

defineExpose({ getList });
</script>

<style lang="scss" scoped>
:deep(.el-table__expand-icon) {
  display: none !important;
}

.medicine-title {
  font-size: 16px;
  font-weight: 600;
  min-width: 280px;
  display: inline-block;
}

.total-amount {
  font-size: 16px;
  font-weight: 600;
  color: #409eff;
  white-space: nowrap;
}

.medicine-info {
  font-size: 15px;
  font-weight: 600;
  color: #606266;
  white-space: nowrap;
}

.form-group {
  display: flex;
  align-items: center;
  gap: 8px;
  background: #fff;
  padding: 6px 10px;
  border-radius: 4px;
  border: 1px solid #ebeef5;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.04);
}

/* 调整element组件默认间距 */
// .el-select,
// .el-input-number {
//   margin-right: 0 !important;
// }

.el-input-number .el-input__inner {
  text-align: center;
}

.el-table__cell .el-form-item--default {
  margin-bottom: 0px;
}

.el-table {
  overflow: visible !important;
  /* 允许内容溢出 */
}

.el-table__body-wrapper {
  overflow: visible !important;
}
</style>