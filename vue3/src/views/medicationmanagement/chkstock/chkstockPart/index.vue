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
        <el-button type="primary" plain icon="Printer" :disabled="multiple" @click="handleDelete" >打印</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="EditPen"
          @click="handleTotalAmount"
          >计算盈亏金额</el-button
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
    <el-form :model="receiptHeaderForm" ref="receiptHeaderRef" :inline="true" label-width="120px" :rules="rules">
      <el-form-item label="单据号：" prop="busNo">
        <el-input v-model="receiptHeaderForm.busNo" placeholder="单据号：" clearable style="width: 260px" disabled />
      </el-form-item>
      <el-form-item label="仓库类型：" prop="purposeTypeEnum">
        <el-select
          v-model="receiptHeaderForm.purposeTypeEnum"
          placeholder=""
          clearable
          style="width: 150px"
          :disabled="data.isEdit"
          @change="handleChangePurposeTypeEnum"
          
        >
        <!-- handlePurposeType原方法 -->
          <el-option
            v-for="dict in warehous_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="盘点仓库：" prop="purposeLocationId">
        <el-select
          v-model="receiptHeaderForm.purposeLocationId"
          placeholder=""
          clearable
          style="width: 150px"
          :disabled="data.isEdit"
          @change="handleCabinetChange(receiptHeaderForm.purposeLocationId)"
        >
          <el-option
            v-for="cabinet in purposeTypeListOptions"
            :key="cabinet.id"
            :label="cabinet.name"
            :value="cabinet.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="货位：" prop="purposeLocation">
        <el-select
          v-model="receiptHeaderForm.purposeLocation"
          placeholder=""
          clearable
          style="width: 150px"
          :disabled="data.isEdit"
        >
          <el-option
            v-for="freight in freightListOptions"
            :key="freight.id"
            :label="freight.name"
            :value="freight.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="盘点日期：">
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
        <!-- categoryListOptions -->
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
      <el-tab-pane label="盘点单明细">
        <el-row :gutter="10" class="mb8" v-if="!viewStatus">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="addNewRow"
              >添加行</el-button
            >
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
                    v-if="viewStatus == 'view'"
                    v-model="scope.row.name"
                    placeholder=""
                    disabled
                  />
                  <PopoverList
                    v-else
                    @search="handleSearch"
                    :width="1000"
                    :modelValue="scope.row.name"
                  >
                    <template #popover-content="{}">
                      <MedicineList
                        @selectRow="(row) => selectRow(row, scope.$index)"
                          :searchKey="medicineSearchKey"
                          :purposeLocationId="receiptHeaderForm.purposeLocationId"
                          :itemType="receiptHeaderForm.medicationType"
                      />
                    </template>
                  </PopoverList>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="规格"
              align="center"
              key="volume"
              prop="volume"
              width="140"
              :show-overflow-tooltip="true"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.volume`"
                  :rules="tableRules.volume"
                > 
                  <el-input
                    disabled
                    v-model="scope.row.volume"
                    placeholder=""
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
                    disabled
                    v-model="scope.row.manufacturer"
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
              width="150"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.lotNumber`"
                >
                  <el-input 
                    v-model="scope.row.lotNumber" 
                    placeholder="" 
                    disabled
                    @blur="lotNumberBlur(scope.row,scope.$index)"
                  />
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="单价 "
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
            <!-- // 盘点单位  一会细研究 -->
            <el-table-column
              label="盘点单位"
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
                      placeholder="请选择盘点单位"
                      :disabled="viewStatus =='view'"
                      :class="{ 'error-border': scope.row.error }"
                      @change="handleUnitCodeChange(scope.row, scope.$index, scope.row.unitCode)"
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
              label="盘前库存"
              align="center"
              key="totalPurposeQuantity"
              prop="totalPurposeQuantity"
              width="120"
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
              label="实盘数量"
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
                    :disabled="viewStatus =='view'"
                    v-model="scope.row.totalSourceQuantity"
                    placeholder=""
                    @change="totalSourceQuantityChange(scope.row, scope.$index, scope.row.totalSourceQuantity)"
                  />
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="实盘金额"
              align="center"
              key="totalPrice"
              prop="totalPrice"
              width="150"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.totalPrice`"
                  :rules="tableRules.totalPrice"
                >
                  <div class="select_wrapper_div">
                    <el-input
                      v-model="scope.row.totalPrice"
                      placeholder=""
            
                      :class="{ 'error-border': scope.row.error }"
                      disabled
                    >
                      <template #suffix>元</template>
                    </el-input>
                  </div>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="盈亏数量"
              align="center"
              key="itemQuantity"
              prop="itemQuantity"
              width="110"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.itemQuantity`"
                  :rules="tableRules.itemQuantity"
                >
                  <el-input
                    v-model="scope.row.itemQuantity"
                    placeholder=""
                    disabled
                  />
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="盈亏金额"
              align="center"
              key="profitAmount"
              prop="profitAmount"
              width="150"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.profitAmount`"
                  :rules="tableRules.profitAmount"
                >
                  <el-input
                    v-model="scope.row.profitAmount"
                    placeholder=""
                    disabled
                  />
                </el-form-item>
              </template>
            </el-table-column>
            <!-- 盈亏类型  一会研究 -->
            <el-table-column
              label="盈亏类型"
              align="center"
              key="reasonCode"
              prop="reasonCode"
              :show-overflow-tooltip="true"
              width="120"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.reasonCode`"
                  :rules="tableRules.reasonCode"
                >
                  <div class="select_wrapper_div">
                    <el-select
                      :disabled="viewStatus =='view'"
                      v-model="scope.row.reasonCode"
                      placeholder="请选择盈亏类型"
                      :class="{ 'error-border': scope.row.error }"
                      clearable
                      @change="reasonCodeChange(scope.row,scope.$index)"
                    >
                      <el-option
                        v-for="(item, index) in profit_reason"
                        :key="index"
                        :label="item.label"
                        :value="item.value"
                      />
                    </el-select>
                  </div>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="盈亏原因"
              align="center"
              key="reason"
              prop="reason"
              :show-overflow-tooltip="true"
              width="150"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.reason`"
                  :rules="tableRules.reason"
                >
                  <el-input
                    :disabled="viewStatus =='view'"
                    v-model="scope.row.reason"
                    placeholder=""
                    @blur="reasonBlur(scope.row,scope.$index)"
                  />
                </el-form-item>
              </template>
            </el-table-column>
               <!-- <el-table-column
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
                      @change="changeValStart($event,scope.$index)"

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
                      @change="changeValEnd($event,scope.$index)"
                    />
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
                <el-tooltip :content="formatContent(scope.row.traceNo)" placement="top" 
                  popper-class="custom-tooltip">
                  <el-form-item
                    :prop="`purchaseinventoryList.${scope.$index}.traceNo`"
                    :rules="tableRules.traceNo"
                  >
                    <el-input v-model="scope.row.traceNo" placeholder="" :disabled="viewStatus =='view'" :id ="'traceNo'+`${scope.$index}`" @change="(value) => handleTraceNo(value, scope.row)"
                    />
                  </el-form-item>
              </el-tooltip>
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
          <pagination
            v-show="total > 0"
            :total="total"
            v-model:page="queryParams.pageNo"
            v-model:limit="queryParams.pageSize"
            @pagination="getTransferProductDetails(1)"
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
            <span>合计盈亏金额：{{ totalAmount?totalAmount.toFixed(4):0 }}</span>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
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
            type="primary"
            plain
            icon="CircleClose"
            @click="handleReturn"
          >
            撤回
          </el-button>
          <el-button
            type="danger"
            plain
            icon="CircleClose"
            @click="handleClear"
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
          style="width: 100%; margin-top: 10px; margin-right: 20px"
        />
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submit">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="chkstockPart">
import {
  submitApproval,
  addProductStocktaking,
  getInit,
  delProductStocktaking,
  getPharmacyList,
  getCount,
  getDispensaryList,
  getMedicineList,
  getDetailInit,
  getstocktakingDetail,
  productStocktakingApproved,
  reject
} from "../components/api";
import PopoverList from "@/components/OpenHis/popoverList/index.vue";
import MedicineList from "../components/medicineList.vue";
import { formatDate,formatDateymd } from "@/utils/index";
import useUserStore from "@/store/modules/user";
import { useRoute } from 'vue-router';
import { useStore } from '@/store/store';
import useTagsViewStore from '@/store/modules/tagsView';
import { debounce } from 'lodash-es';
const tagsViewStore = useTagsViewStore();
const store = useStore();
const router = useRouter();
const route = useRoute();
const userStore = useUserStore();

const { proxy } = getCurrentInstance();
const { warehous_type,purchase_type,profit_reason } = 
proxy.useDict( "warehous_type","purchase_type","profit_reason");

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
const medName = ref("");
const visible = ref(false);
const row = ref({});
const rowIndex = ref(-1);
const totalAmount = ref(0);
const rowList = ref([])

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
    purposeLocationId: [
      { required: true, message: "请选择盘点仓库", trigger: "change" },
    ],
    medicationType: [
      { required: true, message: "请选择药品类型", trigger: "change" },
    ],
  },
  tableRules: {
    name: [{ required: true, message: "项目不能为空", trigger: "change" }],
    // statusEnum_enumText: [
    //   { required: true, message: "规格不能为空", trigger: "blur" },
    // ],
    unitCode: [
      { required: true, message: "计量单位不能为空", trigger: "change" },
    ],
    itemQuantity: [
      { required: true, message: "盈亏数量不能为空", trigger: "blur" },
    ],
    totalSourceQuantity:[
      { required: true, message: "实盘数量不能为空", trigger: "blur" },
    ],
    // reasonCode: [
    //   { required: true, message: "盈亏类型不能为空", trigger: "change" },
    // ],
    // startTime: [
    //   { required: true, message: "开始时间不能为空", trigger: "blur" },
    // ],
    // endTime: [{ required: true, message: "结束时间不能为空", trigger: "blur" }],
    // price: [{ required: true, message: "单价不能为空", trigger: "blur" }],
    // totalPrice: [{ required: true, message: "总价不能为空", trigger: "blur" }],
  },
});
const { queryParams, rules, tableRules } = toRefs(data);
const purposeTypeListOptions = ref(undefined); // 仓库列表
const cabinetListOptionsBk = ref(undefined); // 仓库列表(未过滤的)
const freightListOptions = ref(undefined); // 货位列表
const categoryListOptions = ref(undefined); // 药品类型
const profitReasonOptions = ref(undefined); // 盈利原因
const selectedRows = ref([]); // 用于存储选中的行
const emit = defineEmits(["refresh"]);
const tableRef = ref(undefined); // 表格引用
const currentRow = ref(undefined); // 当前操作的行
const medicineSearchKey = ref("");
const itemType = ref("");
const forms = reactive({
  purchaseinventoryList: [],
});

watch(
  () => store.currentDataPD,
  (newVlaue) => {
    if (newVlaue&&!route.query.supplyBusNo) {
      form.purchaseinventoryList = newVlaue?.purchaseinventoryList
      receiptHeaderForm.busNo = newVlaue?.receiptHeaderForm.busNo
      receiptHeaderForm.occurrenceTime = newVlaue?.receiptHeaderForm.occurrenceTime
   
      receiptHeaderForm.purposeTypeEnum = newVlaue?.receiptHeaderForm.purposeTypeEnum
      handleChangePurposeTypeEnum(receiptHeaderForm.purposeTypeEnum,1)
      receiptHeaderForm.purposeLocationId = newVlaue?.receiptHeaderForm.purposeLocationId
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
)

// 挂载时绑定事件
onMounted(() => {
  document.addEventListener("click", handleClickOutside);
});

// 卸载时移除事件
onUnmounted(() => {
  document.removeEventListener("click", handleClickOutside);
});


const openTraceNo = ref(false)
const traceNo = ref('');
const traceNoList = ref([]);
const traceNoTemp = ref('');
const traceNoTempRef = ref();
const currentIndex = ref("");
const throttledGetList = debounce(handelTraceNo, 500);

// 撤回最后一条追溯码
function handleReturn() {
  if (traceNoList.value.length === 0) return;
  // 1. 从数组中移除最后一项
  traceNoList.value.pop();
  // 2. 重新构建显示文本
  traceNo.value = traceNoList.value
    .map((item, index) => `[${index + 1}]  ${item}`)
    .join('\n');
  if(traceNoList.value != 0){
    traceNo.value += '\n';
  }
}
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
function cancel() {
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
//分页修改不同页之后批量保存
function editBatchTransfer(index){
  if(route.query.supplyBusNo){
    if(queryParams.value.pageNo==1){
      forms.purchaseinventoryList[index] =  form.purchaseinventoryList[index]
    }else{
      let editIndex = (Number(queryParams.value.pageNo)-1)*Number(queryParams.value.pageSize)+index
      forms.purchaseinventoryList[editIndex] =  form.purchaseinventoryList[index]
    }
  }
}
function addNewRow() {
  proxy.$refs["receiptHeaderRef"].validate((valid) => {
    if (valid) {
      // if (data.isAdding) {
      //   proxy.$message.warning("请先保存当前行后再新增！");
      //   return;
      // }
      const newRow = {
        id: "",
        definitionId:"",
        name:"",
        itemBusNo:"",
        itemTableName:"",
        itemTable: "",
        itemType:"",
        itemType_enumText:"",
        itemQuantity: "",
        itemMaxQuantity:"",
        itemId: "",
        detailJson: "",
        supplierId: "",
        purposeTypeEnum: "",
        purposeLocationId: "",
        purposeLocationStoreId: "",
        practitionerId: "",
        traceNo: "",
        invoiceNo: "",
        lotNumber:"",
        occurrenceTime:"",
        startTime: "",
        endTime: "",
        partPercent:"",
        price: "",
        totalPrice: "",
        sellPrice: "",
        minSellPrice: "",
        unitCode:"",
        unitCode_dictText:'',
        minUnitCode:"",
        minUnitCode_dictText:"",
        volume:'',
        wbStr:'',
        ybNo:'',
        // locationInventoryList: [], // 库房列表
        unitList: {}, // 单位列表
        isEditing: true, // 标记当前行是否正在编辑
        error: false, // 新增 error 字段
        isSave: false, // 当前行是否保存
      };
      form.purchaseinventoryList.push(newRow);
      data.isEdit = true
      data.isAdding = true; // 设置标志位为 true，表示有未保存的
    }
  });
}
function changeValStart(val,index){
  const selectedTime = val
  const validTime = this.getNextDayZeroTimeStamp(selectedTime);
  if(form.purchaseinventoryList[index].endTime){
    const endTime = formatDateymd(form.purchaseinventoryList[index].endTime)
    const getNextDayZeroTime = this.getNextDayZeroTimeStamp(endTime)
    if (getNextDayZeroTime<validTime) {
      proxy.$message.warning("生产日期必须小于等于有效期！");
      form.purchaseinventoryList[index].startTime = startTimeOld.value
      return
    }
  }
}
function changeValEnd(val,index){
  const selectedTimes = val
  const validTimes = this.getNextDayZeroTimeStamp(selectedTimes);
  if(form.purchaseinventoryList[index].startTime){
    const startTime = formatDateymd(form.purchaseinventoryList[index].startTime)
    const getNextDayZeroTimes = this.getNextDayZeroTimeStamp(startTime)
    if (getNextDayZeroTimes>validTimes) {
      proxy.$message.warning("有效期必须大于等于生产日期！");
      form.purchaseinventoryList[index].endTime = endTimeOld.value
      return
    }
  }
}

function handleBlur(row, index) {
  let hasError = false;
  for (let key in row) {
    if(!row[key]){
      row[key] = ''
    }
  }
  if(!row.itemTable){
    if (receiptHeaderForm.medicationType == 1) {
      row.itemTable = "med_medication_definition";
    } else {
      row.itemTable = "adm_device_definition";
    }
  }
  if(row.itemQuantity==0 || !row.itemQuantity){
    row.itemQuantity = 0 
    row.profitAmount = 0
  }
  if(row.totalSourceQuantity==0 || !row.totalSourceQuantity ){
    row.totalSourceQuantity = 0
    row.totalPrice = 0
  }
  row.totalSourceQuantity = Number(row.totalSourceQuantity)
  row.purposeLocationId = receiptHeaderForm.purposeLocationId
  row.purposeLocationStoreId = receiptHeaderForm.purposeLocationStoreId;
  row.busNo = receiptHeaderForm.busNo;
  row.applyTime = formatDate(row.applyTime) 
  row.startTime = formatDateymd(row.startTime)
  row.endTime = formatDateymd(row.endTime)
  row.occurrenceTime = receiptHeaderForm.occurrenceTime;

  let purposeTypeEnum = warehous_type.value.filter(e=>{return e.label==receiptHeaderForm.purposeTypeEnum})
  row.purposeTypeEnum = (purposeTypeEnum&&purposeTypeEnum[0])?purposeTypeEnum[0].value:receiptHeaderForm.purposeTypeEnum
 
  let purposeLocationId = purposeTypeListOptions.value.filter(e=>{return e.name==receiptHeaderForm.purposeLocationId})
  row.purposeLocationId = (purposeLocationId&&purposeLocationId[0])?purposeLocationId[0].id:receiptHeaderForm.purposeLocationId
  row.applicantId = userStore.id
}
// 获取调拨详情
function getTransferProductDetails(type){

  if(route.query.supplyBusNo){ // 编辑
    data.isEdit = true;
    store.clearCurrentDataPD()
    queryParams.value.busNo = receiptHeaderForm.busNo
    getstocktakingDetail(queryParams.value).then((res) => {
      form.purchaseinventoryList = res.data.records?res.data.records:res.data
      total.value = res.data.total;
      form.purchaseinventoryList.map((e,index)=>{
        e.isSave = true
        form.purchaseinventoryList[index].statusMaxvalue =  false
        e.volume = e.totalVolume
        e.name = e.itemName
        e.manufacturer = e.manufacturerText
        if(e.purposeTypeEnum){
          warehous_type.value.map(item=>{
            if(item.value == e.purposeTypeEnum ){
              receiptHeaderForm.purposeTypeEnum = item.label
            }
          })
          handleChangePurposeTypeEnum(e.purposeTypeEnum)
        }
       
        if(e.purposeLocationId){
          receiptHeaderForm.purposeLocationId = e.purposeLocationId
          // receiptHeaderForm.purposeLocationId = e.purposeLocationName
        }
        if(e.itemType){
          purchase_type.value.map(item=>{
            if(item.value == e.itemType ){
              // receiptHeaderForm.medicationType = item.label
              receiptHeaderForm.medicationType = item.value
              form.purchaseinventoryList[index].itemType_enumText = item.label
            }
          })
        }
        if(e.partPercent){
          form.purchaseinventoryList[index].partPercent = Number(form.purchaseinventoryList[index].partPercent).toFixed(4);
        }
        form.purchaseinventoryList[index].totalPurposeQuantity = form.purchaseinventoryList[index].totalQuantity?form.purchaseinventoryList[index].totalQuantity:0
      
        if(e.unitList&&e.unitCode){
          form.purchaseinventoryList[index].unitList = e.unitList[0]   //计量单位回显数组
          form.purchaseinventoryList[index].unitCode = form.purchaseinventoryList[index].measurementUnitCode_dictText;
          form.purchaseinventoryList[index].unitCode_dictText = form.purchaseinventoryList[index].measurementUnitCode_dictText;
          if(form.purchaseinventoryList[index].measurementUnitCode == form.purchaseinventoryList[index].unitList.unitCode){  // 回显大单位
            getMaxCounts(e,index,1)
          }else{
            form.purchaseinventoryList[index].totalSourceQuantity = form.purchaseinventoryList[index].totalPurposeQuantity + form.purchaseinventoryList[index].itemQuantity
          }
        }else{
          if(!e.unitList&&e.unitCode){
            form.purchaseinventoryList[index].unitList = {unitCode: form.purchaseinventoryList[index].unitCode,unitCode_dictText:form.purchaseinventoryList[index].unitCode_dictText,
              minUnitCode:form.purchaseinventoryList[index].minUnitCode, minUnitCode_dictText:form.purchaseinventoryList[index].minUnitCode_dictText
            }  //计量单位回显数组
            form.purchaseinventoryList[index].unitCode = form.purchaseinventoryList[index].measurementUnitCode_dictText;
            form.purchaseinventoryList[index].unitCode_dictText = form.purchaseinventoryList[index].measurementUnitCode_dictText;
           
            if(form.purchaseinventoryList[index].measurementUnitCode == form.purchaseinventoryList[index].unitList.unitCode){  // 回显大单位
              getMaxCounts(e,index,1)
            }else{
              form.purchaseinventoryList[index].totalSourceQuantity = form.purchaseinventoryList[index].totalPurposeQuantity + form.purchaseinventoryList[index].itemQuantity
            }
          }
        }
        if(e.price){
          if(form.purchaseinventoryList[index].measurementUnitCode == form.purchaseinventoryList[index].unitList.minUnitCode){
            form.purchaseinventoryList[index].price = form.purchaseinventoryList[index].price / form.purchaseinventoryList[index].partPercent;
            form.purchaseinventoryList[index].price = form.purchaseinventoryList[index].price.toFixed(4);

          }else{
            if(form.purchaseinventoryList[index].price>1){
              form.purchaseinventoryList[index].price = form.purchaseinventoryList[index].price.toFixed(4);
            }
          }
          let purchaseItem = form.purchaseinventoryList[index];
          if (purchaseItem.price > 0 && purchaseItem.totalSourceQuantity > 0) {
            form.purchaseinventoryList[index].totalPrice =
              purchaseItem.price * purchaseItem.totalSourceQuantity;
            form.purchaseinventoryList[index].totalPrice = form.purchaseinventoryList[index].totalPrice.toFixed(4);
          }
          if (purchaseItem.price > 0 && purchaseItem.itemQuantity) {
            form.purchaseinventoryList[index].profitAmount =
              purchaseItem.price * purchaseItem.itemQuantity;
            form.purchaseinventoryList[index].profitAmount = form.purchaseinventoryList[index].profitAmount.toFixed(4);
          }
        }else{
          form.purchaseinventoryList[index].price = 0
          form.purchaseinventoryList[index].totalPrice = 0
          form.purchaseinventoryList[index].profitAmount = 0  
        }
        form.purchaseinventoryList[index].reasonCode =  form.purchaseinventoryList[index].reasonCode
      })
       // 获取所有信息
      if(total.value&&!type){
        let queryParamss = {
          pageNo:1,
          pageSize:total.value,
          busNo:route.query.supplyBusNo
        }
        getstocktakingDetail(queryParamss).then((res) => {
          forms.purchaseinventoryList = res.data.records?res.data.records:res.data
          total.value = res.data.total;
          forms.purchaseinventoryList.map((e,index)=>{
            e.isSave = true
            forms.purchaseinventoryList[index].statusMaxvalue =  false
            e.volume = e.totalVolume
            e.name = e.itemName
            e.manufacturer = e.manufacturerText
            if(e.purposeTypeEnum){
              warehous_type.value.map(item=>{
                if(item.value == e.purposeTypeEnum ){
                  receiptHeaderForm.purposeTypeEnum = item.label
                }
              })
              handleChangePurposeTypeEnum(e.purposeTypeEnum)
            }
            if(e.purposeLocationId){
              receiptHeaderForm.purposeLocationId = e.purposeLocationId
              // receiptHeaderForm.purposeLocationId = e.purposeLocationName
            }
            if(e.itemType){
              purchase_type.value.map(item=>{
                if(item.value == e.itemType ){
                  // receiptHeaderForm.medicationType = item.label
                  receiptHeaderForm.medicationType = item.value
                  forms.purchaseinventoryList[index].itemType_enumText = item.label
                }
              })
            }
            if(e.partPercent){
              forms.purchaseinventoryList[index].partPercent = Number(forms.purchaseinventoryList[index].partPercent).toFixed(4);
            }
            forms.purchaseinventoryList[index].totalPurposeQuantity = forms.purchaseinventoryList[index].totalQuantity?forms.purchaseinventoryList[index].totalQuantity:0
          
            if(e.unitList&&e.unitCode){
              forms.purchaseinventoryList[index].unitList = e.unitList[0]   //计量单位回显数组
              forms.purchaseinventoryList[index].unitCode = forms.purchaseinventoryList[index].measurementUnitCode_dictText;
              forms.purchaseinventoryList[index].unitCode_dictText = forms.purchaseinventoryList[index].measurementUnitCode_dictText;
              if(forms.purchaseinventoryList[index].measurementUnitCode == forms.purchaseinventoryList[index].unitList.unitCode){  // 回显大单位
                getMaxCountsAll(e,index,1)
              }else{
                forms.purchaseinventoryList[index].totalSourceQuantity = forms.purchaseinventoryList[index].totalPurposeQuantity + forms.purchaseinventoryList[index].itemQuantity
              }
            }else{
              if(!e.unitList&&e.unitCode){
                forms.purchaseinventoryList[index].unitList = {unitCode: forms.purchaseinventoryList[index].unitCode,unitCode_dictText:forms.purchaseinventoryList[index].unitCode_dictText,
                  minUnitCode:forms.purchaseinventoryList[index].minUnitCode, minUnitCode_dictText:forms.purchaseinventoryList[index].minUnitCode_dictText
                }  //计量单位回显数组
                forms.purchaseinventoryList[index].unitCode = forms.purchaseinventoryList[index].measurementUnitCode_dictText;
                forms.purchaseinventoryList[index].unitCode_dictText = forms.purchaseinventoryList[index].measurementUnitCode_dictText;
                
                if(forms.purchaseinventoryList[index].measurementUnitCode == forms.purchaseinventoryList[index].unitList.unitCode){  // 回显大单位
                  getMaxCountsAll(e,index,1)
                }else{
                  forms.purchaseinventoryList[index].totalSourceQuantity = forms.purchaseinventoryList[index].totalPurposeQuantity + forms.purchaseinventoryList[index].itemQuantity
                }
              }
            }
            if(e.price){
  
              if(forms.purchaseinventoryList[index].measurementUnitCode == forms.purchaseinventoryList[index].unitList.minUnitCode){
                forms.purchaseinventoryList[index].price = forms.purchaseinventoryList[index].price / forms.purchaseinventoryList[index].partPercent;
                forms.purchaseinventoryList[index].price = forms.purchaseinventoryList[index].price.toFixed(4);

              }else{
                if(forms.purchaseinventoryList[index].price>1){
                  forms.purchaseinventoryList[index].price = forms.purchaseinventoryList[index].price.toFixed(4);
                }
              }
              let purchaseItem = forms.purchaseinventoryList[index];
              if (purchaseItem.price > 0 && purchaseItem.totalSourceQuantity > 0) {
                purchaseItem.totalPrice =
                  purchaseItem.price * purchaseItem.totalSourceQuantity;
                purchaseItem.totalPrice = purchaseItem.totalPrice.toFixed(4);
              }
              if (purchaseItem.price > 0 && purchaseItem.itemQuantity) {
                purchaseItem.profitAmount =
                  purchaseItem.price * purchaseItem.itemQuantity;
                purchaseItem.profitAmount = purchaseItem.profitAmount.toFixed(4);
              }
            }else{
              purchaseItem.price = 0
              purchaseItem.totalPrice = 0
              purchaseItem.profitAmount = 0  
            }
            // form.purchaseinventoryList[index].reasonCode =  form.purchaseinventoryList[index].reasonCode
          })
        })
      }
    })
  }else{ //新增
    data.isEdit = false;
  }
}
// 点击行时记录当前行
function handleRowClick(row) {
  // getMedicineList({ itemId: row.itemId }).then((res) => {
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
  addProductStocktaking(row).then((response) => {
    reset();
    data.isAdding = false; // 允许新增下一行
    proxy.$message.success("保存成功！");
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
    handleLocationClick(receiptHeaderForm.purposeLocationId,form.purchaseinventoryList[index].itemId,index,form.purchaseinventoryList[index].lotNumber)
  }else{
    handleLocationClick(form.purchaseinventoryList[index].purposeLocationId,form.purchaseinventoryList[index].itemId,index,form.purchaseinventoryList[index].lotNumber)
  }
  editBatchTransfer(index) // todo
  store.setCurrentDataPD({ purchaseinventoryList: form.purchaseinventoryList, receiptHeaderForm: receiptHeaderForm });
}
// function selectRow(rowValue, index) {
//   rowIndex.value = index;
//   form.purchaseinventoryList[index].name = rowValue.name;
//   form.purchaseinventoryList[index].volume = rowValue.volume;
//   // form.purchaseinventoryList[index].lotNumber = rowValue.locationInventoryList.length > 0 ? rowValue.locationInventoryList[0].lotNumber : '';
//   form.purchaseinventoryList[index].unitCode = rowValue.unitCode_dictText;
//   form.purchaseinventoryList[index].unitCode_dictText = rowValue.unitCode_dictText;
//   form.purchaseinventoryList[index].unitCode = rowValue.unitCode_dictText;
//   form.purchaseinventoryList[index].stocktakingUnitId = '1';
//   form.purchaseinventoryList[index].partPercent = rowValue.partPercent;
//   // form.purchaseinventoryList[index].price = rowValue.locationInventoryList.length > 0 ? rowValue.locationInventoryList[0].price : 0;
//   // form.purchaseinventoryList[index].totalPurposeQuantity = rowValue.locationInventoryList.length > 0 ? rowValue.locationInventoryList[0].quantity : 0  ; //盘前库存
//   form.purchaseinventoryList[index].totalSourceQuantity = '';
//   form.purchaseinventoryList[index].totalPrice = '';
//   form.purchaseinventoryList[index].itemQuantity = '';
//   form.purchaseinventoryList[index].reasonCode = '';
//   form.purchaseinventoryList[index].reason = '';
//   form.purchaseinventoryList[index].manufacturer = rowValue.manufacturer;
//   form.purchaseinventoryList[index].unitCodeList = [
//           {id:1,name:rowValue.unitCode_dictText,unitName:rowValue.unitCode_dictText,code:rowValue.unitCode},
//           {id:2,name:rowValue.minUnitCode_dictText,unitName:rowValue.minUnitCode_dictText,code:rowValue.minUnitCode}];
// }

// 获取数量
function handleLocationClick(purposeLocationId,itemId, index,lotNumber) {
  getCount({
    itemId: itemId,
    orgLocationId:purposeLocationId,
    // objLocationId:purposeLocationId,
    lotNumber:lotNumber
  }).then((res) => {
    if (res.data&&res.data[0]) {
      form.purchaseinventoryList[index].itemTable = res.data[0].itemTable || "";
      form.purchaseinventoryList[index].totalPurposeQuantity = res.data[0].orgQuantity || 0;
      form.purchaseinventoryList[index].totalSourceQuantity = res.data[0].objQuantity || 0;
      // 单价
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
     
      if (form.purchaseinventoryList[index].totalPurposeQuantity == 0) {
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

// 单位处理
function handleUnitCodeChange(row, index, value) {
  // 防止点击和已选一样的问题
  if(!form.purchaseinventoryList[index].statusMaxvalue&&row.measurementUnitCode==value&&route.query.supplyBusNo){
    return     
  }
  if (row.minUnitCode == value) { //最小计量单位
    form.purchaseinventoryList[index].totalSourceQuantity = form.purchaseinventoryList[index].oldtotalSourceQuantity?form.purchaseinventoryList[index].oldtotalSourceQuantity:form.purchaseinventoryList[index].totalSourceQuantity
    form.purchaseinventoryList[index].totalPurposeQuantity = form.purchaseinventoryList[index].oldtotalPurposeQuantity?form.purchaseinventoryList[index].oldtotalPurposeQuantity:form.purchaseinventoryList[index].totalPurposeQuantity
    form.purchaseinventoryList[index].itemQuantity = form.purchaseinventoryList[index].olditemQuantity?form.purchaseinventoryList[index].olditemQuantity:form.purchaseinventoryList[index].itemQuantity
    form.purchaseinventoryList[index].itemQuantity = 0
    form.purchaseinventoryList[index].totalPrice = 0
    form.purchaseinventoryList[index].totalSourceQuantity = 0
    form.purchaseinventoryList[index].profitAmount = 0
    // 单价
    form.purchaseinventoryList[index].price =
    form.purchaseinventoryList[index].price / row.partPercent;
    form.purchaseinventoryList[index].price = form.purchaseinventoryList[index].price.toFixed(4);

  } else {  // 切换成大的计量单位
    form.purchaseinventoryList[index].itemQuantity = 0
    form.purchaseinventoryList[index].totalPrice = 0
    form.purchaseinventoryList[index].totalSourceQuantity = 0
    form.purchaseinventoryList[index].profitAmount = 0
    getMaxCounts(row,index)
  }
  form.purchaseinventoryList[index].statusMaxvalue = true
  editBatchTransfer(index)
}
function getMaxCountsAll(row,index,counts){
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
    //数量
    if(counts){
      forms.purchaseinventoryList[index].olditemQuantity =  forms.purchaseinventoryList[index].itemQuantity*row.partPercent
      forms.purchaseinventoryList[index].itemMaxQuantity = forms.purchaseinventoryList[index].itemQuantity

      forms.purchaseinventoryList[index].oldtotalSourceQuantity = forms.purchaseinventoryList[index].itemQuantity*row.partPercent
      forms.purchaseinventoryList[index].itemMaxtotalSourceQuantity = forms.purchaseinventoryList[index].totalSourceQuantity

      const integerPart = Math.floor(forms.purchaseinventoryList[index].itemQuantity); // 获取整数部分
      const decimalPart = forms.purchaseinventoryList[index].itemQuantity - integerPart; // 获取小数部分
    
      if(decimalPart){
        forms.purchaseinventoryList[index].itemQuantity = integerPart + forms.purchaseinventoryList[index].unitList.unitCode_dictText +
        (decimalPart*row.partPercent).toFixed(0) +
        forms.purchaseinventoryList[index].minUnitCode_dictText
      }
      const integerPart2 = Math.floor(forms.purchaseinventoryList[index].totalSourceQuantity/row.partPercent); // 获取整数部分
      const decimalPart2 = (forms.purchaseinventoryList[index].totalSourceQuantity/row.partPercent) - integerPart2; // 获取小数部分
    
      if(decimalPart2){
        forms.purchaseinventoryList[index].totalSourceQuantity = integerPart2 + forms.purchaseinventoryList[index].unitList.unitCode_dictText +
        (decimalPart2*row.partPercent).toFixed(0) +
        forms.purchaseinventoryList[index].unitList.minUnitCode_dictText
      }else{
        forms.purchaseinventoryList[index].totalSourceQuantity = forms.purchaseinventoryList[index].totalPurposeQuantity + forms.purchaseinventoryList[index].itemQuantity
      }
    }else{
      forms.purchaseinventoryList[index].price =
      forms.purchaseinventoryList[index].price * row.partPercent;
      forms.purchaseinventoryList[index].price = forms.purchaseinventoryList[index].price.toFixed(4);
    }
}
function getMaxCounts(row,index,counts){
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
    //数量
    if(counts){
      form.purchaseinventoryList[index].olditemQuantity =  form.purchaseinventoryList[index].itemQuantity*row.partPercent
      form.purchaseinventoryList[index].itemMaxQuantity = form.purchaseinventoryList[index].itemQuantity

      form.purchaseinventoryList[index].oldtotalSourceQuantity = form.purchaseinventoryList[index].itemQuantity*row.partPercent
      form.purchaseinventoryList[index].itemMaxtotalSourceQuantity = form.purchaseinventoryList[index].totalSourceQuantity

      const integerPart = Math.floor(form.purchaseinventoryList[index].itemQuantity); // 获取整数部分
      const decimalPart = form.purchaseinventoryList[index].itemQuantity - integerPart; // 获取小数部分
     
      if(decimalPart){
        form.purchaseinventoryList[index].itemQuantity = integerPart + form.purchaseinventoryList[index].unitList.unitCode_dictText +
        (decimalPart*row.partPercent).toFixed(0) +
        form.purchaseinventoryList[index].minUnitCode_dictText
      }
      const integerPart2 = Math.floor(form.purchaseinventoryList[index].totalSourceQuantity/row.partPercent); // 获取整数部分
      const decimalPart2 = (form.purchaseinventoryList[index].totalSourceQuantity/row.partPercent) - integerPart2; // 获取小数部分
      if(decimalPart2){
        form.purchaseinventoryList[index].totalSourceQuantity = integerPart2 + form.purchaseinventoryList[index].unitList.unitCode_dictText +
        (decimalPart2*row.partPercent).toFixed(0) +
        form.purchaseinventoryList[index].unitList.minUnitCode_dictText
      }else{
        form.purchaseinventoryList[index].totalSourceQuantity = form.purchaseinventoryList[index].totalPurposeQuantity + form.purchaseinventoryList[index].itemQuantity
      }
      // form.purchaseinventoryList[index].totalSourceQuantity = form.purchaseinventoryList[index].totalPurposeQuantity + form.purchaseinventoryList[index].itemQuantity
    }else{
      // form.purchaseinventoryList[index].olditemQuantity =  form.purchaseinventoryList[index].itemQuantity
      // form.purchaseinventoryList[index].oldtotalSourceQuantity =  form.purchaseinventoryList[index].totalSourceQuantity

      // form.purchaseinventoryList[index].itemQuantity =
      // form.purchaseinventoryList[index].itemQuantity / row.partPercent;
      // form.purchaseinventoryList[index].totalSourceQuantity =
      // form.purchaseinventoryList[index].totalSourceQuantity / row.partPercent;
      // form.purchaseinventoryList[index].itemMaxQuantity = form.purchaseinventoryList[index].itemQuantity
      // form.purchaseinventoryList[index].itemMaxtotalSourceQuantity = form.purchaseinventoryList[index].totalSourceQuantity
      // const integerPart = Math.floor(form.purchaseinventoryList[index].itemQuantity); // 获取整数部分
      // const decimalPart = form.purchaseinventoryList[index].itemQuantity - integerPart; // 获取小数部分

      // if(decimalPart){
      //   form.purchaseinventoryList[index].itemQuantity = integerPart + form.purchaseinventoryList[index].unitList.unitCode_dictText +
      //   (decimalPart*row.partPercent).toFixed(0) +
      //   form.purchaseinventoryList[index].unitList.minUnitCode_dictText
      // }
      // const integerPart2 = Math.floor(form.purchaseinventoryList[index].totalSourceQuantity); // 获取整数部分
      // const decimalPart2 = form.purchaseinventoryList[index].totalSourceQuantity - integerPart2; // 获取小数部分

      // if(decimalPart2){
      //   form.purchaseinventoryList[index].totalSourceQuantity = integerPart2 + form.purchaseinventoryList[index].unitList.unitCode_dictText +
      //   (decimalPart2*row.partPercent).toFixed(0) +
      //   form.purchaseinventoryList[index].unitList.minUnitCode_dictText
      // }
      form.purchaseinventoryList[index].price =
      form.purchaseinventoryList[index].price * row.partPercent;
      form.purchaseinventoryList[index].price = form.purchaseinventoryList[index].price.toFixed(4);
    }
}

function handleSave(row, index) {
  rowList.value = []
  if(route.query.supplyBusNo){ // 编辑
    forms.purchaseinventoryList.map((row,index)=>{
      if(row){
        handleBlur(row);
        proxy.$refs["receiptHeaderRef"].validate((valid) => {
          if (valid) {
            proxy.$refs["formRef"].validate((valid) => {
              if (valid) {
                let rows = JSON.parse(JSON.stringify(row))
                if(rows.unitCode == rows.unitCode_dictText){
                  if(rows.unitCode_dictText == rows.unitList.minUnitCode_dictText){
                    rows.unitCode = rows.unitList.minUnitCode
                  }else{
                    rows.unitCode = rows.unitList.unitCode
                    rows.unitCode_dictText = rows.unitList.unitCode_dictText
                  }
                }
                if(rows.profitAmount||rows.profitAmount==0){
                  let totalPrice =  rows.totalPrice
                  rows.totalPrice = rows.profitAmount
                  rows.profitAmount = totalPrice
                }
                if(rows.itemQuantity&&typeof rows.itemQuantity==='string'&&rows.itemQuantity.split(rows.unitCode)){
                  rows.price = rows.price/rows.partPercent
                  rows.unitCode = rows.unitList.minUnitCode
                  rows.unitCode_dictText= rows.unitList.minUnitCode_dictText
                  rows.totalPurposeQuantity =rows.oldtotalPurposeQuantity
                  rows.totalSourceQuantity = rows.totalSourceQuantity*rows.partPercent   
                  rows.itemQuantity= rows.totalSourceQuantity - rows.totalPurposeQuantity 
                }
                rowList.value.push(JSON.parse(JSON.stringify(rows)))
                if(rowList._rawValue&&rowList._rawValue.length == forms.purchaseinventoryList.length){
                  addProductStocktakings(rowList._rawValue)
                }
              }
            })
          }
        })
      }
    })
  }else{
    form.purchaseinventoryList.map((row,index)=>{
      if(row){
        handleBlur(row);
        proxy.$refs["receiptHeaderRef"].validate((valid) => {
          if (valid) {
            proxy.$refs["formRef"].validate((valid) => {
              if (valid) {
                let rows = JSON.parse(JSON.stringify(row))
                // delete rows.itemMaxQuantity
                // if(rows.unitCode== rows.unitList.minUnitCode){
                //   rows.itemQuantity = form.purchaseinventoryList[index].olditemQuantity?form.purchaseinventoryList[index].olditemQuantity:form.purchaseinventoryList[index].itemQuantity
                //   rows.totalSourceQuantity = form.purchaseinventoryList[index].oldtotalSourceQuantity?form.purchaseinventoryList[index].oldtotalSourceQuantity:form.purchaseinventoryList[index].totalSourceQuantity
                // }else{
                //   rows.itemQuantity = form.purchaseinventoryList[index].itemMaxQuantity?form.purchaseinventoryList[index].itemMaxQuantity:form.purchaseinventoryList[index].itemQuantity
                //   rows.totalSourceQuantity = form.purchaseinventoryList[index].itemMaxtotalSourceQuantity?form.purchaseinventoryList[index].itemMaxtotalSourceQuantity:form.purchaseinventoryList[index].totalSourceQuantity
                // }
                if(rows.unitCode == rows.unitCode_dictText){
                  if(rows.unitCode_dictText == rows.unitList.minUnitCode_dictText){
                    rows.unitCode = rows.unitList.minUnitCode
                  }else{
                    rows.unitCode = rows.unitList.unitCode
                    rows.unitCode_dictText = rows.unitList.unitCode_dictText
                  }
                }
                if(rows.profitAmount||rows.profitAmount==0){
                  let totalPrice =  rows.totalPrice
                  rows.totalPrice = rows.profitAmount
                  rows.profitAmount = totalPrice
                }
                if(rows.itemQuantity&&typeof rows.itemQuantity==='string'&&rows.itemQuantity.split(rows.unitCode)){
                  rows.price = rows.price/rows.partPercent
                  rows.unitCode = rows.unitList.minUnitCode
                  rows.unitCode_dictText= rows.unitList.minUnitCode_dictText
                  rows.totalPurposeQuantity =rows.oldtotalPurposeQuantity
                  rows.totalSourceQuantity = rows.totalSourceQuantity*rows.partPercent   
                  rows.itemQuantity= rows.totalSourceQuantity - rows.totalPurposeQuantity 
                }
                rowList.value.push(JSON.parse(JSON.stringify(rows)))
                if(rowList._rawValue&&rowList._rawValue.length == form.purchaseinventoryList.length){
                  addProductStocktakings(rowList._rawValue)
                }
              
              }
            })
          }
        })
      }
    })
  }
}
function addProductStocktakings(rowList){
   addProductStocktaking(JSON.parse(JSON.stringify(rowList))).then((res) => {
    // 当前行没有id视为首次新增
    // if (!row.id) {
    //   data.isAdding = false; // 允许新增下一行
    // }
    if (res.data) {
      proxy.$message.success("保存成功！");
      form.purchaseinventoryList.map((row,index)=>{
        form.purchaseinventoryList[index].id = res.data[index]
        form.purchaseinventoryList[index].isSave = true;
      })
      if(route.query.supplyBusNo){ // 编辑
        forms.purchaseinventoryList.map((row,index)=>{
          forms.purchaseinventoryList[index].id = res.data[index]
          forms.purchaseinventoryList[index].isSave = true;
        })
      }
      store.setCurrentDataPD({purchaseinventoryList: form.purchaseinventoryList,receiptHeaderForm: receiptHeaderForm });
      // form.purchaseinventoryList[index].id = res.data;
      // form.purchaseinventoryList[index].isSave = true;
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
    delProductStocktaking(ids).then((res) => {
      if (res.code == 200) {
        proxy.$message.success("删除成功");
      }
    });
  } else {
    if (length > 1) {
      delProductStocktaking(ids).then((res) => {
        if (res.code == 200) {
          proxy.$message.success("删除成功");
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
      return (accumulator + (Number(currentRow.profitAmount) || 0))
    },
    0
  );
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
  proxy.resetForm("receiptHeaderRef");
  form.purchaseinventoryList = [];
}
// 显示弹框
// function show() {
//   data.isEdit = false;
//   data.isAdding = false;
//   reset();
//   visible.value = true;
//   purposeTypeListOptions.value = props.purposeTypeListOptions;
//   categoryListOptions.value = props.categoryListOptions
//   profitReasonOptions.value = props.profitReasonOptions
//   receiptHeaderForm.busNo = props.busNoAdd;
//   // // 设置默认值为字典中的第一个值
//   // if (purchase_type.value.length > 0) {
//   //   form.value.medicationType = purchase_type.value[0].value;
//   // }
// }
// 显示弹框
// function edit() {
//   data.isAdding = false;
//   data.isEdit = true;
//   // reset();
//   visible.value = true;
//   purposeTypeListOptions.value = props.purposeTypeListOptions;
//   categoryListOptions.value = props.categoryListOptions
//   profitReasonOptions.value = props.profitReasonOptions
//   receiptHeaderForm.busNo = props.editRow.supplyBusNo;
//   receiptHeaderForm.supplierId = props.editRow.supplierId;
//   receiptHeaderForm.practitionerId = props.editRow.practitionerId;
//   receiptHeaderForm.occurrenceTime = formatDate(props.editRow.occurrenceTime);
//   receiptHeaderForm.purposeTypeEnum = props.editRow.purposeTypeEnum_enumText;
//   receiptHeaderForm.medicationType =
//     props.editRow.itemTable == "med_medication_definition" ? "1" : "2";
//   total.value = form.purchaseinventoryList.length;
//   // handleChangeLocationType(props.editRow.purposeTypeEnum.toString());
//   setTimeout(() => {
//     form.purchaseinventoryList = props.item.map((item) => {
//       return {
//         ...item,
//         unitCodeList:[
//           {id:1,name:item.unitCode_dictText,unitName:item.unitCode,code:item.unitCode},
//           {id:2,name:item.minUnitCode_dictText,unitName:item.minUnitCode,code:item.minUnitCode}]
//       };
//     });
//   }, 100);
  
//   loading.value = false;
// }
// 驳回
function handleReject() {
  reject(route.query.supplyBusNo).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess("操作成功");
      tagsViewStore.delView(router.currentRoute.value);
      store.clearCurrentDataPD();
      // 跳转到审核页面
      router.replace({ path: '/aaaa/medicationmanagement/billapproval',query:{type:'chkstockPart'}});
    }
  });
}
function handelApply() {
  productStocktakingApproved(route.query.supplyBusNo).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess("操作成功");
      tagsViewStore.delView(router.currentRoute.value);
      store.clearCurrentDataPD();
      // 跳转到审核页面
      router.replace({ path: '/aaaa/medicationmanagement/billapproval',query:{type:'chkstockPart'}});
    }
  })
}
/** 提交审核 */
function submitAudit() {
  let length = form.purchaseinventoryList.length;
  if (length < 1) {
    proxy.$modal.msgWarning("请先添加单据");
  } else if (!form.purchaseinventoryList[length - 1].isSave) {
    proxy.$modal.msgWarning("第" + length + "行单据未保存，请先保存");
  } else {
    submitApproval(receiptHeaderForm.busNo).then((res) => {
      if (res.code == 200) {
        proxy.$modal.msgSuccess("提交审批成功");
        tagsViewStore.delView(router.currentRoute.value);
        // 跳转到盘点管理页面
        router.replace({ path: 'chkstockRecord' });
        store.clearCurrentDataPD();
        emit("refresh");
        visible.value = false;
      }
    });
  }
}
function toLastView(visitedViews, view) {
  const latestView = visitedViews.slice(-1)[0]
  if(view.name== 'ChkstockPart'){ //调拨单据号删除
    sessionStorage.setItem('busNopd',"")
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

/** 删除按钮操作 */
function handleDelete(row) {
  const delId = row.id || ids.value;
  proxy.$modal
    .confirm("是否确认删除以上数据?")
    .then(function () {
      return delPurchaseinventory({ ids: delId.join(",") });
    })
    .then(() => {
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}

function handleTraceNo(value, row){
  row.traceNo = value + ','
}

// 切换仓库类型获取药房/药库列表   目的仓库切换
function handleChangePurposeTypeEnum(value,type) {
  if (value == 16) {
    getPharmacyList().then((res) => {
      purposeTypeListOptions.value = res.data
      if(!route.query.supplyBusNo&&!type){
        receiptHeaderForm.purposeLocationId = ''
      }
      // getinitValue()
    });
  } else if (value == 11) {
    getDispensaryList().then((res) => {
      purposeTypeListOptions.value = res.data
      if(!route.query.supplyBusNo&&!type){
        receiptHeaderForm.purposeLocationId = ''
      }
      // getinitValue()     
    });
  }
}
// 获取默认值
function getinitValue(){
  if (purposeTypeListOptions.value.length > 0) { // 判断是否有盘点仓库
    receiptHeaderForm.purposeLocationId = purposeTypeListOptions.value[0].id // 盘点仓库默认值
    if (purposeTypeListOptions.value[0].children&&purposeTypeListOptions.value[0].children.length > 0) { // 判断盘点仓库内是否有货位
      freightListOptions.value = purposeTypeListOptions.value[0].children
      receiptHeaderForm.purposeLocation = purposeTypeListOptions.value[0].children[0].name
    }
  }
}

// 页面初期处理 -20250414
function getStockReceiptTypeList() {
  data.isAdding = false;
  // 无论任何方式进到画面的默认处理
  getInit().then((response) => {
    // categoryListOptions.value = response.data.categoryListOptions; // 药品类型
    profitReasonOptions.value =  response.data.profitReasonOptions; // 盈亏原因
    // receiptHeaderForm.purposeTypeEnum = warehous_type.value[0].value // 仓库类型默认值
    // handleChangePurposeTypeEnum(receiptHeaderForm.purposeTypeEnum)
    // cabinetListOptionsBk.value = response.data.purposeTypeListOptions; // 盘点仓库列表（未过滤）

    // purposeTypeListOptions.value = cabinetListOptionsBk.value.filter(item => item.formEnum === receiptHeaderForm.purposeTypeEnum) // 盘点仓库列表
    if (purchase_type.value.length > 0) {
      // receiptHeaderForm.medicationType = purchase_type.value[0].value;
    }
    // 药品类型默认值

  });
  // // 点编辑进入画面的处理
  // if(route.query.item !== undefined){
  //   const partParam = JSON.parse(route.query.item)
  //   if (partParam.partFlg === "edit") {
  //     // 编辑时取得盘点单明细
  //     getstocktakingDetail(partParam.rowData.supplyBusNo).then((response) => {
  //       receiptHeaderForm.busNo = partParam.rowData.supplyBusNo; // 单据号新增
  //       receiptHeaderForm.purposeTypeEnum = warehous_type.value.filter(item => item.value === partParam.rowData.purposeTypeEnum.toString())[0].label // 仓库类型   默认值
  //       purposeTypeListOptions.value = cabinetListOptionsBk.value.filter(item => item.formEnum === receiptHeaderForm.purposeTypeEnum) // 盘点仓库列表
  //       if (purposeTypeListOptions.value.length > 0) { // 判断是否有盘点仓库
  //         if (purposeTypeListOptions.value[0].children.length > 0) { // 判断盘点仓库内是否有货位
  //           freightListOptions.value = purposeTypeListOptions.value[0].children
  //         }
  //       }
  //       receiptHeaderForm.purposeLocationId = partParam.rowData.purposeLocationId_dictText // 盘点仓库   默认值
  //       receiptHeaderForm.purposeLocation = partParam.rowData.purposeLocationStoreId_dictText // 货位   默认值
  //       receiptHeaderForm.occurrenceTime = partParam.rowData.occurrenceTime // 盘点日期   默认值
  //       receiptHeaderForm.medicationType = categoryListOptions.value.filter(item => item.value === partParam.rowData.itemType)[0].label // 药品类型   默认值
  //       // receiptHeaderForm.remake = partParam.rowData.remake // 备注   默认值
  //       form.purchaseinventoryList = response.data.map((item) => {
          
  //         return {
  //           ...item,
  //           stocktakingUnitId : item.unitCode === item.unitCode ? 1 : 2,
  //           totalSourceQuantity : item.totalPurposeQuantity + item.itemQuantity,
  //           totalPrice:item.unitCode === item.unitCode ? item.price * (item.totalPurposeQuantity + item.itemQuantity) : parseFloat(item.partPercent) === 0? item.price * ((item.totalPurposeQuantity + item.itemQuantity)):item.price * ((item.totalPurposeQuantity + item.itemQuantity)/item.partPercent),
  //           profitAmount:item.unitCode === item.unitCode ? item.price * item.itemQuantity : parseFloat(item.partPercent) === 0? item.price * item.itemQuantity:item.price * (item.itemQuantity/item.partPercent),
  //           reasonCodeText: profitReasonOptions.value.filter(reasonItem => reasonItem.value ===  parseFloat(item.reasonCode))[0].label,
  //           reasonCode: item.reasonCode,
  //           unitCodeList:[
  //             {id:1,name:item.unitCode_dictText,unitName:item.unitCode,code:item.unitCode},
  //             {id:2,name:item.minUnitCode_dictText,unitName:item.minUnitCode,code:item.minUnitCode}]
  //         }
  //       })
  //     });
      
  //   }
  // }
}

// 切换仓库类型获取药房/药库列表 -20250414
function handlePurposeType(value) {
  receiptHeaderForm.purposeLocationId = ''
  receiptHeaderForm.purposeLocation = ''
  purposeTypeListOptions.value = cabinetListOptionsBk.value.filter(item => item.formEnum === Number(value)) // 盘点仓库列表
    if (purposeTypeListOptions.value.length > 0) { // 判断是否有盘点仓库
      receiptHeaderForm.purposeLocationId = purposeTypeListOptions.value[0].id // 盘点仓库默认值
      if (purposeTypeListOptions.value[0].children.length > 0) { // 判断盘点仓库内是否有货位
        freightListOptions.value = purposeTypeListOptions.value[0].children
        receiptHeaderForm.purposeLocation = purposeTypeListOptions.value[0].children[0].name
      }
    }
} 
// 切换仓库获取货位列表 -20250414
function handleCabinetChange(value) {
  receiptHeaderForm.purposeLocation = ''
  if(value){
    let purposeTypeListOption = purposeTypeListOptions.value.filter(item => item.id === value.toString())
    freightListOptions.value =  purposeTypeListOption[0]?purposeTypeListOption[0].children:[]
    receiptHeaderForm.purposeLocation = (freightListOptions.value&&freightListOptions.value.length>0)?purposeTypeListOption[0].children[0].name:''
  } else {
    freightListOptions.value = []
  }
}
// 盘点单位切换 -- 20250415
// function handleStockUnitCodeChange(rowData,index,value) {
//   const partPer = parseFloat(rowData.partPercent) === 0? 1 : rowData.partPercent
//   if (value === 1) {
//     form.purchaseinventoryList[index].stocktakingUnitId = 1;
//     form.purchaseinventoryList[index].totalPurposeQuantity = rowData.totalPurposeQuantity / partPer;
//     if (rowData.totalSourceQuantity > 0 && rowData.totalSourceQuantity !== '') {
//       form.purchaseinventoryList[index].totalSourceQuantity = rowData.totalSourceQuantity / partPer;
//       form.purchaseinventoryList[index].itemQuantity = (rowData.totalSourceQuantity / partPer )- (rowData.totalPurposeQuantity / partPer);
//     }
//   }
//   if (value === 2) {
//     form.purchaseinventoryList[index].stocktakingUnitId = 2;
//     form.purchaseinventoryList[index].totalPurposeQuantity = rowData.totalPurposeQuantity * partPer;
//     if (rowData.totalSourceQuantity > 0 && rowData.totalSourceQuantity !== '') {
//       form.purchaseinventoryList[index].totalSourceQuantity = rowData.totalSourceQuantity * partPer;
//       form.purchaseinventoryList[index].itemQuantity = (rowData.totalSourceQuantity * partPer )- (rowData.totalPurposeQuantity * partPer);
//     }
//   }
// }
function reasonCodeChange(row,index){
  editBatchTransfer(index)
}
function lotNumberBlur(row,index){
  editBatchTransfer(index)
}
function reasonBlur(row,index){
  editBatchTransfer(index)
}
// 计算总价
function handleTotalPrice(index) {
  form.purchaseinventoryList[index].oldtotalSourceQuantity = form.purchaseinventoryList[index].totalSourceQuantity * row.partPercent;
  form.purchaseinventoryList[index].itemMaxtotalSourceQuantity = form.purchaseinventoryList[index].totalSourceQuantity
  form.purchaseinventoryList[index].olditemQuantity = form.purchaseinventoryList[index].itemQuantity * row.partPercent;
  form.purchaseinventoryList[index].itemMaxQuantity = form.purchaseinventoryList[index].itemQuantity
  let purchaseItem = form.purchaseinventoryList[index];
  if (purchaseItem.price > 0 && purchaseItem.totalSourceQuantity > 0) {
    form.purchaseinventoryList[index].totalPrice =
      purchaseItem.price * purchaseItem.totalSourceQuantity;
     form.purchaseinventoryList[index].totalPrice = form.purchaseinventoryList[index].totalPrice.toFixed(4);
  }
  if (purchaseItem.price > 0 && purchaseItem.itemQuantity) {
    form.purchaseinventoryList[index].profitAmount =
      purchaseItem.price * purchaseItem.itemQuantity;
     form.purchaseinventoryList[index].profitAmount = form.purchaseinventoryList[index].profitAmount.toFixed(4);
  }
  if(form.purchaseinventoryList[index].itemQuantity==0){
    form.purchaseinventoryList[index].profitAmount = 0
  }
  if(form.purchaseinventoryList[index].totalSourceQuantity==0){
    form.purchaseinventoryList[index].totalPrice = 0
  }
  editBatchTransfer(index)    //todo
  store.setCurrentDataPD({ purchaseinventoryList: form.purchaseinventoryList, receiptHeaderForm: receiptHeaderForm });
}
//实盘数量变更
function totalSourceQuantityChange(rowData,index,value) {
  // let purchaseItem = form.purchaseinventoryList[index];
  // const partPer = parseFloat(rowData.partPercent) === 0? 1 : rowData.partPercent
  form.purchaseinventoryList[index].lllstatus = false
  if(rowData.oldtotalPurposeQuantity&&rowData.oldtotalPurposeQuantity>0&&rowData.unitCode==rowData.unitList.unitCode){

    const integerPart = Math.floor(rowData.oldtotalPurposeQuantity/rowData.partPercent); // 获取整数部分
    const decimalPart = (rowData.oldtotalPurposeQuantity/rowData.partPercent) - integerPart; // 获取小数部分
    if(decimalPart){
      let zhengshu = rowData.totalPurposeQuantity.split(form.purchaseinventoryList[index].unitList.unitCode_dictText)[0]
      let xiaoshu = rowData.totalPurposeQuantity.split(form.purchaseinventoryList[index].unitList.unitCode_dictText)[1]
      if(xiaoshu){
        let xiaoshuzhi = xiaoshu.split(form.purchaseinventoryList[index].unitList.minUnitCode_dictText)[0]
       
        form.purchaseinventoryList[index].itemQuantity = 
        (parseFloat(zhengshu) - parseFloat(rowData.totalSourceQuantity))
        + form.purchaseinventoryList[index].unitList.unitCode_dictText
        + xiaoshuzhi
        + form.purchaseinventoryList[index].unitList.minUnitCode_dictText
        if (rowData.price > 0 && rowData.itemQuantity) {
          form.purchaseinventoryList[index].lllstatus = true
          form.purchaseinventoryList[index].profitAmount = 
          rowData.price * (parseFloat(zhengshu) - parseFloat(rowData.totalSourceQuantity))
          + (rowData.price/rowData.partPercent) * xiaoshuzhi
          form.purchaseinventoryList[index].profitAmount = form.purchaseinventoryList[index].profitAmount.toFixed(4);
        }
      }
    }else{
      form.purchaseinventoryList[index].itemQuantity = rowData.totalSourceQuantity - rowData.totalPurposeQuantity 
        if (rowData.price > 0 && rowData.itemQuantity &&rowData.unitList.minUnitCode==rowData.unitCode) {
          form.purchaseinventoryList[index].profitAmount =
            rowData.price * rowData.itemQuantity;
          form.purchaseinventoryList[index].profitAmount = form.purchaseinventoryList[index].profitAmount.toFixed(4);
        }
    }
  }else{
    form.purchaseinventoryList[index].itemQuantity = rowData.totalSourceQuantity - rowData.totalPurposeQuantity 
  }
  if (rowData.price > 0 && rowData.itemQuantity &&!form.purchaseinventoryList[index].lllstatus) {
    form.purchaseinventoryList[index].profitAmount =
      rowData.price * rowData.itemQuantity;
     form.purchaseinventoryList[index].profitAmount = form.purchaseinventoryList[index].profitAmount.toFixed(4);
  }
  if (rowData.price > 0 && rowData.totalSourceQuantity > 0) {
    form.purchaseinventoryList[index].totalPrice =
      rowData.price * rowData.totalSourceQuantity;
     form.purchaseinventoryList[index].totalPrice = form.purchaseinventoryList[index].totalPrice.toFixed(4);
  }
  if(form.purchaseinventoryList[index].itemQuantity==0){
    form.purchaseinventoryList[index].profitAmount =0
  }
  editBatchTransfer(index)
  store.setCurrentDataPD({ purchaseinventoryList: form.purchaseinventoryList, receiptHeaderForm: receiptHeaderForm });
  // form.purchaseinventoryList[index].totalPrice = rowData.purposeLocationId === 1 ? rowData.price * rowData.totalSourceQuantity : rowData.price * (rowData.totalSourceQuantity / partPer)
  // form.purchaseinventoryList[index].profitAmount = rowData.purposeLocationId === 1 ? rowData.price * (rowData.totalSourceQuantity - rowData.totalPurposeQuantity ) : rowData.price * ((rowData.totalSourceQuantity - rowData.totalPurposeQuantity ) / partPer)
  // form.purchaseinventoryList[index].profitAmount = form.purchaseinventoryList[index].profitAmount.toFixed(4);
  // form.purchaseinventoryList[index].totalPrice = form.purchaseinventoryList[index].totalPrice.toFixed(4);
}
// 切换仓库获取货位列表 -20250414
function getbusNo() {
  if(route.query.supplyBusNo){
    store.clearCurrentDataPD()
    receiptHeaderForm.busNo = route.query.supplyBusNo
    viewStatus.value = route.query.view
    sessionStorage.setItem('busNopd', "")
  }else{
    if(!sessionStorage.getItem('busNopd')){
      store.clearCurrentDataPD()
      getDetailInit().then((response) => {
        receiptHeaderForm.busNo = response.data.busNo;
        sessionStorage.setItem('busNopd', receiptHeaderForm.busNo)
        // busNoAdd.value = response.data.busNo; // 单据号新增
      })
    }else{
      receiptHeaderForm.busNo = sessionStorage.getItem('busNopd')
    }
  }
}
getStockReceiptTypeList();// 详情页查询下拉树结构
getbusNo();// 单据号取得
getTransferProductDetails()
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