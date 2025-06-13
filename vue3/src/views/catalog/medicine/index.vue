<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--药品目录-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <div class="head-title">药品目录</div>
          <el-tree
            :data="medicationOptions"
            :props="{ label: 'info', children: 'children' }"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="medicationTreeRef"
            node-key="id"
            highlight-current
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>
      <!--药品目录-->
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
            <el-form-item label="药品" prop="searchKey" label-width="40">
              <el-input
                v-model="queryParams.searchKey"
                placeholder="品名/商品名/英文品名/编码/拼音"
                clearable
                style="width: 240px"
                @keyup.enter="handleQuery"
              />
            </el-form-item>
            <!-- </el-col> -->
            <!-- <el-col :span="5"> -->
            <el-form-item label="状态" prop="statusEnum" label-width="100">
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
            <!-- <el-col :span="4"> -->
            <el-form-item label="医保对码" prop="ybMatchFlag" label-width="100">
              <el-select v-model="queryParams.ybMatchFlag" placeholder="" clearable>
                <el-option
                  v-for="ybMatch in statusWeatherOption"
                  :key="ybMatch.value"
                  :label="ybMatch.info"
                  :value="ybMatch.value"
                />
              </el-select>
            </el-form-item>
            <!-- </el-col> -->
          </el-row>
          <!-- <el-form-item>
                  <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                  <el-button icon="Refresh" @click="resetQuery">重置</el-button>
               </el-form-item> -->
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="openAddMedicine"
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
              >导出Excel</el-button
            >
          </el-col> -->
        </el-row>

        <el-table
          v-loading="loading"
          :data="medicationList"
          @selection-change="handleSelectionChange"
          width="90%"
        >
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column
            label="药品编号"
            align="center"
            key="busNo"
            prop="busNo"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="药品名称"
            align="center"
            key="name"
            prop="name"
            :show-overflow-tooltip="true"
            width="110"
          />
          <el-table-column
            label="药品状态"
            align="center"
            key="statusEnum_enumText"
            prop="statusEnum_enumText"
            :show-overflow-tooltip="true"
          >
          <template #default="scope">
            <el-tag v-if="scope.row.statusEnum == 2" type="success" >{{ scope.row.statusEnum_enumText }}</el-tag>
            <el-tag v-else type="error">{{ scope.row.statusEnum_enumText }}</el-tag>
          </template>
         </el-table-column>
          <el-table-column
            label="药品分类"
            align="center"
            key="categoryCode_dictText"
            prop="categoryCode_dictText"
            :show-overflow-tooltip="true"
            width="90"
          />
          <!-- <el-table-column
            label="所属科室"
            align="center"
            key="orgId_dictText"
            prop="orgId_dictText"
            :show-overflow-tooltip="true"
          /> -->
          <el-table-column
            label="采购入库位置"
            align="center"
            key="locationId_dictText"
            prop="locationId_dictText"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="规格"
            align="center"
            key="totalVolume"
            prop="totalVolume"
            :show-overflow-tooltip="true"
            width="200px"
          />
          <el-table-column
            label="医保编码"
            align="center"
            key="ybNo"
            prop="ybNo"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="医保是否对码"
            align="center"
            key="ybMatchFlag_enumText"
            prop="ybMatchFlag_enumText"
            :show-overflow-tooltip="true"
          />
          <!-- <el-table-column
            label="医保上传"
            align="center"
            key="ybNo"
            prop="ybNo"
            :show-overflow-tooltip="true"
            width="90"
          /> -->
          <el-table-column
            label="采购价"
            align="center"
            key="purchasePrice"
            prop="purchasePrice"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="售价"
            align="center"
            key="retailPrice"
            prop="retailPrice"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="操作"
            align="center"
            width="150"
            class-name="small-padding fixed-width"
            fixed="right"
          >
            <template #default="scope">
              <el-button link type="primary" icon="Edit" @click="openEditMedicine(scope.row)"
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
    <medicine-dialog
      ref="medicineRef"
      :item="currentData"
      :domainEnum="domainEnumOptions"
      :status="statusFlagOptions"
      :supplierListOptions="supplierListOptions"
      :statusRestrictedOptions="statusRestrictedOptions"
      :currentCategoryEnum="currentCategoryEnum"
      :partAttributeEnumOptions="partAttributeEnumOptions"
      :tempOrderSplitPropertyOptions="tempOrderSplitPropertyOptions"
      @submit="submitForm"
      @ybDialog="
        () => {
          proxy.$refs['medicineYbRef'].show();
        }
      "
    />
    <MedicineYbDialog
      ref="medicineYbRef"
      @selectMedicine="
        (row) => {
          proxy.$refs['medicineRef'].setValue(row);
        }
      "
    />
  </div>
</template>

<script setup name="Medication">
import {
  getMedicationList,
  editMedication,
  addMedication,
  getMedicationCategory,
  getMedicationOne,
  startMedication,
  stopMedication,
} from './components/medicine';
import medicineDialog from './components/medicineDialog';
import MedicineYbDialog from './components/medicineYbDialog';
import { nextTick } from 'vue';

const { proxy } = getCurrentInstance();

const medicationList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]); // 存储选择的行数据
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const medicationOptions = ref(undefined);
const statusFlagOptions = ref(undefined);
const domainEnumOptions = ref(undefined);
const supplierListOptions = ref(undefined);
const statusWeatherOption = ref(undefined);
const statusRestrictedOptions = ref(undefined);
const currentCategoryEnum = ref('');
const partAttributeEnumOptions = ref(undefined);
const tempOrderSplitPropertyOptions = ref(undefined);
// 使用 ref 定义当前药品数据
const currentData = ref({});
// 使用 ref 定义当前查看药品数据
const viewData = ref({});

// 使用 ref 定义当前查看药品数据
const viewFlg = ref(false);

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    searchKey: undefined, // 品名/商品名/英文品名/编码/拼音
    statusEnum: undefined, // 状态（包括 1：预置，2：启用，3：停用）
    ybMatchFlag: undefined, // 是否医保匹配（包括 1：是，0：否）
    categoryCode: undefined, // 目录
  },
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);

/** 通过条件过滤节点  */
const filterNode = (value, data) => {
  if (!value) return true;
  return data.label.indexOf(value) !== -1;
};

/** 病种目录分类查询下拉树结构 */
function getMedicationCategoryList() {
  getMedicationCategory().then((response) => {
    console.log(response, 'response药品目录分类查询下拉树结构');
    medicationOptions.value = response.data.medicationCategoryCodeOptions;
    medicationOptions.value.unshift({ info: '全部', value: '' });
    statusFlagOptions.value = response.data.statusFlagOptions;
    domainEnumOptions.value = response.data.domainFlagOptions;
    supplierListOptions.value = response.data.supplierListOptions;
    statusWeatherOption.value = response.data.statusWeatherOptions;
    statusRestrictedOptions.value = response.data.statusRestrictedOptions;
    partAttributeEnumOptions.value = response.data.partAttributeEnumOptions;
    tempOrderSplitPropertyOptions.value = response.data.tempOrderSplitPropertyEnumOptions;
  });
}
/** 查询病种目录列表 */
function getList() {
  loading.value = true;
  console.log(queryParams.value, 'queryParams***********************');
  getMedicationList(queryParams.value).then((res) => {
    loading.value = false;
    console.log(res, 'res');
    medicationList.value = res.data.records;
    total.value = res.data.total;
  });
}
/** 节点单击事件 */
function handleNodeClick(data) {
  queryParams.value.categoryCode = data.value;
  currentCategoryEnum.value = data.value;
  handleQuery();
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNo = 1;
  console.log(queryParams.value, 'queryParams');
  getList();
}

/** 启用按钮操作 */
function handleStart() {
  const startIds = ids.value;
  proxy.$modal
    .confirm('是否确定启用数据！')
    .then(function () {
      return startMedication(startIds);
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
  console.log(data, 'data');
  proxy.$modal
    .confirm('是否确认停用数据！')
    .then(function () {
      return stopMedication(stopIds);
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
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 打开新增弹窗 */
function openAddMedicine() {
  // if (!currentCategoryEnum.value) {
  //   return proxy.$modal.msgError("请选择药品目录分类");
  // }
  // proxy.$refs['medicineYbRef'].show();
  proxy.$refs['medicineRef'].show();
}
/** 打开编辑弹窗 */
function openEditMedicine(row) {
  getMedicationOne(row.id).then((response) => {
    currentData.value = response.data;
    nextTick(() => {
      proxy.$refs['medicineRef'].edit();
    });
  });
}

/** 提交按钮 */
function submitForm(formData) {
  console.log(formData, 'submitForm');
  if (formData.id != undefined) {
    editMedication(formData).then((response) => {
      proxy.$modal.msgSuccess('修改成功');
      open.value = false;
      getList();
    });
  } else {
    addMedication(formData).then((response) => {
      if (response.code == 200) {
        proxy.$modal.msgSuccess('新增成功');
        proxy.$refs['medicineRef'].cancel();
        open.value = false;
        getList();
      }
    });
  }
}

getMedicationCategoryList();
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
