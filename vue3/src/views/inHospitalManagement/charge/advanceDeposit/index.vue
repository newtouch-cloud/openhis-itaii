<template>
  <div class="container">
    <!-- 顶部操作区域 -->
    <el-card class="header-card" shadow="never">
      <el-row :gutter="20" align="middle">
        <el-col :span="6">
          <el-form
            :model="queryParams"
            ref="queryRef"
            :inline="true"
            v-show="showSearch"
            label-width="90px"
            @submit.prevent
          >
            <el-form-item label="住院号：" prop="searchKey">
              <el-input
                v-model="queryParams.searchKey"
                placeholder="请输入内容"
                clearable
                style="width: 240px"
                @keyup.enter="getPatientInfo"
              />
              <el-button type="primary" @click="getPatientInfo">查询</el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="18" style="text-align: right">
          <el-space>
            <!-- <span style="margin-right: 20px">当前数据号: C8866190HT</span>
            <el-button>跳号</el-button>
            <el-button>票据启停</el-button> -->
            <el-button type="primary" @click="confirmCharge()">收预交金</el-button>
            <el-icon :size="20" style="cursor: pointer" @click="showPatient">
              <UserFilled />
            </el-icon>
          </el-space>
        </el-col>
      </el-row>
    </el-card>

    <!-- 患者信息区域 -->
    <el-card class="patient-info-card" shadow="never">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="110px" label-position="left">
        <el-row align="middle" :gutter="20">
          <el-col :span="1">
            <el-avatar
              :size="50"
              src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
            />
          </el-col>
          <el-col :span="15">
            <div class="select_wrapper_div">
              <el-form-item label="" prop="" label-width="0px">
                <span style="font-size: 18px; font-weight: bold; margin-right: 10px">{{
                  form.bedLocationId_dictText
                }}</span>
                <span style="margin-right: 10px">{{ form.patientName }}</span>
                <span style="margin-right: 10px"
                  >{{ form.genderEnum_enumText }} / {{ form.ageString }}</span
                >
                <el-tag type="info" size="small">{{ form.payWay }}</el-tag>
                <span style="margin-left: 20px"
                  >住院号: {{ form.admissionNo }}
                  <el-icon style="cursor: pointer; vertical-align: middle"
                    ><el-icon-document-copy @click="copyText" /></el-icon
                ></span>
              </el-form-item>
            </div>
          </el-col>
          <el-col :span="8" style="text-align: right">
            <el-form-item label="" prop="" label-width="0px">
              <span style="margin-right: 5px">科室: {{ form.wardLocationId_dictText }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <el-col :span="8" style="text-align: left; margin-top: 10px">
            <el-form-item label="总额:" prop="" class="custom-label">
              <!-- <span class="amount-highlight">18999.33</span> -->
              <el-input
                class="amount-highlight"
                v-model="form.totalPrice"
                disabled
                style="width: 50%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8" style="text-align: left; margin-top: 10px">
            <el-form-item label="预交金:" prop="" class="custom-label">
              <!-- <span class="amount-highlight">18909.33</span> -->
              <el-input
                class="amount-highlight"
                v-model="form.deposit"
                disabled
                style="width: 50%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8" style="text-align: left; margin-top: 10px">
            <el-form-item label="余额:" prop="" class="custom-label">
              <el-input
                class="amount-highlight"
                v-model="form.balanceAmount"
                disabled
                style="width: 50%"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- 表格区域 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="tableData"
        style="width: 100%"
        :row-class-name="tableRowClassName"
        v-loading="loading"
      >
        <el-table-column prop="payWay" label="支付方式" />
        <el-table-column prop="paymentEnum_enumText" label="状态" />
        <el-table-column prop="tenderedAmount" label="金额">
          <template #default="scope">
            <span :class="{ 'negative-amount': scope.row.tenderedAmount < 0 }">{{
              scope.row.tenderedAmount
                ? scope.row.tenderedAmount.toFixed(2)
                : scope.row.tenderedAmount
            }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="busNo" label="收据号" width="280" />
        <el-table-column prop="statusEnum_enumText" label="票据状态" min-width="120" />
        <el-table-column prop="afterBalance" label="可退金额" min-width="120">
          <template #default="scope">
            <span>{{
              scope.row.afterBalance ? scope.row.afterBalance.toFixed(2) : scope.row.afterBalance
            }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="payTime" label="收款时间" min-width="180" />
        <el-table-column prop="entererId_dictText" label="收款员" min-width="100" />
        <!-- <el-table-column prop="remarks" label="备注" min-width="100" /> -->
        <el-table-column label="操作" min-width="150" fixed="right">
          <template #default="scope">
            <el-button link type="primary" size="small">补打</el-button>
            <el-button
              link
              type="primary"
              size="small"
              :class="{ 'action-red': scope.row.amount < 0 }"
              >退还</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNo"
        v-model:limit="queryParams.pageSize"
        @pagination="getPatientInfo"
      />
    </el-card>
    <!-- 患者列表弹窗 -->
    <PatientList
      :drawerVisible="showPatientList"
      ref="showPatientRef"
      :wardListOptions="wardListOptions"
      @patientSelected="handlePatientSelected"
    />
    <ChargeDialog
      ref="chargeListRef"
      :open="openDialog"
      @close="handleClose"
      :patientInfo="patientInfo"
      :payLists="payLists"
    />
    <!-- :category="patientInfo.categoryEnum" -->
  </div>
</template>

<script setup>
import { ref, reactive, nextTick } from 'vue';
import { Menu as ElIconMenu, DocumentCopy as ElIconDocumentCopy } from '@element-plus/icons-vue'; // 引入图标
import { getDepositInfoPage, getDepositInfoInit } from './components/api';
import PatientList from './components/patientList.vue'; // 引入同文件夹下的 patientList 文件
import ChargeDialog from './components/chargeDialog.vue';
const search = ref('');
const showPatientList = ref(false);
const showPatientRef = ref();
const openDialog = ref(false);
const chargeListRef = ref();
const loading = ref(false);
const tableData = ref([]);
const wardListOptions = ref(undefined);
const payLists = ref(undefined);
const total = ref(0);
const showSearch = ref(true);
const patientInfo = ref({});
// const tableData = reactive([
//   {
//     paymentMethod: '微信',
//     status: '正常',
//     amount: 500.0,
//     receiptNumber: 'C8856108HT',
//     invoiceStatus: '已结算',
//     refundableAmount: 0,
//     paymentTime: '2024/03/01 11:40',
//     cashier: '小慕',
//     remarks: '',
//   },
//   {
//     paymentMethod: '银行卡 (1775)',
//     status: '退费',
//     amount: 1500.0,
//     receiptNumber: 'C8856108HT',
//     invoiceStatus: '已结算',
//     refundableAmount: 0,
//     paymentTime: '2024/03/01 11:40',
//     cashier: '小慕',
//     remarks: '',
//   },
//   {
//     paymentMethod: '银行卡 (1775)',
//     status: '退费',
//     amount: -1500.0,
//     receiptNumber: 'C8856108HT',
//     invoiceStatus: '已结算',
//     refundableAmount: 0,
//     paymentTime: '2024/03/01 11:40',
//     cashier: '小慕',
//     remarks: '',
//   },
//   {
//     paymentMethod: '现金',
//     status: '补打',
//     amount: 50.0,
//     receiptNumber: 'C8856108HT',
//     invoiceStatus: '已结算',
//     refundableAmount: 0,
//     paymentTime: '2024/03/01 11:40',
//     cashier: '小慕',
//     remarks: '',
//   },
//   {
//     paymentMethod: '现金',
//     status: '补打',
//     amount: -50.0,
//     receiptNumber: 'C8856108HT',
//     invoiceStatus: '已结算',
//     refundableAmount: 0,
//     paymentTime: '2024/03/01 11:40',
//     cashier: '小慕',
//     remarks: '',
//   },
//   {
//     paymentMethod: '现金',
//     status: '正常',
//     amount: 1500.0,
//     receiptNumber: 'C8856108HT',
//     invoiceStatus: '已结算',
//     refundableAmount: 0,
//     paymentTime: '2024/03/01 11:40',
//     cashier: '小慕',
//     remarks: '',
//   },
//   {
//     paymentMethod: '银行卡 (1775)',
//     status: '正常',
//     amount: 500.0,
//     receiptNumber: 'C8856108HT',
//     invoiceStatus: '已结算',
//     refundableAmount: 0,
//     paymentTime: '2024/03/01 11:40',
//     cashier: '小慕',
//     remarks: '',
//   },
//   {
//     paymentMethod: '转押金',
//     status: '正常',
//     amount: 1000.0,
//     receiptNumber: 'C8856108HT',
//     invoiceStatus: '未结算',
//     refundableAmount: 1000.0,
//     paymentTime: '2024/03/01 11:40',
//     cashier: '小慕',
//     remarks: '',
//   },
//   {
//     paymentMethod: '现金',
//     status: '正常',
//     amount: 2000.0,
//     receiptNumber: 'C8856108HT',
//     invoiceStatus: '未结算',
//     refundableAmount: 2000.0,
//     paymentTime: '2024/03/01 11:40',
//     cashier: '小慕',
//     remarks: '',
//   },
// ]);
const { proxy } = getCurrentInstance();
const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    searchKey: undefined, // 供应商名称
  },
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);
const tableRowClassName = ({ row, rowIndex }) => {
  if (row.amount < 0) {
    // 根据图片，当金额为负数时，整行（或特定列）文字变红
    // 这里我们给整行添加一个class，然后在style中定义颜色
    // 或者也可以只给特定列的文字变红，如金额列
    return 'negative-row-text';
  }
  return '';
};

init();

/**
 * 取得初期下拉选
 */
function init() {
  getDepositInfoInit().then((res) => {
    console.log(res, '*************************', res.wardListOptions);
    if (res.data) {
      wardListOptions.value = res.data.wardListOptions;
      payLists.value = res.data.payLists;
      console.log(wardListOptions.value);
    }
  });

  // onSearch();
}

/**
 * 获取患者信息
 */
function getPatientInfo() {
  console.log(queryParams.searchKey, 'queryParams.searchKey');
  if (!queryParams.value.searchKey) {
    proxy.$modal.msgError('请先输入住院号！');
    return;
  }
  loading.value = true;
  console.log(queryParams.value, 'queryParams.value');
  getDepositInfoPage(queryParams.value).then((res) => {
    if (res.data && res.data.records && res.data.records.length > 0) {
      form.value = JSON.parse(JSON.stringify(res.data.records[0]));
    }
    console.log(res, 'resqqqqqqqqqqqqqqqqqqqqqqq', form.value, 'form.value');
    // 过滤掉金额为空的记录
    const list = res.data.records.filter((item) => item.tenderedAmount != null);
    tableData.value = list;
    total.value = res.data.total;
    loading.value = false;
    console.log(res, 'resqqqqqqqqqqqqqqqqqqqqqqq');
  });
}

/**
 * 显示患者列表
 */
function showPatient() {
  showPatientList.value = true;
  nextTick(() => {
    proxy.$refs['showPatientRef'].show();
  });
}

// 确认收费
function confirmCharge() {
  console.log(patientInfo.value, 'patientInfo.valueconfirmCharge');
  if (patientInfo.value.patientId) {
    openDialog.value = true;
  } else {
    proxy.$modal.msgError('请先选择病人！');
  }
}

/** 选择病人 */
function handlePatientSelected(row) {
  console.log(row, 'rowwwwwwwwhandlePatientSelected');
  queryParams.value.searchKey = row.admissionNo;
  patientInfo.value = row;
  nextTick(() => {
    getPatientInfo();
  });
}
/** 重置操作表单 */
function reset() {
  form.value = {
    id: undefined,
    patientName: undefined,
    genderEnum_enumText: undefined,
    ageString: undefined,
    payWay: undefined,
    patientId: undefined,
    organizationId_dictText: undefined,
    totalPrice: undefined,
    deposit: undefined,
    balanceAmount: undefined,
    bedLocationId_dictText,
  };
  proxy.resetForm('formRef');
}

// 复制文本到剪贴板
async function copyText() {
  console.log('复制文本:', form.value);
  try {
    // 使用 navigator.clipboard.writeText 复制文本
    await navigator.clipboard.writeText(form.value.admissionNo);
    proxy.$modal.msgSuccess('复制成功！');
  } catch (err) {
    proxy.$modal.msgError('复制失败！' + err);
  }
}

/**
 * 处理关闭事件
 *
 * @returns {void} 无返回值
 */
function handleClose(str) {
  openDialog.value = false;
  if (str === 'success') {
    getPatientInfo();
    proxy.$modal.msgSuccess('操作成功！');
  }
}

// const { proxy } = getCurrentInstance() // 如果不需要可以移除
// const emits = defineEmits([]) // 如果不需要可以移除
// const props = defineProps({}) // 如果不需要可以移除
// const state = reactive({}) // 如果不需要可以移除
// onBeforeMount(() => {}) // 如果不需要可以移除
// onMounted(() => {}) // 如果不需要可以移除
// defineExpose({ state }) // 如果不需要可以移除
</script>

<style lang="scss" scoped>
.container {
  padding: 15px;
  background-color: #f0f2f5; // 类似图片背景色
  min-height: 100vh;
}

.header-card,
.patient-info-card,
.table-card {
  margin-bottom: 15px;
  border: none; // 移除卡片边框，更接近图片效果
  :deep(.el-card__body) {
    // 调整卡片内边距
    padding: 15px;
  }
}
.patient-info-card {
  :deep(.el-card__body) {
    padding-bottom: 0; // 患者信息卡片底部不需要太多padding
  }
}

.patient-details {
  margin-top: 15px;
  :deep(.el-descriptions__label.el-descriptions__cell.is-bordered-label) {
    background-color: #fafafa; // 表头背景色
  }
  :deep(td),
  :deep(th) {
    // 移除边框，更像图片
    border: none;
  }
}

:deep(.custom-label .el-input__inner) {
  color: #ff7100 !important; // 橙色高亮
  font-weight: bold;
}
.amount-highlight-balance {
  color: #ff7100; // 橙色高亮
  font-weight: bold;
}

// 表格样式调整
.el-table {
  :deep(th) {
    background-color: #f0f8ff !important; // 表头淡蓝色背景
    color: #333;
    font-weight: normal;
  }
  :deep(td),
  :deep(th.is-leaf) {
    border-bottom: 1px solid #ebeef5; // 只保留底部边框
    border-left: none;
    border-right: none;
    border-top: none;
  }
  :deep(.el-table__row):hover > td {
    // 鼠标悬浮时背景色
    background-color: #f5f7fa;
  }
}

.negative-amount {
  color: red;
}

// 如果需要整行文字变红
.el-table .negative-row-text {
  color: red;
  td {
    color: red !important; // 确保覆盖默认样式
  }
  .el-button--primary.is-link {
    // 如果按钮也需要变红
    color: red !important;
  }
}
.action-red {
  // 单独给退还按钮变红
  color: red !important;
}

// 移除el-input-group__append的边框，使其与输入框融为一体
:deep(.el-input-group__append) {
  border-left: 0;
  background-color: #409eff;
  color: white;
  .el-button {
    color: white;
    &:hover {
      background-color: #66b1ff;
    }
  }
}

:deep(.custom-label .el-form-item__label) {
  background-color: #fafafa; /* 设置背景色 */
  padding: 4px 8px; /* 可选：添加一些内边距 */
  border: #e9e8e8 solid 1px; /* 可选：添加边框 */
  display: flex; /* 使用 flex 布局 */
  align-items: center; /* 垂直居中 */
  // justify-content: center; /* 水平居中 */
}
</style>
