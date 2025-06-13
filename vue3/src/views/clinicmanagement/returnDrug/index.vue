<template>
  <div class="container">
    <!-- 左侧患者列表 -->
    <el-card class="patient-list">
      <template #header>
        <div class="card-header">
          <span>患者列表</span>
        </div>
      </template>
      <div style="width: 100%">
        <el-input
          v-model="queryParams.searchKey"
          placeholder="搜索患者"
          style="width: 48%; margin-bottom: 10px; margin-right: 15px"
          @keyup.enter="getEncounterList"
        >
          <template #append>
            <el-button icon="Search" @click="getEncounterList" />
          </template>
        </el-input>
        <el-select
          v-model="queryParams.refundEnum"
          style="width: 48%; margin-bottom: 10px"
          placeholder="收费状态"
          @change="getEncounterList"
        >
          <el-option
            v-for="item in statusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 85%; margin-bottom: 10px"
          value-format="YYYY-MM-DD"
        />
        <el-button
          type="primary"
          @click="getEncounterList"
          style="margin-bottom: 10px; margin-left: 18px"
        >
          搜索
        </el-button>
      </div>
      <el-table
        :data="encounterList"
        border
        style="width: 100%"
        height="calc(100vh - 300px)"
        highlight-current-row
        @cell-click="handleGetReturnDrugList"
      >
        <el-table-column
          prop="patientName"
          align="center"
          label="姓名"
          width="130"
          show-overflow-tooltip
        />
        <el-table-column
          prop="genderEnum_enumText"
          align="center"
          label="性别"
          show-overflow-tooltip
        />
        <el-table-column align="center" width="140" label="就诊日期" show-overflow-tooltip>
          <template #default="scope">
            {{
              scope.row.receptionTime ? formatDateStr(scope.row.receptionTime, 'YYYY-MM-DD') : '-'
            }}
          </template>
        </el-table-column>
        <!-- <el-table-column label="状态" align="center" prop="refundEnum_enumText" /> -->
      </el-table>
    </el-card>

    <!-- 右侧退药列表 -->
    <el-card class="refund-list">
      <template #header>
        <div class="card-header">
          <span>退药单</span>
          <!-- <div v-if="selectedPatient">
            <span class="patient-info">{{ selectedPatient.name }}</span>
            <el-tag type="info">{{ selectedPatient.visitNo }}</el-tag>
          </div> -->
        </div>
      </template>

      <el-button
        type="primary"
        :disabled="!selectedMedicines.length"
        @click="handleReturnDrug(undefined)"
        style="margin-bottom: 10px"
      >
        确认退药
      </el-button>
      <el-button type="primary" @click="handleScan()" style="margin-bottom: 10px"> 扫码 </el-button>
      <el-table
        ref="returnDrugRef"
        :data="returDrugList"
        style="width: 100%"
        height="calc(100vh - 300px)"
        border
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="itemName" label="药品名称" show-overflow-tooltip align="center" />
        <el-table-column
          prop="totalPrice"
          label="总价"
          width="100"
          align="right"
          header-align="center"
        >
          <template #default="scope">
            {{ scope.row.totalPrice ? scope.row.totalPrice.toFixed(2) + ' 元' : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="lotNumber" label="批号" width="180" align="center" />
        <el-table-column prop="traceNo" label="追溯码" width="180" align="center">
          <template #default="scope">
            <el-input v-model="scope.row.traceNo" placeholder="请输入追溯码" />
          </template>
        </el-table-column>
        <el-table-column prop="reqStatus_enumText" label="退药状态" width="100" align="center">
          <template #default="scope">
            {{
              scope.row.reqStatus_enumText == null
                ? scope.row.refundEnum_enumText
                : scope.row.reqStatus_enumText
            }}
          </template>
        </el-table-column>
        <el-table-column prop="waitingQuantity" label="退药数量" width="100" align="center">
          <template #default="scope">
            <span>{{
              scope.row.quantity
                ? Math.abs(scope.row.quantity)
                : '0' + ' ' + scope.row.unitCode_dictText
            }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="doctorName" label="开单医生" align="center" width="180" />
        <el-table-column label="操作" width="100" align="center" fixed="right">
          <template #default="scope">
            <el-popconfirm
              width="150"
              hide-after="10"
              title="操作确认"
              placement="top-start"
              @confirm="handleReturnDrug(scope.row)"
            >
              <template #reference>
                <el-button type="primary" link :disabled="scope.row.reqStatus != 11">
                  退药
                </el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- 底部操作栏 -->
      <div class="footer">
        <div class="statistics">
          <span>已选 {{ selectedMedicines.length }} 种药品</span>
          <span class="total">合计金额：¥ {{ totalAmount.toFixed(2) }}</span>
        </div>
      </div>
    </el-card>
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
          >
            清除
          </el-button>
        </div>
        <el-input
          ref="inputRef"
          v-model="traceNo"
          type="textarea"
          :rows="15"
          disabled
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

<script setup name="ReturnDrug">
import { getCurrentInstance } from 'vue';
import { getList, getReturnDrugList, returnDrug, init, itemTraceNo } from './components/api';
import { formatDateStr } from '@/utils/index';
import { debounce } from 'lodash-es';

const queryParams = ref({
  pageSize: 50,
  pageNum: 1,
  refundEnum: 11,
});
const openTraceNo = ref(false);
const traceNoList = ref([]);
const traceNo = ref('');
const encounterList = ref([]);
const encounterId = ref();
const returDrugList = ref([]);
const selectedMedicines = ref([]);
const statusOptions = ref([]);
const dateRange = ref([]);
const traceNoTemp = ref('');
const traceNoTempRef = ref();
const totalAmount = ref(0);
const { proxy } = getCurrentInstance();
getEncounterList();
function getEncounterList() {
  queryParams.value.receptionTimeSTime = dateRange.value[0] + ' 00:00:00';
  queryParams.value.receptionTimeETime = dateRange.value[1] + ' 23:59:59';
  getList(queryParams.value).then((res) => {
    encounterList.value = res.data.records;
  });
}
initOptions();
function initOptions() {
  init().then((res) => {
    statusOptions.value = res.data.refundStatusOptions;
  });
}

function handleGetReturnDrugList(row) {
  encounterId.value = row.encounterId;
  getReturnDrugList({
    encounterId: row.encounterId,
    refundStatus: queryParams.value.refundEnum,
  }).then((res) => {
    returDrugList.value = res.data;
  });
}
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
      returDrugList.value.forEach((item, index) => {
        if (res.data[item.itemId] && res.data[item.itemId].split(',') > item.quantity) {
          proxy.$modal.msgWarning('操作失败');
          return;
        }
        returDrugList.value[index].traceNo = res.data[item.itemId];
      });
      openTraceNo.value = false;
      traceNoList.value = [];
      // proxy.$modal.msgSuccess('退药成功');
      // getReturnDrugList(encounterId.value);
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

function handleClear() {
  traceNo.value = '';
  traceNoList.value = [];
}
function cancel() {
  openTraceNo.value = false;
  traceNoList.value = [];
  traceNo.value = '';
}
/**
 * 退药
 */
function handleReturnDrug(row) {
  console.log(row);
  let saveList = [];
  if (row) {
    saveList = [
      {
        requestId: row.requestId,
        dispenseId: row.dispenseId,
        tableName: row.serviceTable,
        traceNo: row.traceNo,
      },
    ];
  } else {
    saveList = proxy.$refs.returnDrugRef.getSelectionRows().map((item) => {
      return {
        requestId: item.requestId,
        dispenseId: item.dispenseId,
        tableName: item.serviceTable,
        traceNo: item.traceNo,
      };
    });
    console.log(saveList);
  }
  returnDrug(saveList).then((res) => {
    if (res.code === 200) {
      proxy.$modal.msgSuccess('退药成功');
      getReturnDrugList(encounterId.value);
    }
  });
}

function handleSelectionChange(selectRows) {
  selectedMedicines.value = selectRows;
  totalAmount.value = selectRows.reduce((accumulator, currentRow) => {
    return accumulator + (currentRow.totalPrice || 0);
  }, 0);
}
</script>

<style lang="scss" scoped>
.container {
  display: flex;
  height: 90vh;
  gap: 16px;
  padding: 16px;
  background: #f0f2f5;
}

.patient-list {
  width: 600px;
  flex-shrink: 0;
}

.refund-list {
  flex: 1;
}

.patient-item {
  padding: 12px;
  margin-bottom: 8px;
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.3s;

  &:hover {
    background-color: #f5f7fa;
  }

  &.active {
    background-color: #ecf5ff;
    border-left: 4px solid #409eff;
  }
}

.patient-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;

  .name {
    font-weight: 500;
  }

  .visit-no {
    color: #666;
    font-size: 12px;
  }
}

.visit-time {
  color: #999;
  font-size: 12px;
}

.footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-top: 1px solid #ebeef5;

  .total {
    margin-left: 20px;
    color: #f56c6c;
    font-weight: 500;
  }
}

::v-deep.el-textarea .el-textarea__inner {
  resize: none !important;
}
</style>