<template>
  <div class="pf-card-group">
    <PfPatientCard
      v-for="item in cardList"
      :key="item.bedId"
      :data="item"
      :bed-config="bedConfig"
      @click="clickAct"
      @moreClick="moreClickAct"
    />
  </div>
</template>
<script>
import PfPatientCard from './PfPatientCard'
export default {
  name: 'PfPatientCardB',
  components: { PfPatientCard },
  provide() {
    return {
      PfPatientCards: this
    }
  },
  props: {
    cardList: {
      type: Array,
      default() {
        return []
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
      activePatient: { noCode: '' }
    }
  },
  mounted() {
    if (this.cardList.length > 0) {
      this.$nextTick(() => {
        this.activePatient.noCode = this.cardList[0].noCode
      })
    }
  },
  methods: {
    clickAct(data) {
      this.$emit('itemClick', data)
    },
    moreClickAct(data) {
      this.$emit('itemMoreClick', data)
    }
  }
}
</script>
<style scoped>
  .pf-card-group {
    display: grid;
    grid-template-columns: repeat(auto-fill, 264px);
    margin: 12px;
    grid-gap: 12px;
  }
</style>
