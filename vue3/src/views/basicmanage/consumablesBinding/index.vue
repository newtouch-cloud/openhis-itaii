<template>
  <div class="app-container">
    <div style="height: 780px; display: flex; justify-content: space-between">
      <el-card style="height: 100%; width: 25%">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="用法" :name="1">
            <el-table
              :data="method_code"
              border
              @cell-click="clickRow"
              highlight-current-row
              max-height="650"
            >
              <el-table-column label="项目名" align="center" prop="label" />
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="诊疗" :name="2">
            <el-input
              v-model="queryParams.searchKey"
              placeholder="请输入项目名"
              clearable
              style="width: 100%; margin-bottom: 10px"
              @keyup.enter="getList"
            >
              <template #append>
                <el-button icon="Search" @click="getList" />
              </template>
            </el-input>
            <el-table
              :data="activityList"
              border
              @cell-click="clickRow"
              highlight-current-row
              max-height="650"
            >
              <el-table-column label="项目名" align="center" prop="name" />
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="号源" :name="3">
            <el-input
              v-model="queryParamsRegistration.searchKey"
              placeholder="请输入项目名"
              clearable
              style="width: 100%; margin-bottom: 10px"
              @keyup.enter="getRegistrationList"
            >
              <template #append>
                <el-button icon="Search" @click="getRegistrationList" />
              </template>
            </el-input>
            <el-table
              :data="RegistrationfeeList"
              border
              @cell-click="clickRow"
              highlight-current-row
              max-height="650"
            >
              <el-table-column label="项目名" align="center" prop="name" />
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </el-card>
      <el-card style="height: 100%; width: 74%">
        <ConsumablesList
          :bindList="bindList"
          :bindInfo="bindInfo"
          :tab="activeTab"
          :loading="loading"
          @refresh="clickRow(currentRow)"
        />
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { getActivityList, getBindList, getRegistrationfeeList } from './components/api.js';
import ConsumablesList from './components/consumablesList.vue';

const activityList = ref([]);
const queryParams = ref({});
const queryParamsRegistration = ref({});
const bindList = ref([]);
const bindInfo = ref({});
const activeTab = ref(1);
const currentRow = ref({});
const RegistrationfeeList = ref([]);
const loading = ref(false);
const { proxy } = getCurrentInstance();
const { method_code } = proxy.useDict('method_code');

getList();
getRegistrationList()
function getList() {
  // queryParams.value.typeEnum = activeTab.value;
  getActivityList(queryParams.value).then((res) => {
    activityList.value = res.data.records;
  });

}

function getRegistrationList() {
  getRegistrationfeeList(queryParamsRegistration.value).then(res => {
    RegistrationfeeList.value = res.data.records;
  })
}

// 点击诊疗列表 获取绑定的耗材
function clickRow(row) {
  loading.value = true;
  currentRow.value = row;
  bindInfo.value.id = row.value ? row.value : row.id;
  bindInfo.value.typeCode = activeTab.value;
  getBindList({ itemNo: row.value ? row.value : row.id }).then((res) => {
    bindList.value = res.data.records;
    loading.value = false;
  });
}
</script>

<style scoped>
</style>