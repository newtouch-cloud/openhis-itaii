import request from '@/utils/request'

// 获取住院信息初期数据列表
export function getInit(query) {
  return request({
    url: '/inpatient-manage/init',
    method: 'get',
    params: query
  })
}

// 获取住院信息 分页显示
export function getAdmissionPage(query) {
  return request({
    url: '/inpatient-manage/admission-page',
    method: 'get',
    params: query
  })
}

// 住院无档登记
export function addAdmissionInfo(data) {
  return request({
    url: '/inpatient-manage/admission-information',
    method: 'post',
    data: data
  })
}

// 住院登记
export function admissionInfo(data) {
  return request({
    url: '/inpatient-manage/admission-information',
    method: 'put',
    data: data
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
 * 查询病区下拉列表
 */
export function wardList() {
  return request({
    url: '/app-common/ward-list',
    method: 'get'
  })
}

/**
 * 获取诊断基础下拉数据
 */
export function diagnosisInit() {
  return request({
    url: '/doctor-station/diagnosis/init',
    method: 'get',
  })
}

// 查询患者相关
export function patientlLists() {
  return request({
    url: '/patient-manage/information/init',
    method: 'get'
  })
}

// 查询患者相关
export function doctorList(id) {
  return request({
    url: '/inpatient-manage/doctor-list?orgId=' + id,
    method: 'get'
  })
}

// 查询患者相关
export function getPatientInfo(id) {
  return request({
    url: '/inpatient-manage/admission-one?id=' + id,
    method: 'get'
  })
}

