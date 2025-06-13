<!--
 * @Author: sjjh
 * @Date: 2025-04-16 20:54:48
 * @Description:
-->
<template>
  <div class="medicalOrderManagement-container">
    <div class="medicalOrderManagement-operate">
      <el-space :size="4">
        <el-select v-model="searchForm.patientStatus" placeholder="执行状态" style="width: 100px">
          <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <el-select v-model="searchForm.selectedType" placeholder="医嘱类型" style="width: 100px">
          <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <el-select v-model="searchForm.executionType" placeholder="执行单类型" style="width: 100px">
          <el-option v-for="item in executionTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <el-input v-model="searchForm.searchVal" style="width: 100px" placeholder="医嘱名称" />
        <!-- 增加预计执行时间 时间范围筛选 -->
        <el-date-picker v-model="searchForm.timeRange" type="daterange" range-separator="至" start-placeholder="开始日期"
          end-placeholder="结束日期" style="width: 200px" />
        <el-button type="primary">查询</el-button>
        <el-button type="danger">不执行</el-button>
        <el-checkbox v-model="searchForm.isPrintExecution">执行单打印</el-checkbox>
        <el-checkbox v-model="searchForm.isPrintLabel">瓶贴打印</el-checkbox>
        <el-button type="primary">执行</el-button>
        <el-button type="">皮试结果</el-button>
      </el-space>
      <el-space :size="4">

      </el-space>
    </div>
    <div class="medicalOrderManagement-table">
      <el-table :data="medicalOrderManagementData" row-key="id" style="width: 100%; height: 100%" border
        :span-method="arraySpanMethod">
        <el-table-column type="selection" />

        <el-table-column label="类型" prop="name">
          <template #default="{ row }">
            <span v-if="!row.children">长期/西药</span>
            <template v-else>
              {{ row.name }}
              11 床【000000001】 &nbsp; 林俊杰 女/24 重 &nbsp;1级 自费 主诊断 医生：医生甲&nbsp; 预交金：1990.3
            </template>
          </template>

        </el-table-column>

        <el-table-column label="医嘱内容" prop="content">
          <template #default="{ row }">
            <span v-if="row.children">{{ row.content }}</span>
          </template>
        </el-table-column>
        <el-table-column label="开始终止" prop="gender">
          <template #default="{ row }">
            <div v-if="!row.children">
              <div> 05/04 07:00 </div>
              <div> 05/25 07:00 </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="执行时间" prop="gender" min-width="200px">
          <template #default="{ row }">
            <template v-if="!row.children">

              <div v-for="item in row.dates" :key="item.id">
                <span>{{ item.date }}</span>
                <span v-for="time in item.times" :key="time.id">
                  <el-checkbox v-model="time.checked" :label="`${time.time + time.nurse}`" border />
                </span>
              </div>

            </template>
            <template v-else>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script setup >
import { onBeforeMount, onMounted, reactive, ref } from 'vue'
// const { proxy } = getCurrentInstance()
// const emits = defineEmits([])
// const props = defineProps({})
const state = reactive({})
onBeforeMount(() => { })
onMounted(() => { })
defineExpose({ state })
const medicalOrderManagementData = ref([
  {
    id: 1,
    name: '张三',
    age: 20,
    gender: '男',

    children: [
      {
        name: '',
        age: 10,
        gender: '男',
        content: '维生素B12注射液【1ml：0.5mg*1】  ',
        dates: [{
          date: '05/04',
          times: [{
            time: '08:00',
            nurse: '刘护士',
            checked: false
          }, {
            time: '12:00',
            nurse: '刘护士',
            checked: false
          }]
        }, {
          date: '05/05',
          times: [{
            time: '08:00',
            nurse: '刘护士',
            checked: false
          }, {
            time: '12:00',
            nurse: '刘护士',
            checked: false
          }]
        }]
      }, {
        name: '',
        age: 10,
        gender: '男',
        content: '维生素B12注射液【1ml：0.5mg*1】  ',
        dates: [{
          date: '05/04',
          times: [{
            time: '08:00',
            nurse: '刘护士',
            checked: false
          }, {
            time: '12:00',
            nurse: '刘护士',
            checked: false
          }]
        }, {
          date: '05/05',
          times: [{
            time: '08:00',
            nurse: '刘护士',
            checked: false
          }, {
            time: '12:00',
            nurse: '刘护士',
            checked: false
          }]
        }]
      }]
  },
  {
    id: 2,
    name: '李四',
    age: 20,
    gender: '男',

    children: [
      {
        name: '',
        age: 10,
        gender: '男',
        content: '维生素B12注射液【1ml：0.5mg*1】  ',
        dates: [{
          date: '05/04',
          times: [{
            time: '08:00',
            nurse: '刘护士',
            checked: false
          }, {
            time: '12:00',
            nurse: '刘护士',
            checked: false
          }]
        }, {
          date: '05/05',
          times: [{
            time: '08:00',
            nurse: '刘护士',
            checked: false
          }, {
            time: '12:00',
            nurse: '刘护士',
            checked: false
          }]
        }]
      }, {
        name: '',
        age: 10,
        gender: '男',
        content: '维生素B12注射液【1ml：0.5mg*1】  ',
        dates: [{
          date: '05/04',
          times: [{
            time: '08:00',
            nurse: '刘护士',
            checked: false
          }, {
            time: '12:00',
            nurse: '刘护士',
            checked: false
          }]
        }, {
          date: '05/05',
          times: [{
            time: '08:00',
            nurse: '刘护士',
            checked: false
          }, {
            time: '12:00',
            nurse: '刘护士',
            checked: false
          }]
        }]
      }]
  }
])

const arraySpanMethod = ({
  row,
  column,
  rowIndex,
  columnIndex,
}: any) => {
  console.log(row, column, rowIndex, columnIndex);
  // 如果是父级行
  if (row.children && row.children.length > 0) {
    if (columnIndex === 0) {
      return [1, 1]
    }
    // 如果是患者列
    if (columnIndex === 1) {
      return [1, 6]
    }
    else {
      return [1, 0]
    }
  }
  // 如果是子级行，显示其他列
  if (!row.children || row.children.length == 0) {
    return [1, 1]
  }
  // 如果是父级行，隐藏其他列
  return [1, 1]
}
const statusOptions = [
  { value: '未执行', label: '未执行' },
  { value: '已执行', label: '已执行' },
  { value: '不执行', label: '不执行' },
  { value: '取消执行', label: '取消执行' }
]
const typeOptions = [
  { value: '全部', label: '全部' },
  { value: '长期', label: '长期' },
  { value: '临时', label: '临时' }
]
const executionTypeOptions = [
  { value: '全部', label: '全部' },
  { value: '口服单', label: '口服单' },
  { value: '输液单', label: '输液单' },
  { value: '针剂单', label: '针剂单' },
  { value: '雾化单', label: '雾化单' },
  { value: '治疗单', label: '治疗单' },
  { value: '其他', label: '其他' }
]
const searchForm = reactive({
  searchVal: '',
  patientStatus: '未执行',
  selectedType: '全部',
  timeRange: [],
  executionType: '全部',
  isPrintExecution: true,
  isPrintLabel: true
})

</script>
<style lang="scss" scoped>
.medicalOrderManagement-container {
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;

  .medicalOrderManagement-operate {
    height: 49px;
    width: 100%;
    flex: none;
    align-items: center;
    border-bottom: 1px solid #e8e8e8;
    padding: 8px;
  }

  .medicalOrderManagement-table {
    height: 44px;
    width: 100%;
    flex: 1;
    padding: 8px;
  }
}
</style>
