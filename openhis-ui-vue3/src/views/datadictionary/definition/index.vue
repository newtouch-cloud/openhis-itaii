<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryRef"
      :inline="true"
      v-show="showSearch"
      label-width="90px"
    >
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <el-tab-pane label="药品定价" name="1">
          <el-row :gutter="16">
            <el-col :span="4" style="width: 20%">
              <el-form-item
                label-width="100"
                label="收费项目"
                style="width: 100%"
                prop="chargeItem"
              >
                <el-select
                  v-model="queryParams.chargeItem"
                  placeholder="请选择收费项目"
                  size="large"
                  style="width: 240px"
                  @change="handleQuery"
                >
                  <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4" style="width: 20%">
              <el-form-item
                label-width="100"
                label="名称"
                style="width: 100%"
                prop="searchKey"
              >
                <el-input
                  v-model="queryParams.searchKey"
                  placeholder="名称/编码/拼音"
                  clearable
                  @keyup.enter="handleQuery"
                  @blur="handleQuery"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-table
            v-loading="loading"
            :data="definitionList"
            tooltip-effect="dark"
            :show-overflow-tooltip="true"
          >
            <el-table-column
              type="selection"
              width="40"
              align="center"
              fixed="left"
            />
            <el-table-column
              label="项目编号"
              width="150"
              prop="itemNo"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.itemNo ? scope.row.itemNo : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="项目名称"
              width="150"
              prop="chargeName"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.chargeName ? scope.row.chargeName : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="规格"
              width="150"
              prop="totalVolume"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.totalVolume ? scope.row.totalVolume : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="单位"
              width="150"
              prop="unitCode"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.unitCode ? scope.row.unitCode : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="拆零比"
              width="150"
              prop="partPercent"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.partPercent ? thousandNumber(scope.row.partPercent) : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="指导价"
              width="150"
              prop="conditionYbCode"
              align="center"
            >
              <template #default="scope">
                {{
                  scope.row.conditionYbCode ? thousandNumber(scope.row.conditionYbCode) : "-"
                }}
              </template>
            </el-table-column>
            <el-table-column
              label="实际价格"
              width="150"
              prop="amount"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.amount ? thousandNumber(scope.row.amount) : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="拆零最小单位"
              width="150"
              prop="partMinUnitCode"
              align="center"
            >
              <template #default="scope">
                {{
                  scope.row.partMinUnitCode ? scope.row.partMinUnitCode : "-"
                }}
              </template>
            </el-table-column>
            <el-table-column
              label="拆零指导价"
              width="150"
              prop="partConditionPrice"
              align="center"
            >
              <template #default="scope">
                {{
                  scope.row.partConditionPrice
                    ? thousandNumber(scope.row.partConditionPrice)
                    : "-"
                }}
              </template>
            </el-table-column>
            <el-table-column
              label="拆零价格"
              width="150"
              prop="price"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.price ? thousandNumber(scope.row.price) : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="调价说明"
              width="150"
              prop="description"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.description ? scope.row.description : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="调价时间"
              width="200"
              prop="updateTime"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.updateTime ? moment(scope.row.updateTime).format("YYYY-MM-DD HH:mm:ss") : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="状态"
              width="150"
              prop="statusEnumText"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.statusEnumText ? scope.row.statusEnumText : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              min-width="290"
              label="操作"
              align="center"
              class-name="small-padding fixed-width"
              fixed="right"
            >
              <template #default="scope">
                <el-button link type="primary" @click="handleUpdate(scope.row)"
                  >修改</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNo"
            v-model:limit="queryParams.pageSize" @pagination="getList" />
        </el-tab-pane>
        <el-tab-pane label="器具定价" name="2">
          <el-row :gutter="16">
            <el-col :span="4" style="width: 20%">
              <el-form-item
                label-width="100"
                label="收费项目"
                style="width: 100%"
                prop="chargeItem"
              >
                <el-select
                  v-model="queryParams.chargeItem"
                  placeholder="请选择收费项目"
                  size="large"
                  style="width: 240px"
                  @change="handleQuery"
                >
                  <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4" style="width: 20%">
              <el-form-item
                label-width="100"
                label="名称"
                style="width: 100%"
                prop="searchKey"
              >
                <el-input
                  v-model="queryParams.searchKey"
                  placeholder="名称/编码/拼音"
                  clearable
                  @keyup.enter="handleQuery"
                  @blur="handleQuery"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-table
            v-loading="loading"
            :data="definitionList"
            tooltip-effect="dark"
            :show-overflow-tooltip="true"
          >
            <el-table-column
              type="selection"
              width="40"
              align="center"
              fixed="left"
            />
            <el-table-column
              label="项目编号"
              width="150"
              prop="itemNo"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.itemNo ? scope.row.itemNo : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="项目名称"
              width="150"
              prop="chargeName"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.chargeName ? scope.row.chargeName : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="规格"
              width="150"
              prop="totalVolume"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.totalVolume ? scope.row.totalVolume : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="单位"
              width="150"
              prop="unitCode"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.unitCode ? scope.row.unitCode : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="拆零比"
              width="150"
              prop="partPercent"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.partPercent ? thousandNumber(scope.row.partPercent) : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="指导价"
              width="150"
              prop="conditionYbCode"
              align="center"
            >
              <template #default="scope">
                {{
                  scope.row.conditionYbCode ? thousandNumber(scope.row.conditionYbCode) : "-"
                }}
              </template>
            </el-table-column>
            <el-table-column
              label="价格"
              width="150"
              prop="price"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.price ? thousandNumber(scope.row.price) : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="拆零最小单位"
              width="150"
              prop="partMinUnitCode"
              align="center"
            >
              <template #default="scope">
                {{
                  scope.row.partMinUnitCode ? scope.row.partMinUnitCode : "-"
                }}
              </template>
            </el-table-column>
            <el-table-column
              label="拆零指导价"
              width="150"
              prop="partConditionPrice"
              align="center"
            >
              <template #default="scope">
                {{
                  scope.row.partConditionPrice
                    ? thousandNumber(scope.row.partConditionPrice)
                    : "-"
                }}
              </template>
            </el-table-column>
            <el-table-column
              label="拆零价格"
              width="150"
              prop="price"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.price ? thousandNumber(scope.row.price) : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="调价说明"
              width="150"
              prop="description"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.description ? scope.row.description : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="调价时间"
              width="200"
              prop="updateTime"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.updateTime ? moment(scope.row.updateTime).format("YYYY-MM-DD HH:mm:ss") : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="状态"
              width="150"
              prop="statusEnumText"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.statusEnumText ? scope.row.statusEnumText : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              min-width="290"
              label="操作"
              align="center"
              class-name="small-padding fixed-width"
              fixed="right"
            >
              <template #default="scope">
                <el-button link type="primary" @click="handleUpdate(scope.row)"
                  >修改</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNo"
            v-model:limit="queryParams.pageSize" @pagination="getList" />
        </el-tab-pane>
        <el-tab-pane label="活动定价" name="3">
          <el-row :gutter="16">
            <el-col :span="4" style="width: 20%">
              <el-form-item
                label-width="100"
                label="收费项目"
                style="width: 100%"
                prop="chargeItem"
              >
                <el-select
                  v-model="queryParams.chargeItem"
                  placeholder="请选择收费项目"
                  size="large"
                  style="width: 240px"
                  @change="handleQuery"
                >
                  <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4" style="width: 20%">
              <el-form-item
                label-width="100"
                label="名称"
                style="width: 100%"
                prop="searchKey"
              >
                <el-input
                  v-model="queryParams.searchKey"
                  placeholder="名称/编码/拼音"
                  clearable
                  @keyup.enter="handleQuery"
                  @blur="handleQuery"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-table
            v-loading="loading"
            :data="definitionList"
            tooltip-effect="dark"
            :show-overflow-tooltip="true"
          >
            <el-table-column
              type="selection"
              width="40"
              align="center"
              fixed="left"
            />
            <el-table-column
              label="项目编号"
              width="150"
              prop="itemNo"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.itemNo ? scope.row.itemNo : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="项目名称"
              width="150"
              prop="chargeName"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.chargeName ? scope.row.chargeName : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="规格"
              width="150"
              prop="totalVolume"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.totalVolume ? scope.row.totalVolume : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="单位"
              width="150"
              prop="unitCode"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.unitCode ? scope.row.unitCode : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="拆零比"
              width="150"
              prop="partPercent"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.partPercent ? thousandNumber(scope.row.partPercent) : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="指导价"
              width="150"
              prop="conditionYbCode"
              align="center"
            >
              <template #default="scope">
                {{
                  scope.row.conditionYbCode ? thousandNumber(scope.row.conditionYbCode) : "-"
                }}
              </template>
            </el-table-column>
            <el-table-column
              label="价格"
              width="150"
              prop="price"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.price ? thousandNumber(scope.row.price) : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="拆零最小单位"
              width="150"
              prop="partMinUnitCode"
              align="center"
            >
              <template #default="scope">
                {{
                  scope.row.partMinUnitCode ? scope.row.partMinUnitCode : "-"
                }}
              </template>
            </el-table-column>
            <el-table-column
              label="拆零指导价"
              width="150"
              prop="partConditionPrice"
              align="center"
            >
              <template #default="scope">
                {{
                  scope.row.partConditionPrice
                    ? thousandNumber(scope.row.partConditionPrice)
                    : "-"
                }}
              </template>
            </el-table-column>
            <el-table-column
              label="拆零价格"
              width="150"
              prop="price"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.price ? thousandNumber(scope.row.price) : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="调价说明"
              width="150"
              prop="description"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.description ? scope.row.description : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="调价时间"
              width="200"
              prop="updateTime"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.updateTime ? moment(scope.row.updateTime).format("YYYY-MM-DD HH:mm:ss") : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="状态"
              width="150"
              prop="statusEnumText"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.statusEnumText ? scope.row.statusEnumText : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              min-width="290"
              label="操作"
              align="center"
              class-name="small-padding fixed-width"
              fixed="right"
            >
              <template #default="scope">
                <el-button link type="primary" @click="handleUpdate(scope.row)"
                  >修改</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNo"
            v-model:limit="queryParams.pageSize" @pagination="getList" />
        </el-tab-pane>
      </el-tabs>
    </el-form>
    <edit :title="title" :open="open" :statusOptions="statusOptions" :formData="form" @submit="submitForm" @update:open="handleOpenChange"
      @update:form="handleFormChange" />
  </div>
</template>
<script setup>
import { listDefinition, initOption, updateDefinition, getOptions } from "./components/definition";
import Edit from './components/edit.vue'
import moment from 'moment'
import { thousandNumber } from '@/utils/his.js'

const activeName = ref("1");
const showSearch = ref("true");
const loading = ref(true);
const definitionList = ref([]);
const total = ref(0);

const { proxy } = getCurrentInstance();
const options = ref([]);
const statusOptions = ref([]);
const title = ref("");
const open = ref(false);

const data = reactive({
  form:{},
  queryParams: {
    search: "",
    definitionType: "",
    chargeItem: "",
    searchKey: "",
    pageNo: 1,
    pageSize: 10,
  },
});

const { queryParams, form } = toRefs(data);
const handleClick = (tab, event) => {
  console.log(tab, event);
  activeName.value = tab.props.name;
  queryParams.value.pageNo = 1;
  handleInit();
  getList();
};

/** 查询委托单信息列表 */
function getList() {
  loading.value = true;
  queryParams.value.definitionType = activeName.value;
  listDefinition(queryParams.value).then((response) => {
    definitionList.value = response.data.records;
    total.value = response.data.total;
    loading.value = false;
  });
  getStatusOptions()
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNo = 1;
  getList();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    itemNo: null,
    chargeName: null,
    totalVolume: null,
    unitCode: null,
    partPercent: null,
    conditionYbCode: null,
    price: null,
    amount: null,
    partMinUnitCode: null,
    partConditionPrice: null,
    partPrice: null,
    description: null,
    statusEnum: null,
    itemId: null,
  };
  proxy.resetForm("einfoRef");
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  form.value = row;
  open.value = true;
  title.value = "修改项目定价";
}
/** 搜索按钮操作 */
function handleInit() {
  queryParams.value.definitionType = activeName.value;
  initOption(queryParams.value).then((response) => {
    options.value = response.data;
  });
}

const handleOpenChange = (value) => {
  open.value = value;
};

const handleFormChange = (newForm) => {
  form.value = { ...newForm };
};

/** 提交按钮 */
function submitForm(form) {
  updateDefinition(form).then(response => {
    proxy.$modal.msgSuccess("操作成功");
    open.value = false;
    getList();
  });
}

/**获取状态下拉列表 */
const getStatusOptions = () => {
  getOptions({}).then((response) => {
    statusOptions.value = response.data;
  });
};

handleInit();
getList();
</script>
<style lang="scss" scoped>
:deep(.demo-tabs > .el-tabs__content) {
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}
:deep(.el-input__wrapper) {
  height: 32px;
}
:deep(.el-input__inner) {
  height: 30px;
}
:deep(.el-tabs__content) {
  height: 80vh;
}
</style>