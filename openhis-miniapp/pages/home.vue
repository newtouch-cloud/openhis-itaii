<template>
	<view class="content">
		<!-- <image class="logo" src="/static/logo.png"></image>
		<view class="text-area">
			<text class="title">{{title}}</text>
		</view> -->
		<view :style="{visibility: showSkeleton ? 'hidden' : 'visible'}">
			<!-- 导航 -->
			<home-navi @goLogin="openAuto"></home-navi>
			<!-- 轮播 -->
			<home-swiper :dataList="dataBanner"></home-swiper>
			<!-- 菜单 -->
			<home-menus :dataList="dataMenus" @goLogin="openAuto"></home-menus>
			<!-- 健康科普 -->
			<home-article :dataList="dataArticle" @goLogin="openAuto"></home-article>
			<!-- 视频课堂 -->
			<home-video :dataList="dataVideo" @goLogin="openAuto"></home-video>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				title: '1111'
			}
		},
		onLoad() {

		},
		methods: {

		}
	}
</script>

<style>
	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.logo {
		height: 200rpx;
		width: 200rpx;
		margin-top: 200rpx;
		margin-left: auto;
		margin-right: auto;
		margin-bottom: 50rpx;
	}

	.text-area {
		display: flex;
		justify-content: center;
	}

	.title {
		font-size: 36rpx;
		color: #8f8f94;
	}
</style>