<template>
  <div class="patientList-container">
    <div class="search-container">
      <el-button>查询</el-button>
    </div>
    <div class="patientList-table">
      <el-table :data="patientListData" row-key="id" style="width: 100%; height: 100%" highlight-current-row
        @selection-change="handleSelectionChange" :show-header="false" show-overflow-tooltip>
        <el-table-column label="姓名" prop="name" min-width="100">
          <template #default="{ row }">
            <div class="patient-name">
              <span class="name">{{ row.name }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column type="expand">
          患者详细信息
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup >
import { ref } from 'vue';

const patientListData = ref([
  {
    id: 1,
    name: '张三1111111',
    age: 30,
    gender: '男',
    status: '在科',
    attention: '医生A',
    department: '儿科',
  },
  {
    id: 2,
    name: '李四',
    age: 25,
    gender: '女',
    status: '转科',
    attention: '医生B'
  }
])
const handleSelectionChange = (selection: any[]) => {
  console.log('handleSelectionChange', selection)
}
</script>

<style lang="scss" scoped>
.patientList-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  overflow: hidden;
  width: 240px;
  border-right: 1px solid #e4e7ed;

  .search-container {
    flex: none;
    padding: 8px 8px;
    text-align: right;

  }

  .patientList-table {
    flex: 1;
  }
}
</style>