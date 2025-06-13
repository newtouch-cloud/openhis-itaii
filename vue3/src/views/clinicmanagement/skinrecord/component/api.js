import request from '@/utils/request'

export function listSkinRecord(query) {
  return request({
    url: '/outpatient-manage/skin-test/outpatient-record-page',
    method: 'get',
    params: query
  })
}

export function lists() {
  return request({
    url: '/outpatient-manage/skin-test/init',
    method: 'get',
  })
}


export function updateSkinTestRecord(data) {
    return request({
      url: '/outpatient-manage/skin-test/outpatient-record-skin-test',
      method: 'put',
      data: data
    })
  }

  
  export function updateNurseSign(data) {
      return request({
        url: '/outpatient-manage/skin-test/outpatient-record-sign-check',
        method: 'put',
        data: data
      })
    }