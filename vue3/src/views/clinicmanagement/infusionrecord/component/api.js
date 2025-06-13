import request from '@/utils/request'

// 待执行输液记录查询
export function listInfusionRecord(encounterId) {
  return request({
    url: '/outpatient-manage/infusion/infusion-pending-record',
    method: 'get',
    params: encounterId
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
      url: '/outpatient-manage/infusion/infusion-perform',
      method: 'put',
      data: data
    })
  }
  // 时间更改
export function editPatientInfusionTime(data) {
  return request({
    url: '/outpatient-manage/infusion/infusion-perform-time',
    method: 'put',
    params: data
  })
}
  // 撤销申请
export function cancelPerform(data) {
  return request({
    url: '/outpatient-manage/infusion/cancel-perform',
    method: 'put',
    params: { serviceReqId: data.serviceId }
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
    export function listPatientInfusionPerformRecord(params) {
      return request({
        url: '/outpatient-manage/infusion/infusion-perform-record',
        method: 'get',
        params: params
      })
    }

    // 初始化
    export function init() {
      return request({
        url: '/outpatient-manage/infusion/init',
        method: 'get',
      })
}
 //输液贴打印获取
export function getBottleLabel(params) {
    return request({
      url: '/report-manage/print/bottle-label-batch-print',
      method: 'get',
      params: params
    })
  }

    