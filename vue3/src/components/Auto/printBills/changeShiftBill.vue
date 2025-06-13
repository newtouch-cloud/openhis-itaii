<template>
  <div class="recordBill">
    <div id="div1" class="printView_header">
      <div style="text-align: center;  height: 40px">
        护理交接班
      </div>
      <div>
        <span style="display: inline-block; width: 200px">日期：{{ printData.date.substring(0, 10) }}</span>
        <span style="display: inline-block; width: 180px">发起人：{{ printData.initiator.name }}</span>
        <span style="display: inline-block; width: 140px">接收人：{{ printData.heir.name }}</span>
      </div>
      <div>
        <span
          v-for="item in printData.cols"
          :key="item.propertyType"
          style="display: inline-block; width: 90px"
          v-text="item.display + '：' + printData[item.propertyType]"
        />
      </div>
    </div>
    <div id="div2" class="printView_content">
      <table border="1" cellSpacing="0" width="98%" cellPadding="1" style=" border-collapse:collapse; font-size: 14px" bordercolor="#333333">
        <thead>
          <TR>
            <TD colspan="1">
              <DIV style="width: 40px" align="center">类别</DIV>
            </TD>
            <TD colspan="1">
              <DIV style="width: 50px" align="center">床号</DIV>
            </TD>
            <TD colspan="1">
              <DIV style="width: 60px" align="center">姓名</DIV>
            </TD>
            <TD colspan="1">
              <DIV style="width: 90px" align="center">主诉</DIV>
            </TD>
            <TD colspan="1">
              <DIV style="width: 90px" align="center">既往史</DIV>
            </TD>
            <TD colspan="1">
              <DIV style="width: 90px" align="center">诊断</DIV>
            </TD>
            <TD colspan="1">
              <DIV style="width: 155px" align="center">交接信息</DIV>
            </TD>
          </TR>
        </thead>
        <tbody>
          <tr v-for="item in printData.shiftRecordItems" :key="item.id">
            <td v-html="item.typeDisplay" />
            <td v-html="item.bedName" />
            <td v-html="item.patientName" />
            <td v-html="item.mainSuit" />
            <td v-html="item.previousHistory" />
            <td v-html="item.diagnosis" />
            <td v-html="item.content" />
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
        recordData: {},
        cols: []
      }
    }
  },
  mounted() {
  },
  methods: {
    printTest() {
      const LODOP = getLodop()
      LODOP.PRINT_INIT('')
      LODOP.ADD_PRINT_TABLE(100, 40, 750, 900, document.getElementById('div2').innerHTML)
      LODOP.SET_PRINT_STYLEA(0, 'Horient', 3)
      LODOP.ADD_PRINT_HTM(20, 40, '100%', 100, document.getElementById('div1').innerHTML)
      LODOP.SET_PRINT_STYLEA(0, 'ItemType', 1)
      LODOP.SET_PRINT_STYLEA(0, 'LinkedItem', 1)
      // LODOP.SET_PRINT_PAGESIZE(2, '', '', '');  // 设置横向打印
      LODOP.ADD_PRINT_HTM(1080, 500, 300, 100, '总页数：<span><span tdata="pageNO">第##页</span>/ <span tdata="pageCount">共##页</span></span>')
      LODOP.SET_PRINT_STYLEA(0, 'ItemType', 1)
      LODOP.SET_PRINT_STYLEA(0, 'Horient', 1)
      // LODOP.PREVIEW(); // 打印预览
      LODOP.PRINT() // 直接打印
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
    width: 740px;

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
