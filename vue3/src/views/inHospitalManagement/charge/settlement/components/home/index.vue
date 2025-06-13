<!--
 * @Author: hezhibo@bjgoodwill.com
 * @Date: 2025-02-10 14:21:15
 * @Description: 住院结算
-->
<template>
  <div class="hospitalizationSettlement-container">
    <div class="operate-container">
      <el-space>
        <el-input v-model="queryForm.searchValue" style="width: 304px" placeholder="请输入" class="searchValue"
          @keyup.enter="queryPatient" clearable>
          <template #prepend>
            <el-select v-model="queryForm.searchType" placeholder="" style="width: 115px">
              <el-option label="住院号" value="inpatientCode" />
              <el-option label="主索引" value="patCode" />
            </el-select>
          </template>
        </el-input>
        <el-button style="margin-right: 0" @click="queryPatient">查询</el-button>
      </el-space>
      <el-space>
        <span>
          <span>当前发票号: </span>
          <span style="color: #256d95">{{ 12312312312 }} </span>
        </span>
        <el-button style="margin-right: 0">跳号</el-button>
        <el-button style="margin-right: 0" @click="goSettle"> 结算</el-button>
        <el-button style="margin-right: 0" type="warning" @click="goSettleCancel">取消结算</el-button>
        <el-button style="margin-right: 0" @click="receiptVisible = true" v-if="receivable >= 0">补缴</el-button>
        <el-button style="margin-right: 0" @click="goRefund" v-if="receivable < 0">返费</el-button>
        <el-icon @click="hipPatientList">
          <UserFilled />
        </el-icon>
      </el-space>
    </div>
    <!-- // TODO 患者信息 -->
    <div>患者信息</div>
    <div class="settle-container">
      <div class="settle-operate-container">
        <el-space class="costcollect" :size="16">
          <span class="type"> {{ currentPatient?.feeType }}</span>
          <span style="color: rgba(107.06, 150.91, 172.83, 30%)">|</span>
          <span>费用总额:
            <span class="costValue">{{ econDetailsInfo.settleAmount }}</span>
          </span>
          <!-- <span>账户支付: <span class="costValue">0.00</span>
                    </span> -->
          <span>自付:
            <span class="costValue">{{ econDetailsInfo.settleAmount }}</span>
          </span>
          <!-- <span>报销: <span class="costValue">0.00</span></span> -->
        </el-space>
        <el-space>
        </el-space>
        <div class="patientState">
          <!-- // 患者 结算、支付状态 -->
        </div>
      </div>

      <el-row :gutter="16" class="settle-table-container">
        <el-col :span="12" style="height: 100%">
          <el-table :data="itemSettlementData" height="100%" show-overflow-tooltip
            :style="{ 'border-right': '1px solid #eeeeee', height: '100%' }">
            <el-table-column prop="nameLeft" label="项目" min-width="100" />
            <el-table-column prop="costLeft" label="金额" width="121" align="right" header-align="left" />
            <el-table-column class-name="nameRight" prop="nameRight" label="项目" min-width="100" />
            <el-table-column prop="costRight" label="金额" width="121" align="right" header-align="left" />
            <template v-slot:empty>
              <div>
                <el-icon icon-class="hipNull" height="160px" width="160px" />
                <div class="empty-text-sty">暂无数据</div>
              </div>
            </template>
          </el-table>
        </el-col>
        <el-col :span="12" style="height: 100%">
          <el-table :data="reimbursementMethodData" style="width: 100%" height="100%" show-overflow-tooltip>
            <el-table-column prop="nameLeft" label="报销方式" min-width="100" />
            <el-table-column prop="costLeft" label="金额" width="123" align="right" header-align="left" />
            <el-table-column class-name="nameRight" prop="nameRight" label="报销方式" min-width="100" />
            <el-table-column prop="costRight" label="金额" width="123" align="right" header-align="left" />
            <template v-slot:empty>
              <div>
                <el-icon icon-class="hipNull" height="160px" width="160px" />
                <div class="empty-text-sty">暂无数据</div>
              </div>
            </template>
          </el-table>
        </el-col>
      </el-row>
    </div>
    <div class="accounts-container">
      <div class="receivable">
        <div class="receivable-operate">
          <el-space class="">
            <span>
              <span class="receivableName">{{ `${receivable >= 0 ? '应收' : '应返'}` }}: </span>
              <span class="receivableValue" :class="`${receivable >= 0 ? 'costValue' : 'receivableValueReturn'}`">
                {{ Math.abs(receivable) }}
              </span>
            </span>
          </el-space>
          <el-space class="">
            <el-button style="margin-right: 0" @click="derateVisible = true">添加减免</el-button>
          </el-space>
        </div>
        <div class="eceivableDetails">
          <div>
            <span>总额：</span>
            <span> {{ econDetailsInfo.settleAmount }}</span>
          </div>
          <div>
            <span>报销：</span>
            <span>0.00</span>
          </div>
          <div>
            <span>医保账户：</span>
            <span>0.00</span>
          </div>
          <div>
            <span>预缴金：</span>
            <span>{{ currentPatient?.prepayAmount }}</span>
          </div>
          <div>
            <span>减免：</span>
            <span>{{ currentPatient?.deduAmount }}</span>
          </div>
        </div>
      </div>
      <div class="advanced">
        <div class="advanced-operate">
          <el-space class="advancedTotal" :size="16">
            <span>预缴金合计金额:
              <span class="costValue">{{ currentPatient?.prepayAmountAll }}</span></span>
            <span>余额: <span class="costValue">{{ currentPatient?.prepayAmount }}</span></span>
          </el-space>
          <el-space class="" :size="20">
            <el-icon size="20" color="#999999" icon-class="hipRefresh" style="cursor: pointer" @click="getPrePayData" />
            <el-button style="margin-right: 0">预缴金管理</el-button>
          </el-space>
        </div>
        <div class="advancedData-container">
          <el-table :data="prePayData" v-loading="prePayDataloading" style="width: 100%" height="100%"
            show-overflow-tooltip>
            <el-table-column prop="paywayName" label="支付方式" width="147" />
            <el-table-column prop="amount" label="金额" align="right" header-align="left" width="93" />
            <el-table-column prop="remainingAmount" label="可用金额" width="94" align="right" header-align="left" />
            <el-table-column prop="remark" label="备注" min-width="100" />
            <el-table-column prop="createdDate" label="充值时间" width="143" />
            <template v-slot:empty>
              <div>
                <el-icon icon-class="hipNull" height="160px" width="160px" />
                <div class="empty-text-sty">暂无数据</div>
              </div>
            </template>
          </el-table>
        </div>
      </div>
    </div>
    <receipt v-model:visible="receiptVisible" :settleId="econDetailsInfo.settleId" :receivable="receivable"
      :visitCode="currentPatient?.visitCode || ''" :patCode="currentPatient?.patCode || ''" />
    <refund v-model:visible="refundVisible" :refund="receivable" :settleId="econDetailsInfo.settleId"
      :visitCode="currentPatient?.visitCode || ''" :patCode="currentPatient?.patCode || ''" />
    <patientList v-model:showDrawer="patientListDrawer" @settling="settling" @paying="paying" />
    <preSettlement v-model:showDrawer="preSettlementDrawer" chargeTypeCode="01" />
    <!--preSettlement 的 isEdit 提交状态 -->
    <invoiceSkip v-model:visible="invoiceSkipVisible" />
    <derate v-model:visible="derateVisible" @derateOk="derateOk" />
    <patientListDialog v-model:visible="patientListDialogVisible" :patientList="queryPpatientList" />
  </div>
</template>
<script setup>
import {
  computed,
  getCurrentInstance,
  onBeforeMount,
  onMounted,
  reactive,
  ref,
  watchEffect,
} from 'vue'
import {
  receipt,
  refund,
  patientList,
  invoiceSkip,
  derate,
  patientListDialog,
  preSettlement,
} from './components/index'

import { ElMessage } from 'element-plus'
const { proxy } = getCurrentInstance()
const emits = defineEmits([])
const props = defineProps({})
const state = reactive({})
/* 查询患者 */
const queryForm = reactive({
  searchValue: '',
  ward: '',
  inPatientId: '',
  searchType: 'inpatientCode',
})
const queryPpatientList = ref([]) //查询到的患者列表
const currentPatient = ref() // 当前
const queryPatient = () => {
  //  查询患者接口
  //1. 根据输入精确匹配，根据主索引查找患者时，如果有多条住院记录，弹出患者选择控件
  getPatInfo({
    inpatientCode: queryForm.searchType == 'inpatientCode' ? queryForm.searchValue : undefined,
    patCode: queryForm.searchType == 'patCode' ? queryForm.searchValue : undefined,
  })
    .then((res) => {
      if (res && res.length == 1) {
        currentPatient.value = res[0]
        try {
          if (currentPatient.value) {
            getEconDetails()
            getPrePayData()
            patientsBarRef.value.fetchPatientInfoById(currentPatient.value.visitCode)
          }
        } catch (error) {
          console.log(error)
        }
      } else if (res && res.length > 1) {
        console.log(res)
        queryPpatientList.value = res

        patientListDialogVisible.value = true
      } else {
        ElMessage({
          message: '未查询到患者！',
          type: 'info',
          grouping: true,
          showClose: true,
        })
        queryPpatientList.value = []
        currentPatient.value = {}
      }
    })
    .catch((error) => {
      console.log(error)
    })
}
/* 患者费用信息 */
const econDetailsInfo = ref({
  groupSystemClassTo: {
    billAllToListL: [],
    sumCosts: 0,
    systemClassCostMap: {},
  },
  settleId: '',
  settleAmount: 0, //费用总额
})
const getEconDetails = () => {
  if (currentPatient.value) {
    getPatEconSettleInfo(currentPatient.value).then((res) => {
      econDetailsInfo.value = res
    })
  }
}
watchEffect(() => {
  try {
    if (currentPatient.value && currentPatient.value?.feeType && currentPatient.value?.visitCode) {
      // 计算返回支付金额
      amountCalculation({
        // settleAmount: econDetailsInfo.value.settleAmount,
        // miReimburse: 0,
        // acctPay: 0,
        // psnCashPay: 0,// econDetailsInfo.value.settleAmount,
        // prepayAmount: currentPatient.value?.prepayAmountAll || 0,
        // deduAmount: currentPatient.value?.deduAmount || 0,
        // 增加流水号 费别
        feeType: currentPatient.value?.feeType,
        visitCode: currentPatient.value?.visitCode,
        rcptFlag: currentPatient.value?.rcptFlag,
      }).then((res) => {
        console.log(res)
        receivable.value = res.amount
        // res.flag
      })
    }
  } catch (error) {
    console.log(error)
  }
})
// const calculation = () => {

// }
/*预交金信息 */
const prePayData = ref([])

const prePayDataloading = ref(false)

const getPrePayData = () => {
  if (currentPatient.value && currentPatient.value?.visitCode) {
    prePayDataloading.value = true
    getPrePayInfo(currentPatient.value?.visitCode)
      .then((res) => {
        prePayData.value = res.prepayList
          ; (currentPatient.value).prepayAmount = res.remainingAmountSum //	余额汇总
          ; (currentPatient.value).prepayAmountAll = res.amountSum //金额汇总
        prePayDataloading.value = false
      })
      .finally(() => {
        prePayDataloading.value = false
      })
  }
}

const patientsBarRef = ref()

const hipPatientList = () => {
  patientListDrawer.value = true
}
/* 去结算 */
const settling = (row) => {
  queryForm.searchType = 'inpatientCode'
  queryForm.searchValue = row.inpatientCode
  queryPatient()
  patientListDrawer.value = false
}
/* 去缴费 */
const paying = (row) => {
  queryForm.searchType = 'inpatientCode'
  queryForm.searchValue = row.inpatientCode
  // TODO 去缴费的逻辑？
  queryPatient()
  patientListDrawer.value = false
}
const childDatas = ref([
  {
    name: '迪丽热巴之子1',
    type: '自费',
    cost: '1500',
  },
  {
    name: '迪丽热巴之子2',
    type: '自费',
    cost: '1500',
  },
  {
    name: '迪丽热巴之子2',
    type: '自费',
    cost: '1500',
  },
  {
    name: '迪丽热巴之子2',
    type: '自费',
    cost: '1500',
  },
  {
    name: '迪丽热巴之子2',
    type: '自费',
    cost: '1500',
  },
])

/*费用 */
const treatHospitalizedData = ref([])
const systemClassCostMap = computed(() => {
  const classCost = econDetailsInfo.value?.groupSystemClassTo?.systemClassCostMap
  return classCost
    ? Object.keys(classCost).map((key) => {
      return { name: key, cost: classCost[key] }
    })
    : []
})
/* 项目数据 */
const itemSettlementData = computed(() => {
  const exchangeData = []
  if (systemClassCostMap.value.length > 7) {
    let limitcenter = Math.ceil(systemClassCostMap.value.length / 2)
    limitcenter = limitcenter > 7 ? limitcenter : 7
    for (let index = 0; index < limitcenter; index++) {
      exchangeData.push({
        nameLeft: systemClassCostMap.value[index].name,
        costLeft: systemClassCostMap.value[index].cost,
        nameRight: systemClassCostMap.value[index + limitcenter]?.name,
        costRight: systemClassCostMap.value[index + limitcenter]?.cost,
      })
    }
  } else {
    systemClassCostMap.value.forEach((element) => {
      exchangeData.push({
        nameLeft: element.name,
        costLeft: element.cost,
      })
    })
  }

  return exchangeData
})
const reimbursementMethod = ref([
  // { name: '基金支付总额', cost: '600' },
  // { name: '医保其他支付总额', cost: '150' },
  // { name: '医保账户总额', cost: '600' },
  // { name: '现金总额', cost: '600' },
])
const reimbursementMethodData = computed(() => {
  const exchangeData = []
  // let limitcenter = Math.ceil(reimbursementMethod.value.length / 2)
  // for (let index = 0; index < limitcenter; index++) {
  //     exchangeData.push({
  //         nameLeft: reimbursementMethod.value[index].name,
  //         costLeft: reimbursementMethod.value[index].cost,
  //         nameRight: reimbursementMethod.value[index + limitcenter]?.name,
  //         costRight: reimbursementMethod.value[index + limitcenter]?.cost,
  //     }
  //     )
  // }
  return exchangeData
})

// onBeforeMount(() => {

// })
// onMounted(() => {

// })
/* 先结算后 收款 */
const receiptVisible = ref(false)

const goSettle = () => {
  // receiptVisible.value = true
  ElMessage({
    message: '结算成功！',
    type: 'success',
    grouping: true,
    showClose: true,
  })
}
/* 结算取消 */

const goSettleCancel = () => {
  ElMessage({
    message: '结算取消成功！',
    type: 'success',
    grouping: true,
    showClose: true,
  })
}
const receivable = ref(0)
//应收
const amount = computed(() => {
  return econDetailsInfo.value.settleAmount - (currentPatient.value?.deduAmount || 0)
})
const refundVisible = ref(false) // 返款
const goRefund = () => {
  // receiptVisible.value = true
  refundVisible.value = true
}
/* 患者列表 ,抽屉*/
const patientListDrawer = ref(false)
/* 患者列表，弹窗 */
const patientListDialogVisible = ref(false)
/* 医保 */
const preSettlementDrawer = ref(false)
/* 医保详情 */

/* 减免 */
const derateVisible = ref(false)
const derateOk = (amount) => {
  console.log(amount)
  derateVisible.value = false
  if (currentPatient.value) {
    currentPatient.value.deduAmount = amount
  }
}
/* 取消医保出院 */
const cancelDischarge = async () => {
  await proxy.$hipMessageBox({
    title: '提示',
    type: 'warn',
    content: '是否取消患者的医保出院登记？',
    onConfirm: () => {
      console.log('点击知道了')
    },
    onCancel: () => {
      console.log('点击关闭')
    },
  })
}

/* 发票跳号 */
const invoiceSkipVisible = ref(false)
defineExpose({ state })
</script>
<style lang="scss" scoped>
.hospitalizationSettlement-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  font-weight: 400;
  font-family: '思源黑体 CN';

  // border: 1px solid blue;
  background-color: #f1faff;

  >div {
    flex: none;
  }

  .operate-container {
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: 56px;
    padding: 0 16px;
    background-color: #fff;
    border-bottom: 2px solid #e9f3f9;

    :deep(.el-input) {
      .el-input-group__prepend {
        div.el-select {
          .el-select__wrapper {
            background-color: #fff !important;
          }
        }
      }
    }
  }

  .settle-container {
    // border: 1px solid #DDDDDD;
    // border-radius: 8px;
    width: 100%;
    height: 323px;
    margin-bottom: 8px;
    background-color: #fff;

    // padding: 0 16px;
    // border: 1px solid greenyellow;

    .settle-operate-container {
      position: relative;
      display: flex;
      flex: none;
      align-items: center;
      justify-content: space-between;
      width: 100%;
      width: calc(100% - 32px);
      height: 56px;

      // border: 1px solid black;
      padding: 0 16px;

      /* 患者状态 */
      .patientState {
        position: absolute;
        top: -56px;
        right: 16px;
      }
    }

    :deep(.settle-table-container) {
      flex: auto;
      width: calc(100% - 32px);
      width: 100%;
      height: calc(100% - 56px);

      // border: 1px solid red;
      margin-left: 0 !important;
      padding: 0 8px 14px;

      .table-child {
        height: 100%;

        // border: 1px solid red;
      }

      .nameRight {
        border-left: 1px solid !important;
      }

      th.nameRight {
        .cell {
          &::before {
            border: 0 !important;
          }
        }
      }
    }

    .costcollect {
      .type {
        color: #333;
      }

      :nth-child(2) {
        margin-left: 26px;
      }

      span {
        color: #666;
      }
    }
  }

  .accounts-container {
    display: flex;
    flex: auto;

    // margin: 0 8px;
    width: 100%;
    height: 321px;

    // border: 1px solid black;
    // background: #FFFFFF;
    background-color: #f1faff;

    .receivable {
      flex: none;
      width: 426px;
      padding: 0 16px;
      background-color: #fff;

      // border: 1px solid #DDDDDD;

      .receivable-operate {
        display: flex;
        justify-content: space-between;
        height: 56px;
        border-bottom: 2px solid #e9f3f9;

        .receivableName {
          color: #666;
          font-size: 20px;
        }

        .receivableValue {
          font-size: 32px;
        }

        .receivableValueReturn {
          color: #e95657;
          font-size: 32px;
        }
      }

      .eceivableDetails {
        padding-top: 29px;

        >div {
          height: 40px;
          margin: 0 20px;
          font-size: 22px;
          line-height: 40px;
          border-bottom: 2px solid #eee;

          span:first-child {
            display: inline-block;
            width: 126px;
            color: #666;
            font-weight: 400;
            text-align: right;
          }

          span:nth-child(2) {
            display: inline-block;
            width: 80px;
            color: #333;
            font-weight: 400;
            text-align: right;
          }

          // span:nth-child(2) {
          //     color: #333333;
          // }
          span {
            &:nth-child(2) {
              min-width: 120px;
              color: #333;
              text-align: right;

              // border: 1px solid red
            }
          }
        }
      }
    }

    .advanced {
      flex: auto;
      width: 100px;
      margin-left: 8px;
      background-color: #fff;

      .advanced-operate {
        display: flex;
        justify-content: space-between;
        height: 56px;
        padding: 0 16px;

        .advancedTotal {
          color: #666;
        }
      }

      .advancedData-container {
        height: calc(100% - 64px);
        padding: 0 16px 8px;
      }
    }

    .childs {
      flex: none;
      width: 188px;

      // border: 1px solid rgba(237, 241, 5, 0.894);
      margin-left: 8px;
      padding: 8px;
      background: #fff;

      .child {
        width: 170px;
        padding: 8px;
        color: #666;
        background: #fff;
        border: 1px solid #ddd;
        border-radius: 4px;
        box-shadow: 0 2px 2px 0 rgba(57.55, 69.04, 86.28, 15%);

        .name {
          color: #333;
          font-weight: 500;
          font-size: 16px;
        }

        .typeValue {
          color: #333;
        }
      }
    }
  }

  .costValue {
    color: #ff8616 !important;
  }
}
</style>
