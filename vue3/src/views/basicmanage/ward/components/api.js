import request from '@/utils/request'

/**
 * 收费患者列表
 */
export function getList(queryParams) {
    return request({
      url: '/base-data-manage/location/location-page',
      method: 'get',
      params: queryParams
    })
}
  
/**
 * 获取科室下拉列表
 */
export function getOrgList() {
  return request({
    url: '/base-data-manage/organization/organization',
    method: 'get',
  })
}

/**
 * 初始化
 */
export function init() {
    return request({
      url: '/base-data-manage/location/init',
      method: 'get',
    })
}
  
/**
 * 新增病区/床位/病房
 */
export function addLocation(data) {
    return request({
      url: '/base-data-manage/location/location',
      method: 'post',
      data: data
    })
}
  
  
/**
 * 编辑病区/床位/病房
 */
export function editLocation(data) {
    return request({
      url: '/base-data-manage/location/location',
      method: 'put',
      data: data
    })
}
  
/**
 * 新增病区/床位/病房
 */
export function deleteLocation(busNo) {
    return request({
      url: '/base-data-manage/location/location?busNo=' + busNo,
      method: 'delete',
    })
}
  