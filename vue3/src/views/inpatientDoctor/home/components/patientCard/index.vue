<!--
 * @Author: sjjh
 * @Date: 2025-04-09 17:55:05
 * @Description: 
-->
<template>
  <div class="patient-card" v-for="item in data" :key="item.id" :id="item.id">
    <patient-parent-card :activeId="modelValue" :type="type" :data="item" @click="parentClick">
    </patient-parent-card>

    <!-- // TODO 是否考虑孩子 -->
    <!-- <div v-if="item.children">
      <div
        style="margin-top: 8px"
        v-for="citem in item.children"
        :key="citem.id"
        :id="citem.id"
      >
        <patient-child-card
          :active-id="modelValue"
          :data="citem"
          :parentData="item"
          @click="childClick"
        >
        </patient-child-card>
      </div>
    </div> -->
  </div>
</template>
<script  setup>
import { ref, watch } from 'vue'
import PatientParentCard from './patientParentCard.vue'
// import PatientChildCard from './PatientChildCard.vue'

defineOptions({
  name: 'PatientCard',
})
const props = defineProps({
    modelValue: '',
    data: {},
    type: 0,
    keyChild:'',
    auto: true,
  },
)

const value = ref(props.modelValue)
// const childClick = (node: any, parentVal: any) => {
//   if (props.auto) {
//     value.value = node.id
//   }
//   emits('click', value.value, node, parentVal)
// }

const parentClick = (node) => {
  if (props.auto) {
    value.value = node.id
  }
  emits('click', value.value, node, null)
}

const parentNode = ref()
const getItem = (val) => {
  parentNode.value = null
  const re = props.data.find((item) => {
    return item.id === val
  })
  if (re) return re
  else {
    let rec
    props.data.forEach((item) => {
      if (item.children) {
        item.children.find((citem) => {
          if (citem.id === val) {
            parentNode.value = item
            rec = citem
          }
        })
      }
    })
    return rec
  }
}

const emits = defineEmits(['click', 'change', 'update:modelValue'])
watch(props, (newValue) => {
  value.value = newValue.modelValue
})

watch(value, (val) => {
  emits('update:modelValue', val)
  emits('change', val, getItem(val), parentNode.value)
})
</script>
<style lang="scss" scoped>
.patient-card {
  user-select: none;

  & + .patient-card {
    margin-top: 8px;
  }
}
</style>
