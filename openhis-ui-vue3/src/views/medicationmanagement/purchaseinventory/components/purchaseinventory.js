import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/openhis";

// 查询采购入库列表
export function getPurchaseinventoryList(query) {
  return request({
    url: '/inventory-manage/purchase/inventory-receipt-page',
    method: 'get',
    params: query
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

// 添加/编辑入库单据
export function addPurchaseinventory(data) {
  return request({
    url: '/inventory-manage/purchase/inventory-receipt',
    method: 'put',
    data: data
  })
}


// 查询采购入库单据初始化数据
export function getInit() {
  return request({
    url: '/inventory-manage/purchase/init',
    method: 'get'
  })
}


// 删除单据
export function delPurchaseinventory(param) {
  console.log(param,'aaaa')
  return request({
    url: '/inventory-manage/purchase/inventory-receipt',
    method: 'delete',
    params: param
  })
}

// 提交审批
export function submitApproval(busNo) {
  return request({
    url: '/inventory-manage/purchase/submit-approval',
    method: 'put',
    data: { busNo }
  })
}

// 撤回审批
export function withdrawApproval(busNo) {
  return request({
    url: '/inventory-manage/purchase/withdraw-approval',
    method: 'put',
    data: { busNo }
  })
}