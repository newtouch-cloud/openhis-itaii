<template>
	<view class="container">
		<!-- 轮播图 -->
		<swiper class="swiper-container" autoplay circular indicator-dots>
			<swiper-item v-for="(item, index) in swiperList" :key="index">
				<image :src="item.image" mode="aspectFill" class="swiper-image"></image>
			</swiper-item>
		</swiper>
		<!-- 用户信息栏 -->
		<!-- 		<view class="user-card">
			<view class="user-info">
				<view class="user-info-left">
					<image class="avatar" src="/static/images/user.png"></image>
				</view>
				<text class="name">{{ currentPatient.name }}</text>
				<text class="switch" @click="switchPatient">切换就诊人 ></text>
			</view>
			<text class="patient-id">就诊号：{{ currentPatient.id }}</text>
		</view> -->
		<view class="user-card">
			<view class="user-info-left">
				<image class="avatar" src="/static/images/user.png"></image>
				<text class="name">{{ currentPatient.name }}</text>
				<text class="switch-patient" @click="switchPatient">切换就诊人 ></text>
			</view>
			<text class="patient-id">就诊号：{{ currentPatient.id }}</text>
		</view>

		<!-- 功能宫格区域 -->
		<scroll-view scroll-y class="main-content">
			<view class="grid-container">
				<view class="section-title">医院服务</view>
				<view class="grid-row">
					<view v-for="(item, index) in firstRow" :key="index" class="grid-item"
						@click="handleFunction(item)">
						<image class="icon" :src="item.icon" mode="aspectFit" />
						<text class="label">{{ item.label }}</text>
					</view>
				</view>
				<view class="grid-row">
					<view v-for="(item, index) in secondRow" :key="index" class="grid-item"
						@click="handleFunction(item)">
						<image class="icon" :src="item.icon" mode="aspectFit" />
						<text class="label">{{ item.label }}</text>
					</view>
				</view>
			</view>

			<view class="section">
				<view class="service-grid">
					<view v-for="(item, index) in hospitalServices" :key="index" class="service-item">
						<text>{{ item }}</text>
					</view>
				</view>
			</view>
		</scroll-view>

	</view>
</template>
<script setup>
	import {
		ref,
		reactive
	} from 'vue';

	// 轮播图数据
	const swiperList = ref([{
			image: '/static/shouye/lunbotu1.png'
		},
		{
			image: '/static/shouye/lunbotu2.png'
		},
		{
			image: '/static/shouye/lunbotu1.png'
		}
	]);
	// 当前就诊人信息
	const currentPatient = reactive({
		name: '张三',
		id: '123456'
	});


	// 功能配置（示例数据）
	const firstRow = ref([{
			label: '健康码',
			icon: '/static/icons/health-code.png',
			path: '/pages/health-code'
		},
		{
			label: '预约挂号',
			icon: '/static/icons/appointment.png',
			path: '/pages/appointment'
		},
		{
			label: '当日挂号',
			icon: '/static/icons/today-reg.png',
			path: '/pages/today-reg'
		},
		{
			label: '门诊缴费',
			icon: '/static/icons/payment.png',
			path: '/pages/payment'
		}
	]);

	const secondRow = ref([{
			label: '查看报告',
			icon: '/static/icons/report.png',
			path: '/pages/report'
		},
		{
			label: '就诊记录',
			icon: '/static/icons/record.png',
			path: '/pages/records'
		},
		{
			label: '科室查询',
			icon: '/static/icons/department.png',
			path: '/pages/department'
		},
		{
			label: '医生查询',
			icon: '/static/icons/doctor.png',
			path: '/pages/doctor'
		}
	]);

	// 医院服务
	const hospitalServices = ref([
		'医院介绍',
		'国粹中医',
		'惠民政策',
		'特色疗法'
	]);

	// 底部导航
	const tabItems = ref([{
			text: '首页',
			icon: '/static/tabs/home.png',
			selectedIcon: '/static/tabs/home-active.png'
		},
		{
			text: '健康卡',
			icon: '/static/tabs/health-card.png',
			selectedIcon: '/static/tabs/health-card-active.png'
		},
		{
			text: '个人中心',
			icon: '/static/tabs/profile.png',
			selectedIcon: '/static/tabs/profile-active.png'
		}
	]);

	const activeTab = ref(0);

	// 切换就诊人
	const switchPatient = () => {
		uni.navigateTo({
			url: '/pages/patient/switch'
		});
	};

	// 处理功能点击
	const handleFunction = (item) => {
		if (item.path) {
			uni.navigateTo({
				url: item.path
			});
		}
	};

	// 切换底部导航
	const switchTab = (index) => {
		activeTab.value = index;
		// 实际项目应使用路由跳转
	};
</script>
<style scoped>
	.swiper-container {
		width: 100%;
		height: 300rpx;
	}

	.swiper-image {
		width: 100%;
		height: 100%;
		object-fit: cover;
		border-radius: 15rpx;
	}

	.container {
		height: 100vh;
		display: flex;
		flex-direction: column;
	}

	.user-card {
		/* background: linear-gradient(135deg, #6eb4ff, #007aff); */
		padding: 30rpx;
		color: black;
	}

	.user-info {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding-bottom: 20rpx;
	}

	.name {
		font-size: 36rpx;
		font-weight: bold;
	}

	.switch {
		padding-left: 10rpx;
		font-size: 28rpx;
		/* text-decoration: underline; */
	}

	.patient-id {
		font-size: 28rpx;
	}

	.main-content {
		flex: 1;
		background-color: #f5f5f5;
	}

	.grid-container {
		padding-top: 1vh;
	}

	.grid-row {
		display: flex;
		justify-content: space-between;
		margin-bottom: 1vh;
	}

	.grid-item {
		width: 23%;
		background: white;
		border-radius: 16rpx;
		padding: 20rpx;
		text-align: center;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
	}

	.icon {
		width: 80rpx;
		height: 80rpx;
		margin-bottom: 15rpx;
	}

	.label {
		font-size: 24rpx;
		color: #333;
	}

	.section {
		background: white;
		margin: 20rpx;
		border-radius: 16rpx;
		padding: 30rpx;
	}

	.section-title {
		font-size: 32rpx;
		font-weight: bold;
		margin-bottom: 30rpx;
	}

	.service-grid {
		display: grid;
		grid-template-columns: repeat(2, 1fr);
		gap: 20rpx;
	}

	.service-item {
		background: #f8f8f8;
		padding: 30rpx;
		border-radius: 12rpx;
		text-align: center;
	}

	.tabbar {
		height: 100rpx;
		background: white;
		display: flex;
		justify-content: space-around;
		align-items: center;
		box-shadow: 0 -4rpx 12rpx rgba(0, 0, 0, 0.05);
	}

	.tab-item {
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.tab-icon {
		width: 48rpx;
		height: 48rpx;
		margin-bottom: 8rpx;
	}

	.active {
		color: #007aff;
	}

	.avatar {
		width: 120rpx;
		height: 120rpx;
		border-radius: 50%;
		margin-right: 30rpx;
	}
</style>