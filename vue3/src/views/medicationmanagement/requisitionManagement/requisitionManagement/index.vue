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
        <!-- v-if="scope.row.statusEnum == '1' || scope.row.statusEnum == '9'" -->
        <!-- v-hasPermi="['system:user:remove']" -->
        <el-button
          plain
          type="primary"
          icon="Plus"
          @click="handleSubmitApproval()"
          
          >提交审核</el-button
        >
        <!-- <el-button type="primary" plain icon="Plus" @click="submitAudit"
          >提交审核</el-button
        > -->
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Printer" :disabled="multiple" @click="handleDelete"
          >打印</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="EditPen"
          @click="handleTotalAmount"
          >计算金额</el-button
        >
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
          style="width: 260px"
          disabled
        />
      </el-form-item>
      <el-form-item label="领用部门：" prop="locationId" label-width="120px">
        <el-select
          v-model="receiptHeaderForm.locationId"
          placeholder=""
          clearable
          style="width: 150px"
          :disabled="data.isEdit"
          @change="handleChangepurposeLocationId"
        >
          <el-option
            v-for="supplier in issueDepartmentDto"
            :key="supplier.locationId"
            :label="supplier.name"
            :value="supplier.locationId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="部门经手人：" prop="practitionerId" label-width="160px">
        <el-select
          v-model="receiptHeaderForm.practitionerId"
          placeholder=""
          clearable
          style="width: 150px"
          :disabled="data.isEdit"
        >
          <!-- purchase_type -->
          <el-option
            v-for="supplier in practitionerListOptions"
            :key="supplier.value"
            :label="supplier.label"
            :value="supplier.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="单据日期：">
        <el-date-picker
          v-model="receiptHeaderForm.occurrenceTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="datetime"
          :disabled="data.isEdit"
        />
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
          <!-- 字典 purchase_type -->
          <el-option
            v-for="itemType in purchase_type"
            :key="itemType.value"
            :label="itemType.label"
            :value="itemType.value"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <el-tabs type="border-card">
      <el-tab-pane label="领用单据明细">
        <el-row :gutter="10" class="mb8" v-if="!viewStatus">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="addNewRow">添加行</el-button>
          </el-col>
          <!-- <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="confirmCurrentRow"
              >确认当前行</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="cancelEditRow"
              >取消行编辑</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="cancelEditAllRow"
              >取消所有行编辑</el-button
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
            @row-click="handleRowClick"
            ref="tableRef"
          >
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column label="项目" align="center" key="name" prop="name" width="200" fixed>
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.name`"
                  :rules="tableRules.name"
                  required
                >
                  <el-input
                    v-if="viewStatus == 'view'"
                    v-model="scope.row.name"
                    placeholder=""
                    disabled
                  />
                  <PopoverList v-else  @search="handleSearch" :width="1000" :modelValue="scope.row.name">
                    <template #popover-content="{}">
                      <transferManagement
                        @selectRow="(row) => selectRow(row, scope.$index)"
                        :searchKey="medicineSearchKey"
                        :itemType="itemType"
                      />
                    </template>
                  </PopoverList>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="规格" align="center" key="volume" prop="volume" width="200">
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.volume`"
                  :rules="tableRules.volume"
                >
                  <el-input v-model="scope.row.volume" placeholder="" disabled/>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="厂家/产地"
              align="center"
              key="manufacturer"
              prop="manufacturer"
              :show-overflow-tooltip="true"
              width="240"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.manufacturer`"
                  :rules="tableRules.manufacturer"
                >
                  <el-input v-model="scope.row.manufacturer" placeholder="" disabled/>
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
                  <el-input v-model="scope.row.lotNumber" placeholder="" disabled />
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="仓库"
              align="center"
              key="sourceLocationId"
              prop="sourceLocationId"
              :show-overflow-tooltip="true"
              width="180"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.sourceLocationId`"
                  :rules="tableRules.sourceLocationId"
                  required
                >
                  <div class="select_wrapper_div">
                    <el-select
                      :disabled="viewStatus == 'view'"
                      v-model="scope.row.sourceLocationId"
                      placeholder="请选择仓库"
                      :class="{ 'error-border': scope.row.error }"
                      clearable
                    >
                      <el-option
                        v-for="(item, index) in purposeTypeListOptions"
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
                      :disabled="viewStatus == 'view'"
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
            <el-table-column label="单价 " align="center" key="price" prop="price" width="130">
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.price`"
                  :rules="tableRules.price"
                >
                  <div class="select_wrapper_div">
                    <el-input
                      disabled
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
                  required
                >
                  <div class="select_wrapper_div">
                    <el-select
                      :disabled="viewStatus == 'view'"
                      v-model="scope.row.unitCode"
                      placeholder=" "
                      :class="{ 'error-border': scope.row.error }"
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
              label="总库存数量"
              align="center"
              key="totalQuantity"
              prop="totalQuantity"
              width="120"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.totalQuantity`"
                  :rules="tableRules.totalQuantity"
                >
                  <el-input v-model="scope.row.totalQuantity" placeholder="" disabled />
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="领用数量"
              align="center"
              key="itemQuantity"
              prop="itemQuantity"
              width="120"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.itemQuantity`"
                  :rules="tableRules.itemQuantity"
                  required
                >
                  <div class="select_wrapper_div">
                    <el-input
                      v-model="scope.row.itemQuantity"
                      placeholder=""
                      :disabled="viewStatus == 'view'"
                      @blur="handleTotalPrice(scope.$index)"
                      :class="{ 'error-border': scope.row.error }"
                    />
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
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.totalPrice`"
                >
                  <el-input
                    disabled
                    v-model="scope.row.totalPrice"
                    placeholder=""
                  />
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="国家医保码" align="center" key="ybNo" prop="ybNo" width="220">
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.ybNo`"
                  :rules="tableRules.ybNo"
                  disabled
                >
                  <el-input v-model="scope.row.ybNo" placeholder="" disabled/>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="备注" align="center" key="remake" prop="remake" width="130">
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.remake`"
                  :rules="tableRules.remake"
                >
                  <el-input v-model="scope.row.remake" placeholder="" :disabled="viewStatus == 'view'"/>
                </el-form-item>
              </template>
            </el-table-column>
            <!-- <el-table-column
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
                  @click="handleSave(scope.row, scope.$index)"
                  
                  >保存</el-button
                >
              </template>
            </el-table-column> -->
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
        <el-row :gutter="8" style="display: flex; align-items: center; justify-content: flex-start">
          <el-col :span="10">
            <span>合计金额：{{ totalAmount?totalAmount.toFixed(4):0 }}</span>
          </el-col>
          <!-- <el-col :span="10">
            <el-input v-model="totalAmount" placeholder="" disabled />
          </el-col> -->
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="requisitionManagement">
import {
  getPharmacyCabinetList,
  submitApproval,
  addTransferProduct,
  getInit,
  delTransferProduct,
  getPharmacyList,
  getCount,
  getBusNoInit,
  getDispensaryList,
  getMedicineList,
  getTransferProductDetail,
  requisitionIssueApproved,
  reject
} from "../components/api";
import PopoverList from '@/components/OpenHis/popoverList/index.vue';
import transferManagement from '../components/list.vue';
import { formatDate } from '@/utils/index';
import useUserStore from "@/store/modules/user";
import { useStore } from '@/store/store';
import useTagsViewStore from '@/store/modules/tagsView';
const tagsViewStore = useTagsViewStore();
const store = useStore();

const router = useRouter();
const route = useRoute();
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

const viewStatus = ref("")
const purposeLocationIdListOptions = ref(undefined); //目的仓库
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
const purposeTypeListOptions = ref(undefined);
const props = defineProps({
  sourceTypeListOptions: {
    type: Object,
    required: false,
  },
  purposeTypeListOptions: {
    type: Object,
    required: false,
  },
  categoryListOptions: {
    type: Object,
    required: false,
  },
  busNoAdd: {
    type: String,
    required: false,
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
const rowList = ref([])
const data = reactive({
  isEdit: false,
  isAdding: true,
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    searchKey: undefined, // 供应商名称
    busNo: undefined, // 编码
    statusEnum: undefined, // 状态
    // supplierId: undefined, //
    occurrenceTime: undefined, // 申请时间
    // practitionerId: undefined, // 经手人ID
  },
  rules: {
    medicationType: [{ required: true, message: '请选择药品类型', trigger: 'change' }],
    locationId: [{ required: true, message: '请选择领用部门', trigger: 'change' }],
    practitionerId: [{ required: true, message: '请选择部门经手人', trigger: 'change' }],
  },
  tableRules: {
    name: [{ required: true, message: '项目不能为空', trigger: 'change' }],
    sourceLocationId: [{ required: true, message: '仓库不能为空', trigger: 'change' }],
    unitCode: [{ required: true, message: '计量单位不能为空', trigger: 'change' }],

    itemQuantity: [{ required: true, message: '领用数量不能为空', trigger: 'blur' }],
    // lotNumber: [
    //   { required: true, message: "产品批号不能为空", trigger: "blur" },
    // ],
    // ybNo: [{ required: true, message: "追溯码不能为空", trigger: "blur" }],
    // startTime: [
    //   { required: true, message: "开始时间不能为空", trigger: "blur" },
    // ],
    // endTime: [{ required: true, message: "结束时间不能为空", trigger: "blur" }],
    // price: [{ required: true, message: "单价不能为空", trigger: "blur" }],
    // totalPrice: [{ required: true, message: "总价不能为空", trigger: "blur" }],
  },
});

const { queryParams, rules, tableRules } = toRefs(data);
const issueDepartmentDto = ref(undefined);
const supplierListOptions = ref(undefined);
const sourceLocationStoreIdListOptions = ref(undefined);
const purposeLocationStoreIdListOptions = ref(undefined);
const practitionerListOptions = ref(undefined);
const supplyStatusOptions = ref(undefined);
const selectedRows = ref([]); // 用于存储选中的行
const emit = defineEmits(['refresh']);
const tableRef = ref(undefined); // 表格引用
const currentRow = ref(undefined); // 当前操作的行
const medicineSearchKey = ref('');
const itemType = ref('');

watch(
  () => store.currentDataLYCK,
  (newVlaue) => {
    if (newVlaue&&!route.query.supplyBusNo) {
      form.purchaseinventoryList = newVlaue?.purchaseinventoryList
      receiptHeaderForm.busNo = newVlaue?.receiptHeaderForm.busNo
      receiptHeaderForm.occurrenceTime = newVlaue?.receiptHeaderForm.occurrenceTime
      receiptHeaderForm.locationId = newVlaue?.receiptHeaderForm.locationId
      receiptHeaderForm.practitionerId = newVlaue?.receiptHeaderForm.practitionerId
      getPharmacyCabinetList().then((res) => {
        purposeTypeListOptions.value = res.data
      })
      receiptHeaderForm.medicationType = newVlaue?.receiptHeaderForm.medicationType
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
  document.addEventListener('click', handleClickOutside);
});

// 卸载时移除事件
onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside);
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
        supplyBusNo: '',
        occurrenceTime: '',
        typeEnum_enumText: '',
        statusEnum_enumText: '',
        // sourceLocationId:"", // 源仓库
        // purposeLocationId:"", //目的仓库
        // sourceLocationName:"",
        purposeLocationName: '',
        approverId_dictText: '',
        applicantId_dictText: '',
        approvalTime: '',
        applyTime: '',
        applicantId: '',
        supplierId: '',
        detailJson: '',
        createTime: '',
        itemTable: '',
        itemQuantity: '',
        itemId: '',
        unitCode: '',
        remake: '',
        purposeTypeEnum: '',
        purposeLocationId: '',
        purposeLocationStoreId: '',
        practitionerId: '',
        practitionerName:'',
        applicantId: '',
        traceNo: '',
        traceNo: '',
        // locationName:"",
        ybNo: '',
        invoiceNo: '',
        startTime: '',
        endTime: '',
        price: '',
        totalPrice: '',
        datailJson: '',
        supplierId: '',
        totalQuantity: '',
        // unitList: {unitCode: "2", unitCode_dictText: "盒", minUnitCode: "1", minUnitCode_dictText: "片"},
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

// 获取表格数据
function handleBlur(row, index) {
  let hasError = false;
  for (let key in row) {
    if(!row[key]){
      row[key] = ''
    }
  }
  if(receiptHeaderForm.medicationType){
    if(!row.itemTable){
      if (receiptHeaderForm.medicationType == 1) {
        row.itemTable = "med_medication_definition";
      } else {
        row.itemTable = "adm_device_definition";
      }
    }
  }

  row.busNo = receiptHeaderForm.busNo;
  row.occurrenceTime = receiptHeaderForm.occurrenceTime;

  // 领用仓库
  let sourceLocation = purposeTypeListOptions.value.filter((e) => {
    return e.id == row.sourceLocationId;
  });

 
  row.sourceLocationId = sourceLocation[0].id;

  row.sourceTypeEnum = sourceLocation[0].formEnum;

  // 领用部门
  let purposeLocation = issueDepartmentDto.value.filter((e) => {
    return e.locationId == receiptHeaderForm.locationId;
  });
  console.log('purposeLocation',purposeLocation);
  row.purposeLocationId = purposeLocation[0].locationId;

  row.purposeTypeEnum = purposeLocation[0].typeEnum;

  // 部门经手人
  // row.practitionerId = receiptHeaderForm.practitionerId;
  let practitioner = practitionerListOptions.value.filter((e) => {
    return e.value == receiptHeaderForm.practitionerId;
  });
  console.log('practitioner',practitioner);
  row.practitionerId = practitioner[0].value
  row.practitionerName = practitioner[0].label
  console.log('row111111',row);
  // row.practitionerId = practitioner && practitioner[0].id ? practitioner[0].id : '';
  // 申请人
  row.applicantId = userStore.id;
}

// 详情
function getTransferProductDetails() {
  if (route.query.supplyBusNo) {
    // 编辑
    data.isEdit = true
    getTransferProductDetail(receiptHeaderForm.busNo).then((res) => {
      form.purchaseinventoryList = res.data;
      form.purchaseinventoryList.map((e, index) => {
        form.purchaseinventoryList[index].statusMaxvalue =  false
        e.volume = e.totalVolume;
        e.name = e.itemName
        if (e.purposeLocationId) { //目的仓库
          receiptHeaderForm.locationId = e.purposeLocationId;
        }
        if (e.practitionerId) {  // 经手人
          receiptHeaderForm.practitionerId = e.practitionerId
        }
        if (e.itemType) {
          receiptHeaderForm.medicationType = e.itemType
        }
        getPharmacyCabinetList().then((res) => {
          purposeTypeListOptions.value = res.data;
          // form.purchaseinventoryList[index].purposeLocationId = '';
          // handleLocationClick(1, row, index)
        });
        form.purchaseinventoryList[index].totalQuantity = form.purchaseinventoryList[index].totalQuantity?form.purchaseinventoryList[index].totalQuantity:0
        if(e.unitList&&e.unitCode){
          form.purchaseinventoryList[index].unitList = e.unitList[0]   //计量单位回显数组
          form.purchaseinventoryList[index].unitCode = form.purchaseinventoryList[index].measurementUnitCode_dictText;
          form.purchaseinventoryList[index].unitCode_dictText = form.purchaseinventoryList[index].measurementUnitCode_dictText;
          if(form.purchaseinventoryList[index].measurementUnitCode == form.purchaseinventoryList[index].unitList.unitCode){  // 回显大单位
            getMaxCounts(e,index,1)
          }
          console.log(form.purchaseinventoryList, e.unitList,"获取详情12");
        }
        if(e.price){
          // 单价   大单位单价
          if(form.purchaseinventoryList[index].measurementUnitCode == form.purchaseinventoryList[index].unitList.minUnitCode){
            form.purchaseinventoryList[index].price = form.purchaseinventoryList[index].price / form.purchaseinventoryList[index].partPercent|| "";
            form.purchaseinventoryList[index].price = form.purchaseinventoryList[index].price.toFixed(4);
            // parseFloat(form.purchaseinventoryList[index].price.toFixed(4))
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
    });
  }
}

// 取消行编辑
function cancelEditRow() {
  const findIndexId = form.purchaseinventoryList.findIndex((e) => e.id == currentRow.value.id);
  form.purchaseinventoryList[findIndexId] = {};
  // currentRow.value
}
// 点击行时记录当前行
function handleRowClick(row) {
  // getMedicineList({ pageNum:1,
  //     pageSize:50, purchaseFlag:0, }).then((res) => {
  // });
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
  addTransferProduct(row).then((response) => {
    reset();
    data.isAdding = false; // 允许新增下一行
    proxy.$message.success('保存成功！');
    visible.value = false;
  });
}
// 驳回
function handleReject() {
  reject(route.query.supplyBusNo).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess("操作成功");
      tagsViewStore.delView(router.currentRoute.value);
      store.clearCurrentDataLYCK();
      // 跳转到审核页面
      router.replace({ path: '/aaaa/medicationmanagement/billapproval',query:{type:'requisitionManagement'}});
    }
  });
}
function handelApply() {
  requisitionIssueApproved(route.query.supplyBusNo).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess("操作成功");
      tagsViewStore.delView(router.currentRoute.value);
      store.clearCurrentDataLYCK();
      // 跳转到审核页面
      router.replace({ path: '/aaaa/medicationmanagement/billapproval',query:{type:'requisitionManagement'}});
    }
  })
}
/** 提交审核按钮 */
function handleSubmitApproval() {
  let length = form.purchaseinventoryList.length;
  if (length < 1) {
    proxy.$modal.msgWarning("请先添加单据");
  } else if (!form.purchaseinventoryList[length - 1].isSave) {
    proxy.$modal.msgWarning("第" + length + "行单据未保存，请先保存");
  } else {
    submitApproval(receiptHeaderForm.busNo).then((response) => {
      proxy.$modal.msgSuccess('提交审批成功');
      tagsViewStore.delView(router.currentRoute.value);
      store.clearCurrentDataLYCK();
      router.replace({ path: 'requisitionManagementList' });
    })
  }
}
function toLastView(visitedViews, view) {
  const latestView = visitedViews.slice(-1)[0]
  if(view.name== 'RequisitionManagement'){ //调拨单据号删除
    sessionStorage.setItem('busNoLY',"")
  }  
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
// 药品列表搜索
function handleSearch(value) {
  medicineSearchKey.value = value;
}

// 选择药品
function selectRow(rowValue, index) {
  rowIndex.value = index;
  // form.purchaseinventoryList[index].sourceLocationId = receiptHeaderForm.sourceLocationId
  // form.purchaseinventoryList[index].purposeLocationId = receiptHeaderForm.purposeLocationId
  form.purchaseinventoryList[index].itemId = rowValue.definitionId;
  form.purchaseinventoryList[index].name = rowValue.name;
  form.purchaseinventoryList[index].volume = rowValue.volume;
  form.purchaseinventoryList[index].minUnitCode = rowValue.minUnitCode;
  form.purchaseinventoryList[index].unitCode = rowValue.unitCode;
  form.purchaseinventoryList[index].manufacturer = rowValue.manufacturer;
  form.purchaseinventoryList[index].partPercent = rowValue.partPercent;
  form.purchaseinventoryList[index].unitList = rowValue.unitList[0];
  form.purchaseinventoryList[index].lotNumber = rowValue.lotNumber;
  form.purchaseinventoryList[index].ybNo = rowValue.ybNo;
  form.purchaseinventoryList[index].sourceLocationId = ''
  getPharmacyCabinetList().then((res) => {
    purposeTypeListOptions.value = res.data
    // handleLocationClick(1, row, index)
  })
  form.purchaseinventoryList[index].itemQuantity = 0
  form.purchaseinventoryList[index].totalPrice = 0
  store.setCurrentDataLYCK({ purchaseinventoryList: form.purchaseinventoryList, receiptHeaderForm: receiptHeaderForm })
}

// 选择仓库
function handleLocationClick(item, row, index) {
  getCount({
    itemId: row.itemId,
    orgLocationId: form.purchaseinventoryList[index].sourceLocationId,
    // objLocationId:purposeLocationId,
    lotNumber: form.purchaseinventoryList[index].lotNumber,
  }).then((res) => {
    if (res.data && res.data.length > 0) {
      form.purchaseinventoryList[index].itemTable = res.data[0].itemTable || '';
      form.purchaseinventoryList[index].totalQuantity = res.data[0].orgQuantity || 0;
     
      if (res.data[0].price) {
        form.purchaseinventoryList[index].price = res.data[0].price.toFixed(4);
      }else{
        form.purchaseinventoryList[index].price = 0
      }
      // 获取供应商id
      form.purchaseinventoryList[index].supplierId = res.data[0].supplierId || '';
      // 生产日期
      form.purchaseinventoryList[index].startTime = res.data[0].productionDate;
      // 有效期
      form.purchaseinventoryList[index].endTime = res.data[0].expirationDate;
      form.purchaseinventoryList[index].unitCode = form.purchaseinventoryList[index].unitList.minUnitCode
      form.purchaseinventoryList[index].unitCode_dictText = form.purchaseinventoryList[index].unitList.minUnitCode_dictText
      // 单价   大单位单价
      console.log(form.purchaseinventoryList[index].unitCode==form.purchaseinventoryList[index].unitList.minUnitCode,1212121)
      if(form.purchaseinventoryList[index].unitCode==form.purchaseinventoryList[index].unitList.minUnitCode){
        form.purchaseinventoryList[index].price = res.data[0].price / form.purchaseinventoryList[index].partPercent|| "";
        form.purchaseinventoryList[index].price = form.purchaseinventoryList[index].price.toFixed(4);
        // parseFloat(form.purchaseinventoryList[index].price.toFixed(4))
      }else{
        console.log(form.purchaseinventoryList[index].price>1,1212,form.purchaseinventoryList[index].price)
        if(form.purchaseinventoryList[index].price>1){
          form.purchaseinventoryList[index].price = form.purchaseinventoryList[index].price.toFixed(4);
        }
      }
      if (form.purchaseinventoryList[index].totalQuantity == 0) {
        proxy.$message.warning('仓库数量为0，无法调用！');
        return;
      }
    } else {
      form.purchaseinventoryList[index].totalQuantity = 0;
      form.purchaseinventoryList[index].price = 0;
      // if(form.purchaseinventoryList[index].totalQuantity==0){
      proxy.$message.warning('仓库数量为0，无法调用！');
      // }
    }
  })
  store.setCurrentDataLYCK({ purchaseinventoryList: form.purchaseinventoryList, receiptHeaderForm: receiptHeaderForm })
}
// 切换仓库类型获取药房/药库列表
// function handleChangeLocationType(value) {
//   if (value == 16) {
//     getPharmacyList().then((res) => {
//       locationList.value = res.data;
//     });
//   } else if (value == 11) {
//     getDispensaryList().then((res) => {
//       locationList.value = res.data;
//     });
//   }
// }

// 单位处理
function handleUnitCodeChange(row, index, value) {
    // 防止点击和已选一样的问题
  if(!form.purchaseinventoryList[index].statusMaxvalue&&row.measurementUnitCode==value&&route.query.supplyBusNo){
    return     
  }
  if (row.minUnitCode == value) {
    //最小计量单位
    // form.purchaseinventoryList[index].totalQuantity = oldtotalQuantity.value;
    // form.purchaseinventoryList[index].itemQuantity = olditemQuantity.value;
    form.purchaseinventoryList[index].totalQuantity = form.purchaseinventoryList[index].oldtotalQuantity?form.purchaseinventoryList[index].oldtotalQuantity:form.purchaseinventoryList[index].totalQuantity
    form.purchaseinventoryList[index].itemQuantity = form.purchaseinventoryList[index].olditemQuantity?form.purchaseinventoryList[index].olditemQuantity:form.purchaseinventoryList[index].itemQuantity
    form.purchaseinventoryList[index].itemQuantity = 0
    form.purchaseinventoryList[index].totalPrice = 0
    // 单价
    form.purchaseinventoryList[index].price =
    form.purchaseinventoryList[index].price / row.partPercent;
    form.purchaseinventoryList[index].price = form.purchaseinventoryList[index].price.toFixed(4);
    // if (!form.purchaseinventoryList[index].itemQuantity) {
    // }
  } else {
    form.purchaseinventoryList[index].itemQuantity = 0
    form.purchaseinventoryList[index].totalPrice = 0
    getMaxCounts(row,index)
  }
  form.purchaseinventoryList[index].statusMaxvalue = true
  store.setCurrentDataLYCK({ purchaseinventoryList: form.purchaseinventoryList, receiptHeaderForm: receiptHeaderForm })
    // 切换成大的计量单位
  //   oldtotalQuantity.value = form.purchaseinventoryList[index].totalQuantity;
  //   form.purchaseinventoryList[index].totalQuantity =
  //     form.purchaseinventoryList[index].totalQuantity / row.partPercent;

  //   const integerPart1 = Math.floor(form.purchaseinventoryList[index].totalQuantity); // 获取整数部分
  //   const decimalPart1 = form.purchaseinventoryList[index].totalQuantity - integerPart1; // 获取小数部分

  //   if (decimalPart1) {
  //     form.purchaseinventoryList[index].totalQuantity =
  //       integerPart1 +
  //       form.purchaseinventoryList[index].unitList.unitCode_dictText +
  //       parseFloat(decimalPart1.toFixed(1)) * row.partPercent +
  //       form.purchaseinventoryList[index].unitCode_dictText;
  //   }

  //   // 数量
  //   olditemQuantity.value = form.purchaseinventoryList[index].itemQuantity;
  //   form.purchaseinventoryList[index].itemQuantity =
  //     form.purchaseinventoryList[index].itemQuantity / row.partPercent;

  //   const integerPart = Math.floor(form.purchaseinventoryList[index].itemQuantity); // 获取整数部分
  //   const decimalPart = form.purchaseinventoryList[index].itemQuantity - integerPart; // 获取小数部分

  //   if (decimalPart) {
  //     form.purchaseinventoryList[index].itemQuantity =
  //       integerPart +
  //       form.purchaseinventoryList[index].unitList.unitCode_dictText +
  //       parseFloat(decimalPart.toFixed(1)) * row.partPercent +
  //       form.purchaseinventoryList[index].unitCode_dictText;
  //   }
  //   form.purchaseinventoryList[index].price =
  //     form.purchaseinventoryList[index].price * row.partPercent;
  //   form.purchaseinventoryList[index].price = form.purchaseinventoryList[index].price.toFixed(4);
  // }
}

function getMaxCounts(row,index,counts){
    form.purchaseinventoryList[index].oldtotalQuantity =  form.purchaseinventoryList[index].totalQuantity
    form.purchaseinventoryList[index].totalQuantity =
    form.purchaseinventoryList[index].totalQuantity / row.partPercent;

    const integerPart1 = Math.floor(form.purchaseinventoryList[index].totalQuantity); // 获取整数部分
    const decimalPart1 = form.purchaseinventoryList[index].totalQuantity - integerPart1; // 获取小数部分

    if(decimalPart1){
      form.purchaseinventoryList[index].totalQuantity = integerPart1 + form.purchaseinventoryList[index].unitList.unitCode_dictText +
      (decimalPart1*row.partPercent).toFixed(0) +
      form.purchaseinventoryList[index].unitList.minUnitCode_dictText
    }

    //领用数量
    if(counts){
      form.purchaseinventoryList[index].olditemQuantity =  form.purchaseinventoryList[index].itemQuantity*row.partPercent
      form.purchaseinventoryList[index].itemMaxQuantity = form.purchaseinventoryList[index].itemQuantity
      const integerPart = Math.floor(form.purchaseinventoryList[index].itemQuantity); // 获取整数部分
      const decimalPart = form.purchaseinventoryList[index].itemQuantity - integerPart; // 获取小数部分
      console.log(integerPart,decimalPart,form.purchaseinventoryList[index].itemQuantity,121212121)
      if(decimalPart){
        form.purchaseinventoryList[index].itemQuantity = integerPart + form.purchaseinventoryList[index].unitList.unitCode_dictText +
        (decimalPart*row.partPercent).toFixed(0) +
        form.purchaseinventoryList[index].minUnitCode_dictText
      }
    }else{
      // form.purchaseinventoryList[index].olditemQuantity =  form.purchaseinventoryList[index].itemQuantity
      // form.purchaseinventoryList[index].itemQuantity =
      // form.purchaseinventoryList[index].itemQuantity / row.partPercent;
      // form.purchaseinventoryList[index].itemMaxQuantity = form.purchaseinventoryList[index].itemQuantity
      // const integerPart = Math.floor(form.purchaseinventoryList[index].itemQuantity); // 获取整数部分
      // const decimalPart = form.purchaseinventoryList[index].itemQuantity - integerPart; // 获取小数部分

      // if(decimalPart){
      //   form.purchaseinventoryList[index].itemQuantity = integerPart + form.purchaseinventoryList[index].unitList.unitCode_dictText +
      //   (decimalPart*row.partPercent).toFixed(0) +
      //   form.purchaseinventoryList[index].unitList.minUnitCode_dictText
      // }
      // if(counts==form.purchaseinventoryList[index].unitCode&&route.query.supplyBusNo){  // 修复编辑时在大单位重复点击大单位的bug
      //   form.purchaseinventoryList[index].totalQuantity = form.purchaseinventoryList[index].oldtotalQuantity
      //   form.purchaseinventoryList[index].itemQuantity = form.purchaseinventoryList[index].itemMaxQuantity
      //   form.purchaseinventoryList[index].totalPrice =  (form.purchaseinventoryList[index].price * form.purchaseinventoryList[index].itemQuantity).toFixed(4);
      // }else{
        form.purchaseinventoryList[index].price =
        form.purchaseinventoryList[index].price * row.partPercent;
        form.purchaseinventoryList[index].price = form.purchaseinventoryList[index].price.toFixed(4);
      // }
    } 
    console.log(form.purchaseinventoryList[index].price,"121212121")
}
// 计算总价
function handleTotalPrice(index) {
  form.purchaseinventoryList[index].olditemQuantity = form.purchaseinventoryList[index].itemQuantity * row.partPercent;
  form.purchaseinventoryList[index].itemMaxQuantity = form.purchaseinventoryList[index].itemQuantity
  let purchaseItem = form.purchaseinventoryList[index];
  if (purchaseItem.price > 0 && purchaseItem.itemQuantity > 0) {
    form.purchaseinventoryList[index].totalPrice = purchaseItem.price * purchaseItem.itemQuantity;
    form.purchaseinventoryList[index].totalPrice =
      form.purchaseinventoryList[index].totalPrice.toFixed(4);
    //  parseFloat(form.purchaseinventoryList[index].totalPrice.toFixed(4))
  }
  if(form.purchaseinventoryList[index].itemQuantity==0){
    form.purchaseinventoryList[index].totalPrice = 0
  }
  store.setCurrentDataLYCK({ purchaseinventoryList: form.purchaseinventoryList, receiptHeaderForm: receiptHeaderForm })
}
// 保存
function handleSave(row, index) {
  rowList.value = []
  form.purchaseinventoryList.map((row,index)=>{
    if(row){
      // 触发校验
      handleBlur(row);
      proxy.$refs['receiptHeaderRef'].validate((valid) => {
        if (valid) {
          proxy.$refs['formRef'].validate((valid) => {
            if (valid) {
              let rows = JSON.parse(JSON.stringify(row))
              delete rows.itemMaxQuantity
              // delete rows.olditemQuantity
              // delete rows.oldtotalSourceQuantity
              // delete rows.oldtotalPurposeQuantity 
              if(rows.unitCode== rows.unitList.minUnitCode){
                rows.itemQuantity = form.purchaseinventoryList[index].olditemQuantity?form.purchaseinventoryList[index].olditemQuantity:form.purchaseinventoryList[index].itemQuantity
              }else{
                rows.itemQuantity = form.purchaseinventoryList[index].itemMaxQuantity?form.purchaseinventoryList[index].itemMaxQuantity:form.purchaseinventoryList[index].itemQuantity
              }
              if(rows.unitCode == rows.unitCode_dictText){
                if(rows.unitCode_dictText == rows.unitList.minUnitCode_dictText){
                  rows.unitCode = rows.unitList.minUnitCode
                  console.log(rows.unitCode_dictText,2323232 )
                }else{
                  rows.unitCode = rows.unitList.unitCode
                  rows.unitCode_dictText = rows.unitList.unitCode_dictText
                  console.log(rows.unitCode_dictText,23232326565 )
                }
              } 
              rowList.value.push(JSON.parse(JSON.stringify(rows)))
              if(rowList._rawValue&&rowList._rawValue.length == form.purchaseinventoryList.length){
                addTransferProducts(rowList._rawValue)
              }         
            }
          });
        }
      })
    }
  });
}
function addTransferProducts(rowList){
  addTransferProduct(JSON.parse(JSON.stringify(rowList))).then((res) => {
    // 当前行没有id视为首次新增
    // if (!row.id) {
    //   data.isAdding = false; // 允许新增下一行
    // }
    if (res.data) {
      proxy.$message.success('保存成功！');
      form.purchaseinventoryList.map((row,index)=>{
        form.purchaseinventoryList[index].id = res.data[index]
        form.purchaseinventoryList[index].isSave = true;
      })
    }
  });
}
  /** 选择条数  */
function handleSelectionChange(selection) {
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
    delTransferProduct(ids).then((res) => {
      if (res.code == 200) {
        proxy.$message.success('删除成功');
      }
    });
  } else {
    if (length > 1) {
      delTransferProduct(ids).then((res) => {
        if (res.code == 200) {
          proxy.$message.success('删除成功');
        }
      });
    }
  }
  form.purchaseinventoryList = form.purchaseinventoryList.filter(
    (row) => !selectedRows.value.includes(row)
  );
  // 允许新增下一行
  if(form.purchaseinventoryList&&form.purchaseinventoryList.length>0){
    data.isEdit = true
  }else{
    data.isEdit = false
  }
  data.isAdding = false;
}

/**计算合计金额 */
function handleTotalAmount() {
  totalAmount.value = form.purchaseinventoryList.reduce((accumulator, currentRow) => {
    return (accumulator + (Number(currentRow.totalPrice) || 0))
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
  // purposeTypeListOptions.value = props.purposeTypeListOptions;
  categoryListOptions.value = props.categoryListOptions;
  // sourceTypeListOptions.value = props.sourceTypeListOptions;
  receiptHeaderForm.busNo = props.busNoAdd;
  // // 设置默认值为字典中的第一个值
  // if (purchase_type.value.length > 0) {
  //   form.value.medicationType = purchase_type.value[0].value;
  // }
}

// 确认当前行
function confirmCurrentRow() {}
// 显示弹框
function edit() {
  data.isAdding = false;
  data.isEdit = true;
  // reset();
  visible.value = true;
  // purposeTypeListOptions.value = props.purposeTypeListOptions;
  categoryListOptions.value = props.categoryListOptions;
  // sourceTypeListOptions.value = props.sourceTypeListOptions;
  // receiptHeaderForm.busNo = props.busNoAdd;
  // receiptHeaderForm = props.item.length > 0 ? props.item[0] : {};
  // receiptHeaderForm.busNo = props.item.supplyBusNo;
  //源仓库
  // receiptHeaderForm.sourceLocationId = props.editRow.sourceLocationId
  //目的仓库
  receiptHeaderForm.locationId = props.editRow.locationId;
  receiptHeaderForm.sourceLocationStoreId = props.editRow.sourceLocationStoreId; //源货位
  // receiptHeaderForm.purposeTypeEnum = props.editRow.purposeTypeEnum
  // receiptHeaderForm.purposeLocationStoreId = props.editRow.purposeLocationStoreId // 目的仓位
  receiptHeaderForm.lotNumber = props.editRow.lotNumber;
  receiptHeaderForm.ybNo = props.editRow.ybNo;
  receiptHeaderForm.price = props.editRow.price;
  receiptHeaderForm.totalPrice = props.editRow.totalPrice;
  receiptHeaderForm.busNo = props.editRow.supplyBusNo;
  // receiptHeaderForm.supplierId = props.editRow.supplierId;
  // receiptHeaderForm.practitionerId = props.editRow.practitionerId;
  receiptHeaderForm.occurrenceTime = formatDate(props.editRow.occurrenceTime);
  // receiptHeaderForm.purposeTypeEnum = props.editRow.purposeTypeEnum.toString();
  receiptHeaderForm.locationId = props.editRow.purposeTypeEnum.toString();
  receiptHeaderForm.medicationType =
    props.editRow.itemTable == 'med_medication_definition' ? '1' : '2';
  total.value = form.purchaseinventoryList.length;
  // handleChangePurposeTypeEnum(props.editRow.purposeTypeEnum.toString());
  handleChangepurposeLocationId(props.editRow.locationId.toString());
  setTimeout(() => {
    form.purchaseinventoryList = props.item.map((item) => {
      return {
        ...item,
        name: item.itemName,
        volume: item.volume,
        manufacturer: item.supplierName,
        totalQuantity: item.totalQuantity,
        // totalQuantity: item.totalQuantity,
        startTime: formatDate(item.startTime),
        endTime: formatDate(item.endTime),
        occurrenceTime: formatDate(item.occurrenceTime),
        isSave: true,
      };
    });
  }, 100);

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
    // submitApproval(receiptHeaderForm.busNo).then((res) => {
    //   if (res.code == 200) {
    //     proxy.$modal.msgSuccess("提交审批成功");
    //     emit("refresh");
    //     visible.value = false;
    //   }
    // });
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
  proxy.$modal
    .confirm('是否确认删除以上数据?')
    .then(function () {
      return delTransferProduct({ ids: delId.join(',') });
    })
    .then(() => {
      proxy.$modal.msgSuccess('删除成功');
    })
    .catch(() => {});
}
/** 调拨管理查询下拉树结构 */
function getTransferProductTypeList() {
  data.isAdding = false;
  getInit().then((response) => {
    // busNoAdd.value = response.data.busNo; // 单据号新增
    issueDepartmentDto.value = response.data.issueDepartmentDto;
    practitionerListOptions.value = response.data.practitionerListOptions;
    supplierListOptions.value = response.data.supplierListOptions;
    supplyStatusOptions.value = response.data.supplyStatusOptions;
  });
}

const locationList = ref([]);
function getBusNoInitList() {
  if (route.query.supplyBusNo) {
    receiptHeaderForm.busNo = route.query.supplyBusNo;
    viewStatus.value = route.query.view
    sessionStorage.setItem('busNoLY',"")
  } else {
    if (!sessionStorage.getItem('busNoLY')) {
      store.clearCurrentDataLYCK()
      getBusNoInit().then((response) => {
        receiptHeaderForm.busNo = response.data.busNo;
        sessionStorage.setItem('busNoLY', receiptHeaderForm.busNo);
        // busNoAdd.value = response.data.busNo; // 单据号新增
      });
    } else {
      receiptHeaderForm.busNo = sessionStorage.getItem('busNoLY');
    }
  }
}

function handleChangepurposeLocationId(value) {
  // // 当领用部门变化时，更新 receiptHeaderForm 中的 locationId
  // receiptHeaderForm.locationId = value;
  // // 获取该部门对应的经手人列表
  // if (value) {
  //   const selectedDepartment = issueDepartmentDto.value.find(dept => dept.locationId === value);
  //   if (selectedDepartment) {
  //     // 这里可以根据需要添加获取经手人列表的逻辑
  //     console.log('选中的部门:', selectedDepartment);
  //   }
  // }
}

// 取消所有行编辑
function cancelEditAllRow() {
  // 遍历所有行，重置编辑状态
  form.purchaseinventoryList.forEach((row) => {
    row.isEditing = false;
    row.error = false;
  });
  data.isAdding = false;
}

getTransferProductTypeList();
getBusNoInitList();
getTransferProductDetails();

// defineExpose({
//   show,
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
