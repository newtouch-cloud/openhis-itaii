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
 * 暂离
 */
export function leaveEncounter(encounterId) {
  return request({
    url: '/doctor-station/main/leave-encounter?encounterId=' + encounterId,
    method: 'get',
  })
}

/**
 * 完诊
 */
export function completeEncounter(encounterId) {
  return request({
    url: '/doctor-station/main/complete-encounter?encounterId=' + encounterId,
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
export function deleteEmrTemplate(id){
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

/**
 * 
 * 获取诊断回显数据
 */
export function getEncounterDiagnosis(encounterId) {
  return request({
    url: '/doctor-station/diagnosis/get-encounter-diagnosis?encounterId=' + encounterId,
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
 * 保存处方(单条)
 */
export function savePrescription(data) {
  return request({
    url: '/doctor-station/advice/save-advice',
    method: 'post',
    data: data
  })
}
/**
 * 签发处方
 */
export function savePrescriptionSign(data) {
  return request({
    url: '/doctor-station/advice/sign-advice',
    method: 'post',
    data: data
  })
}
/**
 * 处方签退
 */
export function singOut(data) {
  return request({
    url: '/doctor-station/advice/sign-off',
    method: 'post',
    data: data
  })
}
/**
 * 获取患者本次就诊处方
 */
export function getPrescriptionList(encounterId) {
  return request({
    url: '/doctor-station/advice/request-base-info?encounterId=' + encounterId,
    method: 'get',
  })
}
/**
 * 获取科室列表
 */
export function getOrgTree() {
  return request({
    url: '/base-data-manage/organization/organization',
    method: 'get',
  })
}
/**
 * 获取退费账单
 */
export function getEncounterPatientPayment(encounterId) {
  return request({
    url: '/charge-manage/refund/patient-payment?encounterId=' + encounterId,
    method: 'get',
  })
}
/**
 * 申请退费
 */
export function refundPayment(data) {
  return request({
    url: '/charge-manage/refund/refund-payment',
    method: 'post',
    data: data
  })
}


// 电子处方相关接口
/**
 * 电子处方查询
 */
export function getVeriPrescriptionInfo(queryParams) {
  return request({
    url: '/ybelep-request/get-PrescriptionInfo',
    method: 'get',
    params: queryParams
  })
}

// 处方开立相关接口
/**
 * 获取全部药品信息
 */
export function getAllMedicationInfo(queryParams) {
  return request({
    url: '/doctor-station/elep/get-allMedicationInfo',
    method: 'get',
    params: queryParams
  })
}

/**
 * 电子处方下拉框
 */
export function elepPrescriptionInit() {
  return request({
    url: '/doctor-station/elep/init',
    method: 'get'
  })
}

/**
 * 获取处方信息
 */
export function getPrescriptionInfo(queryParams) {
  return request({
    url: '/doctor-station/elep/get-prescriptionInfo',
    method: 'get',
    params: queryParams
  })
}

/**
 * 获取药品信息
 */
export function getMedicationInfo(queryParams) {
  return request({
    url: '/doctor-station/elep/get-medicationInfo',
    method: 'get',
    params: queryParams
  })
}

/**
 * 获取单据号
 */
export function prescriptionNoInit() {
  return request({
    url: '/doctor-station/elep/prescriptionNoInit',
    method: 'get'
  })
}

/**
 * 新增处方
 */
export function savePrescriptionInfo(data) {
  return request({
    url: '/doctor-station/elep/save-prescriptionInfo',
    method: 'post',
    data: data
  })
}

/**
 * 修改处方
 */
export function updatePrescriptionInfo(data) {
  return request({
    url: '/doctor-station/elep/update-prescriptionInfo',
    method: 'post',
    data: data
  })
}

/**
 * 删除处方
 */
export function deletePrescriptionInfo(data) {
  return request({
    url: '/doctor-station/elep/delete-prescriptionInfo',
    method: 'post',
    data: data
  })
}

/**
 * 签发处方
 */
export function issuancePrescription(prescriptionNoList) {
  return request({
    url: '/doctor-station/elep/issuance-prescription',
    method: 'post',
    data: prescriptionNoList
  })
}

/**
 * 获取医嘱历史数据
 */
export function getAdviceHistoryInfo(params) {
  return request({
    url: '/doctor-station/advice/request-history-info',
    method: 'get',
    params: params
  })
}

/**
 * 组合/拆组
 */
export function updateGroupId(data) {
  return request({
    url: '/doctor-station/advice/update-groupid',
    method: 'put',
    data: data
  })
}

/**
 * 组套列表
 */
export function getOrderGroupList(params) {
  return request({
    url: '/personalization/order-group/order-group',
    method: 'get',
    params: params
  })
}

/**
 * 获取费用性质
 */
export function getContract(params) {
  return request({
    url: '/doctor-station/advice/get-encounter-contract',
    method: 'get',
    params: params
  })
}


/**
 * 取得药品最新数据
 */
export function queryYbCatalogue(params) {
  return request({
    url: '/yb-request/query-yb-catalogue',
    method: 'post',
    params: params
  })
}

/**
 * 获取人员慢性病诊断
 */
export function getChronicDisease(params) {
  return request({
    url: '/yb-request/getConditionDefinition',
    method: 'get',
    params: params
  })
}

/**
 * 获取中药列表
 */
export function getTcmMedicine(params) {
  return request({
    url: '/doctor-station/chinese-medical/tcm-advice-base-info',
    method: 'get',
    params: params
  })
}

/**
 * 获取中医诊断
 */
export function getTcmCondition(params) {
  return request({
    url: '/doctor-station/chinese-medical/condition-info',
    method: 'get',
    params: params
  })
}

/**
 * 获取辩证分型
 */
export function getTcmSyndrome(params) {
  return request({
    url: '/doctor-station/chinese-medical/syndrome-info',
    method: 'get',
    params: params
  })
}