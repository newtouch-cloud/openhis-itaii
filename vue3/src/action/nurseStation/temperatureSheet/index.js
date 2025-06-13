/*
 * @Author: 程堡
 * @Date: 2022-04-01 09:31:59
 * @LastEditTime: 2022-04-01 09:31:59
 * @LastEditors: 程堡
 * @Description: 体温单
 * @FilePath: src\action\nurseStation\temperatureSheet\index.js
*/
import Request from '@/axios/index.js'
const temperaturePath = 'app/temperature'
const getTemperaturePath = 'app/temperature/by-encounter-id'
const delTemperaturePath = 'app/temperature/retired'
// import data from '../temperatureSheet/datas.js';
export const API = {

  /**
   * @description 查询患者体温单
   * @param encounterId
   */
  getTemperatures(id) {
    return Request({
      url: getTemperaturePath, // hash地址
      method: 'get', // 提交方法get
      verification: true, // 是否统一拦截验证
      untoken: false, // 是否不带token
      params: { // 参数列表
        id
      }
    })
  },
  /**
   * @description 创建体温单
   * @param encounterId
   */
  createTemperature(encounterId,
    hisNo,
    clinicCode,
    recordTime,
    type,
    content) {
    return Request({
      url: temperaturePath,
      method: 'post',
      verification: true,
      untoken: false,
      data: { // 参数列表
        encounterId,
        hisNo,
        clinicCode,
        recordTime,
        operCode: sessionStorage.getItem('userCode'),
        operName: sessionStorage.getItem('userName'),
        type,
        content,
        hospitalOrgId: sessionStorage.getItem('hospitalOrgId')
      }
    })
  },

  /**
   * @description 修改体温单
   * @param encounterId
   */
  updateTemperature(encounterId,
    hisNo,
    clinicCode,
    recordTime,
    type,
    content,
    id) {
    return Request({
      url: temperaturePath,
      method: 'put',
      verification: true,
      untoken: false,
      data: { // 参数列表
        encounterId,
        hisNo,
        clinicCode,
        recordTime,
        operCode: sessionStorage.getItem('userCode'),
        operName: sessionStorage.getItem('userName'),
        type,
        content,
        id
      }
    })
  },
  /**
   * @description 删除记录
   * @param encounterId
   */
  deleteTemperature(id) {
    return Request({
      url: delTemperaturePath,
      method: 'post',
      verification: true,
      untoken: false,
      data: { // 参数列表
        id
      }
    })
  },
  /**
   * @description 获取患者时间线
   * @param EncounterId
   */
  NewSheet(patientInfo) {
    return {
      grParamBOS:
        {
          age: patientInfo.age,
          birth: 868723200000,
          cwh: patientInfo.bedName,
          cardNo: patientInfo.hisId,
          hospDate: patientInfo.firstInBedTime.substring(0, 10),
          inDate: patientInfo.firstInBedTime,
          inDiagName: patientInfo.diag,
          name: patientInfo.patientName,
          deptName: patientInfo.deptName,
          operaDays: null,
          outdate: patientInfo.checkOutWardTime,
          sex: patientInfo.gender.display
        },
      rows: [],
      types: []
    }
  }

}
