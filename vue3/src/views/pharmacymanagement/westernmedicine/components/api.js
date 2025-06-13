import request from '@/utils/request'

export function listPatient(query) {
  return request({
    url: '/pharmacy-manage/western-medicine-dispense/encounter-list',
    method: 'get',
    params: query
  })
}

export function listInit(query) {
  return request({
    url: '/pharmacy-manage/western-medicine-dispense/init',
    method: 'get',
    params: query
  })
}

export function listWesternmedicine(query) {
  return request({
    url: '/pharmacy-manage/western-medicine-dispense/prescription-list',
    method: 'get',
    params: query
  })
}

export function updateMedicion(prescriptionList) {
    return request({
      url: '/pharmacy-manage/western-medicine-dispense/medicine-dispense',
      method: 'put',
      data: prescriptionList
    })
  }
export function prepareMedicion(data) {
    return request({
      url: '/pharmacy-manage/western-medicine-dispense/prepare',
      method: 'put',
      data: data
    })
  }

  
export function backMedicion(prescriptionNo,notPerformedReasonEnum) {
  return request({
    url: '/pharmacy-manage/western-medicine-dispense/medicine-cancel',
    method: 'put',
    params: {
      prescriptionNo: prescriptionNo,
      notPerformedReasonEnum:notPerformedReasonEnum
    }
  })
}
//扫码枪返回追溯码筛选
export function itemTraceNo(params) {
  return request({
    url: '/app-common/item-trace-no?traceNoList=' + params,
    method: 'get',
  })
}