<template>
  <el-dialog
    title="确认退费"
    v-model="props.open"
    width="700px"
    append-to-body
    destroy-on-close
    @close="close"
  >
    <div>
      <el-text size="large" style="display: block; margin-bottom: 15px">
        退费日期：{{ currentDate }}
      </el-text>
      <el-text size="large">费用性质：{{ '自费' }}</el-text>
      <div class="amount-row">
        <el-text size="large">应退金额：</el-text>
        <el-text size="large" type="primary" class="amount">
          {{ props.totalAmount.toFixed(2) + ' 元' }}
        </el-text>
      </div>

      <!-- 自费支付 -->
      <div class="payment-container">
        <div v-for="(item, index) in formData.selfPay" :key="index" class="payment-item">
          <span>退费方式：</span>
          <el-select
            v-model="item.payEnum"
            placeholder="选择退费方式"
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
          <span>退费金额：</span>
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
        <div class="payment-container" style="position: relative">
          <span style="position: absolute; top: 5px">退费原因：</span>
          <el-input
            type="textarea"
            :rows="2"
            v-model="reason"
            placeholder="退费原因"
            class="reason-textarea"
            @change="handleAmountChange"
          />
        </div>
        <div class="add-payment">
          <el-button
            type="primary"
            plain
            @click="addPayment"
            :disabled="formData.selfPay.length >= 4 || remainingAmount <= 0"
          >
            添加退费方式
          </el-button>
          <el-text v-if="remainingAmount <= 0" type="danger" class="tip">
            金额已满足应退，不可继续添加
          </el-text>
        </div>
      </div>
      <!-- 金额汇总 -->
      <div class="summary">
        <el-space :size="30">
          <div class="summary-item">
            <el-text type="info">实退合计：</el-text>
            <el-text type="success">{{ displayAmount + ' 元' }}</el-text>
          </div>
          <!-- <div class="summary-item">
            <el-text type="info">应找零：</el-text>
            <el-text type="warning">{{ returnedAmount + ' 元' }}</el-text>
          </div> -->
        </el-space>
      </div>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="submit">确 定</el-button>
        <el-button @click="close">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { cancelRegister } from './outpatientregistration';
import { computed, watch, reactive, ref, getCurrentInstance } from 'vue';
import { Delete } from '@element-plus/icons-vue';

const props = defineProps({
  open: {
    type: Boolean,
    default: false,
  },
  totalAmount: {
    type: Number,
    default: 0.0,
  },
  category: {
    type: String,
  },
  paymentId: {
    type: String,
  },
  patientInfo: {
    type: Object,
    default: undefined,
  },
  chargeItemIds: {
    type: [],
    default: [],
  },
});

const { proxy } = getCurrentInstance();
const reason = ref('');

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
    formData.totalAmount = newValue;
    formData.selfPay[0].amount = newValue;
  }
);

const emit = defineEmits(['close']);

function submit() {
  console.log(props.chargeItemIds);

  if (parseFloat(displayAmount.value) < formData.totalAmount) {
    proxy.$modal.msgError('请输入正确的金额');
    return;
  }
  cancelRegister({
    paymentEnum: 0,
    kindEnum: 1,
    patientId: props.patientInfo.patientId,
    id: props.paymentId,
    encounterId: props.patientInfo.encounterId,
    chargeItemIds: [],
    paymentDetails: formData.selfPay,
    reason: reason.value,
    ybFlag: '1',
    eleFlag: '0',
    // returnedAmount: parseFloat(returnedAmount.value),
  }).then((res) => {
    if (res.code == 200) {
      emit('close', 'success');
    }
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
  emit('close');
}
</script>

<style lang="scss" scoped>
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
.reason-textarea {
  margin-left: 80px;
  width: 59%;
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

/* 调整输入框内边距 */
.amount-input .el-input__inner {
  padding-right: 30px !important;
}
</style>