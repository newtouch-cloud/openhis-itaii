<template>
  <div class="registerForm-container">
    <div class="operate">
      <div>住院信息</div>
    </div>
    <el-form
      class="register-from"
      :model="submitForm"
      style="padding-left: 8px"
      ref="registerRef"
      label-width="100px"
      :rules="rules"
    >
      <el-row :gutter="8">
        <el-col :span="6">
          <el-form-item label="住院号:" prop="inPatientId">
            {{ submitForm.inPatientId ? submitForm.inPatientId : '-' }}
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="住院次数:" prop="hospitalizationCount" autocomplete="off">
            {{ submitForm.hospitalizationCount ? submitForm.hospitalizationCount : '-' }}
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="入院科室" prop="organizationId">
            <el-tree-select
              clearable
              style="width: 100%"
              v-model="submitForm.organizationId"
              filterable
              :data="organization"
              :props="{
                value: 'id',
                label: 'name',
                children: 'children',
              }"
              value-key="id"
              check-strictly
              :check-strictly-except-leaf="false"
              :default-expand-all="true"
              placeholder="请选择上级科室/病区/床位"
              @change="handleChange"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="入院病区" prop="wardLocationId">
            <el-select v-model="submitForm.wardLocationId">
              <el-option
                v-for="item in wardListOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="床位数:" prop="bedCount">
            {{ submitForm.bedCount ? submitForm.bedCount : '-' }}
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="门诊医生" prop="applyDoctorCode">
            <el-select v-model="submitForm.applyDoctorCode">
              <el-option
                v-for="item in doctorListOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="6">
          <el-form-item label="门诊诊断" prop="diagnosisCode">
            <el-select v-model="submitForm.diagnosisCode">
              <el-option
                v-for="item in verificationStatusOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="患者病情" prop="patAdmCondition">
            <el-select v-model="submitForm.patAdmCondition">
              <el-option
                v-for="item in priorityEnumListOptions"
                :key="item.value"
                :label="item.info"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="入院类型" prop="admitSourceCode">
            <el-select v-model="submitForm.admitSourceCode">
              <el-option
                v-for="item in admissionMethodList"
                :key="item.value"
                :label="item.info"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="入院方式" prop="inWayCode">
            <el-select v-model="submitForm.inWayCode">
              <el-option
                v-for="item in admissionTypeList"
                :key="item.value"
                :label="item.info"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="入院日期" prop="inDate">
            <el-date-picker
              v-model="submitForm.inDate"
              value-format="YYYY-MM-DD"
              type="date"
              placeholder="请选择日期"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="6">
          <el-form-item label="来源机构" prop="sourceAgency">
            <hip-dic-sel
              dictCode="CF08.10.996"
              clearable
              class="w-200"
              v-model="submitForm.sourceAgency"
              placeholder="请输入"
            ></hip-dic-sel>
          </el-form-item>
        </el-col> -->
      </el-row>
    </el-form>
  </div>
</template>
<script setup >
import {
  getCurrentInstance,
  onActivated,
  onBeforeMount,
  onMounted,
  reactive,
  ref,
  unref,
  watch,
} from 'vue';
import { ElMessage } from 'element-plus';
import { getInit, getOrgList, wardList, diagnosisInit, doctorList } from "./api";

import dayjs from 'dayjs';
// const { proxy } = getCurrentInstance();
const emits = defineEmits([]);
const props = defineProps({
  patientInfo: {
    type: Object,
    require: true,
    default: () => ({})
  },
})


const dataSourceDoctor = ref([
  {
    name: '',
    id: '',
  },
]);
const admissionMethodList = ref([])
const admissionTypeList = ref([])
const organization = ref([])
const wardListOptions = ref([])
const verificationStatusOptions = ref([])
const priorityEnumListOptions = ref([])
const doctorListOptions = ref([])
const rules = reactive({
  organizationId: [
    {
      required: true,
      message: '入院科室未填写',
      trigger: ['blur', 'change'],
    },
  ],
  wardLocationId: [
    {
      required: true,
      message: '入院病区未填写',
      trigger: ['blur', 'change'],
    },
  ],
  patAdmCondition: [
    {
      required: true,
      message: '患者病情未填写',
      trigger: ['blur', 'change'],
    },
  ],
  inDate: [
    {
      required: true,
      message: '入院日期未填写',
      trigger: ['blur', 'change'],
    },
  ],
});

/*  下拉表格设置*/
const inDeptCodeClick = (row) => {
  // submitForm.inDeptCode = unref(row).id
  // submitForm.inDeptName = unref(row).name
  submitForm.inDocterWorkGroupCode = unref(row).deptCode;
};
const inDeptCodeClearActs = (row) => {
  submitForm.inDeptCode = '';
  submitForm.inDeptName = '';
  submitForm.inDocterWorkGroupCode = '';
};
// <Partial<IRegistration>>
/* 提交表单 */
const submitForm = reactive({
  inDeptCode: '', // 	入院科室
  wardLocationId: '', // 入院护理组编码
  admitSourceCode: '', // 患者入院类型（字典：PatientSource）
  inWayCode: '', // 入院方式（字典InpWay）
  inDate: dayjs(new Date()).format('YYYY-MM-DD 00:00:00'), // 	入院日期
  patAdmCondition: '', // 患者病情（字典：PatAdmCondition）
  applyDoctorCode: '', // 	申请入院医生
  inPatientId: '',
  inTimes: '',
  diagnosisCode: '',
  bedCount: '',
});
/* 科室 病区 */
watch(
  () => submitForm.inDocterWorkGroupCode,
  (newValue) => {
    if (newValue) {
      // TODO 监听科室重新查询病区
      //   getDataWorkGroup(newValue).then(() => {
      //     hipSelTabelInNurseDeptCodeRef.value.setData(dataSourceWorkGroup.value);
      //   });
      if (newValue == '') {
        submitForm.wardLocationId = '';
      }
    } else {
      submitForm.wardLocationId = '';
    }
  }
);

watch(
  () => props.patientInfo,
  (newValue) => {
    Object.assign(submitForm, newValue)
    handleChange(props.patientInfo.organizationId)
  },
  { immediate: true }
);

watch(
  () => submitForm.inNurseDeptCode,
  (newValue) => {
    if (newValue) {
      getBedInfo(newValue);
    }
  }
);

onMounted(() => {
  getInitOptions()
});

function getInitOptions() {
  getInit().then((res) => {
    admissionMethodList.value = res.data.admissionMethodList;
    admissionTypeList.value = res.data.admissionTypeList;
    priorityEnumListOptions.value = res.data.priorityEnumList
  });
  getOrgList().then((res) => {
    organization.value = res.data.records;
  });
  wardList().then(res => {
    wardListOptions.value = res.data;
  })
  diagnosisInit().then(res => {
    verificationStatusOptions.value = res.data.verificationStatusOptions;
  })
}

function handleChange(e) {
  if(e) {
    doctorList(e).then(res => {
      doctorListOptions.value = res.data;
    })
  }
}

/* 预登记 */
const preRegister = (res) => {
  if (res) {
    setTimeout(() => {
      submitForm.inPatientId = res.inpatientNo;
      submitForm.inTimes = res.inTimes;
      submitForm.applyDoctorCode = res?.ipdApplyTo?.patApplyDiagnosiss?.diagnosisName;
      submitForm.inDeptCode = res?.ipdApplyTo?.applyDeptCode;
      submitForm.wardLocationId = res?.ipdApplyTo?.nurseDeptCode;
      submitForm.patAdmCondition = res?.ipdApplyTo?.patAdmCondition.toString();
      submitForm.admitSourceCode = res?.ipdApplyTo?.admitSourceCode;
      submitForm.inWayCode = res?.ipdApplyTo?.inWayCode;
      submitForm.diagnosisCode = res?.ipdApplyTo?.patApplyDiagnosiss?.diagnosisCode || [];
    }, 1000);

    // sourceAgency
  }
};

/* 预登记 */
const getBedInfo = (inNurseDeptCode) => {
  // TODO 预登记接口
  //   getBedsAvailableApi(inNurseDeptCode)
  //     .then((res: any) => {
  //       submitForm.bedCount = res;
  //     })
  //     .catch((error: any) => {
  //       console.log(error);
  //     });
};

const registerRef = ref();
/* 登记 */
const validateData = async (callback) => {
  if (!registerRef.value) return false;
  registerRef.value.validate((valid) => {
    if (valid) {
      callback();
      return true;
    } else {
      return false;
    }
  });
};
// onBeforeMount(() => {

// })
const hipSelTabelInDeptCodeRef = ref();
const hipSelTabelInNurseDeptCodeRef = ref();
onMounted(() => {
  // 获取科室数据
  //   getDataDept().then(() => {
  //     hipSelTabelInDeptCodeRef.value.setData(dataSourceDept.value || []);
  //   });
});
onActivated(() => {
  // 获取科室数据
  //   getDataDept().then(() => {
  //     hipSelTabelInDeptCodeRef.value.setData(dataSourceDept.value);
  //   });
});
const init = () => {
  submitForm.inDocterWorkGroupCode = '';
  submitForm.wardLocationId = '';
};
defineExpose({ validateData, submitForm, preRegister, init });
</script>
<style lang="scss" scoped>
.registerForm-container {
  background: rgba(37, 109, 149, 0.05);
  margin-top: 16px;

  .operate {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 32px;
    border-radius: 4px 4px 0px 0px;
    padding-left: 16px;
    color: var(--hip-color-primary-light);
    font-weight: bold;
    margin-bottom: 8px;
  }

  .register-from {
    padding: 0 8px;
  }

  :deep(.el-form-item) {
    width: 100%;
    margin-right: 0px;
  }
}
</style>
