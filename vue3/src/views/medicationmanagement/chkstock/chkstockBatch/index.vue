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
        <el-button type="primary" plain icon="Printer" disabled @click="handleDelete" >打印</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus"  @click="handleBatchAdd" >生成批量盘点单</el-button>
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
    </el-row>
    <el-form :model="receiptHeaderForm" ref="receiptHeaderRef" :inline="true" label-width="100px" :rules="rules">
      <el-form-item label="单据号：" prop="busNo">
        <el-input v-model="receiptHeaderForm.busNo" placeholder="单据号：" clearable style="width: 260px" :disabled="data.isEdit" />
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
              <el-button
                type="primary"
                plain
                icon="Plus"
                @click="handleSave"
                
                >批量保存</el-button
              >
              <!-- v-hasPermi="['system:user:edit']" -->
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
              label="项目编码"
              align="center"
              key="itemBusNo"
              prop="itemBusNo"
              width="160"
              fixed
              :show-overflow-tooltip="true"
            >
            <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.itemBusNo`"
                  :rules="tableRules.itemBusNo"
                >
                  <el-input
                    v-model="scope.row.itemBusNo"
                    placeholder=""
                    disabled
                  />
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="项目名称"
              align="center"
              key="itemName"
              prop="itemName"
              width="160"
              fixed
              :show-overflow-tooltip="true"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.itemName`"
                  :rules="tableRules.itemName"
                >
                <el-input
                  v-model="scope.row.itemName"
                  placeholder=""
                  disabled
                />
                  <!-- <PopoverList
                    @search="handleSearch"
                    :width="1000"
                    :modelValue="scope.row.name"
                  >
                    <template #popover-content="{}">
                      <MedicineList
                        @selectRow="(row) => selectRow(row, scope.$index)"
                        :searchKey="medicineSearchKey"
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
              :show-overflow-tooltip="true"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.totalVolume`"
                  :rules="tableRules.totalVolume"
                >
                  <el-input
                    disabled
                    v-model="scope.row.totalVolume"
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
              width="120"
              :show-overflow-tooltip="true"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.lotNumber`"
                >
                  <el-input v-model="scope.row.lotNumber" placeholder="" disabled/>
                </el-form-item>
              </template>
            </el-table-column>
            
            <!-- // 包装单位  一会细研究 -->
            <!-- <el-table-column
              label="包装单位"
              align="center"
              key="unitCode_dictText"
              prop="unitCode_dictText"
              :show-overflow-tooltip="true"
              width="110"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.unitCode`"
                  :rules="tableRules.unitCode"
                >
                  <div class="select_wrapper_div">
                    <el-select
                      v-model="scope.row.stockUnitCode"
                      placeholder="请选择包装单位"
                      :class="{ 'error-border': scope.row.error }"
                      clearable
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
            </el-table-column> -->
            <el-table-column
              label="拆零比  "
              align="center"
              key="partPercent"
              prop="partPercent"
              width="110"
              :show-overflow-tooltip="true"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.partPercent`"
                  :rules="tableRules.partPercent"
                >
                  <div class="select_wrapper_div">
                    <el-input
                      v-model="scope.row.partPercent"
                      placeholder=""
                      disabled
                    >
                    </el-input>
                  </div>
                </el-form-item>
              </template>
            </el-table-column>
             <el-table-column
              label="单价 "
              align="center"
              key="price"
              prop="price"
              width="120"
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
                      :disabled="viewStatus == 'view'"
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
            <!-- 小包装进价  有采购单价(大单位) price / 拆零比 partPercent  -->
            <!-- <el-table-column
              label="小包装进价 "
              align="center"
              key="price"
              prop="price"
              width="110"
              :show-overflow-tooltip="true"
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
            </el-table-column> -->
            <el-table-column
              label="盘前库存"
              align="center"
              key="totalPurposeQuantity"
              prop="totalPurposeQuantity"
              width="110"
              :show-overflow-tooltip="true"
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
              width="110"
              :show-overflow-tooltip="true"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.totalSourceQuantity`"
                  :rules="tableRules.totalSourceQuantity"
                >
                  <el-input
                    v-model="scope.row.totalSourceQuantity"
                    placeholder=""
                    :disabled="viewStatus == 'view'"
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
              width="130"
              :show-overflow-tooltip="true"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.totalPrice`"
                  :rules="tableRules.totalPrice"
                >
                  <div class="select_wrapper_div">
                    <el-input
                      disabled
                      v-model="scope.row.totalPrice"
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
              label="盈亏数量"
              align="center"
              key="itemQuantity"
              prop="itemQuantity"
              width="110"
              :show-overflow-tooltip="true"
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
              width="140"
              :show-overflow-tooltip="true"
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
              width="110"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.reasonCode`"
                  :rules="tableRules.reasonCode"
                >
                  <div class="select_wrapper_div">
                    <el-select
                      v-model="scope.row.reasonCode"
                      placeholder="请选择盈亏类型"
                      :class="{ 'error-border': scope.row.error }"
                      clearable
                      :disabled="viewStatus == 'view'"
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
              width="110"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.reason`"
                  :rules="tableRules.reason"
                >
                  <el-input
                    v-model="scope.row.reason"
                    placeholder=""
                    :disabled="viewStatus == 'view'"
                    @blur="reasonBlur(scope.row,scope.$index)"
                  />
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column
              label="国家医保码"
              align="center"
              key="ybNo"
              prop="ybNo"
              width="240"
              :show-overflow-tooltip="true"
            >
              <template #default="scope">
                <el-form-item
                  :prop="`purchaseinventoryList.${scope.$index}.ybNo`"
                  :rules="tableRules.ybNo"
                >
                  <el-input
                    v-model="scope.row.ybNo"
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
              width="260"
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
                    <el-input v-model="scope.row.traceNo" :disabled="viewStatus == 'view'" placeholder="" :id ="'traceNo'+`${scope.$index}`"
                    />
                  </el-form-item>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column
              v-if="viewStatus != 'view'"
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
            v-if="route.query.supplyBusNo||(forms.purchaseinventoryList&&forms.purchaseinventoryList[0]&&forms.purchaseinventoryList[0].id)"
            v-show="total > 0"
            :total="total"
            v-model:page="queryParams.pageNo"
            v-model:limit="queryParams.pageSize"
            @pagination="getbusNo"
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
      <!-- <el-col :span="2">
        <span>审核人：</span>
      </el-col>
      <el-col :span="2">
        <span>单据状态：</span>
      </el-col> -->
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
          <span style="margin-right: 20px">药品名称： {{ medName }}</span>
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
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="chkstockBatch">
import {
  submitApproval,
  getInit,
  delProductStocktaking,
  getPharmacyList,
  getCount,
  getDispensaryList,
  getMedicineList,
  getDetailInit,
  getstocktakingDetail, //查询盘点详情
  getStocktakingReceiptBatch, //生成批量盘点
  addBatch,  //保存批量盘点
  productStocktakingApproved,
  reject
} from "../components/api";
import PopoverList from "@/components/OpenHis/popoverList/index.vue";
import MedicineList from "../components/medicineList.vue";
import { formatDate,formatDateymd } from "@/utils/index";
import { ref } from "vue";
import { debounce } from 'lodash-es';

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();
import useUserStore from "@/store/modules/user";
import { useStore } from '@/store/store';
import useTagsViewStore from '@/store/modules/tagsView';
const tagsViewStore = useTagsViewStore();
const store = useStore();
const { proxy } = getCurrentInstance();
const { warehous_type,purchase_type,profit_reason } = 
proxy.useDict( "warehous_type","purchase_type","profit_reason");
const forms = reactive({
  purchaseinventoryList: [],
});
const viewStatus = ref("")
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
const rowList = ref([])
const openTraceNo = ref(false)
const traceNo = ref('');
const traceNoList = ref([]);
const traceNoTemp = ref('');
const traceNoTempRef = ref();
const currentIndex = ref("");
const medName = ref("");
const props = defineProps({
  // 仓库
  purposeTypeListOptions: {
    type: Object,
    required: false,
  },
  // 药品类型
  categoryListOptions:{
    type: Object,
    required: false,
  },
  //单据号新增
  busNoAdd: {
    type: String,
    required: true,
  },
  // 编辑时盘点单
  item: {
    type: Object,
    required: false,
  },
  // 编辑行
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
    // searchKey: undefined, // 供应商名称forms
    sourceLocationId:'',
    medicationType:'',
    busNo: "", // 编码
    // statusEnum: undefined, // 状态
    // supplierId: undefined, // 供应商ID
    // applyTimeStart: undefined, // 申请时间开始
    // practitionerId: undefined, // 经手人ID
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
  },
});

const { queryParams, rules, tableRules } = toRefs(data);
const purposeTypeListOptions = ref(undefined); // 仓库列表
const freightListOptions = ref(undefined); // 货位列表
const categoryListOptions = ref(undefined); // 药品类型
const profitReasonOptions = ref(undefined); // 盈利原因
const selectedRows = ref([]); // 用于存储选中的行
const emit = defineEmits(["refresh"]);
const tableRef = ref(undefined); // 表格引用
const currentRow = ref(undefined); // 当前操作的行
const medicineSearchKey = ref("");
const itemType = ref("");

watch(
  () => store.currentDataPLPD,
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
  () => store.currentDataPLPDALL,
  (newVlaue) => {
    if (newVlaue&&!route.query.supplyBusNo) {
       console.log(newVlaue,"--------------------------------------")
      forms.purchaseinventoryList = newVlaue?.purchaseinventoryList
      console.log(forms.purchaseinventoryList,"forms.purchaseinventoryList")
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

function addNewRow() {
  proxy.$refs["receiptHeaderRef"].validate((valid) => {
    if (valid) {
      if (data.isAdding) {
        proxy.$message.warning("请先保存当前行后再新增！");
        return;
      }
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
function handleScan(row,index){
  medName.value = row.itemName
  openTraceNo.value = true;
  currentIndex.value = index
}
function handleClear() {
  traceNo.value = '';
  traceNoList.value = [];
}
const throttledGetList = debounce(handelTraceNo, 500);
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
  if(row.itemQuantity==0 || !row.itemQuantity){
    row.itemQuantity = 0 
    row.profitAmount = 0
  }
  if(row.totalSourceQuantity==0 || !row.totalSourceQuantity ){
    row.totalSourceQuantity = 0
    row.totalPrice = 0
  }
  row.totalSourceQuantity = Number(row.totalSourceQuantity)
  console.log(receiptHeaderForm,row,"receiptHeaderForm")
  // row.sourceLocationId =receiptHeaderForm.sourceLocationId
  // row.sourceLocationStoreId = receiptHeaderForm.sourceLocationStoreId;
  row.purposeLocationId = receiptHeaderForm.purposeLocationId
  row.purposeLocationStoreId = receiptHeaderForm.purposeLocationStoreId;
  row.busNo = receiptHeaderForm.busNo;
  row.applyTime = formatDate(row.applyTime) 
  row.startTime = formatDateymd(row.startTime)
  row.endTime = formatDateymd(row.endTime)
  row.occurrenceTime = receiptHeaderForm.occurrenceTime;
  let purposeTypeEnum = warehous_type.value.filter(e=>{return e.label==receiptHeaderForm.purposeTypeEnum})
  row.purposeTypeEnum = (purposeTypeEnum&&purposeTypeEnum[0])?purposeTypeEnum[0].value:receiptHeaderForm.purposeTypeEnum
  // let sourceLocationId = sourceTypeListOptions.value.filter(e=>{return e.name==receiptHeaderForm.sourceLocationId})
  // row.sourceLocationId = (sourceLocationId&&sourceLocationId[0])?sourceLocationId[0].id:receiptHeaderForm.sourceLocationId
  let purposeLocationId = purposeTypeListOptions.value.filter(e=> e.name==receiptHeaderForm.purposeLocationId)
  row.purposeLocationId = (purposeLocationId&&purposeLocationId[0])?purposeLocationId[0].id:receiptHeaderForm.purposeLocationId
  row.applicantId = userStore.id
  if(row.totalPurposeQuantity==row.totalSourceQuantity){
    row.itemQuantity = 0
    row.profitAmount = 0
  }
}


// 点击行时记录当前行
function handleRowClick(row) {
  // getMedicineList({ itemId: row.itemId }).then((res) => {
  //   console.log(res.data);
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

// 药品列表搜索
function handleSearch(value) {
  medicineSearchKey.value = value;
}

const locationList = ref([]);
// 选择药品
function selectRow(rowValue, index) {
  console.log(receiptHeaderForm,rowValue,"receiptHeaderForm.sourceLocationId1")
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
}
// function selectRow(rowValue, index) {
//   console.log('选择药品',rowValue, index)
//   rowIndex.value = index;
//   form.purchaseinventoryList[index].itemBusNo = rowValue.itemBusNo;
//   form.purchaseinventoryList[index].name = rowValue.name;
//   form.purchaseinventoryList[index].totalVolume = rowValue.volume;
//   form.purchaseinventoryList[index].lotNumber = rowValue.lotNumber;
//   form.purchaseinventoryList[index].ybNo = rowValue.ybNo;
//   form.purchaseinventoryList[index].unitCode = rowValue.unitCode_dictText;
//   form.purchaseinventoryList[index].stockUnitCode = rowValue.unitCode_dictText;
//   form.purchaseinventoryList[index].partPercent = rowValue.partPercent;
//   form.purchaseinventoryList[index].price = rowValue.purchasePrice;
//   form.purchaseinventoryList[index].totalQuantity = rowValue.quantity;
//   form.purchaseinventoryList[index].totalSourceQuantity = '';
//   form.purchaseinventoryList[index].totalPrice = '';
//   form.purchaseinventoryList[index].itemQuantity = '';
//   form.purchaseinventoryList[index].reasonCode = '';
//   form.purchaseinventoryList[index].reason = '';
//   form.purchaseinventoryList[index].manufacturer = rowValue.manufacturer;
//   form.purchaseinventoryList[index].unitCodeList = [
//           {id:1,name:rowValue.unitCode_dictText,unitName:rowValue.unitCode_dictText,code:rowValue.unitCode},
//           {id:2,name:rowValue.minUnitCode_dictText,unitName:rowValue.minUnitCode_dictText,code:rowValue.minUnitCode}];
//   console.log(form.purchaseinventoryList[index], 123);
// }

/**计算合计金额 */
function handleTotalAmount() {
  totalAmount.value = form.purchaseinventoryList.reduce(
    (accumulator, currentRow) => {
      if(accumulator){
        return (accumulator + (Number(currentRow.profitAmount) || 0))
      }else{
        return (accumulator + (currentRow.profitAmount) || 0)
      }
    },
    0
  );
}
// 选择仓库
function handleLocationClick(purposeLocationId,itemId,index,lotNumber) {
  getCount({
    itemId: itemId,
    orgLocationId: purposeLocationId,
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
      console.log(res.data[0] ,form.purchaseinventoryList[index].minUnitCode,form.purchaseinventoryList[index].unitList,'res.data[0].minUnitCode ')
      form.purchaseinventoryList[index].unitCode = form.purchaseinventoryList[index].unitList.minUnitCode
      form.purchaseinventoryList[index].unitCode_dictText = form.purchaseinventoryList[index].unitList.minUnitCode_dictText
       // 单价   大单位单价
      if(form.purchaseinventoryList[index].unitCode==form.purchaseinventoryList[index].unitList.minUnitCode){
        form.purchaseinventoryList[index].price = res.data[0].price / form.purchaseinventoryList[index].partPercent|| "";
        form.purchaseinventoryList[index].price = form.purchaseinventoryList[index].price.toFixed(4);
        // parseFloat(form.purchaseinventoryList[index].price.toFixed(4))
      }else{
        if(form.purchaseinventoryList[index].price>1){
          form.purchaseinventoryList[index].price = form.purchaseinventoryList[index].price.toFixed(4);
        }
      }
      startTimeOld.value = form.purchaseinventoryList[index].startTime?form.purchaseinventoryList[index].startTime:''
      endTimeOld.value = form.purchaseinventoryList[index].endTime?form.purchaseinventoryList[index].endTime:''
      console.log( form.purchaseinventoryList[index].endTime,form.purchaseinventoryList[index].startTime,12)
      if (form.purchaseinventoryList[index].totalPurposeQuantity == 0) {
        proxy.$message.warning('仓库数量为0，无法调用！');
        return;
      }
    }else {
      form.purchaseinventoryList[index].totalPurposeQuantity = 0;
      form.purchaseinventoryList[index].totalSourceQuantity = 0
      form.purchaseinventoryList[index].price = 0;
      proxy.$message.warning('仓库数量为0，无法调用！');
    }
  });
}

// 单位处理
function handleUnitCodeChange(row, index, value) {
  console.log(row, index, value,12121211)
  if(!form.purchaseinventoryList[index].statusMaxvalue&&Number(row.measurementUnitCode)==Number(value)){
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
      console.log(integerPart,decimalPart,form.purchaseinventoryList[index].itemQuantity,121212121)
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
    }else{
      form.purchaseinventoryList[index].price =
      form.purchaseinventoryList[index].price * row.partPercent;
      form.purchaseinventoryList[index].price = form.purchaseinventoryList[index].price.toFixed(4);
    }
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
      console.log(integerPart,decimalPart,forms.purchaseinventoryList[index].itemQuantity,121212121)
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
// function getMaxCounts(row,index,counts){
//   form.purchaseinventoryList[index].oldtotalSourceQuantity =  form.purchaseinventoryList[index].totalSourceQuantity
//   form.purchaseinventoryList[index].totalSourceQuantity =
//   form.purchaseinventoryList[index].totalSourceQuantity / row.partPercent;

//   const integerPart1 = Math.floor(form.purchaseinventoryList[index].totalSourceQuantity); // 获取整数部分
//   const decimalPart1 = form.purchaseinventoryList[index].totalSourceQuantity - integerPart1; // 获取小数部分

//   if(decimalPart1){
//     form.purchaseinventoryList[index].totalSourceQuantity = integerPart1 + form.purchaseinventoryList[index].unitList.unitCode_dictText +
//     parseFloat(decimalPart1.toFixed(1))*row.partPercent +
//     form.purchaseinventoryList[index].unitList.minUnitCode_dictText
//   }

//   form.purchaseinventoryList[index].oldtotalPurposeQuantity =  form.purchaseinventoryList[index].totalPurposeQuantity
//   form.purchaseinventoryList[index].totalPurposeQuantity =
//   form.purchaseinventoryList[index].totalPurposeQuantity / row.partPercent;

//   const integerPart2 = Math.floor(form.purchaseinventoryList[index].totalPurposeQuantity); // 获取整数部分
//   const decimalPart2 = form.purchaseinventoryList[index].totalPurposeQuantity - integerPart2; // 获取小数部分

//   if(decimalPart2){
//     form.purchaseinventoryList[index].totalPurposeQuantity = integerPart2 + form.purchaseinventoryList[index].unitList.unitCode_dictText +
//     parseFloat(decimalPart2.toFixed(1))*row.partPercent +
//     form.purchaseinventoryList[index].unitList.minUnitCode_dictText
//   }
//   //调拨数量
//   if(counts){
//     form.purchaseinventoryList[index].olditemQuantity =  form.purchaseinventoryList[index].itemQuantity*row.partPercent
//     form.purchaseinventoryList[index].itemMaxQuantity = form.purchaseinventoryList[index].itemQuantity
//     const integerPart = Math.floor(form.purchaseinventoryList[index].itemQuantity); // 获取整数部分
//     const decimalPart = form.purchaseinventoryList[index].itemQuantity - integerPart; // 获取小数部分
//     console.log(integerPart,decimalPart,form.purchaseinventoryList[index].itemQuantity,121212121)
//     if(decimalPart){
//       form.purchaseinventoryList[index].itemQuantity = integerPart + form.purchaseinventoryList[index].unitList.unitCode_dictText +
//       parseFloat(decimalPart.toFixed(1))*row.partPercent +
//       form.purchaseinventoryList[index].minUnitCode_dictText
//     }
//   }else{
//     form.purchaseinventoryList[index].olditemQuantity =  form.purchaseinventoryList[index].itemQuantity
//     form.purchaseinventoryList[index].itemQuantity =
//     form.purchaseinventoryList[index].itemQuantity / row.partPercent;
//     form.purchaseinventoryList[index].itemMaxQuantity = form.purchaseinventoryList[index].itemQuantity
//     const integerPart = Math.floor(form.purchaseinventoryList[index].itemQuantity); // 获取整数部分
//     const decimalPart = form.purchaseinventoryList[index].itemQuantity - integerPart; // 获取小数部分

//     if(decimalPart){
//       form.purchaseinventoryList[index].itemQuantity = integerPart + form.purchaseinventoryList[index].unitList.unitCode_dictText +
//       parseFloat(decimalPart.toFixed(1))*row.partPercent +
//       form.purchaseinventoryList[index].unitList.minUnitCode_dictText
//     }
//   }
// }

function handleSave() {
  if(form.purchaseinventoryList.length==0){
    proxy.$message.warning("请先生成批量盘点单在进行批量保存！");
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
              // 当前行没有id视为首次新增
              // if (!row.id) {
              //   data.isAdding = false; // 允许新增下一行
              // }
              let rows = JSON.parse(JSON.stringify(row))
              // delete rows.itemMaxQuantity
              // delete rows.olditemQuantity
              // delete rows.oldtotalSourceQuantity
              // delete rows.oldtotalPurposeQuantity 
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
              if(rows.itemQuantity&&typeof rows.itemQuantity==='string'&& rows.itemQuantity.split(rows.unitCode)){
                rows.price = rows.price/rows.partPercent
                rows.unitCode = rows.unitList.minUnitCode
                rows.unitCode_dictText= rows.unitList.minUnitCode_dictText
                rows.totalPurposeQuantity =rows.oldtotalPurposeQuantity
                rows.totalSourceQuantity = rows.totalSourceQuantity*rows.partPercent   
                rows.itemQuantity= rows.totalSourceQuantity - rows.totalPurposeQuantity 
              }
              rowList.value.push(JSON.parse(JSON.stringify(rows)))
              if(rowList._rawValue&&rowList._rawValue.length == forms.purchaseinventoryList.length){
                addTransferProducts(rowList._rawValue)
              }
            }
          });
        }
      })
    }
  })
  // 保存逻辑...
}
function addTransferProducts(rowList){
  console.log(rowList,JSON.parse(JSON.stringify(rowList)),"rowList")
  addBatch(JSON.parse(JSON.stringify(rowList))).then((res) => {

    if (res.data) {
      console.log(res.data,"res.data")
      proxy.$message.success("保存成功！");
      form.purchaseinventoryList.map((row,index)=>{
        console.log(row,"res.data")
        form.purchaseinventoryList[index].id = res.data[index]
        form.purchaseinventoryList[index].isSave = true;
      })
      forms.purchaseinventoryList.map((row,index)=>{
        console.log(row,"res.data")
        forms.purchaseinventoryList[index].id = res.data[index]
        forms.purchaseinventoryList[index].isSave = true;
      })
      store.setCurrentDataPLPD({purchaseinventoryList: form.purchaseinventoryList,receiptHeaderForm: receiptHeaderForm });
      store.setCurrentDataPLPDAll({purchaseinventoryList: forms.purchaseinventoryList});
    }
  });
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
function handleBatchAdd(){
   proxy.$refs["receiptHeaderRef"].validate((valid) => {
    if (valid) {
      if (data.isAdding) {
        proxy.$message.warning("请先保存当前行后再新增！");
        return;
      }
      data.isAdding = true; // 设置标志位为 true，表示有未保存的
      console.log(receiptHeaderForm,form.purchaseinventoryList,"receiptHeaderForm.value")
      getBatchList()
      // console.log(res.data,receiptHeaderForm,"获取详情");
    }
  })
  // proxy.$message.warning("等待后端提供接口进行联调");
}
function getDetailAll(){
  forms.purchaseinventoryList.map((e,index)=>{
    // form.purchaseinventoryList[index].statusMaxvalue =  false
    e.volume = e.totalVolume
    e.name = e.itemName
    e.manufacturer = e.manufacturerText
    if(e.purposeTypeEnum){
      warehous_type.value.map(item=>{
        if(item.value == e.purposeTypeEnum ){
          receiptHeaderForm.purposeTypeEnum = item.label
        }
      })
      handleChangePurposeTypeEnum(e.purposeTypeEnum,1)
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
    if(e.purposeLocationId){
      receiptHeaderForm.purposeLocationId1 = e.purposeLocationId
      receiptHeaderForm.purposeLocationId =  e.purposeLocationName?e.purposeLocationName:""
      if(purposeTypeListOptions.value){
        let purposeTypeList = purposeTypeListOptions.value.filter(k=> k.id==e.purposeLocationId)
        receiptHeaderForm.purposeLocationId = purposeTypeList?purposeTypeList[0].name:''
      }
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
      console.log(forms.purchaseinventoryList, e.unitList,"获取详情12");
    }
    // else{
    //   if(!e.unitList&&e.unitCode){
    //     forms.purchaseinventoryList[index].unitList = {unitCode: forms.purchaseinventoryList[index].unitCode,unitCode_dictText:forms.purchaseinventoryList[index].unitCode_dictText,
    //       minUnitCode:forms.purchaseinventoryList[index].minUnitCode, minUnitCode_dictText:forms.purchaseinventoryList[index].minUnitCode_dictText
    //     }  //计量单位回显数组
    //     forms.purchaseinventoryList[index].unitCode = forms.purchaseinventoryList[index].measurementUnitCode_dictText;
    //     forms.purchaseinventoryList[index].unitCode_dictText = forms.purchaseinventoryList[index].measurementUnitCode_dictText;
    //     console.log(forms.purchaseinventoryList[index].measurementUnitCode == forms.purchaseinventoryList[index].unitList.unitCode,1)
    //     if(forms.purchaseinventoryList[index].measurementUnitCode == forms.purchaseinventoryList[index].unitList.unitCode){  // 回显大单位
    //       console.log(e,index,1,forms.purchaseinventoryList[index].unitList)
    //       getMaxCountsAll(e,index,1)
    //     }else{
    //       forms.purchaseinventoryList[index].totalSourceQuantity = forms.purchaseinventoryList[index].totalPurposeQuantity + forms.purchaseinventoryList[index].itemQuantity
    //     }
    //   }
    // }
    if(e.price){
      console.log(forms.purchaseinventoryList[index].measurementUnitCode,forms.purchaseinventoryList[index].unitList.minUnitCode,1)
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
      forms.purchaseinventoryList[index].price = 0
      forms.purchaseinventoryList[index].totalPrice = 0
      forms.purchaseinventoryList[index].profitAmount = 0  
    }

    forms.purchaseinventoryList[index].reasonCode = forms.purchaseinventoryList[index].reasonCode
    if(forms.purchaseinventoryList[index].totalPurposeQuantity== forms.purchaseinventoryList[index].totalSourceQuantity){
      forms.purchaseinventoryList[index].itemQuantity = 0 
      forms.purchaseinventoryList[index].profitAmount = 0
    }
  })
  store.setCurrentDataPLPDAll({ purchaseinventoryList: forms.purchaseinventoryList});
}
function getDetail(type){
  form.purchaseinventoryList.map((e,index)=>{
    if(type){  //编辑
      e.isSave = true
    }else{
      e.isSave = false
    }
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
      handleChangePurposeTypeEnum(e.purposeTypeEnum,1)
    }
    
    if(e.purposeLocationId){
      receiptHeaderForm.purposeLocationId1 = e.purposeLocationId
      receiptHeaderForm.purposeLocationId =  e.purposeLocationName?e.purposeLocationName:""
      if(purposeTypeListOptions.value){
        let purposeTypeList = purposeTypeListOptions.value.filter(k=> k.id==e.purposeLocationId)
        receiptHeaderForm.purposeLocationId = purposeTypeList?purposeTypeList[0].name:''
      }
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
      console.log(form.purchaseinventoryList, e.unitList,"获取详情12");
    }else{
      if(!e.unitList&&e.unitCode){
        form.purchaseinventoryList[index].unitList = {unitCode: form.purchaseinventoryList[index].unitCode,unitCode_dictText:form.purchaseinventoryList[index].unitCode_dictText,
          minUnitCode:form.purchaseinventoryList[index].minUnitCode, minUnitCode_dictText:form.purchaseinventoryList[index].minUnitCode_dictText
        }  //计量单位回显数组
        form.purchaseinventoryList[index].unitCode = form.purchaseinventoryList[index].measurementUnitCode_dictText;
        form.purchaseinventoryList[index].unitCode_dictText = form.purchaseinventoryList[index].measurementUnitCode_dictText;
        console.log(form.purchaseinventoryList[index].measurementUnitCode == form.purchaseinventoryList[index].unitList.unitCode,1)
        if(form.purchaseinventoryList[index].measurementUnitCode == form.purchaseinventoryList[index].unitList.unitCode){  // 回显大单位
          console.log(e,index,1,form.purchaseinventoryList[index].unitList)
          getMaxCounts(e,index,1)
        }else{
          form.purchaseinventoryList[index].totalSourceQuantity = form.purchaseinventoryList[index].totalPurposeQuantity + form.purchaseinventoryList[index].itemQuantity
        }
      }
    }
    if(e.price){
      console.log(form.purchaseinventoryList[index].measurementUnitCode,form.purchaseinventoryList[index].unitList.minUnitCode,1)
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
    // stocktakingUnitId : item.unitCode === item.unitCode ? 1 : 2,
    // form.purchaseinventoryList[index].totalSourceQuantity = form.purchaseinventoryList[index].totalPurposeQuantity + form.purchaseinventoryList[index].itemQuantity,
    // form.purchaseinventoryList[index].totalPrice = form.purchaseinventoryList[index].unitCode === form.purchaseinventoryList[index].unitCode ? form.purchaseinventoryList[index].price * (form.purchaseinventoryList[index].totalPurposeQuantity + form.purchaseinventoryList[index].itemQuantity) : parseFloat( form.purchaseinventoryList[index].partPercent) === 0?form.purchaseinventoryList[index].price * (( form.purchaseinventoryList[index].totalPurposeQuantity +  form.purchaseinventoryList[index].itemQuantity)): form.purchaseinventoryList[index].price * (( form.purchaseinventoryList[index].totalPurposeQuantity +  form.purchaseinventoryList[index].itemQuantity)/ form.purchaseinventoryList[index].partPercent)
    // form.purchaseinventoryList[index].profitAmount = form.purchaseinventoryList[index].unitCode === form.purchaseinventoryList[index].unitCode ? form.purchaseinventoryList[index].price * form.purchaseinventoryList[index].itemQuantity : parseFloat(form.purchaseinventoryList[index].partPercent) === 0?form.purchaseinventoryList[index].price *  form.purchaseinventoryList[index].itemQuantity: form.purchaseinventoryList[index].price * ( form.purchaseinventoryList[index].itemQuantity/ form.purchaseinventoryList[index].partPercent)
   
    // const profitReasonOptionslabel = profitReasonOptions.value.filter(reasonItem => reasonItem.value ===  parseFloat(form.purchaseinventoryList[index].reasonCode))
    // console.log(profitReasonOptionslabel,"333")
    // form.purchaseinventoryList[index].reasonCodeText = profitReasonOptionslabel[0]?profitReasonOptionslabel[0].label:''
    form.purchaseinventoryList[index].reasonCode =  form.purchaseinventoryList[index].reasonCode
    if(form.purchaseinventoryList[index].totalPurposeQuantity== form.purchaseinventoryList[index].totalSourceQuantity){
      form.purchaseinventoryList[index].itemQuantity = 0 
      form.purchaseinventoryList[index].profitAmount = 0
    }
  })
  if(!type){  //编辑
    store.setCurrentDataPLPD({ purchaseinventoryList: form.purchaseinventoryList, receiptHeaderForm: receiptHeaderForm });
  }
}
function reasonCodeChange(row,index){
  editBatchTransfer(index)
  console.log(forms.purchaseinventoryList[index],form.purchaseinventoryList[index],"备注")
}
function reasonBlur(row,index){
  console.log(row,index,"备注")
  editBatchTransfer(index)
  console.log(forms.purchaseinventoryList[index],form.purchaseinventoryList[index],"备注")
}
function editBatchTransfer(index){
  if(queryParams.value.pageNo==1){
    forms.purchaseinventoryList[index] =  form.purchaseinventoryList[index]
  }else{
    let editIndex = (Number(queryParams.value.pageNo)-1)*Number(queryParams.value.pageSize)+index
    forms.purchaseinventoryList[editIndex] =  form.purchaseinventoryList[index]
  }
}
function getBatchList(type){
  data.isEdit = false;
  handleChangePurposeTypeEnum(receiptHeaderForm.purposeTypeEnum,1)
  let purposeLocationId= purposeTypeListOptions.value.filter(e=>{return e.name==receiptHeaderForm.purposeLocationId})
  queryParams.value.sourceLocationId = (purposeLocationId&&purposeLocationId[0])?purposeLocationId[0].id:receiptHeaderForm.purposeLocationId
  
  // queryParams.value.sourceLocationId=receiptHeaderForm.sourceLocationId
  // queryParams.value.purposeLocationId=receiptHeaderForm.purposeLocationId
  // if(receiptHeaderForm.medicationType=='1'||receiptHeaderForm.medicationType=='2'){
  //   queryParams.value.medicationType=receiptHeaderForm.medicationType
  // }else{
  //   queryParams.value.medicationType=receiptHeaderForm.medicationType1
  // }
  queryParams.value.medicationType=receiptHeaderForm.medicationType
  // console.log(queryParams.value,"queryParams.value")
  if(!type){
    getStocktakingReceiptBatch(queryParams.value).then((res) => {
      form.purchaseinventoryList = res.data.records
      total.value = res.data.total  // 分页
      getDetail()
      getAllBatchList()
    })
  }else{ // 切换页码
    getStocktakingReceiptBatch(queryParams.value).then((res) => {
      form.purchaseinventoryList = res.data.records
      total.value = res.data.total  // 分页
      getDetail()
    })
    console.log(form.purchaseinventoryList[0].id,forms.purchaseinventoryList[0].id,"分页")
    // console.log(total.value,form.purchaseinventoryList,forms.purchaseinventoryList[k],"!212121213")
  }
}
function getAllBatchList(type){
  if(type){
    data.isEdit = true;
  }else{
    data.isEdit = false;
  }
  if(receiptHeaderForm.purposeTypeEnum){
    warehous_type.value.map(item=>{
      if(item.label == receiptHeaderForm.purposeTypeEnum){
        receiptHeaderForm.purposeTypeEnum = item.value
      }
    })
    handleChangePurposeTypeEnum(receiptHeaderForm.purposeTypeEnum,1)
  }
    
  if(purposeTypeListOptions.value){
    let purposeLocationId= purposeTypeListOptions.value.filter(e=>{return e.name==receiptHeaderForm.purposeLocationId})
    queryParams.value.sourceLocationId = (purposeLocationId&&purposeLocationId[0])?purposeLocationId[0].id:receiptHeaderForm.purposeLocationId
  }
  // if(receiptHeaderForm.medicationType=='1'||receiptHeaderForm.medicationType=='2'){
  //   queryParams.value.medicationType=receiptHeaderForm.medicationType
  // }else{
  //   queryParams.value.medicationType=receiptHeaderForm.medicationType1
  // }
  queryParams.value.medicationType=receiptHeaderForm.medicationType
  console.log(queryParams.value,"queryParams.value")
  if(total.value){
    if(type==2){
      let queryParamss = {
        busNo:receiptHeaderForm.busNo,
        pageNo:1,
        pageSize:total.value,
        sourceLocationId:queryParams.value.sourceLocationId,
        // purposeLocationId:queryParams.value.purposeLocationId,
        medicationType:queryParams.value.medicationType
      }
      // 所有数据批量详情
      getstocktakingDetail(queryParamss).then((res) => {
        forms.purchaseinventoryList = res.data.records?res.data.records:res.data
        // total.value = res.data.total
        getDetailAll()
      })
    }else{
      let queryParamss = {
        pageNo:1,
        pageSize:total.value,
        sourceLocationId:queryParams.value.sourceLocationId,
        // purposeLocationId:queryParams.value.purposeLocationId,
        medicationType:queryParams.value.medicationType
      }
      console.log(queryParamss,"queryParamss",total.value)
      getStocktakingReceiptBatch(queryParamss).then((res) => {
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

// 显示弹框
function show() {
  data.isEdit = false;
  data.isAdding = false;
  reset();
  visible.value = true;
  purposeTypeListOptions.value = props.purposeTypeListOptions;
  categoryListOptions.value = props.categoryListOptions
  profitReasonOptions.value = props.profitReasonOptions
  
  console.log('categoryListOptions',categoryListOptions.value)
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
  console.log(props, "111111111111111111111111111111")
  // reset();
  visible.value = true;
  purposeTypeListOptions.value = props.purposeTypeListOptions;
  categoryListOptions.value = props.categoryListOptions
  profitReasonOptions.value = props.profitReasonOptions
  receiptHeaderForm.busNo = props.editRow.supplyBusNo;
  receiptHeaderForm.supplierId = props.editRow.supplierId;
  receiptHeaderForm.practitionerId = props.editRow.practitionerId;
  receiptHeaderForm.occurrenceTime = formatDate(props.editRow.occurrenceTime);
  receiptHeaderForm.purposeTypeEnum = props.editRow.purposeTypeEnum_enumText;
  receiptHeaderForm.medicationType =
    props.editRow.itemTable == "med_medication_definition" ? "1" : "2";
  total.value = form.purchaseinventoryList.length;
  // handleChangeLocationType(props.editRow.purposeTypeEnum.toString());
  setTimeout(() => {
    form.purchaseinventoryList = props.item.map((item) => {
      console.log('ddddddddddddddddddddd',item)
      return {
        ...item,
        unitCodeList:[
          {id:1,name:item.unitCode_dictText,unitName:item.unitCode,code:item.unitCode},
          {id:2,name:item.minUnitCode_dictText,unitName:item.minUnitCode,code:item.minUnitCode}]
      };
    });
    
  console.log(form.purchaseinventoryList, "22222222222222222222")
  }, 100);
  
  loading.value = false;
}
function deleteSelectedRows() {
  let length = selectedRows.value.length;
  let ids  = []
  if(selectedRows.value[0].id){
     ids = selectedRows.value.map((item) => {
      return item.id
    });
  }  
 
  console.log(ids,"ids12121212121")

  if (selectedRows.value[length - 1].isSave) {
    delProductStocktaking(ids).then((res) => {
      if (res.code == 200) {
        proxy.$message.success("删除成功");
      }
    });
  } else {
    if (length > 1&&ids&&ids.length>0) {
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

  // if(form.purchaseinventoryList&&form.purchaseinventoryList.length>0){
  //   data.isEdit = true
  // }else{
  //   data.isEdit = false
  // }
 
  data.isAdding = false;
}
// 驳回
function handleReject() {
  reject(route.query.supplyBusNo).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess("操作成功");
      store.clearCurrentDataPLPD();
      store.clearCurrentDataPLPDALL();
      // 跳转到审核页面
      router.replace({ path: '/aaaa/medicationmanagement/billapproval',query:{type:'chkstockBatch'}});
    }
  });
}
function handelApply() {
  productStocktakingApproved(route.query.supplyBusNo).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess("操作成功");
      store.clearCurrentDataPLPD();
      store.clearCurrentDataPLPDALL();
      // 跳转到审核页面
      router.replace({ path: '/aaaa/medicationmanagement/billapproval',query:{type:'chkstockBatch'}});
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
        // 跳转到审核页面
        router.replace({ path: 'chkstockRecord' });
        store.clearCurrentDataPLDB()
        store.clearCurrentDataPLPDAll()
        emit("refresh");
      }
    });
  }
}

function toLastView(visitedViews, view) {
  const latestView = visitedViews.slice(-1)[0]
  if(view.name== 'ChkstockBatch'){ //调拨单据号删除
    sessionStorage.setItem('busNoplpd',"")
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
      return delProductStocktaking({ ids: delId.join(",") });
    })
    .then(() => {
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}
function handleCabinetChange(value) {
  if(value){
    freightListOptions.value = purposeTypeListOptions.value.filter(item => item.id === value)[0].children
  } else {
    freightListOptions.value = []
  }
}
function getStockReceiptTypeList() {
  data.isAdding = false;
  // 无论任何方式进到画面的默认处理
  getInit().then((response) => {
    console.log('详情页查询下拉树response1111111',response)

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
  editBatchTransfer(index)
}
//实盘数量变更
function totalSourceQuantityChange(rowData,index,value) {
  console.log(rowData.oldtotalPurposeQuantity,rowData.oldtotalPurposeQuantity,222222222222)
  form.purchaseinventoryList[index].lllstatus = false
  if(rowData.oldtotalPurposeQuantity&&rowData.oldtotalPurposeQuantity>0&&rowData.unitCode==rowData.unitList.unitCode){

    const integerPart = Math.floor(rowData.oldtotalPurposeQuantity/rowData.partPercent); // 获取整数部分
    const decimalPart = (rowData.oldtotalPurposeQuantity/rowData.partPercent) - integerPart; // 获取小数部分
    console.log(integerPart,decimalPart,1221212121)
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
}
// 切换仓库类型获取药房/药库列表   目的仓库切换
function handleChangePurposeTypeEnum(value,type) {
  if (value == 16) {
    getPharmacyList().then((res) => {
      purposeTypeListOptions.value = res.data
      if(!route.query.supplyBusNo&&!type){
        receiptHeaderForm.purposeLocationId = ''
        receiptHeaderForm.purposeLocationId1 = ''
      }
      // getinitValue()
    });
  } else if (value == 11) {
    getDispensaryList().then((res) => {
      purposeTypeListOptions.value = res.data
      if(!route.query.supplyBusNo&&!type){
        receiptHeaderForm.purposeLocationId = ''
        receiptHeaderForm.purposeLocationId1 = ''
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
function getbusNo() {
  if(route.query.supplyBusNo||(forms.purchaseinventoryList&&forms.purchaseinventoryList[0]&&forms.purchaseinventoryList[0].id)){  // 编辑
    store.clearCurrentDataPLPD()
    // store.clearCurrentDataPLPDALL();
    data.isEdit = true;
    receiptHeaderForm.busNo = route.query.supplyBusNo?route.query.supplyBusNo:receiptHeaderForm.busNo
    viewStatus.value = route.query.view
    console.log(queryParams.value,"queryParams.value")
    queryParams.value.busNo = receiptHeaderForm.busNo
    getstocktakingDetail(queryParams.value).then((res) => {
      console.log(res.data.records?res.data.records:res.data,1212121)
      form.purchaseinventoryList = res.data.records?res.data.records:res.data
      total.value = res.data.total
      getDetail(1)
      getAllBatchList(2)
    })
    sessionStorage.setItem('busNoplpd', "")
  }else{ //新增
    data.isEdit = false;
    if(!sessionStorage.getItem('busNoplpd')){
      store.clearCurrentDataPLPD()
      // store.clearCurrentDataPLPDALL();
      getDetailInit().then((response) => {
        console.log(response,'response',response.data)
        receiptHeaderForm.busNo = response.data.busNo;
        sessionStorage.setItem('busNoplpd', receiptHeaderForm.busNo)
        // busNoAdd.value = response.data.busNo; // 单据号新增
      })
    }else{
      receiptHeaderForm.busNo = sessionStorage.getItem('busNoplpd')
    }
  }
}
getStockReceiptTypeList();// 详情页查询下拉树结构
getbusNo();// 单据号取得

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
/* 关键样式 */
.custom-tooltip {
  white-space: pre-line;
}
</style>