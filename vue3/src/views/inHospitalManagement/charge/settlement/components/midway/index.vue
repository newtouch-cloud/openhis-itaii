<!--
 * @Author: hezhibo@bjgoodwill.com
 * @Date: 2025-03-10 17:21:55
 * @Description:
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

        <!-- 读卡 -->
      </el-space>
      <el-space>
        <span>
          <span>当前发票号: </span>
          <span style="color: #256d95">{{ 1231231231 }} </span>
        </span>
        <el-button style="margin-right: 0">跳号</el-button>

        <el-button style="margin-right: 0" @click="goSettle">结算</el-button>
        <el-button style="margin-right: 0" type="warning" @click="goSettleCancel">取消结算</el-button>
        <el-button style="margin-right: 0" @click="receiptVisible = true">补缴</el-button>
        <el-icon size="20" color="#999999" icon-class="hipPatientList" style="cursor: pointer"
          @click="hipPatientList" />
      </el-space>
    </div>
    <!-- // TODO 患者信息 -->
    <div>患者信息</div>
    <div class="settle-container">
      <div class="settle-operate-container">
        <el-space class="costcollect" :size="16">
          <span class="type">基线版医保</span>
          <span style="color: rgba(107.06, 150.91, 172.83, 30%)">|</span>
          <span>总费用:
            <span class="costValue">{{ econDetailsInfo.settleAmount }}</span>
          </span>
          <!-- 费用总额 -->
          <span>勾选未结费用:
            <span class="costValue">{{ econDetailsInfo.unRcptAmount }}</span>
          </span>
          <span>勾选已结费用:
            <span class="costValue">{{ econDetailsInfo.rcptAmount || 0 }}</span>
          </span>
          <span>预交余额:
            <span class="costValue">{{ prePayData.remainingAmountSum }}</span>
          </span>
          <!-- <span
            >账户支付:
            <span class="costValue">{{ econDetailsInfo.settleAmount }}</span>
          </span> -->
          <span>自付:
            <span class="costValue">{{ econDetailsInfo.psnPartAmt }}</span>
          </span>

          <!-- <span
            >报销:
            <span class="costValue">{{ econDetailsInfo.settleAmount }}</span>
          </span> -->
        </el-space>
        <el-space>
          <el-button style="margin-right: 0" :disabled="!currentPatient?.visitCode"
            @click="selectSettlementDrawer = true">选择结算费用</el-button>
          <!-- 1. 根据患者费别显示按钮，若患者费别为“自费”则不显示【预结算】按钮；若患者费别为医保则显示【预结算】按钮。 -->
          <el-button style="margin-right: 0" @click="preSettlementDrawer = true">预结算</el-button>
          <!-- 1. 已预结算的患者，重新进行预结算，则点击重新预结算 -->
          <!-- <el-button style="margin-right: 0px;" @click="preSettlementDrawer = true">重新预结算</el-button>
                    <el-button style="margin-right: 0px;" @click="settlementDetailsVisible = true">报销详情</el-button>
                    <el-button style="margin-right: 0px;" @click="cancelDischarge">取消医保出院</el-button> -->
        </el-space>
        <div class="patientState">
          <el-icon size="52" color="#FF8616" icon-class="hipUnpaid" style="cursor: pointer" />
          <el-icon size="52" color="#E95657" icon-class="hipUnsettled" style="cursor: pointer" />
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
          </el-table>
        </el-col>
        <el-col :span="12" style="height: 100%">
          <el-table :data="reimbursementMethodData" style="width: 100%" height="100%" show-overflow-tooltip>
            <el-table-column prop="nameLeft" label="报销方式" min-width="100" />
            <el-table-column prop="costLeft" label="金额" width="123" align="right" header-align="left" />
            <el-table-column class-name="nameRight" prop="nameRight" label="报销方式" min-width="100" />
            <el-table-column prop="costRight" label="金额" width="123" align="right" header-align="left" />
          </el-table>
        </el-col>
      </el-row>
    </div>
    <div class="accounts-container">
      <div class="receivable">
        <div class="receivable-operate">
          <el-space class="">
            <span>
              <span class="receivableName">应收: </span>
              <span class="receivableValue costValue">{{
                Math.abs(econDetailsInfo.settleCosts)
                }}</span>
            </span>
          </el-space>
          <el-space class="">
            <el-button style="margin-right: 0" @click="derateVisible = true">添加减免</el-button>
          </el-space>
        </div>
        <div class="eceivableDetails">
          <div>
            <span>总额：</span>
            <span>{{ econDetailsInfo.settleAmount || '0.00' }}</span>
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
            <span>{{ prePayData.remainingAmountSum || '0.00' }}</span>
          </div>
          <div>
            <span>减免：</span>
            <span>{{ currentPatient?.deduAmount || '0.00' }}</span>
          </div>
        </div>
      </div>
      <div class="advanced">
        <div class="advanced-operate">
          <el-space class="advancedTotal" :size="16">
            <span>预缴金合计金额: <span class="costValue">{{ prePayData.amountSum }}</span></span>
            <span>余额: <span class="costValue">{{ prePayData.remainingAmountSum }}</span></span>
            <span>已使用: <span class="costValue">{{ prePayData.alreadyUsedAmount }}</span></span>
          </el-space>
          <el-space class="" :size="20">
            <el-icon size="20" color="#999999" icon-class="hipRefresh" style="cursor: pointer" @click="getPrePayData" />
            <el-button style="margin-right: 0">预缴金管理</el-button>
          </el-space>
        </div>
        <div class="advancedData-container">
          <el-table :data="prePayData.prepayList" style="width: 100%" height="100%" show-overflow-tooltip>
            <el-table-column prop="paywayName" label="支付方式" width="147" />
            <el-table-column prop="amount" label="金额" align="right" header-align="left" width="93" />
            <el-table-column prop="remainingAmount" label="可用金额" width="94" align="right" header-align="left" />
            <el-table-column prop="remark" label="备注" min-width="100" />
            <el-table-column prop="createdDate" label="充值时间" width="160" :formatter="(row: any) => {
              return row.createdDate && dayjs(row.createdDate).format('YYYY/MM/DD HH:mm')
            }
              " />
            <template v-slot:empty>
              <div>
                <el-icon icon-class="hipNull" height="160px" width="160px" />
                <div class="empty-text-sty">暂无数据</div>
              </div>
            </template>
          </el-table>
        </div>
      </div>
      <!-- <div class="childs" v-if="childDatas.length > 0">
                <el-scrollbar>
                    <el-space wrap :size="8" directio="vertical">
                        <div class="child" v-for="child in childDatas" :key="child.name">
                            <el-space wrap :size="8" directio="vertical">
                                <div class="name">{{ child.name }}</div>
                                <div>医保类型：<span class="typeValue">{{ child.type }}</span></div>
                                <div>费用合计：<span class="costValue">{{ child.cost }}</span> </div>
                            </el-space>

                        </div>
                    </el-space>
                </el-scrollbar>
            </div> -->
    </div>


    <patientList v-model:showDrawer="patientListDrawer" @settling="settling" @paying="paying" />
    <!--preSettlement 的 isEdit 提交状态 -->
    <selectSettlement v-model:showDrawer="selectSettlementDrawer" :visit-code="currentPatient?.visitCode || ''"
      @select-ok="selectOk" />
  </div>
</template>
<script setup >
import { computed, getCurrentInstance, onBeforeMount, onMounted, reactive, ref } from 'vue'
import dayjs from 'dayjs'

import {
  patientList,
  selectSettlement,
} from './components/index'

import { ElMessage } from 'element-plus'
import { method } from 'lodash-es'

const { proxy } = getCurrentInstance() as any
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
const queryPpatientList = ref<Array<any>>([]) //查询到的患者列表
const currentPatient = ref<Partial<any>>() // 当前
const queryPatient = () => {
  // TODO 查询患者接口
  //1. 根据输入精确匹配，根据主索引查找患者时，如果有多条住院记录，弹出患者选择控件
}
/* 当前选择的待结算信息 */
const currentSelectSettlement = reactive<any>({
  selectSettlementData: [],
  deduAmount: 0,
})
const selectOk = (res: any) => {
  selectSettlementDrawer.value = false
  currentSelectSettlement.selectSettlementData = res
  getEconDetails(currentSelectSettlement.selectSettlementData)
}
const isHaveSettleId = () => {
  if (currentPatient.value) {
    if (currentPatient.value.settleId) {
      feeInformationSettle(currentPatient.value).then((res: any) => {
        econDetailsInfo.value = res
      })
    } else {
      selectSettlementDrawer.value = true
    }
  }
}
/* 患者费用信息 */
const econDetailsInfo = ref({
  // groupSystemClassTo: {
  //   billAllToListL: [],
  //   sumCosts: 0,
  //   systemClassCostMap: {},
  // },
  settleAmount: 0, //费用信息-已选择费用
  unRcptAmount: 0, //费用信息-未结算费用
  rcptAmount: 0, //	费用信息-已结算费用
  acctPay: 0, //	费用信息-账户支付
  psnPartAmt: 0, //	费用信息-医保自付
  systemClassCostMap: {}, //	系统分类名称,实收总金额
  settleCosts: 0, //	结算信息-应收金额
  // settleCosts: 0, //减免金额 //TODO
  //明细信息
  econIpdMidwayDetailAsToList: [
    // {
    //   feeClassName: '',
    //   priceItemCode: '',
    //   priceItemName: '',
    //   spec: '',
    //   price: '',
    //   num: 0,
    //   costs: 0,
    // },
  ],
  econIpdBillIdList: [], //住院账单标识列表,
})
const getEconDetails = (res: any) => {
  feeInformationUnSettle(res).then((res: any) => {
    econDetailsInfo.value = res
  })
}

/*预交金信息 */
const prePayData = reactive({
  remainingAmountSum: 0,
  amountSum: 0,
  alreadyUsedAmount: 0,
  prepayList: [],
})
const prePayDataloading = ref(false)
const getPrePayData = () => {
  if (currentPatient.value && currentPatient.value?.visitCode) {
    prePayDataloading.value = true
    // TODO 预结算
  }
}

/*费用 */
const treatHospitalizedData = ref([])
const systemClassCostMap = computed(() => {
  const classCost: any = econDetailsInfo.value?.systemClassCostMap
  return classCost
    ? Object.keys(classCost).map((key) => {
      return { name: key, cost: classCost[key] }
    })
    : []
})
/* 项目数据 */
const itemSettlementData = computed(() => {
  const exchangeData: any[] = []
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
    systemClassCostMap.value.forEach((element: any) => {
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
  const exchangeData: any[] = []
  return exchangeData
})

// onBeforeMount(() => {

// })
// onMounted(() => {

// })
const patientsBarRef = ref()

const hipPatientList = () => {
  patientListDrawer.value = true
}

/* 去结算 */
const settling = (row: any) => {
  queryForm.searchType = 'inpatientCode'
  queryForm.searchValue = row.inpatientCode
  queryPatient()
  patientListDrawer.value = false
}
/* 去缴费 */
const paying = (row: any) => {
  queryForm.searchType = 'inpatientCode'
  queryForm.searchValue = row.inpatientCode
  // TODO 去缴费的逻辑？
  queryPatient()
  patientListDrawer.value = false
}
const goSettle = () => {
  // receiptVisible.value = true
  settlement({
    visitCode: currentPatient.value?.visitCode, //	经济结算-住院流水号s
    patIndexCode: currentPatient.value?.patCode, //经济结算-患者信息编码
    feeType: currentPatient.value?.feeType, //	经济结算-费别
    deduAmount: currentPatient.value?.deduAmount, //、经济结算-减免金额
    deduType: currentPatient.value?.deduType, //	经济结算-减免类型
    prepayAmount:
      econDetailsInfo.value.settleCosts == 0
        ? econDetailsInfo.value.settleCosts
        : prePayData.remainingAmountSum, //	经济结算-结算使用预交金金额
    amount: econDetailsInfo.value.settleAmount, //	经济结算-应收费用总额
    paySelfAmount: econDetailsInfo.value.psnPartAmt, //经济结算-自付金额
    costs: econDetailsInfo.value.settleAmount, //	经济结算-结算费用总额
    econIpdBillIdList: econDetailsInfo.value.econIpdBillIdList,
  }).then((res: any) => {
    console.log(res)
    // econDetailsInfo.value.settleId = res.id;
    ElMessage({
      message: '结算成功！',
      type: 'success',
      grouping: true,
      showClose: true,
    })
    queryPatient()
    if (res.payFlag) {
      // TODO 调用打印
    }
    //付完成标志res.payFlag true:调用打印
    // if (currentPatient.value) {
    //     currentPatient.value.rcptFlag = res.rcptFlag
    //     currentPatient.value.rcptPayFlag = res.rcptPayFlag
    //     currentPatient.value.examineFlag = res.examineFlag
    // }
    if (econDetailsInfo.value.settleCosts < 0) {
      /* 需要交费时 交钱 */
      receiptVisible.value = true
    }
  })
}
/* 结算取消 */

const goSettleCancel = () => {
  // settlementCancel({
  //     visitCode: currentPatient.value?.visitCode,//	经济结算-住院流水号s
  //     feeType: currentPatient.value?.feeType,//	经济结算-费别
  //     settleId: econDetailsInfo.value.settleId
  // }).then((res: any) => {
  //     ElMessage({
  //         message: '结算取消成功！',
  //         type: 'success',
  //         grouping: true,
  //         showClose: true,
  //     });
  //     (currentPatient.value as any).rcptFlag = false;
  //     getEconDetails()
  //     //TODO 结算取消成功，处理后续
  // })
}
/* 收款 */
const receiptVisible = ref(false)
const refundVisible = ref(false) // 返款

/* 患者列表 ,抽屉*/
const patientListDrawer = ref(false)
/* 患者列表，弹窗 */
const patientListDialogVisible = ref(false)
/* 医保 */
const preSettlementDrawer = ref(false)
/* 选择费用 */
const selectSettlementDrawer = ref(false)
/* 减免 */
const derateVisible = ref(false)
const derateOk = (amount: number) => {
  derateVisible.value = false
  if (currentPatient.value) {
    currentPatient.value.deduAmount = amount
    // TODO
    // getEconDetails(currentSelectSettlement.selectSettlementData);
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
