import moment from 'moment'

export function getG(svg, translateX, translateY) {
  return svg
    .append('g')
    .attr('transform', `translate(${translateX},${translateY})`)
}

// function generatePointer ({ pathData, type, yScaleInstance }) {
//   return (event) => {
//     var index = Math.round(
//       (d3.pointer(event)[0] - step - textLeftMargin) / xScale.step()
//     )
//     var val = xScale.domain()[index]
//     const i = d3.bisectCenter(d3.range(pathData.length), val)
//     console.log(val, index, '=====', pathData[i], 'pathData[i]')
//     const yPos = yScaleInstance(pathData[i].value) + marginTop + HEAD_HEIGHT
//     console.log(`translate(${xScale(i)},${yPos})`)
//     tooltip.style('display', null)
//     tooltip.attr('class', 'myTooltip')
//     tooltip.attr('transform', `translate(${xScale(i) + micoStep},${yPos})`)

//     const path = tooltip
//       .selectAll('path')
//       .data(['', ''])
//       .join('path')
//       .attr('fill', 'white')
//       .attr('stroke', 'black')

//     const text = tooltip
//       .selectAll('text')
//       .data(['', ''])
//       .join('text')
//       .call((text) =>
//         text
//           .selectAll('tspan')
//           .data([`${type}: ${pathData[i].value}`, `${pathData[i].date}`])
//           .join('tspan')
//           .attr('x', 0)
//           .attr('y', (_, i) => `${i * 1.2}em`)
//           .attr('font-weight', (_, i) => (i ? null : 'bold'))
//           .text((d) => d)
//       )

//     const { x, y, width: w, height: h } = text.node().getBBox()
//     text.attr('transform', `translate(${-w / 2},${15 - y})`)
//     path.attr(
//       'd',
//       `M${-w / 2 - 10},5H-5l5,-5l5,5H${w / 2 + 10}v${h + 20}h-${w + 20}z`
//     )
//   }
// }
export function getTypeDatas(typeData, beginDate) {
  const types = typeData.sort((a, b) => new Date(a.date) - new Date(b.date))
  return types.map(item => {
    return {
      index: getIndex(beginDate, item.date, '00:00:00') / 6,
      times: 0,
      date: item.date,
      typeCode: item.typeCode,
      typeValue: (item.typeValue) || null
    }
  })
}

export function getTypeData(type, allData = [], isNumber = true, beginDate) {
  const getList = allData
    .map((rowBOSItem, index) => {
      const rowBOS = rowBOSItem.rowBOS
      const cur =
        rowBOS.find((item) => {
          return item.typeCode === type
        }) || {}
      return {
        index: getIndex(beginDate, cur.date, cur.times),
        date: cur.date,
        value: (isNumber ? +cur.typeValue : cur.typeValue) || null
      }
    })
    .map((item) => {
      if (item.value) {
        // item.value = NaN
      }
      return item
    })
  return getList.sort((a, b) => a.index - b.index)
}
// 获取不升
export function getType(type, allData = [], beginDate) {
  const getList = allData
    .map((rowBOSItem, index) => {
      const rowBOS = rowBOSItem.rowBOS
      const cur =
        rowBOS.find((item) => {
          return item.typeCode === type
        }) || {}
      return {
        index: getIndex(beginDate, cur.date, cur.times),
        date: cur.date,
        value: cur.typeValue !== '' ? (parseFloat(cur.typeValue) <= 35 ? '不升' : null) : null
      }
    })
    .map((item) => {
      if (item.value) {
        // item.value = NaN
      }
      return item
    })
  return getList.sort((a, b) => a.index - b.index)
}
// 合并标记内容
export function setMergeTag(ymbolTextArr = [], symbolContent = []) {
  const arr = []
  ymbolTextArr.forEach(item => {
    symbolContent.forEach(res => {
      if (item.index === res.index) {
        arr.push({ ...item, ...res, value: (item.value !== null ? item.value : '') + (res.value !== null ? res.value : '') })
      }
    })
  })
  return arr
}
// 筛选手术产后日数
export function postpartumDays(type, arr) {
  return arr.filter(item => item.typeCode === type).map(i => i.typeValue)
}
// 筛选体温
export function getTypeAnimalHeat(type, allData = [], code, beginDate) {
  const getList = allData
    .map((rowBOSItem, index) => {
      console.log(rowBOSItem.rowBOS, 'rowBOSItem.rowBOS')
      const rowBOS = rowBOSItem.rowBOS
      const curList = // 改为 filter 来获取所有符合条件的项
        rowBOS.filter((item) => {
          return item.typeCode === type
        }) || []
        
      // 针对每个 cur 处理并生成新的对象数组
      return curList.map((cur) => ({
        index: getIndex(beginDate, cur.date, cur.times),
        date: cur.date,
        value: (+cur.collectionMode === +code ? +cur.typeValue : null) || null
      }))
    })
    .flat() // 将二维数组展平为一维数组
    .map((item) => {
      if (item.value) {
        // item.value = NaN
      }
      return item
    })
    console.log(getList, 'getList')
  return getList.sort((a, b) => a.index - b.index)
}
// 设置折线
export function getBrokenLine(type, allData = [], arr = [], list = [], topList = [], beginDate) {
  const result = []
  const getList = allData
    .map((rowBOSItem, index) => {
      const rowBOS = rowBOSItem.rowBOS
      const cur =
          rowBOS.find((item) => {
            return item.typeCode === type
          }) || {}
      return {
        index: getIndex(beginDate, cur.date, cur.times),
        date: cur.date,
        value: +cur.typeValue
      }
    })
    .map((item) => {
      if (item.value) {
        // item.value = NaN
      }
      return item
    })
  const _a = arr.filter(item => item.value)
  const _b = list.filter(item => item.value)
  const _c = topList.filter(item => item.value)
  const mergingData = [..._a, ..._b, ..._c, { index: 50 }].sort((a, b) => a.index - b.index)
  let start = 0
  mergingData.forEach(item => {
    const _p = getList.sort((a, b) => a.index - b.index).slice(start, item.index)
    start = item.index + 1
    result.push(_p)
  })
  return result
}
// 处理脉搏心率
export function getHeartRate(type, allData = [], arr = [], topList = [], isNumber = true, beginDate) {
  const result = []
  const getList = allData
    .map((rowBOSItem, index) => {
      const rowBOS = rowBOSItem.rowBOS
      const cur =
          rowBOS.find((item) => {
            return item.typeCode === type
          }) || {}
      return {
        index: getIndex(beginDate, cur.date, cur.times),
        date: cur.date,
        value: (isNumber ? +cur.typeValue : cur.typeValue) || null
      }
    })
    .map((item) => {
      if (item.value) {
        // item.value = NaN
      }
      return item
    })
  const _a = arr.filter(item => item.value)
  const _c = topList.filter(item => item.value)
  const mergingData = [..._a, ..._c, { index: 50 }].sort((a, b) => a.index - b.index)
  let start = 0
  mergingData.forEach(item => {
    const _p = getList.slice(start, item.index).sort((a, b) => { return a.index - b.index })
    start = item.index
    result.push(_p)
  })
  return result
}
function getIndex(beginDate, date, time) {
  if (beginDate === undefined || date === undefined) return
  const diffTime = moment(date.substring(0, 10)).diff(moment(beginDate.substring(0, 10))) / 1000 / 3600 / 24
  const diffIndex = parseInt(time.substring(0, 2))
  return diffTime * 6 + Math.floor(diffIndex / 4)
}
// 筛选35和40~42数据
export function degreesOnline(allData, data, type) {
  const arr = allData.map((item, index) => {
    const cur = data?.find(res => item.value === res.name && +res.isShowPlace === type) || null
    return {
      index: index,
      date: item.date,
      value: cur?.name || null
    }
  })
  return arr
}
// 35或42上断开的事件
export function disconnectEvents(allData, data, code, type) {
  const arr = allData.map((item, index) => {
    const cur = data?.find(res => item.value === res.name && +res.isShowPlace === type && +res[code] === 1) || null
    return {
      index: index,
      date: item.date,
      value: cur?.name || null
    }
  })
  return arr
}

export function getDrawCoolData(type, allData = [], isNumber = true, beginDate) {
  const getList = allData
    .map((rowBOSItem, index) => {
      const rowBOS = rowBOSItem.rowBOS
      const cur =
        rowBOS.find((item) => {
          return item.typeCode === type
        }) || {}
      return {
        index: getIndex(beginDate, cur.date, cur.times),
        date: cur.date + cur.times,
        value: (isNumber ? +cur.typeValue : cur.typeValue) || null
      }
    })
    .map((item) => {
      if (item.value) {
        // item.value = NaN
      }
      return item
    })
  return getList.sort((a, b) => a.index - b.index)
}
// 筛选降温getDrawData
export function getDrawData(type, allData = [], code, beginDate) {
  const getList = allData
    .map((rowBOSItem, index) => {
      const rowBOS = rowBOSItem.rowBOS
      const cur =
        rowBOS.find((item) => {
          return item.typeCode === type
        }) || {}
      return {
        index: getIndex(beginDate, cur.date, cur.times),
        date: cur.date + cur.times,
        value: (+cur.collectionMode === +code ? +cur.typeValue : null) || null
      }
    })
    .map((item) => {
      if (item.value) {
        // item.value = NaN
      }
      return item
    })
  return getList.sort((a, b) => a.index - b.index)
}
