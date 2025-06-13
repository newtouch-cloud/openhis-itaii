<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--诊疗目录-->
      <el-col :span="4" :xs="24">
        <div class="head-title">取药科室</div>
        <div>
          <el-tree
            :data="pharmacyDepartmentOptions"
            :props="{ label: 'label', children: 'children' }"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="treeRef"
            node-key="id"
            check-strictly
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>
      <!--诊疗目录-->
      <el-col :span="20" :xs="24">
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="Plus"
              @click="openAddPharmacyDepartment"
              v-hasPermi="['system:user:add']"
              >添加新项目</el-button
            >
          </el-col>
        </el-row>

        <el-table
          v-loading="loading"
          :data="diagnosisTreatmentList"
          @selection-change="handleSelectionChange"
        >
          <!-- <el-table-column type="selection" width="50" align="center" /> -->
          <el-table-column
            label="取药科室"
            align="center"
            key="name"
            prop="name"
            :show-overflow-tooltip="true"
          >
            <template #default="scope">
              <div style="display: flex; align-items: center; justify-content: center">
                <el-select
                  v-model="scope.row.organizationId"
                  placeholder="请选择"
                  :class="{ 'error-border': scope.row.error }"
                  clearable
                >
                  <el-option
                    v-for="item in departmentOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </template>
          </el-table-column>
          <el-table-column
            label="药品类别"
            align="center"
            key="pyStr"
            prop="pyStr"
            :show-overflow-tooltip="true"
            width="300"
          >
            <template #default="scope">
              <div style="display: flex; align-items: center; justify-content: center">
                <el-select
                  v-model="scope.row.distributionCategoryCode"
                  placeholder="药品类别"
                  clearable
                  style="width: 200px"
                  :class="{ 'error-border': scope.row.error }"
                >
                  <el-option
                    v-for="dict in distribution_category_code"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                </el-select>
              </div>
            </template>
          </el-table-column>
          <el-table-column
            label="开始时间"
            align="center"
            key="wbStr"
            prop="wbStr"
            :show-overflow-tooltip="true"
            width="300"
          >
            <template #default="scope">
              <el-time-picker
                v-model="scope.row.startTime"
                placeholder="选择时间"
                format="HH:mm:ss"
                value-format="HH:mm:ss"
              >
              </el-time-picker>
            </template>
          </el-table-column>
          <el-table-column
            label="结束时间"
            align="center"
            key="categoryCode_dictText"
            prop="categoryCode_dictText"
            :show-overflow-tooltip="true"
            width="300"
          >
            <template #default="scope">
              <el-time-picker
                v-model="scope.row.endTime"
                placeholder="选择时间"
                format="HH:mm:ss"
                value-format="HH:mm:ss"
              >
              </el-time-picker>
            </template>
          </el-table-column>
          <!-- <el-table-column label="备注" align="center" key="typeEnum_enumText" prop="typeEnum_enumText"
            :show-overflow-tooltip="true" width="300">
            <template #default="scope">
              <el-input v-model="scope.row.detailJson" placeholder="" />
            </template>
          </el-table-column> -->
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
                @click="openSavePharmacyDepartment(scope.row, scope.$index)"
                v-hasPermi="['system:user:edit']"
                >保存</el-button
              >
              <el-button
                type="danger"
                link
                icon="Delete"
                :disabled="scope.row.id == ''"
                @click="deleteSelectedRows(scope.row)"
              >
                删除
              </el-button>
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
  </div>
</template>
  
<script setup name="pharmacyDepartment">
import {
  getPharmacyDepartmentList,
  getDiseaseTreatmentInit,
  getPharmacyDepartmentOne,
  getDiseaseTreatmentInitLoc,
  addPharmacyDepartment,
  editPharmacyDepartment,
  deletePharmacyDepartment,
} from './components/pharmacyDepartment';
const { proxy } = getCurrentInstance();
const { distribution_category_code } = proxy.useDict('distribution_category_code');
import { nextTick } from 'vue';

const diagnosisTreatmentList = ref([]);
const loading = ref(false);
const showSearch = ref(true);
const ids = ref([]); // 存储选择的行数据
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref('');
const pharmacyDepartmentOptions = ref(undefined);
const currentCategoryEnum = ref('');
const departmentOptions = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
  },
  tableRules: {
    organizationId: [{ required: true, message: '取药科室不能为空', trigger: 'blur' }],
    distributionCategoryCode: [{ required: true, message: '药品类别不能为空', trigger: 'blur' }],
  },
});

const { queryParams, form, tableRules } = toRefs(data);

/** 通过条件过滤节点  */
const filterNode = (value, data) => {
  if (!value) return true;
  return data.label.indexOf(value) !== -1;
};

/** 目录分类查询 */
function getDiseaseTreatmentList() {
  getDiseaseTreatmentInit().then((response) => {
    console.log(response, 'response诊疗目录分类查询下拉树结构');
    pharmacyDepartmentOptions.value = response.data.locationFormOptions;
    departmentOptions.value = response.data.departmentOptions;
    // 调用 getDiseaseInitLoc 并在 then 回调中赋值
    getDiseaseInitLoc(response.data.locationFormOptions[0].value).then((children) => {
      pharmacyDepartmentOptions.value[0].children = children;
    });

    getDiseaseInitLoc(response.data.locationFormOptions[1].value).then((children) => {
      pharmacyDepartmentOptions.value[1].children = children;
    });

    console.log(pharmacyDepartmentOptions.value);
  });
}

function getDiseaseInitLoc(id) {
  return getDiseaseTreatmentInitLoc(id).then((response) => {
    return response.data.locationOptions;
  });
}

/** 获取绑定科室列表 */
function getList() {
  loading.value = true;
  getPharmacyDepartmentList(queryParams.value).then((res) => {
    loading.value = false;
    if (res.code === 200) {
      diagnosisTreatmentList.value =
        res.data.records.length > 0
          ? res.data.records.map((res) => {
              return {
                ...res,
                isEditing: false, // 标记当前行是否正在编辑
                error: false, // 新增 error 字段
              };
            })
          : [];
    }
    console.log(diagnosisTreatmentList.value, 'res.data');
    total.value = res.data.total;
  });
}
/** 节点单击事件 */
function handleNodeClick(data, node) {
  // 检查节点是否有子节点
  if (node.data.children && node.data.children.length > 0) {
    proxy.$message.warning('不能选择父节点');
    return;
  }
  queryParams.value.defLocationId = data.value;
  currentCategoryEnum.value = data.value;
  handleQuery();
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNo = 1;
  console.log(queryParams, 'queryParams搜索');
  getList();
}

/** 选择条数  */
function handleSelectionChange(selection) {
  console.log(selection, 'selection');
  // selectedData.value = selection.map((item) => ({ ...item })); // 存储选择的行数据
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

function openAddPharmacyDepartment() {
  if (data.isAdding) {
    proxy.$message.warning('请先保存当前行后再新增！');
    return;
  }
  const newRow = {
    id: '',
    organizationId: '',
    defLocationId: '',
    distributionCategoryCode: '',
    startTime: '00:00:00',
    endTime: '23:59:59',
    isEditing: true, // 标记当前行是否正在编辑
    error: false, // 新增 error 字段
  };
  diagnosisTreatmentList.value.push(newRow);
  total.value = diagnosisTreatmentList.value.length;
  data.isAdding = true; // 设置标志位为 true，表示有未保存的
}

function handleBlur(row, index) {
  console.log(row);
  let hasError = false;
  const fields = ['organizationId', 'distributionCategoryCode'];

  fields.forEach((field) => {
    if (!row[field]) {
      hasError = true;
      console.log(tableRules.value[field]);
      proxy.$message.error(tableRules.value[field][0].message);
    }
  });

  console.log(hasError);

  row.error = hasError;
}

function openSavePharmacyDepartment(row) {
  console.log(row, 'row');
  let params = { ...row };
  let hasError = false;
  // this.purchaseinventoryList.forEach((row) => {
  handleBlur(row);
  if (row.error) {
    hasError = true;
  }
  // });

  if (hasError) {
    proxy.$message.error('请填写完整信息');
    return;
  }
  if (currentCategoryEnum.value) {
    params.defLocationId = currentCategoryEnum.value;
  }
  if (row.id) {
    editPharmacyDepartment(params).then((res) => {
      data.isAdding = false; // 允许新增下一行
      proxy.$message.success('保存成功！');
      console.log(res, 'res');
    });
  } else {
    delete params.id;
    addPharmacyDepartment(params).then((res) => {
      console.log(res, 'res');
      proxy.$message.success('保存成功！');
    });
  }
}

function deleteSelectedRows(row) {
  deletePharmacyDepartment({ orgLocId: row.id }).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess('删除成功');
      getList();
    }
  });
}

getDiseaseTreatmentList();
</script>
<style scoped>
.el-form--inline .el-form-item {
  display: inline-flex;
  vertical-align: middle;
  margin-right: 10px !important;
}
.error-border {
  border: 1px solid red;
}
</style>
  