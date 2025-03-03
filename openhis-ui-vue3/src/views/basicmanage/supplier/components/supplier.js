import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/openhis";

// 查询病种目录列表
export function getSupplierList(query) {
  return request({
    url: '/data-dictionary/supplier/get-supplier-list',
    method: 'get',
    params: query
  })
}

// 查询病种目录详细
export function getSupplierOne(id) {
  return request({
    url: '/data-dictionary/supplier/get-supplier-detail/' + parseStrEmpty(id),
    method: 'get'
  })
}

// 新增病种目录
export function addSupplier(data) {
  return request({
    url: '/data-dictionary/supplier/add-supplier',
    method: 'post',
    data: data
  })
}

// 修改病种目录
export function editSupplier(data) {
  return request({
    url: '/data-dictionary/supplier/edit-supplier',
    method: 'put',
    data: data
  })
}

// // 删除病种目录
// export function delUser(userId) {
//   return request({
//     url: '/system/user/' + userId,
//     method: 'delete'
//   })
// }

// 停用病种目录
export function stopSupplier(ids) {
  return request({
    url: '/data-dictionary/supplier/information-stop',
    method: 'put',
    data: ids
  })
}

// 启用病种目录
export function startSupplier(ids) {
  return request({
    url: '/data-dictionary/supplier/information-start',
    method: 'put',
    data: ids
  })
}

// 查询厂商类型
export function getSupplierInit() {
  return request({
    url: '/data-dictionary/supplier/information-init',
    method: 'get'
  })
}
