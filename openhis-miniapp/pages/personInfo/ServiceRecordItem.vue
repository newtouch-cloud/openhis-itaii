<template>
  <view class="service-item" @click="handleClick">
    <image :src="icon" class="icon" />
    <text class="title">{{ title }}</text>
  </view>
</template>

<script>
export default {
  props: {
    icon: {
      type: String,
      required: true
    },
    title: {
      type: String,
      required: true
    }
  },
  methods: {
    handleClick() {
      this.$emit('click');
    }
  }
}
</script>

<style>
.service-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
}
.icon {
  width: 40px;
  height: 40px;
}
.title {
  margin-top: 5px;
  font-size: 14px;
}
</style>