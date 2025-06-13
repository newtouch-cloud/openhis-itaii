<template>
  <div>
    <el-table
      ref="emrTemplateRef"
      :data="emrTemplate"
      row-key="id"
      highlight-current-row
      @cell-click="clickRow"
    >
      <el-table-column label="模板名称" align="center" prop="templateName" />
      <el-table-column label="使用范围" align="center" prop="useScopeCode" />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button link type="primary" @click.stop="handelDelete(scope.row)"
            >删除</el-button
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
  </div>
</template>
  
<script setup>
import { getEmrTemplateList, deleteEmrTemplate } from "./api";

const queryParams = ref({});
const emrTemplate = ref([]);
const emrTemplateRef = ref();
const emits = defineEmits(["selectRow"]);
const { proxy } = getCurrentInstance();
const selectRow = ref({});
const props = defineProps({
  open: {
    type: Boolean,
    required: true,
  },
});
getList();
function getList() {
  queryParams.value.useScopeCode = 1;
  getEmrTemplateList(queryParams.value).then((res) => {
    emrTemplate.value = res.data.records;
    console.log(emrTemplate.value,"emrTemplate.value")
  });
}

function clickRow(row) {
  console.log(2123);

  selectRow.value = JSON.parse(row.contextJson);
  emits("selectRow", selectRow.value);
}

function handelDelete(row) {
  deleteEmrTemplate(row.id.toString()).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess("删除成功");
      getList();
    }
  });
}
</script>