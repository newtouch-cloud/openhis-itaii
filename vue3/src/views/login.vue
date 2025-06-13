<template>
  <div class="login">
    <!--  顶部  -->
    <!-- <div class="el-login-top">
      <el-image :src="logoNew"></el-image>
    </div> -->
    <el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="login-form">
      <h1 class="title">his管理系统</h1>
      <el-form-item prop="username">
        <el-input
          v-model="loginForm.username"
          type="text"
          size="large"
          auto-complete="off"
          placeholder="账号"
          @input="handleUsernameChange"
        >
          <template #prefix
            ><svg-icon icon-class="user" class="el-input__icon input-icon"
          /></template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          size="large"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter="handleLogin"
          show-password
        >
          <template #prefix
            ><svg-icon icon-class="password" class="el-input__icon input-icon"
          /></template>
        </el-input>
      </el-form-item>
      <el-form-item prop="tenantId">
        <el-select
          v-model="loginForm.tenantId"
          size="large"
          placeholder="所属医院"
          clearable
          filterable
        >
        <el-option
          v-for="item in tenantOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
        </el-select>
      </el-form-item>
      <!--<el-form-item prop="code" v-if="captchaEnabled">
        <el-input
          v-model="loginForm.code"
          size="large"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter="handleLogin"
        >
          <template #prefix><svg-icon icon-class="validCode" class="el-input__icon input-icon" /></template>
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="login-code-img"/>
        </div>
      </el-form-item>
      <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>-->
      <el-form-item style="width: 100%">
        <el-button
          :loading="loading"
          size="large"
          type="primary"
          style="width: 100%"
          @click.prevent="handleLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else-if="!signIng">登 录 中...</span>
          <span v-else>连接医保中...</span>
        </el-button>
        <div style="margin-top: 10px" v-if="env === 'development'">
          <el-button
            :loading="loading"
            link
            type="primary"
            style="width: 20%"
            @click.prevent="handleUserName('admin')"
          >
            管理员
          </el-button>
          <el-button
            :loading="loading"
            link
            type="primary"
            style="width: 20%"
            @click.prevent="handleUserName('doctor')"
          >
            医生
          </el-button>
          <el-button
            :loading="loading"
            link
            type="primary"
            style="width: 20%"
            @click.prevent="handleUserName('drug')"
          >
            药房
          </el-button>
          <!-- <el-button
            :loading="loading"
            type="primary"
            style="width: 20%"
            @click.prevent="handleUserName()"
          >
          款台
          </el-button> -->
          <el-button
            :loading="loading"
            link
            type="primary"
            style="width: 20%"
            @click.prevent="handleUserName('nurse')"
          >
            护士
          </el-button>
        </div>
        <div style="float: right" v-if="register">
          <router-link class="link-type" :to="'/register'">立即注册</router-link>
        </div>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <div class="el-login-footer-link">
        <span><el-link :underline="false">his账号用户协议</el-link></span>
        <span>|</span>
        <span>
          <el-link :underline="false">关于his账号与隐私的声明</el-link>
        </span>
        <span>|</span>
        <span><el-link :underline="false">常见问题</el-link></span>
        <span>|</span>
        <span><el-link :underline="false">Cookies</el-link></span>
      </div>
      <span>
        <el-link :underline="false">Copyright © 2018-2024 his.vip All Rights Reserved. </el-link>
      </span>
    </div>
  </div>
</template>

<script setup>
import { getCodeImg ,sign ,getUserBindTenantList } from '@/api/login';
import Cookies from 'js-cookie';
import { encrypt, decrypt } from '@/utils/jsencrypt';
import useUserStore from '@/store/modules/user';
import logoNew from '@/assets/logo/logoBlack.png';
const userStore = useUserStore();
const route = useRoute();
const router = useRouter();
const { proxy } = getCurrentInstance();
const env = import.meta.env.MODE;

const loginForm = ref({
  username: '',
  password: '',
  rememberMe: false,
  code: '',
  uuid: '',
  tenantId: '',
});

const tenantOptions = ref([]);

const loginRules = {
  username: [{ required: true, trigger: 'blur', message: '请输入您的账号' }],
  password: [{ required: true, trigger: 'blur', message: '请输入您的密码' }],
  code: [{ required: true, trigger: 'change', message: '请输入验证码' }],
};

const codeUrl = ref('');
const loading = ref(false);
const signIng = ref(false);
// 验证码开关
const captchaEnabled = ref(true);
// 注册开关
const register = ref(false);
const redirect = ref(undefined);

watch(
  route,
  (newRoute) => {
    redirect.value = newRoute.query && newRoute.query.redirect;
  },
  { immediate: true }
);

function handleLogin() {
  proxy.$refs.loginRef.validate((valid) => {
    if (valid) {
      loading.value = true;
      // 勾选了需要记住密码设置在 cookie 中设置记住用户名和密码
      if (loginForm.value.rememberMe) {
        Cookies.set('username', loginForm.value.username, { expires: 30 });
        Cookies.set('password', encrypt(loginForm.value.password), {
          expires: 30,
        });
        Cookies.set('rememberMe', loginForm.value.rememberMe, { expires: 30 });
      } else {
        // 否则移除
        Cookies.remove('username');
        Cookies.remove('password');
        Cookies.remove('rememberMe');
      }
      // 调用action的登录方法
      userStore
        .login(loginForm.value)
        .then(() => {
          const query = route.query;
          const otherQueryParams = Object.keys(query).reduce((acc, cur) => {
            if (cur !== 'redirect') {
              acc[cur] = query[cur];
            }
            return acc;
          }, {});
          if (chrome.webview === undefined) {
            router.push({ path: redirect.value || '/', query: otherQueryParams });
          } else {
            signIng.value = true;
            userStore.getInfo();
            GetMacString();
          }
        })
        .catch(() => {
          loading.value = false;
          // 重新获取验证码
          if (captchaEnabled.value) {
            //getCode();
          }
        });
    }
  });
}
// 获取MAC 加密地址
async function GetMacString() {
  if (chrome.webview === undefined) {
  } else {
    try {
      // 必须参数
      const data = {
        IP: 'ddjk.jlhs.gov.cn',
        PORT: 20215,
        TIMEOUT: 50,
        LOG_PATH: 'C:/neu_log/',
        SFZ_DRIVER_TYPE: 0,
      };
      // 获取 mac 地址
      let result = await window.chrome.webview.hostObjects.CSharpAccessor.GetMacString(
        JSON.stringify(data)
      );
      if (result === undefined) {
        alert('获取 mac 地址失败！');
        return;
      }
      let ip = await window.chrome.webview.hostObjects.CSharpAccessor.GetIpString();
      if (ip === undefined) {
        alert('获取 ip 地址失败！');
        return;
      }
      // 医保签到
      signIn(result, ip);
      // 解析返回的结果
      console.log('result', result);
      // let cardInfo = JSON.parse(jsonResult);
    } catch (error) {
      console.error('调用失败:', error);
    }
  }
}
function getCode() {
  getCodeImg().then((res) => {
    captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled;
    if (captchaEnabled.value) {
      codeUrl.value = 'data:image/gif;base64,' + res.img;
      loginForm.value.uuid = res.uuid;
    }
  });
}

//账号变化时
function handleUsernameChange(newVal) {
  getTenantList(newVal); 
}

//查询租户列表
function getTenantList(username) {
  if(!username){
    return;
  }
  getUserBindTenantList(username).then((res) => {
    loginForm.value.tenantId = ''
    if(res.code == 200){
      if(res.data.length > 0){
        tenantOptions.value = res.data.map(item => ({
          value: item.id,
          label: item.tenantName
        }));
        loginForm.value.tenantId = tenantOptions.value[0].value;//默认选中第一个
      }
    }
  });
}

/**
 * description: 签到方法
 * @param practitionerId 参与者 id
 * @param mac 用户 mac 地址
 * @param ip 签到的 IP 地址
 */
async function signIn(mac, ip) {
  // 用户 id
  const practitionerId = userStore.practitionerId;
  console.log('userStore', userStore);
  // 签到的 IP 地址
  try {
    const response = await sign(practitionerId, mac, ip);
    if (response.code !== 200) {
      throw new Error('签到失败，错误信息：' + response.msg);
    }
    signIng.value = false;
    loading.value = false;
    const query = route.query;
    const otherQueryParams = Object.keys(query).reduce((acc, cur) => {
      if (cur !== 'redirect') {
        acc[cur] = query[cur];
      }
      return acc;
    }, {});
    userStore.removeRoles();
    router.push({ path: redirect.value || '/', query: otherQueryParams });
    console.log('签到成功:', response);
  } catch (error) {
    userStore.logOut();
    signIng.value = false;
    loading.value = false;
    proxy.$message.error('医保签到失败');
    console.error('签到失败:', error);
  }
}
function getCookie() {
  const username = Cookies.get('username');
  const password = Cookies.get('password');
  const rememberMe = Cookies.get('rememberMe');
  loginForm.value = {
    username: username === undefined ? loginForm.value.username : username,
    password: password === undefined ? loginForm.value.password : decrypt(password),
    rememberMe: rememberMe === undefined ? false : Boolean(rememberMe),
  };
}

function handleUserName(value) {
  let user = {
    admin: {
      username: 'admin',
      password: 'admin123',
    },
    doctor: {
      username: 'gjlin',
      password: 'wi123456',
    },
    drug: {
      username: 'mqyk',
      password: 'mqyk123',
    },
    nurse: {
      username: 'nmhs',
      password: 'nmhs123',
    },
    charge: {
      username: 'admin',
      password: 'admin123',
    },
  };
  loginForm.value.username = user[value].username;
  loginForm.value.password = user[value].password;
  handleLogin();
}

//getCode();
getCookie();

getTenantList(loginForm.value.username)
</script>

<style lang="scss" scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  //background-image: url("../assets/images/login-background.jpg");
  background-size: cover;
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #000;
  font-family: 'Microsoft Yahei,STHeiti,Simsun,STSong,Helvetica Neue,Helvetica,Arial,sans-serif';
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;
  .el-input {
    height: 50px; // 修改输入框高度
    input {
      height: 50px; // 修改输入框高度
      font-size: 18px; // 修改输入框内文字大小
    }
  }
  .input-icon {
    height: 49px; // 调整图标高度以适应输入框高度
    width: 14px;
    margin-left: 0px;
  }
  
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 50px; // 调整验证码区域高度以适应输入框高度
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-top {
  height: 80px;
  line-height: 40px;
  position: fixed;
  top: 0;
  width: 100%;
  text-align: left;
  color: #000;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
  background-color: #f1f1f1;
  padding: 20px;
}
.el-login-footer {
  height: 100px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #000;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
  background-color: #f1f1f1;

  color: #000;
  span {
    margin: 0 10px;
  }
}
.login-code-img {
  height: 50px; // 调整验证码图片高度以适应输入框高度
  padding-left: 12px;
}
:deep(.el-input__wrapper) {
  background-color: #f5f7fa !important;
  border-color: #e4e7ed !important;
  border-radius: 10px !important;
  font-size: 18px !important;
  height: 50px !important; // 修改输入框高度
}
:deep(.el-button--large) {
  font-size: 20px !important; // 增加按钮内文字大小
  height: 50px !important; // 增加按钮高度
  padding: 10px 20px !important; // 调整按钮内边距
  border-radius: 10px !important;
}
:deep(.el-input__suffix-inner .el-input__icon) {
  width: 24px !important; // 调整图标的宽度
  height: 24px !important; // 调整图标的高度
  font-size: 24px !important; // 调整图标的字体大小
}
:deep(.el-select__wrapper) {
  background-color: #f5f7fa !important;
  border-color: #e4e7ed !important;
  border-radius: 10px !important;
  font-size: 18px !important;
  height: 50px !important; // 修改输入框高度
}
</style>
