<template>
  <div class="patientList-container">
    <div class="search-container">
      <el-space :size="4">
        <el-input v-model="searchForm.searchVal" style="width: 100px" placeholder="请输入" />
        <el-button>查询</el-button>
      </el-space>
    </div>
    <div class="patientList-table">
      <el-table :data="patientListData" row-key="id" style="width: 100%; height: 100%" highlight-current-row
        @selection-change="handleSelectionChange" :show-header="false" show-overflow-tooltip>
        <el-table-column label="姓名" prop="name" min-width="100">
          <template #default="{ row }">
            <span class="name" v-if="row.children"> {{ row.department }}({{ row.children?.length || 0 }})</span>
            <div class="patient-name" v-else>
              <span class="name">{{ row.name }}</span>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup >
import { ref } from 'vue';
const searchForm = ref({
  searchVal: '',
})
const patientListData = ref([
  {
    id: 1,
    department: '儿科',
    children: [
      {
        id: 11,
        name: '张三',
        bedName: '1201',
        gender: '男',
        status: '在科',
        attention: '医生A'
      },
      {
        id: 12,
        name: '张三-2',
        age: 3,
        gender: '女',
        status: '在科',
        attention: '医生A'
      }
    ]
  },
  {
    id: 2,
    department: '神经内科科',
    children: [
      {
        id: 21,
        name: '张三',
        bedName: '1201',
        gender: '男',
        status: '在科',
        attention: '医生A'
      },
      {
        id: 22,
        name: '张三-2',
        age: 3,
        gender: '女',
        status: '在科',
        attention: '医生A'
      }
    ]
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
    padding: 0px 8px;
    border-bottom: 1px solid #e4e7ed;
    height: 40px;
    display: flex;
    align-items: center;
  }

  .patientList-table {
    flex: 1;

    :deep(.el-table__indent) {
      display: none !important;
    }

    :deep(.el-table__placeholder) {
      display: none !important;
    }
  }
}
</style>