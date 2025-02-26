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
            <el-form-item label-width="100" label="项目编号" prop="itemNo">
              <el-input v-model="fromModel.itemNo" disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label-width="100" label="项目名称" prop="chargeName">
              <el-input v-model="fromModel.chargeName" disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label-width="100" label="规格" prop="totalVolume">
              <el-input v-model="fromModel.totalVolume" disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label-width="100" label="单位" prop="unitCode">
              <el-input v-model="fromModel.unitCode" disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label-width="100" label="拆零比" prop="partPercent">
              <el-input-number
                v-model="fromModel.partPercent"
                :min="0"
                :max="999999.99"
                :step="0.01"
                :precision="2"
                disabled="true"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label-width="100"
              label="指导价"
              prop="conditionYbCode"
            >
              <el-input-number
                v-model="fromModel.conditionYbCode"
                :min="0"
                :max="999999.99"
                :step="0.01"
                :precision="2"
                disabled="true"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label-width="100" label="价格" prop="amount">
              <el-input-number
                v-model="fromModel.amount"
                :min="0"
                :max="999999.99"
                :step="0.01"
                :precision="2"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label-width="100"
              label="拆零最小单位"
              prop="partMinUnitCode"
            >
              <el-input v-model="fromModel.partMinUnitCode" disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label-width="100"
              label="拆零指导价"
              prop="partConditionPrice"
            >
              <el-input-number
                v-model="fromModel.partConditionPrice"
                :min="0"
                :max="999999.99"
                :step="0.01"
                :precision="2"
                disabled="true"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label-width="100" label="拆零价格" prop="price">
              <el-input-number
                v-model="fromModel.price"
                :min="0"
                :max="999999.99"
                :step="0.01"
                :precision="2"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label-width="100" label="状态" prop="statusEnum">
              <el-select
                v-model="fromModel.statusEnum"
                placeholder="请选择状态"
              >
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label-width="100" label="调价说明" prop="description">
              <el-input
                v-model="fromModel.description"
                style="width: 100%"
                autosize
                type="textarea"
                placeholder="请输入调价说明"
                maxlength="200"
                show-word-limit
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
import { getOptions } from "./definition";
const { proxy } = getCurrentInstance();
const { charge_item_status } = proxy.useDict("charge_item_status");
const emit = defineEmits(["submit", "update:open"]);
const props = defineProps({
  title: String,
  open: Boolean,
  formData: Object,
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

/**获取状态下拉列表 */
const getStatusOptions = () => {
  getOptions({}).then((response) => {
    options.value = response.data;
    console.log(options.value);
  });
};

/**
 * 取消操作的函数
 */
const cancel = () => {
  definitionRef.value.resetFields();
  emit("update:open", false);
};

watch(
  () => props.open,
  (newVal) => {
    localOpen.value = newVal;
    console.log(props.form);
    fromModel.value = props.formData;
    getStatusOptions();
    if (!newVal) {
      // 如果对话框关闭，重置表单
      definitionRef.value.resetFields();
    }
  }
);
</script>
<style>
</style>