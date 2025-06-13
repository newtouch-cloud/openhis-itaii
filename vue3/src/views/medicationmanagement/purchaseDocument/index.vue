<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8" v-if="viewStatus">
      <el-col :span="1.5">
        <el-button
          v-if="viewStatus != 'view'"
          plain
          type="primary"
          icon="Edit"
          @click="handelApply"
          >审批通过</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-if="viewStatus != 'view'"
          type="primary"
          plain
          icon="Edit"
          @click="handleReject"
          >驳回</el-button
        >
      </el-col>
    </el-row>
    <el-row :gutter="10" class="mb8" v-else>
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="submitAudit">提交审核</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Printer" :disabled="multiple" @click="handleDelete">
          打印
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain icon="EditPen" @click="handleTotalAmount">
          计算金额
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleSave">批量保存</el-button>
      </el-col>
    </el-row>

    <el-form
      :model="receiptHeaderForm"
      ref="receiptHeaderRef"
      :inline="true"
      label-width="100px"
      :rules="rules"
    >
      <el-form-item label="单据号：" prop="busNo">
        <el-input
          v-model="receiptHeaderForm.busNo"
          placeholder="单据号："
          clearable
          style="width: 200px"
          :disabled="true"
        />
      </el-form-item>
      <el-form-item label="经手人：" prop="practitionerId" label-width="120px">
        <el-select
          v-model="receiptHeaderForm.practitionerId"
          placeholder=""
          clearable
          filterable
          :disabled ="viewStatus=='view'"
          style="width: 150px"
        >
          <el-option
            v-for="practitioner in practitionerListOptions"
            :key="practitioner.value"
            :label="practitioner.label"
            :value="practitioner.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="制单日期：">
        <el-date-picker
          v-model="receiptHeaderForm.occurrenceTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="datetime"
          :disabled ="viewStatus=='view'"
        />
      </el-form-item>
      <el-form-item label="供应商：" prop="supplierId">
        <el-select
          v-model="receiptHeaderForm.supplierId"
          placeholder=""
          clearable
          filterable
          style="width: 150px"
          :disabled ="viewStatus=='view'"
        >
          <el-option
            v-for="supplier in supplierListOptions"
            :key="supplier.value"
            :label="supplier.label"
            :value="supplier.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="药品类型：" prop="medicationType">
        <el-select
          v-model="receiptHeaderForm.medicationType"
          placeholder=""
          clearable
          filterable
          style="width: 150px"
          :disabled="disabledForm"
          @change="
            (value) => {
              itemType = value;
            }
          "
        >
          <el-option
            v-for="itemType in purchase_type"
            :key="itemType.value"
            :label="itemType.label"
            :value="itemType.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="仓库类型：" prop="purposeTypeEnum">
        <el-select
          v-model="receiptHeaderForm.purposeTypeEnum"
          placeholder=""
          clearable
          style="width: 150px"
          :disabled="disabledForm"
          @change="handleChangeLocationType"
        >
          <el-option
            v-for="dict in warehous_type"
            :key="dict.value"
            :label="dict.label"
            :disabled="disabledForm"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <el-tabs type="border-card">
      <el-tab-pane label="入库单明细">
        <el-row :gutter="10" class="mb8" v-if="!viewStatus">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="addNewRow">添加行</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="Delete"
              :disabled="multiple"
              @click="deleteSelectedRows"
              >删除行</el-button
            >
          </el-col>
        </el-row>
        <el-form :model="form" :rules="tableRules" ref="formRef">
          <!-- @cell-mouse-enter="handleMouseEnter" -->
          <!-- v-click-outside-row="handleClickOutside" @row-click="handleRowClick" 点击行以外的部分自动保存 -->
          <el-table
            row-key="id"
            v-loading="loading"
            :data="form.purchaseinventoryList"
            @selection-change="handleSelectionChange"
            ref="tableRef"
            @cell-mouse-leave="handleMouseLeave"
          >
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column label="项目" align="center" key="name" prop="name" width="200" fixed>
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.name`"
                  :rules="tableRules.name"
                > 
                  <el-input v-if="viewStatus=='view'"  v-model="scope.row.name" placeholder="" disabled />
                  <PopoverList v-else @search="handleSearch" :width="1000" :modelValue="scope.row.name">
                    <template #popover-content="{}">
                      <MedicineList
                        @selectRow="(row) => selectRow(row, scope.$index)"
                        :searchKey="medicineSearchKey"
                        :itemType="itemType"
                      />
                    </template>
                  </PopoverList>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="规格"
              align="center"
              key="statusEnum_enumText"
              prop="statusEnum_enumText"
              width="150"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.volume`"
                  :rules="tableRules.volume"
                >
                  <el-input v-model="scope.row.volume" placeholder="" disabled />
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="厂家/产地"
              align="center"
              key="manufacturer"
              prop="manufacturer"
              :show-overflow-tooltip="true"
              width="160"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.manufacturer`"
                  :rules="tableRules.manufacturer"
                >
                  <el-input v-model="scope.row.manufacturer" placeholder="" disabled />
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="仓库"
              align="center"
              key="purposeLocationId"
              prop="purposeLocationId"
              :show-overflow-tooltip="true"
              width="180"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.purposeLocationId`"
                  :rules="tableRules.purposeLocationId"
                >
                  <div class="select_wrapper_div">
                    <el-select
                      v-model="scope.row.purposeLocationId"
                      placeholder="请选择仓库"
                      :class="{ 'error-border': scope.row.error }"
                      clearable
                      :disabled ="viewStatus=='view'"
                    >
                      <el-option
                        v-for="(item, index) in locationList"
                        :key="index"
                        :label="item.name"
                        :value="item.id"
                        @click="handleLocationClick(item, scope.row, scope.$index)"
                      />
                    </el-select>
                  </div>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="采购数量"
              align="center"
              key="itemQuantity"
              prop="itemQuantity"
              width="120"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.itemQuantity`"
                  :rules="tableRules.itemQuantity"
                >
                  <div class="select_wrapper_div">
                    <el-input
                      :disabled ="viewStatus=='view'"
                      v-model="scope.row.itemQuantity"
                      placeholder=""
                      @blur="handleTotalPrice(scope.$index)"
                      :class="{ 'error-border': scope.row.error }"
                    />
                  </div>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="计量单位"
              align="center"
              key="unitCode"
              prop="unitCode"
              :show-overflow-tooltip="true"
              width="90"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.unitCode`"
                  :rules="tableRules.unitCode"
                >
                  <!-- :disabled="data.isEdit" -->
                  <div class="select_wrapper_div">
                    <el-select
                      v-model="scope.row.unitCode"
                      placeholder=" "
                      :class="{ 'error-border': scope.row.error }"
                      :clearable="false"
                      :disabled ="viewStatus=='view'"
                      @change="(value) => handleUnitCodeChange(scope.row, scope.$index, value)"
                    >
                      <template v-if="scope.row.unitList && scope.row.partPercent > 1">
                        <el-option
                          :label="scope.row.unitList.unitCode_dictText"
                          :value="scope.row.unitList.unitCode"
                        />
                        <el-option
                          :label="scope.row.unitList.minUnitCode_dictText"
                          :value="scope.row.unitList.minUnitCode"
                        />
                      </template>
                      <template v-if="scope.row.unitList && scope.row.partPercent == 1">
                        <el-option
                          :label="scope.row.unitList.unitCode_dictText"
                          :value="scope.row.unitList.unitCode"
                        />
                      </template>
                      <template v-if="!scope.row.unitList">
                        <el-option
                          :label="scope.row.unitCode_dictText"
                          :value="scope.row.unitCode"
                        />
                      </template>
                    </el-select>
                  </div>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="药品追溯码"
              align="center"
              key="traceNo"
              prop="traceNo"
              width="130"
            >
              <template #default="scope">
                
                <el-tooltip :content="formatContent(scope.row.traceNo)" placement="top" 
                  popper-class="custom-tooltip">
                  <el-form-item
                    :prop="`purchaseinventoryList.${scope.$index}.traceNo`"
                    :rules="tableRules.traceNo"
                  >
                    <el-input
                      :disabled ="viewStatus=='view'"
                      v-model="scope.row.traceNo"
                      @change="(value) => handleTraceNoInput(value, scope.row)"
                      placeholder=""
                      :id="'traceNo' + `${scope.$index}`"
                    />
                  </el-form-item>
              </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column
              label="追溯码包装"
              align="center"
              key="packagingLevels"
              prop="packagingLevels"
              width="130"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.packagingLevels`"
                  :rules="tableRules.packagingLevels"
                >
                  <el-input v-model="scope.row.packagingLevels" placeholder="" :disabled ="viewStatus=='view'"/>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="采购单价（包装单位） "
              align="center"
              key="price"
              prop="price"
              width="160"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.price`"
                  :rules="tableRules.price"
                >
                  <div class="select_wrapper_div">
                    <el-input
                      v-model="scope.row.price"
                      placeholder=""
                      @blur="handleTotalPrice(scope.$index)"
                      :class="{ 'error-border': scope.row.error }"
                      :disabled ="viewStatus=='view'"
                    >
                      <template #suffix>元</template>
                    </el-input>
                  </div>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="合计金额 "
              align="center"
              key="totalPrice"
              prop="totalPrice"
              width="130"
            >
              <template #default="scope">
                <el-form-item :prop="`purchaseinventoryList.${scope.$index}.totalPrice`">
                  <el-input disabled v-model="scope.row.totalPrice" placeholder="">
                    <template #suffix>元</template>
                  </el-input>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="产品批号"
              align="center"
              key="lotNumber"
              prop="lotNumber"
              width="160"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.lotNumber`"
                  :rules="tableRules.lotNumber"
                >
                  <el-input v-model="scope.row.lotNumber" placeholder="" maxlength="100" :disabled ="viewStatus=='view'" />
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="生产日期"
              align="center"
              key="startTime"
              prop="startTime"
              width="150"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.startTime`"
                  :rules="tableRules.startTime"
                >
                  <el-date-picker
                    v-model="scope.row.startTime"
                    type="date"
                    placeholder="选择日期"
                    format="YYYY-MM-DD"
                    value-format="YYYY-MM-DD"
                    :disabled ="viewStatus=='view'"
                    @change="changeValStart($event, scope.$index)"
                  />
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="有效期至"
              align="center"
              key="endTime"
              prop="endTime"
              width="150"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.endTime`"
                  :rules="tableRules.endTime"
                >
                  <el-date-picker
                    :disabled ="viewStatus=='view'"
                    v-model="scope.row.endTime"
                    type="date"
                    placeholder="选择日期"
                    format="YYYY-MM-DD"
                    value-format="YYYY-MM-DD"
                    @change="changeValEnd($event, scope.$index)"
                  />
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="发票号 "
              align="center"
              key="invoiceNo"
              prop="invoiceNo"
              width="130"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.invoiceNo`"
                  :rules="tableRules.invoiceNo"
                >
                  <el-input v-model="scope.row.invoiceNo" placeholder="" :disabled ="viewStatus=='view'"/>
                </el-form-item>
              </template>
            </el-table-column>

            <el-table-column label="备注" align="center" key="remake" prop="remake" width="130">
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.remake`"
                  :rules="tableRules.remake"
                >
                  <el-input v-model="scope.row.remake" placeholder="" :disabled ="viewStatus=='view'" />
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="货位"
              align="center"
              key="locationStoreName"
              prop="locationStoreName"
              width="180"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.locationStoreName`"
                  :rules="tableRules.locationStoreName"
                >
                  <div class="select_wrapper_div">
                    <el-select
                      v-model="scope.row.locationStoreName"
                      placeholder="请选择货位"
                      :class="{ 'error-border': scope.row.error }"
                      clearable
                      :disabled ="viewStatus=='view'"
                    >
                    </el-select>
                  </div>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="总库存数量"
              align="center"
              key="quantity"
              prop="quantity"
              width="120"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.quantity`"
                  :rules="tableRules.quantity"
                >
                  <el-input v-model="scope.row.quantity" placeholder="" disabled />
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              v-if="!viewStatus"
              label="操作"
              align="center"
              width="80"
              class-name="small-padding fixed-width"
              fixed="right"
            >
              <template #default="scope">
                <el-button
                  link
                  type="primary"
                  icon="Edit"
                  @click="handleScan(scope.row, scope.$index)"
                  >扫码</el-button
                >
              </template>
            </el-table-column>
          </el-table>
        </el-form>
      </el-tab-pane>
    </el-tabs>
    <el-row
      :gutter="10"
      class="mb8"
      style="margin-top: 15px; display: flex; align-items: center; justify-content: flex-start"
    >
      <el-col :span="3">
        <span>制单人：{{ userUserStore.name }}</span>
      </el-col>
      <!-- <el-col :span="2">
          <span>审核人：</span>
        </el-col>
        <el-col :span="2">
          <span>单据状态：</span>
        </el-col> -->
      <el-col :span="6">
        <el-row :gutter="8" style="display: flex; align-items: center; justify-content: flex-start">
          <el-col :span="10">
            <span>合计金额：{{ totalAmount ? totalAmount.toFixed(4) : 0 }}</span>
          </el-col>
          <!-- <el-col :span="10">
              <el-input v-model="totalAmount" placeholder="" disabled />
            </el-col> -->
        </el-row>
      </el-col>
    </el-row>
    <!-- </el-dialog> -->
    <el-dialog
      title="药品追溯码"
      v-model="openTraceNo"
      width="800"
      append-to-body
      destroy-on-close
      :draggable="true"
      @opened="
        () => {
          console.log(123);
          traceNoTempRef.focus();
        }
      "
    >
      <div>
        <div style="font-size: 16px">
          <span>药品名称： {{ medName }}</span>
          选择追溯码
          <el-input
            ref="traceNoTempRef"
            v-model="traceNoTemp"
            style="width: 260px; margin-right: 20px"
            @input="throttledGetList"
          />
          <el-button
            type="warning"
            plain
            icon="CircleClose"
            @click="handleClear"
            style="float: right"
          >
            清除
          </el-button>
        </div>
        <el-input
          ref="inputRef"
          v-model="traceNo"
          type="textarea"
          :rows="15"
          disabled
          @input="throttledGetList"
          style="width: 100%; margin-top: 10px; margin-right: 20px"
        />
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submit">确 定</el-button>
          <el-button @click="cancelTraceNo">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="PurchaseDocument">
import {
  submitApproval,
  addPurchaseinventory,
  getInit,
  delPurchaseinventory,
  getPharmacyList,
  getCount,
  getDispensaryList,
  getMedicineList,
  getInitBusNo,
  purchaseInventoryApproved,
  reject,
} from './components/api';
const route = useRoute();
import PopoverList from '@/components/OpenHis/popoverList/index.vue';
import MedicineList from './components/medicineList.vue';
import { formatDate, formatDateymd } from '@/utils/index';
import { useStore } from '@/store/store';
import useUserStore from '@/store/modules/user';
import { nextTick, ref, watch } from 'vue';
import useTagsViewStore from '@/store/modules/tagsView';
import _, { isEqual } from 'lodash';
import { debounce } from 'lodash-es';
const router = useRouter();
const tagsViewStore = useTagsViewStore();
const userUserStore = useUserStore();
const store = useStore();

const { proxy } = getCurrentInstance();
const { warehous_type, category_code, service_type_code, specialty_code, purchase_type } =
  proxy.useDict(
    'warehous_type',
    'category_code',
    'service_type_code',
    'specialty_code',
    'purchase_type'
  );

const viewStatus = ref('');
const startTimeOld = ref('');
const endTimeOld = ref('');
const purchaseinventoryList = ref([]);
const open = ref(false);
const loading = ref(false);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref('');
const visible = ref(false);
const row = ref({});
const rowIndex = ref(-1);
const totalAmount = ref(0);
const editData = ref({});
const form = reactive({
  purchaseinventoryList: [],
});
const forms = reactive({
  purchaseinventoryList: [],
});
const receiptHeaderForm = reactive({
  busNo: undefined,
  occurrenceTime: formatDate(new Date()),
});

const data = reactive({
  isEdit: false,
  isAdding: true,
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    searchKey: undefined, // 供应商名称
    busNo: undefined, // 编码
    statusEnum: undefined, // 状态
    supplierId: undefined, // 供应商ID
    applyTimeStart: undefined, // 申请时间开始
    practitionerId: undefined, // 经手人ID
  },
  rules: {
    practitionerId: [{ required: true, message: '请选择经手人', trigger: 'change' }],
    supplierId: [{ required: true, message: '请选择供应商', trigger: 'change' }],
    purposeTypeEnum: [{ required: true, message: '请选择仓库类型', trigger: 'change' }],
    medicationType: [{ required: true, message: '请选择药品类型', trigger: 'change' }],
  },
  tableRules: {
    itemId: [{ required: true, message: '项目不能为空', trigger: 'blur' }],
    statusEnum_enumText: [{ required: true, message: '规格不能为空', trigger: 'blur' }],
    unitCode: [{ required: true, message: '计量单位不能为空', trigger: 'blur' }],
    purposeLocationId: [{ required: true, message: '目的仓库不能为空', trigger: 'blur' }],
    // purposeLocationStoreId: [
    //   { required: true, message: "货位不能为空", trigger: "blur" },
    // ],
    lotNumber: [{ required: true, message: '产品批号不能为空', trigger: 'change' }],
    // traceNo: [{ required: true, message: "追溯码不能为空", trigger: "blur" }],
    startTime: [{ required: true, message: '开始时间不能为空', trigger: 'blur' }],
    endTime: [{ required: true, message: '结束时间不能为空', trigger: 'blur' }],
    price: [{ required: true, message: '单价不能为空', trigger: 'blur' }],
    invoiceNo: [{ required: true, message: '发票号不能为空', trigger: 'blur' }],
    totalPrice: [{ required: true, message: '总价不能为空', trigger: 'blur' }],
  },
});

const { queryParams, rules, tableRules } = toRefs(data);
const itemTypeOptions = ref(undefined); // 入库项目类型
const practitionerListOptions = ref(undefined); // 查询经手人列表
const supplierListOptions = ref(undefined); // 供应商列表
const selectedRows = ref([]); // 用于存储选中的行
const emit = defineEmits(['refresh']);
const tableRef = ref(undefined); // 表格引用
const currentRow = ref({}); // 当前操作的行
const medicineSearchKey = ref('');
const itemType = ref('');
const price = ref(0);
const unitMap = ref({});
const currentEditRow = ref({});
const currentIndex = ref(-1);
const currentRowId = ref('');
const medName = ref('');
const disabledForm = computed(() => {
  return form.purchaseinventoryList.length > 0;
});
const rowList = ref([]);
const saveRowObject = ref({});

watch(
  () => store.currentData,
  (newVlaue) => {
    console.log(newVlaue, '11212121212121');
    if (newVlaue) {
      editData.value.editRow = newVlaue?.editRow;
      editData.value.item = newVlaue?.item;
      edit();
    } else {
      editData.value = {};
      show();
    }
  },
  { immediate: true }
);
watch(
  () => route.query.view,
  (newVlaue) => {
    getPurchaseinventoryTypeList();
    if (newVlaue) {
      viewStatus.value = newVlaue;
    } else {
      viewStatus.value = '';
    }
  },
  { immediate: true }
);
watch(
  () => form.purchaseinventoryList,
  (newVlaue) => {
    if (newVlaue && newVlaue.length > 0) {
      if (viewStatus.value) {
        handleTotalAmount();
      }
    }
  },
  { immediate: true }
);
onMounted(() => {
  console.log(route, '!1212');
  if (route.query.view) {
    viewStatus.value = route.query.view;
  } else {
    viewStatus.value = '';
  }
  if (Object.keys(editData.value).length === 0) {
    show();
  }
});

function addNewRow() {
  proxy.$refs['receiptHeaderRef'].validate((valid) => {
    if (valid) {
      // if (data.isAdding) {
      //   proxy.$message.warning('请先保存当前行后再新增！');
      //   return;
      // }
      const newRow = {
        id: '',
        itemTable: '',
        itemQuantity: '',
        itemId: '',
        unitCode: '',
        remake: '',
        supplierId: '',
        purposeTypeEnum: '',
        purposeLocationId: null,
        purposeLocationStoreId: null,
        practitionerId: '',
        traceNo: '',
        invoiceNo: '',
        startTime: '',
        endTime: '',
        price: '',
        totalPrice: '',
        sellPrice: '',
        minSellPrice: '',
        // locationInventoryList: [], // 库房列表
        unitList: {}, // 单位列表
        isEditing: true, // 标记当前行是否正在编辑
        error: false, // 新增 error 字段
        isSave: false, // 当前行是否保存
      };
      form.purchaseinventoryList.push(newRow);
      data.isAdding = true; // 设置标志位为 true，表示有未保存的
    }
  });
}

function handleBlur(row, index) {
  let hasError = false;
  if (!row.itemTable) {
    if (receiptHeaderForm.medicationType === '1') {
      row.itemTable = 'med_medication_definition';
    } else {
      row.itemTable = 'adm_device_definition';
    }
  }
  row.practitionerId = receiptHeaderForm.practitionerId;
  row.busNo = receiptHeaderForm.busNo;
  row.occurrenceTime = receiptHeaderForm.occurrenceTime;
  row.supplierId = receiptHeaderForm.supplierId;
  row.purposeTypeEnum = receiptHeaderForm.purposeTypeEnum;
}
const handleMouseEnter = (row, column, cell, event) => {
  // console.log('鼠标移入行', row);
};

const handleMouseLeave = (row, column, cell, event) => {
  // handleBlur(row)
  // if(row.id){ // 编辑
  //   let rows = JSON.parse(JSON.stringify(saveRowObject.value))
  //   delete rows.unitList
  //   let rowss= JSON.parse(JSON.stringify(row))
  //   delete rowss.unitList
  //   let purchaseinventoryIndex = form.purchaseinventoryList.findIndex(k=>k.id==row.id)
  //   if(!areObjectsEqual(rowss,rows)){
  //     handleSave(rowss,purchaseinventoryIndex)
  //   }
  // }else{
  //   let rows = JSON.parse(JSON.stringify(saveRowObject.value))
  //   delete rows.unitList
  //   let rowss= JSON.parse(JSON.stringify(row))
  //   delete rowss.unitList
  //   console.log(form.purchaseinventoryList.length,form.purchaseinventoryList,"211111111")
  //   if(form.purchaseinventoryList.length==1){
  //     handleSave(row,0)
  //   }else if(form.purchaseinventoryList.length>1&&saveRowObject.value){
  //     if(!areObjectsEqual(rowss,rows)){
  //       handleSave(rowss,form.purchaseinventoryList.length-1)
  //     }
  //   }
  // }
};

// 点击行时记录当前行
function handleRowClick(row) {
  if (row.id != currentRowId.value) {
    handleClickOutside();
  }
  currentEditRow.value = JSON.parse(JSON.stringify(row));
  if (row.id) {
    currentIndex.value = form.purchaseinventoryList.findIndex((item) => item.id == row.id);
    currentRowId.value = row.id;
  } else {
    currentIndex.value = form.purchaseinventoryList.length - 1;
  }
  // handleBlur(row);
  // if (row.id) {
  //   // 编辑
  //   let purchaseinventoryIndex = form.purchaseinventoryList.findIndex((k) => k.id == row.id);
  //   let rows = JSON.parse(JSON.stringify(forms.purchaseinventoryList[purchaseinventoryIndex]));
  //   delete rows.unitList;
  //   let rowss = JSON.parse(JSON.stringify(row));
  //   delete rowss.unitList;

  //   console.log(
  //     purchaseinventoryIndex,
  //     '编辑',
  //     forms.purchaseinventoryList[purchaseinventoryIndex],
  //     rowss,
  //     rows
  //   );
  //   if (!areObjectsEqual(rowss, rows)) {
  //     handleSave(rowss, purchaseinventoryIndex);
  //   }
  // } else {
  //   let rows = JSON.parse(JSON.stringify(saveRowObject.value));
  //   delete rows.unitList;
  //   let rowss = JSON.parse(JSON.stringify(row));
  //   delete rowss.unitList;

  //   console.log(form.purchaseinventoryList.length, form.purchaseinventoryList, '211111111');
  //   if (form.purchaseinventoryList.length == 1) {
  //     handleSave(row, 0);
  //   } else if (form.purchaseinventoryList.length > 1 && saveRowObject.value) {
  //     if (!areObjectsEqual(rowss, rows)) {
  //       handleSave(rowss, form.purchaseinventoryList.length - 1);
  //     }
  //   }
  // }
  currentRow.value = row;
}
function areObjectsEqual(obj1, obj2) {
  const keys1 = Object.keys(obj1);
  const keys2 = Object.keys(obj2);
  if (keys1.length !== keys2.length) {
    return false;
  }
  for (let key of keys1) {
    if (obj1[key] !== obj2[key]) {
      return false;
    }
  }
  return true;
}

// 监听表格外的点击事件
// function handleClickOutside(event) {
//   if (tableRef.value && !tableRef.value.$el.contains(event.target)) {
//     if (currentRow.value) {
//       // handleSave(currentRow.value);
//       currentRow.value = null; // 清空当前行
//     }
//   }
// }

function saveRow(row, index) {
  form.purchaseinventoryList[index] = row;
  addPurchaseinventory(row).then((response) => {
    reset();
    data.isAdding = false; // 允许新增下一行
    proxy.$message.success('保存成功！');
    visible.value = false;
  });
}

const openTraceNo = ref(false)
const traceNo = ref('');
const traceNoList = ref([]);
const traceNoTemp = ref('');
const traceNoTempRef = ref();
const throttledGetList = debounce(handelTraceNo, 500);
function handleClear() {
  traceNo.value = '';
  traceNoList.value = [];
}
function handleScan(row,index){
  medName.value = row.name
  openTraceNo.value = true;
  currentIndex.value = index
}
function handelTraceNo(value) {
  traceNoList.value.push(value);
  traceNo.value = traceNo.value + '[' + traceNoList.value.length + ']' + '  ' + value + '\n';
  traceNoTemp.value = '';
  // let saveValue = value.substring(inputValue.length + 5, value.length);
  // inputValue = value;
  // console.log(value);
  // console.log(saveValue);
  // traceNoList.value.push(saveValue);
  // traceNo.value = value + '[' + (traceNoList.value.length + 1) + ']' + '  ';
}
function cancelTraceNo() {
  openTraceNo.value = false;
  traceNoList.value = [];
  traceNo.value = '';
}

function submit(){
  form.purchaseinventoryList[currentIndex.value].traceNo = traceNoList.value.join(',');
  openTraceNo.value = false;
  traceNoList.value = [];
  traceNo.value = '';
}

function formatContent(value){
  let content = ''
  if(value){
    value.split(',').forEach((item, index) => {
      content +=  `[${(index + 1)}] ${item}\n`
    })
    return content
  }
}
// 药品列表搜索
function handleSearch(value) {
  medicineSearchKey.value = value;
}

const locationList = ref([]);
// 选择药品
function selectRow(rowValue, index) {
  form.purchaseinventoryList[index] = { id: form.purchaseinventoryList[index].id };
  price.value = 0;
  rowIndex.value = index;
  form.purchaseinventoryList[index].itemId = rowValue.definitionId;
  form.purchaseinventoryList[index].name = rowValue.name;
  form.purchaseinventoryList[index].volume = rowValue.volume;
  form.purchaseinventoryList[index].minUnitCode = rowValue.minUnitCode;
  form.purchaseinventoryList[index].unitCode = rowValue.unitCode;
  form.purchaseinventoryList[index].manufacturer = rowValue.manufacturer;
  form.purchaseinventoryList[index].partPercent = rowValue.partPercent;
  form.purchaseinventoryList[index].price = (rowValue.purchaseAmount || 0).toFixed(2);
  price.value = (rowValue.purchaseAmount || 0).toFixed(2);
  form.purchaseinventoryList[index].unitList = rowValue.unitList[0];
  // form.purchaseinventoryList[index].locationInventoryList = locationList.value;
  unitMap.value['unitCode'] = rowValue.unitCode;
  unitMap.value['minUnitCode'] = rowValue.minUnitCode;
  unitMap.value['currentCode'] = rowValue.unitCode;
  form.purchaseinventoryList[index].itemQuantity = 0;
  form.purchaseinventoryList[index].totalPrice = 0;
}

let totalQuantity = 0;
// 选择仓库
function handleLocationClick(item, row, index) {
  getCount({
    itemId: row.itemId,
    orgLocationId: item.id,
  }).then((res) => {
    if (res.data) {
      totalQuantity = 0;
      res.data.forEach((item) => {
        totalQuantity += item.orgQuantity;
      });
      form.purchaseinventoryList[index].quantity =
        handleTotalQuantity(
          totalQuantity,
          row.partPercent,
          row.unitList.unitCode_dictText,
          row.unitList.minUnitCode_dictText
        ) || 0;
      // form.purchaseinventoryList[index].price = (res.data[0].price || 0).toFixed(2);
      // price.value = (res.data[0].price || 0).toFixed(2);
      // form.purchaseinventoryList[index].traceNo = res.data[0].traceNo || '';
      form.purchaseinventoryList[index].itemTable = res.data[0].itemTable || '';
      form.purchaseinventoryList[index].startTime = formatDateymd(res.data[0].productionDate) || '';
      form.purchaseinventoryList[index].endTime = formatDateymd(res.data[0].expirationDate) || '';
      startTimeOld.value = form.purchaseinventoryList[index].startTime
        ? form.purchaseinventoryList[index].startTime
        : '';
      endTimeOld.value = form.purchaseinventoryList[index].endTime
        ? form.purchaseinventoryList[index].endTime
        : '';
    }
  });
  // form.purchaseinventoryList[index].purposeLocationStoreId =
  //   item.locationStoreId;
  // form.purchaseinventoryList[index].locationStoreName = item.locationStoreName;
  //
}

// 处理总数量不整时显示的情况,例 10盒2片
function handleTotalQuantity(quantity, partPercent, unitCode, minUnitCode) {
  if (quantity % partPercent != 0) {
    return (
      Math.floor(quantity / partPercent).toString() +
      ' ' +
      unitCode +
      (quantity % partPercent).toString() +
      ' ' +
      minUnitCode
    );
  }
  return quantity / partPercent;
}

function getNextDayZeroTimeStamp(selectedTime) {
  const now = new Date(selectedTime);
  const nextDay = new Date(now);
  nextDay.setDate(now.getDate());
  return nextDay.getTime();
}

function changeValStart(val, index) {
  const selectedTime = val;
  const validTime = this.getNextDayZeroTimeStamp(selectedTime);
  if (form.purchaseinventoryList[index].endTime) {
    const endTime = formatDateymd(form.purchaseinventoryList[index].endTime);
    const getNextDayZeroTime = this.getNextDayZeroTimeStamp(endTime);
    if (getNextDayZeroTime < validTime) {
      proxy.$message.warning('生产日期必须小于等于有效期！');
      form.purchaseinventoryList[index].startTime = startTimeOld.value;
      return;
    }
  }
}
function changeValEnd(val, index) {
  const selectedTimes = val;
  const validTimes = this.getNextDayZeroTimeStamp(selectedTimes);
  if (form.purchaseinventoryList[index].startTime) {
    const startTime = formatDateymd(form.purchaseinventoryList[index].startTime);
    const getNextDayZeroTimes = this.getNextDayZeroTimeStamp(startTime);
    if (getNextDayZeroTimes > validTimes) {
      proxy.$message.warning('有效期必须大于等于生产日期！');
      form.purchaseinventoryList[index].endTime = endTimeOld.value;
      return;
    }
  }
}
// 切换仓库类型获取药房/药库列表
function handleChangeLocationType(value) {
  if (value == 16) {
    getPharmacyList().then((res) => {
      locationList.value = res.data;
    });
  } else if (value == 11) {
    getDispensaryList().then((res) => {
      locationList.value = res.data;
    });
  }
}

// 单位处理
function handleUnitCodeChange(row, index, value) {
  if (
    //小单位
    unitMap.value['unitCode'] != value &&
    unitMap.value['currentCode'] == unitMap.value['unitCode']
  ) {
    console.log(1212121, row, index, value);
    // 总库存数
    form.purchaseinventoryList[index].quantity = totalQuantity;
    // // 采购数量
    // form.purchaseinventoryList[index].itemQuantity =
    //   form.purchaseinventoryList[index].itemQuantity * row.partPercent || 0;
    // // 采购单价
    // form.purchaseinventoryList[index].price = (price.value / row.partPercent).toFixed(4);
    // 采购数量
    form.purchaseinventoryList[index].itemQuantity = undefined;
    // 采购单价
    // form.purchaseinventoryList[index].price = undefined;
  } else {
    //大单位
    console.log(12121212, row, index, value);
    form.purchaseinventoryList[index].quantity =
      handleTotalQuantity(
        totalQuantity,
        row.partPercent,
        row.unitList.unitCode_dictText,
        row.unitList.minUnitCode_dictText
      ) || 0;
    // form.purchaseinventoryList[index].itemQuantity =
    //   form.purchaseinventoryList[index].itemQuantity / row.partPercent || 0;
    // form.purchaseinventoryList[index].price = price.value;
    form.purchaseinventoryList[index].itemQuantity = undefined;
    // form.purchaseinventoryList[index].price = undefined;
  }
  form.purchaseinventoryList[index].totalPrice = undefined;
  unitMap.value['currentCode'] = value;
}

// 计算总价
function handleTotalPrice(index) {
  let purchaseItem = form.purchaseinventoryList[index];
  if (!purchaseItem.unitList) {
    purchaseItem.unitList = {
      unitCode: purchaseItem.unitCode,
      unitCode_dictText: purchaseItem.unitCode_dictText,
    };
  }
  if (purchaseItem.price > 0 && purchaseItem.itemQuantity > 0) {
    if (purchaseItem.unitCode == purchaseItem.unitList.unitCode) {
      //大单位
      form.purchaseinventoryList[index].totalPrice = (
        purchaseItem.price * purchaseItem.itemQuantity
      ).toFixed(4);
    } else {
      // 小单位
      form.purchaseinventoryList[index].totalPrice = (
        (purchaseItem.price / purchaseItem.partPercent) *
        purchaseItem.itemQuantity
      ).toFixed(4);
    }
  }
  if (form.purchaseinventoryList[index].itemQuantity == 0) {
    form.purchaseinventoryList[index].totalPrice = 0;
  }
}
// 批量保存
function handleSave(row, index) {
  rowList.value = [];
  form.purchaseinventoryList.map((row, index) => {
    if (row) {
      handleBlur(row);
      proxy.$refs['receiptHeaderRef'].validate((valid) => {
        if (valid) {
          proxy.$refs['formRef'].validate((valid) => {
            if (valid) {
              if (row.itemQuantity == 0) {
                proxy.$message.warning('采购数量不能为0！');
                return;
              }
              rowList.value.push(JSON.parse(JSON.stringify(row)));
              if (
                rowList._rawValue &&
                rowList._rawValue.length == form.purchaseinventoryList.length
              ) {
                addPurchaseinventorys(rowList._rawValue);
              }
            }
          });
        }
      });
    }
  });
}
function addPurchaseinventorys(rowList) {
  addPurchaseinventory(JSON.parse(JSON.stringify(rowList))).then((res) => {
    // 当前行没有id视为首次新增
    // if (!row.id) {
    //   data.isAdding = false; // 允许新增下一行
    // }
    if (res.data) {
      proxy.$message.success('保存成功！');
      form.purchaseinventoryList.map((row, index) => {
        console.log(row, 'res.data');
        form.purchaseinventoryList[index].id = res.data[index];
        form.purchaseinventoryList[index].isSave = true;
      });
      // forms.purchaseinventoryList[index] = JSON.parse(
      //   JSON.stringify(form.purchaseinventoryList[index])
      // );
      // currentEditRow.value = {};
      // currentIndex.value = -1;
      // saveRowObject.value = JSON.parse(JSON.stringify(row));
      console.log(saveRowObject.value, '!@121211');
    }
  });
}

function handleTraceNoInput(value, row) {
  row.traceNo = value.concat(',');
  // row.traceNo = row.traceNo.startsWith(',') ? row.traceNo.substring(1) : row.traceNo;
}

// function handleSave(row, index) {
//   handleBlur(row);
//   proxy.$refs['receiptHeaderRef'].validate((valid) => {
//     if (valid) {
//       proxy.$refs['formRef'].validate((valid) => {
//         if (valid) {
//           if (row.itemQuantity == 0) {
//             proxy.$message.warning('采购数量不能为0！');
//             return;
//           }
//           addPurchaseinventory(row).then((res) => {
//             // 当前行没有id视为首次新增
//             if (!row.id) {
//               data.isAdding = false; // 允许新增下一行
//             }
//             if (res.data) {
//               proxy.$message.success('保存成功！');
//               form.purchaseinventoryList[index].id = res.data;
//               form.purchaseinventoryList[index].isSave = true;
//               // form.purchaseinventoryList[index].isEditing = false;
//               forms.purchaseinventoryList[index] = JSON.parse(
//                 JSON.stringify(form.purchaseinventoryList[index])
//               );
//               currentEditRow.value = {};
//               currentIndex.value = -1;
//               saveRowObject.value = JSON.parse(JSON.stringify(row));
//               // saveRowObject.value = JSON.parse(JSON.stringify(form.purchaseinventoryList[index]))
//               console.log(saveRowObject.value, '!@121211');
//             }
//           });
//         }
//       });
//     }
//   });
//   // 保存逻辑...
// }

/** 选择条数  */
function handleSelectionChange(selection) {
  console.log(selection, 'selection');
  // selectedData.value = selection.map((item) => ({ ...item })); // 存储选择的行数据
  ids.value = selection.map((item) => item.id);
  selectedRows.value = selection;
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

function deleteSelectedRows() {
  let length = selectedRows.value.length;
  let ids = [];
  if (selectedRows.value[0].id) {
    ids = selectedRows.value.map((item) => {
      return item.id;
    });
  }
  if (selectedRows.value[length - 1].isSave) {
    delPurchaseinventory(ids).then((res) => {
      if (res.code == 200) {
        proxy.$message.success('删除成功');
      }
    });
  } else {
    if (length > 1) {
      delPurchaseinventory(ids.slice(0, -1)).then((res) => {
        if (res.code == 200) {
          proxy.$message.success('删除成功');
        }
      });
    }
  }
  form.purchaseinventoryList = form.purchaseinventoryList.filter(
    (row) => !selectedRows.value.includes(row)
  );
  data.isAdding = false;
  currentIndex.value = -1;
}

/**计算合计金额 */
function handleTotalAmount() {
  totalAmount.value = form.purchaseinventoryList.reduce((accumulator, currentRow) => {
    return accumulator + (Number(currentRow.totalPrice) || 0);
  }, 0);
}

/** 重置操作表单 */
function reset() {
  // form.value = {
  //   id: undefined,
  //   name: undefined,
  //   categoryCode: undefined,
  //   cwTypeCode: undefined,
  //   fwTypeCode: undefined,
  //   specialtyCode: undefined,
  //   locationId: undefined,
  //   offeredOrgId: undefined,
  //   activeFlag: undefined,
  //   extraDetails: undefined,
  //   contact: undefined,
  //   appointmentRequiredFlag: undefined,
  //   chargeName: undefined,
  //   price: undefined,
  //   description: undefined,
  //   ybType: undefined,
  //   title: undefined,
  //   comment: undefined,
  // };
  // proxy.resetForm("purchaseinventoryRef");

  // receiptHeaderForm = {
  //   busNo: undefined,
  //   practitionerId: undefined,
  //   occurrenceTime: undefined,
  //   supplierId: undefined,
  //   medicationType: "1",
  //   purposeTypeEnum: undefined,
  // };
  proxy.resetForm('receiptHeaderRef');
  form.purchaseinventoryList = [];
}
// 显示弹框
function show() {
  data.isEdit = false;
  data.isAdding = false;
  reset();
  visible.value = true;
  getPurchaseinventoryTypeList();
  getInitBusNo().then((res) => {
    receiptHeaderForm.busNo = res.data.busNo;
  });
  // supplierListOptions.value = props.supplierListOptions;
  // itemTypeOptions.value = props.itemTypeOptions;
  // practitionerListOptions.value = props.practitionerListOptions;
  // receiptHeaderForm.busNo = props.busNoAdd;
  // console.log(purchase_type.value, "purchase_type.value")
  // // 设置默认值为字典中的第一个值
  // if (purchase_type.value.length > 0) {
  //   form.value.medicationType = purchase_type.value[0].value;
  // }
}
// 显示弹框
function edit() {
  data.isAdding = false;
  data.isEdit = true;
  // reset();
  visible.value = true;
  // supplierListOptions.value = props.supplierListOptions;
  // itemTypeOptions.value = props.itemTypeOptions;
  // practitionerListOptions.value = props.practitionerListOptions;
  // receiptHeaderForm.busNo = props.busNoAdd;
  receiptHeaderForm.value = editData.value.item.length > 0 ? editData.value.item[0] : {};
  receiptHeaderForm.busNo = editData.value.editRow.supplyBusNo;
  receiptHeaderForm.supplierId = editData.value.editRow.supplierId;
  receiptHeaderForm.practitionerId = editData.value.editRow.practitionerId;
  console.log(editData.value.editRow, editData.value, 'editData.value.editRow');
  receiptHeaderForm.occurrenceTime = editData.value.editRow.occurrenceTime
    ? formatDate(editData.value.editRow.occurrenceTime)
    : formatDate(new Date());
  console.log(
    editData.value.editRow.purposeTypeEnum,
    editData.value,
    'editData.value.editRow.purposeTypeEnum'
  );

  //仓库类型
  receiptHeaderForm.purposeTypeEnum = editData.value.editRow.purposeTypeEnum
    ? editData.value.editRow.purposeTypeEnum.toString()
    : editData.value.item[0].purposeTypeEnum.toString();
  receiptHeaderForm.medicationType =
    editData.value.item[0].itemTable == 'med_medication_definition' ? '1' : '2';
  total.value = form.purchaseinventoryList.length;
  handleChangeLocationType(
    editData.value.editRow.purposeTypeEnum
      ? editData.value.editRow.purposeTypeEnum.toString()
      : editData.value.item[0].purposeTypeEnum.toString()
  );
  setTimeout(() => {
    form.purchaseinventoryList = editData.value.item.map((item) => {
      return {
        ...item,
        name: item.itemName,
        volume: item.totalVolume,
        manufacturer: item.supplierName,
        quantity: item.totalQuantity,
        // locationInventoryList: locationList.value,
        startTime: formatDateymd(item.startTime),
        endTime: formatDateymd(item.endTime),
        applyTime: formatDate(item.applyTime),
        isSave: true,
      };
    });
  }, 100);

  console.log(form.purchaseinventoryList, 'purchaseinventoryList.value');
  console.log(receiptHeaderForm, 'receiptHeaderForm');
  loading.value = false;
}

function getPurchaseinventoryTypeList() {
  getInit().then((response) => {
    itemTypeOptions.value = response.data.itemTypeOptions; // 活动标记
    practitionerListOptions.value = response.data.practitionerListOptions; // 预约必填标记
    supplierListOptions.value = response.data.supplierListOptions; // 供应商列表
  });
}

// 驳回
function handleReject() {
  reject(route.query.supplyBusNo).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess('操作成功');
      tagsViewStore.delView(router.currentRoute.value);
      store.clearCurrentData();
      // 跳转到审核页面
      router.replace({
        path: '/aaaa/medicationmanagement/billapproval',
        query: { type: 'purchaseDocument' },
      });
    }
  });
}
function handelApply() {
  purchaseInventoryApproved(route.query.supplyBusNo).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess('操作成功');
      tagsViewStore.delView(router.currentRoute.value);
      store.clearCurrentData();
      // 跳转到审核页面
      router.replace({
        path: '/aaaa/medicationmanagement/billapproval',
        query: { type: 'purchaseDocument' },
      });
    }
  });
}
function toLastView(visitedViews, view) {
  const latestView = visitedViews.slice(-1)[0];
  if (latestView) {
    router.push(latestView.fullPath);
  } else {
    if (view.name === 'Dashboard') {
      router.replace({ path: '/redirect' + view.fullPath });
    } else {
      router.push('/');
    }
  }
}
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}
/** 提交审核 */
function submitAudit() {
  let length = form.purchaseinventoryList.length;
  if (length < 1) {
    proxy.$modal.msgWarning('请先添加单据');
  } else if (!form.purchaseinventoryList[length - 1].isSave) {
    proxy.$modal.msgWarning('第' + length + '行单据未保存，请先保存');
  } else {
    submitApproval(receiptHeaderForm.busNo).then((res) => {
      if (res.code == 200) {
        proxy.$modal.msgSuccess('提交审批成功');
        emit('refresh');
        visible.value = false;
        // 关闭当前页
        tagsViewStore.delView(router.currentRoute.value);
        // 跳转到退货单管理页面
        router.replace({ path: 'purchaseinventory' });
      }
    });
  }
}
/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  form.value = JSON.parse(JSON.stringify(row));
  form.value.fwTypeCode = form.value.typeCode;
  open.value = true;
  title.value = '编辑';
}

/** 删除按钮操作 */
function handleDelete(row) {
  const delId = row.id || ids.value;
  data.isAdding = false;
  proxy.$modal
    .confirm('是否确认删除以上数据?')
    .then(function () {
      return delPurchaseinventory({ ids: delId.join(',') });
    })
    .then(() => {
      proxy.$modal.msgSuccess('删除成功');
    })
    .catch(() => {});
}
function handleClickOutside() {
  if (
    currentIndex.value != -1 &&
    !isEqual(currentEditRow.value, form.purchaseinventoryList[currentIndex.value])
  ) {
    handleSave(form.purchaseinventoryList[currentIndex.value], currentIndex.value);
  }
}

defineExpose({
  show,
  edit,
});
getPurchaseinventoryTypeList();
</script>
<style scoped>
.custom-tree-node {
  display: flex;
  align-items: center;
}

.title {
  font-weight: bold;
  font-size: large;
  margin-bottom: 10px;
}

.error-border {
  border: 1px solid red;
}
</style>