<template>
  <el-dialog
    title="退费单"
    v-model="props.open"
    width="1300px"
    append-to-body
    destroy-on-close
    @close="close"
    @open="openDialog"
  >
    <div class="footer">
      <div class="statistics">
        <span>共 {{ total }} 个项目</span>
        <!-- <span class="total">合计金额：¥ {{ totalAmount.toFixed(2) }}</span> -->
      </div>
    </div>
    <div>
      <!-- <el-row :gutter="24" class="mb8">
        <el-col :span="12">
          <el-input
            v-model="queryParams.searchKey"
            placeholder="诊断名称/拼音码"
            clearable
            style="width: 100%; margin-bottom: 10px"
            @keyup.enter="queryDiagnosisUse"
          >
            <template #append>
              <el-button icon="Search" @click="queryDiagnosisUse" />
            </template>
          </el-input>
        </el-col>
      </el-row> -->
      <el-table
        ref="refundListRef"
        :data="refundList"
        row-key="paymentId"
        row-class-name="parent-row"
        v-loading="tableLoading"
        border
        height="600"
      >
        <el-table-column
          type="selection"
          width="55"
          align="center"
          class-name="selection-column"
          :selectable="
            (row) => {
              return row.refundStatus == 5;
            }
          "
        />
        <el-table-column label="处方号" align="center" prop="prescriptionNo" />
        <el-table-column label="项目名" align="center" prop="itemName" />
        <el-table-column label="数量" align="center" prop="quantity" />
        <el-table-column label="单位" align="center" prop="unitCode_dictText" />
        <el-table-column label="收款金额" align="center" prop="totalPrice" />
        <el-table-column label="发放状态" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.dispenseStatus != 0" type="default">
              {{ scope.row.dispenseStatus_enumText }}
            </el-tag>
            <el-tag v-else type="default">{{ scope.row.serviceStatus_enumText }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="支付状态" align="center" prop="refundStatus_enumText">
          <template #default="scope">
            <el-tag
              :type="
                handleColor(
                  [1, 2, 3, 4, 5, 8, 9],
                  ['success', 'info', 'warning', 'warning', 'success', 'info', 'error'],
                  scope.row.refundStatus
                )
              "
            >
              {{ scope.row.refundStatus_enumText }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      <!-- <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      /> -->
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="submit">确 定</el-button>
        <el-button @click="close">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>
  
<script setup>
import { getEncounterPatientPayment, refundPayment } from './api';
import { handleColor } from '@/utils/his';

const props = defineProps({
  open: {
    type: Boolean,
    default: false,
  },
  encounterId: {
    type: String,
    default: '',
  },
});
const emit = defineEmits(['close']);
const total = ref(0);
const tableLoading = ref(false);
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
});
const refundList = ref([]);
const expandOrder = ref([]);
const selectRow = ref({});
const { proxy } = getCurrentInstance();
const selectedMedicines = ref(0);
const totalAmount = ref(0);
function openDialog() {
  getList();
}

function getList() {
  refundList.value = [];
  tableLoading.value = true;
  getEncounterPatientPayment(props.encounterId).then((res) => {
    refundList.value = res.data;
    total.value = res.data ? res.data.length : 0;
    // expandOrder.value = refundList.value.map((item) => {
    //   return item.paymentNo;
    // });
    tableLoading.value = false;
  });
}

function submit() {
  // 1. 获取当前选中行并提取去重的 paymentId 列表
  const selectedRows = proxy.$refs['refundListRef'].getSelectionRows();
  const selectedPaymentIds = [...new Set(selectedRows.map((row) => row.paymentId))];

  // 2. 遍历 refundList，筛选出符合条件的数据并设置 refundFlag
  const result = refundList.value
    .filter((row) => selectedPaymentIds.includes(row.paymentId)) // 筛选出选中的 paymentId 对应的原始数据
    .map((row) => ({
      paymentId: row.paymentId,
      chargeItemId: row.chargeItemId,
      refundFlg: selectedRows.some((selectedRow) => selectedRow.chargeItemId === row.chargeItemId), // 是否选中
    }));

  console.log('组装后的数据:', result);

  // 3. 调用接口提交
  refundPayment(result).then((res) => {
    if (res.code === 200) {
      proxy.$modal.msgSuccess('操作成功');
      getList();
    }
  });

  // refundList.value.forEach((item, index) => {
  //   // proxy.$refs['itemRef' + index].getSelectionRows().forEach((row) => {
  //   //   saveList.push({ paymentId: item.paymentId, chargeItemId: row.chargeItemId, refundFlg: true });
  //   // });
  //   const selectedIds = proxy.$refs['refundListRef'].getSelectionRows().map((row) => {
  //     return row.busNo;
  //   });
  //   console.log(selectedIds);
  //   item.chargeItemList.forEach((item) => {
  //     saveList.push({
  //       paymentId: item.paymentId,
  //       chargeItemId: item.chargeItemId,
  //       refundFlg: selectedIds.has(item.busNo),
  //     });
  //   });
  // });
  // refundPayment({
  //   definitionId: selectRow.value.id,
  //   definitionName: selectRow.value.name,
  //   bindingEnum: props.radio == '个人' ? 1 : 2,
  // }).then((res) => {
  //   if (res.code == 200) {
  //     emit('close', 'success');
  //   }
  // });
}

function handleSelectAll(selection) {
  if (selection.length > 0) {
    selection.forEach((item, index) => {
      proxy.$refs['itemRef' + index].toggleAllSelection();
    });
  } else {
    for (let i = 0; i < refundList.value.length; i++) {
      proxy.$refs['itemRef' + i].clearSelection();
    }
  }
}

function handleSelectionChange(value, row) {
  let selectIndex = refundList.value.findIndex((item) => {
    return item.paymentNo === row.paymentNo;
  });
  if (value.includes(row)) {
    proxy.$refs['itemRef' + selectIndex].toggleAllSelection();
  } else {
    proxy.$refs['itemRef' + selectIndex].clearSelection();
  }
}

// 处理外层列表选中状态
function handleItemSelectionChange(parentRow, selection, index) {
  // 子列表全选中，自动选中父级列表
  proxy.$refs['refundListRef'].toggleRowSelection(
    parentRow,
    selection.length == parentRow.chargeItemList.length
  );
}

function queryDiagnosisUse() {
  getList();
}

function close() {
  emit('close');
}

function clickRow(row) {
  selectRow.value = row;
}
</script>

<style lang="scss" scoped>
.sub-table-wrapper {
  padding: 16px 55px;
  background: #f9fafe;

  .nested-sub-table {
    border: 1px solid #e8e8f3;
    border-radius: 2px;

    // :deep(.sub-cell) {
    //   background: #ffffff !important;
    //   padding: 12px 16px;
    // }
  }
}
:deep(.parent-row) {
  td {
    // background: #e8ece6 !important;  /* 浅蓝色背景 */
    // border-color: #e4e7ed !important;
    height: 48px;
  }
}

.footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-top: 1px solid #ebeef5;

  .total {
    margin-left: 20px;
    color: #f56c6c;
    font-weight: 500;
  }
}
</style>