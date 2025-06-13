<!--
 * @Author: yangbo@bjgoodwill.com
 * @Date: 2024-10-23 09:37:17
 * @Description: 患者卡片-孩子节点
-->
<template>
  <div @click="clickAct" style="display: flex">
    <div class="patient-child_broken"></div>
    <div
      class="child-card"
      :style="{
        backgroundColor:
          activeId === data.id ? 'rgba(19,192,179,0.05)' : '#ffffff',
        borderColor: activeId === data.id ? '#13C0B3' : '#eeeeee',
      }"
    >
      <div class="child-bed">
        {{ data.bedName }}
      </div>
      <div class="child-line"></div>
      <div class="child-name">
        {{ data.name }}
        <div class="child-age">{{ data.sexName }}/{{ data.age }}</div>
      </div>
    </div>
  </div>
</template>
<script  setup>
defineOptions({
  name: 'PatientChildCard',
});
const props = defineProps({
  data: {},
  activeId: '',
  parentData: {}
});

const emits = defineEmits(['click']);
const clickAct = () => {
  emits('click', props.data, props.parentData);
};
</script>

<style lang="scss" scoped>
// 孩子节点
.child-card {
  position: relative;
  width: 214px;
  height: 80px;
  margin-left: 4px;
  border: 1px solid;
  border-radius: 4px;
  box-shadow: 0 2px 2px 0 rgba(57.55, 69.04, 86.28, 20%);
  cursor: pointer;

  .child-bed {
    position: absolute;
    top: 8px;
    left: 16px;
    display: flex;
    color: #333;
    font-weight: 600;
    font-size: 16px;
  }

  .child-line {
    position: absolute;
    top: 38px;
    left: 16px;
    width: 182px;
    border-bottom: 1px dashed #ddd;
  }

  .child-num {
    position: absolute;
    top: 9px;
    right: 16px;
    color: #666;
    font-size: 14px;
  }

  .child-name {
    position: absolute;
    top: 48px;
    left: 16px;
    display: flex;
    color: #333;
    font-size: 16px;

    .child-age {
      margin-top: 2.5px;
      margin-left: 16px;
      color: #666;
      font-size: 14px;
    }
  }
}

// 折线
.patient-child_broken {
  width: 17px;
  height: 21px;
  margin-top: 18px;
  border-bottom: 1px solid #ddd;
  border-left: 1px solid #ddd;
}
</style>
