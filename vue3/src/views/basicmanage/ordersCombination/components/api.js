import request from '@/utils/request'

export function getOrderGroup(queryParams) {
  return request({
    url: '/personalization/order-group/order-group',
    method: 'get',
    param: queryParams
  })
}

/**
 * 保存
 */
export function saveOrderGroup(data) {
  return request({
    url: '/personalization/order-group/order-group',
    method: 'post',
    data: data
  })
}

export function getAdviceBaseInfo(queryParams) {
  return request({
    url: '/doctor-station/advice/advice-base-info',
    method: 'get',
    params: queryParams
  })
}

/**
 * 获取科室列表
 */
export function getOrgTree() {
  return request({
    url: '/base-data-manage/organization/organization',
    method: 'get',
  })
}
/**
 * 添加组套获取科室列表
 */
export function getDepartmentList() {
  return request({
    url: '/app-common/department-list',
    method: 'get',
    
  })
}
/**
 * 添加组套获取使用人列表
 */
export function getUserPractitionerPCage(params) {
  return request({
    url: '/base-data-manage/practitioner/user-practitioner-page',
    method: 'get',
    params:params
  })
}
