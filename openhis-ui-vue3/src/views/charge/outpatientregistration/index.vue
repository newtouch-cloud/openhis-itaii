<template>
  <div class="app-container">
    <el-row>
      <el-col :span="24" class="card-box">
        <el-card>
          <template #header>
            <span style="vertical-align: middle">门诊挂号</span></template
          >
          <el-form
            :model="form"
            :rules="rules"
            ref="medicationRef"
            label-width="110px"
          >
            <el-row :gutter="24">
              <el-col :span="5">
                <el-form-item label="病历号/姓名：" prop="searchKey">
                  <el-input
                    v-model="form.searchKey"
                    placeholder="请输入姓名/拼音/身份证"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="3">
                <el-button
                  type="primary"
                  icon="Plus"
                  @click="handleAddPatient"
                  style="width: 65px"
                  v-hasPermi="['system:user:add']"
                  >新建</el-button
                >
                <el-button
                  type="primary"
                  plain
                  icon="Search"
                  @click="handleSearch"
                  v-hasPermi="['system:user:query']"
                  >查询</el-button
                >
              </el-col>
              <el-col :span="2" class="icon-select-container">
                <el-icon><Postcard /></el-icon>
                <el-form-item prop="" label-width="0px">
                  <el-select
                    v-model="form.type"
                    placeholder="医保卡"
                    clearable
                    :disabled="true"
                    width="240px"
                  >
                    <el-option
                      v-for="item in contractList"
                      :key="item.busNo"
                      :label="item.contractName"
                      :value="item.busNo"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="1">
                <el-form-item prop="name" label-width="0px">
                  <el-checkbox
                    v-model="form.allergenFlag"
                    label="(终端)扫码"
                    :style="{ color: 'orange' }"
                  ></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="姓名：" prop="name">
                  <el-input
                    v-model="form.name"
                    placeholder="姓名"
                    :disabled="true"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item
                  label="性别："
                  prop="genderEnum_enumText"
                  class="custom-label-spacing"
                >
                  <el-input
                    v-model="form.genderEnum_enumText"
                    placeholder=""
                    :disabled="true"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item
                  label="年龄："
                  prop="age"
                  class="custom-label-spacing"
                >
                  <el-input
                    v-model="form.age"
                    placeholder=""
                    :disabled="true"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="5">
                <el-form-item label="卡号：" prop="searchKey">
                  <el-input
                    v-model="form.searchKey"
                    placeholder=""
                    :disabled="true"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="证件号：" prop="idCard">
                  <el-input
                    v-model="form.idCard"
                    placeholder=""
                    :disabled="true"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item
                  label="初复诊："
                  prop="firstEnum_enumText"
                  class="custom-label-spacing"
                >
                  <el-input
                    v-model="form.firstEnum_enumText"
                    placeholder=""
                    :disabled="true"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item
                  label="医保余额："
                  prop="pyStr"
                  class="custom-label-spacing"
                >
                  <el-input
                    v-model="form.pyStr"
                    placeholder=""
                    :disabled="true"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item
                  label="参保类型："
                  prop="locationId"
                  class="custom-label-spacing"
                >
                  <el-input
                    v-model="form.pyStr"
                    placeholder=""
                    :disabled="true"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="5">
                <el-form-item
                  label="费用性质："
                  prop="busNo"
                  class="custom-label-spacing"
                >
                  <el-select
                    v-model="form.busNo"
                    placeholder="费用性质"
                    clearable
                    style="width: 240px"
                  >
                    <el-option
                      v-for="item in contractList"
                      :key="item.busNo"
                      :label="item.contractName"
                      :value="item.busNo"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <!-- <el-col :span="6">
                <el-form-item
                  label="就诊位置："
                  prop="locationId"
                  class="custom-label-spacing"
                >
                  <el-tree-select
                    v-model="form.locationId"
                    :data="locationOptions"
                    :props="{
                      value: 'id',
                      label: 'name',
                      children: 'children',
                    }"
                    value-key="id"
                    placeholder="请选择就诊位置"
                    check-strictly
                    :expand-on-click-node="false"
                    :filter-node-method="filterNode"
                    ref="locationTreeRef"
                    node-key="value"
                    highlight-current
                    default-expand-all
                    @node-click="handleNodeClick"
                  />
                </el-form-item>
              </el-col> -->
              <el-col :span="6">
                <el-form-item label="就诊原因：" prop="name">
                  <el-select
                    v-model="form.ybType"
                    placeholder="就诊原因"
                    clearable
                    style="width: 240px"
                  >
                    <el-option
                      v-for="dict in med_chrgitm_type"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item
                  label="电话："
                  prop="phone"
                  class="custom-label-spacing"
                >
                  <el-input v-model="form.phone" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item
                  label="优先级："
                  prop="priorityLevel"
                  class="custom-label-spacing"
                >
                  <el-select
                    v-model="form.priorityLevel"
                    placeholder="优先级"
                    clearable
                    style="width: 240px"
                  >
                    <el-option
                      v-for="item in priorityLevelOptionOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item
                  label=""
                  prop="pyStr"
                  class="custom-label-spacing"
                >
                  <el-checkbox
                    v-model="form.allergenFlag"
                    label="减免"
                  ></el-checkbox>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <!-- <el-col :span="5">
                <el-form-item label="科室：" prop="name">
                  <el-select
                    v-model="form.ybType"
                    placeholder="就诊原因"
                    clearable
                    style="width: 240px"
                  >
                    <el-option
                      v-for="dict in med_chrgitm_type"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col> -->
              <el-col :span="5">
                <el-form-item
                  label="就诊位置："
                  prop="locationId"
                  class="custom-label-spacing"
                >
                  <el-tree-select
                    v-model="form.locationId"
                    :data="locationOptions"
                    :props="{
                      value: 'id',
                      label: 'name',
                      children: 'children',
                    }"
                    value-key="id"
                    placeholder="请选择就诊位置"
                    check-strictly
                    :expand-on-click-node="false"
                    :filter-node-method="filterNode"
                    ref="locationTreeRef"
                    node-key="value"
                    highlight-current
                    default-expand-all
                    @node-click="handleNodeClick"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item
                  label="挂号类型"
                  prop="healthcareType"
                  class="custom-label-spacing"
                >
                  <el-select
                    v-model="form.healthcareType"
                    placeholder="挂号类型"
                    clearable
                    style="width: 240px"
                  >
                    <el-option
                      v-for="healthcare in healthcareList"
                      :key="healthcare.id"
                      :label="healthcare.name"
                      :value="healthcare.id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="医生：" prop="doctorId">
                  <el-select
                    v-model="form.doctorId"
                    placeholder="医生"
                    clearable
                    style="width: 240px"
                    @change="setInfo"
                  >
                    <el-option
                      v-for="doctor in doctorList"
                      :key="doctor.id"
                      :label="doctor.name"
                      :value="doctor.id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item
                  label="特病病种："
                  prop="pyStr"
                  class="custom-label-spacing"
                >
                  <el-select
                    v-model="form.ybType"
                    placeholder="特病病种"
                    clearable
                    style="width: 240px"
                  >
                    <el-option
                      v-for="dict in med_chrgitm_type"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="5">
                <el-form-item label="挂号科室：" prop="locationId_dictText">
                  <el-input
                    v-model="form.locationId_dictText"
                    placeholder=""
                    :disabled="true"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="医生：" prop="doctorName">
                  <el-input
                    v-model="form.doctorName"
                    placeholder=""
                    :disabled="true"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item
                  label="挂号费："
                  prop="price"
                  class="custom-label-spacing"
                >
                  <el-input
                    v-model="form.price"
                    placeholder=""
                    :disabled="true"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item
                  label="诊疗费："
                  prop="pyStr"
                  class="custom-label-spacing"
                >
                  <el-input
                    v-model="form.pyStr"
                    placeholder=""
                    :disabled="true"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item
                  label="总金额："
                  prop="pyStr"
                  class="custom-label-spacing"
                >
                  <el-input
                    v-model="form.pyStr"
                    placeholder=""
                    :disabled="true"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
    <patient-info-dialog
      ref="patientInfoRef"
      :patientInfoData="patientInfoList"
      :searchInfo="form.searchKey"
      @submit="setForm"
    />
    <patient-add-dialog ref="patientAddRef" @submit="setForm" />
  </div>
</template>

<script setup name="OutpatientRegistration">
import {
  getOutpatientRegistrationList,
  getInit,
  getContractList,
  getConditionDefinitionMetadata,
  getLocationTree,
  getPractitionerMetadata,
  getHealthcareMetadata,
} from "./components/outpatientregistration";
import patientInfoDialog from "./components/patientInfoDialog";
import PatientAddDialog from "./components/patientAddDialog";
import { nextTick } from "vue";

const router = useRouter();
const { proxy } = getCurrentInstance();
const { sys_normal_disable, sys_user_sex, med_chrgitm_type } = proxy.useDict(
  "sys_normal_disable",
  "sys_user_sex",
  "med_chrgitm_type"
);

const outpatientRegistrationList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const selectedData = ref([]); // 存储选择的行数据
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const priorityLevelOptionOptions = ref(undefined); // 优先级

// 使用 ref 定义查询所得用户信息数据
const patientInfoList = ref(undefined);
// 费用性质
const contractList = ref(undefined);
const locationOptions = ref(undefined); // 地点树选项
const doctorList = ref(undefined); // 医生选项
const healthcareList = ref(undefined); // 挂号项目选项
// const locationOptions = ref(undefined); // 诊断信息
// const initPassword = ref(undefined);
// const postOptions = ref([]);
// const roleOptions = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 50,
    searchKey: undefined, // 品名/商品名/英文品名/编码/拼音
    statusEnum: undefined, // 状态（包括 1：预置，2：启用，3：停用）
    ybMatchFlag: undefined, // 是否医保匹配（包括 1：是，0：否）
    status: undefined, // 状态（包括 1：预置，2：启用，3：停用）
  },
  rules: {
    // name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
    // conditionCode: [
    //   { required: true, message: "编码不能为空", trigger: "blur" },
    // ],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 初期所用数据查询 */
function getInitData() {
  getInit().then((response) => {
    console.log(response, "response");
    priorityLevelOptionOptions.value = response.data.priorityLevelOptionOptions; // 优先级
  });
}

/** 打开用户信息弹窗 */
function handleSearch() {
  console.log(form.value.searchKey, "form.value.searchKey");
  if (!form.value.searchKey) {
    proxy.$modal.msgError("请输入查询内容");
    return;
  }
  const param = {
    searchKey: form.value.searchKey,
  };
  getOutpatientRegistrationList(param).then((res) => {
    loading.value = false;
    console.log(param, "param");
    if (res.data.records.length > 0) {
      patientInfoList.value = res.data;
      console.log(patientInfoList.value, "patientInfoList.value");
      nextTick(() => {
        proxy.$refs["patientInfoRef"].show(); // 确保子组件更新后再调用 show 方法
      });
    }
  });
}

/** 新增用户信息弹窗 */
function handleAddPatient() {
  proxy.$refs["patientAddRef"].show(); // 确保子组件更新后再调用 show 方法
}

// 设定表单
function setForm(formData) {
  console.log(formData, "formData");
  form.value = { ...form.value, ...formData };
}
// 设定表单
function setInfo() {
  const doctorData = doctorList.value.filter(
    (doctor) => doctor.id === form.value.doctorId
  );
  form.value.doctorName = doctorData.length > 0 ? doctorData[0].name : "";
  console.log(doctorData, "datayisheng");
  const healthcareData = healthcareList.value.filter(
    (healthcare) => healthcare.id === form.value.healthcareType
  );
  form.value.locationId_dictText =
    healthcareData.length > 0 ? healthcareData[0].name : "";
  form.value.price = healthcareData.length > 0 ? healthcareData[0].price : "";
}
/** 查询病种目录列表 */
function getList() {
  loading.value = true;
  getOutpatientRegistrationList(queryParams.value).then((res) => {
    loading.value = false;
    outpatientRegistrationList.value = res.data.records;
    total.value = res.data.total;
  });
}

/** 查询费用性质 */
function getContract() {
  getContractList().then((response) => {
    contractList.value = response.data;
  });
}

/** 查询诊断信息 */
function getConditionDefinition() {
  getConditionDefinitionMetadata().then((response) => {
    console.log("getConditionDefinitionMetadata", "response", response.data);
  });
}

/** 查询就诊位置 */
function getLocationInfo() {
  getLocationTree().then((response) => {
    locationOptions.value = response.data.records;
  });
}
/** 通过条件过滤节点  */
const filterNode = (value, data) => {
  if (!value) return true;
  return data.label.indexOf(value) !== -1;
};
/** 节点单击事件 */
function handleNodeClick(data) {
  // queryParams.value.sourceEnum = data.value;
  // handleQuery();
  console.log("handleNodeClick", "data", data);
  getPractitioner(data);
  getHealthcare(data);
}

/** 根据位置id筛选医生 */
function getPractitioner(data) {
  const param = {
    locationId: data.id,
  };
  console.log("getPractitioner", "param", param);
  getPractitionerMetadata(param).then((response) => {
    console.log("getPractitioner", "response", response.data);
    doctorList.value = response.data.records;
  });
}

/** 根据机构id筛选服务项目 */
function getHealthcare(data) {
  const param = {
    organizationId: data.organizationId,
  };
  getHealthcareMetadata(param).then((response) => {
    healthcareList.value = response.data.records;
    console.log("getHealthcareMetadata", "response", response.data);
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNo = 1;
  getList();
}

/** 重置操作表单 */
function reset() {
  form.value = {
    id: undefined,
    searchKey: undefined,
    type: undefined,
    allergenFlag: undefined,
    name: undefined,
    genderEnum_enumText: undefined,
    age: undefined,
    idCard: undefined,
    pyStr: undefined,
    busNo: undefined,
    ybType: undefined,
    phone: undefined,
    allergenFlag: undefined,
    locationId: undefined,
    healthcareType: undefined,
    doctorId: undefined,
    locationId_dictText: undefined,
    doctorName: undefined,
    price: undefined,
    priorityLevel: undefined,
  };
  proxy.resetForm("outpatientRegistrationRef");
}
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增";
}

getInitData();
getList();
getContract();
getConditionDefinition();
getLocationInfo();
// getPractitioner();
// getHealthcare();
</script>
<style scoped>
.el-form--inline .el-form-item {
  display: inline-flex;
  vertical-align: middle;
  margin-right: 10px !important;
}

/* 让图标和下拉选在同一行显示 */
.icon-select-container {
  display: flex; /* 使用 Flexbox 布局 */
  align-items: center; /* 垂直居中 */
  padding-left: 0px;
}

/* 调整 el-form-item 的样式 */
.icon-select-container .el-form-item {
  margin-bottom: 0; /* 去掉默认的 margin-bottom */
  margin-left: 8px; /* 图标和下拉选之间的间距 */
}
</style>
