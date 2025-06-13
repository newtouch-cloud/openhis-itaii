import request from '@/utils/request'
// 查询已退库单详情
export function getPurchaseinventoryList(busNo) {
  return request({
    url: '/inventory-manage/return/generated-return-detail',
    method: 'get',
    params: { busNo }
  })
}

// 根据订单号查询退货详情
export function getpurchaseInventoryDetail(busNo) {
  return request({
    url: '/inventory-manage/return/return-detail',
    method: 'get',
    params: { busNo } // 确保参数正确传递
  })
}

// 添加/编辑退货单据
export function addPurchaseinventory(data) {
  return request({
    url: '/inventory-manage/return/generate-return-receipt',
    method: 'put',
    data: data
  })
}


// 查询采购入库单据初始化数据
export function getInit() {
  return request({
    url: '/inventory-manage/return/init',
    method: 'get'
  })
}

// 查询busNo
export function getInitBusNo() {
  return request({
    url: '/inventory-manage/return/bus-no-init',
    method: 'get'
  })
}

// 删除单据
export function delPurchaseinventory(param) {
  return request({
    url: '/inventory-manage/return/delete-return-receipt?supplyRequestIds=' + param,
    method: 'delete',
  })
}

// 获取药品目录
export function getMedicineList(queryParams) {
  return request({
    url: '/app-common/inventory-item',
    method: 'get',
    params: queryParams
  })
}
// 获取药品目录
export function getCount(queryParams) {
  return request({
    url: '/app-common/inventory-item-info',
    method: 'get',
    params: queryParams
  })
}

// 获取药房列表
export function getPharmacyList() {
  return request({
    url: '/app-common/pharmacy-list',
    method: 'get',
  })
}

// 获取药库列表
export function getDispensaryList() {
  return request({
    url: '/app-common/cabinet-list',
    method: 'get',
  })
}
// 退库提交审批
export function submitApproval(busNo) {
  return request({
    url: '/inventory-manage/return/return-submit-approval',
    method: 'put',
    data: busNo
  })
}

// 退库撤回审批
export function withdrawApproval(busNo) {
  return request({
    url: '/inventory-manage/return/return-withdraw-approval',
    method: 'put',
    data: busNo
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
/**
 * 审批通过
 */
export function purchaseInventoryApproved(busNo) {
  return request({
    url: '/inventory-manage/receipt/purchase-return-approved?busNo=' + busNo,
    method: 'put',
  })
}