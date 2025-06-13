<template>
  <div class="printWrist">
    <div id="div1" class="printView_content">
      <div style="margin: 1px;font-size: 12px">
        <span>姓名: </span>
        <span>{{ printData.patientName }}</span>
        <span style="position: absolute; left: 110px">病历号:</span>
        <span style="position: absolute; left: 155px">{{ printData.hisId }}</span>
        <span style="position: absolute; left: 250px">入院时间:</span>
        <span style="position: absolute; left: 305px">{{ printData.checkInWardTime? printData.checkInWardTime.substr(0,16):'' }}</span>
      </div>
      <div style="margin: 1px;font-size: 12px">
        <span>性别: </span>
        <span>{{ printData.gender? printData.gender.display:'' }}</span>
        <span style="position: absolute; left: 110px">科室:</span>
        <span style="position: absolute; left: 140px">{{ printData.dept }}</span>
      </div>
      <div style="margin-top: 2px;font-size: 12px">
        <span>床号: </span>
        <span>{{ printData.bedName }}</span>
        <span style="position: absolute; left: 110px">分级:</span>
        <span style="position: absolute; left: 140px">{{ printData.triageLevel }}</span>
      </div>
    </div>
    <div id="qrcode" ref="refQr" style="padding-top: 1px" />
  </div>
</template>
<script>
import QRCode from 'qrcodejs2'
import { getLodop } from '../../../plugins/print/LodopFuncs'
export default {
  name: 'WristPrint',
  data() {
    return {
      printData: {
        patientName: '',
        deptName: '',
        triageLevel: '',
        triageTime: '',
        hisId: '11111',
        lastId: ''
      }
    }
  },
  mounted() {
  },
  updated() {
    this.initBarCode()
  },
  methods: {
    initBarCode() {
      this.$nextTick(() => {
        if (this.lastId !== this.printData.hisId) {
          new QRCode('qrcode', {
            text: this.printData.hisId,
            width: 40,
            height: 40,
            colorDark: '#000000',
            colorLight: '#ffffff',
            correctLevel: QRCode.CorrectLevel.H
          })
        }
        this.lastId = this.printData.hisId
      })
    },
    clear() {
      document.getElementById('qrcode').innerHTML = ''
    },
    execPrint(preview, printerName) {
      const LODOP = getLodop()
      const printer = this.getPrinter(LODOP, printerName)
      if (printer === null) {
        this.openMesBox('6', '没有找到打印机【' + printerName + '】')
        return
      }
      LODOP.PRINT_INIT('')
      LODOP.ADD_PRINT_HTM(30, 100, '100%', 100, document.getElementById('div1').innerHTML)
      LODOP.ADD_PRINT_HTM(30, 20, '100%', 40, document.getElementById('qrcode').innerHTML)
      LODOP.SET_PRINT_PAGESIZE(2, '25mm', '270mm', '') // 设置横向打印
      if (preview) {
        LODOP.PREVIEW() // 打印预览
        return
      }
      // LODOP.PREVIEW(); // 打印预览
      LODOP.PRINT() // 直接打印
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
<style scoped lang="less">
  .printWrist {
    display: grid;
    width: 460px;
    grid-template-columns:  330px 40px;
    height: 45px;

   .printView_content{
    display: grid;
    padding-top: 1px;
    padding-left: 60px;
    grid-template-rows: repeat(3,14px);
   }

  }
</style>
