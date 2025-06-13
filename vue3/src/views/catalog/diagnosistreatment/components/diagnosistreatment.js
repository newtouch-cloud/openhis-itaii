import request from '@/utils/request'

// 查询诊疗目录列表
export function getDiagnosisTreatmentList(query) {
  return request({
    url: '/data-dictionary/diagnosis-treatment/information-page',
    method: 'get',
    params: query
  })
}

// 查询诊疗目录详细
export function getDiagnosisTreatmentOne(id) {
  return request({
    url: '/data-dictionary/diagnosis-treatment/information-one/',
    method: 'get',
    params: { id } // 确保参数正确传递
  })
}

// 新增诊疗目录
export function addDiagnosisTreatment(data) {
  return request({
    url: '/data-dictionary/diagnosis-treatment/information',
    method: 'post',
    data: data
  })
}

// 修改诊疗目录
export function editDiagnosisTreatment(data) {
  return request({
    url: '/data-dictionary/diagnosis-treatment/information',
    method: 'put',
    data: data
  })
}

// 诊疗目录分类查询
export function getDiseaseTreatmentInit() {
  return request({
    url: '/data-dictionary/diagnosis-treatment/init',
    method: 'get'
  })
}

// 停用诊疗目录
export function stopDiseaseTreatment(ids) {
  console.log(ids)
  return request({
    url: '/data-dictionary/diagnosis-treatment/information-stop',
    method: 'put',
    data: ids
  })
}

// 启用诊疗目录
export function startDiseaseTreatment(ids) {
  console.log(ids)
  return request({
    url: '/data-dictionary/diagnosis-treatment/information-start',
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

// 获取医用耗材目录
export function getYbDiagnosisTreatmentList(queryParams) {
  return request({
    url: '/catalog/page',
    method: 'get',
    params: queryParams
  })
}
