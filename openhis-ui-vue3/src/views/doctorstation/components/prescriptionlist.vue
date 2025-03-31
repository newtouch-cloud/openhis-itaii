<template>
  <div style="width: 1250px">
    <el-button
      type="primary"
      plain
      @click="handleAddPrescription()"
      :disabled="buttonDisabled"
    >
      新增处方
    </el-button>
    <el-button
      type="primary"
      plain
      @click="handleSave()"
      :disabled="buttonDisabled"
    >
      发送处方
    </el-button>
    <el-table
      v-horizontal-scroll
      ref="prescriptionRef"
      :data="prescriptionList"
      row-key="patientId"
      border
      @cell-click="clickRow"
      @selection-change="handleSelectionChange"
    >
      <el-table-column label="组号" align="center" prop="name">
        <template #default="scope">
          <el-input v-model="scope.row.name" placeholder="" />
        </template>
      </el-table-column>
      <el-table-column
        label="名称"
        align="center"
        prop="productName"
        width="200"
      >
        <template #default="scope">
          <el-popover
            :popper-style="{ padding: '0' }"
            placement="bottom-start"
            :visible="scope.row.showPopover"
            trigger="manual"
            :width="1200"
          >
            <advicebaselist
              :searchkey="searchkey"
              @selsectAdviceBase="selsectAdviceBase"
            />
            <template #reference>
              <el-input
                v-model="scope.row.productName"
                placeholder=""
                @input="handleChange"
                @focus="handleFocus(scope.row, scope.$index)"
                @blur="handleBlur(scope.row)"
              />
            </template>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column
        label="批次号"
        align="center"
        prop="lotNumber"
        width="180"
      >
        <template #default="scope">
          <el-select v-model="scope.row.lotNumber" placeholder=" ">
            <el-option
              v-for="item in scope.row.stockList"
              :key="item.lotNumber"
              :label="
                item.lotNumber +
                '  ' +
                item.locationName +
                '  库存：' +
                item.baseQuantity +
                item.baseUnitCode +
                '  单价：' +
                item.price +
                '/' +
                item.unitCode
              "
              :value="item.lotNumber"
              @click="handleNumberClick(item, scope.$index)"
            />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="规格" align="center" prop="volume" width="180" />
      <el-table-column label="单价" align="center" prop="unitPrice" />
      <el-table-column label="单次计量" align="center" prop="dose">
        <template #default="scope">
          <el-input v-model="scope.row.dose" placeholder="" />
        </template>
      </el-table-column>
      <el-table-column
        label="单位"
        align="center"
        prop="doseUnitCode_dictText"
        width="90"
      >
        <template #default="scope">
          <el-select v-model="scope.row.unitCode" placeholder=" ">
            <el-option
              v-for="dict in unit_code"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column
        label="用法"
        align="center"
        prop="methodCode_dictText"
        width="130"
      >
        <template #default="scope">
          <el-select v-model="scope.row.methodCode" placeholder=" " clearable>
            <el-option
              v-for="dict in method_code"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="组套" align="center" prop="zt" />
      <el-table-column
        label="频次"
        align="center"
        prop="rateCode_dictText"
        width="110"
      >
        <template #default="scope">
          <el-select v-model="scope.row.rateCode" placeholder="">
            <el-option
              v-for="dict in rate_code"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="滴速" align="center" prop="" />
      <el-table-column label="用药天数" align="center" prop="day">
        <template #default="scope">
          <el-input v-model="scope.row.day" placeholder="" />
        </template>
      </el-table-column>
      <el-table-column label="药品总量" align="center" prop="quantity">
        <template #default="scope">
          <el-input v-model="scope.row.quantity" placeholder="" />
        </template>
      </el-table-column>
      <el-table-column label="单位" align="center" prop="" width="90">
        <template #default="scope">
          <el-select v-model="scope.row.unitCode" placeholder=" ">
            <el-option
              v-for="dict in unit_code"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="金额" align="center" prop="" />
      <el-table-column label="皮试" align="center" prop="">
        <template #default="scope">
          <el-select v-model="scope.row.name" placeholder="" />
        </template>
      </el-table-column>
      <el-table-column label="留观" align="center" prop="">
        <template #default="scope">
          <el-select v-model="scope.row.name" placeholder="" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" fixed="right">
        <template #default="scope">
          <el-button link type="primary" @click="handleDelete(scope.$index)"
            >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
      
<script setup>
import { getDiagnosisDefinitionList, savePrescription } from "./api";
import advicebaselist from "./advicebaselist";
import { getCurrentInstance } from "vue";
const emit = defineEmits(["selsectDiagnosis"]);
const total = ref(0);
const queryParams = ref({});
const prescriptionList = ref([]);
const searchkey = ref("");
const rowIndex = ref(-1);
const buttonDisabled = computed(() => {
  return !props.patientInfo;
});
const props = defineProps({
  patientInfo: {
    type: Object,
    required: true,
  },
});
const prescriptionRef = ref();
const stockList = ref([]);
const { proxy } = getCurrentInstance();
const { method_code, unit_code, rate_code } = proxy.useDict(
  "method_code",
  "unit_code",
  "rate_code"
);

getList();
function getList() {
  getDiagnosisDefinitionList(queryParams.value).then((res) => {
    // prescriptionList.value = res.data.records;
    total.value = res.data.total;
  });
}

function handleAddPrescription() {
  prescriptionList.value.push({ showPopover: false });
}

/**
 * 点击行赋值
 */
function clickRow(row) {
  emit("selsectDiagnosis", row);
}

function handleFocus(row, index) {
  rowIndex.value = index;
  row.showPopover = true;
}

function handleBlur(row) {
  row.showPopover = false;
}

function handleChange(value) {
  searchkey.value = value;
}

/**
 * 选择药品回调
 */
function selsectAdviceBase(row) {
  prescriptionList.value[rowIndex.value] = JSON.parse(JSON.stringify(row));
  prescriptionList.value[rowIndex.value].definitionId = JSON.parse(
    JSON.stringify(row)
  ).chargeItemDefinitionId;

  console.log(row, 234567890);

  // 库存列表 + 价格列表拼成批次号的下拉框
  prescriptionList.value[rowIndex.value].stockList = row.inventoryList.map(
    (item, index) => {
      return { ...item, ...row.priceList[index] };
    }
  );
}

function handleDelete(index) {
  prescriptionList.value.splice(index, 1);
}

function handleNumberClick(item, index) {
  prescriptionList.value[index].unitPrice = item.price;
}

/**
 * 保存处方
 */
function handleSave() {
  prescriptionList.value.forEach((item) => {
    item.patientId = props.patientInfo.patientId;
    item.encounterId = props.patientInfo.encounterId;
    item.accountId = props.patientInfo.accountId;
    item.unitPrice = 1;
  });
  savePrescription({ adviceSaveList: prescriptionList.value }).then((res) => {
    if (res.code === 200) {
      proxy.$modal.msgSuccess("保存成功");
    }
  });
}
</script>
    
<style scoped>
::v-deep .el-table__row:hover > td {
  background-color: #cde5ff !important; /* 设置为透明或其他你想要的颜色 */
}
</style>