<template>
  <div class="diagnose-container">
    <!-- 常用诊断、个人诊断、科室诊断、历史诊断、 -->
    <diagnose-folder :folder="mockData" :level="0" />
    <div class="diagnose-main">
      <div class="operate-btns">
        <el-space>
          <el-button type="primary" @click="addNewWestern">开立诊断</el-button>
          <el-button type="primary">既往诊断</el-button>
          <!-- 患者诊断 -->
          <el-button type="danger" @click="addNewChinese">中医诊断</el-button>
        </el-space>
      </div>
      <div class="diagnoseData-container">
        <el-table
          :data="diagnoseData"
          border
          row-key="id"
          style="width: 100%; height: 100%"
          highlight-current-row
        >
          <el-table-column type="selection" fixed="left" width="40" />
          <el-table-column prop="date" label="诊断类型" width="180" sortable />
          <el-table-column prop="name" label="诊断名称" width="180" />
          <el-table-column prop="address" label="主诊" />
          <el-table-column prop="address" label="复诊" />
          <el-table-column prop="address" label="疑似" />
          <el-table-column prop="address" label="传染" />
          <el-table-column prop="address" label="入院病情" width="180" />
          <el-table-column prop="address" label="转归" width="180" />
          <el-table-column prop="address" label="转归日期" width="180" />
          <el-table-column prop="address" label="诊断科室" width="180" />
          <el-table-column prop="address" label="诊断医师" width="180" />
          <el-table-column prop="address" label="诊断日期" width="180" />
          <el-table-column fixed="right" label="操作" width="120">
            <template #default="props">
              <el-space>
                <el-tooltip content="删除" placement="bottom">
                  <el-icon @click="deleteDiagnose(row)"><Delete /></el-icon>
                </el-tooltip>
                <el-tooltip
                  content="下移"
                  placement="bottom"
                  v-if="props.$index !== diagnoseData.length - 1"
                >
                  <el-icon @click="download(props.row)"><Download /></el-icon>
                </el-tooltip>
                <el-tooltip content="上移" placement="bottom" v-if="props.$index !== 0">
                  <el-icon @click="upload(props.row)"><Upload /></el-icon>
                </el-tooltip>
                <el-tooltip content="置顶" placement="bottom" v-if="props.$index !== 0">
                  <el-icon @click="top(props.row)"><Top /></el-icon>
                </el-tooltip>
                <el-tooltip
                  content="置底"
                  placement="bottom"
                  v-if="props.$index !== diagnoseData.length - 1"
                >
                  <el-icon @click="bottom(props.row)"><Bottom /></el-icon>
                </el-tooltip>
              </el-space>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <WesternMedicineDialog v-model:visible="WesternMedicineDialogVisible" />
    <ChineseMedicineDialog v-model:visible="ChineseMedicineDialogVisible" />
  </div>
</template>
<script setup >
import { getCurrentInstance, onBeforeMount, onMounted, reactive, ref } from 'vue'
// const { proxy } = getCurrentInstance()
// const emits = defineEmits([])
// const props = defineProps({})
import DiagnoseFolder from './diagnoseFolder.vue'
import WesternMedicineDialog from './westernMedicineDialog.vue'
import ChineseMedicineDialog from './chineseMedicineDialog.vue'
const diagnoseData = ref([
  {
    id: 1,
    sort: 1,
    name: '新冠',
  },
  {
    id: 2,
    sort: 2,
    name: '新冠as',
  },
  {
    id: 3,
    sort: 3,
    name: '新冠12',
  },
  {
    id: 4,
    sort: 4,
    name: '新冠2121',
  },
  {
    id: 5,
    sort: 5,
    name: '新冠12',
  },
  {
    id: 6,
    sort: 6,
    name: '新冠21',
  },
])

// 模拟数据
const mockData = ref([
  {
    name: '常用',
    children: [
      {
        name: '文件夹 1',
        children: [
          {
            name: '霍乱',
          },
          {
            name: '新型冠状病毒新型冠状病毒新型冠状病毒',
          },
        ],
      },
      {
        name: '文件夹 2',
        children: [
          {
            name: '普外科',
          },
          {
            name: '骨科',
          },
        ],
      },
      {
        name: '新型冠状病毒',
      },
    ],
  },
  {
    name: '科室',
    children: [
      {
        name: '内科',
        children: [
          {
            name: '呼吸内科',
          },
          {
            name: '消化内科',
          },
        ],
      },
      {
        name: '外科',
        children: [
          {
            name: '普外科',
          },
          {
            name: '骨科',
          },
        ],
      },
      {
        name: '儿科',
      },
    ],
  },
  {
    name: '个人',
    children: [
      {
        name: '内科',
        children: [
          {
            name: '呼吸内科',
          },
          {
            name: '消化内科',
          },
        ],
      },
      {
        name: '外科',
        children: [
          {
            name: '普外科',
          },
          {
            name: '骨科',
          },
        ],
      },
      {
        name: '儿科',
      },
    ],
  },
  {
    name: '历史',
    children: [
      {
        name: '心率失常',
      },
      {
        name: '心率失常',
      },
      {
        name: '心率失常',
      },
    ],
  },
])
const state = reactive({})
onBeforeMount(() => {})
onMounted(() => {})
defineExpose({ state })

// const deleteDiagnose = (row: any) => {
//   // TODO 删除
//   console.log(row)
// }

// const download = (row: any) => {
//   // TODO 删除
// }

// const upload = (row: any) => {
//   // TODO 删除
// }

// const top = (row: any) => {
//   // TODO 删除
// }

// const bottom = (row: any) => {
//   // TODO 删除
// }

const addNewWestern = () => {
  WesternMedicineDialogVisible.value = true
}
const addNewChinese = () => {
  ChineseMedicineDialogVisible.value = true
}
const WesternMedicineDialogVisible = ref(false)
const ChineseMedicineDialogVisible = ref(false)
</script>
<style lang="scss" scoped>
.diagnose-container {
  height: 100%;
  display: flex;
  .folder-container {
    width: 200px;
    flex: none;
  }
  .diagnose-main {
    width: 300px;
    flex: auto;
    display: flex;
    flex-direction: column;
    .operate-btns {
      height: 44px;
      display: flex;
      align-items: center;
      flex: none;
    }
    .diagnoseData-container {
      flex: auto;
    }
  }
}
</style>
