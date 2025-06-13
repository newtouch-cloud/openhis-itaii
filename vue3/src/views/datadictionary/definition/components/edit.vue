<template>
  <el-dialog
    :title="title"
    v-model="localOpen"
    width="800px"
    append-to-body
    @close="cancel"
  >
    <template #header>
      <div class="custom-header">
        <span>{{ title }}</span>
      </div>
    </template>
    <div class="scrollable-content">
      <el-form ref="definitionRef" :model="fromModel" label-width="140px">
        <el-row>
          <el-col :span="8">
            <el-form-item label-width="100" label="项目名称" prop="chargeName">
              <el-input v-model="fromModel.chargeName" disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label-width="100" label="所属科室" prop="orgId_dictText">
              <el-input v-model="fromModel.orgId_dictText" disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label-width="100" label="财务类别" prop="typeCode_dictText">
              <el-input v-model="fromModel.typeCode_dictText" disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label-width="100" label="医保类别" prop="ybType_dictText">
              <el-input v-model="fromModel.ybType_dictText" disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label-width="100"
              label="基础价格"
              prop="price"
            >
              <el-input-number
                v-model="fromModel.price"
                :min="0"
                :max="999999.99"
                :step="0.01"
                :precision="2"
                controls-position="right"
                :controls="false"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script setup>
const emit = defineEmits(["submit", "update:open"]);
const props = defineProps({
  title: String,
  open: Boolean,
  formData: Object,
  statusOptions: Object
});
const localOpen = ref(props.open);
const definitionRef = ref(null);
const fromModel = ref(props.formData);
const options = ref([]);
/**
 * 提交表单函数
 */
const submitForm = () => {
  // 调用表单引用上的validate方法进行表单验证
  definitionRef.value.validate((valid) => {
    if (valid) {
      // 验证成功，触发'submit'事件并传递表单数据
      fromModel.value.statusEnum = Number(fromModel.value.statusEnum);
      // fromModel.value.statusEnum = "active"
      emit("submit", fromModel.value);
    } else {
      // 验证失败，显示错误消息
      ElMessage.warning("请确认后再提交");
      return false;
    }
  });
};

/**
 * 取消操作的函数
 */
const cancel = () => {
  emit("update:open", false);
};

watch(
  () => props.open,
  (newVal) => {
    localOpen.value = newVal;
    fromModel.value = JSON.parse(JSON.stringify(props.formData));;
    options.value = props.statusOptions
    if (!newVal) {
      // 如果对话框关闭，重置表单
      definitionRef.value.resetFields();
    }
  }
);
</script>
<style lang="scss" scoped>
:deep(.el-input-number .el-input__inner){
  -webkit-appearance: none;
  -moz-appearance: textfield;
  text-align: left;
  line-height: 1;
}
</style>