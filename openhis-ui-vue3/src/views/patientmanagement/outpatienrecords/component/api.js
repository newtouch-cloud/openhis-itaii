import request from '@/utils/request'

export function listOutpatienRecords(query) {
    return request({
      url: '/patientmanage/records/outpatient-record-page',
      method: 'get',
      params: query
    })
  }

  export function listDoctorNames() {
    return request({
      url: '/patientmanage/records/init',
      method: 'get',
    })
  }

  
