<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--诊疗目录-->
      <el-col :span="4" :xs="24">
        <div class="head-title">诊疗目录</div>
        <div class="head-container">
          <el-tree
            :data="diagnosisCategoryOptions"
            :props="{ label: 'info', children: 'children' }"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="treeRef"
            node-key="id"
            highlight-current
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>
      <!--诊疗目录-->
      <el-col :span="20" :xs="24">
        <el-form
          :model="queryParams"
          ref="queryRef"
          :inline="true"
          v-show="showSearch"
          label-width="68px"
        >
          <el-row :gutter="24">
            <!-- <el-col :span="5"> -->
            <el-form-item label="项目名" prop="searchKey" label-width="55">
              <el-input
                v-model="queryParams.searchKey"
                placeholder="品名/商品名/英文品名/编码/拼音"
                clearable
                style="width: 220px"
                @keyup.enter="handleQuery"
              />
            </el-form-item>
            <!-- </el-col> -->
            <!-- <el-col :span="5"> -->
            <el-form-item label="状态" prop="statusEnum" label-width="80">
              <el-select v-model="queryParams.statusEnum" clearable>
                <el-option
                  v-for="status in statusFlagOptions"
                  :key="status.value"
                  :label="status.info"
                  :value="status.value"
                />
              </el-select>
            </el-form-item>
            <!-- </el-col> -->
            <!-- <el-col :span="5"> -->
            <el-form-item label="医保是否对码" prop="ybMatchFlag" label-width="120">
              <el-select v-model="queryParams.ybMatchFlag" placeholder="" clearable>
                <el-option
                  v-for="item in statusWeatherOption"
                  :key="item.value"
                  :label="item.info"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <!-- </el-col> -->
            <!-- <el-col :span="4">
              <el-form-item label="执行科室" prop="ruleId" label-width="100">
                <el-select
                  v-model="queryParams.ruleId"
                  placeholder=""
                  clearable
                >
                  <el-option
                    v-for="item in exeOrganizations"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col> -->
            <!-- <el-col :span="4"> -->
            <el-form-item label="类型" prop="typeEnum" label-width="100">
              <el-select v-model="queryParams.typeEnum" placeholder="" clearable>
                <el-option
                  v-for="item in typeEnumOptions"
                  :key="item.value"
                  :label="item.info"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <!-- </el-col> -->
          </el-row>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="openAddDiagnosisTreatment"
              >添加新项目</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Remove" :disabled="multiple" @click="handleClose"
              >停用</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              plain
              icon="CirclePlus"
              :disabled="multiple"
              @click="handleStart"
              >启用</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Search" @click="getList">查询</el-button>
          </el-col>
          <!-- <el-col :span="1.5">
            <el-button
              type="warning"
              plain
              icon="Download"
              @click="handleExport"
              v-hasPermi="['system:user:export']"
              >导出Excel</el-button
            >
          </el-col> -->
        </el-row>

        <el-table
          v-loading="loading"
          :data="diagnosisTreatmentList"
          @selection-change="handleSelectionChange"
          width="90%"
        >
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column
            label="编码"
            align="center"
            key="busNo"
            prop="busNo"
            :show-overflow-tooltip="true"
            width="150"
          />
          <el-table-column
            label="项目名称"
            align="center"
            key="name"
            prop="name"
            :show-overflow-tooltip="true"
            width="150"
          />
          <el-table-column
            label="拼音"
            align="center"
            key="pyStr"
            prop="pyStr"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="五笔码"
            align="center"
            key="wbStr"
            prop="wbStr"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="目录类别"
            align="center"
            key="categoryCode_dictText"
            prop="categoryCode_dictText"
            :show-overflow-tooltip="true"
            width="100"
          />
          <el-table-column
            label="业务类型"
            align="center"
            key="typeEnum_enumText"
            prop="typeEnum_enumText"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="使用单位"
            align="center"
            key="permittedUnitCode_dictText"
            prop="permittedUnitCode_dictText"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="医保标记"
            align="center"
            key="ybFlag_enumText"
            prop="ybFlag_enumText"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="医保编码"
            align="center"
            key="ybNo"
            prop="ybNo"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="医保对码标记"
            align="center"
            key="ybMatchFlag_enumText"
            prop="ybMatchFlag_enumText"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="状态"
            align="center"
            key="statusEnum_enumText"
            prop="statusEnum_enumText"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="归属科室"
            align="center"
            key="orgId_dictText"
            prop="orgId_dictText"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="所在位置"
            align="center"
            key="locationId_dictText"
            prop="locationId_dictText"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="身体部位"
            align="center"
            key="bodySiteCode_dictText"
            prop="bodySiteCode_dictText"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="所需标本"
            align="center"
            key="specimenCode_dictText"
            prop="specimenCode_dictText"
            :show-overflow-tooltip="true"
            width="100"
          />
          <el-table-column
            label="财务类别"
            align="center"
            key="itemTypeCode_dictText"
            prop="itemTypeCode_dictText"
            :show-overflow-tooltip="true"
            width="100"
          />
          <el-table-column
            label="医保类别"
            align="center"
            key="ybType_dictText"
            prop="ybType_dictText"
            :show-overflow-tooltip="true"
            width="100"
          />
          <el-table-column
            label="售价"
            align="center"
            key="retailPrice"
            prop="retailPrice"
            :show-overflow-tooltip="true"
            width="100"
          />
          <el-table-column
            label="说明"
            align="center"
            key="descriptionText"
            prop="descriptionText"
            :show-overflow-tooltip="true"
          />
          <!-- <el-table-column
            label="执行科室"
            align="center"
            key="ruleId"
            prop="ruleId"
            :show-overflow-tooltip="true"
            width="90"
          /> -->
          <el-table-column
            label="操作"
            align="center"
            width="150"
            class-name="small-padding fixed-width"
            fixed="right"
          >
            <template #default="scope">
              <el-button
                link
                type="primary"
                icon="Edit"
                @click="openEditDiagnosisTreatment(scope.row)"
                >编辑</el-button
              >
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
      </el-col>
    </el-row>
    <diagnosis-treatment-dialog
      ref="diagnosisTreatmentRef"
      :currentCategoryEnum="currentCategoryEnum"
      :diagnosisCategoryOptions="diagnosisCategoryOptions"
      :statusFlagOptions="statusFlagOptions"
      :exeOrganizations="exeOrganizations"
      :typeEnumOptions="typeEnumOptions"
      :title="title"
      :item="currentData"
      @submit="getList()"
      @ybDialog="
        () => {
          proxy.$refs['diagTreYbRef'].show();
        }
      "
    />
    <DiagTreYbDialog
      ref="diagTreYbRef"
      @selectDiagnosisTreatment="
        (row) => {
          proxy.$refs['diagnosisTreatmentRef'].setValue(row);
        }
      "
    />
  </div>
</template>
  
<script setup name="DiagnosisTreatment">
import {
  getDiagnosisTreatmentList,
  stopDiseaseTreatment,
  startDiseaseTreatment,
  getDiseaseTreatmentInit,
  getDiagnosisTreatmentOne,
} from './components/diagnosistreatment';
import diagnosisTreatmentDialog from './components/diagnosisTreatmentDialog';
import DiagTreYbDialog from './components/diagTreYbDialog';
import { nextTick } from 'vue';

const { proxy } = getCurrentInstance();

const diagnosisTreatmentList = ref([]);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]); // 存储选择的行数据
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref('');
const diagnosisCategoryOptions = ref(undefined);
const statusFlagOptions = ref(undefined);
const exeOrganizations = ref(undefined);
const typeEnumOptions = ref(undefined);
const statusWeatherOption = ref(undefined);
// 使用 ref 定义当前诊疗数据
const currentData = ref({});
// 使用 ref 定义当前查看诊疗数据
const viewData = ref({});
const currentCategoryEnum = ref('');

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    searchKey: undefined, // 品名/商品名/英文品名/编码/拼音
    typeEnum: undefined, // 类型（包括 1：中药，2：成药）
    statusEnum: undefined, // 状态（包括 1：预置，2：启用，3：停用）
    ybMatchFlag: undefined, // 是否医保匹配（包括 1：是，0：否）
    ruleId: undefined, // 执行科室
    categoryCode: undefined, // 目录分类
  },
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);

/** 通过条件过滤节点  */
const filterNode = (value, data) => {
  if (!value) return true;
  return data.label.indexOf(value) !== -1;
};

/** 诊断目录分类查询下拉树结构 */
function getDiseaseTreatmentList() {
  getDiseaseTreatmentInit().then((response) => {
    console.log(response, 'response诊疗目录分类查询下拉树结构');
    diagnosisCategoryOptions.value = response.data.diagnosisCategoryOptions.sort((a, b) => {
      return parseInt(a.value) - parseInt(b.value);
    });
    diagnosisCategoryOptions.value.push({ info: '全部', value: '' });
    statusFlagOptions.value = response.data.statusFlagOptions;
    exeOrganizations.value = response.data.exeOrganizations;
    typeEnumOptions.value = response.data.typeEnumOptions;
    statusWeatherOption.value = response.data.statusWeatherOption;
  });
}
/** 查询诊断目录列表 */
function getList() {
  console.log(queryParams.value, 'queryParams***********************');
  loading.value = true;
  getDiagnosisTreatmentList(queryParams.value).then((res) => {
    loading.value = false;
    diagnosisTreatmentList.value = res.data.records;
    console.log(diagnosisTreatmentList, 'res.data');
    total.value = res.data.total;
  });
}
/** 节点单击事件 */
function handleNodeClick(data) {
  console.log(data, '节点单击事件');
  queryParams.value.categoryCode = data.value;
  currentCategoryEnum.value = data.value;
  handleQuery();
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNo = 1;
  console.log(queryParams, 'queryParams搜索');
  getList();
}

/** 启用按钮操作 */
function handleStart() {
  const stardIds = ids.value;
  //   selectedData
  proxy.$modal
    .confirm('是否确定启用数据！')
    .then(function () {
      return startDiseaseTreatment(stardIds);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess('启用成功');
    })
    .catch(() => {});
}
/** 停用按钮操作 */
function handleClose() {
  const stopIds = ids.value;
  proxy.$modal
    .confirm('是否确认停用数据！')
    .then(function () {
      return stopDiseaseTreatment(stopIds);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess('停用成功');
    })
    .catch(() => {});
}
/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    'system/user/export',
    {
      ...queryParams.value,
    },
    `user_${new Date().getTime()}.xlsx`
  );
}

/** 选择条数  */
function handleSelectionChange(selection) {
  console.log(selection, 'selection');
  // selectedData.value = selection.map((item) => ({ ...item })); // 存储选择的行数据
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 打开新增弹窗 */
function openAddDiagnosisTreatment() {
  // if (currentCategoryEnum.value) {
  console.log('打开新增弹窗');
  title.value = '新增';
  nextTick(() => {
    proxy.$refs.diagnosisTreatmentRef.show();
  });
  // } else {
  //   proxy.$modal.msgError("请先选择目录分类！");
  // }
}
/** 打开编辑弹窗 */
function openEditDiagnosisTreatment(row) {
  getDiagnosisTreatmentOne(row.id).then((response) => {
    console.log(response, 'response88888');

    currentData.value = response.data;
    currentData.value.ybFlag == 1
      ? (currentData.value.ybFlag = true)
      : (currentData.value.ybFlag = false);
    currentData.value.ybMatchFlag == 1
      ? (currentData.value.ybMatchFlag = true)
      : (currentData.value.ybMatchFlag = false);
    title.value = '编辑';
    nextTick(() => {
      proxy.$refs['diagnosisTreatmentRef'].edit();
    });
    getList();
  });
}

getDiseaseTreatmentList();
getList();
</script>
  <style scoped>
.el-form--inline .el-form-item {
  display: inline-flex;
  vertical-align: middle;
  margin-right: 10px !important;
}
.el-select {
  width: 150px !important;
}
</style>
  