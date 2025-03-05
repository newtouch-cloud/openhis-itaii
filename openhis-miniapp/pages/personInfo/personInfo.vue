<template>
	<view class="container">
		<!-- 用户信息区域 -->
		<view class="user-info">
			<view class="avatar-section">
				<view class="user-info-left">
					<image class="avatar" src="/static/images/user.png"></image>
				</view>
				<view class="user-info-right">
					<view class="name-section">
						<view>
							<text class="name">{{ currentPatient.name }}</text>
							<text class="switch-patient" @click="switchPatient">切换就诊人></text>
						</view>
						<view class="patient-id">
							<text>就诊号：{{ currentPatient.id }}</text>
						</view>
					</view>
				</view>
			</view>
		</view>
		<!-- 顶部功能入口 -->
		<view class="top-section">
			<view class="top-item" @click="navigateTo('my-doctor')">
				<uni-icons custom-prefix="iconfont" type="my-jiaofeijilu" size="30"></uni-icons>
				<text>缴费记录</text>
			</view>
			<view class="top-item" @click="navigateTo('my-doctor')">
				<uni-icons custom-prefix="iconfont" type="my-guahaojilu" size="30"></uni-icons>
				<text>挂号记录</text>
			</view>
			<view class="top-item" @click="navigateTo('my-doctor')">
				<uni-icons custom-prefix="iconfont" type="my-jiuzhenjilu" size="30"></uni-icons>
				<text>就诊记录</text>
			</view>
			<view class="top-item" @click="navigateTo('online-consult')">
				<uni-icons custom-prefix="iconfont" type="my-wodebaogao" size="30"></uni-icons>
				<text>我的报告</text>
			</view>
			<view class="top-item" @click="navigateTo('my-doctor')">
				<uni-icons custom-prefix="iconfont" type="my-jiuzhenrenguanli" size="30"></uni-icons>
				<text>就诊人管理</text>
			</view>
		</view>
		<!-- 功能列表 -->
		<view class="menu-list">
			<navigator v-for="(item, index) in menus" :key="index" class="menu-item" :url="item.url">
				<text>{{ item.label }}</text>
			</navigator>
		</view>

	</view>
</template>
<script setup>
	import {
		ref,
		reactive
	} from 'vue';
	import {
		onLoad
	} from '@dcloudio/uni-app';

	// 用户信息
	const userInfo = reactive({
		avatar: ''
	});

	// 当前就诊人信息
	const currentPatient = ref({
		name: '张三',
		id: '123456'
	});

	// 菜单配置
	const menus = ref([{
			label: '缴费记录',
			url: '/pages/records/payment'
		},
		{
			label: '挂号记录',
			url: '/pages/records/registration'
		},
		{
			label: '就诊记录',
			url: '/pages/records/medical'
		},
		{
			label: '我的报告',
			url: '/pages/records/report'
		},
		{
			label: '就诊人管理',
			url: '/pages/patient/manage'
		}
	]);

	// 处理头像点击
	const handleAvatarClick = () => {
		uni.chooseImage({
			count: 1,
			success: (res) => {
				userInfo.avatar = res.tempFilePaths[0];
			}
		});
	};

	// 切换就诊人
	const switchPatient = () => {
		uni.navigateTo({
			url: '/pages/patient/select'
		});
	};

	// 初始化数据
	onLoad(() => {
		// 这里可以添加请求用户数据的逻辑
	});
</script>

<style scoped lang='scss'>
	.container {
		background-color: #f5f5f5;
		min-height: 100vh;
	}

	/* 设置整个用户信息区域 */
	.user-info {
		background-color: #fff;
		padding-top: 20rpx;
		padding-left: 20rpx;
		padding-right: 20rpx;
		display: flex;
		/* 启用Flex布局 */
		justify-content: space-between;
		/* 在水平上分配空间 */
		align-items: center;
		/* 垂直方向居中对齐 */
	}

	/* 名字和切换按钮布局 */
	.name-section {
		display: flex;
		flex-direction: column;
	}

	/* 名字布局 */
	.name {
		font-weight: bold;
	}

	/* 选择就诊人布局 */
	.switch-patient {
		border: #007aff;
		color: #007aff;
		padding-left: 10px;
		font-size: 28rpx;
	}

	/* 就诊号 */
	.patient-id {
		font-size: 26rpx;
		color: #666;
		padding-top: 10rpx;
	}

	.avatar-section {
		display: flex;
		align-items: center;
		margin-bottom: 20rpx;

		/* 左侧区域 */
		.user-info-left {
			display: flex;
			/* 启用Flex布局 */
			align-items: center;
			/* 垂直方向居中 */
		}

		/* 右侧区域 */
		.user-info-right {
			display: flex;
			/* 启用Flex布局 */
			align-items: center;
			/* 垂直方向居中 */
		}
	}

	.avatar {
		width: 120rpx;
		height: 120rpx;
		border-radius: 50%;
		margin-right: 30rpx;
	}

	.menu-list {
		margin-top: 20rpx;
		background-color: #fff;
	}

	/* 	.menu-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 30rpx;
		border-bottom: 2rpx solid #eee;
	} */
	.top-section {
		display: flex;
		margin: 20rpx 0;
		padding: 30rpx;
		background-color: #fff;
		border-radius: 16rpx;

		.top-item {
			display: flex;
			flex-direction: column;
			align-items: center;
			width: 45%;
			font-size: 25	rpx;

			.image {
				width: 100rpx;
				height: 100rpx;
				margin-bottom: 20rpx;
			}

			.text {
				/* font-size: 20rpx ; */
				color: #333;
			}
		}
	}
</style>