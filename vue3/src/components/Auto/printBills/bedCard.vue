<template>
  <div class="printCard">
    <div ref="refQr" style="float: left; margin: 30px 15px">
      <img :src="emptyBed" style="height: 120px" class="pf_card_emptyBed_img">
    </div>
    <div class="printView_content" style=" margin: 30px 0">
      <div>
        <span>床号：</span>
        <span>{{ printData.bedName }}</span>
      </div>
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
    </div>
  </div>
</template>
<script>
import emptyBed from '@/assets/png/emptyBed.png'
export default {
  name: 'TriageTicket',
  props: {
    printData: {
      type: Object,
      default() {
        return {
          bedName: '',
          patientName: '',
          dept: '',
          triageLevel: '',
          triageTime: '',
          hisId: '11111'
        }
      }
    }
  },
  data() {
    return {
      emptyBed
    }
  },
  mounted() {
  },
  updated() {
  },
  methods: {
  }
}
</script>
<style>
  .printCard {
    border: #8d8d8d 1px solid;
    height: 200px !important;
    width: 320px;
    display: grid;
    grid-template-columns: 150px 200px;
  }

  .printView_content{
    display: grid;
    padding-top: 10px;
    grid-template-rows: repeat(5,25px);
  }
</style>
