/*
 * @Author: sjjh
 * @Date: 2025-04-09 17:55:05
 * @Description: 
 */
// import  { IInPatient } from '@/model/IInPatient'
import { ref } from 'vue'

// 定义护士等级(没接口前mock)
export const nursingLevel = ref('0')

export function updateNursingLevel(level) {
  nursingLevel.value = level
}

// 选择患者信息
export const patientInfo = ref()
export function updatePatientInfo(info) {
  patientInfo.value = info
}

