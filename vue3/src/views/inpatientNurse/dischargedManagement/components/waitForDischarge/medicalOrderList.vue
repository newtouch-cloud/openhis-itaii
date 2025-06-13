<template>
  <div class="medicalOrderList-container">
    <div class="search-container">
      <el-button>出院</el-button>
    </div>
    <div class="medicalOrderList-table">
      <el-table :data="medicalOrderListData" row-key="id" style="width: 100%; height: 100%" highlight-current-row
        show-overflow-tooltip>
        <el-table-column label="类型" prop="name" min-width="100">
          <template #default="{ row }">
            <span>长期/西药</span>
          </template>
        </el-table-column>
        <el-table-column label="开始时间">
          <template #default="{ row }">
            <span>{{ row.dates[0].date }}</span>
          </template>
        </el-table-column>
        <el-table-column label="医嘱内容" prop="name" min-width="100">
          <template #default="{ row }">
            <span>{{ row.content }}</span>
          </template>
        </el-table-column>
        <el-table-column label="执行科室" prop="name" min-width="100">
          <template #default="{ row }">
            <span>{{ row.content }}</span>
          </template>
        </el-table-column>
        <el-table-column label="医生" prop="name" min-width="100">
          <template #default="{ row }">
            <span>{{ row.content }}</span>
          </template>
        </el-table-column>
        <el-table-column label="开立科室" prop="name" min-width="100">
          <template #default="{ row }">
            <span>{{ row.content }}</span>
          </template>
        </el-table-column>
        <el-table-column label="停止作废" prop="name" min-width="100">
          <template #default="{ row }">
            <span>{{ row.content }}</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script setup >
import { ref } from 'vue';

const medicalOrderListData = ref([
  {
    name: '',
    age: 10,
    gender: '男',
    content: '维生素B12注射液【1ml：0.5mg*1】  ',
    dates: [{
      date: '05/04',
      times: [{
        time: '08:00',
        nurse: '刘护士',
        checked: false
      }, {
        time: '12:00',
        nurse: '刘护士',
        checked: false
      }]
    }, {
      date: '05/05',
      times: [{
        time: '08:00',
        nurse: '刘护士',
        checked: false
      }, {
        time: '12:00',
        nurse: '刘护士',
        checked: false
      }]
    }]
  }, {
    name: '',
    age: 10,
    gender: '男',
    content: '维生素B12注射液【1ml：0.5mg*1】  ',
    dates: [{
      date: '05/04',
      times: [{
        time: '08:00',
        nurse: '刘护士',
        checked: false
      }, {
        time: '12:00',
        nurse: '刘护士',
        checked: false
      }]
    }, {
      date: '05/05',
      times: [{
        time: '08:00',
        nurse: '刘护士',
        checked: false
      }, {
        time: '12:00',
        nurse: '刘护士',
        checked: false
      }]
    }]
  }
  ,
  {
    name: '',
    age: 10,
    gender: '男',
    content: '维生素B12注射液【1ml：0.5mg*1】  ',
    dates: [{
      date: '05/04',
      times: [{
        time: '08:00',
        nurse: '刘护士',
        checked: false
      }, {
        time: '12:00',
        nurse: '刘护士',
        checked: false
      }]
    }, {
      date: '05/05',
      times: [{
        time: '08:00',
        nurse: '刘护士',
        checked: false
      }, {
        time: '12:00',
        nurse: '刘护士',
        checked: false
      }]
    }]
  }, {
    name: '',
    age: 10,
    gender: '男',
    content: '维生素B12注射液【1ml：0.5mg*1】  ',
    dates: [{
      date: '05/04',
      times: [{
        time: '08:00',
        nurse: '刘护士',
        checked: false
      }, {
        time: '12:00',
        nurse: '刘护士',
        checked: false
      }]
    }, {
      date: '05/05',
      times: [{
        time: '08:00',
        nurse: '刘护士',
        checked: false
      }, {
        time: '12:00',
        nurse: '刘护士',
        checked: false
      }]
    }]
  }
])
</script>
<style lang="scss" scoped>
.medicalOrderList-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  overflow: hidden;

  .search-container {
    flex: none;
    padding: 8px 8px;
    text-align: right;
  }

  .medicalOrderList-table {
    flex: 1;
  }
}
</style>