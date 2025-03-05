<template>
  <view class="service-page">
    <!-- 顶部功能入口 -->
    <view class="top-section">
      <view class="top-item" @click="navigateTo('my-doctor')">
        <image src="/static/icons/doctor.png" mode="aspectFit"></image>
        <text>我的医生</text>
      </view>
      <view class="top-item" @click="navigateTo('online-consult')">
        <image src="/static/icons/online.png" mode="aspectFit"></image>
        <text>一键在线复诊</text>
      </view>
    </view>

    <!-- 会员中心 -->
    <view class="vip-section" @click="navigateTo('vip-center')">
      <image src="/static/icons/vip.png" mode="aspectFit"></image>
      <text>会员中心 · 精选权益超值享</text>
    </view>

    <!-- 服务与工具网格 -->
    <view class="service-grid">
      <view 
        class="grid-item" 
        v-for="(item, index) in serviceList" 
        :key="index"
        @click="handleServiceClick(item)"
      >
        <image :src="item.icon" mode="aspectFit"></image>
        <text>{{ item.title }}</text>
      </view>
    </view>

    <!-- 商务合作 -->
    <view class="cooperation" @click="navigateTo('business')">
      <text>商务合作</text>
      <image src="/static/icons/arrow-right.png" mode="aspectFit"></image>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue';

// 服务列表数据
const serviceList = ref([
  { id: 1, title: '收藏', icon: '/static/icons/collect.png', type: 'collect' },
  { id: 2, title: '我的医生', icon: '/static/icons/doctor.png', type: 'my-doctor' },
  { id: 3, title: '随访计划', icon: '/static/icons/follow-up.png', type: 'follow-plan' },
  { id: 4, title: '客服中心', icon: '/static/icons/service.png', type: 'customer-service' }
]);

// 处理服务点击
const handleServiceClick = (item) => {
  switch(item.type) {
    case 'customer-service':
      uni.makePhoneCall({ phoneNumber: '400-123-4567' });
      break;
    default:
      navigateTo(item.type);
  }
};

// 通用跳转方法
const navigateTo = (path) => {
  uni.navigateTo({ url: `/pages/${path}/${path}` });
};
</script>

<style lang="scss" scoped>
.service-page {
  padding: 20rpx;
  background-color: #f5f5f5;
}

.top-section {
  display: flex;
  justify-content: space-between;
  margin: 20rpx 0;
  padding: 30rpx;
  background-color: #fff;
  border-radius: 16rpx;

  .top-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 45%;

    image {
      width: 100rpx;
      height: 100rpx;
      margin-bottom: 20rpx;
    }

    text {
      font-size: 28rpx;
      color: #333;
    }
  }
}

.vip-section {
  display: flex;
  align-items: center;
  padding: 30rpx;
  margin: 20rpx 0;
  background-color: #fff;
  border-radius: 16rpx;

  image {
    width: 60rpx;
    height: 60rpx;
    margin-right: 20rpx;
  }

  text {
    font-size: 28rpx;
    color: #ff9900;
  }
}

.service-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20rpx;
  padding: 30rpx;
  background-color: #fff;
  border-radius: 16rpx;

  .grid-item {
    display: flex;
    flex-direction: column;
    align-items: center;

    image {
      width: 80rpx;
      height: 80rpx;
      margin-bottom: 20rpx;
    }

    text {
      font-size: 24rpx;
      color: #666;
    }
  }
}

.cooperation {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  margin-top: 20rpx;
  background-color: #fff;
  border-radius: 16rpx;

  text {
    font-size: 28rpx;
    color: #333;
  }

  image {
    width: 32rpx;
    height: 32rpx;
  }
}
</style>