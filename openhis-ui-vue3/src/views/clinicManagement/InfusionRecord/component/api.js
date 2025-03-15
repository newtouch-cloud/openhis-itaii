import request from '@/utils/request'

export function listSkinRecord(query) {
  return request({
    url: '/outpatientmanage/outpatient-record-page',
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