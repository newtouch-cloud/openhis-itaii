<template>
  <div
    :class="{ 'has-logo': showLogo }"
    :style="{
      backgroundColor:
        sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground,
    }"
  >
    <!-- <logo v-if="showLogo" :collapse="isCollapse" /> -->
    <!-- <el-scrollbar :class="sideTheme" wrap-class="scrollbar-wrapper"> -->
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="
          sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground
        "
        :text-color="sideTheme === 'theme-dark' ? variables.menuColor : variables.menuLightColor"
        :unique-opened="true"
        :active-text-color="theme"
        :collapse-transition="false"
        mode="horizontal"
      >
        <sidebar-item
          v-for="(route, index) in sidebarRouters"
          :key="route.path + index"
          :item="route"
          :base-path="route.path"
        />
      </el-menu>
    <!-- </el-scrollbar> -->
    <navbar @setLayout="setLayout" class="navbar-container" />
    <settings ref="settingRef" />
  </div>
</template>

<script setup>
import Logo from './Logo';
import SidebarItem from './SidebarItem';
import variables from '@/assets/styles/variables.module.scss';
import useAppStore from '@/store/modules/app';
import useSettingsStore from '@/store/modules/settings';
import usePermissionStore from '@/store/modules/permission';
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import { Navbar, Settings } from '@/layout/components';

import defaultSettings from '@/settings';
const route = useRoute();
const appStore = useAppStore();
const settingsStore = useSettingsStore();
const permissionStore = usePermissionStore();

const sidebarRouters = computed(() => permissionStore.sidebarRouters);
const showLogo = computed(() => settingsStore.sidebarLogo);
const sideTheme = computed(() => settingsStore.sideTheme);
const theme = computed(() => settingsStore.theme);
const isCollapse = computed(() => !appStore.sidebar.opened);

const activeMenu = computed(() => {
  const { meta, path } = route;
  // if set path, the sidebar will highlight the path you set
  if (meta.activeMenu) {
    return meta.activeMenu;
  }
  return path;
});
const settingRef = ref(null);
function setLayout() {
  settingRef.value.openSetting();
}
</script>

<style lang="scss" scoped>
.scrollbar-wrapper {
  overflow-x: auto !important;
  width: 100%;
}

.el-menu--horizontal {
  display: flex !important;
  border-bottom: none !important;
  background-color: transparent !important;
  min-width: auto;
  flex-wrap: nowrap;

  & > .el-menu-item,
  & > .el-sub-menu {
    height: 50px;
    line-height: 50px;
    color: #fff;
    padding: 0 20px !important;
    font-size: 14px;
    min-width: 120px !important;
    border: 1px solid red !important;
  }

  :deep(.svg-icon) {
    margin-right: 8px !important;
    font-size: 14px;
  }

  :deep(.el-sub-menu__title) {
    padding-right: 25px !important;
  }

  :deep(.el-sub-menu__icon-arrow) {
    right: 8px !important;
  }

  :deep(.menu-title) {
    font-size: 13px;
  }
}

/* 水平布局，并与 Navbar 正确配合 */
div {
  &.has-logo {
    display: flex;
    flex-direction: row;
    align-items: center;
    height: 50px;
    padding: 0;
    width: 100%;

    & > .el-scrollbar {
      flex: 1;
      height: 50px;
      min-width: 0;
    }
  }
}
</style>
