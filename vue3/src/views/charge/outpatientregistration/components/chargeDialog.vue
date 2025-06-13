<template>
  <el-dialog title="确认收费" v-model="props.open" width="700px" append-to-body destroy-on-close>
    <div v-loading="dialogLoading">
      <el-text size="large" style="display: block; margin-bottom: 15px">
        收费日期：{{ currentDate }}
      </el-text>
      <el-text size="large">费用性质：{{ '自费' }}</el-text>
      <div class="amount-row">
        <el-text size="large">应收金额：</el-text>
        <el-text size="large" type="primary" class="amount">
          {{ props.totalAmount.toFixed(2) + ' 元' }}
        </el-text>
      </div>

      <!-- 自费支付 -->
      <div class="payment-container">
        <div v-for="(item, index) in formData.selfPay" :key="index" class="payment-item">
          <span>支付方式：</span>
          <el-select
            v-model="item.payEnum"
            placeholder="选择支付方式"
            style="width: 160px"
            @change="clearAmount(index)"
          >
            <el-option
              v-for="payEnum in selfPayMethods"
              :key="payEnum.value"
              :label="payEnum.label"
              :value="payEnum.value"
              :disabled="isMethodDisabled(payEnum.value)"
            />
          </el-select>
          <span>支付金额：</span>
          <div class="suffix-wrapper">
            <el-input-number
              v-model="item.amount"
              :precision="2"
              :min="0"
              :max="getMax(index)"
              :controls="false"
              placeholder="金额"
              class="amount-input"
              @change="handleAmountChange"
            />
            <span class="suffix-text">元</span>
          </div>
          <el-button
            type="danger"
            circle
            :icon="Delete"
            @click="removePayment(index)"
            v-if="index > 0"
          />
        </div>
        <div class="add-payment">
          <el-button
            type="primary"
            plain
            @click="addPayment"
            :disabled="formData.selfPay.length >= 4 || remainingAmount <= 0"
          >
            添加支付方式
          </el-button>
          <el-text v-if="remainingAmount <= 0" type="danger" class="tip">
            金额已满足应收，不可继续添加
          </el-text>
        </div>
      </div>
      <!-- 金额汇总 -->
      <div class="summary">
        <el-space :size="30">
          <div class="summary-item">
            <el-text type="info">实收合计：</el-text>
            <el-text type="success">{{ displayAmount + ' 元' }}</el-text>
          </div>
          <div class="summary-item">
            <el-text type="info">应找零：</el-text>
            <el-text type="warning">{{ returnedAmount + ' 元' }}</el-text>
          </div>
        </el-space>
      </div>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="throttledGetList" :disabled="dialogLoading"
          >确 定</el-button
        >
        <el-button @click="close" :disabled="dialogLoading">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>
  
<script setup>
import { savePayment } from './outpatientregistration';
import { computed, watch, reactive, ref, getCurrentInstance, nextTick } from 'vue';
import { Delete } from '@element-plus/icons-vue';
import { debounce } from 'lodash-es';

const props = defineProps({
  open: {
    type: Boolean,
    default: false,
  },
  totalAmount: {
    type: Number,
    default: 0.0,
  },
  patientInfo: {
    type: Object,
    default: undefined,
  },
  chargeItemIds: {
    type: [],
    default: [],
  },
  chrgBchnoList: {
    type: [],
    default: [],
  },
  chrgBchno: {
    type: String,
    default: '',
  },
  registerBusNo: {
    type: String,
    default: '',
  },
  transformedData: {
    type: Object,
    default: undefined,
  },
});

const { proxy } = getCurrentInstance();
const dialogLoading = ref(false);
const throttledGetList = debounce(submit, 300);
const formData = reactive({
  totalAmount: 0,
  selfPay: [{ payEnum: 220100, amount: 0.0, payLevelEnum: 2 }],
  medicalInsurance: {
    account: '',
    poolPay: 0,
    personalPay: 0,
  },
});

watch(
  () => props.totalAmount,
  (newValue) => {
    nextTick(() => {
      formData.totalAmount = newValue;
      formData.selfPay[0].amount = newValue;
    });
  }
);

const emit = defineEmits(['close']);

async function printReceipt(param) {
  console.log(param, 'param');
  console.log(props.patientInfo, 'props.patientInfo');

  // 构造一个新的对象，添加头 "data"
  const result = {
    data: [
      {
        ...param,
        // 基础支付类型
        YB_FUND_PAY: param.detail.find((t) => t.payEnum === 100000)?.amount ?? 0, // 基金支付总额
        SELF_PAY: param.detail.find((t) => t.payEnum === 200000)?.amount ?? 0, // 个人负担总金额
        OTHER_PAY: param.detail.find((t) => t.payEnum === 300000)?.amount ?? 0, // 其他（如医院负担金额）

        // 基本医保统筹基金支出
        YB_TC_FUND_AMOUNT: param.detail.find((t) => t.payEnum === 110000)?.amount ?? 0, // 基本医保统筹基金支出
        YB_BC_FUND_AMOUNT: param.detail.find((t) => t.payEnum === 120000)?.amount ?? 0, // 补充医疗保险基金支出
        YB_JZ_FUND_AMOUNT: param.detail.find((t) => t.payEnum === 130000)?.amount ?? 0, // 医疗救助基金支出
        YB_OTHER_AMOUNT: param.detail.find((t) => t.payEnum === 140000)?.amount ?? 0, // 其他支出

        // 职工基本医疗保险
        YB_TC_ZG_FUND_VALUE: param.detail.find((t) => t.payEnum === 110100)?.amount ?? 0, // 职工基本医疗保险
        YB_TC_JM_FUND_VALUE: param.detail.find((t) => t.payEnum === 110200)?.amount ?? 0, // 居民基本医疗保险（修正原错误注释）

        // 补充医疗保险基金支出细分
        YB_BC_JM_DB_VALUE: param.detail.find((t) => t.payEnum === 120100)?.amount ?? 0, // 全体参保人的居民大病保险
        YB_BC_DE_BZ_VALUE: param.detail.find((t) => t.payEnum === 120200)?.amount ?? 0, // 大额医疗费用补助
        YB_BC_ZG_DE_BZ_VALUE: param.detail.find((t) => t.payEnum === 120300)?.amount ?? 0, // 企业职工大额医疗费用补助
        YB_BC_GWY_BZ_VALUE: param.detail.find((t) => t.payEnum === 120400)?.amount ?? 0, // 公务员医疗补助

        // 其他支出细分
        OTHER_PAY_DD_FUND_VALUE: param.detail.find((t) => t.payEnum === 300001)?.amount ?? 0, // 兜底基金支出
        OTHER_PAY_YW_SH_FUND_VALUE: param.detail.find((t) => t.payEnum === 300002)?.amount ?? 0, // 意外伤害基金支出
        OTHER_PAY_LX_YL_FUND_VALUE: param.detail.find((t) => t.payEnum === 300003)?.amount ?? 0, // 离休人员医疗保障金支出
        OTHER_PAY_LX_YH_FUND_VALUE: param.detail.find((t) => t.payEnum === 300004)?.amount ?? 0, // 离休人员优惠金支出
        OTHER_PAY_CZ_FUND_VALUE: param.detail.find((t) => t.payEnum === 300005)?.amount ?? 0, // 财政基金支出
        OTHER_PAY_CZ_YZ_FUND_VALUE: param.detail.find((t) => t.payEnum === 300006)?.amount ?? 0, // 财政预支支出
        OTHER_PAY_ZG_DB_FUND_VALUE: param.detail.find((t) => t.payEnum === 300007)?.amount ?? 0, // 职工大病基金支出
        OTHER_PAY_EY_FUND_VALUE: param.detail.find((t) => t.payEnum === 300008)?.amount ?? 0, // 二乙基金支出
        OTHER_PAY_QX_JZ_FUND_VALUE: param.detail.find((t) => t.payEnum === 300009)?.amount ?? 0, // 倾斜救助支出
        OTHER_PAY_YL_JZ_FUND_VALUE: param.detail.find((t) => t.payEnum === 300010)?.amount ?? 0, // 医疗救助再救助基金
        HOSP_PART_AMT: param.detail.find((t) => t.payEnum === 300011)?.amount ?? 0, // 医院负担金额

        // 医保结算返回值
        FULAMT_OWNPAY_AMT: param.detail.find((t) => t.payEnum === 1)?.amount ?? 0, // 全自费金额
        OVERLMT_SELFPAY: param.detail.find((t) => t.payEnum === 3)?.amount ?? 0, // 超限价自费费用
        PRESELFPAY_AMT: param.detail.find((t) => t.payEnum === 4)?.amount ?? 0, // 先行自付金额
        INSCP_SCP_AMT: param.detail.find((t) => t.payEnum === 5)?.amount ?? 0, // 符合政策范围金额
        ACT_PAY_DEDC: param.detail.find((t) => t.payEnum === 6)?.amount ?? 0, // 实际支付起付线
        POOL_PROP_SELFPAY: param.detail.find((t) => t.payEnum === 7)?.amount ?? 0, // 基本医疗保险统筹基金支付比例
        BALC: param.detail.find((t) => t.payEnum === 8)?.amount ?? 0, // 余额

        // 特殊支付方式
        SELF_YB_ZH_PAY: param.detail.find((t) => t.payEnum === 210000)?.amount ?? 0, // 个人医保账户支付
        SELF_YB_ZH_GJ_VALUE: param.detail.find((t) => t.payEnum === 210100)?.amount ?? 0, // 账户共济支付金额
        SELF_CASH_PAY: param.detail.find((t) => t.payEnum === 220000)?.amount ?? 0, // 个人现金支付金额
        SELF_VX_PAY: param.detail.find((t) => t.payEnum === 230000)?.amount ?? 0, // 微信支付金额
        SELF_ALI_PAY: param.detail.find((t) => t.payEnum === 240000)?.amount ?? 0, // 阿里支付金额

        // 现金支付细分
        SELF_CASH_VALUE: param.detail.find((t) => t.payEnum === 220400)?.amount ?? 0, // 个人现金支付金额(现金)
        SELF_CASH_VX_VALUE: param.detail.find((t) => t.payEnum === 220100)?.amount ?? 0, // 个人现金支付金额(微信)
        SELF_CASH_ALI_VALUE: param.detail.find((t) => t.payEnum === 220200)?.amount ?? 0, // 个人现金支付金额(支付宝)
        SELF_CASH_UNION_VALUE: param.detail.find((t) => t.payEnum === 220300)?.amount ?? 0, // 个人现金支付金额(银联)

        // 基金类型（扩展）
        BIRTH_FUND: param.detail.find((t) => t.payEnum === 510100)?.amount ?? 0, // 生育基金
        RETIREE_MEDICAL: param.detail.find((t) => t.payEnum === 340100)?.amount ?? 0, // 离休人员医疗保障基金
        URBAN_BASIC_MEDICAL: param.detail.find((t) => t.payEnum === 390100)?.amount ?? 0, // 城乡居民基本医疗保险基金
        URBAN_SERIOUS_ILLNESS: param.detail.find((t) => t.payEnum === 390200)?.amount ?? 0, // 城乡居民大病医疗保险基金
        MEDICAL_ASSISTANCE: param.detail.find((t) => t.payEnum === 610100)?.amount ?? 0, // 医疗救助基金
        GOVERNMENT_SUBSIDY: param.detail.find((t) => t.payEnum === 640100)?.amount ?? 0, // 政府兜底基金
        ACCIDENT_INSURANCE: param.detail.find((t) => t.payEnum === 390400)?.amount ?? 0, // 意外伤害基金
        CARE_INSURANCE: param.detail.find((t) => t.payEnum === 620100)?.amount ?? 0, // 照护保险基金
        FINANCIAL_FUND: param.detail.find((t) => t.payEnum === 360100)?.amount ?? 0, // 财政基金
        HOSPITAL_ADVANCE: param.detail.find((t) => t.payEnum === 999900)?.amount ?? 0, // 医院垫付
        SUPPLEMENTARY_INSURANCE: param.detail.find((t) => t.payEnum === 390300)?.amount ?? 0, // 城乡居民大病补充保险基金
        HEALTHCARE_PREPAYMENT: param.detail.find((t) => t.payEnum === 360300)?.amount ?? 0, // 保健预支基金
        Mr_QR_Code: param.regNo,
        sex: props.patientInfo.genderEnum_enumText,
        age: props.patientInfo.age,
        personType: '职工医保',
        fixmedinsName: param.fixmedinsName + '门诊收费明细',
      },
    ],
    // feeDetial: param.detail, //收费项目，后端还未返回
  };
  // 将对象转换为 JSON 字符串
  let jsonString = JSON.stringify(result, null, 2);
  console.log(jsonString, 'jsonString');
  await window.chrome.webview.hostObjects.CSharpAccessor.PrintReport(
    '门诊收费明细单.grf',
    jsonString
  )
    .then((response) => {
      //返回结果是jsonString，可判断其调用是否成功
      console.log(response, 'response');
      var res = JSON.parse(response);
      if (!res.IsSuccess) {
        proxy.$modal.msgError('调用打印插件失败:' + res.ErrorMessage);
      }
    })
    .catch((error) => {
      proxy.$modal.msgError('调用打印插件失败:' + error);
    });
}

function submit() {
  if (parseFloat(displayAmount.value) < formData.totalAmount) {
    proxy.$modal.msgError('请输入正确的结算金额');
    return;
  }
  dialogLoading.value = true;
  savePayment({
    // paymentEnum: 0,
    // kindEnum: 1,
    // patientId: props.patientInfo.patientId,
    // encounterId: props.patientInfo.encounterId,
    // chargeItemIds: props.chargeItemIds,
    outpatientRegistrationAddParam: props.transformedData,
    chrgBchno: props.chrgBchno,
    busNo: props.registerBusNo,
    paymentDetails: formData.selfPay,
    // ybFlag: '0',
    // eleFlag: '0',
    // returnedAmount: parseFloat(returnedAmount.value),
  })
    .then((res) => {
      if (res.code == 200) {
        printReceipt(res.data);
        (formData.selfPay = [{ payEnum: 220100, amount: 0.0, payLevelEnum: 2 }]),
          emit('close', 'success');
      }
    })
    .finally(() => {
      dialogLoading.value = false;
    });
}

const currentDate = ref(new Date().toLocaleString());

const selfPayMethods = [
  { label: '现金', value: 220400 },
  { label: '微信', value: 220100 },
  { label: '支付宝', value: 220200 },
  { label: '银联', value: 220300 },
];

// 计算剩余可输入金额
const remainingAmount = computed(() => {
  return (
    formData.totalAmount - formData.selfPay.reduce((sum, item) => sum + Number(item.amount), 0)
  );
});

// 获取单个支付方式的最大可输入金额
const getMax = (index) => {
  const otherSum = formData.selfPay.reduce(
    (sum, item, i) => (i !== index ? sum + Number(item.amount) : sum),
    0
  );
  if (formData.selfPay[index].payEnum == 220400) {
    return formData.totalAmount + 100 - otherSum;
  }
  return formData.totalAmount - otherSum;
};

// 检查支付方式是否已使用
const isMethodDisabled = (payEnum) => {
  return formData.selfPay.some((item) => item.payEnum === payEnum);
};

const handleAmountChange = () => {
  // 不需要在这里直接设置 returnedAmount，依赖 computed 属性
};

const addPayment = () => {
  if (remainingAmount.value <= 0) {
    return;
  }
  formData.selfPay.push({ payEnum: '', amount: remainingAmount.value });
};

const removePayment = (index) => {
  formData.selfPay.splice(index, 1);
};

const clearAmount = (index) => {
  // formData.selfPay[index].amount = 0;
};

// 计算属性
const displayAmount = computed(() => {
  return formData.selfPay.reduce((sum, item) => sum + (Number(item.amount) || 0), 0).toFixed(2);
});

const returnedAmount = computed(() => {
  const display = parseFloat(displayAmount.value);
  if (isNaN(display) || display <= 0) {
    return '0.00';
  }
  const returned = display - formData.totalAmount;
  return returned >= 0 ? returned.toFixed(2) : '0.00';
});

function close() {
  emit('close', 'cancel');
}
</script>

<style scoped>
:deep(.pagination-container .el-pagination) {
  right: 20px !important;
}
.charge-container {
  max-width: 600px;
  margin: 20px auto;
  padding: 20px 30px;
}

.header {
  margin-bottom: 10px;
}

.amount-row {
  display: flex;
  align-items: center;
  gap: 15px;
  margin: 15px 0;
}

.amount {
  font-size: 20px;
  font-weight: bold;
}

.payment-type {
  margin: 15px 0;
}

.payment-container {
  margin: 15px 0;
}

.payment-item {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 12px;
}

.amount-input {
  width: 140px;
}

.add-payment {
  margin-top: 10px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.tip {
  font-size: 12px;
}

.summary {
  margin: 25px 0;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 4px;
}

.summary-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.action-buttons {
  text-align: center;
  margin-top: 25px;
}

.el-text.el-text--success {
  font-size: 18px !important;
  font-weight: 500;
}

.el-text.el-text--warning {
  font-size: 18px !important;
  font-weight: 500;
}
.suffix-wrapper {
  position: relative;
  display: inline-block;
}
.suffix-text {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: #999;
  pointer-events: none; /* 避免点击干扰 */
}
</style>