import request from '@/utils/request'

// 查询药品目录列表
export function getMedicationList(query) {
  return request({
    url: '/data-dictionary/medication/information-page',
    method: 'get',
    params: query
  })
}

// 查询药品目录详细
export function getMedicationOne(id) {
  return request({
    url: '/data-dictionary/medication/information-one',
    method: 'get',
    params: { id } // 确保参数正确传递
  })
}

// 新增药品目录
export function addMedication(data) {
  return request({
    url: '/data-dictionary/medication/information',
    method: 'post',
    data: data
  })
}

// 修改药品目录
export function editMedication(data) {
  return request({
    url: '/data-dictionary/medication/information',
    method: 'put',
    data: data
  })
}

// 删除药品目录
export function delUser(userId) {
  return request({
    url: '/system/user/' + userId,
    method: 'delete'
  })
}

// 药品目录分类查询
export function getMedicationCategory() {
  return request({
    url: '/data-dictionary/medication/information-init',
    method: 'get'
  })
}


// 停用药品目录
export function stopMedication(ids) {
  console.log(ids)
  return request({
    url: '/data-dictionary/medication/information-stop',
    method: 'put',
    data: ids
  })
}

// 启用药品目录
export function startMedication(ids) {
  console.log(ids)
  return request({
    url: '/data-dictionary/medication/information-start',
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
    url: '/base-data-manage/location/location-page-tree',
    method: 'get',
    param: queryParams
  })
}

// 获取医保药品目录
export function getYbMedicationList(queryParams) {
  return request({
    url: '/catalog/page',
    method: 'get',
    params: queryParams
  })
}
