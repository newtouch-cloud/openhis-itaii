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

// 新增
export function addPatient(data) {
  return request({
    url: '/patientmanage/information/patient-information',
    method: 'post',
    data: data
  })
}

// 查询菜单列表
export function listPatient(query) {
    return request({
      url: '/patientmanage/information/patient-information-page',
      method: 'get',
      params: query
    })
  }

  // 修改
export function updatePatient(data) {
    return request({
      url: '/patientmanage/information/patient-information',
      method: 'put',
      data: data
    })
  }


