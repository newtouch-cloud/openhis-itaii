<template>
  <div class="app-container">
    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title" v-model="visible" width="800px" append-to-body>
      <el-form
        :model="form"
        :rules="rules"
        ref="medicationRef"
        label-width="110px"
        label-position="left"
      >
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="编号" prop="busNo">
              <el-input
                v-model="form.busNo"
                placeholder="请输入编码"
                :disabled="form.id != undefined"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="器材名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入器材名称" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="器材分类" prop="categoryEnum">
              <el-tree-select
                v-model="form.categoryEnum"
                :data="deviceCategories"
                :props="{ value: 'value', label: 'info', children: 'children' }"
                value-key="value"
                placeholder=""
                check-strictly
                disabled
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="器材种类" prop="typeCode">
              <el-select v-model="form.typeCode" placeholder="请选择" clearable>
                <el-option
                  v-for="dict in device_type_code"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="提供部门" prop="orgId">
              <el-tree-select
                v-model="form.orgId"
                :data="deptOptions"
                :props="{ value: 'id', label: 'name', children: 'children' }"
                value-key="id"
                placeholder="请选择提供部门"
                check-strictly
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="地点" prop="locationId">
              <el-tree-select
                v-model="form.locationId"
                :data="locationOptions"
                :props="{ value: 'id', label: 'name', children: 'children' }"
                value-key="id"
                placeholder="请选择地点"
                check-strictly
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="包装单位" prop="unitCode">
              <el-select v-model="form.unitCode" placeholder="请选择" clearable>
                <el-option
                  v-for="dict in unit_code"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="销售单位" prop="salesUnitCode">
              <el-select v-model="form.salesUnitCode" placeholder="请选择" clearable>
                <el-option
                  v-for="dict in unit_code"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="最小单位" prop="minUnitCode">
              <el-select v-model="form.minUnitCode" placeholder="请选择" clearable>
                <el-option
                  v-for="dict in unit_code"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="包装规格" prop="size">
              <el-input v-model="form.size" placeholder="" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="拆零比" prop="partPercent">
              <el-input v-model="form.partPercent" placeholder="" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="产品型号" prop="modelNumber">
              <el-input v-model="form.modelNumber" placeholder="" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="批准文号" prop="approvalNumber">
              <el-input v-model="form.approvalNumber" placeholder="" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="医保编码" prop="ybNo">
              <el-input v-model="form.ybNo" placeholder="" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="器材版本" prop="version">
              <el-input v-model="form.version" placeholder="" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="主要成分" prop="substanceText">
              <el-input v-model="form.substanceText" placeholder="" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="适用范围" prop="jurisdiction">
              <el-input v-model="form.jurisdiction" placeholder="" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="状态" prop="statusEnum">
              <el-select v-model="form.statusEnum" placeholder="请选择" clearable>
                <el-option
                  v-for="dict in statusFlagOptions"
                  :key="dict.value"
                  :label="dict.info"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="生产厂家" prop="manufacturerText">
              <el-input v-model="form.manufacturerText" placeholder="" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="供应商" prop="supplyId">
              <el-select
                v-model="form.supplierId"
                placeholder=""
                clearable
                style="width: 150px"
              >
                <el-option
                  v-for="supplier in supplierListOptions"
                  :key="supplier.value"
                  :label="supplier.label"
                  :value="supplier.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="过敏标记" prop="allergenFlag">
              <el-checkbox v-model="form.allergenFlag"></el-checkbox>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="医保标记" prop="ybFlag">
              <el-checkbox v-model="form.ybFlag"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="医保对码标记" prop="ybMatchFlag">
              <el-checkbox v-model="form.ybMatchFlag"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="高值器材标志" prop="hvcmFlag">
              <el-checkbox v-model="form.hvcmFlag"></el-checkbox>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="财务统计类型" prop="minimalFee">
              <el-select
                v-model="form.minimalFee"
                clearable
                :disabled="form.id != undefined"
              >
                <el-option
                  v-for="category in fin_type_code"
                  :key="category.value"
                  :label="category.label"
                  :value="category.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="医保类别" prop="ybType">
              <el-select
                v-model="form.ybType"
                placeholder="医保类别"
                clearable
                style="width: 240px"
              >
                <el-option
                  v-for="dict in yb_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="购入价" prop="purchasePrice">
              <el-input v-model="form.purchasePrice" placeholder="" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="零售价" prop="retailPrice">
              <el-input v-model="form.retailPrice" placeholder="" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="最高零售价" prop="maximumRetailPrice">
              <el-input v-model="form.maximumRetailPrice" placeholder="" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="16">
            <el-form-item label="说明" prop="description">
              <el-input
                v-model="form.description"
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

<script setup name="MedicineDialog">
import {
  editDevice,
  addDevice,
  deptTreeSelect,
  locationTreeSelect,
} from "./device";

const { proxy } = getCurrentInstance();
const { device_type_code, unit_code } = proxy.useDict(
  "device_type_code",
  "unit_code"
);

const title = ref("");
const visible = ref(false);
const emits = defineEmits(["submit"]); // 声明自定义事件
const deptOptions = ref(undefined); // 部门树选项
const locationOptions = ref(undefined); // 地点树选项
const deviceCategories = ref([]); // 器材分类
const statusFlagOptions = ref([]); // 状态标记
const supplierListOptions = ref([]); // 供应商列表

const data = reactive({
  form: {},
  rules: {
    busNo: [{ required: true, message: "编码不能为空", trigger: "blur" }],
    name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
    pyStr: [{ required: true, message: "拼音不能为空", trigger: "blur" }],
    wbStr: [{ required: true, message: "五笔拼音不能为空", trigger: "blur" }],
    categoryEnum: [
      { required: true, message: "器材分类不能为空", trigger: "blur" },
    ],
    typeCode: [
      { required: true, message: "器材种类不能为空", trigger: "blur" },
    ],
    unitCode: [
      { required: true, message: "包装单位不能为空", trigger: "blur" },
    ],
    size: [{ required: true, message: "包装规格不能为空", trigger: "blur" }],
    partPercent: [
      { required: true, message: "拆零比不能为空", trigger: "blur" },
    ],
    minUnitCode: [
      { required: true, message: "最小使用单位不能为空", trigger: "blur" },
    ],
    modelNumber: [
      { required: true, message: "产品型号不能为空", trigger: "blur" },
    ],
    hvcmFlag: [
      { required: true, message: "高值器材标志不能为空", trigger: "blur" },
    ],
    salesUnitCode: [
      { required: true, message: "销售单位不能为空", trigger: "blur" },
    ],
    approvalNumber: [
      { required: true, message: "批准文号不能为空", trigger: "blur" },
    ],
    ybFlag: [{ required: true, message: "医保标记不能为空", trigger: "blur" }],
    // ybNo: [{ required: true, message: "医保编码不能为空", trigger: "blur" }],
    ybMatchFlag: [
      { required: true, message: "医保对码标记不能为空", trigger: "blur" },
    ],
    statusEnum: [{ required: true, message: "状态不能为空", trigger: "blur" }],
    manufacturerId: [
      { required: true, message: "生产厂家不能为空", trigger: "blur" },
    ],
    supplyId: [{ required: true, message: "供应商不能为空", trigger: "blur" }],
    // description: [{ required: true, message: "说明不能为空", trigger: "blur" }],
    jurisdiction: [
      { required: true, message: "适用范围不能为空", trigger: "blur" },
    ],
    ruleId: [{ required: true, message: "执行科室不能为空", trigger: "blur" }],
    // version: [{ required: true, message: "器材版本不能为空", trigger: "blur" }],
    // substanceText: [{ required: true, message: "主要成分不能为空", trigger: "blur" }],
    allergenFlag: [
      { required: true, message: "过敏标记不能为空", trigger: "blur" },
    ],
    orgId: [{ required: true, message: "提供部门不能为空", trigger: "blur" }],
    locationId: [{ required: true, message: "地点不能为空", trigger: "blur" }],
  },
});

const { queryParams, form, rules } = toRefs(data);

const props = defineProps({
  item: {
    type: Object,
    required: false,
  },
  title: {
    type: String,
    required: false,
  },
  deviceCategories: {
    type: Object,
    required: false,
  },
  statusFlagOptions: {
    type: Object,
    required: false,
  },
  currentCategoryEnum: {
    type: String,
    required: true,
  },
});

// 显示弹框
function show() {
  reset();
  title.value = "";
  title.value = props.title;
  deviceCategories.value = props.deviceCategories;
  statusFlagOptions.value = props.statusFlagOptions;
  form.value.categoryEnum = props.currentCategoryEnum;
  console.log(props, "22222", title.value, props.deviceCategories);
  getDeptTree();
  getLocationTree();
  visible.value = true;
}
/** 查询部门下拉树结构 */
function getDeptTree() {
  deptTreeSelect().then((response) => {
    console.log(response, "response查询部门下拉树结构");
    deptOptions.value = response.data.records;
  });
}

/** 查询地点下拉树结构 */
function getLocationTree() {
  locationTreeSelect().then((response) => {
    console.log(response, "response查询部门下拉树结构");
    locationOptions.value = response.data.records;
  });
}
// 显示弹框
function edit() {
  reset();
  title.value = "";
  title.value = props.title;
  form.value = props.item;
  deviceCategories.value = props.deviceCategories;
  statusFlagOptions.value = props.statusFlagOptions;
  getDeptTree();
  getLocationTree();
  visible.value = true;
}
/** 重置操作表单 */
function reset() {
  form.value = {
    id: undefined,
    busNo: undefined, // 编码
    name: undefined, // 名称
    pyStr: undefined, // 拼音码
    wbStr: undefined, // 五笔码
    categoryEnum: undefined, // 类别
    typeCode: undefined, // 类型编码
    unitCode: undefined, // 单位编码
    size: undefined, // 规格
    partPercent: undefined, // 占比
    minUnitCode: undefined, // 最小单位编码
    modelNumber: undefined, // 型号
    hvcmFlag: undefined, // 高值器材标志
    salesUnitCode: undefined, // 销售单位编码
    approvalNumber: undefined, // 批准文号
    ybFlag: undefined, // 医保标志
    ybNo: undefined, // 医保编码
    ybMatchFlag: undefined, // 医保对码标记
    statusEnum: undefined, // 状态（包括 1：预置，2：启用，3：停用）
    manufacturerId: undefined, // 厂家编码
    supplyId: undefined, // 供应商编码
    description: undefined, // 说明
    jurisdiction: undefined, // 适用范围
    ruleId: undefined, // 执行科室
    version: undefined, // 器材版本
    substanceText: undefined, // 主要成分
    allergenFlag: undefined, // 过敏标记
    orgId: undefined, // 科室ID
    locationId: undefined, // 地点ID
    ybType: undefined, // 医保类型
    minimalFee: undefined, // 最小收费
    purchasePrice: undefined, // 购入价
    retailPrice: undefined, // 零售价
    maximumRetailPrice: undefined, // 最高零售价
  };
  proxy.resetForm("medicationRef");
}

/** 提交按钮 */
function submitForm() {
  form.value.hvcmFlag ? (form.value.hvcmFlag = 1) : (form.value.hvcmFlag = 0);
  form.value.ybFlag ? (form.value.ybFlag = 1) : (form.value.ybFlag = 0);
  form.value.ybMatchFlag
    ? (form.value.ybMatchFlag = 1)
    : (form.value.ybMatchFlag = 0);
  form.value.allergenFlag
    ? (form.value.allergenFlag = 1)
    : (form.value.allergenFlag = 0);
  console.log(form.value, "form.value");
  if (form.value.id != undefined) {
    editDevice(form.value).then((response) => {
      // 触发自定义事件，并传递数据给父组件
      emits("submit");
      proxy.$modal.msgSuccess("修改成功");
      visible.value = false;
      reset(); // 重置表单数据
    });
  } else {
    addDevice(form.value).then((response) => {
      // 触发自定义事件，并传递数据给父组件
      emits("submit");
      proxy.$modal.msgSuccess("新增成功");
      visible.value = false;
      reset(); // 重置表单数据
    });
  }
}

/** 取消按钮 */
function cancel() {
  visible.value = false;
  reset();
}
defineExpose({
  show,
  edit,
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
