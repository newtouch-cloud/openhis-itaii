<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" v-show="showSearch" :inline="true">
         <el-form-item label="用户名称" prop="userName">
            <el-input v-model="queryParams.userName" placeholder="请输入用户名称" clearable style="width: 240px"
               @keyup.enter="handleQuery" />
         </el-form-item>
         <el-form-item label="用户昵称" prop="nickName">
            <el-input v-model="queryParams.nickName" placeholder="请输入用户昵称" clearable style="width: 240px"
               @keyup.enter="handleQuery" />
         </el-form-item>
         <el-form-item label="手机号" prop="phoneNumber">
            <el-input v-model="queryParams.phoneNumber" placeholder="请输入手机号" clearable style="width: 240px"
               @keyup.enter="handleQuery" />
         </el-form-item>
         <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
         </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
         <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="openBindUser">绑定用户</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button type="danger" plain icon="CircleClose" :disabled="multiple"
               @click="cancelBindUserAll">解绑用户</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button type="warning" plain icon="Close" @click="handleClose">关闭</el-button>
         </el-col>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="55" align="center" />
         <el-table-column label="用户名称" prop="userName" :show-overflow-tooltip="true" />
         <el-table-column label="用户昵称" prop="nickName" :show-overflow-tooltip="true" />
         <el-table-column label="邮箱" prop="email" :show-overflow-tooltip="true" />
         <el-table-column label="手机号" prop="phonenumber" :show-overflow-tooltip="true" />
         <el-table-column label="状态" align="center" prop="status">
            <template #default="scope">
               <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
            </template>
         </el-table-column>
         <el-table-column label="创建时间" align="center" prop="createTime" width="180">
            <template #default="scope">
               <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
               <el-tooltip content="解绑" placement="top">
                  <el-button link type="primary" icon="Delete" @click="cancelBindUser(scope.row)"></el-button>
               </el-tooltip>
            </template>
         </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
         v-model:limit="queryParams.pageSize" @pagination="getList" />
      <bind-user ref="bindRef" :tenantId="queryParams.tenantId" @ok="handleQuery" />
   </div>
</template>

<script setup name="SetUser">
import bindUser from "./bindUser";
import { getTenantUserPage, unbindTenantUser } from "@/api/system/tenant";

const route = useRoute();
const { proxy } = getCurrentInstance();
const { sys_normal_disable } = proxy.useDict("sys_normal_disable");

const userList = ref([]);
const loading = ref(true);
const showSearch = ref(true);
const multiple = ref(true);
const total = ref(0);
const userIds = ref([]);

const queryParams = reactive({
   pageNum: 1,
   pageSize: 10,
   tenantId: route.params.tenantId,
   userName: undefined,
   nickName: undefined,
   phoneNumber: undefined,
});

/** 查询授权用户列表 */
function getList() {
   loading.value = true;
   getTenantUserPage(queryParams).then(response => {
      userList.value = response.data.records;
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
   userIds.value = selection.map(item => item.userId);
   multiple.value = !selection.length;
}
/** 打开绑定用户弹窗 */
function openBindUser() {
   proxy.$refs["bindRef"].show();
}
/** 解绑按钮操作 */
function cancelBindUser(row) {
   proxy.$modal.confirm('确认要解绑该用户"' + row.userName + '"吗？').then(function () {
      return unbindTenantUser(queryParams.tenantId, [row.userId]);
   }).then(() => {
      getList();
      proxy.$modal.msgSuccess("解绑成功");
   }).catch(() => { });
}
/** 批量解绑按钮操作 */
function cancelBindUserAll() {
   proxy.$modal.confirm("确认解绑这些用户吗?").then(function () {
      return unbindTenantUser(queryParams.tenantId, userIds.value);
   }).then(() => {
      getList();
      proxy.$modal.msgSuccess("解绑成功");
   }).catch(() => { });
}

getList();
</script>
