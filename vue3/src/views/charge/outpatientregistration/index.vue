<template>
  <div class="app-container" v-loading="readCardLoading" :element-loading-text="loadingText">
    <el-row>
      <el-col :span="24" class="card-box">
        <el-card>
          <template #header> <span style="vertical-align: middle">门诊挂号</span></template>
          <el-form :model="form" :rules="rules" ref="outpatientRegistrationRef" label-width="110px">
            <el-row :gutter="24">
              <el-col :span="5">
                <el-form-item label="病历号/姓名：" prop="searchKey">
                  <el-popover
                    :popper-style="{ padding: '0' }"
                    placement="bottom-start"
                    :visible="showPopover"
                    trigger="manual"
                    :width="1200"
                  >
                    <patientList :searchkey="patientSearchKey" @selsectPatient="selsectPatient" />
                    <template #reference>
                      <el-input
                        @focus="handleFocus"
                        @blur="handleBlur"
                        @input="handleSearchPatient"
                        v-model="form.searchKey"
                        placeholder="请输入姓名/拼音/身份证"
                      />
                    </template>
                  </el-popover>
                </el-form-item>
              </el-col>
              <el-col :span="6" style="padding: 0">
                <el-button type="primary" icon="Plus" @click="handleAddPatient" style="width: 65px"
                  >新建</el-button
                >
                <el-button
                  type="primary"
                  plain
                  icon="Search"
                  @click="handleSearch"
                  style="width: 65px"
                  >查询</el-button
                >
                <el-button type="primary" plain @click="handleReadCard('01')" style="width: 65px">
                  电子凭证
                </el-button>
                <el-button
                  type="primary"
                  plain
                  @click="handleReadCard('02')"
                  style="width: 65px"
                  :disabled="true"
                >
                  身份证
                </el-button>
                <el-button type="primary" plain @click="handleReadCard('03')" style="width: 65px">
                  医保卡
                </el-button>
                <el-button
                  type="primary"
                  plain
                  @click="handleReadCard('99')"
                  style="width: 65px"
                  :disabled="true"
                >
                  学生卡
                </el-button>
              </el-col>
              <!-- <el-col :span="2" class="icon-select-container"> -->
              <!-- <el-icon><Postcard /></el-icon>
                <el-form-item prop="" label-width="0px">
                  <el-select
                    v-model="form.type"
                    placeholder="医保卡"
                    clearable
                    :disabled="true"
                    width="240px"
                  >
                    <el-option
                      v-for="item in contractList"
                      :key="item.busNo"
                      :label="item.contractName"
                      :value="item.busNo"
                    />
                  </el-select>
                </el-form-item> -->
              <!-- </el-col> -->
              <!-- <el-col :span="1"> -->
              <!-- <el-form-item prop="allergenFlag" label-width="0px">
                  <el-checkbox
                    v-model="form.allergenFlag"
                    label="(终端)扫码"
                    :style="{ color: 'orange' }"
                  ></el-checkbox>
                </el-form-item> -->
              <!-- </el-col> -->
              <el-col :span="5">
                <el-form-item label="姓名：" prop="name">
                  <el-input v-model="form.name" placeholder="姓名" :disabled="true" />
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item
                  label="性别："
                  prop="genderEnum_enumText"
                  class="custom-label-spacing"
                >
                  <el-input v-model="form.genderEnum_enumText" placeholder="" :disabled="true" />
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item label="年龄：" prop="age" class="custom-label-spacing">
                  <el-input v-model="form.age" placeholder="" :disabled="true" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="5">
                <el-form-item label="卡号：" prop="card">
                  <el-input v-model="form.card" placeholder="" :disabled="true" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="证件号：" prop="idCard">
                  <el-input v-model="form.idCard" placeholder="" :disabled="true" />
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item
                  label="初复诊："
                  prop="firstEnum_enumText"
                  class="custom-label-spacing"
                >
                  <el-input v-model="form.firstEnum_enumText" placeholder="" :disabled="true" />
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item label="医保余额：" prop="balanceAmount" class="custom-label-spacing">
                  <el-input v-model="form.balanceAmount" placeholder="" :disabled="true" />
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item label="参保类型：" prop="cb" class="custom-label-spacing">
                  <el-input v-model="form.pyStr" placeholder="" :disabled="true" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="5">
                <el-form-item label="医保名称：" prop="ybName" class="custom-label-spacing">
                  <el-input v-model="form.ybName" placeholder="" :disabled="true" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="医保区域：" prop="ybAreaNo">
                  <el-select
                    v-model="form.ybAreaNo"
                    placeholder="医保区域"
                    clearable
                    style="width: 240px"
                    :disabled="true"
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
              <el-col :span="5">
                <el-form-item
                  label="欠费限制额度："
                  prop="limitAccount"
                  class="custom-label-spacing"
                >
                  <el-input v-model="form.limitAccount" placeholder="" :disabled="true" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="5">
                <el-form-item label="费用性质：" prop="contractNo" class="custom-label-spacing">
                  <el-select
                    v-model="form.contractNo"
                    placeholder="费用性质"
                    clearable
                    style="width: 240px"
                    ref="contractNameRef"
                  >
                    <el-option
                      v-for="item in contractList"
                      :key="item.busNo"
                      :label="item.contractName"
                      :value="item.busNo"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="就诊原因：" prop="jzyy">
                  <el-select
                    v-model="form.jzyy"
                    placeholder="就诊原因"
                    clearable
                    style="width: 240px"
                    ref="jzyyRef"
                  >
                    <el-option
                      v-for="dict in jzyyList"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="电话：" prop="phone" class="custom-label-spacing">
                  <el-input v-model="form.phone" placeholder="" ref="phoneRef" />
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item label="优先级：" prop="priorityEnum" class="custom-label-spacing">
                  <el-select
                    v-model="form.priorityEnum"
                    placeholder="优先级"
                    clearable
                    style="width: 240px"
                    ref="prioritySelectRef"
                  >
                    <el-option
                      v-for="item in priorityLevelOptionOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <!-- <el-col :span="4">
                <el-form-item
                  label=""
                  prop="pyStr"
                  class="custom-label-spacing"
                >
                  <el-checkbox
                    v-model="form.allergenFlag"
                    label="减免"
                  ></el-checkbox>
                </el-form-item>
              </el-col> -->
            </el-row>
            <el-row :gutter="24">
              <!-- <el-col :span="5">
                <el-form-item label="科室：" prop="name">
                  <el-select
                    v-model="form.ybType"
                    placeholder="就诊原因"
                    clearable
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
              </el-col> -->
              <el-col :span="5">
                <el-form-item label="就诊科室：" prop="orgId" class="custom-label-spacing">
                  <el-tree-select
                    v-model="form.orgId"
                    :data="orgOptions"
                    :props="{
                      value: 'id',
                      label: 'name',
                      children: 'children',
                    }"
                    value-key="id"
                    placeholder="请选择就诊科室"
                    check-strictly
                    :expand-on-click-node="false"
                    :filter-node-method="filterNode"
                    ref="locationTreeRef"
                    node-key="value"
                    highlight-current
                    default-expand-all
                    @node-click="handleNodeClick"
                    clearable
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="挂号类型" prop="serviceTypeId" class="custom-label-spacing">
                  <el-select
                    v-model="form.serviceTypeId"
                    placeholder="挂号类型"
                    clearable
                    style="width: 240px"
                    @change="setchargeItem"
                    ref="serviceTypeRef"
                  >
                    <el-option
                      v-for="healthcare in healthcareList"
                      :key="healthcare.id"
                      :label="healthcare.name"
                      :value="healthcare.id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="医生：" prop="practitionerId">
                  <el-select
                    v-model="form.practitionerId"
                    placeholder="医生"
                    clearable
                    style="width: 240px"
                    @change="setInfo"
                    ref="doctorRef"
                  >
                    <el-option
                      v-for="doctor in doctorList"
                      :key="doctor.id"
                      :label="doctor.name"
                      :value="doctor.id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="特病病种：" prop="pyStr" class="custom-label-spacing">
                  <el-select
                    v-model="form.ybType"
                    placeholder="特病病种"
                    clearable
                    style="width: 240px"
                    ref="ybTypeRef"
                    disabled
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
            </el-row>
            <el-row :gutter="24">
              <el-col :span="4">
                <el-form-item label="挂号科室：" prop="locationId_dictText">
                  <el-input v-model="form.locationId_dictText" placeholder="" :disabled="true" />
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item label="医生：" prop="doctorName">
                  <el-input v-model="form.doctorName" placeholder="" :disabled="true" />
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item label="挂号费：" prop="price" class="custom-label-spacing">
                  <el-input v-model="form.price" placeholder="" :disabled="true" />
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item label="诊疗费：" prop="activityPrice" class="custom-label-spacing">
                  <el-input v-model="form.activityPrice" placeholder="" :disabled="true" />
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item label="总金额：" prop="totalPrice" class="custom-label-spacing">
                  <el-input v-model="form.totalPrice" placeholder="" :disabled="true" />
                </el-form-item>
              </el-col>
              <el-col :span="4" style="text-align: right">
                <el-button type="warning" plain icon="CircleClose" @click="handleClear"
                  >清空</el-button
                >
                <el-button type="primary" plain icon="Plus" @click="handleAdd">保存挂号</el-button>
              </el-col>
            </el-row>
            <!-- <el-row :gutter="24" justify="end">
              <el-col :span="5" style="text-align: right">
                <el-button
                  type="warning"
                  plain
                  icon="CircleClose"
                  @click="handleClear"
                  v-hasPermi="['system:user:export']"
                  >清空</el-button
                >
                <el-button
                  type="primary"
                  plain
                  icon="Plus"
                  @click="handleAdd"
                  v-hasPermi="['system:user:add']"
                  >添加</el-button
                >
              </el-col>
            </el-row> -->
          </el-form>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24" class="card-box">
        <el-card>
          <template #header>
            <span style="vertical-align: middle">当日已挂号</span>
          </template>
          <el-input
            v-model="queryParams.searchKey"
            style="width: 200px; margin-bottom: 10px"
            placeholder="请输入患者姓名"
            @keyup.enter="handleQuery"
          >
            <template #append>
              <el-button icon="Search" @click="handleQuery" />
            </template>
          </el-input>
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 300px; margin-bottom: 10px; margin-left: 20px"
            value-format="YYYY-MM-DD"
            :clearable="false"
            @change="handleQuery"
          />
          <el-table v-loading="loading" :data="outpatientRegistrationList" max-height="250">
            <!-- <el-table-column
              label="租户ID"
              align="center"
              key="tenantId"
              prop="tenantId"
            />
            <el-table-column
              label="就诊ID"
              align="center"
              key="encounterId"
              prop="encounterId"
            />
            <el-table-column
              label="科室ID"
              align="center"
              key="organizationId"
              prop="organizationId"
              :show-overflow-tooltip="true"
            /> -->
            <el-table-column label="" align="center" width="50">
              <template #default="scope">
                {{ scope.$index + 1 }}
              </template>
            </el-table-column>
            <el-table-column
              label="科室名称"
              align="center"
              key="organizationName"
              prop="organizationName"
              :show-overflow-tooltip="true"
            />
            <el-table-column
              label="挂号类型 "
              align="center"
              key="healthcareName"
              prop="healthcareName"
              :show-overflow-tooltip="true"
              width="200"
            />
            <!-- <el-table-column
              label="专家账号"
              align="center"
              key="practitionerUserId"
              prop="practitionerUserId"
            /> -->
            <el-table-column
              label="专家"
              align="center"
              key="practitionerName"
              prop="practitionerName"
            />
            <el-table-column
              label="费用性质"
              align="center"
              key="contractName"
              prop="contractName"
            />
            <el-table-column label="挂号金额" align="center" key="totalPrice" prop="totalPrice">
              <template #default="scope">
                <span>
                  {{ scope.row.totalPrice ? scope.row.totalPrice.toFixed(2) + ' 元' : '0.00 元' }}
                </span>
              </template>
            </el-table-column>
            <el-table-column label="收款人" align="center" key="entererName" prop="entererName" />
            <!-- <el-table-column
              label="收款方式"
              align="center"
              key="contractName"
              prop="contractName"
            /> -->
            <!-- <el-table-column
              label="患者id"
              align="center"
              key="patientId"
              prop="patientId"
            /> -->
            <el-table-column
              label="患者姓名"
              align="center"
              key="patientName"
              prop="patientName"
              width="120"
            />
            <el-table-column label="年龄" align="center" key="age" prop="age" width="120" />
            <el-table-column
              label="患者性别"
              align="center"
              key="genderEnum_enumText"
              prop="genderEnum_enumText"
            />
            <!-- <el-table-column label="证件号" align="center" key="idCard" prop="idCard" width="180" /> -->
            <el-table-column
              label="就诊状态"
              align="center"
              key="statusEnum_enumText"
              prop="statusEnum_enumText"
            >
              <template #default="scope">
                <el-tag
                  :type="
                    handleColor(
                      [1, 2, 3, 4, 5, 6, 7, 8, 9],
                      [
                        'default',
                        'success',
                        'default',
                        'info',
                        'success',
                        'info',
                        'warning',
                        'error',
                        'info',
                      ],
                      scope.row.statusEnum
                    )
                  "
                  >{{ scope.row.statusEnum_enumText || '未知' }}</el-tag
                >
              </template>
            </el-table-column>
            <el-table-column
              label="挂号日期/时间"
              align="center"
              key="registerTime"
              prop="registerTime"
              width="180"
            >
              <template #default="scope">
                <span>{{ parseTime(scope.row.registerTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" key="registerTime" prop="registerTime">
              <template #default="scope">
                <!-- <el-tooltip
                  :content="
                    scope.row.statusEnum == 6
                      ? '已退号'
                      : scope.row.statusEnum == 2
                      ? '已接诊，不允许退号'
                      : ''
                  "
                  placement="top"
                  :disabled="scope.row.statusEnum != 6"
                > -->
                <el-button
                  link
                  type="primary"
                  @click="handleReturn(scope.row)"
                  :disabled="scope.row.statusEnum == 6"
                >
                  退号
                </el-button>
                <!-- </el-tooltip> -->
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
        </el-card>
      </el-col>
    </el-row>
    <patient-info-dialog
      ref="patientInfoRef"
      :patientInfoData="patientInfoList"
      :searchInfo="form.searchKey"
      @submit="setForm"
    />
    <patient-add-dialog ref="patientAddRef" @submit="setForm" />
    <ChargeDialog
      :open="openDialog"
      @close="handleClose"
      :category="patientInfo.categoryEnum"
      :totalAmount="form.totalPrice"
      :patientInfo="patientInfo"
      :chargeItemIds="chargeItemIdList"
      :chrgBchnoList="chrgBchnoList"
      :transformedData="transformedData"
      :chrgBchno="chrgBchno"
      :registerBusNo="registerBusNo"
    />
    <RefundDialog
      :open="openRefundDialog"
      @close="
        (value) => {
          if (value == 'success') {
            proxy.$modal.msgSuccess('操作成功');
            getList();
          }
          openRefundDialog = false;
        }
      "
      :totalAmount="totalAmount"
      :patientInfo="patientInfo"
      :paymentId="paymentId"
      :chargeItemIds="chargeItemIdList"
    />
  </div>
</template>

<script setup name="OutpatientRegistration">
import {
  getOutpatientRegistrationList,
  getInit,
  getContractList,
  getConditionDefinitionMetadata,
  getLocationTree,
  getPractitionerMetadata,
  getHealthcareMetadata,
  addOutpatientRegistration,
  getOutpatientRegistrationCurrent,
  returnRegister,
  precharge,
  cancelRegister,
  gerPreInfo,
} from './components/outpatientregistration';
import patientInfoDialog from './components/patientInfoDialog';
import PatientAddDialog from './components/patientAddDialog';
import patientList from './components/patientList';
import { nextTick, ref } from 'vue';
import ChargeDialog from './components/chargeDialog.vue';
import RefundDialog from './components/refundDialog.vue';
import { handleColor } from '@/utils/his';
import useUserStore from '@/store/modules/user';
import { formatDate, formatDateStr } from '@/utils/index';

const patientInfo = ref({});

const router = useRouter();
const { proxy } = getCurrentInstance();
const { sys_normal_disable, sys_user_sex, med_chrgitm_type } = proxy.useDict(
  'sys_normal_disable',
  'sys_user_sex',
  'med_chrgitm_type'
);

const outpatientRegistrationList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const selectedData = ref([]); // 存储选择的行数据
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref('');
const BusiCardInfo = ref(''); // miyao
const priorityLevelOptionOptions = ref(undefined); // 优先级
const jzyyList = ref([{ value: '1', label: '其他' }]);
const showPopover = ref(false);
const patientSearchKey = ref();
const chrgBchno = ref('');
const registerBusNo = ref('');
// 键盘事件用
const contractNameRef = ref(null);
const jzyyRef = ref(null);
const phoneRef = ref(null);
const prioritySelectRef = ref(null);
const locationTreeRef = ref(null);
const serviceTypeRef = ref(null);
const doctorRef = ref(null);
const ybTypeRef = ref(null);
const openDialog = ref(false);
const openRefundDialog = ref(false);
const totalAmount = ref(0);
const chargeItemIdList = ref([]);
const chrgBchnoList = ref([]);
const paymentId = ref('');
const loadingText = ref('');

// 使用 ref 定义查询所得用户信息数据
const patientInfoList = ref(undefined);
// 费用性质
const contractList = ref(undefined);
// const locationOptions = ref(undefined); // 地点树选项
const doctorList = ref(undefined); // 医生选项
const healthcareList = ref(undefined); // 挂号项目选项
const orgOptions = ref(undefined); // 科室选项
const readCardLoading = ref(false);
const transformedData = ref({});
const dateRange = ref([
  formatDateStr(new Date(), 'YYYY-MM-DD'),
  formatDateStr(new Date(), 'YYYY-MM-DD'),
]);
// const initPassword = ref(undefined);
// const postOptions = ref([]);
// const roleOptions = ref([]);
const userStore = useUserStore();

const data = reactive({
  form: {
    priorityEnum: 3,
  },
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    registerTimeSTime: dateRange.value[0] + ' 00:00:00',
    registerTimeETime: dateRange.value[1] + ' 23:59:59',
    // searchKey: undefined, // 品名/商品名/英文品名/编码/拼音
    // statusEnum: undefined, // 状态（包括 1：预置，2：启用，3：停用）
    // ybMatchFlag: undefined, // 是否医保匹配（包括 1：是，0：否）
    // status: undefined, // 状态（包括 1：预置，2：启用，3：停用）
  },
  rules: {
    patientId: [{ required: true, message: '病人不能为空', trigger: 'blur' }],
    priorityEnum: [{ required: true, message: '优先级不能为空', trigger: 'blur' }],
    serviceTypeId: [{ required: true, message: '挂号类型不能为空', trigger: 'blur' }],
    organizationId: [{ required: true, message: '优先级不能为空', trigger: 'blur' }],
    orgId: [{ required: true, message: '就诊科室不能为空', trigger: 'blur' }],
    // practitionerId: [
    //   { required: true, message: "医生不能为空", trigger: "blur" },
    // ],
    typeCode: [{ required: true, message: '账户类型不能为空', trigger: 'blur' }],
    definitionId: [{ required: true, message: '费用定价不能为空', trigger: 'blur' }],
    // totalPrice: [{ required: true, message: "总价不能为空", trigger: "blur" }],
  },
});

// 其他输入框和选择框的 ref
const inputs = [
  contractNameRef,
  jzyyRef,
  phoneRef,
  prioritySelectRef,
  locationTreeRef,
  serviceTypeRef,
  doctorRef,
  ybTypeRef,
];

// 键盘事件处理函数
const handleKeyDown = (event) => {
  const { key } = event;

  // 获取当前焦点的元素
  const currentIndex = inputs.findIndex((input) => {
    if (input.value && input.value.$el) {
      return input.value.$el.contains(document.activeElement);
    }
    return input.value === document.activeElement;
  });

  if (key === 'ArrowDown' || key === 'ArrowRight' || key === 'Tab') {
    event.preventDefault();
    const nextIndex = (currentIndex + 1) % inputs.length;
    const nextInput = inputs[nextIndex].value;
    if (nextInput && nextInput.focus) {
      nextInput.focus();
    } else if (nextInput && nextInput.$el) {
      nextInput.$el.querySelector('input').focus();
    }
  } else if (key === 'ArrowUp' || key === 'ArrowLeft') {
    event.preventDefault();
    const prevIndex = (currentIndex - 1 + inputs.length) % inputs.length;
    const prevInput = inputs[prevIndex].value;
    if (prevInput && prevInput.focus) {
      prevInput.focus();
    } else if (prevInput && prevInput.$el) {
      prevInput.$el.querySelector('input').focus();
    }
  }
};

// 添加事件监听器
onMounted(() => {
  window.addEventListener('keydown', handleKeyDown);
});

// 移除事件监听器
onUnmounted(() => {
  window.removeEventListener('keydown', handleKeyDown);
});

const { queryParams, form, rules } = toRefs(data);

/** 初期所用数据查询 */
function getInitData() {
  getInit().then((response) => {
    console.log(response, 'response');
    priorityLevelOptionOptions.value = response.data.priorityLevelOptionOptions; // 优先级
  });
}

/** 打开用户信息弹窗 */
function handleSearch() {
  console.log(form.value.searchKey, 'form.value.searchKey');
  if (!form.value.searchKey) {
    proxy.$modal.msgError('请输入查询内容');
    return;
  }
  const param = {
    searchKey: form.value.searchKey,
  };
  getOutpatientRegistrationList(param).then((res) => {
    loading.value = false;
    console.log(param, 'param');
    if (res.data.records.length > 0) {
      patientInfoList.value = res.data;
      console.log(patientInfoList.value, 'patientInfoList.value');
      nextTick(() => {
        proxy.$refs['patientInfoRef'].show(); // 确保子组件更新后再调用 show 方法
      });
    }
  });
}
let userCardInfo = ref({});
async function handleReadCard(value) {
  if (chrome.webview === undefined) {
    alert('请在医保版本中调用读卡功能！');
  } else {
    try {
      let webView = window.chrome.webview.hostObjects.CSharpAccessor;
      // string url,
      // string fixmedins_code,
      // string businessType,
      // string operatorCode,
      // string operatorName,
      // string officeId,
      // string officeName

      // readCardLoading.value = true;
      let jsonResult;
      let cardInfo;
      let userMessage = undefined;
      switch (value) {
        case '01': // 电子凭证
          // readCardLoading.value = true;
          await webView
            .GetInfoByQrCodeAsync(
              'http://10.47.0.67:8089/localcfc/api/hsecfc/localQrCodeQuery',
              'H22017200667',
              '01101',
              userStore.id,
              userStore.name,
              'D83',
              '财务科'
            )
            .then((res) => {
              readCardLoading.value = true;
              loadingText.value = '正在读取...';
              jsonResult = res;
            })
            .catch(() => {
              readCardLoading.value = false;
            });
          cardInfo = JSON.parse(jsonResult);
          let message = JSON.parse(cardInfo.message);
          userMessage = {
            certType: '02', // 证件类型
            certNo: message.data.idNo, // 身份证号
            psnCertType: '02', // 居民身份证
          };
          userCardInfo = {
            certType: '01', // 证件类型
            certNo: message.data.idNo, // 身份证号
            psnCertType: '01', // 居民身份证
            busiCardInfo: message.data.ecToken, // 令牌
          };
          BusiCardInfo.value = message.data.ecToken;
          console.log(BusiCardInfo.value);
          break;
        case '02':
          break;
        case '03': // 社保卡
          readCardLoading.value = true;
          loadingText.value = '正在读取...';
          await webView
            .ReadHeaSecCardAsync(
              JSON.stringify({
                IP: 'ddjk.jlhs.gov.cn',
                PORT: 20215,
                TIMEOUT: 60,
                SFZ_DRIVER_TYPE: 1,
              })
            )
            .then((res) => {
              jsonResult = res;
            })
            .finally(() => {
              readCardLoading.value = false;
            });
          // console.log(
          //   'jsonResult',
          //   JSON.parse({
          //     IssuingAreaCode: '310000',
          //     SocialSecurityNumber: '371324198810224515',
          //     CardNumber: 'M501A1A78',
          //     CardIdentificationCode: '310000D15600000535925154E880AB97',
          //     Name: '\u5218\u5CF0',
          //     CardResetInfo: '00814A444686603100333E4FA9',
          //     SpecificationVersion: '3.00',
          //     IssuingDate: '20190313',
          //     ExpirationDate: '20290313',
          //     TerminalNumber: '000000000000',
          //     TerminalDeviceNumber: '00041161201901000005',
          //     Code: 0,
          //     ErrorMessage: null,
          //   })
          // );
          let message1 = JSON.parse(jsonResult);
          userMessage = {
            certType: '02', // 证件类型
            certNo: message1.SocialSecurityNumber, // 身份证号
            psnCertType: '02', // 居民身份证
          };
          userCardInfo = {
            certType: '02', // 证件类型
            certNo: message1.SocialSecurityNumber, // 身份证号
            psnCertType: '02', // 居民身份证
            busiCardInfo: message1.BusiCardInfo, //卡号
          };
          BusiCardInfo.value = message1.BusiCardInfo;
          console.log(message1.BusiCardInfo);
          break;
        case '99':
          break;
      }
      readCardLoading.value = true;
      if (userMessage.certNo) {
        gerPreInfo(userMessage)
          .then((res) => {
            if (res.code == 200) {
              form.value.patientId = res.data.id;
              form.value.name = res.data.name;
              form.value.age = res.data.age;
              form.value.idCard = res.data.idCard;
              form.value.card = res.data.id;
              form.value.contractNo = res.data.contractBusNo;
              form.value.genderEnum = res.data.genderEnum;
              form.value.ybAreaNo = res.data.contractName;
            }
          })
          .finally(() => {
            readCardLoading.value = false;
          });
      }
    } catch (error) {
      console.error('调用失败:', error);
      readCardLoading.value = false;
    }
  }
}

/** 新增用户信息弹窗 */
function handleAddPatient() {
  proxy.$refs['patientAddRef'].show(); // 确保子组件更新后再调用 show 方法
}

// 设定表单
function setForm(formData) {
  console.log(formData, 'formData');
  form.value = { ...form.value, ...formData };
  form.value.patientId = formData.id;
  // 使用 nextTick 确保 DOM 更新完成后设置焦点
  nextTick(() => {
    const prioritySelect = prioritySelectRef.value?.$el?.querySelector('input');
    if (prioritySelect) {
      prioritySelect.focus();
    }
  });
}
// 设定表单
function setInfo() {
  const doctorData = doctorList.value.filter((doctor) => doctor.id === form.value.practitionerId);
  form.value.doctorName = doctorData.length > 0 ? doctorData[0].name : '';
}

// 设定费用项管理表单
function setchargeItem() {
  const healthcareData = healthcareList.value.filter(
    (healthcare) => healthcare.id === form.value.serviceTypeId
  );
  form.value.locationId_dictText = healthcareData.length > 0 ? healthcareData[0].name : '';
  form.value.price = healthcareData.length > 0 ? healthcareData[0].price : '';
  form.value.activityPrice = healthcareData.length > 0 ? healthcareData[0].activityPrice : '';
  form.value.totalPrice =
    healthcareData.length > 0 ? healthcareData[0].price + healthcareData[0].activityPrice : '';
  form.value.definitionId = healthcareData.length > 0 ? healthcareData[0].definitionId : '';
}
/**  查询患者信息 */
function getList() {
  loading.value = true;
  getOutpatientRegistrationCurrent(queryParams.value).then((res) => {
    loading.value = false;
    outpatientRegistrationList.value = res.data.records;
    total.value = res.data.total;
  });
}

/** 查询费用性质 */
function getContract() {
  form.value.jzyy = jzyyList.value[0]; // 设置默认值为第一项
  getContractList().then((response) => {
    contractList.value = response.data;
    console.log('getContractList', 'response', response.data);
    form.value.contractNo = response.data.length > 0 ? response.data[0].busNo : '0000';
  });
}

function handleCharge() {}

// /** 查询诊断信息 */
// function getConditionDefinition() {
//   getConditionDefinitionMetadata().then((response) => {
//     console.log("getConditionDefinitionMetadata", "response", response.data);
//   });
// }

// /** 查询就诊科室 */
// function getLocationInfo() {
//   getLocationTree().then((response) => {
//     locationOptions.value = response.data.records;
//   });
// }
/** 查询就诊位置 */
function getLocationInfo() {
  getLocationTree().then((response) => {
    console.log('getLocationTree', 'response', response.data);
    orgOptions.value = response.data;
  });
}
/** 通过条件过滤节点  */
const filterNode = (value, data) => {
  if (!value) return true;
  return data.label.indexOf(value) !== -1;
};
/** 节点单击事件 */
function handleNodeClick(data) {
  // queryParams.value.sourceEnum = data.value;
  // handleQuery();
  console.log('handleNodeClick', 'data', data);
  form.value.organ = data.id;
  getPractitioner(data);
  getHealthcare(data);
}

/** 根据位置id筛选医生 */
function getPractitioner(data) {
  const param = {
    orgId: data.id,
  };
  console.log('getPractitioner', 'param', param);
  getPractitionerMetadata(param).then((response) => {
    console.log('getPractitioner', 'response', response.data);
    doctorList.value = response.data.records;
  });
}

/** 根据机构id筛选服务项目 */
function getHealthcare(data) {
  const param = {
    organizationId: data.id,
  };
  // 设定表单中的机构ID
  form.value.organizationId = data.organizationId;

  getHealthcareMetadata(param).then((response) => {
    healthcareList.value = response.data.records;
    console.log('getHealthcareMetadata', 'response', response.data);
  });
}

/** 清空条件按钮操作 */
function handleClear() {
  reset();
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNo = 1;
  queryParams.value.registerTimeSTime = dateRange.value[0] + ' 00:00:00';
  queryParams.value.registerTimeETime = dateRange.value[1] + ' 23:59:59';
  getList();
}

/** 重置操作表单 */
function reset() {
  form.value = {
    id: undefined,
    searchKey: undefined,
    type: undefined,
    allergenFlag: undefined,
    name: undefined,
    genderEnum_enumText: undefined,
    age: undefined,
    idCard: undefined,
    pyStr: undefined,
    busNo: undefined,
    ybType: undefined,
    phone: undefined,
    orgId: undefined,
    serviceTypeId: undefined,
    practitionerId: undefined,
    locationId_dictText: undefined,
    doctorName: undefined,
    price: undefined,
    activityPrice: undefined,
    priorityEnum: 3,
    patientId: undefined,
    organizationId: undefined,
    contractNo: undefined,
    typeCode: 1, // 个人现金账户 目前固定传1
    ybName: undefined,
    ybAreaNo: undefined,
    limitAccount: undefined,
    definitionId: undefined,
    serviceId: undefined,
    totalPrice: undefined,
    jzyy: 1,
  };
  proxy.resetForm('outpatientRegistrationRef');
}

/** 新增按钮操作 */
function handleAdd() {
  transformedData.value = transformFormData(form.value);
  console.log(transformedData, 'transformedData门诊挂号');
  chargeItemIdList.value = [];
  patientInfo.value.patientId = form.value.patientId;
  proxy.$refs['outpatientRegistrationRef'].validate((valid) => {
    if (valid) {
      readCardLoading.value = true;
      transformedData.value.busiCardInfo = userCardInfo.busiCardInfo;
      transformedData.value.certType = userCardInfo.certType;
      transformedData.value.certNo = userCardInfo.certNo;
      transformedData.value.ybMdtrtCertType = userCardInfo.psnCertType;
      addOutpatientRegistration(transformedData.value)
        .then((res) => {
          if (res.code == 200) {
            // proxy.$modal.msgSuccess('挂号成功');
            chrgBchno.value = res.data.chrgBchno;
            registerBusNo.value = res.data.busNo;
            readCardLoading.value = false;
            openDialog.value = true;
            // chargeItemIdList.value = res.data;
            // patientInfo.value.encounterId = res.data.encounterId[0];
            // precharge({
            //   patientId: patientInfo.value.patientId,
            //   encounterId: patientInfo.value.encounterId,
            //   chargeItemIds: chargeItemIdList.value,
            // }).then((res) => {
            //   if (res.code == 200) {
            //     // proxy.$modal.msgSuccess('操作成功');
            //     // totalAmount.value = res.data.psnCashPay;
            //     chrgBchnoList.value = res.data.chrgBchnoList;
            //     openDialog.value = true;
            //   } else {
            //     proxy.$modal.msgError(res.msg);
            //   }
            // });
            // getList();
          } else {
            readCardLoading.value = false;
            proxy.$modal.msgError(res.msg);
          }
        })
        .finally(() => {
          readCardLoading.value = false;
        });
    }
  });
}

/**
 * 姓名表单获取焦点打开列表
 */
function handleFocus() {
  showPopover.value = true;
}
/**
 * 姓名表单失去焦点关闭列表
 */
function handleBlur() {
  showPopover.value = false;
}

/**
 * 搜索患者
 */
function handleSearchPatient(value) {
  patientSearchKey.value = value;
}

function handleReturn(row) {
  openRefundDialog.value = true;
  patientInfo.value.patientId = row.patientId;
  patientInfo.value.encounterId = row.encounterId;
  totalAmount.value = row.totalPrice;
  chargeItemIdList.value = row.chargeItemIds.split(',');
  paymentId.value = row.paymentId;
  console.log(paymentId.value);
}

function handleReturnRegister() {
  returnRegister(patientInfo.value.encounterId).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess('操作成功');
      getList();
    }
  });
}

/**
 * 点击患者列表给表单赋值
 */
function selsectPatient(row) {
  form.value = { ...form.value, ...row };
  form.value.patientId = row.id;
  form.value.searchKey = row.name;
  form.value.name = row.name;
  form.value.idCard = row.idCard;
  form.value.genderEnum_enumText = row.genderEnum_enumText;
  form.value.phone = row.phone;
  form.value.firstEnum_enumText = row.firstEnum_enumText;
  form.value.age = row.age;
}

// 设置新增参数
function transformFormData(form) {
  console.log(form, 'transformFormData*****************');
  return {
    encounterFormData: {
      patientId: form.patientId,
      priorityEnum: form.priorityEnum,
      serviceTypeId: form.serviceTypeId,
      organizationId: form.orgId,
    },
    encounterLocationFormData: {
      locationId: form.locationId,
    },
    encounterParticipantFormData: {
      practitionerId: form.practitionerId,
    },
    accountFormData: {
      patientId: form.patientId,
      typeCode: 1, // 默认值为 "1"
      name: form.ybName,
      balanceAmount: form.balanceAmount,
      ybAreaNo: form.ybAreaNo,
      contractNo: form.contractNo,
      limitAccount: form.limitAccount,
    },
    chargeItemFormData: {
      patientId: form.patientId,
      definitionId: form.definitionId,
      serviceId: form.serviceTypeId,
      totalPrice: form.price, // 默认值为 99.99
    },
  };
}

function handleClose(value) {
  openDialog.value = false;
  if (value == 'success') {
    proxy.$modal.msgSuccess('操作成功');
    getList();
    reset();
    // addOutpatientRegistration(transformedData.value).then((response) => {
    //   reset();
    //   proxy.$modal.msgSuccess('新增成功');
    //   getList();
    // });
  } else if (value == 'cancel') {
    // cancelRegister(patientInfo.value.encounterId).then((res) => {
    //   if (res.code == 200) {
    //     getList();
    //   }
    // });
  } else {
    openRefundDialog.value = false;
  }
}

getInitData();
getList();
getContract();
// getConditionDefinition();
getLocationInfo();
</script>
<style scoped>
.el-form--inline .el-form-item {
  display: inline-flex;
  vertical-align: middle;
  margin-right: 10px !important;
}

/* 让图标和下拉选在同一行显示 */
.icon-select-container {
  display: flex; /* 使用 Flexbox 布局 */
  align-items: center; /* 垂直居中 */
  padding-left: 0px;
}

/* 调整 el-form-item 的样式 */
.icon-select-container .el-form-item {
  margin-bottom: 0; /* 去掉默认的 margin-bottom */
  margin-left: 8px; /* 图标和下拉选之间的间距 */
}
</style>
