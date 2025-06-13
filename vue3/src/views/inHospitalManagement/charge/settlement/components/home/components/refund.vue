<!--
 * @Author: hezhibo@bjgoodwill.com
 * @Date: 2025-02-17 17:32:06
 * @Description: 收款
-->
<template>
  <el-dialog v-model="visible" width="870px" title="返款" @open="openAct" @closed="closedAct" destroy-on-close
    class="refundContainer">
    <div class="operate">
      <el-space :size="8">
        <span class="title">应返：</span>
        <span class="charge">¥{{ Math.abs(refund) }}</span>
        <!-- -->
      </el-space>
      <el-space :size="16">
        <div>
          <span class="title">预缴金合计：</span>
          <span class="castvalue">{{ refundCalculation.prepayAmountAll }}</span>
        </div>
        <div>
          <span class="title">余额：</span>
          <span class="castvalue castvalueWeight">{{ refundCalculation.prepayAmount }}</span>
        </div>
        <div>
          <span class="title">返还方式</span>
          <el-select class="refundType" v-model="paywaytype" placeholder="返还方式" @change="paywaytypeChange">
            <el-option label="原路返还" value="1" />
            <el-option label="现金" value="2" />
          </el-select>
        </div>
      </el-space>
    </div>
    <div class="table-container">
      <el-table :data="refundData" height="100%" show-overflow-tooltip :style="{ height: '100%' }"
        v-loading="prePayDataloading" @selection-change="selectionChange">
        <el-table-column prop="checkStatus" type="selection" width="38" header-row-class-name="removeAllSelect"
          :selectable="selectablejisuna">
        </el-table-column>
        />

        <el-table-column prop=" paywayName" label="支付方式" width="160" />
        <el-table-column prop="amount" label="总金额" width="98" align="right" header-align="left" />
        <el-table-column prop="remainingAmount" label="可返金额" width="98" align="right" header-align="left" :formatter="(row: any) => {
          return row.remainingAmount && row.remainingAmount == 0
            ? h('div', { style: { color: '#E95657' } }, row.remainingAmount)
            : row.remainingAmount
        }
          " />
        <el-table-column prop="returnAmount" label="返还金额" width="98" align="right" header-align="left" :formatter="(row: any) => {
          return row.remainingAmount && row.remainingAmount == 0
            ? h('div', { style: { color: '#E95657' } }, row.returnAmount)
            : row.returnAmount
        }
          " />
        <el-table-column prop="createdDate" label="缴费日期" width="104" />
        <el-table-column prop="payway" label="状态" min-width="70" :formatter="(row: any) => {
          return row.remainingAmount && row.remainingAmount == 0 ? '已返换' : ''
        }
          ">
        </el-table-column>
        <el-table-column prop="" label="返还方式" width="168">
          <template #default="{ row }">
            <el-select v-if="row.checkStatus" v-model="row.returnWay" placeholder="返还方式" style="width: 100%">
              <el-option label="原路返还" value="1" />
              <el-option label="现金" value="2" />
            </el-select>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="line-container">
      返还时请勾选需要退的预交金，退款顺序为由上到下依次（选中的）退款。
    </div>

    <template v-slot:tool>
      <el-button class="margin-left-auto" @click="close">取消 </el-button>
      <el-button type="primary" @click="submitForm()">确定 </el-button>
    </template>
  </el-dialog>
</template>
<script setup >
import { getCurrentInstance, onBeforeMount, onMounted, reactive, ref, h } from 'vue'
// const { proxy } = getCurrentInstance();
const emits = defineEmits([])
const props = defineProps<{
  settleId: string
  refund: number //应返
  visitCode: string //预交金
  patCode: string //主索引
}>()

const state = reactive({})
const visible = defineModel('visible', { type: Boolean, default: false })
const openAct = () => {
  sumRefund.value = 0
  sumRefundold.value = 0
  //字典等基础数据
  getPrePayData()
}
const currentChargeType = ref('现金')
const changeType = (name: string) => {
  currentChargeType.value = name
}

const paywaytype = ref('1')
const paywaytypeChange = (value: any) => {
  refundData.value.forEach((elementAll: any) => {
    elementAll.returnWay = value
  })

  switch (value) {
    case '1':
      break

    default:
      break
  }
}
const receive = ref('')
const refundData = ref([])
const selectablejisuna = (row: any) => {
  return sumRefund.value < Math.abs(props.refund) || row.checkStatus
}
const refundCalculation = reactive({
  prepayAmount: 0,
  prepayAmountAll: 0,
})
const prePayDataloading = ref(false)
const getPrePayData = () => { }

const sumRefundold = ref(0)
const sumRefund = ref(0)

const selectionChange = (newSelection: any[]) => { }

const collectFee = () => {
  console.log('收费')
}
const closedAct = () => {
  visible.value = false
}
//
// onBeforeMount(() => {

// })
// onMounted(() => {

// })
/* 发票类型 */
const receiptType = ref('1')
const jump = () => {
  console.log('跳号')
}
const close = () => {
  visible.value = false
}

const submitFormloading = ref(false)
/* 返还 */
const submitForm = () => { }
defineExpose({ state })
</script>
<style lang="scss" scoped>
.refundContainer {
  font-weight: 400;
  font-family: '思源黑体 CN';

  .operate {
    display: flex;
    justify-content: space-between;
    height: 72px;
    margin: -16px 8px 0;

    .title {
      color: #666;
      font-size: 14px;
    }

    .castvalue {
      font-size: 20px;
    }

    .castvalueWeight {
      font-weight: 500;
    }

    .charge {
      margin-right: 8px;
      color: #e95657;
      font-size: 24px;
    }

    .refundType {
      width: 145px;
      margin-left: 8px;
    }
  }

  .table-container {
    height: 310px;

    :deep(.el-table__row) {
      height: 40px !important;
    }

    :deep(.el-table__header-wrapper .el-checkbox) {
      visibility: hidden;
    }
  }

  .line-container {
    width: 100%;
    color: #999;
    font-weight: 400;
    font-size: 14px;
    font-family: '思源黑体 CN';
    text-align: center;
  }
}
</style>
