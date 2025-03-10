import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/openhis";

// 查询初期所需数据
export function getInit() {
  return request({
    url: '/outpatient-service/register/init',
    method: 'get'
  })
}

// 查询患者信息
export function getOutpatientRegistrationList(query) {
  return request({
    url: '/outpatient-service/register/patient-metadata',
    method: 'get',
    params: query
  })
}

// 查询费用性质
export function getContractList() {
  return request({
    url: '/outpatient-service/register/contract-list',
    method: 'get'
  })
}

// 查询诊断信息
export function getConditionDefinitionMetadata(query) {
  return request({
    url: '/outpatient-service/register/condition-definition-metadata',
    method: 'get',
    params: query
  })
}

// 查询就诊位置
export function getLocationTree(query) {
  return request({
    url: '/outpatient-service/register/location-tree',
    method: 'get',
    params: query
  })
}


// 根据位置id筛选医生
export function getPractitionerMetadata(query) {
  return request({
    url: '/outpatient-service/register/practitioner-metadata',
    method: 'get',
    params: query
  })
}

// 根据机构id筛选服务项目
export function getHealthcareMetadata(query) {
  return request({
    url: '/outpatient-service/register/healthcare-metadata',
    method: 'get',
    params: query
  })
}

// // 门诊挂号查询
// export function getOutpatientRegistrationCategory() {
//   return request({
//     url: '/outpatient-service/registerinformation-init',
//     method: 'get'
//   })
// }

// 新增门诊挂号信息
export function addOutpatientRegistration(data) {
  return request({
    url: '/outpatient-service/register/save',
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