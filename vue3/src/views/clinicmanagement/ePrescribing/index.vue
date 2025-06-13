<template>
  <div class="app-container">
    <div style="width: 100%">
      <div style="margin-bottom: 5px">
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
      </div>
      <!-- <div style="margin-bottom: 5px">
        <el-button type="primary" @click="handleSave()" :disabled="false"> 取药查询 </el-button>
        <el-button type="primary" plain @click="open()" :disabled="false"> 审核查询 </el-button>
        <el-button type="default" @click="combination()" :disabled="false"> 处方查询 </el-button>
        <el-button type="danger" @click="split()" :disabled="false"> 处方撤销 </el-button>
      </div> -->
      <el-table max-height="650" ref="eprescriptionRef" :data="prescriptionList" border>
        <el-table-column label="处方号" align="center" prop="prescriptionNo" sortable width="190" />
        <el-table-column label="门诊号" align="center" prop="iptOtpNo" width="110" />
        <el-table-column label="姓名" align="center" prop="patientName" width="100" />
        <el-table-column label="身份证号" align="center" prop="certno" width="180" />
        <el-table-column label="取药状态" align="center" prop="medStatus" width="130" />
        <el-table-column label="状态" align="center" prop="statusEnum_enumText" width="80" />
        <el-table-column label="科室" align="center" prop="prscDeptName" />
        <el-table-column label="挂号日期" align="center" prop="mdtrtTime" width="190">
          <template #default="scope">
            {{ formatDate(scope.row.mdtrtTime) }}
          </template>
        </el-table-column>
        <el-table-column label="处方开立日期" align="center" prop="prscTime" width="190">
          <template #default="scope">
            {{ formatDate(scope.row.prscTime) }}
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="600"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button link type="primary" icon="View" @click="openEPrescribingDetail(scope.row)"
              >查看</el-button
            >
            <el-button
              link
              type="primary"
              icon="Plus"
              :disabled="scope.row.statusEnum != '2'"
              @click="uploadElePrescriptions(scope.row)"
              >处方上传</el-button
            >
            <el-button
              link
              type="danger"
              icon="CloseBold"
              :disabled="scope.row.statusEnum != '2'"
              @click="refusePrescription(scope.row)"
              >拒绝上传</el-button
            >
            <el-button
              link
              type="primary"
              icon="Search"
              :disabled="scope.row.statusEnum != '3'"
              @click="openPrescriptionQuery(scope.row)"
              >处方查询</el-button
            >
            <el-button
              link
              type="warning"
              icon="Switch"
              :disabled="scope.row.statusEnum != '3'"
              @click="openRevokeDialog(scope.row)"
              >处方撤销</el-button
            >
            <el-button
              link
              type="primary"
              icon="Search"
              :disabled="scope.row.statusEnum != '3'"
              @click="openMedicinePickupQuery(scope.row)"
              >取药查询</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNo"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </div>
    <prescription-query-dialog
      ref="prescriptionQueryRef"
      :prescriptionQuery="prescriptionQueryData"
      @submit="getList()"
    />
    <medicine-pickup-query-dialog
      ref="medicinePickupQueryRef"
      :medicinePickupQuery="medicinePickupQueryData"
      :medicinePickupInfo="medicinePickupInfoData"
      @submit="getList()"
    />
    <e-prescribing-detail-dialog
      ref="ePrescribingDetailRef"
      :ePrescribingDetail="eprescriptionDetailData"
      @submit="getList()"
    />
    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="处方撤销" v-model="open" width="600px" append-to-body>
      <el-form :model="form" :rules="rules" ref="removeRef" label-width="80px">
        <el-row :gutter="24">
          <el-col :span="16">
            <el-form-item label="撤销原因" prop="description">
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
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="revokePrescriptionStatus">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
        
  <script setup>
import {
  getVeriPrescriptionInfo,
  queryMedPrescription,
  queryPrescription,
  getPrescriptionDetailInfo,
  uploadPrescriptionStatus,
  refusePrescriptionStatus,
  quashPrescriptionStatus,
  preVerification,
  eleSignature,
  uploadElePrescription,
  revokePrescription,
} from './components/api';
import medicinePickupQueryDialog from './components/medicinePickupQueryDialog.vue';
import prescriptionQueryDialog from './components/prescriptionQueryDialog.vue';
import ePrescribingDetailDialog from './components/ePrescribingDetailDialog.vue';
import { getCurrentInstance } from 'vue';
import useUserStore from '@/store/modules/user';
import { dayjs, ElMessage } from 'element-plus';
import { formatDate, formatDateStr } from '@/utils/index';

const userStore = useUserStore();

const emit = defineEmits(['selectDiagnosis']);
const open = ref(false);
const total = ref(0);
const queryParams = ref({
  pageNo: 1,
  pageSize: 10,
  searchKey: undefined, // 门诊号/姓名
});
const dateRange = ref([]);
const showSearch = ref(true);
const prescriptionList = ref([]);
const form = ref({
  description: undefined,
});
const prescriptionQueryRef = ref();
const medicinePickupQueryRef = ref();
const eprescriptionRef = ref();
const ePrescribingDetailRef = ref();
// 使用 ref 定义当前电子处方
const prescriptionQueryData = ref({});
// 使用 ref 定义当前查看取药结果
const medicinePickupQueryData = ref({});
// 使用 ref 定义当前查看处方详细
const eprescriptionDetailData = ref({});
// 要撤销的处方信息
const revokePrescriptionData = ref({});
// 撤销原因
const description = ref('');

const { proxy } = getCurrentInstance();
const { method_code, unit_code, rate_code, distribution_category_code } = proxy.useDict(
  'method_code',
  'unit_code',
  'rate_code',
  'distribution_category_code'
);

getList();
function getList() {
  console.log(queryParams.value, 'queryParams.value电子处方');
  getVeriPrescriptionInfo(queryParams.value).then((res) => {
    prescriptionList.value = res.data.records;
    total.value = res.data.total;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.prscTimeSTime =
    dateRange.value && dateRange.value.length == 2 ? dateRange.value[0] : '';
  queryParams.value.prscTimeETime =
    dateRange.value && dateRange.value.length == 2 ? dateRange.value[1] : '';
  getList();
}

/** 电子处方撤销按钮 */
function openRevokeDialog(row) {
  form.value.description = '';
  open.value = true;
  revokePrescriptionData.value = row;
}

/** 电子处方撤销按钮操作 */
function revokePrescriptionStatus() {
  open.value = false;
  description.value = form.value.description;
  const practitionerId = userStore.id;
  // 撤销时间
  const revokeDate = dayjs(new Date()).format('YYYY-MM-DD HH:mm:ss');
  console.log(revokePrescriptionData.value, 'revokePrescriptionData');
  console.log(description.value, '撤销原因');
  revokePrescription(
    revokePrescriptionData.value.hiRxno,
    practitionerId,
    description.value,
    revokeDate,
    revokePrescriptionData.value.tenantId
  ).then((response) => {
    if (response.code == 200) {
      quashPrescriptionStatus(revokePrescriptionData.value.prescriptionNo, description.value).then(
        (response) => {
          if (response.code == 200) {
            console.log(response, '电子处方撤销');
            proxy.$modal.msgSuccess('电子处方撤销成功');
            getList();
          } else {
            ElMessage.error(response.msg);
          }
        }
      );
    } else {
      ElMessage.error(response.msg);
    }
  });
}

/** 打开查看弹窗 */
function openEPrescribingDetail(row) {
  console.log(row.prescriptionNo, 'row88888', row);
  getPrescriptionDetailInfo(row.prescriptionNo).then((response) => {
    console.log(response, 'response88888');
    eprescriptionDetailData.value = response.data;
    nextTick(() => {
      proxy.$refs['ePrescribingDetailRef'].show();
    });
    getList();
  });
}

/** 打开电子处方查询结果弹窗 */
function openPrescriptionQuery(row) {
  queryPrescription(row.hiRxno).then((response) => {
    console.log(response, 'response打开电子处方查询结果弹窗');

    prescriptionQueryData.value = response.data;
    nextTick(() => {
      proxy.$refs['prescriptionQueryRef'].show();
    });
    getList();
  });
}

/** 打开取药查询弹窗 */
function openMedicinePickupQuery(row) {
  queryMedPrescription(row.hiRxno).then((response) => {
    console.log(response, 'responseopenMedicinePickupQuery');

    medicinePickupQueryData.value = response.data;
    nextTick(() => {
      proxy.$refs['medicinePickupQueryRef'].show();
    });
    getList();
  });
}

/** 处方上传 */
function uploadElePrescriptions(row) {
  console.log(row, '处方上传');
  // 电子处方上传预核验
  preVerification(row.prescriptionNo, row.ecToken, row.authNo, row.tenantId).then((response) => {
    console.log(response, '电子处方上传预核验');
    let date = new Date();
    const preVerificationResult = response.data;
    if (response.code == 200) {
      const practitionerId = userStore.id;
      // 电子处方医保电子签名
      eleSignature(
        preVerificationResult.hiRxno,
        practitionerId,
        dayjs(date).format('YYYY-MM-DD HH:mm:ss'),
        row.tenantId
      ).then((response) => {
        console.log(response, '电子处方医保电子签名');
        if (response.code == 200) {
          // 电子处方上传
          uploadElePrescription(
            preVerificationResult.hiRxno,
            practitionerId,
            dayjs(date).format('YYYY-MM-DD HH:mm:ss'),
            row.tenantId
          ).then((response) => {
            if (response.code == 200) {
              console.log(response, '电子处方上传');
              // 医保电子处方状态更新（上传）
              console.log(row.prescriptionNo, '处方上传prescriptionNo');
              uploadPrescriptionStatus(row.prescriptionNo).then((response) => {
                if (response.code == 200) {
                  console.log(response, '医保电子处方状态更新（上传）');
                  proxy.$modal.msgSuccess('处方上传成功');
                  getList();
                } else {
                  proxy.$modal.msgError(response.msg);
                }
              });
            }
          });
        }
      });
    }

    // medicinePickupQueryData.value = response.data;
    // // nextTick(() => {
    // //   proxy.$refs['medicinePickupQueryRef'].show();
    // // });
    // getList();
  });
}

/** 医保电子处方拒绝上传 */
function refusePrescription(row) {
  refusePrescriptionStatus(row.prescriptionNo).then((response) => {
    if (response.code == 200) {
      console.log(response, '医保电子处方状态更新（拒绝上传）');
      proxy.$modal.msgSuccess('拒绝上传成功');
      getList();
    } else {
      ElMessage.error(response.msg);
    }
    getList();
  });
}

/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}
</script>
      
  <style lang="scss" scoped>
:deep(.el-table__expand-icon) {
  display: none !important;
}
.medicine-title {
  font-size: 16px;
  font-weight: 600;
  min-width: 280px;
  display: inline-block;
}

.total-amount {
  font-size: 16px;
  font-weight: 600;
  color: #409eff;
  white-space: nowrap;
}

.medicine-info {
  font-size: 15px;
  font-weight: 600;
  color: #606266;
  white-space: nowrap;
}

.form-group {
  display: flex;
  align-items: center;
  gap: 8px;
  background: #fff;
  padding: 6px 10px;
  border-radius: 4px;
  border: 1px solid #ebeef5;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.04);
}

/* 调整element组件默认间距 */
// .el-select,
// .el-input-number {
//   margin-right: 0 !important;
// }

.el-input-number .el-input__inner {
  text-align: center;
}
.el-table__cell .el-form-item--default {
  margin-bottom: 0px;
}
</style>