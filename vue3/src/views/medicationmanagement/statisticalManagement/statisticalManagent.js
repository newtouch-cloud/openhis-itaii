import request from '@/utils/request'
// 查询管理列表
export function getproductReturnPage(query) {
  return request({
    url:'/inventory-manage/product/product-page', 
    method: 'get',
    params: query
  })
}

// 查询单据初始化数据
export function getInit() {
  return request({
    url: '/inventory-manage/product/product-init',
    method: 'get'
  })
} 

export function getBusNoInit() {  //单据号
  return request({
    url: '/inventory-manage/product/bus-no-init',
    method: 'get'
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

// 停供
export function stopSupply(busNo) {
  return request({
    url: '/inventory-manage/product/stop-supply',
    method: 'put',
    data: busNo
  })
}

// 取消停供
export function cancelSupply(busNo) {
  return request({
    url: '/inventory-manage/product/cancel-supply',
    method: 'put',
    data: busNo
  })
}


// 查询库存商品明细查询报表列表
export function getReportProductPage(query) {
  return request({
    url:'/report-manage/inventory-product/report-product-page', 
    method: 'get',
    params: query
  })
}
//查询采购入库明细报表
export function getReportInboundPCage(query) {
  return request({
    url:'/report-manage/inbound/report-inbound-page', 
    method: 'get',
    params: query
  })
}

//查询调拨入库明细报表
export function getReportTransferPage(query) {
  return request({
    url:'/report-manage/transfer/report-transfer-page', 
    method: 'get',
    params: query
  })
}

//查询库存盘点明细报表
export function getReportStocktakingPage(query) {
  return request({
    url:'/report-manage/stocktaking/report-stocktaking-page', 
    method: 'get',
    params: query
  })
}
//报损明细
export function getReportLossPage(query) {
  return request({
    url:'/report-manage/loss/report-loss-page', 
    method: 'get',
    params: query
  })
}
// 医保对账
export function ybRequestReconcile(data) {
  return request({
    url: '/yb-request/reconcile',
    method: 'post',
    data: data
  })
}
// 合同
export function getContractList() {
  return request({
    url: '/charge-manage/register/contract-list',
    method: 'get',
  })
}
// 对账列表 结算
export function getReconcileList(params) {
  return request({
    url: '/yb-request/reconcile-list',
    method: 'get',
    params:params
  })
}
//领用出库明细
export function getRreportOutboundPage(query) {
  return request({
    url:'/report-manage/outbound/report-outbound-page', 
    method: 'get',
    params: query
  })
}

// 获取科室列表
export function getDepartmentList() {
  return request({
    url: '/app-common/department-list',
    method: 'get',
  })
}

// 查询领用出库明细初始化数据   供应商
export function getOutboundInit() {
  return request({
    url: '/report-manage/outbound/init',
    method: 'get'
  })
} 
// 查询采购入库明细初始化数据   供应商
export function getInboundInit() {
  return request({
    url: '/report-manage/inbound/init',
    method: 'get'
  })
} 
//领用退库明细
export function getRreportReturnIssue(query) {
  return request({
    url:'/report-manage/return-issue/report-return-issue', 
    method: 'get',
    params: query
  })
}
// 查询领用退库明细初始化数据   供应商
export function getReturnIssueInit() {
  return request({
    url: '/report-manage/return-issue/init',
    method: 'get'
  })
} 
//采购退库明细
export function getReportPurchaseReturn(query) {
  return request({
    url:'/report-manage/purchase-return/report-purchase-return', 
    method: 'get',
    params: query
  })
}
// 查询采购退库明细初始化数据  
export function getPurchaseReturnInit() {
  return request({
    url: '/report-manage/purchase-return/init',
    method: 'get'
  })
}
//待发药查询
export function getAwaitingPendingMedicationPageList(query) {
  return request({
    url:'/pharmacy-manage/pending-medication/pending-medication-page', 
    method: 'get',
    params: query
  })
}
//发药明细 切换统计类型
export function getAmbPractitionerDetail(query) {
  return request({
    url:'/pharmacy-manage/medication-details/amb-practitioner-detail', 
    method: 'get',
    params: query
  })
}
// 门诊发药明细流水账接口
export function getAmbMedicationDetail(query) {
  return request({
    url:'/pharmacy-manage/medication-details/amb-medication-detail', 
    method: 'get',
    params: query
  })
}
// 查询发药明细初始化数据  
export function getMedicationDetailsInit() {
  return request({
    url: '/pharmacy-manage/medication-details/init',
    method: 'get'
  })
}
// 医保对账明细
export function reconcileDetailList(data) {
  return request({
    url: '/yb-request/reconcile-detail-list',
    method: 'post',
    data:data
  })
}
// 医保对账明细导出txt
export function reconcileDetailTxt(data) {
  return request({
    url: '/yb-request/reconcile-detail-txt',
    method: 'post',
    data: data
  })
}

// 医保明细对总账
export function reconcileGeneral(data) {
  return request({
    url: '/yb-request/reconcile-general',
    method: 'post',
    data: data
  })
}
//查询门诊收费明细
export function getReportChargePage(params) {
  return request({
    url: '/report-manage/charge/report-charge-page',
    method: 'get',
    params:params
  })
}
//查询门诊收费明细初始值
export function getReportChargeInit() {
  return request({
    url: '/report-manage/charge/init',
    method: 'get'
  })
}
//查询挂号收费明细
export function getReportRegisterPage(params) {
  return request({
    url: '/report-manage/register/report-register-page',
    method: 'get',
    params:params
  })
}

//查询挂号收费明细初始值
export function getReportRegisterInit() {
  return request({
    url: '/report-manage/register/init',
    method: 'get'
  })
}