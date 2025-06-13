<template>
  <div ref="print">
    <div class="printInjectCard">
      <div :id="'div1'">
        <div style="width: 300px; text-align: center">
          <span style="font-weight: bolder; font-size: 18px; line-height: 36px">{{ printData.greenText }}</span>
          <span style="font-weight: bolder; font-size: 18px; line-height: 36px">分诊单</span>
        </div>
        <div style="position: absolute; top: 135px; text-align: center; width: 300px">{{ printData.hisId }}</div>
        <div style="position: absolute; top: 155px; text-align: center; width: 300px">
          {{ printData.triageLevel }}（{{ printData.dept }}）
        </div>
        <div
          style="
            position: absolute;
            top: 180px;
            left: 15px;
            display: block;
            width: 300px;
            height: 10px;
            border-bottom: 1px solid #5a5a5a;
          "
        />
      </div>
      <div :id="'div2'">
        <div style="width: 320px; margin-left: 25px">
          <div style="font-size: 15px">
            <span>姓名：</span>
            <span>{{ printData.patientName }}</span>
            <span style="margin-left: 15px">性别：</span>
            <span>{{ printData.sex }}</span>
            <span style="margin-left: 15px">年龄：</span>
            <span>{{ printData.age }}</span>
          </div>
          <div
            style="
              position: absolute;
              top: 15px;
              left: 15px;
              display: block;
              width: 300px;
              height: 10px;
              border-bottom: 1px solid #5a5a5a;
            "
          />
        </div>
      </div>
      <div :id="'div3'">
        <div style="margin-left: 15px; vertical-align: center; line-height: 18px">
          <div>
            <div style="display: inline-block; font-size: 15px">
              <span>Temp：</span>
              <span>{{ printData.observation ? printData.observation.temperature : '' }}℃</span>
            </div>
            <div style="display: inline-block; position: absolute; left: 180px; font-size: 15px">
              <span>P：</span>
              <span>{{ printData.observation ? printData.observation.sphygmus : '' }}次/分</span>
            </div>
          </div>
          <div style="display: block">
            <div style="display: inline-block; font-size: 15px">
              <span>R：</span>
              <span>{{ printData.observation ? printData.observation.breath : '' }}次/分</span>
            </div>
            <div style="display: inline-block; position: absolute; left: 180px; font-size: 15px">
              <span>BP：</span>
              <span>{{ getBloodPressure(printData.observation) }}mmHg</span>
            </div>
          </div>
          <div style="display: block; font-size: 15px">
            <span>SPO2：</span>
            <span>{{ printData.observation ? printData.observation.bloodOxygen : '' }}%</span>
          </div>
          <div
            style="
              position: absolute;
              top: 48px;
              left: 5px;
              display: block;
              width: 300px;
              height: 10px;
              border-bottom: 1px solid #5a5a5a;
            "
          />
        </div>
      </div>
      <div :id="'div4'">
        <div style="margin-left: 15px; font-size: 15px">
          <div>
            <span>分诊时间：</span>
            <span>{{ printData.triageTime }}</span>
          </div>
          <div>
            <span>联系电话：</span>
            <span>{{ printData.tel }}</span>
          </div>
        </div>
        <div
          style="
            position: absolute;
            top: 35px;
            left: 5px;
            display: block;
            width: 300px;
            height: 10px;
            border-bottom: 1px solid #5a5a5a;
          "
        />
        <div style="margin-left: 15px">
          <div style="font-size: 14px; margin-top: 15px; font-weight: bolder">请仔细核对个人信息后进行挂号</div>
          <div style="margin-top: 5px; font-size: 14px">为了您家人和其他患者的健康</div>
          <div style="font-size: 14px">请您保持就诊秩序保持诊区安静</div>
          <div style="font-size: 14px">祝您早日康复</div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { getLodop } from '../../../plugins/print/LodopFuncs'
export default {
  name: 'VuePrintNb',
  props: {
    printData: {
      type: Object,
      default() {
        return {}
      }
    }
  },
  data() {
    return {
      lastId: ''
    }
  },
  mounted() {},

  methods: {
    // 获取血压值
    getBloodPressure(pressure) {
      if (!pressure) return ''
      else if (pressure.bloodPressureShrinkOne === null) return ''
      return pressure.bloodPressureShrinkOne + '/' + pressure.bloodPressureDiastoleOne
    },
    printTriage(printerName) {
      console.log(this.printData, 'printData')
      const LODOP = getLodop()
      const printer = this.getPrinter(LODOP, printerName)
      if (printer === null) {
        this.openMesBox('6', '没有找到打印机【' + printerName + '】')
        return
      }
      LODOP.PRINT_INIT()
      LODOP.SET_PRINTER_INDEX(printer) // 指定打印机
      this.setPrint(LODOP)
      LODOP.SET_PRINT_PAGESIZE(0, '100mm', '140mm', '')
      // LODOP.PREVIEW(); // 打印预览
      LODOP.PRINT() // 直接打印
    },
    setPrint(LODOP) {
      LODOP.ADD_PRINT_HTM(0, 0, '100%', '100%', document.getElementById('div1').innerHTML)
      LODOP.ADD_PRINT_BARCODE(40, 100, 100, 100, 'qrcode', this.printData.hisId) // 设置条码位置、宽高、字体、值
      LODOP.ADD_PRINT_HTM(200, 0, '100%', '100%', document.getElementById('div2').innerHTML)
      LODOP.ADD_PRINT_HTM(230, 10, '100%', '100%', document.getElementById('div3').innerHTML)
      LODOP.ADD_PRINT_HTM(295, 10, '100%', '100%', document.getElementById('div4').innerHTML)
    },
    // 获取打印机
    getPrinter(LODOP, name) {
      const listCount = LODOP.GET_PRINTER_COUNT() // 当前打印设备数量
      for (let i = 0; i < listCount; i++) {
        if (LODOP.GET_PRINTER_NAME(i) === name) {
          return name
        }
      }
      return null
    }
  }
}
</script>
<style lang="less">
.printInjectCard {
  display: grid;
  width: 400px;
  grid-template-rows: 60px 205px 30px;
  border: solid #555 1px;
  background-color: #ffffff;
}
@page {
  size: auto;
  margin: 32px;
}
</style>
