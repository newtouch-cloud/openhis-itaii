<template>
  <div class="basic-form">
    <div class="header">
      <div class="title">基础信息</div>
    </div>
    <div class="content">
      <div class="info-grid">
        <div class="info-item">
          <span class="label">患者姓名：</span>
          <span class="value">{{ formData.patientId }}</span>
        </div>
        <div class="info-item">
          <span class="label">患者编号：</span>
          <span class="value">{{ formData.name }}</span>
        </div>
        <div class="info-item">
          <span class="label">证件类型：</span>
          <span class="value">{{ formData.idType }}</span>
        </div>
        <div class="info-item">
          <span class="label">性别：</span>
          <span class="value">{{ formData.gender }}</span>
        </div>
        <div class="info-item">
          <span class="label">出生日期：</span>
          <span class="value">{{ formData.birthDate }}</span>
        </div>
        <div class="info-item">
          <span class="label">年龄：</span>
          <span class="value">{{ formData.age }}</span>
        </div>
        <div class="info-item">
          <span class="label">账户余额：</span>
          <span class="value">{{ formData.balance }}</span>
        </div>
        <div class="info-item">
          <span class="label">险种类型：</span>
          <span class="value">{{ formData.insuranceType }}</span>
        </div>
        <div class="info-item">
          <span class="label">人员类别：</span>
          <span class="value">{{ formData.personType }}</span>
        </div>
        <div class="info-item">
          <span class="label">参保区域：</span>
          <span class="value">{{ formData.insuredArea }}</span>
        </div>
        <div class="info-item">
          <span class="label">单位名称：</span>
          <span class="value">{{ formData.companyName }}</span>
        </div>
        <div class="info-item">
          <span class="label">原费别：</span>
          <span class="value">{{ formData.originalFeeType }}</span>
        </div>
      </div>
      <div class="footer">
        <el-button type="primary" style="width:100px" fixed>转自费</el-button>
      </div>
    </div>
  </div>
</template>
<script setup >
import { ref } from 'vue';

interface FormData {
  patientId: string;
  name: string;
  idType: string;
  idNumber: string;
  gender: string;
  birthDate: string;
  age: string;
  balance: string;
  insuranceType: string;
  personType: string;
  insuredArea: string;
  companyName: string;
  originalFeeType: string;
}

const formData = ref<FormData>({
  patientId: '12112',
  name: '张三',
  idType: '身份证',
  idNumber: '210381198702200034',
  gender: '男',
  birthDate: '1996/05/28',
  age: '10岁',
  balance: '2645.08',
  insuranceType: '商业保险',
  personType: '',
  insuredArea: '沈阳市',
  companyName: '哈药大厂',
  originalFeeType: '市医保',
});
</script>
<style lang="scss" scoped>
.basic-form {
  background: #fff;
  border-radius: 4px;
  padding: 0 16px 16px;

  .header {
    height: 32px;
    border-radius: 4px 4px 0px 0px;
    background: rgba(37, 109, 149, 0.05);
    display: flex;
    align-items: center;
    padding: 0 12px;
    margin: 0 -16px;

    .title {
      font-size: 16px;
      font-weight: 500;
      color: #256d95;
    }
  }

  .content {
    padding-top: 8px;

    .info-grid {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 8px;

      .info-item {
        display: flex;
        align-items: center;
        min-height: 32px;

        .label {
          width: 100px;
          text-align: right;
          color: #606266;
          padding-right: 12px;
          flex-shrink: 0;
        }

        .value {
          color: #333;
          flex: 1;
        }
      }
    }
  }

  .footer {
    margin-top: 24px;
    text-align: center;

    .el-button {
      width: 100px;
    }
  }
}
</style>
