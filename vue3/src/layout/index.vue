<template>
  <div :class="classObj" class="app-wrapper" :style="{ '--current-color': theme }">
    <div
      v-if="device === 'mobile' && sidebar.opened"
      class="drawer-bg"
      @click="handleClickOutside"
    />
    <div class="top-container">
      <sidebar v-if="!sidebar.hide" class="sidebar-container" />
      <!-- <navbar @setLayout="setLayout" class="navbar-container" /> -->
    </div>
    <div :class="{ hasTagsView: needTagsView, sidebarHide: sidebar.hide }" class="main-container">
      <div :class="{ 'fixed-header': fixedHeader }">
        <tags-view v-if="needTagsView" />
      </div>
      <app-main />
      <settings ref="settingRef" />
    </div>
  </div>
</template>

<script setup>
import { useWindowSize } from '@vueuse/core';
import Sidebar from './components/Sidebar/index.vue';
import { AppMain, Navbar, Settings, TagsView } from './components';
import defaultSettings from '@/settings';

import useAppStore from '@/store/modules/app';
import useSettingsStore from '@/store/modules/settings';

const settingsStore = useSettingsStore();
const theme = computed(() => settingsStore.theme);
const sideTheme = computed(() => settingsStore.sideTheme);
const sidebar = computed(() => useAppStore().sidebar);
const device = computed(() => useAppStore().device);
const needTagsView = computed(() => settingsStore.tagsView);
const fixedHeader = computed(() => settingsStore.fixedHeader);

const classObj = computed(() => ({
  hideSidebar: !sidebar.value.opened,
  openSidebar: sidebar.value.opened,
  withoutAnimation: sidebar.value.withoutAnimation,
  mobile: device.value === 'mobile',
}));

const { width, height } = useWindowSize();
const WIDTH = 992; // refer to Bootstrap's responsive design

watchEffect(() => {
  if (device.value === 'mobile' && sidebar.value.opened) {
    useAppStore().closeSideBar({ withoutAnimation: false });
  }
  if (width.value - 1 < WIDTH) {
    useAppStore().toggleDevice('mobile');
    useAppStore().closeSideBar({ withoutAnimation: true });
  } else {
    useAppStore().toggleDevice('desktop');
  }
});

function handleClickOutside() {
  useAppStore().closeSideBar({ withoutAnimation: false });
}

const settingRef = ref(null);
function setLayout() {
  settingRef.value.openSetting();
}
</script>

<style lang="scss" scoped>
@import '@/assets/styles/mixin.scss';
@import '@/assets/styles/variables.module.scss';

.app-wrapper {
  @include clearfix;
  position: relative;
  height: 100%;
  width: 100%;

  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}

.drawer-bg {
  background: #000;
  opacity: 0.3;
  width: 100%;
  top: 0;
  height: 100%;
  position: absolute;
  z-index: 999;
}

.top-container {
  display: flex;
  align-items: center;
  width: 100%;
  height: 50px;
  background-color: $base-menu-background;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1001;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.35);
}

.sidebar-container {
  flex: 1;
  height: 50px;
  min-width: 0;
  overflow: hidden;
}

.navbar-container {
  min-width: 280px;
  height: 50px;
}

.fixed-header {
  position: fixed;
  top: 50px;
  right: 0;
  z-index: 9;
  width: 100%;
  transition: width 0.28s;
}

.mobile .fixed-header {
  width: 100%;
}

.main-container {
  padding-top: 50px;
}
</style>
