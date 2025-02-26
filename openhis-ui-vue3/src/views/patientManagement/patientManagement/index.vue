<template>
	<div class="app-container">
		<el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
		   <el-form-item label="病人名称" prop="patientname">
		      <el-input v-model="queryParams.patientname"  placeholder="请输入病人名称" clearable style="width: 200px"
		         @keyup.enter="handleQuery" />
		   </el-form-item>
		   <el-form-item label="病人ID" prop="patientid">
		      <el-input v-model="queryParams.patientid"  placeholder="请输入病人ID" clearable style="width: 200px"
		         @keyup.enter="handleQuery" />
		   </el-form-item>
		   <el-form-item>
		      <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
		      <el-button icon="Refresh" @click="resetQuery">重置</el-button>
		   </el-form-item>
		</el-form>
		
		<el-row :gutter="10" class="mb8">
		   <el-col :span="1.5">
		      <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['system:menu:add']">添加病人</el-button>
		   </el-col>
		   <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
		</el-row>
		
		 <el-table :data="tableData" border style="width: 100%">
		    <el-table-column prop="idCard" label="身份证号" width="180" />
		    <el-table-column prop="busNo" label="病人ID" width="180" />
			<el-table-column prop="name" label="病人名称" width="180" />
			<el-table-column prop="genderEnum" label="性别" width="180" />
			<el-table-column prop="maritalStatusEnum" label="婚姻状况" width="180" />
			<el-table-column prop="nationalityCode" label="民族" width="180" />
			<el-table-column prop="age" label="年龄" width="180" />
			<el-table-column prop="phone" label="电话" width="180" />
			<el-table-column prop="address" label="家庭地址" width="180" />
			<el-table-column prop="workCompany" label="工作单位" width="180" />
			<el-table-column prop="organizationName" label="登记医院" width="180" />
			<el-table-column prop="createTime" label="登记时间" width="180" />
			<el-table-column label="操作" align="center" width="210" fixed="right" class-name="small-padding fixed-width">
			   <template #default="scope">
			      <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:menu:edit']">修改</el-button>
			      <el-button link type="primary" icon="Plus" @click="handleSee(scope.row)" v-hasPermi="['system:menu:add']">查看</el-button>
			      <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:menu:remove']">删除</el-button>
			   </template>
			</el-table-column>
		  </el-table>
		  <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
		  
		  <!-- 添加或修改对话框 -->
		  <el-dialog :title="title" v-model="open" width="980px" append-to-body>
			<el-form ref="patientRef" :model="form" :rules="rules" label-width="100px">
				<el-row>
					  <el-col :span="6">
					  	<el-form-item label="姓名" prop="name">
					  		<el-input v-model="form.name"  />
					  	</el-form-item>
					  </el-col>
					  <el-col :span="7">
						<el-form-item label="其他姓名" prop="nameJson">
						   <el-input v-model="form.nameJson" />
						</el-form-item>
					  </el-col>	
					  <el-col :span="6">
						<el-form-item label="民族" prop="nationalityCode">
						   <el-input v-model="form.nationalityCode" />
						</el-form-item>
					  </el-col>					  
				  </el-row>
				  <el-row>
					  <el-col :span="6">
						<el-form-item label="年龄" prop="age">
						   <el-input v-model="form.age" />
						</el-form-item>
					  </el-col>
					  <el-col :span="8">
						<el-form-item label="性别" prop="genderEnum">
						   <el-radio-group v-model="form.genderEnum">
								<el-radio v-for="item in administrativegenderList" :key="item.value" :label="item.value" >
									{{ item.info }}
								</el-radio>
						   </el-radio-group>
						</el-form-item>
					  </el-col>
					  <el-col :span="7">
						<el-form-item label="活动标识" prop="tempFlag">
						   <el-radio-group v-model="form.tempFlag">
								<el-radio v-for="dict in patient_temp_flag" :key="dict.value" :label="dict.value" >
									{{ dict.label }}
								</el-radio>
						   </el-radio-group>
						</el-form-item>
					  </el-col>						  
				  </el-row>
				  <el-row>
					  <el-col :span="7">
						<el-form-item label="证件类别" prop="idType">
						   <el-select v-model="form.idType" placeholder="证件类别" clearable >
						      <el-option v-for="dict in sys_idtype"
						         :key="dict.value" :label="dict.label" :value="dict.value" />
						   </el-select>
						</el-form-item>
					  </el-col>
					  <el-col :span="8">
						<el-form-item label="证件号码" prop="idCard">
						   <el-input v-model="form.idCard"   />
						</el-form-item>
					  </el-col>
					  <el-col :span="6">
						<el-form-item label="国家编码" prop="countryCode">
						   <el-input v-model="form.countryCode"   />
						</el-form-item>
					  </el-col>					  
				  </el-row>
				  <el-row>
					  <el-col :span="7">
						<el-form-item label="联系方式" prop="phone">
						   <el-input v-model="form.phone"   />
						</el-form-item>
					  </el-col>
					  <el-col :span="8">
						<el-form-item label="职业" prop="prfsEnum">
                            <el-select v-model="form.prfsEnum" placeholder="职业" clearable >
						      <el-option v-for="item in occupationtypeList"
						         :key="item.value" :label="item.info" :value="item.value" />
						   </el-select>
						</el-form-item>
					  </el-col>	
					  <el-col :span="6">
						<el-form-item label="工作单位" prop="workCompany">
						   <el-input v-model="form.workCompany"   />
						</el-form-item>
					  </el-col>				  
				  </el-row>
				  <el-row>
					  <el-col :span="7">
						<el-form-item label="联系人" prop="linkName">
						   <el-input v-model="form.linkName"   />
						</el-form-item>
					  </el-col>
					  <el-col :span="8">
						<el-form-item label="联系人关系" prop="linkRelationCode">
							<el-select v-model="form.linkRelationCode" placeholder="联系人关系" clearable >
						      <el-option v-for="item in familyrelationshiptypeList"
						         :key="item.value" :label="item.info" :value="item.value" />
						   </el-select>
						</el-form-item>
					  </el-col>	
					  <el-col :span="6">
						<el-form-item label="联系人电话" prop="linkRelationCode">
						   <el-input v-model="form.linkTelcom"   />
						</el-form-item>
					  </el-col>				  
				  </el-row>
				  <el-row>
					  <el-col :span="10">
						<el-form-item label="地址选择" prop="addressSelect">
							<RegionFullGroup v-model="form.addressSelectvalue" @change="handleChangeAddress"/>
						</el-form-item>
					  </el-col>		
					  <el-col :span="8">
						<el-form-item label="详细地址" prop="address">
						   <el-input v-model="form.address"   />
						</el-form-item>
					  </el-col>			  
				  </el-row>
				  <el-row>
					  <el-col :span="10">
						<el-form-item label="血型ABO" prop="bloodAbo">
							<el-select v-model="form.bloodAbo" placeholder="血型ABO" clearable >
						      <el-option v-for="item in bloodtypeaboList"
						         :key="item.value" :label="item.info" :value="item.value" />
						   </el-select>
						</el-form-item>
					  </el-col>
					  <el-col :span="10">
						<el-form-item label="血型RH" prop="bloodRh">
							<el-select v-model="form.bloodRh" placeholder="血型RH" clearable >
						      <el-option v-for="item in bloodtypearhList"
						         :key="item.value" :label="item.info" :value="item.value" />
						   </el-select>
						</el-form-item>
					  </el-col>					  
				  </el-row>
				  <el-row>
					  <el-col :span="10">
						<el-form-item label="婚姻状态" prop="maritalStatusEnum">
							<el-select v-model="form.maritalStatusEnum" placeholder="婚姻状态" clearable >
						      <el-option v-for="item in maritalstatusList"
						         :key="item.value" :label="item.info" :value="item.value" />
						   </el-select>
						</el-form-item>
					  </el-col>		
					  <el-col :span="10">
						<el-form-item label="死亡时间" prop="deceasedDate">
							<el-date-picker v-model="form.deceasedDate" type="datetime" placeholder="请选择时间" format="YYYY/MM/DD HH:mm:ss"/>
						</el-form-item>
					  </el-col>			  
				  </el-row>
			</el-form>
			<template #footer>
            <div class="dialog-footer">
               <el-button type="primary" @click="submitForm">确 定</el-button>
               <el-button @click="cancel">取 消</el-button>
            </div>
         </template>
		  </el-dialog>
		  
	</div>
</template>

<script  setup name="patientManagement">
import { ref, computed } from 'vue';
import familyRelationships from "./component/familyRelationships"
import {listmaritalstatus,listoccupationtype,lisadministrativegender,listbloodtypeabo,listbloodtypearh,listfamilyrelationshiptype} from "./component/api"
import { RegionFullGroup } from 'v-region'

const showSearch = ref(true);
const open = ref(false);
const title = ref("");
const total = ref(1);
const maritalstatusList = ref([])  //婚姻
const occupationtypeList = ref([]) //职业
const administrativegenderList = ref([]) //性别
const bloodtypeaboList = ref([]) //血型abo
const bloodtypearhList = ref([]) //血型RH
const familyrelationshiptypeList = ref([]) //家庭关系

const { proxy } = getCurrentInstance();
const { patient_gender_enum,sys_idtype,prfs_enum,blood_rh,blood_abo,marital_status_enum,patient_temp_flag,link_relation_code} =
 proxy.useDict("patient_gender_enum", "sys_idtype","prfs_enum","blood_rh","blood_abo","marital_status_enum","patient_temp_flag","link_relation_code");

const data = reactive({
  form: {},
  queryParams: {
	pageNum: 1,
    pageSize: 10,
    patientname: undefined,
    patientid: undefined
  },
  rules: {
    name: [{ required: true, message: "姓名不能为空", trigger: "blur" }],
    idCard: [
    { required: true, message: '证件号码不能为空', trigger: 'blur' },
    { min: 18, message: '证件号码不能少于18位', trigger: 'blur' }
  	],
    phone: [{ required: true, message: "联系方式不能为空", trigger: "blur" }],
  },
});
const { queryParams, form, rules } = toRefs(data);


/** 查询菜单列表 */
function getList() {
  listmaritalstatus().then(response => {
	maritalstatusList.value = response.data
  });
  listoccupationtype().then(response => {
	occupationtypeList.value = response.data
  });
  lisadministrativegender().then(response => {
	administrativegenderList.value = response.data
  });
  listbloodtypeabo().then(response => {
	bloodtypeaboList.value = response.data
  });
  listbloodtypearh().then(response => {
	bloodtypearhList.value = response.data
  });
  listfamilyrelationshiptype().then(response => {
	familyrelationshiptypeList.value = response.data
  });
}

/** 表单重置 */
function reset() {
  form.value = {
    name: undefined,
    nameJson: undefined,
    menuName: undefined,
    age: undefined,
    genderEnum: undefined,
    idType: undefined,
	idCard: undefined,
	phone: undefined,
	prfsEnum: undefined,
	address: undefined,
	tempFlag: undefined,
	countryCode: undefined,
	bloodRh: undefined,
	bloodAbo: undefined,
	nationalityCode: undefined,
	deceasedDate: undefined,
	linkName: undefined,
	linkRelationCode: undefined,
	linkTelcom: undefined,
	workCompany: undefined,
	addressCity: undefined,
	addressDistrict: undefined,
	addressStreet: undefined,
	addressProvince: undefined,
	maritalStatusEnum: undefined,
	busNo: undefined,
	organizationId: undefined,
	addressSelectvalue : {  province: null, city: null, area: null, town: null },
  };
  proxy.resetForm("patientRef");
}
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
/** 新增按钮操作 */
function handleAdd(row) {
	console.log("123")
  reset();
  open.value = true;
  title.value = "添加病人";
}
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}
/**地区选择器 */
function handleChangeAddress(data){
  form.value.addressProvince = data.province || '';
  form.value.addressCity = data.city || '';
  form.value.addressDistrict = data.area || '';
  form.value.addressStreet = data || '';
}
/** 提交按钮 */
function submitForm() {
  proxy.$refs["patientRef"].validate(valid => {
    if (valid) {
    //   if (form.value.menuId != undefined) {
    //     updateMenu(form.value).then(response => {
    //       proxy.$modal.msgSuccess("修改成功");
    //       open.value = false;
    //       getList();
    //     });
    //   } else {
    //     addMenu(form.value).then(response => {
    //       proxy.$modal.msgSuccess("新增成功");
    //       open.value = false;
    //       getList();
    //     });
    //   }
    }
  });
}
getList();
</script>

<style>
</style>