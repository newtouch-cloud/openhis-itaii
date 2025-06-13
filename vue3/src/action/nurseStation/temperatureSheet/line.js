import * as d3 from 'd3'
import { iconDrawObj, getG, getData, drawTopMask, drawBottomMask, initArrow, lineArrow, drawSpecialText, drawTagText, drawBottomSpecialText, drawSpecialNoTimeText, 
  drawSurgery,
} from './drawfn'
import { getMaxList } from './dataProcess'
import {
  timeNumber,
  nightTime,
  leftTEXT,
  showPain,
  painTEXT,
  bodyTemperature,
  painScore,
  starNumEnv,
  endNumEnv,
  heartRange,
  INFO_KEYS,
  Header,
  TOP_KEYS,
  BOTTOM_KEYS,
  HEAD_HEIGHT,
  LINE_HEIGHT,
  textLeftMargin,
  TEXT_MARGIN_BOTTOM
} from './config'
import ViewConfig from './ViewConfig'
export async function init(data = []) {
  const ajaxData = getData(data)
  const chart = ConnectedScatterplot({
    x: (d) => d.year,
    y: (d) => d.gas,
    xType: d3.scaleBand,
    // yDomain: [starNumEnv1, starNumEnv2, endNumEnv1, endNumEnv2],
    yDomain: [starNumEnv, endNumEnv],
    width: 1080,
    height: 1290 + BOTTOM_KEYS.length * 20 + 7,
    marginLeft: 10,
    marginRight: 100,
    marginTop: 10,
    marginBottom: 80,
    duration: 5000, // for the intro animation; 0 to disable
    renderData: ajaxData
  })
  await nextTick() // 等待DOM更新
  if (document.getElementById('my_dataviz').children.length > 0) {
    document.getElementById('my_dataviz').removeChild(document.getElementById('my_dataviz').children[0])
  }
  document.getElementById('my_dataviz').appendChild(chart)
}
function ConnectedScatterplot(options) {
  // 计算出各种位置常量
  const viewConfig = new ViewConfig(options)
  let tooltip
  const svg = d3
    .create('svg')
    .attr('id', 'printsvg')
    .attr('width', viewConfig.width)
    .attr('height', viewConfig.height)
    .attr('viewBox', [0, 0, viewConfig.width, viewConfig.height])
    .attr('style', 'height: auto; height: intrinsic;')
    .attr('transform', `translate(0,${viewConfig.marginTop})`)

  setTimeout(() => {
    tooltip = svg.append('g').style('pointer-events', 'none')
  })
  const xScale = d3
    .scaleBand()
    .domain(d3.range(42))
    .rangeRound(viewConfig.xRange)
  // 体温的 scale
  const bodyScale = d3.scaleLinear(
    [bodyTemperature[0], bodyTemperature[1]],
    viewConfig.yRange
  )
  // const y2Scale = d3.scaleLinear()
  //   .domain([94, 107]) // 新的 y 轴范围
  //   .range([viewConfig.height, 0]) // SVG 高度，0 表示顶端
  // 心率的 Scale
  const heartScale = d3.scaleLinear(
    [heartRange[0], heartRange[1]], // [20, 180];
    viewConfig.yRange // [560,60];
  )
  // 心率上限
  const bodyOverflowData = getMaxList({
    list: viewConfig.renderData.datasetHeartRate.flat(Infinity),
    max: heartRange[1],
    min: heartRange[0],
    maxDefault: 180,
    minDefault: 40
  })
  // 脉搏上限
  const datasetPulse = getMaxList({
    list: viewConfig.renderData.datasetPulse.flat(Infinity),
    max: heartRange[1],
    min: heartRange[0],
    maxDefault: 180,
    minDefault: 40
  })
   //口温下限
   const bodyData = getMaxList({
     list: viewConfig.renderData.bodyData,
     max: bodyTemperature[1],
    min: bodyTemperature[0],
     maxDefault: 42,
     minDefault: 35
   });
  // 腋温下限
  const datasetAnus = getMaxList({
    list: viewConfig.renderData.datasetAnus,
    max: bodyTemperature[1],
    min: bodyTemperature[0],
    maxDefault: 42,
    minDefault: 35
  })
  // 肛温下限
  const datasetHeartrate = getMaxList({
    list: viewConfig.renderData.datasetHeartrate,
    max: bodyTemperature[1],
    min: bodyTemperature[0],
    maxDefault: 42,
    minDefault: 35
  })

  // 耳温下限
  const earCool = getMaxList({
    list: viewConfig.renderData.earCool,
    max: bodyTemperature[1],
    min: bodyTemperature[0],
    maxDefault: 42,
    minDefault: 35
  })
  const allTemperature = [bodyOverflowData, datasetPulse, bodyData, datasetAnus, datasetHeartrate, earCool]
  // viewConfig.renderData.bodyData = levelingData({
  //   list: viewConfig.renderData.bodyData,
  //   maxDefault: 43,
  //   minDefault: 33
  // })

  initArrow(svg)
  // ==========================
  //      开始调用绘制函数
  // ==========================
  // 1、绘制竖线格子
  // 2、绘制折线图
  // 3、绘制上下的字段属性（遮挡住超出的线条）【需要控制一个上下限，不超过遮罩层，体温【43°,】】
  // 4、绘制被遮挡的竖线格子
  // 一定要按照循序调用
  // 绘制脉搏体温
  drwaPulse(svg)
  // 绘制折线区域格子
  drawbgLine(svg)
  // 绘制红色竖线
  drwaVerticallLine(svg)
  // 绘制折线
  viewConfig.renderData.brokenLineData.forEach(item => {
    brokenLine(svg, item)
  })
  const tmpList = getIndexValue(
    viewConfig.renderData.bodyData,
    viewConfig.renderData.datasetAnus,
    viewConfig.renderData.datasetHeartrate,
    viewConfig.renderData.earCool
  )
  // 绘制脉搏线条
  viewConfig.renderData.datasetPulse.forEach((item) => {
    const item2 = []
    item.forEach((x) => {
      // const obj = tmpList.find((o) => o.index === x.index && o.value === heartScale(x.value))
      const obj = tmpList.find((o) => o.index === x.index && Math.abs(o.value - heartScale(x.value)) < 1)
      if (!obj) {
        item2.push(x)
      }
    })
    drawHeart(svg, item, true)
    drawHeart(svg, item2, false)
  })
  // 绘制心率线条
  viewConfig.renderData.datasetHeartRate.forEach((item) => {
    drawHeartRate(svg, item)
  })
  // // 绘制口温线条
  // drawPathBody(svg, viewConfig.renderData.bodyData)
  // 绘制腋温线条
  drawAnus(svg, viewConfig.renderData.bodyData)
  // // 绘制肛温线条
  // drawJuhua(svg, viewConfig.renderData.datasetHeartrate)
  // // 绘制耳温
  // thermometer(svg, viewConfig.renderData.earCool)
  // 绘制疼痛
  // if (showPain) {
  //   viewConfig.renderData.painScore.forEach(item => {
  //     drawPain(svg, item);
  //   });
  // }
  // 需要放在绘制折线后，起到遮挡折线的作用
  // 绘制呼吸格子
  drwaBreathing(svg, viewConfig.renderData.datasetPain.filter(x => x.value !== null))
  // 需要放在绘制呼吸后，绘制下划线
  // 绘制底部横线和值
  // ===================== //
  // 遮罩层挡住超出的折线     //
  // =====================//
  drawTopMask(svg, viewConfig)
  drawBottomMask(svg, viewConfig)
  // 绘制底部线条
  drwaBottomLine(svg)
  drwaBottomLineData(svg)
  // 绘制顶部值
  drwaTopData(svg)
  // 绘制特殊事件文字---有时间
  if (viewConfig.renderData.otherArr.length > 0) {
    for (let j = 0; j < viewConfig.renderData.otherArr.length; j++) {
      drawSpecialText(svg, viewConfig, viewConfig.renderData.otherArr[j])
    }
  }
  
  drawSurgery(svg, viewConfig, viewConfig.renderData.minSurgeryArr)
  drawSurgery(svg, viewConfig, viewConfig.renderData.surgeryArr)
  drawSpecialNoTimeText(svg, viewConfig, viewConfig.renderData.temArr)
  //drawSpecialNoTimeText(svg, viewConfig, viewConfig.renderData.smallSurgerytArr)
  //drawSpecialNoTimeText(svg, viewConfig, viewConfig.renderData.dischargeArr)
 // drawSpecialNoTimeText(svg, viewConfig, viewConfig.renderData.transferArr)
  // 绘制标签文字
 // drawTagText(svg, viewConfig, viewConfig.renderData.symbolContent)
  // drawBottomSpecialText(svg, viewConfig, viewConfig.renderData.symbolDegreesOnline);
  drawBottomSpecialText(svg, viewConfig, viewConfig.renderData.symbolGoUp)
  drawBottomSpecialText(svg, viewConfig, viewConfig.renderData.symbolDegreesEvents)
  // 绘制降温的红圈和虚线
  drawCoolBody(svg, viewConfig.renderData.dataCool, viewConfig.renderData.allTemperatureData)
  // 绘制同一时间心率和脉搏的阴影
  drawRateBody(svg, viewConfig.renderData.datasetPulse, viewConfig.renderData.datasetHeartRate)
  drwaOtherInfo(svg, viewConfig.renderData.infoData)
  // 最后一步
  // 绘制四周加粗边框
  drwaTabelBorder(svg)
  // ========== //
  // 最后展示浮层的内容
  // ========== //
  allTemperature.forEach((item) => {
    lineArrow({
      svg,
      viewConfig,
      scale: xScale,
      data: item
    })
  })

  // ==========================
  //      结束调用绘制函数
  // ==========================

  return svg.node()
  // 绘制最外面边框
  function drwaTabelBorder(svg) {
    const g = svg
      .append('g')
      .attr(
        'transform',
        `translate(${viewConfig.marginLeft},${viewConfig.topPos})`
      )

    g.append('rect')
      .attr('x', 0)
      .attr('y', 0)
      .attr('width', viewConfig.contentWidth)
      .attr('height', viewConfig.tableHeight)
      .attr('stroke', viewConfig.stroke)
      .attr('fill', 'none')
      .attr('style', 'stroke-width: 4')
  }
  // 绘制所有竖线
  function drwaVerticallLine(svg) {
    const g = getG(svg, viewConfig)
    let start = viewConfig.step
    g.attr('class', 'maskline2')
    while (start < viewConfig.contentWidth) {
      // const isLastLine = (start + viewConfig.step) >= viewConfig.contentWidth
      g.append('line')
        .attr('x1', start)
        .attr('y1', 0)
        .attr('y2', viewConfig.tableHeight)
        .attr('x2', start)
        .attr('stroke', start > viewConfig.step ? '#B22222' : viewConfig.stroke)
        // .attr('stroke', isLastLine ? 'black' : (start > viewConfig.step ? '#B22222' : viewConfig.stroke))
        .attr('stroke-width', 4) // 设置线条宽度为2
      start = start + viewConfig.step
    }
  }
  // 绘制底部的线条
  function drwaBottomLine(svg) {
    // 补上下竖线
    let start = viewConfig.step
    const lineG = getG(svg, viewConfig)
      .append('g')
      .attr('class', 'maskline')
    const topPos = viewConfig.bottomKeysPosStart + 40
    while (start < viewConfig.contentWidth) {
      BOTTOM_KEYS.forEach((item, i) => {
        const step = viewConfig.step / item.times
        let itemStart = start
        for (let j = 0; j < 6; j++) {
          if (((j * viewConfig.step) / 6) % step === 0) {
            lineG
              .append('line')
              .attr('fill', 'stroke')
              .attr('x1', itemStart)
              .attr('y1', topPos + i * LINE_HEIGHT)
              .attr('y2', topPos + (i + 1) * LINE_HEIGHT)
              .attr('x2', itemStart)
              .attr('stroke', viewConfig.stroke)
          }
          itemStart += viewConfig.step / 6
        }
      })
      start += viewConfig.step
    }
  }
    // 绘制底部的线条和文字
    function drwaBottomLineData(svg) {
      const g = getG(svg, viewConfig)
      // 绘制底部的横向线条
      g.selectAll('line')
        .data([...BOTTOM_KEYS])
        .join('line')
        .attr('x1', 0)
        .attr('y1', (d, i) => {
          const isLast = i === BOTTOM_KEYS.length - 1;
          return viewConfig.bottomKeysPosStart + (i + 2) * LINE_HEIGHT + (isLast ? 15 : 0);
          // return viewConfig.bottomKeysPosStart + (i + 2) * LINE_HEIGHT // 2是呼吸，占了2行
        })
        .attr('y2', (d, i) => {
          const isLast = i === BOTTOM_KEYS.length - 1;
    return viewConfig.bottomKeysPosStart + (i + 2) * LINE_HEIGHT + (isLast ? 15 : 0)
          // return viewConfig.bottomKeysPosStart + (i + 2) * LINE_HEIGHT
        })
        .attr(
          'x2',
          viewConfig.width - viewConfig.marginLeft - viewConfig.marginRight
        )
        .attr('fill', 'none')
        .attr('class', 'dataLine')
        .attr('stroke', viewConfig.stroke)
        .attr('stroke-width', 1)
        .attr('stroke-linejoin', viewConfig.strokeLinejoin)
        .attr('stroke-linecap', viewConfig.strokeLinecap)
      const textArr = BOTTOM_KEYS
      const repeatArr = d3.range(8)
      // 绘制文字
      textArr.map(({ key, name, times }, index) => {
        if (key !== '100') {
          if (times && times === 2) {
            let str = []
            g.append('g')
              .selectAll('text')
              .data(repeatArr)
              .join('text')
              .attr('style', 'font-size:15px; font-weight:bold;fill: black')
              .attr('class', 'mytext')
              .text((i) => {
                if (i == 0) {
                  return name
                } else {
                  const text = getTypeValue(key, viewConfig.renderData.typesData)[i - 1]?.typeValue
                  if (text) {
                    str = text.split(',')
                    if (str.length > 1) {
                      return str[0]
                    }
                  }
                  return text
                }
              })
              .attr('x', (i) => {
                const pos = getTypeValue(key, viewConfig.renderData.typesData)[i - 1]?.index + 1
                if (i === 0) {
                  return textLeftMargin * 2
                }
                return pos * viewConfig.step + textLeftMargin
              })
              .attr('y', () => {
                return viewConfig.bottomKeysPosStart + (index + 3) * LINE_HEIGHT - TEXT_MARGIN_BOTTOM
              })
            g.append('g')
              .selectAll('text')
              .data(repeatArr)
              .join('text')
              .attr('style', 'font-size:15px; font-weight:bold;fill: black')
              .attr('class', 'mytext')
              .text((i) => {
                if (i == 0) {
                  return ''
                } else {
                  const text = getTypeValue(key, viewConfig.renderData.typesData)[i - 1]?.typeValue
                  if (text) {
                    str = text.split(',')
                    if (str.length > 1) {
                      return str[1]
                    }
                  }
                  return ''
                }
              })
              .attr('x', (i) => {
                const pos = getTypeValue(key, viewConfig.renderData.typesData)[i - 1]?.index + 1
                if (i === 0) {
                  // return textLeftMargin * 2
                }
                return pos * viewConfig.step + textLeftMargin + 60
              })
              .attr('y', () => {
                return viewConfig.bottomKeysPosStart + (index + 3) * LINE_HEIGHT - TEXT_MARGIN_BOTTOM
              })
          }
          else if (key === '017') {
            for (let j = 0; j < 8; j++) {
              const testText = getTypeValue(key, viewConfig.renderData.typesData)[j - 1]?.typeValue
              if (j === 0){
                g.append('g')
                  .selectAll('text')
                  .data(repeatArr)
                  .join('text')
                  .attr('style', 'font-size:15px;')
                  .attr('class', 'mytext')
                  .text(name)
                  .attr('x', () => {
                    return textLeftMargin * 2 + 10
                  })
                  .attr('y', () => {
                    return viewConfig.bottomKeysPosStart + (index + 3) * LINE_HEIGHT - TEXT_MARGIN_BOTTOM + 1
                  })
              }
              else if (testText !== undefined){
                if(testText.length > 8){
                  for (let i = 0; i < testText.length; i += 8) {
                    // 使用slice方法获取当前段的字符串
                    const line = testText.slice(i, i + 8);
                    g.append('g')
                      .selectAll('text')
                      .data(repeatArr)
                      .join('text')
                      .attr('style', 'font-size:14px')
                      .attr('class', 'mytext')
                      .text(line)
                      .attr('x', () => {
                        const pos = getTypeValue(key, viewConfig.renderData.typesData)[j - 1]?.index + 1
                        
                        return pos * viewConfig.step + textLeftMargin
                      })
                      .attr('y', () => {
                        return viewConfig.bottomKeysPosStart + (index + 3) * LINE_HEIGHT - TEXT_MARGIN_BOTTOM + i*2.5
                      })
                  }
                }else{
                  g.append('g')
                      .selectAll('text')
                      .data(repeatArr)
                      .join('text')
                      .attr('style', 'font-size:14px;')
                      .attr('class', 'mytext')
                      .text(testText)
                      .attr('x', () => {
                        const pos = getTypeValue(key, viewConfig.renderData.typesData)[j - 1]?.index + 1
                        
                        return pos * viewConfig.step + textLeftMargin
                      })
                      .attr('y', () => {
                        return viewConfig.bottomKeysPosStart + (index + 3) * LINE_HEIGHT - TEXT_MARGIN_BOTTOM
                      })
                }
              }
            }
          } 
          else {
          g.append('g')
            .selectAll('text')
            .data(repeatArr)
            .join('text')
            // .attr('style', 'font-size:15px; font-weight:bold')
            .attr('style', (i, d) => { // 使用回调函数来设置样式
              let textContent = (i === 0) ? name : getTypeValue(key, viewConfig.renderData.typesData)[i - 1]?.typeValue;
              let fontSize = '15px'; // 默认字体大小margin-left: 15px
              
              // 检查条件并设置不同的字体大小
              if (key === '005' && textContent === '*') {
                fontSize = '30px'; // 或者你想要的任何特定大小
              }
              
              // 构建并返回样式字符串
              return `font-size: ${fontSize};font-weight:bold;`; // 注意这里只设置了字体大小，其他样式未受影响
            })
            .attr('class', 'mytext')
            .text((i) => {
              if (i === 0) {
                return name
              } else {
                let textValue = getTypeValue(key, viewConfig.renderData.typesData)[i - 1]?.typeValue
                return textValue
              }
            })
            .attr('x', (i) => {
              let nameLocation = textLeftMargin * 2 + 10
              if (i === 0) {
                if(key === '006'){
                  nameLocation = textLeftMargin * 2 - 3
                }
                return nameLocation
              }
              const pos = getTypeValue(key, viewConfig.renderData.typesData)[i - 1]?.index + 1
              const typesData = getTypeValue(key, viewConfig.renderData.typesData)[i-1]
              if (typesData) {
                  const firstNameLength = typesData.typeValue.length
                  if (firstNameLength === 1) {
                    return pos * viewConfig.step + textLeftMargin + 50
                  } else if (firstNameLength === 2) {
                    return pos * viewConfig.step + textLeftMargin + 46
                  } else if (firstNameLength === 3) {
                    return pos * viewConfig.step + textLeftMargin + 43
                  } else if (firstNameLength === 4) {
                    if (typesData.typeCode === '010') {
                      return pos * viewConfig.step + textLeftMargin + 45
                    } else {
                      return pos * viewConfig.step + textLeftMargin + 40
                    }
                  } else if (firstNameLength === 5) {
                    return pos * viewConfig.step + textLeftMargin + 38
                  } else if (firstNameLength === 6) {
                    return pos * viewConfig.step + textLeftMargin + 32
                  } else if (firstNameLength > 10) {
                    return pos * viewConfig.step + textLeftMargin
                  } else {
                    return pos * viewConfig.step + textLeftMargin + 32
                  }
              } else {
                return pos * viewConfig.step + textLeftMargin + 30
              }
            })
            .attr('y', (i) => {
              const daBianValue = getTypeValue(key, viewConfig.renderData.typesData)[i - 1]?.typeValue
              let textHeight = viewConfig.bottomKeysPosStart + (index + 3) * LINE_HEIGHT - TEXT_MARGIN_BOTTOM
              if(key === '005' && daBianValue === '*'){
                textHeight = textHeight + 12
              }

              return textHeight
            })
            // .attr('y', () => {
            //   return (
            //     viewConfig.bottomKeysPosStart + (index + 3) * LINE_HEIGHT - TEXT_MARGIN_BOTTOM + 1
            //   )
            // })
        }
      }
      })
    }
  // 绘制底部的线条和文字
  // function drwaBottomLineData(svg) {
  //   const g = getG(svg, viewConfig)
  //   // 绘制底部的横向线条
  //   g.selectAll('line')
  //     .data([...BOTTOM_KEYS])
  //     .join('line')
  //     .attr('x1', 0)
  //     .attr('y1', (d, i) => {
  //       return viewConfig.bottomKeysPosStart + (i + 2) * LINE_HEIGHT // 2是呼吸，占了2行
  //     })
  //     .attr('y2', (d, i) => {
  //       return viewConfig.bottomKeysPosStart + (i + 2) * LINE_HEIGHT
  //     })
  //     .attr('x2', viewConfig.width - viewConfig.marginLeft - viewConfig.marginRight)
  //     .attr('fill', 'none')
  //     .attr('class', 'dataLine')
  //     .attr('stroke', viewConfig.stroke)
  //     .attr('stroke-width', 1)
  //     .attr('stroke-linejoin', viewConfig.strokeLinejoin)
  //     .attr('stroke-linecap', viewConfig.strokeLinecap)
  //   const textArr = BOTTOM_KEYS
  //   const repeatArr = d3.range(8)
  //   // 绘制文字
  //   textArr.map(({ key, name, times }, index) => {
  //     // 绘制血压
  //     if (times && times === 2) {
  //       let str = []
  //       g.append('g')
  //         .selectAll('text')
  //         .data(repeatArr)
  //         .join('text')
  //         .attr('style', 'font-size:14px')
  //         .attr('class', 'mytext')
  //         .text((i) => {
  //           if (i === 0) {
  //             return name
  //           } else {
  //             const text = getTypeValue(key, viewConfig.renderData.typesData)[i - 1]?.typeValue
  //             if (text) {
  //               str = text.split(',')
  //               if (str.length > 1) {
  //                 return str[0]
  //               }
  //             }
  //             return text
  //           }
  //         })
  //         .attr('x', (i) => {
  //           const pos = getTypeValue(key, viewConfig.renderData.typesData)[i - 1]?.index + 1
  //           if (i === 0) {
  //             return textLeftMargin * 2
  //           }
  //           return pos * viewConfig.step + textLeftMargin
  //         })
  //         .attr('y', () => {
  //           return viewConfig.bottomKeysPosStart + (index + 3) * LINE_HEIGHT - TEXT_MARGIN_BOTTOM
  //         })
  //       g.append('g')
  //         .selectAll('text')
  //         .data(repeatArr)
  //         .join('text')
  //         .attr('style', 'font-size:14px; margin-left: 5px')
  //         .attr('class', 'mytext')
  //         .text((i) => {
  //           if (i === 0) {
  //             return name
  //           } else {
  //             const text = getTypeValue(key, viewConfig.renderData.typesData)[i - 1]?.typeValue
  //             if (text) {
  //               str = text.split(',')
  //               if (str.length > 1) {
  //                 return str[1]
  //               }
  //             }
  //             return ''
  //           }
  //         })
  //         .attr('x', (i) => {
  //           const pos = getTypeValue(key, viewConfig.renderData.typesData)[i - 1]?.index + 1
  //           if (i === 0) {
  //             return textLeftMargin * 2
  //           }
  //           return pos * viewConfig.step + textLeftMargin + 60
  //         })
  //         .attr('y', () => {
  //           return viewConfig.bottomKeysPosStart + (index + 3) * LINE_HEIGHT - TEXT_MARGIN_BOTTOM
  //         })
  //     } else {
  //       g.append('g')
  //         .selectAll('text')
  //         .data(repeatArr)
  //         .join('text')
          
  //         // .attr('style', 'font-size:14px')
          // .attr('style', (i, d) => { // 使用回调函数来设置样式
          //   let textContent = (i === 0) ? name : getTypeValue(key, viewConfig.renderData.typesData)[i - 1]?.typeValue;
          //   let fontSize = '14px'; // 默认字体大小margin-left: 5px
            
          //   // 检查条件并设置不同的字体大小
          //   if (key === '005' && textContent === '*') {
          //     fontSize = '30px'; // 或者你想要的任何特定大小
          //   }
            
          //   // 构建并返回样式字符串
          //   return `font-size: ${fontSize};`; // 注意这里只设置了字体大小，其他样式未受影响
          // })
  //         .attr('class', 'mytext')
  //         .text((i) => {
  //           if (i === 0) {
  //             return name
  //           } else {
  //             return getTypeValue(key, viewConfig.renderData.typesData)[i - 1]?.typeValue
  //           }
  //         })
  //         .attr('x', (i) => {
  //           const pos = getTypeValue(key, viewConfig.renderData.typesData)[i - 1]?.index + 1
  //           if (i === 0) {
  //             return textLeftMargin * 2
  //           }
  //           return pos * viewConfig.step + textLeftMargin
  //         })
  //         .attr('y', (i) => {
  //           const daBianValue = getTypeValue(key, viewConfig.renderData.typesData)[i - 1]?.typeValue
  //           let textHeight = viewConfig.bottomKeysPosStart + (index + 3) * LINE_HEIGHT - TEXT_MARGIN_BOTTOM
  //           if(key === '005' && daBianValue === '*'){
  //             textHeight = textHeight + 12
  //           }
  //           return textHeight
  //         })
  //     }
  //   })
  // }
  // 绘制呼吸
  function drwaBreathing(svg, breathData) {
    const g = getG(svg, viewConfig)
    // 遮罩层挡住超出的折线
    g.append('rect')
      .attr('class', 'mask-rect')
      .attr('x', 0)
      .attr('y', viewConfig.bottomKeysPosStart)
      .attr(
        'width',
        viewConfig.width - viewConfig.marginRight - viewConfig.marginLeft
      )
      .attr('height', viewConfig.micoStep * 2 - 1)
      .attr('stroke', viewConfig.stroke)
      .attr('fill', '#fff')
      .attr('style', 'stroke-width: 0')
    // 绘制横线
    g.append('line')
      .attr('x1', 0)
      .attr('y1', viewConfig.bottomKeysPosStart)
      .attr('y2', viewConfig.bottomKeysPosStart)
      .attr(
        'x2',
        viewConfig.width - viewConfig.marginLeft - viewConfig.marginRight
      )
      .attr('fill', 'none')
      .attr('class', 'dataLine')
      .attr('stroke', viewConfig.stroke)
      .attr('stroke-width', 1)
      .attr('stroke-linejoin', viewConfig.strokeLinejoin)
      .attr('stroke-linecap', viewConfig.strokeLinecap)
    const textYPos =
      viewConfig.bottomKeysPosStart + LINE_HEIGHT + TEXT_MARGIN_BOTTOM
    // 绘制汉字
    g.append('text')
      .attr('style', 'font-size:15px;font-weight:bold')
      .text('呼    吸(次/分)')
      .attr('x', textLeftMargin * 2)
      .attr('y', textYPos)
    // 绘制数据
    const data = d3.range(42)
    g.append('g')
      .selectAll('text')
      .data(data)
      .join('text')
      .attr('style', 'font-size:14px')
      .attr('class', 'mytext')
      .text((d) => {
        return breathData[d]?.value
      })
      .attr('x', (i) => {
        const index = breathData[i]?.index
        return viewConfig.step + index * viewConfig.micoStep + 3
      })
      .attr('y', (i) => {
        if (i % 2) {
          return textYPos + 10
        } else {
          return textYPos - 10
        }
      })
    // 绘制呼吸竖线
    g.append('g')
      .selectAll('line')
      .data(data)
      .join('line')
      .attr('x1', (d, i) => {
        return viewConfig.step + i * viewConfig.micoStep
      })
      .attr('y1', viewConfig.bottomKeysPosStart + 2 * LINE_HEIGHT)
      .attr('x2', (d, i) => {
        return viewConfig.step + i * viewConfig.micoStep
      })
      .attr('y2', viewConfig.bottomKeysPosStart)
      .attr('fill', 'none')
      .attr('class', 'dataLine')
      .attr('stroke', viewConfig.stroke)
      .attr('stroke-width', 1)
      .attr('stroke-linejoin', viewConfig.strokeLinejoin)
      .attr('stroke-linecap', viewConfig.strokeLinecap)
  }
  // 绘制脉搏体温文字
  function drwaPulse(svg) {
    // 左侧的文字
    const g = getG(svg, viewConfig)
    g.append('line')
      .attr('class', 'slefline')
      .attr('y1', viewConfig.topKeysPos) // 这个还要修改一下
      .attr('x1', viewConfig.step / 2)
      .attr('y2', viewConfig.bottomKeysPosStart)
      .attr('x2', viewConfig.step / 2)
      .attr('stroke', viewConfig.stroke)
    // 画刻度尺

   // for (let i = 5; i <= 70; i++) {
    //  const yPosition = viewConfig.topKeysPos + viewConfig.micoStep * 2.8 * i / 5 + 50
    //  let lineLength = 9 // 设置加长的刻度线长度
    //  if (i % 5 === 0) {
    //    lineLength = 16
    //  }
      // 画刻度线
    //  g.append('line')
     //   .attr('x1', viewConfig.step / 2) // 刻度线的x坐标
      //  .attr('y1', yPosition)
       // .attr('x2', viewConfig.step / 2 - lineLength) // 刻度线的结束坐标
      //  .attr('y2', yPosition)
      //  .attr('stroke', viewConfig.stroke)
        //.attr('stroke-width', 2)
    //}

    leftTEXT.map((texts, index) => {
      g.append('g')
        .selectAll('text')
        .data(texts)
        .join('text')
        .attr('style', `font-size:14px;fill:${['red', 'blue'][index]}`)
        .attr('class', 'mytext')
        .html((d, i) => {
          if (i === 0) {
            const value = d.split(',')
            return `<tspan>${value[0]}</tspan><tspan dx="${-35 + index * 10}" dy="15">${value[1]}</tspan><tspan dx="${
              -35 + index * 10
            }" dy="15">${value[2]}</tspan>`
          }
          return `${d}`
        })
        .attr('x', (d, i) => {
          // 调整坐标文字显示位置
          if (i === 0) {
            if (index === 0) {
              return viewConfig.micoStep * Math.max(index * 4, 1) - 12
            }
            return viewConfig.micoStep * Math.max(index * 4, 1) - 6
          }
          return viewConfig.micoStep * Math.max(index * 4, 0.9)
        })
        .attr('y', (d, i) => {
          if (i === 0) {
            return viewConfig.topKeysPos + viewConfig.micoStep * 1
          }
          return viewConfig.topKeysPos + viewConfig.micoStep * 5 * i
        })
    })

    // 绘制疼痛表格
    if (showPain) {
      // 绘制疼痛分割线
      g.append('line')
        .attr('x1', 0)
        .attr('y1', viewConfig.bottomKeysPosStart - viewConfig.micoStep * 6)
        .attr('y2', viewConfig.bottomKeysPosStart - viewConfig.micoStep * 6)
        .attr('x2', viewConfig.width - viewConfig.marginLeft - viewConfig.marginRight)
        .attr('fill', 'none')
        .attr('class', 'dataLine')
        .attr('stroke', viewConfig.stroke)
        .attr('stroke-width', 1)
        .attr('stroke-linejoin', viewConfig.strokeLinejoin)
        .attr('stroke-linecap', viewConfig.strokeLinecap)

      painTEXT.map((texts, index) => {
        g.append('g')
          .selectAll('text')
          .data(texts)
          .join('text')
          .attr('style', `font-size:14px;fill:${['black', 'blue'][index]}`)
          .attr('class', 'mytext')
          .html((d, i) => {
            if (d.length > 2) {
              const value = d.split(' ')
              return `<tspan dx="10" dy="10">${value[0]}</tspan><tspan dx="${-15 + index * 10}" dy="25">${value[1]}</tspan><tspan dx="${-15 + index * 10}" dy="25">${value[2]}</tspan><tspan dx="${-15 + index * 10}" dy="25">${value[3]}</tspan>`
            } else if (i === 0) {
              return `<tspan dx="10">${d}</tspan>`
            }
            return `<tspan dx="4">${d}</tspan>`
          })
          .attr('x', (d, i) => {
            // 调整坐标文字显示位置
            if (i === 0) {
              if (index === 0) {
                return viewConfig.micoStep * Math.max(index * 4, 1) - 12
              }
              return viewConfig.micoStep * Math.max(index * 4, 1) - 6
            }
            return viewConfig.micoStep * Math.max(index * 4, 0.9)
          })
          .attr('y', (d, i) => {
            if (i === 0) {
              return viewConfig.bottomKeysPosStart - viewConfig.micoStep * 3.8 - 30
            }
            return viewConfig.bottomKeysPosStart - viewConfig.micoStep * 3.8 + viewConfig.micoStep * i - 30
          })
      })
    }
  }
  // 绘制折线的背景格子
  function drawbgLine(svg) {
    const g = getG(svg, viewConfig)
    // 绘制横线
    const horizontallength = (bodyTemperature[1] - bodyTemperature[0] + 1) * 5
    const horizontalData = [...new Array(horizontallength).keys()]
    g.append('g')
      .attr('class', 'line-content')
      .selectAll('line')
      .data(horizontalData)
      .join('line')
      // .attr('x1', viewConfig.step)
      .attr('x1', (d, i) => {
        // 判断是否是最后五条线，如果是，则减少几厘米
        if (i >= horizontalData.length - 9) {
          return viewConfig.step - 60 // 向前缩短20像素
        }
        // 判断是否是最后五条线，如果是，则减少几厘米
        if (i >= horizontalData.length - 10) {
          return viewConfig.step -120 // 向前缩短20像素
        }
        return viewConfig.step // 其他线使用默认长度
      })
      .attr('y1', (d, i) => {
        return viewConfig.topKeysPos + i * viewConfig.micoStep
      })
      .attr('y2', (d, i) => {
        return viewConfig.topKeysPos + i * viewConfig.micoStep
      })
      // .attr('x2', (d, i) => {
      //   // 判断是否是最后五条线，如果是，则减少几厘米
      //   if (i >= horizontalData.length - 10) {
      //     return viewConfig.width - viewConfig.marginLeft - viewConfig.marginRight // 向前缩短20像素
      //   }
      //   return viewConfig.width - viewConfig.marginLeft - viewConfig.marginRight // 其他线使用默认长度
      // })
      .attr('x2', viewConfig.width - viewConfig.marginLeft - viewConfig.marginRight)
      .attr('fill', 'none')
      .attr('class', 'dataLine')
      .attr('stroke', viewConfig.stroke)
      .attr('stroke-width', 1)
      .attr('stroke-linejoin', viewConfig.strokeLinejoin)
      .attr('stroke-linecap', viewConfig.strokeLinecap)
      .attr('style', (d, i) => {
        if (i === horizontalData.length - 5) {
          return 'stroke-width: 1; stroke: black;' // 不绘制最后一条线
        }
        if (i % 5 === 0 && i !== 0) {
          return 'stroke-width: 3; stroke: blue;'
        }
        return 'stroke-width: 1'
      })
    // 竖线绘制
    const verticalData = [...new Array(42).keys()]
    g.append('g')
      .selectAll('line')
      .data(verticalData)
      .join('line')
      .attr('x1', (d, i) => {
        return viewConfig.step + i * viewConfig.micoStep
      })
      .attr('y1', viewConfig.topKeysPos)
      .attr('x2', (d, i) => {
        return viewConfig.step + i * viewConfig.micoStep
      })
      .attr('y2', viewConfig.bottomKeysPosStart)
      .attr('fill', 'none')
      .attr('class', 'dataLine')
      .attr('stroke', viewConfig.stroke)
      .attr('stroke-width', 1)
      .attr('stroke-linejoin', viewConfig.strokeLinejoin)
      .attr('stroke-linecap', viewConfig.strokeLinecap)

    g.append('line')
      .attr('class', 'myline')
      .attr('x1', 0)
      .attr('y1', viewConfig.topKeysPos)
      .attr('x2', viewConfig.width - viewConfig.marginLeft - viewConfig.marginRight)
      .attr('y2', viewConfig.topKeysPos)
      .attr('stroke', viewConfig.stroke)
      .attr('stroke-width', 1)
      .attr('stroke-linejoin', viewConfig.strokeLinejoin)
      .attr('stroke-linecap', viewConfig.strokeLinecap)
  }
  // 绘制顶部数据
  function drwaTopData(svg) {
    const g = getG(svg, viewConfig)
    // 绘制横向线条
    g.selectAll('line')
      .data(TOP_KEYS)
      .join('line')
      .attr('x1', 0)
      .attr('y1', (d, i) => {
        return LINE_HEIGHT * (i + 1)
      })
      .attr('y2', (d, i) => {
        return LINE_HEIGHT * (i + 1)
      })
      .attr('x2', viewConfig.contentWidth)
      .attr('fill', 'none')
      .attr('class', 'newline')
      .attr('stroke', viewConfig.stroke)
      .attr('stroke-width', 1)
      .attr('stroke-linejoin', viewConfig.strokeLinejoin)
      .attr('stroke-linecap', viewConfig.strokeLinecap)
    // 数据的值
    const repeatArr = [...new Array(8).keys()]
    // 绘制文字
    TOP_KEYS.map(({ getValue, name }, index) => {
      g.append('g')
        .selectAll('text')
        .data(repeatArr)
        .join('text')
        .attr('style', 'font-size:14px;text-anchor:middle;')
        .attr('class', 'mytext')
        .text((i) => {
          if (i === 0) {
            return name
          } else {
            return getValue(i - 1, viewConfig.renderData)
          }
        })
        .attr('x', (i) => i * viewConfig.step + viewConfig.step / 2)
        .attr('y', () => {
          return LINE_HEIGHT * (index + 1) - TEXT_MARGIN_BOTTOM
        })
    })
    // 绘制时间字段
    // 绘制汉字
    g.append('text')
      .attr('style', 'font-size:14px;text-anchor:middle;')
      .text('时 间')
      .attr('x', viewConfig.step / 2)
      .attr('y', viewConfig.topKeysPos - TEXT_MARGIN_BOTTOM)
    // 绘制竖线和时间汉字
    const data = new Array(timeNumber.length * 7).fill('').map((d, i) => timeNumber[i % timeNumber.length])
    g.append('g')
      .selectAll('text')
      .data(data)
      .join('text')
      .attr('style', 'font-size:14px')
      .attr('class', 'mytext')
      .attr('fill', (d) => {
        if (nightTime.includes(d)) {
          return 'red'
        } else {
          return viewConfig.stroke
        }
      })
      .text((d) => {
        return d
      })
      .attr('x', (d, i) => {
        return viewConfig.step + i * viewConfig.micoStep + 3
      })
      .attr('y', (d, i) => {
        return viewConfig.topKeysPos - TEXT_MARGIN_BOTTOM
      })
    // 线条
    g.append('g')
      .attr('class', 'textYPos')
      .selectAll('line')
      .data(data)
      .join('line')
      .attr('x1', (d, i) => {
        return viewConfig.step + i * viewConfig.micoStep
      })
      .attr('y1', viewConfig.topKeysPos - LINE_HEIGHT)
      .attr('x2', (d, i) => {
        return viewConfig.step + i * viewConfig.micoStep
      })
      .attr('y2', viewConfig.topKeysPos)
      .attr('fill', 'none')
      .attr('class', 'dataLine')
      .attr('stroke', viewConfig.stroke)
      .attr('stroke-width', (d, i) => {
        return i % 6 ? 1 : 0
      })
      .attr('stroke-linejoin', viewConfig.strokeLinejoin)
      .attr('stroke-linecap', viewConfig.strokeLinecap)
  }
  // 绘制折线
  function brokenLine(svg, pathData) {
    const I = d3.map(pathData, (_, i) => i)
    const g = getG(svg, viewConfig)
    const line = d3
      .line()
      .defined((i) => pathData[i].value)
      .x((i) => {
        return xScale(pathData[i].index) + viewConfig.X_OFFSET
      })
      .y((i) => bodyScale(pathData[i].value))
    g.attr('class', 'body-line')
    getDrawPath({
      content: g,
      line: line(I.filter((i) => pathData[i].value))
    })
  }
  // 获取体温数据
  function getIndexValue(list1, list2, list3, list4) {
    let list = []
    list = list.concat(list1.filter((x) => x.value !== null))
    list = list.concat(list2.filter((x) => x.value !== null))
    list = list.concat(list3.filter((x) => x.value !== null))
    list = list.concat(list4.filter((x) => x.value !== null))
    list.sort((x, y) => x.index - y.index)
    return list.map((x) => {
      return {
        index: x.index,
        value: bodyScale(x.value)
      }
    })
  }
  // // 绘制口温
  // function drawPathBody(svg, pathData) {
  //   const g = getG(svg, viewConfig);
  //   g.on(
  //     'pointerenter pointermove',
  //     generatePointer({
  //       pathData,
  //       type: '口温',
  //       yScaleInstance: bodyScale
  //     })
  //   ).on('pointerleave', pointerleft);
  //   iconDrawObj.getDrawRoundIcon({
  //     content: g,
  //     data: d3.range(pathData.length),
  //     x: (i) => {
  //       return xScale(pathData[i].index) + viewConfig.X_OFFSET;
  //     },
  //     y: (i) => bodyScale(pathData[i].value <= 35 ? 0 : pathData[i].value),
  //     r: 3
  //   });
  // }
  // // 绘制肛温
  // function drawJuhua(svg, pathData) {
  //   const g = getG(svg, viewConfig);
  //   g.on(
  //     'pointerenter pointermove',
  //     generatePointer({
  //       pathData,
  //       type: '肛温',
  //       yScaleInstance: bodyScale
  //     })
  //   ).on('pointerleave', pointerleft);
  //   iconDrawObj.getDrawRoundDotIcon({
  //     content: g,
  //     data: d3.range(pathData.length),
  //     x: (i) => xScale(pathData[i].index) + viewConfig.X_OFFSET,
  //     y: (i) => bodyScale(pathData[i].value <= 35 ? 0 : pathData[i].value),
  //     fill: 'white',
  //     deepFill: 'blue',
  //     r: 3
  //   });
  // }
  // // 绘制 耳温
  // function thermometer(svg, pathData) {
  //   const g = getG(svg, viewConfig);
  //   g.on(
  //     'pointerenter pointermove',
  //     generatePointer({
  //       pathData,
  //       type: '耳温',
  //       yScaleInstance: bodyScale
  //     })
  //   ).on('pointerleave', pointerleft);
  //   iconDrawObj.drawThreeIcon({
  //     content: g,
  //     data: d3.range(pathData.length),
  //     x: (i) => {
  //       return xScale(pathData[i].index) + viewConfig.X_OFFSET;
  //     },
  //     y: (i) => {
  //       return bodyScale(pathData[i].value <= 35 ? 0 : pathData[i].value);
  //     },
  //     fill: 'white',
  //     riangle: 24
  //   });
  // }

  // 绘制 疼痛曲线
  function drawPain(svg, pathData) {
    const g = getG(svg, viewConfig)
    const I = d3.map(pathData, (_, i) => i)
    g.on(
      'pointerenter pointermove',
      generatePointer({
        pathData: viewConfig.renderData.painScore.flat(Infinity),
        type: '疼痛',
        yScaleInstance: heartScale
      })
    ).on('pointerleave', pointerleft)
    const line = d3
      .line()
      .defined((i) => pathData[i].value || pathData[i].value === 0)
      .x((i) => {
        return xScale(pathData[i].index) + viewConfig.X_OFFSET
      })
      .y((i) => {
        if (pathData[i].value) {
          return painScale(pathData[i].value / 10) + 10
        }
        return painScale(0) + 10
      })
    getDrawPath({
      content: g,
      line: line(I.filter((i) => pathData[i].value || pathData[i].value === 0)),
      stroke: 'black'
    })
    // iconDrawObj.getDrawRoundIcon({
    //   content: g,
    //   data: d3.range(pathData.length),
    //   x: (i) => {
    //     return xScale(pathData[i].index) + viewConfig.X_OFFSET
    //   },
    //   y: (i) => {
    //     if (pathData[i].value) {
    //       return painScale(pathData[i].value / 10) + 10
    //     }
    //     return painScale(0) + 10
    //   },
    //   // riangle: 24,
    //   fill: 'black',
    //   stroke: 'black'
    // })
    
    iconDrawObj.drawThreeIcon({
      content: g,
      data: d3.range(pathData.length),
      x: (i) => {
        return xScale(pathData[i].index) + viewConfig.X_OFFSET
      },
      y: (i) => {
        if (pathData[i].value) {
          return painScale(pathData[i].value / 10) + 10
        }
        return painScale(0) + 10
      },
      riangle: 24
    })
  }

  // 绘制脉搏
  function drawHeart(svg, pathData, isEmpty) {
    const I = d3.map(pathData, (_, i) => i)
    const g = getG(svg, viewConfig)
    g.on(
      'pointerenter pointermove',
      generatePointer({
        pathData: viewConfig.renderData.datasetPulse.flat(Infinity),
        type: '脉搏',
        yScaleInstance: heartScale
      })
    ).on('pointerleave', pointerleft)
    const line = d3
      .line()
      .defined((i) => pathData[i].value)
      .x((i) => {
        return xScale(pathData[i].index) + viewConfig.X_OFFSET
      })
      .y((i) => heartScale(pathData[i].value || 0))
    if(isEmpty){
      getDrawPath({
        content: g,
        line: line(I.filter((i) => pathData[i].value)),
        stroke: 'red'
      })
    }
    iconDrawObj.getDrawRoundIcon({
      content: g,
      data: d3.range(pathData.length),
      x: (i) => {
        return xScale(pathData[i].index) + viewConfig.X_OFFSET
      },
      y: (i) => {
        return heartScale(pathData[i].value)
      },
      fill: isEmpty ? 'transparent' : 'red',
      stroke: 'red'
    })
  }
  // 绘制心率
  function drawHeartRate(svg, pathData) {
    const I = d3.map(pathData, (_, i) => i)
    const g = getG(svg, viewConfig)
    g.on(
      'pointerenter pointermove',
      generatePointer({
        pathData: viewConfig.renderData.datasetHeartRate.flat(Infinity),
        type: '心率',
        yScaleInstance: heartScale
      })
    ).on('pointerleave', pointerleft)
    const line = d3
      .line()
      .defined((i) => pathData[i].value)
      .x((i) => {
        return xScale(pathData[i].index) + viewConfig.X_OFFSET
      })
      .y((i) => heartScale(pathData[i].value || 0))
    getDrawPath({
      content: g,
      line: line(I.filter((i) => pathData[i].value)),
      stroke: 'red'
    })

    iconDrawObj.getDrawRoundIcon({
      content: g,
      data: d3.range(pathData.length),
      x: (i) => {
        return xScale(pathData[i].index) + viewConfig.X_OFFSET
      },
      y: (i) => {
        return heartScale(pathData[i].value)
      },
      fill: 'white',
      stroke: 'red'
    })
  }
  // 腋温
  function drawAnus(svg, pathData) {
    const g = getG(svg, viewConfig)
    g.on(
      'pointerenter pointermove',
      generatePointer({
        pathData,
        type: '体温',
        yScaleInstance: bodyScale
      })
    ).on('pointerleave', pointerleft)
    iconDrawObj.getDrawXIcon({
      content: g,
      data: d3.range(pathData.length),
      x: (i) => {
        return xScale(pathData[i].index) + viewConfig.X_OFFSET
      },
      y: (i) => bodyScale(pathData[i].value ? (pathData[i].value <= 35 ? 0 : pathData[i].value) : 0)
      // style: {
      //   stroke: 'black', // 线的颜色
      //   'stroke-width': 6, // 加粗，设置合适的宽度
      //   fill: 'none' // 如果需要可以调整填充
      // }
    })
    const line = d3
      .line()
      .x((d, i) => xScale(d.index) + viewConfig.X_OFFSET)
      .y((d) => bodyScale(d.value ? (d.value <= 35 ? 0 : d.value) : 0))
      .defined((d) => d.value !== null && d.value !== undefined) // 过滤无效数据点

      g.append('path')
      .attr('class', 'axillary-line')
      .attr('d', line(pathData))
      .attr('fill', 'none')
      .attr('stroke', 'blue')
      .attr('stroke-width', 2)
      .on('pointerleave', () => tooltip.style('display', 'none'))
  }

  // 绘制一个线条
  function getDrawPath({ content, line, stroke = 'blue' }) {
    content
      .append('path')
      .attr('class', 'mylines')
      .attr('fill', 'none')
      .attr('stroke', stroke)
      .attr('stroke-width', viewConfig.strokeWidth)
      .attr('stroke-linejoin', viewConfig.strokeLinejoin)
      .attr('stroke-linecap', viewConfig.strokeLinecap)
      .attr('d', line)
  }

  // // 绘制降温的红圈和虚线
  // function drawCoolBody(svg, coolData, allData) {
  //   console.log('绘制降温的红圈和虚线', coolData, allData)
  //   const g = getG(svg, viewConfig)

  //   const lineData = [];
 
  //   // 遍历data1中的每个对象
  //   coolData.forEach(item1 => {
  //       // 如果item1有index和date属性
  //       if (item1.index !== undefined && item1.date !== undefined) {
  //           // 遍历data2中的每个对象
  //           allData.forEach(item2 => {
  //               // 如果item2也有index和date属性，并且它们与item1的相等
  //               if (item2.index === item1.index && item2.date === item1.date) {
  //                   // 将匹配的对象添加到结果数组中
  //                   // 这里可以添加整个对象，或者根据需要添加对象的某些属性
  //                   lineData.push({ ...item1, ...item2 }); // 使用展开运算符合并对象，注意这可能会覆盖相同属性的值
  //                   // 如果不希望覆盖，可以只添加item1或item2，或者创建一个新对象包含所需属性
  //               }
  //           });
  //       }
  //   });
  //   console.log('绘制降温的红圈和虚线lineData',lineData, coolData, allData)
  //   g.append('g')
  //     .selectAll('line')
  //     .data(lineData)
  //     .join('line')
  //     .attr('class', 'xuxianliane')
  //     .attr('x1', function({ index,date,value }) {
  //       return xScale(index) + viewConfig.X_OFFSET + (index - 20) / 4
  //     })
  //     .attr('y1', function({ value }) {
  //       return bodyScale(value)
  //     })
  //     .attr('x2', function({ index }) {
  //       return xScale(index) + viewConfig.X_OFFSET + (index - 20) / 4
  //     })
  //     .attr('y2', function({ index,date }) {
  //       const bodyValue = allData.filter(item => item.index === index &&  item.date === date )[0].value
  //       return bodyScale(bodyValue)
  //     })
  //     .attr('stroke', 'red')
  //     .attr('stroke-width', 2)
  //     .style('stroke-dasharray', '3, 3')
  //     .attr('stroke-linejoin', viewConfig.strokeLinejoin)
  //     .attr('stroke-linecap', viewConfig.strokeLinecap)
  //   // 绘制icon
  //   iconDrawObj.getDrawRoundIcon({
  //     content: g,
  //     data: d3.range(lineData.length),
  //     x: (i) => {
  //       return xScale(lineData[i].index) + viewConfig.X_OFFSET + (lineData[i].index - 20) / 4
  //     },
  //     y: (i) => {
  //       return bodyScale(lineData[i].value)
  //     },
  //     fill: 'transparent',
  //     stroke: 'red',
  //     r: 6
  //   })
  // }
  // 绘制降温的红圈和虚线
  function drawCoolBody(svg, coolData, allData) {
    const g = getG(svg, viewConfig)
    // 获取同个位置记录的温度
    const temArrMap = allData.reduce((data, items) => {
      items.map((item) => {
        if (item.value) {
          data[item.index] = item.value
        }
      })
      return data
    }, {})
    const vaildData = coolData.filter((item) => item.value)
    const lineData = vaildData.filter(({ index, value }) => temArrMap[index] !== value)
    g.append('g')
      .selectAll('line')
      .data(lineData)
      .join('line')
      .attr('class', 'xuxianliane')
      .attr('x1', function({ index }) {
        return xScale(index) + viewConfig.X_OFFSET
      })
      .attr('y1', function({ value }) {
        return bodyScale(value)
      })
      .attr('x2', function({ index }) {
        return xScale(index) + viewConfig.X_OFFSET
      })
      .attr('y2', function({ index,value }) {
        const bodyValue = temArrMap[index]
        let y2Location = bodyScale(value)
        if (bodyValue !== undefined) {

          y2Location = bodyScale(bodyValue)
        }
        return y2Location
      })

      .attr('stroke', 'red')
      .attr('stroke-width', 2)
      .style('stroke-dasharray', '3, 3')
      .attr('stroke-linejoin', viewConfig.strokeLinejoin)
      .attr('stroke-linecap', viewConfig.strokeLinecap)
    // 绘制icon
    iconDrawObj.getDrawRoundIcon({
      content: g,
      data: d3.range(vaildData.length),
      x: (i) => {
        return xScale(vaildData[i].index) + viewConfig.X_OFFSET
      },
      y: (i) => {
        return bodyScale(vaildData[i].value)
      },
      fill: 'transparent',
      stroke: 'red',
      r: 6
    })
  }
  // 绘制同一时间心率和脉搏的阴影
  function drawRateBody(svg, pulseData, heartRateData) {
    if (pulseData[0].length > 0 || heartRateData[0].length > 0) {
      const g = getG(svg, viewConfig)

      const defs = g.append("defs");
      const pattern = defs.append("pattern")
        .attr("id", "diagonal-hatch")
        .attr("patternUnits", "userSpaceOnUse")
        .attr("width", 1)
        .attr("height", 6)
        .attr("patternTransform", "rotate(100)"); // 45度斜线
 
      pattern.append("path")
        // .attr("d", "M-1,1 l2,-2 M0,4 l4,-4 M3,5 l2,-2")
        .attr("d", "M-1,-1 l2,2 M0,0 l4,4 M3,3 l2,2")
        .attr("stroke", 'red') // 虚线颜色
        .attr("stroke-width", 1)
        .attr("opacity", 0.8); // 透明度

      // 创建一个映射，用于快速查找心率值
      const heartRateMap = heartRateData[0].reduce((acc, curr) => {
        acc[curr.index] = curr.value
        return acc
      }, {})
      // 创建一个映射，用于快速查找脉搏值
      const pulseDataMap = pulseData[0].reduce((acc, curr) => {
        acc[curr.index] = curr.value
        return acc
      }, {})

      if (Object.keys(heartRateMap).length >= 3 && Object.keys(pulseDataMap).length >= 3) {
        // 获取脉搏和心率的起点和终点
        const pulseStart = pulseData[0][0]
        // const pulseEnd = pulseData[0][pulseData[0].length - 1]
        const heartRateStart = heartRateMap[pulseStart.index]
        // const heartRateEnd = heartRateMap[pulseEnd.index]

        // 生成路径数据
        const pathData = pulseData[0].reduce((acc, { value, index }, i) => {
          const heartRateValue = heartRateMap[index]

          // 过滤掉没有脉搏值和对应心率值的项
          if (value !== null && value !== undefined && heartRateValue !== null && heartRateValue !== undefined) {

            const y1 = heartScale(value || 0)
            const x = xScale(index) + viewConfig.X_OFFSET
            const y2 = heartScale(heartRateValue || 0)
            // 如果是第一个点，移动到起始点
            if (acc === '') {
              acc += `M${x},${y1}` // Move to the starting point
            }
            acc += `L${x},${y2}` // Draw line to heart rate point
          }
          return acc
        }, '')

        // 生成闭合路径
        let closingPath = pulseData[0].reduceRight((acc, { value, index }) => {
          const heartRateValue = heartRateMap[index]

          if (value !== null && value !== undefined && heartRateValue !== null && heartRateValue !== undefined) {
            const x = xScale(index) + viewConfig.X_OFFSET
            const y1 = heartScale(value || 0)
            // const y2 = heartScale(heartRateValue)

            acc += `L${x},${y1}` // Draw line down to pulse point
          }
          return acc
        }, '') + `L${xScale(pulseStart.index) + viewConfig.X_OFFSET},${heartScale(heartRateStart)}` + 'Z' // Close the path
        closingPath += `L${xScale(pulseStart.index) + viewConfig.X_OFFSET},${heartScale(heartRateStart)}Z`
        // 绘制阴影区域
        g.append('path')
          .attr('class', 'rate-shade')
          .attr('d', pathData + closingPath) // 完整的路径数据
          .attr('fill', 'url(#diagonal-hatch)') // 使用图案填充
          .attr('stroke', 'red')  // 设置边框颜色为红色
      }
    }
  }
  // 绘制表格区域外面的信息数据
  function drwaOtherInfo(svg, data) {
    const g = svg
      .append('g')
      .attr('transform', `translate(${viewConfig.marginLeft},${viewConfig.marginTop})`)
      .attr('style', 'font-size:14px;')
    // 绘制第几周
    getG(svg, viewConfig)
      .append('text')
      .attr('style', 'font-size:24px;')
      .attr('class', 'mytext')
      .text(`第${+data.weekNo + 1}周`)
      .attr('x', viewConfig.width / 2 - 40)
      .attr('y', viewConfig.bottomPos + TEXT_MARGIN_BOTTOM + 1.5 * LINE_HEIGHT - 25)
    // 绘制标注
    getG(svg, viewConfig)
      .attr(
        'transform',
        `translate(${viewConfig.marginLeft},${viewConfig.bottomPos + viewConfig.marginTop + HEAD_HEIGHT})`
      )
      .attr('style', 'font-size:14px;')
      .call((g) => {
        const dataList = [
          {
            name: '体温',
            fn: iconDrawObj.getDrawXIcon,
            params: {
              content: g,
              data: [0]
            }
          },
          {
            name: '心率',
            fn: iconDrawObj.getDrawRoundIcon,
            params: {
              content: g,
              data: [0],
              fill: 'white',
              stroke: 'red'
            }
          },
          {
            name: '脉搏',
            fn: iconDrawObj.getDrawRoundIcon,
            params: {
              content: g,
              data: [0],
              fill: 'red',
              stroke: 'red'
            }
          }
        ]
        g.append('text').text('标注：')
        dataList.map((item, i) => {
          g.append('text')
            .text(item.name)
            .attr('x', 40 + i * 80)
          item.fn({
            ...item.params,
            x: () => 80 + i * 80,
            y: () => -4
          })
        })
      })
      .attr('x', viewConfig.width / 2 - 40)
      .attr('y', viewConfig.bottomPos + TEXT_MARGIN_BOTTOM)
    // 绘制顶部的信息数据
    g.append('text')
      .attr('class', 'mytext')
      .attr('x', 0)
      .attr('y', () => {
        return HEAD_HEIGHT - TEXT_MARGIN_BOTTOM - 6
      })
      .html(() => {
        return INFO_KEYS.map(({ name, key }, index) => {
          return `<tspan dx="${index === 0 ? 0 : 40}" dy="${0}">${name}: ${data[key] ? data[key] : ''}</tspan>`
        }).join('')
      })
    // 绘制标题
    g.append('text')
      .attr('style', 'font-size:22px;text-anchor: middle;')
      .attr('class', 'mytext')
      .attr('x', viewConfig.width / 2)
      .attr('y', () => {
        return HEAD_HEIGHT - 4 * LINE_HEIGHT
      })
      .text(Header.HospitalName)
    // 体温单
    g.append('text')
      .attr('style', 'font-size:22px;text-anchor: middle;')
      .attr('class', 'mytext')
      .attr('x', viewConfig.width / 2)
      .attr('y', () => {
        return HEAD_HEIGHT - 2.5 * LINE_HEIGHT
      })
      .text(Header.temperatureName)
  }

  function generatePointer({ pathData, type, yScaleInstance }) {
    return (event) => {
      var index = Math.round((d3.pointer(event)[0] - viewConfig.step - textLeftMargin) / xScale.step())
      var val = xScale.domain()[index]
      // const i = d3.bisectCenter(d3.range(pathData.length), val);
      let i = -1
      pathData.forEach((item, index) => {
        if (item.index === val) {
          i = index
        }
      })
      const yPos = yScaleInstance(+pathData[i].value) + viewConfig.marginTop + HEAD_HEIGHT
      tooltip.style('display', null)
      tooltip.attr('class', 'myTooltip')
      tooltip.attr('transform', `translate(${xScale(val) + viewConfig.micoStep},${yPos})`)

      const path = tooltip.selectAll('path').data(['', '']).join('path').attr('fill', 'white').attr('stroke', 'black')

      const text = tooltip
        .selectAll('text')
        .data(['', ''])
        .join('text')
        .call((text) =>
          text
            .selectAll('tspan')
            .data([`${type}: ${pathData[i].value}`, `${pathData[i].date}`])
            .join('tspan')
            .attr('x', 0)
            .attr('y', (_, i) => `${i * 1.2}em`)
            .attr('font-weight', (_, i) => (i ? null : 'bold'))
            .text((d) => d)
        )

      const { y, width: w, height: h } = text.node().getBBox()
      text.attr('transform', `translate(${-w / 2},${15 - y})`)
      path.attr('d', `M${-w / 2 - 10},5H-5l5,-5l5,5H${w / 2 + 10}v${h + 20}h-${w + 20}z`)
    }
  }

  function pointerleft() {
    tooltip.style('display', 'none')
  }
}
function getTypeValue(type, allData = [], isNumber = true) {
  return allData.filter((item) => item.typeCode === type || '')
}
