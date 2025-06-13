// 数据处理
import * as d3 from 'd3'
import { symbol } from 'd3-shape'
import {
  getTypeData,
  getTypeDatas,
  setMergeTag,
  postpartumDays,
  getTypeAnimalHeat,
  getDrawData,
  getDrawCoolData,
  degreesOnline,
  getType,
  getHeartRate,
  disconnectEvents,
  getBrokenLine
} from './utils'
import { HEAD_HEIGHT, LINE_HEIGHT, TOP_KEYS, BOTTOM_KEYS, symbolArrowHeight, textLeftMargin} from './config'
export const iconDrawObj = {
  // 绘制圆形图标
  getDrawRoundIcon: ({ content,data, x, y, fill = 'blue',stroke = 'blue', r = 5 }) => {
    // 增加icon  红色空心
    content
      .append('g')
      .attr('fill', fill)
      .attr('stroke', stroke)
      .attr('stroke-width', 1)
      .selectAll('circle')
      .data(data)
      .join('circle')
      .attr('transform', (i) => {
        const yVal = y(i) || 0
        if (!yVal) {
          return 'scale(0)'
        }
        return ''
      })
      .attr('cx', x)
      .attr('cy', y)
      .attr('r', (i) => {
        if (y(i)) {
          return r
        }
        return 0
      })
  },
  // 绘制圆形点图标
  getDrawRoundDotIcon: ({ content, data, x,  y, fill = 'white', stroke = 'blue', deepFill = 'blue', r = 6 }) => {
    content
      .append('g')
      .attr('fill', fill)
      .attr('stroke', stroke)
      .attr('stroke-width', 1)
      .selectAll('circle')
      .data(data)
      .join('circle')
      .attr('transform', (i) => {
        const yVal = y(i) || 0
        if (!yVal) {
          return 'scale(0)'
        }
        return ''
      })
      .attr('cx', x)
      .attr('cy', y)
      .attr('r', r)
      .clone()
      .attr('cx', x)
      .attr('cy', y)
      .attr('r', 1)
      .attr('fill', deepFill)
  },
  // 绘制x
  getDrawXIcon: ({ content, data, x, y, fill = 'blue', stroke = 'blue' }) => {
    content
      .append('g')
      .attr('fill', fill)
      .attr('stroke', stroke)
      .attr('stroke-width', 1)
      .selectAll('line')
      .data(data)
      .join('line')
      .attr('transform', (i) => {
        let yVal = y
        if (typeof y === 'function') {
          yVal = y(i) || 0
        }
        if (!yVal) {
          return 'scale(0)'
        }
        return ''
      })
      .attr('x1', function(d) {
        return x(d) - 4
      })
      .attr('y1', function(d) {
        return y(d) - 4
      })
      .attr('x2', function(d) {
        return x(d) + 4
      })
      .attr('y2', function(d) {
        return y(d) + 4
      })
      .clone()
      .attr('x1', function(d) {
        return x(d) + 4
      })
      .attr('y1', function(d) {
        return y(d) - 4
      })
      .attr('x2', function(d) {
        return x(d) - 4
      })
      .attr('y2', function(d) {
        return y(d) + 4
      })
  },
  // 绘制三角形
  drawThreeIcon: ({ content, data,  x,y, fill = 'blue', stroke = 'blue', riangle = 48 }) => {
    // 蓝色三角形
    content
      .append('g')
      .attr('class', 'ceshiname')
      .selectAll('g')
      .data(data)
      .join('g')
      .attr('transform', (i) => {
        const yVal = y(i) || 0
        if (!yVal) {
          return 'scale(0)'
        }
        return `translate(${x(i)},${yVal})`
      })
      .append('path')
      .call((path) => {
        const symbolThree = symbol()
        const symbolIndex = 5
        symbolThree.type(d3.symbols[symbolIndex])
        path.attr('d', symbolThree.size(riangle)).attr('fill', fill) .attr('stroke', stroke)
        // .transition()
        // .duration(1500)
        // .attr('d', symbolThree.size(48))
      })
  }
}

export function getG(svg, viewConfig) {
  return svg .append('g') .attr( 'transform',  `translate(${viewConfig.marginLeft},${viewConfig.marginTop +  HEAD_HEIGHT})`  )
}

// 设置数据
export function getData(allData) {
  const rowsData = allData.rows // allData, '【全部数据】'
  const infoData = allData.grParamBOS
  const typesData = getTypeDatas(allData.types, allData.grParamBOS.beginDate)
  const selectOp = allData.selectOp
  const symbolTextArr = getTypeData('018', rowsData, false, allData.grParamBOS.beginDate) // 【特殊标记】
  const symbolGoUp = getType('003', rowsData, allData.grParamBOS.beginDate) // 不升
  // 体温单特殊数据
  const dicData = JSON.parse(window.localStorage.getItem('transDictCode'))
  const signsManagementList = dicData ? dicData.filter(item => item.name === '95')[0].concept.sort((a, b) => { return a.displayOrder - b.displayOrder }) :[]
  const otherArr = []
  if (signsManagementList.length > 0) {
    for (let j = 0; j < signsManagementList.length; j++) {
      if (signsManagementList[j].code !== '9502' & signsManagementList[j].code !== '9503') {
        const otherArrItem = getTypeData(signsManagementList[j].code, rowsData, false, allData.grParamBOS.beginDate)
        otherArr.push(otherArrItem)
      }
    }
  }
  const surgeryNumDays = allData.types.filter(item => item.typeCode === '031')
  const surgeryArr = getTypeData('9502', rowsData, false, allData.grParamBOS.beginDate) // '【手术】'
  const minSurgeryArr = getTypeData('9503', rowsData, false, allData.grParamBOS.beginDate) // '【小手术】'
  const temArr = getTypeData('00301', rowsData, false, allData.grParamBOS.beginDate) // '【体温拒测等】'
 // const symbolGoUp = getType('003', rowsData, allData.grParamBOS.beginDate) // symbolGoUp, '不升'
  // 35度线上的内容
  const symbolDegreesEvents = degreesOnline(symbolTextArr, selectOp, 0)
  const symbolDegreesOnline = disconnectEvents(  symbolTextArr, selectOp, 'isBeforeAfter',  0 )
  // 40~42线上的内容
  const symbolTopOnline = degreesOnline(symbolTextArr, selectOp, 1)
  const symbolTopDegreesOnline = disconnectEvents(
    symbolTextArr,
    selectOp,
    'isBeforeAfter',
    1
  )
  const symbolContent = getTypeData('013', rowsData, false, allData.grParamBOS.beginDate) // symbolContent, '【标记内容】'
  const mergeTag = setMergeTag(symbolTopOnline, symbolContent) // mergeTag, '【合并标记，标记内容】'
  // 产后日数
  infoData.postpartum = postpartumDays('031', typesData) // infoData, '产后日数'
  // 写死的先
  infoData.dateClosed = {
    stopTime: true, // 控制结束日期
    stopNumber: true // 控制住院天数
  }
  // 折线
  const brokenLineData = getBrokenLine(
    '003',
    rowsData,
    symbolDegreesOnline,
    symbolGoUp,
    symbolTopDegreesOnline,
    allData.grParamBOS.beginDate
  )
  // 模拟数据 体温
  const datasetAnus = getTypeAnimalHeat('003', rowsData, '1', allData.grParamBOS.beginDate) // datasetAnus, '腋温【x】'
  const bodyData = getTypeAnimalHeat('003', rowsData, '2', allData.grParamBOS.beginDate) // dbodyData, '口温'
  // const datasetHeartrate = getTypeAnimalHeat('003', rowsData)
  const datasetHeartrate = getTypeAnimalHeat('003', rowsData, '3', allData.grParamBOS.beginDate) // datasetHeartrate, '肛温【红空圆】'
  const earCool = getTypeAnimalHeat('003', rowsData, '4', allData.grParamBOS.beginDate) // earCool, '耳朵温'
  const painScore = getHeartRate( // painScore, '疼痛'
    '016',
    rowsData,
    symbolDegreesOnline,
    symbolTopDegreesOnline,
    true,
    allData.grParamBOS.beginDate
  )
  const allTemperatureData = [bodyData, datasetAnus, datasetHeartrate, earCool] // 所有的温度记录
  // '脉搏【红实圆】'
  const datasetPulse = getHeartRate(
    '002',
    rowsData,
    symbolDegreesOnline,
    symbolTopDegreesOnline,
    true,
    allData.grParamBOS.beginDate
  )
  // 心率【空心】
  const datasetHeartRate = getHeartRate(
    '014',
    rowsData,
    symbolDegreesOnline,
    symbolTopDegreesOnline,
    true,
    allData.grParamBOS.beginDate
  )
  // const allTemperatureData =  getDrawData('003', rowsData, '1', allData.grParamBOS.beginDate) // datasetAnus, '腋温【x】'
  // const dataCool = getDrawCoolData('015', rowsData, true, allData.grParamBOS.beginDate) // dataCool, '【物理降温】'
  const dataCool = getTypeData('015', rowsData, true, allData.grParamBOS.beginDate) // 【物理降温】
  // 呼吸【黑实圆】
  const datasetPain = getTypeData('001', rowsData, false, allData.grParamBOS.beginDate)// 呼吸

  const title = infoData.title
  return {
    title,
    datasetHeartRate,
    bodyData,
    datasetAnus,
    datasetHeartrate,
    earCool,
    painScore,
    allTemperatureData,
    datasetPulse,
    symbolContent,
    symbolTextArr,
    mergeTag,
    dataCool,
    datasetPain,
    typesData,
    infoData,
    rowsData,
    symbolDegreesOnline,
    symbolGoUp,
    symbolDegreesEvents,
    brokenLineData,
    otherArr,
    temArr,
    surgeryArr,
    minSurgeryArr,
    surgeryNumDays
  }
}

export function drawTopMask(svg, viewConfig) {
  // ===================== //
  // 遮罩层挡住超出的折线     //
  // =====================//
  svg
    .append('g')
    .attr('transform', `translate(${viewConfig.marginLeft},${HEAD_HEIGHT - LINE_HEIGHT})`)
    .append('rect')
    .attr('class', 'mask-rect')
    .attr('x', 0)
    .attr('y', -100)
    .attr('width', viewConfig.contentWidth)
    // 2.5是上下的一个区域    1 是原来有 2的宽度
    .attr('height', LINE_HEIGHT * (TOP_KEYS.length + 7.5) - 1)
    .attr('stroke', viewConfig.stroke)
    .attr('fill', '#fff')
    .attr('style', 'stroke-width: 0')

  drawTopVerticalLine(svg, viewConfig)
}

export function drawTopVerticalLine(svg, viewConfig) {
  // 补上下竖线
  let start = viewConfig.step
  const lineG = getG(svg, viewConfig)
    .append('g')
    .attr('class', 'maskline-top')
  while (start < viewConfig.contentWidth) {
   // const isLastLine = (start + viewConfig.step) >= viewConfig.contentWidth
    lineG
      .append('line')
      .attr('fill', 'stroke')
      .attr('x1', start)
      .attr('y1', 0)
      .attr('y2', (TOP_KEYS.length + 1) * LINE_HEIGHT)
      .attr('x2', start)
      .attr('stroke', start > viewConfig.step ? '#B22222' : viewConfig.stroke)
      // .attr('stroke', isLastLine ? 'black' : (start > viewConfig.step ? '#B22222' : viewConfig.stroke))
      .attr('stroke-width', 4) // 设置线条宽度为2
    start = start + viewConfig.step
  }
}

export function drawBottomMask(svg, viewConfig) {
  const g = getG(svg, viewConfig)
  // 遮罩层挡住超出的折线
  g.append('rect')
    .attr('class', 'mask-rect')
    .attr('x', 4)
    .attr('y', viewConfig.bottomKeysPosStart + viewConfig.micoStep * 2)
    .attr('width', viewConfig.contentWidth)
    .attr('height', LINE_HEIGHT * (BOTTOM_KEYS.length + 5))
    .attr('stroke', viewConfig.stroke)
    .attr('fill', '#ffffff')
    .attr('style', 'stroke-width: 0')
  drawBottomMaskLine(svg, viewConfig)
}

function drawBottomMaskLine(svg, viewConfig) {
  // 补上下竖线
  let start = viewConfig.step
  const lineG = getG(svg, viewConfig)
    .append('g')
    .attr('class', 'maskline')
  while (start < viewConfig.contentWidth) {
   // const isLastLine = (start + viewConfig.step) >= viewConfig.contentWidth;
    lineG
      .append('line')
      .attr('fill', 'stroke')
      .attr('x1', start)
      .attr('y1', viewConfig.bottomKeysPosStart)
      .attr('y2', viewConfig.tableHeight)
      .attr('x2', start)
      .attr('stroke', start > viewConfig.step ? '#B22222' : viewConfig.stroke)
      //.attr('stroke', isLastLine ? 'black' : (start > viewConfig.step ? '#B22222' : viewConfig.stroke))
      .attr('stroke-width', 4) // 设置线条宽度为2
    start = start + viewConfig.step
  }
}

// 绘制特殊事件文字 --- 有时间
export function drawSpecialText(svg, viewConfig, textData) {
  const g = getG(svg, viewConfig)
  g.append('g')
    .selectAll('text')
    .data(textData)
    .join('text')
    .attr('style', 'font-size:14px; fill: red;')
    .attr('class', 'mytext')
    .html((d, i) => {
      const t = (d?.value || '').split(',')
      let time = ''
      if (t.length > 1) {
        time = t[0] + '于' + transNum(t[1].split(':')[0]) + '时' + transNum(t[1].split(':')[1]) + '分'
        // time = t[0] + '㇑' + transNum(t[1].split(':')[0]) + '时' + transNum(t[1].split(':')[1]) + '分'
      }
      const texts = time.split('')
      return texts
        .map((text, i) => {
          return `<tspan dx="${i === 1 ? -14 : i === 0 ? 0 : Number(text) ? -8 : -14}" dy="${20}">${text}</tspan>`
        })
        .join('')
    })
    .attr('x', (d, i) => {
      return viewConfig.step + d.index * viewConfig.micoStep + textLeftMargin
    })
    .attr('y', () => {
      return viewConfig.topKeysPos - textLeftMargin
    })
}
// 绘制特殊事件文字 --- 大手术小手术
export function drawSurgery(svg, viewConfig, textData) {
  const g = getG(svg, viewConfig)
  const textDatas = []
  if (textData.length > 0) {
    for (let j = 0; j < textData.length; j++) {
      let item = {}
      if (textData[j].value) {
        item = {
          index: textData[j].index,
          value: textData[j].value.split(',')[0],
          date: textData[j].date
        }
      } else {
        item = textData[j]
      }
      textDatas.push(item)
    }
  }
  g.append('g')
    .selectAll('text')
    .data(textDatas)
    .join('text')
    .attr('style', 'font-size:15px; fill: red;')
    .attr('class', 'mytext')
    .html((d, i) => {
      const texts = (d?.value || '').split('')
      return texts
        .map((text, i) => {
          return `<tspan dx="${
            i === 1 ? -15 : i === 0 ? 0 : Number(text) ? -8 : -15
          }" dy="${20}">${text}</tspan>`
        })
        .join('')
    })
    .attr(
      'x',
      (d, i) => { return viewConfig.step + d.index * viewConfig.micoStep + textLeftMargin - 2 }
    )
    .attr('y', () => {
      return viewConfig.topKeysPos - textLeftMargin
    })
}
// 绘制特殊事件文字 --- 无时间
export function drawSpecialNoTimeText(svg, viewConfig, textData) {
  const g = getG(svg, viewConfig)
  const textDatas = []
  if (textData.length > 0) {
    for (let j = 0; j < textData.length; j++) {
      let item = {}
      if (textData[j].value) {
        item = {
          index: textData[j].index,
          value: textData[j].value.split(',')[0],
          date: textData[j].date
        }
      } else {
        item = textData[j]
      }
      textDatas.push(item)
    }
  }
  g.append('g')
    .selectAll('text')
    .data(textDatas)
    .join('text')
    .attr('style', 'font-size:15px; fill: red;')
    .attr('class', 'mytext')
    .html((d, i) => {
      const texts = (d?.value || '').split('')
      // let time = ''
      // if (t.length > 1) {
      //   time = t[0] + '㇑' + transNum(t[1].split(':')[0]) + '时' + transNum(t[1].split(':')[1]) + '分'
      // }
      // const texts = time.split('')
      return texts
        .map((text, i) => {
          return `<tspan dx="${
            i === 1 ? -15 : i === 0 ? 0 : Number(text) ? -8 : -15
          }" dy="${ i === 0 ? 20 : 80}">${text}</tspan>`
        })
        .join('')
    })
    .attr(
      'x',
      (d, i) => { return viewConfig.step + d.index * viewConfig.micoStep + textLeftMargin - 2 }
    )
    .attr('y', () => {
      return viewConfig.topKeysPos - textLeftMargin
    })
}

// 数字转换成文字
function transNum(num) {
  const arr1 = ['', '十']
  const arr2 = ['零', '一', '二', '三', '四', '五', '六', '七', '八', '九']
  const sw = num.split('')[0]
  const gw = num.split('')[1]
  const str1 = sw > 1 ? arr2[sw] + '十' : arr1[sw]
  let strNum = str1 + arr2[gw]
  // 若H为十点整小时，则把零去掉，若m为十分,则去掉零
  if (strNum.indexOf('十') > 0 || strNum.indexOf('零') === 1) {
    strNum = strNum.replace('零', '')
  }
  return strNum
}

// 绘制标签文字
export function drawTagText(svg, viewConfig, textData) {
  const g = getG(svg, viewConfig)
  g.append('g')
    .selectAll('text')
    .data(textData)
    .join('text')
    .attr('style', 'font-size:14px; fill: black;')
    .attr('class', 'mytext')
    .html((d, i) => {
      const texts = (d?.value || '').split('')
      return texts
        .map((text, i) => {
          return `<tspan dx="${i === 1 ? -14 : i === 0 ? 0 : Number(text) ? -8 : -14}" dy="${20}">${text}</tspan>`
        })
        .join('')
    })
    .attr(
      'x',
      (d, i) => viewConfig.step + d.index * viewConfig.micoStep + textLeftMargin
    )
    .attr('y', () => {
      return viewConfig.topKeysPos - textLeftMargin + 198
    })
}

// 绘制特殊事件文字底部
export function drawBottomSpecialText(svg, viewConfig, textData) {
  const g = getG(svg, viewConfig)
  g.append('g')
    .selectAll('text')
    .data(textData)
    .join('text')
    .attr('style', 'font-size:14px; fill: blue;')
    .attr('class', 'mytext')
    .html((d, i) => {
      const texts = (d?.value || '').split('')
      return texts
        .map((text, i) => {
          return `<tspan dx="${
            i === 1 ? -14 : i === 0 ? 0 : Number(text) ? -8 : -14
          }" dy="${20}">${text}</tspan>`
        })
        .join('')
    })
    .attr(
      'x',
      (d, i) => viewConfig.step + d.index * viewConfig.micoStep + textLeftMargin
    )
    .attr('y', (d) => {
      const texts = (d?.value || '').split('')
      return viewConfig.bottomKeysPosStart - texts.length * LINE_HEIGHT - 6 - 58
    })
}

export function initArrow(svg) {
  const arrowMarkerRed = svg
    .append('defs')
    .append('marker')
    .attr('id', 'redArrow')
    .attr('markerUnits', 'strokeWidth')
    .attr('markerWidth', '12')
    .attr('markerHeight', '12')
    .attr('viewBox', '0 0 12 12')
    .attr('refX', '6')
    .attr('refY', '6')
    .attr('orient', 'auto')
  const arrowPath = 'M2,2 L10,6 L2,10 L6,6 L2,2'
  arrowMarkerRed
    .append('path')
    .attr('d', arrowPath)
    .attr('fill', 'red')
  const arrowMarkerBlue = svg
    .append('defs')
    .append('marker')
    .attr('id', 'blueArrow')
    .attr('markerUnits', 'strokeWidth')
    .attr('markerWidth', '12')
    .attr('markerHeight', '12')
    .attr('viewBox', '0 0 12 12')
    .attr('refX', '6')
    .attr('refY', '6')
    .attr('orient', 'auto')
  arrowMarkerBlue
    .append('path')
    .attr('d', arrowPath)
    .attr('fill', 'blue')
}

export function lineArrow({ svg, viewConfig, scale, data } = {}) {
  const g = getG(svg, viewConfig)
  const vaildData = data.filter((item) => item)
  // {
  //   ...item,
  //   value: ismax ? maxDefault : minDefault,
  //   sourceValue: item,
  //   ismax: ismax ,
  //   max,
  //   min
  // }
  // 绘制直线
  g.selectAll('line')
    .data(vaildData)
    .join('line')
    .attr('x1', (d, i) => {
      return scale(d.index) + +viewConfig.X_OFFSET
    })
    .attr('y1', (d, i) => {
      if (d.ismax) {
        // 顶部箭头
        return viewConfig.topKeysPos + symbolArrowHeight * 1.5
      } else {
        // 底部箭头
        return viewConfig.bottomKeysPosStart - symbolArrowHeight * 1.5
      }
    })
    .attr('x2', (d, i) => {
      return scale(d.index) + +viewConfig.X_OFFSET
    })
    .attr('y2', (d, i) => {
      if (d.ismax) {
        // 顶部箭头
        return viewConfig.topKeysPos + symbolArrowHeight / 2
      } else {
        // 底部箭头
        return viewConfig.bottomKeysPosStart - symbolArrowHeight / 2
      }
    })
    .attr('stroke', (d) => {
      return d.ismax ? 'red' : 'blue'
    })
    .attr('stroke-width', 2)
    .attr('marker-end', (d, i) => {
      return d.ismax ? 'url(#redArrow)' : 'url(#blueArrow)'
    })
  g.selectAll('text')
    .data(vaildData)
    .join('text')
    .attr('class', 'desctextname')
    .html((item, i) => {
      const value = `${item.sourceValue}`.split('')
      const dotIndex = value.indexOf('.')
      return value
        .map((d, index) => {
          let y
          let multiple = index
          let addition = 0 // dot height
          if (item.ismax) {
            if (index >= dotIndex) {
              addition = 8
              multiple = index - 1
            }
            y =
              viewConfig.topKeysPos +
              symbolArrowHeight * 1.5 +
              (2 + multiple * 1.5) * viewConfig.X_OFFSET +
              addition
          } else {
            if (index >= dotIndex) {
              addition = 9
              multiple = index - 1
            }
            y =
              viewConfig.bottomKeysPosStart -
              (2 + (value.length - multiple) * 1.5) * viewConfig.X_OFFSET +
              addition
          }
          return `
              <tspan rotate="90" x="${scale(item.index) +
                viewConfig.X_OFFSET -
                4}"
              y="${y}">${d}</tspan>`
        })
        .join('')
    })
    .attr('x', (item, i) => {
      return scale(item.index) + viewConfig.X_OFFSET - 4
    })
    .attr('y', item => {
      if (item.ismax) {
        return (
          viewConfig.topKeysPos + symbolArrowHeight * 1.5 + LINE_HEIGHT / 2
        )
      } else {
        return (
          viewConfig.bottomKeysPosStart -
          symbolArrowHeight / 2 -
          LINE_HEIGHT / 2
        )
      }
    })
    .attr('style', item => {
      return `font-size:14px;fill:${
        item.ismax ? 'red' : 'blue'
      };font-weight: bold;`
    })
}

