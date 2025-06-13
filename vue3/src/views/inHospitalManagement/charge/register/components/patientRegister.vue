<template>
  <el-dialog
    v-model="dialogVisible"
    top="6vh"
    :width="width"
    :title="props.title"
    @open="openAct"
    @closed="closedAct"
    :z-index="20"
  >
    <el-scrollbar height="700px">
      <PatientInfoComp 
        :patientInfo="patientApiInfo"
        :registrationType="props.registrationType"
        ref="patientInfoRef"
        @onChangFeeType="onChangFeeType"
      />
      <!-- <PatientRelationList
        class="relationList"
        :patCode="patientInfo.code"
        ref="PatientRelationListRef"
      /> -->
      <!--联系人组件  -->
      <RegisterForm :code="code" ref="RegisterFormRef" :patientInfo="patientApiInfo" />
    </el-scrollbar>
    <template v-slot:footer>
      <div class="advance-container">
        <el-space>
          <div>缴费预交金</div>
          <el-input
            style="width: 142px"
            placeholder="请输入"
            v-model="advance"
            @input="handleAdvanceInput"
            :formatter="handleAdvanceFormatter"
            :parser="handleAdvanceParser"
          ></el-input>
          <div
            class="feeType"
            :class="currentFeeType == typeitem.type ? 'activeFeeType' : ''"
            v-for="typeitem in feeTypeOptions"
            :key="typeitem.type"
            @click="currentFeeType = typeitem.type"
          >
            <svg-icon
              :icon-class="typeitem.type"
              :color="currentFeeType == typeitem.type ? '#13C0B3' : '#666666'"
              height="18px"
              width="18px"
              style="margin-right: 4px"
            />{{ typeitem.label }}
          </div>
        </el-space>
      </div>
      <el-button size="fixed" class="margin-left-auto" @click="closedAct">取消 </el-button>
      <el-button size="fixed" type="primary" @click="handleSubmit">登记</el-button>
      <!-- <hip-button size="fixed" type="primary" @click="supplementMi">医保登记</hip-button> -->
      <!-- <AdvancePayment
        v-model="advancePaymentVisible"
        @submitOk="advancePaymentSubmitOk"
        :money="advance"
      /> -->
    </template>
  </el-dialog>
</template>
<script setup>
import {
  getCurrentInstance,
  onBeforeMount,
  onMounted,
  provide,
  reactive,
  ref,
  unref,
  watch,
  watchEffect,
  defineModel,
} from 'vue';
// const { proxy } = getCurrentInstance();
import PatientInfoComp from './patientInfo.vue';
import RegisterForm from './registerForm.vue';
import { addAdmissionInfo, getPatientInfo } from "./api";
const emits = defineEmits(['okAct']);

const props = defineProps({
  title: '',
  registrationType: {
    type: [String, Boolean, Number], // 根据实际类型调整
    default: null // 或者 false、'' 等
  },
  patientInfo: {
    type: Object
  }
});

watch(
  () => props.registrationType,
  (newValue) => {
    console.log('registrationType changed:', newValue);
  },
  { immediate: true }
);

const dialogVisible = defineModel('dialogVisible', {
  type: Boolean,
  default: false,
});
const code = defineModel('code');
import { ElMessage } from 'element-plus';
const width = '1128px';
const patientApiInfo = ref({});

/* 取消 */
const cancelAct = () => {
  dialogVisible.value = false;
};
const patientInfoRef = ref()

/* 预交金 */
const advancePaymentVisible = ref(false);
/* 保存 */
const handleSubmit = () => {
  let params = {}
  RegisterFormRef.value.validateData(async () => {
    params = { ...params, ...RegisterFormRef.value.submitForm };
    patientInfoRef.value.submitForm(async () => {
      params = { ...params, ...patientInfoRef.value.form };
      params.advance = advance.value
      params.typeCode = currentFeeType.value
      addAdmissionInfo(params).then(res => {
        if (res.code == 200) {
          emits('okAct');
          ElMessage.success(res.message);
          advancePaymentVisible.value = true;
        } else {
          ElMessage.error(res.message);
        }
      })
    })
    // if (patientInfo.value?.feeType == '08') {
    //   if (currentFeeType.value == 'hipCash' && Number.parseFloat(advance.value) > 0) {
    //     advancePaymentVisible.value = true;
    //   } else {
    //     createRegistration();
    //   }
    // } else {
    //   medicalInsuranceTitle.value = '医保登记'; //医保信息、医保登记
    //   medicalInsuranceVisible.value = true;
    // }
  });


};

const advancePaymentSubmitOk = () => {
  advancePaymentVisible.value = false;
  createRegistration();
};
/* 登记 */
const createRegistration = async () => {
  // 住院登记
  //   try {
  //     const response = await postCreatePatIpdRegistrationApi({
  //       ...RegisterFormRef.value.submitForm,
  //       patCode: unref(code),
  //       sex: patientInfo.value.sex,
  //       sexName: patientInfo.value.sexName,
  //       name: patientInfo.value.name,
  //       payWay: currentFeeType.value,
  //       prepayAmount: advance.value,
  //       /* 医保 */
  //       medType: medicalInsuranceForm.value.miMedType,
  //     });
  //     ElMessage({
  //       message: '登记成功！',
  //       type: 'success',
  //       grouping: true,
  //       showClose: true,
  //     });
  //     emits('okAct');
  //   } catch (error: any) {
  //     console.log('error', error);
  //     const msg = error.message.splice(error.message.findLastIndex(':'));
  //     ElMessage({
  //       message: msg,
  //       type: 'error',
  //       grouping: true,
  //       showClose: true,
  //     });
  //   }
};

const openAct = () => {
  /* 初始化数据 */
  advance.value = '0.00';
  advancePaymentVisible.value = false;
  // PatientRelationListRef.value.setPatientRelationList([]);
  queryPatientData();
  RegisterFormRef.value.init();
};
const closedAct = () => {
  dialogVisible.value = false;
};

onMounted(() => {});
const state = reactive({});
const PatientRelationListRef = ref();

/* 查询患者 */
const queryPatientData = () => {
  // TODO 查询患者信息
    getPatientInfo(props.patientInfo.id)
      .then((res) => {
        if (res) {
          patientApiInfo.value = res.data
        }
      })
      .catch(() => {
        console.log();
      });
};
/* 登记 */
const RegisterFormRef = ref();

const feeTypeOptions = reactive([
  {
    type: 'hipCash',
    label: '现金',
  },
  {
    type: 'hipAlipay',
    label: '支付宝',
  },
  {
    type: 'wechat',
    label: '微信',
  },
  {
    type: 'hipPayCard',
    label: '银行卡',
  },
]);
const advance = ref('0.00');
const currentFeeType = ref('hipCash');
/* 预交金录入框格式 */
const handleAdvanceInput = (value) => {
  if (value.length === 1 && value === '.') {
    value = '0.';
  } else {
    advance.value = value.replace(/[^\d.]/g, '').replace(/^(\d*\.\d{2}).*$/, '$1');
  }
};
const handleAdvanceFormatter = (value) => {
  return `¥ ${value}`;
};
const handleAdvanceParser = (value) => {
  return value.replace(/\$\s?|(,*)/g, '');
};
const medicalInsuranceVisible = ref(false);
const medicalInsuranceTitle = ref('');

/* 患者费别变更 */
const onChangFeeType = () => {
  medicalInsuranceTitle.value = '医保信息'; //医保信息、医保登记
  medicalInsuranceVisible.value = true;
};
/* 患者费别变更 ,成功之后 */
const chargeTypeOk = (data) => {
  if (data.chargeType) {
    patientInfo.value.feeType = data.chargeType;
    patientInfo.value.feeTypeName = getName('ChargeType', data.chargeType);
    medicalInsuranceVisible.value = false;
  }
};
const medicalInsuranceForm = ref({});
const registerOk = (data) => {
  // debugger;
  medicalInsuranceForm.value = unref(data);
  if (currentFeeType.value == 'hipCash' && Number.parseFloat(advance.value) > 0) {
    advancePaymentVisible.value = true;
  } else {
    createRegistration();
  }
};
/*  */
const supplementMi = () => {
  medicalInsuranceTitle.value = '医保登记'; //医保信息、医保登记
  medicalInsuranceVisible.value = true;
};

/*  */
</script>
<style lang="scss" scoped>
.patientRegister-container {
  height: 700px;
  overflow-y: auto;
}

.advance-container {
  width: 660px;
  display: flex;

  .feeType {
    display: flex;
    align-items: center;
    height: 32px;
    padding: 0px 8px;
    color: #666666;
    border-radius: 4px;
    cursor: pointer;

    :deep(.svg-icon) {
      color: #666666;
    }
  }

  .activeFeeType {
    color: #13c0b3;
    background: rgba(19, 192, 179, 0.1);

    :deep(.svg-icon) {
      color: #13c0b3;
    }
  }
}
</style>
