<template>
  <div>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd"
          >新增
        </el-button>
      </el-col>
    </el-row>
    <el-table
      ref="emrHistoryRef"
      :data="emrHistory"
      row-key="patientId"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column label="诊断" align="left" prop="name" />
      <el-table-column label="时间" align="center" prop="typeEnum" />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="handelEdit(scope.row)"
            v-hasPermi="['system:dict:edit']"
            >修改
          </el-button>
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
</template>

<script>
import { getEmrHistoryList } from "./api";

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
});
const emrHistory = ref([]);

getList();
function getList() {
  getEmrHistoryList(queryParams.value).then((res) => {
    emrHistory.value = res.data.records;
  });
}
</script>