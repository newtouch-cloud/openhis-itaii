<template>
  <div class="business-temperature" style="display: flex; justify-content: space-evenly">
    <el-card style="width: 35%">
      <template #header>
        <span style="vertical-align: middle">患者列表</span>
      </template>
      <div style="width: 100%">
        <el-input
          v-model="queryParams.searchKey"
          placeholder="请输入患者名/病历号"
          clearable
          style="width: 48%; margin-bottom: 10px; margin-right: 10px"
          @keyup.enter="getPatientListInfo"
        >
        </el-input>
        <el-button type="primary" style="margin-bottom: 10px" @click="getPatientListInfo">
          搜索
        </el-button>
        <el-table
          ref="patientListRef"
          height="672"
          :data="patientList"
          row-key="encounterId"
          @row-click="viewPatient"
          highlight-current-row
        >
          <el-table-column prop="bedLocationId_dictText" label="床号" min-width="50" />
          <el-table-column label="病历号" align="center" prop="patientBusNo" />
          <el-table-column label="姓名" align="center" prop="patientName" />
          <el-table-column label="性别" align="center" prop="genderEnum_enumText" />
          <el-table-column label="年龄" align="center" prop="ageString" />
        </el-table>
        <pagination
          v-show="total > 0"
          :total="total"
          v-model:page="queryParams.pageNo"
          v-model:limit="queryParams.pageSize"
          @pagination="getPatientListInfo"
          style="margin-bottom: 20px"
        />
      </div>
    </el-card>
    <el-card style="width: 60%">
      <template #header>
        <span style="vertical-align: middle">护理记录</span>
      </template>
      <div style="width: 100%">
        <div style="width: 100%">
          <el-date-picker
            v-model="recordingTime"
            type="daterange"
            range-separator="~"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            placement="bottom"
            value-format="YYYY-MM-DD"
            style="width: 64%; margin-bottom: 10px; margin-right: 10px"
          />
          <el-button type="primary" style="margin-bottom: 10px" @click="viewPatient(patientData)">
            搜索
          </el-button>
        </div>
        <div style="margin-bottom: 10px">
          <el-button size="default" type="primary" @click="openAddTprDialog">新增</el-button>
          <el-button type="danger" plain @click="deletePrescription()" :disabled="false">
            删除
          </el-button>
        </div>
        <el-table
          ref="patientListRef"
          height="680"
          :data="recordList"
          row-key="recordId"
          highlight-current-row
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="content.recordingTime" label="记录时间" width="180" />
          <el-table-column prop="content.tw" label="体温" width="80" />
          <el-table-column prop="content.systolicBloodPressure" label="收缩压" min-width="80" />
          <el-table-column prop="content.diastolicBloodPressure" label="舒张压" width="80" />
          <el-table-column prop="content.xl" label="心率" min-width="120" />
          <el-table-column prop="content.mb" label="脉搏" width="80" />
          <el-table-column prop="content.hx" label="呼吸" min-width="120" />
          <el-table-column prop="content.xy" label="血氧" width="80" />
          <el-table-column prop="content.bqgcOther" label="病情观察与护理记录" min-width="80" />
          <el-table-column label="操作" min-width="150" fixed="right">
            <template #default="scope">
              <el-button link type="primary" icon="Edit" @click="handleEdit(scope.row)"
                >编辑</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="recordsTotal > 0"
          :total="recordsTotal"
          v-model:page="recordQueryParams.pageNo"
          v-model:limit="recordQueryParams.pageSize"
          @pagination="viewPatient(patientData)"
          style="margin-bottom: 20px"
        />
      </div>
    </el-card>
    <add-nursing-record-dialog
      ref="addNursingRecordDialogRef"
      :open="openAddTpr"
      :patientId="patientId"
      :patientInfo="patientData"
      :editData="editData"
      :title="title"
      @close="closePatientDetialDialog"
    />
    <!-- </div> -->
  </div>
</template>

<script setup>
import addNursingRecordDialog from './components/addNursingRecordDialog.vue';
import { listPatient, getNursingPatientPage, delRecord } from './components/api';
import useUserStore from '@/store/modules/user';
const userStore = useUserStore();
// 响应式数据
const patientId = ref('');
const data = ref(undefined);
const openAddTpr = ref(false);
const total = ref(0);
const recordsTotal = ref(0);
const recordList = ref([]);
const contextJson = ref(undefined);
const editData = ref({});
const { proxy } = getCurrentInstance();
const print = ref(null);
const queryParams = ref({
  pageNo: 1,
  pageSize: 10,
  searchKey: undefined,
  orgId: userStore.orgId,
});

const recordQueryParams = ref({
  pageNo: 1,
  pageSize: 10,
});

const patientData = ref({});
const recordingTime = ref([
  // formatDateStr(new Date(), 'YYYY-MM-DD'),
  // formatDateStr(new Date(), 'YYYY-MM-DD'),
]);
const title = ref('');
const patientList = ref([]);
const addNursingRecordDialogRef = ref(null);
const selectedData = ref([]);

const ids = ref([]); // 存储选择的药品信息行数据
const single = ref(true);
const multiple = ref(true);

getPatientListInfo();

/**
 * 患者列表
 */
function getPatientListInfo() {
  listPatient(queryParams.value).then((res) => {
    console.log(userStore, 'userStore', res);
    patientList.value = res.data.records;
    total.value = res.data.total;
  });
}

/**
 * 查看患者护理记录单
 *
 * @param row 患者数据对象
 */
function viewPatient(row) {
  patientData.value = row;
  patientData.value.orgId = userStore.orgId;

  // recordQueryParams
  recordQueryParams.value.encounterId = row.encounterId;
  if (recordingTime.value.length > 0) {
    recordQueryParams.value.recordingTimeSTime = recordingTime.value[0] + ' 00:00:00';
    recordQueryParams.value.recordingTimeETime = recordingTime.value[1] + ' 23:59:59';
  } else {
    recordQueryParams.value.recordingTimeSTime = undefined;
    recordQueryParams.value.recordingTimeETime = undefined;
  }

  recordList.value = [];
  getNursingPatientPage(recordQueryParams.value).then((response) => {
    recordList.value = [];
    // recordList.value = res.data.records;
    if (response.data && response.data.records.length > 0) {
      for (let i = 0; i < response.data.records.length; i++) {
        console.log(
          typeof response.data.records[i].contextJson,
          'typeofcontextJson',
          response.data.records[i]
        );
        if (typeof response.data.records[i].contextJson === 'string') {
          console.log('Parsing string...');
          try {
            contextJson.value = JSON.parse(response.data.records[i].contextJson);
          } catch (error) {
            console.error('Parsing error:', error);
          }
        } else {
          contextJson.value = response.data.records[i].contextJson; // 如果已经是对象
        }

        contextJson.value.recordTime = contextJson.value.recordTime
          ? moment(contextJson.value.recordTime).format('YYYY-MM-DD HH:mm:ss')
          : '';
        const tableItems = {
          content: contextJson.value,
          recordId: response.data.records[i].recordId,
          patientId: response.data.records[i].patientId,
          encounterId: response.data.records[i].encounterId,
          recordingTime: response.data.records[i].recordingTime,
        };
        recordList.value.push(tableItems);
      }
    } else {
      recordList.value = [];
    }
    console.log(recordList.value, 'recordList');
    recordsTotal.value = response.data.total;
  });
  console.log('查看患者体温单', row);
  proxy.$refs.addNursingRecordDialogRef.show(row);
}

/**
 * 打开体征录入
 */
function openAddTprDialog() {
  if (!patientData.value.patientId) {
    proxy.$modal.msgError('请先选择患者！');
    return;
  }
  title.value = '新增';
  openAddTpr.value = true;

  nextTick(() => {
    proxy.$refs['addNursingRecordDialogRef'].show();
  });
  console.log(openAddTpr.value, '打开体征录入对话框');
}

/**
 * 关闭新增体征弹窗
 */
function closePatientDetialDialog(str) {
  openAddTpr.value = false;
  viewPatient(patientData.value);
  if (str === 'success') {
    proxy.$modal.msgSuccess('操作成功！');
  }
}

/** 选择条数  */
function handleSelectionChange(selection) {
  console.log(selection, '选择条数');
  selectedData.value = selection.map((item) => ({ ...item })); // 存储选择的行数据
  ids.value = selection.map((item) => item.recordId);
  console.log(ids.value, '选择条数');
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/**
 * 编辑护理记录
 */
function handleEdit(row) {
  title.value = '编辑';
  openAddTpr.value = true;
  editData.value = row;
  nextTick(() => {
    proxy.$refs['addNursingRecordDialogRef'].show();
  });
  console.log(openAddTpr.value, '打开体征录入对话框');
}

/**
 * 删除记录单
 *
 * @param index - 要删除的处方在列表中的索引
 */
function deletePrescription(index) {
  console.log(selectedData.value, '删除记录单');
  if (selectedData.value.length == 0) {
    proxy.$modal.msgWarning('请选择要删除的数据信息！');
    return;
  }
  console.log('deletePrescription删除', data);
  proxy.$modal
    .confirm('是否确认删除以上数据！')
    .then(function () {
      return delRecord(selectedData.value);
    })
    .then(() => {
      viewPatient(patientData.value);
      proxy.$modal.msgSuccess('删除成功');
    })
    .catch(() => {});
}
</script>

<style lang="scss" scoped>
.business-temperature {
  grid-template-columns: 100%;
  background-color: white;
}

::v-deep .business-temperature .el-icon-arrow-down {
  font-size: 12px;
}
.business1 {
  background: white;
  padding: 10px 16px;
  border-radius: 6px;
  height: 40%;
  justify-content: center;
}
.layui-form-label {
  line-height: 35px;
  text-align: right;
  padding-right: 10px;
}
.layui-input-inline {
  display: inline-block;
}
.business-temperature-sheet {
  display: grid;
  grid-template-columns: 59px 1px 780px;
}
::v-deep .business-temperature-sheet .el-icon-arrow-down {
  font-size: 12px;
}
</style>