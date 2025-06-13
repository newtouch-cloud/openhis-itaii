<template>
  <el-dialog
    title="患者详情"
    v-model="props.open"
    width="1200px"
    append-to-body
    destroy-on-close
    @close="close"
  >
    <div>
      <el-form :model="form" ref="formRef" label-width="110">
        <el-row :gutter="24" class="mb8">
          <el-col :span="6">
            <el-form-item label="患者姓名：" prop="patientName">
              <el-input
                v-model="form.patientName"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="性别：" prop="genderEnum_enumText">
              <el-input
                v-model="form.genderEnum_enumText"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="住院号：" prop="hospitalNo">
              <el-input v-model="form.hospitalNo" placeholder="" clearable disabled />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="床号：" prop="locationId_dictText">
              <el-input v-model="form.locationId_dictText" placeholder="" clearable disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24" class="mb8">
          <el-col :span="6">
            <el-form-item label="年龄：" prop="ageString">
              <el-input
                v-model="form.ageString"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="生日：" prop="birthDate">
              <el-input
                v-model="form.birthDate"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="婚姻状态：" prop="maritalStatusEnum_enumText">
              <el-input
                v-model="form.maritalStatusEnum_enumText"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="职业：" prop="prfsEnum_enumText">
              <el-input
                v-model="form.prfsEnum_enumText"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24" class="mb8">
          <el-col :span="6">
            <el-form-item label="电话：" prop="phone">
              <el-input v-model="form.phone" placeholder="" clearable disabled />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="地址：" prop="addressInfo">
              <el-input
                v-model="form.addressInfo"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="民族：" prop="nationalityCode">
              <el-input
                v-model="form.nationalityCode"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="身份证号：" prop="idCard">
              <el-input
                v-model="form.idCard"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24" class="mb8">
          <el-col :span="6">
            <el-form-item label="籍贯：" prop="nativePlace">
              <el-input
                v-model="form.nativePlace"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="国家：" prop="countryCode">
              <el-input
                v-model="form.countryCode"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="联系人：" prop="linkName">
              <el-input
                v-model="form.linkName"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="联系人关系：" prop="linkRelationCode_codeText">
              <el-input
                v-model="form.linkRelationCode_codeText"
                placeholder=""
                clearable
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24" class="mb8">
          <el-col :span="6">
            <el-form-item label="联系人电话：" prop="linkTelcom">
              <el-input
                v-model="form.linkTelcom"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="其他联系人：" prop="linkJsons">
              <el-input
                v-model="form.linkJsons"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="工作单位：" prop="workCompany">
              <el-input v-model="form.workCompany" placeholder="" clearable disabled />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="护理级别：" prop="priorityEnum_enumText">
              <el-input
                v-model="form.priorityEnum_enumText"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24" class="mb8">
          <el-col :span="6">
            <el-form-item label="患者状态：" prop="statusEnum_enumText">
              <el-input
                v-model="form.statusEnum_enumText"
                placeholder=""
                clearable
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="入院科室：" prop="organizationId_dictText">
              <el-input
                v-model="form.organizationId_dictText"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="入院日期：" prop="admissionDate">
              <el-input
                v-model="form.admissionDate"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="责任医生：" prop="responsibleDoctor">
              <el-input
                v-model="form.responsibleDoctor"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24" class="mb8">
          <el-col :span="6">
            <el-form-item label="责任护士：" prop="responsibleNurse">
              <el-input v-model="form.responsibleNurse" placeholder="" clearable disabled />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="主要诊断：" prop="mainDiagnosis">
              <el-input
                v-model="form.mainDiagnosis"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="费别：" prop="typeCode_dictText">
              <el-input
                v-model="form.typeCode_dictText"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="住院天数：" prop="hospitalizationDays">
              <el-input
                v-model="form.hospitalizationDays"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24" class="mb8">
          <el-col :span="6">
            <el-form-item label="就诊类别：" prop="classEnum_enumText">
              <el-input
                v-model="form.classEnum_enumText"
                placeholder=""
                clearable
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="术后天数：" prop="postoperativeDays">
              <el-input
                v-model="form.postoperativeDays"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="手术开始日期：" prop="surgeryStartTime">
              <el-input
                v-model="form.surgeryStartTime"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="手术结束日期：" prop="surgeryEndTime">
              <el-input
                v-model="form.surgeryEndTime"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24" class="mb8">
          <el-col :span="6">
            <el-form-item label="手术状态：" prop="surgeryStatusEnum_enumText">
              <el-input
                v-model="form.surgeryStatusEnum_enumText"
                placeholder=""
                clearable
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="过敏原类别：" prop="categoryCode_dictText">
              <el-input
                v-model="form.categoryCode_dictText"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="入院科室名：" prop="caty">
              <el-input
                v-model="form.caty"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="血型ABO：" prop="bloodAbo_enumText">
              <el-input
                v-model="form.bloodAbo_enumText"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="血型RH：" prop="bloodRh_enumText">
              <el-input
                v-model="form.bloodRh_enumText"
                placeholder=""
                clearable
                style="width: 260px"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="close">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, nextTick } from 'vue';
import { listPatient } from './api';

const { proxy } = getCurrentInstance();
const props = defineProps({
  open: {
    type: Boolean,
    default: false,
  },
  patientId: {
    type: String,
    default: '',
  },
});
const { method_code, unit_code, rate_code, distribution_category_code } = proxy.useDict(
  'method_code',
  'unit_code',
  'rate_code',
  'distribution_category_code'
);

const emit = defineEmits(['close']);
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  patientId: undefined, // 患者id
});
const form = ref({});
const title = ref('');
const rowRules = ref({
  conditionDefinitionId: [{ required: true, message: '请选择诊断', trigger: 'change' }],
  dose: [{ required: true, message: '请输入单次剂量', trigger: 'change' }],
  doseQuantity: [{ required: true, message: '请输入单次剂量', trigger: 'change' }],
  quantity: [{ required: true, message: '请输入数量', trigger: 'change' }],
  dispensePerDuration: [{ required: true, message: '请输入用药天数', trigger: 'change' }],
});

/**
 * 取得患者信息详细
 */
function getPatientDetial() {
  reset();
  title.value = '';
  title.value = props.title;
  console.log(props, 'props', title.value);
  console.log(queryParams.value, 'queryParams.value', props.patientId);
  queryParams.value.patientId = props.patientId;
  listPatient(queryParams.value).then((res) => {
    if (res.data && res.data.records && res.data.records.length > 0) {
      form.value = res.data.records[0];
    }
    form.value.addressInfo =
      form.value.addressProvince +
      '' +
      form.value.addressCity +
      '' +
      form.value.addressDistrict +
      '' +
      form.value.addressStreet +
      '' +
      form.value.address;

    // loading.value = false;
    console.log(res, 'resqqqqqqqqqqqqqqqqqqqqqqq', form.value);
  });
}

function close() {
  reset();
  emit('close');
}
/** 重置操作表单 */
function reset() {
  form.value = {
    id: undefined,
    activeFlag: undefined, // 活动标记
    tempFlag: undefined, // 临时标识
    patientName: undefined, // 患者姓名
    nameJson: undefined, // 患者其他名称
    hospitalNo: undefined, // 病历号
    genderEnum: undefined, // 性别
    genderEnum_enumText: undefined, // 性别
    birthDate: undefined, // 生日
    deceasedDate: undefined, // 死亡时间
    maritalStatusEnum: undefined, // 婚姻状态
    maritalStatusEnum_enumText: undefined, // 婚姻状态
    prfsEnum: undefined, // 职业编码
    prfsEnum_enumText: undefined, // 职业编码
    phone: undefined, // 电话
    address: undefined, // 地址
    addressProvince: undefined, // 地址省
    addressCity: undefined, // 地址市
    addressDistrict: undefined, // 地址区
    addressStreet: undefined, // 地址街道
    addressJson: undefined, // 患者其他地址
    nationalityCode: undefined, // 民族
    idCard: undefined, // 身份证号
    pyStr: undefined, //拼音码
    wbStr: undefined, // 五笔码
    bloodAbo: undefined, // 血型ABO
    bloodAbo_enumText: undefined, // 血型ABO
    bloodRh: undefined, // 血型RH
    bloodRh_enumText: undefined, // 血型RH
    workCompany: undefined, // 工作单位
    nativePlace: undefined, // 籍贯
    countryCode: undefined, // 国家编码
    linkName: undefined, // 联系人
    linkRelationCode: undefined, // 联系人关系
    linkRelationCode_codeText: undefined, // 联系人关系
    linkTelcom: undefined, // 联系人电话
    linkJsons: undefined, // 其他联系人
    tenanid: undefined, // 租户ID
    ageString: undefined, // 病人年龄
    priorityEnum: undefined, // 护理级别
    priorityEnum_enumText: undefined, // 护理级别
    statusEnum: undefined, // 患者状态
    statusEnum_enumText: undefined, // 患者状态
    organizationId: undefined, // 入院科室
    organizationId_dictText: undefined, // 入院科室
    admissionDate: undefined, // 入院日期
    dischargeDate: undefined, // 出院日期
    responsibleDoctor: undefined, // 责任医生
    responsibleNurse: undefined, // 责任护士
    mainDiagnosis: undefined, // 主要诊断
    typeCode: undefined, // 费别
    typeCode_dictText: undefined, // 费别
    hospitalizationDays: undefined, //住院天数
    classEnum: undefined, // 就诊类别
    classEnum_enumText: undefined, // 就诊类别
    postoperativeDays: undefined, // 术后天数
    surgeryStartTime: undefined, // 手术开始日期
    surgeryEndTime: undefined, // 手术结束日期
    surgeryStatusEnum: undefined, // 手术状态
    surgeryStatusEnum_enumText: undefined, // 手术状态
    categoryCode: undefined, // 过敏源类别
    categoryCode_dictText: undefined, // 过敏源类别
    caty: undefined, // 入院科室名称
    locationId: undefined, // 床位号
    locationId_dictText: undefined, // 床位号
    encounterId: undefined, // 就诊流水号
    encounterLocationId: undefined, // 就诊地点流水号
  };
  proxy.resetForm('formRef');
}

defineExpose({
  getPatientDetial,
});
</script>

<style scoped>
:deep(.pagination-container .el-pagination) {
  right: 20px !important;
}
</style>