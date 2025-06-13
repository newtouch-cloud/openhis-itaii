<!--
 * @Author: sjjh
 * @Date: 2025-04-10 13:02:22
 * @Description:
-->
<template>
  <div class="bedCardsInfo-container">
    <div class="bedCardsInfo" v-for="item in 20" :key="item">
      <div class="">
        <span>{{ bed.roomNo }}-{{ bed.bedName }}</span>
        <el-icon><Male /></el-icon>
        <!-- 男女的标识 -->
      </div>
      <div>
        <el-icon size="40px"><HomeFilled /></el-icon>
      </div>
    </div>
  </div>
</template>
<script setup >
import { getCurrentInstance, onBeforeMount, onMounted, reactive, ref } from 'vue'
// const { proxy } = getCurrentInstance();
const emits = defineEmits(['drop_handler', 'click_handler'])
const props = defineProps({
  // bed: {
  //   type: Object as PropType<Array<Partial<IBedInfo>>>,
  //   require: true,
  //   default: () => {
  //     return {}
  //   },
  // },
})
const state = reactive({})
const bed = ref({
  roomNo: 1,
  bedName: 2,
})
// onBeforeMount(() => {

// })
// onMounted(() => {

// })
defineExpose({ state })
</script>
<style lang="scss" scoped>
.bedCardsInfo-container {
  display: flex;
  flex-wrap: wrap;
  .bedCardsInfo {
    width: 163px;
    height: 104px;
    margin: 8px;
    padding: 0 16px;
    background: #fff;
    border: 1px solid #ddd;
    border-radius: 4px;

    .selectbed {
      cursor: pointer;
      fill: #999;
    }

    &::backdrop {
      outline: 2px solid #13c0b3;

      .selectbed {
        fill: #13c0b3;
      }
    }

    &:hover {
      outline: 2px solid;

      .selectbed {
        fill: #13c0b3;
      }
    }

    > div:first-child {
      display: flex;
      align-items: center;
      justify-content: space-between;
      height: 40px;
      line-height: 40px;
      border-bottom: 1px dashed #ddd;

      // > :last-child {
      //   // text-align: right;
      //   margin-left: auto;
      // }
    }

    > div:nth-child(2) {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 100%;
      height: calc(100% - 41px);
    }
  }

  .bedCardInfo-active {
    outline: 2px solid #13c0b3;

    .selectbed {
      fill: #13c0b3;
    }
  }
}
</style>
