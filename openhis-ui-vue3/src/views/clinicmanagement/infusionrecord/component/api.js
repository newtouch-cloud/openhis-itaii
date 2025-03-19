import request from '@/utils/request'

// 待执行输液记录查询
export function listInfusionRecord(query) {
  return request({
    url: '/outpatient-manage/infusion/infusion-wait-perform-record',
    method: 'get',
    params: query
  })
}
// 病人列表
export function listPatients() {
  return request({
    url: '/outpatient-manage/infusion/patient-infusion',
    method: 'get',
  })
}

// 批量执行患者门诊输液
export function updateInfusionRecord(data) {
    return request({
      url: '/outpatient-manage/infusion/infusion-perform/batch',
      method: 'put',
      data: data
    })
  }

  // 查询单个患者门诊输液记录查询
  export function listPatientInfusionRecord(query) {
    return request({
      url: '/outpatient-manage/infusion/patient-infusion',
      method: 'get',
      params: query
    })
  }

    // 门诊输液执行历史记录查询
    export function listPatientInfusionPerformRecord(query) {
      return request({
        url: '/outpatient-manage/infusion/infusion-perform-record',
        method: 'get',
        params: query
      })
    }