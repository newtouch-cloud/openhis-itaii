import mycomp from './index.vue'
/* istanbul ignore next */
mycomp.install = function(Vue) {
  Vue.component(mycomp.name, mycomp)
}
export default mycomp
