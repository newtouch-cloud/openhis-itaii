<!--
 * @Author: hezhibo@bjgoodwill.com
 * @Date: 2025-02-25 14:46:43
 * @Description:
-->
<template>
  <el-dialog v-model="visible" ref="returnStoreDialogAddRef" @open="okActTh" @close="closedAct" width="388px"
    title="添加费用减免">
    <el-form :model="form" ref="derateRef" label-width="auto" style="max-width: 600px" :rules="rules">
      <el-form-item label="减免项目">
        <el-select v-model="form.region" placeholder="减免项目">
          <el-option label="全部" value="0" />
          <el-option :label="bpubType.elementName" value="bpubType.elementCode" v-for="bpubType in bpubTypeOption"
            :key="bpubType" />
        </el-select>
      </el-form-item>
      <el-form-item label="减免金额" placeholder="请输入减免金额" prop="amount">
        <el-input v-model="form.amount" @input="(value: any) => {
          // 第一步：去掉非数字和小数点的字符
          let cleanedValue = value.replace(/[^0-9.]/g, '')
          // 第二步：处理多余的小数点，只保留第一个小数点
          const parts = cleanedValue.split('.');
          if (parts.length > 2) {
            // 只保留第一个小数点及其后面的部分
            cleanedValue = parts[0] + '.' + parts.slice(1).join('');
          }
          // 第三步：限制小数位数为两位
          const dotIndex = cleanedValue.indexOf('.');
          if (dotIndex !== -1) {
            cleanedValue = cleanedValue.slice(0, dotIndex + 3);
          }
          form.amount = cleanedValue
        }" />
        <!-- (/[^\d{1,3}(,\d{3})*(\.\d{1,2})?$]/g, '') -->
      </el-form-item>
    </el-form>
    <template v-slot:tool>
      <el-button @click="close" class="margin-left-auto" type="plain">取消</el-button>
      <el-button @click="submitForm" class="confirm-button" type="primary">确定</el-button>
    </template>
  </el-dialog>
</template>
<script setup lang='ts'>
import { FormRules } from 'element-plus';
import { getCurrentInstance, onBeforeMount, onMounted, reactive, ref } from 'vue'
// const { proxy } = getCurrentInstance();
const emits = defineEmits(["derateOk"])
const props = defineProps({

})
const state = reactive({

})
const visible = defineModel('visible', { type: Boolean, default: false });
function okActTh(row: any) {
  // if (thFlag.value === true || currentDataNumberPZH.value === '无启用收据') {
  //     ElMessage.warning('无在用收据或使用共享号段时不能操作跳号!');
  // }
  derateRef.value.resetFields()
}
const closedAct = () => {
  visible.value = false;
};
const derateRef = ref()
const form = reactive({
  amount: 0,
  region: '0',
})

const bpubTypeOption = ref<Array<any>>([])
const rules = reactive<FormRules<any>>({
  amount: [
    { required: true, message: '请输入减免金额', trigger: 'blur' },
  ],
})
const close = () => {
  visible.value = false;
};
const submitForm = () => {
  if (!derateRef.value) return false
  derateRef.value.validate((valid: boolean) => {
    if (valid) {
      emits('derateOk', form.amount)
      return true
    } else {
      return false

    }
  })
};
onMounted(() => {

  derateRef.value && derateRef.value.resetFields()
})

defineExpose({ state })

</script>
<style lang="scss" scoped></style>