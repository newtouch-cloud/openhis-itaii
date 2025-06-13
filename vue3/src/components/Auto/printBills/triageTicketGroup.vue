<template>
  <div>
    <div ref="print">
      <div class="myccs2">
        <triageTicketNew ref="printTriage" :print-data="printData" />
      </div>
      <!--      <div v-for="item in printData" :key="item.id">-->
      <!--        <div class="myccs2">-->
      <!--          <triageTicketNew :ref="item.id" :print-data="printData"/>-->
      <!--        </div>-->
      <!--      </div>-->
    </div>
  </div>
</template>

<script>
import Print from 'vue-print-nb'
import triageTicketNew from './triageTicketNew'
export default {
  components: { triageTicketNew },
  props: {
    printData: {
      // type: Array,
      default() {
        return {
        }
      }
    }
  },
  Print,
  data() {
    return {
    }
  },
  methods: {
    // 打印
    fprint(preview, printer) {
      this.$nextTick(() => {
        if (preview) {
          this.$print(this.$refs.print)
        } else {
          // this.$refs.printTriage.initBarCode();
          this.$refs.printTriage.printTriage(printer)
          // this.printData.forEach(data => {
          //   this['$refs'][data.id][0].print(printer);
          // });
        }
      })
    }
  }
}
</script>

<style lang="less">
    .myccs{
        background-color: forestgreen;
        height: 100px;
        width: 200px;
        padding: 1px;
        border: 1px solid red;
        color: #0EB396;
    }
    @page{
        size: auto;
        margin: 32px;
    }
    @media print {
        .myccs2{
            page-break-before: always;
        }
    }
</style>
