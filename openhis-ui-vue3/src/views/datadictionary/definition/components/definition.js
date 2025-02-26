import request from '@/utils/request'

// 查询费用定价信息列表
export function listDefinition(query) {
  return request({
    url: '/dict-manager/definition/item-definition-page',
    method: 'get',
    params: query
  })
}

// 初始化下拉选
export function initOption(query) {
  return request({
    url: '/dict-manager/definition/init',
    method: 'get',
    params: query
  })
}

// 修改费用定价信息
export function updateDefinition(data) {
  return request({
    url: '/dict-manager/definition/item-definition',
    method: 'put',
    data: data
  })
}

// 修改费用定价信息
export function getOptions() {
  return request({
    url: '/dict-manager/definition/status-enum-option',
    method: 'get',
  })
}