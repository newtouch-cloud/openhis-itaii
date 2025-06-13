import request from '@/utils/request'
// 查询管理列表
export function getTransferProductList(query) {
  return request({
    url:'/issue-manage/requisition/requisition-issue-page', 
    method: 'get',
    params: query
  })
}

// 详情
export function getTransferProductDetail(busNo) {
  return request({
    url: '/issue-manage/requisition/requisition-issue-detail',
    method: 'get',
    params: { busNo } // 确保参数正确传递
  })
}

// 添加/编辑单据
export function addTransferProduct(data) {
  return request({
    url: '/issue-manage/requisition/requisition-issue-edit',
    method: 'put',
    data: data
  })
}


// 查询单据初始化数据
export function getInit() {
  return request({
    url: '/issue-manage/requisition/init',
    method: 'get'
  })
} 
export function getBusNoInit() {  //单据号
  return request({
    url: '/issue-manage/requisition/bus-no-init',
    method: 'get'
  })
}


// 删除单据
export function delTransferProduct(param) {
  return request({
    url: '/issue-manage/requisition/requisition-issue-del?supplyRequestIds=' + param,
    // '/inventory-manage/purchase/inventory-receipt?supplyRequestIds=' + param,
    method: 'delete',
  })
}

// 提交审批
export function submitApproval(busNo) {
  return request({
    url: '/issue-manage/requisition/submit-approval',
    //  '/inventory-manage/purchase/submit-approval',
    method: 'put',
    data: busNo
  })
}

// 撤回审批
export function withdrawApproval(busNo) {
  return request({
    url: '/issue-manage/requisition/withdraw-approval',
    // '/inventory-manage/purchase/withdraw-approval',
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
    url: '/app-common/pharmacy-list',
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
    url: '/app-common/cabinet-list',
    method: 'get',
  })
}

// 查询管理列表
export function getTHTransferProductlist(query) {
  return request({
    url:'/issue-manage/return/return-issue-page', 
    method: 'get',
    params: query
  })
}

// 详情
export function getTHTransferProductDetail(busNo) {
  return request({
    url: '/issue-manage/return/return-issue-detail',
    method: 'get',
    params: { busNo } // 确保参数正确传递
  })
}

// 添加/编辑单据
export function addTHTransferProduct(data) {
  return request({
    url: '/issue-manage/return/return-issue-edit',
    method: 'put',
    data: data
  })
}


// 查询单据初始化数据
export function getTHInit() {
  return request({
    url: '/issue-manage/return/init',
    method: 'get'
  })
} 
export function getTHBusNoInit() {  //单据号
  return request({
    url: '/issue-manage/return/bus-no-init',
    method: 'get'
  })
}


// 删除单据
export function delTHTransferProduct(param) {
  return request({
    url: '/issue-manage/return/return-issue-del?supplyRequestId=' + param,
    // '/inventory-manage/purchase/inventory-receipt?supplyRequestIds=' + param,
    method: 'delete',
  })
}

// 提交审批
export function submitTHApproval(busNo) {
  return request({
    url: '/issue-manage/return/submit-approval',
    //  '/inventory-manage/purchase/submit-approval',
    method: 'put',
    data: busNo
  })
}

// 撤回审批
export function withdrawTHApproval(busNo) {
  return request({
    url: '/issue-manage/return/withdraw-approval',
    // '/inventory-manage/purchase/withdraw-approval',
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