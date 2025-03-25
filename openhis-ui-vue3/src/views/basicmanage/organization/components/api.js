import request from '@/utils/request'

export function getList(queryParams) {
  return request({
    url: '/base-data-manage/organization/organization',
    method: 'get',
    param: queryParams
  })
}

export function addOrganization(data) {
  return request({
    url: '/base-data-manage/organization/organization',
    method: 'post',
    data: data
  })
}

export function updateOrganization(data) {
  return request({
    url: '/base-data-manage/organization/organization',
    method: 'put',
    data: data
  })
}

export function deleteOrganization(param) {
  return request({
    url: '/base-data-manage/organization/organization',
    method: 'delete',
    params: param
  })
}

export function getOrgDetail(id) {
  return request({
    url: '/base-data-manage/organization/organization?orgId=' + id,
    method: 'get',
  })
}

export function initOrgTypeOption() {
  return request({
    url: '/base-data-manage/organization/init',
    method: 'get',
  })
}

export function disableOrg(id) {
  return request({
    url: '/base-data-manage/organization/organization-inactive?orgId=' + id,
    method: 'put',
  })
}

export function enableOrg(id) {
  return request({
    url: '/base-data-manage/organization/organization-active?orgId=' + id,
    method: 'put',
  })
}
