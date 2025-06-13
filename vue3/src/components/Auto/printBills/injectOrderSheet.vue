<template>
  <div class="recordBill">
    <div id="div1" class="printView_header">
      <div style="text-align: center; font-size: 20px; height: 40px">
        呼和浩特市第一医院输液执行单
      </div>
      <div>
        <span>座位：{{ printData.patientInfo.encounterLocationName }}</span>
        <span style="margin-left: 18px">姓名：{{ printData.patientInfo.name }}</span>
        <span style="margin-left: 18px">性别：{{ printData.patientInfo.sexName }}</span>
        <span style="margin-left: 18px">年龄：{{ printData.patientInfo.patientAge }}</span>
        <span style="margin-left: 18px">卡号：{{ printData.patientInfo.hisNo }}</span>
        <span style="margin-left: 18px">科室：{{ printData.patientInfo.deptName }}</span>
      </div>
    </div>
    <div id="div2" class="printView_content">
      <table border="1" cellSpacing="0" cellPadding="1" style=" border-collapse:collapse; font-size: 14px" bordercolor="#333333">
        <thead>
          <TR style="height: 30px">
            <TD rowspan="1">
              <DIV style="width: 35px" align="center">时间</DIV>
            </TD>
            <TD colspan="1">
              <DIV style="width: 280px" align="center">药品名称</DIV>
            </TD>
            <TD colspan="1">
              <DIV style="width: 10px" align="center" />
            </TD>
            <TD rowspan="1">
              <DIV style="width: 55px" align="center">剂量</DIV>
            </TD>
            <TD colspan="1">
              <DIV style="width: 30px" align="center">频次</DIV>
            </TD>
            <TD colspan="1">
              <DIV style="width: 55px" align="center">用法</DIV>
            </TD>
            <TD rowspan="1">
              <DIV style="width: 70px" align="center">执行时间</DIV>
            </TD>
            <TD rowspan="1">
              <DIV style="width: 55px" align="center">执行人</DIV>
            </TD>
          </TR>
        </thead>
        <tbody style=" border-collapse:collapse;">
          <tr v-for="item in printData.recordData" :key="item.id">
            <td v-html="item.moTime.substring(0,16)" />
            <td v-html="item.orderName" />
            <td v-html="item.flag" />
            <td v-html="item.doseOnce + item.doseUnit" />
            <td v-html="item.freqName" />
            <td v-html="item.usageName" />
            <td />
            <td />
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
<script>
import { getLodop } from '../../../plugins/print/LodopFuncs'
export default {
  data() {
    return {
      printData: {
        patientInfo: {},
        recordData: {}
      }
    }
  },
  mounted() {
  },
  methods: {
    printTest() {
      const LODOP = getLodop()
      LODOP.PRINT_INIT('')
      LODOP.ADD_PRINT_TABLE(100, 35, 700, 900, document.getElementById('div2').innerHTML)
      LODOP.ADD_PRINT_HTM(20, 40, '100%', 100, document.getElementById('div1').innerHTML)
      LODOP.SET_PRINT_PAGESIZE(0, '148mm', '210mm', '') // 设置横向打印
      // LODOP.SET_SHOW_MODE('LANDSCAPE_DEFROTATED', 1);// 横向时的正向显示
      LODOP.PREVIEW() // 打印预览
      // LODOP.PRINT(); // 直接打印
    }
  }
}
</script>
<style  scoped lang="less">
  .recordBill {
    border: #8d8d8d 1px solid;
    display: grid;
    grid-template-rows: 90px 1fr;
    height: 200px !important;
    width: 680px;

    /deep/ .el-table .cell {
      font-size: 10px !important;
    }
    .printView_header {
      grid-template-rows: 40px 30px 30px;
      height: 200px !important;
      h4{
        text-align: center;
        margin: 15px;
      }
    }
    .printView_content{

    }
  }

</style>
