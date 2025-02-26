<template>
  <div class="app-container">
    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title" v-model="visible" width="800px" append-to-body>
      <el-tabs type="border-card">
        <el-tab-pane label="基本信息">
          <el-form
            :model="form"
            :rules="rules"
            ref="diseaseRef"
            label-width="110px"
            label-position="left"
          >
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="编号" prop="conditionCode">
                  <el-input
                    v-model="form.conditionCode"
                    placeholder="请输入编码"
                    maxlength="30"
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="品名" prop="name">
                  <el-input
                    v-model="form.name"
                    placeholder="请输入药品名"
                    maxlength="30"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="拼音码(品名)" prop="name">
                  <el-input v-model="form.name" placeholder="" maxlength="30" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="规格" prop="conditionCode">
                  <el-input
                    v-model="form.conditionCode"
                    placeholder=""
                    maxlength="30"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="小规格含量" prop="name">
                  <el-input v-model="form.name" placeholder="" maxlength="30" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="商品名" prop="name">
                  <el-input v-model="form.name" placeholder="" maxlength="30" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="厂家/产地" prop="conditionCode">
                  <el-input
                    v-model="form.conditionCode"
                    placeholder=""
                    maxlength="30"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="包装单位" prop="name">
                  <el-input
                    v-model="form.name"
                    placeholder=""
                    maxlength="30"
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="最小单位" prop="name">
                  <el-input
                    v-model="form.name"
                    placeholder=""
                    maxlength="30"
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="拆零比" prop="conditionCode">
                  <el-input
                    v-model="form.conditionCode"
                    placeholder=""
                    maxlength="30"
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="零售价" prop="name">
                  <el-input
                    v-model="form.name"
                    placeholder=""
                    maxlength="30"
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="零售拆零价" prop="name">
                  <el-input
                    v-model="form.name"
                    placeholder=""
                    maxlength="30"
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="批准文号" prop="conditionCode">
                  <el-input
                    v-model="form.conditionCode"
                    placeholder=""
                    maxlength="30"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="采购单价" prop="name">
                  <el-input v-model="form.name" placeholder="" maxlength="30" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="采购拆零价" prop="name">
                  <el-input v-model="form.name" placeholder="" maxlength="30" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="精一" prop="conditionCode">
                  <el-checkbox v-model="form.status"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="皮试判别" prop="name">
                  <el-checkbox v-model="form.status"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="注射药品" prop="name">
                  <el-checkbox v-model="form.status"></el-checkbox>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="精二" prop="conditionCode">
                  <el-checkbox v-model="form.status"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="麻醉药品" prop="conditionCode">
                  <el-checkbox v-model="form.status"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="基药标志" prop="name">
                  <el-checkbox v-model="form.status"></el-checkbox>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="给药途径" prop="conditionCode">
                  <el-select
                    v-model="queryParams.status"
                    clearable
                    :disabled="form.id != undefined"
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
                <el-form-item label="用药频次" prop="name">
                  <el-select
                    v-model="queryParams.status"
                    clearable
                    :disabled="form.id != undefined"
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
                <el-form-item label="单次剂量" prop="name" inline>
                  <el-input
                    v-model="form.name"
                    placeholder=""
                    maxlength="30"
                    style="width: 49%"
                  />
                  <el-select
                    v-model="queryParams.status"
                    clearable
                    style="width: 49%"
                    :disabled="form.id != undefined"
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
                  <el-select
                    v-model="queryParams.status"
                    clearable
                    :disabled="form.id != undefined"
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
                <el-form-item label="病案结算项" prop="name">
                  <el-select
                    v-model="queryParams.status"
                    clearable
                    :disabled="form.id != undefined"
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
                <el-form-item label="剂量单位换算比" prop="name">
                  <el-input v-model="form.name" placeholder="" maxlength="30" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="确认可用标记" prop="status">
                  <el-checkbox v-model="form.status"></el-checkbox>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="其他信息">
          <el-form
            :model="form"
            :rules="rules"
            ref="diseaseRef"
            label-width="115px"
            label-position="left"
          >
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="目录类别" prop="conditionCode">
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
                <el-form-item label="医保等级" prop="name">
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
                <el-form-item label="发票项目" prop="name">
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
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item
                  label="库存预警量(最小单位)"
                  prop="conditionCode"
                  class="custom-label-spacing"
                >
                  <el-input
                    v-model="form.conditionCode"
                    placeholder=""
                    maxlength="30"
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="启用库存预警" prop="name">
                  <el-checkbox v-model="form.status"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item
                  label="取消批次号管理"
                  prop="name"
                  class="custom-label-spacing"
                >
                  <el-checkbox v-model="form.status"></el-checkbox>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="统计科室" prop="conditionCode">
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
                <el-form-item label="参与统计" prop="name">
                  <el-checkbox v-model="form.status"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="高值耗材标记" prop="name">
                  <el-input
                    v-model="form.name"
                    placeholder=""
                    maxlength="30"
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="目录限价" prop="conditionCode">
                  <el-input
                    v-model="form.conditionCode"
                    placeholder=""
                    maxlength="30"
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="限制使用范围" prop="name">
                  <el-input
                    v-model="form.name"
                    placeholder=""
                    maxlength="30"
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="	限制使用标记" prop="name">
                  <el-input
                    v-model="form.name"
                    placeholder=""
                    maxlength="30"
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="目录范围" prop="conditionCode">
                  <el-select
                    v-model="queryParams.status"
                    clearable
                    style="width: 240px"
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
                <el-form-item label="项目统计分类" prop="name">
                  <el-select
                    v-model="queryParams.status"
                    clearable
                    style="width: 240px"
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
                <el-form-item label="项目显示顺序" prop="name">
                  <el-input
                    v-model="form.name"
                    placeholder=""
                    maxlength="30"
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="国家医保码" prop="conditionCode">
                  <el-input
                    v-model="form.name"
                    placeholder=""
                    maxlength="30"
                    :disabled="form.id != undefined"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="	默认单位" prop="name">
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
