import request from '@/utils/request'

export function listSkinRecord(query) {
  return request({
    url: '/outpatientmanage/outpatient-record-page',
    method: 'get',
    params: query
  })
}

export function listStatus() {
  return request({
    url: '/outpatientmanage/list-skinteststatus',
    method: 'get',
  })
}

export function listSkinResult() {
  return request({
    url: '/outpatientmanage/list-skintestresult',
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