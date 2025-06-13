import request from '@/utils/request'

/**
 * 获取患者列表
 */
export function getList(queryParams) {
  return request({
    url: '/outpatient-manage/disposal/encounter-list',
    method: 'get',
    params: queryParams
  })
}

/**
 * 诊疗列表
 */
export function getDisposalList(encounterId) {
  return request({
    url: '/outpatient-manage/disposal/disposal-list?encounterId=' + encounterId,
    method: 'get',
  })
}

/**
 * 执行列表
 */
export function getExecuteList(queryParams) {
  return request({
    url: '/outpatient-manage/disposal/execute-list',
    method: 'get',
    params: queryParams
  })
}

/**
 * 初始化
 */
export function init() {
  return request({
    url: '/outpatient-manage/disposal/init',
    method: 'get',
  })
}

/**
 * 执行
 */
export function execute(queryParams) {
  return request({
    url: '/outpatient-manage/disposal/execute?itemId=' + queryParams.itemId + '&type=' + queryParams.type,
    method: 'put',
  })
}

/**
 * 取消
 */
export function cancel(queryParams) {
  return request({
    url: '/outpatient-manage/disposal/cancel',
    method: 'put',
    params: queryParams
  })
}
