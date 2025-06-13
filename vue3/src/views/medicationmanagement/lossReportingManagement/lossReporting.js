import request from '@/utils/request'
// 查询管理列表
export function getTransferProductList(query) {
  return request({
    url:'/inventory-manage/loss/loss-report-form-page', 
    method: 'get',
    params: query
  })
}

// 详情
export function getTransferProductDetail(busNo) {
  return request({
    url: '/inventory-manage/loss/loss-receipt',
    method: 'get',
    params: { busNo } // 确保参数正确传递
  })
}

// 添加/编辑单据
export function addTransferProduct(data) {
  return request({
    url: '/inventory-manage/loss/loss-receipt-edit',
    method: 'put',
    data: data
  })
}


// 查询单据初始化数据
export function getInit() {
  return request({
    url: '/inventory-manage/loss/init',
    method: 'get'
  })
} 
export function getBusNoInit() {  //单据号
  return request({
    url: '/inventory-manage/loss/bus-no-init',
    method: 'get'
  })
}


// 删除单据
export function delTransferProduct(param) {
  return request({
    url: '/inventory-manage/loss/loss-receipt-del?supplyRequestIds=' + param,
    method: 'delete',
  })
}

// 提交审批
export function submitApproval(busNo) {
  return request({
    url: '/inventory-manage/loss/submit-approval',
    method: 'put',
    data: busNo
  })
}

// 撤回审批
export function withdrawApproval(busNo) {
  return request({
    url: '/inventory-manage/loss/withdraw-approval',
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
    url:'/app-common/inventory-item-info',
    // url: '/inventory-manage/purchase/inventory-item-info',
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
// 药房药库列表
export function getPharmacyCabinetList() {
  return request({
    url: '/app-common/pharmacy-cabinet-list',
    method: 'get',
  })
}


// 获取药库列表
export function getDispensaryList() {
  return request({
    url: '/app-common/inventory-cabinet-list',
    // '/app-common/cabinet-list',
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
// 报损审批通过
export function lossReportApproved(busNo) {
  return request({
    url: '/inventory-manage/receipt/loss-report-approved?busNo=' + busNo,
    method: 'put',
  })
}