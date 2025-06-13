<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--疾病目录数据-->
      <el-col :span="4" :xs="24">
        <div class="head-title">疾病目录</div>
        <div class="head-container">
          <el-tree
            :data="conditionDefinitionOptions"
            :props="{ label: 'info', children: 'children' }"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="deptTreeRef"
            node-key="value"
            highlight-current
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>
      <!--用户数据-->
      <el-col :span="20" :xs="24">
        <el-form
          :model="queryParams"
          ref="queryRef"
          :inline="true"
          v-show="showSearch"
          label-width="68px"
        >
          <el-form-item label="疾病：" prop="searchKey">
            <el-input
              v-model="queryParams.searchKey"
              placeholder="名称/ICD10编码/拼音助记码"
              clearable
              style="width: 240px"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-form-item label="是否停用" prop="statusEnum">
            <el-select v-model="queryParams.statusEnum" style="width: 240px" clearable>
              <el-option
                v-for="status in statusFlagOptions"
                :key="status.value"
                :label="status.info"
                :value="status.value"
              />
            </el-select>
          </el-form-item>
          <!-- <el-form-item>
                  <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                  <el-button icon="Refresh" @click="resetQuery">重置</el-button>
               </el-form-item> -->
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="handleAdd">添加新项目</el-button>
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

        <el-table v-loading="loading" :data="diseaseList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="编码" align="center" key="conditionCode" prop="conditionCode" />
          <el-table-column
            label="名称"
            align="center"
            key="name"
            prop="name"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="疾病分类"
            align="center"
            key="sourceEnum_enumText"
            prop="sourceEnum_enumText"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="拼音助记码"
            align="center"
            key="pyStr"
            prop="pyStr"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="类型"
            align="center"
            key="typeCode_dictText"
            prop="typeCode_dictText"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="医保编码 "
            align="center"
            key="ybNo"
            prop="ybNo"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="医保标记"
            align="center"
            key="ybMatchFlag"
            prop="ybMatchFlag_enumText"
          />
          <el-table-column
            label="医保对码标志"
            align="center"
            key="ybMatchFlag"
            prop="ybMatchFlag_enumText"
          />
          <el-table-column
            label="状态"
            align="center"
            key="statusEnum_enumText"
            prop="statusEnum_enumText"
            width="160"
          />
          <el-table-column
            label="描述"
            align="center"
            key="description"
            prop="description"
            width="160"
          />
          <el-table-column
            label="操作"
            align="center"
            width="150"
            class-name="small-padding fixed-width"
          >
            <template #default="scope">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
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

    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form :model="form" :rules="rules" ref="diseaseRef" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="名称" prop="name">
              <el-input
                v-model="form.name"
                placeholder="请输入名称"
                :disabled="form.id != undefined"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.id != undefined">
            <el-form-item label="编码" prop="conditionCode">
              <el-input v-model="form.conditionCode" placeholder="请输入编码" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="疾病类型" prop="sourceEnum">
              <el-select
                v-model="form.sourceEnum"
                placeholder="请选择"
                clearable
                :disabled="form.sourceEnum != '' && form.sourceEnum != undefined"
              >
                <el-option
                  v-for="dict in conditionDefinitionList"
                  :key="dict.value"
                  :label="dict.info"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类型" prop="typeCode">
              <el-select v-model="form.typeCode" placeholder="请选择" clearable>
                <el-option
                  v-for="dict in condition_type_code"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <!-- <el-col :span="12">
            <el-form-item label="状态" prop="statusEnum">
              <el-select
                v-model="form.statusEnum"
                placeholder="请选择"
                clearable
              >
                <el-option
                  v-for="dict in statusFlagOptions"
                  :key="dict.value"
                  :label="dict.info"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col> -->
          <el-col :span="12">
            <el-form-item label="医保编码" prop="ybNo">
              <el-input v-model="form.ybNo" placeholder="" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="医保标记" prop="ybFlag">
              <el-checkbox v-model="form.ybFlag"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="医保对码" prop="ybMatchFlag">
              <el-checkbox v-model="form.ybMatchFlag"></el-checkbox>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="16">
            <el-form-item label="说明" prop="description">
              <el-input
                v-model="form.description"
                :autosize="{ minRows: 4, maxRows: 10 }"
                type="textarea"
                placeholder=""
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Disease">
import {
  getDiseaseList,
  editDisease,
  addDisease,
  getDiseaseCategory,
  getDiseaseOne,
  stopDisease,
  startDisease,
} from './components/disease';
const { proxy } = getCurrentInstance();
const { condition_type_code } = proxy.useDict('condition_type_code');

const diseaseList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref('');
const conditionDefinitionOptions = ref(undefined);
const conditionDefinitionList = ref(undefined);
const conditionDefinition = ref(undefined);
// 是否停用
const statusFlagOptions = ref(undefined);

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    searchKey: undefined, // 疾病名称
    statusEnum: undefined, // 状态（包括 1：预置，2：启用，3：停用）
    sourceEnum: undefined, // 来源（包括 1：病种目录分类，2：自定义）
  },
  rules: {
    name: [{ required: true, message: '名称不能为空', trigger: 'blur' }],
    //typeCode: [{ required: true, message: "类型不能为空", trigger: "blur" }],
    // statusEnum: [{ required: true, message: "状态不能为空", trigger: "blur" }],
    // ybFlag: [{ required: true, message: "医保标记不能为空", trigger: "blur" }],
    // ybMatchFlag: [
    //   { required: true, message: "医保对码标记不能为空", trigger: "blur" },
    // ],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 通过条件过滤节点  */
const filterNode = (value, data) => {
  if (!value) return true;
  return data.label.indexOf(value) !== -1;
};

/** 病种目录分类查询下拉树结构 */
function getDiseaseCategoryList() {
  getDiseaseCategory().then((response) => {
    console.log(response, 'response病种目录分类查询下拉树结构');
    conditionDefinitionOptions.value = JSON.parse(
      JSON.stringify(response.data.diseaseCategoryList)
    ).sort((a, b) => {
      return parseInt(a.value) - parseInt(b.value);
    });
    conditionDefinitionOptions.value.push({ info: '全部', value: '' });
    conditionDefinitionList.value = JSON.parse(
      JSON.stringify(response.data.diseaseCategoryList)
    ).sort((a, b) => {
      return parseInt(a.value) - parseInt(b.value);
    });
    statusFlagOptions.value = response.data.statusFlagOptions;
  });
}
/** 查询病种目录列表 */
function getList() {
  loading.value = true;
  getDiseaseList(queryParams.value).then((res) => {
    loading.value = false;
    diseaseList.value = res.data.records;
    total.value = res.data.total;
    console.log(total.value, 'total.value', res, 'res');
  });
}
/** 节点单击事件 */
function handleNodeClick(data) {
  queryParams.value.sourceEnum = data.value;
  conditionDefinition.value = data.value;
  handleQuery();
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNo = 1;
  getList();
}

/** 启用按钮操作 */
function handleStart(row) {
  const stardIds = row.id || ids.value;
  proxy.$modal
    .confirm('是否确定启用数据！')
    .then(function () {
      return startDisease(stardIds);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess('启用成功');
    })
    .catch(() => {});
}
/** 停用按钮操作 */
function handleClose(row) {
  const stopIds = row.id || ids.value;
  proxy.$modal
    .confirm('是否确认停用数据！')
    .then(function () {
      return stopDisease(stopIds);
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

/** 重置操作表单 */
function reset() {
  form.value = {
    id: undefined,
    conditionCode: undefined,
    pyStr: undefined,
    statusEnum: undefined,
    sourceEnum: undefined,
    typeCode: undefined,
    description: undefined,
    ybFlag: undefined,
    ybNo: undefined,
    ybMatchFlag: undefined,
  };
  proxy.resetForm('diseaseRef');
}
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}
/** 新增按钮操作 */
function handleAdd() {
  // if (conditionDefinition.value === undefined) {
  //   return proxy.$modal.msgError("请选择病种目录分类");
  // }
  reset();
  form.value.sourceEnum = conditionDefinition.value;
  open.value = true;
  title.value = '新增';
}
/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  getDiseaseOne(row.id).then((response) => {
    console.log(response, 'responsebbbb', row.id);
    form.value = response.data;
    form.value.ybFlag == 1 ? (form.value.ybFlag = true) : (form.value.ybFlag = false);
    form.value.ybMatchFlag == 1
      ? (form.value.ybMatchFlag = true)
      : (form.value.ybMatchFlag = false);
    getList();
  });
  // console.log(row, "row");
  // form.value = JSON.parse(JSON.stringify(row));
  open.value = true;
  title.value = '病种编辑';
}
/** 提交按钮 */
function submitForm() {
  proxy.$refs['diseaseRef'].validate((valid) => {
    if (valid) {
      form.value.sourceEnum = conditionDefinition.value;
      form.value.ybFlag ? (form.value.ybFlag = 1) : (form.value.ybFlag = 0);
      form.value.ybMatchFlag ? (form.value.ybMatchFlag = 1) : (form.value.ybMatchFlag = 0);
      if (form.value.id != undefined) {
        console.log(form.value, 'editDisease', form.value.statusEnum);
        editDisease(form.value).then((response) => {
          proxy.$modal.msgSuccess('修改成功');
          open.value = false;
          getList();
        });
      } else {
        addDisease(form.value).then((response) => {
          proxy.$modal.msgSuccess('新增成功');
          open.value = false;
          getList();
        });
      }
    }
  });
}

getDiseaseCategoryList();
getList();
</script>
<style scoped>
.custom-tree-node {
  display: flex;
  align-items: center;
}
</style>