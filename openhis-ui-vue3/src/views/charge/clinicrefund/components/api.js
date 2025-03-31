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
      url: '/charge-manage/refund/patient-payment?encounterId=' + encounterId,
      method: 'get',
    })
}

/**
 * 退费
 */
export function refund(data) {
    return request({
      url: '/charge-manage/refund/refund-payment',
      method: 'post',
      data: data
    })
}
