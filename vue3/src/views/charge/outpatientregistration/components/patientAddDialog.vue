<template>
  <!-- <div class="app-container"> -->
  <!-- 添加或修改对话框 -->
  <el-dialog :title="title" v-model="visible" width="980px" append-to-body>
    <el-form ref="patientRef" :model="form" :rules="rules" label-width="100px">
      <el-row>
        <el-col :span="6">
          <el-form-item label="姓名" prop="name">
            <el-input v-model="form.name" clearable :disabled="isViewMode" />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="7">
          <el-form-item label="其他姓名" prop="nameJson">
            <el-input v-model="form.nameJson" clearable :disabled="isViewMode" />
          </el-form-item>
        </el-col> -->
        <el-col :span="6">
          <el-form-item label="民族" prop="nationalityCode">
            <el-select v-model="form.nationalityCode" clearable filterable :disabled="isViewMode">
              <el-option
                v-for="item in nationality_code"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <!-- <el-col :span="6">
						<el-form-item label="年龄" prop="age">
						   <el-input v-model="form.age" clearable :disabled="isViewMode"/>
						</el-form-item>
					  </el-col> -->
        <el-col :span="12">
          <el-form-item label="性别" prop="genderEnum">
            <el-radio-group v-model="form.genderEnum" :disabled="isViewMode">
              <el-radio
                v-for="item in administrativegenderList"
                :key="item.value"
                :label="item.value"
              >
                {{ item.info }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item label="活动标识" prop="tempFlag">
            <el-radio-group v-model="form.tempFlag" :disabled="isViewMode">
              <el-radio v-for="dict in patient_temp_flag" :key="dict.value" :label="dict.value">
                {{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="7">
          <el-form-item label="证件类别" prop="typeCode">
            <el-select
              v-model="form.typeCode"
              placeholder="证件类别"
              clearable
              :disabled="isViewMode"
            >
              <el-option
                v-for="dict in sys_idtype"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="证件号码" prop="idCard">
            <el-input v-model="form.idCard" clearable :disabled="isViewMode" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="国家编码" prop="countryCode">
            <el-input v-model="form.countryCode" clearable :disabled="isViewMode" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="7">
          <el-form-item label="联系方式" prop="phone">
            <el-input v-model="form.phone" clearable :disabled="isViewMode" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="职业" prop="prfsEnum">
            <el-select v-model="form.prfsEnum" placeholder="职业" clearable :disabled="isViewMode">
              <el-option
                v-for="item in occupationtypeList"
                :key="item.value"
                :label="item.info"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="工作单位" prop="workCompany">
            <el-input v-model="form.workCompany" clearable :disabled="isViewMode" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="7">
          <el-form-item label="联系人" prop="linkName">
            <el-input v-model="form.linkName" clearable :disabled="isViewMode" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="联系人关系" prop="linkRelationCode">
            <el-select
              v-model="form.linkRelationCode"
              placeholder="联系人关系"
              clearable
              :disabled="isViewMode"
            >
              <el-option
                v-for="item in familyrelationshiptypeList"
                :key="item.value"
                :label="item.info"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="联系人电话" prop="linkRelationCode">
            <el-input v-model="form.linkTelcom" clearable :disabled="isViewMode" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="10">
          <el-form-item label="地址选择" prop="addressSelect">
            <el-cascader
              :options="options"
              :props="{ checkStrictly: true, value: 'code', label: 'name' }"
              v-model="selectedOptions"
              @change="handleChange"
              :disabled="isViewMode"
            >
              <template #default="{ node, data }">
                <span>{{ data.name }}</span>
                <span v-if="!node.isLeaf"> ({{ data.children.length }}) </span>
              </template>
            </el-cascader>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="详细地址" prop="address">
            <el-input v-model="form.address" clearable :disabled="isViewMode" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="10">
          <el-form-item label="血型ABO" prop="bloodAbo">
            <el-select
              v-model="form.bloodAbo"
              placeholder="血型ABO"
              clearable
              :disabled="isViewMode"
            >
              <el-option
                v-for="item in bloodtypeaboList"
                :key="item.value"
                :label="item.info"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item label="血型RH" prop="bloodRh">
            <el-select v-model="form.bloodRh" placeholder="血型RH" clearable :disabled="isViewMode">
              <el-option
                v-for="item in bloodtypearhList"
                :key="item.value"
                :label="item.info"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="10">
          <el-form-item label="婚姻状态" prop="maritalStatusEnum">
            <el-select
              v-model="form.maritalStatusEnum"
              placeholder="婚姻状态"
              clearable
              :disabled="isViewMode"
            >
              <el-option
                v-for="item in maritalstatusList"
                :key="item.value"
                :label="item.info"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="10">
          <el-form-item label="死亡时间" prop="deceasedDate">
            <el-date-picker
              v-model="form.deceasedDate"
              type="datetime"
              placeholder="请选择时间"
              format="YYYY/MM/DD HH:mm:ss"
              :disabled="isViewMode"
              value-format="YYYY/MM/DD HH:mm:ss"
            />
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
  <!-- </div> -->
</template>

<script setup name="PatientAddDialog">
import pcas from 'china-division/dist/pcas-code.json';
import { addPatient, patientlLists, getOutpatientRegistrationList } from './outpatientregistration';

const router = useRouter();
const { proxy } = getCurrentInstance();
const {
  patient_gender_enum,
  sys_idtype,
  prfs_enum,
  blood_rh,
  blood_abo,
  marital_status_enum,
  patient_temp_flag,
  link_relation_code,
  nationality_code,
} = proxy.useDict(
  'patient_gender_enum',
  'sys_idtype',
  'prfs_enum',
  'blood_rh',
  'blood_abo',
  'marital_status_enum',
  'patient_temp_flag',
  'link_relation_code',
  'nationality_code'
);

const selectedOptions = ref([]); // v-model 绑定的选中值
const maritalstatusList = ref([]); //婚姻
const occupationtypeList = ref([]); //职业
const administrativegenderList = ref([]); //性别
const bloodtypeaboList = ref([]); //血型abo
const bloodtypearhList = ref([]); //血型RH
const familyrelationshiptypeList = ref([]); //家庭关系
// 使用 ref 定义查询所得用户信息数据
const patientInfo = ref(undefined);
const addressCom = ref(''); //地址

const options = ref(pcas); // 地区数据

const title = ref('添加病人');
const visible = ref(false);
const emits = defineEmits(['submit']); // 声明自定义事件

const data = reactive({
  isViewMode: false,
  form: {},
  rules: {
    name: [{ required: true, message: '姓名不能为空', trigger: 'change' }],
    genderEnum: [{ required: true, message: '请选择性别', trigger: 'change' }],
    idCard: [{ required: true, message: '证件号码不能为空', trigger: 'change' }],
    phone: [{ required: true, message: '联系方式不能为空', trigger: 'change' }],
  },
});

const { queryParams, form, rules, isViewMode } = toRefs(data);

const props = defineProps({
  item: {
    type: Object,
    required: false,
  },
});
/** 查询菜单列表 */
function getList() {
  patientlLists().then((response) => {
    console.log(response);
    occupationtypeList.value = response.data.occupationType;
    administrativegenderList.value = response.data.sex;
    bloodtypeaboList.value = response.data.bloodTypeABO;
    bloodtypearhList.value = response.data.bloodTypeRH;
    familyrelationshiptypeList.value = response.data.familyRelationshipType;
    maritalstatusList.value = response.data.maritalStatus;
  });
}

/** 打开用户信息弹窗 */
function getPatientInfo() {
  const param = {
    searchKey: form.value.idCard,
  };
  getOutpatientRegistrationList(param).then((res) => {
    console.log(param, 'param');
    if (res.data.records.length > 0) {
      patientInfo.value = res.data.records[0];
      console.log(patientInfo.value, 'patientInfo.value');
      // 将表单数据发送给父组件
      emits('submit', patientInfo.value);
    }
  });
}
//地址选择
const handleChange = () => {
  const checkedNodes = selectedOptions.value.map((code) => {
    const node = findNodeByCode(options.value, code);
    return node ? node.name : null;
  });
  form.value.addressProvince = checkedNodes[0] || '';
  form.value.addressCity = checkedNodes[1] || '';
  form.value.addressDistrict = checkedNodes[2] || '';
  form.value.addressStreet = checkedNodes[3] || '';
  form.value.address = '';
};

// 递归查找节点
const findNodeByCode = (data, code) => {
  for (const item of data) {
    if (item.code === code) return item;
    if (item.children) {
      const result = findNodeByCode(item.children, code);
      if (result) return result;
    }
  }
  return null;
};
// 显示弹框
function show() {
  // queryParams.roleId = props.roleId;
  getList();
  visible.value = true;
}

/** 表单重置 */
function reset() {
  form.value = {
    name: undefined,
    nameJson: undefined,
    menuName: undefined,
    age: undefined,
    genderEnum: undefined,
    typeCode: undefined,
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
  };
  proxy.resetForm('patientRef');
}
/** 提交按钮 */
function submitForm() {
  if (form.value.idCard) {
    form.value.birthDate =
      form.value.idCard.toString().substring(6, 10) +
      '-' +
      form.value.idCard.toString().substring(10, 12) +
      '-' +
      form.value.idCard.toString().substring(12, 14);
    console.log(form.value.birthDate, 123);
  }
  proxy.$refs['patientRef'].validate((valid) => {
    if (valid) {
      // 使用
      form.value.address = getAddress(form);
      addPatient(form.value).then((response) => {
        proxy.$modal.msgSuccess('新增成功');
        visible.value = false;
        getPatientInfo();
        reset()
      });
    }
  });
}
// 获取完整地址字符串
function getAddress(form) {
  const addressParts = [
    form.value.addressProvince,
    form.value.addressCity,
    form.value.addressDistrict,
    form.value.addressStreet,
    form.value.address,
  ];

  // 使用 reduce 方法拼接地址
  return addressParts.reduce((acc, part) => {
    return part ? acc + part : acc;
  }, '');
}
/** 取消按钮 */
function cancel() {
  visible.value = false;
  reset();
}
defineExpose({
  show,
});
</script>
<style scoped>
.el-form--inline .el-form-item {
  display: inline-flex;
  vertical-align: middle;
  margin-right: 10px !important;
}

/* 使用深度选择器 */
.custom-label-spacing :deep(.el-form-item__label) {
  line-height: 1.2; /* 调整行间距 */
  margin-bottom: 4px; /* 调整 label 和输入框之间的间距 */
}
</style>
