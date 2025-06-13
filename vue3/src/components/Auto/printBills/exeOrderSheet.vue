<template>
  <div class="recordBill">
    <div :id="'exeSheetTitle' + printData.id" class="printView_header">
      <div style="text-align: center;  height: 60px">
        呼和浩特市第一医院医嘱执行单
      </div>
      <div>
        <span style="display: inline-block; width: 100px">床号：{{ printData.patientInfo.encounterLocationName }}</span>
        <span style="display: inline-block; width: 180px">姓名：{{ printData.patientInfo.name }}</span>
        <span style="display: inline-block; width: 140px">年龄：{{ printData.patientInfo.patientAge }}</span>
        <span style="display: inline-block; width: 280px">诊断：{{ printData.patientInfo.diag }}</span>
      </div>
      <!--      <div>
        <span style="display: inline-block; width: 200px">档案号：{{printData.patientInfo.hisNo}}</span>
        <span style="display: inline-block; width: 260px">入室时间：{{printData.patientInfo.checkInTime}}</span>
        <span style="display: inline-block; width: 140px">性别：{{!printData.patientInfo.gender? '':printData.patientInfo.gender.display}}</span>
      </div>-->
    </div>
    <div :id="'exeSheet' + printData.id" class="printView_content">
      <table border="1" cellSpacing="0" width="97%" cellPadding="1" style=" border-collapse:collapse; font-size: 13px" bordercolor="#333333">
        <thead>
          <TR>
            <TD rowspan="1">
              <DIV style="width: 65px;text-align: center">医嘱日期</DIV>
            </TD>

            <TD colspan="1">
              <DIV style="width: 120px" align="center">医嘱</DIV>
            </TD>
            <TD colspan="1">
              <DIV style="width: 10px" align="center" />
            </TD>
            <TD colspan="1">
              <DIV style="width: 70px" align="center">嘱托</DIV>
            </TD>
            <TD rowspan="1">
              <DIV style="width: 60px" align="center">用量</DIV>
            </TD>
            <TD colspan="1">
              <DIV style="width: 40px" align="center">用法</DIV>
            </TD>
            <TD colspan="1">
              <DIV style="width: 40px" align="center">频次</DIV>
            </TD>
            <TD rowspan="1">
              <DIV style="width: 65px" align="center">开立医生</DIV>
            </TD>
            <TD rowspan="1">
              <DIV style="width: 65px" align="center">执行时间</DIV>
            </TD>
            <TD colspan="1">
              <DIV style="width: 65px" align="center">执行护士</DIV>
            </TD>
            <TD colspan="1">
              <DIV style="width: 55px" align="center">终止时间</DIV>
            </TD>
            <TD colspan="1">
              <DIV style="width: 55px" align="center">终止人</DIV>
            </TD>
          </TR>
        </thead>
        <tbody>
          <tr v-for="item in printData.recordData" :key="item.id">
            <td v-html="item.moTime" />
            <td v-html="item.orderName" />
            <td v-html="item.flag" />
            <td v-html="item.remark" />
            <td v-html="item.doseOnce<=0?'':(item.doseOnce+ item.doseUnit)" />
            <td v-html="item.usageName" />
            <td v-html="item.frequency" />
            <td :id="item.id">
              <span v-if="(item.docSignImage === ''||item.docSignImage === null)">{{ item.moDocName }}</span>
              <img v-if="(item.docSignImage !== ''&&item.docSignImage !== null)" :src="'data:image/png;base64,'+ item.docSignImage" style="height: 100%; width: 100%;object-fit: cover;">
            </td>
            <td v-html="item.occurrence" />
            <td :id="item.id">
              <span v-if="(item.perNurserSignImage === ''||item.perNurserSignImage === null)">{{ item.performName }}</span>
              <img v-if="(item.perNurserSignImage !== ''&&item.perNurserSignImage !== null)" :src="'data:image/png;base64,'+ item.perNurserSignImage" style="height: 100%; width: 100%;object-fit: cover;">
            </td>
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
    }
  },
  mounted() {
  },
  methods: {
    printTest() {
      const LODOP = getLodop()
      LODOP.PRINT_INIT('')
      LODOP.ADD_PRINT_TABLE(120, 35, 750, 900, document.getElementById('exeSheet' + this.printData.id).innerHTML)
      LODOP.SET_PRINT_STYLEA(0, 'Horient', 3)
      LODOP.ADD_PRINT_HTM(20, 40, '100%', 100, document.getElementById('exeSheetTitle' + this.printData.id).innerHTML)
      LODOP.SET_PRINT_STYLEA(0, 'ItemType', 1)
      LODOP.SET_PRINT_STYLEA(0, 'LinkedItem', 1)
      // LODOP.SET_PRINT_PAGESIZE(2, '', '', '');  // 设置横向打印
      LODOP.ADD_PRINT_HTM(1080, 500, 300, 100, '总页数：<span><span tdata="pageNO">第##页</span>/ <span tdata="pageCount">共##页</span></span>')
      LODOP.SET_PRINT_STYLEA(0, 'ItemType', 1)
      LODOP.SET_PRINT_STYLEA(0, 'Horient', 1)
      LODOP.SET_SHOW_MODE('LANDSCAPE_DEFROTATED', 1)// 横向时的正向显示
      LODOP.PREVIEW() // 打印预览
      // LODOP.PRINT(); // 直接打印
    }
  }
}
</script>
<style  scoped lang="less">
  .recordBill {
    display: grid;
    grid-template-rows: 90px 1fr;
    height: 500px !important;
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
