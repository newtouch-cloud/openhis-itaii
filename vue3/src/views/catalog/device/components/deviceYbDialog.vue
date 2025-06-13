<template>
  <div class="app-container">
    <el-dialog
      title="医用耗材目录"
      v-model="visible"
      width="1500px"
      append-to-body
      destroy-on-close
      @close="cancel"
    >
      <div>
        <el-input
          v-model="queryParams.searchKey"
          placeholder="请输入耗材名"
          clearable
          style="width: 20%; margin-bottom: 10px"
          @keyup.enter="getList"
        >
          <template #append>
            <el-button icon="Search" @click="getList" />
          </template>
        </el-input>
      </div>
      <el-table v-loading="listLoading" border :data="list" highlight-current-row max-height="450">
        <el-table-column align="center" label="医保目录编码" prop="medicalCatalogCode" />
        <el-table-column align="center" label="耗材名称" prop="consumableName" />
        <el-table-column align="center" label="产品型号" prop="productModel">
          <template #default="scope">
            {{ formatStr(scope.row.drugCategoryName) }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="耗材材质" prop="materialType" />
        <el-table-column align="center" label="规格" prop="specification" />
        <el-table-column align="center" label="生产厂家" prop="manufacturerName" />
        <el-table-column align="center" label="操作" width="80">
          <template #default="scope">
            <el-button link type="primary" @click="handlewAddDevice(scope.row)"> 对照 </el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNo"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { getYbDeviceList } from './device';

const emit = defineEmits(['selectDevice']);
const visible = ref(false);
const total = ref(0);
const listLoading = ref(false);
const queryParams = ref({
  pageNo: 1,
  pageSize: 10,
  //医用耗材目录
  catalogType: 1306,
});
const list = ref([]);
getList();
function getList() {
  listLoading.value = true;
  getYbDeviceList(queryParams.value).then((res) => {
    total.value = res.data.data.total;
    list.value = res.data.data.records;
    listLoading.value = false;
  });
}

function show() {
  getList();
  visible.value = true;
}

function cancel() {
  visible.value = false;
}

function handlewAddDevice(row) {
  emit('selectDevice', row);
  cancel();
}

function formatStr(str) {
  if (str === null || str === undefined || str === '' || str === 'null') {
    return '-';
  }
  return str;
}

defineExpose({
  show,
});
</script>

<style lang="scss" scoped>
:deep(.el-dialog__body .pagination-container .el-pagination) {
  padding-right: 20px !important;
}
</style>