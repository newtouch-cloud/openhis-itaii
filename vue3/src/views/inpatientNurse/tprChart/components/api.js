import request from '@/utils/request'

/**
 * 获取患者列表
 *
 * @param query 查询参数对象，可以包含多个查询条件
 * @returns Promise<any> 返回一个Promise对象，解析后得到患者列表数据
 */
export function listPatient(query) {
  return request({
    url: '/nursing-record/patient-page',
    method: 'get',
    params: query
  })
}

/**
 * 查询患者体征数据
 *
 * @param query 查询参数对象，可以包含多个查询条件
 * @returns Promise<any> 返回一个Promise对象，解析后得到患者列表数据
 */
export function getVitalSignsInfo(query) {
  return request({
    url: '/vital-signs/patient-message',
    method: 'get',
    params: query
  })
}

/**
 * 获取空床列表
 *
 * @param organizationId 科室ID
 * @returns Promise<any> 返回一个Promise对象，解析后得到患者列表数据
 */
export function getEmptyBed(organizationId) {
  return request({
    url: '/patient-home-manage/empty-bed',
    method: 'get',
    params: { organizationId }
  })
}

/**
 * 保存换床信息
 *
 */
export function saveBedTransfer(params) {
  return request({
    url: '/patient-home-manage/bed-transfer',
    method: 'put',
    params: params
  })
}

/**
 * 获取科室列表
 *
 * @returns Promise<any> 返回一个Promise对象，解析后得到科室列表数据
 */
export function getCaty() {
  return request({
    url: '/patient-home-manage/caty',
    method: 'get'
  })
}


/**
 * 保存转科信息
 *
 */
export function saveDepartmentTransfer(params) {
  return request({
    url: '/patient-home-manage/department-transfer',
    method: 'put',
    params: params
  })
}

/**
 * 保存出院信息
 *
 */
export function saveDischargeHospital(params) {
  return request({
    url: '/patient-home-manage/discharge-from-hospital',
    method: 'put',
    params: params
  })
}

/**
 * 收费患者列表
 */
export function getList(queryParams) {
    return request({
      url: '/charge-manage/charge/encounter-patient-page',
      method: 'get',
      params: queryParams
    })
}

/**
 * 新增体征录入
 */
export function addVitalSigns(data) {
  return request({
    url: '/vital-signs/record-saving',
    method: 'put',
    data: data
  })
}

/**
 * 删除体征录入
 */
export function delVitalSigns(data) {
  return request({
    url: '/vital-signs/record-delete',
    method: 'delete'
  })
}
