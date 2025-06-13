<template>
   <!-- 绑定用户 -->
   <el-dialog title="选择用户" v-model="visible" width="1000px" top="5vh" append-to-body>
      <el-form :model="queryParams" ref="queryRef" :inline="true">
         <el-form-item label="用户名称" prop="userName">
            <el-input v-model="queryParams.userName" placeholder="请输入用户名称" clearable style="width: 150px"
               @keyup.enter="handleQuery" />
         </el-form-item>
         <el-form-item label="用户昵称" prop="nickName">
            <el-input v-model="queryParams.nickName" placeholder="请输入用户昵称" clearable style="width: 150px"
               @keyup.enter="handleQuery" />
         </el-form-item>
         <el-form-item label="手机号" prop="phoneNumber">
            <el-input v-model="queryParams.phoneNumber" placeholder="请输入手机号" clearable style="width: 150px"
               @keyup.enter="handleQuery" />
         </el-form-item>
         <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
         </el-form-item>
      </el-form>
      <el-row>
         <el-table @row-click="clickRow" ref="refTable" :data="userList" @selection-change="handleSelectionChange"
            height="260px">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column label="用户名称" prop="userName" :show-overflow-tooltip="true" />
            <el-table-column label="用户昵称" prop="nickName" :show-overflow-tooltip="true" />
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
         </el-table>
         <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize" @pagination="getList" />
      </el-row>
      <template #footer>
         <div class="dialog-footer">
            <el-button type="primary" @click="handleBindUser">确 定</el-button>
            <el-button @click="visible = false">取 消</el-button>
         </div>
      </template>
   </el-dialog>
</template>

<script setup name="BindUser">
import { getUnbindTenantUserList, bindTenantUser } from "@/api/system/tenant";

const props = defineProps({
   tenantId: {
      type: [Number, String]
   }
});

const { proxy } = getCurrentInstance();
const { sys_normal_disable } = proxy.useDict("sys_normal_disable");

const userList = ref([]);
const visible = ref(false);
const total = ref(0);
const userIds = ref([]);

const queryParams = reactive({
   pageNum: 1,
   pageSize: 10,
   tenantId: undefined,
   userName: undefined,
   nickName: undefined,
   phonenumber: undefined
});

// 显示弹框
function show() {
   queryParams.tenantId = props.tenantId;
   getList();
   visible.value = true;
}
/**选择行 */
function clickRow(row) {
   proxy.$refs["refTable"].toggleRowSelection(row);
}
// 多选框选中数据
function handleSelectionChange(selection) {
   userIds.value = selection.map(item => item.userId);
}
// 查询表数据
function getList() {
   getUnbindTenantUserList(queryParams).then(res => {
      userList.value = res.data.records;
      total.value = res.data.total;
   });
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
const emit = defineEmits(["ok"]);
/** 选择绑定用户操作 */
function handleBindUser() {
   if (userIds.value.length == 0) {
      proxy.$modal.msgError("请选择要绑定的用户");
      return;
   }
   bindTenantUser(queryParams.tenantId, userIds.value).then(res => {
      proxy.$modal.msgSuccess(res.msg);
      if (res.code === 200) {
         visible.value = false;
         emit("ok");
      }
   });
}

defineExpose({
   show,
});
</script>
