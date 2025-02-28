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
            <el-form-item label="项目名称" prop="name">
              <el-input
                v-model="form.name"
                placeholder="请输入器材名称"
                maxlength="30"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="拼音码(项目名称)" prop="pyStr" class="custom-label-spacing">
              <el-input v-model="form.pyStr" placeholder="" maxlength="30" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="五笔拼音" prop="wbStr">
              <el-input v-model="form.wbStr" placeholder="" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="目录分类" prop="categoryEnum">
              <el-input
                v-model="form.categoryEnum"
                placeholder=""
                maxlength="30"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="类型" prop="typeCode">
              <el-input v-model="form.typeCode" placeholder="" maxlength="30" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="使用单位" prop="permittedUnitCode">
              <el-input v-model="form.permittedUnitCode" placeholder="" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="医保标记" prop="ybFlag">
              <!-- <el-input v-model="form.ybFlag" placeholder="" maxlength="30" /> -->
              <el-checkbox v-model="form.ybFlag"></el-checkbox>
              <!-- <el-input v-model="form.ybFlag" placeholder="" maxlength="30" /> -->
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="医保编码" prop="conditionCode">
              <el-input v-model="form.ybNo" placeholder="" maxlength="30" />
            </el-form-item>
          </el-col>
        </el-row>
      
        <el-row :gutter="24">
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
          <el-col :span="8">
            <el-form-item label="身体部位" prop="bodySiteCode">
              <!-- <el-input v-model="form.ybFlag" placeholder="" maxlength="30" /> -->
              <el-input v-model="form.bodySiteCode" placeholder="" maxlength="30" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="所需标本" prop="specimenCode">
              <el-input v-model="form.specimenCode" placeholder="" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="规则id" prop="ruleId">
              <el-checkbox v-model="form.ruleId"></el-checkbox>
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
      <template #footer v-if="title !='查看'">
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
  getDiagnosisTreatmentList,
  editDiagnosisTreatment,
  addDiagnosisTreatment,
  getDiseaseTreatmentInit,
  getDiagnosisTreatmentOne,
} from "./diagnosisTreatment";

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
  queryParams: {
    pageNum: 1,
    pageSize: 50,
    diseaseName: undefined, // 疾病名称
    status: undefined, // 状态（包括 1：预置，2：启用，3：停用）
  },
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
  title.value = ''
  title.value = props.title
  console.log(props, "22222",title.value);
  visible.value = true;
}
// 显示弹框
function edit() {
  // queryParams.roleId = props.roleId;
  // getList();
  title.value = ''
  title.value = props.title
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
    permittedUnitCode: undefined, // 使用单位
    ybFlag: undefined, // 医保标记
    ybNo: undefined, // 医保编码
    ybMatchFlag: undefined, // 医保对码标记
    statusEnum: undefined, // 状态（包括 1：预置，2：启用，3：停用）
    bodySiteCode: undefined, // 身体部位
    specimenCode: undefined, // 所需标本
    description: undefined, // 说明
    ruleId: undefined, // 执行科室
  };
  proxy.resetForm("medicationRef");
}

/** 提交按钮 */
function submitForm() {
  if (form.value.id != undefined) {
    editDiagnosisTreatment(form.value).then((response) => {
      proxy.$modal.msgSuccess("修改成功");
      visible.value = false;
      reset(); // 重置表单数据
    });
  } else {
    addDiagnosisTreatment(form.value).then((response) => {
      // 触发自定义事件，并传递数据给父组件
      emits("submit");
      proxy.$modal.msgSuccess("新增成功");
      visible.value = false;
      reset(); // 重置表单数据
    });
  }
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
