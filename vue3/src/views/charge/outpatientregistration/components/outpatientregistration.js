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

// // 查询就诊位置
// export function getLocationTree(query) {
//   return request({
//     url: '/charge-manage/register/location-tree',
//     method: 'get',
//     params: query
//   })
// }

// 查询就诊科室
export function getLocationTree(query) {
  return request({
    url: '/charge-manage/register/org-list',
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
    url: '/payment/payment/reg-pre-pay',
    method: 'post',
    data: data
  })
}

// 新增病人信息
export function addPatient(data) {
  return request({
    url: '/patient-manage/information/patient-information',
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

  // 患者退号
  export function returnRegister(encounterId) {
    return request({
      url: '/charge-manage/register/return?encounterId=' + encounterId,
      method: 'put'
    })
  }

  /**
   * 收费
   */
  export function savePayment(data) {
    return request({
      url: '/payment/payment/reg-pay',
      method: 'post',
      data: data
    })
  }

  
/**
 * 收费预结算
 */
export function precharge(data) {
  return request({
    url: '/payment/payment/precharge',
    method: 'post',
    data: data
  })
}

/**
 * 退费
 */
export function refund(data) {
  return request({
    url: '/payment/payment/uncharge',
    method: 'post',
    data: data
  })
}

/**
 * 取消挂号
 */
export function cancelRegister(data) {
  return request({
    url: '/charge-manage/register/return',
    method: 'put',
    data: data
  })
}

/**
 * 获取用户信息
 */
export function gerPreInfo(userMaessage) {
  return request({
    url: '/yb-request/per-info',
    method: 'post',
    params: userMaessage
  })
}