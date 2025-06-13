<template>
  <el-dialog v-model="visible" top="6vh" :width="width" title="体征录入" @open="openAct" @closed="closedAct" :z-index="20">
    <div class="transferIn-container">
      <div class="beds"></div>
      <el-form :model="signForm" :rules="rules" ref="signFormRef">
        <div class="admission-signs">
          <el-row>
            <el-col :span="8">
              <el-form-item label="身高" label-width="50px">
                <el-input-number style="width: 160px" clearable v-model="signForm.height" placeholder="请输入" :min="0"
                  :max="999"></el-input-number><span class="unit">cm</span>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="体重" label-width="50px">
                <el-input-number style="width: 160px" v-model="signForm.weight" clearable placeholder="请输入" :min="0"
                  :max="999"></el-input-number>
                <span class="unit">kg</span>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="体温" label-width="50px">
                <el-input-number style="width: 160px" v-model="signForm.temperature" clearable placeholder="请输入"
                  :min="0" :max="99"></el-input-number>
                <span class="unit">℃</span>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="心率" label-width="50px">
                <el-input-number style="width: 160px" v-model="signForm.hertRate" clearable placeholder="请输入" :min="0"
                  :max="999"></el-input-number>
                <span class="unit">BPM</span>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="脉搏" label-width="50px">
                <el-input-number style="width: 160px" v-model="signForm.pulse" clearable placeholder="请输入" :min="0"
                  :max="999"></el-input-number><span class="unit">P</span>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="血压" label-width="50px">
                <el-input-number style="width: 72px" v-model="signForm.endBloodPressure" clearable placeholder="请输入"
                  :min="0" :max="999"></el-input-number>
                <span style="display: inline-block; width: 8px; margin: 0 4px"> ~ </span>

                <el-input-number style="width: 72px" v-model="signForm.highBloodPressure" clearable placeholder="请输入"
                  :min="0" :max="999"></el-input-number>
                <span class="unit">mmHg</span>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-form>
    </div>
    <template #footer>
      <el-button class="margin-left-auto" @click="cancelAct">取消 </el-button>
      <el-button type="primary" @click="handleSubmit(signFormRef)">保存</el-button>
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
const signForm = ref({
  visitCode: '', // 	就诊流水号
  height: 0, // 身高
  weight: 0, // 体重
  temperature: 0, // 体温
  hertRate: 0, // 心率
  pulse: 0, // 	脉搏
  highBloodPressure: 0, // 收缩压
  endBloodPressure: 0, // 舒张压
  loginDeptCode: '', // 	当前登录科室
  bingqing: '', //患者病情
  inDeptDate: dayjs(new Date()).format('YYYY-MM-DD HH:mm:ss'), //入院时间
  signsId: '',
})
const rules = reactive<FormRules>({
  admittedDoctor: [{ required: true, message: '请选择住院医生', trigger: ['blur', 'change'] }],
  masterNurse: [{ required: true, message: '请选择责任护士', trigger: ['blur', 'change'] }],
})
const printWristband = ref(false)
const emits = defineEmits(['okAct'])

const visible = defineModel('visible')
const width = '920px'

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
onMounted(() => { })
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
