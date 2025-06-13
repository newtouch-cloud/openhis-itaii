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
        <el-button type="danger">退回</el-button>
        <el-button type="primary">通过校对</el-button>
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
              {{ row.name }} 11 床【000000001】 &nbsp; 林俊杰 女/24 重 &nbsp;1级 自费 主诊断 医生：医生甲&nbsp; 预交金：1990.3
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
        <el-table-column label="补费绑定" prop="gender">
          <template #default="{ row }">
            <el-select v-model="row.reimbursementBinding" placeholder="请选择" style="width: 100%">
              <el-option v-for="item in reimbursementOptions" :key="item.value" :label="item.label"
                :value="item.value" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="执行科室" prop="gender">

        </el-table-column>
        <el-table-column label="签发时间" prop="gender">

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

const reimbursementOptions = [
  { value: '首瓶8.5', label: '首瓶8.5' },
  { value: '未绑定', label: '未绑定' },
  { value: '待绑定', label: '待绑定' }
]
const arraySpanMethod = ({
  row,
  column,
  rowIndex,
  columnIndex,
}: any) => {
  // console.log(row, column, rowIndex, columnIndex);
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
  { value: '全部', label: '全部' },
  { value: '药品', label: '药品' },
  { value: '本院区', label: '本院区' },
  { value: '他科', label: '他科' },
  { value: '床头卡', label: '床头卡' }
]
const typeOptions = [
  { value: '全部', label: '全部' },
  { value: '长期', label: '长期' },
  { value: '临时', label: '临时' }
]
const executionTypeOptions = [
  { value: '未交对', label: '未交对' },
  { value: '已校对', label: '已校对' },
  { value: '已停止', label: '已停止' },
  { value: '已作废', label: '已作废' }
]
const searchForm = reactive({
  searchVal: '',
  patientStatus: '全部',
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
