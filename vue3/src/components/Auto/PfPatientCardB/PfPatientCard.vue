<template>
  <div
    class="pf_card"
    :style="{
      outlineWidth:active?'2px':'0',
      outlineColor:active?getBedBackColor(data.triageLevel):'#9aa6b5',
      backgroundColor:data.bedOperationalStatus==='U'?'#EDFFFD':'',
      borderColor: getBedBackColor(data.triageLevel)
    }"
    @click="clickAct"
  >
    <div v-if="data.bedOperationalStatus==='U'">
      <img :src="emptyBed" class="pf_card_emptyBed_img">
      <div class="pf_card_emptyBed_text">{{ data.bedName }}</div>
    </div>
    <div v-else>
      <div v-if="data.isDischarge" class="pf_card_discharge">
        <span style="margin-left: 6px">离</span>
      </div>
      <div class="pf_card_card">
        <CardSign :color="getBedBackColor(data.triageLevel)" :title="data.bedName" :tail="getDisplay(data.triageLevel)" />
      </div>
      <div class="pf_card_nameSexAndAge">
        <span class="pf_card_name">{{ data.patientName }}</span>
        <span class="pf_card_sexAndAge">{{ data.gender.display }}/{{ data.age }}</span>
      </div>
      <div class="pf_card_rescueTime">
        <span style="margin-right: 16px">入室时间</span>
        {{ moment(data.checkInWardTime).format('YYYY-MM-DD HH:mm') }}
      </div>
      <div class="pf_card_noCode">{{ data.hisId }}</div>
      <div class="pf_card_rescueTimeText">{{ rescueTimeText() }}</div>
      <div v-if="data.diag!==''" class="pf_card_diagnosis">
        <div class="card-rectangle-text">{{ data.diag }}</div>
        <span style="margin-left: 4px">(诊断)</span>
      </div>
      <div v-if="isNewSign()" class="card-rectangle">新</div>
      <div v-if="is72HourSign()" class="card-rectangle2">超72H</div>
      <hr class="pf_card_line">
      <div class="pf_card_nursingMeasuresString">{{ getStringByCode(data.nursingMeasures, nursingMeasures) }}</div>
      <div class="pf_card_specialArrangementString">{{ getStringByCode(data.specialArrangement, specialArrangementList) }}</div>
      <div v-if="false" class="pf_card_btn" @click="moreClick">更多</div>
    </div>

  </div>
</template>
<script>
// Ⅰ－1、Ⅱ－2、Ⅲ－3、Ⅳ－4、Ⅴ－5、Ⅵ－6、Ⅶ－7、Ⅷ－8、Ⅸ－9
import emptyBed from '@/assets/png/emptyBed.png'

export default {
  name: 'PfPatientCard',
  inject: ['PfPatientCards'],
  props: {
    data: {
      type: Object,
      default() {
        return {}
      }
    },
    bedConfig: {
      type: Object,
      default() {
        return {}
      }
    }
  },
  data() {
    return {
      colors: [],
      emptyBed,
      bedStations: [],
      nursingMeasures: [],
      specialArrangementList: []
    }
  },
  computed: {
    active() {
      return this.PfPatientCards.activePatient.bedId === this.data.bedId
    }
  },
  created() {
    this.colors = this.config.echartsColor.color
    this.nursingMeasures = this.bedConfig.nursingMeasures
    this.specialArrangementList = this.bedConfig.specialArrangementList
    this.bedStations = this.config.bedStation.options
  },
  mounted() {
    const setIntervalId = setInterval(() => {
      this.$forceUpdate()
    }, 60000)
    this.$once('hook:beforeDestroy', () => {
      clearInterval(setIntervalId)
    })
  },
  methods: {
    clickAct() {
      if (!this.data.isBedNull) {
        this.PfPatientCards.activePatient = this.$props.data
      }
      this.$emit('click', this.$props.data)
    },
    moreClick() {
      this.$emit('moreClick', this.$props.data)
    },

    getStringByCode(codeList, itemList) {
      const arr = itemList.filter((item) => {
        return codeList.includes(item.code)
      })

      const rearr = arr.map((item) => {
        return item.display
      })
      return rearr.join('、')
    },
    isNewSign() {
      const ytime = this.data.checkInWardTime
      const hour = this.moment().diff(ytime, 'hours')
      return hour < 24
    },
    is72HourSign() {
      const ytime = this.data.checkInWardTime
      const hour = this.moment().diff(ytime, 'hours')
      return hour > 72
    },
    rescueTimeText() {
      const ytime = this.data.checkInWardTime
      const days = this.moment().diff(ytime, 'days')
      const hour = this.moment().diff(ytime, 'hours')
      const minutes = this.moment().diff(ytime, 'minutes')
      if (hour >= 24) {
        return days + '天' + hour % 24 + '时' + minutes % 60 + '分'
      } else
      if (hour < 24) {
        return hour + '时' + minutes % 60 + '分'
      } else
      if (minutes < 60) {
        return minutes + '分'
      }
    },
    getDisplay(triageLevel) {
      return triageLevel?.display ?? ''
    },
    getBedBackColor(triageLevel) {
      const Level = triageLevel?.display ?? '0级'
      let backColor = '#808080'
      switch (Level) {
        case '一级':
          backColor = this.colors[0]
          break
        case '二级':
          backColor = this.colors[1]
          break
        case '三级':
          backColor = this.colors[2]
          break
        case '四级':
          backColor = this.colors[3]
          break
        default:
          backColor = '#C4C4C4'
      }
      return backColor
    }
  }
}
</script>
<style scoped lang="less">
  .pf_card {
    outline-style: solid;
    position: relative;
    border-radius: 5px;
    background-color: #FFFFFF;
    box-shadow: 1px 1px 4px #888888;
    border: 1px solid #e2dfdf;
    width: 265px;
    height: 194px;
    user-select: none;
    font-family: Microsoft YaHei;

    &:hover {
      box-shadow: 4px 4px 16px #888888;
    }

    .pf_card_card {
      position: absolute;
      top: 0;
      left: 7px;
    }

    .pf_card_nameSexAndAge {
      position: absolute;
      top: 16px;
      left: 74px;
      width: 192px;
      line-height: 18px;

      .pf_card_name {
        font-size: 18px;
        font-weight: bold;
      }

      .pf_card_sexAndAge {
        position: absolute;
        font-size: 12px;
        color: #9AA6B5;
        margin-left: 8px;
      }
    }

    .pf_card_rescueTime {
      position: absolute;
      left: 74px;
      top: 45px;
      font-size: 13px;
      color: #4C5E75;
    }

    .pf_card_noCode {
      position: absolute;
      left: 74px;
      top: 68px;
      font-size: 13px;
      color: #4C5E75;
    }

    .pf_card_rescueTimeText {
      position: absolute;
      right: 8px;
      top: 68px;
      font-size: 13px;
      color: #4C5E75;
    }

    .pf_card_diagnosis {
      position: absolute;
      display: flex;
      left: 8px;
      top: 100px;
      font-size: 14px;
      color: #111111;

      .card-rectangle-text {
        width: 100px;
        text-overflow: ellipsis;
        white-space: nowrap;
        overflow: hidden;
      }
    }

      .card-rectangle {
        position: absolute;
        width: 20px;
        height: 20px;
        left: 178px;
        top: 100px;
        font-size: 14px;
        color: #FFFFFF;
        background-color: #FF3939;
        border-radius: 2px;
        line-height: 20px;
        text-align: center;
      }
      .card-rectangle2 {
        position: absolute;
        width: 54px;
        height: 20px;
        right: 8px;
        top: 100px;
        font-size: 14px;
        color: #FFFFFF;
        background-color: #FDC13F;
        border-radius: 2px;
        line-height: 20px;
        text-align: center;
      }

    .pf_card_line {
      position: absolute;
      left: 8px;
      top: 127px;
      border: none;
      width: 250px;
      height: 1px;
      background-color: #DCDFE6;
    }

    .pf_card_nursingMeasuresString {
      position: absolute;
      width: 240px;
      left: 8px;
      top: 140px;
      color: #0EB396;
      font-size: 12px;
      text-overflow: ellipsis;
      white-space: nowrap;
      overflow: hidden;
    }

    .pf_card_specialArrangementString{
      position: absolute;
      width: 200px;
      left: 8px;
      top: 164px;
      color: #FF9F3B;
      font-size: 12px;
      text-overflow: ellipsis;
      white-space: nowrap;
      overflow: hidden;
    }

    .pf_card_btn{
      position: absolute;
      width: 46px;
      height: 20px;
      right: 8px;
      top: 164px;
      color: #0EB396;
      text-align: center;
      line-height: 20px;
      font-size: 12px;
      border-radius: 4px;
      background-color:#9CF6E6;
      cursor: pointer;
    }

    .pf_card_discharge {
      position: absolute;
      width: 24px;
      height: 20px;
      top: 10px;
      left: 242px;
      font-size: 14px;
      text-align: center;
      line-height: 20px;
      color: white;
      border-top-left-radius: 50%;
      border-bottom-left-radius: 50%;
      background-color: #0EB396;
    }

    .pf_card_emptyBed_img {
      position: absolute;
      left: 72px;
      top: 16px
    }

    .pf_card_emptyBed_text {
      position: absolute;
      width: 100%;
      font-size: 18px;
      font-weight: bold;
      color: #0EB396;
      text-align: center;
      top: 152px;
    }
  }
</style>
