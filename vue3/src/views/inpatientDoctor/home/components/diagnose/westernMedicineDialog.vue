<template>
  <el-dialog
    v-model="visible"
    :width="width"
    title="西医诊断"
    @open="openAct"
    @closed="closedAct"
    :z-index="20"
  >
    <el-form :inline="true" :model="diagnoseform" class="demo-form-inline" label-width="auto">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="诊断名称" style="width: 100%">
            <el-input v-model="diagnoseform.user" placeholder="诊断名称" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="诊断类型" style="width: 100%">
            <el-select
              v-model="diagnoseform.user"
              placeholder="诊断类型"
              clearable
              style="width: 100%"
            >
              <el-option label="Zone one" value="shanghai" />
              <el-option label="Zone two" value="beijing" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="前缀" style="width: 100%">
            <el-select v-model="diagnoseform.user" placeholder="前缀" clearable style="width: 100%">
              <el-option label="Zone one" value="shanghai" />
              <el-option label="Zone two" value="beijing" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="后缀" style="width: 100%">
            <el-select v-model="diagnoseform.user" placeholder="后缀" clearable style="width: 100%">
              <el-option label="Zone one" value="shanghai" />
              <el-option label="Zone two" value="beijing" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="ICD" style="width: 100%">
            <el-input v-model="diagnoseform.user" placeholder="ICD" clearable style="width: 100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="备注" style="width: 100%">
            <el-input
              v-model="diagnoseform.user"
              placeholder="备注"
              clearable
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="入院病情" style="width: 100%">
            <el-select
              v-model="diagnoseform.user"
              placeholder="入院病情"
              clearable
              style="width: 100%"
            >
              <el-option label="Zone one" value="shanghai" />
              <el-option label="Zone two" value="beijing" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="转归" style="width: 100%">
            <el-select v-model="diagnoseform.user" placeholder="转归" clearable style="width: 100%">
              <el-option label="Zone one" value="shanghai" />
              <el-option label="Zone two" value="beijing" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="转归日期" style="width: 100%">
            <el-date-picker
              v-model="diagnoseform.user"
              type="date"
              placeholder="转归日期"
              clearable
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12"> </el-col>
        <el-col :span="6">
          <el-form-item label="主诊断" style="width: 100%">
            <el-checkbox v-model="diagnoseform.checked1" label="" size="large" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="疑似诊断">
            <el-checkbox v-model="diagnoseform.checked1" label="" size="large" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="复诊">
            <el-checkbox v-model="diagnoseform.checked1" label="" size="large" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="子诊断">
            <el-checkbox v-model="diagnoseform.checked1" label="" size="large" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button size="fixed" class="margin-left-auto" @click="cancelAct">取消 </el-button>
      <el-button size="fixed" type="primary" @click="handleSubmit(signFormRef)">保存</el-button>
    </template>
  </el-dialog>
</template>
<script  setup>
import { reactive, ref, onMounted, inject } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { dayjs, ElMessage } from 'element-plus'
import type { IInPatient } from '@/model/IInPatient'

const currentInPatient = ref<Partial<IInPatient>>({})
const initCurrentInPatient = () => {
  currentInPatient.value = {
    feeType: '08',
    sexName: '男',
    age: '0',
  }
}
/* 初始化数据 */
const init = () => {
  initCurrentInPatient()
}

/* 入科 */
const diagnoseform = ref({
  user: '',
  checked1: '',
})
const rules = reactive<FormRules>({
  admittedDoctor: [{ required: true, message: '请选择住院医生', trigger: ['blur', 'change'] }],
  masterNurse: [{ required: true, message: '请选择责任护士', trigger: ['blur', 'change'] }],
})
const printWristband = ref(false)
const emits = defineEmits(['okAct'])

const visible = defineModel('visible')
const width = '600px'

/* 取消 */
const cancelAct = () => {
  visible.value = false
}
/* 录入患者体征*/
const signFormRef = ref()
const handleSubmit = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      console.log('submit!')
      try {
        // 录入患者体征方法(signForm.value).then((res: any) => {
        //   ElMessage({
        //     message: '登记成功！',
        //     type: 'success',
        //     grouping: true,
        //     showClose: true,
        //   })
        //   emits('okAct')
        // })
      } catch (error) {
        console.log(error)
      }
    }
  })
}
const openAct = () => {
  init()
}
const closedAct = () => {
  visible.value = false
}
onMounted(() => {})
</script>
<style lang="scss" scoped>
.transferIn-container {
  width: 100%;

  .admission-signs,
  .admission-information {
    width: 888px;
    .unit {
      display: inline-block;
      margin-left: 10px;
      color: #bbb;
      font-weight: 400;
      font-size: 14px;
      font-family: '思源黑体 CN';
    }
  }
}

.print-wriBtn {
  margin-left: 565px;
}

.w-p100 {
  width: 100%;
}

.w-80 {
  width: 80px;
}

.mb-90 {
  margin-bottom: 90px !important;
}
</style>
