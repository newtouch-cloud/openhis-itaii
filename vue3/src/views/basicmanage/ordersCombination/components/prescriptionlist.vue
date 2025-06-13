<template>
  <div style="width: 100%">
    <div style="margin-bottom: 5px">
      <el-button type="primary" @click="handleAddPrescription()" :disabled="false">
        添加项目
      </el-button>
      <el-button type="default" @click="saveCombination()" :disabled="false"> 保存 </el-button>
      <el-button type="default" @click="combination()" :disabled="false"> 组合 </el-button>
      <el-button type="default" @click="split()" :disabled="false"> 拆组 </el-button>
      <el-button type="danger" plain @click="handleDelete()" :disabled="false"> 删除 </el-button>
    </div>
    <el-table
      max-height="650"
      ref="prescriptionRef"
      :data="combinationList"
      row-key="uniqueKey"
      border
      @cell-click="clickRow"
      @row-dblclick="clickRowDb"
      :expand-row-keys="expandOrder"
    >
      <el-table-column type="expand" width="1" style="width: 0">
        <template #default="scope">
          <el-form :model="scope.row" :rules="rowRules" :ref="'formRef' + scope.$index">
            <div style="padding: 16px; background: #f8f9fa; border-radius: 8px">
              <template v-if="scope.row.adviceType == 1">
                <div style="display: flex; align-items: center; margin-bottom: 16px; gap: 16px">
                  <span class="medicine-title">
                    {{
                      scope.row.adviceName +
                      ' ' +
                      scope.row.volume +
                      ' [' +
                      scope.row.unitPrice.toFixed(2) +
                      ' 元' +
                      '/' +
                      scope.row.unitCode_dictText +
                      ']'
                    }}
                  </span>
                  <el-form-item prop="lotNumber">
                    <el-select
                      v-model="scope.row.inventoryId"
                      style="width: 180px; margin-right: 20px"
                      placeholder="药房"
                    >
                      <el-option
                        v-for="item in scope.row.stockList"
                        :key="item.inventoryId"
                        :value="item.inventoryId"
                        :label="
                          item.locationName +
                          '  ' +
                          '批次号: ' +
                          item.lotNumber +
                          '  ' +
                          '  库存：' +
                          item.quantity / scope.row.partPercent +
                          item.unitCode_dictText +
                          '  单价：' +
                          item.price.toFixed(2) +
                          '/' +
                          item.unitCode_dictText
                        "
                        @click="handleNumberClick(item, scope.$index)"
                      />
                    </el-select>
                  </el-form-item>
                  <el-form-item
                    label="执行次数："
                    prop="executeNum"
                    class="required-field"
                    data-prop="executeNum"
                    v-if="scope.row.injectFlag == 1"
                  >
                    <el-input-number
                      :min="0"
                      v-model="scope.row.executeNum"
                      controls-position="right"
                      :controls="false"
                      :ref="(el) => (inputRefs.executeNum = el)"
                      @keyup.enter.prevent="handleEnter('executeNum', scope.row, scope.$index)"
                      style="width: 70px; margin-right: 20px"
                    />
                  </el-form-item>
                  <span class="medicine-info"> 皮试：{{ scope.row.skinTestFlag_enumText }} </span>
                  <span class="medicine-info"> 注射药品：{{ scope.row.injectFlag_enumText }} </span>
                  <span class="total-amount">
                    总金额：{{ scope.row.totalPrice ? scope.row.totalPrice + ' 元' : '0.00 元' }}
                  </span>
                </div>
                <div style="display: flex; align-items: center; gap: 12px; flex-wrap: wrap">
                  <div class="form-group">
                    <!-- 单次剂量 -->
                    <el-form-item
                      label="单次剂量："
                      prop="dose"
                      class="required-field"
                      data-prop="dose"
                    >
                      <el-input-number
                        :min="0"
                        v-model="scope.row.dose"
                        controls-position="right"
                        :controls="false"
                        style="width: 70px; margin-right: 20px"
                        :ref="(el) => (inputRefs.dose = el)"
                        @input="convertValues(scope.row, scope.$index)"
                        @keyup.enter.prevent="handleEnter('dose', scope.row, scope.$index)"
                      />
                    </el-form-item>
                    <!-- 剂量单位 -->
                    <el-select
                      v-model="scope.row.doseUnitCode"
                      style="width: 70px; margin-right: 20px"
                      placeholder=" "
                    >
                      <template v-for="item in scope.row.unitCodeList" :key="item.value">
                        <el-option
                          v-if="
                            scope.row.unitCodeList.length == 3
                              ? item.type == unitMap['minUnit']
                              : item.type == unitMap['unit']
                          "
                          :value="item.value"
                          :label="item.label"
                        />
                      </template>
                    </el-select>
                    <span>=</span>
                    <!-- 单次剂量 -->
                    <el-form-item prop="doseQuantity">
                      <el-input-number
                        v-model="scope.row.doseQuantity"
                        controls-position="right"
                        :controls="false"
                        style="width: 70px; margin: 0 20px"
                        @input="convertDoseValues(scope.row, scope.$index)"
                      />
                    </el-form-item>
                    <!-- 全部单位 -->
                    <el-select
                      v-model="scope.row.minUnitCode"
                      style="width: 70px"
                      placeholder=" "
                      @change="convertValues(scope.row, scope.$index)"
                    >
                      <el-option
                        v-for="item in scope.row.unitCodeList"
                        :value="item.value"
                        :label="item.label"
                        :key="item.value"
                      />
                    </el-select>
                  </div>
                  <div class="form-group">
                    <el-form-item
                      label=" "
                      prop="methodCode"
                      class="required-field"
                      data-prop="methodCode"
                    >
                      <el-select
                        v-model="scope.row.methodCode"
                        placeholder="给药途径"
                        clearable
                        style="width: 160px"
                        filterable
                        :ref="(el) => (inputRefs.methodCode = el)"
                        @keyup.enter.prevent="
                          () => {
                            inputRefs.methodCode.blur();
                          }
                        "
                        @visible-change="
                          (value) => {
                            if (!value) {
                              handleEnter('methodCode', scope.row, scope.$index);
                            }
                          }
                        "
                      >
                        <el-option
                          v-for="dict in method_code"
                          :key="dict.value"
                          :label="dict.label"
                          :value="dict.value"
                        />
                      </el-select>
                    </el-form-item>
                    <el-form-item
                      label=" "
                      prop="rateCode"
                      class="required-field"
                      data-prop="rateCode"
                    >
                      <el-select
                        v-model="scope.row.rateCode"
                        placeholder="频次"
                        style="width: 120px"
                        filterable
                        @keyup.enter.prevent="
                          () => {
                            inputRefs.rateCode.blur();
                          }
                        "
                        @change="calculateTotalAmount(scope.row, scope.$index)"
                        @visible-change="
                          (value) => {
                            if (!value) {
                              handleEnter('rateCode', scope.row, scope.$index);
                            }
                          }
                        "
                        :ref="(el) => (inputRefs.rateCode = el)"
                      >
                        <el-option
                          v-for="dict in rate_code"
                          :key="dict.value"
                          :label="dict.label"
                          :value="dict.value"
                        />
                      </el-select>
                    </el-form-item>
                    <el-form-item
                      label="用药天数："
                      prop="dispensePerDuration"
                      class="required-field"
                      data-prop="dispensePerDuration"
                    >
                      <el-input-number
                        v-model="scope.row.dispensePerDuration"
                        style="width: 80px"
                        :min="0"
                        controls-position="right"
                        :controls="false"
                        :ref="(el) => (inputRefs.dispensePerDuration = el)"
                        @keyup.enter.prevent="
                          handleEnter('dispensePerDuration', scope.row, scope.$index)
                        "
                        @input="calculateTotalAmount(scope.row, scope.$index)"
                      >
                        <template #suffix>天</template>
                      </el-input-number>
                    </el-form-item>

                    <el-form-item label="总量：" prop="quantity">
                      <el-input-number
                        v-model="scope.row.quantity"
                        style="width: 70px"
                        controls-position="right"
                        :controls="false"
                      />
                    </el-form-item>
                    <el-select
                      v-model="scope.row.unitCode"
                      style="width: 70px; margin-right: 20px"
                      placeholder=" "
                      @change="calculateTotalAmount(scope.row, scope.$index)"
                    >
                      <template v-for="item in scope.row.unitCodeList" :key="item.value">
                        <el-option
                          v-if="item.type != unitMap['dose']"
                          :value="item.value"
                          :label="item.label"
                        />
                      </template>
                    </el-select>
                  </div>
                  <el-button type="primary" @click="handleSaveSign(scope.row, scope.$index)">
                    保存
                  </el-button>
                </div>
              </template>
              <template v-else-if="scope.row.adviceType == 2">
                <div style="display: flex; align-items: center; margin-bottom: 16px; gap: 16px">
                  <span style="font-size: 16px; font-weight: 600">
                    {{
                      scope.row.adviceName +
                      ' ' +
                      scope.row.volume +
                      ' ' +
                      scope.row.unitPrice +
                      ' 元/' +
                      scope.row.unitCode_dictText
                    }}
                  </span>
                  <div class="form-group">
                    <el-select
                      v-model="scope.row.lotNumber"
                      style="width: 180px; margin-right: 20px"
                      placeholder="药房"
                    >
                      <el-option
                        v-for="item in scope.row.stockList"
                        :key="item.lotNumber"
                        :value="item.lotNumber"
                        :label="
                          item.locationName +
                          '  ' +
                          '批次号: ' +
                          item.lotNumber +
                          '  ' +
                          '  库存：' +
                          item.quantity / scope.row.partPercent +
                          item.unitCode_dictText +
                          '  单价：' +
                          item.price.toFixed(2) +
                          '/' +
                          item.unitCode_dictText
                        "
                        @click="handleNumberClick(item, scope.$index)"
                      />
                    </el-select>
                    <el-form-item
                      label="数量："
                      prop="quantity"
                      class="required-field"
                      data-prop="quantity"
                    >
                      <el-input-number
                        placeholder="数量"
                        v-model="scope.row.quantity"
                        style="width: 70px"
                        controls-position="right"
                        :controls="false"
                        @keyup.enter.prevent="handleEnter('quantity', scope.row, scope.$index)"
                        @input="calculateTotalPrice(scope.row, scope.$index)"
                      />
                    </el-form-item>
                    <el-select
                      v-model="scope.row.unitCode"
                      style="width: 70px; margin-right: 20px"
                      placeholder=" "
                      @change="calculateTotalAmount(scope.row, scope.$index)"
                    >
                      <template v-for="item in scope.row.unitCodeList" :key="item.value">
                        <el-option
                          v-if="item.type != unitMap['dose']"
                          :value="item.value"
                          :label="item.label"
                        />
                      </template>
                    </el-select>
                    <span class="total-amount">
                      总金额：{{ scope.row.totalPrice ? scope.row.totalPrice + ' 元' : '0.00 元' }}
                    </span>
                  </div>
                  <el-button type="primary" @click="handleSaveSign(scope.row, scope.$index)">
                    保存
                  </el-button>
                </div>
              </template>
              <template v-else>
                <div style="display: flex; align-items: center; margin-bottom: 16px; gap: 16px">
                  <span style="font-size: 16px; font-weight: 600">
                    {{
                      scope.row.adviceName + ' ' + scope.row.unitPrice
                        ? Number(scope.row.unitPrice).toFixed(2)
                        : '-' + '元'
                    }}
                  </span>
                  <div class="form-group">
                    <el-form-item
                      label="执行次数："
                      prop="quantity"
                      class="required-field"
                      data-prop="quantity"
                    >
                      <el-input-number
                        placeholder="执行次数"
                        style="width: 100px; margin: 0 20px"
                        v-model="scope.row.quantity"
                        controls-position="right"
                        :controls="false"
                        @keyup.enter.prevent="handleEnter('quantity', scope.row, scope.$index)"
                        @input="calculateTotalPrice(scope.row, scope.$index)"
                      />
                    </el-form-item>
                    <el-tree-select
                      clearable
                      v-model="scope.row.orgId"
                      :data="organization"
                      :props="{ value: 'id', label: 'name', children: 'children' }"
                      value-key="id"
                      check-strictly
                      placeholder="请选择执行科室"
                    />
                    <span class="total-amount">
                      总金额：{{ scope.row.totalPrice ? scope.row.totalPrice + ' 元' : '0.00 元' }}
                    </span>
                    <span style="font-size: 16px; font-weight: 600">
                      <!-- 金额: {{ scope.row.priceList[0].price }} -->
                    </span>
                  </div>
                  <el-button type="primary" @click="handleSaveSign(scope.row, scope.$index)">
                    保存
                  </el-button>
                </div>
              </template>
            </div>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column label="" align="center" prop="groupId" width="60">
        <template #default="scope">
          <el-checkbox
            v-model="scope.row.check"
            placeholder=""
            @click.stop=""
            @change="
              (value) => {
                if (value) {
                  groupIndexList.push(scope.$index);
                } else {
                  groupIndexList.splice(groupIndexList.indexOf(scope.$index), 1);
                }
              }
            "
          />
        </template>
      </el-table-column>
      <el-table-column label="组" align="center" width="60">
        <template #default="scope">
          <div v-if="groupMarkers[scope.$index] === '┏'">┏</div>
          <div v-if="groupMarkers[scope.$index] === '┗'">┗</div>
          <div v-if="groupMarkers[scope.$index] === '┃'">┃</div>
        </template>
      </el-table-column>
      <el-table-column label="医嘱项目" align="center" prop="productName" width="400">
        <template #default="scope">
          <template v-if="getRowDisabled(scope.row)">
            <el-select
              style="width: 35%; margin-right: 20px"
              v-model="scope.row.adviceType"
              :ref="'adviceTypeRef' + scope.$index"
              @change="
                (value) => {
                  expandOrder = [];
                  combinationList[scope.$index].adviceName = undefined;
                  adviceQueryParams.adviceType = value;
                }
              "
            >
              <el-option
                v-for="item in adviceTypeList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                @click="
                  () => {
                    combinationList[scope.$index].adviceType = item.value;
                    combinationList[scope.$index].adviceType_dictText = item.label;
                  }
                "
              />
            </el-select>
            <el-popover
              :popper-style="{ padding: '0' }"
              placement="bottom-start"
              :visible="scope.row.showPopover"
              :width="1200"
            >
              <adviceBaseList
                ref="adviceTableRef"
                :popoverVisible="scope.row.showPopover"
                :adviceQueryParams="adviceQueryParams"
                :patientInfo="props.patientInfo"
                @selectAdviceBase="(row) => selectAdviceBase(scope.row.uniqueKey, row)"
              />
              <template #reference>
                <el-input
                  :ref="'adviceRef' + scope.$index"
                  style="width: 50%"
                  v-model="scope.row.adviceName"
                  placeholder="请选择项目"
                  @input="handleChange"
                  @click="handleFocus(scope.row, scope.$index)"
                  @keyup.enter.stop="handleFocus(scope.row, scope.$index)"
                  @keydown="
                    (e) => {
                      if (!scope.row.showPopover) return;
                      // 拦截上下键和回车事件
                      if (['ArrowUp', 'ArrowDown', 'Enter'].includes(e.key)) {
                        e.preventDefault();
                        // 传递事件到弹窗容器
                        adviceTableRef.handleKeyDown(e);
                      }
                    }
                  "
                  @blur="handleBlur(scope.row)"
                />
              </template>
            </el-popover>
          </template>
          <span v-else>{{ scope.row.adviceName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="" width="90">
        <template #default="scope">
          <el-tag v-if="scope.row.statusEnum == 2" type="success">已签发</el-tag>
          <el-tag v-else-if="scope.row.statusEnum == 1" type="">待签发</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="单次剂量" align="center" prop="">
        <template #default="scope">
          <span v-if="!scope.row.isEdit">
            {{
              scope.row.dose
                ? formatNumber(scope.row.dose) + ' ' + scope.row.doseUnitCode_dictText
                : ''
            }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="总量" align="center" prop="">
        <template #default="scope">
          <span v-if="!scope.row.isEdit">
            {{ scope.row.quantity ? scope.row.quantity + ' ' + scope.row.unitCode_dictText : '' }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="总金额" align="right" prop="" header-align="center">
        <template #default="scope">
          <span v-if="!scope.row.isEdit" style="text-align: right">
            {{ scope.row.totalPrice ? Number(scope.row.totalPrice).toFixed(2) + ' 元' : '-' }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="药房/科室" align="center" prop="" width="200">
        <template #default="scope">
          <span v-if="!scope.row.isEdit">
            {{ scope.row.positionName }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="频次/用法" align="center" prop="" width="180">
        <template #default="scope">
          <span v-if="!scope.row.isEdit && scope.row.adviceType == 1" style="text-align: right">
            {{
              scope.row.rateCode_dictText
                ? scope.row.rateCode_dictText +
                  ' ' +
                  scope.row.dispensePerDuration +
                  '天' +
                  ' ' +
                  scope.row.methodCode_dictText
                : ''
            }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="注射药品" align="center" prop="" width="80">
        <template #default="scope">
          <span v-if="!scope.row.isEdit">
            {{ scope.row.injectFlag_enumText || '-' }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="皮试" align="center" prop="" width="80">
        <template #default="scope">
          <span v-if="!scope.row.isEdit">
            {{ scope.row.skinTestFlag_enumText || '-' }}
          </span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
      
<script setup>
import { getOrgTree, saveOrderGroup } from './api';
import adviceBaseList from './adviceBaseList';
import { getCurrentInstance, nextTick, watch } from 'vue';
import { calculateQuantityByDays, formatNumber } from '@/utils/his';

const emit = defineEmits(['selectDiagnosis']);
const total = ref(0);
const queryParams = ref({});
const combinationList = ref([]);
const form = ref({
  combinationList: combinationList.value,
});
const adviceQueryParams = ref({});
const rowIndex = ref(-1);
const groupIndex = ref(1);
const groupIndexList = ref([]);
const nextId = ref(1);
const unitCodeList = ref([]);
const adviceTableRef = ref([]);
const organization = ref([]);
const rowRules = ref({
  conditionDefinitionId: [{ required: true, message: '请选择诊断', trigger: 'change' }],
  dose: [{ required: true, message: '请输入单次剂量', trigger: 'change' }],
  doseQuantity: [{ required: true, message: '请输入单次剂量', trigger: 'change' }],
  quantity: [{ required: true, message: '请输入数量', trigger: 'change' }],
  dispensePerDuration: [{ required: true, message: '请输入用药天数', trigger: 'change' }],
});
const unitMap = ref({
  dose: 'dose',
  minUnit: 'minUnit',
  unit: 'unit',
});
const buttonDisabled = computed(() => {
  return !props.patientInfo;
});
const props = defineProps({
  patientInfo: {
    type: Object,
    required: true,
  },
  comtination: {
    type: Object,
  },
});
const isAdding = ref(false);
const prescriptionRef = ref();
const expandOrder = ref([]); //目前的展开行
const stockList = ref([]);
const { proxy } = getCurrentInstance();
const inputRefs = ref({}); // 存储输入框实例
const requiredProps = ref([]); // 存储必填项 prop 顺序
const { method_code, unit_code, rate_code, distribution_category_code } = proxy.useDict(
  'method_code',
  'unit_code',
  'rate_code',
  'distribution_category_code'
);

const adviceTypeList = ref([
  {
    label: '西药中成药',
    value: 1,
  },
  {
    label: '耗材',
    value: 2,
  },
  {
    label: '诊疗',
    value: 3,
  },
  {
    label: '全部',
    value: undefined,
  },
]);
onMounted(() => {
  document.addEventListener('keydown', escKeyListener);
});

onBeforeUnmount(() => {
  document.removeEventListener('keydown', escKeyListener);
});
watch(
  () => expandOrder.value,
  (newValue) => {
    if (newValue.length > 0) {
      nextTick(() => {
        const index = combinationList.value.findIndex((row) => row.uniqueKey === newValue[0]);
        const items = proxy.$refs['formRef' + index]?.$el?.querySelectorAll('[data-prop]');
        requiredProps.value = Array.from(items).map((item) => item.dataset.prop);
      });
    } else {
      requiredProps.value = {};
    }
  }
);

watch(
  () => props.comtination,
  (newValue) => {
    if (newValue.groupJson && JSON.parse(newValue.groupJson).length > 0) {
      combinationList.value = JSON.parse(newValue.groupJson).map((item) => {
        return {
          ...item,
          isEdit: false,
        };
      });
    } else {
      combinationList.value = [];
    }
  },
  { deep: true }
);

function getListInfo(addNewRow) {
  isAdding.value = false;
}

function getRowDisabled(row) {
  return row.isEdit;
}

// 新增医嘱
function handleAddPrescription() {
  if (isAdding.value) {
    proxy.$modal.msgWarning('请先保存当前医嘱');
    return;
  }
  isAdding.value = true;
  // 在数组最前方添加一行，让新增行显示在最上边
  combinationList.value.unshift({
    uniqueKey: Date.now().toString() + (nextId.value++).toString(),
    showPopover: false,
    check: false,
    isEdit: true,
    statusEnum: 1,
  });
  groupMarkers.value = getGroupMarkers(combinationList.value);
  nextTick(() => {
    proxy.$refs['adviceRef0'].focus();
  });
  console.log(combinationList.value, 'add');
}

/**
 * 点击行赋值
 */
function clickRow(row) {
  emit('selectDiagnosis', row);
}

// 行双击打开编辑块，仅待发送的可编辑
function clickRowDb(row) {
  if (row.statusEnum == 1) {
    row.isEdit = true;
    const index = combinationList.value.findIndex((item) => item.uniqueKey === row.uniqueKey);
    combinationList.value[index] = row;
    expandOrder.value = [row.uniqueKey];
  }
}

function handleFocus(row, index) {
  rowIndex.value = index;
  row.showPopover = true;
}

function handleBlur(row) {
  row.showPopover = false;
}

function handleChange(value) {
  adviceQueryParams.value.searchKey = value;
}

/**
 * 选择药品回调
 */
function selectAdviceBase(key, row) {
  getOrgList();
  unitCodeList.value = [];
  unitCodeList.value.push({ value: row.unitCode, label: row.unitCode_dictText, type: 'unit' });
  if (row.doseUnitCode != row.minUnitCode) {
    unitCodeList.value.push({
      value: row.doseUnitCode,
      label: row.doseUnitCode_dictText,
      type: 'dose',
    });
  }
  if (
    (row.partAttributeEnum == 1 || row.partAttributeEnum == 3) &&
    row.minUnitCode != row.unitCode
  ) {
    unitCodeList.value.push({
      value: row.minUnitCode,
      label: row.minUnitCode_dictText,
      type: 'minUnit',
    });
  }
  if (row.adviceType == 2 && row.minUnitCode != row.unitCode) {
    unitCodeList.value.push({
      value: row.minUnitCode,
      label: row.minUnitCode_dictText,
      type: 'minUnit',
    });
  }
  combinationList.value[rowIndex.value] = {
    ...combinationList.value[rowIndex.value],
    ...JSON.parse(JSON.stringify(row)),
  };
  combinationList.value[rowIndex.value].orgId = undefined;
  combinationList.value[rowIndex.value].dose = undefined;
  combinationList.value[rowIndex.value].unitCodeList = unitCodeList.value;
  combinationList.value[rowIndex.value].doseUnitCode =
    row.minUnitCode != row.unitCode ? row.minUnitCode : row.unitCode;
  combinationList.value[rowIndex.value].minUnitCode = JSON.parse(JSON.stringify(row.doseUnitCode));
  combinationList.value[rowIndex.value].unitCode =
    row.partAttributeEnum == 1 ? row.minUnitCode : row.unitCode;
  // combinationList.value[rowIndex.value].doseUnitCode_dictText = row.minUnitCode_dictText;
  combinationList.value[rowIndex.value].definitionId = JSON.parse(
    JSON.stringify(row)
  ).chargeItemDefinitionId;

  // 库存列表 + 价格列表拼成批次号的下拉框
  if (row.adviceType != 3) {
    if (row.inventoryList && row.inventoryList.length == 0) {
      expandOrder.value = [];
      proxy.$modal.msgWarning('该项目无库存');
      return;
    }
    stockList.value = row.inventoryList.map((item, index) => {
      return { ...item, ...row.priceList[index] };
    });
    combinationList.value[rowIndex.value].stockList = stockList.value;
    // 获取默认批次号的库存，如果没有让医生重新选
    let stock = stockList.value.filter((item) => {
      return item.lotNumber == row.defaultLotNumber;
    })[0];
    if (stock != {} && stock != undefined) {
      if (stock.quantity <= 0) {
        proxy.$modal.msgWarning('该项目库存不足，请选择其它库房');
        // return;
      }
      combinationList.value[rowIndex.value].lotNumber = stock.lotNumber;
      combinationList.value[rowIndex.value].inventoryId = stock.inventoryId;
      combinationList.value[rowIndex.value].locationId = stock.locationId;
      combinationList.value[rowIndex.value].unitPrice = stock.price;
      combinationList.value[rowIndex.value].positionName = stock.locationName;
    }
  } else {
    combinationList.value[rowIndex.value].orgId = JSON.parse(JSON.stringify(row)).positionId;
    combinationList.value[rowIndex.value].unitPrice = row.priceList[0].price;
  }
  expandOrder.value = [key];
  nextTick(() => {
    if (row.adviceType == 1) {
      if (row.injectFlag == 1) {
        inputRefs.value['executeNum']?.focus();
      } else {
        inputRefs.value['dose']?.focus();
      }
    } else {
      inputRefs.value['quantity']?.focus();
    }
  });
}

function getOrgList() {
  getOrgTree().then((res) => {
    organization.value = res.data.records;
  });
}

function handleDelete() {
  let deleteList = combinationList.value
    .filter((item) => {
      return item.check && item.statusEnum == 1;
    })
    .map((item) => {
      return {
        requestId: item.requestId,
        dbOpType: '3',
        adviceType: item.adviceType,
      };
    });
  if (deleteList.length == 0) {
    proxy.$modal.msgWarning('请选择要删除的处方');
    return;
  }
  if (!deleteList[0].requestId) {
    combinationList.value.shift();
  } else {
  }
  // groupIndexList.value
  //   .sort((a, b) => b - a)
  //   .forEach((item) => {
  //     combinationList.value.splice(item, 1);
  //   });
  // groupIndexList.value = [];
  expandOrder.value = [];
  isAdding.value = false;
  adviceQueryParams.value.adviceType = undefined;
  // combinationList.value.splice(index, 1);
}

function handleNumberClick(item, index) {
  combinationList.value[index].unitPrice = item.price;
  // combinationList.value[index].lotNumber = item.lotNumber;
  combinationList.value[index].locationId = item.locationId;
  combinationList.value[index].positionId = item.locationId;
  combinationList.value[index].positionName = item.locationName;
}

/**
 * 保存处方
 */
function handleSave() {
  if (expandOrder.value.length > 0) {
    proxy.$modal.msgWarning('请先保存当前医嘱');
    return;
  }
  let saveList = combinationList.value.filter((item) => {
    return item.statusEnum == 1;
  });
  if (saveList.length == 0) {
    proxy.$modal.msgWarning('当前无可签发处方');
    return;
  }
  if (!validateGroups(saveList)) {
    return;
  }
  // 此处签发处方和单行保存处方传参相同，后台已经将传参存为JSON字符串，此处直接转换为JSON即可
  let list = saveList.map((item) => {
    return {
      ...JSON.parse(item.contentJson),
      requestId: item.requestId,
      dbOpType: '1',
      groupId: item.groupId,
    };
  });
}

// 单行处方保存
function handleSaveSign(row, index) {
  proxy.$refs['formRef' + index].validate((valid) => {
    if (valid) {
      row.isEdit = false;
      isAdding.value = false;
      expandOrder.value = [];
      row.patientId = props.patientInfo.patientId;
      row.encounterId = props.patientInfo.encounterId;
      row.accountId = props.patientInfo.accountId;
      row.contentJson = JSON.stringify(row);
      row.dbOpType = row.requestId ? '2' : '1';
      row.minUnitQuantity = row.quantity * row.partPercent;
      savePrescription({ adviceSaveList: [row] }).then((res) => {
        if (res.code === 200) {
          proxy.$modal.msgSuccess('保存成功');
          // getListInfo(true);
          nextId.value == 1;
        }
      });
    }
  });
}

function saveCombination() {
  console.log(props,"参数")
  let params = JSON.parse(JSON.stringify(props.comtination));
  console.log(params);

  params.groupJson = JSON.stringify(combinationList.value);
  saveOrderGroup(params).then((res) => {
    if (res.code === 200) {
      proxy.$modal.msgSuccess('保存成功');
    }
  });
}

// 处理回车事件
const handleEnter = (currentProp, row, rowIndex, e) => {
  proxy.$refs['formRef' + rowIndex].validateField(currentProp, (valid) => {
    if (valid) {
      const index = requiredProps.value.indexOf(currentProp);
      if (index === -1) return;
      // 跳转下一个或提交
      const nextIndex = index + 1;
      if (nextIndex < requiredProps.value.length) {
        const nextProp = requiredProps.value[nextIndex];
        inputRefs.value[nextProp]?.focus(); // 直接调用 Element 的 focus 方法
      } else {
        handleSaveSign(row, rowIndex);
      }
    }
  });
};

function escKeyListener(e) {
  if (e.key === 'Escape') {
    if (isAdding.value == true) {
      let index;
      if (expandOrder.value.length > 0) {
        index = combinationList.value.findIndex((item) => item.uniqueKey == expandOrder.value[0]);
      }
      if (index == 0) {
        expandOrder.value = [];
      }
      combinationList.value.shift();
      isAdding.value = false;
    }
  }
}

// 组合
function combination() {
  if (groupIndexList.value.length < 1) {
    // proxy.$modal.msgWarning('至少选择两项');
    return;
  }
  let uniqueValues = new Set();
  groupIndexList.value.forEach((index) => {
    uniqueValues.add(combinationList.value[index].methodCode);
  });
  if (uniqueValues.size != 1) {
    proxy.$modal.msgWarning('同一分组药品用法必须相同');
    return;
  }
  let timeStamp = new Date().now().toString();
  groupIndexList.value.forEach((index) => {
    combinationList.value[index].groupId = JSON.parse(JSON.stringify(timeStamp + groupIndex.value));
    combinationList.value[index].check = false;
  });
  sortPrescriptionList();
  groupMarkers.value = getGroupMarkers(combinationList.value); // 更新标记
  groupIndex.value++;
  groupIndexList.value = [];
}

// 拆组
function split() {
  groupIndexList.value.forEach((index) => {
    combinationList.value[index].groupId = undefined;
    combinationList.value[index].check = false;
  });
  sortPrescriptionList();
  groupMarkers.value = getGroupMarkers(combinationList.value); // 更新标记
  groupIndexList.value = [];
}

// 分组标记处理
function getGroupMarkers(combinationList) {
  const groupMap = {};
  const markers = [];

  // 遍历处方列表，记录每组的索引范围（忽略无 groupId 的项）
  combinationList.forEach((item, index) => {
    if (!item.groupId) {
      markers[index] = null; // 没有组号的标记为 null
      return;
    }

    if (!groupMap[item.groupId]) {
      groupMap[item.groupId] = [];
    }
    groupMap[item.groupId].push(index);
  });

  // 根据每组的索引范围设置标记
  Object.values(groupMap).forEach((indices) => {
    if (indices.length === 1) {
      // 单个组成员，显示上下括号
      markers[indices[0]] = 'all';
    } else {
      indices.forEach((index, i) => {
        if (i === 0) {
          markers[index] = '┏';
        } else if (i === indices.length - 1) {
          markers[index] = '┗';
        } else {
          markers[index] = '┃';
        }
      });
    }
  });
  return markers;
}
const groupMarkers = ref([]);

// 计算总量
function calculateTotalPrice(row, index) {
  nextTick(() => {
    row.totalPrice = (row.unitPrice * row.quantity * 100) / 100;
  });
}

// 单位切换时 自动计算对应单位的总量
function convertValues(row, index) {
  nextTick(() => {
    let code = unitCodeList.value.filter((item) => {
      return item.value == row.minUnitCode;
    })[0];
    switch (code.type) {
      case 'dose':
        row.doseQuantity = row.dose * row.unitConversionRatio;
        break;
      case 'minUnit':
        row.doseQuantity = row.dose;
        break;
      case 'unit':
        row.doseQuantity = row.dose / row.partPercent;
        break;
    }
  });
  calculateTotalAmount(row, index);
}

// 单次剂量数量改变时自动计算总量
function convertDoseValues(row, index) {
  nextTick(() => {
    let code = unitCodeList.value.filter((item) => {
      return item.value == row.minUnitCode;
    })[0];
    switch (code.type) {
      case 'dose':
        row.dose = row.doseQuantity / row.unitConversionRatio;
        break;
      case 'minUnit':
        row.dose = row.doseQuantity;
        break;
      case 'unit':
        row.dose = row.doseQuantity * row.partPercent;
        break;
    }
  });
  calculateTotalAmount(row, index);
}

// 总量计算,仅适用只有两种单位的情况
function calculateTotalAmount(row, index) {
  nextTick(() => {
    if (row.rateCode && row.dispensePerDuration) {
      // 根据用药天数和用药频次计算数量
      let count = calculateQuantityByDays(row.rateCode, row.dispensePerDuration);
      let quantity;
      if (row.unitCode == row.doseUnitCode) {
        quantity = calculateQuantityBySplitType(row.partAttributeEnum, row.dose, count);
        combinationList.value[index].quantity = quantity;
        combinationList.value[index].totalPrice = (
          (quantity * row.unitPrice) /
          row.partPercent
        ).toFixed(2);
      } else {
        quantity = calculateQuantity(row.partAttributeEnum, row.dose, count, row.partPercent);
        combinationList.value[index].quantity = quantity;
        combinationList.value[index].totalPrice = (quantity * row.unitPrice).toFixed(2);
      }
    }
  });
  // proxy.$refs['formRef' + index].validate();
}

/**
 * 根据门诊拆分类型计算总药量
 *
 * @param type 门诊拆分类型
 * @param dose 单次剂量 最小单位
 * @param count 用药频次和用药天数计算出的总数
 */
function calculateQuantityBySplitType(type, dose, count) {
  switch (type) {
    case 1: // 门诊按最小单位每次量向上取整
      return Math.ceil(dose) * count;
    case 2: // 门诊按包装单位不可拆分
      return Math.ceil(dose * count);
    case 3: // 门诊按最小单位总量向上取整
      return Math.ceil(dose * count);
    case 4: // 门诊按包装单位每次量向上取整
      return Math.ceil(dose) * count;
  }
}

/**
 * 根据门诊拆分类型计算总药量
 *
 * @param type 门诊拆分类型
 * @param dose 单次剂量 最小单位
 * @param count 用药频次和用药天数计算出的总数
 */
function calculateQuantity(type, dose, count, partPercent) {
  switch (type) {
    case 1: // 门诊按最小单位每次量向上取整
      return Math.ceil(dose / partPercent) * count;
    case 2: // 门诊按包装单位不可拆分
      return Math.ceil(dose * count);
    case 3: // 门诊按最小单位总量向上取整
      return Math.ceil((dose / partPercent) * count);
    case 4: // 门诊按包装单位每次量向上取整
      return Math.ceil(dose) * count;
  }
}

// 处方列表按照组号排序，后端已经将未签发的显示在上边，此处只处理未签发的处方
function sortPrescriptionList() {
  combinationList.value.sort((a, b) => {
    if (a.statusEnum == 1 && b.statusEnum == 1) {
      // 如果 a 和 b 都有 groupId，则按 groupId 升序排列
      if (a.groupId !== undefined && b.groupId !== undefined) {
        return a.groupId - b.groupId;
      }
      // 如果 a 有 groupId 而 b 没有，则 a 排在前面
      else if (a.groupId !== undefined && b.groupId === undefined) {
        return -1;
      }
      // 如果 a 没有 groupId 而 b 有，则 b 排在前面
      else if (a.groupId === undefined && b.groupId !== undefined) {
        return 1;
      }
      // 如果 a 和 b 都没有 groupId，则保持原顺序
      else {
        return 0;
      }
    }
  });
}

// 校验每个组号数量是否大于5和对应分组金额是否大于500
function validateGroups(saveList) {
  // 获取到全部组号和对应的金额
  const groups = saveList.map((item) => {
    return {
      groupId: item.groupId,
      totalPrice: item.totalPrice,
    };
  });
  // 计算每个组号数量,以及每组金额
  const counts = {};
  groups.forEach((item) => {
    const groupId = item.groupId;
    const totalPrice = Number(item.totalPrice);
    if (!counts[groupId]) {
      counts[groupId] = {
        count: 0,
        totalPrice: 0,
      };
    }
    counts[groupId].groupId = groupId;
    counts[groupId].count++;
    counts[groupId].totalPrice += totalPrice;
  });

  // 获取组数大于5的组号
  const countStr = Object.values(counts)
    .filter((group) => {
      return group.count > 5;
    })
    .map((group) => group.groupId);
  // 获取总金额大于500的组号
  const totalStr = Object.values(counts)
    .filter((group) => {
      return group.totalPrice > 500;
    })
    .map((group) => group.groupId);

  if (countStr.length > 0) {
    proxy.$modal.msgWarning('分组"' + countStr + '"数量超出限制');
    return false;
  } else if (totalStr.length > 0) {
    proxy.$modal.msgWarning('分组"' + totalStr + '"金额总和超出限制');
    return false;
  }
  return true;
}
defineExpose({ getListInfo });
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