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
          <el-button
            type="primary"
            plain
            icon="Printer"
            :disabled="multiple"
            @click="handleDelete"
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
            icon="EditPen"
            @click="handleBatchAdd"
            >生成批量调拨单</el-button
          >
        </el-col>
      </el-row>

      <el-form
        :model="receiptHeaderForm"
        ref="receiptHeaderRef"
        :inline="true"
        label-width="120px"
        :rules="rules"
      >
      <el-row :gutter="10">
        <el-form-item label="单据号：" prop="busNo">
          <el-input
            v-model="receiptHeaderForm.busNo"
            placeholder="单据号："
            clearable
            style="width: 260px"
            disabled
          />
        </el-form-item>
        <el-form-item label="单据日期：">
          <el-date-picker
            v-model="receiptHeaderForm.occurrence_time"
            value-format="YYYY-MM-DD HH:mm:ss"
            type="datetime"
            :disabled="data.isEdit"
          />
        </el-form-item>
          <el-form-item label="药品类型：" prop="medicationType" label-width="115px">
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
        </el-row>
        <el-row :gutter="10">
        <el-form-item label="源仓库类型：" prop="sourceTypeEnum">
          <el-select
            v-model="receiptHeaderForm.sourceTypeEnum"
            placeholder=""
            clearable
            style="width: 150px"
            :disabled="data.isEdit"
            @change="handleChangeSourceTypeEnum"
          >
            <el-option
              v-for="supplier in warehous_type"
              :key="supplier.value"
              :label="supplier.label"
              :value="supplier.value"
            />
          </el-select>
        </el-form-item>
        <!-- sourceLocationName -->
        <el-form-item label="源仓库：" prop="sourceLocationId" required>
          <el-select
            v-model="receiptHeaderForm.sourceLocationId"
            placeholder=""
            clearable
            style="width: 150px"
            :disabled="data.isEdit"
          >
           <!-- purchase_type -->
            <el-option
              v-for="supplier in sourceTypeListOptions"
              :key="supplier.id"
              :label="supplier.name"
              :value="supplier.id"
            />
          </el-select>
        </el-form-item>
        <!-- sourceLocationStoreName -->
        <el-form-item label="源货位：" prop="sourceLocationStoreId">
          <el-select
            v-model="receiptHeaderForm.sourceLocationStoreId"
            placeholder=""
            clearable
            style="width: 150px"
            :disabled="data.isEdit"
          >
            <el-option
              v-for="supplier in sourceLocationStoreIdListOptions"
              :key="supplier.value"
              :label="supplier.label"
              :value="supplier.value"
            />
          </el-select>
        </el-form-item>
        </el-row>
        <el-row>
        <el-form-item label="目的仓库类型：" prop="purposeTypeEnum">
          <el-select
            v-model="receiptHeaderForm.purposeTypeEnum"
            placeholder=""
            clearable
            style="width: 150px"
            :disabled="data.isEdit"
            @change="handleChangePurposeTypeEnum"
          >
            <el-option
              v-for="dict in warehous_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <!-- purposeLocationName -->
        <el-form-item label="目的仓库：" prop="purposeLocationId" required>
          <el-select
            v-model="receiptHeaderForm.purposeLocationId"
            placeholder=""
            clearable
            style="width: 150px"
            :disabled="data.isEdit"
          >
          <!-- purchase_type -->
            <el-option
              v-for="supplier in purposeTypeListOptions"
              :key="supplier.id"
              :label="supplier.name"
              :value="supplier.id"
            />
          </el-select>
        </el-form-item>
        <!-- purposeLocationStoreName -->
        <el-form-item label="目的货位：" prop="purposeLocationStoreId">
          <el-select
            v-model="receiptHeaderForm.purposeLocationStoreId"
            placeholder=""
            clearable
            style="width: 150px"
            :disabled="data.isEdit"
          >
            <el-option
              v-for="supplier in purposeLocationStoreIdListOptions"
              :key="supplier.value"
              :label="supplier.label"
              :value="supplier.value"
            />
          </el-select>
        </el-form-item>
        </el-row>
      </el-form>
      <el-tabs type="border-card">
        <el-tab-pane label="调拨单据明细">
          <el-row :gutter="10" class="mb8" v-if="!viewStatus">
            <!-- <el-col :span="1.5">
              <el-button type="primary" plain icon="Plus" @click="addNewRow"
                >添加行</el-button
              >
            </el-col>
            <el-col :span="1.5">
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
              <!-- v-hasPermi="['system:user:edit']" -->
              <el-button
                type="primary"
                plain
                icon="Plus"
                @click="handleSave"
                
                >批量保存所有数据</el-button
              >
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
              <el-table-column
                label="项目"
                align="center"
                key="name"
                prop="name"
                width="200"
                fixed
              >
                <template #default="scope">
                  <el-form-item
                    :prop="`purchaseinventoryList.${scope.$index}.name`"
                    :rules="tableRules.name"
                    required
                  >
                   <el-input
                      v-model="scope.row.name"
                      placeholder=""
                       disabled
                    />
                    <!-- <PopoverList
                      @search="handleSearch"
                      :width="1000"
                      :modelValue="scope.row.name"
                    >
                      <template #popover-content="{}">
                        <transferManagement
                          disabled
                          @selectRow="(row) => selectRow(row, scope.$index)"
                           :searchKey="medicineSearchKey"
                           :sourceLocationId="receiptHeaderForm.sourceLocationId"
                           :purposeLocationId="receiptHeaderForm.purposeLocationId"
                           :itemType="itemType"
                        />
                      </template>
                    </PopoverList> -->
                  </el-form-item>
                </template>
              </el-table-column>
              <el-table-column
                label="规格"
                align="center"
                key="totalVolume"
                prop="totalVolume"
                width="200"
              >
                <template #default="scope">
                  <el-form-item
                    :prop="`purchaseinventoryList.${scope.$index}.totalVolume`"
                    :rules="tableRules.totalVolume"
                  >
                    <el-input
                      v-model="scope.row.totalVolume"
                      placeholder=""
                      disabled
                    />
                  </el-form-item>
                </template>
              </el-table-column>
              <el-table-column
                label="厂家/产地"
                align="center"
                key="manufacturer"
                prop="manufacturer"
                :show-overflow-tooltip="true"
                width="220"
              >
                <template #default="scope">
                  <el-form-item
                    :prop="`purchaseinventoryList.${scope.$index}.manufacturer`"
                    :rules="tableRules.manufacturer"
                  >
                    <el-input
                      v-model="scope.row.manufacturer"
                      placeholder=""
                      disabled
                    />
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
                        v-model="scope.row.unitCode"
                        placeholder=" "
                        :disabled="viewStatus == 'view'"
                        :class="{ 'error-border': scope.row.error }"
                        @change="
                          (value) =>
                            handleUnitCodeChange(scope.row, scope.$index, value)
                        "
                      >
                        <template
                          v-if="scope.row.partPercent > 1"
                        >
                          <el-option
                            :label="scope.row.unitList.unitCode_dictText"
                            :value="scope.row.unitList.unitCode"
                          />
                          <el-option
                            :label="scope.row.unitList.minUnitCode_dictText"
                            :value="scope.row.unitList.minUnitCode"
                          />
                        </template>
                        <template
                          v-if="scope.row.partPercent == 1"
                        >
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
                label="源仓库库存数量"
                align="center"
                key="totalSourceQuantity"
                prop="totalSourceQuantity"
                width="120"
              >
                <template #default="scope">
                  <el-form-item
                    :prop="`purchaseinventoryList.${scope.$index}.totalSourceQuantity`"
                    :rules="tableRules.totalSourceQuantity"
                  >
                    <el-input
                      v-model="scope.row.totalSourceQuantity"
                      placeholder=""
                      disabled
                    />
                  </el-form-item>
                </template>
              </el-table-column>
              <el-table-column
                label="目的仓库库存数量"
                align="center"
                key="totalPurposeQuantity"
                prop="totalPurposeQuantity"
                width="130"
              >
                <template #default="scope">
                  <el-form-item
                    :prop="`purchaseinventoryList.${scope.$index}.totalPurposeQuantity`"
                    :rules="tableRules.totalPurposeQuantity"
                  >
                    <el-input
                      v-model="scope.row.totalPurposeQuantity"
                      placeholder=""
                      disabled
                    />
                  </el-form-item>
                </template>
              </el-table-column>
              <el-table-column
                label="调拨数量"
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
                        :disabled="viewStatus == 'view'"
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
                label="调拨单价 "
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
                    <el-input 
                      v-model="scope.row.lotNumber" 
                      placeholder="" 
                      disabled
                    />
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
                    />
                  </el-form-item>
                </template>
              </el-table-column>
              <!-- <el-table-column
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
              </el-table-column> -->
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
                    <el-input v-model="scope.row.traceNo" placeholder=""  disabled/>
                  </el-form-item>
                </template>
              </el-table-column>
              <el-table-column
                label="追溯码包装层级"
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
                    <el-input v-model="scope.row.packagingLevels" placeholder=""  disabled/>
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
                    <el-input v-model="scope.row.remake" placeholder="" @blur="remakeBlur(scope.row,scope.$index)" :disabled="viewStatus == 'view'"/>
                  </el-form-item>
                </template>
              </el-table-column>
              <!-- <el-table-column
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
                    v-hasPermi="['system:user:edit']"
                    >保存</el-button
                  >
                </template>
              </el-table-column> -->
            </el-table>
            <pagination
              v-if="route.query.supplyBusNo||(forms.purchaseinventoryList&&forms.purchaseinventoryList[0]&&forms.purchaseinventoryList[0].id)"
              v-show="total > 0"
              :total="total"
              v-model:page="queryParams.pageNo"
              v-model:limit="queryParams.pageSize"
              @pagination="getBusNoInitList"
            />
            <pagination
              v-else
              v-show="total > 0"
              :total="total"
              v-model:page="queryParams.pageNo"
              v-model:limit="queryParams.pageSize"
              @pagination="getBatchList(1)"
            />
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
        <el-col :span="2">
          <span>审核人：</span>
        </el-col>
        <el-col :span="2">
          <span>单据状态：</span>
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
    <!-- </el-dialog> -->
  </div>
</template>

<script setup name="batchTransfer">
import {
  submitApproval,
  addTransferProductBatch,
  getInit,
  delTransferProduct,
  getPharmacyList,
  getCount,
  getBusNoInit,
  getDispensaryList,
  getMedicineList,
  getTransferProductDetails,
  getTransferProductDetail,
  reject,
  productTransferApproved
} from "../components/transferManagement";
import PopoverList from "@/components/OpenHis/popoverList/index.vue";
import transferManagement from "../components/transferManagement.vue";
import { formatDate,formatDateymd } from "@/utils/index";
import useUserStore from "@/store/modules/user";
import { useStore } from '@/store/store';
import useTagsViewStore from '@/store/modules/tagsView';
const tagsViewStore = useTagsViewStore();
const store = useStore();
const router = useRouter();
const route = useRoute();
const userStore = useUserStore();

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
const viewStatus = ref("")
 // 总页数
const totalPages = ref(0)
// 总数据条数
const totalSize = ref(0)
// 存放列表数据
const tableData = ref([])
// 当前页码
const pageNum = ref(1)
// 每页显示数据
const pageSize = ref(10)
const purchaseinventoryList = ref([]);
const open = ref(false);
const loading = ref(false);
const ids = ref([]);
const rowList = ref([]);

const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const visible = ref(false);
const row = ref({});
const rowIndex = ref(-1);
const totalAmount = ref(0);

const form = reactive({
  purchaseinventoryList: [],
})

const forms = reactive({
  purchaseinventoryList: [],
});
const receiptHeaderForm = reactive({
  busNo: undefined,
  occurrence_time: formatDate(new Date()),
});

const data = reactive({
  isEdit: false,
  isAdding: true,
  queryParams: {
    pageNo: 1,
    pageSize:10,
    sourceLocationId:'',
    purposeLocationId:'',
    medicationType:''
    // searchKey: undefined, // 供应商名称
    // busNo: undefined, // 编码
    // statusEnum: undefined, // 状态
    // supplierId: undefined, // 
    // occurrence_time: undefined, // 申请时间
    // practitionerId: undefined, // 经手人ID
  },
  rules: {
    purposeLocationId: [
      { required: true, message: "请选择目的仓库", trigger: "change" },
    ],
    sourceLocationId: [
      { required: true, message: "请选择源仓库", trigger: "change" },
    ],
    medicationType: [
      { required: true, message: "请选择药品类型", trigger: "change" },
    ],
  },
  tableRules: {
    name: [{ required: true, message: "项目不能为空", trigger: "change" }],
    // totalpackagingLevels: [
    //   { required: true, message: "规格不能为空", trigger: "blur" },
    // ],
    unitCode: [
      { required: true, message: "计量单位不能为空", trigger: "change" },
    ],
   
    itemQuantity: [
      { required: true, message: "调拨数量不能为空", trigger: "blur" },
    ],
    // lotNumber: [
    //   { required: true, message: "产品批号不能为空", trigger: "blur" },
    // ],
    // traceNo: [{ required: true, message: "追溯码不能为空", trigger: "blur" }],
    // startTime: [
    //   { required: true, message: "开始时间不能为空", trigger: "blur" },
    // ],
    // endTime: [{ required: true, message: "结束时间不能为空", trigger: "blur" }],
    // price: [{ required: true, message: "单价不能为空", trigger: "blur" }],
    // totalPrice: [{ required: true, message: "总价不能为空", trigger: "blur" }],
  },
});

const { queryParams, rules, tableRules } = toRefs(data);
const purposeTypeListOptions = ref(undefined); 
const sourceTypeListOptions = ref(undefined);
const sourceLocationStoreIdListOptions = ref(undefined); 
const purposeLocationStoreIdListOptions = ref(undefined);
const categoryListOptions = ref(undefined); 

const selectedRows = ref([]); // 用于存储选中的行
const emit = defineEmits(["refresh"]);
const tableRef = ref(undefined); // 表格引用
const currentRow = ref(undefined); // 当前操作的行
const medicineSearchKey = ref("");
const itemType = ref("");

watch(
  () => store.currentDataPLDB,
  (newVlaue) => {
    if (newVlaue&&!route.query.supplyBusNo) {
      form.purchaseinventoryList = newVlaue?.purchaseinventoryList
      receiptHeaderForm.busNo = newVlaue?.receiptHeaderForm.busNo
      receiptHeaderForm.occurrence_time = newVlaue?.receiptHeaderForm.occurrence_time
      receiptHeaderForm.sourceTypeEnum = newVlaue?.receiptHeaderForm.sourceTypeEnum
      handleChangeSourceTypeEnum(receiptHeaderForm.sourceTypeEnum,1)
      receiptHeaderForm.sourceLocationId = newVlaue?.receiptHeaderForm.sourceLocationId
      receiptHeaderForm.purposeTypeEnum = newVlaue?.receiptHeaderForm.purposeTypeEnum
      handleChangePurposeTypeEnum(receiptHeaderForm.purposeTypeEnum,1)
      receiptHeaderForm.purposeLocationId = newVlaue?.receiptHeaderForm.purposeLocationId
      receiptHeaderForm.medicationType = newVlaue?.receiptHeaderForm.medicationType
    }
  },
  { immediate: true }
)
watch(
  () => store.currentDataPLDBALL,
  (newVlaue) => {
    if (newVlaue&&!route.query.supplyBusNo) {
      forms.purchaseinventoryList = newVlaue?.purchaseinventoryList
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
  document.addEventListener("click", handleClickOutside);
});

// 卸载时移除事件
onUnmounted(() => {
  document.removeEventListener("click", handleClickOutside);
});

function addNewRow() {
  proxy.$refs["receiptHeaderRef"].validate((valid) => {
    if (valid) {
      if (data.isAdding) {
        proxy.$message.warning("请先保存当前行后再新增！");
        return;
      }
      const newRow = {
        id: "",
        supplyBusNo:"",
        occurrence_time:"",
        typeEnum_enumText:"",
        statusEnum_enumText:"",
        sourceLocationId:"", // 源仓库
        purposeLocationId:"", //目的仓库
        sourceLocationName:"",
        purposeLocationName:"",
        approverId_dictText:"",
        applicantId_dictText:"",
        approvalTime:"",
        createTime:"",
        itemTable: "",
        itemQuantity: "",
        itemMaxQuantity:"",
        itemId: "",
        unitCode: "",
        remake: "",
        // supplierId: "",
        purposeTypeEnum: "",
        purposeLocationId: null,
        purposeLocationStoreId: null,
        // practitionerId: "",
        traceNo: "",
        invoiceNo: "",
        startTime: "",
        endTime: "",
        price: "",
        totalPrice: "",
        objQuantity:"",
        orgQuantity:"",
        // sellPrice: "",
        // minSellPrice: "",
        // locationInventoryList: [{value:1,label:'药房'},{value:2,label:'仓库'}], // 库房列表
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
   for (let key in row) {
    if(!row[key]){
      if(key=='itemQuantity'||key=='totalSourceQuantity'||key=='totalPurposeQuantity'||key=='price'||key=='totalPrice'){
        row[key] = 0
      }else{
        row[key] = ''
      }
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
  row.sourceLocationId =receiptHeaderForm.sourceLocationId
  row.sourceLocationStoreId = receiptHeaderForm.sourceLocationStoreId;
  row.purposeLocationId = receiptHeaderForm.purposeLocationId
  row.purposeLocationStoreId = receiptHeaderForm.purposeLocationStoreId;
  row.busNo = receiptHeaderForm.busNo;
  row.applyTime = formatDate(row.applyTime) 
  row.startTime = formatDateymd(row.startTime)
  row.endTime = formatDateymd(row.endTime)
  row.occurrence_time = receiptHeaderForm.occurrence_time;
 
  let sourceTypeEnum = warehous_type.value.filter(e=>{return e.label==receiptHeaderForm.sourceTypeEnum})

  row.sourceTypeEnum = (sourceTypeEnum&&sourceTypeEnum[0])?sourceTypeEnum[0].value:receiptHeaderForm.sourceTypeEnum
  let purposeTypeEnum = warehous_type.value.filter(e=>{return e.label==receiptHeaderForm.purposeTypeEnum})
 
  row.purposeTypeEnum = (purposeTypeEnum&&purposeTypeEnum[0])?purposeTypeEnum[0].value:receiptHeaderForm.purposeTypeEnum

  let sourceLocationId = sourceTypeListOptions.value.filter(e=>{return e.name==receiptHeaderForm.sourceLocationId})
  row.sourceLocationId = (sourceLocationId&&sourceLocationId[0])?sourceLocationId[0].id:receiptHeaderForm.sourceLocationId

  let purposeLocationId = purposeTypeListOptions.value.filter(e=>{return e.name==receiptHeaderForm.purposeLocationId})
  row.purposeLocationId = (purposeLocationId&&purposeLocationId[0])?purposeLocationId[0].id:receiptHeaderForm.purposeLocationId
 
  row.applicantId = userStore.id;
}

// 取消行编辑
function cancelEditRow(){
  const findIndexId  = form.purchaseinventoryList.findIndex(e=>e.id==currentRow.value.id)
  form.purchaseinventoryList[findIndexId] = {}
}
// 点击行时记录当前行
function handleRowClick(row) {
  // getMedicineList({ itemId: row.itemId,sourceLocationId:row.sourceLocationId,purposeLocationId:row.purposeLocationId }).then((res) => {
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

function handelApply() {
  productTransferApproved(route.query.supplyBusNo).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess("操作成功");
      store.clearCurrentDataPLDB();
      store.clearCurrentDataPLDBAll();
      // 跳转到审核页面
      router.replace({ path: '/aaaa/medicationmanagement/billapproval',query:{type:'batchTransfer'}});
    }
  })
}

// 驳回
function handleReject() {
  reject(route.query.supplyBusNo).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess("操作成功")
      store.clearCurrentDataPLDB();
      store.clearCurrentDataPLDBAll();
      // 跳转到审核页面
      router.replace({ path: '/aaaa/medicationmanagement/billapproval',query:{type:'batchTransfer'}});
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
      proxy.$modal.msgSuccess("提交审批成功");
      // proxy.$tab.closePage(route).then(({ visitedViews }) => {  // 关闭当前页
      //   toLastView(visitedViews, route) 
      // })
      tagsViewStore.delView(router.currentRoute.value);
      // 跳转到调拨管理页面
      router.replace({ path: 'transferManagentList' });
      store.clearCurrentDataPLDB();
      store.clearCurrentDataPLDBAll();
    })
  }
}
function toLastView(visitedViews, view) {
  const latestView = visitedViews.slice(-1)[0]
  if(view.name== 'BatchTransfer'){ //调拨单据号删除
    sessionStorage.setItem('busNopl',"")
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
  form.purchaseinventoryList[index].sourceLocationId = receiptHeaderForm.sourceLocationId
  form.purchaseinventoryList[index].purposeLocationId = receiptHeaderForm.purposeLocationId
  form.purchaseinventoryList[index].itemId = rowValue.definitionId;
  form.purchaseinventoryList[index].name = rowValue.name;
  form.purchaseinventoryList[index].volume = rowValue.volume;
  form.purchaseinventoryList[index].minUnitCode = rowValue.minUnitCode;
  form.purchaseinventoryList[index].unitCode = rowValue.unitCode;
  form.purchaseinventoryList[index].manufacturer = rowValue.manufacturer;
  form.purchaseinventoryList[index].partPercent = rowValue.partPercent;
  form.purchaseinventoryList[index].unitList = rowValue.unitList[0];
  form.purchaseinventoryList[index].lotNumber = rowValue.lotNumber
  if(route.query.supplyBusNo){
    handleLocationClick(receiptHeaderForm.sourceLocationId1,receiptHeaderForm.purposeLocationId1,form.purchaseinventoryList[index].itemId,index)
  }else{
    handleLocationClick(form.purchaseinventoryList[index].sourceLocationId,form.purchaseinventoryList[index].purposeLocationId,form.purchaseinventoryList[index].itemId,index)
  }
  form.purchaseinventoryList[index].itemQuantity = 0
  form.purchaseinventoryList[index].totalPrice = 0
}

function handleLocationClick(id,purposeLocationId,itemId, index) {
  getCount({
    itemId: itemId,
    orgLocationId: id,
    objLocationId:purposeLocationId,
    lotNumber:form.purchaseinventoryList[index].lotNumber
  }).then((res) => {
    if (res.data&&res.data[0]) {
      form.purchaseinventoryList[index].itemTable = res.data[0].itemTable || ""
      form.purchaseinventoryList[index].totalPurposeQuantity = res.data[0].objQuantity || 0;
      form.purchaseinventoryList[index].totalSourceQuantity = res.data[0].orgQuantity || 0;
      form.purchaseinventoryList[index].traceNo = res.data[0].traceNo || "";
      form.purchaseinventoryList[index].supplierId = res.data[0].supplierId || "";
      form.purchaseinventoryList[index].startTime = formatDateymd(res.data[0].productionDate)|| "";
      form.purchaseinventoryList[index].endTime =  formatDateymd(res.data[0].expirationDate) || "";
    
      form.purchaseinventoryList[index].unitCode = form.purchaseinventoryList[index].unitList.minUnitCode
      form.purchaseinventoryList[index].unitCode_dictText = form.purchaseinventoryList[index].unitList.minUnitCode_dictText

      // 单价   大单位单价
      if(form.purchaseinventoryList[index].unitCode==form.purchaseinventoryList[index].unitList.minUnitCode){
        form.purchaseinventoryList[index].price = res.data[0].price / form.purchaseinventoryList[index].partPercent|| "";
        form.purchaseinventoryList[index].price = form.purchaseinventoryList[index].price.toFixed(4);
      }else{
        if(form.purchaseinventoryList[index].price>1){
          form.purchaseinventoryList[index].price = form.purchaseinventoryList[index].price.toFixed(4);
        }
      }
      startTimeOld.value = form.purchaseinventoryList[index].startTime?form.purchaseinventoryList[index].startTime:''
      endTimeOld.value = form.purchaseinventoryList[index].endTime?form.purchaseinventoryList[index].endTime:''
      if (form.purchaseinventoryList[index].totalSourceQuantity == 0) {
        proxy.$message.warning('仓库数量为0，无法调用！');
        return;
      }
    }else {
      form.purchaseinventoryList[index].totalPurposeQuantity = 0
      form.purchaseinventoryList[index].totalSourceQuantity = 0
      form.purchaseinventoryList[index].price = 0
      proxy.$message.warning('仓库数量为0，无法调用！');
    }
  });
}
// 切换仓库类型获取药房/药库列表   目的仓库切换
function handleChangePurposeTypeEnum(value,type) {
  if (value == 16) {
    getPharmacyList().then((res) => {
      purposeTypeListOptions.value = res.data;
      if(!route.query.supplyBusNo&&!type){
        receiptHeaderForm.purposeLocationId = ''
        receiptHeaderForm.purposeLocationId1 = ''
      }
    });
  } else if (value == 11) {
    getDispensaryList().then((res) => {
      purposeTypeListOptions.value = res.data;
      if(!route.query.supplyBusNo&&!type){
        receiptHeaderForm.purposeLocationId = ''
        receiptHeaderForm.purposeLocationId1 = ''
      }
    });
  }
}
 // 源仓库切换
function handleChangeSourceTypeEnum(value,type) {
  if (value == 16) {
    getPharmacyList().then((res) => {
      sourceTypeListOptions.value = res.data;
      if(!route.query.supplyBusNo&&!type){
        receiptHeaderForm.sourceLocationId = ''
        receiptHeaderForm.sourceLocationId1 = ''
      }
    });
  } else if (value == 11) {
    getDispensaryList().then((res) => {
      sourceTypeListOptions.value = res.data;
      if(!route.query.supplyBusNo&&!type){
        receiptHeaderForm.sourceLocationId = ''
        receiptHeaderForm.sourceLocationId1 = ''
      }
    });
  }
}
function editBatchTransfer(index){
  if(queryParams.value.pageNo==1){
    forms.purchaseinventoryList[index] =  form.purchaseinventoryList[index]
  }else{
    let editIndex = (Number(queryParams.value.pageNo)-1)*Number(queryParams.value.pageSize)+index
    forms.purchaseinventoryList[editIndex] =  form.purchaseinventoryList[index]
  }
}
// 单位处理
function handleUnitCodeChange(row, index, value) {
  // 防止点击和已选一样的问题
  if(!form.purchaseinventoryList[index].statusMaxvalue&&Number(row.measurementUnitCode)==Number(value)){
    return     
  }
  if (row.minUnitCode == value) { //最小计量单位
    form.purchaseinventoryList[index].totalSourceQuantity =  form.purchaseinventoryList[index].oldtotalSourceQuantity
    form.purchaseinventoryList[index].totalPurposeQuantity = form.purchaseinventoryList[index].oldtotalPurposeQuantity
    form.purchaseinventoryList[index].itemQuantity = 0
    form.purchaseinventoryList[index].totalPrice = 0
    // form.purchaseinventoryList[index].itemQuantity =  form.purchaseinventoryList[index].olditemQuantity 
   
    // 单价
    form.purchaseinventoryList[index].price =
    form.purchaseinventoryList[index].price / row.partPercent;
    form.purchaseinventoryList[index].price = form.purchaseinventoryList[index].price.toFixed(4);
  } else {  // 切换成大的计量单位
    form.purchaseinventoryList[index].itemQuantity = 0
    form.purchaseinventoryList[index].totalPrice = 0
    getMaxCounts(row,index)
  }
  form.purchaseinventoryList[index].statusMaxvalue = true
  
  editBatchTransfer(index)
}
function getMaxCounts(row,index,counts){
    form.purchaseinventoryList[index].oldtotalSourceQuantity =  form.purchaseinventoryList[index].totalSourceQuantity
    form.purchaseinventoryList[index].totalSourceQuantity =
    form.purchaseinventoryList[index].totalSourceQuantity / row.partPercent;

    const integerPart1 = Math.floor(form.purchaseinventoryList[index].totalSourceQuantity); // 获取整数部分
    const decimalPart1 = form.purchaseinventoryList[index].totalSourceQuantity - integerPart1; // 获取小数部分

    if(decimalPart1){
      form.purchaseinventoryList[index].totalSourceQuantity = integerPart1 + form.purchaseinventoryList[index].unitList.unitCode_dictText +
      (decimalPart1*row.partPercent).toFixed(0) +
      form.purchaseinventoryList[index].unitList.minUnitCode_dictText
    }

    form.purchaseinventoryList[index].oldtotalPurposeQuantity =  form.purchaseinventoryList[index].totalPurposeQuantity
    form.purchaseinventoryList[index].totalPurposeQuantity =
    form.purchaseinventoryList[index].totalPurposeQuantity / row.partPercent;

    const integerPart2 = Math.floor(form.purchaseinventoryList[index].totalPurposeQuantity); // 获取整数部分
    const decimalPart2 = form.purchaseinventoryList[index].totalPurposeQuantity - integerPart2; // 获取小数部分

    if(decimalPart2){
      form.purchaseinventoryList[index].totalPurposeQuantity = integerPart2 + form.purchaseinventoryList[index].unitList.unitCode_dictText +
      (decimalPart2*row.partPercent).toFixed(0) +
      form.purchaseinventoryList[index].unitList.minUnitCode_dictText
    }
    //调拨数量
    if(counts){
      form.purchaseinventoryList[index].olditemQuantity =  form.purchaseinventoryList[index].itemQuantity*row.partPercent
      form.purchaseinventoryList[index].itemMaxQuantity = form.purchaseinventoryList[index].itemQuantity
      const integerPart = Math.floor(form.purchaseinventoryList[index].itemQuantity); // 获取整数部分
      const decimalPart = form.purchaseinventoryList[index].itemQuantity - integerPart; // 获取小数部分
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
}
// 计算总价
function handleTotalPrice(index) {
  form.purchaseinventoryList[index].olditemQuantity = form.purchaseinventoryList[index].itemQuantity * row.partPercent;
  form.purchaseinventoryList[index].itemMaxQuantity = form.purchaseinventoryList[index].itemQuantity
  let purchaseItem = form.purchaseinventoryList[index];
  if (purchaseItem.price > 0 && purchaseItem.itemQuantity > 0) {
    form.purchaseinventoryList[index].totalPrice =
      purchaseItem.price * purchaseItem.itemQuantity;
     form.purchaseinventoryList[index].totalPrice = form.purchaseinventoryList[index].totalPrice.toFixed(4);
  }
  if(form.purchaseinventoryList[index].itemQuantity==0){
    form.purchaseinventoryList[index].totalPrice = 0
  }
  editBatchTransfer(index)
}
//批量调拨修改备注
function remakeBlur(row,index){
  editBatchTransfer(index)
}
function handleSave() {
  if(form.purchaseinventoryList.length==0){
    proxy.$message.warning("请先生成批量调拨单在进行批量保存！");
    return
  }
  rowList.value = []
  forms.purchaseinventoryList.map((row,index)=>{
    if(row){
      handleBlur(row)
   
    proxy.$refs["receiptHeaderRef"].validate((valid) => {
      if (valid) {
        proxy.$refs["formRef"].validate((valid) => {
          if (valid) {
            // // 当前行没有id视为首次新增
            // if (!row.id) {
            //   data.isAdding = false; // 允许新增下一行
            // }
            let rows = JSON.parse(JSON.stringify(row))
            delete rows.itemMaxQuantity
            if(rows.unitCode== rows.unitList.minUnitCode){
              rows.itemQuantity = row.olditemQuantity?row.olditemQuantity:row.itemQuantity
            }else{
              rows.itemQuantity = row.itemMaxQuantity?row.itemMaxQuantity:row.itemQuantity
            }
            if(rows.unitCode == rows.unitCode_dictText){
              if(rows.unitCode_dictText == rows.unitList.minUnitCode_dictText){
                rows.unitCode = rows.unitList.minUnitCode
              }else{
                rows.unitCode = rows.unitList.unitCode
                rows.unitCode_dictText = rows.unitList.unitCode_dictText
              }
            }
            if(rows.unitCode == rows.unitList.unitCode){
              rows.unitCode_dictText = rows.unitList.unitCode_dictText 
            }else if(rows.unitCode == rows.unitList.minUnitCode){
              rows.unitCode_dictText = rows.unitList.minUnitCode_dictText 
            }
            rowList.value.push(JSON.parse(JSON.stringify(rows)))
            if(rowList._rawValue&&rowList._rawValue.length == forms.purchaseinventoryList.length){
              addTransferProducts(rowList._rawValue)
            }
          }
        });
      }
    });
    }
  })
  // form.purchaseinventoryList.map((row,index)=>{
  //   handleBlur(row)
  //   proxy.$refs["receiptHeaderRef"].validate((valid) => {
  //     if (valid) {
  //       proxy.$refs["formRef"].validate((valid) => {
  //         if (valid) {
  //           let rows = JSON.parse(JSON.stringify(row))
  //           delete rows.itemMaxQuantity
  //           // delete rows.olditemQuantity
  //           // delete rows.oldtotalSourceQuantity
  //           // delete rows.oldtotalPurposeQuantity 
  //           if(rows.unitCode== rows.unitList.minUnitCode){
  //             rows.itemQuantity = form.purchaseinventoryList[index].olditemQuantity?form.purchaseinventoryList[index].olditemQuantity:form.purchaseinventoryList[index].itemQuantity
  //           }else{
  //             rows.itemQuantity = form.purchaseinventoryList[index].itemMaxQuantity?form.purchaseinventoryList[index].itemMaxQuantity:form.purchaseinventoryList[index].itemQuantity
  //           }
  //           if(rows.unitCode == rows.unitCode_dictText){
  //             if(rows.unitCode_dictText == rows.unitList.minUnitCode_dictText){
  //               rows.unitCode = rows.unitList.minUnitCode
  //             }else{
  //               rows.unitCode = rows.unitList.unitCode
  //               rows.unitCode_dictText = rows.unitList.unitCode_dictText
  //             }
  //           }
  //           rowList.value.push(JSON.parse(JSON.stringify(rows)))
  //           if(rowList._rawValue&&rowList._rawValue.length == form.purchaseinventoryList.length){
  //             addTransferProducts(rowList._rawValue)
  //           }
  //         }
  //       });
  //     }
  //   });
  // })
  // 保存逻辑...
}
function addTransferProducts(rowList){
  addTransferProductBatch(JSON.parse(JSON.stringify(rowList))).then((res) => {
    if (res.data) {
      proxy.$message.success("保存成功！");
      form.purchaseinventoryList.map((row,index)=>{
        form.purchaseinventoryList[index].id = res.data[index]
        form.purchaseinventoryList[index].isSave = true;
      })
      forms.purchaseinventoryList.map((row,index)=>{
        forms.purchaseinventoryList[index].id = res.data[index]
        forms.purchaseinventoryList[index].isSave = true;
      })
      store.setCurrentDataPLDB({purchaseinventoryList: form.purchaseinventoryList,receiptHeaderForm: receiptHeaderForm });
      store.setCurrentDataPLDBAll({purchaseinventoryList: forms.purchaseinventoryList});
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
    if (length > 1&&ids&&ids.length>0) {
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

  if(form.purchaseinventoryList&&form.purchaseinventoryList.length>0){
    data.isEdit = true
  }else{
    data.isEdit = false
  }
 
  data.isAdding = false;
}

/**计算合计金额 */
function handleTotalAmount() {
  totalAmount.value = form.purchaseinventoryList.reduce(
    (accumulator, currentRow) => {
      return (accumulator + (Number(currentRow.totalPrice) || 0))
    },
    0
  );
}

function handleBatchAdd(){
   proxy.$refs["receiptHeaderRef"].validate((valid) => {
    if (valid) {
      if (data.isAdding) {
        proxy.$message.warning("请先保存当前行后再新增！");
        return;
      }
      data.isAdding = true; // 设置标志位为 true，表示有未保存的
      getBatchList()
      // getAllBatchList()
    }
  })
}
function getDetailAll(){
  forms.purchaseinventoryList.map((e,index)=>{
    // if(type){  //编辑
    //   e.isSave = true
    // }else{  // 新增
    //   e.isSave = false
    // }
    // forms.purchaseinventoryList[index].statusMaxvalue =  false
    e.volume = e.totalVolume
    if(e.purposeTypeEnum){
      warehous_type.value.map(item=>{
        if(item.value == e.purposeTypeEnum ){
          receiptHeaderForm.purposeTypeEnum = item.label
        }
      })
      handleChangePurposeTypeEnum(e.purposeTypeEnum,1)
    }
    if(e.purposeLocationId){
      receiptHeaderForm.purposeLocationId1 = e.purposeLocationId
      receiptHeaderForm.purposeLocationId = e.purposeLocationName
    }
    if(e.sourceTypeEnum){
      warehous_type.value.map(item=>{
        if(item.value == e.sourceTypeEnum ){
          receiptHeaderForm.sourceTypeEnum = item.label
        }
      })
      handleChangeSourceTypeEnum(e.sourceTypeEnum,1)
    }
    if(e.sourceLocationId){
      receiptHeaderForm.sourceLocationId1 = e.sourceLocationId
      receiptHeaderForm.sourceLocationId = e.sourceLocationName
    }
    if(e.itemType){
      purchase_type.value.map(item=>{
        if(item.value == e.itemType ){
          receiptHeaderForm.medicationType = item.value
          forms.purchaseinventoryList[index].itemType_enumText = item.label
        }
      })
    }
   
    forms.purchaseinventoryList[index].totalPurposeQuantity = forms.purchaseinventoryList[index].totalPurposeQuantity?forms.purchaseinventoryList[index].totalPurposeQuantity:0
    if(e.unitList&&e.unitCode){
      forms.purchaseinventoryList[index].unitList = e.unitList[0]?e.unitList[0]:e.unitList   //计量单位回显数组
      forms.purchaseinventoryList[index].unitCode = forms.purchaseinventoryList[index].measurementUnitCode
      forms.purchaseinventoryList[index].unitCode_dictText = forms.purchaseinventoryList[index].measurementUnitCode_dictText;
      if(forms.purchaseinventoryList[index].measurementUnitCode == forms.purchaseinventoryList[index].unitList.unitCode){  // 回显大单位
        getMaxCountsAll(e,index,1)
      }
    }
    if(e.price){
      // 单价   大单位单价
      if(forms.purchaseinventoryList[index].measurementUnitCode == forms.purchaseinventoryList[index].unitList.minUnitCode){
        forms.purchaseinventoryList[index].price = forms.purchaseinventoryList[index].price / forms.purchaseinventoryList[index].partPercent|| "";
        forms.purchaseinventoryList[index].price = forms.purchaseinventoryList[index].price.toFixed(4);
      }else{
        if(forms.purchaseinventoryList[index].price>1){
          forms.purchaseinventoryList[index].price = forms.purchaseinventoryList[index].price.toFixed(4);
        }
      }
      if (forms.purchaseinventoryList[index].price > 0 && forms.purchaseinventoryList[index].itemQuantity > 0) {
        forms.purchaseinventoryList[index].totalPrice =
        forms.purchaseinventoryList[index].price * forms.purchaseinventoryList[index].itemQuantity;
        forms.purchaseinventoryList[index].totalPrice = forms.purchaseinventoryList[index].totalPrice.toFixed(4);
      }
    }else{
      forms.purchaseinventoryList[index].price = 0
      forms.purchaseinventoryList[index].totalPrice = 0
    }
  })
  // if(!type){  //新增
    store.setCurrentDataPLDBAll({ purchaseinventoryList: forms.purchaseinventoryList });
  // }
}
function getMaxCountsAll(row,index,counts){
    forms.purchaseinventoryList[index].oldtotalSourceQuantity =  forms.purchaseinventoryList[index].totalSourceQuantity
    forms.purchaseinventoryList[index].totalSourceQuantity =
    forms.purchaseinventoryList[index].totalSourceQuantity / row.partPercent;

    const integerPart1 = Math.floor(forms.purchaseinventoryList[index].totalSourceQuantity); // 获取整数部分
    const decimalPart1 = forms.purchaseinventoryList[index].totalSourceQuantity - integerPart1; // 获取小数部分

    if(decimalPart1){
      forms.purchaseinventoryList[index].totalSourceQuantity = integerPart1 + forms.purchaseinventoryList[index].unitList.unitCode_dictText +
      (decimalPart1*row.partPercent).toFixed(0) +
      forms.purchaseinventoryList[index].unitList.minUnitCode_dictText
    }

    forms.purchaseinventoryList[index].oldtotalPurposeQuantity =  forms.purchaseinventoryList[index].totalPurposeQuantity
    forms.purchaseinventoryList[index].totalPurposeQuantity =
    forms.purchaseinventoryList[index].totalPurposeQuantity / row.partPercent;

    const integerPart2 = Math.floor(forms.purchaseinventoryList[index].totalPurposeQuantity); // 获取整数部分
    const decimalPart2 = forms.purchaseinventoryList[index].totalPurposeQuantity - integerPart2; // 获取小数部分

    if(decimalPart2){
      forms.purchaseinventoryList[index].totalPurposeQuantity = integerPart2 + forms.purchaseinventoryList[index].unitList.unitCode_dictText +
      (decimalPart2*row.partPercent).toFixed(0) +
      forms.purchaseinventoryList[index].unitList.minUnitCode_dictText
    }
    //调拨数量
    if(counts){
      forms.purchaseinventoryList[index].olditemQuantity =  forms.purchaseinventoryList[index].itemQuantity*row.partPercent
      forms.purchaseinventoryList[index].itemMaxQuantity = forms.purchaseinventoryList[index].itemQuantity
      const integerPart = Math.floor(forms.purchaseinventoryList[index].itemQuantity); // 获取整数部分
      const decimalPart = forms.purchaseinventoryList[index].itemQuantity - integerPart; // 获取小数部分
      if(decimalPart){
        forms.purchaseinventoryList[index].itemQuantity = 
        integerPart + forms.purchaseinventoryList[index].unitList.unitCode_dictText +
        (decimalPart*row.partPercent).toFixed(0) + forms.purchaseinventoryList[index].minUnitCode_dictText
      }
    }else{
      forms.purchaseinventoryList[index].price =
      forms.purchaseinventoryList[index].price * row.partPercent;
      forms.purchaseinventoryList[index].price = forms.purchaseinventoryList[index].price.toFixed(4);
    }
}
function getDetail(type){
  form.purchaseinventoryList.map((e,index)=>{
    if(type){  //编辑
      e.isSave = true
    }else{  // 新增
      e.isSave = false
    }
    form.purchaseinventoryList[index].statusMaxvalue =  false
    e.volume = e.totalVolume
    if(e.purposeTypeEnum){
      warehous_type.value.map(item=>{
        if(item.value == e.purposeTypeEnum ){
          receiptHeaderForm.purposeTypeEnum = item.label
        }
      })
      handleChangePurposeTypeEnum(e.purposeTypeEnum,1)
    }
    if(e.purposeLocationId){
      receiptHeaderForm.purposeLocationId1 = e.purposeLocationId
      receiptHeaderForm.purposeLocationId = e.purposeLocationName
    }
    if(e.sourceTypeEnum){
      warehous_type.value.map(item=>{
        if(item.value == e.sourceTypeEnum ){
          receiptHeaderForm.sourceTypeEnum = item.label
        }
      })
      handleChangeSourceTypeEnum(e.sourceTypeEnum,1)
    }
    if(e.sourceLocationId){
      receiptHeaderForm.sourceLocationId1 = e.sourceLocationId
      receiptHeaderForm.sourceLocationId = e.sourceLocationName
    }
    if(e.itemType){
      purchase_type.value.map(item=>{
        if(item.value == e.itemType ){
          receiptHeaderForm.medicationType = item.value
          // receiptHeaderForm.medicationType1 = item.value
          form.purchaseinventoryList[index].itemType_enumText = item.label
        }
      })
    }
   
    form.purchaseinventoryList[index].totalPurposeQuantity = form.purchaseinventoryList[index].totalPurposeQuantity?form.purchaseinventoryList[index].totalPurposeQuantity:0
    if(e.unitList&&e.unitCode){
      form.purchaseinventoryList[index].unitList = e.unitList[0]?e.unitList[0]:e.unitList   //计量单位回显数组
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
  if(!type){   // 新增
    store.setCurrentDataPLDB({ purchaseinventoryList: form.purchaseinventoryList, receiptHeaderForm: receiptHeaderForm });
  }
}
function getAllBatchList(type){ //获取所有页的数据
    if(type){
      data.isEdit = true;
    }else{
      data.isEdit = false;
    }
    
    let sourceLocationId = sourceTypeListOptions.value.filter(e=>{return e.name==receiptHeaderForm.sourceLocationId})
    queryParams.value.sourceLocationId = (sourceLocationId&&sourceLocationId[0])?sourceLocationId[0].id:receiptHeaderForm.sourceLocationId

    let purposeLocationId = purposeTypeListOptions.value.filter(e=>{return e.name==receiptHeaderForm.purposeLocationId})
    queryParams.value.purposeLocationId = (purposeLocationId&&purposeLocationId[0])?purposeLocationId[0].id:receiptHeaderForm.purposeLocationId

    queryParams.value.medicationType=receiptHeaderForm.medicationType
    if(total.value){
      if(type==2){
        let queryParamss = {
          busNo:receiptHeaderForm.busNo,
          pageNo:1,
          pageSize:total.value,
          sourceLocationId:queryParams.value.sourceLocationId,
          purposeLocationId:queryParams.value.purposeLocationId,
          medicationType:queryParams.value.medicationType
        }
        // 所有数据批量详情
        getTransferProductDetail(queryParamss).then((res) => {
          forms.purchaseinventoryList = res.data.records?res.data.records:res.data
          // total.value = res.data.total
          getDetailAll()
        })
      }else{
        let queryParamss = {
          pageNo:1,
          pageSize:total.value,
          sourceLocationId:queryParams.value.sourceLocationId,
          purposeLocationId:queryParams.value.purposeLocationId,
          medicationType:queryParams.value.medicationType
        }
        getTransferProductDetails(queryParamss).then((res) => {
          forms.purchaseinventoryList = res.data.records
          // total.value = res.data.total  // 分页
          getDetailAll()
        }).catch(error => {
          forms.purchaseinventoryList = form.purchaseinventoryList
          console.error("请求失败", error);
        })
      }
    }
}
function getBatchList(type){
    data.isEdit = false;
    handleChangeSourceTypeEnum(receiptHeaderForm.sourceTypeEnum,1)
    handleChangePurposeTypeEnum(receiptHeaderForm.purposeTypeEnum,1)
    let sourceLocationId = sourceTypeListOptions.value.filter(e=>{return e.name==receiptHeaderForm.sourceLocationId})
    queryParams.value.sourceLocationId = (sourceLocationId&&sourceLocationId[0])?sourceLocationId[0].id:receiptHeaderForm.sourceLocationId

    let purposeLocationId = purposeTypeListOptions.value.filter(e=>{return e.name==receiptHeaderForm.purposeLocationId})
    queryParams.value.purposeLocationId = (purposeLocationId&&purposeLocationId[0])?purposeLocationId[0].id:receiptHeaderForm.purposeLocationId
    
    queryParams.value.medicationType=receiptHeaderForm.medicationType
    if(!type){
      getTransferProductDetails(queryParams.value).then((res) => {
        form.purchaseinventoryList = res.data.records
        total.value = res.data.total  // 分页
        getDetail()
        getAllBatchList()
      })
    }else{ // 切换页码
      getTransferProductDetails(queryParams.value).then((res) => {
        form.purchaseinventoryList = res.data.records
        total.value = res.data.total  // 分页
        getDetail()
      })
    //   for(let i=0;i<queryParams.value.pageSize;i++){
    //     let k = i+queryParams.value.pageSize
    //     form.purchaseinventoryList[i] = forms.purchaseinventoryList[k]
    //   }
    }
}

/** 删除按钮操作 */
function handleDelete(row) {
  const delId = row.id || ids.value;
  proxy.$modal
    .confirm("是否确认删除以上数据?")
    .then(function () {
      return delTransferProduct({ ids: delId.join(",") });
    })
    .then(() => {
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}
/** 调拨管理查询下拉树结构 */
function getTransferProductTypeList() {
  data.isAdding = false;
  getInit().then((response) => {
    categoryListOptions.value = response.data.categoryListOptions 
  });
}
function getBusNoInitList() {
  if(route.query.supplyBusNo||(forms.purchaseinventoryList&&forms.purchaseinventoryList[0]&&forms.purchaseinventoryList[0].id)){  // 编辑
    store.clearCurrentDataPLDB()
    store.clearCurrentDataPLDBAll()
    receiptHeaderForm.busNo = route.query.supplyBusNo?route.query.supplyBusNo:receiptHeaderForm.busNo
    viewStatus.value = route.query.view
    data.isEdit = true;
    queryParams.value.busNo = receiptHeaderForm.busNo
    getTransferProductDetail(queryParams.value).then((res) => {
      form.purchaseinventoryList = res.data.records?res.data.records:res.data
      total.value = res.data.total
      getDetail(1)
      getAllBatchList(2)
    })
    sessionStorage.setItem('busNopl',"")
  }else{ //新增
    data.isEdit = false;
    if(!sessionStorage.getItem('busNopl')){
      store.clearCurrentDataPLDB()
      store.clearCurrentDataPLDBAll()
      getBusNoInit().then((response) => {
        receiptHeaderForm.busNo = response.data.busNo;
        sessionStorage.setItem('busNopl', receiptHeaderForm.busNo)
      })
    }else{
      receiptHeaderForm.busNo = sessionStorage.getItem('busNopl')
    }
  }
}
 
getTransferProductTypeList();
getBusNoInitList()
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

.el-row.pagination{
  height: 100px;
   
}
.pagehand{
  height: 100px;
  line-height: 100px;
}
</style>