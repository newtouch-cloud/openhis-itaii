<!--
 * @Author: hezhibo@bjgoodwill.com
 * @Date: 2025-03-07 16:10:45
 * @Description:
-->
<template>
  <el-dialog v-model="visible" top="6vh" width="930px" title="患者列表" :z-index="20">
    <el-table :data="patientList" style="width: 100%;" height="300px" show-overflow-tooltip>
      <el-table-column prop="deptNurseName" label="病区" width="100">
      </el-table-column>
      <el-table-column prop="name" label="姓名" width="80" />
      <el-table-column prop="sexName" label="性别" width="60" />
      <el-table-column prop="age" label="年龄" width="80">
      </el-table-column>
      <el-table-column prop="inpatientCode" label="住院号" min-width="120" />
      <el-table-column prop="code" label="结算状态" min-width="120" />
      <el-table-column prop="code" label="支付状态" min-width="120" />
      <el-table-column prop="tel" label="医疗总费用" min-width="120" />
      <el-table-column fixed="right" label="操作" width="88">
        <template #default="scope">
          <el-button type="primary" text @click="doRegistering(scope.row)">
            缴费
          </el-button>
          <el-button type="primary" text @click="doRegistering(scope.row)">
            结算
          </el-button>
          <span>费用未审核</span>
          <span>已完成</span>
        </template>
      </el-table-column>
    </el-table>
    <template v-slot:tool>
      <el-button class="margin-left-auto" @click="cancelAct">取消
      </el-button>
      <el-button type="primary" @click="handleSubmit">保存</el-button>
    </template>
  </el-dialog>
</template>
<script setup lang='ts'>
import { getCurrentInstance, onBeforeMount, onMounted, reactive, ref, watch, watchEffect } from 'vue'
// const { proxy } = getCurrentInstance();
const emits = defineEmits(["selectOK"])
const props = defineProps<{
  patientList?: Array<any>
}>();
const visible = defineModel('visible', { type: Boolean, default: false });
const newProps = reactive({ ...props });


const doRegistering = (row: any) => {
  emits("selectOK", row.patMiCode);
  //patMiCode 主索引
}
// onBeforeMount(() => {

// })
// onMounted(() => {

// })
/* 取消 */
const cancelAct = () => {
  console.log('121231');

}
/* 保存,登记 */
const handleSubmit = () => {
  console.log('121231');
}
defineExpose({})

</script>
<style lang="scss" scoped></style>