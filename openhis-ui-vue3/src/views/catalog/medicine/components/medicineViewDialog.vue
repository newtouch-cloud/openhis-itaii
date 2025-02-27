<template>
  <div class="app-container">
    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title" v-model="visible" width="800px" append-to-body>
      <el-form
        :model="form"
        :rules="rules"
        ref="diseaseRef"
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
                :disabled="true"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="品名" prop="name">
              <el-input
                v-model="form.name"
                placeholder="请输入药品名"
                maxlength="30"
                :disabled="true"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="拼音码(品名)" prop="name">
              <el-input
                v-model="form.name"
                placeholder=""
                maxlength="30"
                :disabled="true"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="厂家/产地" prop="conditionCode">
              <el-select v-model="queryParams.status" clearable>
                <el-option
                  v-for="dict in sys_normal_disable"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="批准文号" prop="name">
              <el-input
                v-model="form.name"
                placeholder=""
                maxlength="30"
                :disabled="true"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="条形码" prop="name">
              <el-input v-model="form.name" placeholder="" maxlength="30" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="单位" prop="conditionCode">
              <el-select
                v-model="queryParams.status"
                clearable
                :disabled="true"
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
          <el-col :span="8">
            <el-form-item label="零售价" prop="name">
              <el-input
                v-model="form.name"
                placeholder=""
                maxlength="30"
                :disabled="true"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="规格" prop="name">
              <el-input
                v-model="form.name"
                placeholder=""
                maxlength="30"
                :disabled="true"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item
              label="最小单位"
              prop="conditionCode"
            >
              <el-select v-model="queryParams.status" clearable :disabled="true">
                <el-option
                  v-for="dict in sys_normal_disable"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="拆零比" prop="conditionCode">
              <el-input
                v-model="form.conditionCode"
                placeholder=""
                maxlength="30"
                :disabled="true"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="拆零零售价" prop="name">
              <el-input
                v-model="form.name"
                placeholder=""
                maxlength="30"
                :disabled="true"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="处方限量（最小单位）" prop="conditionCode" class="custom-label-spacing">
              <el-input
                v-model="form.conditionCode"
                placeholder=""
                maxlength="30"
                :disabled="form.id != undefined"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="皮试判别" prop="name">
              <el-checkbox v-model="form.status" :disabled="true"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="注射药品" prop="name">
              <el-checkbox v-model="form.status" :disabled="true"></el-checkbox>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="给药途径" prop="conditionCode">
              <el-select
                v-model="queryParams.status"
                clearable
                :disabled="true"
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
          <el-col :span="8">
            <el-form-item label="用药频次" prop="name" :disabled="true">
              <el-select v-model="queryParams.status" clearable :disabled="true">
                <el-option
                  v-for="dict in sys_normal_disable"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="单次剂量" prop="name" inline :disabled="true">
              <el-input
                v-model="form.name"
                placeholder=""
                maxlength="30"
                :disabled="form.id != undefined"
                style="width: 49%"
              />
              <el-select
                v-model="queryParams.status"
                clearable
                style="width: 49%"
                :disabled="true"
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
          <el-col :span="8">
            <el-form-item label="剂型" prop="conditionCode">
              <el-select v-model="queryParams.status" clearable :disabled="true">
                <el-option
                  v-for="dict in sys_normal_disable"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="结算项目" prop="name">
              <el-select
                v-model="queryParams.status"
                clearable
                :disabled="true"
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
          <el-col :span="16">
            <el-form-item label="说明" prop="status">
              <el-input
                v-model="textarea2"
                :autosize="{ minRows: 4, maxRows: 10 }"
                type="textarea"
                placeholder=""
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
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

const router = useRouter();
const { proxy } = getCurrentInstance();
const { sys_normal_disable, sys_user_sex } = proxy.useDict(
  "sys_normal_disable",
  "sys_user_sex"
);

const diseaseList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const selectedData = ref([]); // 存储选择的行数据
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const dateRange = ref([]);
const deptName = ref("");
const visible = ref(false);
const conditionDefinitionOptions = ref(undefined);
// const initPassword = ref(undefined);
// const postOptions = ref([]);
// const roleOptions = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 50,
    diseaseName: undefined, // 疾病名称
    status: undefined, // 状态（包括 1：预置，2：启用，3：停用）
  },
  rules: {
    name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
    conditionCode: [
      { required: true, message: "编码不能为空", trigger: "blur" },
    ],
  },
});

const { queryParams, form, rules } = toRefs(data);

const props = defineProps({
  item: {
    type: Object,
    required: false,
  },
});

// 显示弹框
function show() {
  // queryParams.roleId = props.roleId;
  // getList();
  visible.value = true;
}
// 显示弹框
function edit() {
  // queryParams.roleId = props.roleId;
  // getList();
  console.log(props, "22222");
  console.log(props.item);
  console.log("props.item");
  form.value = props.item;
  visible.value = true;
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
