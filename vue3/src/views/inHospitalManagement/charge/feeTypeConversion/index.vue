<template>
  <div class="insurance-wrap">
    <div class="left-wrap" v-loading="loading">
      <div class="left-wrap-content">
        <SearchForm @search="handleSearch" @reset="handleReset" />
        <PatientList :data="patientList" @current-change="handleCurrentChange" />
      </div>
    </div>
    <div class="right-wrap">
      <PatientDetail :patient-info="currentPatient || {}" />
      <div class="right-wrap-content">
        <el-scrollbar height="100%">
          <TopForm></TopForm>
          <BasicForm></BasicForm>
          <InsuranceForm :visit-code="currentPatient?.visitCode" />
        </el-scrollbar>
      </div>

    </div>
  </div>
</template>

<script setup >
import { provide, reactive, ref } from 'vue';
import SearchForm from './components/SearchForm.vue';
import PatientList from './components/PatientList.vue';
import PatientDetail from './components/PatientDetail.vue';
import InsuranceForm from './components/InsuranceForm.vue';
// import { getPatientListApi } from '@/api/feeTypeConversion';
import TopForm from './components/TopForm.vue';
import BasicForm from './components/BasicForm.vue';

const patientList = ref<any[]>([
  {}
]);
const currentPatient = ref();

const formState = reactive({});
const loading = ref(false);

provide('formState', formState);
const handleSearch = (params: any) => {
  loading.value = true;
  // getPatientListApi(params)
  //   .then((res) => {
  //     patientList.value = res;
  //   })
  //   .finally(() => {
  //     loading.value = false;
  //   });
};

const handleReset = () => {
  currentPatient.value = null;
  patientList.value = [];
};

const handleCurrentChange = (patient: any) => {
  currentPatient.value = patient;
};
</script>

<style lang="scss" scoped>
.insurance-wrap {
  width: 100%;
  height: 100%;
  height: calc(100vh - 84px);
  // background: #c0ddf0;
  display: flex;
  // flex-direction: row;
}

.left-wrap {
  display: flex;
  flex-direction: column;
  height: 100%;
  box-sizing: border-box;
  width: 302px;
  overflow: hidden;
  flex: none;
  border-right: 1px solid #c0ddf0;
  padding: 8px;

  .left-wrap-content {
    height: 100%;
    display: flex;
    flex-direction: column;

    .search-form {
      flex: none;
      height: 182px;
    }

    .patient-list {
      flex: auto;
      height: 200px;
    }
  }
}

.right-wrap {
  width: 302px;
  flex: auto;
  height: 100%;
  display: flex;
  flex-direction: column;

  .pat-info {
    width: 100%;
    height: 72px;
    flex: none;
  }

  .right-wrap-content {
    flex: auto;
    height: 200px;
  }
}

// .content {
//   padding: 16px;
// }</style>
