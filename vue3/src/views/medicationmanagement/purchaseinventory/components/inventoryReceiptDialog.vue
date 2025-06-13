<template>
  <div class="app-container">
    <el-dialog title="入库单据" v-model="visible" width="1500" append-to-body>
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" plain icon="Plus" @click="submitAudit">提交审核</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="primary" plain icon="Printer" :disabled="multiple" @click="handleDelete"
            >打印</el-button
          >
        </el-col>
        <el-col :span="1.5">
          <el-button type="primary" plain icon="EditPen" @click="handleTotalAmount"
            >计算金额</el-button
          >
        </el-col>
      </el-row>

      <el-form
        :model="receiptHeaderForm"
        ref="receiptHeaderRef"
        :inline="true"
        label-width="100px"
        :rules="rules"
      >
        <el-form-item label="单据号" prop="busNo">
          <el-input
            v-model="receiptHeaderForm.busNo"
            placeholder="单据号："
            clearable
            style="width: 260px"
            :disabled="data.isEdit"
          />
        </el-form-item>
        <el-form-item label="经手人：" prop="practitionerId" label-width="120px">
          <el-select
            v-model="receiptHeaderForm.practitionerId"
            placeholder=""
            clearable
            style="width: 150px"
            :disabled="data.isEdit"
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
            :disabled="data.isEdit"
          />
        </el-form-item>
        <el-form-item label="供应商：" prop="supplierId">
          <el-select
            v-model="receiptHeaderForm.supplierId"
            placeholder=""
            clearable
            style="width: 150px"
            :disabled="data.isEdit"
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
            style="width: 150px"
            :disabled="data.isEdit"
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
            :disabled="data.isEdit"
            @change="handleChangeLocationType"
          >
            <el-option
              v-for="dict in warehous_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <el-tabs type="border-card">
        <el-tab-pane label="入库单明细">
          <el-row :gutter="10" class="mb8">
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
            <el-table
              v-loading="loading"
              :data="form.purchaseinventoryList"
              @selection-change="handleSelectionChange"
              @row-click="handleRowClick"
              ref="tableRef"
            >
              <el-table-column type="selection" width="50" align="center" />
              <el-table-column label="项目" align="center" key="name" prop="name" width="200" fixed>
                <template #default="scope">
                  <el-form-item
                    :prop="`purchaseinventoryList.${scope.$index}.name`"
                    :rules="tableRules.name"
                  >
                    <PopoverList @search="handleSearch" :width="1000" :modelValue="scope.row.name">
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
                      >
                        <el-option
                          v-for="(item, index) in scope.row.locationInventoryList"
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
                    <div class="select_wrapper_div">
                      <el-select
                        v-model="scope.row.unitCode"
                        placeholder=" "
                        :disabled="data.isEdit"
                        :class="{ 'error-border': scope.row.error }"
                        @change="(value) => handleUnitCodeChange(scope.row, scope.$index, value)"
                      >
                        <template v-if="!data.isEdit && scope.row.partPercent > 1">
                          <el-option
                            :label="scope.row.unitList.unitCode_dictText"
                            :value="scope.row.unitList.unitCode"
                          />
                          <el-option
                            :label="scope.row.unitList.minUnitCode_dictText"
                            :value="scope.row.unitList.minUnitCode"
                          />
                        </template>
                        <template v-if="!data.isEdit && scope.row.partPercent == 1">
                          <el-option
                            :label="scope.row.unitList.unitCode_dictText"
                            :value="scope.row.unitList.unitCode"
                          />
                        </template>
                        <template v-if="data.isEdit">
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
                label="采购单价 "
                align="center"
                key="price"
                prop="price"
                width="130"
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
                    <el-input disabled v-model="scope.row.totalPrice" placeholder="" />
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
                    <el-input v-model="scope.row.lotNumber" placeholder="" />
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
                      v-model="scope.row.endTime"
                      type="date"
                      placeholder="选择日期"
                      format="YYYY-MM-DD"
                      value-format="YYYY-MM-DD"
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
                    <el-input v-model="scope.row.invoiceNo" placeholder="" />
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
                  <el-form-item
                    :prop="`purchaseinventoryList.${scope.$index}.traceNo`"
                    :rules="tableRules.traceNo"
                  >
                    <el-input v-model="scope.row.traceNo" placeholder="" />
                  </el-form-item>
                </template>
              </el-table-column>
              <el-table-column
                label="追溯码包装"
                align="center"
                key="approvalTime"
                prop="approvalTime"
                width="130"
              >
                <template #default="scope">
                  <el-form-item
                    :prop="`purchaseinventoryList.${scope.$index}.zsbz`"
                    :rules="tableRules.zsbz"
                  >
                    <el-input v-model="scope.row.zsbz" placeholder="" />
                  </el-form-item>
                </template>
              </el-table-column>
              <el-table-column
                label="备注"
                align="center"
                key="detailJson"
                prop="detailJson"
                width="130"
              >
                <template #default="scope">
                  <el-form-item
                    :prop="`purchaseinventoryList.${scope.$index}.detailJson`"
                    :rules="tableRules.detailJson"
                  >
                    <el-input v-model="scope.row.detailJson" placeholder="" />
                  </el-form-item>
                </template>
              </el-table-column>
              <el-table-column
                label="操作"
                align="center"
                width="80"
                class-name="small-padding fixed-width"
                fixed="right"
              >
               <!-- v-hasPermi="['system:user:edit']" -->
                <template #default="scope">
                  <el-button
                    link
                    type="primary"
                    icon="Edit"
                    @click="handleSave(scope.row, scope.$index)"
                   
                    >保存</el-button
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
          <span>制单人：{{ userStore.name }}</span>
        </el-col>
        <!-- <el-col :span="2">
          <span>审核人：</span>
        </el-col>
        <el-col :span="2">
          <span>单据状态：</span>
        </el-col> -->
        <el-col :span="6">
          <el-row
            :gutter="8"
            style="display: flex; align-items: center; justify-content: flex-start"
          >
            <el-col :span="10">
              <span>合计金额：{{ totalAmount }}</span>
            </el-col>
            <!-- <el-col :span="10">
              <el-input v-model="totalAmount" placeholder="" disabled />
            </el-col> -->
          </el-row>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script setup name="InventoryReceiptDialog">
import {
  submitApproval,
  addPurchaseinventory,
  getInit,
  delPurchaseinventory,
  getPharmacyList,
  getCount,
  getDispensaryList,
  getMedicineList,
} from './purchaseinventory';
import PopoverList from '@/components/OpenHis/popoverList/index.vue';
import MedicineList from './medicineList.vue';
import { formatDate } from '@/utils/index';
import useUserStore from '@/store/modules/user';

const router = useRouter();
const userStore = useUserStore();

const { proxy } = getCurrentInstance();
const { warehous_type, category_code, service_type_code, specialty_code, purchase_type } =
  proxy.useDict(
    'warehous_type',
    'category_code',
    'service_type_code',
    'specialty_code',
    'purchase_type'
  );

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
const props = defineProps({
  practitionerListOptions: {
    type: Object,
    required: false,
  },
  itemTypeOptions: {
    type: Object,
    required: false,
  },
  supplierListOptions: {
    type: Object,
    required: false,
  },
  busNoAdd: {
    type: String,
    required: true,
  },
  item: {
    type: Object,
    required: false,
  },
  editRow: {
    type: Object,
    required: false,
  },
});

const form = reactive({
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
    lotNumber: [{ required: true, message: '产品批号不能为空', trigger: 'blur' }],
    // traceNo: [{ required: true, message: "追溯码不能为空", trigger: "blur" }],
    startTime: [{ required: true, message: '开始时间不能为空', trigger: 'blur' }],
    endTime: [{ required: true, message: '结束时间不能为空', trigger: 'blur' }],
    price: [{ required: true, message: '单价不能为空', trigger: 'blur' }],
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
const currentRow = ref(undefined); // 当前操作的行
const medicineSearchKey = ref('');
const itemType = ref('');
// 挂载时绑定事件
onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});

// 卸载时移除事件
onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside);
});

function addNewRow() {
  proxy.$refs['receiptHeaderRef'].validate((valid) => {
    if (valid) {
      if (data.isAdding) {
        proxy.$message.warning('请先保存当前行后再新增！');
        return;
      }
      const newRow = {
        id: '',
        itemTable: '',
        itemQuantity: '',
        itemId: '',
        unitCode: '',
        detailJson: '',
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
        locationInventoryList: [], // 库房列表
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
  if (receiptHeaderForm.medicationType === '1') {
    row.itemTable = 'med_medication_definition';
  } else {
    row.itemTable = 'adm_device_definition';
  }
  row.practitionerId = receiptHeaderForm.practitionerId;
  row.busNo = receiptHeaderForm.busNo;
  row.occurrenceTime = receiptHeaderForm.occurrenceTime;
  row.supplierId = receiptHeaderForm.supplierId;
  row.purposeTypeEnum = receiptHeaderForm.purposeTypeEnum;
}

// 点击行时记录当前行
function handleRowClick(row) {
  getMedicineList({ itemId: row.itemId }).then((res) => {
    console.log(res.data);
  });
  currentRow.value = row;
}

// 监听表格外的点击事件
function handleClickOutside(event) {
  // if (tableRef.value && !tableRef.value.$el.contains(event.target)) {
  //   if (currentRow.value) {
  //     handleSave(currentRow.value);
  //     currentRow.value = null; // 清空当前行
  //   }
  // }
}

function saveRow(row, index) {
  form.purchaseinventoryList[index] = row;
  addPurchaseinventory(row).then((response) => {
    reset();
    data.isAdding = false; // 允许新增下一行
    proxy.$message.success('保存成功！');
    visible.value = false;
  });
}

// 药品列表搜索
function handleSearch(value) {
  medicineSearchKey.value = value;
}

const locationList = ref([]);
// 选择药品
function selectRow(rowValue, index) {
  rowIndex.value = index;
  form.purchaseinventoryList[index].itemId = rowValue.definitionId;
  form.purchaseinventoryList[index].name = rowValue.name;
  form.purchaseinventoryList[index].volume = rowValue.volume;
  form.purchaseinventoryList[index].minUnitCode = rowValue.minUnitCode;
  form.purchaseinventoryList[index].unitCode = rowValue.unitCode;
  form.purchaseinventoryList[index].manufacturer = rowValue.manufacturer;
  form.purchaseinventoryList[index].partPercent = rowValue.partPercent;
  form.purchaseinventoryList[index].unitList = rowValue.unitList[0];
  form.purchaseinventoryList[index].locationInventoryList = locationList.value;
  console.log(form.purchaseinventoryList[index].unitList[0], 123);
}

// 选择仓库
function handleLocationClick(item, row, index) {
  getCount({
    itemId: row.itemId,
    locationId: item.id,
  }).then((res) => {
    if (res.data) {
      form.purchaseinventoryList[index].quantity = res.data[0].quantity || 0;
      form.purchaseinventoryList[index].price = res.data[0].price || 0;
    }
  });
  // form.purchaseinventoryList[index].purposeLocationStoreId =
  //   item.locationStoreId;
  // form.purchaseinventoryList[index].locationStoreName = item.locationStoreName;
  //
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
  if (row.minUnitCode != value) {
    // 总库存数
    form.purchaseinventoryList[index].quantity = row.quantity / row.partPercent;
    // 采购数量
    form.purchaseinventoryList[index].itemQuantity =
      form.purchaseinventoryList[index].itemQuantity / row.partPercent;
    // 采购单价
    form.purchaseinventoryList[index].price =
      form.purchaseinventoryList[index].price / row.partPercent;
  } else {
    form.purchaseinventoryList[index].quantity = row.quantity * row.partPercent;
    form.purchaseinventoryList[index].itemQuantity =
      form.purchaseinventoryList[index].itemQuantity * row.partPercent;
    form.purchaseinventoryList[index].price =
      form.purchaseinventoryList[index].price * row.partPercent;
  }
}

// 计算总价
function handleTotalPrice(index) {
  let purchaseItem = form.purchaseinventoryList[index];
  if (purchaseItem.price > 0 && purchaseItem.itemQuantity > 0) {
    form.purchaseinventoryList[index].totalPrice = purchaseItem.price * purchaseItem.itemQuantity;
  }
}

function handleSave(row, index) {
  handleBlur(row);
  proxy.$refs['receiptHeaderRef'].validate((valid) => {
    if (valid) {
      proxy.$refs['formRef'].validate((valid) => {
        if (valid) {
          addPurchaseinventory(row).then((res) => {
            // 当前行没有id视为首次新增
            if (!row.id) {
              data.isAdding = false; // 允许新增下一行
            }
            if (res.data) {
              proxy.$message.success('保存成功！');
              form.purchaseinventoryList[index].id = res.data;
              form.purchaseinventoryList[index].isSave = true;
            }
          });
        }
      });
    }
  });
  // 保存逻辑...
}

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
  let ids = selectedRows.value.map((item) => {
    return item.id;
  });
  if (selectedRows.value[length - 1].isSave) {
    delPurchaseinventory(ids).then((res) => {
      if (res.code == 200) {
        proxy.$message.success('删除成功');
      }
    });
  } else {
    if (length > 1) {
      delPurchaseinventory(ids).then((res) => {
        if (res.code == 200) {
          proxy.$message.success('删除成功');
        }
      });
    }
  }
  form.purchaseinventoryList = form.purchaseinventoryList.filter(
    (row) => !selectedRows.value.includes(row)
  );
}

/**计算合计金额 */
function handleTotalAmount() {
  totalAmount.value = form.purchaseinventoryList.reduce((accumulator, currentRow) => {
    return accumulator + (currentRow.totalPrice || 0);
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
  supplierListOptions.value = props.supplierListOptions;
  itemTypeOptions.value = props.itemTypeOptions;
  practitionerListOptions.value = props.practitionerListOptions;
  receiptHeaderForm.busNo = props.busNoAdd;
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
  supplierListOptions.value = props.supplierListOptions;
  itemTypeOptions.value = props.itemTypeOptions;
  practitionerListOptions.value = props.practitionerListOptions;
  // receiptHeaderForm.busNo = props.busNoAdd;
  // receiptHeaderForm = props.item.length > 0 ? props.item[0] : {};
  // receiptHeaderForm.busNo = props.item.supplyBusNo;
  receiptHeaderForm.busNo = props.editRow.supplyBusNo;
  receiptHeaderForm.supplierId = props.editRow.supplierId;
  receiptHeaderForm.practitionerId = props.editRow.practitionerId;
  receiptHeaderForm.occurrenceTime = formatDate(props.editRow.occurrenceTime);
  receiptHeaderForm.purposeTypeEnum = props.editRow.purposeTypeEnum.toString();
  receiptHeaderForm.medicationType =
    props.editRow.itemTable == 'med_medication_definition' ? '1' : '2';
  total.value = form.purchaseinventoryList.length;
  handleChangeLocationType(props.editRow.purposeTypeEnum.toString());
  setTimeout(() => {
    form.purchaseinventoryList = props.item.map((item) => {
      return {
        ...item,
        name: item.itemName,
        volume: item.totalVolume,
        manufacturer: item.supplierName,
        quantity: item.totalQuantity,
        locationInventoryList: locationList.value,
        startTime: formatDate(item.startTime),
        endTime: formatDate(item.endTime),
        applyTime: formatDate(item.applyTime),
        isSave: true,
      };
    });
  }, 100);

  console.log(form.purchaseinventoryList, 'purchaseinventoryList.value');
  console.log(receiptHeaderForm, 'receiptHeaderForm');
  loading.value = false;
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

defineExpose({
  show,
  edit,
});
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