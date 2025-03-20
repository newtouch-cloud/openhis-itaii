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

// 查询服务管理详细
export function getPurchaseinventoryOne(id) {
  return request({
    url: '/basic-service/healthcare/healthcare-service-detail/' + parseStrEmpty(id),
    method: 'get'
  })
}

// 添加/编辑入库单据
export function addPurchaseinventory(data) {
  return request({
    url: '/inventory-manage/purchase/inventory-receipt',
    method: 'post',
    data: data
  })
}


// 查询厂商类型
export function getInit() {
  return request({
    url: '/inventory-manage/purchase/init',
    method: 'get'
  })
}

// 查询部门树形数据
export function deptTreeSelect(queryParams) {
  return request({
    url: '/base-data-manage/organization/organization',
    method: 'get',
    param: queryParams
  })
}

// 查询地点树形数据
export function locationTreeSelect(queryParams) {
  return request({
    url: '/base-data-manage/cabinet-location/cabinet-location',
    method: 'get',
    param: queryParams
  })
}

// 删除收费挂号项目
export function delPurchaseinventory(param) {
  console.log(param,'aaaa')
  return request({
    url: '/basic-service/healthcare/healthcare-service',
    method: 'delete',
    params: param
  })
}