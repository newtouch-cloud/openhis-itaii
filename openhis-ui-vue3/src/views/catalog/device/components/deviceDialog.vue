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
                maxlength="30"
                :disabled="form.id != undefined"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="器材名称" prop="name">
              <el-input
                v-model="form.name"
                placeholder="请输入器材名称"
                maxlength="30"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="拼音码(器材名称)"
              prop="pyStr"
              class="custom-label-spacing"
            >
              <el-input v-model="form.pyStr" placeholder="" maxlength="30" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="器材五笔拼音" prop="wbStr">
              <el-input v-model="form.wbStr" placeholder="" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="器材分类" prop="categoryEnum">
              <el-input
                v-model="form.categoryEnum"
                placeholder=""
                maxlength="30"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="器材种类" prop="typeCode">
              <el-input v-model="form.typeCode" placeholder="" maxlength="30" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="包装单位" prop="unitCode">
              <el-input v-model="form.unitCode" placeholder="" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="包装规格" prop="size">
              <el-input
                v-model="form.size"
                placeholder=""
                maxlength="30"
                :disabled="form.id != undefined"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="最小单位" prop="minUnitCode">
              <el-input
                v-model="form.minUnitCode"
                placeholder=""
                maxlength="30"
                :disabled="form.id != undefined"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="拆零比" prop="partPercent">
              <el-input
                v-model="form.partPercent"
                placeholder=""
                maxlength="30"
                :disabled="form.id != undefined"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="产品型号" prop="modelNumber">
              <el-input
                v-model="form.modelNumber"
                placeholder=""
                maxlength="30"
                :disabled="form.id != undefined"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="高值器材标志" prop="hvcmFlag">
              <!-- <el-input
                v-model="form.hvcmFlag"
                placeholder=""
                maxlength="30"
                :disabled="form.id != undefined"
              /> -->
              <el-checkbox v-model="form.hvcmFlag"></el-checkbox>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="销售单位" prop="salesUnitCode">
              <el-input
                v-model="form.salesUnitCode"
                placeholder=""
                maxlength="30"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="批准文号" prop="approvalNumber">
              <el-input
                v-model="form.approvalNumber"
                placeholder=""
                maxlength="30"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="医保标记" prop="ybFlag">
              <!-- <el-input v-model="form.ybFlag" placeholder="" maxlength="30" /> -->
              <el-checkbox v-model="form.ybFlag"></el-checkbox>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="医保编码" prop="ybNo">
              <el-input v-model="form.ybNo" placeholder="" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="医保对码标记" prop="ybMatchFlag">
              <el-checkbox v-model="form.ybMatchFlag"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="状态" prop="statusEnum">
              <el-input
                v-model="form.statusEnum"
                placeholder=""
                maxlength="30"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="生产厂家" prop="manufacturerId">
              <el-input
                v-model="form.manufacturerId"
                placeholder=""
                maxlength="30"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="供应商" prop="supplyId">
              <el-input v-model="form.supplyId" placeholder="" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="适用范围" prop="jurisdiction">
              <el-input
                v-model="form.jurisdiction"
                placeholder=""
                maxlength="30"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="执行科室" prop="ruleId">
              <el-input v-model="form.ruleId" placeholder="" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="器材版本" prop="version">
              <el-input v-model="form.version" placeholder="" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="主要成分" prop="substanceText">
              <el-input
                v-model="form.substanceText"
                placeholder=""
                maxlength="30"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="过敏标记" prop="allergenFlag">
              <el-checkbox v-model="form.allergenFlag"></el-checkbox>
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
  getDeviceList,
  editDevice,
  addDevice,
  getDiseaseTreatmentInit,
  getDeviceOne,
} from "./device";

const router = useRouter();
const { proxy } = getCurrentInstance();
const { sys_normal_disable, sys_user_sex } = proxy.useDict(
  "sys_normal_disable",
  "sys_user_sex"
);

const title = ref("");
const visible = ref(false);
const emits = defineEmits(["submit"]); // 声明自定义事件

const data = reactive({
  form: {},
  rules: {
    // busNo: [{ required: true, message: "编码不能为空", trigger: "blur" }],
    // name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
    // conditionCode: [
    //   { required: true, message: "编码不能为空", trigger: "blur" },
    // ],
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
});

// 显示弹框
function show() {
  // queryParams.roleId = props.roleId;
  // getList();
  title.value = "";
  title.value = props.title;
  console.log(props, "22222", title.value);
  visible.value = true;
}
// 显示弹框
function edit() {
  // queryParams.roleId = props.roleId;
  // getList();
  title.value = "";
  title.value = props.title;
  form.value = props.item;
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
  };
  proxy.resetForm("medicationRef");
}

/** 提交按钮 */
function submitForm() {
  if (form.value.id != undefined) {
    // form.value.status
    //   ? (form.value.statusEnum = "3")
    //   : (form.value.statusEnum = "2");
    // console.log(form.value, "editDevice", form.value.statusEnum);
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
  open.value = false;
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
