import request from '@/utils/request'

/**
 * 收费患者列表
 */
export function getList(queryParams) {
    return request({
      url: '/charge-manage/refund/encounter-patient-page',
      method: 'get',
      params: queryParams
    })
}

/**
 * 患者退费账单
 */
export function getRefundList(encounterId) {
    return request({
      url: '/charge-manage/refund/patient-refund?encounterId=' + encounterId,
      method: 'get',
    })
}

/**
 * 未退费账单列表
 */
export function getChargeItemIds(encounterId) {
    return request({
      url: '/charge-manage/refund/regenerate_charge?encounterId=' + encounterId,
      method: 'get',
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
 * 初始化
 */
export function init() {
    return request({
      url: '/charge-manage/refund/init',
      method: 'get',
    })
}

/**
 * 校验退药
 */
export function validReturnDrug(params) {
    return request({
      url: '/charge-manage/refund/verify_refund?chargeItemIdList=' + params,
      method: 'get',
    })
}
