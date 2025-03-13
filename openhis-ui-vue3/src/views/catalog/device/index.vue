<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--器材目录-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-tree
            :data="deviceCategories"
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
      <!--器材目录-->
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
              <el-form-item label="项目名" prop="searchKey" label-width="55">
                <el-input
                  v-model="queryParams.searchKey"
                  placeholder="品名/商品名/英文品名/编码/拼音"
                  clearable
                  style="width: 240px"
                  @keyup.enter="handleQuery"
                />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="状态" prop="statusEnum" label-width="50">
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
                label="医保是否对码"
                prop="ybMatchFlag"
                label-width="100"
              >
                <el-select v-model="queryParams.ybMatchFlag" placeholder="">
                  <el-option
                    v-for="item in exeOrganizations"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
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
            </el-col>
          </el-row>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="Plus"
              @click="openAddDevice"
              v-hasPermi="['system:user:add']"
              >添加新项目</el-button
            >
          </el-col>
          <!-- <el-col :span="1.5">
                  <el-button
                     type="primary"
                     plain
                     icon="Plus"
                     @click="handleAdd"
                     v-hasPermi="['system:user:add']"
                  >添加为本机构项目</el-button>
               </el-col> -->
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
          :data="deviceList"
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
          />
          <el-table-column
            label="器材名称"
            align="center"
            key="name"
            prop="name"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="拼音"
            align="center"
            key="pyStr"
            prop="pyStr"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="器材分类"
            align="center"
            key="categoryEnum"
            prop="categoryEnum"
            :show-overflow-tooltip="true"
            width="100"
          />
          <el-table-column
            label="器材种类"
            align="center"
            key="typeCode"
            prop="typeCode"
            :show-overflow-tooltip="true"
            width="50"
          />
          <el-table-column
            label="包装单位"
            align="center"
            key="unitCode"
            prop="unitCode"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="包装规格"
            align="center"
            key="size"
            prop="size"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="拆零比"
            align="center"
            key="partPercent"
            prop="partPercent"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="最小使用单位"
            align="center"
            key="minUnitCode"
            prop="minUnitCode"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="产品型号"
            align="center"
            key="modelNumber"
            prop="modelNumber"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="高值器材标志"
            align="center"
            key="hvcmFlag"
            prop="hvcmFlag"
            :show-overflow-tooltip="true"
          />

          <el-table-column
            label="销售单位"
            align="center"
            key="salesUnitCode"
            prop="salesUnitCode"
            :show-overflow-tooltip="true"
            width="100"
          />
          <el-table-column
            label="批准文号"
            align="center"
            key="approvalNumber"
            prop="approvalNumber"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="医保标记"
            align="center"
            key="ybFlag"
            prop="ybFlag"
            :show-overflow-tooltip="true"
            width="110"
          />
          <el-table-column
            label="医保编码"
            align="center"
            key="ybNo"
            prop="ybNo"
            :show-overflow-tooltip="true"
            width="110"
          />
          <el-table-column
            label="医保对码标记"
            align="center"
            key="ybMatchFlag"
            prop="ybMatchFlag"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="状态"
            align="center"
            key="statusEnum"
            prop="statusEnum"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="生产厂家"
            align="center"
            key="manufacturerId"
            prop="manufacturerId"
            :show-overflow-tooltip="true"
            width="90"
          />
          <el-table-column
            label="供应商"
            align="center"
            key="supplyId"
            prop="supplyId"
            :show-overflow-tooltip="true"
            width="110"
          />
          <el-table-column
            label="说明"
            align="center"
            key="description"
            prop="description"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="适用范围"
            align="center"
            key="jurisdiction"
            prop="jurisdiction"
            :show-overflow-tooltip="true"
            width="120"
          />
          <el-table-column
            label="执行科室"
            align="center"
            key="ruleId"
            prop="ruleId"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="器材版本"
            align="center"
            key="version"
            prop="version"
            :show-overflow-tooltip="true"
            width="120"
          />
          <el-table-column
            label="主要成分"
            align="center"
            key="substanceText"
            prop="substanceText"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="过敏标记"
            align="center"
            key="allergenFlag"
            prop="allergenFlag"
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
                @click="openEditDevice(scope.row)"
                v-hasPermi="['system:user:edit']"
                >编辑</el-button
              >
              <el-button
                link
                type="primary"
                icon="View"
                @click="openViewDevice(scope.row)"
                v-hasPermi="['system:user:remove']"
                >查看</el-button
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
    <device-dialog
      ref="deviceRef"
      :title="title"
      :item="currentData"
      @submit="getList()"
    />
    <!-- <device-view-dialog
      ref="deviceViewRef"
      :item="viewData"
      :viewFlg="viewFlg"
    /> -->
  </div>
</template>

<script setup name="Device">
import {
  getDeviceList,
  stopDevice,
  startDevice,
  getDiseaseTreatmentInit,
  getDeviceOne,
} from "./components/device";
import deviceDialog from "./components/deviceDialog";
import deviceViewDialog from "./components/deviceViewDialog";
import { nextTick } from "vue";

const router = useRouter();
const { proxy } = getCurrentInstance();
const { sys_normal_disable, sys_user_sex } = proxy.useDict(
  "sys_normal_disable",
  "sys_user_sex"
);

const deviceList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]); // 存储选择的行数据
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const deviceCategories = ref(undefined);
const statusFlagOptions = ref(undefined);
const exeOrganizations = ref(undefined);
// 使用 ref 定义当前器材数据
const currentData = ref({});
// 使用 ref 定义当前查看器材数据
const viewData = ref({});

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
    categoryEnum: undefined, // 目录分类
  },
  rules: {
    // name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
    // conditionCode: [
    //   { required: true, message: "编码不能为空", trigger: "blur" },
    // ],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 通过条件过滤节点  */
const filterNode = (value, data) => {
  if (!value) return true;
  return data.label.indexOf(value) !== -1;
};

/** 器材目录分类查询下拉树结构 */
function getDiseaseTreatmentList() {
  getDiseaseTreatmentInit().then((response) => {
    console.log(response, "response器材目录分类查询下拉树结构");
    deviceCategories.value = response.data.deviceCategories;
    statusFlagOptions.value = response.data.statusFlagOptions;
    exeOrganizations.value = response.data.exeOrganizations;
  });
}
/** 查询器材目录列表 */
function getList() {
  console.log(getList, "getList");
  loading.value = true;
  getDeviceList(queryParams.value).then((res) => {
    loading.value = false;
    deviceList.value = res.data.records;
    total.value = res.data.total;
  });
}
/** 节点单击事件 */
function handleNodeClick(data) {
  queryParams.value.categoryEnum = data.value;
  handleQuery();
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNo = 1;
  getList();
}

/** 启用按钮操作 */
function handleStart() {
  const stardIds = ids.value;
  //   selectedData
  proxy.$modal
    .confirm("是否确定启用数据！")
    .then(function () {
      return startDevice(stardIds);
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
  proxy.$modal
    .confirm("是否确认停用数据！")
    .then(function () {
      return stopDevice(stopIds);
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
  console.log(selection, "selection");
  // selectedData.value = selection.map((item) => ({ ...item })); // 存储选择的行数据
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download(
    "system/user/importTemplate",
    {},
    `user_template_${new Date().getTime()}.xlsx`
  );
}

/** 打开新增弹窗 */
function openAddDevice() {
  console.log("打开新增弹窗");
  title.value = "新增";
  nextTick(() => {
    proxy.$refs.deviceRef.show();
  });
}
/** 打开编辑弹窗 */
function openEditDevice(row) {
  currentData.value = {};
  console.log("打开编辑弹窗");
  currentData.value = JSON.parse(JSON.stringify(row));
  console.log(currentData.value, "currentData");
  currentData.value.hvcmFlag == 1
    ? (currentData.value.hvcmFlag = true)
    : (currentData.value.hvcmFlag = false);
  currentData.value.ybFlag == 1
    ? (currentData.value.ybFlag = true)
    : (currentData.value.ybFlag = false);
  currentData.value.ybMatchFlag == 1
    ? (currentData.value.ybMatchFlag = true)
    : (currentData.value.ybMatchFlag = false);
  currentData.value.allergenFlag == 1
    ? (currentData.value.allergenFlag = true)
    : (currentData.value.allergenFlag = false);
  console.log(currentData.value, "currentDataform");

  title.value = "编辑";
  // 确保子组件已经接收到最新的 props
  nextTick(() => {
    proxy.$refs["deviceRef"].edit();
  });
  // proxy.$refs["deviceRef"].edit();
}
/** 打开查看弹窗 */
function openViewDevice(row) {
  // viewData.value = row;
  getDeviceOne(row.id).then((response) => {
    currentData.value = response.data;
    currentData.value.hvcmFlag == 1
      ? (currentData.value.hvcmFlag = true)
      : (currentData.value.hvcmFlag = false);
    currentData.value.ybFlag == 1
      ? (currentData.value.ybFlag = true)
      : (currentData.value.ybFlag = false);
    currentData.value.ybMatchFlag == 1
      ? (currentData.value.ybMatchFlag = true)
      : (currentData.value.ybMatchFlag = false);
    currentData.value.allergenFlag == 1
      ? (currentData.value.allergenFlag = true)
      : (currentData.value.allergenFlag = false);
    title.value = "查看";
    nextTick(() => {
      proxy.$refs["deviceRef"].edit();
    });
    getList();
  });
  // console.log(viewData.value, "currentData");
  // // 确保子组件已经接收到最新的 props
  // nextTick(() => {
  //   proxy.$refs["deviceViewRef"].edit();
  // });
  // proxy.$refs["deviceRef"].edit();
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
</style>
