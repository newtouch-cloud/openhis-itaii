<template>
  <div class="top-form">
    <el-form :model="form" inline>
      <el-form-item label="费别" class="fee-type">
        <el-select v-model="form.medicalType" placeholder="请选择">
          <el-option label="市职工医保" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item class="card-type" label-width="0px">
        <el-radio-group v-model="form.payType">
          <el-radio :value="1">医保卡</el-radio>
          <el-radio :value="2">医保电子凭证</el-radio>
          <el-radio :value="3">居民身份证</el-radio>
          <el-radio :value="4">社会保障卡</el-radio>
          <el-radio :value="5">医保刷脸</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item class="operate-btns">
        <el-button type="primary" class="main-btn" @click="handleReadCard">读卡</el-button>
        <el-button type="primary" class="main-btn" @click="handleUpload">费用上传</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup >
import { ref, inject } from 'vue';
import { FormInstance } from 'element-plus';

const form = inject('formState') as any;
const formRef = ref<FormInstance>();
const handleReset = () => {
  formRef.value?.resetFields();
};

const handleReadCard = () => {
  console.log('读卡');
};

const handleUpload = () => {
  console.log('费用上传');
};
</script>

<style lang="scss" scoped>
.top-form {
  padding: 8px;
  background: #fff;
  border-radius: 4px;
  padding-left: 16px;


  :deep(.el-form) {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 8px;
  }

  :deep(.el-radio) {
    margin-right: 8px;
  }

  .fee-type {
    margin-right: 8px;

    :deep(.el-select) {
      width: 160px;
    }
  }

  .card-type {
    // flex: 1;
    // margin-left: auto;
    // margin-right: 24px;
    margin-right: 8px;

    :deep(.el-radio-group) {
      display: flex;
      // gap: 24px;
    }
  }

  .operate-btns {
    .main-btn {
      // margin-left: 16px;
      // width: 100px;

      &:first-child {
        margin-left: 0;
      }
    }
  }

  :deep(.el-form-item) {
    margin-bottom: 0;
  }
}
</style>
