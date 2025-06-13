<!--
 * @Author: sjjh
 * @Date: 2025-04-07 20:42:45
 * @Description:住院患者信息，给医生用，带折叠
-->
<template>
  <div class="inPatientBarDoctorFold-container">
    <div class="basic_info">
      <div class="patient-header white-bg">
        <div class="select_wrapper_div">
          <b class="bedNumber" style="margin-left: 12px">{{ patientInfo.bedName }}</b>
          <label class="content-text-color" style="margin-left: 12px; color: #a15209"
            >{{ patientInfo.name
            }}<span class="sex-age">{{ patientInfo.sexName }}/{{ patientInfo.age }}</span></label
          >
          <div style="margin-left: 16px" v-if="patientInfo?.nursingLevel">
            <!-- <care-tag v-model="patientInfo.nursingLevel"></care-tag> -->
            {{ patientInfo.nursingLevel }}
            <!-- //护理等级 -->
          </div>
          <div style="display: flex; margin-left: 8px">
            <!-- 状态展示// TODO 后端给状态，前段 -->
            <ball-tag
              style="margin-right: 4px"
              v-for="item in patientInfo.list"
              :key="item"
              :tagId="item"
            ></ball-tag>
          </div>
          <div
            class="gray-border"
            v-show="patientInfo?.feeTypeName && patientInfo.feeTypeName !== ''"
          >
            {{ patientInfo.feeTypeName }}
          </div>
          <label style="margin-left: 24px"
            ><span class="label-text-color">住院：</span
            ><span class="content-text-color">{{ patientInfo.inDays }}</span></label
          >
          <label style="margin-left: 24px"
            ><span class="label-text-color">入科：</span
            ><span class="content-text-color">{{ patientInfo.inDeptDate }}</span></label
          >
          <label style="margin-left: 24px"
            ><span class="label-text-color">入院时间：</span
            ><span class="content-text-color">{{ patientInfo.inTime }}</span></label
          >
          <label style="margin-left: 24px"
            ><span class="label-text-color">住院号:{{ patientInfo.inpatientCode }}</span>
          </label>
          <svg-icon icon-class="hipCopy" height="20px" width="20px" class="copy-svg" />
          <label style="margin-left: 30px"
            ><span class="label-text-color">诊断：</span
            ><span class="content-text-color">{{ patientInfo.impDiagnosis }}</span></label
          >
          <div style="margin-left: auto">
            <el-icon v-if="expand" @click="toggleExpand"><ArrowUpBold /></el-icon>
            <el-icon v-else @click="toggleExpand"><ArrowDownBold /></el-icon>
          </div>
        </div>
        <!-- <el-divider v-if="showDividers" style="margin: 0"></el-divider> -->
      </div>
    </div>
    <div v-if="expand" class="expand_more">
      <div style="background-color: #ffffff">
        <div style="margin-top: -10px">
          <label style="font-size: 14px">
            <span class="primary-text">过敏：</span>
            <span class="primary-text">{{ patientInfo.allergies || '无过敏史' }}</span>
          </label>
          <label style="font-size: 14px; margin-left: 32px" v-show="patientInfo.insuplcAdmdvsName">
            <span class="primary-text">医保统筹区:</span>
            <span class="primary-text">{{ patientInfo.insuplcAdmdvsName }}</span>
          </label>

          <label style="font-size: 14px; margin-left: 32px" v-show="patientInfo.ciType">
            <span class="primary-text">商保信息：</span>
            <span class="primary-text">{{ patientInfo.ciType }}</span>
          </label>
          <div style="display: flex; flex-wrap: nowrap; margin-top: 8px; white-space: nowrap">
            <div
              class="blue-bg"
              style="background-color: #f1faff; flex-shrink: 0; min-width: fit-content"
            >
              <span class="content-text-color">
                {{
                  patientInfo.height && patientInfo.weight
                    ? `${patientInfo.height}cm/${patientInfo.weight}kg`
                    : '身高/体重'
                }}
              </span>
            </div>
            <div
              class="blue-bg"
              style="
                margin-left: 24px;
                background-color: #f1faff;
                flex-shrink: 0;
                min-width: fit-content;
              "
              v-show="patientInfo.postoperativeDays"
            >
              <span class="content-text-color">术后{{ patientInfo.postoperativeDays }}天</span>
            </div>
            <div
              class="blue-bg"
              style="
                margin-left: 16px;
                background-color: #f1faff;
                flex-shrink: 0;
                min-width: fit-content;
              "
              v-show="patientInfo.poorTypeName"
            >
              <span class="label-text-color">贫困类型:</span>
              <span class="content-text-color" style="margin-left: 4px">{{
                patientInfo.poorTypeName
              }}</span>
            </div>

            <div
              class="blue-bg"
              style="
                margin-left: 16px;
                background-color: #f1faff;
                flex-shrink: 0;
                min-width: fit-content;
              "
              v-show="patientInfo.pathwayName"
            >
              <span class="label-text-color">路径情况:</span>
              <span class="content-text-color" style="margin-left: 4px">{{
                patientInfo.pathwayName
              }}</span>
            </div>
          </div>
        </div>
      </div>
      <div style="background-color: #ffffff">
        <div style="margin-top: -10px">
          <div class="patient-board">
            <div class="item-center">
              <div class="line-block">
                <div class="line-block-top">
                  <span class="label-text-color">科室：</span>
                  <span class="content-text-color">{{ patientInfo.admissionDeptName }}</span>
                </div>
                <div class="line-block-bottom">
                  <span class="label-text-color">病区：</span>
                  <span class="content-text-color">{{ patientInfo.deptNurseName }}</span>
                </div>
              </div>
              <div class="line-block">
                <div class="line-block-top">
                  <span class="label-text-color">主治医生：</span>
                  <span class="content-text-color">{{ patientInfo.masterDoctorName }}</span>
                </div>
                <div class="line-block-bottom">
                  <span class="label-text-color">责任护士：</span>
                  <span class="content-text-color">{{ patientInfo.masterNurseName }}</span>
                </div>
              </div>
              <div class="line-blockMoney">
                <div class="line-blockMoney-top">
                  <span class="label-text-color">费用</span>
                </div>
                <div class="line-blockMoney-bottom">
                  <b class="money-content size-15">{{
                    patientInfo.totalAmount ? patientInfo.totalAmount : 0
                  }}</b>
                </div>
              </div>
              <div class="line-blockMoney">
                <div class="line-blockMoney-top">
                  <span class="label-text-color">预交金</span>
                </div>
                <div class="line-blockMoney-bottom">
                  <b class="money-content size-15">{{
                    patientInfo.prepayAmount ? patientInfo.prepayAmount : 0
                  }}</b>
                </div>
              </div>
              <div class="line-blockMoney">
                <div class="line-blockMoney-top">
                  <span class="label-text-color">余额</span>
                </div>
                <div class="line-blockMoney-bottom">
                  <b class="money-content size-15">{{
                    patientInfo.balance ? patientInfo.balance : 0
                  }}</b>
                </div>
              </div>
            </div>
          </div>
        </div>
        <hip-dividers></hip-dividers>
      </div>
    </div>
  </div>
</template>
<script  setup>
import { computed, onMounted, ref, watch } from 'vue'
import BallTag from './components/BallTag.vue'
// import { ElMessage } from 'element-plus'

const expand = ref(false)
const fakePatientInfo = ref({
  care: '2',
  balls: ['0', '1', '2', '3', '4'],
  number: '0023445454',
})

const iconClassPhoto = computed(() => {
  // 获取 sex 和 age 的值
  const sexValue = patientInfo.value.sexName
  const ageValue = patientInfo.value.age

  // 确保 ageValue 是一个字符串，如果不是，尝试将其转换为字符串
  let ageString = ''

  if (typeof ageValue === 'string') {
    ageString = ageValue
  } else if (typeof ageValue !== 'undefined') {
    ageString = ageValue.toString() // 尝试将 ageValue 转换为字符串
  }

  // 匹配年龄的正则表达式，包括 "31岁" 和 "x" 的情况
  const ageMatch = ageString.match(/^(\d+)/)
  const ageNumber = ageMatch ? parseInt(ageMatch[1], 10) : NaN

  // 如果 ageNumber 是有效的，进行分类
  if (!isNaN(ageNumber)) {
    // 普通数字年龄分类
    if (ageNumber < 3) {
      return sexValue === '男' ? 'headMaleBaby' : 'headFemaleBaby' // 婴儿
    } else if (ageNumber <= 14) {
      return sexValue === '男' ? 'headMaleChild' : 'headFemaleChild' // 儿童
    } else if (ageNumber <= 28) {
      return sexValue === '男' ? 'headMaleYoung' : 'headFemaleYoung' // 青年
    } else if (ageNumber <= 44) {
      return sexValue === '男' ? 'headMaleMiddleAged' : 'headFemaleMiddleAged' // 中年
    } else {
      return sexValue === '男' ? 'headMaleElder' : 'headFemaleElder' // 老年
    }
  }

  // 如果 ageString 不是有效的年龄格式，返回未知
  return 'headUnknown'
})
const patientInfo = ref({
  visitCode: 'IPD0000107',
  inpatientCode: '20240000068',
  name: '金沙县',
  birthDate: '2018-08-19 00:00:00',
  patCode: '202400000032',
  sex: '1',
  sexName: '男',
  age: '6岁',
  tel: null,
  feeType: null,
  feeTypeName: null,
  relation: null,
  allergies: null,
  ambDoctor: null,
  ambDiagnosis: null,
  impDiagnosis: null,
  dischargeDiagnosis: null,
  nursingLevel: null,
  height: 1.0,
  weight: 1.0,
  temperature: null,
  heartRate: null,
  pulse: null,
  highBloodPressure: null,
  endBloodPressure: null,
  admissionDeptCode: '2302',
  admissionDeptName: '儿科住院',
  deptNurseCode: '2302',
  deptNurseName: '儿科',
  bedId: '2',
  bedName: '2床',
  admittedDoctor: '000239',
  admittedDoctorName: null,
  masterDoctor: '000239',
  masterDoctorName: null,
  directorDoctor: '000239',
  directorDoctorName: null,
  masterNurse: '0005',
  masterNurseName: null,
  condition: null,
  conditionName: null,
  totalAmount: 0,
  prepayAmount: 0,
  balance: 0,
  statusCode: 'RECEIVED',
  inTime: '2024年12月16日 00:00',
  outTime: null,
  postoperativeDays: null,
  inDays: '112天',
  poorType: null,
  poorTypeName: null,
  pathwayFlag: null,
  pathwayName: '类风湿性关节炎',
  inDeptDate: null,
  list: ['预警'],
  ciType: null,
  insuplcAdmdvsName: null,
})
// 定义一个布尔变量来控制是否显示 hip-dividers
const showDividers = ref(true)

// 示例方法：切换显示状态
const toggleDividers = () => {
  showDividers.value = !showDividers.value
}
const iconClass = ref('hipBarDown')

// 切换展开状态的方法
function toggleExpand() {
  expand.value = !expand.value
  iconClass.value = expand.value ? 'hipBarUp' : 'hipBarDown'
  toggleDividers()
}

const fetchPatientInfoById = async (patientId) => {
  // 查询患者信息
  console.log(patientId)
}

const props = defineProps(
  {
    visitCode: '',
  },
)
watch(
  () => props.visitCode,
  (val) => {
    if (val !== null && val !== '') {
      fetchPatientInfoById(val)
    }
  },
)

defineOptions({
  name: 'NurserDoctorPatientBarminimal',
})

// onMounted(function () {
//   // fetchPatientInfoById('IPD0000063')
//   fetchPatientInfoById(props.visitCode)
// })
</script>

<style lang="scss" scoped>
.inPatientBarDoctorFold-container {
  border-bottom: 1px solid #ebeef5;
  .basic_info {
    height: 43px;
    padding: 0 8px;
  }
  /* expand_more */
  .expand_more {
    width: 100%;
    height: 56px;
    display: flex;
    justify-content: space-between;
    padding: 0 8px;
  }
  .patient-header {
    width: 100%;
    padding: 6px 0;
    font-size: 13px;

    .sex-age {
      margin-left: 20px;
      color: var(--hip-color-text-description);
      font-size: 14px;
    }

    .gray-border {
      display: inline-flex;
      color: var(--hip-color-text-description);
      justify-content: center;
      align-items: center;
      border: 1px solid var(--hip-color-text-description);
      border-radius: 20px;
      padding: 0px 8px;
    }

    .copy-svg {
      fill: var(--hip-color-primary);
      cursor: pointer;
      margin-left: 4px;
    }
  }

  .size-15 {
    font-size: 15px;
  }

  .bedNumber {
    font-weight: bold;
    font-size: 18px;
  }

  .primary-text {
    color: var(--hip-color-primary);
  }

  .flex-between {
    display: flex;
    justify-content: space-between;
  }

  .item-center {
    display: flex;
    align-items: center;
  }

  .flex-row {
    display: flex;
  }

  .patient-board {
    margin-left: 20px;

    .line-block {
      position: relative;
      min-width: 73px;
      padding-left: 15px;
      padding-right: 10px;

      &::before {
        content: '';
        position: absolute;
        width: 1px;
        height: 50%;
        top: 25%;
        left: 0;
        background-color: #e1e1e1;
      }

      &-top {
        margin-bottom: 8px;
      }

      .money-content {
        color: #ff8616;
        font-size: 20px;
        font-weight: 500;
      }
    }

    .line-blockMoney {
      position: relative;
      min-width: 73px;
      padding-left: 15px;
      padding-right: 10px;

      &::before {
        content: '';
        position: absolute;
        width: 1px;
        height: 50%;
        top: 25%;
        left: 0;
        background-color: #e1e1e1;
      }

      &-top {
        margin-bottom: 0px;
      }

      .money-content {
        color: #ff8616;
        font-size: 20px;
        font-weight: 500;
      }
    }

    :deep(.color-content) {
      background-color: rgba(252, 252, 252, 0.8);
    }
  }

  .white-bg {
    background-color: #ffffff;
  }

  .blue-bg {
    display: inline-flex;
    justify-content: center;
    align-items: center;
    background-color: #f1faff;
    padding-left: 8px;
    padding-right: 8px;
    border-radius: 4px; /*圆角*/
  }

  .label-text-color {
    font-size: 14px;
  }

  .content-text-color {
    font-size: 14px;
    color: #666666;
  }
}
</style>
