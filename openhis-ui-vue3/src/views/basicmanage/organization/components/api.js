import request from '@/utils/request'

export function getList(queryParams) {
  return request({
    url: '/basedatamanage/organization/organization',
    method: 'get',
    param: queryParams
  })
}

export function addOrganization(data) {
  return request({
    url: '/basedatamanage/organization/organization',
    method: 'post',
    data: data
  })
}

export function updateOrganization(data) {
  return request({
    url: '/basedatamanage/organization/organization',
    method: 'put',
    data: data
  })
}

export function deleteOrganization(param) {
  return request({
    url: '/basedatamanage/organization/organization',
    method: 'delete',
    params: param
  })
}

export function getOrgDetail(id) {
  return request({
    url: '/basedatamanage/organization/organization?orgId=' + id,
    method: 'get',
  })
}

export function initOrgTypeOption() {
  return request({
    url: '/basedatamanage/organization/organization',
    method: 'get',
  })
}

export function disableOrg(id) {
  return request({
    url: '/basedatamanage/organization/organization?orgId=' + id,
    method: 'put',
  })
}
