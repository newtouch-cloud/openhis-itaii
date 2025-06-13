<template>
  <Graphics v-if="graphicsDataDone" :value="resInfo" print @done="printPage" />
</template>

<script setup>
import Graphics from '../../../views/inpatientNurse/tprChart/index';
import data from '../../../action/nurseStation/temperatureSheet/datas';

const printData = ref({});
const resInfo = ref({});
const graphicsDataDone = ref(false);
const printPromiseReslove = ref(null);
const dateClosed = ref({
  stopTime: true, // 控制结束日期
  stopNumber: true, // 控制住院天数
});

const route = useRoute();

function removeIframe(id) {
  const child = window.parent.document.getElementById(id);
  if (child) {
    child.parentElement.removeChild(child);
  }
}
function setTime(num) {
  return new Promise((resolve) => {
    setTimeout(resolve, num);
  });
}
// export default {
//   components: {
//     Graphics,
//   },
//   data() {
//     return {
//       printData: {},
//       resInfo: {},
//       graphicsDataDone: false,
//       // 当前页面是否完成打印的reslove函数
//       printPromiseReslove: null,
//       dateClosed: {
//         stopTime: true, // 控制结束日期
//         stopNumber: true, // 控制住院天数
//       },
//     };
//   },
//   mounted() {
//     const printData = window.localStorage.getItem('printItemData');
//     this.printData = JSON.parse(printData);
//     this.addPrintEvent();
//     this.runTask();
//   },
//   methods: {
    function addPrintEvent() {
      window.addEventListener('afterprint', () => {
         if (printPromiseReslove.value) {
      printPromiseReslove.value();
    }
      });
    }
    async function runTask() {
      // const weeks = this.printData.weekList;
      const weeks = [];
      for (let index = 0; index < weeks.length; index++) {
        const week = weeks[index];
        await this.getData(week);
      }
      try {
        removeIframe(this.$route.query.id);
      } catch (error) {
        // window.location.href = './compTemperature';
        var child = window.parent.document.getElementById('my_dataviz');
        child.parentElement.appendChild(child);
      }
    }
    function printPage() {
      window.print();
    }
    // 获取每周数据
    async function getData(curWeekInfo) {
      this.resInfo = data;
      if (this.graphicsDataDone) {
        this.graphicsDataDone = false;
      }
      await setTime(10);
      this.graphicsDataDone = true;
      return new Promise((resolve) => {
        this.printPromiseReslove = resolve;
      });
    }
//   },
// };
</script>

<style>
@page {
  margin-top: 30;
  margin-bottom: 0;
  background-color: #1890ff;
}
</style>
