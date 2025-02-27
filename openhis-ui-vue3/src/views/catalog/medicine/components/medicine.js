import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/openhis";

// 查询药品目录列表
export function getMedicationList(query) {
  return request({
    url: '/datadictionary/medication/information-page',
    method: 'get',
    params: query
  })
}

// 查询药品目录详细
export function getMedicationOne(id) {
  return request({
    url: '/datadictionary/medication/information-one/' + parseStrEmpty(id),
    method: 'get'
  })
}

// 新增药品目录
export function addMedication(data) {
  return request({
    url: '/datadictionary/medication/information',
    method: 'post',
    data: data
  })
}

// 修改药品目录
export function editMedication(data) {
  return request({
    url: '/datadictionary/medication/information',
    method: 'put',
    data: data
  })
}

// 删除药品目录
export function delUser(userId) {
  return request({
    url: '/system/user/' + userId,
    method: 'delete'
  })
}

// 药品目录分类查询
export function getMedicationCategory() {
  return request({
    url: '/datadictionary/medication/information-init',
    method: 'get'
  })
}

// 修改用户个人信息
export function updateUserProfile(data) {
  return request({
    url: '/system/user/profile',
    method: 'put',
    data: data
  })
}

// 查询部门下拉树结构
export function deptTreeSelect() {
  return request({
    url: '/system/user/deptTree',
    method: 'get'
  })
}
