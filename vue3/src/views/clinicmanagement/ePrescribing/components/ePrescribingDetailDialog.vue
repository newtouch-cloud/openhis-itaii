<template>
  <div class="app-container">
    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="详情" v-model="visible" width="1200px" append-to-body>
      <div style="width: 100%">
        <el-table max-height="650" :data="ePrescribingDetailList" border>
          <el-table-column
            label="处方号"
            align="center"
            prop="prescriptionNo"
            sortable
            width="100"
          />
          <el-table-column label="门诊号" align="center" prop="iptOtpNo" />
          <el-table-column label="请求数量" align="center" prop="quantity" />
          <el-table-column label="请求单位" align="center" prop="unitCode" />
          <el-table-column label="审核状态" align="center" prop="statusEnum_enumText" />
          <el-table-column label="药品名" align="center" prop="medicationName" />
          <el-table-column label="药品规格" align="center" prop="drugSpecification" />
          <el-table-column label="药品剂量" align="center" prop="medDosage" />
          <el-table-column label="药品剂量单位" align="center" prop="medDosageUnitCode" />
          <el-table-column label="使用频次" align="center" prop="medFrequency_dictText" />
          <el-table-column label="途径" align="center" prop="medRoute_dictText" />
          <el-table-column label="取药状态" align="center" prop="medStatus" />
          <el-table-column label="处方状态" align="center" prop="prescriptionStatus" />
          <el-table-column label="处方类别" align="center" prop="rxTypeCode_enumText" />
          <el-table-column label="支持用药信息" align="center" prop="supportInfo" />
          <el-table-column label="服药时间(开始)" align="center" prop="effectiveDoseStart">
            <template #default="scope">
              {{ formatDate(scope.row.effectiveDoseStart) }}
            </template>
          </el-table-column>
          <el-table-column label="服药时间(结束)" align="center" prop="effectiveDoseEnd">
            <template #default="scope">
              {{ formatDate(scope.row.effectiveDoseEnd) }}
            </template>
          </el-table-column>
          <el-table-column label="给药间隔" align="center" prop="dispenseInterval" />
          <el-table-column label="单次发药数" align="center" prop="dispensePerQuantity" />
          <el-table-column label="每次发药供应天数" align="center" prop="dispensePerDuration" />
          <el-table-column label="患者姓名" align="center" prop="patnName" />
          <el-table-column label="身份证号" align="center" prop="certno" />
          <el-table-column label="开方医生名" align="center" prop="practitionerName" />
          <el-table-column label="挂号科室" align="center" prop="mdtrtDeptName" />
          <el-table-column label="开单科室" align="center" prop="prscDeptName" />
          <el-table-column label="挂号日期" align="center" prop="mdtrtTime">
            <template #default="scope">
              {{ formatDate(scope.row.mdtrtTime) }}
            </template>
          </el-table-column>
          <el-table-column label="处方开立日期" align="center" prop="prscTime">
            <template #default="scope">
              {{ formatDate(scope.row.prscTime) }}
            </template>
          </el-table-column>
          <el-table-column label="诊断名" align="center" prop="conditionName" />
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

<script setup name="EPrescribingDetailDialog">
import { formatDate, formatDateStr } from '@/utils/index';
const { proxy } = getCurrentInstance();
const { unit_code, yb_type, fin_type_code, activity_category_code, chrgitm_lv } = proxy.useDict(
  'unit_code',
  'yb_type',
  'fin_type_code',
  'activity_category_code',
  'chrgitm_lv'
);

const title = ref('');
const visible = ref(false);
const emits = defineEmits(['submit']); // 声明自定义事件
const ePrescribingDetailList = ref(undefined);

const data = reactive({
  form: {},
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);

const props = defineProps({
  ePrescribingDetail: {
    type: Object,
    required: false,
  },
});

// 显示弹框
function show() {
  reset();
  ePrescribingDetailList.value = props.ePrescribingDetail;
  visible.value = true;
}

/** 重置操作表单 */
function reset() {
  ePrescribingDetailList.value = [];
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
