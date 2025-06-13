import request from '@/utils/request'

/**
 * 获取患者列表
 */
export function getList(queryParams) {
  return request({
    url: '/charge-manage/pricing/patient-info',
    method: 'get',
    params: queryParams
  })
}

// 处方相关接口
/**
 * 获取药品列表
 */
export function getAdviceBaseInfo(queryParams) {
  return request({
    url: '/charge-manage/pricing/advice-base-info',
    method: 'get',
    params: queryParams
  })
}
/**
 * 保存处方(单条)
 */
export function savePrescription(data) {
  return request({
    url: '/doctor-station/advice/save-advice',
    method: 'post',
    data: data
  })
}
/**
 * 签发处方
 */
export function savePrescriptionSign(data) {
  return request({
    url: '/doctor-station/advice/sign-advice',
    method: 'post',
    data: data
  })
}
/**
 * 处方签退
 */
export function singOut(data) {
  return request({
    url: '/doctor-station/advice/sign-off',
    method: 'post',
    data: data
  })
}
/**
 * 获取患者本次就诊处方
 */
export function getPrescriptionList(encounterId) {
  return request({
    url: '/doctor-station/advice/request-base-info?encounterId=' + encounterId,
    method: 'get',
  })
}
/**
 * 获取科室列表
 */
export function getOrgTree() {
  return request({
    url: '/base-data-manage/organization/organization',
    method: 'get',
  })
}
/**
 * 获取退费账单
 */
export function getEncounterPatientPayment(encounterId) {
  return request({
    url: '/charge-manage/refund/patient-payment?encounterId=' + encounterId,
    method: 'get',
  })
}
/**
 * 申请退费
 */
export function refundPayment(data) {
  return request({
    url: '/charge-manage/refund/refund-payment',
    method: 'post',
    data: data
  })
}


// 电子处方相关接口
/**
 * 电子处方查询
 */
export function getVeriPrescriptionInfo(queryParams) {
  return request({
    url: '/ybelep-request/get-PrescriptionInfo',
    method: 'get',
    params: queryParams
  })
}

// 处方开立相关接口
/**
 * 获取全部药品信息
 */
export function getAllMedicationInfo(queryParams) {
  return request({
    url: '/doctor-station/elep/get-allMedicationInfo',
    method: 'get',
    params: queryParams
  })
}

/**
 * 电子处方下拉框
 */
export function elepPrescriptionInit() {
  return request({
    url: '/doctor-station/elep/init',
    method: 'get'
  })
}

/**
 * 获取处方信息
 */
export function getPrescriptionInfo(queryParams) {
  return request({
    url: '/doctor-station/elep/get-prescriptionInfo',
    method: 'get',
    params: queryParams
  })
}

/**
 * 获取药品信息
 */
export function getMedicationInfo(queryParams) {
  return request({
    url: '/doctor-station/elep/get-medicationInfo',
    method: 'get',
    params: queryParams
  })
}

/**
 * 获取单据号
 */
export function prescriptionNoInit() {
  return request({
    url: '/doctor-station/elep/prescriptionNoInit',
    method: 'get'
  })
}

/**
 * 新增处方
 */
export function savePrescriptionInfo(data) {
  return request({
    url: '/doctor-station/elep/save-prescriptionInfo',
    method: 'post',
    data: data
  })
}

/**
 * 修改处方
 */
export function updatePrescriptionInfo(data) {
  return request({
    url: '/doctor-station/elep/update-prescriptionInfo',
    method: 'post',
    data: data
  })
}

/**
 * 删除处方
 */
export function deletePrescriptionInfo(data) {
  return request({
    url: '/doctor-station/elep/delete-prescriptionInfo',
    method: 'post',
    data: data
  })
}

/**
 * 签发处方
 */
export function issuancePrescription(prescriptionNoList) {
  return request({
    url: '/doctor-station/elep/issuance-prescription',
    method: 'post',
    data: prescriptionNoList
  })
}