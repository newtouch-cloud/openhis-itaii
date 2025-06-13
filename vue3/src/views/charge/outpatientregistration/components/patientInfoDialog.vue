<template>
  <!-- <div class="app-container"> -->
    <el-dialog :title="title" v-model="visible" width="800px" append-to-body>
      <el-table
        :data="patientInfoList"
        @selection-change="handleSelectionChange"
        width="90%"
         @cell-dblclick="handleCellDblClick"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column
          label="患者姓名"
          align="center"
          key="name"
          prop="name"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="性别"
          align="center"
          key="genderEnum_enumText"
          prop="genderEnum_enumText"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="身份证号"
          align="center"
          key="idCard"
          prop="idCard"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="电话"
          align="center"
          key="phone"
          prop="phone"
          :show-overflow-tooltip="true"
          width="100"
        />
        <el-table-column
          label="生日"
          align="center"
          key="birthDate"
          prop="birthDate"
          :show-overflow-tooltip="true"
          width="50"
        />
        <el-table-column
          label="年龄"
          align="center"
          key="age"
          prop="age"
          :show-overflow-tooltip="true"
        />
      </el-table>
      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNo"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  <!-- </div> -->
</template>

<script setup name="PatientInfoDialog">
import {
  getOutpatientRegistrationList,
} from "./outpatientregistration";
const { proxy } = getCurrentInstance();


const patientInfoList = ref([]);
const selectedData = ref([]); // 存储选择的行数据
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("病人信息");
const visible = ref(false);
const emits =  defineEmits(['submit']); // 声明自定义事件

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    searchKey: undefined, // 品名/商品名/英文品名/编码/拼音
  },
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);

const props = defineProps({
  patientInfoData: {
    type: Object,
    required: false,
    default: () => ({}), // 提供默认值
  },
  searchInfo: {
    type: String,
    required: true,
    default: "",
  },
});

// 显示弹框
function show() {
  patientInfoList.value = props.patientInfoData.records;
  total.value = props.patientInfoData.total;
  console.log(props.patientInfoData, "props.patientInfoData");
  visible.value = true;
}

/** 查询病种目录列表 */
function getList() {
  const query = { 
    searchKey: props.searchInfo,
    pageNo: queryParams.value.pageNo,
    pageSize: queryParams.value.pageSize,
   };
  getOutpatientRegistrationList(query).then((res) => {
    if (res.data.records.length > 0) {
      patientInfoList.value = res.data.records;
      total.value = res.data.total;
      console.log(patientInfoList.value, "patientInfoList.value");
    }
  });
}

/** 选择条数  */
function handleSelectionChange(selection) {
  console.log(selection, "selection");
  selectedData.value = selection.map((item) => ({ ...item })); // 存储选择的行数据
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 取消按钮 */
function cancel() {
  visible.value = false;
  patientInfoList.value = [];
}

/** 提交按钮 */
function submitForm() {
  if (selectedData.value.length > 1) {
    proxy.$modal.msgSuccess("只能选中一条数据操作！");
  } else if (selectedData.value.length === 1) {
    console.log(selectedData.value[0], "selectedData.value");
    const data = selectedData.value[0];
    // 将表单数据发送给父组件
    emits("submit", data);
  }
  visible.value = false;
}
// 双击数据时触发的方法
function handleCellDblClick(row, column, cell, event) {
  emits("submit", row);
  visible.value = false;
      // 获取整条数据
      console.log('双击的行数据:', row);
      // 根据需求进行进一步操作
}
defineExpose({
  show,
});
</script>
<style scoped>
.el-form--inline .el-form-item {
  display: inline-flex;
  vertical-align: middle;
  margin-right: 10px !important;
}
</style>
