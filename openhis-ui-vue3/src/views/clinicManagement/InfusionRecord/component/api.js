import request from '@/utils/request'

export function listInfusionRecord(query) {
  return request({
    url: '/outpatientmanage/infusion/init',
    method: 'get',
    params: query
  })
}

export function listPatients() {
  return request({
    url: '/outpatientmanage/infusion/patients',
    method: 'get',
  })
}

export function updateInfusionRecord(data) {
    return request({
      url: '/outpatientmanage/infusion/outpatient-record-skintest',
      method: 'put',
      data: data
    })
  }