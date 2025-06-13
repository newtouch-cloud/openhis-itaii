<template>
  <div class="drugDistribution-container">
    <patientList />
    <DrugDistributionList />
  </div>
</template>
<script setup >
import patientList from './patientList.vue'
import DrugDistributionList from './drugDistributionList.vue'
</script>
<style lang="scss" scoped>
.drugDistribution-container {
  display: flex;
  height: 100%;
  width: 100%;

  .patientList-container {
    flex: none;
    width: 240px;
  }

  .medicalOrderList-container {
    flex: 1;
  }
}
</style>