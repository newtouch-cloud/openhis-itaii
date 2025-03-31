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
  