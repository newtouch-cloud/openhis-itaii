import request from '@/utils/request';

// 查询租户分页列表
export function getTenantPage(query) {
  return request({
    url: '/system/tenant/page',
    method: 'get',
    params: query,
  });
}

// 查询租户详情
export function getTenantDetail(tenantId) {
  return request({
    url: `/system/tenant/${tenantId}`,
    method: 'get',
  });
}

// 查询租户所属用户分页列表
export function getTenantUserPage(query) {
  return request({
    url: '/system/tenant/user/page',
    method: 'get',
    params: query,
  });
}

// 新增租户
export function addTenant(data) {
  return request({
    url: '/system/tenant',
    method: 'post',
    data: data,
  });
}

// 修改租户
export function editTenant(data) {
  return request({
    url: '/system/tenant',
    method: 'put',
    data: data,
  });
}

// 删除租户
export function delTenant(tenantIdList) {
  return request({
    url: '/system/tenant',
    method: 'delete',
    data: Array.isArray(tenantIdList) ? tenantIdList : [tenantIdList],
  });
}

// 启用租户
export function enableTenant(tenantIdList) {
  return request({
    url: '/system/tenant/enable',
    method: 'put',
    data: Array.isArray(tenantIdList) ? tenantIdList : [tenantIdList],
  });
}

// 停用租户
export function disableTenant(tenantIdList) {
  return request({
    url: '/system/tenant/disable',
    method: 'put',
    data: Array.isArray(tenantIdList) ? tenantIdList : [tenantIdList],
  });
}

// 查询租户未绑定的用户列表
export function getUnbindTenantUserList(query) {
  return request({
    url: `/system/tenant/${query.tenantId}/unbind-users`,
    method: 'get',
    params: query,
  });
}

// 绑定租户用户
export function bindTenantUser(tenantId, userIdList) {
  return request({
    url: `/system/tenant/${tenantId}/bind-users`,
    method: 'post',
    data: userIdList,
  });
}

// 解绑租户用户
export function unbindTenantUser(tenantId, userIdList) {
  return request({
    url: `/system/tenant/${tenantId}/unbind-users`,
    method: 'post',
    data: userIdList,
  });
}

// 查询租户配置项分页列表
export function getTenantOptionPage(query) {
  return request({
    url: '/system/tenant-option/page',
    method: 'get',
    params: query,
  });
}

// 查询租户配置项详情
export function getTenantOptionDetail(id) {
  return request({
    url: `/system/tenant-option/${id}`,
    method: 'get',
  });
}

// 新增租户配置项
export function addTenantOption(data) {
  return request({
    url: '/system/tenant-option',
    method: 'post',
    data,
  });
}

// 修改租户配置项
export function editTenantOption(data) {
  return request({
    url: '/system/tenant-option',
    method: 'put',
    data,
  });
}

// 删除租户配置项
export function delTenantOption(data) {
  return request({
    url: '/system/tenant-option',
    method: 'delete',
    data,
  });
}

// 查询租户配置项下拉列表
export function getTenantOptionDropdown() {
  return request({
    url: '/system/tenant-option/dropdown',
    method: 'get',
  });
}
