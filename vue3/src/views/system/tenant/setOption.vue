<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" v-show="showSearch" :inline="true">
         <el-form-item label="配置项" prop="optionCode">
            <el-select v-model="queryParams.optionCode" placeholder="配置项" clearable style="width: 300px">
               <el-option v-for="dict in optionCodeList" :key="dict.code" :label="dict.name + '（' + dict.code + '）'"
                  :value="dict.code" />
            </el-select>
         </el-form-item>
         <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
         </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
         <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="addOption">新增</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="delOptions">删除</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button type="warning" plain icon="Close" @click="handleClose">关闭</el-button>
         </el-col>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="optionList" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="55" align="center" />
         <el-table-column label="配置项编码" prop="optionCode" :show-overflow-tooltip="true" />
         <el-table-column label="配置项名称" prop="optionName" :show-overflow-tooltip="true" />
         <el-table-column label="配置项内容" prop="optionContent" :show-overflow-tooltip="true" />
         <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
               <el-tooltip content="修改" placement="top">
                  <el-button link type="primary" icon="Edit" @click="editOption(scope.row)"></el-button>
               </el-tooltip>
               <el-tooltip content="删除" placement="top">
                  <el-button link type="primary" icon="Delete" @click="handelDel(scope.row)"></el-button>
               </el-tooltip>
            </template>
         </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
         v-model:limit="queryParams.pageSize" @pagination="getList" />


      <!-- 添加或修改配置项对话框 -->
      <el-dialog :title="title" v-model="open" width="500px" append-to-body>
         <el-form ref="optionRef" :model="form" :rules="rules" label-width="100px">
            <el-form-item label="配置项" prop="optionCode">
               <el-select v-model="form.optionCode" placeholder="配置项" :disabled="form.id" clearable
                  style="width: 500px">
                  <el-option v-for="dict in optionCodeList" :key="dict.code" :label="dict.name + '（' + dict.code + '）'"
                     :value="dict.code" />
               </el-select>
            </el-form-item>
            <el-form-item label="内容" prop="optionContent">
               <el-input v-model="form.optionContent" type="textarea" style="width: 500px" placeholder="请输入内容" />
            </el-form-item>
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

<script setup name="SetOption">
import { getTenantOptionPage, getTenantOptionDetail, addTenantOption, editTenantOption, delTenantOption, getTenantOptionDropdown } from "@/api/system/tenant";

const route = useRoute();
const { proxy } = getCurrentInstance();

const optionList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const optionIds = ref([]);
const optionCodeList = ref([]);

const data = reactive({
   form: {},
   queryParams: {
      pageNum: 1,
      pageSize: 10,
      tenantId: route.params.tenantId,
      optionCode: undefined,
   },
   rules: {
      optionCode: [
         { required: true, message: "配置项不能为空", trigger: "blur" },
      ],
      optionContent: [
         { required: true, message: "内容不能为空", trigger: "blur" },
         { min: 1, max: 500, message: '长度需在1到500个字符之间', trigger: 'blur' }
      ]
   }
})

const { queryParams, form, rules } = toRefs(data);

/** 查询授权用户列表 */
function getList() {
   loading.value = true;
   getTenantOptionPage(queryParams.value).then(response => {
      optionList.value = response.data.records;
      total.value = response.data.total;
      loading.value = false;
   });
}
// 返回按钮
function handleClose() {
   const obj = { path: "/system/basicmanage/tenant" };
   proxy.$tab.closeOpenPage(obj);
}
/** 搜索按钮操作 */
function handleQuery() {
   queryParams.pageNum = 1;
   getList();
}
/** 重置按钮操作 */
function resetQuery() {
   proxy.resetForm("queryRef");
   handleQuery();
}
// 多选框选中数据
function handleSelectionChange(selection) {
   optionIds.value = selection.map(item => item.id);
   multiple.value = !selection.length;
}
/** 重置新增的表单以及其他数据  */
function reset() {
   form.value = {
      id: undefined,
      optionCode: undefined,
      optionContent: undefined,
   };
   proxy.resetForm("optionRef");
}
/** 新增配置 */
function addOption() {
   reset();
   open.value = true;
   title.value = "新增配置项";
}
/** 批量删除配置 */
function delOptions() {
   proxy.$modal.confirm('确认要删除这些配置项吗？').then(function () {
      return delTenantOption(optionIds.value);
   }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
   }).catch(() => { });
}
/** 编辑配置 */
function editOption(row) {
   reset();
   getTenantOptionDetail(row.id).then(response => {
      form.value = response.data
      open.value = true;
      title.value = "修改配置项";
   }
   )
}
/** 提交按钮 */
function submitForm() {
   form.value.tenantId = route.params.tenantId
   proxy.$refs["optionRef"].validate(valid => {
      if (valid) {
         if (form.value.id != undefined) {
            editTenantOption(form.value).then(response => {
               proxy.$modal.msgSuccess("修改成功");
               open.value = false;
               getList();
            });
         } else {
            addTenantOption(form.value).then(response => {
               if (response.code != 0) {
                  proxy.$modal.msgSuccess(response.msg);
               } else {
                  proxy.$modal.msgSuccess("新增成功");
               }
               open.value = false;
               getList();
            });
         }
      }
   });
}
/** 删除配置 */
function handelDel(row) {
   proxy.$modal.confirm("确认要删除该配置项吗？").then(function () {
      return delTenantOption([row.id]);
   }).then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
   }).catch(() => { });
}
/** 查询配置项下拉列表 */
function getOptionCodeList() {
   getTenantOptionDropdown().then(response => {
      optionCodeList.value = response.data;
   });
}
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}

getList();
getOptionCodeList();
</script>
