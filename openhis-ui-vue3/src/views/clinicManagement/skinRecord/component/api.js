import request from '@/utils/request'

export function listSkinRecord(query) {
  return request({
    url: '/outpatientmanage/skintest/outpatient-record-page',
    method: 'get',
    params: query
  })
}

export function lists() {
  return request({
    url: '/outpatientmanage/skintest/init',
    method: 'get',
  })
}


export function updateSkinTestRecord(data) {
    return request({
      url: '/outpatientmanage/outpatient-record-skintest',
      method: 'put',
      data: data
    })
  }

  
  export function updateNurseSign(data) {
      return request({
        url: '/outpatientmanage/outpatient-record-signcheck',
        method: 'put',
        data: data
      })
    }