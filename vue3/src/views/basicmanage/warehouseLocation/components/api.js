import request from '@/utils/request'

// 新增
export function addWarehouse(data) {
  return request({
    url: '/base-data-manage/location/location',
    method: 'post',
    data: data
  })
}

// 查询菜单列表
export function getList(query) {
    return request({
      url: '/base-data-manage/location/location-page',
      method: 'get',
      params: query
    })
  }
  export function init() {
    return request({
      url: '/base-data-manage/location/init',
      method: 'get'
    })
  }

  // 修改
export function updateWarehouse(data) {
  return request({
    url: '/base-data-manage/location/location',
    method: 'put',
    data: data
  })
}

  // 删除
  export function deleteWarehouse(data) {
    return request({
      url: '/base-data-manage/location/location?locationId=' + data.locationId,
      method: 'delete',
    })
  }


