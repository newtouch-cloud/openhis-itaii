<template>
  <div class="app-container">
    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title" v-model="visible" width="1000px" append-to-body>
      <el-tabs type="border-card">
        <el-tab-pane label="基本信息">
          <el-form
            :model="form"
            :rules="rules"
            ref="medicationRef"
            label-width="110px"
            label-position="left"
          >
            <!-- <el-row :gutter="24">
              <el-col :span="6">
                <el-form-item label="通用名称" prop="name">
                  <el-input
                    v-model="form.name"
                    placeholder=""
                    @input="generateCodes"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="拼音码" prop="pyStr">
                  <el-input v-model="form.pyStr" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="药品五笔码" prop="wbStr">
                  <el-input v-model="form.wbStr" placeholder="" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="6">
                <el-form-item label="商品名称" prop="merchandiseName">
                  <el-input v-model="form.merchandiseName" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="拼音码" prop="merchandisePyStr">
                  <el-input v-model="form.merchandisePyStr" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="商品五笔码" prop="merchandiseWbStr">
                  <el-input v-model="form.merchandiseWbStr" placeholder="" />
                </el-form-item>
              </el-col>
            </el-row> -->
            <el-row :gutter="24">
              <el-col :span="6">
                <el-form-item label="通用名称" prop="name">
                  <el-input
                    v-model="form.name"
                    placeholder=""
                    @input="generateCodes"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="商品名称" prop="merchandiseName">
                  <el-input v-model="form.merchandiseName" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="药品状态" prop="statusEnum">
                  <el-select
                    v-model="form.statusEnum"
                    clearable
                    :disabled="form.id != undefined"
                  >
                    <el-option
                      v-for="status in statusFlagOptions"
                      :key="status.value"
                      :label="status.info"
                      :value="status.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="所属科室" prop="orgId">
                  <el-input v-model="form.orgId" maxlength="11" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="6">
                <el-form-item label="系统类别" prop="category">
                  <el-select
                    v-model="form.category"
                    clearable
                    :disabled="form.id != undefined"
                  >
                    <el-option
                      v-for="category in system_categories"
                      :key="category.value"
                      :label="category.label"
                      :value="category.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="药品分类" prop="categoryCode">
                  <el-select
                    v-model="form.categoryCode"
                    clearable
                    :disabled="form.id != undefined"
                  >
                    <el-option
                      v-for="category in medicine_category"
                      :key="category.value"
                      :label="category.label"
                      :value="category.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="最小费用" prop="categoryCode">
                  <el-select
                    v-model="form.categoryCode"
                    clearable
                    :disabled="form.id != undefined"
                  >
                    <el-option
                      v-for="category in medicine_category"
                      :key="category.value"
                      :label="category.label"
                      :value="category.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="药品性质" prop="pharmacologyCategoryCode">
                  <el-select
                    v-model="form.pharmacologyCategoryCode"
                    clearable
                    :disabled="form.id != undefined"
                  >
                    <el-option
                      v-for="category in medicine_properties"
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
                <el-form-item label="规格" prop="totalVolume">
                  <el-input v-model="form.totalVolume" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="包装数量" prop="packagingQuantity">
                  <el-input
                    v-model="form.packagingQuantity"
                    placeholder=""
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="包装单位" prop="unitCode">
                  <el-select
                    v-model="form.unitCode"
                    clearable
                    :disabled="form.id != undefined"
                  >
                    <el-option
                      v-for="category in medicine_unit"
                      :key="category.value"
                      :label="category.label"
                      :value="category.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="贯标国家编码" prop="minUnitCode">
                  <el-input
                    v-model="form.minUnitCode"
                    placeholder=""
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="6">
                <el-form-item label="基本剂量" prop="dose">
                  <el-input
                    v-model="form.dose"
                    placeholder=""
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="剂量单位" prop="doseUnitCode">
                  <el-select
                    v-model="form.doseUnitCode"
                    clearable
                    :disabled="form.id != undefined"
                  >
                    <el-option
                      v-for="category in medicine_unit"
                      :key="category.value"
                      :label="category.label"
                      :value="category.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="剂型" prop="doseFormCode">
                  <el-select
                    v-model="form.doseFormCode"
                    clearable
                    :disabled="form.id != undefined"
                  >
                    <el-option
                      v-for="category in medicine_formulation"
                      :key="category.value"
                      :label="category.label"
                      :value="category.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="最小单位" prop="minUnitCode">
                  <el-select
                    v-model="form.minUnitCode"
                    clearable
                    :disabled="form.id != undefined"
                  >
                    <el-option
                      v-for="category in medicine_unit"
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
                <el-form-item label="拆分属性" prop="minUnitCode">
                  <el-select
                    v-model="form.categoryCode"
                    clearable
                    :disabled="form.id != undefined"
                  >
                    <el-option
                      v-for="category in medicine_unit"
                      :key="category.value"
                      :label="category.label"
                      :value="category.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="购入价" prop="name">
                  <el-input v-model="form.name" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="零售价" prop="name">
                  <el-input v-model="form.name" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="最高零售价" prop="name">
                  <el-input v-model="form.name" placeholder="" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="6">
                <el-form-item label="停用" prop="status">
                  <el-checkbox v-model="form.status"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="皮试判别" prop="skinTestFlag">
                  <el-checkbox v-model="form.skinTestFlag"></el-checkbox>
                </el-form-item>
              </el-col>
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
            </el-row>
            <el-row :gutter="24">
              <el-col :span="6">
                <el-form-item label="成分" prop="ingredientItem">
                  <el-input v-model="form.ingredientItem" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="批次号" prop="lotNumber">
                  <el-input v-model="form.lotNumber" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="用法" prop="methodCode">
                  <el-select
                    v-model="form.methodCode"
                    clearable
                    :disabled="form.id != undefined"
                  >
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
                  <el-select
                    v-model="form.rateCode"
                    clearable
                    :disabled="form.id != undefined"
                  >
                    <el-option
                      v-for="category in rate_code"
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
                <el-form-item label="单次最大剂量" prop="maxUnit">
                  <el-input v-model="form.maxUnit" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="药品定义" prop="definition">
                  <el-input v-model="form.definition" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="药品编号" prop="busNo">
                  <el-input v-model="form.busNo" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="适用范围" prop="domainEnum">
                  <el-select
                    v-model="form.domainEnum"
                    clearable
                    :disabled="form.id != undefined"
                  >
                    <el-option
                      v-for="category in medicine_unit"
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
                <el-form-item label="药品版本" prop="version">
                  <el-input v-model="form.version" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="英文药名" prop="nameEn">
                  <el-input v-model="form.nameEn" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="所含耗材" prop="comprisedText">
                  <el-input v-model="form.comprisedText" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="拆零比" prop="partPercent">
                  <el-input v-model="form.partPercent" placeholder="" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="6">
                <el-form-item label="剂量形式" prop="doseFrom">
                  <el-input v-model="form.doseFrom" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="批准文号" prop="approvalNumber">
                  <el-input v-model="form.approvalNumber" placeholder="" />
                </el-form-item>
              </el-col>
              <!-- <el-col :span="6">
                <el-form-item label="所含耗材" prop="comprisedText">
                  <el-input v-model="form.comprisedText" placeholder="" />
                </el-form-item>
              </el-col> -->
              <el-col :span="6">
                <el-form-item label="拆零比" prop="partPercent">
                  <el-input v-model="form.partPercent" placeholder="" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="6">
                <el-form-item label="医保对码" prop="ybMatchFlag">
                  <el-input v-model="form.ybMatchFlag" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="医保编码" prop="ybNo">
                  <el-input v-model="form.ybNo" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="生产厂家 " prop="manufacturerId">
                  <el-input v-model="form.manufacturerId" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="供应商" prop="supplyId">
                  <el-input v-model="form.supplyId" placeholder="" />
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
                <el-form-item label="抗生素" prop="restrictedFlag">
                  <el-checkbox v-model="form.restrictedFlag"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="限制使用范围" prop="restrictedScope">
                  <el-input v-model="form.restrictedScope" placeholder="" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="产品特性 " prop="characteristic">
                  <el-input v-model="form.characteristic" placeholder="" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="6">
                <el-form-item label="生效日期" prop="effectiveDate">
                  <el-date-picker
                    v-model="form.effectiveDate"
                    type="datetime"
                    value-format="YYYY-MM-DD HH:mm:ss"
                    placeholder="生效日期"
                    :default-time="defaultTime"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item
                  label="到期日期"
                  prop="expirationDate"
                  style="margin-left: 10px"
                >
                  <el-date-picker
                    v-model="form.expirationDate"
                    type="datetime"
                    value-format="YYYY-MM-DD HH:mm:ss"
                    placeholder="到期日期"
                    :default-time="defaultTime"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="抗生素信息" v-if="form.id != undefined">
          <el-form
            :model="antibioticForm"
            :rules="rules"
            ref="antibioticRef"
            label-width="115px"
            label-position="left"
          >
            <el-row :gutter="24">
              <el-col :span="20">
                <el-form-item label="抗生素分类" prop="conditionCode">
                  <el-select v-model="antibioticForm.statusEnum" clearable>
                    <el-option
                      v-for="dict in sys_normal_disable"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    />
                  </el-select>
                  <el-select
                    v-model="antibioticForm.statusEnum"
                    clearable
                    style="margin-left: 5px"
                  >
                    <el-option
                      v-for="dict in sys_normal_disable"
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
                  <el-select v-model="antibioticForm.statusEnum" clearable>
                    <el-option
                      v-for="dict in sys_normal_disable"
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
                <el-form-item label="单次剂量" prop="conditionCode">
                  <el-input
                    v-model="antibioticForm.name"
                    placeholder="输入剂量"
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
              -
              <el-col :span="5">
                <el-form-item label="" prop="conditionCode" label-width="0px">
                  <el-input
                    v-model="antibioticForm.name"
                    placeholder="输入剂量"
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="频次范围" prop="conditionCode">
                  <el-input
                    v-model="antibioticForm.name"
                    placeholder=""
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
              -
              <el-col :span="5">
                <el-form-item label="" prop="conditionCode" label-width="0px">
                  <el-input
                    v-model="antibioticForm.name"
                    placeholder=""
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6"> (小时一次) </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="DDD值" prop="name">
                  <el-input
                    v-model="antibioticForm.name"
                    placeholder=""
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="DDD单位" prop="name">
                  <el-input
                    v-model="antibioticForm.name"
                    placeholder=""
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="剂量单位" prop="conditionCode">
                  <el-input
                    v-model="antibioticForm.name"
                    placeholder=""
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6"> (单位g) </el-col>
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
// import {
//   getDiseaseList,
//   editDisease,
//   addDisease,
//   getDiseaseCategory,
//   getDiseaseOne,
// } from "./components/medicine";
import pinyin from "pinyin"; // 需要安装 pinyin 库

const router = useRouter();
const { proxy } = getCurrentInstance();
const {
  medicine_category,
  system_categories,
  medicine_properties,
  medicine_unit,
  medicine_formulation,
  medical_insurance_types,
  medicine_default_usage,
  medicine_default_frequency,
  medicine_basic_flag,
  sys_normal_disable,
  rate_code,
  method_code,
} = proxy.useDict(
  "medicine_category",
  "system_categories",
  "medicine_properties",
  "medicine_unit",
  "medicine_formulation",
  "medical_insurance_types",
  "medicine_default_usage",
  "medicine_default_frequency",
  "medicine_basic_flag",
  "sys_normal_disable",
  "rate_code",
  "method_code"
);

const title = ref("");
const visible = ref(false);
const emits = defineEmits(["submit"]); // 声明自定义事件
const statusFlagOptions = ref(undefined);
const domainEnumOptions = ref(undefined);

const data = reactive({
  form: {},
  antibioticForm: {},
  rules: {
    // busNo: [{ required: true, message: "编码不能为空", trigger: "blur" }],
    // name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
    // conditionCode: [
    //   { required: true, message: "编码不能为空", trigger: "blur" },
    // ],
  },
});

// 生成拼音码和五笔码
const generateCodes = () => {
  form.value.pyStr = pinyin(form.value.name, {
    style: pinyin.STYLE_FIRST_LETTER,
  }).join(""); // 生成拼音首字母
  console.log(form.value.pyStr, "form.pyStr", form.value.name);
  // form.wbStr = wubi(form.name).join(''); // 如果有五笔库，可以生成五笔码
};
const { queryParams, form, antibioticForm, rules } = toRefs(data);

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
});

// 显示弹框
function show() {
  // queryParams.roleId = props.roleId;
  // getList();
  reset();
  statusFlagOptions.value = props.status;
  // currentData.value.activeFlag == 1
  //   ? (currentData.value.activeFlag = true)
  //   : (currentData.value.activeFlag = false); //是否为活性
  // currentData.value.ybMatchFlag == 1
  //   ? (currentData.value.ybMatchFlag = true)
  //   : (currentData.value.ybMatchFlag = false); //医保是否对码
  // currentData.value.skinTestFlag == 1
  //   ? (currentData.value.skinTestFlag = true)
  //   : (currentData.value.skinTestFlag = false); //是否皮试
  // currentData.value.injectFlag == 1
  //   ? (currentData.value.injectFlag = true)
  //   : (currentData.value.injectFlag = false); //是否为注射药物
  // currentData.value.restrictedFlag == 1
  //   ? (currentData.value.restrictedFlag = true)
  //   : (currentData.value.restrictedFlag = false); //是否限制使用
  // currentData.value.childrenFlag == 1
  //   ? (currentData.value.childrenFlag = true)
  //   : (currentData.value.childrenFlag = false); //儿童用药标志
  // console.log(currentData.value, "currentData");
  visible.value = true;
}
// 显示弹框
function edit() {
  // queryParams.roleId = props.roleId;
  // getList();
  console.log(props, "22222");
  console.log(props.item);
  console.log("props.item");
  reset();
  form.value = props.item;
  statusFlagOptions.value = props.status;
  visible.value = true;
}
/** 重置操作表单 */
function reset() {
  form.value = {
    id: undefined,
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
    dose: undefined,
    doseUnitCode: undefined,
    doseFormCode: undefined,
    statusEnum: undefined,
    skinTestFlag: undefined,
    injectFlag: undefined,
    childrenFlag: undefined,
    ingredientItem: undefined,
    lotNumber: undefined,
    methodCode: undefined,
    maxUnit: undefined,
    busNo: undefined,
    domainEnum: undefined,
    version: undefined,
    nameEn: undefined,
    comprisedText: undefined,
    partPercent: undefined,
    ybMatchFlag: undefined,
    ybNo: undefined,
    manufacturerId: undefined,
    supplyId: undefined,
    restrictedFlag: undefined,
    restrictedScope: undefined,
    characteristic: undefined,
  };
  proxy.resetForm("medicationRef");
}
/** 提交按钮 */
function submitForm() {
  proxy.$refs["medicationRef"].validate((valid) => {
    if (valid) {
      // 将表单数据发送给父组件
      emits("submit", form.value);
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
  margin-bottom: 7px;
}
</style>
