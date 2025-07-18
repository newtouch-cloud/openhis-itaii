import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/openhis";

// 查询初期所需数据
export function getInit() {
  return request({
    url: '/charge-manage/register/init',
    method: 'get'
  })
}

// 查询患者信息
export function getOutpatientRegistrationList(query) {
  return request({
    url: '/charge-manage/register/patient-metadata',
    method: 'get',
    params: query
  })
}

// 查询费用性质
export function getContractList() {
  return request({
    url: '/charge-manage/register/contract-list',
    method: 'get'
  })
}

// 查询诊断信息
export function getConditionDefinitionMetadata(query) {
  return request({
    url: '/charge-manage/register/condition-definition-metadata',
    method: 'get',
    params: query
  })
}

// 查询就诊位置
export function getLocationTree(query) {
  return request({
    url: '/charge-manage/register/location-tree',
    method: 'get',
    params: query
  })
}


// 根据位置id筛选医生
export function getPractitionerMetadata(query) {
  return request({
    url: '/charge-manage/register/practitioner-metadata',
    method: 'get',
    params: query
  })
}

// 根据机构id筛选服务项目
export function getHealthcareMetadata(query) {
  return request({
    url: '/charge-manage/register/healthcare-metadata',
    method: 'get',
    params: query
  })
}

// 门诊挂号查询
export function getOutpatientRegistrationCurrent(query) {
  return request({
    url: '/charge-manage/register/current-day-encounter',
    method: 'get',
    params: query
  })
}

// 新增门诊挂号信息
export function addOutpatientRegistration(data) {
  return request({
    url: '/charge-manage/register/save',
    method: 'post',
    data: data
  })
}

// 新增病人信息
export function addPatient(data) {
  return request({
    url: '/patientmanage/information/patient-information',
    method: 'post',
    data: data
  })
}


export function listmaritalstatus() {
  return request({
    url: '/patientmanage/information/list-maritalstatus',
    method: 'get',
  })
}

export function listoccupationtype() {
  return request({
    url: '/patientmanage/information/list-occupationtype',
    method: 'get',
  })
}

export function lisadministrativegender() {
  return request({
    url: '/patientmanage/information/list-administrativegender',
    method: 'get',
  })
}

export function listbloodtypeabo() {
  return request({
    url: '/patientmanage/information/list-bloodtypeabo',
    method: 'get',
  })
}


export function listbloodtypearh() {
  return request({
    url: '/patientmanage/information/list-bloodtypearh',
    method: 'get',
  })
}

export function listfamilyrelationshiptype() {
    return request({
      url: '/patientmanage/information/list-familyrelationshiptype',
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
