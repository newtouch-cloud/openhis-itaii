<template>
    <div class="app-container-infusion">
		<div class="left">
			<el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
           		<el-form-item label="医嘱执行时间">
            		<!-- <el-date-picker v-model="dateRange"  value-format="YYYY-MM-DD" type="daterange" range-separator="-" 
            		start-placeholder="开始日期" end-placeholder="结束日期" style="width: auto;"></el-date-picker> -->

					<el-date-picker v-model="dateRange" type="datetimerange" start-placeholder="开始日期" end-placeholder="结束日期"
					  style="width: auto;"  value-format="YYYY-MM-DD HH:mm:ss"/>
		   		</el-form-item>
		   		<el-form-item label="" prop="searchKey">
		      		<el-input v-model="queryParams.searchKey"  placeholder="门诊号/病人/ID" clearable style="width: 180px"
		         		@keyup.enter="handleQuery" />
		   		</el-form-item>
				
		   		<el-form-item>
		      		<el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
		      		<el-button icon="Refresh" @click="resetQuery">重置</el-button>
		   		</el-form-item>
			</el-form>

			<el-table :data="patientList" border style="width: 100%" highlight-current-row @current-change="handleCurrentChange" >
				<el-table-column prop="prescriptionNo" label="处方号" width="150" />
				<el-table-column prop="patientName" label="姓名" width="100" />
				<el-table-column prop="genderEnum_enumText" label="性别" width="80" /> 
				<el-table-column prop="ageString" label="年龄" width="80" />
				<el-table-column prop="status" label="身份证号" width="140" />
		  </el-table>
		  <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNo" 
			v-model:limit="queryParams.pageSize" @pagination="getList" />
		</div>

		<div class="right">
			<el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
				<el-form-item label="执行时间">
					<el-date-picker v-model="dateRangeRight" type="datetimerange" start-placeholder="开始日期" end-placeholder="结束日期"
					  style="width: auto;"  value-format="YYYY-MM-DD HH:mm:ss"/>
		   		</el-form-item>
		   		<el-form-item>
		      		<el-button type="primary" icon="Search" @click="handleQueryRight" style="margin-left: 10px;">搜索</el-button>
		      		<el-button icon="Refresh" @click="resetQueryRight">重置</el-button>
			 	 	<el-button  type="primary" icon="SuccessFilled" @click="handleSubmit">确认执行</el-button>
			 	 	<!-- <el-button  type="primary" icon="SuccessFilled" @click="handleSubmitCanel">取消执行</el-button> -->
			 	 	<el-button  type="primary" plain icon="Printer" @click="resetQuery">打印患者卡</el-button>
			 	 	<el-button  type="primary" plain icon="Printer" @click="resetQuery">打印瓶签</el-button>
			 	 	<el-button  type="primary" plain icon="Printer" @click="resetQuery">打印输液单</el-button>
		   		</el-form-item>
			</el-form>
			<div>
				<p style="margin: 0px 0px 10px 0px;">院注医嘱</p>
				<el-table :data="infusionList" border style="width: 100%;height: 300px;"  :row-class-name="rowClassName"
					 @selection-change="handleSelectionChange" ref="tableRef">
         			<el-table-column type="selection" width="55" align="center" />
					<el-table-column prop="groupId" label="组" width="60" />
					<el-table-column prop="executeNum" label="总执行次数" width="90" />
					<el-table-column prop="doneNum" label="已执行次数" width="90" />
					<el-table-column prop="doctorId_dictText" label="开单医生" width="100" />
					<el-table-column prop="patientName" label="患者姓名" width="100" />
					<el-table-column prop="genderEnum_enumText" label="性别" width="80" /> 
					<el-table-column prop="prescriptionNo" label="处方号" width="140" />
					<el-table-column prop="medicationInformation" label="药品信息" width="180" />
					<el-table-column prop="medicationQuantity" label="药品数量" width="80" />
					<el-table-column prop="rateCode" label="用药频次" width="80" />
					<el-table-column prop="dose" label="单次剂量" width="160" />
					<el-table-column prop="speed" label="输液速度" width="80" />
					<el-table-column prop="performOrg_dictText" label="发放科室" width="120" />
					<el-table-column prop="medicationStatusEnum_enumText" label="药品状态" width="100" />
					<el-table-column prop="skinTestFlag_enumText" label="是否皮试" width="60" /> 
					<!-- <el-table-column prop="clinicalStatusEnum_enumText" label="皮试结果" width="70" /> -->
				</el-table>
			</div>
			<div>
				<p style="margin: 13px 0px 10px 0px;">院注执行历史</p>
				<el-table :data="historyRecordsList" border style="width: 100%;height: 250px;">
					<el-table-column prop="occurrenceEndTime" label="执行时间" width="180" >
                        <template #default="scope">
                            <el-date-picker v-model="scope.row.occurrenceEndTime" type="datetime"
                            value-format="YYYY-MM-DD HH:mm:ss"   style="width: 170px;"/>
                        </template>
					</el-table-column>
					<el-table-column prop="performerId_dictText" label="执行人" width="80" />
					<el-table-column prop="prescriptionNo" label="处方号" width="100" />
					<el-table-column prop="doctorId_dictText" label="开单医生" width="100" />
					<el-table-column prop="medicationInformation" label="药品信息" width="180" />
					<el-table-column prop="medicationQuantity" label="药品数量" width="80" />
					<el-table-column prop="rateCode" label="用药频次" width="80" />
					<el-table-column prop="dose" label="单词剂量" width="160" />
					<el-table-column prop="speed" label="输液速度" width="80" />
					<el-table-column prop="orgId_dictText" label="发放科室" width="120" />
					<el-table-column prop="medicationStatusEnum_enumText" label="药品状态" width="100" />
					<el-table-column label="操作" align="center" width="90" fixed="right" class-name="small-padding fixed-width">
					<template #default="scope">
						<el-button link type="primary" icon="Edit" @click="handleUpdateTime(scope.row)">确认</el-button>
					</template>
			</el-table-column>
				</el-table>
			</div>
		</div>
		<div>
			<el-dialog title="处方信息" v-model="showPrescription" width="60vw" :before-close="handleClose">
				<prescriptioncard></prescriptioncard>
			</el-dialog>
		</div>
        
    </div>
</template>

<script  setup name="InfusionRecord">
import { ref, computed } from 'vue';
import { listPatients,updateInfusionRecord,listInfusionRecord,editPatientInfusionTime,
	listPatientInfusionRecord,listPatientInfusionPerformRecord } from './component/api'; 

const showSearch = ref(true);
const showPrescription = ref(false);
const total = ref(1);
const selectedItems = ref(new Set());

const tableRef = ref(null);
const selectedGroupIds = ref(new Set());
const selectedPrescriptionNos  = ref(new Set());

const currentRow = ref(null);
const dateRange = ref([]);
const dateRangeRight = ref([]);
const historyRecordsList = ref([]);
const patientList = ref([]);
const infusionList = ref([]);
// const timeRightStart = ref([]);
// const timeRightEnd = ref([]);
const ids = ref([]);

const { proxy } = getCurrentInstance();

const data = reactive({
  form: {},
  queryParams: {
	pageNo: 1,
    pageSize: 10,
    searchKey: undefined
  },
});
const { queryParams } = toRefs(data);

/** 查询门诊输液列表 */
function getList() {
    listInfusionRecord(queryParams.value).then(response => {
        console.log('Full response1:', response);
        infusionList.value = response.data;
    });
	listPatients().then(response => {
		console.log('Full response2:', response);
		patientList.value = response.data.records;
	});
	listPatientInfusionPerformRecord().then(response => {
		console.log('Full response3:', response);
		historyRecordsList.value = response.data;
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
	console.log("111",queryParams.value)
  queryParams.value.pageNo = 1;
  listPatients(queryParams.value).then(response => {
		console.log('Full response2:', response);
		patientList.value = response.data.records;
	});
}
/** 右边搜索按钮操作 */
function handleQueryRight() {
	const createTimeSTime = dateRangeRight.value[0];
	const createTimeETime = dateRangeRight.value[1];
    // timeRightStart.value = createTimeSTime;
    // timeRightEnd.value = createTimeETime;
	console.log("111",createTimeSTime,createTimeETime)
	listInfusionRecord(createTimeSTime,createTimeETime).then(response => {
		console.log('Full response1:', response);
		infusionList.value = response.data;
  	});
  	listPatientInfusionPerformRecord(createTimeSTime,createTimeETime).then(response => {
		console.log('Full response3:', response);
		historyRecordsList.value = response.data;
  });
}
/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = [];
  proxy.resetForm("queryRef");
  getList();
//   listPatients().then(response => {
// 		console.log('Full response2:', response);
// 		patientList.value = response.data.records;
// 	});
}

/** 重置按钮操作 */
function resetQueryRight() {
    dateRangeRight.value = [];
    listInfusionRecord().then(response => {
        console.log('Full response1:', response);
        infusionList.value = response.data;
    });
    listPatientInfusionPerformRecord().then(response => {
    	console.log('Full response3:', response);
    });	
}

// 执行输液
function handleSubmit(){
	 // 将 Set 转换为数组
	 const itemsList = Array.from(selectedItems.value);

	// 如果没有有效数据，直接返回
	if (itemsList.length === 0) {
	console.error("No valid items to process");
	proxy.$modal.msgError("没有有效的数据可供提交");
	return;
	}
	updateInfusionRecord(itemsList).then(response => {
		proxy.$modal.msgSuccess("执行成功");
		clearSelections();
    });
}

function handleSelectionChange(selection) {
  // 清空之前选中的数据
  selectedItems.value.clear();
  // 将当前选中的数据存到 selectedItems 中
  selection.forEach(item => {
    selectedItems.value.add(item);
  });
  // 更新 selectedGroupIds 和 selectedPrescriptionNos
  selection.forEach(item => {
    const groupId = item.groupId;
    const prescriptionNo = item.prescriptionNo;
    // 检查 groupId 和 prescriptionNo 是否同时存在
    if ( selectedGroupIds.value.has(groupId)) { //selectedPrescriptionNos.value.has(prescriptionNo) &&
      // 如果都存在，则移除它们
      selectedGroupIds.value.delete(groupId);
      selectedPrescriptionNos.value.delete(prescriptionNo);
    } else {
      // 否则添加它们
      selectedGroupIds.value.add(groupId);
      selectedPrescriptionNos.value.add(prescriptionNo);
    }
  });
  // 动态更新表格行的选中状态
  infusionList.value.forEach(row => {
    // 检查当前行的 groupId 和 prescriptionNo 是否同时在 selectedGroupIds 和 selectedPrescriptionNos 中
    const isSelected =  selectedGroupIds.value.has(row.groupId);
    tableRef.value.toggleRowSelection(row, isSelected);
  });
  console.log('Current selectedGroupIds:', selectedGroupIds.value);
  console.log('Current selectedPrescriptionNos:', selectedPrescriptionNos.value);
  console.log('Current selectedItems:', selectedItems.value);
}
function clearSelections() {
  // 清空选中状态
  selectedItems.value.clear();
  selectedGroupIds.value.clear();
  selectedPrescriptionNos.value.clear();

  // 取消表格所有行的选中状态
  infusionList.value.forEach(row => {
    tableRef.value.toggleRowSelection(row, false);
  });
  dateRangeRight.value = [];
  listPatientInfusionRecord(currentRow.value).then(response => {
		infusionList.value = response.data;
	});
  	listPatientInfusionPerformRecord().then(response => {
		console.log('Full response3:', response);
		historyRecordsList.value = response.data;
  });
}

function rowClassName({ row }) {
  if (selectedGroupIds.value.has(row.groupId)) {
    return 'selected-row';
  }
  return '';
}
function handleUpdateTime(row){
    console.log("row",row)
	editPatientInfusionTime(row).then(response => {
		proxy.$modal.msgSuccess("执行成功");
		open.value = false;
		getList();
    });
}

function handleCurrentChange(row) {
	currentRow.value = row; // 更新当前选中行的数据
	console.log("当前选中行的数据：", currentRow.value);
	listPatientInfusionRecord(currentRow.value).then(response => {
		console.log('Full response4:', response);
		infusionList.value = response.data;
	});
}

getList();

</script>

<style>
.app-container-infusion {
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
.selected-row {
  background-color: #effae8 !important;
}

</style>