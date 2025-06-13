<template>
  <div ref="print">
    <div class="printInjectCard">
      <div :id="printData.id + 'div1'">
        <div style="display:block; height: 60px; width: 280px; float:left;">
          <span style="font-weight: bolder; font-size: 16px; line-height: 36px; margin-left: 160px;">急诊输液贴</span>
          <div>
            <span style="margin-left: 8px;">{{ printData.patient.hisNo }}</span>
            <span style="margin-left: 8px;">{{ printData.patient.name }}</span>
            <span style="margin-left: 8px;">{{ printData.patient.sexName }}</span>
            <span style="margin-left: 8px;">{{ printData.patient.patientAge }}</span>
          </div>
        </div>
        <div style="display: block; width: 120px; height: 60px; float:left; ">
          <div :id="getId(printData.id)" style="float: left; margin: 5px;" />
          <span style="float: left; margin: 5px">{{ printData.priority }}</span>
        </div>
      </div>
      <div :id="printData.id + 'div2'">
        <table border="1" cellSpacing="0" width="390px" cellPadding="1" style="margin-left: 8px; border-collapse:collapse; table-layout: fixed; font-size: 14px" bordercolor="#333333">
          <thead>
            <TR>
              <Th style="width: 160px" v-html="'药品名称'" />
              <Th style="width: 75px" v-html="'用量'" />
              <Th style="width: 10px" v-html="''" />
              <Th style="width: 50px" v-html="'频次'" />
              <Th style="width: 75px" v-html="'用法'" />
            </TR>
          </thead>
          <tbody>
            <tr v-for="item in printData.orderDetail" :key="item.id">
              <td v-html="item.orderName" />
              <td v-html="item.doseOnce + item.doseUnit" />
              <td v-html="item.flag" />
              <td v-html="item.frequency" />
              <td v-html="item.usageName" />
            </tr>
          </tbody>
        </table>
      </div>
      <div :id="printData.id + 'div3'">
        <span>日期：</span>
        <span>{{ moment().format('YYYY-MM-DD HH:mm') }}</span>
      </div>
    </div>
  </div>
</template>
<script>
// import QRCode from 'qrcodejs2'
import { getLodop } from '../../../plugins/print/LodopFuncs'
export default {
  name: 'VuePrintNb',
  props: {
    printData: {
      type: Object,
      default() {
        return {
        }
      }
    }
  },
  data() {
    return {
      lastId: '',
      qrCode: ''
    }
  },
  mounted() {
    // console.log('mounted方法');
    // this.initBarCode();
  },
  methods: {
    // initBarCode() {
    //   this.$nextTick(() => {
    //     if (this.lastId !== this.printData.patient.hisId) {
    //       new QRCode(this.getId(this.printData.id), {
    //         text: this.printData.patient.hisId,
    //         width: 50,
    //         height: 50,
    //         colorDark: '#000000',
    //         colorLight: '#ffffff',
    //         correctLevel: QRCode.CorrectLevel.H
    //       });
    //     }
    //     this.lastId = this.printData.patient.hisId;
    //   });
    // },
    //
    // getId(id) {
    //   return 'qrcode' + id;
    // },
    print(printerName) {
      const LODOP = getLodop()
      const printer = this.getPrinter(LODOP, printerName)
      if (printer === null) {
        this.openMesBox('6', '没有找到打印机【' + printerName + '】')
        return
      }
      console.log(this.printData, 'printData')
      this.qrCode = this.printData.orderDetail[0].comboNo + this.printData.orderDetail[0].executionSeq
      LODOP.PRINT_INIT()
      LODOP.SET_PRINTER_INDEX(printer)// 指定打印机
      this.setPrint(LODOP)
      LODOP.SET_PRINT_PAGESIZE(0, 1070, 800, '')
      LODOP.PREVIEW() // 打印预览
      // LODOP.PRINT(); // 直接打印
    },
    setPrint(LODOP) {
      LODOP.ADD_PRINT_HTM(0, 0, '100%', '100%', document.getElementById(this.printData.id + 'div1').innerHTML)
      LODOP.ADD_PRINT_HTM(82, 0, '100%', '100%', document.getElementById(this.printData.id + 'div2').innerHTML)
      LODOP.ADD_PRINT_HTM(265, 10, '100%', '100%', document.getElementById(this.printData.id + 'div3').innerHTML)
      LODOP.SET_PRINT_STYLEA(0, 'ItemType', 1)
      // 设置二维码 qrcode ，条码128B等
      // LODOP.ADD_PRINT_BARCODE(Top,Left,Width,Height,BarCodeType,BarCodeValue);
      LODOP.ADD_PRINT_BARCODE(0, 300, 75, 75, 'qrcode', this.qrCode)// 设置条码位置、宽高、字体、值
      LODOP.SET_PRINT_STYLEA(0, 'FontSize', 18)// 设置上面这个条码下方的文字字体大小
      // LODOP.SET_PRINT_STYLEA(0,"Color","#FF0000");//设置当前条码以及条码下方字体的颜色
      LODOP.SET_PRINT_STYLEA(0, 'Angle', 180)// 设置旋转角度
      LODOP.SET_PRINT_STYLEA(0, 'ShowBarText', 0)// 设置是否显示下方的文字
      LODOP.SET_PRINT_STYLEA(0, 'AlignJustify', 2)// 设置条码下方的文字相对于条码本身居中
      // LODOP.SET_PRINT_STYLEA(0,"AlignJustify",1);//设置条码下方的文字相对于条码本身居左
      // LODOP.SET_PRINT_STYLEA(0,"AlignJustify",3);//设置条码下方的文字相对于条码本身居右
      // LODOP.SET_PRINT_STYLEA(0,"GroundColor","#0080FF");//设置条码的背景色
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
  .printInjectCard{
    display: grid;
    width: 400px;
    grid-template-rows: 60px 205px 30px;
    border: solid #555 1px;
    background-color: #FFFFFF;

    td{
      padding-left: 3px;
    }
  }
  @page{
    size: auto;
    margin: 32px;
  }
</style>

