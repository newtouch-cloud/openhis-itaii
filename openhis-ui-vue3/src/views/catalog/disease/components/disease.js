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
    url: '/datadictionary/disease/information-one',
    method: 'get',
    params: id
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

// 删除病种目录
export function delUser(userId) {
  return request({
    url: '/system/user/' + userId,
    method: 'delete'
  })
}

// 用户状态修改
export function changeUserStatus(userId, status) {
  const data = {
    userId,
    status
  }
  return request({
    url: '/system/user/changeStatus',
    method: 'put',
    data: data
  })
}

// 查询用户个人信息
export function getUserProfile() {
  return request({
    url: '/system/user/profile',
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
