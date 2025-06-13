<template>
  <div class="waitForDischarge-container">
    <patientList />
    <Review />
  </div>
</template>
<script setup >
import patientList from './patientList.vue'
import Review from './review.vue'
</script>
<style lang="scss" scoped>
.waitForDischarge-container {
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