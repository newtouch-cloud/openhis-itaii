import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/openhis";

// 查询病种目录列表
export function getDiseaseList(query) {
  return request({
    url: '/datadictionary/disease/information-page',
    method: 'get',
    params: query
  })
}

// 查询病种目录详细
export function getDiseaseOne(id) {
  return request({
    url: '/datadictionary/disease/information-one/' + parseStrEmpty(id),
    method: 'get'
  })
}

// 新增病种目录
export function addDisease(data) {
  return request({
    url: '/datadictionary/disease/information',
    method: 'post',
    data: data
  })
}

// 修改病种目录
export function editDisease(data) {
  return request({
    url: '/datadictionary/disease/information',
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

// 病种目录分类查询
export function getDiseaseCategory() {
  return request({
    url: '/datadictionary/disease/information-init',
    method: 'get'
  })
}

// 停用病种目录
export function stopDisease(ids) {
  return request({
    url: '/datadictionary/disease/information-stop',
    method: 'put',
    data: ids
  })
}

// 启用病种目录
export function startDisease(ids) {
  return request({
    url: '/datadictionary/disease/information-start',
    method: 'put',
    data: ids
  })
}