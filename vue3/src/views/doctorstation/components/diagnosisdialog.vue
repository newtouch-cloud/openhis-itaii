<template>
  <el-dialog
    :title="title"
    v-model="props.openDiagnosis"
    width="1000px"
    append-to-body
    destroy-on-close
    @close="close"
  >
    <div>
      <el-row :gutter="24" class="mb8">
        <el-col :span="12">
          <el-input
            v-model="queryParams.searchKey"
            placeholder="诊断名称/拼音码"
            clearable
            style="width: 50%; margin-bottom: 10px"
            @keyup.enter="queryDiagnosisUse"
          >
            <template #append>
              <el-button icon="Search" @click="queryDiagnosisUse" />
            </template>
          </el-input>
        </el-col>
        <!-- <el-col :span="12">
          <span>使用范围：</span>
          <el-radio-group v-model="radio">
            <el-radio :label="1" size="default">个人</el-radio>
            <el-radio :label="2" size="default">科室</el-radio>
          </el-radio-group>
        </el-col> -->
      </el-row>

      <el-table
        ref="diagnosisDefinitionRef"
        :data="diagnosisDefinitionList"
        row-key="patientId"
        @cell-click="clickRow"
        highlight-current-row
        @selection-change="handleSelectionChange"
      >
        <el-table-column label="诊断名称" align="center" prop="name" />
        <el-table-column label="医保编码" align="center" prop="ybNo" />
      </el-table>
      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNo"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="submit">确 定</el-button>
        <el-button @click="close">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>
  
<script setup>
import { computed } from "vue";
import { getDiagnosisDefinitionList, saveDiagnosisBind } from "./api";

const radio = ref(1);
const props = defineProps({
  openDiagnosis: {
    type: Boolean,
    default: false,
  },
  radio: {
    type: String,
    default: '',
  },
});
const emit = defineEmits(["close"]);
const total = ref(0);
const queryParams = ref({
  pageNo: 1,
  pageSize: 10,
});
const diagnosisDefinitionList = ref([]);
const selectRow = ref({});
const title = computed(() => {
  return props.radio == "个人" ? "个人常用诊断" : "科室常用诊断";
});

getList();
function getList() {
  getDiagnosisDefinitionList(queryParams.value).then((res) => {
    diagnosisDefinitionList.value = res.data.records;
    total.value = res.data.total;
  });
}

function submit() {
  saveDiagnosisBind({
    definitionId: selectRow.value.id,
    definitionName: selectRow.value.name,
    bindingEnum: props.radio == "个人" ? 1 : 2,
  }).then((res) => {
    if (res.code == 200) {
      emit("close", "success");
    }
  });
}

function queryDiagnosisUse() {
  getList();
}

function close() {
  emit("close");
}

function clickRow(row) {
  selectRow.value = row;
}
</script>

<style scoped>
:deep( .pagination-container .el-pagination) {
  right: 20px !important;
}
</style>