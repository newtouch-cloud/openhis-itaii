import request from '@/utils/request'

// 登录方法
export function login(username, password, code, uuid, tenantId) {
  const data = {
    username,
    password,
    code,
    uuid,
    tenantId
  }
  return request({
    url: '/login',
    headers: {
      isToken: false,
      repeatSubmit: false
    },
    method: 'post',
    data: data
  })
}

// 注册方法
export function register(data) {
  return request({
    url: '/register',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/getInfo',
    method: 'get'
  })
}

// 退出方法
export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}

// 获取验证码
export function getUserBindTenantList(username) {
  return request({
    url: '/system/tenant/user-bind/'+username,
    headers: {
      isToken: false
    },
    method: 'get',
    timeout: 20000
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/captchaImage',
    headers: {
      isToken: false
    },
    method: 'get',
    timeout: 20000
  })
}

// 获取当前登录用户所属科室
export function getOrg() {
  return request({
    url: '/base-data-manage/practitioner/get-selectable-org-list',
    method: 'get',
  })
}

// 切换科室
export function switchOrg(orgId) {
  return request({
    url: '/base-data-manage/practitioner/switch-org?orgId=' + orgId,
    method: 'put',
  })
}

// 医保签到
export function sign(practitionerId, mac, ip) {
  return request({
    url: `/yb-request/sign?practitionerId=${practitionerId}&mac=${mac}&ip=${ip}`,
    method: 'post',
  })
}