<template>
  <div class="patient-list">
    <el-table
      :data="data"
      highlight-current-row
      show-overflow-tooltip
      @current-change="handleCurrentChange"
      height="100%"
    >
      <el-table-column label="住院号" prop="inpatientCode" />
      <el-table-column label="姓名" prop="name" min-width="90px" />
      <el-table-column label="年龄" prop="age" width="60px" />
      <el-table-column label="费别" prop="feeType" width="60px" />
      <el-table-column label="病区" prop="nurseDeptName" width="60px" />
    </el-table>
  </div>
</template>

<script setup >
defineProps<{
  data: any[];
}>();

const emit = defineEmits(['current-change']);

const handleCurrentChange = (row: any) => {
  emit('current-change', row);
};
</script>

<style lang="scss" scoped>
.patient-list {
  height: 100%;
  box-sizing: border-box;
  .el-table {
    height: 100%;
  }
  .el-table__row {
    cursor: pointer;

    &:hover > td {
      background-color: #f5f7fa !important;
    }

    &.current-row > td {
      background-color: #dcf8f6 !important;
      color: #256d95;
      &:first-child {
        &::before {
          content: '';
          display: block;
          position: absolute;
          width: 2px;
          height: 12px;
          top: 50%;
          transform: translateY(-50%);
          background-color: #00bbaa;
        }
      }
    }
  }
}
</style>
