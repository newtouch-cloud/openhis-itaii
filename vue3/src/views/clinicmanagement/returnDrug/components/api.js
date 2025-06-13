import request from '@/utils/request'

/**
 * 获取患者列表
 */
export function getList(queryParams) {
  return request({
    url: '/pharmacy-manage/return-medicine/return-patient-page',
    method: 'get',
    params: queryParams
  })
}

/**
 * 获取退药列表
 */
export function getReturnDrugList(params) {
  return request({
    url: '/pharmacy-manage/return-medicine/medicine-return-list',
    method: 'get',
    params: params
  })
}

/**
 * 退药
 */
export function returnDrug(data) {
  return request({
    url: '/pharmacy-manage/return-medicine/medicine-return',
    method: 'put',
    data: data
  })
}

/**
 * 初始化
 */
export function init() {
  return request({
    url: '/pharmacy-manage/return-medicine/init',
    method: 'get',
  })
}
//扫码枪返回追溯码筛选
export function itemTraceNo(params) {
  return request({
    url: '/app-common/item-trace-no?traceNoList=' + params,
    method: 'get',
  })
}
