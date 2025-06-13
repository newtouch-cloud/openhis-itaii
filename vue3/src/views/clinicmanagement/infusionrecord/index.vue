<template>
  <div class="app-container">
    <div class="left">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
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
        <el-form-item label="基础信息" prop="searchKey">
          <el-input
            v-model="queryParams.searchKey"
            placeholder="门诊号/病人/ID"
            clearable
            style="width: 150px"
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="执行状态" prop="searchKey">
          <el-select
            v-model="queryParams.serviceStatus"
            placeholder="执行状态"
            style="width: 150px"
            @change="handleQuery"
          >
            <el-option
              v-for="item in statusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <div style="margin-bottom: 10px">
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </div>
      </el-form>

      <el-table
        :data="patientList"
        border
        style="width: 100%; height: 73%"
        highlight-current-row
        @row-click="handleCurrentChange"
      >
        <el-table-column prop="encounterBusNo" label="就诊号" width="120px" />
        <el-table-column prop="patientName" label="姓名" />
        <el-table-column prop="genderEnum_enumText" label="性别" />
        <el-table-column prop="ageString" label="年龄" />
        <el-table-column prop="serviceStatus_enumText" label="执行状态" />
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
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
        <el-form-item>
          <el-button type="primary" icon="SuccessFilled" @click="handleSubmit">确认执行</el-button>
          <el-button type="primary" plain icon="Printer" @click="resetQuery">打印患者卡</el-button>
          <el-button type="primary" plain icon="Printer" @click="printLabel">打印瓶签</el-button>
          <el-button type="primary" plain icon="Printer" @click="printbloodLabel"
            >打印采血条码</el-button
          >
          <el-button type="primary" plain icon="Printer" @click="resetQuery">打印输液单</el-button>
        </el-form-item>
      </el-form>
      <div>
        <p style="margin: 0px 0px 10px 0px">院注医嘱</p>
        <el-table
          :data="infusionList"
          highlight-current-row
          border
          style="width: 100%; height: 300px"
          :row-style="rowStyle"
          @selection-change="handleSelectionChange"
          @row-click="handleRowClick"
          ref="tableRef"
        >
          <el-table-column type="selection" width="55" align="center" />
          <!-- <el-table-column label="组" width="50">
            <template #default="scope">
              <span>{{ markers[scope.$index] }}</span>
            </template>
          </el-table-column> -->
          <!-- <el-table-column prop="groupId" label="组" width="60" /> -->
          <el-table-column prop="busNo" label="编码" align="center" />
          <el-table-column prop="serviceName" label="项目" align="center" />
          <el-table-column prop="executeNum" label="总执行次数" align="center" />
          <el-table-column prop="performCount" label="已执行次数" align="center" />
          <el-table-column prop="practitionerName" label="开单医生" align="center" />
          <el-table-column prop="medicationName" label="药品信息" align="center" />
          <el-table-column prop="dose" label="药品数量" align="center">
            <template #default="scope">
              <span style="text-align: right">
                {{
                  scope.row.unitCode_dictText
                    ? scope.row.quantity + ' ' + scope.row.unitCode_dictText
                    : ''
                }}
              </span>
            </template>
          </el-table-column>
          <!-- <el-table-column prop="speed" label="输液速度" width="80" /> -->
          <el-table-column prop="dispenseStatus_enumText" label="药品状态" align="center" />
          <el-table-column prop="skinTestFlag_enumText" label="皮试标志" align="center" />
          <!-- <el-table-column prop="clinicalStatusEnum_enumText" label="皮试结果" width="70" /> -->
        </el-table>
      </div>
      <div>
        <p style="margin: 13px 0px 10px 0px">院注执行历史</p>
        <el-table :data="historyRecordsList" border style="width: 100%; height: 300px">
          <el-table-column prop="occurrenceEndTime" label="执行时间" align="center" width="260">
            <template #default="scope">
              <div v-if="scope.row.isEdit" @dblclick="scope.row.isEdit = !scope.row.isEdit">
                {{ formatDate(scope.row.occurrenceEndTime) }}
              </div>
              <el-date-picker
                v-else
                v-model="scope.row.occurrenceEndTime"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 220px"
                @change="(value) => handleOccurrenceTimeChange(value, scope.row)"
              />
            </template>
          </el-table-column>
          <el-table-column prop="performerName" label="执行人" align="center" />
          <el-table-column prop="serviceStatus_enumText" label="执行状态" align="center" />
          <el-table-column prop="serviceName" label="项目" align="center" />
          <el-table-column prop="orgName" label="执行科室" align="center" />
          <el-table-column
            label="操作"
            align="center"
            width="90"
            fixed="right"
            class-name="small-padding fixed-width"
          >
            <template #default="scope">
              <el-button link type="primary" icon="Edit" @click="handleCancelPerform(scope.row)">
                撤销
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script setup name="InfusionRecord">
import { ref, computed } from 'vue';
import {
  listPatients,
  updateInfusionRecord,
  listInfusionRecord,
  editPatientInfusionTime,
  listPatientInfusionPerformRecord,
  getBottleLabel,
  cancelPerform,
  init,
} from './component/api';
import { formatDate } from '@/utils/index';

const showSearch = ref(true);
const total = ref(1);
const selectedItems = ref([]);

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
const statusOptions = ref([]);
const recordRow = ref({}); // 待执行点击行

const groupColors = ['#C6E2FF', '#FFFFFF'];
const markers = ref([]);

const { proxy } = getCurrentInstance();

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    searchKey: undefined,
    serviceStatus: 10,
  },
});
const { queryParams } = toRefs(data);
initOptions();
function initOptions() {
  init().then((res) => {
    statusOptions.value = res.data.serviceStatusOptions;
  });
}

/** 查询门诊输液列表 */
function getList() {
  listPatients(queryParams.value).then((response) => {
    patientList.value = response.data.records;
  });
}
function updateTableRowStyles() {
  const tableRows = document.querySelectorAll('.infusion-table-row');
  tableRows.forEach((row) => {
    const groupId = row.getAttribute('data-group-id');
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
/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = [];
  proxy.resetForm('queryRef');
  getList();
}
/** 打印瓶贴 */
async function printLabel() {
  const selectedRows = proxy.$refs['tableRef'].getSelectionRows();
  if (selectedRows.length === 0) {
    proxy.$modal.msgWarning('未选择要打印的医嘱，请重新选择，打印失败');
    return;
  }
  const result = {
    data: selectedRows.map((item) => {
      // 创建新对象避免修改原数据
      return {
        ...item,
        // 当 groupid 为 null 时，用 bus_no 替换
        groupId: item.groupId === null ? item.busNo : item.groupId,
        name: currentRow.value.patientName,
        sex: currentRow.value.genderEnum_enumText,
        age: currentRow.value.ageString,
        printDate: formatDate(new Date(), 'yyyy-MM-dd hh:mm:ss'),
      };
    }),
  };
  // 将对象转换为 JSON 字符串
  let jsonString = JSON.stringify(result, null, 2);
  await window.chrome.webview.hostObjects.CSharpAccessor.PrintReport(
    '输液瓶贴.grf',
    jsonString
  )
    .then((response) => {
      //返回结果是jsonString，可判断其调用是否成功
      console.log(response, 'response');
      var res = JSON.parse(response);
      if (!res.IsSuccess) {
        proxy.$modal.msgError('调用打印插件失败:' + res.ErrorMessage);
      }
    })
    .catch((error) => {
      proxy.$modal.msgError('调用打印插件失败:' + error);
    });
}
//打印采血条码
async function printbloodLabel() {
  const selectedRows = proxy.$refs['tableRef'].getSelectionRows();
  if (selectedRows.length === 0) {
    proxy.$modal.msgWarning('未选择要打印的检验项目，请重新选择，打印失败');
    return;
  }
  // 构造一个新的对象，添加头 "data"
  const result = {
    data: selectedRows.map((item) => {
      // 创建新对象避免修改原数据
      return {
        ...item,
        // 当 groupid 为 null 时，用 bus_no 替换
        groupId: item.groupId === null ? item.busNo : item.groupId,
        name: currentRow.value.patientName,
        sex: currentRow.value.genderEnum_enumText,
        age: currentRow.value.ageString,
      };
    }),
  };
  // 将对象转换为 JSON 字符串
  let jsonString = JSON.stringify(result, null, 2);
  console.log(jsonString, 'jsonstring');
  await window.chrome.webview.hostObjects.CSharpAccessor.PrintReport('采血条码.grf', jsonString)
    .then((response) => {
      //返回结果是jsonString，可判断其调用是否成功
      console.log(response, 'response');
      var res = JSON.parse(response);
      if (!res.IsSuccess) {
        proxy.$modal.msgError('调用打印插件失败:' + res.ErrorMessage);
      }
    })
    .catch((error) => {
      proxy.$modal.msgError('调用打印插件失败:' + error);
    });
}
function handleRowClick(row) {
  recordRow.value = row;
  listPatientInfusionPerformRecord({
    serviceReqId: row.serviceId,
  }).then((response) => {
    historyRecordsList.value =
      response.data.records.length > 0
        ? response.data.records.map((item) => {
            return {
              ...item,
              isEdit: true,
            };
          })
        : [];
  });
}

function getRowMarkers(groupCounts, data) {
  const markers = new Array(data.length).fill('');

  groupCounts.forEach((groupInfo, groupId) => {
    const { count, indices } = groupInfo;
    if (count === 1) {
      // 如果只有一行，不显示标记
      return;
    } else if (count === 2) {
      // 如果有两行，分别显示左右括号
      markers[indices[0]] = '┏';
      markers[indices[1]] = '┗ ';
    } else {
      // 如果有两行以上，第一条显示左括号，中间用竖线，最后一条显示右括号
      markers[indices[0]] = '┏';
      for (let i = 1; i < indices.length - 1; i++) {
        markers[indices[i]] = '┃';
      }
      markers[indices[indices.length - 1]] = '┗ ';
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
  console.log(selectedItems.value);

  const itemsList = selectedItems.value.map((item) => {
    return item.serviceId;
  });
  // 如果没有有效数据，直接返回
  if (itemsList.length === 0) {
    proxy.$modal.msgError('没有有效的数据可供提交');
    return;
  }
  // const allExecuted = itemsList.every((item) => item.executeNum === item.doneNum);
  // if (allExecuted) {
  //   // 如果所有药品的 executeNum 和 doneNum 都相等，提示用户
  //   proxy.$modal.msgError('已执行完总次数');
  //   return;
  // }
  updateInfusionRecord(itemsList).then((response) => {
    proxy.$modal.msgSuccess('执行成功');
    clearSelections();
  });
}

function handleSelectionChange(selection) {
  // // 清空之前选中的数据
  selectedItems.value = [];
  // // 将当前选中的数据存到 selectedItems 中
  selection.forEach((item) => {
    selectedItems.value.push(item);
  });
  // // 更新 selectedGroupIds
  // selection.forEach((item) => {
  //   const groupId = item.groupId;
  //   // 检查 groupId 是否同时存在
  //   if (selectedGroupIds.value.has(groupId)) {
  //     // 如果都存在，则移除它们
  //     selectedGroupIds.value.delete(groupId);
  //   } else {
  //     // 否则添加它们
  //     selectedGroupIds.value.add(groupId);
  //   }
  // });
  // // 动态更新表格行的选中状态
  // infusionList.value.forEach((row) => {
  //   const isSelected = selectedGroupIds.value.has(row.groupId);
  //   tableRef.value.toggleRowSelection(row, isSelected);
  // });
}
function clearSelections() {
  listInfusionRecord(currentRow.value.patientId).then((response) => {
    infusionList.value = response.data;
  });
  listPatientInfusionPerformRecord(currentRow.value.patientId).then((response) => {
    historyRecordsList.value = response.data;
  });
}
// function rowStyle({ row }) {
//   const colorIndex = row.groupId % 2; // 奇偶性决定颜色索引
//   return { backgroundColor: groupColors[colorIndex] };
// }

function handleCurrentChange(row) {
  currentRow.value = row; // 更新当前选中行的数据
  console.log(row, '123123');

  listInfusionRecord({ encounterId: row.encounterId, serviceStatus: row.serviceStatus }).then((response) => {
    console.log('Full response1:', response);
    infusionList.value = response.data.records;
    // 为每个 groupId 分配固定颜色
    response.data.records.forEach((item) => {
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
  historyRecordsList.value = [];
  // listPatientInfusionPerformRecord(currentRow.value.patientId).then((response) => {
  //   historyRecordsList.value = response.data;
  // });
}

function handleOccurrenceTimeChange(value, row) {
  editPatientInfusionTime({ serviceReqId: row.serviceId, performTime: value }).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess('操作成功');
      row.isEdit = true;
    }
  });
}

function handleCancelPerform(row) {
  cancelPerform(row).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess('操作成功');
      handleRowClick(recordRow.value);
    }
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
