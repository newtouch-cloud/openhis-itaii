<template>
  <div class="insurance-form">
    <div class="header">
      <div class="title">医保登记</div>
    </div>
    <div class="content">
      <el-form :model="formData" label-width="100px" :rules="rules">
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="医疗类别" prop="medicalType">
              <el-select v-model="formData.medicalType" placeholder="请选择">
                <el-option label="特殊门诊" value="special" />
                <!-- 其他选项 -->
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="病种名称" prop="diseaseName">
              <el-select v-model="formData.diseaseName" placeholder="请选择">
                <!-- 病种选项 -->
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="入院日期" prop="admissionDate">
              <el-date-picker v-model="formData.admissionDate" type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd"
                style="width: 100%" placeholder="请选择日期" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="参保地" prop="insuredPlace">
              <el-select v-model="formData.insuredPlace" placeholder="请选择">
                <el-option label="特困户" value="special" />
                <!-- 其他选项 -->
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item label="入院诊断" prop="admissionDiagnosis">
              <el-input v-model="formData.admissionDiagnosis" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="重复住院">
              <el-switch inline-prompt v-model="formData.isRepeatHospitalization" active-text="是" inactive-text="否" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="外伤标志">
              <el-radio-group v-model="formData.hasOutsideStandard" class="ml-20">
                <el-radio :value="true">是</el-radio>
                <el-radio :value="false">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="临床试验">
              <el-radio-group v-model="formData.hasClinicalTrial" class="ml-20">
                <el-radio :value="true">是</el-radio>
                <el-radio :value="false">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="第三方责任">
              <el-radio-group v-model="formData.hasThirdParty" class="ml-20">
                <el-radio :value="true">是</el-radio>
                <el-radio :value="false">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="急诊标志">
              <el-switch inline-prompt v-model="formData.isEmergency" active-text="是" inactive-text="否" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="涉外标志">
              <el-radio-group v-model="formData.hasOverseasStandard" class="ml-20">
                <el-radio :value="true">是</el-radio>
                <el-radio :value="false">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24" class="text-center">
            <el-button type="primary" fixed style="width: 100px">登记变更</el-button>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>

<script setup >
import { ref } from 'vue';

interface FormData {
  medicalType: string;
  diseaseName: string;
  admissionDate: string;
  insuredPlace: string;
  admissionDiagnosis: string;
  isRepeatHospitalization: boolean;
  hasOutsideStandard: boolean;
  isEmergency: boolean;
  hasOverseasStandard: boolean;
}

const formData = ref<FormData>({
  medicalType: '',
  diseaseName: '',
  admissionDate: '',
  insuredPlace: '',
  admissionDiagnosis: '',
  isRepeatHospitalization: false,
  hasOutsideStandard: false,
  isEmergency: false,
  hasOverseasStandard: false,
});

const rules = {
  medicalType: [
    { required: true, message: '请选择医疗类别', trigger: 'change' },
  ],
  diseaseName: [
    { required: true, message: '请选择病种名称', trigger: 'change' },
  ],
  admissionDate: [
    { required: true, message: '请选择入院日期', trigger: 'change' },
  ],
  admissionDiagnosis: [
    { required: true, message: '请输入入院诊断', trigger: 'blur' },
  ],
};

const handleSubmit = () => {
  // 处理表单提交

};
</script>

<style lang="scss" scoped>
.insurance-form {
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
  }

  .ml-20 {
    margin-left: 20px;
  }

  .text-center {
    text-align: center;
  }

  .label {
    color: #606266;
  }
}
</style>
