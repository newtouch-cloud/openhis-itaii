<template>
  <div>
    <div ref="print">
      <div v-for="item in printData" :key="item.id">
        <div class="myccs2">
          <injectLabel :ref="item.id" :print-data="item" />
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import Print from 'vue-print-nb'
import injectLabel from './injectLabel'
export default {
  components: { injectLabel },
  props: {
    printData: {
      type: Array,
      default() {
        return []
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
          this.printData.forEach(data => {
            this['$refs'][data.id][0].print(printer)
          })
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

