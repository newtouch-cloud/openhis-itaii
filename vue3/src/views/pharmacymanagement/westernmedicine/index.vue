<template>
  <div class="app-container">
    <div class="left">
      <div class="form">
        <el-form
          :model="queryParams"
          ref="queryRef"
          :inline="true"
          v-show="showSearch"
          label-position="right"
          style="min-width: 500px"
        >
          <el-form-item label="姓名或证件号" prop="condition">
            <el-input
              v-model="queryParams.condition"
              placeholder="请输入姓名/证件号"
              clearable
              style="width: 160px"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-form-item label="发药状态" prop="departmentId" style="margin-left: 40px">
            <el-select
              v-model="queryParams.statusEnum"
              placeholder="请选择发药状态"
              clearable
              style="width: 160px"
              @change="handleQuery"
            >
              <el-option
                v-for="item in dispenseStatusOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="就诊日期">
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 250px"
              value-format="YYYY-MM-DD"
              @change="handleQuery"
            />
          </el-form-item>
          <!-- <el-form-item label="科室" prop="departmentId">
            <el-select
              v-model="queryParams.departmentId"
              placeholder="请选择科室"
              clearable
              style="width: 160px"
              @change="handleQuery"
            >
              <el-option
                v-for="item in departmentList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item> -->
          <el-form-item style="float: right">
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table
        :data="patientList"
        border
        style="width: 100%; height: 60vh"
        highlight-current-row
        @row-click="handleCurrentChange"
      >
        <el-table-column prop="departmentName" label="科室" width="150" align="center" />
        <el-table-column prop="patientName" label="姓名" width="130" align="center" />
        <el-table-column prop="genderEnum_enumText" label="性别" width="80" align="center" />
        <!-- <el-table-column prop="ageString" label="开单医生" width="80" /> -->
        <el-table-column prop="receptionTime" label="就诊日期" align="center">
          <template #default="scope">
            {{ scope.row.receptionTime ? formatDate(scope.row.receptionTime) : '-' }}
          </template>
        </el-table-column>
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
      <!-- <div class="select_wrapper_div">
        <p style="margin-right: 60px; font-size: 19px">患者基本信息</p> -->

      <!-- <el-button type="success" plain @click="print" icon="Printer" style="margin-left: 30px;">打印</el-button> -->
      <!-- </div> -->
      <div class="top">
        <el-descriptions :column="4" title="患者基本信息">
          <el-descriptions-item label="姓名：">{{ personInfo.patientName }}</el-descriptions-item>
          <el-descriptions-item label="性别：">
            {{ personInfo.genderEnum_enumText }}
          </el-descriptions-item>
          <el-descriptions-item label="年龄：">{{ personInfo.age }}</el-descriptions-item>
          <el-descriptions-item label="合同类型：">
            {{ personInfo.categoryEnum_enumText }}
          </el-descriptions-item>
          <el-descriptions-item label="就诊科室：">
            {{ personInfo.organizationName }}
          </el-descriptions-item>
          <el-descriptions-item label="就诊日期：">
            {{ personInfo.encounterDate }}
          </el-descriptions-item>
          <el-descriptions-item label="证件号：">{{ personInfo.idCard }}</el-descriptions-item>
          <el-descriptions-item label="总金额：">
            {{ personInfo.totalPrice ? personInfo.totalPrice.toFixed(2) : '0.00' }}元
          </el-descriptions-item>
        </el-descriptions>
        <!-- <el-row>
          <el-col :span="4">姓名：{{ personInfo.patientName }}</el-col>
          <el-col :span="3">性别：{{ personInfo.genderEnum_enumText }}</el-col>
          <el-col :span="3">年龄：{{ personInfo.age }}</el-col>
          <el-col :span="5">合同类型：{{ personInfo.categoryEnum_enumText }}</el-col> </el-row
        ><br />
        <el-row>
          <el-col :span="5">就诊科室：{{ personInfo.organizationName }}</el-col>
          <el-col :span="5">就诊日期：{{ personInfo.encounterDate }}</el-col>
          <el-col :span="7">证件号：{{ personInfo.idCard }}</el-col>

           </el-row
        ><br />
        <el-row>
          <el-col :span="4"
            >总金额：{{
              personInfo.totalPrice ? personInfo.totalPrice.toFixed(2) : '0.00'
            }}元</el-col
          >
        </el-row> -->
        <span style="color: #606266; font-size: 14px; font-weight: 700; margin-right: 15px"
          >调配药师</span
        >
        <el-select v-model="preparerDoctor" placeholder="调配药师" style="width: 160px">
          <el-option
            v-for="item in preparerDoctorOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <el-button
          :disabled="medicineInfoList && medicineInfoList.length == 0"
          type="primary"
          @click="handleBatch()"
          style="margin-left: 30px"
        >
          批量发药
        </el-button>
        <el-button type="primary" @click="handleScan()" style="margin-left: 30px"> 扫码 </el-button>
        <el-button
          :disabled="medicineInfoList && medicineInfoList.length == 0"
          type="primary"
          @click="printPrescription()"
          style="margin-left: 30px"
        >
          处方打印
        </el-button>
      </div>
      <el-table
        :data="medicineInfoList"
        border
        style="width: 100%; height: 65vh; margin-top: 10px"
        :row-style="rowStyle"
        :span-method="spanMethod"
        @select="handleSelectionChange"
        ref="tableRef"
      >
        <el-table-column type="selection" width="55" align="center" fixed="left" />
        <el-table-column prop="departmentName" label="科室" width="90" align="center" />
        <el-table-column prop="doctorName" label="开单医生" width="100" align="center" />
        <el-table-column prop="statusEnum_enumText" label="发药状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="tagType(scope.row.statusEnum)">{{
              scope.row.statusEnum_enumText
            }}</el-tag>
          </template>
        </el-table-column>
        <!-- <el-table-column prop="itemType" label="项目类型" width="100" align="center" /> -->
        <el-table-column prop="conditionName" label="诊断" width="120" align="center" />
        <el-table-column prop="prescriptionNo" label="处方号" width="120" align="center" />
        <!-- <el-table-column prop="markers" label="成组" width="60" align="center">
          <template #default="scope">
            <span>{{ markers[scope.$index] }}</span>
          </template>
        </el-table-column> -->
        <el-table-column prop="flag" label="组合" width="60" align="center" />
        <el-table-column prop="quantity" label="发药数量" width="100" align="center">
          <template #default="scope">
            {{ scope.row.quantity }}{{ scope.row.unitCode_dictText }}
          </template>
        </el-table-column>
        <!-- <el-table-column prop="quantity" label="发药数量" width="100" align="center" /> -->
        <el-table-column
          prop="totalPrice"
          label="金额"
          width="100"
          :formatter="formatPrice"
          align="right"
          header-align="center"
        />
        <el-table-column prop="medicineName" label="药品名称" width="120" align="center" />
        <el-table-column prop="lotNumber" label="批次号" width="120" align="center" />
        <el-table-column prop="totalVolume" label="规格" width="100" align="center" />
        <!-- <el-table-column prop="unitCode_dictText" label="单位" width="100" align="center" /> -->
        <el-table-column prop="doseUnitCode_dictText" label="单次剂量" width="100" align="center">
          <template #default="scope">
            {{ scope.row.dose }}{{ scope.row.doseUnitCode_dictText }}
          </template>
        </el-table-column>
        <!-- <el-table-column prop="dose" label="剂量" width="100" align="center" /> -->
        <el-table-column prop="rateCode" label="频次" width="100" align="center" />
        <el-table-column prop="methodCode_dictText" label="用法" width="100" align="center" />
        <el-table-column prop="dispensePerDuration" label="天数" width="80" align="center" />
        <el-table-column prop="traceNo" label="追溯码" width="180" align="center">
          <template #default="scope">
            <el-input v-model="scope.row.traceNo" placeholder="请输入追溯码" />
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="160"
          fixed="right"
          class-name="small-padding fixed-width"
        >
          <template #default="scope">
            <el-button
              link
              type="primary"
              :disabled="scope.row.statusEnum != 2"
              icon="SuccessFilled"
              @click="handleBatch(scope.row)"
            >
              发药
            </el-button>
            <el-button
              link
              type="primary"
              :disabled="scope.row.statusEnum != 2"
              @click="backMedicine(scope.row)"
              icon="CircleClose"
            >
              作废
            </el-button>
          </template>
        </el-table-column>
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
        <!-- <el-table-column
          prop="unitPrice"
          label="单价"
          width="100"
          :formatter="formatPrice"
          align="right"
          header-align="center"
        /> -->
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
          <el-button type="primary" @click="handleConfirm">确 定</el-button>
          <el-button @click="handleCancel">取 消</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog
      title="药品追溯码"
      v-model="openTraceNo"
      width="800"
      append-to-body
      destroy-on-close
      :draggable="true"
      @opened="
        () => {
          console.log(123);
          traceNoTempRef.focus();
        }
      "
    >
      <!-- <div>
        <el-input
          v-model="traceNo"
          style="width: 260px; margin-right: 20px"
          @change="handelTraceNo"
        />
        <el-button type="warning" plain icon="CircleClose" @click="handleClear">清除</el-button>
      </div> -->
      <div>
        <div style="font-size: 16px">
          选择追溯码
          <el-input
            ref="traceNoTempRef"
            v-model="traceNoTemp"
            style="width: 260px; margin-right: 20px"
            @input="throttledGetList"
          />
          <el-button
            type="warning"
            plain
            icon="CircleClose"
            @click="handleClear"
            style="float: right"
            >清除</el-button
          >
        </div>
        <el-input
          ref="inputRef"
          v-model="traceNo"
          type="textarea"
          disabled
          :rows="15"
          @input="throttledGetList"
          style="width: 100%; margin-top: 10px; margin-right: 20px"
        />
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submit">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="westernmedicine">
import { ref, computed, onMounted, onBeforeMount } from 'vue';
import { ElMessage } from 'element-plus';
import {
  listWesternmedicine,
  listPatient,
  updateMedicion,
  listInit,
  backMedicion,
  prepareMedicion,
  itemTraceNo,
} from './components/api';
import { formatDate, formatDateStr } from '@/utils/index';
import { debounce } from 'lodash-es';

const showSearch = ref(true);
const total = ref(0);
const markers = ref([]);
const patientList = ref([]);
const medicineInfoList = ref([]);
const departmentList = ref([]);
const dateRange = ref([]);
const personInfo = ref([]);
const dispenseStatusOptions = ref([]);
const preparerDoctorOptions = ref([]);
const diagnoses = ref('');
const preparerDoctor = ref();
const backReason = ref([]);
const selectedPrescriptionNo = ref('');
const showDialog = ref(false);
const notPerformedReasonEnum = ref();
const currentRow = ref(null);
const tableRef = ref(null);
const selectedGroupIds = ref(new Set());
const selectedItems = ref(new Set());
const traceNoList = ref([]);
const traceNo = ref('');
const traceNoTemp = ref('');
const openTraceNo = ref(false);
const isManualSelection = ref(false);
const groups = ref({});
const traceNoTempRef = ref();

const { proxy } = getCurrentInstance();

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    condition: null,
    departmentId: null,
    statusEnum: 3,
  },
});
const { queryParams } = toRefs(data);

// 在组件挂载后调用 getList
onMounted(() => {
  setDefaultDateRange();
  getList();
});
function handleScan() {
  openTraceNo.value = true;
  // traceNo.value = '';
  // traceNoList.value = [];
  // document.addEventListener('keydown', function (event) {
  //   // 检查是否为条码输入
  //   if (event.key === 'Enter' || event.key === 'Tab') {
  //     // 某些扫码枪在输入后可能发送Enter或Tab键
  //     traceNo.value = event.target.value;
  //     traceNoList.value.push(traceNo.value);
  //     // 处理条码数据
  //   }
  // });
}
function submit() {
  itemTraceNo(traceNoList.value).then((res) => {
    if (res.code === 200) {
      medicineInfoList.value.forEach((item, index) => {
        if (res.data[item.medicineId] && res.data[item.medicineId].split(',') > item.quantity) {
          proxy.$modal.msgWarning('操作失败');
          return;
        }
        medicineInfoList.value[index].traceNo = res.data[item.medicineId];
      });
      openTraceNo.value = false;
      traceNoList.value = [];
    }
  });
}
function handleClear() {
  traceNo.value = '';
  traceNoList.value = [];
}
function cancel() {
  openTraceNo.value = false;
  traceNoList.value = [];
  traceNo.value = '';
}
// 设置默认日期范围为当天
function setDefaultDateRange() {
  const today = new Date();
  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, '0');
  const day = String(today.getDate()).padStart(2, '0');
  const formattedDate = `${year}-${month}-${day}`;
  dateRange.value = [formattedDate, formattedDate];
  console.log('222', dateRange.value);
}

function getList() {
  console.log('224555552', dateRange.value);
  queryParams.value.receptionTimeSTime = dateRange.value[0] + ' 00:00:00';
  queryParams.value.receptionTimeETime = dateRange.value[1] + ' 23:59:59';

  console.log('222', queryParams.value);
  listPatient(queryParams.value).then((response) => {
    console.log('Full response1:', response);
    patientList.value = response.data.records;
    total.value = response.data.total;
  });
  listInit().then((response) => {
    console.log('Full response2:', response);
    departmentList.value = [{ value: null, label: '全部' }, ...response.data.departmentOptions];
    backReason.value = response.data.notPerformedReasonOptions;
    dispenseStatusOptions.value = response.data.dispenseStatusOptions;
    preparerDoctorOptions.value = response.data.preparerDoctorOptions;
    preparerDoctor.value = preparerDoctorOptions.value[0].value;
  });
}
//打印中西药处方
async function printPrescription() {
  const selectedRows = tableRef.value.getSelectionRows();
  if (selectedRows.length === 0) {
    proxy.$modal.msgWarning('未选择要打印的项目，请重新选择，打印失败');
    return;
  }
  console.log(selectedRows, personInfo, 'selectedRows');
  // 计算总价

  const result = {
    data: selectedRows.map((item) => ({
      ...item,
      patientName: personInfo.value.patientName,
      genderEnum_enumText: personInfo.value.genderEnum_enumText,
      age: personInfo.value.age,
      organizationName: personInfo.value.organizationName,
    })),
  };
  // 将对象转换为 JSON 字符串
  let jsonString = JSON.stringify(result, null, 2);
  await window.chrome.webview.hostObjects.CSharpAccessor.PrintReport(
    '西药(中成药)处方笺.grf',
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
/** 重置按钮操作 */
function resetQuery() {
  setDefaultDateRange();
  medicineInfoList.value = [];
  personInfo.value = [];
  proxy.resetForm('queryRef');
  getList();
}
/** 搜索按钮操作 */
function handleQuery() {
  if (dateRange.value) {
    queryParams.value.receptionTimeSTime = dateRange.value[0] + ' 00:00:00';
    queryParams.value.receptionTimeETime = dateRange.value[1] + ' 23:59:59';
  } else {
    queryParams.value.receptionTimeSTime = null;
    queryParams.value.receptionTimeETime = null;
  }
  queryParams.value.pageNo = 1;
  console.log('************', queryParams.value);
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
  const markers = new Array(data.length).fill('');

  groupCounts.forEach((groupInfo, prescriptionNo) => {
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

function spanMethod({ row, column, rowIndex, columnIndex }) {
  // 定义需要合并的列范围（前6列，包括selection列）
  const columnsToMerge = [1, 2, 3, 4, 5, 17]; // 假设selection列是第0列，其他列依次是1, 2, 3, 4, 5

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

function handleSelectionChange(selectedRows, currentRow) {
  medicineInfoList.value
    .filter((item) => {
      return item.prescriptionNo == currentRow.prescriptionNo;
    })
    .forEach((item, index) => {
      tableRef.value.toggleRowSelection(item, selectedRows.includes(currentRow));
    });
}

function formatPrice(row, column, cellValue) {
  if (cellValue === null || cellValue === undefined) {
    return '0.00 元'; // 如果值为空，返回0.00
  }
  return cellValue.toFixed(2) + ' 元'; // 保留两位小数
}

function tagType(statusEnum) {
  if (statusEnum == 2 || statusEnum == 2) {
    return 'default';
  } else if (statusEnum == 4) {
    return 'success';
  } else if (statusEnum == 5 || statusEnum == 6 || statusEnum == 7) {
    return 'warning';
  } else if (statusEnum == 12) {
    return 'info';
  }
}

function handleCurrentChange(row) {
  currentRow.value = row; // 更新当前选中行的数据
  console.log('当前选中行的数据：', currentRow.value);
  currentRow.value.statusEnum = undefined;
  currentRow.value.dispenseStatus = queryParams.value.statusEnum;
  listWesternmedicine(currentRow.value).then((response) => {
    console.log('121212', response);
    personInfo.value = response.data.prescriptionPatientInfoDto;
    medicineInfoList.value = Array.isArray(response.data.prescriptionMedicineInfoDtoList)
      ? response.data.prescriptionMedicineInfoDtoList
      : [response.data.prescriptionMedicineInfoDtoList];
    // 创建分组映射表
    const groupMap = {};
    medicineInfoList.value.forEach((item) => {
      const groupId = item.groupId; // 确保属性名一致
      if (groupId != null) {
        // 过滤掉null/undefined
        groupMap[groupId] = groupMap[groupId] || [];
        groupMap[groupId].push(item);
      }
    });

    // 处理每个分组
    Object.values(groupMap).forEach((group) => {
      const count = group.length;
      if (count === 2) {
        group[0].flag = '┓';
        group[1].flag = '┛';
      } else if (count > 2) {
        group[0].flag = '┓';
        group.slice(1, -1).forEach((item) => (item.flag = '┃')); // 中间元素
        group[group.length - 1].flag = '┛';
      }
    });

    // 处理没有分组的项
    medicineInfoList.value.forEach((item) => {
      if (item.groupId == null) {
        // 确保属性名一致
        item.flag = '';
      } else if (!item.flag) {
        // 确保所有项都有flag属性
        item.flag = '';
      }
    });

    diagnoses.value = medicineInfoList.value.map((item) => item.诊断 || '无').join(', ');
  });
}

function submitMedicine(saveList) {
  prepareMedicion(saveList).then((res) => {
    if (res.code == 200) {
      updateMedicion(saveList).then((response) => {
        proxy.$modal.msgSuccess('发药成功');
        listWesternmedicine(currentRow.value).then((response) => {
          medicineInfoList.value = Array.isArray(response.data.prescriptionMedicineInfoDtoList)
            ? response.data.prescriptionMedicineInfoDtoList
            : [response.data.prescriptionMedicineInfoDtoList];
          // 统计每个 prescriptionNo 的行数
          const groupCounts = countGroupRows(medicineInfoList.value);
          // 设置每行的标记
          markers.value = getRowMarkers(groupCounts, medicineInfoList.value);
        });
      });
    }
  });
}
const throttledGetList = debounce(handelTraceNo, 500);

let inputValue = '';
function handelTraceNo(value) {
  traceNoList.value.push(value);
  traceNo.value = traceNo.value + '[' + traceNoList.value.length + ']' + '  ' + value + '\n';
  traceNoTemp.value = '';
  // let saveValue = value.substring(inputValue.length + 5, value.length);
  // inputValue = value;
  // console.log(value);
  // console.log(saveValue);
  // traceNoList.value.push(saveValue);
  // traceNo.value = value + '[' + (traceNoList.value.length + 1) + ']' + '  ';
}

function handleBatch(row) {
  let saveList = [];
  if (row) {
    saveList = medicineInfoList.value
      .filter((item) => {
        return item.prescriptionNo === row.prescriptionNo;
      })
      .map((item) => {
        return {
          requestId: item.requestId,
          dispenseId: item.dispenseId,
          traceNo: item.traceNo,
          prescriptionNo: item.prescriptionNo,
          preparerId: preparerDoctor.value,
        };
      });
  } else {
    if (tableRef.value.getSelectionRows().length > 0) {
      saveList = tableRef.value.getSelectionRows().map((item) => {
        return {
          requestId: item.requestId,
          dispenseId: item.dispenseId,
          traceNo: item.traceNo,
          prescriptionNo: item.prescriptionNo,
          preparerId: preparerDoctor.value,
        };
      });
    }
  }
  console.log(saveList);

  submitMedicine(saveList);
}

function backMedicine(row) {
  showDialog.value = true;
  selectedPrescriptionNo.value = row.prescriptionNo;
  console.log('作废原因:', selectedPrescriptionNo.value, row.prescriptionNo);
}

function handleConfirm() {
  if (!notPerformedReasonEnum.value) {
    ElMessage.error('请选择作废原因');
    return;
  }
  backMedicion(selectedPrescriptionNo.value, notPerformedReasonEnum.value).then((response) => {
    proxy.$modal.msgSuccess('作废成功');
  });
  listWesternmedicine(currentRow.value).then((response) => {
    console.log('1212*******12', response);
    medicineInfoList.value = Array.isArray(response.data.prescriptionMedicineInfoDtoList)
      ? response.data.prescriptionMedicineInfoDtoList
      : [response.data.prescriptionMedicineInfoDtoList];
    // medicineInfoList.value = response.data;
    // 统计每个 prescriptionNo 的行数
    const groupCounts = countGroupRows(medicineInfoList.value);
    // 设置每行的标记
    markers.value = getRowMarkers(groupCounts, medicineInfoList.value);
  });
  showDialog.value = false;
  notPerformedReasonEnum.value = ''; // 清空选择
}

function handleCancel() {
  showDialog.value = false;
  notPerformedReasonEnum.value = ''; // 清空选择
}

// getList();
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
  display: flex;
}

.left {
  width: 28%;

  .form {
    width: 100%;
    display: flex;
    justify-content: space-between;
  }
}

.right {
  margin-left: 2%;
  width: 70%;
}

:deep(.el-table tbody tr:hover > td) {
  background-color: inherit !important;
}

.el-form--inline .el-form-item {
  margin-right: 0px;
}
::v-deep.el-textarea .el-textarea__inner {
  resize: none !important;
}
</style>
