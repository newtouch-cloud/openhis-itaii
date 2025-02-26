import request from '@/utils/request'


export function listmaritalstatus() {
  return request({
    url: '/patientmanage/information/list-maritalstatus',
    method: 'get',
  })
}

export function listoccupationtype() {
  return request({
    url: '/patientmanage/information/list-occupationtype',
    method: 'get',
  })
}

export function lisadministrativegender() {
  return request({
    url: '/patientmanage/information/list-administrativegender',
    method: 'get',
  })
}

export function listbloodtypeabo() {
  return request({
    url: '/patientmanage/information/list-bloodtypeabo',
    method: 'get',
  })
}


export function listbloodtypearh() {
  return request({
    url: '/patientmanage/information/list-bloodtypearh',
    method: 'get',
  })
}

export function listfamilyrelationshiptype() {
    return request({
      url: '/patientmanage/information/list-familyrelationshiptype',
      method: 'get',
    })
  }


