<template>
	<div class="app-container">
		<el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
			<el-form-item label="查询内容" prop="searchKey">
				<el-input v-model="queryParams.searchKey" placeholder="身份证号/病人ID/门诊号/姓名" clearable style="width: 210px"
					@keyup.enter="handleQuery" />
			</el-form-item>
			<el-form-item label="电话" prop="phone">
				<el-input v-model="queryParams.phone" placeholder="请输入联系方式" clearable style="width: 200px"
					@keyup.enter="handleQuery" />
			</el-form-item>
			<el-form-item label="病人名称" prop="patientname">
				<el-date-picker v-model="dateRange" value-format="YYYY-MM-DD" type="daterange" range-separator="-"
					start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
			</el-form-item>
			<el-form-item label="医生" prop="patientid">
				<el-select v-model="queryParams.doctorName" placeholder="请选择医生" clearable @keyup.enter="handleQuery"
					style="width: 160px">
					<el-option v-for="item in doctorOptions" :key="item.value" :label="item.label"
						:value="item.label" />
				</el-select>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
				<el-button icon="Refresh" @click="resetQuery">重置</el-button>
			</el-form-item>
		</el-form>

		<el-table :data="outpatienRecordsList" border style="width: 100%">
			<el-table-column prop="name" label="患者" width="180" />
			<el-table-column prop="idCard" label="身份证" width="180" />
			<el-table-column prop="description" label="疾病" width="180" />
			<el-table-column prop="patientBusNo" label="病人ID" width="180" />
			<el-table-column prop="genderEnum_enumText" label="性别" width="80" />
			<el-table-column prop="phone" label="电话" width="120" />
			<el-table-column prop="encounterTime" label="就诊时间" width="180" />
			<el-table-column prop="subjectStatusEnum_enumText" label="状态" width="120" />
			<el-table-column prop="organizationName" label="接诊医院" width="180" />
			<el-table-column prop="doctorName" label="接诊医生" width="180" />
			<!-- <el-table-column prop="address" label="会诊医院" width="180" />
			<el-table-column prop="workCompany" label="会诊医生工作单位" width="180" />
			<el-table-column prop="organizationName" label="协同服务" width="180" /> -->
		</el-table>
		<pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
			v-model:limit="queryParams.pageSize" @pagination="getList" />
	</div>
</template>

<script setup name="outpatienRecords">
import { ref, computed } from 'vue';
import { listOutpatienRecords, listDoctorNames } from "./component/api"

const showSearch = ref(true);
const total = ref(0);
const dateRange = ref([]);
const outpatienRecordsList = ref([]);
const doctorList = ref([]);

const { proxy } = getCurrentInstance();

const data = reactive({
	form: {},
	queryParams: {
		pageNo: 1,
		pageSize: 10,
		doctorName: undefined,
		searchKey: undefined,
		phone: undefined,
		patientid: undefined
	},
});
const { queryParams } = toRefs(data);

const doctorOptions = computed(() => {
	return doctorList.value.map((name, index) => ({
		value: index, // 使用索引作为 value
		label: name   // 使用名字作为 label
	}));
});

/** 查询门诊记录列表 */
function getList() {
	listOutpatienRecords(proxy.addDateRange(queryParams.value, dateRange.value)).then(response => {
		console.log(response);
		outpatienRecordsList.value = response.data.records;
		total.value = response.data.total;
	});
	listDoctorNames().then(response => {
		doctorList.value = response.data;
	});
}

/** 搜索按钮操作 */
function handleQuery() {
	console.log("123",queryParams.value)
	queryParams.value.pageNum = 1;
	getList();
}
/** 重置按钮操作 */
function resetQuery() {
	proxy.resetForm("queryRef");
	handleQuery();
}

getList();

</script>