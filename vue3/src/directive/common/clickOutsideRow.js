const clickOutsideRow = {
  mounted(el, binding) {
    const rowSelector = binding.arg || '.expend_div' // 允许自定义行选择器
    const handler = (e) => {
      // 判断点击的是否为行元素且在表格容器内
      const rowEl = e.target.closest(rowSelector)
      const isInside = rowEl && el.contains(rowEl)
      if (!isInside) {
        binding.value(e) // 触发回调
      }
    }

    el._clickOutsideHandler = handler
    document.addEventListener('click', handler)
  },
  unmounted(el) {
    document.removeEventListener('click', el._clickOutsideHandler)
    delete el._clickOutsideHandler
  }
}

export default clickOutsideRow