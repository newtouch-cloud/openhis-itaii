<template>
  <el-popover
    :popper-style="{ padding: '0' }"
    :placement="placement"
    :visible="isPopoverVisible"
    trigger="manual"
    :width="width"
  >
    <slot name="popover-content" :row="row" :index="index">
      <div>列表内容</div>
    </slot>
    <template #reference>
      <el-input
        v-model="inputValue"
        :placeholder="placeholder"
        @input="handleInput"
        @focus="handleFocus"
        @blur="handleBlur"
      />
    </template>
  </el-popover>
</template>

<script setup>
import { ref, watch, defineProps, defineEmits } from "vue";

const props = defineProps({
  modelValue: {
    type: String,
    default: "",
  },
  row: {
    type: Object,
    default: () => ({}),
  },
  index: {
    type: Number,
    default: -1,
  },
  placeholder: {
    type: String,
    default: "",
  },
  placement: {
    type: String,
    default: "bottom-start",
  },
  width: {
    type: Number,
    default: 800,
  },
});

const emit = defineEmits([
  "update:modelValue",
  "selsect-advice-base",
  "focus",
  "blur",
]);

const isPopoverVisible = ref(false);
const inputValue = ref(props.modelValue);

// 同步外部modelValue变化
watch(
  () => props.modelValue,
  (newVal) => {
    inputValue.value = newVal;
  }
);

const handleInput = (value) => {
  emit("search", value);
};

const handleFocus = () => {
  isPopoverVisible.value = true;
  emit("focus", props.row, props.index);
};

const handleBlur = () => {
  // 延迟隐藏以允许选择操作完成
  isPopoverVisible.value = false;
  emit("blur", props.row);
};

const handleSelectAdvice = (payload) => {
  isPopoverVisible.value = false;
  emit("selsect-advice-base", payload);
};
</script>