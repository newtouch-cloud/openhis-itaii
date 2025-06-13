<template>
  <el-dialog
    v-model="visible"
    top="6vh"
    :width="width"
    title="入科选床"
    @open="openAct"
    @closed="closedAct"
    :z-index="20"
  >
    <div class="transferIn-container">
      <el-form :model="interventionForm" :rules="rules" ref="interventionFormRef">
        <div class="admission-information">
          <el-row>
            <el-col :span="8">
              <el-form-item label="入院科室" label-width="100px">
                <el-input
                  class="w-p100"
                  clearable
                  disabled
                  v-model="interventionForm.inDeptName"
                  placeholder="请输入"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="入院病区" label-width="100px">
                <el-input
                  class="w-p100"
                  v-model="interventionForm.inNurseDeptName"
                  disabled
                  clearable
                  placeholder="请输入"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="入住床位" label-width="100px">
                <el-input
                  class="w-p100"
                  v-model="interventionForm.bedName"
                  disabled
                  clearable
                  placeholder="请输入"
                ></el-input>
              </el-form-item>
            </el-col>

            <el-col :span="24">
              <el-scrollbar height="400px" class="beds">
                <BedCards />
              </el-scrollbar>
            </el-col>
            <el-col :span="8">
              <el-form-item label="住院医生" label-width="100px" prop="admittedDoctor">
                <el-select
                  v-model="interventionForm.admittedDoctor"
                  placeholder="Select"
                  style="width: 240px"
                >
                  <el-option key="1" label="医生甲" value="医生甲" />
                  <el-option key="2" label="医生乙" value="医生乙" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="主治医生" label-width="100px">
                <el-select
                  v-model="interventionForm.masterDoctor"
                  placeholder="Select"
                  style="width: 240px"
                >
                  <el-option key="1" label="医生甲" value="医生甲" />
                  <el-option key="2" label="医生乙" value="医生乙" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="主任医生" label-width="100px">
                <el-select
                  v-model="interventionForm.directorDoctor"
                  placeholder="Select"
                  style="width: 240px"
                >
                  <el-option key="1" label="医生甲" value="医生甲" />
                  <el-option key="2" label="医生乙" value="医生乙" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="入科时间" label-width="100px">
                <el-date-picker
                  class="w-p100"
                  v-model="interventionForm.inDeptDate"
                  type="datetime"
                  value-format="YYYY-MM-DD HH:mm:ss"
                  placeholder="请输入"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="责任护士" label-width="100px" prop="masterNurse">
                <el-select
                  v-model="interventionForm.masterNurse"
                  placeholder="Select"
                  style="width: 240px"
                >
                  <el-option key="1" label="护士甲" value="护士甲" />
                  <el-option key="2" label="护士乙" value="护士乙" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="患者病情" label-width="100px">
                <el-select
                  v-model="interventionForm.bingqing"
                  placeholder="Select"
                  style="width: 240px"
                >
                  <el-option key="1" label="护士甲" value="护士甲" />
                  <el-option key="2" label="护士乙" value="护士乙" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-form>
    </div>
    <template #footer>
      <!-- <div class="transferInDialog-footer"> -->
      <div class="isPrintWristband">
        <el-checkbox v-model="printWristband">打印腕带</el-checkbox>
      </div>
      <el-button class="margin-left-auto" @click="cancelAct">取消 </el-button>
      <el-button type="primary" @click="handleSubmit(interventionFormRef)">入科</el-button>
      <!-- </div> -->
    </template>
  </el-dialog>
</template>
<script  setup>
import { reactive, ref, onMounted, inject, type Ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { dayjs, ElMessage } from 'element-plus'
import type { IInPatient } from '@/model/IInPatient'

import { BedCards } from './index'
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
const interventionForm = ref({
  // id: undefined, // 标识
  changeDeptOutId: '', // 住院患者转科出科标识
  visitCode: '', // 	就诊流水号
  patCode: '', // 主索引
  inDept: '', // 入科科室
  inDeptName: '', // 入科科室名称
  inNurseDept: '', // 入科护理组
  inNurseDeptName: '', // 入科护理组名称
  bedId: '', // 床位服务信息标识
  bedName: '', // 床号
  admittedDoctor: '', // 住院医师
  admittedDoctorName: '', // 住院医师名称
  masterDoctor: '', // 主治医师
  masterDoctorName: '', // 主治医师名称
  directorDoctor: '', // 主任医师
  directorDoctorName: '', // 主任医师名称
  masterNurse: '', // 责任护士
  masterNurseName: '', // 责任护士名称
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
const props = defineProps<{
  bedInfo?: any
}>()
const visible = defineModel('visible')
const width = '950px'
/* 取消 */
const cancelAct = () => {
  visible.value = false
}
/* 入科 */
const interventionFormRef = ref()
const handleSubmit = async (formEl: FormInstance | undefined) => {
  // TODO 登记入科
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      try {
        // 登记入科方法(interventionForm.value).then((res: any) => {
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

  .admission-information {
    width: 896px;
    .unit {
      display: inline-block;
      margin-left: 10px;
      color: #bbb;
      font-weight: 400;
      font-size: 14px;
      font-family: '思源黑体 CN';
    }
  }
  .beds {
    margin-bottom: 8px;
  }
}

.isPrintWristband {
  float: left;
  display: inline-block;
}
.w-p100 {
  width: 100%;
}

.w-80 {
  width: 80px;
}
</style>
