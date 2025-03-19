<template>
    <div class="app-container-infusion">
		<div class="left">
			<el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
           		<el-form-item label="医嘱执行时间" prop="patientname">
            		<el-date-picker v-model="dateRange"  value-format="YYYY-MM-DD" type="daterange" range-separator="-" 
            		start-placeholder="开始日期" end-placeholder="结束日期" style="width: auto;"></el-date-picker>
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
				<!-- <el-table-column label="操作" align="center" width="90" fixed="right">
					<template #default="scope">
							<el-button link type="primary" icon="Edit" @click="handlePrescription(scope.row)" v-hasPermi="['system:menu:edit']">处方</el-button>
					</template>
				</el-table-column> -->
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
		   		<el-form-item>
		      		<el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
		      		<el-button icon="Refresh" @click="resetQuery">重置</el-button>
			 	 	<el-button  type="primary" icon="SuccessFilled" @click="handleSubmit">确认执行</el-button>
			 	 	<el-button  type="primary" icon="SuccessFilled" @click="handleSubmitCanel">取消执行</el-button>
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
					<el-table-column prop="executionCount" label="已执行次数" width="100" />
					<el-table-column prop="doctorId_dictText" label="开单医生" width="100" />
					<el-table-column prop="patientName" label="患者姓名" width="100" />
					<el-table-column prop="genderEnum_enumText" label="性别" width="80" /> 
					<el-table-column prop="status" label="身份证号" width="140" />
					<el-table-column prop="medicationInformation" label="药品信息" width="180" />
					<el-table-column prop="medicationAntity" label="药品数量" width="80" />
					<el-table-column prop="rateCode" label="用药频次" width="80" />
					<el-table-column prop="dose" label="单词剂量" width="160" />
					<el-table-column prop="speed" label="输液速度" width="80" />
					<el-table-column prop="orgId_dictText" label="发放科室" width="120" />
					<el-table-column prop="medicationStatusEnum_enumText" label="药品状态" width="100" />
					<el-table-column prop="skinTestFlag_enumText" label="是否皮试" width="60" /> 
					<el-table-column prop="clinicalStatusEnum_enumText" label="皮试结果" width="60" />
					<el-table-column label="操作" align="center" width="90" fixed="right">
						<template #default="scope">
								<el-button link type="primary" icon="Edit" @click="handleSubmit(scope.row)" >执行</el-button>
						</template>
					</el-table-column>
				</el-table>
			</div>
			<div>
				<p style="margin: 13px 0px 10px 0px;">院注执行历史</p>
				<el-table :data="historyRecordsList" border style="width: 100%;height: 250px;">
					<el-table-column prop="occurrenceStartTime" label="执行时间" width="150" />
					<el-table-column prop="performerId_dictText" label="执行人" width="80" />
					<el-table-column prop="prescriptionNo" label="处方号" width="100" />
					<el-table-column prop="doctorId_dictText" label="开单医生" width="100" />
					<el-table-column prop="medicationInformation" label="药品信息" width="180" />
					<el-table-column prop="medicationAntity" label="药品数量" width="80" />
					<el-table-column prop="rateCode" label="用药频次" width="80" />
					<el-table-column prop="dose" label="单词剂量" width="160" />
					<el-table-column prop="speed" label="输液速度" width="80" />
					<el-table-column prop="orgId_dictText" label="发放科室" width="120" />
					<el-table-column prop="medicationStatusEnum_enumText" label="药品状态" width="100" />
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
import { listPatients,updateInfusionRecord,listInfusionRecord,
	listPatientInfusionRecord,listPatientInfusionPerformRecord } from './component/api.js'; 

const showSearch = ref(true);
const showPrescription = ref(false);
const total = ref(1);
const selectedItems = ref(new Set());

const tableRef = ref(null);
const selectedGroupIds = ref(new Set());

const currentRow = ref(null);
const dateRange = ref([]);
const historyRecordsList = ref([])
const patientList = ref([]);
const infusionList = ref([]);
// const infusionList = ref([
//       { groupId: 1, executionCount: 2, doctorId_dictText: '张三', patientName: '李四', genderEnum_enumText: '男', status: '123456789012345678', medicationInformation: '药品A', medicationAntity: 10, rateCode: '每日一次', dose: '10mg', speed: '50ml/h', orgId_dictText: '内科', medicationStatusEnum_enumText: '已发放', skinTestFlag_enumText: '是', clinicalStatusEnum_enumText: '阴性' },
//       { groupId: 1, executionCount: 2, doctorId_dictText: '张三', patientName: '王五', genderEnum_enumText: '女', status: '123456789012345679', medicationInformation: '药品A', medicationAntity: 10, rateCode: '每日一次', dose: '10mg', speed: '50ml/h', orgId_dictText: '内科', medicationStatusEnum_enumText: '已发放', skinTestFlag_enumText: '是', clinicalStatusEnum_enumText: '阴性' },
//       { groupId: 2, executionCount: 1, doctorId_dictText: '李六', patientName: '赵七', genderEnum_enumText: '男', status: '123456789012345680', medicationInformation: '药品B', medicationAntity: 5, rateCode: '每日两次', dose: '5mg', speed: '30ml/h', orgId_dictText: '外科', medicationStatusEnum_enumText: '已发放', skinTestFlag_enumText: '否', clinicalStatusEnum_enumText: '无' },
//     ]);

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
  listInfusionRecord(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
	console.log('Full response:', response);
  });
	listPatients().then(response => {
		console.log('Full response:', response); // 打印完整响应
		patientList.value = response.data;
		total.value = response.total;
	});
	listPatientInfusionPerformRecord().then(response => {
		console.log('Full response:', response); // 打印完整响应
		historyRecordsList.value = response.data;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
	queryParams.value.beginTime = dateRange.value[0];
	queryParams.value.endTime = dateRange.value[1];
  queryParams.value.pageNo = 1;
  getList();
}
/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}

function handleSubmit(){
	const itemsList = Array.isArray(selectedItems.value) ? selectedItems.value : [selectedItems.value];
	console.log('Full response:', itemsList,selectedItems.value);
	updateInfusionRecord(itemsList).then(response => {
		proxy.$modal.msgSuccess("执行成功");
		open.value = false;
		getList();
    });
}

function isEqual(set1, set2) {
  if (set1.size !== set2.size) return false;
  for (let item of set1) {
    if (!set2.has(item)) return false;
  }
  return true;
}

function handleSelectionChange(selection) {
  // 清空之前选中的数据
  selectedItems.value.clear();
  // 将当前选中的数据存到 selectedItems 中
  selection.forEach(item => {
    selectedItems.value.add(item);
  });
  // 获取当前选中的 groupId 和 medicationId 集合
  const currentGroupIds = new Set(selection.map(item => item.groupId));
//   const currentMedicationIds = new Set(selection.map(item => item.medicationId));
  // 更新 selectedGroupIds 和 selectedMedicationIds
  selection.forEach(item => {
    const groupId = item.groupId;
    const medicationId = item.medicationId;
    // 检查 groupId 和 medicationId 是否同时存在
    if (selectedGroupIds.value.has(groupId) ) {
      // 如果都存在，则移除它们
      selectedGroupIds.value.delete(groupId);
    //   selectedMedicationIds.value.delete(medicationId);
    } else {
      // 否则添加它们
      selectedGroupIds.value.add(groupId);
    //   selectedMedicationIds.value.add(medicationId);
    }
  });
  // 动态更新表格行的选中状态
  infusionList.value.forEach(row => {
    // 检查当前行的 groupId 和 medicationId 是否同时在 selectedGroupIds 和 selectedMedicationIds 中
    const isSelected = selectedGroupIds.value.has(row.groupId) 
    tableRef.value.toggleRowSelection(row, isSelected);
  });
  console.log('Current selectedGroupIds:', selectedGroupIds.value);
//   console.log('Current selectedMedicationIds:', selectedMedicationIds.value);
  console.log('Current selectedItems:', selectedItems.value);
}
function handleSubmitCanel(){
	ids.value = []
	currentRow.value  = []
	selectedGroupIds.value.clear(); // 清空 selectedGroupIds
    infusionList.value.forEach(row => {
    tableRef.value.toggleRowSelection(row, false); // 取消选中所有行
    });
}

function rowClassName({ row }) {
  if (selectedGroupIds.value.has(row.groupId)) {
	console.log('Row groupId:', row.groupId, 'selectedGroupIds:', selectedGroupIds.value);
    return 'selected-row';
  }
  return '';
}


function handleCurrentChange(row) {
      currentRow.value = row; // 更新当前选中行的数据
      console.log("当前选中行的数据：", currentRow.value);
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