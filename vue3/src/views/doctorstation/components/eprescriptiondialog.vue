<template>
  <el-dialog
    :title="title"
    v-model="props.openPrescription"
    width="1840px"
    append-to-body
    destroy-on-close
    @close="close"
  >
    <div>
      <el-form :model="infoForm" ref="infoFormRef" :rules="rules">
        <el-row :gutter="24" class="mb8">
          <el-col :span="12">
            <el-form-item label="患者ID：" prop="patientId" label-width="100">
              <el-input
                v-model="infoForm.patientId"
                placeholder="患者ID"
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="就诊ID：" prop="encounterId" label-width="100px">
              <el-input
                v-model="infoForm.encounterId"
                placeholder="就诊ID"
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- <el-row :gutter="24" class="mb8">
          <el-col :span="12">
            <el-form-item label="处方号：" prop="prescriptionNo" label-width="100px">
              <el-input
                v-model="form.prescriptionNo"
                placeholder="处方号"
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="处方类别：" prop="rxTypeCode" label-width="100px">
              <el-select v-model="form.rxTypeCode" clearable disabled style="width: 35%">
                <el-option
                  v-for="prescriptionType in prescriptionTypeList"
                  :key="prescriptionType.value"
                  :label="prescriptionType.label"
                  :value="prescriptionType.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row> -->
        <el-row :gutter="24" class="mb8">
          <el-col :span="12">
            <el-form-item label="处方号：" prop="prescriptionNo" label-width="100px">
              <el-input v-model="infoForm.prescriptionNo" clearable style="width: 260px" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="有效天数：" prop="validityDays" label-width="100px">
              <el-input-number
                :min="0"
                controls-position="right"
                :controls="false"
                v-model.number="infoForm.validityDays"
                clearable
                style="width: 260px"
                :disabled="title == '查看处方'"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="延长原因：" prop="extensionReason" label-width="100px">
              <el-input
                v-model="form.extensionReason"
                clearable
                style="width: 260px"
                :disabled="title == '查看处方'"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-form :model="form" ref="formRef" :rules="rowRules">
        <div style="margin-bottom: 5px" v-if="title != '查看处方'">
          <el-button type="primary" @click="handleAddPrescription()"> 新增 </el-button>
        </div>
        <el-table
          max-height="650"
          ref="prescriptionRef"
          :data="form.medicationInfoList"
          row-key="id"
          border
          @row-dblclick="clickMedicineRowDb"
          @selection-change="handleSelectionChange"
          :selectable="selectable"
        >
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="药品名称" align="center" prop="medicationName" width="150">
            <template #default="scope">
              <template v-if="getRowDisabled(scope.row)">
                <el-form-item :prop="`medicationInfoList.${scope.$index}.medicationName`">
                  <el-popover
                    :popper-style="{ padding: '0' }"
                    placement="bottom-start"
                    :visible="scope.row.showPopover"
                    trigger="manual"
                    :width="1200"
                  >
                    <prescriptionMedicineList
                      :searchKey="medicineSearchKey"
                      @selectRow="(row) => selectMedRow(scope.row.uniqueKey, row, scope.$index)"
                    />
                    <template #reference>
                      <el-input
                        style="width: 96%"
                        v-model="scope.row.medicationName"
                        @input="handleChange"
                        @focus="handleFocus(scope.row, scope.$index)"
                      />
                      <!-- @blur="handleBlur(scope.row)" -->
                    </template>
                  </el-popover>
                </el-form-item>
              </template>
              <span v-else>{{ scope.row.medicationName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="药品规格" align="center" prop="" width="100">
            <template #default="scope">
              <el-form-item :prop="`medicationInfoList.${scope.$index}.drugSpecification`">
                <span v-if="!scope.row.isEdit">
                  {{ scope.row.drugSpecification }}
                </span>
                <el-input v-else v-model="scope.row.drugSpecification" placeholder="" disabled />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="生产厂家" align="center" prop="" width="220">
            <template #default="scope">
              <el-form-item :prop="`medicationInfoList.${scope.$index}.manufacturerName`">
                <el-tooltip :content="scope.row.manufacturerName" placement="top" effect="dark">
                  <span v-if="!scope.row.isEdit">
                    {{ scope.row.manufacturerName }}
                  </span>
                  <el-input v-else v-model="scope.row.manufacturerName" placeholder="" disabled />
                </el-tooltip>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="药品剂量" align="center" prop="" width="80">
            <template #default="scope">
              <el-form-item
                :prop="`medicationInfoList.${scope.$index}.medDosage`"
                :rules="rowRules.medDosage"
              >
                <span v-if="!scope.row.isEdit">
                  {{ scope.row.medDosage ? formatNumber(scope.row.medDosage) : '' }}
                </span>
                <el-input
                  v-else
                  v-model="scope.row.medDosage"
                  placeholder=""
                  :ref="(el) => setDosageInputRef(el, scope.$index)"
                />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="剂量单位" align="center" prop="medDosageUnitCode" width="80">
            <template #default="scope">
              <el-form-item :prop="`medicationInfoList.${scope.$index}.dose`">
                <span v-if="!scope.row.isEdit">
                  {{ scope.row.medDosageUnitCode }}
                </span>
                <!-- <el-select v-else v-model="scope.row.medDosageUnitCode" clearable disabled>
                  <el-option v-for="category in unit_code" :key="category.value" :label="category.label"
                    :value="category.value" />
                </el-select> -->
                <el-input v-else v-model="scope.row.medDosageUnitCode" placeholder="" disabled />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="使用频次" align="center" prop="medFrequency" width="230">
            <template #default="scope">
              <el-form-item
                :prop="`medicationInfoList.${scope.$index}.medFrequency`"
                :rules="rowRules.medFrequency"
              >
                <span v-if="!scope.row.isEdit" style="text-align: right">
                  {{
                    scope.row.medFrequency_dictText
                      ? scope.row.medFrequency_dictText +
                        ' ' +
                        scope.row.dispensePerDuration +
                        '天' +
                        ' ' +
                        scope.row.medRoute_dictText
                      : ''
                  }}
                </span>
                <el-select v-else v-model="scope.row.medFrequency" clearable style="width: 200px">
                  <el-option
                    v-for="category in elep_rate_code"
                    :key="category.value"
                    :label="category.label"
                    :value="category.value"
                  />
                </el-select>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column
            label="服药时间(开始)"
            align="center"
            prop="effectiveDoseStart"
            width="220"
          >
            <template #default="scope">
              <el-form-item
                :prop="`medicationInfoList.${scope.$index}.effectiveDoseStart`"
                :rules="rowRules.effectiveDoseStart"
              >
                <span v-if="!scope.row.isEdit">{{ parseTime(scope.row.effectiveDoseStart) }}</span>
                <el-date-picker
                  v-else
                  v-model="scope.row.effectiveDoseStart"
                  type="datetime"
                  placeholder="选择日期"
                  format="YYYY-MM-DD HH:mm:ss"
                  value-format="YYYY-MM-DD HH:mm:ss"
                  style="width: 90%"
                  @input="calculateEndDate(scope.$index)"
                />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column
            label="每次发药供应天数"
            align="center"
            prop="dispensePerDuration"
            width="130"
          >
            <template #default="scope">
              <el-form-item
                :prop="`medicationInfoList.${scope.$index}.dispensePerDuration`"
                :rules="rowRules.dispensePerDuration"
              >
                <span v-if="!scope.row.isEdit">{{ scope.row.dispensePerDuration }}</span>
                <el-input
                  v-else
                  v-model="scope.row.dispensePerDuration"
                  clearable
                  @input="calculateEndDate(scope.$index)"
                />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column
            label="服药时间(结束)"
            align="center"
            prop="effectiveDoseEnd"
            width="220"
          >
            <template #default="scope">
              <el-form-item :prop="`medicationInfoList.${scope.$index}.effectiveDoseEnd`">
                <span v-if="!scope.row.isEdit">{{ parseTime(scope.row.effectiveDoseEnd) }}</span>
                <el-date-picker
                  v-else
                  v-model="scope.row.effectiveDoseEnd"
                  type="datetime"
                  placeholder="选择日期"
                  format="YYYY-MM-DD HH:mm:ss"
                  value-format="YYYY-MM-DD HH:mm:ss"
                  disabled
                  style="width: 90%"
                />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="途径" align="center" prop="medRoute" width="140">
            <template #default="scope">
              <el-form-item
                :prop="`medicationInfoList.${scope.$index}.medRoute`"
                :rules="rowRules.medRoute"
              >
                <span v-if="!scope.row.isEdit">
                  {{ scope.row.medRoute_dictText }}
                </span>
                <el-select v-else v-model="scope.row.medRoute" clearable style="width: 110px">
                  <el-option
                    v-for="dict in method_code"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                </el-select>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="数量" align="center" prop="quantity">
            <template #default="scope">
              <el-form-item
                :prop="`medicationInfoList.${scope.$index}.quantity`"
                :rules="rowRules.quantity"
              >
                <span v-if="!scope.row.isEdit">
                  {{ scope.row.quantity ? scope.row.quantity : '' }}
                </span>
                <el-input v-else v-model="scope.row.quantity" placeholder="" clearable />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="单位" align="center" prop="unitCode" width="80">
            <template #default="scope">
              <el-form-item :prop="`medicationInfoList.${scope.$index}.dose`">
                <span v-if="!scope.row.isEdit">
                  {{ scope.row.unitCode }}
                </span>
                <!-- <el-select v-else v-model="scope.row.medDosageUnitCode" clearable disabled>
                  <el-option v-for="category in unit_code" :key="category.value" :label="category.label"
                    :value="category.value" />
                </el-select> -->
                <el-input v-else v-model="scope.row.unitCode" placeholder="" disabled />
              </el-form-item>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="submit" v-if="title != '查看处方'">保存</el-button>
        <el-button type="danger" plain @click="deletePrescription()" v-if="title != '查看处方'">
          删除
        </el-button>
        <el-button @click="close">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { computed } from 'vue';
// import { calculateQuantityByDays, formatNumber } from '@/utils/his';
import { ref, nextTick, reactive, getCurrentInstance, defineProps, defineEmits } from 'vue';
// import { useModal, useDict } from '@/hooks';
import { parseTime, formatNumber } from '@/utils/his';
import { queryYbCatalogue } from './api';

import {
  prescriptionNoInit,
  savePrescriptionInfo,
  getOrgTree,
  updatePrescriptionInfo,
  deletePrescriptionInfo,
  getMedicationInfo,
} from './api';
import prescriptionMedicineList from './prescriptionMedicineList';
const { proxy } = getCurrentInstance();
const radio = ref(1);
const props = defineProps({
  openPrescription: {
    type: Boolean,
    default: false,
  },
  patient: {
    type: Object,
    required: true,
  },
  prescriptionType: {
    type: Object,
    required: true,
  },
  title: {
    type: String,
    required: false,
  },
  medicationInfo: {
    type: Object,
    required: true,
  },
  prescriptionData: {
    type: Object,
    required: true,
  },
});
const { method_code, unit_code, elep_rate_code, distribution_category_code } = proxy.useDict(
  'method_code',
  'unit_code',
  'elep_rate_code',
  'distribution_category_code'
);
const ids = ref([]); // 存储选择的药品信息行数据
const selectData = ref([]); // 存储选择的药品信息行数据
const single = ref(true);
const multiple = ref(true);
const emit = defineEmits(['close']);
const total = ref(0);
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
});
const rowIndex = ref(-1);
const medicineSearchKey = ref('');
const selectRow = ref({});
const prescriptionTypeList = ref([]);
const form = reactive({
  medicationInfoList: [],
});

const infoForm = reactive({
  patientId: '', // 患者
  encounterId: '', // 就诊id
  prescriptionNo: '', // 处方号
  rxTypeCode: 1, // 处方类型编码
  validityDays: 0, // 有效天数
  extensionReason: '', // 延长原因
});
const formRef = ref(null);
const data = reactive({
  isEdit: false,
  isAdding: true,
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    searchKey: undefined, // 供应商名称
    busNo: undefined, // 编码
    statusEnum: undefined, // 状态
    supplierId: undefined, // 供应商ID
    applyTimeStart: undefined, // 申请时间开始
    practitionerId: undefined, // 经手人ID
  },
  rules: {
    practitionerId: [{ required: true, message: '请选择经手人', trigger: 'change' }],
    supplierId: [{ required: true, message: '请选择供应商', trigger: 'change' }],
    purposeTypeEnum: [{ required: true, message: '请选择仓库类型', trigger: 'change' }],
    medicationType: [{ required: true, message: '请选择药品类型', trigger: 'change' }],
  },
  rowRules: {
    medDosage: [{ required: true, message: '药品剂量不能为空', trigger: 'blur' }],
    medFrequency: [{ required: true, message: '使用频次不能为空', trigger: 'change' }],
    effectiveDoseStart: [{ required: true, message: '服药时间(开始)不能为空', trigger: 'blur' }],
    dispensePerDuration: [{ required: true, message: '每次发药供应天数不能为空', trigger: 'blur' }],
    medRoute: [{ required: true, message: '途径不能为空', trigger: 'blur' }],
    quantity: [{ required: true, message: '数量不能为空', trigger: 'blur' }],
  },
});

const { rules, rowRules } = toRefs(data);
const prescriptionInfo = ref({});
const totalMedication = ref(0);
const queryMedicationParams = ref({
  pageNo: 1,
  pageSize: 10,
  prescriptionNo: undefined, // 处方号
});

const dosageInputRefs = ref([]);

const title = ref('');

const unitMap = ref({
  dose: 'dose',
  minUnit: 'minUnit',
  unit: 'unit',
});

/**
 * 取得处方号
 */
function getPrescriptionNoInit() {
  reset();
  title.value = '';
  title.value = props.title;
  console.log(props, 'props', title.value);
  prescriptionTypeList.value = props.prescriptionType;
  console.log(prescriptionTypeList.value, 'prescriptionTypeList');
  infoForm.patientId = props.patient.patientId;
  infoForm.encounterId = props.patient.encounterId;
  infoForm.validityDays = props.prescriptionData.validityDays;
  infoForm.extensionReason = props.prescriptionData.extensionReason;
  if (title.value != '新增处方') {
    form.rxTypeCode = props.prescriptionData.rxTypeCode;
    infoForm.prescriptionNo = props.prescriptionData.prescriptionNo;
  }
  console.log(infoForm.prescriptionNo, 'infoForm.prescriptionNo', props.prescriptionData);
  // prescriptionInfo.value = props.prescriptionData;
  if (title.value === '新增处方') {
    prescriptionNoInit().then((res) => {
      infoForm.prescriptionNo = res.data;
      console.log(props, 'props', res, 'res', 'form', form.value);
    });
  }
  form.medicationInfoList = props.medicationInfo;
}

/**
 * 计算用药信息的结束日期
 *
 * @param index 用药信息在数组中的索引
 */
const calculateEndDate = (index) => {
  const item = form.medicationInfoList[index];
  if (item.effectiveDoseStart && item.dispensePerDuration) {
    const startDate = new Date(item.effectiveDoseStart);
    const duration = parseInt(item.dispensePerDuration, 10);
    const endDate = new Date(startDate);
    endDate.setDate(startDate.getDate() + duration);

    // 使用本地时间格式化，而不是ISO格式
    item.effectiveDoseEnd = formatLocalDateTime(endDate);
  }
};
// 辅助函数：格式化本地时间为YYYY-MM-DD HH:mm:ss
function formatLocalDateTime(date) {
  const pad = (num) => num.toString().padStart(2, '0');
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(
    date.getHours()
  )}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
}

/**
 * 新增处方按钮操作
 */
function handleAddPrescription() {
  console.log('handleAddPrescription新增处方按钮操作');
  if (form.medicationInfoList.length == 5) {
    proxy.$modal.msgWarning('一张处方最多包含5种药！');
    return;
  }
  form.medicationInfoList.unshift({
    // rowKey: nextId.value++,
    showPopover: false,
    check: false,
    isEdit: true,
    statusEnum: 1,
    disabled: true,
  });
  // selectable();
}

function getRowDisabled(row) {
  return row.isEdit;
}

function handleFocus(row, index) {
  rowIndex.value = index;
  row.showPopover = true;
}

function handleChange(value) {
  // adviceQueryParams.value.searchKey = value;
  medicineSearchKey.value = value;
  // medicationId
}

function handleBlur(row) {
  // 只有非选择操作时才关闭
  if (!row.medicationName) {
    row.showPopover = false;
  }
}

function setDosageInputRef(el, index) {
  dosageInputRefs.value[index] = el;
}
/**
 * 选择药品
 */
function selectMedRow(key, row, index) {
  const data = {
    ...form.medicationInfoList[rowIndex.value],
    ...JSON.parse(JSON.stringify(row)),
  };
  form.medicationInfoList[rowIndex.value] = data;
  form.medicationInfoList[rowIndex.value].medicationId = row.medicalCatalogCode;
  form.medicationInfoList[rowIndex.value].medicationName = row.registeredName;
  form.medicationInfoList[rowIndex.value].unitCode = row.minPackageUnit;
  form.medicationInfoList[rowIndex.value].medDosageUnitCode = row.minPreparationUnit;
  proxy.$nextTick(() => {
    form.medicationInfoList[rowIndex.value].showPopover = false;
    const param = {
      hilistCode: row.medicalCatalogCode,
      dateStr: '2020-01-01',
    };
    // 医保目录信息查询
    queryYbCatalogue(param).then((res) => {
      const list = res.data;

      // 有效标志为0的列表
      // const valiFlagList = list.filter((item) => item.valiFlag == '0');
      // 按开始日期升序排序，取第一个元素作为结果
      const item = list.sort((a, b) => {
        return new Date(b.begndate) - new Date(a.begndate);
      });
      //debugger
      if (item.length > 0) {
        form.medicationInfoList[rowIndex.value].rxTypeCode = item[0].medChrgitmType;
      }
      console.log(res, 'queryYbCatalogue医保目录信息查询');
      // 处方类别为医保信息的医疗收费项目类别

      // const unitMap = res.data.unitMap;
      // form.medicationInfoList[rowIndex.value].minUnit = unitMap['minUnit']; // 最小包装单位
      // form.medicationInfoList[rowIndex.value].dose = unitMap['dose']; // 剂量单位
      // form.medicationInfoList[rowIndex.value].unit = unitMap['unit']; // 单位
    });
  });

  form.medicationInfoList[rowIndex.value].showPopover = false;
  // 聚焦到药品剂量输入框
  setTimeout(() => {
    if (dosageInputRefs.value[index]) {
      dosageInputRefs.value[index].focus();
      dosageInputRefs.value[index].select(); // 可选：选中文本
    }
  }, 50); // 小延迟确保DOM完全更新
}

/**
 * 保存处方
 */
function submit() {
  if (!infoForm.validityDays) {
    proxy.$modal.msgWarning('请填写有效期天数！');
    return;
  }
  console.log(form, 'form, prescriptionList.value新增处方');
  if (form.medicationInfoList.length == 0) {
    proxy.$modal.msgWarning('请选择药品！');
    return;
  }
  // console.log(row, ' 保存处方row 1234567890');
  // 新增的药品信息调用新增接口
  formRef.value.validate((valid, fields) => {
    console.log('验证结果:', valid);
    console.log('失败字段:', fields);
    if (valid) {
      console.log(' 新增处方updateParam');
      const updateParam = {
        patientId: infoForm.patientId,
        encounterId: infoForm.encounterId,
        prescriptionNo: infoForm.prescriptionNo,
        // rxTypeCode: infoForm.rxTypeCode,
        validityDays: infoForm.validityDays,
        extensionReason: infoForm.extensionReason,
        medicationInfoList: form.medicationInfoList,
      };
      savePrescriptionInfo(updateParam).then((res) => {
        if (res.code == 200) {
          emit('close', 'success');
          reset();
        }
      });
    }
  });
}

/**
 * 双击药品行
 */
function clickMedicineRowDb(row) {
  if (title.value == '查看处方') {
    row.isEdit = false;
  } else {
    row.isEdit = true;
  }
}

/** 选择条数  */
function handleSelectionChange(selection) {
  console.log(selection, 'selection');
  selectData.value = selection.map((item) => ({ ...item })); // 存储选择的行数据
  // prescriptionNos.value = selection.map((item) => item.prescriptionNo);
  ids.value = selection
    .filter((item) => item.id != null && item.id !== '') // 筛选 id 不为空的项
    .map((item) => item.id); // 提取 id 值
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/**
 * @description 判断某行是否可选
 */
function selectable(row, index) {
  // 返回 true 表示该行可选，返回 false 表示该行不可选
  console.log(row, 'selectable', rowIndex.value);
  return row.id == '' || row.id == null || row.id === undefined ? false : true;
}

/**
 * 删除处方/药品信息
 *
 * @param index - 要删除的处方在列表中的索引
 */
function deletePrescription(index) {
  console.log(ids.value.length, 'ids.value', form.medicationInfoList);

  if (selectData.value.length == 0) {
    console.log('eeeeeeeeeeeeeeeeeeeeee');
    proxy.$modal.msgWarning('请选择要删除的数据信息！');
    return;
  }
  // 药品信息有值，但是ids没有值时，说明是新增的药品信息，直接过滤掉即可。
  if (selectData.value.length > 0 && ids.value.length == 0) {
    console.log('=======================');
    proxy.$modal
      .confirm('是否确认删除以上数据！')
      .then(function () {
        form.medicationInfoList = form.medicationInfoList.filter(
          (item) => item.id != '' && item.id !== null && item.id !== undefined
        );
      })
      .catch(() => {});
    return;
  }
  const data = {
    idList: ids.value,
    prescriptionNoList: [],
  };
  console.log('deletePrescription删除', data);
  proxy.$modal
    .confirm('是否确认删除以上数据！')
    .then(function () {
      form.medicationInfoList = form.medicationInfoList.filter(
        (item) => item.id != '' && item.id !== null && item.id !== undefined
      );
      console.log('rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr');
      return deletePrescriptionInfo(data);
    })
    .then(() => {
      // if (prescriptionNos.value.length > 0) {
      //   getList();
      // }
      queryMedicationParams.value.prescriptionNo = infoForm.prescriptionNo;
      console.log(
        queryMedicationParams.value.prescriptionNo,
        'rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrdddddddddddddddddddwwwwwwwwwwwwwww'
      );
      getMedicationInfo(queryMedicationParams.value).then((response) => {
        console.log('shanchuhouchaxun', response);
        form.medicationInfoList = response.data.records;
        form.medicationInfoList.forEach((medicationInfo) => {
          medicationInfo.isEdit = false;
        });
        console.log(response, '处方详细的药品信息', form.medicationInfoList);
      });
      // if (res.code == 200) {
      //   emit('close', 'success');
      //   reset();
      // }
      proxy.$modal.msgSuccess('删除成功');
    })
    .catch(() => {});
}

function close() {
  reset();
  emit('close');
}

function reset() {
  form.medicationInfoList = [];
  infoForm.patientId = '';
  infoForm.encounterId = '';
  infoForm.prescriptionNo = '';
  infoForm.rxTypeCode = 1;
  infoForm.validityDays = 0;
  infoForm.extensionReason = '';
}

defineExpose({
  getPrescriptionNoInit,
});
</script>

<style scoped>
:deep(.pagination-container .el-pagination) {
  right: 20px !important;
}

::v-deep .el-table .cell {
  display: flex;
  align-items: center; /* 垂直居中对齐 */
}

::v-deep .el-table td {
  vertical-align: middle !important;
}
</style>