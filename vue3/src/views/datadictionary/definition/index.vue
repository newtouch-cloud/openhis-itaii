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
            <!-- <el-col :span="4" style="width: 20%"> -->
              <el-form-item
                label-width="100"
                label="财务类别"
         
                prop="chargeItem"
              >
              <el-select
                v-model="queryParams.typeCode"
                placeholder="请选择财务类别"
                clearable
             
                :disabled="editShow"
                @change="handleQuery"
              >
                <el-option
                  v-for="dict in fin_type_code"
                  :key="dict.value" :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
              </el-form-item>
            <!-- </el-col> -->
            <!-- <el-col :span="4" style="width: 20%"> -->
              <el-form-item
                label-width="100"
                label="状态"
          
                prop="chargeItem"
              >
              <el-select
                v-model="queryParams.statusEnum"
                placeholder="请选择状态"
                clearable
         
                :disabled="editShow"
                @change="handleQuery"
              >
                <el-option
                  v-for="dict in options"
                  :key="dict.value" :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
              </el-form-item>
            <!-- </el-col> -->
            <!-- <el-col :span="4" style="width: 20%"> -->
              <el-form-item
                label-width="100"
                label="名称"
            
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
            <!-- </el-col> -->
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
              label="项目名称"
              width="200"
              prop="chargeName"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.chargeName ? scope.row.chargeName : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="所属科室"
              width="200"
              prop="orgId_dictText"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.orgId_dictText ? scope.row.orgId_dictText : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="财务类别"
              width="200"
              prop=" typeCode_dictText"
              align="center"
            >
              <template #default="scope">
                {{
                  scope.row.typeCode_dictText
                    ? scope.row.typeCode_dictText
                    : "-"
                }}
              </template>
            </el-table-column>
            <el-table-column
              label="医保类别"
              width="200"
              prop="ybType_dictText"
              align="center"
            >
              <template #default="scope">
                {{
                  scope.row.ybType_dictText ? scope.row.ybType_dictText : "-"
                }}
              </template>
            </el-table-column>
            <el-table-column
              label="基础价格"
              width="200"
              prop="price"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.price ? thousandNumber(scope.row.price) : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="费用明细个数"
              width="200"
              prop="detailCount"
              align="center"
            >
              <template #default="scope">
                <div v-if="scope.row.detailCount != 0">
                  <el-button
                    link
                    type="primary"
                    @click="handleDetails(scope.row)"
                    >{{ thousandNumber(scope.row.detailCount) }}</el-button
                  >
                </div>
                <div v-else>
                  {{ scope.row.detailCount == 0 ? "0" : "-" }}
                </div>
              </template>
            </el-table-column>
            <el-table-column
              label="状态"
              width="200"
              prop="statusEnum_enumText"
              align="center"
            >
              <template #default="scope">
                {{
                  scope.row.statusEnum_enumText
                    ? scope.row.statusEnum_enumText
                    : "-"
                }}
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
          <pagination
            v-show="total > 0"
            :total="total"
            v-model:page="queryParams.pageNo"
            v-model:limit="queryParams.pageSize"
            @pagination="getList"
          />
        </el-tab-pane>
        <el-tab-pane label="器具定价" name="2">
          <el-row :gutter="16">
            <!-- <el-col :span="4" style="width: 20%"> -->
              <el-form-item
                label-width="100"
                label="财务类别"
       
                prop="chargeItem"
              >
              <el-select
                v-model="queryParams.typeCode"
                placeholder="请选择财务类别"
                clearable
           
                :disabled="editShow"
                @change="handleQuery"
              >
                <el-option
                  v-for="dict in fin_type_code"
                  :key="dict.value" :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
              </el-form-item>
            <!-- </el-col> -->
            <!-- <el-col :span="4" style="width: 20%"> -->
              <el-form-item
                label-width="100"
                label="状态"
     
                prop="chargeItem"
              >
              <el-select
                v-model="queryParams.statusEnum"
                placeholder="请选择状态"
                clearable
        
                :disabled="editShow"
                @change="handleQuery"
              >
                <el-option
                  v-for="dict in options"
                  :key="dict.value" :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
              </el-form-item>
            <!-- </el-col> -->
            <!-- <el-col :span="4" style="width: 20%"> -->
              <el-form-item
                label-width="100"
                label="名称"
         
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
            <!-- </el-col> -->
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
              label="项目名称"
              width="200"
              prop="chargeName"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.chargeName ? scope.row.chargeName : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="所属科室"
              width="200"
              prop="orgId_dictText"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.orgId_dictText ? scope.row.orgId_dictText : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="财务类别"
              width="200"
              prop=" typeCode_dictText"
              align="center"
            >
              <template #default="scope">
                {{
                  scope.row.typeCode_dictText
                    ? scope.row.typeCode_dictText
                    : "-"
                }}
              </template>
            </el-table-column>
            <el-table-column
              label="医保类别"
              width="200"
              prop="ybType_dictText"
              align="center"
            >
              <template #default="scope">
                {{
                  scope.row.ybType_dictText ? scope.row.ybType_dictText : "-"
                }}
              </template>
            </el-table-column>
            <el-table-column
              label="基础价格"
              width="200"
              prop="price"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.price ? thousandNumber(scope.row.price) : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="费用明细个数"
              width="200"
              prop="detailCount"
              align="center"
            >
              <template #default="scope">
                <div v-if="scope.row.detailCount != 0">
                  <el-button
                    link
                    type="primary"
                    @click="handleDetails(scope.row)"
                    >{{ thousandNumber(scope.row.detailCount) }}</el-button
                  >
                </div>
                <div v-else>
                  {{ scope.row.detailCount == 0 ? "0" : "-" }}
                </div>
              </template>
            </el-table-column>
            <el-table-column
              label="状态"
              width="200"
              prop="statusEnum_enumText"
              align="center"
            >
              <template #default="scope">
                {{
                  scope.row.statusEnum_enumText
                    ? scope.row.statusEnum_enumText
                    : "-"
                }}
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
          <pagination
            v-show="total > 0"
            :total="total"
            v-model:page="queryParams.pageNo"
            v-model:limit="queryParams.pageSize"
            @pagination="getList"
          />
        </el-tab-pane>
        <el-tab-pane label="活动定价" name="3">
          <el-row :gutter="16">
            <!-- <el-col :span="4" style="width: 20%"> -->
              <el-form-item
                label-width="100"
                label="财务类别"
        
                prop="chargeItem"
              >
              <el-select
                v-model="queryParams.typeCode"
                placeholder="请选择财务类别"
                clearable
        
                :disabled="editShow"
                @change="handleQuery"
              >
                <el-option
                  v-for="dict in fin_type_code"
                  :key="dict.value" :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
              </el-form-item>
            <!-- </el-col> -->
            <!-- <el-col :span="4" style="width: 20%"> -->
              <el-form-item
                label-width="100"
                label="状态"
           
                prop="chargeItem"
              >
              <el-select
                v-model="queryParams.statusEnum"
                placeholder="请选择状态"
                clearable
        
                :disabled="editShow"
                @change="handleQuery"
              >
                <el-option
                  v-for="dict in options"
                  :key="dict.value" :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
              </el-form-item>
            <!-- </el-col> -->
            <!-- <el-col :span="4" style="width: 20%"> -->
              <el-form-item
                label-width="100"
                label="名称"
             
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
            <!-- </el-col> -->
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
              label="项目名称"
              width="200"
              prop="chargeName"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.chargeName ? scope.row.chargeName : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="所属科室"
              width="200"
              prop="orgId_dictText"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.orgId_dictText ? scope.row.orgId_dictText : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="财务类别"
              width="200"
              prop=" typeCode_dictText"
              align="center"
            >
              <template #default="scope">
                {{
                  scope.row.typeCode_dictText
                    ? scope.row.typeCode_dictText
                    : "-"
                }}
              </template>
            </el-table-column>
            <el-table-column
              label="医保类别"
              width="200"
              prop="ybType_dictText"
              align="center"
            >
              <template #default="scope">
                {{
                  scope.row.ybType_dictText ? scope.row.ybType_dictText : "-"
                }}
              </template>
            </el-table-column>
            <el-table-column
              label="基础价格"
              width="200"
              prop="price"
              align="center"
            >
              <template #default="scope">
                {{ scope.row.price ? thousandNumber(scope.row.price) : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              label="费用明细个数"
              width="200"
              prop="detailCount"
              align="center"
            >
              <template #default="scope">
                <div v-if="scope.row.detailCount != 0">
                  <el-button
                    link
                    type="primary"
                    @click="handleDetails(scope.row)"
                    >{{ thousandNumber(scope.row.detailCount) }}</el-button
                  >
                </div>
                <div v-else>
                  {{ scope.row.detailCount == 0 ? "0" : "-" }}
                </div>
              </template>
            </el-table-column>
            <el-table-column
              label="状态"
              width="200"
              prop="statusEnum_enumText"
              align="center"
            >
              <template #default="scope">
                {{
                  scope.row.statusEnum_enumText
                    ? scope.row.statusEnum_enumText
                    : "-"
                }}
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
          <pagination
            v-show="total > 0"
            :total="total"
            v-model:page="queryParams.pageNo"
            v-model:limit="queryParams.pageSize"
            @pagination="getList"
          />
        </el-tab-pane>
      </el-tabs>
    </el-form>
    <el-dialog
      :title="title"
      v-model="openDetails"
      width="600px"
      append-to-body
    >
      <el-table
        v-loading="detailLoading"
        :data="definitionDetailList"
        tooltip-effect="dark"
        :show-overflow-tooltip="true"
      >
        <el-table-column
          label="条件"
          prop="conditionCode_enumText"
          align="center"
        >
          <template #default="scope">
            {{
              scope.row.conditionCode_enumText
                ? scope.row.conditionCode_enumText
                : "-"
            }}
          </template>
        </el-table-column>
        <el-table-column label="价格" width="200" prop="amount" align="center">
          <template #default="scope">
            {{ scope.row.amount ? scope.row.amount : "-" }}
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
    <edit
      :title="title"
      :open="open"
      :formData="form"
      @submit="submitForm"
      @update:open="handleOpenChange"
      @update:form="handleFormChange"
    />
  </div>
</template>
<script setup>
import {
  listDefinition,
  initOption,
  updateDefinition,
  getDetail,
} from "./components/definition";
import Edit from "./components/edit.vue";
import moment from "moment";
import { thousandNumber } from "@/utils/his.js";

const activeName = ref("1");
const showSearch = ref("true");
const loading = ref(true);
const detailLoading = ref(true);
const definitionList = ref([]);
const definitionDetailList = ref([]);
const total = ref(0);

const { proxy } = getCurrentInstance();
const options = ref([]);
const title = ref("");
const open = ref(false);
const openDetails = ref(false);
const { fin_type_code } = proxy.useDict("fin_type_code");

const data = reactive({
  form: {},
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
  queryParams.value.chargeItemContext = activeName.value;
  listDefinition(queryParams.value).then((response) => {
    definitionList.value = response.data.records;
    total.value = response.data.total;
    loading.value = false;
  });
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
    options.value = response.data.publicationStatusOptions;
  });
}

const handleOpenChange = (value) => {
  open.value = value;
};

function handleDetails(row) {
  getDetail(row.id).then((res) => {
    if (res.code == 200) {
      definitionDetailList.value = res.data;
      openDetails.value = true;
      detailLoading.value = false;
      title.value = "明细详情";
    }
  });
}

const handleFormChange = (newForm) => {
  0;
  form.value = { ...newForm };
};

/** 提交按钮 */
function submitForm(form) {
  updateDefinition(form).then((response) => {
    proxy.$modal.msgSuccess("操作成功");
    open.value = false;
    getList();
  });
}

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
.el-select{
  width: 150px!important;
}
</style>
