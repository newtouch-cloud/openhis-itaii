import request from '@/utils/request'

export function listOutpatienRecords(query) {
    return request({
      url: '/patient-manage/records/outpatient-record-page',
      method: 'get',
      params: query
    })
  }

  export function listDoctorNames() {
    return request({
      url: '/patient-manage/records/init',
      method: 'get',
    })
  }

  
