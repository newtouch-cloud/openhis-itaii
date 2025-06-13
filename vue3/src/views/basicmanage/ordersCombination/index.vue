<template>
  <div
    class="app-container"
    style="padding: 10px 20px; display: flex; justify-content: space-between"
  >
    <div style="width: 25%">
      <!-- <div style="height: 30px; background-color: #f5f5f5; border-bottom: 1px solid #eee">
        <span style="color: #606266">组套信息</span>
      </div> -->
      <div style="margin: 10px 0; display: flex; gap: 10px">
        <el-button type="primary" @click="handleAdd">添加组套</el-button>
        <el-input
          style="width: 250px"
          placeholder="输入组套名称"
          v-model="queryParams.name"
          @keyup.enter="getList"
        >
          <template #append>
            <el-button icon="Search" @click="getList" />
          </template>
        </el-input>
        <el-select placeholder="组套类型" v-model="queryParams.typeEnum">
          <el-option
            v-for="item in typeOption"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </div>
      <el-table
        :data="combinationList"
        border
        highlight-current-row
        max-height="750"
        @row-click="handleGetCombinationList"
      >
        <el-table-column type="index" :index="(index) => index + 1" />
        <el-table-column prop="name" label="名称" align="left">
          <template #default="scope">
            <span v-if="!scope.row.isEdit">{{ scope.row.name }}</span>
            <el-input v-else v-model="scope.row.name" placeholder="组套名称" />
          </template>
        </el-table-column>
        <el-table-column prop="typeEnum_enumText" label="类型" width="100" align="center">
          <template #default="scope">
            <el-tag v-if="!scope.row.isEdit" type="default">
              {{ scope.row.typeEnum_enumText }}
            </el-tag>
            <el-select v-else placeholder="类型" v-model="scope.row.typeEnum">
              <el-option
                v-for="item in typeOption"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="使用范围" width="100" align="center">
          <template #default="scope">
            <el-tag v-if="!scope.row.isEdit" type="default">
              {{ scope.row.rangeCode_dictText }}
            </el-tag>
            <el-select
              v-else
              placeholder="使用范围"
              v-model="scope.row.rangeCode"
              @change="rangeCodeChange(scope.row, scope.$index)"
            >
              <el-option
                v-for="item in use_range"
                :disabled="item.value != 3"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </template>
        </el-table-column>
        <!-- <el-table-column label="使用人" width="100" align="center">
          <template #default="scope">
            <el-tag v-if="!scope.row.isEdit" type="default">
              {{ scope.row.practitionerName }}
            </el-tag>
            <el-select
              v-else
              placeholder="使用人"
              v-model="scope.row.practitionerId"
              :disabled="scope.row.rangeCode != 1"
              @change="practitionerIdChange(scope.row, scope.$index)"
            >
              <el-option
                v-for="item in userPractitionerPCage"
                :key="item.practitionerId"
                :label="item.nickName"
                :value="item.practitionerId"
              />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="科室" width="100" align="center">
          <template #default="scope">
            <el-tag v-if="!scope.row.isEdit" type="default">
              {{ scope.row.orgName }}
            </el-tag>
            <el-select
              v-else
              placeholder="科室"
              v-model="scope.row.orgId"
              :disabled="scope.row.rangeCode != 2"
              @change="orgIdChange(scope.row, scope.$index)"
            >
              <el-option
                v-for="item in departmentList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </template>
        </el-table-column> -->
        <!-- <el-table-column prop="name" label="操作" align="center" width="70">
          <template #default="scope">
            <el-button
              v-if="scope.row.isEdit"
              type="primary"
              link
              @click="handleSave(scope.row, scope.$index)"
            >
              保存
            </el-button>
            <el-button v-else type="primary" link @click="handleEdit(scope.row, scope.$index)">
              编辑
            </el-button>
          </template>
        </el-table-column> -->
      </el-table>
    </div>
    <div style="width: 74%">
      <!-- <div style="margin: 10px 0; display: flex; gap: 10px">
        <el-button type="primary" @click="handleAddTable">添加项目</el-button>
        <el-button v-if="editingIndex === -1" type="primary" @click="handleEditTable" plain>
          编辑
        </el-button>
        <el-button v-else type="primary" @click="handleSaveTable" plain>保存</el-button>
      </div> -->
      <div style="margin: 10px 0; display: flex; gap: 10px">
        <Prescriptionlist ref="prescriptionRef" :comtination="combinationList[currentIndex]" />
      </div>
    </div>
  </div>
</template>

<script setup name="">
import {
  getOrderGroup,
  saveOrderGroup,
  getAdviceBaseInfo,
  getDepartmentList,
  getUserPractitionerPCage,
} from './components/api';
import Prescriptionlist from './components/prescriptionlist.vue';
import useUserStore from '@/store/modules/user';
import { ref } from 'vue';
const { proxy } = getCurrentInstance();
const { method_code, unit_code, rate_code, use_range } = proxy.useDict(
  'method_code',
  'unit_code',
  'rate_code',
  'use_range'
);

const queryParams = ref({});
const combinationList = ref([]);
const combinationInfoList = ref([]);
const adviceBaseOption = ref([]);
const editingIndex = ref(-1);
const userStore = useUserStore();
const currentIndex = ref(-1);
const groupItem = ref({});
const groupId = ref(null);
const userPractitionerPCage = ref([]);
const departmentList = ref([]);
const typeOption = ref([
  { label: '医嘱组套', value: 1 },
  { label: '中医协定处方', value: 2 },
  { label: '划价组套', value: 3 },
]);

getList();
function getList() {
  getOrderGroup().then((res) => {
    combinationList.value = res.data.records;
  });
  getAdviceBaseInfo({ organizationId: userStore.orgId }).then((res) => {
    // todo organizationId获取的参数需要修改
    adviceBaseOption.value = res.data.records;
  });
}
function practitionerIdChange(row, index) {
  let practitionerName = userPractitionerPCage.value.filter(
    (e) => e.practitionerId == row.practitionerId
  );
  combinationList.value[index].practitionerName = practitionerName[0].nickName;
}
function orgIdChange(row, index) {
  let orgName = departmentList.value.filter((e) => e.id == row.orgId);
  combinationList.value[index].orgName = orgName[0].name;
}
function rangeCodeChange(row, index) {
  if (row.rangeCode == 1) {
    // 个人
    getUserPractitionerPCage({ pageNo: 1, pageSize: 100 }).then((res) => {
      userPractitionerPCage.value = res.data.records;
      combinationList.value[index].orgId = '';
      combinationList.value[index].orgName = '';
    });
  } else if (row.rangeCode == 2) {
    //科室
    getDepartmentList().then((res) => {
      departmentList.value = res.data;
      combinationList.value[index].practitionerId = '';
      combinationList.value[index].practitionerName = '';
    });
  }
}
function handleAdd() {
  // 检查combinationList中是否存在isEdit为true的项
  const hasEditableItem = combinationList.value.some((item) => item.isEdit);
  if (!hasEditableItem) {
    combinationList.value.unshift({ isEdit: true });
    combinationInfoList.value.forEach((item, index) => {
      if (!item.isEdit) {
        item.isEdit = true;
        editingIndex.value = 0;
      }
    });
    combinationInfoList.value.push({ isEdit: true });
  } else {
    proxy.$modal.msgWarning('请先保存当前行！');
  }
  currentIndex.value = 0;
  console.log(combinationList.value, 'combinationList.value');
}

function handleAddTable() {
  combinationInfoList.value.forEach((item, index) => {
    if (!item.isEdit) {
      item.isEdit = true;
      editingIndex.value = 0;
    }
  });
  combinationInfoList.value.push({ isEdit: true });
}

function handleGetCombinationList(row) {
  if (groupItem.value && row.id === groupItem.value.id) {
    return;
  }
  groupItem.value = row;
  groupId.value = row.id;
  if (row.groupJson) {
    try {
      const parsedData = JSON.parse(row.groupJson);
      if (Array.isArray(parsedData)) {
        combinationInfoList.value = parsedData.map((item) => ({
          ...item,
          isEdit: false,
        }));
      } else {
        combinationInfoList.value = [parsedData].map((item) => ({
          ...item,
          isEdit: false,
        }));
      }
    } catch (error) {
      combinationInfoList.value = [];
    }
  } else {
    combinationInfoList.value = [];
  }
  editingIndex.value = -1;
  if (row.id) {
    currentIndex.value = combinationList.value.findIndex((item) => item.id === row.id);
  } else {
    currentIndex.value = 0;
  }
}

function handleDelete(row, index) {
  combinationInfoList.value.splice(index, 1);
}

function handleSave(row) {
  combinationInfoList.value.forEach((item, index) => {
    if (item.isEdit) {
      item.isEdit = false;
      editingIndex.value = -1;
    }
  });
  row.isEdit = false;
  // let params = groupItem.value
  let params = row;
  params.groupJson = JSON.stringify(combinationInfoList.value);
  saveOrderGroup(params).then((res) => {});
}

function handleEdit(row) {
  combinationInfoList.value.forEach((item, index) => {
    if (item.isEdit) {
      item.isEdit = true;
      editingIndex.value = 0;
    }
  });
  row.isEdit = true;
}

function handleEditTable() {
  if ((combinationInfoList.value[0].isEdit = true)) {
    editingIndex.value = 0;
  }
}

function handleSaveTable() {
  combinationInfoList.value.forEach((item, index) => {
    if (item.isEdit) {
      item.isEdit = false;
      editingIndex.value = -1;
    }
  });
  let params = groupItem.value;
  params.groupJson = JSON.stringify(combinationInfoList.value);
  saveOrderGroup(params).then((res) => {});
}
</script>