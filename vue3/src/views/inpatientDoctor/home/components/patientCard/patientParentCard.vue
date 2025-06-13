<!--
 * @Author: yangbo@bjgoodwill.com
 * @Date: 2024-10-23 09:39:24
 * @Description: 患者卡片-父节点
-->
<template>
  <div
    @click="clickAct"
    class="patient-card"
    :class="{
      actived: activeId === data.id,
    }"
  >
    <!-- //跨科 考虑不考虑 -->

    <div class="main-info-container">
      <div class="bed-container">
        <!-- 患者床号 -->
        <div class="bed">
          <el-text truncated :tclass="bedfont" width="auto">{{ data.bedName }}</el-text>
        </div>
        <!-- 新入院患者标志 -->
        <div class="bed_new" />
      </div>

      <div class="indepatient-code-container">
        <!-- 患者重/危标识符 -->
        <div
          class="sign"
          :style="{
            backgroundColor: data?.criticalCarePatientName === '危' ? '#BA7BC8' : '#E95657',
          }"
        >
          {{ data.criticalCarePatientName }}
        </div>
        <!-- 住院号 -->
        <span style="margin-left: 4px"> {{ data.inpatientCode }} </span>
      </div>
    </div>
    <div class="doctor-parent-line" />
    <div class="personal-info-container">
      <div class="name-container">
        <!-- 患者姓名 -->
        <div class="name" style="max-width: 70px">
          <el-text :text="data.name" tclass="name" width="auto">
            {{ data.name }}
          </el-text>
        </div>
        <!-- 患者性别/年龄 -->
        <div class="age">{{ data.sexName }}/{{ data.age }}</div>
      </div>
    </div>

    <div class="dept">
      <div class="doctor">
        <el-icon size="20px">
          <UserFilled />
        </el-icon>
        <!-- // TODO 医生图标 -->
        <div class="doctor_name">{{ data.admittedDoctorName }}</div>
      </div>
      <div class="deptNurseName">
        <el-icon size="20px">
          <Avatar />
        </el-icon>
        <!-- // TODO 护士图标 -->
        {{ data.deptNurseName }}
      </div>
    </div>
  </div>
</template>
<script  setup>
defineOptions({
  name: 'PatientParentCard',
})

const bedfont = 'bed-font'

const props = defineProps({
  data: {},
  // type：0:在科、1:出院、2:转科、3:会诊、
  type: 0,
  activeId: ''
})
const emits = defineEmits(['click'])
const clickAct = () => {
  emits('click', props.data)
}
</script>

<style lang="scss" scoped>
.patient-card {
  width: 100%;
  overflow: hidden;
  background-color: #fff;
  border: 1px solid;
  border-color: #eee;
  border-radius: 4px;
  box-shadow: 0 2px 2px 0 rgba(57.55, 69.04, 86.28, 20%);
  cursor: pointer;

  &.actived {
    background-color: rgb(7, 155, 140, 5%);
    border-color: var(--el-color-primary);
  }

  .cross-dept {
    height: 24px;
    padding: 0 16px;
    color: #fff;
    font-size: 14px;
    line-height: 24px;
    background-color: #256d95;
  }

  .main-info-container {
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: 24px;
    margin: 7px 0;
    padding: 0 16px;

    .bed-container {
      display: flex;
      flex: 1;
      align-items: center;
      min-width: 0;

      .bed {
        flex-grow: 0;
        flex-shrink: 1;
        min-width: 0;

        :deep(.bed-font) {
          color: #333;
          font-weight: 600;
          font-size: 16px;
        }
      }

      .bed_new {
        flex-shrink: 0;
        width: 10px;
        height: 10px;
        margin-left: 4px;
        background: #29af6f;
        border-radius: 50%;
      }
    }

    .indepatient-code-container {
      display: flex;
      flex-shrink: 0;
      align-items: center;
      padding-left: 6px;
      color: #666;
      font-size: 14px;

      .sign {
        width: 24px;
        height: 24px;
        color: white;
        line-height: 24px;
        text-align: center;
        border-radius: 50%;
        user-select: none;
      }
    }
  }

  .doctor-parent-line {
    margin: 0 16px;
    border-bottom: 1px dashed #ddd;
  }

  .personal-info-container {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin: 10px 0;
    padding: 0 16px;

    .name-container {
      display: flex;
      align-items: center;
      height: 24px;

      .name {
        color: #333;
        font-size: 16px;
      }

      .age {
        margin-left: 10px;
        color: #666;
        font-size: 14px;
      }
    }

    .change-department {
      width: 58px;
      height: 24px;
      color: #5585e3;
      font-size: 14px;
      line-height: 24px;
      text-align: center;
      background: #e6edfb;
      border-radius: 4px;
    }
  }

  .dept {
    margin-bottom: 4px;
    padding: 0 16px;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .doctor {
      display: flex;
      align-items: center;
      height: 32px;
      line-height: 32px;

      .doctor_name {
        display: flex;
        align-items: center;
        margin-left: 4px;
        color: #333;
      }
    }

    .deptNurseName {
      display: flex;
      align-items: center;
      height: 32px;
      color: #256d95;
      line-height: 32px;
    }
  }
}
</style>
