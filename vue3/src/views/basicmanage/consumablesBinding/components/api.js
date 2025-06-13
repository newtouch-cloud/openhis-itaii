import request from '@/utils/request'

// 查询诊疗项目列表
export function getActivityList(queryParams) {
  return request({
    url: '/personalization/activity-device/activity-page',
    method: 'get',
    params: queryParams
  })
}

// 查询诊疗用法绑定耗材项目列表
export function getBindList(queryParams) {
  return request({
    url: '/personalization/activity-device/activity-device',
    method: 'get',
    params: queryParams
  })
}

// 获取耗材列表
export function getDeviceList(queryParams) {
  return request({
    url: '/personalization/activity-device/device-page',
    method: 'get',
    params: queryParams
  })
}

// 获取耗材列表
export function init() {
  return request({
    url: '/personalization/activity-device/init',
    method: 'get',
  })
}

// 绑定用法/诊疗
export function bind(data) {
  return request({
    url: '/personalization/activity-device/activity-device',
    method: 'post',
    data: data
  })
}

// 删除绑定
export function deleteBind(bindId) {
  return request({
    url: '/personalization/activity-device/activity-device?bindId=' + bindId,
    method: 'delete',
  })
}


// 查询服务管理列表
export function getRegistrationfeeList(query) {
  return request({
    url: '/basic-service/healthcare/healthcare-service-page',
    method: 'get',
    params: query
  })
}
