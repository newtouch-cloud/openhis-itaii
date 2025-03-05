<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryRef"
      :inline="true"
      v-show="showSearch"
      label-width="90px"
    >
      <el-form-item label="服务名称：" prop="searchKey">
        <el-input
          v-model="queryParams.searchKey"
          placeholder="服务名称"
          clearable
          style="width: 240px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item
        label="是否需要预约："
        prop="appointmentRequiredFlag"
        label-width="120px"
      >
        <el-select
          v-model="queryParams.appointmentRequiredFlag"
          placeholder=""
          clearable
          style="width: 240px"
        >
          <el-option
            v-for="dict in appointmentRequiredFlagOptions"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="活动标记：" prop="activeFlag">
        <el-select
          v-model="queryParams.activeFlag"
          placeholder=""
          clearable
          style="width: 240px"
        >
          <el-option
            v-for="dict in activeFlagOptions"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['system:user:add']"
          >添加</el-button
        >
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Remove"
          :disabled="multiple"
          @click="handleClose"
          v-hasPermi="['system:user:edit']"
          >停用</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="CirclePlus"
          :disabled="multiple"
          @click="handleStart"
          v-hasPermi="['system:user:remove']"
          >启用</el-button
        >
      </el-col> -->
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['monitor:job:remove']"
          >删除</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Search"
          @click="getList"
          v-hasPermi="['system:user:import']"
          >查询</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="CircleClose"
          @click="handleClear"
          v-hasPermi="['system:user:export']"
          >清空条件</el-button
        >
      </el-col>
    </el-row>

    <el-table
      v-loading="loading"
      :data="registrationfeeList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="服务名称" align="center" key="name" prop="name" />
      <el-table-column
        label="活动标记"
        align="center"
        key="activeFlag_enumText"
        prop="activeFlag_enumText"
      />
      <el-table-column
        label="提供部门"
        align="center"
        key="offeredOrgId_dictText"
        prop="offeredOrgId_dictText"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="服务分类"
        align="center"
        key="categoryCode_dictText"
        prop="categoryCode_dictText"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="服务类型 "
        align="center"
        key="typeCode_dictText"
        prop="typeCode_dictText"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="服务专业"
        align="center"
        key="specialtyCode_dictText"
        prop="specialtyCode_dictText"
      />
      <el-table-column
        label="地点"
        align="center"
        key="locationId_dictText"
        prop="locationId_dictText"
      />
      <el-table-column
        label="说明"
        align="center"
        key="comment"
        prop="comment"
      />
      <el-table-column
        label="额外细节"
        align="center"
        key="extraDetails"
        prop="extraDetails"
      />
      <el-table-column
        label="联系方式"
        align="center"
        key="contact"
        prop="contact"
        width="120"
      />
      <el-table-column
        label="预约要求"
        align="center"
        key="appointmentRequiredFlag_enumText"
        prop="appointmentRequiredFlag_enumText"
      />
      <el-table-column
        label="名称"
        align="center"
        key="chargeName"
        prop="chargeName"
      />
      <el-table-column
        label="基础价格"
        align="center"
        key="price"
        prop="price"
      />
      <el-table-column
        label="操作"
        align="center"
        width="140"
        class-name="small-padding fixed-width"
      >
        <template #default="scope">
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:user:edit']"
            >编辑</el-button
          >
          <el-button
            link
            type="primary"
            icon="View"
            @click="handleView(scope.row)"
            v-hasPermi="['system:user:remove']"
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

    <!-- 添加或修改服务管理对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form
        :model="form"
        :rules="rules"
        ref="registrationfeeRef"
        label-width="100px"
      >
        <div class="title">服务管理</div>
        <el-row>
          <el-col :span="12">
            <el-form-item label="服务名称" prop="name">
              <el-input
                v-model="form.name"
                placeholder="请输入服务名称"
                maxlength="30"
                :disabled="form.id != undefined"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="服务分类" prop="categoryCode">
              <el-select v-model="form.categoryCode" placeholder="请选择">
                <el-option
                  v-for="dict in category_code"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="服务类型" prop="fwTypeCode">
              <el-select v-model="form.fwTypeCode" placeholder="请选择">
                <el-option
                  v-for="dict in service_type_code"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="服务专业" prop="specialtyCode">
              <el-select v-model="form.specialtyCode" placeholder="请选择">
                <el-option
                  v-for="dict in specialty_code"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="地点" prop="locationId">
              <el-tree-select
                v-model="form.locationId"
                :data="deptOptions"
                :props="{ value: 'id', label: 'name', children: 'children' }"
                value-key="id"
                placeholder="请选择地点"
                check-strictly
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="提供部门" prop="offeredOrgId">
              <el-tree-select
                v-model="form.offeredOrgId"
                :data="deptOptions"
                :props="{ value: 'id', label: 'name', children: 'children' }"
                value-key="id"
                placeholder="请选择提供部门"
                check-strictly
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="活动标记" prop="activeFlag">
              <el-select v-model="form.activeFlag" placeholder="请选择">
                <el-option
                  v-for="item in activeFlagOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="额外细节" prop="extraDetails;">
              <el-input v-model="form.extraDetails" maxlength="11" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="联系方式" prop="contact">
              <el-input v-model="form.contact" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预约要求" prop="appointmentRequiredFlag">
              <el-select
                v-model="form.appointmentRequiredFlag"
                placeholder="请选择"
              >
                <el-option
                  v-for="item in appointmentRequiredFlagOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="16">
            <el-form-item label="服务说明" prop="description">
              <el-input
                v-model="form.description"
                :autosize="{ minRows: 4, maxRows: 10 }"
                type="textarea"
                placeholder=""
              />
            </el-form-item>
          </el-col>
        </el-row>
        <div class="title">费用管理</div>
        <el-row>
          <el-col :span="12">
            <el-form-item label="名称" prop="chargeName;">
              <el-input
                v-model="form.chargeName"
                :disabled="form.id != undefined"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="基础价格" prop="price">
              <el-input v-model="form.price" :disabled="form.id != undefined" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="form.id == undefined">
          <el-col :span="12">
            <el-form-item label="收费项目标题" prop="title">
              <el-input v-model="form.title" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="医保类别" prop="ybType">
              <el-select
                v-model="form.ybType"
                placeholder="医保类别"
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
        <el-row v-if="form.id == undefined">
          <el-col :span="12">
            <el-form-item label="财务类型" prop="cwTypeCode">
              <el-select v-model="form.cwTypeCode" placeholder="请选择">
                <el-option
                  v-for="dict in financial_type_code"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24" v-if="form.id == undefined">
          <el-col :span="16">
            <el-form-item label="收费说明" prop="comment">
              <el-input
                v-model="form.comment"
                :autosize="{ minRows: 4, maxRows: 10 }"
                type="textarea"
                placeholder=""
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer v-if="title != '查看'">
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Registrationfee">
import {
  getRegistrationfeeList,
  editRegistrationfee,
  addRegistrationfee,
  getRegistrationfeeOne,
  getInit,
  deptTreeSelect,
  locationTreeSelect,
  delRegistrationfee,
} from "./components/registrationfee";

const router = useRouter();
const { proxy } = getCurrentInstance();
const {
  adm_location,
  category_code,
  service_type_code,
  specialty_code,
  med_chrgitm_type,
  financial_type_code,
} = proxy.useDict(
  "adm_location",
  "category_code",
  "service_type_code",
  "specialty_code",
  "med_chrgitm_type",
  "financial_type_code"
);

const registrationfeeList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const activeFlagOptions = ref(undefined);
const appointmentRequiredFlagOptions = ref(undefined);
const deptOptions = ref(undefined); // 部门树选项
const locationOptions = ref(undefined); // 地点树选项

// 是否停用
const statusFlagOptions = ref(undefined);

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 50,
    searchKey: undefined, // 供应商名称
    busNo: undefined, // 编码
    statusEnum: undefined, // 状态（包括 1：预置，2：启用，3：停用）
    sourceEnum: undefined, // 来源（包括 1：厂商/产地目录分类，2：自定义）
  },
  rules: {
    offeredOrgId: [
      { required: true, message: "提供部门不能为空", trigger: "blur" },
    ],
    categoryCode: [
      { required: true, message: "服务分类不能为空", trigger: "blur" },
    ],
    fwTypeCode: [
      { required: true, message: "服务类型不能为空", trigger: "blur" },
    ],
    specialtyCode: [
      { required: true, message: "服务专业不能为空", trigger: "blur" },
    ],
    locationId: [{ required: true, message: "地点不能为空", trigger: "blur" }],
    name: [{ required: true, message: "服务名称不能为空", trigger: "blur" }],
    contact: [
      { required: true, message: "联系人电话不能为空", trigger: "blur" },
    ],
    appointmentRequiredFlag: [
      { required: true, message: "预约要求不能为空", trigger: "blur" },
    ],
    activeFlag: [
      { required: true, message: "活动标识不能为空", trigger: "blur" },
    ],
    chargeName: [{ required: true, message: "名称不能为空", trigger: "blur" }],
    description: [{ required: true, message: "描述不能为空", trigger: "blur" }],
    cwTypeCode: [
      { required: true, message: "财务类别不能为空", trigger: "blur" },
    ],
    ybType: [{ required: true, message: "医保类别不能为空", trigger: "blur" }],
    price: [{ required: true, message: "基础价格不能为空", trigger: "blur" }],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 挂号收费查询下拉树结构 */
function getregistrationfeeTypeList() {
  getInit().then((response) => {
    console.log(response, "response");
    activeFlagOptions.value = response.data.activeFlagOptions; // 活动标记
    appointmentRequiredFlagOptions.value =
      response.data.appointmentRequiredFlagOptions; // 预约必填标记
  });
}

/** 查询部门下拉树结构 */
function getDeptTree() {
  deptTreeSelect().then((response) => {
    console.log(response, "response查询部门下拉树结构");

    deptOptions.value = response.data.records;
    console.log(deptOptions.value, "部门下拉树结构");
  });
}

/** 查询地点下拉树结构 */
function getLocationTree() {
  locationTreeSelect().then((response) => {
    console.log(response, "response查询部门下拉树结构");
    locationOptions.value = response.data.records;
    console.log(locationOptions.value, "部门下拉树结构");
  });
}

/** 查询挂号收费项目列表 */
function getList() {
  loading.value = true;
  // queryParams.value.statusEnum = +queryParams.value.statusEnum
  console.log(queryParams.value, "queryParams.value");
  getRegistrationfeeList(queryParams.value).then((res) => {
    loading.value = false;
    console.log(res, "res");
    registrationfeeList.value = res.data.records;
    total.value = res.data.total;
    console.log(total.value, "total.value");
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNo = 1;
  getList();
}

/** 清空条件按钮操作 */
function handleClear() {
  // 清空查询条件
  proxy.resetForm("queryRef");
  getList();
}

/** 选择条数  */
function handleSelectionChange(selection) {
  console.log(selection, "selection");
  // selectedData.value = selection.map((item) => ({ ...item })); // 存储选择的行数据
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 重置操作表单 */
function reset() {
  form.value = {
    id: undefined,
    name: undefined,
    categoryCode: undefined,
    cwTypeCode: undefined,
    fwTypeCode: undefined,
    specialtyCode: undefined,
    locationId: 1,
    offeredOrgId: undefined,
    activeFlag: undefined,
    extraDetails: undefined,
    contact: undefined,
    appointmentRequiredFlag: undefined,
    chargeName: undefined,
    price: undefined,
    description: undefined,
    ybType: undefined,
    title: undefined,
  };
  proxy.resetForm("registrationfeeRef");
}
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}
/** 新增按钮操作 */
function handleAdd() {
  reset();
  // 恢复规则
  rules.value.cwTypeCode = [
    { required: true, message: "财务类别不能为空", trigger: "blur" },
  ];
  rules.value.ybType = [
    { required: true, message: "医保类别不能为空", trigger: "blur" },
  ];
  rules.value.price = [
    { required: true, message: "基础价格不能为空", trigger: "blur" },
  ];

  rules.value.chargeName = [
    { required: true, message: "名称不能为空", trigger: "blur" },
  ];
  rules.value.description = [
    { required: true, message: "描述不能为空", trigger: "blur" },
  ];

  open.value = true;
  title.value = "新增";
}
/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  // 移除规则
  rules.value.chargeName = [];
  rules.value.description = [];
  rules.value.cwTypeCode = [];
  rules.value.ybType = [];
  rules.value.price = [];
  console.log(row, "row");
  form.value = JSON.parse(JSON.stringify(row));
  form.value.fwTypeCode = form.value.typeCode;
  open.value = true;
  title.value = "编辑";
}
/** 提交按钮 */
function submitForm() {
  proxy.$refs["registrationfeeRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != undefined) {
        // 调用转换函数
        const transformFormEditParam = transformFormEditData(form);
        console.log(transformFormEditData, "transformFormEditData");
        console.log(form.value, "editRegistrationfee", form.value.statusEnum);
        editRegistrationfee(transformFormEditParam).then((response) => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        // 调用转换函数
        const transformedData = transformFormData(form);
        console.log(transformedData, "transformedData");
        addRegistrationfee(transformedData).then((response) => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 详细按钮操作 */
function handleView(row) {
  reset();
  title.value = "查看";
  open.value = true;
  getRegistrationfeeOne(row.id).then((response) => {
    console.log(response, "responsebbbb", row.id);
    form.value = response.data;
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const delId = row.id || ids.value;
  proxy.$modal
    .confirm("是否确认删除以上数据?")
    .then(function () {
      return delRegistrationfee({ ids: delId.join(",") });
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}
// 转换insert参数函数
const transformFormData = (form) => {
  const {
    id,
    name,
    categoryCode,
    // typeCode,
    cwTypeCode,
    fwTypeCode,
    specialtyCode,
    locationId,
    offeredOrgId,
    activeFlag,
    extraDetails,
    contact,
    appointmentRequiredFlag,
    chargeName,
    price,
    description,
    ybType,
    title,
  } = form.value;

  return {
    healthcareServiceFormData: {
      id,
      activeFlag,
      offeredOrgId,
      categoryCode,
      typeCode: fwTypeCode,
      specialtyCode,
      locationId,
      name,
      contact,
      appointmentRequiredFlag,
      extraDetails,
    },
    chargeItemDefinitionFormData: {
      id,
      chargeName,
      title,
      orgId: offeredOrgId,
      description,
      typeCode: cwTypeCode,
      ybType,
      price,
    },
  };
};

// 转换insert参数函数
const transformFormEditData = (form) => {
  const {
    id,
    name,
    categoryCode,
    // typeCode,
    cwTypeCode,
    fwTypeCode,
    specialtyCode,
    locationId,
    offeredOrgId,
    activeFlag,
    extraDetails,
    contact,
    appointmentRequiredFlag,
    chargeName,
    price,
    description,
    ybType,
    title,
  } = form.value;

  return {
    healthcareServiceFormData: {
      id,
      activeFlag,
      offeredOrgId,
      categoryCode,
      typeCode: fwTypeCode,
      specialtyCode,
      locationId,
      name,
      contact,
      appointmentRequiredFlag,
      extraDetails,
    },
  };
};
getregistrationfeeTypeList();
getDeptTree();
getList();
</script>
<style scoped>
.custom-tree-node {
  display: flex;
  align-items: center;
}

.title {
  font-weight: bold;
  font-size: large;
  margin-bottom: 10px;
}
</style>