<!--
 * @Author: sjjh
 * @Date: 2025-04-09 17:53:29
 * @Description: 
-->
<template>
  <div class="ball-tag" v-show="tagId" :style="{ backgroundColor: _tag?.color }">
    {{ _tag?.name }}
  </div>
</template>

<script setup>
import { computed } from 'vue'
const props = defineProps({
  tagId: {
    type: String,
    default: '',
  },
})
const tagList = [
  {
    id: '危',
    name: '危',
    color: '#ba7bc8',
  },
  {
    id: '径',
    name: '径',
    color: '#57bfe0',
  },
  {
    id: '术',
    name: '术',
    color: '#51ccc2',
  },
  {
    id: '预警',
    name: '￥',
    color: '#ee9038',
  },
  {
    id: '欠',
    name: '￥',
    color: '#E95657',
  },
  {
    id: '敏',
    name: '敏',
    color: '#5585e3',
  },
  {
    id: '重',
    name: '重',
    color: '#E95657',
  },
]

const _tag = computed(() => {
  if (!props.tagId) {
    return tagList[0]
  } else {
    return tagList.find((item) => item.id == props.tagId)
  }
})
</script>

<style lang="scss" scoped>
.ball-tag {
  display: inline-flex;
  flex-shrink: 0; // 防止子元素缩小
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  font-size: 13px;
  border-radius: 20px;
  color: white;
}
</style>
