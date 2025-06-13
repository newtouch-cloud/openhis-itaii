<template>
  <div class="printTicket">
    <p>xx人民医院</p>
    <div>
      <span>姓名：</span>
      <span>{{ printData.patientName }}</span>
    </div>
    <div>
      <span>患者编号：</span>
      <span>{{ printData.hisId }}</span>
    </div>
    <div>
      <span>分诊科室：</span>
      <span>{{ printData.dept }}</span>
    </div>
    <div>
      <span>分诊等级：</span>
      <span>{{ printData.triageLevel }}</span>
    </div>
    <div>
      <span>分诊时间：</span>
      <span>{{ printData.triageTime }}</span>
    </div>
    <img ref="refQr" style="position: absolute; top: 10px; left: 100px">
  </div>
</template>
<script>
import JsBarcode from 'jsbarcode'
export default {
  name: 'TriageTicket',
  props: {
    printData: {
      type: Object,
      default() {
        return {
          patientName: '',
          dept: '',
          triageLevel: '',
          triageTime: '',
          hisId: ''
        }
      }
    }
  },
  data() {
    return {

    }
  },
  updated() {
    JsBarcode(this.$refs.refQr,
      this.printData.hisId,
      {
        format: 'CODE128',
        lineColor: '#000',
        background: '#fff',
        displayValue: false,
        height: 30,
        margin: 2
      })
  },
  mounted() {
  },
  methods: {

  }
}
</script>
<style>
  .printTicket{
    display: block;
    width: 300px;
    height: 200px;
    border: 1px solid #A3A3A3;
  }

</style>
