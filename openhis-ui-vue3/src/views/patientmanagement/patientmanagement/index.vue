<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryRef"
      :inline="true"
      v-show="showSearch"
    >
      <el-form-item label="病人名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入病人名称/缩写"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="病人ID" prop="busNo">
        <el-input
          v-model="queryParams.busNo"
          placeholder="请以输入病人ID"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['system:menu:add']"
          >添加病人</el-button
        >
      </el-col>
      <right-toolbar
        v-model:showSearch="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table :data="patientList" border style="width: 100%">
      <el-table-column prop="idCard" label="身份证号" width="180" />
      <el-table-column prop="busNo" label="病人ID" width="180" />
      <el-table-column prop="name" label="病人名称" width="180" />
      <el-table-column prop="genderEnum_enumText" label="性别" width="180" />
      <el-table-column
        prop="maritalStatusEnum_enumText"
        label="婚姻状况"
        width="180"
      /><!--:formatter="formatMaritalStatus"-->
      <el-table-column prop="nationalityCode" label="民族" width="180" />
      <el-table-column prop="birthDate" label="生日" width="160" />
      <el-table-column prop="phone" label="电话" width="140" />
      <el-table-column prop="bloodAbo_text" label="血型ABO" width="140" />
      <el-table-column prop="bloodRh_enumText" label="血型RH" width="140" />
      <el-table-column prop="linkName" label="联系人" width="180" />
      <el-table-column prop="linkTelcom" label="联系人电话" width="180" />
      <el-table-column
        prop="linkRelationCode_enumText"
        label="联系人关系"
        width="180"
      />
      <el-table-column prop="address" label="家庭地址" width="180" />
      <el-table-column prop="prfsEnum_enumText" label="职业" width="180" />
      <el-table-column prop="workCompany" label="工作单位" width="180" />
      <el-table-column prop="organizationName" label="登记医院" width="180" />
      <el-table-column prop="deceasedDate" label="死亡时间" width="180" />
      <el-table-column prop="createTime" label="登记时间" width="180" />
      <el-table-column
        label="操作"
        align="center"
        width="210"
        fixed="right"
        class-name="small-padding fixed-width"
      >
        <template #default="scope">
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:menu:edit']"
            >修改</el-button
          >
          <el-button
            link
            type="primary"
            icon="View"
            @click="handleSee(scope.row)"
            v-hasPermi="['system:menu:add']"
            >查看</el-button
          >
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

    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" v-model="open" width="980px" append-to-body>
      <el-form
        ref="patientRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-row>
          <el-col :span="6">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" clearable :disabled="isViewMode" />
            </el-form-item>
          </el-col>
          <el-col :span="7">
            <el-form-item label="其他姓名" prop="nameJson">
              <el-input
                v-model="form.nameJson"
                clearable
                :disabled="isViewMode"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="民族" prop="nationalityCode">
              <el-input
                v-model="form.nationalityCode"
                clearable
                :disabled="isViewMode"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
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
                <el-radio
                  v-for="item in tempFlagList"
                  :key="item.value"
                  :label="item.value"
                >
                  {{ item.info }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="7">
            <el-form-item label="证件类别" prop="idType">
              <el-select
                v-model="form.idType"
                placeholder="证件类别"
                clearable
                :disabled="isViewMode"
              >
                <el-option
                  v-for="item in idTypeList"
                  :key="item.value"
                  :label="item.info"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="证件号码" prop="idCard">
              <el-input
                v-model="form.idCard"
                clearable
                :disabled="isViewMode"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="国家编码" prop="countryCode">
              <el-input
                v-model="form.countryCode"
                clearable
                :disabled="isViewMode"
              />
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
              <el-select
                v-model="form.prfsEnum"
                placeholder="职业"
                clearable
                :disabled="isViewMode"
              >
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
              <el-input
                v-model="form.workCompany"
                clearable
                :disabled="isViewMode"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="7">
            <el-form-item label="联系人" prop="linkName">
              <el-input
                v-model="form.linkName"
                clearable
                :disabled="isViewMode"
              />
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
              <el-input
                v-model="form.linkTelcom"
                clearable
                :disabled="isViewMode"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="10">
            <el-form-item label="地址选择" prop="addressSelect">
              <!-- <RegionFullGroup v-model="form.addressSelectvalue" @change="handleChangeAddress" :disabled="isViewMode"/> -->
              <el-cascader
                :options="options"
                :props="{ checkStrictly: true, value: 'code', label: 'name' }"
                v-model="selectedOptions"
                @change="handleChange"
                :disabled="isViewMode"
              >
                <template #default="{ node, data }">
                  <span>{{ data.name }}</span>
                  <span v-if="!node.isLeaf">
                    ({{ data.children.length }})
                  </span>
                </template>
              </el-cascader>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="详细地址" prop="address">
              <el-input
                v-model="form.address"
                clearable
                :disabled="isViewMode"
              />
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
              <el-select
                v-model="form.bloodRh"
                placeholder="血型RH"
                clearable
                :disabled="isViewMode"
              >
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
  </div>
</template>

<script  setup name="patientManagement">
import pcas from "china-division/dist/pcas-code.json";
import { ref, computed } from "vue";
import { addPatient, listPatient, updatePatient, lists } from "./component/api";

const showSearch = ref(true);
const open = ref(false);
const title = ref("");
const total = ref();
const patientList = ref([]);
const maritalstatusList = ref([]); //婚姻
const occupationtypeList = ref([]); //职业
const administrativegenderList = ref([]); //性别
const bloodtypeaboList = ref([]); //血型abo
const bloodtypearhList = ref([]); //血型RH
const familyrelationshiptypeList = ref([]); //家庭关系
const addressCom = ref(""); //地址
const tempFlagList = ref([]);
const idTypeList = ref([]);

const options = ref(pcas); // 地区数据
const selectedOptions = ref([]); // v-model 绑定的选中值

const { proxy } = getCurrentInstance();

const data = reactive({
  isViewMode: false,
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    name: undefined,
    busNo: undefined,
  },
  rules: {
    name: [{ required: true, message: "姓名不能为空", trigger: "blur" }],
    idCard: [
      { required: true, message: "证件号码不能为空", trigger: "blur" },
      { min: 18, message: "证件号码不能少于18位", trigger: "blur" },
    ],
    phone: [{ required: true, message: "联系方式不能为空", trigger: "blur" }],
  },
});
const { queryParams, form, rules, isViewMode } = toRefs(data);

const handleChange = () => {
  const checkedNodes = selectedOptions.value.map((code) => {
    const node = findNodeByCode(options.value, code);
    return node ? node.name : null;
  });
  form.value.addressProvince = checkedNodes[0] || "";
  form.value.addressCity = checkedNodes[1] || "";
  form.value.addressDistrict = checkedNodes[2] || "";
  form.value.addressStreet = checkedNodes[3] || "";
  form.value.address = "";
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

/** 查询菜单列表 */
function getList() {
  listPatient(queryParams.value).then((response) => {
    patientList.value = response.data.records;
    total.value = response.data.total;
  });
  lists().then((response) => {
    console.log(response);
    occupationtypeList.value = response.data.occupationType;
    administrativegenderList.value = response.data.administrativeGender;
    bloodtypeaboList.value = response.data.bloodTypeABO;
    bloodtypearhList.value = response.data.bloodTypeRH;
    familyrelationshiptypeList.value = response.data.familyRelationshipType;
    maritalstatusList.value = response.data.maritalStatus;
    tempFlagList.value = response.data.whetherStatus;
    idTypeList.value = response.data.identityDocumentType;
  });
}

/** 表单重置 */
function reset() {
  form.value = {
    name: undefined,
    nameJson: undefined,
    menuName: undefined,
    age: undefined,
    genderEnum: 0,
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
  };
  proxy.resetForm("patientRef");
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
/** 新增按钮操作 */
function handleAdd() {
  reset();
  selectedOptions.value = [];
  isViewMode.value = false;
  open.value = true;
  title.value = "添加病人";
}
//查看按钮
function handleSee(row) {
  isViewMode.value = true;
  form.value = row;
  open.value = true;
  title.value = "查看病人";
}
/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  form.value = JSON.parse(JSON.stringify(row));
  addressCom.value =
    row.addressProvince +
    row.addressCity +
    row.addressDistrict +
    row.addressStreet +
    row.address;
  const selectedOptions1 = ref([
    row.addressProvince,
    row.addressCity,
    row.addressDistrict,
    row.addressStreet,
  ]);
  const codes = convertAddressToCodes(selectedOptions1.value);
  selectedOptions.value = codes.filter((code) => code !== null);
  isViewMode.value = false;
  open.value = true;
  title.value = "修改菜单";
}
const convertAddressToCodes = (selectedOptions1) => {
  const [provinceName, cityName, areaName, streetName] = selectedOptions1;
  const findCode = (data, name) => {
    for (const item of data) {
      if (item.name === name) {
        return item.code;
      }
      if (item.children) {
        const result = findCode(item.children, name);
        if (result) return result;
      }
    }
    return null;
  };
  const provinceCode = findCode(options.value, provinceName);
  const cityCode = findCode(options.value, cityName);
  const areaCode = findCode(options.value, areaName);
  const streetCode = findCode(options.value, streetName);
  return [provinceCode, cityCode, areaCode, streetCode];
};
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
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
  }, "");
}
/** 提交按钮 */
function submitForm() {
  proxy.$refs["patientRef"].validate((valid) => {
    if (valid) {
      if (form.value.busNo != undefined) {
        const newAddress =
          form.value.addressProvince +
          form.value.addressCity +
          form.value.addressDistrict +
          form.value.addressStreet +
          form.value.address;
        if (addressCom.value !== newAddress) {
          form.value.address = getAddress(form);
        }
        console.log("/***/*/*/*/*/*/*/*/*/",form.value)
        return;
        updatePatient(form.value).then((response) => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        form.value.address = getAddress(form);
        addPatient(form.value).then((response) => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}
getList();
</script>

<style>
</style>