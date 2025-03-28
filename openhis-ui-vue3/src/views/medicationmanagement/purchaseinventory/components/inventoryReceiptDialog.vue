<template>
  <div class="app-container">
    <el-dialog :title="title" v-model="visible" width="95%" append-to-body>
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="Plus"
            @click="handleAdd"
            v-hasPermi="['system:user:add']"
            >提交审核</el-button
          >
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="Printer"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['monitor:job:remove']"
            >打印</el-button
          >
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="EditPen"
            @click="getList"
            v-hasPermi="['system:user:import']"
            >计算金额</el-button
          >
        </el-col>
      </el-row>

      <el-form
        :model="receiptHeaderForm"
        ref="receiptHeaderRef"
        :inline="true"
        v-show="showSearch"
        label-width="90px"
      >
        <el-form-item label="单据号" prop="busNo">
          <el-input
            v-model="receiptHeaderForm.busNo"
            placeholder="单据号："
            clearable
            style="width: 260px"
          />
        </el-form-item>
        <el-form-item
          label="经手人："
          prop="practitionerId"
          label-width="120px"
        >
          <el-select
            v-model="receiptHeaderForm.practitionerId"
            placeholder=""
            clearable
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
        <el-form-item label="单据日期：">
          <el-date-picker
            v-model="receiptHeaderForm.occurrenceTime"
            value-format="YYYY/MM/DD HH:mm:ss"
            type="datetime"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="供应商：" prop="supplierId">
          <el-select
            v-model="receiptHeaderForm.supplierId"
            placeholder=""
            clearable
            style="width: 150px"
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
              <el-button
                type="primary"
                plain
                icon="Plus"
                @click="addNewRow"
                v-hasPermi="['system:user:add']"
                >添加行</el-button
              >
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="Delete"
                :disabled="multiple"
                @click="deleteSelectedRows"
                v-hasPermi="['monitor:job:remove']"
                >删除行</el-button
              >
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="Plus"
                @click="handleAdd"
                v-hasPermi="['system:user:add']"
                >确认当前行</el-button
              >
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="Delete"
                :disabled="multiple"
                @click="handleDelete"
                v-hasPermi="['monitor:job:remove']"
                >取消行编辑</el-button
              >
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="Delete"
                :disabled="multiple"
                @click="handleDelete"
                v-hasPermi="['monitor:job:remove']"
                >取消所有修改</el-button
              >
            </el-col>
            <!-- <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="Search"
                @click="getList"
                v-hasPermi="['system:user:import']"
                style="margin-left: 20px"
                >另存模板</el-button
              >
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="CircleClose"
                @click="handleClear"
                v-hasPermi="['system:user:export']"
                >选择模板</el-button
              >
            </el-col> -->
          </el-row>

          <el-table
            v-loading="loading"
            :data="purchaseinventoryList"
            @selection-change="handleSelectionChange"
            @row-click="handleRowClick"
            ref="tableRef"
          >
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column
              label="项目"
              align="center"
              key="itemId"
              prop="itemId"
              width="300"
            >
              <template #default="scope">
                <div style="display: flex; align-items: center">
                  <el-select
                    v-model="scope.row.itemId"
                    placeholder="请选择"
                    :class="{ 'error-border': scope.row.error }"
                    clearable
                  >
                    <el-option label="项目1" value="1" />
                    <el-option label="项目2" value="2" />
                  </el-select>
                  <el-tooltip content="该项目必填" placement="top">
                    <el-icon style="color: red; margin-left: 5px"
                      ><Warning
                    /></el-icon>
                  </el-tooltip>
                </div>
              </template>
            </el-table-column>
            <el-table-column
              label="规格"
              align="center"
              key="statusEnum_enumText"
              prop="statusEnum_enumText"
              width="130"
            >
              <template #default="scope">
                <el-input v-model="scope.row.guige" placeholder="" />
              </template>
            </el-table-column>
            <el-table-column
              label="厂家/产地"
              align="center"
              key="supplierName"
              prop="supplierName"
              :show-overflow-tooltip="true"
              width="130"
            >
              <template #default="scope">
                <el-input v-model="scope.row.cahngdi" placeholder="" />
              </template>
            </el-table-column>
            <el-table-column
              label="计量单位"
              align="center"
              key="unitCode"
              prop="unitCode"
              :show-overflow-tooltip="true"
              width="100"
            >
              <template #default="scope">
                <div style="display: flex; align-items: center">
                  <el-select
                    v-model="scope.row.unitCode"
                    placeholder="请选择计量单位"
                    :class="{ 'error-border': scope.row.error }"
                    clearable
                  >
                    <el-option label="单位1" value="1" />
                    <el-option label="单位2" value="2" />
                  </el-select>
                  <el-tooltip content="该项目必填" placement="top">
                    <el-icon style="color: red; margin-left: 5px"
                      ><Warning
                    /></el-icon>
                  </el-tooltip>
                </div>
              </template>
            </el-table-column>
            <el-table-column
              label="仓库"
              align="center"
              key="purposeLocationId"
              prop="purposeLocationId"
              :show-overflow-tooltip="true"
              width="130"
            >
              <template #default="scope">
                <div style="display: flex; align-items: center">
                  <el-select
                    v-model="scope.row.purposeLocationId"
                    placeholder="请选择仓库"
                    :class="{ 'error-border': scope.row.error }"
                    clearable
                  >
                    <el-option label="仓库1" value="1" />
                    <el-option label="仓库2" value="2" />
                  </el-select>
                  <el-tooltip content="该项目必填" placement="top">
                    <el-icon style="color: red; margin-left: 5px"
                      ><Warning
                    /></el-icon>
                  </el-tooltip>
                </div>
              </template>
            </el-table-column>
            <el-table-column
              label="货位"
              align="center"
              key="purposeLocationStoreId"
              prop="purposeLocationStoreId"
              width="130"
            >
              <template #default="scope">
                <div style="display: flex; align-items: center">
                  <el-select
                    v-model="scope.row.purposeLocationStoreId"
                    placeholder="请选择货位"
                    :class="{ 'error-border': scope.row.error }"
                    clearable
                  >
                    <el-option label="货位1" value="1" />
                    <el-option label="货位2" value="2" />
                  </el-select>
                  <el-tooltip content="该项目必填" placement="top">
                    <el-icon style="color: red; margin-left: 5px"
                      ><Warning
                    /></el-icon>
                  </el-tooltip>
                </div>
              </template>
            </el-table-column>
            <el-table-column
              label="总库存数量"
              align="center"
              key="approverId"
              prop="approverId"
              width="130"
            >
              <template #default="scope">
                <el-input v-model="scope.row.busNo" placeholder="" />
              </template>
            </el-table-column>
            <el-table-column
              label="采购数量"
              align="center"
              key="itemQuantity"
              prop="itemQuantity"
              width="130"
            >
              <template #default="scope">
                <div style="display: flex; align-items: center">
                  <el-input
                    v-model="scope.row.itemQuantity"
                    placeholder=""
                    :class="{ 'error-border': scope.row.error }"
                  />
                  <el-tooltip content="该项目必填" placement="top">
                    <el-icon style="color: red; margin-left: 5px"
                      ><Warning
                    /></el-icon>
                  </el-tooltip>
                </div>
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
                <div style="display: flex; align-items: center">
                  <el-input
                    v-model="scope.row.price"
                    placeholder=""
                    :class="{ 'error-border': scope.row.error }"
                  />
                  <el-tooltip content="该项目必填" placement="top">
                    <el-icon style="color: red; margin-left: 5px"
                      ><Warning
                    /></el-icon>
                  </el-tooltip>
                </div>
              </template>
            </el-table-column>
            <el-table-column
              label="售价 "
              align="center"
              key="sellPrice"
              prop="sellPrice"
              width="130"
            >
              <template #default="scope">
                <el-input v-model="scope.row.sellPrice" placeholder="" />
              </template>
            </el-table-column>
            <el-table-column
              label="拆零售价 "
              align="center"
              key="minSellPrice"
              prop="minSellPrice"
              width="130"
            >
              <template #default="scope">
                <el-input v-model="scope.row.minSellPrice" placeholder="" />
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
                <el-input v-model="scope.row.totalPrice" placeholder="" />
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
                <el-input v-model="scope.row.lotNumber" placeholder="" />
              </template>
            </el-table-column>
            <el-table-column
              label="生产日期"
              align="center"
              key="startTime"
              prop="startTime"
              width="120"
            >
              <template #default="scope">
                <el-date-picker
                  v-model="scope.row.startTime"
                  type="date"
                  placeholder="选择日期"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
                />
              </template>
            </el-table-column>
            <el-table-column
              label="有效期至"
              align="center"
              key="endTime"
              prop="endTime"
              width="120"
            >
              <template #default="scope">
                <el-date-picker
                  v-model="scope.row.endTime"
                  type="date"
                  placeholder="选择日期"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
                />
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
                <el-input v-model="scope.row.invoiceNo" placeholder="" />
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
                <el-input v-model="scope.row.traceNo" placeholder="" />
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
                <el-input v-model="scope.row.zsbz" placeholder="" />
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
                <el-input v-model="scope.row.detailJson" placeholder="" />
              </template>
            </el-table-column>
            <el-table-column
              label="操作"
              align="center"
              width="140"
              class-name="small-padding fixed-width"
              fixed="right"
            >
              <template #default="scope">
                <el-button
                  link
                  type="primary"
                  icon="Edit"
                  @click="handleSave(scope.row, scope.$index)"
                  v-hasPermi="['system:user:edit']"
                  >保存</el-button
                >
                <!-- <el-button
                  link
                  type="primary"
                  icon="View"
                  @click="handleView(scope.row)"
                  v-hasPermi="['system:user:remove']"
                  >查看</el-button
                > -->
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
        </el-tab-pane>
      </el-tabs>
      <el-row
        :gutter="10"
        class="mb8"
        style="
          margin-top: 15px;
          display: flex;
          align-items: center;
          justify-content: flex-start;
        "
      >
        <el-col :span="2">
          <span>制单人：</span>
        </el-col>
        <el-col :span="2">
          <span>审核人：</span>
        </el-col>
        <el-col :span="2">
          <span>单据状态：</span>
        </el-col>
        <el-col :span="1.5">
          <el-row
            :gutter="8"
            style="
              display: flex;
              align-items: center;
              justify-content: flex-start;
            "
          >
            <el-col :span="6">
              <span>合计：</span>
            </el-col>
            <el-col :span="18">
              <el-input v-model="heji" placeholder="" disabled />
            </el-col>
          </el-row>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script setup name="InventoryReceiptDialog">
import {
  getPurchaseinventoryList,
  addPurchaseinventory,
  getInit,
  delPurchaseinventory,
} from "./purchaseinventory";

const router = useRouter();
const { proxy } = getCurrentInstance();
const purchaseinventoryRef = ref(null); // 初始化 ref
const {
  warehous_type,
  category_code,
  service_type_code,
  specialty_code,
  purchase_type,
} = proxy.useDict(
  "warehous_type",
  "category_code",
  "service_type_code",
  "specialty_code",
  "purchase_type"
);

const purchaseinventoryList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const activeFlagOptions = ref(undefined);
const appointmentRequiredFlagOptions = ref(undefined);
const deptOptions = ref(undefined); // 部门树选项
const locationOptions = ref(undefined); // 地点树选项
const dateRange = ref([]);
const visible = ref(false);

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
});

const data = reactive({
  form: {},
  receiptHeaderForm: {},
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
    offeredOrgId: [
      { required: true, message: "提供部门不能为空", trigger: "blur" },
    ],
    categoryCode: [
      { required: true, message: "服务分类不能为空", trigger: "blur" },
    ],
    fwTypeCode: [
      { required: true, message: "服务类型不能为空", trigger: "blur" },
    ],
    specialtyCode: [
      { required: true, message: "服务专业不能为空", trigger: "blur" },
    ],
    locationId: [{ required: true, message: "地点不能为空", trigger: "blur" }],
    name: [{ required: true, message: "服务名称不能为空", trigger: "blur" }],
    contact: [
      { required: true, message: "联系人电话不能为空", trigger: "blur" },
    ],
    appointmentRequiredFlag: [
      { required: true, message: "预约要求不能为空", trigger: "blur" },
    ],
    activeFlag: [
      { required: true, message: "活动标识不能为空", trigger: "blur" },
    ],
    chargeName: [{ required: true, message: "名称不能为空", trigger: "blur" }],
    description: [{ required: true, message: "描述不能为空", trigger: "blur" }],
    cwTypeCode: [
      { required: true, message: "财务类别不能为空", trigger: "blur" },
    ],
    ybType: [{ required: true, message: "医保类别不能为空", trigger: "blur" }],
    price: [{ required: true, message: "基础价格不能为空", trigger: "blur" }],
  },
  tableRules: {
    itemId: [{ required: true, message: "项目不能为空", trigger: "blur" }],
    itemQuantity: [
      { required: true, message: "采购数量不能为空", trigger: "blur" },
    ],
    unitCode: [
      { required: true, message: "计量单位不能为空", trigger: "blur" },
    ],
    supplierId: [
      { required: true, message: "供应商不能为空", trigger: "blur" },
    ],
    purposeTypeEnum: [
      { required: true, message: "仓库类型不能为空", trigger: "blur" },
    ],
    purposeLocationId: [
      { required: true, message: "仓库不能为空", trigger: "blur" },
    ],
    purposeLocationStoreId: [
      { required: true, message: "货位不能为空", trigger: "blur" },
    ],
    practitionerId: [
      { required: true, message: "经手人不能为空", trigger: "blur" },
    ],
    lotNumber: [
      { required: true, message: "产品批号不能为空", trigger: "blur" },
    ],
    traceNo: [
      { required: true, message: "药品追溯码不能为空", trigger: "blur" },
    ],
    startTime: [
      { required: true, message: "生产日期不能为空", trigger: "blur" },
    ],
    endTime: [{ required: true, message: "有效期至不能为空", trigger: "blur" }],
    price: [{ required: true, message: "采购单价不能为空", trigger: "blur" }],
    totalPrice: [
      { required: true, message: "合计金额不能为空", trigger: "blur" },
    ],
    sellPrice: [{ required: true, message: "售价不能为空", trigger: "blur" }],
    minSellPrice: [
      { required: true, message: "拆零售价不能为空", trigger: "blur" },
    ],
  },
});

const { queryParams, form, receiptHeaderForm, rules, tableRules } =
  toRefs(data);
const itemTypeOptions = ref(undefined); // 入库项目类型
const practitionerListOptions = ref(undefined); // 查询经手人列表
const supplierListOptions = ref(undefined); // 供应商列表
const selectedRows = ref([]); // 用于存储选中的行
const emit = defineEmits(["new-item-added"]);
const tableRef = ref(undefined); // 表格引用
const currentRow = ref(undefined); // 当前操作的行


 // 挂载时绑定事件
 onMounted(() => {
      document.addEventListener('click', handleClickOutside);
    });

    // 卸载时移除事件
    onUnmounted(() => {
      document.removeEventListener('click', handleClickOutside);
    });

function addNewRow() {
  if (data.isAdding) {
    proxy.$message.warning("请先保存当前行后再新增！");
    return;
  }
  const newRow = {
    id: "",
    itemTable: "",
    itemQuantity: "",
    itemId: "",
    unitCode: "",
    detailJson: "",
    supplierId: "",
    purposeTypeEnum: "",
    purposeLocationId: null,
    purposeLocationStoreId: null,
    practitionerId: "",
    traceNo: "",
    invoiceNo: "",
    startTime: "",
    endTime: "",
    price: "",
    totalPrice: "",
    sellPrice: "",
    minSellPrice: "",
    isEditing: true, // 标记当前行是否正在编辑
    error: false, // 新增 error 字段
  };
  purchaseinventoryList.value.push(newRow);
  total.value = purchaseinventoryList.value.length;
  data.isAdding = true; // 设置标志位为 true，表示有未保存的
}

function handleBlur(row, index) {
  let hasError = false;
  if (receiptHeaderForm.value.medicationType === "1") {
    row.itemTable = "med_medication_definition";
  } else {
    row.itemTable = "adm_device_definition";
  }
  row.practitionerId = receiptHeaderForm.value.practitionerId;
  row.occurrenceTime = receiptHeaderForm.value.occurrenceTime;
  row.supplierId = receiptHeaderForm.value.supplierId;
  row.purposeTypeEnum = receiptHeaderForm.value.purposeTypeEnum;
  const fields = [
    "itemId",
    "itemQuantity",
    "unitCode",
    "supplierId",
    "purposeTypeEnum",
    "purposeLocationId",
    "purposeLocationStoreId",
    "practitionerId",
    "lotNumber",
    "traceNo",
    "startTime",
    "endTime",
    "price",
    "totalPrice",
    "sellPrice",
    "minSellPrice",
  ];

  fields.forEach((field) => {
    if (!row[field]) {
      hasError = true;
      proxy.$message.error(tableRules.value[field][0].message);
    }
  });

  row.error = hasError;

  console.log(row, "rowhandleBlurhandleBlurhandleBlurhandleBlurhandleBlur");
  // if (
  //   row.itemTable &&
  //   row.unitCode &&
  //   row.purposeLocationStoreId &&
  //   row.itemQuantity &&
  //   row.price
  // ) {
  //   saveRow(row, index); // 调用保存方法
  // }
}

// 点击行时记录当前行
function handleRowClick(row) {
  currentRow.value = row;
}

// 监听表格外的点击事件
function handleClickOutside(event) {
  if (tableRef.value && !tableRef.value.$el.contains(event.target)) {
    if (currentRow.value) {
      handleSave(currentRow.value);
      currentRow.value = null; // 清空当前行
    }
  }
}

function saveRow(row, index) {
  console.log(row, "saveRowsaveRowsaveRowsaveRowsaveRowsaveRow");
  // 保存当前行的逻辑...
  // 例如：调用 API 保存数据
  // 保存成功后，将标志位设置为 false
  // data.isAdding = false;
  // if (receiptHeaderForm.value.medicationType === "1") {
  //   row.itemTable = "med_medication_definition";
  // } else {
  //   row.itemTable = "adm_device_definition";
  // }
  console.log(row, "rowabbbbbbbbbbbbbb");
  try {
    // 调用 API 保存数据
    // await savePurchaseInventory(row);

    // 保存成功后，更新本地数据
    purchaseinventoryList.value[index] = row;
    // 将表单数据发送给父组件
    // emits("submit", row);
    addPurchaseinventory(row).then((response) => {
      reset();
      data.isAdding = false; // 允许新增下一行
      proxy.$message.success("保存成功！");
      visible.value = false;
      emit("new-item-added"); // 通知父组件
      // getList();
    });
  } catch (error) {
    proxy.$message.error("保存失败，请重试！");
  }

  // proxy.$message.success("保存成功！");
}

function handleSave(row, index) {
  let hasError = false;
  // this.purchaseinventoryList.forEach((row) => {
  handleBlur(row);
  if (row.error) {
    hasError = true;
  }
  // });

  if (hasError) {
    proxy.$message.error("请填写完整信息");
    return;
  }
  try {
    addPurchaseinventory(row).then((response) => {
      reset();
      data.isAdding = false; // 允许新增下一行
      proxy.$message.success("保存成功！");
      visible.value = false;
      emit("new-item-added"); // 通知父组件
      // getList();
    });
  } catch (error) {
    proxy.$message.error("保存失败，请重试！");
  }
  // 保存逻辑...
}
/** 查询挂号收费项目列表 */
function getList() {
  loading.value = true;
  // // queryParams.value.statusEnum = +queryParams.value.statusEnum
  // console.log(queryParams.value, "queryParams.value");
  // getPurchaseinventoryList(queryParams.value).then((res) => {
  loading.value = false;
  //   console.log(res, "res");
  //   purchaseinventoryList.value = res.data.records;
  //   total.value = res.data.total;
  //   console.log(total.value, "total.value");
  // });
}

/** 选择条数  */
function handleSelectionChange(selection) {
  console.log(selection, "selection");
  // selectedData.value = selection.map((item) => ({ ...item })); // 存储选择的行数据
  ids.value = selection.map((item) => item.id);
  selectedRows.value = selection;
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

function deleteSelectedRows() {
  if (selectedRows.value.length === 0) {
    alert("请先选择要删除的行");
    return;
  }

  purchaseinventoryList.value = purchaseinventoryList.value.filter(
    (row) => !selectedRows.value.includes(row)
  );

  selectedRows.value = []; // 清空选中行
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

  receiptHeaderForm.value = {
    busNo: undefined,
    practitionerId: undefined,
    occurrenceTime: undefined,
    supplierId: undefined,
    medicationType: "1",
    purposeTypeEnum: undefined,
  };
  proxy.resetForm("receiptHeaderRef");
  purchaseinventoryList.value = [];
}
// 显示弹框
function show() {
  data.isAdding = false;
  reset();
  visible.value = true;
  supplierListOptions.value = props.supplierListOptions;
  itemTypeOptions.value = props.itemTypeOptions;
  practitionerListOptions.value = props.practitionerListOptions;
  receiptHeaderForm.value.busNo = props.busNoAdd;
  // console.log(purchase_type.value, "purchase_type.value")
  // // 设置默认值为字典中的第一个值
  // if (purchase_type.value.length > 0) {
  //   form.value.medicationType = purchase_type.value[0].value;
  // }
  getList();
}
// 显示弹框
function edit() {
  data.isAdding = false;
  reset();
  supplierListOptions.value = props.supplierListOptions;
  itemTypeOptions.value = props.itemTypeOptions;
  practitionerListOptions.value = props.practitionerListOptions;
  // receiptHeaderForm.value.busNo = props.busNoAdd;
  form.value = props.item;
  receiptHeaderForm.value = props.item.length > 0 ? props.item[0] : {};
  // receiptHeaderForm.value.busNo = props.item.supplyBusNo;
  purchaseinventoryList.value = props.item;
  total.value = purchaseinventoryList.value.length;
  console.log(purchaseinventoryList.value, "purchaseinventoryList.value");
  console.log(receiptHeaderForm.value, "receiptHeaderForm.value");
  loading.value = false;
  visible.value = true;
}
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}
/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增";
}
/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  form.value = JSON.parse(JSON.stringify(row));
  form.value.fwTypeCode = form.value.typeCode;
  open.value = true;
  title.value = "编辑";
}

/** 删除按钮操作 */
function handleDelete(row) {
  const delId = row.id || ids.value;
  proxy.$modal
    .confirm("是否确认删除以上数据?")
    .then(function () {
      return delPurchaseinventory({ ids: delId.join(",") });
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
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