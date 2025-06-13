<!--
 * @Author: sjjh
 * @Date: 2025-04-16 20:14:58
 * @Description: 患者列表组件
-->
<template>
  <div class="patientList-container">
    <div class="patientList-wrapper" :class="{ 'collapsed': isCollapsed }">
      <div class="patientList-header">
        <el-space :size="4">
          <el-input v-model="searchForm.searchVal" style="width: 100px" placeholder="住院号/姓名" v-if="!isCollapsed" />
          <el-select v-model="searchForm.patientStatus" placeholder="" style="min-width: 70px" v-if="!isCollapsed">
            <el-option key="1" label="在科" value="1" />
            <el-option key="2" label="转科" value="2" />

          </el-select>
          <el-icon @click="refresh" class="refresh-icon">
            <Refresh />
          </el-icon>
          <el-icon @click="toggleCollapse" class="collapse-icon">
            <template v-if="isCollapsed">
              <Expand />
            </template>
            <template v-else>
              <Fold />
            </template>
          </el-icon>
        </el-space>
      </div>
      <!-- 待入科列表 -->
      <div class="patientList-table">
        <el-table :data="patientListData" row-key="id" style="width: 100%; height: 100%" highlight-current-row
          @selection-change="handleSelectionChange" :show-header="false" show-overflow-tooltip>
          <!-- <el-table-column type="selection" :width="isCollapsed ? 14 : 20" /> -->
          <el-table-column label="姓名" prop="name" min-width="100">
            <template #default="{ row }">
              <template v-if="row?.children?.length > 0">
                <span>{{ row.department }}</span>
              </template>
              <div class="patient-name" v-else>
                <span class="name">{{ row.name }}</span>
                <span class="age" v-if="!isCollapsed">{{ row.age }}岁</span>
                <span class="gender" v-if="!isCollapsed">{{ row.gender }}</span>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script setup >
import { ref, reactive } from 'vue'
import { Expand, Fold, Refresh } from '@element-plus/icons-vue'

const isCollapsed = ref(false)

const toggleCollapse = () => {
  isCollapsed.value = !isCollapsed.value
}

const refresh = () => {
  // 刷新逻辑
}

const searchForm = reactive({
  searchVal: '',
  patientStatus: '',
  attention: ''
})

const patientListData = ref([
  {
    id: 1,
    name: '张三1111111',
    age: 30,
    gender: '男',
    status: '在科',
    attention: '医生A',
    department: '儿科',
    children: [
      {
        id: 11,
        name: '张三-11111111',
        age: 5,
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
    name: '李四',
    age: 25,
    gender: '女',
    status: '转科',
    attention: '医生B'
  }
])

const selectedRows = ref<any[]>([])

const handleSelectionChange = (selection: any[]) => {
  selectedRows.value = selection
}
</script>

<style scoped lang="scss">
.patientList-container {
  // width: 100%;
  height: 100%;
  overflow: hidden;
  max-width: 240px;
  position: relative;
  display: flex;
  flex-direction: column;

  .patientList-wrapper {
    height: 100%;
    width: 240px;
    background: #fff;
    border-right: 1px solid #e8e8e8;
    display: flex;
    flex-direction: column;
    transition: width 0.3s ease;

    &.collapsed {
      width: 70px;

      .el-input,
      .el-select {
        display: none;
      }

      .patientList-table {
        width: 100% !important;
        overflow: hidden;
        padding: 0 0px 8px 0px;
      }

      .el-table {
        width: 100% !important;
        overflow: hidden;
        position: relative;
      }

      .el-table__header-wrapper,
      .el-table__body-wrapper {
        width: 100% !important;
        overflow: hidden;
      }

      .el-table__header {
        display: none;
      }

      .el-table__body {
        display: block;
        overflow: hidden;
      }

      .el-table__body tr {
        display: block;
        padding: 4px 8px;
        text-align: center;
        border-bottom: 1px solid #e8e8e8;
        overflow: hidden;
      }

      .el-table__body td {
        display: block;
        padding: 0;
        border: none;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .el-table__body .name {
        font-size: 16px;
      }

      .el-table__body .status,
      .el-table__body .attention {
        display: none;
      }
    }

    .patientList-header {
      min-height: 88px;
      padding: 8px 8px;
      border-bottom: 1px solid #e8e8e8;
      background: #fff;
      display: flex;
      flex-direction: column;
      gap: 8px;
      min-height: 40px;

      .el-space {
        flex: 1;
        display: flex;
        align-items: center;
        width: 100%;
        gap: 8px;

        .el-input {
          width: 120px;
        }

        .el-select {
          flex: 1;
          min-width: 70px;
        }

        .el-icon {
          flex: none;
          width: auto;
        }
      }
    }

    .patientList-table {
      flex: 1;
      overflow: hidden;
      width: 100%;
      position: relative;
      display: flex;
      flex-direction: column;
      padding: 0 8px 8px 8px;
    }

    .el-table {
      width: 100% !important;
      height: 100%;
      overflow: hidden;
      position: relative;
    }

    .el-table__body {
      overflow: hidden;
      position: relative;
    }

    .el-table__body-wrapper {
      overflow: hidden !important;
    }

    .el-table__body tr {
      position: relative;
    }

    .collapse-icon,
    .refresh-icon {
      cursor: pointer;
      font-size: 20px;
    }
  }
}

.patient-name {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  color: #666;
}

::v-deep .el-table__indent {
  display: none !important;
}

::v-deep .el-table__placeholder {
  display: none !important;
}

::v-deep .el-table__expanded-cell {
  padding: 0 !important;
}

::v-deep .el-table__expanded-cell .el-table__cell {
  padding: 0 !important;
}

::v-deep .el-table__expanded-cell .el-table__cell .patient-name {
  margin-left: 4px;
}

::v-deep .cell {
  padding-left: 0 !important;
  padding-right: 0 !important;
}

::v-deep .el-table__expanded-cell {
  padding: 0 !important;
}

::v-deep .el-table__expanded-cell .el-table__cell .patient-name {
  margin-left: 4px;
}
</style>
