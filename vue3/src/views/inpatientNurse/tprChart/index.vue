<template>
  <div class="business-temperature" style="display: flex; justify-content: space-evenly">
    <!--<div class="business1">
      <PatientInfo @send-data="handleData">
        <el-button size="small" type="primary" style="margin-left: 10px" @click="seleteData"
          >查询</el-button
        >
        <el-button size="small" type="primary" style="margin-left: 10px" @click="printJS"
          >打印</el-button
        >
        <label class="layui-form-label" style="margin-left: 10px">日期</label>
        <div class="layui-input-inline" style="width: 180px">
          <el-date-picker
            v-model="recordingDate"
            style="width: 180px"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择日期"
            size="small"
          />
        </div>
      </PatientInfo> 
    </div>-->
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
        />
        <el-button type="primary" style="margin-bottom: 10px" @click="getPatientListInfo">
          搜索
        </el-button>
        <el-table
          ref="patientListRef"
          height="620"
          :data="patientList"
          row-key="encounterId"
          @row-click="viewPatient"
          highlight-current-row
        >
          <el-table-column prop="bedLocationId_dictText" label="床号" min-width="50" />
          <el-table-column label="病历号" align="center" prop="encounterId" />
          <el-table-column label="姓名" align="center" prop="patientName" />
          <el-table-column label="性别" align="center" prop="genderEnum_enumText" />
          <el-table-column label="年龄" align="center" prop="ageString" />
          <!-- <el-table-column label="时间" align="center" prop="receptionTime" width="160">
            <template #default="scope">
              {{ formatDate(scope.row.receptionTime) }}
            </template>
          </el-table-column> -->
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
      <div>
        <el-button size="default" type="primary" @click="openAddTprDialog"
          >新增</el-button
        >
      </div>
      <div class="business-temperature-sheet" style="width: 60%">
        <div>
          <el-tooltip content="首页" placement="right-end">
            <el-button
              style="margin-left: 12px; margin-top: 400px"
              icon="Upload"
              size="default"
              type="primary"
              @click="toFirst"
            />
          </el-tooltip>
          <el-tooltip content="上一页" placement="right-end">
            <el-button
              style="margin-left: 12px; margin-top: 15px"
              icon="ArrowUpBold"
              size="default"
              type="primary"
              @click="lastWeek"
            />
          </el-tooltip>
          <el-tooltip content="下一页" placement="right-end">
            <el-button
              style="margin-left: 12px; margin-top: 15px"
              icon="ArrowDown"
              size="default"
              type="primary"
              @click="nextWeek"
            />
          </el-tooltip>
          <el-tooltip content="尾页" placement="right-end">
            <el-button
              style="margin-left: 12px; margin-top: 15px"
              icon="Download"
              size="default"
              type="primary"
              @click="toEnd"
            />
          </el-tooltip>
          <el-tooltip content="打印本页" placement="right-end">
            <el-button
              style="margin-left: 12px; margin-top: 15px"
              icon="Printer"
              size="default"
              type="primary"
              @click="printPage"
            />
          </el-tooltip>
        </div>
        <div id="my_dataviz" ref="printRef" style="width: 780px; background-color: white" />
      </div>
    </el-card>
    <div v-show="false">
      <temperatureSheet ref="refTemp" />
    </div>
    <add-tpr-dialog
      ref="addTprDialogRef"
      :open="openAddTpr"
      :patientId="patientId"
      :patientInfo="patientData"
      @close="closePatientDetialDialog"
    />
    <!-- </div> -->
    <iframe id="printIframe" ref="printIframe" style="display:none;"></iframe>
  </div>
</template>

<script setup>
// import PatientInfo from '@/components/PatientInfo'
import html2pdf from 'html2pdf.js';
import cloneDeep from 'lodash.clonedeep';
import { init } from '@/action/nurseStation/temperatureSheet/line.js';
import temperatureSheet from '../../../components/Auto/printBills/temperatureSheet';
import addTprDialog from './components/addTprDialog.vue';
// import printJS from 'print-js';
import { formatDateStr } from '@/utils';
import moment from 'moment';
import { listPatient, getVitalSignsInfo } from './components/api';
import useUserStore from '@/store/modules/user';
// import { getSignsCharts } from '@/api/signsManagement'
// import { date } from 'jszip/lib/defaults'
const userStore = useUserStore();
// 响应式数据
const printRef = ref(null);
const printIframe = ref(null);
const refTemp = ref(null);
const recordingDate = ref(undefined);
const week = ref(0);
const patientId = ref('');
const visitId = ref('');
const admissionDate = ref(undefined);
const inputData = ref({});
const openAddTpr = ref(false);
const { proxy } = getCurrentInstance();
const print = ref(null);
const total = ref(0);
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  searchKey: undefined,
  orgId: userStore.orgId,
});

const data1 = reactive({        
  patientInfo: {},
  grParamBOS: {
    age: '',
    birth: '',
    cwh: '',
    hosNum: '',
    inDate: '',
    inDiagName: '',
    name: '',
    deptName: '',
    operaDays: '',
    sex: '',
    weekNo: '',
    beginDate: '',
    hospDays: '',
    total: '',
    hospDate: '',
    operaDate: '',
    outdate: ''
  },
  rows: [],
  types: []
});
const patientData = ref({});
const integerPoints = [2, 6, 10, 14, 18, 22];
const closestPoint = ref(null);
const receptionTime = ref([
  formatDateStr(new Date(), 'YYYY-MM-DD'),
  formatDateStr(new Date(), 'YYYY-MM-DD'),
]);
const patientList = ref([]);
const chargeLoading = ref(false);
const encounterId = ref('');
const addTprDialogRef = ref(null);
const patientInfo = ref({});

// /**
//  * 查看患者
//  */
// function viewPatient(row) {
//   console.log('接收子组件传来的数据', patientInfo);
//   patientId.value = patientInfo.patientId; // 接收子组件传来的数据
//   visitId.value = String(patientInfo.visitId); // 接收子组件传来的数据

//   // // 入科时间存在的场合使用入科时间
//   // if (patientInfo.admWardDateTime) {
//   //   admissionDate = patientInfo.admWardDateTime; // 接收子组件传来的数据
//   // } else {
//   //   admissionDate = patientInfo.admissionDate; // 接收子组件传来的数据
//   // }

//   // data1.patientInfo = patientInfo;
//   // data1.grParamBOS.patientId = patientInfo.patientId;
//   // data1.grParamBOS.age = patientInfo.age;
//   // data1.grParamBOS.birth = null;
//   // data1.grParamBOS.cwh = patientInfo.bedLabel;
//   // data1.grParamBOS.hosNum = patientInfo.hosNum;

//   // // 入科时间存在的场合使用入科时间
//   // if (patientInfo.admWardDateTime) {
//   //   data1.grParamBOS.inDate = patientInfo.admWardDateTime;
//   // } else {
//   //   data1.grParamBOS.inDate = patientInfo.admissionDate;
//   // }

//   // console.log('patientInfo.admissionDate', patientInfo.admissionDate, data1.grParamBOS.inDate);
//   // data1.grParamBOS.inDiagName = patientInfo.diagnosis;
//   // data1.grParamBOS.name = patientInfo.name;
//   // data1.grParamBOS.deptName = patientInfo.deptName;
//   data1.grParamBOS.operaDays = null;
//   // data1.grParamBOS.sex = patientInfo.sex;
//   data1.grParamBOS.weekNo = null;
//   data1.grParamBOS.beginDate = getCurrentDate();
//   data1.grParamBOS.hospDays = null;
//   data1.grParamBOS.total = null;
//   getSignsCharts();
// }
// init1(data1);
getPatientListInfo();
// init1(data1);
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
function init1(data) {
  console.log('体温单初始化', data);
  const inDate = data.grParamBOS.hospDate;
  const outdate = data.grParamBOS.outdate;
  week.value = Math.floor(dateDiff(inDate, outdate) / 7);
  setTemperatureComp(data);
}

/**
 * 点击患者列表行 获取患者体温单数据
 */
function viewPatient(row) {
  patientInfo.value = row;
  console.log('点击患者列表行 获取患者体温单数据', row);
  chargeLoading.value = true;
  encounterId.value = row.encounterId;
  patientId.value = row.patientId; // 接收子组件传来的数据
  // visitId.value = String(patientInfo.value.visitId); // 接收子组件传来的数据

  // 入科时间存在的场合使用入科时间
  admissionDate.value = row.admissionDate; // 接收子组件传来的数据

  data1.patientInfo = row;
  data1.grParamBOS.patientId = row.patientId;
  data1.grParamBOS.age = row.ageString;
  data1.grParamBOS.birth = row.birthDate;
  data1.grParamBOS.cwh = row.bedLocationId_dictText;
  data1.grParamBOS.hosNum = row.encounterId;

  // 入科时间存在的场合使用入科时间
  data1.grParamBOS.inDate = row.admissionDate;

  console.log(
    'patientInfo.admissionDate',
    patientInfo.value.admissionDate,
    data1.grParamBOS.inDate
  );
  // data1.grParamBOS.inDiagName = patientInfo.value.diagnosis;
  data1.grParamBOS.name = patientInfo.value.patientName;
  // data1.grParamBOS.deptName = patientInfo.value.deptName;
  data1.grParamBOS.operaDays = null;
  // data1.grParamBOS.sex = patientInfo.sex;
  data1.grParamBOS.weekNo = null;
  data1.grParamBOS.beginDate = getCurrentDate();
  data1.grParamBOS.hospDays = null;
  data1.grParamBOS.total = null;
  // getChargeList(row.encounterId).then((res) => {
  //   chargeList.value = res.data;
  //   setTimeout(() => {
  //     chargeLoading.value = false;
  //     chargeListRef.value.toggleAllSelection();
  //   }, 100);
  // });
  getSignsCharts();
}

function getSignsCharts() {
  const params = {
    patientId: patientId.value,
  };
  getVitalSignsInfo(params).then((response) => {
    console.log('体温单返回值', response);
    if (response.code === 200) {
      // data1.grParamBOS.hospDate = data1.grParamBOS.inDate.substring(0, 10)
      data1.grParamBOS.hospDate =
        response.data.hospDate && response.data.hospDate.length > 10
          ? response.data.hospDate.substring(0, 10)
          : response.data.hospDate;
      data1.grParamBOS.operaDate = response.data.operaDate;
      data1.grParamBOS.outdate = response.data.outdate;
      data1.rows = response.data.temperaturePulses.map((item) => ({
        rowBOS: item.chartsSmalls,
        weekNo: item.weekNo - 1,
      }));
      data1.types = response.data.others.map((item) => ({
        ...item, // 保留其他属性
        weekNo: item.weekNo - 1, // 将 weekNo 减 1
      }));
      // // 取入院特殊标记的值
      // const typeValue = '院,' + formatDateTo(response.data.hospDate).formattedTime;
      // const newRowBOS = {
      //   collectionMode: null,
      //   date: formatDateTo(response.data.hospDate).formattedDate,
      //   times: formatHourTimes(response.data.hospDate, response.data.temperaturePulses)
      //     .formattedTime,
      //   typeCode: '018',
      //   typeValue: typeValue,
      //   weekNo: 1,
      // };
      // let foundZero = false;
      // for (const item of data1.rows) {
      //   if (item.weekNo1 === 0) {
      //     item.rowBOS.push(newRowBOS);
      //     foundZero = true;
      //     break; // 找到后可以跳出循环
      //   }
      // }
      // if (!foundZero) {
      //   data1.rows.push({
      //     rowBOS: [newRowBOS],
      //     weekNo: 0,
      //   });
      // }
      console.log('体温单查询this.data1', data1);
      init1(data1);
    }
  });
}
function getPatientList() {}
// 体温单控件数据设置
function setTemperatureComp(data) {
  console.log(data,'体温单控件数据设置');
  if (data !== undefined) {
    inputData.value = data;
  }
  const inDate = inputData.value.grParamBOS.hospDate;
  const outdate = inputData.value.grParamBOS.outdate;
  const begin = moment(new Date(inDate))
    .add(week.value * 7, 'day')
    .format('YYYY-MM-DD HH:mm:ss');
  inputData.value.grParamBOS.weekNo = week.value;
  inputData.value.grParamBOS.beginDate = begin;
  inputData.value.grParamBOS.hospDays = week.value * 7;
  inputData.value.grParamBOS.total = Math.floor(dateDiff(inDate, outdate) / 7);
  init(sliceData(inputData.value));
}
function getCurrentDate() {
  // 获取当前日期
  const today = new Date();
  const year = today.getFullYear();
  const month = (today.getMonth() + 1).toString().padStart(2, '0'); // 月份从0开始，所以需要加1
  const day = today.getDate().toString().padStart(2, '0');

  return `${year}-${month}-${day}`; // 格式化为 YYYY-MM-DD
}
function seleteData() {
  const inDate = inputData.value.grParamBOS.hospDate;
  week.value = Math.floor(dateDiff(inDate, recordingDate.value) / 7);
  setTemperatureComp();
}
function lastWeek() {
  week.value--;
  if (week.value < 0) {
    week.value = 0;
  }
  setTemperatureComp();
}
function nextWeek() {
  week.value = Number(week.value) + 1;
  if (week.value > inputData.value.grParamBOS.total) {
    week.value = inputData.value.grParamBOS.total;
  }
  setTemperatureComp();
}
function toFirst() {
  week.value = 0;
  setTemperatureComp();
}
function toEnd() {
  week.value = inputData.value.grParamBOS.total;
  setTemperatureComp();
}
// 拆分当前周数据
function sliceData(data) {
  const rows = data.rows.filter((item) => item.weekNo === week.value);
  const types = data.types.filter((item) => item.weekNo === week.value);
  // const datas = JSON.parse(JSON.stringify(data));
  const datas = cloneDeep(data);
  datas.rows = rows;
  datas.types = types;
  console.log(datas,"666666666666666666")
  return datas;
}
// 计算时间差
function dateDiff(start, end) {
  let diffTime = start ? moment(new Date()).diff(moment(start.substring(0, 10))) / 1000 :start;
  if (end) {
    diffTime = moment(end.substring(0, 10)).diff(moment(start.substring(0, 10))) / 1000;
  }
  if (diffTime > 24 * 3600) {
    return Math.floor(diffTime / (24 * 3600));
  } else if (diffTime > 3600) {
    return '0';
  } else {
    return '0';
  }
}
// 打印体温单
function printTW() {
  // this.$print(this.$refs.print);
  printRef.value.focus();
  printRef.value.contentWindow.print();
  // this.$refs.refTemp.printPage();
}
// 打印体温单
function printPage() {
  const element = printRef.value;

  if (!element) {
    console.error('未找到可打印的内容');
    return;
  }

  // 创建一个克隆元素用于打印，避免修改原 DOM
  const clone = element.cloneNode(true);

  // 设置宽度为 A4（780px ≈ 210mm），高度自适应
  clone.style.transform = 'scale(0.7)';
  clone.style.transformOrigin = 'top left';
  clone.style.width = 'calc(210mm * 1.11)';
  clone.style.height = 'calc(297mm * 1.11)';
  clone.style.marginLeft = '50px'; 

  // 插入到 body 中以便 html2pdf 渲染
  document.body.appendChild(clone);

  // 设置 html2pdf 配置
  const opt = {
    margin:       0,
    filename:     '体温单.pdf',
    image:        { type: 'jpeg', quality: 1 },
    html2canvas:  { scale: 2, useCORS: true }, // 启用跨域资源支持
    jsPDF:        { unit: 'mm', format: 'a4', orientation: 'portrait' },
    pagebreak:    { mode: ['avoid-all'] },
    onclone: (clonedDoc) => {
      const chart = clonedDoc.getElementById(clone.id);
      if (chart) {
        chart.style.width = '210mm'; // 强制 A4 宽度
        chart.style.margin = '0 auto';
      }
    }
  };

  // 导出为 PDF 并打印
  html2pdf().from(clone).set(opt).toPdf().get('pdf').then(function (pdf) {
    pdf.autoPrint(); // 自动打印
    window.open(pdf.output('bloburl'), '_blank'); // 在新窗口打开 PDF（以便用户确认）
  }).finally(() => {
    document.body.removeChild(clone); // 清理临时元素
  });
}
// 转化时间
function formatDateTo(dateStr) {
  const [formattedDate, timePartWithSeconds] = dateStr ? dateStr.split('T'): ['', ''];
  const [formattedHour, minutePart] = timePartWithSeconds.split(':');
  const formattedTime = `${formattedHour}:${minutePart}`;

  // 返回格式化的日期字符串
  return {
    formattedDate: formattedDate,
    formattedTime: formattedTime,
    formattedHour: formattedHour,
  };
}
// 转化时间
function formatHourTimes(dateStr, temperaturePulses) {
  const formattedTime = formatDateTo(dateStr).formattedTime;
  const formattedDate = formatDateTo(dateStr).formattedDate;
  let times = '';
  if (formattedTime >= '00:00' && formattedTime < '04:00') {
    times = '02:00:00';
  } else if (formattedTime >= '04:00' && formattedTime < '08:00') {
    times = '06:00:00';
  } else if (formattedTime >= '08:00' && formattedTime < '12:00') {
    times = '10:00:00';
  } else if (formattedTime >= '12:00' && formattedTime < '16:00') {
    times = '14:00:00';
  } else if (formattedTime >= '16:00' && formattedTime < '20:00') {
    times = '18:00:00';
  } else if (formattedTime >= '20:00' && formattedTime <= '23:59') {
    times = '22:00:00';
  }
  const filteredData = temperaturePulses.filter((item) =>
    item.chartsSmalls.some(
      (chart) =>
        chart.date === formattedDate &&
        chart.times === times &&
        (chart.typeCode === '9507' ||
          chart.typeCode === '9505' ||
          chart.typeCode === '9502' ||
          chart.typeCode === '9503')
    )
  );
  if (filteredData.length > 0) {
    if (formattedTime >= '00:00' && formattedTime < '04:00') {
      if (filteredData.length === 1) {
        times = '06:00:00';
      } else if (filteredData.length === 2) {
        times = '10:00:00';
      } else if (filteredData.length === 3) {
        times = '14:00:00';
      } else {
        times = '02:00:00';
      }
    } else if (formattedTime >= '04:00' && formattedTime < '08:00') {
      times = '02:00:00';
    } else if (formattedTime >= '08:00' && formattedTime < '12:00') {
      times = '06:00:00';
    } else if (formattedTime >= '12:00' && formattedTime < '16:00') {
      times = '10:00:00';
    } else if (formattedTime >= '16:00' && formattedTime < '20:00') {
      times = '18:00:00';
    } else {
      times = '18:00:00';
    }
  }
  // 返回格式化的日期字符串
  return {
    formattedTime: times,
  };
}

/**
 * 打开体征录入
 */
function openAddTprDialog() {
  patientData.value = patientInfo.value
  openAddTpr.value = true;
  // patientId.value = row.id;
  nextTick(() => {
    proxy.$refs['addTprDialogRef'].getPatientDetial();
  });
  console.log(openAddTpr.value, '打开体征录入对话框');
}

/**
 * 关闭新增体征弹窗
 */
function closePatientDetialDialog(str) {
  openAddTpr.value = false;
}
</script>

<style lang="scss" scoped>
.business-temperature {
  // display: grid;
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
#chart {
  max-height: 270mm;
  overflow: hidden;
}
</style>