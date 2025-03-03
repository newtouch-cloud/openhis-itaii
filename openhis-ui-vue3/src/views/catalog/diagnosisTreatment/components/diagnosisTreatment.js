import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/openhis";

// 查询诊疗目录列表
export function getDiagnosisTreatmentList(query) {
  return request({
    url: '/datadictionary/diagnosistreatment/information-page',
    method: 'get',
    params: query
  })
}

// 查询诊疗目录详细
export function getDiagnosisTreatmentOne(id) {
  return request({
    url: '/datadictionary/diagnosistreatment/information-one/' + parseStrEmpty(id),
    method: 'get'
  })
}

// 新增诊疗目录
export function addDiagnosisTreatment(data) {
  return request({
    url: '/datadictionary/diagnosistreatment/information',
    method: 'post',
    data: data
  })
}

// 修改诊疗目录
export function editDiagnosisTreatment(data) {
  return request({
    url: '/datadictionary/diagnosistreatment/information',
    method: 'put',
    data: data
  })
}

// // 删除诊疗目录
// export function delUser(userId) {
//   return request({
//     url: '/system/user/' + userId,
//     method: 'delete'
//   })
// }

// 诊疗目录分类查询
export function getDiseaseTreatmentInit() {
  return request({
    url: '/datadictionary/diagnosistreatment/init',
    method: 'get'
  })
}

// 停用诊疗目录
export function stopDiseaseTreatment(ids) {
  console.log(ids)
  return request({
    url: '/datadictionary/diagnosistreatment/information-stop',
    method: 'put',
    data: ids
  })
}

// 启用诊疗目录
export function startDiseaseTreatment(ids) {
  console.log(ids)
  return request({
    url: '/datadictionary/diagnosistreatment/information-start',
    method: 'put',
    data: ids
  })
}
