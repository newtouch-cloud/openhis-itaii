<template>
  <div
    class="sidebar-logo-container"
    :class="{ collapse: collapse }"
    :style="{
      backgroundColor:
        sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground,
    }"
  >
    <!-- <el-image
      :src="sideTheme === 'theme-dark' ? Logo : Logo"
      class="sidebar-logo"
      fit="scale-down"
    /> -->
    <!-- <transition name="sidebarLogoFade">
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" to="/">
        <el-image
          v-if="logo"
          :src="sideTheme === 'theme-dark' ? logoNew : logoBlack"
          class="sidebar-logo"
          fit="scale-down"
        />
      </router-link>
      <router-link v-else key="expand" class="sidebar-logo-link" to="/">
        <el-image
          v-if="logo"
          :src="sideTheme === 'theme-dark' ? logoNew : logoBlack"
          class="sidebar-logo"
          fit="scale-down"
        />
      </router-link>
    </transition> -->
  </div>
</template>

<script setup>
import variables from '@/assets/styles/variables.module.scss';
import logoNew from '@/assets/logo/logoNew.png';
import Logo from '@/assets/images/jlau.jpg';
import logoBlack from '@/assets/logo/logoBlack.png';
import useSettingsStore from '@/store/modules/settings';
import { ref, computed } from 'vue';

defineProps({
  collapse: {
    type: Boolean,
    required: true,
  },
});

const title = import.meta.env.VITE_APP_TITLE;
const settingsStore = useSettingsStore();
const sideTheme = computed(() => settingsStore.sideTheme);
const logo = ref(true);
</script>

<style lang="scss" scoped>
.sidebarLogoFade-enter-active {
  transition: opacity 1.5s;
}

.sidebarLogoFade-enter,
.sidebarLogoFade-leave-to {
  opacity: 0;
}

.sidebar-logo-container {
  position: relative;
  width: auto;
  min-width: 120px;
  max-width: 160px;
  height: 50px;
  line-height: 50px;
  background: transparent;
  text-align: center;
  overflow: hidden;
  padding: 0 8px;
  margin-left: 5px;

  & .sidebar-logo-link {
    height: 100%;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;

    & .sidebar-logo {
      width: auto;
      max-width: 120px;
      height: 28px;
      vertical-align: middle;
    }

    & .sidebar-title {
      display: inline-block;
      margin: 0;
      color: #fff;
      font-weight: 600;
      line-height: 50px;
      font-size: 14px;
      font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
      vertical-align: middle;
    }
  }

  &.collapse {
    .sidebar-logo {
      margin-right: 0px;
    }
  }
}
</style>
