<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--疾病目录数据-->
      <el-col :span="4" :xs="24">
        <!-- <div class="head-container">
               <el-input
                  v-model="deptName"
                  placeholder="请输入部门名称"
                  clearable
                  prefix-icon="Search"
                  style="margin-bottom: 20px"
               />
            </div> -->
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
          >
            <template v-slot="{ node, data }">
              <span class="custom-tree-node">
                <i
                  :class="{
                    'el-icon-folder': !node.expanded && !data.children.length,
                    'el-icon-folder-opened': node.expanded,
                    'el-icon-document': data.children.length === 0,
                  }"
                  style="color: #409eff"
                />
                <span>{{ node.label }}</span>
              </span>
            </template>
          </el-tree>
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
          <el-form-item label="疾病：" prop="diseaseName">
            <el-input
              v-model="queryParams.diseaseName"
              placeholder="名称/ICD10编码/拼音助记码"
              clearable
              style="width: 240px"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-form-item label="是否停用" prop="status">
            <el-select
              v-model="queryParams.status"
              clearable
              style="width: 240px"
            >
              <el-option
                v-for="dict in sys_normal_disable"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <!-- <el-form-item label="是否系统预置：" prop="status" label-width="120">
                  <el-select
                     v-model="queryParams.status"
                     placeholder="用户状态"
                     clearable
                     style="width: 240px"
                  >
                     <el-option
                        v-for="dict in sys_normal_disable"
                        :key="dict.value"
                        :label="dict.label"
                        :value="dict.value"
                     />
                  </el-select>
               </el-form-item> -->
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
              @click="handleAdd"
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
          :data="diseaseList"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column
            label="编码"
            align="center"
            key="conditionCode"
            prop="conditionCode"
          />
          <el-table-column
            label="名称"
            align="center"
            key="name"
            prop="name"
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
            label="医保编码 "
            align="center"
            key="ybNo"
            prop="ybNo"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="医保名称"
            align="center"
            key="ybName"
            prop="ybName"
            width="120"
          />
          <el-table-column
            label="医保对码标志"
            align="center"
            key="ybMatchflag"
            prop="ybMatchflag"
          />
          <el-table-column
            label="停用"
            align="center"
            key="statusEnum"
            rop="statusEnum"
            width="160"
          />
          <el-table-column
            label="操作"
            align="center"
            width="150"
            class-name="small-padding fixed-width"
          >
            <template #default="scope">
              <el-button
                link
                type="primary"
                icon="Edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['system:user:edit']"
                >编辑</el-button
              >
              <el-button
                link
                type="primary"
                icon="View"
                @click="handleView(scope.row)"
                v-hasPermi="['system:user:remove']"
                >查看</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="total > 0"
          :total="total"
          v-model:page="queryParams.pageNum"
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
                maxlength="30"
                :disabled="form.id != undefined"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="编码" prop="conditionCode">
              <el-input
                v-model="form.conditionCode"
                placeholder="请输入编码"
                maxlength="30"
                :disabled="form.id != undefined"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="拼音" prop="pyStr">
              <el-input v-model="form.pyStr" maxlength="11" />
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item label="停用" prop="status">
              <el-checkbox v-model="form.status"></el-checkbox>
            </el-form-item>
          </el-col> -->
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
  startDisease
} from "./components/disease";

const router = useRouter();
const { proxy } = getCurrentInstance();
const { sys_normal_disable, sys_user_sex } = proxy.useDict(
  "sys_normal_disable",
  "sys_user_sex"
);

const diseaseList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const conditionDefinitionOptions = ref(undefined);
// const initPassword = ref(undefined);
// const postOptions = ref([]);
// const roleOptions = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 50,
    diseaseName: undefined, // 疾病名称
    status: undefined, // 状态（包括 1：预置，2：启用，3：停用）
  },
  rules: {
    name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
    conditionCode: [
      { required: true, message: "编码不能为空", trigger: "blur" },
    ],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 通过条件过滤节点  */
const filterNode = (value, data) => {
  if (!value) return true;
  return data.label.indexOf(value) !== -1;
};
// /** 根据名称筛选部门树 */
// watch(deptName, val => {
//   proxy.$refs["deptTreeRef"].filter(val);
// });
/** 病种目录分类查询下拉树结构 */
function getDiseaseCategoryList() {
  getDiseaseCategory().then((response) => {
    console.log(response, "response病种目录分类查询下拉树结构");
    conditionDefinitionOptions.value = response.data;
  });
}
/** 查询病种目录列表 */
function getList() {
  loading.value = true;
  getDiseaseList(queryParams.value).then((res) => {
    loading.value = false;
    console.log(res, "res");
    diseaseList.value = res.data.records;
    total.value = res.data.total;
    console.log(total.value, "total.value");
  });
}
/** 节点单击事件 */
function handleNodeClick(data) {
  queryParams.value.deptId = data.id;
  handleQuery();
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}
// /** 重置按钮操作 */
// function resetQuery() {
//    dateRange.value = [];
//    proxy.resetForm("queryRef");
//    queryParams.value.deptId = undefined;
//    proxy.$refs.deptTreeRef.setCurrentKey(null);
//    handleQuery();
// };
/** 启用按钮操作 */
function handleStart(row) {
  const stardIds = row.id || ids.value;
  proxy.$modal
    .confirm("是否确定启用数据！")
    .then(function () {
      return startDisease(stardIds);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("启用成功");
    })
    .catch(() => {});
}
/** 停用按钮操作 */
function handleClose(row) {
  const stopIds = row.id || ids.value;
  proxy.$modal
    .confirm("是否确认停用数据！")
    .then(function () {
      return stopDisease(stopIds);
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
// /** 用户状态修改  */
// function handleStatusChange(row) {
//    let text = row.status === "0" ? "启用" : "停用";
//    proxy.$modal.confirm('确认要"' + text + '""' + row.userName + '"用户吗?').then(function () {
//       return changeUserStatus(row.userId, row.status);
//    }).then(() => {
//       proxy.$modal.msgSuccess(text + "成功");
//    }).catch(function () {
//       row.status = row.status === "0" ? "1" : "0";
//    });
// };

/** 选择条数  */
function handleSelectionChange(selection) {
  console.log(selection, "selection");
  // selectedData.value = selection.map((item) => ({ ...item })); // 存储选择的行数据
  ids.value = selection.map(item => item.userId);
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
/** 重置操作表单 */
function reset() {
  form.value = {
    id: undefined,
    conditionCode: undefined,
    pyStr: undefined,
    status: undefined,
    statusEnum: undefined,
  };
  proxy.resetForm("diseaseRef");
}
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}
/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增";
}
/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  console.log(row, "row");
  form.value = row;
  open.value = true;
  title.value = "病种编辑";
}
/** 提交按钮 */
function submitForm() {
  proxy.$refs["diseaseRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != undefined) {
        // form.value.status
        //   ? (form.value.statusEnum = "3")
        //   : (form.value.statusEnum = "2");
        console.log(form.value, "editDisease", form.value.statusEnum);
        editDisease(form.value).then((response) => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addDisease(form.value).then((response) => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 详细按钮操作 */
function handleView(row) {
  reset();
  open.value = true;
  getDiseaseOne(row.id).then((response) => {
    console.log(response, "responsebbbb", row.id);
    form.value = response.data;
    //  getList();
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