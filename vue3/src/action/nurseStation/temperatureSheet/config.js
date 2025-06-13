import dayjs from 'dayjs'
import {
  HospitalName,
  temperatureName,
  showPainFlag,
  getInfoKeys,
  getBottomKeys
} from './template'

// 存放体温单的配置信息
export const Header = {
  HospitalName,
  temperatureName
}
// 患者信息
export const INFO_KEYS = getInfoKeys()
// 头部信息标签
export const TOP_KEYS = [
  //{
  //  name: '患病日数',
  //  getValue: (i, renderData) => {
  //    const { beginDate, hospDays, outdate = '', dateClosed } = renderData.infoData
  //    const tieml = new Date()
  //    const timeNew = new Date((tieml / 1000 + 86400) * 1000)
  //    const todayDate = dayjs(timeNew).format('YYYY-MM-DD')
  //    const endDate = dayjs(outdate).add(1, 'day').format('YYYY-MM-DD')
  //    const eachTime = dayjs(beginDate)
  //      .add(i, 'day')
  //      .format('YYYY-MM-DD')
  //    if (eachTime === endDate || todayDate === eachTime) {
  //      dateClosed.stopNumber = true
  //    }
  //   return dateClosed.stopNumber ? hospDays + i + 1 : ''
  //  }
  //},
  {
    name: '日   期',
    getValue: (i, renderData) => {
      const { beginDate, outdate = '', hospDate = '', dateClosed } = renderData.infoData
      const tieml = new Date()
      const timeNew = new Date((tieml / 1000 + 86400) * 1000)
      const todayDate = dayjs(timeNew).format('YYYY-MM-DD')
      const endDate = dayjs(outdate).format('YYYY-MM-DD')
      const startDate = dayjs(hospDate).format('YYYY-MM-DD')
      let eachTime = dayjs(beginDate).add(i, 'day').format('YYYY-MM-DD')
      if (eachTime === endDate || eachTime === todayDate) {
        dateClosed.stopTime = true
      }
      if ((startDate === eachTime && i === 0) || dayjs(eachTime).format('MM-DD') === '01-01') {
        eachTime = dayjs(eachTime).format('YYYY年MM月DD日')
      } else if (i === 0 || dayjs(eachTime).format('DD') === '01') {
        eachTime = dayjs(eachTime).format('MM月DD日')
      } else {
        eachTime = dayjs(eachTime).format('DD日')
      }
      return dateClosed.stopTime ? eachTime : ''
    }
  },
  {
    name: '住院日数',
    getValue: (i, renderData) => {
      const { beginDate, hospDays, outdate = '', dateClosed } = renderData.infoData
      const tieml = new Date()
      const timeNew = new Date((tieml / 1000 + 86400) * 1000)
      const todayDate = dayjs(timeNew).format('YYYY-MM-DD')
      const endDate = dayjs(outdate).add(1, 'day').format('YYYY-MM-DD')
      const eachTime = dayjs(beginDate).add(i, 'day').format('YYYY-MM-DD')
      if (eachTime === endDate || todayDate === eachTime) {
        dateClosed.stopNumber = false
      }
      const num = dateClosed.stopNumber ? hospDays + i + 1 : ''
      let hosNum = ''
      if (num !== '') {
        hosNum = '第' + "\xa0\xa0\xa0" + num + "\xa0\xa0\xa0" + '日'
      } else {
        hosNum = '第' + "\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0" + '日'
      }
      return hosNum
    }
  },
  {
    name: '术/娩后日数',
    getValue: (i, renderData) => {
      const  { beginDate }  = renderData.infoData
      const  surgeryNumDays   = renderData.surgeryNumDays
      const eachTime = dayjs(beginDate).add(i, 'day').format('YYYY-MM-DD')
      let num = surgeryNumDays.filter(item => item.date === eachTime)
      let hosNum = ''
      if (num.length > 0) {
        hosNum = '第' + "\xa0\xa0\xa0" + num[0].typeValue + "\xa0\xa0\xa0" + '日'
      } else {
        hosNum = '第' + "\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0" + '日'
      }
      return hosNum
    }
  }
]
// 最底部的字段绘制
export const BOTTOM_KEYS = getBottomKeys()
// 是否显示疼痛评分
export const showPain = showPainFlag

/** ***********    以下是固定选项     **************************/
export const timeNumber = [2, 6, 10, 14, 18, 22] // 时间展示
export const nightTime = [2, 18, 22] // 夜间红色高亮时间
//export const leftTEXT1 = [
//  // ['脉3搏,(次/分), 180', '160', '140', '120', '100', '80', '60', '40'],
//  // ['体4温,(℃), 42', '41', '40', '39', '38', '37', '36', '35']
 // // ['华氏,( °F),  107.6', '105.8', '104', '102.2', '100.4', '98.6', '96.8', '95']
 // ['华氏,( °F),  ', '', '107', '106', '105', '104', '103', '102', '101', '100', '99', '98', '97', '96', '95', '94']
 // // ['摄氏,(℃), 42', '41', '40', '39', '38', '37', '36', '35']
//]
//export const leftTEXT2 = [
//  ['摄氏,(℃), ', '42', '41', '40', '39', '38', '37', '36', '35']
//]
//export const rightTEXT = [
 // [',, ', '180', '160', '140', '120', '100', '80', '60', '40']
//]
//export const painTEXT = [
 // ['疼 痛'],
 // ['8', '6', '4', '2']
//]
export const leftTEXT = [
  ['脉搏,(次/分),', '180', '160', '140', '120', '100', '80', '60', '40'],
  ['体温,(℃), ', '42', '41', '40', '39', '38', '37', '36', '35']
]
export const painTEXT = [['疼 痛 强 度'], ['10', '8', '6', '4', '2', '0']]
export const inOutItem = ['入观', '分娩', '手术', '转入', '出观', '死亡']
export const otherItem = ['外出', '请假', '拒测', '离院', '其他']
export const sheetOptions = {
  locations: [{
    code: '1',
    display: '体温'
  }, {
    code: '2',
    display: '口温'
  }, {
    code: '3',
    display: '肛温'
  }, {
    code: '4',
    display: '耳温'
  }],
  breath: [{
    code: '',
    display: '自主呼吸'
  }, {
    code: '®',
    display: '机械通气'
  }],
  poop: [{
    code: '',
    display: '正常'
  }, {
    code: '※',
    display: '失禁'
  }, {
    code: '☆',
    display: '人工肛门'
  }, {
    code: '/E',
    display: '灌肠'
  }],
  pee: [{
    code: '',
    display: '正常'
  }, {
    code: '※',
    display: '失禁'
  }, {
    code: 'C+',
    display: '导尿'
  }],
  heart: ['窦性心律', '起搏心律', '房性心律', '异常心律'],
  weight: ['正常', '卧床', '轮椅', '平车']
}
// 此处是显示数字的 要和leftTEXT保持一直
// export const bodyTemperature1 = [94, 107]
// export const starNumEnv1 = bodyTemperature1[0] // 开始体温
// export const endNumEnv1 = bodyTemperature1[1] // 结束体温
export const bodyTemperature = [33, 43]
export const starNumEnv = bodyTemperature[0] // 开始体温
export const endNumEnv = bodyTemperature[1] // 结束体温
export const heartRange = [0, 200]
export const painScore = [0, 10]

// 中间图表字段对应
export const CHART_KEYS = [
  {
    key: '001',
    code: 'breath',
    name: '呼吸'
  },
  {
    key: '002',
    code: 'sphygmus',
    name: '脉搏'
  },
  {
    key: '003',
    code: 'temperature',
    name: '体温'
  },
  {
    key: '012',
    code: 'inOut',
    name: '特殊标记'
  },
  {
    key: '013',
    code: 'refuse',
    name: '标记内容'
  },
  {
    key: '014',
    code: 'heartRate',
    name: '心率'
  },
  {
    key: '015',
    code: 'lowerTemp',
    name: '物理降温'
  },
  {
    key: '016',
    code: 'painScore',
    name: '疼痛评分'
  }
  // {
  //   key: '017',
  //   code: 'admission',
  //   name: '入院'
  // },
  // {
  //   key: '018',
  //   code: 'bigSurgery',
  //   name: '大手术'
  // },
  // {
  //   key: '019',
  //   code: 'smallSurgery',
  //   name: '小手术'
  // },
  // {
  //   key: '020',
  //   code: 'discharge',
  //   name: '出院'
  // },
  // {
  //   key: '021',
  //   code: 'parturition',
  //   name: '分娩'
  // },
  // {
  //   key: '022',
  //   code: 'transfer',
  //   name: '转科'
  // },
  // {
  //   key: '9507',
  //   code: 'death',
  //   name: '死亡'
  // }
]

export const HEAD_HEIGHT = 80 // 头部文字预留位置
export const LINE_HEIGHT = 20 // 一行的行高
export const textLeftMargin = 4 // 文字左边边距
export const TEXT_MARGIN_BOTTOM = 6 // 文字向上偏移量
export const symbolArrowHeight = 20

