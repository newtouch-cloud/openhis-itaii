<template>
  <div>
    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd"
          >新增
        </el-button>
      </el-col>
    </el-row> -->
    <el-table
      ref="emrHistoryRef"
      :data="emrHistory"
      row-key="patientId"
      highlight-current-row
      @selection-change="handleSelectionChange"
    >
      <!-- @cell-click="clickRow" -->
      <el-table-column type="selection" width="55" />
      <el-table-column label="主诉" align="left" prop="name" width="460"  :show-overflow-tooltip="true"/>
      <el-table-column label="时间" align="center" prop="createTime" width="180"  :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" width="80">
        <template #default="scope">
          <!-- <el-button
            link
            type="primary"
            @click.stop="handelDetail(scope.row)"
            >详情
          </el-button> -->
          <el-button link type="primary" @click.stop="clickRow(scope.row)">复用 </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script setup>
import { formatDate, formatDateymd } from '@/utils/index';
import { getEmrHistoryList } from './api';
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  patientId: '',
});
const selectRow = ref({});
const emrHistory = ref([]);
const emits = defineEmits(['selectRow']);

getList();
function getList() {
  if (sessionStorage.getItem('patientId')) {
    queryParams.value.patientId = sessionStorage.getItem('patientId');
    getEmrHistoryList(queryParams.value).then((res) => {
      emrHistory.value = res.data.records;
      emrHistory.value.map((k) => {
        k.name = JSON.parse(k.contextJson).chiefComplaint;
        k.createTime = formatDate(k.createTime);
      });
    });
  }
}

function clickRow(row) {
  selectRow.value = JSON.parse(row.contextJson);
  emits('selectRow', selectRow.value);
}
</script>