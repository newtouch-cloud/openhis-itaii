// 医院名称
// export const HospitalName = '中国人民解放军联勤保障部队第九六四医院'
export const HospitalName = ''
// 体温单名称
export const temperatureName = '体 温 单'
// 患者信息显示项目（修改显示顺序及是否显示）
export const INFO_KEYS = [
  {
    name: '姓名',
    key: 'name',
    order: 1,
    show: true
  },
  {
    name: '科室',
    key: 'deptName',
    order: 2,
    show: true
  },
  {
    name: '床号',
    key: 'cwh',
    order: 3,
    show: true
  },
  {
    name: '病人ID',
    key: 'patientId',
    order: 4,
    show: true
  },
  {
    name: '住院号',
    key: 'hosNum',
    order: 5,
    show: true
  },
  // {
  //   name: '入院日期',
  //   key: 'hospDate',
  //   order: 6,
  //   show: true
  // }
]
// 体温单录入项目（修改显示顺序及是否显示）
// 新增项目注意 key值、code值唯一性，不要与之前的项目重复
export const BOTTOM_KEYS = [
  {
    name: '血\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0压',
    code: 'bloodPressure',
    key: '008',
    times: 2,
    order: 1,
    show: true
  },
  {
    name: '大便次数',
    code: 'poop',
    key: '005',
    order: 2,
    show: true
  },
  {
    name: '体\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0重',
    code: 'weight',
    key: '009',
    order: 3,
    show: true
  },
  {
    name: '尿\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0量',
    code: 'urine',
    key: '004',
    order: 4,
    show: true
  },
  {
    name: '摄入液量',
    code: 'input',
    key: '006',
    order: 5,
    show: true
  },
  {
    name: '排出液量',
    code: 'output',
    key: '007',
    order: 6,
    show: true
  },
  {
    name: '术后天数',
    code: 'output',
    key: '010',
    order: 7,
    show: true
  },
  {
    name: '',
    code: 'useless1',
    key: '100',
    order: 20,
    show: true
  },
  {
    name: '',
    code: 'useless2',
    key: '100',
    order: 21,
    show: true
  },
  {
    name: '',
    code: 'useless3',
    key: '100',
    order: 22,
    show: true
  },
  {
    name: '',
    code: 'useless4',
    key: '100',
    order: 23,
    show: true
  },
  {
    name: '',
    code: 'useless5',
    key: '100',
    order: 24,
    show: true
  },
  {
    name: '',
    code: 'useless6',
    key: '100',
    order: 25,
    show: true
  }
]
// 是否显示疼痛评分
export const showPainFlag = true

/** ***********    以上信息是本地化可以修改的部分     **************************/
// 获取患者信息显示项目
export function getInfoKeys() {
  return INFO_KEYS.filter(x => x.show).sort((a, b) => a.order - b.order)
}
// 获取患者信息显示项目
export function getBottomKeys() {
  return BOTTOM_KEYS.filter(x => x.show).sort((a, b) => a.order - b.order)
}

export const timeNumber = [2, 6, 10, 14, 18, 22] // 时间展示
export const nightTime = [2, 18, 22] // 夜间红色高亮时间
export const leftTEXT = [
  ['脉搏,(次/分),180', '160', '140', '120', '100', '80', '60', '40'],
  ['体温,(℃), 42', '41', '40', '39', '38', '37', '36', '35']
]

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
export const bodyTemperature = [34, 42]
export const starNumEnv = bodyTemperature[0] // 开始体温
export const endNumEnv = bodyTemperature[1] // 结束体温
export const heartRange = [20, 180]

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
    name: '不升'
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
]

export const HEAD_HEIGHT = 120 // 头部文字预留位置
export const LINE_HEIGHT = 20 // 一行的行高
export const textLeftMargin = 4 // 文字左边边距
export const TEXT_MARGIN_BOTTOM = 6 // 文字向上偏移量
export const symbolArrowHeight = 20
