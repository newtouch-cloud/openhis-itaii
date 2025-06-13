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
export function getstocktakingDetail(busNo) {
  return request({
    url: '/inventory-manage/stocktaking/stocktaking-receipt',
    method: 'get',
    params: { busNo } // 确保参数正确传递
  })
}

// 添加/编辑入库单据
export function addPurchaseinventory(data) {
  return request({
    url: '/inventory-manage/purchase/inventory-receipt',
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


// 删除单据
export function delPurchaseinventory(param) {
  return request({
    url: '/inventory-manage/purchase/inventory-receipt?supplyRequestIds=' + param,
    method: 'delete',
  })
}

// 提交审批
export function submitApproval(busNo) {
  return request({
    url: '/inventory-manage/purchase/submit-approval',
    method: 'put',
    data: busNo
  })
}

// 撤回审批
export function withdrawApproval(busNo) {
  return request({
    url: '/inventory-manage/purchase/withdraw-approval',
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
    url: '/inventory-manage/purchase/inventory-item-info',
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