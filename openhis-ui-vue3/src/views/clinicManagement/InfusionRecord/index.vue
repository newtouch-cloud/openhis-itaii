<template>
    <div class="app-container">
		<div class="left">
			<el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
           		<el-form-item label="入院日期" prop="patientname">
            		<el-date-picker v-model="dateRange"  value-format="YYYY-MM-DD" type="daterange" range-separator="-" 
            		start-placeholder="开始日期" end-placeholder="结束日期" style="width: auto;"></el-date-picker>
		   		</el-form-item>
		   		<el-form-item label="" prop="phone">
		      		<el-input v-model="queryParams.phone"  placeholder="门诊号/病人/ID" clearable style="width: 180px"
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
				<el-table-column prop="busNo" label="处方号" width="150" />
				<el-table-column prop="doctorName" label="姓名" width="100" />
				<el-table-column prop="name" label="性别" width="80" /> 
				<el-table-column prop="name" label="年龄" width="80" />
				<el-table-column prop="status" label="身份证号" width="140" />
		  </el-table>
		  <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" 
			v-model:limit="queryParams.pageSize" @pagination="getList" />
		</div>

		<div class="right">
			<el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
           		<el-form-item label="医嘱执行时间" prop="patientname">
            		<el-date-picker v-model="dateRange"  value-format="YYYY-MM-DD" type="daterange" range-separator="-" 
            		start-placeholder="开始日期" end-placeholder="结束日期" ></el-date-picker>
		   		</el-form-item>
		   		<el-form-item>
		      		<el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
		      		<el-button icon="Refresh" @click="resetQuery">重置</el-button>
			 	 	<el-button icon="Refresh" @click="resetQuery">打印输液卡</el-button>
		   		</el-form-item>
			</el-form>
			<div>
				<p style="margin: 0px 0px 10px 0px;">院注医嘱</p>
				<el-table :data="outpatienRecordsList" border style="width: 100%;height: 300px;">
					<el-table-column prop="name" label="院注次数" width="180" />
					<el-table-column prop="idCard" label="已确认次数" width="180" />
					<el-table-column prop="description" label="开立时间" width="180" />
					<el-table-column prop="patientBusNo" label="开单医生" width="180" />
					<el-table-column prop="encounterBusNo" label="科别" width="180" />
					<el-table-column prop="genderEnum_enumText" label="性别" width="80" />
					<el-table-column prop="phone" label="医嘱" width="160" />
					<el-table-column prop="encounterTime" label="组" width="180" />
					<el-table-column prop="subjectStatusEnum_enumText" label="频次" width="120" />
					<el-table-column prop="organizationName" label="每次量" width="180" /> 
					<el-table-column prop="doctorName" label="用法" width="180" />
				</el-table>
			</div>
			<div>
				<p style="margin: 13px 0px 10px 0px;">院注执行历史</p>
				<el-table :data="outpatienRecordsList" border style="width: 100%;max-height: 250px;">
					<el-table-column prop="name" label="执行时间" width="150" />
					<el-table-column prop="genderEnum_enumText" label="执行人" width="80" />
					<el-table-column prop="name" label="患者姓名" width="100" />
					<el-table-column prop="idCard" label="开单医生" width="100" />
					<el-table-column prop="description" label="科别" width="120" />
					<el-table-column prop="phone" label="医嘱" width="160" />
					<el-table-column prop="encounterTime" label="组" width="50" />
					<el-table-column prop="subjectStatusEnum_enumText" label="频次" width="80" />
					<el-table-column prop="organizationName" label="每次量" width="80" />
					<el-table-column prop="doctorName" label="用法" width="160" />
					<el-table-column prop="encounterBusNo" label="医嘱备注" width="180" />
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
import prescriptioncard from './component/prescription.vue'

const showSearch = ref(true);
const showPrescription = ref(false);
const total = ref(1);
const currentRow = ref(null);
// const notes = ref('');
// const time = ref('');
const dateRange = ref([]);
const patientList = ref([
  {
    "busNo": "PX20250308001",
    "name": "张三",
    "genderEnum": "2025-03-08 10:30",
    "status": "待审核",
    "doctorName": "李医生"
  },
  {
    "busNo": "PX20250308002",
    "name": "李四",
    "genderEnum": "2025-03-08 11:15",
    "status": "已审核",
    "doctorName": "王医生"
  }
]);
const medicineData = ref([
  {
    idCard: '1组克林霉素磷酸酯注射液 4ml:0.6g(按C18H33ClN2O5S计) 静滴 1.00克/次 1.00支qd1天', // 药品信息
    busNo: '黄诗韧  男  25岁10月10天',             // 患者信息
    name: 'M2503040009100252',              // 门诊号
    genderEnum: '12341',              // 床号（假设 1 表示男，2 表示女）
    maritalStatusEnum: 'qd',       // 频次（假设 3 表示每天三次）
    nationalityCode: '2025-03-01 10:00', // 时间
    age: '克林霉素磷酸酯注射液4ml:0.6g(按C18H33ClN2O5S计) 静滴 1.00克 1.00支克林霉素磷酸酯注射液4ml:0.6g(按C18H33ClN2O5S计) 静滴 1.00克 1.00支', // 药品规格(用法，计量，速度，数量)
    phone: '备注信息',             // 备注
    address: '会诊医院A',
	nurse:'李护士'
  },
  {
    idCard: '1组克林霉素磷酸酯注射液 4ml:0.6g(按C18H33ClN2O5S计) 静滴 1.00克/次 1.00支qd1天', // 药品信息
    busNo: '黄诗韧  男  25岁10月10天',             // 患者信息
    name: 'M2503040009100252',              // 门诊号
    genderEnum: '12341',              // 床号（假设 1 表示男，2 表示女）
    maritalStatusEnum: 'qd',       // 频次（假设 3 表示每天三次）
    nationalityCode: '2025-03-01 10:00', // 时间
    age: '克林霉素磷酸酯注射液4ml:0.6g(按C18H33ClN2O5S计) 静滴 1.00克 1.00支', // 药品规格(用法，计量，速度，数量)
    phone: '备注信息',             // 备注
    address: '会诊医院A',
	nurse:'李护士'
  },
  {
    idCard: '1组克林霉素磷酸酯注射液 4ml:0.6g(按C18H33ClN2O5S计) 静滴 1.00克/次 1.00支qd1天', // 药品信息
    busNo: '黄诗韧  男  25岁10月10天',             // 患者信息
    name: 'M2503040009100252',              // 门诊号
    genderEnum: '12341',              // 床号（假设 1 表示男，2 表示女）
    maritalStatusEnum: 'qd',       // 频次（假设 3 表示每天三次）
    nationalityCode: '2025-03-01 10:00', // 时间
    age: '克林霉素磷酸酯注射液4ml:0.6g(按C18H33ClN2O5S计) 静滴 1.00克 1.00支', // 药品规格(用法，计量，速度，数量)
    phone: '备注信息',             // 备注
    address: '会诊医院A',
	nurse:'李护士'
  }
]);

const { proxy } = getCurrentInstance();

const data = reactive({
  form: {},
  queryParams: {
	pageNum: 1,
    pageSize: 10,
    patientname: undefined,
    patientid: undefined
  },
});
const { queryParams } = toRefs(data);

/** 查询门诊输液列表 */
function getList() {
//   listJobLog(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
//     jobLogList.value = response.rows;
//     total.value = response.total;
//     loading.value = false;
//   });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = [];
  proxy.resetForm("queryRef");
  handleQuery();
}

function handlePrescription(row){
	showPrescription.value = true;
}

function handleRowClick(row){
	console.log("handleRowClick",row);
	
}
function handleCurrentChange(row) {
      currentRow.value = row; // 更新当前选中行的数据
      console.log("当前选中行的数据：", currentRow.value);
	  medicineData.value = [];
    }

getList();

</script>

<style scoped>
.app-container {
  	display: flex;
}
.left {
  	width: 28%;
}
.right {
	margin-left: 2%;
  	width: 72%;
}
.el-table__row--current {
  background-color: cyan ; /* 青色背景 */
}
</style>