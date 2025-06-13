import request from '@/utils/request'

// 新增
export function addPatient(data) {
  return request({
    url: '/patient-manage/information/patient-information',
    method: 'post',
    data: data
  })
}

// 查询菜单列表
export function listPatient(query) {
    return request({
      url: '/patient-manage/information/patient-information-page',
      method: 'get',
      params: query
    })
  }
  export function lists() {
    return request({
      url: '/patient-manage/information/init',
      method: 'get'
    })
  }

  // 修改
export function updatePatient(data) {
    return request({
      url: '/patient-manage/information/patient-information',
      method: 'put',
      data: data
    })
  }


