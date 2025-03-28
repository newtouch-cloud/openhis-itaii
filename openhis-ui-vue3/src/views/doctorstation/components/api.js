import request from '@/utils/request'

// 病历相关接口
/**
 * 获取患者列表
 */
export function getList(queryParams) {
  return request({
    url: '/doctor-station/main/patient-info',
    method: 'get',
    params: queryParams
  })
}

/**
 * 获取患者历史病历
 */
export function getEmrHistoryList(queryParams) {
  return request({
    url: '/doctor-station/emr/emr-page',
    method: 'get',
    params: queryParams
  })
}

/**
 * 获取病历模板列表
 */
export function getEmrTemplateList(queryParams) {
  return request({
    url: '/doctor-station/emr/emr-template-page',
    method: 'get',
    params: queryParams
  })
}

/**
 * 接诊
 */
export function receiveEncounter(encounterId) {
  return request({
    url: '/doctor-station/main/receive-encounter?encounterId=' + encounterId,
    method: 'get',
  })
}

/**
 * 保存病历
 */
export function saveEmr(data) {
  return request({
    url: '/doctor-station/emr/emr',
    method: 'post',
    data: data
  })
}

/**
 * 删除病历模板
 */
export function deleteEmrTemplte(id){
  return request({
    url: '/doctor-station/emr/emr-template?id=' + id,
    method: 'delete',
  })
}

/**
 * 获取病历详情
 */
export function getEmrDetail(encounterId){
  return request({
    url: '/doctor-station/emr/emr-detail?encounterId=' + encounterId,
    method: 'get',
  })
}

/**
 * 保存病历模板
 */
export function saveEmrTemplate(data){
  return request({
    url: '/doctor-station/emr/emr-template',
    method: 'post',
    data: data
  })
}

// 诊断相关接口
/**
 * 保存诊断
 */
export function saveDiagnosis(data) {
  return request({
    url: '/doctor-station/diagnosis/save-doctor-diagnosis',
    method: 'post',
    data: data
  })
}

/**
 * 添加诊断绑定
 */
export function saveDiagnosisBind(data) {
  return request({
    url: '/doctor-station/diagnosis/diagnosis-belong-binding',
    method: 'post',
    data: data
  })
}
/**
 * 删除诊断绑定
 */
export function deleteDiagnosisBind(id) {
  return request({
    url: '/doctor-station/diagnosis/diagnosis-belong-binding?id=' + id,
    method: 'delete',
  })
}

/**
 * 获取诊断定义列表
 */
export function getDiagnosisDefinitionList(queryParams) {
  return request({
    url: '/doctor-station/diagnosis/condition-definition-metadata',
    method: 'get',
    params: queryParams
  })
}

/**
 * 获取诊断分类数据，历史诊断/个人常用诊断/科室常用诊断
 */
export function getConditionDefinitionInfo(patientId) {
  return request({
    url: '/doctor-station/diagnosis/get-condition-definition-class?patientId=' + patientId,
    method: 'get',
  })
}

/**
 * 获取诊断基础下拉数据
 */
export function diagnosisInit() {
  return request({
    url: '/doctor-station/diagnosis/init',
    method: 'get',
  })
}

// 处方相关接口
/**
 * 获取药品列表
 */
export function getAdviceBaseInfo(queryParams) {
  return request({
    url: '/doctor-station/advice/advice-base-info',
    method: 'get',
    params: queryParams
  })
}
/**
 * 保存处方
 */
export function savePrescription(data) {
  return request({
    url: '/doctor-station/advice/save-advice',
    method: 'post',
    data: data
  })
}
