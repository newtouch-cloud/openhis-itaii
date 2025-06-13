<template>
  <div class="navbar">
    <!-- <hamburger id="hamburger-container" :is-active="appStore.sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" /> -->
    <!-- <breadcrumb id="breadcrumb-container" class="breadcrumb-container" v-if="!settingsStore.topNav" /> -->
    <!-- <top-nav id="topmenu-container" class="topmenu-container" style="border: 1px solid blue;"/> -->

    <div class="right-menu">
      <template v-if="appStore.device !== 'mobile'">
        <header-search id="header-search" class="right-menu-item" />

        <!-- <el-tooltip content="源码地址" effect="dark" placement="bottom">
          <ruo-yi-git id="openhis-git" class="right-menu-item hover-effect" />
        </el-tooltip> -->

        <!-- <el-tooltip content="文档地址" effect="dark" placement="bottom">
          <ruo-yi-doc id="openhis-doc" class="right-menu-item hover-effect" />
        </el-tooltip> -->

        <!-- <screenfull id="screenfull" class="right-menu-item hover-effect" /> -->

        <el-tooltip content="布局大小" effect="dark" placement="bottom">
          <size-select id="size-select" class="right-menu-item hover-effect" />
        </el-tooltip>
      </template>
      <div class="avatar-container">
        <el-dropdown @command="handleCommand" class="right-menu-item hover-effect" trigger="click">
          <div class="avatar-wrapper">
            <img :src="userStore.avatar" class="user-avatar" />
            <span class="nick-name">{{ userStore.nickName }}</span>
            <!-- <el-icon><caret-bottom /></el-icon> -->
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <router-link to="/user/profile">
                <el-dropdown-item>个人中心</el-dropdown-item>
              </router-link>
              <el-dropdown-item command="switch">
                <span>切换科室</span>
              </el-dropdown-item>
              <el-dropdown-item command="setLayout" v-if="settingsStore.showSettings">
                <span>布局设置</span>
              </el-dropdown-item>
              <el-dropdown-item divided command="logout">
                <span>退出登录</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
    <el-dialog title="切换科室" v-model="showDialog" width="400px" append-to-body destroy-on-close>
      <el-select v-model="orgId">
        <el-option
          v-for="item in orgOptions"
          :key="item.orgId"
          :label="item.orgName"
          :value="item.orgId"
        />
      </el-select>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submit">确 定</el-button>
          <el-button @click="showDialog = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ElMessageBox } from 'element-plus';
import Breadcrumb from '@/components/Breadcrumb';
import TopNav from '@/components/TopNav';
import Hamburger from '@/components/Hamburger';
import Screenfull from '@/components/Screenfull';
import SizeSelect from '@/components/SizeSelect';
import HeaderSearch from '@/components/HeaderSearch';
import useAppStore from '@/store/modules/app';
import useUserStore from '@/store/modules/user';
import useSettingsStore from '@/store/modules/settings';
import { getOrg, switchOrg } from '@/api/login';

const appStore = useAppStore();
const userStore = useUserStore();
const settingsStore = useSettingsStore();
const orgOptions = ref([]);
const showDialog = ref(false);
const orgId = ref('');
function toggleSideBar() {
  appStore.toggleSideBar();
}

function handleCommand(command) {
  switch (command) {
    case 'setLayout':
      setLayout();
      break;
    case 'logout':
      logout();
      break;
    case 'switch':
      orgId.value = userStore.orgId;
      getOrg().then((res) => {
        orgOptions.value = res.data;
        showDialog.value = true;
      });
      break;
    default:
      break;
  }
}

function logout() {
  ElMessageBox.confirm('确定注销并退出系统吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(() => {
      userStore.logOut().then(() => {
        location.href = '/index';
      });
    })
    .catch(() => {});
}

function submit() {
  switchOrg(orgId.value).then((res) => {
    if (res.code === 200) {
      userStore.logOut().then(() => {
        location.href = '/index';
      });
    }
  });
}

const emits = defineEmits(['setLayout']);
function setLayout() {
  emits('setLayout');
}
</script>

<style lang='scss' scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background-color: transparent;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding-right: 10px;

  .right-menu {
    display: flex;
    align-items: center;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 6px;
      height: 100%;
      font-size: 16px;
      color: #606266;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(255, 255, 255, 0.1);
        }
      }
    }

    .avatar-container {
      margin-right: 0;
      margin-left: 5px;

      .avatar-wrapper {
        display: flex;
        align-items: center;
        margin-top: 5px;
        gap: 8px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 30px;
          height: 30px;
          border-radius: 6px;
        }
        .nick-name {
          color: #606266;
          font-size: 14px;
          width: 80px;
        }
        .el-icon {
          cursor: pointer;
          position: absolute;
          right: -15px;
          top: 25px;
          font-size: 12px;
          color: #fff;
        }
      }
    }
  }
}
</style>
