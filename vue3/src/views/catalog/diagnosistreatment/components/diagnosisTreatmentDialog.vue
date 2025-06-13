<template>
  <div class="app-container">
    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title" v-model="visible" width="800px" append-to-body>
      <el-form
        :model="form"
        :rules="rules"
        ref="diagnosisTreatmentRef"
        label-width="110px"
        label-position="left"
      >
        <div class="title">
          <el-button
            type="primary"
            plain
            @click="handleImportYb()"
            size="small"
            style="margin-left: 5px; margin-top: -10px; margin-bottom: 20px"
            >从医保目录导入</el-button
          >
        </div>
        <el-row :gutter="24">
          <el-col :span="8" v-if="form.id != undefined">
            <el-form-item label="编号" prop="busNo">
              <el-input v-model="form.busNo" placeholder="请输入编码" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="项目名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入名称" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
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
                filterable
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="地点" prop="locationId">
              <el-tree-select
                v-model="form.locationId"
                :data="locationOptions"
                :props="{
                  value: 'id',
                  label: 'name',
                  children: 'children',
                }"
                value-key="id"
                placeholder="请选择地点"
                check-strictly
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="目录分类" prop="categoryCode">
              <el-select v-model="form.categoryCode" clearable filterable :disabled="false">
                <el-option
                  v-for="category in activity_category_code"
                  :key="category.value"
                  :label="category.label"
                  :value="category.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="业务类型" prop="typeEnum">
              <el-select v-model="form.typeEnum" placeholder="" clearable filterable>
                <el-option
                  v-for="item in typeEnumOptions"
                  :key="item.value"
                  :label="item.info"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="8">
            <el-form-item label="状态" prop="statusEnum">
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
            <el-form-item label="医保编码" prop="conditionCode">
              <el-input v-model="form.ybNo" placeholder="" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="医保类别" prop="ybType">
              <el-select
                v-model="form.ybType"
                placeholder="医保类别"
                clearable
                filterable
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
          <el-col :span="8">
            <el-form-item label="身体部位" prop="bodySiteCode">
              <el-select v-model="form.bodySiteCode" clearable filterable>
                <el-option
                  v-for="category in body_site_code"
                  :key="category.value"
                  :label="category.label"
                  :value="category.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="所需标本" prop="specimenCode">
              <el-select v-model="form.specimenCode" clearable filterable>
                <el-option
                  v-for="category in specimen_code"
                  :key="category.value"
                  :label="category.label"
                  :value="category.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="医保等级" prop="chrgitmLv">
              <el-select v-model="form.chrgitmLv" placeholder="" clearable filterable>
                <el-option
                  v-for="item in chrgitm_lv"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="8">
            <el-form-item label="执行科室" prop="ruleId">
              <el-select v-model="form.ruleId" placeholder="" clearable>
                <el-option
                  v-for="item in exeOrganizations"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col> -->
          <el-col :span="8">
            <el-form-item label="使用单位" prop="permittedUnitCode">
              <el-select v-model="form.permittedUnitCode" clearable filterable>
                <el-option
                  v-for="category in unit_code"
                  :key="category.value"
                  :label="category.label"
                  :value="category.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="财务类型" prop="itemTypeCode">
              <el-select v-model="form.itemTypeCode" clearable filterable>
                <el-option
                  v-for="category in fin_type_code"
                  :key="category.value"
                  :label="category.label"
                  :value="category.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <!-- <el-col :span="8">
            <el-form-item label="购入价" prop="purchasePrice">
              <el-input
                v-model="form.purchasePrice"
                placeholder=""
                :disabled="form.id != undefined"
              />
            </el-form-item>
          </el-col> -->
          <el-col :span="8">
            <el-form-item label="零售价" prop="retailPrice">
              <el-input
                v-model="form.retailPrice"
                placeholder=""
                :disabled="false"
                @input="updatePrices"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="最高零售价" prop="maximumRetailPrice">
              <el-input v-model="form.maximumRetailPrice" placeholder="" :disabled="false" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="划价标记" prop="maximumRetailPrice">
              <el-checkbox
                v-model="form.pricing_flag"
                :true-value="1"
                :false-value="0"
              ></el-checkbox>
            </el-form-item>
          </el-col>
        </el-row>
        <div class="treatment-items-section">
          <div v-for="(item, index) in treatmentItems" :key="index" class="form-row">
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="诊疗子项">
                  <el-select v-model="item.adviceDefinitionId" placeholder="诊疗子项" filterable>
                    <el-option
                      v-for="item in diagnosisTreatmentList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="执行次数">
                  <el-input-number
                    v-model="item.childrenRequestNum"
                    controls-position="right"
                    :min="1"
                    :max="999"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8" style="display: flex; align-items: center; padding-bottom: 15px">
                <el-button @click="addItem" circle type="priamry" size="small" plain icon="Plus" />
                <el-button
                  @click="removeItem(index)"
                  circle
                  type="danger"
                  size="small"
                  plain
                  icon="Minus"
                />
              </el-col>
            </el-row>
          </div>
        </div>
        <el-row :gutter="24">
          <el-col :span="16">
            <el-form-item label="说明" prop="descriptionText">
              <el-input
                v-model="form.descriptionText"
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

<script setup name="DiagnosisTreatmentDialog">
import {
  getDiagnosisTreatmentList,
  editDiagnosisTreatment,
  addDiagnosisTreatment,
  deptTreeSelect,
  locationTreeSelect,
} from './diagnosistreatment';

const { proxy } = getCurrentInstance();
const { unit_code, med_chrgitm_type, fin_type_code, activity_category_code, chrgitm_lv } =
  proxy.useDict(
    'unit_code',
    'med_chrgitm_type',
    'fin_type_code',
    'activity_category_code',
    'chrgitm_lv'
  );

const title = ref('');
const visible = ref(false);
const emits = defineEmits(['submit']); // 声明自定义事件
const categoryCode = ref('');
const deptOptions = ref(undefined); // 部门树选项
const locationOptions = ref(undefined); // 地点树选项
const diagnosisCategoryOptions = ref(undefined);
const statusFlagOptions = ref(undefined);
const exeOrganizations = ref(undefined);
const typeEnumOptions = ref(undefined);
const diagnosisTreatmentList = ref([]);

const data = reactive({
  form: {},
  rules: {
    // busNo: [{ required: true, message: "编码不能为空", trigger: "blur" }],
    name: [{ required: true, message: '名称不能为空', trigger: 'blur' }],
    // statusEnum: [{ required: true, message: "状态不能为空", trigger: "blur" }],
    categoryCode: [{ required: true, message: '诊疗目录不能为空', trigger: 'blur' }],
    // typeEnum: [{ required: true, message: '器材种类不能为空', trigger: 'blur' }],
    permittedUnitCode: [{ required: true, message: '使用单位不能为空', trigger: 'blur' }],
    // ybFlag: [{ required: true, message: "医保标记不能为空", trigger: "blur" }],
    // ybMatchFlag: [
    //   { required: true, message: "医保对码标记不能为空", trigger: "blur" },
    // ],
    // purchasePrice: [
    //   { required: true, message: "购入价不能为空", trigger: "blur" },
    // ],
    retailPrice: [{ required: true, message: '零售价不能为空', trigger: 'blur' }],
    // maximumRetailPrice: [{ required: true, message: '最高零售价不能为空', trigger: 'blur' }],
    ybType: [{ required: true, message: '医保类型不能为空', trigger: 'blur' }],
    chrgitmLv: [{ required: true, message: '医保等级不能为空', trigger: 'blur' }],
    itemTypeCode: [{ required: true, message: '财务类型不能为空', trigger: 'blur' }],
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
  currentCategoryEnum: {
    type: String,
    required: true,
  },
  diagnosisCategoryOptions: {
    type: Object,
    required: false,
  },
  statusFlagOptions: {
    type: Object,
    required: false,
  },
  exeOrganizations: {
    type: Object,
    required: false,
  },
  typeEnumOptions: {
    type: Object,
    required: false,
  },
});

// 表单数组，初始一条记录
const treatmentItems = ref([{ adviceDefinitionId: '', childrenRequestNum: 1 }]);

// 添加表单项
function addItem() {
  treatmentItems.value.push({ adviceDefinitionId: '', childrenRequestNum: 1 });
}

// 删除表单项
function removeItem(index) {
  if (treatmentItems.value.length > 1) {
    treatmentItems.value.splice(index, 1);
  }
}

function handleImportYb() {
  emits('ybDialog');
}

// 显示弹框
function show() {
  reset();
  getLocationTree();
  getItemList();
  getDeptTree();
  title.value = '';
  title.value = props.title;
  diagnosisCategoryOptions.value = props.diagnosisCategoryOptions;
  statusFlagOptions.value = props.statusFlagOptions;
  exeOrganizations.value = props.exeOrganizations;
  typeEnumOptions.value = props.typeEnumOptions;
  form.value.categoryCode = props.currentCategoryEnum;
  visible.value = true;
}

function setValue(row) {
  form.value = {
    name: formatValue(row.medicalServiceName), //医疗服务项目名称
    ybNo: formatValue(row.medicalCatalogCode), // 医保编码
    categoryCode: props.currentCategoryEnum,
    // chrgitmLv: formatValue(
    //   row.insuranceClass == '甲' ? '1' : row.insuranceClass == '乙' ? '2' : '3'
    // ), // 医保等级
  };
}

// 显示弹框
function edit() {
  reset();
  getLocationTree();
  getItemList();
  getDeptTree();
  title.value = '';
  title.value = props.title;
  form.value = props.item;
  form.value.chrgitmLv = form.value.chrgitmLv ? form.value.chrgitmLv.toString() : undefined;
  treatmentItems.value = props.item.childrenJson
    ? JSON.parse(props.item.childrenJson)
    : [{ adviceDefinitionId: '', childrenRequestNum: 1 }];
  form.value.permittedUnitCode = form.value.permittedUnitCode
    ? form.value.permittedUnitCode.toString()
    : undefined;
  diagnosisCategoryOptions.value = props.diagnosisCategoryOptions;
  statusFlagOptions.value = props.statusFlagOptions;
  exeOrganizations.value = props.exeOrganizations;
  typeEnumOptions.value = props.typeEnumOptions;
  visible.value = true;
}
/** 重置操作表单 */
function reset() {
  form.value = {
    id: undefined,
    busNo: undefined, // 编码
    name: undefined, // 名称
    locationId: undefined, // 地点
    orgId: undefined, // 执行科室
    pyStr: undefined, // 拼音码
    wbStr: undefined, // 五笔码
    categoryCode: undefined, // 类别
    typeEnum: undefined, // 类型编码
    // statusEnum: undefined, // 状态（包括 1：预置，2：启用，3：停用）
    ybFlag: undefined, // 医保标记
    ybMatchFlag: undefined, // 医保对码标记
    ybNo: undefined, // 医保编码
    ybType: undefined, // 医保类型
    bodySiteCode: undefined, // 身体部位
    specimenCode: undefined, // 所需标本
    ruleId: undefined, // 执行科室
    permittedUnitCode: undefined, // 使用单位
    itemTypeCode: undefined, // 最小收费
    // purchasePrice: undefined, // 购入价
    retailPrice: undefined, // 零售价
    maximumRetailPrice: undefined, // 最高零售价
    descriptionText: undefined, // 说明
    chrgitmLv: undefined, //医保等级
  };
  treatmentItems.value = [{ adviceDefinitionId: '', childrenRequestNum: 1 }];
  proxy.resetForm('diagnosisTreatmentRef');
}

/** 提交按钮 */
function submitForm() {
  form.value.ybFlag ? (form.value.ybFlag = 1) : (form.value.ybFlag = 0);
  form.value.ybMatchFlag ? (form.value.ybMatchFlag = 1) : (form.value.ybMatchFlag = 0);
  form.value.ruleId ? (form.value.ruleId = 1) : (form.value.ruleId = 0);
  form.value.childrenJson =
    treatmentItems.value.length > 0 && treatmentItems.value[0].adviceDefinitionId != ''
      ? JSON.stringify(treatmentItems.value)
      : undefined;
  proxy.$refs['diagnosisTreatmentRef'].validate((valid) => {
    if (valid) {
      if (form.value.id != undefined) {
        editDiagnosisTreatment(form.value).then((response) => {
          // 触发自定义事件，并传递数据给父组件
          emits('submit');
          proxy.$modal.msgSuccess('修改成功');
          visible.value = false;
          reset(); // 重置表单数据
        });
      } else {
        addDiagnosisTreatment(form.value).then((response) => {
          // 触发自定义事件，并传递数据给父组件
          emits('submit');
          proxy.$modal.msgSuccess('新增成功');
          visible.value = false;
          reset(); // 重置表单数据
        });
      }
    }
  });
}
/** 查询部门下拉树结构 */
function getDeptTree() {
  deptTreeSelect().then((response) => {
    console.log(response, 'response查询部门下拉树结构');
    deptOptions.value = response.data.records;
  });
}
/** 查询地点下拉树结构 */
function getLocationTree() {
  locationTreeSelect().then((response) => {
    console.log(response, 'response查询部门下拉树结构');
    locationOptions.value = response.data.records;
  });
}

/** 取消按钮 */
function cancel() {
  visible.value = false;
  reset();
}

// 获取诊疗子项列表，只筛出无子项的数据
function getItemList() {
  getDiagnosisTreatmentList({ statusEnum: 2, pageSize: 1000, pageNo: 1 }).then((res) => {
    diagnosisTreatmentList.value = res.data.records.filter((item) => {
      return item.childrenJson == null;
    });
  });
}

function formatValue(str) {
  if (str === null || str === undefined || str === '' || str === 'null') {
    return undefined;
  }
  return str;
}
// 在这里可以根据购入价来更新零售价
function updatePrices(value) {
  form.value.maximumRetailPrice = form.value.retailPrice;
}

defineExpose({
  show,
  edit,
  setValue,
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
