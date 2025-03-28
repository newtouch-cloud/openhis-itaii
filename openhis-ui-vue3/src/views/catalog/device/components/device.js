import request from '@/utils/request'

// 查询器材目录列表
export function getDeviceList(query) {
  console.log(query,'aaaaa')
  return request({
    url: '/data-dictionary/device/information-page',
    method: 'get',
    params: query
  })
}

// 查询器材目录详细
export function getDeviceOne(id) {
  return request({
    url: '/data-dictionary/device/information-one',
    method: 'get',
    params: { id } // 确保参数正确传递
  })
}

// 新增器材目录
export function addDevice(data) {
  return request({
    url: '/data-dictionary/device/information',
    method: 'post',
    data: data
  })
}

// 修改器材目录
export function editDevice(data) {
  return request({
    url: '/data-dictionary/device/information',
    method: 'put',
    data: data
  })
}

// // 删除器材目录
// export function delUser(userId) {
//   return request({
//     url: '/system/user/' + userId,
//     method: 'delete'
//   })
// }

// 器材目录分类查询
export function getDiseaseTreatmentInit() {
  return request({
    url: '/data-dictionary/device/init',
    method: 'get'
  })
}

// 停用病种目录
export function stopDevice(ids) {
  console.log(ids)
  return request({
    url: '/data-dictionary/device/information-stop',
    method: 'put',
    data: ids
  })
}

// 启用病种目录
export function startDevice(ids) {
  console.log(ids)
  return request({
    url: '/data-dictionary/device/information-start',
    method: 'put',
    data: ids
  })
}

// 查询部门树形数据
export function deptTreeSelect(queryParams) {
  return request({
    url: '/base-data-manage/organization/organization',
    method: 'get',
    param: queryParams
  })
}

// 查询地点树形数据
export function locationTreeSelect(queryParams) {
  return request({
    url: '/base-data-manage/cabinet-location/cabinet-location',
    method: 'get',
    param: queryParams
  })
}
