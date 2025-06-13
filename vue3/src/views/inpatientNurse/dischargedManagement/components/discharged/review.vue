<template>
  <div class="review-container">
    <div class="search-container">
      <el-button>出院召回</el-button>
      <el-button>费用审核</el-button>
    </div>
    <div class="review-table">
      <el-tabs v-model="activeTabName" type="card" class="inOut-tabs">
        <el-tab-pane label="待处理执行单" name="first">
          <el-table :data="tableData" style="width: 100%">
            <el-table-column prop="date" label="类型" width="180" />
            <el-table-column prop="name" label="医嘱内容" width="180" />
            <el-table-column prop="address" label="开始/终止" />
            <el-table-column prop="address" label="预计执行" />
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="退费申请" name="second">
          <el-table :data="tableData" style="width: 100%">
            <el-table-column prop="date" label="退费申请科室" width="180" />
            <el-table-column prop="name" label="申请时间" width="180" />
            <el-table-column prop="address" label="项目名称【规格】" />
            <el-table-column prop="date" label="申请数量" width="180" />
            <el-table-column prop="date" label="单价" width="180" />
            <el-table-column prop="date" label="总金额" width="180" />
            <el-table-column prop="date" label="申请人" width="180" />
            <el-table-column prop="date" label="备注" width="180" />
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="待取药/待退药" name="third">
          <el-table :data="tableData" style="width: 100%">
            <el-table-column prop="date" label="类型（取/退）" width="180" />
            <el-table-column prop="name" label="发送科室" width="180" />
            <el-table-column prop="address" label="发送时间" />
            <el-table-column prop="address" label="药房" />
            <el-table-column prop="address" label="项目名称【规格】" />
            <el-table-column prop="address" label="单价" />
            <el-table-column prop="address" label="数量" />
            <el-table-column prop="address" label="金额" />
            <el-table-column prop="address" label="发送人" />
            <el-table-column prop="address" label="预计执行时间" />
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>
<script setup >
import { ref } from 'vue';
const activeTabName = ref('first');
const tableData = [
  {
    date: '2016-05-03',
    name: 'Tom',
    address: 'No. 189, Grove St, Los Angeles',
  },
  {
    date: '2016-05-02',
    name: 'Tom',
    address: 'No. 189, Grove St, Los Angeles',
  },
  {
    date: '2016-05-04',
    name: 'Tom',
    address: 'No. 189, Grove St, Los Angeles',
  },
  {
    date: '2016-05-01',
    name: 'Tom',
    address: 'No. 189, Grove St, Los Angeles',
  },
]
</script>
<style lang="scss" scoped>
.review-container {
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