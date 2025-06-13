<template>
  <div class="awaitList-container">
    <div class="operate">
      <el-space>
        <el-input
          v-model="queryParams.searchVal"
          style="max-width: 600px"
          placeholder="请输入内容"
          class="input-with-select"
        >
          <template #prepend>
            <el-select v-model="queryParams.searchType" placeholder="" style="width: 115px">
              <el-option label="患者姓名" value="name" />
              <el-option label="诊疗卡号" value="MED_ID" />
              <el-option label="身份证号" value="1" />
            </el-select>
          </template>
        </el-input>
        <el-button @click="resetQuery">重置</el-button>
        <el-button type="primary" @click="handleQuery">查询</el-button>
      </el-space>
      <el-space>
        <!-- <el-button>读卡</el-button> -->
        <el-button type="primary" @click="doRegistering('no-file')">无档登记</el-button>
      </el-space>
    </div>
    <div class="table-container">
      <el-table
        :data="treatHospitalizedData"
        style="width: 100%"
        height="100%"
        show-overflow-tooltip
      >
      <el-table-column type="index" width="54" label="序号" />
        <el-table-column prop="patientClass" label="申请来源"> </el-table-column>
        <el-table-column prop="createTime" label="申请时间" min-width="160" />
        <el-table-column prop="name" label="申请患者" />
        <el-table-column prop="wardLocationId_dictText" label="入院病区" />
        <el-table-column prop="genderEnum_enumText" label="性别" align="right" />
        <el-table-column prop="ageString" label="年龄" align="right"> </el-table-column>
        <el-table-column fixed="right" label="操作" width="88">
          <template #default="scope">
            <el-button type="primary" text @click="doRegistering(scope.row)"> 登记 </el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNo"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </div>
    <!-- // :code="currentPatientCode" -->
    <PatientRegister v-model="patientRegisterVisible" :patientInfo="patient" title="登记" :registrationType="registrationType" @okAct="patientRegisterOK" />
  </div>
</template>
<script setup >
// import { TabsPaneContext } from 'element-plus';
import { getCurrentInstance, onBeforeMount, onMounted, reactive, ref } from 'vue';
//const { proxy } = getCurrentInstance();
import PatientRegister from './patientRegister.vue';
import { getInit, getAdmissionPage } from "./api";
const emits = defineEmits(['okList']);
// const props = defineProps({});

const total = ref();
const patient = ref({})
const registrationType = ref(true);
const queryParams = ref({
  pageNo: 1,
  pageSize: 10,
  searchKey: undefined,
  statusEnum: 1,
  searchVal: '',
  searchType: 'name',
})

const treatHospitalizedData = ref([]);
const doRegistering = (row) => {
  if(row === 'no-file') {
    registrationType.value = false
  }else {
    registrationType.value = true
    patient.value = row
  }
  patientRegisterVisible.value = true;
};
onBeforeMount(() => {});
onMounted(() => {
  getList()
});
// defineExpose({ state });

const getList = () => {
  getAdmissionPage(queryParams.value).then(res => {
    treatHospitalizedData.value = res.data.records
    total.value = res.data.total;
  })
};

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNo = 1;
  getList();
}

function resetQuery() {
  queryParams.value = {
    pageNo: 1,
    pageSize: 10,
    searchKey: undefined,
    statusEnum: 2,
    searchVal: '',
    searchType: 'name',
  }
  getList();
}

const activeName = ref('first');

const patientRegisterVisible = ref(false);

const patientRegisterOK = () => {
  patientRegisterVisible.value = false;
  queryParams.value.searchVal = '';
  emits('okList');
  queryData();
};
const queryData = () => {};

// const handleClick = (tab: TabsPaneContext, event: Event) => {
//   console.log(tab, event);
// };
</script>
<style lang="scss" scoped>
.awaitList-container {
  width: 100%;
  height: 100%;
  .operate {
    display: flex;
    justify-content: space-between;
    height: 40px;
    padding: 0 16px;
    align-items: center;
  }
  .table-container {
    padding: 8px 16px;
  }
}
</style>
