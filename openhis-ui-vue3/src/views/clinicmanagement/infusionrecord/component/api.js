import request from '@/utils/request'

// 待执行输液记录查询
export function listInfusionRecord() {
  return request({
    url: '/outpatient-manage/infusion/infusion-wait-perform-record',
    method: 'get'
  })
}
// 病人列表
export function listPatients(query) {
  return request({
    url: '/outpatient-manage/infusion/infusion-patient-list',
    method: 'get',
    params: query
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
  // 时间更改
export function editPatientInfusionTime(data) {
  return request({
    url: '/outpatient-manage/infusion/infusion-perform-time',
    method: 'put',
    data: data
  })
}

  // 点击患者，查询该患者的输液记录
  export function listPatientInfusionRecord(query) {
    return request({
      url: '/outpatient-manage/infusion/patient-infusion-record',
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