import request from '@/utils/request'

// 获取支付记录列表
export function getList(query) {
  return request({
    url: '/payment/payment/page',
    method: 'get',
    params: query
  })
}
export function invoiceMzInvoice(data) {
  return request({
    url: '/invoice/mzInvoice',
    method: 'post',
    data: data
  })
}
export function invoiceReissue(data) {
  return request({
    url: '/invoice/invoiceReissue',
    method: 'post',
    data: data
  })
}
export function invoiceWriteoff(data) {
  return request({
    url: '/invoice/invoiceWriteoff',
    method: 'post',
    data: data
  })
}
export function invoiceOpen(invoiceId) {
  return request({
    url: '/invoice/invoiceOpen?invoiceId=' + invoiceId,
    method: 'put',
  })
}


