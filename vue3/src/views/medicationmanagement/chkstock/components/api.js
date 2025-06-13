import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/openhis";

// 查询盘点列表
export function getStockinventoryList(query) {
  return request({
    url: '/inventory-manage/stocktaking/stocktaking-receipt-page',
    method: 'get',
    params: query
  })
}

// 盘点编辑页列表
export function getstocktakingDetail(params) {
  return request({
    url: '/inventory-manage/stocktaking/stocktaking-receipt',
    method: 'get',
    params: params // 确保参数正确传递
  })
}

// 添加/编辑入库单据
export function addProductStocktaking(data) {
  return request({
    url: '/inventory-manage/stocktaking/product-stocktaking',
    method: 'put',
    data: data
  })
}


// 查询盘点列表初始化查询区数据
export function getInit() {
  return request({
    url: '/inventory-manage/stocktaking/init',
    method: 'get'
  })
}

// 查询盘点详情初始化查询区数据
export function getDetailInit() {
  return request({
    url: '/inventory-manage/stocktaking/detail-init',
    method: 'get'
  })
}

// 生成批量盘点
export function getStocktakingReceiptBatch(params) {
  return request({
    url: '/inventory-manage/stocktaking/stocktaking-receipt-batch',
    method: 'get',
    params: params // 确保参数正确传递
  })
}             
//保存批量盘点
export function addBatch(data) {
  return request({
    url: '/inventory-manage/stocktaking/stocktaking-receipt-addBatch',
    method: 'put',
    data: data
  })
}

// 删除单据
export function delProductStocktaking(param) {
  return request({
    url: '/inventory-manage/stocktaking/product-stocktaking?supplyRequestIds=' + param,
    method: 'delete',
  })
}

// 提交审批
export function submitApproval(busNo) {
  return request({
    url: '/inventory-manage/stocktaking/submit-approval',
    method: 'put',
    data: busNo
  })
}

// 撤回审批
export function withdrawApproval(busNo) {
  return request({
    url: '/inventory-manage/stocktaking/withdraw-approval',
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
    //  '/app-common/pharmacy-list',
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
/**
 * 盘点审批通过
 */
export function productStocktakingApproved(busNo) {
  return request({
    url: '/inventory-manage/receipt/product-stocktaking-approved?busNo=' + busNo,
    method: 'put',
  })
}
