// 表格上滑滚轮滚动条横向滚动，在只有横向滚动条时使用
export default {
    mounted: (el) => {
      const container = el.querySelector('.el-scrollbar__wrap')
      if (!container) return
  
      const handleWheel = (e) => {
        // 阻止默认事件
        e.preventDefault()
        const delta = e.deltaY || e.detail || (-e.wheelDelta)
        container.scrollLeft += delta * 0.6
      }

      container.addEventListener('wheel', handleWheel, { passive: false })
      el._horizontalScrollCleanup = () => container.removeEventListener('wheel', handleWheel)
    },
    unmounted: (el) => {
      el._horizontalScrollCleanup?.()
    }
  }