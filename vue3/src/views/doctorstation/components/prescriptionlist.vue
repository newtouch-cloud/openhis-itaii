<template>
  <div style="width: 100%">
    <div style="margin-bottom: 5px">
      <el-button type="primary" @click="handleAddPrescription()" :disabled="false">
        新增
      </el-button>
      <el-button type="primary" @click="handleSaveBatch()" :disabled="false"> 保存 </el-button>
      <el-button type="primary" @click="handleSave()" :disabled="false"> 签发 </el-button>
      <el-button type="warning" plain @click="handleSingOut()" :disabled="false"> 撤回 </el-button>
      <el-button
        type="primary"
        plain
        @click="proxy.$refs.orderFroupRef.handleOpen()"
        :disabled="false"
      >
        组套
      </el-button>
      <el-button
        type="primary"
        plain
        :disabled="false"
        @click="proxy.$refs.prescriptionHistoryRef.handleOpen()"
      >
        历史
      </el-button>
      <el-button type="default" @click="combination()" :disabled="false"> 组合 </el-button>
      <el-button type="default" @click="split()" :disabled="false"> 拆组 </el-button>
      <el-button type="danger" plain @click="handleDelete()" :disabled="false"> 删除 </el-button>
      <span style="color: #606266; font-size: 14px; font-weight: 700; margin-left: 15px">
        诊断：
      </span>
      <el-select v-model="conditionDefinitionId" placeholder="诊断" style="width: 180px">
        <el-option
          v-for="item in diagnosisList"
          :key="item.conditionId"
          :label="item.name"
          :value="item.definitionId"
          @click="handleDiagnosisChange(item)"
        />
      </el-select>
      <span style="color: #606266; font-size: 14px; font-weight: 700; margin-left: 15px">
        费用性质：
      </span>
      <el-select v-model="accountId" placeholder="费用性质" style="width: 180px">
        <el-option
          v-for="item in contractList"
          :key="item.accountId"
          :label="item.contractName"
          :value="item.accountId"
        />
      </el-select>
      <span style="color: #606266; font-size: 14px; font-weight: 700; margin-left: 15px">
        合计金额：{{ totalAmount ? totalAmount.toFixed(2) : 0 }}元
      </span>
    </div>
    <el-table
      max-height="650"
      ref="prescriptionRef"
      :data="prescriptionList"
      row-key="uniqueKey"
      border
      @cell-click="clickRow"
      @row-dblclick="clickRowDb"
      v-loading="loading"
      :expand-row-keys="expandOrder"
    >
      <el-table-column type="expand" width="1" style="width: 0">
        <template #default="scope">
          <el-form :model="scope.row" :rules="rowRules" :ref="'formRef' + scope.$index">
            <div class="expend_div" style="padding: 16px; background: #f8f9fa; border-radius: 8px">
              <template v-if="scope.row.adviceType == 1">
                <div style="display: flex; align-items: center; margin-bottom: 16px; gap: 16px">
                  <span class="medicine-title">
                    {{
                      scope.row.adviceName +
                      ' ' +
                      scope.row.volume +
                      ' [' +
                      Number(scope.row.unitPrice).toFixed(2) +
                      ' 元' +
                      '/' +
                      scope.row.unitCode_dictText +
                      ']'
                    }}
                  </span>

                  <!-- <el-form-item prop="conditionDefinitionId">
                    <el-select
                      v-model="scope.row.conditionDefinitionId"
                      style="width: 180px; margin: 0 20px"
                      placeholder="诊断"
                    >
                      <el-option
                        v-for="item in diagnosisList"
                        :key="item.conditionId"
                        :label="item.name"
                        :value="item.definitionId"
                        @click="handleDiagnosisChange(item, scope.row)"
                      />
                    </el-select>
                  </el-form-item> -->
                  <el-form-item prop="lotNumber" label="药房：">
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
                      :min="1"
                      v-model="scope.row.executeNum"
                      controls-position="right"
                      :controls="false"
                      :ref="(el) => (inputRefs.executeNum = el)"
                      @keyup.enter.prevent="handleEnter('executeNum', scope.row, scope.$index)"
                      style="width: 70px; margin-right: 20px"
                    />
                  </el-form-item>
                  <span class="medicine-info"> 诊断：{{ diagnosisName }} </span>
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
                      label="单次用量："
                      prop="dose"
                      class="required-field"
                      data-prop="doseQuantity"
                    >
                      <el-input-number
                        :min="0"
                        v-model="scope.row.doseQuantity"
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
                      v-model="scope.row.minUnitCode"
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
                    <el-form-item prop="dose">
                      <el-input-number
                        v-model="scope.row.dose"
                        controls-position="right"
                        :controls="false"
                        style="width: 70px; margin: 0 20px"
                        @input="convertDoseValues(scope.row, scope.$index)"
                      />
                    </el-form-item>
                    <!-- 全部单位 -->
                    <el-select
                      v-model="scope.row.doseUnitCode"
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
                        :min="1"
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

                    <el-form-item
                      label="总量："
                      prop="quantity"
                      class="required-field"
                      data-prop="quantity"
                    >
                      <el-input-number
                        v-model="scope.row.quantity"
                        style="width: 70px"
                        controls-position="right"
                        :controls="false"
                        :ref="(el) => (inputRefs.quantity = el)"
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
                  </div>
                  <el-button type="primary" @click="handleSaveSign(scope.row, scope.$index)">
                    确定
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
                        @input="calculateTotalAmount(scope.row, scope.$index)"
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
                          @click="
                            () => {
                              scope.row.unitCode_dictText = item.label;
                            }
                          "
                        />
                      </template>
                    </el-select>
                    <span class="total-amount">
                      总金额：{{ scope.row.totalPrice ? scope.row.totalPrice + ' 元' : '0.00 元' }}
                    </span>
                  </div>
                  <el-button type="primary" @click="handleSaveSign(scope.row, scope.$index)">
                    确定
                  </el-button>
                </div>
              </template>
              <template v-else>
                <div style="display: flex; align-items: center; margin-bottom: 16px; gap: 16px">
                  <span style="font-size: 16px; font-weight: 600">
                    {{ scope.row.adviceName }}
                    {{
                      scope.row.unitPrice
                        ? Number(scope.row.unitPrice).toFixed(2) + '/次'
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
                    <el-form-item
                      label="执行科室："
                      prop="orgId"
                      class="required-field"
                      data-prop="orgId"
                    >
                      <el-tree-select
                        clearable
                        v-model="scope.row.orgId"
                        style="width: 200px"
                        :data="organization"
                        :props="{ value: 'id', label: 'name', children: 'children' }"
                        value-key="id"
                        check-strictly
                        default-expand-all
                        placeholder="请选择执行科室"
                      />
                    </el-form-item>
                    <span class="total-amount">
                      总金额：{{ scope.row.totalPrice ? scope.row.totalPrice + ' 元' : '0.00 元' }}
                    </span>
                    <span style="font-size: 16px; font-weight: 600">
                      <!-- 金额: {{ scope.row.priceList[0].price }} -->
                    </span>
                  </div>
                  <el-button type="primary" @click="handleSaveSign(scope.row, scope.$index)">
                    确定
                  </el-button>
                </div>
              </template>
            </div>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column label="" align="center" prop="groupId" width="60">
        <template #header>
          <el-checkbox
            v-model="checkAll"
            @change="
              (value) => {
                prescriptionList.forEach((item, index) => {
                  groupIndexList.push(index);
                  item.check = value;
                });
              }
            "
          />
        </template>
        <template #default="scope">
          <el-checkbox
            v-model="scope.row.check"
            @dblclick.stop=""
            placeholder=""
            @change="(value) => handleCheckBoxChange(value, scope.$index, scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="组" align="center" width="60">
        <template #default="scope">
          <div v-if="groupMarkers[scope.$index] === '┏'">┏</div>
          <div v-if="groupMarkers[scope.$index] === '┗'">┗</div>
          <div v-if="groupMarkers[scope.$index] === '┃'">┃</div>
          <!-- <div v-if="groupMarkers[scope.$index] === 'all'">┏</div> -->
          <!-- <div v-if="groupMarkers[scope.$index] === 'all'">┗</div> -->
        </template>
      </el-table-column>
      <el-table-column label="医嘱" align="center" prop="productName" width="400">
        <template #default="scope">
          <template v-if="getRowDisabled(scope.row)">
            <el-select
              style="width: 35%; margin-right: 20px"
              v-model="scope.row.adviceType"
              :ref="'adviceTypeRef' + scope.$index"
              @change="
                (value) => {
                  expandOrder = [];
                  prescriptionList[scope.$index].adviceName = undefined;
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
                    prescriptionList[scope.$index].adviceType = item.value;
                    prescriptionList[scope.$index].adviceType_dictText = item.label;
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
          <el-tag v-else-if="!scope.row.requestId && scope.row.statusEnum == 1" type="warning"
            >待保存</el-tag
          >
          <el-tag v-else-if="scope.row.statusEnum == 1" type="primary">待签发</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="单次剂量" align="center" prop="">
        <template #default="scope">
          <span v-if="!scope.row.isEdit">
            {{
              scope.row.dose
                ? formatNumber(scope.row.doseQuantity) + ' ' + scope.row.doseUnitCode_dictText
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
      <el-table-column label="药房/科室" align="center" prop="" width="240">
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
      <el-table-column label="诊断" align="center" prop="diagnosisName" width="150">
        <template #default="scope">
          <span v-if="!scope.row.isEdit">
            {{ scope.row.diagnosisName || scope.row.conditionDefinitionName }}
          </span>
        </template>
      </el-table-column>
    </el-table>
    <OrderGroupDrawer
      ref="orderFroupRef"
      :diagnosis="diagnosisInfo"
      @useOrderGroup="handleSaveGroup"
    />
    <PrescriptionHistory
      ref="prescriptionHistoryRef"
      :diagnosis="diagnosisInfo"
      :patientInfo="props.patientInfo"
      @userPrescriptionHistory="handleSaveHistory"
    />
  </div>
</template>
      
<script setup>
import {
  getDiagnosisDefinitionList,
  savePrescription,
  getEncounterDiagnosis,
  getPrescriptionList,
  getOrgTree,
  savePrescriptionSign,
  singOut,
  updateGroupId,
  getContract,
  getAdviceBaseInfo,
} from './api';
import adviceBaseList from './adviceBaseList';
import { computed, getCurrentInstance, nextTick, watch } from 'vue';
import { calculateQuantityByDays, formatNumber } from '@/utils/his';
import OrderGroupDrawer from './orderGroupDrawer';
import PrescriptionHistory from './prescriptionHistory';
import Decimal from 'decimal.js';

const emit = defineEmits(['selectDiagnosis']);
const total = ref(0);
const queryParams = ref({});
const prescriptionList = ref([]);
const form = ref({
  prescriptionList: prescriptionList.value,
});
const adviceQueryParams = ref({});
const rowIndex = ref(-1);
const groupIndex = ref(1);
const groupIndexList = ref([]);
const diagnosisList = ref([]);
const nextId = ref(1);
const unitCodeList = ref([]);
const adviceTableRef = ref([]);
const organization = ref([]);
const conditionDefinitionId = ref('');
const encounterDiagnosisId = ref('');
const diagnosisName = ref('');
const diagnosisInfo = ref({});
const loading = ref(false);
const rowRules = ref({
  conditionDefinitionId: [{ required: true, message: '请选择诊断', trigger: 'change' }],
  dose: [{ required: true, message: '请输入单次剂量', trigger: 'change' }],
  doseQuantity: [{ required: true, message: '请输入单次剂量', trigger: 'change' }],
  quantity: [{ required: true, message: '请输入数量', trigger: 'change' }],
  dispensePerDuration: [{ required: true, message: '请输入用药天数', trigger: 'change' }],
  executeNum: [{ required: true, message: '请输入执行次数', trigger: 'change' }],
  rateCode: [{ required: true, message: '请选择频次', trigger: 'change' }],
  methodCode: [{ required: true, message: '请选择给药途径', trigger: 'change' }],
  orgId: [{ required: true, message: '请选择执行科室', trigger: 'change' }],
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
  activeTab: {
    type: String,
  },
});
const isAdding = ref(false);
const prescriptionRef = ref();
const expandOrder = ref([]); //目前的展开行
const stockList = ref([]);
const contractList = ref([]);
const conditionId = ref('');
const accountId = ref('');
const checkAll = ref(false);
const { proxy } = getCurrentInstance();
const inputRefs = ref({}); // 存储输入框实例
const requiredProps = ref([]); // 存储必填项 prop 顺序
const totalAmount = ref(0);
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
    value: '',
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
        const index = prescriptionList.value.findIndex((row) => row.uniqueKey === newValue[0]);
        const items = proxy.$refs['formRef' + index]?.$el?.querySelectorAll('[data-prop]');
        requiredProps.value = Array.from(items).map((item) => item.dataset.prop);
      });
    } else {
      requiredProps.value = {};
    }
  }
);
watch(
  () => prescriptionList.value,
  (newVlaue) => {
    if (newVlaue && newVlaue.length > 0) {
      handleTotalAmount();
    }
  },
  { immediate: true }
);
function handleTotalAmount() {
  totalAmount.value = prescriptionList.value.reduce((accumulator, currentRow) => {
    // if (currentRow.statusEnum != 2) {
    return accumulator + (Number(currentRow.totalPrice) || 0);
    // } else {
    // return accumulator || 0;
    // }
  }, 0);
}
getList();
function getList() {
  getDiagnosisDefinitionList(queryParams.value).then((res) => {
    // prescriptionList.value = res.data.records;
    total.value = res.data.total;
  });
}

function getListInfo(addNewRow) {
  isAdding.value = false;
  getPrescriptionList(props.patientInfo.encounterId).then((res) => {
    prescriptionList.value = res.data.map((item) => {
      return {
        ...JSON.parse(item.contentJson),
        ...item,
        doseQuantity: JSON.parse(item.contentJson)?.doseQuantity,
        doseUnitCode_dictText: JSON.parse(item.contentJson)?.doseUnitCode_dictText,
      };
    });
    groupMarkers.value = getGroupMarkers(prescriptionList.value); // 更新标记
    if (props.activeTab == 'prescription' && addNewRow) {
      handleAddPrescription();
    }
  });
  getContract({ encounterId: props.patientInfo.encounterId }).then((res) => {
    contractList.value = res.data;
  });
  accountId.value = props.patientInfo.accountId;
}

function getDiagnosisInfo() {
  getEncounterDiagnosis(props.patientInfo.encounterId).then((res) => {
    diagnosisList.value = res.data;
    let diagnosisInfo = diagnosisList.value.filter((item) => {
      return item.maindiseFlag == 1;
    });
    diagnosisInfo.value = diagnosisInfo[0];
    conditionDefinitionId.value = diagnosisInfo[0].definitionId;
    conditionId.value = diagnosisInfo[0].conditionId;
    encounterDiagnosisId.value = diagnosisInfo[0].encounterDiagnosisId;
    diagnosisName.value = diagnosisInfo[0].name;
  });
}

function getRowDisabled(row) {
  return row.isEdit;
}

// 新增医嘱
function handleAddPrescription() {
  if (diagnosisList.value.length == 0) {
    proxy.$modal.msgWarning('请先保存诊断后再开立医嘱');
    return;
  }
  if (isAdding.value) {
    proxy.$modal.msgWarning('请先保存当前医嘱');
    return;
  }
  isAdding.value = true;
  // 在数组最前方添加一行，让新增行显示在最上边
  prescriptionList.value.unshift({
    uniqueKey: nextId.value++,
    showPopover: false,
    check: false,
    isEdit: true,
    statusEnum: 1,
  });
  groupMarkers.value = getGroupMarkers(prescriptionList.value);
  nextTick(() => {
    proxy.$refs['adviceRef0'].focus();
  });
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
    row = { ...row, ...JSON.parse(row.contentJson), uniqueKey: row.uniqueKey };
    row.isEdit = true;
    row.doseUnitCode == JSON.parse(JSON.stringify(row.minUnitCode));
    const index = prescriptionList.value.findIndex((item) => item.uniqueKey === row.uniqueKey);
    prescriptionList.value[index] = row;
    expandOrder.value = [row.uniqueKey];
  }
}

function handleDiagnosisChange(item) {
  diagnosisName.value = item.name;
  conditionId.value = item.conditionId;
  encounterDiagnosisId.value = item.encounterDiagnosisId;
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
  prescriptionList.value[rowIndex.value] = {
    ...prescriptionList.value[rowIndex.value],
    ...JSON.parse(JSON.stringify(row)),
  };
  prescriptionList.value[rowIndex.value].orgId = undefined;
  prescriptionList.value[rowIndex.value].dose = undefined;
  prescriptionList.value[rowIndex.value].unitCodeList = unitCodeList.value;
  // prescriptionList.value[rowIndex.value].doseUnitCode =
  //   row.minUnitCode != row.unitCode ? row.minUnitCode : row.unitCode;
  // prescriptionList.value[rowIndex.value].minUnitCode = JSON.parse(JSON.stringify(row.doseUnitCode));
  prescriptionList.value[rowIndex.value].doseUnitCode = row.doseUnitCode;
  prescriptionList.value[rowIndex.value].minUnitCode = row.minUnitCode;
  prescriptionList.value[rowIndex.value].unitCode =
    row.partAttributeEnum == 1 ? row.minUnitCode : row.unitCode;
  // prescriptionList.value[rowIndex.value].doseUnitCode_dictText = row.minUnitCode_dictText;
  prescriptionList.value[rowIndex.value].definitionId = JSON.parse(
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
    prescriptionList.value[rowIndex.value].stockList = stockList.value;
    // 获取默认批次号的库存，如果没有让医生重新选
    let stock = stockList.value.filter((item) => {
      return item.lotNumber == row.defaultLotNumber;
    })[0];
    if (stock != {} && stock != undefined) {
      if (stock.quantity <= 0) {
        proxy.$modal.msgWarning('该项目库存不足，请选择其它库房');
        // return;
      }
      prescriptionList.value[rowIndex.value].lotNumber = stock.lotNumber;
      prescriptionList.value[rowIndex.value].inventoryId = stock.inventoryId;
      prescriptionList.value[rowIndex.value].locationId = stock.locationId;
      prescriptionList.value[rowIndex.value].unitPrice = stock.price;
      prescriptionList.value[rowIndex.value].positionName = stock.locationName;
    }
  } else {
    prescriptionList.value[rowIndex.value].orgId = JSON.parse(JSON.stringify(row)).positionId;
    prescriptionList.value[rowIndex.value].unitPrice = row.priceList[0].price;
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
  let selectRow = prescriptionList.value.filter((item) => {
    return item.check;
  });
  if (selectRow.length == 0) {
    proxy.$modal.msgWarning('请选择要删除的医嘱');
    return;
  }
  let deleteList = [];
  for (let i = prescriptionList.value.length - 1; i >= 0; i--) {
    let deleteItem = prescriptionList.value[i];
    // 通过requestId判断是否已保存，如果选中项未保存 直接从数组中移除，如果已保存，调接口删除
    if (deleteItem.check && deleteItem.statusEnum == 1 && !deleteItem.requestId) {
      prescriptionList.value.splice(i, 1);
    } else if (deleteItem.check && deleteItem.statusEnum == 1 && deleteItem.requestId) {
      deleteList.push({
        requestId: deleteItem.requestId,
        dbOpType: '3',
        adviceType: deleteItem.adviceType,
      });
    }
  }

  if (deleteList.length > 0) {
    savePrescription({ adviceSaveList: deleteList }).then((res) => {
      if (res.code == 200) {
        proxy.$modal.msgSuccess('操作成功');
        getListInfo(false);
      }
    });
  } else {
    proxy.$modal.msgWarning('所选医嘱不可删除，请先撤回后再删除');
    return;
  }
  expandOrder.value = [];
  isAdding.value = false;
  adviceQueryParams.value.adviceType = undefined;
  groupMarkers.value = getGroupMarkers(prescriptionList.value); // 删除行会出现组号混乱的情况，所以这里重新更新标记
}

function handleNumberClick(item, index) {
  prescriptionList.value[index].unitPrice = item.price;
  // prescriptionList.value[index].lotNumber = item.lotNumber;
  prescriptionList.value[index].locationId = item.locationId;
  prescriptionList.value[index].positionId = item.locationId;
  prescriptionList.value[index].positionName = item.locationName;
}

/**
 * 签发处方
 */
function handleSave() {
  if (expandOrder.value.length > 0) {
    proxy.$modal.msgWarning('请先保存当前医嘱');
    return;
  }
  if (prescriptionList.value[0].isEdit && !prescriptionList.value[0].adviceType) {
    prescriptionList.value.shift();
    isAdding.value = false;
    expandOrder.value = [];
  }
  let saveList = prescriptionList.value.filter((item) => {
    return item.statusEnum == 1;
  });

  let validList = saveList.filter((item) => {
    return !item.requestId;
  });
  if (validList.length > 0) {
    proxy.$modal.msgWarning('存在未保存的医嘱，请先点击保存按钮后再执行签发');
    return;
  }
  if (saveList.length == 0) {
    proxy.$modal.msgWarning('当前无可签发处方');
    return;
  }
  if (!validateGroups(saveList)) {
    return;
  }
  // let itemName = '';
  // saveList.forEach((item) => {
  //   if (item.injectFlag == 1 && item.groupId == undefined) {
  //     itemName = itemName ? itemName + ', ' : '';
  //     itemName += item.adviceName;
  //   }
  // });
  // if (itemName != '') {
  //   proxy.$modal.msgWarning(itemName + '为输液项目，请进行分组');
  //   return;
  // }
  // saveList.forEach((item) => {
  //   item.patientId = props.patientInfo.patientId;
  //   item.encounterId = props.patientInfo.encounterId;
  //   item.accountId = props.patientInfo.accountId;
  //   item.dbOpType = '1';
  // });
  // 此处签发处方和单行保存处方传参相同，后台已经将传参存为JSON字符串，此处直接转换为JSON即可
  loading.value = true;
  let list = saveList.map((item) => {
    return {
      ...JSON.parse(item.contentJson),
      adviceType: item.adviceType,
      requestId: item.requestId,
      dbOpType: '1',
      groupId: item.groupId,
      uniqueKey: undefined,
    };
  });
  savePrescriptionSign({
    organizationId: props.patientInfo.orgId,
    adviceSaveList: list,
  })
    .then((res) => {
      if (res.code === 200) {
        proxy.$modal.msgSuccess('保存成功');
        getListInfo(false);
        nextId.value == 1;
      }
    })
    .finally(() => {
      loading.value = false;
    });
}

function handleClickOutside(row, index) {
  nextTick(() => {
    handleSaveSign(row, index);
  });
}

// 单行处方保存
function handleSaveSign(row, index) {
  proxy.$refs['formRef' + index].validate((valid) => {
    if (valid) {
      row.isEdit = false;
      isAdding.value = false;
      expandOrder.value = [];
      row.contentJson = undefined;
      row.patientId = props.patientInfo.patientId;
      row.encounterId = props.patientInfo.encounterId;
      row.accountId = accountId.value;
      if (row.adviceType == 1) {
        row.minUnitQuantity =
          row.doseUnitCode == row.unitCode ? row.quantity : row.quantity * row.partPercent;
      } else {
        row.minUnitQuantity = row.quantity;
      }
      row.conditionId = conditionId.value;
      row.unitPrice =
        row.unitCodeList.find((item) => item.value == row.unitCode).type == 'unit'
          ? row.unitPrice
          : new Decimal(row.unitPrice).div(row.partPercent).toFixed(2);
      row.conditionDefinitionId = conditionDefinitionId.value;
      row.encounterDiagnosisId = encounterDiagnosisId.value;
      row.diagnosisName = diagnosisName.value;
      // row.dose = row.doseQuantity;
      // row.doseUnitCode = unitCodeList.value.find((item) => item.type == 'dose').value;
      // row.doseUnitCode = JSON.parse(JSON.stringify(row.minUnitCode)); // 页面显示与赋值不符，此处先简单处理，后续修改
      row.contentJson = JSON.stringify(row);
      // savePrescription({ adviceSaveList: prescriptionList.value }).then((res) => {
      //   if (res.code === 200) {
      //     proxy.$modal.msgSuccess('保存成功');
      //     getListInfo(true);
      //     nextId.value == 1;
      //   }
      // });
    }
  });
}

function handleSaveBatch() {
  let saveList = prescriptionList.value
    .filter((item) => {
      return item.statusEnum == 1 && !item.requestId;
    })
    .map((item) => {
      return {
        ...item,
        dbOpType: item.requestId ? '2' : '1',
      };
    });
  if (saveList.length == 0) {
    proxy.$modal.msgWarning('当前没有可保存医嘱');
    return;
  }
  savePrescription({ adviceSaveList: saveList }).then((res) => {
    if (res.code === 200) {
      proxy.$modal.msgSuccess('保存成功');
      getListInfo(true);
      nextId.value == 1;
    }
  });
}

function setValue(row) {
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
  prescriptionList.value[rowIndex.value] = {
    ...prescriptionList.value[rowIndex.value],
    ...JSON.parse(JSON.stringify(row)),
  };
  prescriptionList.value[rowIndex.value].orgId = undefined;
  prescriptionList.value[rowIndex.value].dose = undefined;
  prescriptionList.value[rowIndex.value].unitCodeList = unitCodeList.value;
  // prescriptionList.value[rowIndex.value].doseUnitCode =
  //   row.minUnitCode != row.unitCode ? row.minUnitCode : row.unitCode;
  // prescriptionList.value[rowIndex.value].minUnitCode = JSON.parse(JSON.stringify(row.doseUnitCode));
  prescriptionList.value[rowIndex.value].doseUnitCode = row.doseUnitCode;
  prescriptionList.value[rowIndex.value].minUnitCode = row.minUnitCode;
  prescriptionList.value[rowIndex.value].unitCode =
    row.partAttributeEnum == 1 ? row.minUnitCode : row.unitCode;
  // prescriptionList.value[rowIndex.value].doseUnitCode_dictText = row.minUnitCode_dictText;
  prescriptionList.value[rowIndex.value].definitionId = JSON.parse(
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
    prescriptionList.value[rowIndex.value].stockList = stockList.value;
    // 获取默认批次号的库存，如果没有让医生重新选
    let stock = stockList.value.filter((item) => {
      return item.lotNumber == row.defaultLotNumber;
    })[0];
    if (stock != {} && stock != undefined) {
      if (stock.quantity <= 0) {
        proxy.$modal.msgWarning('该项目库存不足，请选择其它库房');
        // return;
      }
      prescriptionList.value[rowIndex.value].lotNumber = stock.lotNumber;
      prescriptionList.value[rowIndex.value].inventoryId = stock.inventoryId;
      prescriptionList.value[rowIndex.value].locationId = stock.locationId;
      prescriptionList.value[rowIndex.value].unitPrice = stock.price;
      prescriptionList.value[rowIndex.value].positionName = stock.locationName;
    }
  } else {
    prescriptionList.value[rowIndex.value].orgId = JSON.parse(JSON.stringify(row)).positionId;
    prescriptionList.value[rowIndex.value].unitPrice = row.priceList[0].price;
  }
}

// 组套保存
function handleSaveGroup(orderGroupList) {
  // orderGroupList.map((item) => {
  //   item.patientId = props.patientInfo.patientId;
  //   item.encounterId = props.patientInfo.encounterId;
  //   item.accountId = accountId.value;
  //   item.dbOpType = item.requestId ? '2' : '1';
  //   item.minUnitQuantity = item.quantity * item.partPercent;
  //   item.conditionId = conditionId.value;
  //   item.conditionDefinitionId = conditionDefinitionId.value;
  //   item.encounterDiagnosisId = encounterDiagnosisId.value;
  //   item.contentJson = JSON.stringify(item);
  //   prescriptionList.value.push(item);
  // });
  let paramList = orderGroupList.map((item) => {
    return item.adviceDefinitionId;
  });
  getAdviceBaseInfo({
    adviceDefinitionIdParamList: paramList.join(','),
    organizationId: props.patientInfo.orgId,
  }).then((res) => {
    getOrgList();
    res.data.records.forEach((item, index) => {
      rowIndex.value = prescriptionList.value.length;
      setValue(item);
      let orderGroupValue = orderGroupList.find(
        (k) => k.adviceDefinitionId == item.adviceDefinitionId
      );

      prescriptionList.value[rowIndex.value] = {
        ...prescriptionList.value[rowIndex.value],
        ...orderGroupValue,
        patientId: props.patientInfo.patientId,
        encounterId: props.patientInfo.encounterId,
        accountId: accountId.value,
        dbOpType: orderGroupValue.requestId ? '2' : '1',
        minUnitQuantity: orderGroupValue.quantity * orderGroupValue.partPercent,
        conditionId: conditionId.value,
        conditionDefinitionId: conditionDefinitionId.value,
        encounterDiagnosisId: encounterDiagnosisId.value,
      };
      prescriptionList.value[rowIndex.value].contentJson = JSON.stringify(
        prescriptionList.value[rowIndex.value]
      );
    });
    console.log(prescriptionList.value);
  });
  // savePrescription({ adviceSaveList: saveList }).then((res) => {
  //   if (res.code === 200) {
  //     proxy.$modal.msgSuccess('保存成功');
  //     getListInfo(true);
  //     nextId.value == 1;
  //   }
  // });
}

// 历史医嘱复用
function handleSaveHistory(value) {
  let saveRow = {
    ...value,
    patientId: props.patientInfo.patientId,
    encounterId: props.patientInfo.encounterId,
    accountId: accountId.value,
    uniqueKey: undefined,
    dbOpType: value.requestId ? '2' : '1',
    minUnitQuantity: value.quantity * value.partPercent,
    conditionId: conditionId.value,
    conditionDefinitionId: conditionDefinitionId.value,
    encounterDiagnosisId: encounterDiagnosisId.value,
    contentJson: JSON.stringify(value),
  };
  savePrescription({ adviceSaveList: [saveRow] }).then((res) => {
    if (res.code === 200) {
      proxy.$modal.msgSuccess('保存成功');
      getListInfo(true);
      nextId.value == 1;
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
        setTimeout(() => {
          const nextProp = requiredProps.value[nextIndex];
          inputRefs.value[nextProp]?.focus(); // 直接调用 Element 的 focus 方法
        }, 100);
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
        index = prescriptionList.value.findIndex((item) => item.uniqueKey == expandOrder.value[0]);
      }
      if (index == 0) {
        expandOrder.value = [];
      }
      prescriptionList.value.shift();
      isAdding.value = false;
      groupMarkers.value = getGroupMarkers(prescriptionList.value); // 删除行会出现组号混乱的情况，所以这里重新更新标记
    }
  }
}

// 签退
function handleSingOut() {
  let requestIdList = prescriptionList.value
    .filter((item) => {
      return item.check && item.statusEnum == 2;
    })
    .map((item) => {
      return item.requestId;
    });
  if (requestIdList.length == 0) {
    proxy.$modal.msgWarning('请选择已签发医嘱撤回');
  }
  singOut(requestIdList).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess('操作成功');
      getListInfo(false);
    }
  });
}

function handleGroupId(paramList) {
  updateGroupId(paramList).then(() => {
    getListInfo(false);
  });
}

// 组合
function combination() {
  console.log(groupIndexList.value);

  if (groupIndexList.value.length <= 1) {
    proxy.$modal.msgWarning('至少选择两项');
    return;
  }
  // 相同分组用法需要相同
  let uniqueValues = new Set();
  // 相同分组诊断需要相同
  let uniqDiagnosis = new Set();
  // 相同分组诊断需要相同
  let uniqInjectFlag = new Set();
  let status = false;
  groupIndexList.value.forEach((index) => {
    if (prescriptionList.value[index].statusEnum == 2) {
      status = true;
    }
    uniqueValues.add(prescriptionList.value[index].methodCode);
    uniqDiagnosis.add(prescriptionList.value[index].diagnosisName);
    uniqInjectFlag.add(prescriptionList.value[index].injectFlag);
  });
  // 校验是否有已签发的医嘱
  if (status) {
    proxy.$modal.msgWarning('已签发医嘱不允许分组');
    return;
  }
  if (uniqueValues.size != 1) {
    proxy.$modal.msgWarning('同一分组药品用法必须相同');
    return;
  }
  if (uniqDiagnosis.size != 1) {
    proxy.$modal.msgWarning('同一分组诊断必须相同');
    return;
  }
  if (uniqInjectFlag.size != 1) {
    proxy.$modal.msgWarning('同一分组必须全部为输液药品');
    return;
  }
  // 获取当前时间戳拼接组号做唯一组号
  let timestamp = Date.now().toString();
  let updateList = [];
  groupIndexList.value.forEach((index) => {
    updateList.push({
      requestId: prescriptionList.value[index].requestId,
      groupId: timestamp + groupIndex.value,
    });
    // prescriptionList.value[index].groupId = JSON.parse(JSON.stringify(groupIndex.value));
    prescriptionList.value[index].check = false;
  });
  // 更新组号
  handleGroupId({ groupList: updateList });
  // 根据组号排序
  sortPrescriptionList();
  groupMarkers.value = getGroupMarkers(prescriptionList.value); // 更新标记
  groupIndex.value++;
  groupIndexList.value = [];
}

// 拆组
function split() {
  if (groupIndexList.value.length < 1) {
    proxy.$modal.msgWarning('至少选择一项');
    return;
  }

  // 获取选中的所有行
  const selectedRows = groupIndexList.value.map((index) => prescriptionList.value[index]);

  // 校验是否包含已签发的医嘱
  if (selectedRows.some((row) => row.statusEnum === 2)) {
    proxy.$modal.msgWarning('已签发医嘱不允许拆组');
    return;
  }

  // 提取出这些行涉及的所有 groupId
  const selectedGroupIds = [...new Set(selectedRows.map((row) => row.groupId).filter(Boolean))];
  if (selectedGroupIds.length === 0) {
    proxy.$modal.msgWarning('请选择已分组的医嘱');
    return;
  }

  // 构建最终要更新的列表
  let updateList = [];

  // 遍历每个 groupId
  selectedGroupIds.forEach((groupId) => {
    // 当前分组下所有的医嘱
    const groupItems = prescriptionList.value.filter((item) => item.groupId === groupId);

    // 当前分组中被选中的医嘱
    const selectedInGroup = selectedRows.filter((row) => row.groupId === groupId);

    // 如果选中数 = 总数 - 1 → 拆掉整个分组
    if (selectedInGroup.length === groupItems.length - 1) {
      updateList.push(
        ...groupItems.map((item) => ({
          requestId: item.requestId,
          groupId: '',
        }))
      );
    } else {
      // 否则只更新选中的
      updateList.push(
        ...selectedInGroup.map((item) => ({
          requestId: item.requestId,
          groupId: '',
        }))
      );
    }
  });

  // 清除本地数据中的 groupId
  prescriptionList.value.forEach((item) => {
    if (updateList.some((u) => u.requestId === item.requestId)) {
      item.groupId = undefined;
      item.check = false; // 取消勾选
    }
  });

  // 更新分组号
  handleGroupId({ groupList: updateList });

  // 更新分组标记
  groupMarkers.value = getGroupMarkers(prescriptionList.value);

  // 排序保持一致性
  sortPrescriptionList();

  // 清空选中索引
  groupIndexList.value = [];

  proxy.$modal.msgSuccess('拆组成功');
}
// 分组标记处理
function getGroupMarkers(prescriptionList) {
  const groupMap = {};
  const markers = [];

  // 遍历处方列表，记录每组的索引范围（忽略无 groupId 的项）
  prescriptionList.forEach((item, index) => {
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

// 计算总价
function calculateTotalPrice(row, index) {
  nextTick(() => {
    if (row.unitCode == row.minUnitCode) {
      row.totalPrice = (row.unitPrice * row.quantity * 100) / 100;
    } else {
      row.totalPrice = new Decimal(row.unitPrice).div(row.partPercent) * row.quantity;
    }
  });
}

// 单位切换时 自动计算对应单位的总量
function convertValues(row, index) {
  nextTick(() => {
    let code = unitCodeList.value.filter((item) => {
      return item.value == row.doseUnitCode;
    })[0];

    switch (code.type) {
      case 'dose':
        row.dose = row.doseQuantity * row.unitConversionRatio;
        break;
      case 'minUnit':
        row.dose = row.doseQuantity;
        break;
      case 'unit':
        row.dose = row.doseQuantity / row.partPercent;
        break;
    }
  });
  calculateTotalAmount(row, index);
}

// 单次剂量数量改变时自动计算总量
function convertDoseValues(row, index) {
  nextTick(() => {
    let code = unitCodeList.value.filter((item) => {
      return item.value == row.doseUnitCode;
    })[0];

    switch (code.type) {
      case 'dose':
        row.doseQuantity = row.dose / row.unitConversionRatio;
        break;
      case 'minUnit':
        row.doseQuantity = row.dose;
        break;
      case 'unit':
        row.doseQuantity = row.dose * row.partPercent;
        break;
    }
  });
  calculateTotalAmount(row, index);
}

// 总量计算,仅适用只有两种单位的情况
function calculateTotalAmount(row, index) {
  nextTick(() => {
    if (row.adviceType == 2) {
      if (row.partPercent == 1) {
        row.totalPrice = row.quantity * row.unitPrice;
      } else {
        if (row.unitCodeList.find((k) => k.value == row.unitCode).type == 'unit') {
          row.totalPrice = row.quantity * row.unitPrice;
        } else {
          row.totalPrice = ((row.quantity * row.unitPrice) / row.partPercent).toFixed(2);
        }
      }
    } else if (row.adviceType == 1) {
      if (row.rateCode && row.dispensePerDuration) {
        // 根据用药天数和用药频次计算数量
        let count = calculateQuantityByDays(row.rateCode, row.dispensePerDuration);
        if (count) {
          let quantity;
          if (row.unitCode == row.minUnitCode) {
            quantity = calculateQuantityBySplitType(row.partAttributeEnum, row.doseQuantity, count);
            prescriptionList.value[index].quantity = quantity;
            prescriptionList.value[index].totalPrice = (
              (quantity * row.unitPrice) /
              row.partPercent
            ).toFixed(2);
          } else {
            quantity = calculateQuantity(row.partAttributeEnum, row.doseQuantity, count, row.partPercent);
            prescriptionList.value[index].quantity = quantity;
            prescriptionList.value[index].totalPrice = (quantity * row.unitPrice).toFixed(2);
          }
        }
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
  prescriptionList.value.sort((a, b) => {
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

// 获取全部选中行
// function getSelectRows() {
//   return prescriptionList.value.filter((item) => item.check);
// }

// 处理行chexkbox选中
function handleCheckBoxChange(value, index, row) {
  // 选中将当前行索引记录下来，取消将当前行索引删除
  if (value) {
    groupIndexList.value.push(index);
  } else {
    groupIndexList.value.splice(groupIndexList.value.indexOf(index), 1);
  }
  // 如果选中或取消行有组号，将全部相同组号的行全部选中，并记录或删除这些行的索引
  if (row.groupId) {
    // 获取组号相同行
    let sameGroupIdList = prescriptionList.value.filter((item) => {
      return item.groupId == row.groupId;
    });
    // 如果只有一个组号的情况不做处理
    if (sameGroupIdList.length == 1) {
      return;
    } else {
      sameGroupIdList.forEach((item) => {
        // 排除掉当前选中行
        if (row.uniqueKey != item.uniqueKey) {
          // 同步选中状态
          let currentIndex = prescriptionList.value.findIndex((k) => k.uniqueKey == item.uniqueKey);
          prescriptionList.value[currentIndex].check = value;
          if (value) {
            groupIndexList.value.push(currentIndex); // 或使用索引或唯一标识
          } else if (!value) {
            groupIndexList.value.splice(groupIndexList.value.indexOf(currentIndex), 1);
          }
        }
      });
    }
  }
  console.log(groupIndexList.value);
}

// 校验每个组号数量是否大于5和对应分组金额是否大于500
function validateGroups(saveList) {
  // 获取到全部组号和对应的金额
  const groups = saveList
    .filter((item) => item.groupId != null && item.groupId !== '') // 过滤掉 groupId 为空或空字符串的项
    .map((item) => ({
      groupId: item.groupId,
      totalPrice: item.totalPrice,
    }));
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
defineExpose({ getListInfo, getDiagnosisInfo });
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