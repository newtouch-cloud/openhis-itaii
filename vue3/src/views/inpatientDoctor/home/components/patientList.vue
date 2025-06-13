<!--
 * @Author:
 * @Description: 患者列表
-->
<template>
  <div class="patientList-container">
    <div class="patientList-operate" :class="{ 'patientList-operate-unexpand': !expand }">
      <el-space>
        <el-icon icon-class="Refresh" size="24" @click="queryPatientData">
          <Refresh />
        </el-icon>
        <el-icon class="svg-sty-menu" size="24" @click="updateExpand">
          <Expand v-if="!expand" />
          <Fold v-if="expand" />
        </el-icon>
      </el-space>
    </div>
    <div class="patientList-list" v-if="expand">
      <div class="search-operate">
        <!-- 在科 -->
        <el-input
          placeholder="床号/住院号/姓名"
          v-model="searchData.keyword"
          @keyup.enter="queryPatientData"
          :prefix-icon="Search"
        >
        </el-input>
      </div>
      <div class="patient-cards" v-loading="queryloading">
        <template v-if="filteredCardData.length > 0">
          <el-scrollbar ref="expandScrollbarRef" class="patient-cards-scrollbar">
            <patient-card
              v-model="cardId"
              :data="filteredCardData"
              :type="active"
              @change="cardChange"
            >
            </patient-card>
          </el-scrollbar>
        </template>
        <el-empty v-else description="暂无数据" />
      </div>
    </div>
    <div class="patientList-list" v-loading="queryloading" v-else>
      <el-scrollbar ref="contractScrollbarRef" class="patient-cards-scrollbar">
        <el-tooltip
          v-for="item in filteredCardData"
          :show-after="200"
          :key="item.id"
          :show-arrow="true"
          placement="right"
          effect="light"
          :offset="4"
        >
          <template #content>
            <div class="card-tooltip">
              <span class="f-16">{{ item.bedName }}</span>
              <span class="f-14">{{ item.name }}</span>
              <el-icon v-if="item.sexName === '女'" :size="24"> <Female /></el-icon>
              <el-icon v-else icon-class="headMale" :size="24"><Male /></el-icon>
            </div>
          </template>
          <div>
            <div
              class="card-small"
              :class="{ 'patient-active': cardId === item.id }"
              @click="smallCardClick(item)"
              :key="item.id"
            >
              {{ item.bedName }}
            </div>
            <div class="patient-card-small-border"></div>
          </div>
        </el-tooltip>
      </el-scrollbar>
    </div>
  </div>
</template>

<script  setup>
import { ref, reactive, onMounted, computed } from 'vue'
// import { store } from '@hip/portal'
import { patientInfo, updatePatientInfo } from '../store/patient'
import PatientCard from './patientCard/index.vue'
import { Search } from '@element-plus/icons-vue'
// const props = defineProps({
// })
const expand = defineModel('expand')
// const emit = defineEmits(['update:expand'])
const active = ref(0)
const searchData = reactive({
  keyword: '',
  patientType: 1,
  type: 1,
  timeLimit: 3,
})
// const { userInfo } = store.useGlobalStore()
// 卡片
const cardId = ref('')
// 所有卡片数据
const cardAllData = ref([
  {
    id: '1',
    name: '张三',
    sexName: '女',
    bedName: '1-1床',
    deptNurseName: '护士甲',
    crossDeptFlag: false,
    criticalCarePatientName: '危',
    inpatientCode: '1212121212',
    age: '30',
    admittedDoctorName: '医生乙',
  },
  {
    id: '2',
    name: '李四',
    sexName: '男',
    bedName: '1-2床',
    deptNurseName: '护士甲',
    crossDeptFlag: false,
    criticalCarePatientName: '重',
    inpatientCode: '1212121212',
    age: '30',
    admittedDoctorName: '医生乙',
  },
])
// 过滤后的卡片数据
const filteredCardData = computed(() => {
  // switch (active.value) {
  //   case 0:
  //     return cardAllData.value.filter((item: IInPatient) => {
  //       // const staffId = userInfo?.staffId
  //       // 在科患者-我的患者 住院/主治/主任医生只要有一个对应即可显示
  //       if (searchData.patientType === 1) {
  //         if (
  //           staffId !== item.directorDoctor &&
  //           staffId !== item.masterDoctor &&
  //           staffId !== item.admittedDoctor
  //         ) {
  //           return false
  //         }
  //       }
  //       // 在科患者-科室患者 全部/婴儿/非婴儿
  //       if (searchData.type === 2) {
  //         // 婴儿
  //         if (!(item.children && item.children.length > 0)) {
  //           return false
  //         }
  //       } else if (searchData.type === 3) {
  //         // 非婴儿
  //         if (item.children && item.children.length > 0) {
  //           return false
  //         }
  //       }
  //       // 关键字
  //       if (
  //         searchData.keyword &&
  //         !(
  //           item.bedName.includes(searchData.keyword) ||
  //           item.name.includes(searchData.keyword) ||
  //           item.inpatientCode.includes(searchData.keyword)
  //         )
  //       ) {
  //         return false
  //       }
  //       return true
  //     })
  //   default:
  //     return cardAllData.value
  // }
  return cardAllData.value
})

// 是否初始化激活状态
const isInitActive = ref(true)
// 展开患者列表Ref
const expandScrollbarRef = ref()
// 收缩患者列表Ref
const contractScrollbarRef = ref()
const queryloading = ref(false)

onMounted(() => {
  cardId.value = patientInfo.value?.id || ''
  queryPatientData()
})

/**
 * 滚动到选中位置
 * @param {*} value
 */
const scrollToSelected = () => {
  // 如果不是第一次定位，则不进行滚动
  if (!isInitActive.value) return
  const currentRef = props.expand === true ? expandScrollbarRef : contractScrollbarRef
  if (!currentRef.value) return
  const value = getSelectedOffsetTop(currentRef)
  currentRef.value.setScrollTop(value || 0)
  isInitActive.value = false
}

/**
 * 获取选中元素位置
 * @param {*} ref
 */
const getSelectedOffsetTop = (ref) => {
  const childrenNodeArray = ref?.value?.wrapRef?.children[0]?.children
  if (!childrenNodeArray?.length) return 0
  // TODO 当有子元素时候需要优化
  const targetNode = Array.from(childrenNodeArray).find((childrenNode) => {
    return childrenNode?.__vnode?.key === cardId.value
  })
  return targetNode?.offsetTop || 0
}

/**
 * 更新展开状态
 */
const updateExpand = () => {
  // emit('update:expand', !props.expand)
  expand.value = !expand.value
}

const cardChange = (val, node, parent) => {
  updatePatientInfo(node)
}

const smallCardClick = (val) => {
  cardId.value = val.id
  updatePatientInfo(val)
}

//  TODO 从后端获取数据
const queryPatientData = async () => {
  if (queryloading.value) return
  try {
    // queryloading.value = true
    // const res: IInPatient[] = []
    // // TODO 获取患者列表
    // // 设置patMiCode和patCode，实际是同一字段
    // res.forEach((item: IInPatient) => {
    //   item.patMiCode = item.patCode
    // })
    // cardAllData.value = res
    // scrollToSelected()
  } catch (error) {
    cardAllData.value = []
  } finally {
    queryloading.value = false
  }
}
</script>

<style lang="scss" scoped>
.patientList-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  height: 100%;
  border-right: 1px solid #ebeef5;
  background-color: #ffffff;
  .patientList-operate {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    height: 44px;
    padding: 0 8px;
    border-bottom: 1px solid #ebeef5;
    flex: none;
    &-unexpand {
      justify-content: space-around;
      padding: 0 8px;
    }
  }

  .patientList-list {
    display: flex;
    flex: 1;
    flex-direction: column;
    height: 0;

    .search-operate {
      padding: 0 8px;
      height: 48px;
      display: flex;
      align-items: center;
      flex: none;
    }

    .patient-cards {
      flex: 1;
      padding: 0 8px;

      overflow: hidden;

      :deep(.patient-cards-scrollbar) {
        width: 100%;
        height: 100%;

        .el-scrollbar__bar {
          width: 0;
        }
      }
    }
    .patient-active {
      background-color: var(--el-color-primary);
    }
    .card-small {
      height: 44px;
      padding-right: 4px;
      padding-left: 12px;
      overflow: hidden;
      font-size: 16px;
      line-height: 44px;
      white-space: nowrap;
      text-overflow: ellipsis;
      border-right: none;
      cursor: pointer;

      &-active {
        background-color: rgb(243, 252, 251);
      }

      // &:hover {
      //   background-color: #f8f8f8;
      // }
    }

    .patient-card-small-border {
      display: block;
      width: 100%;
      height: 2px;
      background-color: #f1faff;
    }
  }
}

.card-tooltip {
  display: inline-flex;
  align-items: center;
  height: 45px;
  padding: 0 10px;
}

.svg-gray {
  fill: var(--hip-color-text-unit);
}

:deep(.scrollbar) {
  width: 100%;
  height: 100%;

  .el-scrollbar__bar {
    width: 0;
  }
}

.f-16 {
  font-weight: 600;
  font-size: 16px;
}

.f-14 {
  font-size: 14px;
}

.empty-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;

  .empty-text-sty {
    margin-top: 0;
  }
}
</style>
