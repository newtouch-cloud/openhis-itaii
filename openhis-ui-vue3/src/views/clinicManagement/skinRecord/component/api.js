import request from '@/utils/request'

export function listSkinRecord(query) {
    return request({
      url: '/patientmanage/records/outpatient-record-page',
      method: 'get',
      params: query
    })
  }