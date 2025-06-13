<template>
  <div class="awaitList-container">
    <div class="operate">
      <el-space>
        <el-input
          v-model="searchForm.searchVal"
          style="max-width: 600px"
          placeholder="请输入内容"
          class="input-with-select"
        >
          <template #prepend>
            <el-select v-model="searchForm.searchType" placeholder="" style="width: 115px">
              <el-option label="患者姓名" value="name" />
              <el-option label="诊疗卡号" value="MED_ID" />
              <el-option label="身份证号" value="1" />
            </el-select>
          </template>
        </el-input>
        <el-button>重置</el-button>
        <el-button type="primary">查询</el-button>
      </el-space>
      <el-space>
        <!-- <el-button>读卡</el-button>
        <el-button type="primary">无档登记</el-button> -->
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
        <el-table-column prop="storageName" label="申请来源"> </el-table-column>
        <el-table-column prop="createdDate" label="申请时间" min-width="160" />
        <el-table-column prop="name" label="申请患者" />
        <el-table-column prop="deptNurseName" label="入院病区" />
        <el-table-column prop="sexName" label="性别" />
        <el-table-column prop="purchaseTotalAmount" label="年龄"> </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script setup >
// import { TabsPaneContext } from 'element-plus';
import { getCurrentInstance, onBeforeMount, onMounted, reactive, ref } from 'vue';
import { getAdmissionPage } from "./api";
//const { proxy } = getCurrentInstance();
const emits = defineEmits([]);
// const props = defineProps({});
const searchForm = reactive({
  searchType: 'name',
  searchVal: '',
});

const treatHospitalizedData = ref([{}, {}, {}, {}, {}, {}]);
const doRegistering = (row) => {};
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

const activeName = ref('first');

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
