import request from '@/utils/request'

/**
 * 收费患者列表
 */
export function getList(queryParams) {
    return request({
      url: '/charge-manage/charge/encounter-patient-page',
      method: 'get',
      params: queryParams
    })
}
  
/**
 * 患者处方列表
 */
export function getChargeList(encounterId) {
    return request({
      url: '/charge-manage/charge/patient-prescription?encounterId=' + encounterId,
      method: 'get',
    })
}
  
  
/**
 * 医保转自费
 */
export function changeToSelfPay(encounterId) {
    return request({
      url: '/charge-manage/charge/self-pay?encounterId=' + encounterId,
      method: 'put',
    })
}
  
  
/**
 * 自费转医保
 */
export function changeToMedicalInsurance(encounterId) {
    return request({
      url: '/charge-manage/charge/medical-insurance?encounterId=' + encounterId,
      method: 'put',
    })
}

/**
 * 收费
 */
export function savePayment(data) {
    return request({
      url: '/payment/payment/charge',
      method: 'post',
      data: data
    })
}

/**
 * 初始化
 */
export function init() {
    return request({
      url: '/charge-manage/charge/init',
      method: 'get',
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
 * 取消预结算
 */
export function unprecharge(data) {
    return request({
      url: '/payment/payment/unprecharge',
      method: 'post',
      params: data
    })
}
  