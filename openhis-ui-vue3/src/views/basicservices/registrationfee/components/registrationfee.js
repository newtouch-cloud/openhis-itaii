import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/openhis";

// 查询服务管理列表
export function getRegistrationfeeList(query) {
  return request({
    url: '/basic-service/healthcare/healthcare-service-page',
    method: 'get',
    params: query
  })
}

// 查询服务管理详细
export function getRegistrationfeeOne(id) {
  return request({
    url: '/basic-service/healthcare/healthcare-service-detail/' + parseStrEmpty(id),
    method: 'get'
  })
}

// 新增服务管理
export function addRegistrationfee(data) {
  return request({
    url: '/basic-service/healthcare/healthcare-service',
    method: 'post',
    data: data
  })
}

// 修改服务管理
export function editRegistrationfee(data) {
  return request({
    url: '/basic-service/healthcare/healthcare-service',
    method: 'put',
    data: data
  })
}

// 查询厂商类型
export function getInit() {
  return request({
    url: '/basic-service/healthcare/init',
    method: 'get'
  })
}

// 查询部门树形数据
export function deptTreeSelect(queryParams) {
  return request({
    url: '/basedatamanage/organization/organization',
    method: 'get',
    param: queryParams
  })
}

// 查询地点树形数据
export function locationTreeSelect(queryParams) {
  return request({
    url: '/basedatamanage/organization/organization',
    method: 'get',
    param: queryParams
  })
}

// 删除收费挂号项目
export function delRegistrationfee(param) {
  return request({
    url: '/basedatamanage/organization/healthcare-service',
    method: 'delete',
    params: param
  })
}