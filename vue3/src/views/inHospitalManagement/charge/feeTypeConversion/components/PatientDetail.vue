<template>
  <div class="pat-info">
    <!-- 患者信息 -->
    <el-space direction="vertical" alignment="flex-start">
      <el-space>
        <div>
          100床
        </div>
        <div>
          头像
        </div>
        <div>
          任素汐
        </div>
        <div>
          女/24 岁
        </div>
        <div>
          自费
        </div>
        <div>
          住院:29 天
        </div>
        <div>
          入科:2025-04-04
        </div>
        <div>
          住院号:0023445454
        </div>
        <div>
          诊断:人工流产
        </div>
      </el-space>
      <el-space>
        <div>
          过明历史：鸡蛋过敏
        </div>
        <div>
          低保人员
        </div>
        <div>
          主治医生：张三
        </div>
        <div>
          总额：56545.89
        </div>
        <div>
          预交金：12312312.9
        </div>
        <div>
          余额：12312312.9
        </div>
      </el-space>
    </el-space>
  </div>
</template>

<script setup >
import { onMounted, ref } from 'vue';

const visitCode = ref('');

onMounted(() => {
  visitCode.value = 'IPD0000188';
})

function updatePatientInfo(params: string) {
  console.log('updatePatientInfo', updatePatientInfo);
}

defineProps<{
  patientInfo: any;
}>();
</script>

<style lang="scss" scoped>
.pat-info {
  width: 100%;
  align-items: center;
  padding: 8px;
  // background: #c12626;
}
</style>
