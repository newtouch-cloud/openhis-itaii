<template>
  <div>
    <el-dialog
      :title="title"
      v-model="props.open"
      width="1400px"
      append-to-body
      destroy-on-close
      @close="close"
    >
      <div style="display: flex; justify-content: space-between; width: 100%">
        <div style="width: 40%">
          <el-row :gutter="24">
            <el-col :span="24" :xs="24">
              <el-input
                v-model="searchKey"
                placeholder="模板名称"
                clearable
                style="width: 100%; margin-bottom: 10px"
                @keyup.enter="getTemplateListInfo"
              >
                <template #append>
                  <el-button icon="Search" @click="getTemplateListInfo" />
                </template>
              </el-input>
              <el-button size="default" type="primary" @click="openTemplateDialog"
                >新增模板</el-button
              >
              <el-button type="danger" plain @click="deleteTemplate()" :disabled="false">
                删除
              </el-button>
              <el-table
                ref="patientListRef"
                height="680"
                :data="templateList"
                row-key="id"
                highlight-current-row
                @row-click="setTemplate"
                @selection-change="handleSelectionChange"
              >
                <el-table-column type="selection" width="55" />
                <el-table-column prop="templateName" label="模板名称" width="180" />
                <el-table-column prop="contextJson" label="模板内容" width="80" />
                <el-table-column prop="useScopeCodeText" label="使用范围" width="80" />
                <el-table-column label="操作" min-width="150" fixed="right">
                  <template #default="scope">
                    <el-button
                      link
                      type="primary"
                      icon="Edit"
                      @click="handleEditTemplate(scope.row)"
                      >编辑</el-button
                    >
                  </template>
                </el-table-column>
              </el-table>
              <pagination
                v-show="templateTotal > 0"
                :total="templateTotal"
                v-model:page="templateQueryParams.pageNo"
                v-model:limit="templateQueryParams.pageSize"
                @pagination="getTemplateListInfo"
                style="margin-bottom: 20px"
              />
            </el-col>
          </el-row>
        </div>
        <div
          style="display: flex; justify-content: space-between; width: 69%"
          class="app-container"
        >
          <el-form ref="formRef" :model="form" label-width="100px">
            <el-form-item class="changeMajorFromItem" label-width="100px" label="记录时间：">
              <el-date-picker
                v-model="form.recordingTime"
                type="datetime"
                placeholder="选择日期"
                style="width: 30%"
                value-format="YYYY-MM-DD HH:mm:ss"
              />
            </el-form-item>
            <el-form-item label="生命体征：" class="changeMajorFromItem" />
            <el-form-item class="changeMajorFromItem" label-width="10px">
              <el-row :span="24">
                <!-- 第一个表单项 -->
                <el-col :span="5">
                  <el-form-item label="体温：" prop="tw" label-width="80px">
                    <el-input v-model="form.tw" style="width: 80%" /> ℃
                  </el-form-item>
                </el-col>
                <!-- 第二个表单项 -->
                <el-col :span="5">
                  <el-form-item label="脉搏：" prop="mb" label-width="80px">
                    <el-input v-model="form.mb" style="width: 80%" /> 次
                  </el-form-item>
                </el-col>
                <!-- 第三个表单项 -->
                <el-col :span="5">
                  <el-form-item label="呼吸：" prop="hx" label-width="80px">
                    <el-input v-model="form.hx" style="width: 80%" /> 次
                  </el-form-item>
                </el-col>
                <!-- 第四个表单项 -->
                <el-col :span="9">
                  <el-form-item label="血压：" prop="bloodPressure" label-width="80px">
                    <div class="xy-container" style="display: flex; align-items: center">
                      <el-input v-model="form.systolicBloodPressure" style="width: 25%" />
                      <span>/</span>
                      <el-input v-model="form.diastolicBloodPressure" style="width: 25%" />
                      (高/低)mmHg
                    </div>
                  </el-form-item>
                </el-col>
                <!-- 第一个表单项 -->
                <el-col :span="5" style="margin-top: 10px">
                  <el-form-item label="心率：" prop="xl" label-width="80px">
                    <el-input v-model="form.xl" style="width: 80%" />
                  </el-form-item>
                </el-col>
                <!-- 第二个表单项 -->
                <el-col :span="5" style="margin-top: 10px">
                  <el-form-item label="血氧：" prop="xy" label-width="80px">
                    <el-input v-model="form.xy" style="width: 80%" />
                  </el-form-item>
                </el-col>
                <!-- 第五个表单项 -->
                <el-col :span="4" style="margin-top: 10px">
                  <el-form-item style="margin-left: 20px">
                    <el-checkbox
                      v-model="form.vitalSignsSyncFlag"
                      label="同步到体征表"
                      name="SYP"
                    />
                    <!-- @change="checkTimeValidity" -->
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item
              label="病情观察与护理记录："
              label-width="90px"
              class="changeMajorFromItem"
              prop="column081"
            >
              <el-input
                v-model="form.bqgcOther"
                type="textarea"
                :rows="8"
                :autosize="{ minRows: 3, maxRows: 8 }"
                class="el-textarea"
                style="width: 95%"
              />
            </el-form-item>
          </el-form>
        </div>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submit">保存</el-button>
          <el-button @click="close">取 消</el-button>
        </div>
      </template>
    </el-dialog>
    <record-template
      ref="recordRemplateDialogRef"
      :open="openRecordTemplate"
      :patientId="patientId"
      :editData="editData"
      :recordTitle="recordTitle"
      @close="closeRecordTemplateDialog"
    />
  </div>
</template>

<script setup>
import recordTemplate from './recordTemplate.vue';
import { ref, nextTick } from 'vue';
import { saveRecord, updateRecord, getRecordTemplateList, deleteRecordTemplate } from './api';

const { proxy } = getCurrentInstance();
const props = defineProps({
  open: {
    type: Boolean,
    default: false,
  },
  title: {
    type: String,
    default: '',
  },
  patientId: {
    type: String,
    default: '',
  },
  patientInfo: {
    type: Object,
    default: () => {},
  },
  editData: {
    type: Object,
    default: () => {},
  },
});
const searchKey = ref('');
const emit = defineEmits(['close']);
const templateQueryParams = ref({
  pageNum: 1,
  pageSize: 10,
  searchKey: undefined, // 患者id
});
const templateTotal = ref(0);
const templateList = ref([]);

const patientInfo = ref({});
const form = ref({});
const templateIds = ref([]);
const single = ref(true);
const multiple = ref(true);
const editData = ref({});

const title = ref('');

const recordTitle = ref('');
const openRecordTemplate = ref(false);

/**
 * 取得患者信息详细
 */
function show() {
  patientInfo.value = props.patientInfo;
  console.log(props, 'props', props.patientInfo);
  reset();
  title.value = '';
  title.value = props.title;
  if (title.value === '编辑') {
    form.value = props.editData.content;
  }
  console.log(props, 'props', title.value);
  getTemplateListInfo();
}

/** 选择删除模板条数  */
function handleSelectionChange(selection) {
  console.log(selection, '选择条数');
  templateIds.value = selection.map((item) => item.id);
  console.log(templateIds.value, '选择条数');
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}
/**
 * 模板列表
 */
function getTemplateListInfo() {
  console.log(searchKey.value, '搜索关键字');
  templateQueryParams.value.searchKey = searchKey.value;
  getRecordTemplateList(templateQueryParams.value).then((res) => {
    console.log('模板列表', res);
    templateList.value = res.data.records;
    templateTotal.value = res.data.total;
  });
}

/**
 * 删除模板
 *
 * @param index - 要删除的处方在列表中的索引
 */
function deleteTemplate(index) {
  console.log(templateIds.value, '删除记录单模板');
  if (templateIds.value.length == 0) {
    proxy.$modal.msgWarning('请选择要删除的数据信息！');
    return;
  }
  proxy.$modal
    .confirm('是否确认删除以上数据！')
    .then(function () {
      return deleteRecordTemplate(templateIds.value);
    })
    .then(() => {
      getTemplateListInfo();
      proxy.$modal.msgSuccess('删除成功');
    })
    .catch(() => {});
}
function close() {
  reset();
  emit('close');
}
/** 重置操作表单 */
function reset() {
  form.value = {
    tw: undefined,
    mb: undefined,
    hx: undefined,
    systolicBloodPressure: undefined,
    diastolicBloodPressure: undefined,
    xl: undefined,
    xy: undefined,
    vitalSignsSyncFlag: false,
    bqgc: undefined,
    bqgcOther: undefined,
  };
  proxy.resetForm('formRef');
}

/**
 * 保存记录单
 */
function submit() {
  if (title.value === '新增') {
    const insertParam = {
      encounterId: props.patientInfo.encounterId,
      patientId: props.patientInfo.patientId,
      orgId: props.patientInfo.orgId,
      recordingTime: form.value.recordingTime,
      vitalSignsSyncFlag: form.value.vitalSignsSyncFlag ? form.value.vitalSignsSyncFlag : false,
      contextJson: JSON.stringify(form.value),
    };
    console.log(insertParam, 'insertParam');
    saveRecord(insertParam).then((res) => {
      if (res.code == 200) {
        emit('close', 'success');
        reset();
      }
    });
  } else {
    const updateParamParam = {
      recordId: props.editData.recordId,
      encounterId: props.patientInfo.encounterId,
      patientId: props.patientInfo.patientId,
      orgId: props.patientInfo.orgId,
      recordingTime: form.value.recordingTime,
      vitalSignsSyncFlag: form.value.vitalSignsSyncFlag ? form.value.vitalSignsSyncFlag : false,
      contextJson: JSON.stringify(form.value),
    };
    console.log(updateParamParam, 'updateParamParam');
    updateRecord(updateParamParam).then((res) => {
      if (res.code == 200) {
        emit('close', 'success');
        reset();
      }
    });
  }
}

/**
 * 设定病情观测
 */
function setTemplate(row) {
  console.log(row, '设定病情观测');
  form.value.bqgcOther = row.contextJson;
}

/**
 * 新增护理模板
 */
function openTemplateDialog() {
  recordTitle.value = '新增模板';
  openRecordTemplate.value = true;
  nextTick(() => {
    proxy.$refs['recordRemplateDialogRef'].show();
  });
}

/**
 * 编辑护理模板
 */
function handleEditTemplate(row) {
  recordTitle.value = '编辑模板';
  openRecordTemplate.value = true;
  editData.value = row;
  nextTick(() => {
    proxy.$refs['recordRemplateDialogRef'].show();
  });
}

/**
 * 关闭记录模板对话框
 *
 * 该函数用于关闭记录模板对话框。
 */
function closeRecordTemplateDialog(str) {
  openRecordTemplate.value = false;
  getTemplateListInfo();
  if (str === 'success') {
    proxy.$modal.msgSuccess('操作成功！');
  }
}
defineExpose({
  show,
});
</script>

<style scoped>
:deep(.pagination-container .el-pagination) {
  right: 20px !important;
}

.input-select-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.input-select-container .el-input__inner,
.input-select-container .el-select {
  width: 100%;
}

.flex-container {
  display: flex;
  align-items: center; /* 垂直居中对齐 */
  justify-content: flex-start; /* 水平起始对齐 */
  gap: 8px; /* 两个输入框之间的间距 */
}

.flex-container label {
  margin: 0 8px; /* 标签的间距 */
}
</style>