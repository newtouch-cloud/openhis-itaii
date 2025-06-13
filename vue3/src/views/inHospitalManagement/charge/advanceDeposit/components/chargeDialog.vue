<template>
  <el-dialog
    title="确认收费"
    v-model="props.open"
    width="700px"
    append-to-body
    destroy-on-close
    @close="close"
  >
    <div>
      <el-form
        :model="form"
        :rules="rules"
        ref="chargeDialogRef"
        label-width="110px"
        label-position="left"
      >
        <el-text size="large">
          <el-form-item label="费用性质：" prop="currentDate"> {{ '自费' }} </el-form-item>
        </el-text>
        <!-- 自费支付 -->
        <div class="payment-container">
          <el-form-item label="支付方式：" prop="payEnum" style="display: flex">
            <el-select
              v-model="form.payEnum"
              placeholder="选择支付方式"
              style="width: 160px"
              @change="clearAmount(index)"
            >
              <el-option
                v-for="payEnum in props.payLists"
                :key="payEnum.value"
                :label="payEnum.label"
                :value="payEnum.value"
                :disabled="isMethodDisabled(payEnum.value)"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="支付金额：" prop="amount">
            <div class="suffix-wrapper">
              <el-input-number
                v-model="form.amount"
                :precision="2"
                :min="0"
                :controls="false"
                placeholder="金额"
                class="amount-input"
                @change="handleAmountChange"
              />
              <span class="suffix-text">元</span>
            </div>
          </el-form-item>
        </div>
        <!-- 金额汇总 -->
        <div class="summary">
          <el-space :size="30">
            <div class="summary-item">
              <!-- <el-text type="info">实收合计：</el-text> -->
              <el-form-item label="实收合计：" prop="displayAmount">
                <el-text type="success">{{
                  form.displayAmount ? form.displayAmount : 0 + ' 元'
                }}</el-text>
              </el-form-item>
            </div>
          </el-space>
        </div>
      </el-form>
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
import { savePayment } from './api';
import { computed, watch, reactive, ref, getCurrentInstance, nextTick } from 'vue';
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
  patientInfo: {
    type: Object,
    default: undefined,
  },
  payLists: {
    type: Array,
    default: () => [],
  },
});

const { proxy } = getCurrentInstance();
const data = reactive({
  form: {},
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);

const emit = defineEmits(['close']);
const payMethods = ref(undefined);

async function submit() {
  // if (parseFloat(displayAmount.value) < formData.totalAmount.toFixed(2)) {
  //   proxy.$modal.msgError('请输入正确的结算金额');
  //   return;
  // }

  console.log(form.value, 'aaaaaaaaaaaaaaaaaaaaaaaaaa');

  // if ( chrome.webview === undefined) {
  //   alert('当前版本不支持银联支付');
  // } else {
  //   try {
  //     let jsonResult = await window.chrome.webview.hostObjects.CSharpAccessor.ReadCardAsync();
  //     let cardInfo = JSON.parse(jsonResult);
  //     console.log(cardInfo.CardType);
  //   } catch (error) {
  //     console.error('调用失败:', error);
  //   }
  // }
  form.value.patientId = props.patientInfo.patientId;
  form.value.encounterId = props.patientInfo.encounterId;
  console.log(props.patientInfo, 'patientInfo')
  savePayment(form.value).then((res) => {
    if (res.code == 200) {
      // (formData.selfPay = [{ payEnum: 220100, amount: 0.0, payLevelEnum: 2 }]),
      reset();
      emit('close', 'success');
    }
  });
}

/** 重置操作表单 */
function reset() {
  form.value = {
    patientId: undefined,
    encounterId: undefined,
    displayAmount: 0,
    payEnum: undefined,
    amount: undefined,
  };
  proxy.resetForm('chargeDialogRef');
}

// 检查支付方式是否已使用
const isMethodDisabled = (payEnum) => {
  return null;
};

/**
 * 处理金额变化事件
 *
 * 当表单中的金额发生变化时，将显示金额设置为实际金额
 */
function handleAmountChange() {
  form.value.displayAmount = form.value.amount;
}

/**
 * 变化支付方式事件
 *
 */
function clearAmount() {
  form.value.amount = 0;
}

function close() {
  reset();
  emit('close');
}

// function show() {
//   // formData.selfPay[index].amount = value;
// }

// defineExpose({
//   show,
// });
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
  display: inline-block; /* 保持行内布局 */
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