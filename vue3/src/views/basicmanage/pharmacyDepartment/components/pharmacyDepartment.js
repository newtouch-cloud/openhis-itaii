import request from '@/utils/request'

// 初始化
export function getPharmacyDepartmentList(query) {
  return request({
    url: '/base-data-manage/org-loc/org-loc',
    method: 'get',
    params: query
  })
}

// 查询诊疗目录详细
export function getPharmacyDepartmentOne(query) {
  return request({
    url: '/base-data-manage/org-loc/org-loc',
    method: 'get',
    params: query // 确保参数正确传递
  })
}

// 新增
export function addPharmacyDepartment(data) {
  return request({
    url: '/base-data-manage/org-loc/org-loc',
    method: 'post',
    data: data
  })
}

// 修改
export function editPharmacyDepartment(data) {
  return request({
    url: '/base-data-manage/org-loc/org-loc',
    method: 'post',
    data: data
  })
}

// 删除
export function deletePharmacyDepartment(data) {
  return request({
    url: '/base-data-manage/org-loc/org-loc?orgLocId=' + data.orgLocId,
    method: 'delete',
  })
}

// 目录分类查询
export function getDiseaseTreatmentInit(id) {
  return request({
    url: '/base-data-manage/org-loc/init/',
    method: 'get',
    params: { id } // 确保参数正确传递
  })
}

// 目录分类子查询
export function getDiseaseTreatmentInitLoc(id) {
  return request({
    url: '/base-data-manage/org-loc/loc-list?locationForm=' + id,
    method: 'get'
  })
}