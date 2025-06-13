import request from '@/utils/request'

// 初始化
export function getImplementDepartmentList(query) {
  return request({
    url: '/base-data-manage/organization/organization',
    method: 'get',
    params: query
  })
}
// 查询诊疗目录列表
export function getDiagnosisTreatmentList(query) {
  return request({
    url: '/base-data-manage/org-loc/org-loc',
    method: 'get',
    params: query
  })
}
//查询诊疗目录详细
export function getImplementDepartmentOne(query) {
  return request({
    url: '/data-dictionary/diagnosis-treatment/information-page',
    method: 'get',
    params: query // 确保参数正确传递
  })
}

// 新增
export function addImplementDepartment(data) {
  return request({
    url: '/base-data-manage/org-loc/org-loc',
    method: 'post',
    data: data
  })
}

// 修改
export function editImplementDepartment(data) {
  return request({
    url: '/base-data-manage/org-loc/org-loc',
    method: 'post',
    data: data
  })
}

// 删除
export function deleteImplementDepartment(data) {
  return request({
    url: '/base-data-manage/org-loc/org-loc?orgLocId=' + data.orgLocId,
    method: 'delete',
  })
}

// 目录分类查询
export function getDiseaseTreatmentInit() {
  return request({
    url: '/data-dictionary/diagnosis-treatment/init',
    method: 'get'
  })
}

// 目录分类子查询
export function getDiseaseTreatmentInitLoc(id) {
  return request({
    url: '/data-dictionary/diagnosis-treatment/information-one?id=' + id,
    method: 'get'
  })
}