<template>
  <div class="app-container">
    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="电子处方取药结果" v-model="visible" width="800px" append-to-body>
      <div style="width: 100%">
        <!-- <div style="margin-bottom: 5px">
          <el-form
            :model="queryParams"
            ref="queryRef"
            :inline="true"
            v-show="showSearch"
            label-width="90px"
          >
            <el-form-item label="订单时间">
              <el-date-picker
                v-model="dateRange"
                value-format="YYYY-MM-DD HH:mm:ss"
                type="datetimerange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                style="width: 400px"
              ></el-date-picker>
            </el-form-item>
            <el-form-item label="关键字" prop="searchKey">
              <el-input
                v-model="queryParams.searchKey"
                placeholder="门诊号/姓名："
                clearable
                style="width: 150px"
              />
            </el-form-item>
            <el-form-item label=" " class="search-button" style="margin-left: 10px">
              <el-button type="primary" @click="handleQuery()" :disabled="false"> 查询 </el-button>
            </el-form-item>
          </el-form>
        </div> -->
        <el-form
          :model="form"
          :rules="rules"
          ref="medresultInfoRef"
          label-width="130px"
          label-position="left"
        >
          <el-row :gutter="24">
            <el-col :span="8">
              <el-form-item label="医保处方编号" prop="hiRxno">
                <el-input v-model="form.hiRxno" placeholder="" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="医保结算时间" prop="setlTime">
                <el-input v-model="form.setlTime" placeholder="" disabled />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="24">
            <el-col :span="8">
              <el-form-item label="医保处方状态编码" prop="rxStasCodg">
                <el-input v-model="form.rxStasCodg" placeholder="" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="医保处方状态名称" prop="rxStasName">
                <el-input v-model="form.rxStasName" placeholder="" disabled />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="24">
            <el-col :span="8">
              <el-form-item label="处方使用状态编号" prop="rxUsedStasCodg">
                <el-input v-model="form.rxUsedStasCodg" placeholder="" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="处方使用状态名称" prop="rxUsedStasName">
                <el-input v-model="form.rxUsedStasName" placeholder="" disabled />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-table max-height="650" :data="medicinePickupQueryList" border>
          <el-table-column
            label="医疗目录编码"
            align="center"
            prop="medListCodg"
            width="200"
            sortable
          />
          <el-table-column label="药品通用名" align="center" prop="drugGenname" width="90" />
          <el-table-column label="药品商品名" align="center" prop="drugProdname" width="60" />
          <el-table-column label="药品剂型" align="center" prop="drugDosform" />
          <el-table-column label="药品规格" align="center" prop="drugSpec" width="130" />
          <el-table-column label="数量" align="center" prop="cnt" width="80" />
          <el-table-column label="批准文号" align="center" prop="aprvno" />
          <el-table-column label="批次号" align="center" prop="bchno" />
          <el-table-column label="生产批号" align="center" prop="manuLotnum" />
          <el-table-column label="生产厂家" align="center" prop="prdrName" />
          <el-table-column label="是否取药" align="center" prop="takeDrugFlag" />
        </el-table>
        <!-- <pagination
          v-show="total > 0"
          :total="total"
          v-model:page="queryParams.pageNo"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
        /> -->
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="MedicinePickupQueryDialog">
// import {
//   getDiagnosisTreatmentList,
//   editDiagnosisTreatment,
//   addDiagnosisTreatment,
//   deptTreeSelect,
//   locationTreeSelect,
// } from './diagnosistreatment';
import { formatDate, formatDateStr } from '@/utils/index';
const { proxy } = getCurrentInstance();
const { unit_code, yb_type, fin_type_code, activity_category_code, chrgitm_lv } = proxy.useDict(
  'unit_code',
  'yb_type',
  'fin_type_code',
  'activity_category_code',
  'chrgitm_lv'
);

const visible = ref(false);
const emits = defineEmits(['submit']); // 声明自定义事件
const medicinePickupQueryList = ref(undefined);

const data = reactive({
  form: {},
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);

const props = defineProps({
  medicinePickupQuery: {
    type: Object,
    required: false,
  },
  medicinePickupInfo: {
    type: Object,
    required: false,
  },
});

// 显示弹框
function show() {
  reset();
  medicinePickupQueryList.value =
    props.medicinePickupQuery.seltdelts > 0 ? props.medicinePickupQuery.seltdelts : [];
  form.value = props.medicinePickupQuery ? props.medicinePickupQuery : {};
  visible.value = true;
}
/** 重置操作表单 */
function reset() {
  form.value = {
    id: undefined,
    hiRxno: undefined, // 编码
    setlTime: undefined, // 名称
    rxStasCodg: undefined, // 地点
    rxStasName: undefined, // 执行科室
    rxUsedStasCodg: undefined, // 拼音码
    rxUsedStasName: undefined, // 五笔码
  };
  proxy.resetForm('medresultInfoRef');

  medicinePickupQueryList.value = [];
}

/** 取消按钮 */
function cancel() {
  visible.value = false;
  reset();
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

/* 使用深度选择器 */
.custom-label-spacing :deep(.el-form-item__label) {
  line-height: 1.2; /* 调整行间距 */
  margin-bottom: 4px; /* 调整 label 和输入框之间的间距 */
}
</style>
