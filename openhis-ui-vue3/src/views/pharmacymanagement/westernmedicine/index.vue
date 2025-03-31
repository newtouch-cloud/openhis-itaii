<template>
  <div class="app-container">
    <div class="left">
      <el-form
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        v-show="showSearch"
      >
        <el-form-item label="姓名或证件号" prop="condition">
          <el-input
            v-model="queryParams.condition"
            placeholder="请输入姓名/证件号"
            clearable
            style="width: 150px"
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="就诊日期">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: auto"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="科室" prop="departmentId">
          <el-select
            v-model="queryParams.departmentId"
            placeholder="请选择科室"
            clearable
            @keyup.enter="handleQuery"
            style="width: 160px"
          >
            <el-option
              v-for="item in departmentList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery"
            >搜索</el-button
          >
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table
        :data="patientList"
        border
        style="width: 100%; height: 60vh"
        highlight-current-row
        @current-change="handleCurrentChange"
      >
        <el-table-column prop="departmentName" label="科室" width="120" />
        <el-table-column prop="patientName" label="姓名" width="100" />
        <el-table-column prop="genderEnum_enumText" label="性别" width="80" />
        <!-- <el-table-column prop="ageString" label="开单医生" width="80" /> -->
        <el-table-column prop="encounterDate" label="就诊日期" width="140" />
      </el-table>
      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNo"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </div>

    <div class="right">
      <div style="display: flex; align-items: center">
        <p style="margin-right: 60px; font-size: 19px">患者基本信息</p>

        <!-- <el-button type="success" plain @click="print" icon="Printer" style="margin-left: 30px;">打印</el-button> -->
      </div>
      <div class="top">
        <el-row>
          <el-col :span="4">姓名：{{ personInfo.patientName }}</el-col>
          <el-col :span="3">性别：{{ personInfo.genderEnum_enumText }}</el-col>
          <el-col :span="3">年龄：{{ personInfo.age }}</el-col>
          <el-col :span="5"
            >合同类型：{{ personInfo.categoryEnum_enumText }}</el-col
          > </el-row
        ><br />
        <el-row>
          <el-col :span="5">就诊科室：{{ personInfo.organizationName }}</el-col>
          <el-col :span="5">就诊日期：{{ personInfo.encounterDate }}</el-col>
          <el-col :span="7">证件号：{{ personInfo.idCard }}</el-col>

          <!-- <el-col :span="6">门诊诊断：{{ personInfo.patientName }}</el-col> --> </el-row
        ><br />
        <el-row>
          <el-col :span="4"
            >总金额：{{
              personInfo.totalPrice ? personInfo.totalPrice.toFixed(2) : "0.00"
            }}元</el-col
          >
        </el-row>
      </div>
      <el-table
        :data="medicineInfoList"
        border
        style="width: 100%; height: 65vh; margin-top: 10px"
        :row-style="rowStyle"
        :span-method="spanMethod"
        @selection-change="handleSelectionChange"
        ref="tableRef"
      >
        <el-table-column
          label="操作"
          align="center"
          width="160"
          class-name="small-padding fixed-width"
        >
          <template #default="scope">
            <el-button
              link
              type="primary"
              icon="SuccessFilled"
              @click="submitMedicine(scope.row)"
              >发药</el-button
            >
            <el-button
              link
              type="primary"
              @click="backMedicine(scope.row)"
              icon="CircleClose"
              >作废</el-button
            >
          </template>
        </el-table-column>
        <el-table-column prop="departmentName" label="科室" width="90" />
        <el-table-column prop="doctorName" label="开单医生" width="100" />
        <el-table-column prop="itemType" label="项目类型" width="100" />
        <el-table-column prop="conditionName" label="诊断" width="120" />
        <el-table-column prop="prescriptionNo" label="处方号" width="120" />
        <el-table-column prop="markers" label="成组" width="60">
          <template #default="scope">
            <span>{{ markers[scope.$index] }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="medicineName" label="药品名称" width="120" />
        <el-table-column prop="totalVolume" label="规格" width="100" />
        <el-table-column prop="dose" label="剂量" width="100" />
        <el-table-column prop="rateCode" label="频次" width="100" />
        <el-table-column prop="methodCode" label="用法" width="80" />
        <el-table-column prop="dispensePerDuration" label="天数" width="60" />
        <el-table-column
          prop="dispensePerQuantity"
          label="单次发药数"
          width="80"
        />
        <el-table-column prop="quantity" label="数量" width="80" />
        <!-- <el-table-column prop="performOrg_dictText" label="是否拆零" width="120">
                    <template #default="scope">
                        <el-select v-model="scope.row.performOrg_dictText" placeholder="请选择"
                            @change="handleSelectChange(scope.row)">
                            <el-option v-for="option in options" :key="option.value" :label="option.label"
                                :value="option.value"></el-option>
                        </el-select>
                    </template>
                </el-table-column> -->
        <!-- <el-table-column prop="medicationStatusEnum_enumText" label="追溯码" width="100" /> -->
        <el-table-column
          prop="unitPrice"
          label="单价"
          width="60"
          :formatter="formatPrice"
        />
        <el-table-column
          prop="totalPrice"
          label="金额"
          width="70"
          :formatter="formatPrice"
        />
      </el-table>
    </div>
    <el-dialog title="选择作废原因" v-model="showDialog" width="30%">
      <!-- 下拉选择框 -->
      <el-select v-model="notPerformedReasonEnum" placeholder="请选择作废原因">
        <el-option
          v-for="item in backReason"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>

      <!-- 弹窗底部按钮 -->
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleCancel">取 消</el-button>
          <el-button type="primary" @click="handleConfirm">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="westernmedicine">
import { ref, computed, onMounted, onBeforeMount } from "vue";
import { ElMessage } from "element-plus";
import {
  listWesternmedicine,
  listPatient,
  updateMedicion,
  listInit,
  backMedicion,
} from "./components/api";

const showSearch = ref(true);
const total = ref(0);
const markers = ref([]);
const patientList = ref([]);
const medicineInfoList = ref([]);
const departmentList = ref([]);
const dateRange = ref([]);
const personInfo = ref([]);
const diagnoses = ref("");
const backReason = ref([]);
const selectedPrescriptionNo = ref("");
const showDialog = ref(false);
const notPerformedReasonEnum = ref();
const currentRow = ref(null);
const tableRef = ref(null);
const selectedGroupIds = ref(new Set());
const selectedItems = ref(new Set());

const { proxy } = getCurrentInstance();

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    condition: null,
    departmentId: null,
  },
});
const { queryParams } = toRefs(data);

// 在组件挂载后调用 getList
onMounted(() => {
  setDefaultDateRange();
  getList();
});

// 设置默认日期范围为当天
function setDefaultDateRange() {
  const today = new Date();
  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, "0");
  const day = String(today.getDate()).padStart(2, "0");
  const formattedDate = `${year}-${month}-${day}`;
  dateRange.value = [formattedDate, formattedDate];
  console.log("222", dateRange.value);
}

function getList() {
  console.log("224555552", dateRange.value);
  queryParams.value.startTimeSTime = dateRange.value[0] + " 00:00:00";
  queryParams.value.startTimeETime = dateRange.value[1] + " 23:59:59";

  console.log("222", queryParams.value);
  listPatient(queryParams.value).then((response) => {
    console.log("Full response1:", response);
    patientList.value = response.data.records;
    total.value = response.data.total;
  });
  listInit().then((response) => {
    console.log("Full response2:", response);
    departmentList.value = [
      { value: null, label: "全部" },
      ...response.data.departmentOptions,
    ];
    backReason.value = response.data.notPerformedReasonOptions;
  });
}

/** 重置按钮操作 */
function resetQuery() {
  setDefaultDateRange();
  medicineInfoList.value = [];
  personInfo.value = [];
  proxy.resetForm("queryRef");
  getList();
}
/** 搜索按钮操作 */
function handleQuery() {
  if (dateRange.value) {
    queryParams.value.startTimeSTime = dateRange.value[0] + " 00:00:00";
    queryParams.value.startTimeETime = dateRange.value[1] + " 23:59:59";
  } else {
    queryParams.value.startTimeSTime = null;
    queryParams.value.startTimeETime = null;
  }
  queryParams.value.pageNo = 1;
  listPatient(queryParams.value).then((response) => {
    patientList.value = response.data.records;
    total.value = response.data.total;
  });
}

function countGroupRows(data) {
  const groupCounts = new Map();
  data.forEach((item, index) => {
    if (!groupCounts.has(item.prescriptionNo)) {
      groupCounts.set(item.prescriptionNo, { count: 0, indices: [] });
    }
    const groupInfo = groupCounts.get(item.prescriptionNo);
    groupInfo.count++;
    groupInfo.indices.push(index);
  });
  return groupCounts;
}

function getRowMarkers(groupCounts, data) {
  const markers = new Array(data.length).fill("");

  groupCounts.forEach((groupInfo, prescriptionNo) => {
    const { count, indices } = groupInfo;
    if (count === 1) {
      // 如果只有一行，不显示标记
      return;
    } else if (count === 2) {
      // 如果有两行，分别显示左右括号
      markers[indices[0]] = "┏";
      markers[indices[1]] = "┗ ";
    } else {
      // 如果有两行以上，第一条显示左括号，中间用竖线，最后一条显示右括号
      markers[indices[0]] = "┏";
      for (let i = 1; i < indices.length - 1; i++) {
        markers[indices[i]] = "┃";
      }
      markers[indices[indices.length - 1]] = "┗ ";
    }
  });
  return markers;
}

function handleSelectionChange(selection) {
  // 清空之前选中的数据
  selectedItems.value.clear();
  // 将当前选中的数据存到 selectedItems 中
  selection.forEach((item) => {
    selectedItems.value.add(item);
  });
  // 更新 selectedGroupIds
  selection.forEach((item) => {
    const prescriptionNo = item.prescriptionNo;
    // 检查 prescriptionNo 是否同时存在
    if (selectedGroupIds.value.has(prescriptionNo)) {
      // 如果都存在，则移除它们
      selectedGroupIds.value.delete(prescriptionNo);
    } else {
      // 否则添加它们
      selectedGroupIds.value.add(prescriptionNo);
    }
  });
  // 动态更新表格行的选中状态
  medicineInfoList.value.forEach((row) => {
    const isSelected = selectedGroupIds.value.has(row.prescriptionNo);
    tableRef.value.toggleRowSelection(row, isSelected);
  });
}

function spanMethod({ row, column, rowIndex, columnIndex }) {
  // 定义需要合并的列范围（前6列，包括selection列）
  const columnsToMerge = [0, 1, 2, 3, 4, 5]; // 假设selection列是第0列，其他列依次是1, 2, 3, 4, 5

  // 检查当前列是否在需要合并的列范围内
  if (columnsToMerge.includes(columnIndex)) {
    const prescriptionNo = row.prescriptionNo;

    // 查找当前处方号在列表中第一次出现的索引
    const firstRowIndex = medicineInfoList.value.findIndex(
      (item) => item.prescriptionNo === prescriptionNo
    );

    // 如果当前行是该处方号的首行，则合并count行
    if (rowIndex === firstRowIndex) {
      // 计算该处方号的总行数
      const count = medicineInfoList.value.filter(
        (item) => item.prescriptionNo === prescriptionNo
      ).length;
      return [count, 1]; // 合并count行，1列
    } else {
      return [0, 0]; // 其他行不显示
    }
  }

  // 其他列不进行合并
  return [1, 1];
}

function formatPrice(row, column, cellValue) {
  if (cellValue === null || cellValue === undefined) {
    return "0.00"; // 如果值为空，返回0.00
  }
  return cellValue.toFixed(2); // 保留两位小数
}

function handleCurrentChange(row) {
  currentRow.value = row; // 更新当前选中行的数据
  console.log("当前选中行的数据：", currentRow.value);
  listWesternmedicine(currentRow.value).then((response) => {
    console.log("121212", response);
    personInfo.value = response.data.prescriptionPatientInfoDto;
    medicineInfoList.value = Array.isArray(
      response.data.prescriptionMedicineInfoDtoList
    )
      ? response.data.prescriptionMedicineInfoDtoList
      : [response.data.prescriptionMedicineInfoDtoList];
    // 统计每个 prescriptionNo 的行数
    const groupCounts = countGroupRows(medicineInfoList.value);
    // 设置每行的标记
    markers.value = getRowMarkers(groupCounts, medicineInfoList.value);

    diagnoses.value = medicineInfoList.value
      .map((item) => item.诊断 || "无")
      .join(", ");
  });
}

function submitMedicine(row) {
  updateMedicion(row.prescriptionNo).then((response) => {
    proxy.$modal.msgSuccess("发药成功");
    listWesternmedicine(currentRow.value).then((response) => {
      medicineInfoList.value = Array.isArray(
        response.data.prescriptionMedicineInfoDtoList
      )
        ? response.data.prescriptionMedicineInfoDtoList
        : [response.data.prescriptionMedicineInfoDtoList];
      // 统计每个 prescriptionNo 的行数
      const groupCounts = countGroupRows(medicineInfoList.value);
      // 设置每行的标记
      markers.value = getRowMarkers(groupCounts, medicineInfoList.value);
    });
  });
}

function backMedicine(row) {
  showDialog.value = true;
  selectedPrescriptionNo.value = row.prescriptionNo;
  console.log("作废原因:", selectedPrescriptionNo.value, row.prescriptionNo);
}

function handleConfirm() {
  if (!notPerformedReasonEnum.value) {
    ElMessage.error("请选择作废原因");
    return;
  }
  backMedicion(selectedPrescriptionNo.value, notPerformedReasonEnum.value).then(
    (response) => {
      proxy.$modal.msgSuccess("作废成功");
    }
  );
  listWesternmedicine(currentRow.value).then((response) => {
    console.log("1212*******12", response);
    medicineInfoList.value = Array.isArray(
      response.data.prescriptionMedicineInfoDtoList
    )
      ? response.data.prescriptionMedicineInfoDtoList
      : [response.data.prescriptionMedicineInfoDtoList];
    // medicineInfoList.value = response.data;
    // 统计每个 prescriptionNo 的行数
    const groupCounts = countGroupRows(medicineInfoList.value);
    // 设置每行的标记
    markers.value = getRowMarkers(groupCounts, medicineInfoList.value);
  });
  showDialog.value = false;
  notPerformedReasonEnum.value = ""; // 清空选择
}

function handleCancel() {
  showDialog.value = false;
  notPerformedReasonEnum.value = ""; // 清空选择
}

// getList();
</script>

<style scoped>
.app-container {
  padding: 20px;
  display: flex;
}

.left {
  width: 28%;
}

.right {
  margin-left: 2%;
  width: 70%;
}

:deep(.el-table tbody tr:hover > td) {
  background-color: inherit !important;
}
</style>