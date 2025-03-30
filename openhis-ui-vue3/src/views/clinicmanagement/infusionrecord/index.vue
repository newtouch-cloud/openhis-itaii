<template>
  <div class="app-container">
    <div class="left">
      <el-form
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        v-show="showSearch"
      >
        <el-form-item label="医嘱执行时间">
          <el-date-picker
            v-model="dateRange"
            type="datetimerange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: auto"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="" prop="searchKey">
          <el-input
            v-model="queryParams.searchKey"
            placeholder="门诊号/病人/ID"
            clearable
            style="width: 150px"
            @keyup.enter="handleQuery"
          />
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
        style="width: 100%; height: 73%"
        highlight-current-row
        @current-change="handleCurrentChange"
      >
        <el-table-column prop="prescriptionNo" label="处方号" width="120" />
        <el-table-column prop="patientName" label="姓名" width="100" />
        <el-table-column prop="genderEnum_enumText" label="性别" width="80" />
        <el-table-column prop="ageString" label="年龄" width="80" />
        <el-table-column prop="idCard" label="身份证号" width="140" />
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
      <el-form
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        v-show="showSearch"
      >
        <el-form-item label="执行时间">
          <el-date-picker
            v-model="dateRangeRight"
            type="datetimerange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: auto"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            icon="Search"
            @click="handleQueryRight"
            style="margin-left: 10px"
            >搜索</el-button
          >
          <el-button icon="Refresh" @click="resetQueryRight">重置</el-button>
          <el-button type="primary" icon="SuccessFilled" @click="handleSubmit"
            >确认执行</el-button
          >
          <el-button type="primary" plain icon="Printer" @click="resetQuery"
            >打印患者卡</el-button
          >
          <el-button type="primary" plain icon="Printer" @click="resetQuery"
            >打印瓶签</el-button
          >
          <el-button type="primary" plain icon="Printer" @click="resetQuery"
            >打印输液单</el-button
          >
        </el-form-item>
      </el-form>
      <div>
        <p style="margin: 0px 0px 10px 0px">院注医嘱</p>
        <el-table
          :data="infusionList"
          border
          style="width: 100%; height: 300px"
          :row-style="rowStyle"
          @selection-change="handleSelectionChange"
          ref="tableRef"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="组" width="50">
            <template #default="scope">
              <span>{{ markers[scope.$index] }}</span>
            </template>
          </el-table-column>
          <!-- <el-table-column prop="groupId" label="组" width="60" /> -->
          <el-table-column prop="executeNum" label="总执行次数" width="90" />
          <el-table-column prop="doneNum" label="已执行次数" width="90" />
          <el-table-column
            prop="doctorId_dictText"
            label="开单医生"
            width="100"
          />
          <el-table-column prop="patientName" label="患者姓名" width="100" />
          <el-table-column prop="genderEnum_enumText" label="性别" width="80" />
          <el-table-column prop="prescriptionNo" label="处方号" width="140" />
          <el-table-column
            prop="medicationInformation"
            label="药品信息"
            width="180"
          />
          <el-table-column
            prop="medicationQuantity"
            label="药品数量"
            width="80"
          />
          <el-table-column prop="rateCode" label="用药频次" width="80" />
          <el-table-column prop="dose" label="单次剂量" width="160" />
          <el-table-column prop="speed" label="输液速度" width="80" />
          <el-table-column
            prop="performOrg_dictText"
            label="发放科室"
            width="120"
          />
          <el-table-column
            prop="medicationStatusEnum_enumText"
            label="药品状态"
            width="100"
          />
          <el-table-column
            prop="skinTestFlag_enumText"
            label="是否皮试"
            width="60"
          />
          <!-- <el-table-column prop="clinicalStatusEnum_enumText" label="皮试结果" width="70" /> -->
        </el-table>
      </div>
      <div>
        <p style="margin: 13px 0px 10px 0px">院注执行历史</p>
        <el-table
          :data="historyRecordsList"
          border
          style="width: 100%; height: 300px"
        >
          <el-table-column
            prop="occurrenceEndTime"
            label="执行时间"
            width="180"
          >
            <template #default="scope">
              <el-date-picker
                v-model="scope.row.occurrenceEndTime"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 170px"
              />
            </template>
          </el-table-column>
          <el-table-column
            prop="performerId_dictText"
            label="执行人"
            width="80"
          />
          <el-table-column prop="patientName" label="患者姓名" width="100" />
          <el-table-column prop="prescriptionNo" label="处方号" width="100" />
          <el-table-column
            prop="doctorId_dictText"
            label="开单医生"
            width="100"
          />
          <el-table-column
            prop="medicationInformation"
            label="药品信息"
            width="180"
          />
          <el-table-column
            prop="medicationQuantity"
            label="药品数量"
            width="80"
          />
          <el-table-column prop="rateCode" label="用药频次" width="80" />
          <el-table-column prop="dose" label="单词剂量" width="160" />
          <el-table-column prop="speed" label="输液速度" width="80" />
          <el-table-column prop="performOrg_dictText" label="发放科室" width="120" />
          <el-table-column
            prop="medicationStatusEnum_enumText"
            label="药品状态"
            width="100"
          />
          <el-table-column
            label="操作"
            align="center"
            width="90"
            fixed="right"
            class-name="small-padding fixed-width"
          >
            <template #default="scope">
              <el-button
                link
                type="primary"
                icon="Edit"
                @click="handleUpdateTime(scope.row)"
                >确认</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script setup name="InfusionRecord">
import { ref, computed } from "vue";
import {
  listPatients,
  updateInfusionRecord,
  listInfusionRecord,
  editPatientInfusionTime,
  listPatientInfusionRecord,
  listPatientInfusionPerformRecord,
} from "./component/api";

const showSearch = ref(true);
const total = ref(1);
const selectedItems = ref(new Set());

const tableRef = ref(null);
const selectedGroupIds = ref(new Set());

const currentRow = ref(null);
const dateRange = ref([]);
const dateRangeRight = ref([]);
const historyRecordsList = ref([]);
const patientList = ref([]);
const infusionList = ref([]);
const timeRightStart = ref([]);
const timeRightEnd = ref([]);

const groupColors = ["#C6E2FF", "#FFFFFF"];
const markers = ref([]);

const { proxy } = getCurrentInstance();

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    searchKey: undefined,
  },
});
const { queryParams } = toRefs(data);

/** 查询门诊输液列表 */
function getList() {
  listInfusionRecord(queryParams.value).then((response) => {
    console.log("Full response1:", response);
    infusionList.value = response.data;
    // 为每个 groupId 分配固定颜色
    response.data.forEach((item) => {
      const colorIndex = item.groupId % 2; // 奇偶性决定颜色索引
      item.color = groupColors[colorIndex];
    });
    // 更新表格行的样式
    updateTableRowStyles();
    // 统计每个 groupId 的行数
    const groupCounts = countGroupRows(infusionList.value);
    // 设置每行的标记
    markers.value = getRowMarkers(groupCounts, infusionList.value);
  });
  listPatients().then((response) => {
    patientList.value = response.data.records;
  });
}
function updateTableRowStyles() {
  const tableRows = document.querySelectorAll(".infusion-table-row");
  tableRows.forEach((row) => {
    const groupId = row.getAttribute("data-group-id");
    const color = groupColors[groupId % 2]; // 奇偶性决定颜色
    if (color) {
      row.style.backgroundColor = color;
    }
  });
}
/** 搜索按钮操作 */
function handleQuery() {
  if (dateRange.value) {
    queryParams.value.createTimeSTime = dateRange.value[0];
    queryParams.value.createTimeETime = dateRange.value[1];
  } else {
    queryParams.value.createTimeSTime = null;
    queryParams.value.createTimeETime = null;
  }
  queryParams.value.pageNo = 1;
  listPatients(queryParams.value).then((response) => {
    patientList.value = response.data.records;
  });
}
/** 右边搜索按钮操作 */
function handleQueryRight() {
  const createTimeSTime = dateRangeRight.value[0];
  const createTimeETime = dateRangeRight.value[1];
  timeRightStart.value = createTimeSTime;
  timeRightEnd.value = createTimeETime;
  listInfusionRecord(createTimeSTime, createTimeETime).then((response) => {
    infusionList.value = response.data;
  });
  listPatientInfusionPerformRecord(createTimeSTime, createTimeETime).then((response) => {
      historyRecordsList.value = response.data;
    }
  );
}
/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = [];
  proxy.resetForm("queryRef");
  getList();
  listPatientInfusionPerformRecord().then((response) => {
    historyRecordsList.value = response.data;
  });
}

/** 右边重置按钮操作 */
function resetQueryRight() {
  if (historyRecordsList.value.length > 0) {
    dateRangeRight.value = [];
    listInfusionRecord().then((response) => {
      infusionList.value = response.data;
    });
    listPatientInfusionPerformRecord().then((response) => {
      historyRecordsList.value = response.data;
    });
  } else {
    // 清空选中状态
    selectedItems.value.clear();
    selectedGroupIds.value.clear();
    dateRangeRight.value = [];
    // 取消表格所有行的选中状态
    infusionList.value.forEach((row) => {
      tableRef.value.toggleRowSelection(row, false);
    });
    listPatientInfusionRecord(currentRow.value).then((response) => {
      infusionList.value = response.data;
    });
  }
}

function getRowMarkers(groupCounts, data) {
  const markers = new Array(data.length).fill("");

  groupCounts.forEach((groupInfo, groupId) => {
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

// 生成行数
function countGroupRows(data) {
  const groupCounts = new Map();
  data.forEach((item, index) => {
    if (!groupCounts.has(item.groupId)) {
      groupCounts.set(item.groupId, { count: 0, indices: [] });
    }
    const groupInfo = groupCounts.get(item.groupId);
    groupInfo.count++;
    groupInfo.indices.push(index);
  });
  return groupCounts;
}

// 执行输液
function handleSubmit() {
  const itemsList = Array.from(selectedItems.value);
  // 如果没有有效数据，直接返回
  if (itemsList.length === 0) {
    proxy.$modal.msgError("没有有效的数据可供提交");
    return;
  }

  const allCompleted = itemsList.every(
    (item) => item.medicationStatusEnum_enumText === "已完成"
  );
  if (!allCompleted) {
    // 如果存在未完成的药品，提示用户
    proxy.$modal.msgError("存在未完成的药品，请检查后再提交");
    return;
  }

  const allExecuted = itemsList.every(
    (item) => item.executeNum === item.doneNum
  );
  if (allExecuted) {
    // 如果所有药品的 executeNum 和 doneNum 都相等，提示用户
    proxy.$modal.msgError("已执行完总次数");
    return;
  }
  updateInfusionRecord(itemsList).then((response) => {
    proxy.$modal.msgSuccess("执行成功");
    clearSelections();
  });
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
    const groupId = item.groupId;
    // 检查 groupId 是否同时存在
    if (selectedGroupIds.value.has(groupId)) {
      // 如果都存在，则移除它们
      selectedGroupIds.value.delete(groupId);
    } else {
      // 否则添加它们
      selectedGroupIds.value.add(groupId);
    }
  });
  // 动态更新表格行的选中状态
  infusionList.value.forEach((row) => {
    const isSelected = selectedGroupIds.value.has(row.groupId);
    tableRef.value.toggleRowSelection(row, isSelected);
  });
}
function clearSelections() {
  dateRangeRight.value = [];
  if (!currentRow.value) {
    const createTimeSTime = timeRightStart.value || null;
    const createTimeETime = timeRightEnd.value || null;
    listInfusionRecord(createTimeSTime, createTimeETime).then((response) => {
      infusionList.value = response.data;
    });
  } else {
    listPatientInfusionRecord(currentRow.value).then((response) => {
      infusionList.value = response.data;
    });
  }
  listPatientInfusionPerformRecord().then((response) => {
    historyRecordsList.value = response.data;
  });
}
function rowStyle({ row }) {
  const colorIndex = row.groupId % 2; // 奇偶性决定颜色索引
  return { backgroundColor: groupColors[colorIndex] };
}
function handleUpdateTime(row) {
  console.log("row", row);
  editPatientInfusionTime(row).then((response) => {
    proxy.$modal.msgSuccess("执行成功");
    open.value = false;
    getList();
  });
}

function handleCurrentChange(row) {
  currentRow.value = row; // 更新当前选中行的数据
  console.log("当前选中行的数据：", currentRow.value);
  listPatientInfusionRecord(currentRow.value).then((response) => {
    infusionList.value = response.data;
	// 统计每个 groupId 的行数
    const groupCounts = countGroupRows(infusionList.value);
    // 设置每行的标记
    markers.value = getRowMarkers(groupCounts, infusionList.value);
  });
  listPatientInfusionPerformRecord(currentRow.value).then((response) => {
    historyRecordsList.value = response.data;
  });
}

getList();
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