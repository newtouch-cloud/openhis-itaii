<template>
  <el-dialog
    v-model="visible"
    ref="returnStoreDialogAddRef"
    @ok="okActTh"
    @close="closedAct"
    width="388px"
    title="结算发票跳号"
  >
    <el-space direction="vertical" :size="24" class="rinvoiceSkipContainer" alignment="flex-start">
      <el-space>
        <span class="title">当前收据号:</span>
        <span>{{ 12312312312 }} </span>
        <el-tooltip placement="bottom" content="复制">
          <el-icon
            icon-class="hipCopy"
            height="23px"
            width="23px"
            class="svg-blue"
            @click="copyToClipboard"
          />
        </el-tooltip>
      </el-space>
      <el-space>
        <span class="title">调整至:</span>
        <el-input
          class="hzlb-width"
          v-model="newInvoiceNumber"
          clearable
          placeholder="格式如：xx00168x"
        ></el-input>

        <el-button @click="skip" class="confirm-button" style="margin-left: 10px">跳号</el-button>
      </el-space>
    </el-space>
    <template v-slot:tool>
      <el-button @click="close" class="margin-left-auto" type="plain">取消</el-button>
      <el-button @click="submitForm" class="confirm-button" type="primary">确定</el-button>
    </template>
  </el-dialog>
</template>
<script setup >
import { ElMessage } from 'element-plus'
import { onMounted, ref } from 'vue'

// const { proxy } = getCurrentInstance();
const emits = defineEmits([])
const props = defineProps({})
const visible = defineModel('visible', { type: Boolean, default: false })

function okActTh(row: any) {
  // if (thFlag.value === true || currentDataNumberPZH.value === '无启用收据') {
  //     ElMessage.warning('无在用收据或使用共享号段时不能操作跳号!');
  // }
}
const closedAct = () => {
  visible.value = false
}

/*  */
// const currentInvoiceNumber = ref('1000000000030001')//当前发票号
// 复制当前发票号
const copyToClipboard = async () => {
  try {
    // 使用 Clipboard API 复制文本
    await toClipboard(12312312312)
    ElMessage.success('复制成功') // Element Plus 的消息组件
  } catch (err) {
    ElMessage.error('复制失败')
    console.error('复制失败:', err)
  }
}
const close = () => {
  visible.value = false
}
const submitForm = () => {
  visible.value = false
}
const newInvoiceNumber = ref('')
const skip = () => {
  newInvoiceNumber.value = '1000000000030002'
}
// onBeforeMount(() => {

// })
onMounted(() => {})
defineExpose({})
</script>
<style lang="scss" scoped>
.rinvoiceSkipContainer {
  height: 142px;
  justify-content: center;

  .title {
    text-align: right;
    width: 100px;
  }
}
</style>
