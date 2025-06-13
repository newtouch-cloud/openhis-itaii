<!--
 * @Author: hezhibo@bjgoodwill.com
 * @Date: 2025-02-17 17:32:06
 * @Description: 收款
-->
<template>
  <el-dialog v-model="visible" width="689px" title="收款" @open="openAct" @closed="closedAct" destroy-on-close
    class="receiptContainer">
    <div class="operate">
      <el-space :size="16">
        <el-button link v-for="type in chargeTypeData" :key="type.elementCode" @click="changeType(type.elementCode)"
          class="chargeType" :class="{
            currentChargeType: payForm.payway == type.elementCode,
          }">
          <el-icon size="16" color="#666666" :icon-class="type.iconClass" style="cursor: pointer; margin-right: 4px" />
          {{ type.elementName }}
        </el-button>
      </el-space>
    </div>
    <div class="collectFee">
      <el-space :size="0" class="amountReceivable">
        <span class="title">应收：</span>
        <span class="costValue">¥{{ receivable }}</span>
      </el-space>
      <el-space :size="8">
        <span>金额</span>
        <el-input v-model="payForm.amount" placeholder="">
          <template #suffix>
            <span>元</span>
          </template>
        </el-input>
        <el-button style="margin-right: 0px" @click="collectFee">收费</el-button>
      </el-space>
    </div>
    <div class="payData-container">
      <el-table :data="payData" height="100%" show-overflow-tooltip
        :style="{ 'border-right': '1px solid #eeeeee', height: '100%' }">
        <el-table-column prop="paywayName" label="缴费方式" width="99" />
        <el-table-column prop="amount" label="金额" width="117" align="right" header-align="left" />
        <el-table-column class-name="amount" prop="nameRight" label="状态" width="124" :formatter="(row: any) => {
          if (row.transactionType == 'IN') return '已缴费'
          if (row.transactionType == 'OUT') return '已退费'
        }
          " />
        <el-table-column prop="costRight" label="退款方式" min-width="124" align="right" header-align="left">
          <template #default="{ row }">
            <el-select v-if="row.transactionType == 'IN'" v-model="row.returnWay" placeholder="返还方式"
              style="width: 100%">
              <el-option label="原路返还" value="1" />
              <el-option label="现金" value="2" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="操作" width="113">
          <template #default="{ row }">
            <el-button v-if="row.transactionType == 'IN'" link type="warning" width="115"
              @click="collectFeeCancel(row)">退款</el-button>
            <el-button link width="115" style="margin-right: 0">打印</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="line-container"></div>
    <el-space :size="40" class="confirm-container">
      <div>
        <span class="title">实收：</span>
        <span class="costReal">{{ payForm.amount }}</span>
        <span class="unit">元</span>
      </div>
      <div>
        <span class="title">找零：</span>
        <span class="charge">
          88
        </span>
        <span class="unit">元</span>
      </div>
    </el-space>

    <div class="confirm-container">
      <el-space :size="40">
        <el-space :size="12">
          <span>发票类型</span>
          <el-select v-model="receiptType" placeholder="发票类型" style="width: 145px">
            <el-option label="纸质发票" value="1" />
          </el-select>
        </el-space>
        <div>
          <span>当前发票号：</span>
          <span style="color: #256d95">{{ 123213 }}</span>
          <el-button style="margin-right: 0px; margin-left: 8px">跳号</el-button>
        </div>
      </el-space>
    </div>
    <template v-slot:tool>
      <el-button class="margin-left-auto" @click="close">取消 </el-button>
      <el-button type="primary" @click="submitForm()">确定 </el-button>
    </template>
    <invoiceSkip v-model:visible="invoiceSkipVisible" />
  </el-dialog>
</template>
<script setup >
import { getCurrentInstance, onActivated, onBeforeMount, onMounted, reactive, ref } from 'vue'
import { invoiceSkip } from './index'

import { ElMessage } from 'element-plus'

// const { proxy } = getCurrentInstance();
const emits = defineEmits([])
const props = defineProps<{
  settleId: string
  receivable: number //应收
  patCode: string //主索引,
  visitCode: string
}>()
const state = reactive({})

const payForm = reactive({
  visitCode: '',
  // econIpdSettlePayAddAsTo: {
  settleId: '', //结算 id
  payway: '', //支付方式
  amount: 0, //金额
  bankCard: '', //银行卡号
  posId: '',
  econPosBusinessAddto: {
    patCode: '',
    posAmount: 0,
  },
  // }
})
const visible = defineModel('visible', { type: Boolean, default: false })
const openAct = () => {
  //字典等基础数据
  payForm.settleId = props.settleId
  payForm.econPosBusinessAddto.patCode = props.patCode
  // TODO
  payForm.amount = props.receivable
  payForm.econPosBusinessAddto.posAmount = props.receivable

  getPayData()
}

// /* 收费方式 */
const chargeTypeData = ref<Array<any>>([
])

const changeType = (elementCode: string) => {
  payForm.payway = elementCode
}
/* 支付信息 */
const payData = ref([])
const getPayData = () => {
  if (props.settleId) {
    getSettlePay(props.settleId).then((res: any) => {
      payData.value = res.map((i: any) => {
        return {
          ...i,
          returnWay: '1',
        }
      })
    })
  }
}
/* 收费 */
const collectFee = () => {
  payForm.econPosBusinessAddto.posAmount = payForm.amount
  payForm.visitCode = props.visitCode
  settlePay(payForm).then((res: any) => {
    payForm.amount = 0
    getPayData()
  })
}
/* 退费 */
const collectFeeCancel = (row: any) => {
  settleRefundPay(row.id, {
    settleId: props.settleId,
    returnWay: row.returnWay,
    payway: row.payway, //支付方式
    amount: row.amount, //金额
    bankCard: '', //银行卡号
    posId: '',
    visitCode: props.visitCode,
    econPosBusinessAddto: {
      patCode: props.patCode,
      posAmount: row.amount,
    },
  }).then((res: any) => {
    payForm.amount = 0
    getPayData()
  })
}
const closedAct = () => {
  visible.value = false
}
//
// onBeforeMount(() => {

// })
//  onMounted(() => {
//     getPayData()
// })

onActivated(() => {
  getPayData()
})
/* 发票类型 */
const receiptType = ref('1')

/* 发票跳号 */
const invoiceSkipVisible = ref(false)
// const jump = () => {
//     invoiceSkipVisible.value = true
// }
const close = () => {
  visible.value = false
}
const submitForm = () => {
  ElMessage({
    message: '打印发票！',
    type: 'success',
    grouping: true,
    showClose: true,
  })
  visible.value = false
}
defineExpose({ state })
</script>
<style lang="scss" scoped>
.receiptContainer {
  font-family: '思源黑体 CN';
  font-weight: 400;

  .operate {
    padding: 8px;
    margin-top: -16px;
    margin: -16px 8px 0;
    border-bottom: 2px solid #e9f3f9;
    box-shadow: 0px 1px 1px 0px rgba(181.05, 210.63, 255, 0.25);

    .currentChargeType {
      background: rgba(19, 192, 179, 0.1);
      height: 32px;
    }

    .chargeType {
      padding: 0 8px;
    }
  }

  .collectFee {
    height: 60px;
    display: flex;
    padding: 0 8px 0 22px;
    align-items: center;
    justify-content: space-between;

    .amountReceivable {
      .title {
        color: #666666;
        font-family: '思源黑体 CN';
      }

      .costValue {
        font-size: 24px;
        font-family: '思源黑体 CN';
        font-weight: 400;
        color: #ff8616;
      }
    }
  }

  .payData-container {
    height: 228px;
  }

  .line-container {
    margin: 8px -16px 4px;
    border: 1px solid #e9f3f9;
    width: calc(100% + 30px);
    box-shadow: 0px 1px 1px 0px rgba(181.05, 210.63, 255, 0.25);
  }

  .confirm-container {
    padding-left: 22px;
    height: 30px;
    margin-top: 8px;

    .title {
      color: #666666;
    }

    .charge {
      font-size: 20px;
      margin-right: 8px;
      color: #e95657;
    }

    .costReal {
      font-size: 20px;
      font-weight: 400;
      color: #333333;
      margin-right: 10px;
    }

    .unit {
      color: #bbbbbb;
    }
  }

  .costValue {
    color: #ff8616 !important;
  }
}
</style>
