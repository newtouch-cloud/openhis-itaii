import request from '@/utils/request'
// 查询调拨管理列表
export function getTransferProductList(query) {
  return request({
    url:'/inventory-manage/transfer/product-transfer-page', 
    method: 'get',
    params: query
  })
}

// 调拨详情
export function getTransferProductDetail(busNo) {
  return request({
    url: '/inventory-manage/transfer/product-transfer-detail',
    method: 'get',
    params: busNo // 确保参数正确传递
  })
}

// 添加/编辑调拨单据
export function addTransferProduct(data) {
  return request({
    url: '/inventory-manage/transfer/product-transfer',
    // product-transfer-edit',
    method: 'put',
    data: data
  })
}

// 添加/编辑批量调拨单据
export function addTransferProductBatch(data) {
  return request({
    url: '/inventory-manage/transfer/product-transfer-batch',
    // product-transfer-edit',
    method: 'put',
    data: data
  })
}

// 批量调拨详情
export function getTransferProductDetails(params) {
  return request({
    url: '/inventory-manage/transfer/product-transfer-batch',
    method: 'get',
    params: params // 确保参数正确传递
  })
}


// 查询单据初始化数据
export function getInit() {
  return request({
    url: '/inventory-manage/transfer/init',
    method: 'get'
  })
} 
export function getBusNoInit() {  //单据号
  return request({
    url: '/inventory-manage/transfer/bus-no-init',
    method: 'get'
  })
}


// 删除单据
export function delTransferProduct(param) {
  return request({
    url: '/inventory-manage/transfer/product-transfer-del?supplyRequestIds=' + param,
    method: 'delete',
  })
}

// 提交审批
export function submitApproval(busNo) {
  return request({
    url: '/inventory-manage/transfer/submit-approval',
    // product-transfer-approved
    method: 'put',
    data: busNo
  })
}

// 撤回审批
export function withdrawApproval(busNo) {
  return request({
    url: '/inventory-manage/transfer/withdraw-approval',
    method: 'put',
    data: busNo
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
    url: '/app-common/inventory-pharmacy-list',
    // '/app-common/pharmacy-list',
    method: 'get',
  })
}

// 获取药库列表
export function getDispensaryList() {
  return request({
    url:'/app-common/inventory-cabinet-list',
    //  '/app-common/cabinet-list',
    method: 'get',
  })
}
// 获取仓库药房列表
export function getpharmacyCabinetList() {
  return request({
    url: '/app-common/pharmacy-cabinet-list',
    method: 'get',
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
 * 商品调拨审批通过
 */
export function productTransferApproved(busNo) {
  return request({
    url: '/inventory-manage/receipt/product-transfer-approved?busNo=' + busNo,
    method: 'put',
  })
}