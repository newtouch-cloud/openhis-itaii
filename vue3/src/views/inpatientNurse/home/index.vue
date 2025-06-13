<template>
  <div class="inpatientNurse-container">
    <div class="search-container">
      <div class="view-container">
        <el-radio-group v-model="isBed" @change="bedChange">
          <el-radio-button label="card" border>床卡</el-radio-button>
          <el-radio-button label="table" border>列表</el-radio-button>
        </el-radio-group>
      </div>
      <el-space>
        <el-form
          :model="queryParams"
          ref="queryRef"
          :inline="true"
          v-show="showSearch"
          @submit.prevent
        >
          <el-form-item label="" prop="statusEnum">
            <el-radio-group v-model="queryParams.statusEnum" size="default" @change="getList">
              <el-radio-button
                v-for="(item, index) in statusEnumList"
                :key="index"
                :label="item.value"
                :disabled="item.disabled"
                >{{ item.label }}</el-radio-button
              >
            </el-radio-group>
          </el-form-item>
          <el-form-item label="" prop="checkboxGroup2">
            <el-checkbox-group v-model="queryParams.nursingLevelList" @change="getList">
              <el-checkbox-button
                v-for="item in levelList"
                :key="item.value"
                v-model="item.value"
                style="display: inline-block"
                :label="item.label"
              >
                {{ item.label }}
              </el-checkbox-button>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="" prop="searchKey">
            <el-input
              placeholder="住院号/姓名/责任医生/责任护士"
              v-model="queryParams.searchKey"
              clearable
              @keyup.enter="handleQuery"
            />
          </el-form-item>

          <el-button @click="getList">查询</el-button>
        </el-form>
      </el-space>
    </div>
    <div class="medicalOrderList-table">
      <patientCard
        v-if="isBed === 'card'"
        ref="cardsRef"
        :cardList="patientData"
        :bed-config="bedManageConfig"
        @openPatientDetail="openPatientDetailDialog"
        @closePatientDetail="closePatientDetialDialog"
        @openTransferToBed="openTransferToBedDialog"
        @closeTransferToBed="closeTransferToBedDialog"
        @openTransferZk="openTransferZkDialog"
        @openTransferCy="openTransferCyDialog"
        @closeTransfer="closeTransferDialog"
      />
      <el-table
        v-if="isBed === 'table'"
        ref="table"
        :data="patientData"
        row-key="id"
        style="width: 100%; height: 100%"
        highlight-current-row
        show-overflow-tooltip
        @row-dblclick="openPatientDetailDialog"
        max-height="800px"
      >
        <el-table-column label="床号" prop="locationId_dictText" min-width="80" align="center" />
        <el-table-column label="患者姓名" prop="patientName" min-width="100" align="center" />
        <el-table-column
          label="就诊类别"
          prop="classEnum_enumText"
          min-width="100"
          align="center"
        />
        <el-table-column label="性别" prop="genderEnum_enumText" min-width="60" align="center" />
        <el-table-column label="年龄" prop="ageString" min-width="60" align="center" />
        <el-table-column label="手机号" prop="phone" min-width="120" align="center" />
        <el-table-column label="费别" prop="typeCode_dictText" min-width="80" align="center" />
        <el-table-column label="住院号" prop="hospitalNo" min-width="120" align="center" />
        <el-table-column
          label="患者状态"
          prop="statusEnum_enumText"
          min-width="100"
          align="center"
        />
        <el-table-column
          label="入院科室"
          prop="organizationId_dictText"
          min-width="120"
          align="center"
        />
        <el-table-column label="入院日期" prop="admissionDate" min-width="120" align="center" />
        <el-table-column
          label="护理级别"
          prop="priorityEnum_enumText"
          min-width="100"
          align="center"
        />
        <el-table-column label="主要诊断" prop="mainDiagnosis" min-width="150" align="center" />
        <el-table-column
          label="过敏源"
          prop="categoryCode_dictText"
          min-width="100"
          align="center"
        />
        <el-table-column
          label="住院天数"
          prop="hospitalizationDays"
          min-width="80"
          align="center"
        />
        <el-table-column label="责任医生" prop="responsibleDoctor" min-width="120" align="center" />
        <el-table-column label="责任护士" prop="responsibleNurse" min-width="120" align="center" />
        <el-table-column
          label="操作"
          prop="operations"
          min-width="360"
          fixed="right"
          align="center"
        >
          <template #default="scope">
            <el-button type="primary" link>打印腕带</el-button>
            <el-button type="primary" link>打印床头卡</el-button>
            <el-button type="primary" link @click="openTransferCyDialog(scope.row)">出院</el-button>
            <el-button type="primary" link @click="openTransferZkDialog(scope.row)">转科</el-button>
            <el-button type="primary" link @click="openTransferToBedDialog(scope.row)"
              >转床</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNo"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
        style="margin-bottom: 20px"
      />
    </div>
    <patient-detial-dialog
      ref="patientDetialDialogRef"
      :open="openPatientDetail"
      :patientId="patientId"
      @close="closePatientDetialDialog"
    />
    <transfer-to-bed-dialog
      ref="transferToBedRef"
      :transferToBedInfo="transferToBedInfo"
      :open="openTransferToBed"
      @close="closeTransferToBedDialog"
    />

    <transfer-dialog
      ref="transferRef"
      :transferInfo="transferInfo"
      :open="openTransfer"
      :title="title"
      @close="closeTransferDialog"
    />
  </div>
</template>
<script setup>
import { ref } from 'vue';
import patientCard from './components/patientCard.vue';
import { listPatient } from './components/api';
import patientDetialDialog from './components/patientDetialDialog.vue';
import transferToBedDialog from './components/transferToBedDialog.vue';
import transferDialog from './components/transferDialog.vue';
const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    searchKey: undefined, // 品名/商品名/英文品名/编码/拼音
    checkboxGroup2: [], // 类型（包括 1：中药，2：成药）
    statusEnum: 1, // 状态
  },
  rules: {},
});
const { proxy } = getCurrentInstance();
const { queryParams, form, rules } = toRefs(data);

const statusEnumList = ref([
  { label: '在科', value: 1 },
  { label: '待转科', value: 2 },
  { label: '待出院', value: 3 },
  { label: '危重', value: 4 },
  { label: '手术', value: 5 },
  { label: '欠费', value: 6 },
  { label: '已出院', value: 7 },
]);

const levelList = ref([
  { label: '特级', value: 1 },
  { label: '一级', value: 2 },
  { label: '二级', value: 3 },
  { label: '三级', value: 4 },
]);

const patientData = ref([]);
const bedStations = ref([]); // 床卡状态
const isBed = ref('card');
const bedManageConfig = ref({});
const showSearch = ref(true);
const total = ref(0);
const currentPatient = ref({}); // 当前患者
const nursingMeasuresVal = ref([]); // 护理措施
const specialArrangementVal = ref([]); // 特殊安排
const cardsRef = ref(null); // 卡片引用
const table = ref(null); // 表格引用

// 患者详细对话框相关变量
const patientDetialDialogRef = ref();
const openPatientDetail = ref(false);
const patientId = ref(''); // 患者ID

// 转床对话框相关变量
const transferToBedRef = ref();
const openTransferToBed = ref(false);

// 转科/出院对话框相关变量
const transferRef = ref();
const openTransfer = ref(false);
const title = ref('');
// 转床信息
const transferToBedInfo = ref({
  organizationId: '',
  organizationId_dictText: '',
  oldLocationId: '',
  patientId: '', // 处方号
  busNo: '',
  encounterLocationId: '',
  encounterId: '',
});

// 转院/出院信息
const transferInfo = ref({
  // organizationId: '',
  organizationIdOld: '',
  patientName: '',
  patientId: '', // 处方号
  busNo: '',
  encounterId: '',
  encounterLocationId: '',
  patientNo: '',
});
getList();
function getList() {
  // loading.value = true;
  console.log(queryParams.value, 'queryParams.value');
  listPatient(queryParams.value).then((res) => {
    patientData.value = res.data.records;
    total.value = res.data.total;
    // loading.value = false;
    console.log(res, 'resqqqqqqqqqqqqqqqqqqqqqqq');
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNo = 1;
  getList();
}
/**
 * 切换患者信息显示形式（卡片/列表）
 *
 * @param val 床号
 */
function bedChange(val) {
  getBadFirstMes();
}
/**
 * 切换患者信息显示形式实现
 *
 */
function getBadFirstMes() {
  if (patientData.value.length > 0 && patientData.value[0].bedOperationalStatus !== 'U') {
    currentPatient.value = JSON.parse(JSON.stringify(patientData.value[0]));
    nursingMeasuresVal.value = currentPatient.value.nursingMeasures || [];
    specialArrangementVal.value = currentPatient.value.specialArrangement || [];
  } else {
    currentPatient.value = {};
    nursingMeasuresVal.value = [];
    specialArrangementVal.value = [];
  }

  if (
    patientData.value.length > 0 &&
    patientData.value[0].bedOperationalStatus !== bedStations.value[0]?.code
  ) {
    nextTick(() => {
      if (isBed.value === 'card' && cardsRef.value) {
        cardsRef.value.activePatient.bedId = patientData.value[0].bedId;
      }
      if (isBed.value === 'table' && table.value) {
        table.value.setCurrentRow(patientData.value[0]);
      }
    });
  }
}

/**
 * 打开患者信息详细对话框
 */
function openPatientDetailDialog(row) {
  openPatientDetail.value = true;
  patientId.value = row.id;
  nextTick(() => {
    proxy.$refs['patientDetialDialogRef'].getPatientDetial();
  });
  console.log(openPatientDetail.value, '打开患者信息详细对话框');
}

/**
 * 关闭患者信息详细对话框
 */
function closePatientDetialDialog(str) {
  if (str === 'success') {
    getList();
    proxy.$modal.msgSuccess('操作成功');
  }
  openPatientDetail.value = false;
}

/**
 * 打开转床对话框
 */
function openTransferToBedDialog(row) {
  transferToBedInfo.value.organizationId = row.organizationId;
  transferToBedInfo.value.oldLocationId = row.locationId;
  transferToBedInfo.value.organizationId_dictText = row.organizationId_dictText;
  transferToBedInfo.value.patientId = row.id; // 病人ID
  transferToBedInfo.value.busNo = row.busNo;
  transferToBedInfo.value.encounterLocationId = row.encounterLocationId;
  transferToBedInfo.value.encounterId = row.encounterId;
  openTransferToBed.value = true;
  // console.log(transferToBedInfo.value, '打开转床对话框==============');
  nextTick(() => {
    proxy.$refs['transferToBedRef'].show();
  });
  console.log(openTransferToBed.value, '打开转床对话框1', transferToBedInfo.value);
}

/**
 * 关闭转床对话框
 */
function closeTransferToBedDialog(str) {
  if (str === 'success') {
    getList();
    proxy.$modal.msgSuccess('操作成功');
  }
  openTransferToBed.value = false;
}

/**
 * 打开转科对话框
 */
function openTransferZkDialog(row) {
  transferInfo.value.organizationIdOld = row.organizationId;
  transferInfo.value.patientName = row.patientName;
  transferInfo.value.patientId = row.id; // 病人ID
  transferInfo.value.busNo = row.busNo; // 病人ID
  transferInfo.value.encounterId = row.encounterId;
  transferInfo.value.encounterLocationId = row.encounterLocationId;
  transferInfo.value.patientNo = row.patientNo;
  title.value = '';
  openTransfer.value = true;
  title.value = '转科';
  nextTick(() => {
    proxy.$refs['transferRef'].show();
  });
  console.log(openTransfer.value, '打开转床对话框2');
}

/**
 * 打开出院对话框
 */
function openTransferCyDialog(row) {
  transferInfo.value.patientName = row.patientName;
  transferInfo.value.patientId = row.id; // 病人ID
  transferInfo.value.busNo = row.busNo; // 病人ID
  transferInfo.value.encounterId = row.encounterId;
  transferInfo.value.encounterLocationId = row.encounterLocationId;
  transferInfo.value.locationId = row.locationId;
  transferInfo.value.patientNo = row.patientNo;
  title.value = '';
  openTransfer.value = true;
  title.value = '出院';
  nextTick(() => {
    proxy.$refs['transferRef'].show();
  });
  console.log(openTransfer.value, '打开转床对话框3');
}

/**
 * 关闭转科/出院对话框
 */
function closeTransferDialog(str) {
  if (str === 'success') {
    getList();
    proxy.$modal.msgSuccess('操作成功');
  }
  openTransfer.value = false;
}
</script>
<style lang="scss" scoped>
.inpatientNurse-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  overflow: hidden;

  .search-container {
    flex: none;
    padding: 8px 8px;
    text-align: right;
    border-bottom: 1px solid #e4e7ed;
  }

  .view-container {
    flex: none;
    padding: 8px 8px;
    text-align: left;
    border-bottom: 1px solid #e4e7ed;
  }

  .medicalOrderList-table {
    flex: 1;
  }
}
</style>