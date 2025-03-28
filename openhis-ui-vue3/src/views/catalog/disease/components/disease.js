import request from '@/utils/request'

// 查询病种目录列表
export function getDiseaseList(query) {
  return request({
    url: '/data-dictionary/disease/information-page',
    method: 'get',
    params: query
  })
}

// 查询病种目录详细
export function getDiseaseOne(id) {
  return request({
    url: '/data-dictionary/disease/information-one',
    method: 'get',
    params: { id } // 确保参数正确传递
  })
}

// 新增病种目录
export function addDisease(data) {
  return request({
    url: '/data-dictionary/disease/information',
    method: 'post',
    data: data
  })
}

// 修改病种目录
export function editDisease(data) {
  return request({
    url: '/data-dictionary/disease/information',
    method: 'put',
    data: data
  })
}

// // 删除病种目录
// export function delUser(userId) {
//   return request({
//     url: '/system/user/' + userId,
//     method: 'delete'
//   })
// }

// 病种目录分类查询
export function getDiseaseCategory() {
  return request({
    url: '/data-dictionary/disease/information-init',
    method: 'get'
  })
}

// 停用病种目录
export function stopDisease(ids) {
  return request({
    url: '/data-dictionary/disease/information-stop',
    method: 'put',
    data: ids
  })
}

// 启用病种目录
export function startDisease(ids) {
  return request({
    url: '/data-dictionary/disease/information-start',
    method: 'put',
    data: ids
  })
}