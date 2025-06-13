<!--
 * @Author: hezhibo@bjgoodwill.com
 * @Date: 2025-03-10 17:21:55
 * @Description:
-->
<template>
  <div class="hospitalizationSettlementCancel-container">
    <div class="operate-container">
      <el-space>
        <el-input v-model="queryForm.searchValue" style="width: 304px" placeholder="请输入" class="searchValue"
          @keyup.enter="queryPatient" clearable>
          <template #prepend>
            <el-select v-model="queryForm.searchType" placeholder="" style="width: 115px">
              <el-option label="发票号" value="receipt" />
              <el-option label="住院号" value="inpatientCode" />
            </el-select>
          </template>
        </el-input>
        <el-button style="margin-right: 0px" @click="queryPatient">查询</el-button>
      </el-space>
      <el-space>
        <el-button style="margin-right: 0px" @click="goSettleCancel"
          :disabled="!currentPatient?.visitCode">取消结算</el-button>
      </el-space>
    </div>
    <div> //患者信息</div>
    <!-- // TODO el-dialog -->
    <div class="billData-container">
      <el-table :data="queryPpatientList" height="186px" show-overflow-tooltip :style="{ height: '100%' }">
        <el-table-column prop="name" label="姓名" min-width="131" />
        <el-table-column prop="inpatientCode" label="住院号" width="167" />
        <el-table-column prop="settleNo" label="发票号" min-width="210" />
        <el-table-column prop="costs" label="金额" width="160" align="right" header-align="left" :formatter="(row) => {
          return row.costs && round(row.costs, 2).toFixed(2)
        }
          " />
        <el-table-column prop="feeTypeName" label="类别" min-width="155" />
        <el-table-column prop="settleTypeName" label="结算类型" min-width="174" />
        <el-table-column prop="createdDate" label="结算时间" min-width="194" :formatter="(row) => {
          return row.createdDate && dayjs(row.createdDate).format('YYYY/MM/DD HH:mm')
        }
          " />
        <el-table-column prop="createdStaffName" label="结算人" min-width="145" />
        <template v-slot:empty>
          <div>
            <div class="empty-text-sty">暂无数据</div>
          </div>
        </template>
      </el-table>
    </div>
    <div class="pay-container">
      <div class="payList-container">
        <div class="totals">
          <el-space class="costcollect" :size="32">
            <span>费用总额: <span class="costValue">{{ econDetailsInfo.totalAmount }}</span></span>
            <span>预缴金: <span class="costValue">{{ econDetailsInfo.prepayAmount }}</span></span>
            <span>已补收: <span class="costValue">{{ econDetailsInfo.payAmount }}</span></span>
          </el-space>
        </div>
        <div class="payListData-container">
          <div>
            <el-table :data="econDetailsInfo.bpubTypeList" height="186px" show-overflow-tooltip
              :style="{ height: '100%' }">
              <el-table-column prop="nameLeft" label="缴费方式" width="165" />
              <el-table-column prop="costRight" label="金额" width="159" align="right" header-align="left" />
              <template v-slot:empty>
                <div>
                  <div class="empty-text-sty">暂无数据</div>
                </div>
              </template>
            </el-table>
          </div>
          <div>
            <el-table :data="econDetailsInfo.econIpdSettlePayAsToList" height="186px" show-overflow-tooltip
              :style="{ height: '100%' }">
              <el-table-column prop="paywayName" label="缴费方式" width="165" />
              <el-table-column prop="amount" label="金额" min-width="159" align="right" header-align="left" />
              <el-table-column prop="returnWay" label="返还方式" min-width="145">
                <template #default="{ row }">
                  <el-select v-model="row.returnWay" placeholder="返还方式" style="width: 100%">
                    <el-option label="转押金" value="1" />
                  </el-select>
                </template>
              </el-table-column>
              <template v-slot:empty>
                <div>
                  <div class="empty-text-sty">暂无数据</div>
                </div>
              </template>
            </el-table>
          </div>
        </div>
      </div>
      <div class="currentbill-container">
        <div class="totals">
          <span class="countTitle">退还金额: <span class="countValue">1500.00</span></span>
        </div>
        <div class="eceivableDetails">
          <div>
            <span>转押金：</span>
            <span>2490.00</span>
          </div>
          <div>
            <span>微信：</span>
            <span>200.00</span>
          </div>
          <div>
            <span>支付宝：</span>
            <span>1500.00</span>
          </div>
          <div>
            <span>银行卡：</span>
            <span>2000.00</span>
          </div>
          <div>
            <span>现金：</span>
            <span>-</span>
          </div>
        </div>
      </div>
    </div>
    <patientListDialog v-model:visible="patientListDialogVisible" :patientList="queryPpatientList" />
  </div>
</template>
<script setup >
import { getCurrentInstance, onBeforeMount, onMounted, reactive, ref } from 'vue'
import { getPatInfo, getPatInpatientEconInfo, cancelSettle } from '@/api/inpatient/cancelSettlement'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'
import { round } from 'lodash-es'
// const { proxy } = getCurrentInstance();
const emits = defineEmits([])
const props = defineProps({})
const state = reactive({})
/* 查询患者 */
const queryForm = reactive({
  searchValue: '',
  ward: '',
  inPatientId: '',
  searchType: 'receipt',
})
const patientsBarRef = ref()
const queryPpatientList = ref<Array<any>>([])
const currentPatient = ref<Partial<any>>() // 当前
/* 查询患者 */
const queryPatient = () => {
  // TODO 查询患者接口
  //1. 根据输入精确匹配，根据主索引查找患者时，如果有多条住院记录，弹出患者选择控件
  getPatInfo({
    inpatientCode: queryForm.searchType == 'inpatientCode' ? queryForm.searchValue : undefined,
    invoiceNo: queryForm.searchType == 'receipt' ? queryForm.searchValue : undefined,
  })
    .then((res) => {
      if (res && res.length == 1) {
        queryPpatientList.value = res
        currentPatient.value = res[0]
        try {
          if (currentPatient.value) {
            getEconDetails()
            patientsBarRef.value.fetchPatientInfoById(currentPatient.value.visitCode)
          }
        } catch (error) {
          console.log(error)
        }
      } else if (res && res.length > 1) {
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
      queryPpatientList.value
    })
}

/* 患者费用信息 */
const econDetailsInfo = ref<{
  bpubTypeList: []
  econIpdSettlePayAsToList: Array<any>
  payAmount: 0
  prepayAmount: 0
  totalAmount: 0
}>({
  bpubTypeList: [],
  econIpdSettlePayAsToList: [],
  payAmount: 0,
  prepayAmount: 0,
  totalAmount: 0,
})
const getEconDetails = () => {
  if (currentPatient.value && currentPatient.value.settleId) {
    getPatInpatientEconInfo(currentPatient.value.settleId).then((res) => {
      econDetailsInfo.value = res
      econDetailsInfo.value.econIpdSettlePayAsToList =
        econDetailsInfo.value.econIpdSettlePayAsToList.map((i) => {
          return { ...i, returnWay: '1' }
        })
    })
  }
}
/* 弹窗 */
/* 患者列表，弹窗 */
const patientListDialogVisible = ref(false)
/* 结算取消 */

const goSettleCancel = () => {
  if (currentPatient.value?.visitCode) {
    cancelSettle({
      patIndexCode: currentPatient.value?.patCode,
      amount: econDetailsInfo.value.payAmount,
      visitCode: currentPatient.value?.visitCode, //	经济结算-住院流水号s
      feeType: currentPatient.value?.feeType, //	经济结算-费别
      settleId: currentPatient.value?.settleId,
      econCancelSettlePrepayAsTo: econDetailsInfo.value.econIpdSettlePayAsToList.map((i) => {
        return { id: i.id }
      }),
    }).then((res) => {
      ElMessage({
        message: '结算取消成功！',
        type: 'success',
        grouping: true,
        showClose: true,
      })
      getEconDetails()
      //TODO 结算取消成功，处理后续
    })
  }
}
// onBeforeMount(() => {

// })
// onMounted(() => {

// })
defineExpose({ state })
</script>
<style lang="scss" scoped>
.hospitalizationSettlementCancel-container {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  font-family: '思源黑体 CN';
  font-weight: 400;

  .operate-container {
    height: 56px;
    padding: 0 16px;
    display: flex;
    align-items: center;
    background-color: #ffffff;
    justify-content: space-between;
    border-bottom: 2px solid #e9f3f9;
    flex: none;

    :deep(.el-input) {
      .el-input-group__prepend {
        div.el-select {
          .el-select__wrapper {
            background-color: #ffffff !important;
          }
        }
      }
    }
  }

  .patientBar {
    box-shadow: 0px 2px 2px 0px rgba(199, 224, 240, 0.4);
    background: #ffffff;
    flex: none;
  }

  .billData-container {
    height: 154px;
    padding: 16px;
    border-bottom: 8px solid #f1faff;
    flex: none;
  }

  .pay-container {
    width: 100%;
    display: flex;
    flex: auto;
    height: 100px;

    .totals {
      height: 56px;
      border-bottom: 2px solid #f1faff;
      box-shadow: 0px 1px 1px 0px rgba(181.05, 210.63, 255, 0.25);
      line-height: 56px;
      padding: 0 16px;

      .costcollect {
        span {
          color: #666666;
        }

        .costValue {
          color: #ff8616 !important;
          font-size: 20px;
        }
      }

      .countTitle {
        font-size: 20px;
      }

      .countValue {
        font-size: 32px;
        color: #e95657;
        font-weight: 500;
      }
    }

    .payList-container {
      flex: auto;
      width: 100px;

      .payListData-container {
        width: calc(100% - 32px);
        padding: 8px 16px;
        display: flex;
        height: calc(100% - 74px);

        >div:first-child {
          width: 324px;
          flex: none;
        }

        >div:nth-child(2) {
          width: calc(100% - 340px);
          margin-left: 16px;
          flex: auto;

          .el-table {
            width: 100%;
          }
        }
      }
    }

    .currentbill-container {
      border-left: 8px solid #f1faff;
      width: 380px;
      flex: none;

      // border: 1px solid #DDDDDD;
      .eceivableDetails {
        padding-top: 29px;

        >div {
          border-bottom: 2px solid #eeeeee;
          height: 40px;
          line-height: 40px;
          margin: 0 24px;
          font-size: 22px;

          span:first-child {
            width: 138px;
            display: inline-block;
            text-align: right;
            font-weight: 400;
            color: #666666;
          }

          span:nth-child(2) {
            width: 80px;
            display: inline-block;
            text-align: right;
            font-weight: 400;
            color: #333333;
          }

          // span:nth-child(2) {
          //     color: #333333;
          // }
          span {
            &:nth-child(2) {
              color: #333333;
              min-width: 120px;
              text-align: right;
              // border: 1px solid red
            }
          }
        }
      }
    }
  }
}
</style>
