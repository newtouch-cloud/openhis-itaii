<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--药品目录-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
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
            <el-col :span="5">
              <el-form-item label="药品" prop="searchKey" label-width="40">
                <el-input
                  v-model="queryParams.searchKey"
                  placeholder="品名/商品名/英文品名/编码/拼音"
                  clearable
                  style="width: 240px"
                  @keyup.enter="handleQuery"
                />
              </el-form-item>
            </el-col>
            <el-col :span="5">
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
            </el-col>
            <el-col :span="4">
              <el-form-item
                label="医保对码"
                prop="ybMatchFlag"
                label-width="80"
              >
                <el-select
                  v-model="queryParams.ybMatchFlag"
                  placeholder=""
                  clearable
                >
                  <el-option
                    v-for="ybMatch in statusWeatherOption"
                    :key="ybMatch.value"
                    :label="ybMatch.info"
                    :value="ybMatch.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <!-- <el-form-item>
                  <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                  <el-button icon="Refresh" @click="resetQuery">重置</el-button>
               </el-form-item> -->
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="Plus"
              @click="openAddMedicine"
              v-hasPermi="['system:user:add']"
              >添加新项目</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="Remove"
              :disabled="multiple"
              @click="handleClose"
              v-hasPermi="['system:user:edit']"
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
              v-hasPermi="['system:user:remove']"
              >启用</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="Search"
              @click="getList"
              v-hasPermi="['system:user:import']"
              >查询</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              plain
              icon="Download"
              @click="handleExport"
              v-hasPermi="['system:user:export']"
              >导出Excel</el-button
            >
          </el-col>
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
            label="药品编码"
            align="center"
            key="medicationDefId"
            prop="medicationDefId"
            :show-overflow-tooltip="true"
            width="200px"
          />
          <el-table-column
            label="药品状态"
            align="center"
            key="statusEnum_enumText"
            prop="statusEnum_enumText"
            :show-overflow-tooltip="true"
          />
          <!-- <el-table-column
            label="医保类别"
            align="center"
            key="ybType_enumText"
            prop="ybType_enumText"
            :show-overflow-tooltip="true"
          /> -->
          <el-table-column
            label="药品名称拼音码"
            align="center"
            key="pyStr"
            prop="pyStr"
            :show-overflow-tooltip="true"
            width="120"
          />
          <el-table-column
            label="药品五笔码"
            align="center"
            key="wbStr"
            prop="wbStr"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="药品分类"
            align="center"
            key="categoryCode_dictText"
            prop="categoryCode_dictText"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="所属科室"
            align="center"
            key="orgId_dictText"
            prop="orgId_dictText"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="地点"
            align="center"
            key="locationId_dictText"
            prop="locationId_dictText"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="剂型"
            align="center"
            key="doseFormCode_dictText"
            prop="doseFormCode_dictText"
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
            label="成分"
            align="center"
            key="ingredientItem"
            prop="ingredientItem"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="是否为活性"
            align="center"
            key="activeFlag_enumText"
            prop="activeFlag_enumText"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="批次号"
            align="center"
            key="lotNumber"
            prop="lotNumber"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="生效日期"
            align="center"
            key="effectiveDate"
            prop="effectiveDate"
            :show-overflow-tooltip="true"
          >
            <template #default="scope">
              <span>{{ parseTime(scope.row.effectiveDate) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="到期日期"
            align="center"
            key="expirationDate"
            prop="expirationDate"
            :show-overflow-tooltip="true"
          >
            <template #default="scope">
              <span>{{ parseTime(scope.row.expirationDate) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="用法"
            align="center"
            key="methodCode_dictText"
            prop="methodCode_dictText"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="用药频次"
            align="center"
            key="rateCode_dictText"
            prop="rateCode_dictText"
            :show-overflow-tooltip="true"
            width="100"
          />
          <el-table-column
            label="单次剂量"
            align="center"
            key="dose"
            prop="dose"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="剂量单位"
            align="center"
            key="doseUnitCode_dictText"
            prop="doseUnitCode_dictText"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="单次最大剂量"
            align="center"
            key="maxUnit"
            prop="maxUnit"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="药品定义"
            align="center"
            key="definition"
            prop="definition"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="用量限定"
            align="center"
            key="usageLimit"
            prop="usageLimit"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="DDD值"
            align="center"
            key="dddCode_dictText"
            prop="dddCode_dictText"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="DDD单位"
            align="center"
            key="dddUnitCode_dictText"
            prop="dddUnitCode_dictText"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="适用范围"
            align="center"
            key="domainEnum_enumText"
            prop="domainEnum_enumText"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="药品版本"
            align="center"
            key="version"
            prop="version"
            :show-overflow-tooltip="true"
            width="120"
          />
          <el-table-column
            label="英文药名"
            align="center"
            key="nameEn"
            prop="nameEn"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="商品名称"
            align="center"
            key="merchandiseName"
            prop="merchandiseName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="商品名称拼音码"
            align="center"
            key="merchandisePyStr"
            prop="merchandisePyStr"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="商品五笔码"
            align="center"
            key="merchandiseWbStr"
            prop="merchandiseWbStr"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="包装单位"
            align="center"
            key="unitCode_dictText"
            prop="unitCode_dictText"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="财务类型"
            align="center"
            key="typeCode_dictText"
            prop="typeCode_dictText"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="所含耗材"
            align="center"
            key="comprisedText"
            prop="comprisedText"
            :show-overflow-tooltip="true"
            width="110"
          />
          <el-table-column
            label="拆零比"
            align="center"
            key="partPercent"
            prop="partPercent"
            :show-overflow-tooltip="true"
            width="120"
          />
          <el-table-column
            label="剂量形式"
            align="center"
            key="doseFrom_dictText"
            prop="doseFrom_dictText"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="批准文号"
            align="center"
            key="approvalNumber"
            prop="approvalNumber"
            :show-overflow-tooltip="true"
            width="120"
          />
          <el-table-column
            label="医保是否对码"
            align="center"
            key="ybMatchFlag_enumText"
            prop="ybMatchFlag_enumText"
            :show-overflow-tooltip="true"
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
            label="药理作用分类"
            align="center"
            key="pharmacologyCategoryCode"
            prop="pharmacologyCategoryCode"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="是否皮试"
            align="center"
            key="skinTestFlag_enumText"
            prop="skinTestFlag_enumText"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="是否为注射药物"
            align="center"
            key="injectFlag_enumText"
            prop="injectFlag_enumText"
            :show-overflow-tooltip="true"
            width="90"
          />
          <!-- <el-table-column
            label="生产厂家"
            align="center"
            key="manufacturerId"
            prop="manufacturerId"
            :show-overflow-tooltip="true"
          /> -->
          <el-table-column
            label="生产厂家名称"
            align="center"
            key="manufacturerText"
            prop="manufacturerText"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="供应商"
            align="center"
            key="supplyId_dictText"
            prop="supplyId_dictText"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="是否限制使用"
            align="center"
            key="restrictedFlag_enumText"
            prop="restrictedFlag_enumText"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="限制使用范围"
            align="center"
            key="restrictedScope"
            prop="restrictedScope"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="儿童用药标志"
            align="center"
            key="childrenFlag_enumText"
            prop="childrenFlag_enumText"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="产品特性"
            align="center"
            key="characteristic"
            prop="characteristic"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="贯标国家编码"
            align="center"
            key="nationalDrugCode"
            prop="nationalDrugCode"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="拆分属性"
            align="center"
            key="partAttributeEnum_enumText"
            prop="partAttributeEnum_enumText"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="住院临时医嘱拆分属性"
            align="center"
            key="thoPartAttributeEnum_enumText"
            prop="thoPartAttributeEnum_enumText"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="抗生素分类"
            align="center"
            key="antibioticCode_dictText"
            prop="antibioticCode_dictText"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="权限限制"
            align="center"
            key="restrictedEnum_enumText"
            prop="restrictedEnum_enumText"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="是否自制"
            align="center"
            key="selfFlag_enumText"
            prop="selfFlag_enumText"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="是否抗生素"
            align="center"
            key="antibioticFlag_enumText"
            prop="antibioticFlag_enumText"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="基药标识"
            align="center"
            key="basicFlag_enumText"
            prop="basicFlag_enumText"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="当前库存数量(常规单位)"
            align="center"
            key="baseQuantity"
            prop="baseQuantity"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="当前库存数量(最小单位数量)"
            align="center"
            key="minQuantity"
            prop="minQuantity"
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
              <el-button
                link
                type="primary"
                icon="Edit"
                @click="openEditMedicine(scope.row)"
                v-hasPermi="['system:user:edit']"
                >编辑</el-button
              >
              <!-- <el-button
                link
                type="primary"
                icon="View"
                @click="openViewMedicine(scope.row)"
                v-hasPermi="['system:user:remove']"
                >查看</el-button
              > -->
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
    />
    <!-- <medicine-view-dialog
      ref="medicineViewRef"
      :item="viewData"
      :viewFlg="viewFlg"
    /> -->
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
} from "./components/medicine";
import medicineDialog from "./components/medicineDialog";
import medicineViewDialog from "./components/medicineViewDialog";
import { nextTick } from "vue";

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
const currentCategoryEnum = ref("");
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
    console.log(response, "response药品目录分类查询下拉树结构");
    medicationOptions.value = response.data.medicationCategoryCodeOptions.sort((a, b) => { return parseInt(a.value) - parseInt(b.value) });
    statusFlagOptions.value = response.data.statusFlagOptions;
    domainEnumOptions.value = response.data.domainFlagOptions;
    supplierListOptions.value = response.data.supplierListOptions;
    statusWeatherOption.value = response.data.statusWeatherOptions;
    statusRestrictedOptions.value = response.data.statusRestrictedOptions;
    partAttributeEnumOptions.value = response.data.partAttributeEnumOptions;
    tempOrderSplitPropertyOptions.value =
      response.data.tempOrderSplitPropertyEnumOptions;
  });
}
/** 查询病种目录列表 */
function getList() {
  loading.value = true;
  console.log(queryParams.value, "queryParams***********************");
  getMedicationList(queryParams.value).then((res) => {
    loading.value = false;
    console.log(res, "res");
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
  console.log(queryParams.value, "queryParams");
  getList();
}

/** 启用按钮操作 */
function handleStart() {
  const startIds = ids.value;
  proxy.$modal
    .confirm("是否确定启用数据！")
    .then(function () {
      return startMedication(startIds);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("启用成功");
    })
    .catch(() => {});
}
/** 停用按钮操作 */
function handleClose() {
  const stopIds = ids.value;
  console.log(data, "data");
  proxy.$modal
    .confirm("是否确认停用数据！")
    .then(function () {
      return stopMedication(stopIds);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("停用成功");
    })
    .catch(() => {});
}
/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    "system/user/export",
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
  if (!currentCategoryEnum.value) {
    return proxy.$modal.msgError("请选择药品目录分类");
  }
  proxy.$refs["medicineRef"].show();
}
/** 打开编辑弹窗 */
function openEditMedicine(row) {
  getMedicationOne(row.id).then((response) => {
    console.log(response, "responsebbbb", row.id);
    currentData.value = response.data;
    nextTick(() => {
      proxy.$refs["medicineRef"].edit();
    });
  });
}
/** 打开查看弹窗 */
function openViewMedicine(row) {
  getMedicationOne(row.id).then((response) => {
    viewData.value = response.data;
    nextTick(() => {
      proxy.$refs["medicineViewRef"].edit();
    });
    getList();
  });
}

/** 提交按钮 */
function submitForm(formData) {
  console.log(formData, "submitForm");
  if (formData.id != undefined) {
    editMedication(formData).then((response) => {
      proxy.$modal.msgSuccess("修改成功");
      open.value = false;
      getList();
    });
  } else {
    addMedication(formData).then((response) => {
      proxy.$modal.msgSuccess("新增成功");
      open.value = false;
      getList();
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
</style>
