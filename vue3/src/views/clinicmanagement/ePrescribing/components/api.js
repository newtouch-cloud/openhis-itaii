import request from '@/utils/request'


// 电子处方管理画面相关接口
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

/**
* 电子处方详细
*/
export function getPrescriptionDetailInfo(prescriptionNo) {
  return request({
    url: '/ybelep-request/get-PrescriptionDetailInfo',
    method: 'get',
    params: { prescriptionNo }
  })
}
/**
 * 电子处方取药结果查询
 */
export function queryMedPrescription(hiRxno) {
  return request({
    url: '/ybelep-request/med-query',
    method: 'get',
    params: {hiRxno}
  })
}

/**
 * 电子处方查询
 */
export function queryPrescription(hiRxno) {
  return request({
    url: '/ybelep-request/pre-query',
    method: 'get',
    params: {hiRxno}
  })
}

/**
 * 医保电子处方状态更新（上传）
 */
export function uploadPrescriptionStatus(prescriptionNo) {
  return request({
    url: '/ybelep-request/uploadPrescriptionStatus',
    method: 'put',
    params: {prescriptionNo}
  })
}


/**
 * 医保电子处方拒绝上传
 */
export function refusePrescriptionStatus(prescriptionNo) {
  return request({
    url: '/ybelep-request/refusePrescriptionStatus',
    method: 'put',
    params: {prescriptionNo}
  })
}


/**
 * 医保电子处方状态更新（撤销）
 */
export function quashPrescriptionStatus(prescriptionNo,quashReason) {
  return request({
    url: '/ybelep-request/quashPrescriptionStatus',
    method: 'put',
    params: {prescriptionNo,quashReason}
  })
}


/**
 * 电子处方上传预核验
 */
export function preVerification(prescriptionNo,ecToken,authNo,tenantId) {
  return request({
    url: '/ybelep-request/pre-verification',
    method: 'get',
    params: {prescriptionNo,ecToken,authNo,tenantId}
  })
}

/**
 * 电子处方医保电子签名
 */
export function eleSignature(hiRxno,practitionerId,checkDate,tenantId) {
  return request({
    url: '/ybelep-request/pre-signature',
    method: 'get',
    params: {hiRxno,practitionerId,checkDate,tenantId}
  })
}

/**
 * 电子处方上传
 */
export function uploadElePrescription(hiRxno,practitionerId,checkDate,tenantId) {
  return request({
    url: '/ybelep-request/pre-upload',
    method: 'get',
    params: {hiRxno,practitionerId,checkDate,tenantId}
  })
}

/**
 * 电子处方撤销
 */
export function revokePrescription(hiRxno,practitionerId,description,revokeDate,tenantId) {
  return request({
    url: '/ybelep-request/pre-revoke',
    method: 'get',
    params: {hiRxno,practitionerId,description,revokeDate,tenantId}
  })
}