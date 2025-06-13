<template>
  <div class="app-container">
    <!-- <el-dialog title="调拨单据" v-model="visible" width="1500" append-to-body> -->
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
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleSave"
          >批量保存</el-button
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
      <el-form-item label="供应商：" prop="supplierName">
        <el-select
          v-model="receiptHeaderForm.supplierName"
          placeholder=""
          clearable
          style="width: 150px"
          :disabled="disabledForm"
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
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <el-tabs type="border-card">
      <el-tab-pane label="采购退货明细">
        <el-row :gutter="10" class="mb8" v-if="!viewStatus">
          <!-- <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="addNewRow"
              >添加行</el-button
            >
          </el-col> -->
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
            ref="tableRef"
          >
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column label="项目" align="center" key="name" prop="name" width="200" fixed>
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.name`"
                  :rules="tableRules.name"
                >
                  <el-input v-model="scope.row.name" placeholder="" disabled />
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
              width="160"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.purposeLocationId`"
                  :rules="tableRules.purposeLocationId"
                >
                  <div class="select_wrapper_div">
                    <el-select
                      :disabled="disabledForm"
                      v-model="scope.row.purposeLocationId"
                      placeholder="请选择仓库"
                      :class="{ 'error-border': scope.row.error }"
                      clearable
                    >
                      <el-option
                        v-for="(item, index) in locationInventoryList"
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
              width="160"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.locationStoreName`"
                  :rules="tableRules.locationStoreName"
                >
                  <div class="select_wrapper_div">
                    <el-select
                      :disabled="disabledForm"
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
                      :disabled="data.isEdit||viewStatus=='view'"
                      :class="{ 'error-border': scope.row.error }"
                      :clearable="false"
                      @change="(value) => handleUnitCodeChange(scope.row, scope.$index, value)"
                    >
                      <template v-if="scope.row.partPercent > 1">
                        <el-option
                          :label="scope.row.unitList.unitCode_dictText"
                          :value="scope.row.unitList.unitCode"
                        />
                        <el-option
                          :label="scope.row.unitList.minUnitCode_dictText"
                          :value="scope.row.unitList.minUnitCode"
                        />
                      </template>
                      <template v-if="scope.row.partPercent == 1">
                        <el-option
                          :label="scope.row.unitList.unitCode_dictText"
                          :value="scope.row.unitList.unitCode"
                        />
                      </template>
                    </el-select>
                  </div>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="退库数量"
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
                  <el-input v-model="scope.row.itemQuantity" placeholder="" @blur="handleTotalPrice(scope.$index)" :disabled="viewStatus=='view'"/>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="退库原因"
              align="center"
              key="reason"
              prop="reason"
              width="130"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.reason`"
                  :rules="tableRules.reason"
                >
                  <el-input v-model="scope.row.reason" placeholder="" :disabled="viewStatus=='view'"/>
                </el-form-item>
              </template>
            </el-table-column>
             <el-table-column
              label="已退数量"
              align="center"
              key="returnedQuantity"
              prop="returnedQuantity"
              width="120"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.returnedQuantity`"
                  :rules="tableRules.returnedQuantity"
                >
                  <el-input v-model="scope.row.returnedQuantity" placeholder="" disabled />
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
              key="purchaseQuantity"
              prop="purchaseQuantity"
              width="120"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.purchaseQuantity`"
                  :rules="tableRules.purchaseQuantity"
                >
                  <div class="select_wrapper_div">
                    <el-input
                      disabled
                      v-model="scope.row.purchaseQuantity"
                      placeholder=""
                      :class="{ 'error-border': scope.row.error }"
                    />
                  </div>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="采购单位"
              align="center"
              key="purchaseUnitCode_dictText"
              prop="purchaseUnitCode_dictText"
              width="80"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.purchaseUnitCode_dictText`"
                  :rules="tableRules.purchaseUnitCode_dictText"
                >
                  <el-input v-model="scope.row.purchaseUnitCode_dictText" placeholder="" disabled/>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="采购单价(包装单位)" align="center" key="price_text" prop="price_text" width="150">
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.price_text`"
                  :rules="tableRules.price_text"
                >
                  <div class="select_wrapper_div">
                    <el-input
                      disabled
                      v-model="scope.row.price_text"
                      placeholder=""
                      :class="{ 'error-border': scope.row.error }"
                    >
                      <template #suffix>元</template>
                    </el-input>
                  </div>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="退库合计金额 "
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
                  <el-input v-model="scope.row.lotNumber" placeholder="" disabled/>
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
                    disabled
                    v-model="scope.row.startTime"
                    type="date"
                    placeholder="选择日期"
                    format="YYYY-MM-DD"
                    value-format="YYYY-MM-DD"
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
                    disabled
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
                  <el-input v-model="scope.row.invoiceNo" placeholder="" disabled/>
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
                  <el-input v-model="scope.row.traceNo" placeholder="" :disabled="viewStatus=='view'"/>
                </el-form-item>
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
                  <el-input v-model="scope.row.packagingLevels" placeholder="" disabled/>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="备注"
              align="center"
              key="remake"
              prop="remake"
              width="130"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.remake`"
                  :rules="tableRules.remake"
                >
                  <el-input v-model="scope.row.remake" placeholder="" disabled/>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="操作"
              align="center"
              width="80"
              class-name="small-padding fixed-width"
              fixed="right"
              v-if="!viewStatus"
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
      style="
        margin-top: 15px;
        display: flex;
        align-items: center;
        justify-content: flex-start;
      "
    >
      <el-col :span="3">
        <span>制单人：{{ userStore.name }}</span>
      </el-col>
      <el-col :span="6">
        <el-row
          :gutter="8"
          style="
            display: flex;
            align-items: center;
            justify-content: flex-start;
          "
        >
          <el-col :span="10">
            <span>合计金额：{{ totalAmount?totalAmount.toFixed(4):0 }}</span>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="returnedPurchase">
import {
  submitApproval,
  addPurchaseinventory,
  getInit,
  getInitBusNo,
  delPurchaseinventory,
  getPharmacyList,
  getCount,
  purchaseInventoryApproved,
  reject,
  getDispensaryList,
  getMedicineList,
  getPurchaseinventoryList,
  getpurchaseInventoryDetail
} from "./components/api";
import PopoverList from "@/components/OpenHis/popoverList/index.vue";
import MedicineList from "./components/transferManagement.vue";
import { formatDate,formatDateymd } from "@/utils/index";
import useUserStore from "@/store/modules/user";
import { time } from "echarts";
import { useStore } from '@/store/store';
import useTagsViewStore from '@/store/modules/tagsView';
const tagsViewStore = useTagsViewStore();
const router = useRouter();
const route = useRoute();
const userStore = useUserStore();
const store = useStore();

const { proxy } = getCurrentInstance();
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
const rowList = ref([])
const olditemQuantity = ref("")
const oldreturnedQuantity = ref("")
const oldquantity = ref("")
const viewStatus = ref("")
const startTimeOld = ref("")
const endTimeOld = ref("")
const purchaseinventoryList = ref([]);
const open = ref(false);
const loading = ref(false);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const visible = ref(false);
const row = ref({});
const rowIndex = ref(-1);
const totalAmount = ref(0);
const editData = ref({});
const locationInventoryList = ref([])
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
    // supplierId: [{ required: true, message: '请选择供应商', trigger: 'change' }],
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
    price_text: [{ required: true, message: '单价不能为空', trigger: 'blur' }],
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
const price = ref(0);
const unitMap = ref({});
const disabledForm = computed(() => {
  return form.purchaseinventoryList.length > 0;
});

// watch(
//   () => store.currentReturnData,
//   (newVlaue) => {
//      console.log(newVlaue,111111)
//     if (newVlaue) {
     
//       editData.value.editRow = newVlaue?.editRow;
//       editData.value.item = newVlaue?.item;
//       edit();
//     } else {
//       editData.value = {};
//       show();
//     }
//   },
//   { immediate: true }
// );
watch(
  () => route.query.view,
  (newVlaue) => {
    getPurchaseinventoryTypeList()
    if(newVlaue){
      viewStatus.value = newVlaue
    }else{
      viewStatus.value = ""
    }
  },
  { immediate: true }
)
watch(
  () => form.purchaseinventoryList,
  (newVlaue) => {
    if(newVlaue&&newVlaue.length>0){
      if(viewStatus.value){
        handleTotalAmount()
      }
    }
  },
  { immediate: true }
);
// 挂载时绑定事件
onMounted(() => {
  if(route.query.view){
    viewStatus.value = route.query.view
  }else{
    viewStatus.value = ""
  }
  // if (Object.keys(editData.value).length === 0) {
  //   show();
  // }
  // document.addEventListener("click", handleClickOutside);
});

// 卸载时移除事件
// onUnmounted(() => {
  // document.removeEventListener("click", handleClickOutside);
// });
function handleScan(row,index){
  document.addEventListener('keydown', function(event) {
    // 检查是否为条码输入
    if (event.key === 'Enter' || event.key === 'Tab') { // 某些扫码枪在输入后可能发送Enter或Tab键
      console.log('Barcode detected:', event.target.value);
      form.purchaseinventoryList[index].traceNo = event.target.value
      // 处理条码数据
    }
  })
}
function addNewRow() {
  proxy.$refs["receiptHeaderRef"].validate((valid) => {
    if (valid) {
      // if (data.isAdding) {
      //   proxy.$message.warning("请先保存当前行后再新增！");
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
        supplierName:"",
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
  if(!row.itemTable){
    if (receiptHeaderForm.medicationType === '1') {
      row.itemTable = 'med_medication_definition';
    } else {
      row.itemTable = 'adm_device_definition';
    }
  }
  row.practitionerId = receiptHeaderForm.practitionerId
  let practitionerList = practitionerListOptions.value.filter(e=>{
    return e.value == receiptHeaderForm.practitionerId
  })
  row.itemQuantity = Number(row.itemQuantity)
  row.price = Number(row.price)
  row.totalPrice = Number(row.totalPrice)
  console.log(practitionerList,"practitionerList")
  row.practitionerName = (practitionerList&&practitionerList[0])?practitionerList[0].label:''
  row.busNo = receiptHeaderForm.busNo;
  row.originalBusNo = route.query.originalSupplyBusNo
  row.occurrenceTime = receiptHeaderForm.occurrenceTime;
  row.supplierId = receiptHeaderForm.supplierId;
  row.purposeTypeEnum = receiptHeaderForm.purposeTypeEnum;
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
  form.purchaseinventoryList[index] = {};
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
  form.purchaseinventoryList[index].locationInventoryList = locationList.value;
  unitMap.value['unitCode'] = rowValue.unitCode;
  unitMap.value['minUnitCode'] = rowValue.minUnitCode;
  unitMap.value['currentCode'] = rowValue.unitCode;
  form.purchaseinventoryList[index].itemQuantity = 0
  form.purchaseinventoryList[index].totalPrice = 0
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
      console.log(row);
      console.log(row.unitList);
      form.purchaseinventoryList[index].quantity =
        handleTotalQuantity(
          totalQuantity,
          row.partPercent,
          row.unitList.unitCode_dictText,
          row.unitList.minUnitCode_dictText
        ) || 0;
 
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
  })
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
  const validTime = getNextDayZeroTimeStamp(selectedTime);
  if (form.purchaseinventoryList[index].endTime) {
    const endTime = formatDateymd(form.purchaseinventoryList[index].endTime);
    const getNextDayZeroTime = getNextDayZeroTimeStamp(endTime);
    if (getNextDayZeroTime < validTime) {
      proxy.$message.warning('生产日期必须小于等于有效期！');
      form.purchaseinventoryList[index].startTime = startTimeOld.value;
      return;
    }
  }
}
function changeValEnd(val, index) {
  const selectedTimes = val;
  const validTimes = getNextDayZeroTimeStamp(selectedTimes);
  if (form.purchaseinventoryList[index].startTime) {
    const startTime = formatDateymd(form.purchaseinventoryList[index].startTime);
    const getNextDayZeroTimes = getNextDayZeroTimeStamp(startTime);
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
   // 防止点击和已选一样的问题
  if(!form.purchaseinventoryList[index].statusMaxvalue&&row.measurementUnitCode==value&&route.query.supplyBusNo){
    return     
  }
   if (row.minUnitCode == value) { //最小计量单位
    form.purchaseinventoryList[index].returnedQuantity = form.purchaseinventoryList[index].oldreturnedQuantity?form.purchaseinventoryList[index].oldreturnedQuantity:form.purchaseinventoryList[index].returnedQuantity
    form.purchaseinventoryList[index].quantity = form.purchaseinventoryList[index].oldquantity?form.purchaseinventoryList[index].oldquantity:form.purchaseinventoryList[index].quantity
    form.purchaseinventoryList[index].itemQuantity = 0
    form.purchaseinventoryList[index].totalPrice = 0
  //  form.purchaseinventoryList[index].itemQuantity = form.purchaseinventoryList[index].olditemQuantity?form.purchaseinventoryList[index].olditemQuantity:form.purchaseinventoryList[index].itemQuantity
   
    // 单价
    form.purchaseinventoryList[index].price =
    form.purchaseinventoryList[index].price / row.partPercent;
    form.purchaseinventoryList[index].price = form.purchaseinventoryList[index].price.toFixed(4);
    console.log(form.purchaseinventoryList[index],row,"111111111111111112")
    if(form.purchaseinventoryList[index].measurementUnitCode == form.purchaseinventoryList[index].unitList.unitCode){
      form.purchaseinventoryList[index].returnedQuantity =
      form.purchaseinventoryList[index].returnedQuantity * row.partPercent
    }
  } else {  // 切换成大的计量单位
    if(form.purchaseinventoryList[index].measurementUnitCode==form.purchaseinventoryList[index].unitList.unitCode){
      form.purchaseinventoryList[index].returnedQuantity = form.purchaseinventoryList[index].returnedQuantity / row.partPercent;
    }else{
      form.purchaseinventoryList[index].returnedQuantity =
      form.purchaseinventoryList[index].returnedQuantity * row.partPercent
    }

    form.purchaseinventoryList[index].itemQuantity = 0
    form.purchaseinventoryList[index].totalPrice = 0
    getMaxCounts(row,index)
  }
  form.purchaseinventoryList[index].statusMaxvalue = true
}
function getMaxCounts(row,index,counts){
    form.purchaseinventoryList[index].oldreturnedQuantity =  form.purchaseinventoryList[index].returnedQuantity
    // form.purchaseinventoryList[index].returnedQuantity =
    // form.purchaseinventoryList[index].returnedQuantity / row.partPercent;

    const integerPart1 = Math.floor(form.purchaseinventoryList[index].returnedQuantity); // 获取整数部分
    const decimalPart1 = form.purchaseinventoryList[index].returnedQuantity - integerPart1; // 获取小数部分

    if(decimalPart1){
      form.purchaseinventoryList[index].returnedQuantity = 
      integerPart1 + form.purchaseinventoryList[index].unitList.unitCode_dictText +
      (decimalPart1*row.partPercent).toFixed(0) + form.purchaseinventoryList[index].unitList.minUnitCode_dictText
    }

    form.purchaseinventoryList[index].oldquantity =  form.purchaseinventoryList[index].quantity
    form.purchaseinventoryList[index].quantity =
    form.purchaseinventoryList[index].quantity / row.partPercent;

    const integerPart2 = Math.floor(form.purchaseinventoryList[index].quantity); // 获取整数部分
    const decimalPart2 = form.purchaseinventoryList[index].quantity - integerPart2; // 获取小数部分

    if(decimalPart2){
      form.purchaseinventoryList[index].quantity = 
      integerPart2 + form.purchaseinventoryList[index].unitList.unitCode_dictText +
      (decimalPart2*row.partPercent).toFixed(0) + form.purchaseinventoryList[index].unitList.minUnitCode_dictText
    }
    //数量
    if(counts){
      form.purchaseinventoryList[index].olditemQuantity =  form.purchaseinventoryList[index].itemQuantity*row.partPercent
      form.purchaseinventoryList[index].itemMaxQuantity = form.purchaseinventoryList[index].itemQuantity
      const integerPart = Math.floor(form.purchaseinventoryList[index].itemQuantity); // 获取整数部分
      const decimalPart = form.purchaseinventoryList[index].itemQuantity - integerPart; // 获取小数部分
      console.log(integerPart,decimalPart,form.purchaseinventoryList[index].itemQuantity,121212121)
      if(decimalPart){
        form.purchaseinventoryList[index].itemQuantity = 
        integerPart + form.purchaseinventoryList[index].unitList.unitCode_dictText +
        (decimalPart*row.partPercent).toFixed(0) + form.purchaseinventoryList[index].minUnitCode_dictText
      }
    }else{
      form.purchaseinventoryList[index].price =
      form.purchaseinventoryList[index].price * row.partPercent;
      form.purchaseinventoryList[index].price = form.purchaseinventoryList[index].price.toFixed(4);
    } 
    console.log(form.purchaseinventoryList[index],"121212121")
}
// // 单位处理
// function handleUnitCodeChange(row, index, value) {
//   if (
//     unitMap.value['unitCode'] != value &&
//     unitMap.value['currentCode'] == unitMap.value['unitCode']
//   ) {
//     // 总库存数
//     form.purchaseinventoryList[index].quantity = totalQuantity;
//     // // 采购数量
//     // form.purchaseinventoryList[index].itemQuantity =
//     //   form.purchaseinventoryList[index].itemQuantity * row.partPercent || 0;
//     // // 采购单价
//     // form.purchaseinventoryList[index].price = (price.value / row.partPercent).toFixed(4);
//     // 采购数量
//     form.purchaseinventoryList[index].itemQuantity = undefined;
//     // 采购单价
//     form.purchaseinventoryList[index].price = undefined;
//   } else {
//     form.purchaseinventoryList[index].quantity =
//       handleTotalQuantity(
//         totalQuantity,
//         row.partPercent,
//         row.unitList.unitCode_dictText,
//         row.unitList.minUnitCode_dictText
//       ) || 0;
//     // form.purchaseinventoryList[index].itemQuantity =
//     //   form.purchaseinventoryList[index].itemQuantity / row.partPercent || 0;
//     // form.purchaseinventoryList[index].price = price.value;
//     form.purchaseinventoryList[index].itemQuantity = undefined;
//     form.purchaseinventoryList[index].price = undefined;
//   }
//   form.purchaseinventoryList[index].totalPrice = undefined;
//   unitMap.value['currentCode'] = value;
// }

// 计算总价
function handleTotalPrice(index) {
  let purchaseItem = form.purchaseinventoryList[index];
  if (purchaseItem.price > 0 && purchaseItem.itemQuantity > 0) {
    form.purchaseinventoryList[index].totalPrice = (
      purchaseItem.price * purchaseItem.itemQuantity
    ).toFixed(4);
  }
  if(form.purchaseinventoryList[index].itemQuantity==0){
    form.purchaseinventoryList[index].totalPrice = 0
  }
}
function handleSave(row, index) {
  rowList.value = []
  form.purchaseinventoryList.map((row,index)=>{
    if(row){
      handleBlur(row);
      proxy.$refs["receiptHeaderRef"].validate((valid) => {
        if (valid) {
          proxy.$refs['formRef'].validate((valid) => {
            if (valid) {
              let rows = JSON.parse(JSON.stringify(row))
              rows.unitList = [rows.unitList]
              rows.price = rows.price_text
              delete rows.price_text
              rowList.value.push(JSON.parse(JSON.stringify(rows)))
              if(rowList._rawValue&&rowList._rawValue.length == form.purchaseinventoryList.length){
                addPurchaseinventorys(rowList._rawValue)
              }
            }
          });
        }
      })
    }
  })
}
function addPurchaseinventorys(rowList){
  addPurchaseinventory(JSON.parse(JSON.stringify(rowList))).then((res) => {
    // 当前行没有id视为首次新增
    if (res.data) {
      proxy.$message.success('保存成功！');
      form.purchaseinventoryList.map((row,index)=>{
        console.log(row,"res.data")
        form.purchaseinventoryList[index].id = res.data[index]
        form.purchaseinventoryList[index].isSave = true;
      })
    }
  });
}
// function handleSave(row, index) {
//   handleBlur(row);
//   proxy.$refs["receiptHeaderRef"].validate((valid) => {
//     if (valid) {
//       proxy.$refs['formRef'].validate((valid) => {
//         if (valid) {
//           let rows = JSON.parse(JSON.stringify(row))
//           rows.unitList = [rows.unitList]
//           rows.price = rows.price_text
//           delete rows.price_text
//           addPurchaseinventory(rows).then((res) => {
//             // 当前行没有id视为首次新增
//             if (!row.id) {
//               data.isAdding = false; // 允许新增下一行
//             }
//             if (res.data) {
//               proxy.$message.success('保存成功！');
//               form.purchaseinventoryList[index].id = res.data;
//               form.purchaseinventoryList[index].isSave = true;
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
  let ids  = []
  if(selectedRows.value[0].id){
     ids = selectedRows.value.map((item) => {
      return item.id
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
  if(form.purchaseinventoryList.length==0){
    tagsViewStore.delView(router.currentRoute.value)
    router.replace({ path: 'purchaseinventory' })
  }
}

/**计算合计金额 */
function handleTotalAmount() {
  totalAmount.value = form.purchaseinventoryList.reduce((accumulator, currentRow) => {
    return (accumulator + (Number(currentRow.totalPrice) || 0))
  }, 0);
}

/** 重置操作表单 */
function reset() {
 
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
  })
  getpurchaseInventoryDetails()
  
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
  receiptHeaderForm.occurrenceTime = formatDate(editData.value.editRow.occurrenceTime);
  receiptHeaderForm.purposeTypeEnum = editData.value.editRow.purposeTypeEnum?editData.value.editRow.purposeTypeEnum.toString():editData.value.item[0].purposeTypeEnum.toString()
  receiptHeaderForm.medicationType =
    editData.value.editRow.itemTable == 'med_medication_definition' ? '1' : '2';
  total.value = form.purchaseinventoryList.length;
 handleChangeLocationType(editData.value.editRow.purposeTypeEnum?editData.value.editRow.purposeTypeEnum.toString():editData.value.item[0].purposeTypeEnum.toString())
  setTimeout(() => {
    form.purchaseinventoryList = editData.value.item.map((item) => {
      return {
        ...item,
        name: item.itemName,
        volume: item.totalVolume,
        manufacturer: item.supplierName,
        quantity: item.totalQuantity,
        locationInventoryList: locationList.value,
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
  if(route.query.view){
    viewStatus.value = route.query.view
    receiptHeaderForm.busNo = route.query.originalSupplyBusNo
  }else if(route.query.supplyBusNo){
    receiptHeaderForm.busNo = route.query.supplyBusNo
  }else{
    getInitBusNo().then((res) => {
    receiptHeaderForm.busNo = res.data.busNo;
  })
  }
  getInit().then((response) => {
    itemTypeOptions.value = response.data.itemTypeOptions; // 活动标记
    practitionerListOptions.value = response.data.practitionerListOptions; // 预约必填标记
    supplierListOptions.value = response.data.supplierListOptions; // 供应商列表
  })
}
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}
// 驳回
function handleReject() {
  reject(route.query.originalSupplyBusNo).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess("操作成功");
      tagsViewStore.delView(router.currentRoute.value);
      store.clearCurrentReturnData();
      // 跳转到审核页面
      router.replace({ path: '/aaaa/medicationmanagement/billapproval',query:{type:'returnedPurchase'}});
    }
  });
}
function handelApply() {
  purchaseInventoryApproved(route.query.originalSupplyBusNo).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess("操作成功");
      tagsViewStore.delView(router.currentRoute.value);
      store.clearCurrentReturnData();
      // 跳转到审核页面
      router.replace({ path: '/aaaa/medicationmanagement/billapproval',query:{type:'returnedPurchase'}});
    }
  })
}
function toLastView(visitedViews, view) {
  const latestView = visitedViews.slice(-1)[0]
  if (latestView) {
    router.push(latestView.fullPath)
  } else {
    if (view.name === 'Dashboard') {
      router.replace({ path: '/redirect' + view.fullPath })
    }else {
      router.push('/')
    }
  }
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
        router.replace({ path: 'purchaseinventory' })
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


// 获取详情
function getpurchaseInventoryDetails(){
  console.log(route,router,receiptHeaderForm.busNo,receiptHeaderForm.busNo,12222)
  if(route.query.originalSupplyBusNo){
   let busNo = 0

    if(route.query.originalSupplyBusNo&&route.query.supplyBusNo){
      busNo = route.query.supplyBusNo
    }else if(route.query.originalSupplyBusNo&&!route.query.supplyBusNo){
      busNo = route.query.originalSupplyBusNo
    }
    getpurchaseInventoryDetail(busNo).then((res) => {
      form.purchaseinventoryList = res.data
      form.purchaseinventoryList.map((e,index)=>{
        e.isSave = e.id?true:false
        form.purchaseinventoryList[index].statusMaxvalue =  false
        e.volume = e.totalVolume
        e.name = e.itemName
        e.price_text = e.price
        if(e.purposeTypeEnum){
          warehous_type.value.map(item=>{
            if(item.value == e.purposeTypeEnum ){
              receiptHeaderForm.purposeTypeEnum = item.value
            }
          })
          handleChangePurposeTypeEnum(e.purposeTypeEnum)
          console.log(e.purposeTypeEnum,"e.purposeTypeEnum")
        }

        if(e.supplierId){
          receiptHeaderForm.supplierName = e.supplierName
          receiptHeaderForm.supplierId = e.supplierId
        }
        if(e.practitionerId){
          receiptHeaderForm.practitionerId = e.practitionerId
        }
        if(e.itemType){
          purchase_type.value.map(item=>{
            if(item.value == e.itemType ){
              // receiptHeaderForm.medicationType = item.label
              receiptHeaderForm.medicationType = item.value
            }
          })
        }
        e.returnedQuantity =  e.returnedQuantity? e.returnedQuantity:0
        e.quantity =  e.quantity?e.quantity:0
        // if(e.price){
        //   if (form.purchaseinventoryList[index].price > 0 && form.purchaseinventoryList[index].itemQuantity > 0) {
        //     form.purchaseinventoryList[index].totalPrice =
        //     form.purchaseinventoryList[index].price * form.purchaseinventoryList[index].itemQuantity;
        //   }
        // }
        if(e.unitList&&e.unitCode){
          form.purchaseinventoryList[index].unitList = e.unitList[0]   //计量单位回显数组
          form.purchaseinventoryList[index].unitCode = form.purchaseinventoryList[index].measurementUnitCode
          form.purchaseinventoryList[index].unitCode_dictText = form.purchaseinventoryList[index].measurementUnitCode_dictText;
          if(form.purchaseinventoryList[index].measurementUnitCode == form.purchaseinventoryList[index].unitList.unitCode){  // 回显大单位
            getMaxCounts(e,index,1)
          }
        }
        if(e.price){
          // 单价   大单位单价
          if(form.purchaseinventoryList[index].measurementUnitCode == form.purchaseinventoryList[index].unitList.minUnitCode){
            form.purchaseinventoryList[index].price = form.purchaseinventoryList[index].price / form.purchaseinventoryList[index].partPercent|| "";
            form.purchaseinventoryList[index].price = form.purchaseinventoryList[index].price.toFixed(4);
          }else{
            if(form.purchaseinventoryList[index].price>1){
              form.purchaseinventoryList[index].price = form.purchaseinventoryList[index].price.toFixed(4);
            }
          }
          if (form.purchaseinventoryList[index].price > 0 && form.purchaseinventoryList[index].itemQuantity > 0) {
            form.purchaseinventoryList[index].totalPrice =
            form.purchaseinventoryList[index].price * form.purchaseinventoryList[index].itemQuantity;
            form.purchaseinventoryList[index].totalPrice = form.purchaseinventoryList[index].totalPrice.toFixed(4);
          }
        }else{
          form.purchaseinventoryList[index].price = 0
          form.purchaseinventoryList[index].totalPrice = 0
        }
      })
      console.log(res.data,receiptHeaderForm,"获取详情");
    })
  }
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

// 切换仓库类型获取药房/药库列表   目的仓库切换
function handleChangePurposeTypeEnum(value,type) {
  if (value == 16) {
    getPharmacyList().then((res) => {
     locationInventoryList.value = res.data
      if(!route.query.supplyBusNo&&!type){
        receiptHeaderForm.purposeLocationId = ''
        receiptHeaderForm.purposeLocationId1 = ''
      }
    });
  } else if (value == 11) {
    getDispensaryList().then((res) => {
      locationInventoryList.value = res.data
      if(!route.query.supplyBusNo&&!type){
        receiptHeaderForm.purposeLocationId = ''
        receiptHeaderForm.purposeLocationId1 = ''
      }         
    });
  }
}
//  // 源仓库切换
// function handleChangeSourceTypeEnum(value) {
//   if (value == 16) {
//     getPharmacyList().then((res) => {
//       sourceTypeListOptions.value = res.data;
//     });
//   } else if (value == 11) {
//     getDispensaryList().then((res) => {
//       sourceTypeListOptions.value = res.data;
//     });
//   }
// }

function getBusNoInitList() {
  console.log(route,route.query,"路由传值")
  if(route.query.supplyBusNo){
    receiptHeaderForm.busNo = route.query.supplyBusNo
  }else{
    if(!sessionStorage.getItem('busNo')){
      // getBusNoInit().then((response) => {
      //   console.log(response,'response',response.data)
      //   receiptHeaderForm.busNo = response.data.busNo;
      //   sessionStorage.setItem('busNo', receiptHeaderForm.busNo)
      //   // busNoAdd.value = response.data.busNo; // 单据号新增
      // })
    }else{
      receiptHeaderForm.busNo = sessionStorage.getItem('busNo')
    }
  }
 
}
 
// getTransferProductTypeList();
// getBusNoInitList()
// getPurchaseinventoryLists()
getPurchaseinventoryTypeList()
getpurchaseInventoryDetails()
// defineExpose({
  // show,
//   edit,
// });
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