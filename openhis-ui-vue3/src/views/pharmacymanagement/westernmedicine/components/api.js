import request from '@/utils/request'

export function listPatient(query) {
  return request({
    url: '/pharmacy-manage/western-medicine-dispense/encounter-list',
    method: 'get',
    params: query
  })
}

export function listInit(query) {
  return request({
    url: '/pharmacy-manage/western-medicine-dispense/init',
    method: 'get',
    params: query
  })
}

export function listWesternmedicine(query) {
  return request({
    url: '/pharmacy-manage/western-medicine-dispense/prescription-list',
    method: 'get',
    params: query
  })
}


export function updateMedicion(data) {
    return request({
      url: '/pharmacy-manage/western-medicine-dispense/medicine-dispense',
      method: 'put',
      data: data
    })
  }

  
  export function backMedicion(data) {
      return request({
        url: '/pharmacy-manage/western-medicine-dispense/medicine-cancel',
        method: 'put',
        data: data
      })
    }