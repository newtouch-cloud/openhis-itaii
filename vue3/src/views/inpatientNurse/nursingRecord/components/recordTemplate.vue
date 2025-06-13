<template>
  <el-dialog
    :title="props.recordTitle"
    v-model="props.open"
    width="800px"
    append-to-body
    destroy-on-close
    @close="close"
  >
    <div>
      <span> 模板名称： </span>
      <el-input v-model="templateName" style="width: 260px; margin-top: 10px; margin-right: 20px" />
      <el-radio-group v-model="radio">
        <el-radio-button :label="1">个人</el-radio-button>
        <el-radio-button :label="2">科室</el-radio-button>
        <el-radio-button :label="3">全院</el-radio-button>
      </el-radio-group>
    </div>
    <div style="display: flex">
      <span> 模板内容： </span>
      <el-input
        v-model="contextJson"
        type="textarea"
        :rows="8"
        :autosize="{ minRows: 3, maxRows: 8 }"
        class="el-textarea"
        style="width: 80%; margin-top: 10px; margin-right: 20px"
      />
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="submit">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>
  
<script setup>
import { parse } from 'vue/compiler-sfc';
import { saveRecordTemplate, updateRecordTemplate } from './api';
import useUserStore from '@/store/modules/user';
const title = ref('');
const templateName = ref('');
const radio = ref(1);
const contextJson = ref('');
const userStore = useUserStore();

const emits = defineEmits(['close']);
const props = defineProps({
  open: {
    type: Boolean,
    required: true,
  },
  recordTitle: {
    type: String,
    default: '',
  },
  editData: {
    type: Object,
    default: () => {},
  },
});

/**
 * 取得患者信息详细
 */
function show() {
  console.log(props.recordTitle, 'props.recordTitle', props.editData, 'props.editData');
  title.value = '';
  title.value = props.recordTitle;
  reset();
  if (title.value === '编辑模板') {
    console.log(typeof props.editData.useScopeCode, 'props.editData',props.editData.useScopeCode);
    templateName.value = props.editData.templateName;
    contextJson.value = props.editData.contextJson;
    radio.value = +props.editData.useScopeCode;
  }
}
/**
 * 弹窗确认操作，保存模板
 */
function submit() {
  console.log(title.value, 'title');
  if (title.value === '新增模板') {
    const insertParam = {
      templateName: templateName.value,
      templateTypeCode: '1',
      useScopeCode: radio.value,
      contextJson: contextJson.value,
    };

    if (radio.value == 1) {
      insertParam.userId = userStore.id;
    } else if (radio.value == 2) {
      insertParam.userId = userStore.orgId;
    } else {
      insertParam.userId = '';
    }

    console.log(insertParam, 'insertParam');
    saveRecordTemplate(insertParam).then((res) => {
      if (res.code == 200) {
        emits('close', 'success');
        reset();
      }
    });
  } else {
    const updateParamParam = {
      id: props.editData.id,
      templateName: templateName.value,
      templateTypeCode: '1',
      useScopeCode: radio.value,
      contextJson: contextJson.value,
    };
    if (radio.value == 1) {
      updateParamParam.userId = userStore.id;
    } else if (radio.value == 2) {
      updateParamParam.userId = userStore.orgId;
    } else {
      updateParamParam.userId = '';
    }
    console.log(updateParamParam, 'updateParamParam');
    updateRecordTemplate(updateParamParam).then((res) => {
      if (res.code == 200) {
        emits('close', 'success');
        reset();
      }
    });
  }
}

function close() {
  emits('close');
}

function reset() {
  templateName.value = '';
  contextJson.value = '';
  radio.value = 1;
}

defineExpose({
  show,
});
</script>