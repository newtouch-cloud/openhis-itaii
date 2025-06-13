import request from '@/utils/request'

/**
 * 获取单据待审批列表
 */
export function getReceiptList(queryParams) {
  return request({
    url: '/inventory-manage/receipt/receipt-page',
    method: 'get',
    params: queryParams
  })
}

/**
 * 初始化
 */
export function init() {
  return request({
    url: '/inventory-manage/receipt/init',
    method: 'get',
  })
}

/**
 * 审批通过
 */
export function purchaseInventoryApproved(busNo) {
  return request({
    url: '/inventory-manage/receipt/purchase-inventory-approved?busNo=' + busNo,
    method: 'put',
  })
}


/**
 * 领用出库审批通过
 */
export function requisitionIssueApproved(busNo) {
  return request({
    url: '/inventory-manage/receipt/requisition-issue-approved?busNo=' + busNo,
    method: 'put',
  })
}
// 领用退库审批通过
export function returnIssueApproved(busNo) {
  return request({
    url: '/inventory-manage/receipt/return-issue-approved?busNo=' + busNo,
    method: 'put',
  })
}
// 报损审批通过
export function lossReportApproved(busNo) {
  return request({
    url: '/inventory-manage/receipt/loss-report-approved?busNo=' + busNo,
    method: 'put',
  })
}

/**
 * 商品调拨审批通过
 */
export function productTransferApproved(busNo) {
  return request({
    url: '/inventory-manage/receipt/product-transfer-approved?busNo=' + busNo,
    method: 'put',
  })
}
/**
 * 盘点审批通过
 */
export function productStocktakingApproved(busNo) {
  return request({
    url: '/inventory-manage/receipt/product-stocktaking-approved?busNo=' + busNo,
    method: 'put',
  })
}

/**
 * 审批驳回
 */
export function reject(busNo) {
  return request({
    url: '/inventory-manage/receipt/reject?busNo=' + busNo,
    method: 'put',
  })
}
// 入库单据详情
export function getpurchaseInventoryDetail(busNo) {
  return request({
    url: '/inventory-manage/purchase/inventory-receipt',
    method: 'get',
    params: { busNo } // 确保参数正确传递
  })
}
// 查询已退库单详情
export function getpurchaseInventoryDetailReturn(busNo) {
  return request({
    url: '/inventory-manage/return/return-detail',
    method: 'get',
    params: { busNo }
  })
}