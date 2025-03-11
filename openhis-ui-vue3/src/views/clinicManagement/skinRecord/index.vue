<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
		   <el-form-item label="门诊号" prop="patientname">
		      <el-input v-model="queryParams.patientname"  placeholder="身份证号/病人ID/门诊号/姓名" clearable style="width: 210px"
		         @keyup.enter="handleQuery" />
		   </el-form-item>
		   <el-form-item label="处方号" prop="phone">
		      <el-input v-model="queryParams.phone"  placeholder="请输入联系方式" clearable style="width: 200px"
		         @keyup.enter="handleQuery" />
		   </el-form-item>
           <el-form-item label="病人ID" prop="phone">
		      <el-input v-model="queryParams.phone"  placeholder="请输入联系方式" clearable style="width: 200px"
		         @keyup.enter="handleQuery" />
		   </el-form-item>
           <el-form-item label="电话" prop="phone">
		      <el-input v-model="queryParams.phone"  placeholder="请输入联系方式" clearable style="width: 200px"
		         @keyup.enter="handleQuery" />
		   </el-form-item>
           <el-form-item label="查询时间" prop="patientname">
            <el-date-picker v-model="dateRange"  value-format="YYYY-MM-DD" type="daterange" range-separator="-" 
            start-placeholder="开始日期" end-placeholder="结束日期" ></el-date-picker>
		   </el-form-item>
		   <el-form-item label="状态" prop="patientid">
				<el-select v-model="queryParams.bloodAbo" placeholder="请选择医生" clearable   @keyup.enter="handleQuery" style="width: 160px">
					<el-option v-for="item in bloodtypeaboList"
					:key="item.value" :label="item.info" :value="item.value" />
				</el-select>
		   </el-form-item>
		   <el-form-item>
		      <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
		      <el-button icon="Refresh" @click="resetQuery">重置</el-button>
		   </el-form-item>
		</el-form>

		<el-row :gutter="10" class="mb8">
		   <el-col :span="1.5">
		      <!-- <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['system:menu:add']">确认项目</el-button> -->
		   </el-col>
		   <el-col :span="1.5">
		      <el-button type="success" plain icon="EditPen" @click="handleUpdate" v-hasPermi="['system:menu:add']">修改</el-button>
		   </el-col>
		   <el-col :span="1.5">
		      <!-- <el-button type="danger" plain icon="Plus" @click="handleAdd" v-hasPermi="['system:menu:add']">取消修改</el-button> -->
		   </el-col>
		   <el-col :span="1.5">
		      <!-- <el-button type="warning" plain icon="Plus" @click="handleAdd" v-hasPermi="['system:menu:add']">保存</el-button> -->
		   </el-col>
		   <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
		</el-row>

        <el-table :data="skinRecordList" border style="width: 100%">
		    <el-table-column prop="prescriptionNo" label="处方号" width="150" />
		    <el-table-column prop="encounterBusNo" label="门诊号" width="150" />
			<el-table-column prop="patientName" label="病人" width="120" />
			<el-table-column prop="medicationInformation" label="药品信息" width="150" />
			<el-table-column prop="medicationDetail" label="药品" width="160" />
			<el-table-column prop="medicationLotNumber" label="药品批号" width="160" />
			<el-table-column prop="verificationStatusEnum_enumText" label="状态" width="80" />
			<el-table-column prop="clinicalStatusEnum_enumText" label="皮试结果" width="120" />
			<el-table-column prop="performerId_dictText" label="执行护士" width="130" />
			<el-table-column prop="performerCheckId_dictText" label="核对护士" width="130" />
		    <el-table-column prop="occurrenceStartTime" label="开始时间" width="180" />
			<el-table-column prop="occurrenceEndTime" label="结束时间" width="180" />
			<el-table-column prop="organizationName" label="开单医生" width="180" />
			<el-table-column prop="medicationStatusEnum" label="发药状态" width="180" />
			<el-table-column prop="note" label="备注" width="180" />
            <el-table-column label="操作" align="center" width="210" fixed="right" class-name="small-padding fixed-width">
			   <template #default="scope">
			      <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:menu:edit']">修改</el-button>
			      <el-button link type="primary" icon="EditPen" @click="sign(scope.row)" v-hasPermi="['system:menu:add']">签名</el-button>
			      <!-- <el-button link type="primary" icon="Finished" @click="submitForm(scope.row)" v-hasPermi="['system:menu:add']">保存</el-button> -->
			   </template>
			</el-table-column>
		  </el-table>
		  <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
		<el-dialog title="查看" v-model="open" width="900px" append-to-body>
			<el-form ref="skinRecordRef" :model="form" :rules="rules" label-width="100px">
				<el-row>
					<el-col :span="7">
						<el-form-item label="处方号" prop="prescriptionNo">
					  		<el-input v-model="form.prescriptionNo" disabled/>
					  	</el-form-item>
					</el-col>
					<el-col :span="7">
						<el-form-item label="门诊号" prop="encounterBusNo">
					  		<el-input v-model="form.encounterBusNo" disabled/>
					  	</el-form-item>
					</el-col>
					<el-col :span="7">
						<el-form-item label="病人" prop="patientName">
					  		<el-input v-model="form.patientName"  disabled/>
					  	</el-form-item>
					</el-col>
				</el-row><br>
				<el-row>
					<el-col :span="8">
						<el-form-item label="药品信息" prop="medicationInformation">
					  		<el-input v-model="form.medicationInformation" clearable  disabled/>
					  	</el-form-item>
					</el-col>
					<el-col :span="10">
						<el-form-item label="药品" prop="medicationDetail">
					  		<el-input v-model="form.medicationDetail"  disabled/>
					  	</el-form-item>
					</el-col>
				</el-row><br>
				<el-row>
					<el-col :span="7">
						<el-form-item label="药品批号" prop="medicationLotNumber">
					  		<el-input v-model="form.medicationLotNumber" disabled/>
					  	</el-form-item>
					</el-col>
					<el-col :span="6">
						<el-form-item label="状态" prop="verificationStatusEnum">
							<el-select v-model="form.verificationStatusEnum" placeholder="请选择状态" clearable >
						      <el-option v-for="item in statusList"
						         :key="item.value" :label="item.info" :value="item.value" />
						   </el-select>
					  	</el-form-item>
					</el-col>
					<el-col :span="7">
						<el-form-item label="皮试结果" prop="clinicalStatusEnum">
							<el-select v-model="form.clinicalStatusEnum" placeholder="请选择皮试结果" clearable :disabled="isViewMode">
						      <el-option v-for="item in skinResultList"
						         :key="item.value" :label="item.info" :value="item.value" />
						   </el-select>
					  	</el-form-item>
					</el-col>
				</el-row><br>
				<el-row>
					<el-col :span="7">
						<el-form-item label="执行护士" prop="performerId_dictText">
					  		<el-input v-model="form.performerId_dictText"  disabled/>
					  	</el-form-item>
					</el-col>
					<el-col :span="7">
						<el-form-item label="核对护士" prop="performerCheckId_dictText">
					  		<el-input v-model="form.performerCheckId_dictText"  disabled/>
					  	</el-form-item>
					</el-col>
					<el-col :span="7">
						<el-form-item label="开单医生" prop="name">
					  		<el-input v-model="form.name"  disabled/>
					  	</el-form-item>
					</el-col>
				</el-row><br>
				<el-row>
					<el-col :span="8">
						<el-form-item label="开始时间" prop="occurrenceStartTime">
							<el-date-picker v-model="form.occurrenceStartTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="开始时间" :default-time="defaultTime"/>
					  	</el-form-item>
					</el-col>
					<el-col :span="8">
						<el-form-item label="结束时间" prop="occurrenceEndTime">
							<el-date-picker v-model="form.occurrenceEndTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="结束时间" :default-time="defaultTime"/>
					  	</el-form-item>
					</el-col>
				</el-row><br>
				<el-row>
					<el-col :span="7">
						<el-form-item label="发药状态" prop="medicationStatusEnum">
							<el-input v-model="form.medicationStatusEnum"  disabled/>
					  	</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="备注" prop="note">
							<el-input v-model="form.note" clearable />
					  	</el-form-item>
					</el-col>
				</el-row>
			</el-form>
			<template #footer>
            <div class="dialog-footer">
               <el-button type="primary" @click="saveForm">确认项目</el-button>
               <el-button @click="cancel">取 消</el-button>
            </div>
         </template>
		</el-dialog>
	
	
	</div>
</template>

<script  setup name="skinRecord">
import { ref, computed } from 'vue';
import { ElMessage } from 'element-plus'

import { listSkinRecord,listStatus,listSkinResult,updateNurseSign,updateSkinTestRecord } from './component/api'; 

const showSearch = ref(true);
const total = ref(1);
const dateRange = ref([]);
const skinRecordList = ref([]);
const skinResultList = ref([]);
const statusList = ref([]);

const open = ref(false);


const { proxy } = getCurrentInstance();

const data = reactive({
  form: {},
  queryParams: {
	pageNo: 1,
    pageSize: 10,
    patientname: undefined,
    patientid: undefined
  },
});
const { queryParams,form } = toRefs(data);
/** 表单重置 */
function reset() {
  form.value = {
    prescriptionNo: 0,
    encounterBusNo: undefined,
	patientName: undefined,
	medicationInformation: undefined,
    medicationDetail: undefined,
    medicationLotNumber: undefined,
    status: undefined,
    clinicalStatusEnum: 0,
    performerId_dictText: undefined,
	performerCheckId_dictText: undefined,
	occurrenceStartTime: undefined,
	occurrenceEndTime: undefined,
	medicationStatusEnum: undefined,
	note: undefined,
  };
  proxy.resetForm("skinRecordRef");
}

/** 查询门诊皮试列表 */
function getList() {
  listSkinRecord(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
	console.log("1234",response);
    skinRecordList.value = response.data.records;
    total.value = response.data.total;
  });
  listStatus().then(response => {
	statusList.value = response.data
  });
  listSkinResult().then(response => {
	skinResultList.value = response.data
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNo = 1;
  getList();
}
/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

function cancel() {
  open.value = false;
  reset();
}
function handleUpdate(row) {
	open.value = true;
	form.value = row;
}
function sign(row){
	console.log("564564",row);
	updateNurseSign(row).then(response => {
          proxy.$modal.msgSuccess("签名成功");
          open.value = false;
          getList();
        });
}

function saveForm() {
	if (form.value.startTime > form.value.endTime) {
		ElMessage({
		message: '开始时间不能大于结束时间',
		type: 'error',
		})
	}else{
		// const index = skinRecordList.value.findIndex(item => item.id === form.id);
		// const statusInfo = statusList.value.find(item => item.value === form.value.verificationStatusEnum)?.info || '';
		// const clinicalStatusInfo = skinResultList.value.find(item => item.value === form.value.clinicalStatusEnum)?.info || '';
		// if (index !== -1) {
		// 	//暂存数据
		// 	skinRecordList.value[index].verificationStatusEnum_enumText = statusInfo;
		// 	skinRecordList.value[index].clinicalStatusEnum_enumText = clinicalStatusInfo;
		// 	skinRecordList.value[index].occurrenceStartTime = form.occurrenceStartTime;
		// 	skinRecordList.value[index].occurrenceEndTime = form.occurrenceEndTime;
		// 	skinRecordList.value[index].note = form.note;
		// 	console.log(skinRecordList.value[index]);
		// }
		console.log("564564",form.value);
		updateSkinTestRecord(form.value).then(response => {
          proxy.$modal.msgSuccess("更新成功");
          open.value = false;
          getList();
        });
		open.value = false;
	} 
}

function submitForm() {
  proxy.$refs["patientRef"].validate(valid => {
    if (valid) {
        updateSkinTestRecord(form.value).then(response => {
          proxy.$modal.msgSuccess("更新成功");
          open.value = false;
          getList();
        });
    }
  });
}

getList();

</script>