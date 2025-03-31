<template>
  <div class="app-container">
    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title" v-model="visible" width="1150px" append-to-body>
      <el-tabs type="border-card">
        <el-tab-pane label="基本信息">
          <el-form
            :model="form"
            :rules="rules"
            ref="medicationRef"
            label-width="110px"
            label-position="left"
          >
            <div class="title">基本信息</div>
            <el-row :gutter="24">
              <el-col :span="6" v-if="form.id != undefined">
                <el-form-item label="药品编号" prop="busNo">
                  <el-input v-model="form.busNo" placeholder="" disabled />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="通用名称" prop="name">
                  <el-input v-model="form.name" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="商品名称" prop="merchandiseName">
                  <el-input v-model="form.merchandiseName" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="医保编码" prop="ybNo">
                  <el-input v-model="form.ybNo" placeholder="" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="6">
                <el-form-item label="药品分类" prop="categoryCode">
                  <el-select v-model="form.categoryCode" clearable :disabled="form.categoryCode != ''">
                    <el-option
                      v-for="category in med_category_code"
                      :key="category.value"
                      :label="category.label"
                      :value="category.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="医保对码" prop="ybMatchFlag">
                  <el-checkbox v-model="form.ybMatchFlag" placeholder="" />
                </el-form-item>
              </el-col>
            </el-row>
            <div class="title">临床信息</div>
            <el-row :gutter="24">
              <el-col :span="6">
                <el-form-item label="药品性质" prop="pharmacologyCategoryCode">
                  <el-select v-model="form.pharmacologyCategoryCode" clearable>
                    <el-option
                      v-for="category in medicine_properties"
                      :key="category.value"
                      :label="category.label"
                      :value="category.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="规格" prop="totalVolume">
                  <el-input v-model="form.totalVolume" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="剂型" prop="doseFormCode">
                  <el-select v-model="form.doseFormCode" clearable>
                    <el-option
                      v-for="category in dose_form_code"
                      :key="category.value"
                      :label="category.label"
                      :value="category.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="剂量单位" prop="doseUnitCode">
                  <el-select v-model="form.doseUnitCode" clearable>
                    <el-option
                      v-for="category in unit_code"
                      :key="category.value"
                      :label="category.label"
                      :value="category.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="6">
                <el-form-item label="用量限定" prop="usageLimit">
                  <el-input v-model="form.usageLimit" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="用法" prop="methodCode">
                  <el-select v-model="form.methodCode" clearable>
                    <el-option
                      v-for="category in method_code"
                      :key="category.value"
                      :label="category.label"
                      :value="category.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="用药频次" prop="rateCode">
                  <el-select v-model="form.rateCode" clearable>
                    <el-option
                      v-for="category in rate_code"
                      :key="category.value"
                      :label="category.label"
                      :value="category.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="单次最大剂量" prop="maxUnit">
                  <el-input v-model="form.maxUnit" placeholder="" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="6">
                <el-form-item label="皮试判别" prop="skinTestFlag">
                  <el-checkbox v-model="form.skinTestFlag"></el-checkbox>
                </el-form-item>
              </el-col>
            </el-row>
            <div class="title">库存信息</div>
            <el-row :gutter="24">
              <!-- <el-col :span="6">
                <el-form-item label="所属科室" prop="orgId">
                  <el-tree-select
                    v-model="form.orgId"
                    :data="deptOptions"
                    :props="{
                      value: 'id',
                      label: 'name',
                      children: 'children',
                    }"
                    value-key="id"
                    placeholder="请选择提供部门"
                    check-strictly
                    clearable
                  />
                </el-form-item>
              </el-col> -->
              <el-col :span="6">
                <el-form-item label="采购入库位置" prop="locationId">
                  <el-tree-select
                    v-model="form.locationId"
                    :data="locationOptions"
                    :props="{
                      value: 'id',
                      label: 'name',
                      children: 'children',
                    }"
                    value-key="id"
                    placeholder="请选择采购入库位置"
                    check-strictly
                    clearable
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="包装单位" prop="unitCode">
                  <el-select v-model="form.unitCode" clearable>
                    <el-option
                      v-for="category in unit_code"
                      :key="category.value"
                      :label="category.label"
                      :value="category.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="最小单位" prop="minUnitCode">
                  <el-select v-model="form.minUnitCode" clearable>
                    <el-option
                      v-for="category in unit_code"
                      :key="category.value"
                      :label="category.label"
                      :value="category.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="6">
                <el-form-item label="门诊拆分属性" prop="partAttributeEnum">
                  <el-select v-model="form.partAttributeEnum" clearable>
                    <el-option
                      v-for="category in partAttributeEnumOptions"
                      :key="category.value"
                      :label="category.info"
                      :value="category.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item
                  label="住院临时医嘱拆分属性"
                  prop="thoPartAttributeEnum"
                  class="custom-label-height"
                >
                  <el-select v-model="form.thoPartAttributeEnum" clearable>
                    <el-option
                      v-for="category in tempOrderSplitPropertyOptions"
                      :key="category.value"
                      :label="category.info"
                      :value="category.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="拆零比" prop="partPercent">
                  <el-input v-model="form.partPercent" placeholder="" />
                </el-form-item>
              </el-col>
            </el-row>
            <div class="title">价格信息</div>
            <el-row :gutter="24">
              <el-col :span="6">
                <el-form-item label="购入价" prop="purchasePrice">
                  <el-input
                    v-model="form.purchasePrice"
                    placeholder=""
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="零售价" prop="retailPrice">
                  <el-input
                    v-model="form.retailPrice"
                    placeholder=""
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="最高零售价" prop="maximumRetailPrice">
                  <el-input
                    v-model="form.maximumRetailPrice"
                    placeholder=""
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <div class="title">业务信息</div>
            <el-row :gutter="24">
              <el-col :span="6">
                <el-form-item label="财务类型" prop="typeCode">
                  <el-select v-model="form.typeCode" clearable>
                    <el-option
                      v-for="category in fin_type_code"
                      :key="category.value"
                      :label="category.label"
                      :value="category.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="贯标国家编码" prop="nationalDrugCode">
                  <el-input v-model="form.nationalDrugCode" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="药品版本" prop="version">
                  <el-input v-model="form.version" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="批准文号" prop="approvalNumber">
                  <el-input v-model="form.approvalNumber" placeholder="" />
                </el-form-item>
              </el-col>
              <!-- <el-col :span="6">
                <el-form-item label="药品状态" prop="statusEnum">
                  <el-select v-model="form.statusEnum" clearable>
                    <el-option
                      v-for="status in statusFlagOptions"
                      :key="status.value"
                      :label="status.info"
                      :value="status.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col> -->
              <!-- <el-col :span="6">
                <el-form-item label="基本剂量" prop="doseUnitCode">
                  <el-input
                    v-model="form.doseUnitCode"
                    placeholder=""
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col> -->
            </el-row>
            <el-row :gutter="24">
              <el-col :span="6">
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
              <el-col :span="6">
                <el-form-item label="生产厂家 " prop="manufacturerText">
                  <el-input v-model="form.manufacturerText" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="供应商" prop="supplyId">
                  <el-select
                    v-model="form.supplyId"
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
              <el-col :span="6">
                <el-form-item label="基药标识" prop="basicFlag">
                  <el-checkbox v-model="form.basicFlag"></el-checkbox>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="6">
                <el-form-item label="注射药品" prop="injectFlag">
                  <el-checkbox v-model="form.injectFlag"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="儿童用药标志" prop="childrenFlag">
                  <el-checkbox v-model="form.childrenFlag"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="自制药标识" prop="selfFlag">
                  <el-checkbox v-model="form.selfFlag"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="是否活性" prop="activeFlag">
                  <el-checkbox v-model="form.activeFlag"></el-checkbox>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="6">
                <el-form-item label="限制使用" prop="restrictedFlag">
                  <el-checkbox v-model="form.restrictedFlag"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="限制使用范围" prop="restrictedScope">
                  <el-input v-model="form.restrictedScope" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="抗生素" prop="antibioticFlag">
                  <el-checkbox v-model="form.antibioticFlag"></el-checkbox>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>
        <!-- <el-tab-pane label="抗生素信息" v-if="form.id != undefined && form.antibioticFlag"> -->
        <el-tab-pane label="抗生素信息" v-if="form.antibioticFlag">
          <el-form
            :model="antibioticForm"
            :rules="rules"
            ref="antibioticRef"
            label-width="115px"
            label-position="left"
          >
            <el-row :gutter="24">
              <el-col :span="20">
                <el-form-item label="抗生素分类" prop="antibioticCode">
                  <el-select v-model="antibioticForm.antibioticCode" clearable>
                    <el-option
                      v-for="dict in antibiotic_type_code"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item
                  label="权限级别"
                  prop="conditionCode"
                  class="custom-label-spacing"
                >
                  <el-select v-model="antibioticForm.restrictedEnum" clearable>
                    <el-option
                      v-for="statusRestricted in statusRestrictedOptions"
                      :key="statusRestricted.value"
                      :label="statusRestricted.info"
                      :value="statusRestricted.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="单次剂量" prop="dose">
                  <el-input
                    v-model="antibioticForm.dose"
                    placeholder="输入剂量"
                  />
                </el-form-item>
              </el-col>
              -
              <el-col :span="5">
                <el-form-item label="" prop="maxUnit" label-width="0px">
                  <el-input
                    v-model="antibioticForm.maxUnit"
                    placeholder="输入剂量"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="频次范围" prop="minRateCode">
                  <el-input
                    v-model="antibioticForm.minRateCode"
                    placeholder=""
                  />
                </el-form-item>
              </el-col>
              -
              <el-col :span="5">
                <el-form-item label="" prop="maxRateCode" label-width="0px">
                  <el-input
                    v-model="antibioticForm.maxRateCode"
                    placeholder=""
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6"> (小时一次) </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="DDD值" prop="dddCode">
                  <el-select v-model="antibioticForm.dddCode" clearable>
                    <el-option
                      v-for="category in ddd_code"
                      :key="category.value"
                      :label="category.label"
                      :value="category.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="DDD单位" prop="dddUnitCode">
                  <el-select v-model="antibioticForm.dddUnitCode" clearable>
                    <el-option
                      v-for="category in unit_code"
                      :key="category.value"
                      :label="category.label"
                      :value="category.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>
      </el-tabs>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="MedicineDialog">
import { deptTreeSelect, locationTreeSelect } from "./medicine";
import moment from "moment";

const router = useRouter();
const { proxy } = getCurrentInstance();
const {
  med_category_code,
  system_categories,
  medicine_properties,
  unit_code,
  dose_form_code,
  yb_type,
  rate_code,
  method_code,
  fin_type_code,
  antibiotic_type_code,
  ddd_code,
  dose_from_code,
} = proxy.useDict(
  "med_category_code",
  "system_categories",
  "medicine_properties",
  "unit_code",
  "dose_form_code",
  "yb_type",
  "rate_code",
  "method_code",
  "fin_type_code",
  "antibiotic_type_code",
  "ddd_code",
  "dose_from_code"
);

const title = ref("");
const visible = ref(false);
const emits = defineEmits(["submit"]); // 声明自定义事件
const statusFlagOptions = ref(undefined);
const domainEnumOptions = ref(undefined);
const deptOptions = ref(undefined); // 部门树选项
const locationOptions = ref(undefined); // 地点树选项
const supplierListOptions = ref(undefined); // 供应商列表选项
const statusRestrictedOptions = ref(undefined); // 权限级别选项
const partAttributeEnumOptions = ref(undefined); // 部位属性选项
const tempOrderSplitPropertyOptions = ref(undefined); // 临时订单拆分属性选项
const data = reactive({
  form: {},
  antibioticForm: {},
  rules: {
    // statusEnum: [
    //   { required: true, message: "药品状态不能为空", trigger: "blur" },
    // ],
    // orgId: [{ required: true, message: "所属科室不能为空", trigger: "blur" }],
    locationId: [
      { required: true, message: "所在位置不能为空", trigger: "blur" },
    ],
    doseFormCode: [
      { required: true, message: "剂型不能为空", trigger: "blur" },
    ],
    totalVolume: [{ required: true, message: "规格不能为空", trigger: "blur" }],
    activeFlag: [{ required: true, message: "活性不能为空", trigger: "blur" }],
    methodCode: [{ required: true, message: "用法不能为空", trigger: "blur" }],
    rateCode: [
      { required: true, message: "用药频次不能为空", trigger: "blur" },
    ],
    dose: [{ required: true, message: "单次剂量不能为空", trigger: "blur" }],
    doseUnitCode: [
      { required: true, message: "剂量单位不能为空", trigger: "blur" },
    ],
    maxUnit: [
      { required: true, message: "单次最大剂量不能为空", trigger: "blur" },
    ],
    busNo: [{ required: true, message: "药品编号不能为空", trigger: "blur" }],
    name: [{ required: true, message: "药品名称不能为空", trigger: "blur" }],
    categoryCode: [
      { required: true, message: "药品分类不能为空", trigger: "blur" },
    ],
    merchandiseName: [
      { required: true, message: "商品名称不能为空", trigger: "blur" },
    ],
    unitCode: [
      { required: true, message: "药品单位不能为空", trigger: "blur" },
    ],
    minUnitCode: [
      { required: true, message: "最小单位不能为空", trigger: "blur" },
    ],
    ingredient: [{ required: true, message: "成分不能为空", trigger: "blur" }],
    partPercent: [
      { required: true, message: "拆零比不能为空", trigger: "blur" },
    ],
    doseFrom: [
      { required: true, message: "剂量形式不能为空", trigger: "blur" },
    ],
    approvalNumber: [
      { required: true, message: "批准文号不能为空", trigger: "blur" },
    ],
    ybMatchFlag: [
      { required: true, message: "医保对码不能为空", trigger: "blur" },
    ],
    ybNo: [{ required: true, message: "医保编码不能为空", trigger: "blur" }],
    pharmacologyCategoryCode: [
      { required: true, message: "药品性质不能为空", trigger: "blur" },
    ],
    skinTestFlag: [
      { required: true, message: "皮试不能为空", trigger: "blur" },
    ],
    injectFlag: [{ required: true, message: "注射不能为空", trigger: "blur" }],
    supplyId: [{ required: true, message: "供应商不能为空", trigger: "blur" }],
    restrictedFlag: [
      { required: true, message: "限制使用不能为空", trigger: "blur" },
    ],
    childrenFlag: [
      { required: true, message: "儿童用药不能为空", trigger: "blur" },
    ],
    restrictedScope: [
      { required: true, message: "限制使用范围不能为空", trigger: "blur" },
    ],
    nationalDrugCode: [
      { required: true, message: "贯标国家编码不能为空", trigger: "blur" },
    ],
    partAttributeEnum: [
      { required: true, message: "拆分属性不能为空", trigger: "blur" },
    ],
    thoPartAttributeEnum: [
      {
        required: true,
        message: "住院临时医嘱拆分属性不能为空",
        trigger: "blur",
      },
    ],
    basicFlag: [
      { required: true, message: "基药标识不能为空", trigger: "blur" },
    ],
    antibioticFlag: [
      { required: true, message: "抗生素不能为空", trigger: "blur" },
    ],
    selfFlag: [{ required: true, message: "自制不能为空", trigger: "blur" }],
    purchasePrice: [
      { required: true, message: "购入价不能为空", trigger: "blur" },
    ],
    retailPrice: [
      { required: true, message: "零售价不能为空", trigger: "blur" },
    ],
    maximumRetailPrice: [
      { required: true, message: "最高零售价不能为空", trigger: "blur" },
    ],
    ybType: [{ required: true, message: "医保类型不能为空", trigger: "blur" }],
    typeCode: [
      { required: true, message: "财务类型不能为空", trigger: "blur" },
    ],
  },
});

const { form, antibioticForm, rules } = toRefs(data);

const props = defineProps({
  item: {
    type: Object,
    required: false,
  },
  domainEnum: {
    type: Object,
    required: false,
  },
  status: {
    type: Object,
    required: false,
  },
  supplierListOptions: {
    type: Object,
    required: false,
  },
  statusRestrictedOptions: {
    type: Object,
    required: false,
  },
  currentCategoryEnum: {
    type: String,
    required: true,
  },
  partAttributeEnumOptions: {
    type: Object,
    required: false,
  },
  tempOrderSplitPropertyOptions: {
    type: Object,
    required: false,
  },
});

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
  });
}
// 显示弹框
function show() {
  getLocationTree();
  getDeptTree();
  reset();
  statusFlagOptions.value = props.status;
  domainEnumOptions.value = props.domainEnum;
  supplierListOptions.value = props.supplierListOptions;
  statusRestrictedOptions.value = props.statusRestrictedOptions;
  form.value.categoryCode = props.currentCategoryEnum;
  partAttributeEnumOptions.value = props.partAttributeEnumOptions;
  tempOrderSplitPropertyOptions.value = props.tempOrderSplitPropertyOptions;
  console.log(form.value.categoryCode, "form.value.categoryCode");
  visible.value = true;
}
// 显示弹框
function edit() {
  // getList();
  console.log(props, "22222");
  reset();
  getLocationTree();
  getDeptTree();
  form.value = props.item;
  if (form.value) {
    setFlag(form.value);
  }
  statusFlagOptions.value = props.status;
  domainEnumOptions.value = props.domainEnum;
  supplierListOptions.value = props.supplierListOptions;
  statusRestrictedOptions.value = props.statusRestrictedOptions;
  partAttributeEnumOptions.value = props.partAttributeEnumOptions;
  tempOrderSplitPropertyOptions.value = props.tempOrderSplitPropertyOptions;
  antibioticForm.value.antibioticCode = form.value.antibioticCode;
  antibioticForm.value.restrictedEnum = form.value.restrictedEnum;
  antibioticForm.value.dose = form.value.dose;
  antibioticForm.value.maxUnit = form.value.maxUnit;
  antibioticForm.value.minRateCode = form.value.maxRateCode;
  antibioticForm.value.maxRateCode = form.value.maxRateCode;
  antibioticForm.value.dddUnitCode = form.value.dddUnitCode;
  antibioticForm.value.dddCode = form.value.dddCode;
  visible.value = true;
}
// checkbox值转换
function setFlag(data) {
  data.activeFlag == 1 ? (data.activeFlag = true) : (data.activeFlag = false); //是否为活性
  data.ybMatchFlag == 1
    ? (data.ybMatchFlag = true)
    : (data.ybMatchFlag = false); //医保是否对码
  data.skinTestFlag == 1
    ? (data.skinTestFlag = true)
    : (data.skinTestFlag = false); //是否皮试
  data.injectFlag == 1 ? (data.injectFlag = true) : (data.injectFlag = false); //是否为注射药物
  data.restrictedFlag == 1
    ? (data.restrictedFlag = true)
    : (data.restrictedFlag = false); //是否限制使用
  data.childrenFlag == 1
    ? (data.childrenFlag = true)
    : (data.childrenFlag = false); //儿童用药标志
  data.antibioticFlag == 1
    ? (data.antibioticFlag = true)
    : (data.antibioticFlag = false); //抗生素
  data.selfFlag == 1 ? (data.selfFlag = true) : (data.selfFlag = false); //自制
  data.basicFlag == 1 ? (data.basicFlag = true) : (data.basicFlag = false); //自制
}
/** 重置操作表单 */
function reset() {
  form.value = {
    id: undefined,
    medicationDefId: undefined,
    // orgId: undefined,
    locationId: undefined,
    activeFlag: undefined,
    doseFrom: undefined,
    rateCode: undefined,
    approvalNumber: undefined,
    name: undefined,
    pyStr: undefined,
    wbStr: undefined,
    merchandiseName: undefined,
    merchandisePyStr: undefined,
    merchandiseWbStr: undefined,
    categoryCode: undefined,
    pharmacologyCategoryCode: undefined,
    totalVolume: undefined,
    unitCode: undefined,
    minUnitCode: undefined,
    doseUnitCode: undefined,
    doseFormCode: undefined,
    // statusEnum: undefined,
    skinTestFlag: undefined,
    injectFlag: undefined,
    childrenFlag: undefined,
    methodCode: undefined,
    maxUnit: undefined,
    busNo: undefined,
    domainEnum: undefined,
    version: undefined,
    partPercent: undefined,
    ybMatchFlag: undefined,
    ybNo: undefined,
    manufacturerId: undefined,
    manufacturerText: undefined,
    supplyId: undefined,
    restrictedFlag: undefined,
    restrictedScope: undefined,
    purchasePrice: undefined,
    retailPrice: undefined,
    maximumRetailPrice: undefined,
    ybType: undefined,
    typeCode: undefined,
    nationalDrugCode: undefined,
    antibioticFlag: undefined,
    selfFlag: undefined,
    // minRateCode: undefined,
    // maxRateCode: undefined,
    partAttributeEnum: undefined,
    thoPartAttributeEnum: undefined,
    usageLimit: undefined,
    basicFlag: undefined,
  };
  proxy.resetForm("medicationRef");
  antibioticForm.value = {
    antibioticCode: undefined,
    restrictedEnum: undefined,
    dose: undefined,
    maxUnit: undefined,
    minRateCode: undefined,
    maxRateCode: undefined,
    dddUnitCode: undefined,
    dddCode: undefined,
  };
  proxy.resetForm("antibioticRef");
}
/** 提交按钮 */
function submitForm() {
  form.value.activeFlag == true
    ? (form.value.activeFlag = 1)
    : (form.value.activeFlag = 0); //是否为活性
  form.value.ybMatchFlag == true
    ? (form.value.ybMatchFlag = 1)
    : (form.value.ybMatchFlag = 0); //医保是否对码
  form.value.skinTestFlag == true
    ? (form.value.skinTestFlag = 1)
    : (form.value.skinTestFlag = 0); //是否皮试
  form.value.injectFlag == true
    ? (form.value.injectFlag = 1)
    : (form.value.injectFlag = 0); //是否为注射药物
  form.value.restrictedFlag == true
    ? (form.value.restrictedFlag = 1)
    : (form.value.restrictedFlag = 0); //是否限制使用
  form.value.childrenFlag == true
    ? (form.value.childrenFlag = 1)
    : (form.value.childrenFlag = 0); //儿童用药标志
  form.value.antibioticFlag == true
    ? (form.value.antibioticFlag = 1)
    : (form.value.antibioticFlag = 0); //抗生素标志v
  form.value.basicFlag == true
    ? (form.value.basicFlag = 1)
    : (form.value.basicFlag = 0); //抗生素标志
  form.value.selfFlag == true
    ? (form.value.selfFlag = 1)
    : (form.value.selfFlag = 0); //自制标志
  form.value.status == true ? (form.value.status = 1) : (form.value.status = 0); //启用状态
  proxy.$refs["medicationRef"].validate((valid) => {
    if (valid) {
      if (form.value.activeFlag == true) {
        form.value.antibioticCode = antibioticForm.value.antibioticCode;
        form.value.restrictedEnum = antibioticForm.value.restrictedEnum;
        form.value.dose = antibioticForm.value.dose;
        form.value.maxUnit = antibioticForm.value.maxUnit;
        form.value.minRateCode = antibioticForm.value.minRateCode;
        form.value.maxRateCode = antibioticForm.value.maxRateCode;
        form.value.dddUnitCode = antibioticForm.value.dddUnitCode;
        form.value.dddCode = antibioticForm.value.dddCode;
      }
      // 将表单数据发送给父组件
      emits("submit", form.value);
      visible.value = false;
    }
  });
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
.el-form-item {
  margin-bottom: 13px;
}
::v-deep .custom-label-height .el-form-item__label {
  line-height: 20px; /* 设置 label 的行高为 15px */
}
.title {
  font-weight: bold;
  font-size: large;
  margin-bottom: 10px;
  /* background-color: #f5f7fa; */
}
</style>
