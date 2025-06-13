import request from '@/utils/request'

// 获取预交金信息初期数据列表
export function getDepositInfoPage(query) {
  return request({
    url: '/deposit-manage/deposit-page',
    method: 'get',
    params: query
  })
}

// 获取预交金信息初期数据列表
export function getDepositInfoInit() {
  return request({
    url: '/deposit-manage/init',
    method: 'get'
  })
}

// 获取患者信息 分页显示
export function getPatientInfoPage(query) {
  return request({
    url: '/deposit-manage/deposit-patient-page',
    method: 'get',
    params: query
  })
}


/**
 * 收费
 */
export function savePayment(data) {
    return request({
      url: '/deposit-manage/charge',
      method: 'post',
      data: data
    })
}