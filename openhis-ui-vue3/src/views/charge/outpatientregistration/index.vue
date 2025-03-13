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
            ref="outpatientregistrationRef"
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
                <el-form-item prop="allergenFlag" label-width="0px">
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
                <el-form-item label="卡号：" prop="card">
                  <el-input
                    v-model="form.card"
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
                  prop="balanceAmount"
                  class="custom-label-spacing"
                >
                  <el-input
                    v-model="form.balanceAmount"
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
                  label="医保名称："
                  prop="ybName"
                  class="custom-label-spacing"
                >
                  <el-input
                    v-model="form.ybName"
                    placeholder=""
                    :disabled="true"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="医保区域：" prop="ybAreaNo">
                  <el-select
                    v-model="form.ybAreaNo"
                    placeholder="医保区域"
                    clearable
                    style="width: 240px"
                    :disabled="true"
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
                  label="欠费限制额度："
                  prop="limitAccount"
                  class="custom-label-spacing"
                >
                  <el-input
                    v-model="form.limitAccount"
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
                  prop="contractNo"
                  class="custom-label-spacing"
                >
                  <el-select
                    v-model="form.contractNo"
                    placeholder="费用性质"
                    clearable
                    style="width: 240px"
                    ref="contractNameRef"
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
              <el-col :span="6">
                <el-form-item label="就诊原因：" prop="jzyy">
                  <el-select
                    v-model="form.jzyy"
                    placeholder="就诊原因"
                    clearable
                    style="width: 240px"
                    ref="jzyyRef"
                  >
                    <el-option
                      v-for="dict in jzyyList"
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
                  <el-input
                    v-model="form.phone"
                    placeholder=""
                    ref="phoneRef"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item
                  label="优先级："
                  prop="priorityEnum"
                  class="custom-label-spacing"
                >
                  <el-select
                    v-model="form.priorityEnum"
                    placeholder="优先级"
                    clearable
                    style="width: 240px"
                    ref="prioritySelectRef"
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
              <!-- <el-col :span="4">
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
              </el-col> -->
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
                  label="就诊科室："
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
                    placeholder="请选择就诊科室"
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
                  prop="serviceTypeId"
                  class="custom-label-spacing"
                >
                  <el-select
                    v-model="form.serviceTypeId"
                    placeholder="挂号类型"
                    clearable
                    style="width: 240px"
                    @change="setchargeItem"
                    ref="serviceTypeRef"
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
                <el-form-item label="医生：" prop="practitionerId">
                  <el-select
                    v-model="form.practitionerId"
                    placeholder="医生"
                    clearable
                    style="width: 240px"
                    @change="setInfo"
                    ref="doctorRef"
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
                    ref="ybTypeRef"
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
            <el-row :gutter="24" justify="end">
              <el-col :span="5" style="text-align: right">
                <el-button
                  type="warning"
                  plain
                  icon="CircleClose"
                  @click="handleClear"
                  v-hasPermi="['system:user:export']"
                  >清空</el-button
                >
                <el-button
                  type="primary"
                  plain
                  icon="Plus"
                  @click="handleAdd"
                  v-hasPermi="['system:user:add']"
                  >添加</el-button
                >
              </el-col>
            </el-row>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24" class="card-box">
        <el-card>
          <template #header>
            <span style="vertical-align: middle">当日已挂号</span></template
          >
          <el-table v-loading="loading" :data="outpatientRegistrationList">
            <!-- <el-table-column
              label="租户ID"
              align="center"
              key="tenantId"
              prop="tenantId"
            />
            <el-table-column
              label="就诊ID"
              align="center"
              key="encounterId"
              prop="encounterId"
            />
            <el-table-column
              label="科室ID"
              align="center"
              key="organizationId"
              prop="organizationId"
              :show-overflow-tooltip="true"
            /> -->
            <el-table-column
              label="科室名称"
              align="center"
              key="organizationName"
              prop="organizationName"
              :show-overflow-tooltip="true"
            />
            <el-table-column
              label="挂号类型 "
              align="center"
              key="healthcareName"
              prop="healthcareName"
              :show-overflow-tooltip="true"
            />
            <!-- <el-table-column
              label="专家账号"
              align="center"
              key="practitionerUserId"
              prop="practitionerUserId"
            /> -->
            <el-table-column
              label="专家"
              align="center"
              key="practitionerName"
              prop="practitionerName"
            />
            <el-table-column
              label="费用性质"
              align="center"
              key="contractName"
              prop="contractName"
            />
            <!-- <el-table-column
              label="患者id"
              align="center"
              key="patientId"
              prop="patientId"
            /> -->
            <el-table-column
              label="患者姓名"
              align="center"
              key="patientName"
              prop="patientName"
              width="120"
            />
            <el-table-column
              label="患者性别"
              align="center"
              key="genderEnum_enumText"
              prop="genderEnum_enumText"
            />
            <el-table-column
              label="证件号"
              align="center"
              key="idCard"
              prop="idCard"
            />
            <el-table-column
              label="就诊状态"
              align="center"
              key="statusEnum_enumText"
              prop="statusEnum_enumText"
            />
            <el-table-column
              label="挂号日期/时间"
              align="center"
              key="registerTime"
              prop="registerTime"
            >
              <template #default="scope">
                <span>{{ parseTime(scope.row.registerTime) }}</span>
              </template>
            </el-table-column>
          </el-table>
          <pagination
            v-show="total > 0"
            :total="total"
            v-model:page="queryParams.pageNo"
            v-model:limit="queryParams.pageSize"
            @pagination="getList"
          />
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
  addOutpatientRegistration,
  getOutpatientRegistrationCurrent,
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
const jzyyList = ref([{ value: "1", label: "其他" }]);
// 键盘事件用
const contractNameRef = ref(null);
const jzyyRef = ref(null);
const phoneRef = ref(null);
const prioritySelectRef = ref(null);
const locationTreeRef = ref(null);
const serviceTypeRef = ref(null);
const doctorRef = ref(null);
const ybTypeRef = ref(null);

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
    pageSize: 10,
    // searchKey: undefined, // 品名/商品名/英文品名/编码/拼音
    // statusEnum: undefined, // 状态（包括 1：预置，2：启用，3：停用）
    // ybMatchFlag: undefined, // 是否医保匹配（包括 1：是，0：否）
    // status: undefined, // 状态（包括 1：预置，2：启用，3：停用）
  },
  rules: {
    // name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
    // conditionCode: [
    //   { required: true, message: "编码不能为空", trigger: "blur" },
    // ],
  },
});

// 其他输入框和选择框的 ref
const inputs = [
  contractNameRef,
  jzyyRef,
  phoneRef,
  prioritySelectRef,
  locationTreeRef,
  serviceTypeRef,
  doctorRef,
  ybTypeRef,
];

// 键盘事件处理函数
const handleKeyDown = (event) => {
  const { key } = event;

  // 获取当前焦点的元素
  const currentIndex = inputs.findIndex((input) => {
    if (input.value && input.value.$el) {
      return input.value.$el.contains(document.activeElement);
    }
    return input.value === document.activeElement;
  });

  if (key === "ArrowDown" || key === "ArrowRight" || key === "Tab") {
    event.preventDefault();
    const nextIndex = (currentIndex + 1) % inputs.length;
    const nextInput = inputs[nextIndex].value;
    if (nextInput && nextInput.focus) {
      nextInput.focus();
    } else if (nextInput && nextInput.$el) {
      nextInput.$el.querySelector("input").focus();
    }
  } else if (key === "ArrowUp" || key === "ArrowLeft") {
    event.preventDefault();
    const prevIndex = (currentIndex - 1 + inputs.length) % inputs.length;
    const prevInput = inputs[prevIndex].value;
    if (prevInput && prevInput.focus) {
      prevInput.focus();
    } else if (prevInput && prevInput.$el) {
      prevInput.$el.querySelector("input").focus();
    }
  }
};

// 添加事件监听器
onMounted(() => {
  window.addEventListener("keydown", handleKeyDown);
});

// 移除事件监听器
onUnmounted(() => {
  window.removeEventListener("keydown", handleKeyDown);
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
  form.value.patientId = formData.id;
  // 使用 nextTick 确保 DOM 更新完成后设置焦点
  nextTick(() => {
    const prioritySelect = prioritySelectRef.value?.$el?.querySelector("input");
    if (prioritySelect) {
      prioritySelect.focus();
    }
  });
}
// 设定表单
function setInfo() {
  const doctorData = doctorList.value.filter(
    (doctor) => doctor.id === form.value.practitionerId
  );
  form.value.doctorName = doctorData.length > 0 ? doctorData[0].name : "";
  console.log(doctorData, "datayisheng");
  const healthcareData = healthcareList.value.filter(
    (healthcare) => healthcare.id === form.value.serviceTypeId
  );
  form.value.locationId_dictText =
    healthcareData.length > 0 ? healthcareData[0].name : "";
  form.value.price = healthcareData.length > 0 ? healthcareData[0].price : "";
}

// 设定费用项管理表单
function setchargeItem() {
  const healthcareData = healthcareList.value.filter(
    (healthcare) => healthcare.id === form.value.serviceTypeId
  );
  form.value.definitionId =
    healthcareData.length > 0 ? healthcareData[0].definitionId : "";
  form.value.totalPrice =
    healthcareData.length > 0 ? healthcareData[0].price : "";
}
/**  查询患者信息 */
function getList() {
  loading.value = true;
  getOutpatientRegistrationCurrent(queryParams.value).then((res) => {
    loading.value = false;
    outpatientRegistrationList.value = res.data.records;
    console.log(
      outpatientRegistrationList.value,
      "outpatientRegistrationList.value"
    );
    total.value = res.data.total;
  });
}

/** 查询费用性质 */
function getContract() {
  form.value.jzyy = jzyyList.value[0]; // 设置默认值为第一项
  getContractList().then((response) => {
    contractList.value = response.data;
    form.value.contractNo = response.data[0].busNo;
  });
}

// /** 查询诊断信息 */
// function getConditionDefinition() {
//   getConditionDefinitionMetadata().then((response) => {
//     console.log("getConditionDefinitionMetadata", "response", response.data);
//   });
// }

/** 查询就诊科室 */
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
  // 设定表单中的机构ID
  form.value.organizationId = data.organizationId;

  getHealthcareMetadata(param).then((response) => {
    healthcareList.value = response.data.records;
    console.log("getHealthcareMetadata", "response", response.data);
  });
}

/** 清空条件按钮操作 */
function handleClear() {
  reset();
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
    locationId: undefined,
    serviceTypeId: undefined,
    practitionerId: undefined,
    locationId_dictText: undefined,
    doctorName: undefined,
    price: undefined,
    priorityEnum: undefined,
    patientId: undefined,
    organizationId: undefined,
    contractNo: undefined,
    typeCode: 1, // 个人现金账户 目前固定传1
    ybName: undefined,
    ybAreaNo: undefined,
    limitAccount: undefined,
    definitionId: undefined,
    serviceId: undefined,
    totalPrice: undefined,
    jzyy: 1,
  };
  proxy.resetForm("outpatientRegistrationRef");
}

/** 新增按钮操作 */
function handleAdd() {
  const transformedData = transformFormData(form.value);
  console.log(transformedData, "transformedData门诊挂号");
  addOutpatientRegistration(transformedData).then((response) => {
    reset();
    proxy.$modal.msgSuccess("新增成功");
    getList();
  });
}

// 设置新增参数
function transformFormData(form) {
  return {
    encounterFormData: {
      patientId: form.patientId,
      priorityEnum: form.priorityEnum,
      serviceTypeId: form.serviceTypeId,
      organizationId: form.organizationId,
    },
    encounterLocationFormData: {
      locationId: form.locationId,
    },
    encounterParticipantFormData: {
      practitionerId: form.practitionerId,
    },
    accountFormData: {
      patientId: form.patientId,
      typeCode: 1, // 默认值为 "1"
      name: form.ybName,
      balanceAmount: form.balanceAmount,
      ybAreaNo: form.ybAreaNo,
      contractNo: form.contractNo,
      limitAccount: form.limitAccount,
    },
    chargeItemFormData: {
      patientId: form.patientId,
      definitionId: form.definitionId,
      serviceId: form.serviceTypeId,
      totalPrice: form.totalPrice, // 默认值为 99.99
    },
  };
}

getInitData();
getList();
getContract();
// getConditionDefinition();
getLocationInfo();
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
