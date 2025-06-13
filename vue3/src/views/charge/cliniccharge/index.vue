<template>
  <div
    style="display: flex; justify-content: space-between"
    class="app-container"
    v-loading="readCardLoading"
    :element-loading-text="loadingText"
  >
    <el-card style="width: 30%">
      <template #header>
        <span style="vertical-align: middle">患者列表</span>
      </template>
      <div style="width: 100%">
        <el-input
          v-model="queryParams.searchKey"
          placeholder="请输入患者名/病历号"
          clearable
          style="width: 48%; margin-bottom: 10px; margin-right: 10px"
          @keyup.enter="getPatientList"
        >
          <template #append>
            <el-button icon="Search" @click="getPatientList" />
          </template>
        </el-input>
        <el-select
          v-model="queryParams.statusEnum"
          style="width: 48%; margin-bottom: 10px; margin-right: 10px"
          placeholder="收费状态"
          @change="getPatientList"
        >
          <el-option
            v-for="item in chargeStatusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <div style="width: 100%">
          <el-date-picker
            v-model="receptionTime"
            type="daterange"
            range-separator="~"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            placement="bottom"
            value-format="YYYY-MM-DD"
            style="width: 84%; margin-bottom: 10px; margin-right: 10px"
          />
          <el-button type="primary" style="margin-bottom: 10px" @click="getPatientList">
            搜索
          </el-button>
        </div>
        <el-table
          ref="patientListRef"
          height="620"
          :data="patientList"
          row-key="encounterId"
          @cell-click="clickRow"
          highlight-current-row
        >
          <el-table-column label="病历号" align="center" prop="encounterBusNo" />
          <el-table-column label="姓名" align="center" prop="patientName" />
          <!-- <el-table-column label="时间" align="center" prop="receptionTime" width="160">
            <template #default="scope">
              {{ formatDate(scope.row.receptionTime) }}
            </template>
          </el-table-column> -->
          <el-table-column label="收费状态" align="center" prop="statusEnum_enumText" />
        </el-table>
      </div>
    </el-card>
    <div style="width: 69%">
      <el-card style="margin-bottom: 20px">
        <template #header>
          <span style="vertical-align: middle">基本信息</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item label="姓名:">{{ patientInfo.patientName }}</el-descriptions-item>
          <el-descriptions-item label="性别:">
            {{ patientInfo.genderEnum_enumText }}
          </el-descriptions-item>
          <el-descriptions-item label="年龄:">{{ patientInfo.age }}</el-descriptions-item>
          <el-descriptions-item label="合同类型:">
            {{ patientInfo.categoryEnum_enumText }}
          </el-descriptions-item>
          <!-- <el-descriptions-item label="身份证号:">{{ patientInfo.idCard }}</el-descriptions-item> -->
          <!-- <el-descriptions-item label="手机号">{{ patientInfo.name }}</el-descriptions-item>
          <el-descriptions-item label="出生日期">{{ patientInfo.name }}</el-descriptions-item> -->
        </el-descriptions>
      </el-card>
      <el-card style="min-width: 1100px">
        <template #header>
          <span style="vertical-align: middle">收费项目</span>
        </template>
        <div style="margin-bottom: 10px">
          <el-button type="primary" @click="confirmCharge()" :disabled="buttonDisabled">
            确认收费
          </el-button>
          <el-button type="primary" plain @click="handleReadCard('01')" style="width: 65px">
            电子凭证
          </el-button>
          <el-button
            type="primary"
            plain
            @click="handleReadCard('02')"
            style="width: 65px"
            :disabled="true"
          >
            身份证
          </el-button>
          <el-button type="primary" plain @click="handleReadCard('03')" style="width: 65px">
            医保卡
          </el-button>
          <el-button
            type="primary"
            @click="payToSelt()"
            style="margin-left: 20px"
            :disabled="buttonDisabled"
          >
            医保转自费
          </el-button>
          <el-button
            type="primary"
            @click="patToMedicalInsurance()"
            style="margin-left: 20px"
            :disabled="buttonDisabled"
          >
            自费转医保
          </el-button>
          <span style="float: right;">合计金额：{{ totalAmounts?totalAmounts.toFixed(2):0 }}元</span>
        </div>
        <el-table
          ref="chargeListRef"
          height="530"
          :data="chargeList"
          row-key="id"
          @selection-change="handleSelectionChange"
          v-loading="chargeLoading"
          border
        >
          <el-table-column type="selection" :selectable="checkSelectable" width="55" />
          <el-table-column label="单据号" align="center" prop="busNo" width="180" />
          <el-table-column label="收费项目" align="center" prop="itemName" width="200" />
          <el-table-column label="数量" align="center" prop="quantityValue" width="80" />
          <el-table-column label="医疗类型" align="center" prop="medTypeCode_dictText" />
          <el-table-column label="医保编码" align="center" prop="ybNo" />
          <el-table-column label="费用性质" align="center" prop="contractName" />
          <el-table-column label="收费状态" align="center" prop="statusEnum_enumText" width="150">
            <template #default="scope">
              <el-tag
                :type="scope.row.statusEnum === 1 ? 'default' : 'success'"
                disable-transitions
              >
                {{ scope.row.statusEnum_enumText }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="金额" align="right" prop="totalPrice" header-align="center">
            <template #default="scope">
              {{ scope.row.totalPrice.toFixed(2) + ' 元' || '0.00' + ' 元' }}
            </template>
          </el-table-column>
          <el-table-column label="收款人" align="center" prop="entererId_dictText" />
        </el-table>
      </el-card>
    </div>
    <ChargeDialog
      :open="openDialog"
      @close="handleClose"
      :category="patientInfo.categoryEnum"
      :totalAmount="totalAmount"
      :patientInfo="patientInfo"
      :chargeItemIds="chargeItemIdList"
      :chrgBchnoList="chrgBchnoList"
      :userCardInfo="userCardInfo"
      :paymentId="paymentId"
      :details="details"
    />
  </div>
</template>

<script setup name="ClinicCharge">
import {
  getList,
  getChargeList,
  changeToSelfPay,
  changeToMedicalInsurance,
  init,
  precharge,
} from './components/api';
import ChargeDialog from './components/chargeDialog.vue';
import { formatDateStr } from '@/utils';
import useUserStore from '@/store/modules/user';

const { proxy } = getCurrentInstance();
const userStore = useUserStore();
const queryParams = ref({
  pageNum: 1,
  pageSize: 50,
  statusEnum: 1,
});
const totalAmounts = ref(0);
const selectedRows = ref([])
const patientList = ref([]);
const chargeList = ref([]);
const chargeItemIdList = ref([]);
const chrgBchnoList = ref([]);
const chargeLoading = ref(false);
const encounterId = ref('');
const paymentId = ref('');
const patientInfo = ref({});
const openDialog = ref(false);
const totalAmount = ref(0);
const chargeListRef = ref();
const details = ref({});
const chargeStatusOptions = ref([]);
const receptionTime = ref([
  formatDateStr(new Date(), 'YYYY-MM-DD'),
  formatDateStr(new Date(), 'YYYY-MM-DD'),
]);
const buttonDisabled = computed(() => {
  return Object.keys(patientInfo.value).length === 0;
});

watch(
  () => selectedRows.value,
  (newVlaue) => {
    if(newVlaue&&newVlaue.length>0){
      handleTotalAmount()
    }
  },
  { immediate: true }
);
function handleSelectionChange(selection) {
  selectedRows.value = selection;
}
function handleTotalAmount() {
  totalAmounts.value = selectedRows.value.reduce(
    (accumulator, currentRow) => {
      return (accumulator + (Number(currentRow.totalPrice) || 0))
    },
    0
  );
}
getPatientList();
initOption();
/**
 * 患者列表
 */
function getPatientList() {
  if (receptionTime.value.length > 0) {
    queryParams.value.receptionTimeSTime = receptionTime.value[0] + ' 00:00:00';
    queryParams.value.receptionTimeETime = receptionTime.value[1] + ' 23:59:59';
  } else {
    queryParams.value.receptionTimeSTime = undefined;
    queryParams.value.receptionTimeETime = undefined;
  }
  getList(queryParams.value).then((res) => {
    patientList.value = res.data.data.records;
  });
}

function initOption() {
  init().then((res) => {
    chargeStatusOptions.value = res.data.chargeItemStatusOptions;
  });
}

function checkSelectable(row, index) {
  // 已结算时禁用选择框
  return row.statusEnum === 1;
}

/**
 * 点击患者列表行 获取处方列表
 */
function clickRow(row) {
  patientInfo.value = row;
  chargeLoading.value = true;
  encounterId.value = row.encounterId;
  getChargeList(row.encounterId).then((res) => {
    chargeList.value = res.data;
    setTimeout(() => {
      chargeLoading.value = false;
      chargeListRef.value.toggleAllSelection();
    }, 100);
  });
}

function handleClose(value, msg) {
  openDialog.value = false;
  if (value == 'success') {
    proxy.$modal.msgSuccess(msg);
    chargeLoading.value = true;
    getChargeList(patientInfo.value.encounterId).then((res) => {
      chargeList.value = res.data;
      setTimeout(() => {
        chargeLoading.value = false;
      }, 100);
    });
  }
}

// 确认收费
function confirmCharge() {
  let selectRows = chargeListRef.value.getSelectionRows();
  if (selectRows.length == 0) {
    proxy.$modal.msgWarning('请选择一条收费项目');
    return;
  }
  chargeItemIdList.value = selectRows.map((item) => {
    return item.id;
  });
  // totalAmount.value = selectRows.reduce((accumulator, currentRow) => {
  //   return accumulator + (currentRow.totalPrice || 0);
  // }, 0);
  precharge({
    patientId: patientInfo.value.patientId,
    encounterId: patientInfo.value.encounterId,
    chargeItemIds: chargeItemIdList.value,
  }).then((res) => {
    if (res.code == 200) {
      // totalAmount.value = res.data.psnCashPay;
      paymentId.value = res.data.paymentId;
      chrgBchnoList.value = res.data.chrgBchnoList;
      totalAmount.value = res.data.details.find((item) => item.payEnum == 220000).amount;
      details.value = res.data.details;
      openDialog.value = true;
    } else {
      proxy.$modal.msgError(res.msg);
    }
  });
  // console.log(patientInfo)
}

let userCardInfo = ref({});
const readCardLoading = ref(false);
const loadingText = ref('');
const BusiCardInfo = ref(''); // miyao
async function handleReadCard(value) {
  if (chrome.webview === undefined) {
    alert('请在医保版本中调用读卡功能！');
  } else {
    try {
      let webView = window.chrome.webview.hostObjects.CSharpAccessor;
      // string url,
      // string fixmedins_code,
      // string businessType,
      // string operatorCode,
      // string operatorName,
      // string officeId,
      // string officeName

      // readCardLoading.value = true;
      let jsonResult;
      let cardInfo;
      let userMessage = undefined;
      switch (value) {
        case '01': // 电子凭证
          // readCardLoading.value = true;
          await webView
            .GetInfoByQrCodeAsync(
              'http://10.47.0.67:8089/localcfc/api/hsecfc/localQrCodeQuery',
              'H22017200667',
              '01101',
              userStore.id,
              userStore.name,
              'D83',
              '财务科'
            )
            .then((res) => {
              readCardLoading.value = true;
              loadingText.value = '正在读取...';
              jsonResult = res;
            })
            .catch(() => {
              readCardLoading.value = false;
            })
            .finally(() => {
              readCardLoading.value = false;
            });
          cardInfo = JSON.parse(jsonResult);
          let message = JSON.parse(cardInfo.message);
          userMessage = {
            certType: '02', // 证件类型
            certNo: message.data.idNo, // 身份证号
            psnCertType: '02', // 居民身份证
          };
          userCardInfo = {
            certType: '01', // 证件类型
            certNo: message.data.idNo, // 身份证号
            psnCertType: '01', // 居民身份证
            busiCardInfo: message.data.ecToken, // 令牌
          };
          BusiCardInfo.value = message.data.ecToken;
          console.log(BusiCardInfo.value);
          break;
        case '02':
          break;
        case '03': // 社保卡
          readCardLoading.value = true;
          loadingText.value = '正在读取...';
          await webView
            .ReadHeaSecCardAsync(
              JSON.stringify({
                IP: 'ddjk.jlhs.gov.cn',
                PORT: 20215,
                TIMEOUT: 60,
                SFZ_DRIVER_TYPE: 1,
              })
            )
            .then((res) => {
              jsonResult = res;
            })
            .finally(() => {
              readCardLoading.value = false;
            });
          // console.log(
          //   'jsonResult',
          //   JSON.parse({
          //     IssuingAreaCode: '310000',
          //     SocialSecurityNumber: '371324198810224515',
          //     CardNumber: 'M501A1A78',
          //     CardIdentificationCode: '310000D15600000535925154E880AB97',
          //     Name: '\u5218\u5CF0',
          //     CardResetInfo: '00814A444686603100333E4FA9',
          //     SpecificationVersion: '3.00',
          //     IssuingDate: '20190313',
          //     ExpirationDate: '20290313',
          //     TerminalNumber: '000000000000',
          //     TerminalDeviceNumber: '00041161201901000005',
          //     Code: 0,
          //     ErrorMessage: null,
          //   })
          // );
          let message1 = JSON.parse(jsonResult);
          userMessage = {
            certType: '02', // 证件类型
            certNo: message1.SocialSecurityNumber, // 身份证号
            psnCertType: '02', // 居民身份证
          };
          userCardInfo = {
            certType: '02', // 证件类型
            certNo: message1.SocialSecurityNumber, // 身份证号
            psnCertType: '02', // 居民身份证
            busiCardInfo: message1.BusiCardInfo, //卡号
          };
          BusiCardInfo.value = message1.BusiCardInfo;
          console.log(message1.BusiCardInfo);
          break;
        case '99':
          break;
      }
      readCardLoading.value = false;
      if (userMessage.certNo) {
        let selectRows = chargeListRef.value.getSelectionRows();
        if (selectRows.length == 0) {
          proxy.$modal.msgWarning('请选择一条收费项目');
          return;
        }
        chargeItemIdList.value = selectRows.map((item) => {
          return item.id;
        });
        totalAmount.value = selectRows.reduce((accumulator, currentRow) => {
          return accumulator + (currentRow.totalPrice || 0);
        }, 0);
        precharge({
          patientId: patientInfo.value.patientId,
          encounterId: patientInfo.value.encounterId,
          chargeItemIds: chargeItemIdList.value,
          ybMdtrtCertType: userCardInfo.psnCertType,
          busiCardInfo: userCardInfo.busiCardInfo,
        }).then((res) => {
          if (res.code == 200) {
            // totalAmount.value = res.data.psnCashPay;
            paymentId.value = res.data.paymentId;
            totalAmount.value = res.data.details.find((item) => item.payEnum == 220000).amount;
            details.value = res.data.details;
            // chrgBchnoList.value = res.data.chrgBchnoList;
            openDialog.value = true;
          } else {
            proxy.$modal.msgError(res.msg);
          }
        });
      }
    } catch (error) {
      console.error('调用失败:', error);
      readCardLoading.value = false;
    }
  }
}

/**
 * 医保转自费
 */
function payToSelt() {
  changeToSelfPay(encounterId.value).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess('操作成功');
    }
  });
}

/**
 * 自费转医保
 */
function patToMedicalInsurance() {
  changeToMedicalInsurance(encounterId.value).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess('操作成功');
    }
  });
}
</script>
<style scoped>
</style>