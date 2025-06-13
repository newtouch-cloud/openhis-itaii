<!--
 * @Author: hezhibo@bjgoodwill.com
 * @Date: 2025-02-19 11:23:31
 * @Description: 
-->
<template>
    <el-drawer ref="elDrawerComRef" title="基线版医保-预结算" width="640px" v-model="showDrawer" :showFoot="false" :style="{
        maxWidth: '640px', minWidth: '640px',
    }" :showFootLine="false">
        基线版医保-预结算
    </el-drawer>
</template>
<script setup lang='ts'>
import { getCurrentInstance, onBeforeMount, onMounted, reactive } from 'vue'
// const { proxy } = getCurrentInstance();
const emits = defineEmits([])
const props = defineProps({

})
const state = reactive({

})
const showDrawer = defineModel('showDrawer', { type: Boolean, default: false });
// onBeforeMount(() => {

// })
// onMounted(() => {

// })
defineExpose({ state })

</script>
<style lang="scss" scoped></style>