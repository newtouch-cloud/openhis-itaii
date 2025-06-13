<template>
  <div class="search-form">
    <el-form :model="form" inline :label-width="80">
      <el-form-item label="病区">
        <el-select style="width: 206px" v-model="form.nurseDeptCode" placeholder="请选择" clearable>
          <el-option v-for="item in nursingGroupList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="患者姓名">
        <el-input v-model="form.name" style="width: 206px" placeholder="请输入" clearable @keyup.enter="handleSearch" />
      </el-form-item>
      <el-form-item label="住院号">
        <el-input v-model="form.inpatientCode" style="width: 206px" placeholder="请输入" @keyup.enter="handleSearch"
          clearable />
      </el-form-item>
      <div class="button-group">
        <el-button @click="handleReset">重置</el-button>
        <el-button type="primary" @click="handleSearch">查询</el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup >
import { ref, onMounted } from 'vue';
import { getGroupApi } from '@/api/patientLimitDrug';
// import { store } from '@hip/portal';

// const { userInfo } = store.useGlobalStore();
const emit = defineEmits(['search', 'reset']);

const form = ref({
  nurseDeptCode: '',
  name: '',
  inpatientCode: '',
});

const nursingGroupList = ref<any[]>([]);

onMounted(() => {
  //   获取科室
});

const handleSearch = () => {
  emit('search', form.value);
};

const handleReset = () => {
  form.value = {
    nurseDeptCode: '',
    name: '',
    inpatientCode: '',
  };
  emit('reset');
};
</script>

<style scoped>
.button-group {
  display: flex;
  gap: 10px;
  justify-content: right;
  margin-bottom: 12px;
}
</style>
