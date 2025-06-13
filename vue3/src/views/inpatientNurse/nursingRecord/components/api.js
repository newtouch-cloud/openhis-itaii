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
 * 获取患者记录单
 *
 * @param query 查询参数对象，可以包含多个查询条件
 * @returns Promise<any> 返回一个Promise对象，解析后得到患者列表数据
 */
export function getNursingPatientPage(query) {
  return request({
    url: '/nursing-record/nursing-patient-page',
    method: 'get',
    params: query
  })
}

/**
 * 新增记录单信息
 *
 */
export function saveRecord(data) {
  return request({
    url: '/nursing-record/save-nursing',
    method: 'post',
    data: data
  })
}

/**
 * 保存记录单信息
 *
 */
export function updateRecord(data) {
  return request({
    url: '/nursing-record/update-nursing',
    method: 'post',
    data: data
  })
}

/**
 * 删除记录单
 */
export function delRecord(data) {
  return request({
    url: '/nursing-record/delete-nursing',
    method: 'post',
    data: data
  })
}

/**
 *获取记录单模板列表
 *
 * @param query 查询参数对象，可以包含多个查询条件
 * @returns Promise<any> 返回一个Promise对象，解析后得到患者列表数据
 */
export function getRecordTemplateList(query) {
  return request({
    url: '/nursing-record/emr-template-page',
    method: 'get',
    params: query
  })
}

/**
 * 新增记录单模板
 *
 */
export function saveRecordTemplate(data) {
  return request({
    url: '/nursing-record/emr-template-save',
    method: 'post',
    data: data
  })
}

/**
 * 保存记录单模板
 *
 */
export function updateRecordTemplate(data) {
  return request({
    url: '/nursing-record/emr-template-update',
    method: 'post',
    data: data
  })
}

/**
 * 删除记录单模板
 */
export function deleteRecordTemplate(data) {
  return request({
    url: '/nursing-record/emr-template-del',
    method: 'post',
    data: data
  })
}