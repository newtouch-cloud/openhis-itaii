<template>
  <div class="container">
    <el-tabs v-model="activeName" type="card" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="待登记入院" name="first">
        <AwaitList v-if="activeName === 'first'" @okList="activeName = 'second'" />
      </el-tab-pane>
      <el-tab-pane label="已登记入院" name="second">
        <AccomplishList v-if="activeName === 'second'" />
      </el-tab-pane>
      <!-- <el-tab-pane label="在院患者" name="third">
        <ExistListt />
      </el-tab-pane> -->
    </el-tabs>
  </div>
</template>
<script setup >
// import { TabsPaneContext } from 'element-plus';
import { getCurrentInstance, onBeforeMount, onMounted, reactive, ref } from 'vue';
import { AwaitList, AccomplishList, ExistList } from './components/index.js';
//const { proxy } = getCurrentInstance();
const emits = defineEmits([]);
const props = defineProps({});
const state = reactive({});
onBeforeMount(() => {});
onMounted(() => {});
defineExpose({ state });

const activeName = ref('first');

// const handleClick = (tab: TabsPaneContext, event: Event) => {
//   console.log(tab, event);
// };
</script>
<style lang="scss" scoped>
.sds {
  background-color: red;
}
</style>
