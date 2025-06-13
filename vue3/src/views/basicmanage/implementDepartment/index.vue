<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--诊疗目录-->
      <el-col :span="4" :xs="24">
        <div class="head-title">执行科室配置</div>
        <div class="head-container">
          <el-tree
            :data="organization"
            :props="{ label: 'name', children: 'children' }"
            :expand-on-click-node="true"
            :filter-node-method="filterNode"
            ref="treeRef"
            node-key="id"
            highlight-current
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
              :disabled="isAddDisable"
              @click="handleAddItem"
              v-hasPermi="['system:user:add']"
              >添加新项目</el-button
            >
          </el-col>
        </el-row>

        <el-table
          v-loading="loading"
          :data="catagoryList"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="100" align="center" />
          <el-table-column label="诊疗目录" align="center" :show-overflow-tooltip="true">
            <template #default="scope">
              <div style="display: flex; align-items: center; justify-content: center">
                <el-select
                  v-model="scope.row.activityCategoryCode"
                  placeholder="请选择"
                  ref="dropdown"
                  @change="DiagnosisTreatmentList(scope.row,scope.$index,1)"
                  :class="{ 'error-border': scope.row.error }"
                  clearable
                >
                  <el-option
                    v-for="dict in catagoryDicts"
                    :key="dict.value"
                    :label="dict.info"
                    :value="dict.value"
                  />
                </el-select>
              </div>
            </template>
          </el-table-column>
          <el-table-column
            label="项目名称"
            align="center"
            :show-overflow-tooltip="true"
            width="300"
          >
            <template #default="scope">
              <div style="display: flex; align-items: center; justify-content: center">
                <el-select
                  v-model="scope.row.activityDefinitionId"
                  placeholder="请选择"
                  clearable
                  style="width: 200px"
                  :class="{ 'error-border': scope.row.error }"
                >
                  <el-option
                    v-for="dict in scope.row.projectList"
                    :key="dict.value"
                    :label="dict.info"
                    :value="dict.value"
                  />
                </el-select>
              </div>
            </template>
          </el-table-column>
          <el-table-column
            label="开始时间"
            align="center"
            key="startTime"
            prop="startTime"
            :show-overflow-tooltip="true"
            width="300"
          >
            <template #default="scope">
              <div style="display: flex; align-items: center; justify-content: center">
                <el-time-picker
                  v-model="scope.row.startTime"
                  placeholder="选择时间"
                  format="HH:mm:ss"
                  value-format="HH:mm:ss"
                >
                </el-time-picker>
              </div>
            </template>
          </el-table-column>

          <el-table-column
            label="结束时间"
            align="center"
            key="endTime"
            prop="endTime"
            :show-overflow-tooltip="true"
            width="300"
          >
            <template #default="scope">
              <div style="display: flex; align-items: center; justify-content: center">
                <el-time-picker
                  v-model="scope.row.endTime"
                  placeholder="选择时间"
                  format="HH:mm:ss"
                  value-format="HH:mm:ss"
                >
                </el-time-picker>
              </div>
            </template>
          </el-table-column>
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
                @click="openSaveImplementDepartment(scope.row, scope.$index)"
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

<script setup name="implementDepartment">
import { ref } from 'vue';
import {
  getImplementDepartmentList,
  getDiagnosisTreatmentList,
  getDiseaseTreatmentInit,
  getImplementDepartmentOne,
  // getDiseaseTreatmentInitLoc,
  addImplementDepartment,
  editImplementDepartment,
  deleteImplementDepartment,
} from './components/implementDepartment';
const { proxy } = getCurrentInstance();
const organization = ref([]);
const loading = ref(true);
const ids = ref([]); // 存储选择的行数据
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const catagoryList = ref([]);
const projectList = ref([]);
const catagoryDicts = ref([]);
const isAddDisable = ref(true);
const organizationId = ref('');
//默认传8(诊疗)
const distributionCategoryCode = ref('8');
const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
  },
  tableRules: {
    activityCategoryCode: [{ required: true, message: '诊疗目录不能为空', trigger: 'blur' }],
    activityDefinitionId: [{ required: true, message: '项目名称不能为空', trigger: 'blur' }],
  },
  isAdding: false,
});

const { queryParams, tableRules } = toRefs(data);

/** 查询表格数据列表 */
function getList() {
  let params = {
    // 科室id
    organizationId: organizationId.value,
    // 类别
    distributionCategoryCode: distributionCategoryCode.value,
  };
  getDiagnosisTreatmentList(params).then((res) => {
    loading.value = false;
    catagoryList.value = res.data.records
    catagoryList.value.map((k,index)=>{
      // if(k.activityCategoryCode){
        DiagnosisTreatmentList(k,index)
      // }
    })
    total.value = res.data.total;
  });
}
/** 通过条件过滤节点  */
const filterNode = (value, data) => {
  if (!value) return true;
  return data.name.indexOf(value) !== -1;
};

//下拉诊疗目录点击事件
function DiagnosisTreatmentList(row,index,type) {
  let params = {
    statusEnum: 2, // 状态（包括 1：预置，2：启用，3：停用）
    ...row,
    categoryCode: row.activityCategoryCode,
  }
  if(type){
    catagoryList.value[index].activityDefinitionId =''
  }
  getImplementDepartmentOne(params)
    .then((res) => {
      if (res.code === 200) {
        projectList.value = [];
        row.name = null;
        projectList.value = res.data.records.map((item) => ({ value: item.id, info: item.name }));
        catagoryList.value[index].projectList = projectList.value
      } else {
        proxy.$modal.msgError(res.msg);
      }
    })
    .catch((error) => {
      console.error('请求失败', error);
    });
}

/** 选择条数  */
function handleSelectionChange(selection) {
  // selectedData.value = selection.map((item) => ({ ...item })); // 存储选择的行数据
  ids.value = selection.map((item) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}
// 新增项目
function handleAddItem() {
  if (data.isAdding) {
    proxy.$message.warning('请先保存当前行后再新增！');
    return;
  }
  const newRow = {
    startTime:'00:00:00',
    endTime:'23:59:59'
  };
  catagoryList.value.push(newRow);
  total.value = organization.value.length;
  data.isAdding = true; // 设置标志位为 true，表示有未保存的
}
// 检验 编辑或 保存数据
function handleBlur(row, index) {
  let hasError = false;
  const fields = ['activityCategoryCode', 'activityDefinitionId', 'startTime', 'endTime'];

  fields.forEach((field) => {
    if (!row[field]) {
      hasError = true;
      const message = tableRules.value[field]?.[0]?.message;
      if (message) {
        proxy.$message.error(message);
      } else {
        console.error(`No rule defined for field: ${field}`);
        proxy.$message.error(`No rule defined for field: ${field}`);
      }
    }
  });
  row.error = hasError;
}

// 编辑或 保存当前行
function openSaveImplementDepartment(row) {
  const params = {
    // 科室id
    organizationId: organizationId.value,
    // 类别
    distributionCategoryCode: distributionCategoryCode.value,
    ...row,
  };
  let hasError = false;
  handleBlur(row);
  if (row.error) {
    hasError = true;
  }
  const startTime = params.startTime;
  const endTime = params.endTime;
  if (startTime > endTime) {
    proxy.$message.error('开始时间不能大于结束时间');
    return;
  }
  if (hasError) {
    proxy.$message.error('请填写完整信息');
    return;
  }
  if (row.id) {
    editImplementDepartment(params).then((res) => {
      data.isAdding = false; // 允许新增下一行
      proxy.$modal.msgSuccess('保存成功！');
    });
  } else {
    delete params.id;
    addImplementDepartment(params).then((res) => {
      data.isAdding = false; // 允许新增下一行
      proxy.$modal.msgSuccess('保存成功！');
    });
  }
}
// 删除当前所选行
function deleteSelectedRows(row) {
  if (row.id) {
    deleteImplementDepartment({ orgLocId: row.id }).then((res) => {});
  } else {
    catagoryList.value.pop();
  }
  proxy.$modal.msgSuccess('删除成功');
  data.isAdding = false;
  getList();
}
/** 节点单击事件 */
function handleNodeClick(data, node) {
  catagoryList.value.map(k=>{
    if(!k.id){
      openSaveImplementDepartment(k)
    }
  })
  // 新增按钮是否 disable
  if (node.parent === null || node.level === 1) {
    isAddDisable.value = true;
  } else {
    isAddDisable.value = false;
  }
  // 检查节点是否有子节点
  if (node.data.children && node.data.children.length > 0) {
    // proxy.$message.warning("不能选择父节点");
    return;
  }
  // 选中科室id
  organizationId.value = node.data.id;
  // 获取 右侧 table 信息
  getList();
}

/** 目录分类查询 */
function getDiseaseTreatmentList() {
  getDiseaseTreatmentInit().then(({ data }) => {
    //分类目录初始化获取
    catagoryDicts.value = data.diagnosisCategoryOptions.sort((a, b) => {
      return parseInt(a.value) - parseInt(b.value);
    });
  });
  // 诊疗目录分类查询下拉树结d构
  loading.value = true;
  // 诊疗目录分类查询下拉树结d构
  getImplDepartList();
}
// 诊疗目录分类查询下拉树结d构
function getImplDepartList() {
  getImplementDepartmentList().then((res) => {
    loading.value = false;
    if (res.code === 200) {
      if (res.data.records.length > 0) {
        organization.value = res.data.records.map((res) => {
          return {
            ...res,
            isEditing: false, // 标记当前行是否正在编辑
            error: false, // 新增 error 字段
          };
        });
      } else {
        organization.value = [];
      }
    } else {
      this.$modal.msgError(res.code);
    }
  });
}
// 获取左侧执行科室配置目录
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
/* ::v-deep.el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content{ */
  /* background-color:  #c5e1ff!important; */
  /* #d8ebff!important; */
  /* #c5e1ff!important; */
  /* #9fceff!important; */
/* } */
/* ::v-deep.el-tree--highlight-current{ */
  /* background-color:#f8f8f9 !important; */
   /* #ebf5ff!important; */
/* } */

</style>