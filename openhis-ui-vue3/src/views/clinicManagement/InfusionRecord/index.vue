<template>
    <div class="app-container1">

		<div class="left">
			<el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
           		<el-form-item label="入院日期" prop="patientname">
            		<el-date-picker v-model="dateRange"  value-format="YYYY-MM-DD" type="daterange" range-separator="-" 
            		start-placeholder="开始日期" end-placeholder="结束日期" ></el-date-picker>
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
				<el-table-column label="操作" align="center" width="90" fixed="right">
					<template #default="scope">
							<el-button link type="primary" icon="Edit" @click="handlePrescription(scope.row)" v-hasPermi="['system:menu:edit']">处方</el-button>
					</template>
				</el-table-column>
				<el-table-column prop="busNo" label="处方号" width="160" />
				<el-table-column prop="name" label="病人" width="120" />
				<el-table-column prop="genderEnum" label="开方时间" width="140" />
				<el-table-column prop="status" label="状态" width="140" />
				<el-table-column prop="doctorName" label="开方医生" width="140" />
		  </el-table>
		  <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" style="margin-left: 15px;" />
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
			<el-row :gutter="20" style="display: flex; flex-wrap: wrap;" v-if="medicineData.length > 0">
    			<el-col v-for="(item, index) in medicineData" :key="index" style="flex: 0 0 auto;"  >
					<el-card style="width: 480px;margin-top: 15px;">
						<template #header>
							<div class="card-header">
								<span>1组<br>克林霉素磷酸酯注射液 4ml:0.6g(按C18H33ClN2O5S计) 静滴 1.00克/次 1.00支qd1天</span>
							</div>
						</template>
						<div style="margin-top: -3%;border-bottom: 1px dashed #F0F0F0; ">
							<div style="display: flex;">
								<p>病人 男 25岁10月10天</p>
								<p>
									<span span style="margin-left: 15px;border-left: 1px solid #DEDEDE;"></span>
									<span style="margin-left: 15px;">床号：</span>
									<span>123456</span>
								</p>
								<p>
									<span span style="margin-left: 15px;border-left: 1px solid #DEDEDE;"></span>
									<span style="margin-left: 15px;">频次：</span>
									<span>qd</span>
								</p>
							</div>
							<p style="margin-top: 0;">
								<span style="color: black;">门诊号：</span>{{ item.name }}
								<span style="margin-left: 20px;">
									<el-date-picker style="width: 180px;" v-model="item.time" type="datetime" placeholder="时间选择" :default-time="defaultTime"/>
								</span>
							</p>
						</div>
						
						<div style="text-align: center;">
							<div style="border-bottom: 1px solid #CFCFCF;margin-top: 10px;padding-bottom: 5px;">
								药品规格<div style="height: 10px;"></div>用法&nbsp;&nbsp;&nbsp;计量&nbsp;&nbsp;&nbsp;速度&nbsp;&nbsp;&nbsp;数量
							</div>
							<div style="margin-top: 10px;">{{ item.age }}</div>
						</div><br>
						<div style="line-height: 15px;align-items: center;">
							<span>
								备注:
							</span>
							<span>
								<el-input v-model="item.notes" clearable style="width: 210px;"/>
							</span>
							<span style="margin-left: 30px;color: red;">
								{{ item.nurse }}
							</span>
						</div>
						
						<template #footer="scope">
							<el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:menu:edit']">确认</el-button>
							<el-button link type="primary" icon="View" @click="handleSee(scope.row)" v-hasPermi="['system:menu:add']">打印</el-button>
						</template>
					</el-card>
    			</el-col>
			</el-row>
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
// function getList() {
//   listJobLog(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
//     jobLogList.value = response.rows;
//     total.value = response.total;
//     loading.value = false;
//   });
// }

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
/** 重置按钮操作 */
function resetQuery() {
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

// getList();

</script>

<style scoped>
.app-container1 {
	padding: 20px;
  	display: flex;
}
.left {
	/* background-color: #f3f1f19f; */
  	width: 27%;
}
.right {
	margin-left: 5%;
  	width: 72%;
}
.el-table__row--current {
  background-color: cyan !important; /* 青色背景 */
}
</style>