<template>
  <div style="width: 100%">
    <div style="margin-bottom: 5px">
      <el-button type="primary" @click="handleAddPrescription()" :disabled="false">
        新增
      </el-button>
      <el-button type="primary" @click="handleSave()" :disabled="handleSaveDisabled"> 签发 </el-button>
      <el-button type="warning" plain @click="handleSingOut()" :disabled="handleSingOutDisabled"> 签退 </el-button>
      <!-- <el-button type="primary" plain @click="open()" :disabled="false"> 组套 </el-button> -->
      <el-button type="danger" plain @click="handleDelete()" :disabled="false"> 删除 </el-button>
    </div>
    <el-table
      max-height="650"
      ref="prescriptionRef"
      :data="prescriptionList"
      row-key="uniqueKey"
      border
      @row-dblclick="clickRowDb"
      :expand-row-keys="expandOrder"
    >
      <el-table-column type="expand" width="1" style="width: 0">
        <template #default="scope">
          <el-form :model="scope.row" :rules="rowRules" :ref="'formRef' + scope.$index">
            <div style="padding: 16px; background: #f8f9fa; border-radius: 8px">
              <template v-if="scope.row.adviceType == 2">
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
            :disabled = "scope.row.bizRequestFlag==0"
            v-model="scope.row.check"
            placeholder=""
            @click.stop=""
            @change="changeCheck(scope.row.check,scope.$index,scope.row)"
          />
        </template>
        <!-- (value) => {
          if (value) {
            groupIndexList.push(scope.$index);
          } else {
            groupIndexList.splice(groupIndexList.indexOf(scope.$index), 1);
          }
        } -->
      </el-table-column>
      <el-table-column label="项目" align="center" prop="productName" width="400">
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
          <el-tag v-else-if="scope.row.statusEnum == 1" type="">待签发</el-tag>
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
      <el-table-column label="签发人" align="center" prop="" width="240">
        <template #default="scope">
          <span v-if="!scope.row.isEdit">
            {{ scope.row.requesterId_dictText }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="签发时间" align="center" prop="" width="240">
        <template #default="scope">
          <span v-if="!scope.row.isEdit">
            {{ scope.row.requestTime }}
          </span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
      
<script setup>
import {
  savePrescription,
  getPrescriptionList,
  getOrgTree,
  savePrescriptionSign,
  singOut,
} from './api';
import adviceBaseList from './adviceBaseList';
import { getCurrentInstance, nextTick, ref, watch } from 'vue';

const emit = defineEmits(['selectDiagnosis']);
const prescriptionList = ref([]);
const form = ref({
  prescriptionList: prescriptionList.value,
});
const adviceQueryParams = ref({});
const rowIndex = ref(-1);
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
const groupList = ref([])
const { proxy } = getCurrentInstance();
const inputRefs = ref({}); // 存储输入框实例
const requiredProps = ref([]); // 存储必填项 prop 顺序
const { method_code, unit_code, rate_code, distribution_category_code } = proxy.useDict(
  'method_code',
  'unit_code',
  'rate_code',
  'distribution_category_code'
);
const handleSaveDisabled = ref(false)  //签发状态
const handleSingOutDisabled = ref(false) //签退状态
const adviceTypeList = ref([
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
watch(
  () => expandOrder.value,
  (newValue) => {
     console.log(newValue,"监听·")
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
  (newValue) => {
    console.log(prescriptionList.value,"prescriptionList.value")
    if(newValue&&newValue.length>0){
      let saveList = prescriptionList.value.filter((item) => {
        return item.statusEnum == 1&&(Number(item.bizRequestFlag)==1||!item.bizRequestFlag)
      })
      prescriptionList.value.map(k=>{
        k.check = false
      })
       console.log(saveList,"prescriptionList.value")
      if (saveList.length == 0) {
        handleSaveDisabled.value = true
      }else{
        handleSaveDisabled.value = false
      }
    }
  },
  { immediate: true }
);

function getListInfo(addNewRow) {
  isAdding.value = false;
  getPrescriptionList(props.patientInfo.encounterId).then((res) => {
    prescriptionList.value = res.data;
    if (props.activeTab == 'prescription' && addNewRow) {
      handleAddPrescription();
    }
  });
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
  prescriptionList.value.unshift({
    uniqueKey: nextId.value++,
    showPopover: false,
    check: false,
    isEdit: true,
    statusEnum: 1,
  });
  nextTick(() => {
    proxy.$refs['adviceRef0'].focus();
  });
}

// 行双击打开编辑块，仅待发送的可编辑
function clickRowDb(row) {
  if (row.statusEnum == 1) {
    row = { ...row, ...JSON.parse(row.contentJson) };
    row.isEdit = true;
    const index = prescriptionList.value.findIndex((item) => item.uniqueKey === row.uniqueKey);
    prescriptionList.value[index] = row;
    console.log(prescriptionList.value,"prescriptionList.value")
    expandOrder.value = [row.uniqueKey];
  }
}

function handleDiagnosisChange(item, row) {
  row.diagnosisName = item.name;
  row.conditionId = item.conditionId;
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
  prescriptionList.value[rowIndex.value] = {
    ...prescriptionList.value[rowIndex.value],
    ...JSON.parse(JSON.stringify(row)),
  };
  prescriptionList.value[rowIndex.value].orgId = undefined;
  prescriptionList.value[rowIndex.value].dose = undefined;
  prescriptionList.value[rowIndex.value].unitCodeList = unitCodeList.value;
  prescriptionList.value[rowIndex.value].doseUnitCode =
    row.minUnitCode != row.unitCode ? row.minUnitCode : row.unitCode;
  prescriptionList.value[rowIndex.value].minUnitCode = JSON.parse(JSON.stringify(row.doseUnitCode));
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
    console.log(organization.value,"organization.value")
  });
}

function handleDelete() {
  let deleteList = prescriptionList.value
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
    proxy.$modal.msgWarning('请选择要删除的项目');
    return;
  }
  if (!deleteList[0].requestId) {
    prescriptionList.value.shift();
  } else {
    savePrescription({ adviceSaveList: deleteList }).then((res) => {
      if (res.code == 200) {
        proxy.$modal.msgSuccess('操作成功');
        getListInfo(false);
      }
    });
  }
  // groupIndexList.value
  //   .sort((a, b) => b - a)
  //   .forEach((item) => {
  //     prescriptionList.value.splice(item, 1);
  //   });
  // groupIndexList.value = [];
  expandOrder.value = [];
  isAdding.value = false;
  adviceQueryParams.value.adviceType = undefined;
  // prescriptionList.value.splice(index, 1);
}

function handleNumberClick(item, index) {
  prescriptionList.value[index].unitPrice = item.price;
  // prescriptionList.value[index].lotNumber = item.lotNumber;
  prescriptionList.value[index].locationId = item.locationId;
  prescriptionList.value[index].positionId = item.locationId;
  prescriptionList.value[index].positionName = item.locationName;
}
function changeCheck(value,index,row){
  if (value) {
    groupIndexList.value.push(index)
    groupList.value.push(row)
  } else {
    groupIndexList.value.splice(groupIndexList.value.indexOf(index), 1)
    groupList.value.splice(groupList.value.indexOf(index), 1)
  }
  groupList.value.map(k=>{
    if(k.check){
      if(k.statusEnum == 1){//待签发
        if(Number(k.bizRequestFlag)==1||!k.bizRequestFlag){
          if(handleSaveDisabled.value&&!handleSingOutDisabled.value&&groupList.value.length>1){
            proxy.$modal.msgWarning('请选择相同的状态的项目进行操作')
            return
          }else{
            handleSaveDisabled.value = false
            handleSingOutDisabled.value = true
          }
        }else{
          handleSaveDisabled.value = true
          handleSingOutDisabled.value = true
          return
        }
      }
      if(k.statusEnum == 2){ //已签发
        if(Number(k.bizRequestFlag)==1||!k.bizRequestFlag){
          if(!handleSaveDisabled.value&&handleSingOutDisabled.value&&groupList.value.length>1){
            proxy.$modal.msgWarning('请选择相同的状态的项目进行操作')
            return
          }else{
            handleSaveDisabled.value = true
            handleSingOutDisabled.value = false
          }
        }else{
          handleSaveDisabled.value = true
          handleSingOutDisabled.value = true
          return
        }
      }
    }
  })
  console.log(groupIndexList.value,"!21")
}
/**
 * 保存处方
 */
function handleSave() {
  if (expandOrder.value.length > 0) {
    proxy.$modal.msgWarning('请先保存当前医嘱');
    return;
  }
  let saveList = prescriptionList.value.filter((item) => {
    return item.statusEnum == 1&&(Number(item.bizRequestFlag)==1||!item.bizRequestFlag)
  });
  // let saveList = prescriptionList.value
  // .filter((item) => {
  //   return item.check;
  // }).filter((item) => {
  //   return item.statusEnum == 1&&item.bizRequestFlag==1
  // })

  // if (saveList.length == 0) {
  //   proxy.$modal.msgWarning('当前无可签发处方');
  //   return;
  // }
  // 此处签发处方和单行保存处方传参相同，后台已经将传参存为JSON字符串，此处直接转换为JSON即可
  let list = saveList.map((item) => {
    return {
      ...JSON.parse(item.contentJson),
      requestId: item.requestId,
      dbOpType: '1',
      groupId: item.groupId,
    };
  });
  savePrescriptionSign({
    organizationId: props.patientInfo.orgId,
    adviceSaveList: list,
  }).then((res) => {
    if (res.code === 200) {
      proxy.$modal.msgSuccess('保存成功');
      getListInfo(false);
      prescriptionList.value.map(k=>{
        k.check = false
      })
      groupIndexList.value = []
      groupList.value = []
      nextId.value == 1;
    }
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

// 签退
function handleSingOut() {
  let requestIdList = prescriptionList.value
    .filter((item) => {
      return item.check;
    })
    .filter((item) => {
      return item.statusEnum == 2&&(Number(item.bizRequestFlag)==1||!item.bizRequestFlag)
    })
    .map((item) => {
      return item.requestId;
    });
    console.log(requestIdList,"签退")
  if (requestIdList.length == 0) {
    proxy.$modal.msgWarning('未选择可签退的医嘱');
    return
  }
  singOut(requestIdList).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess('操作成功');
      getListInfo(false);
      console.log( prescriptionList.value," groupIndexList.value")
      prescriptionList.value.map(k=>{
        k.check = false
      })
      groupIndexList.value = []
      groupList.value = []
    }
  });
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