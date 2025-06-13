import request from '@/utils/request'
export function getRreportReturnIssue(query) {
    return request({
      url:'/report-manage/return-issue/report-return-issue', 
      method: 'get',
      params: query
    })
}
// 获取科室列表
export function getDepartmentList() {
    return request({
      url: '/app-common/department-list',
      method: 'get',
    })
  }

// 获取日结
export function getTotal(params) {
    return request({
      url: '/payment/bill/getTotal',
      method: 'get',
      params: params
    })
  }