<!--
 * @Author: sjjh
 * @Date: 2025-04-07 23:47:54
 * @Description:
-->
<template>
  <div class="advice-container">
    <div class="operate-btns">
      <el-space>
        <el-button type="primary" @click="addNew">新增</el-button>
        <el-button type="primary">签发</el-button>
        <el-button type="danger">撤回</el-button>
        <el-button type="danger">作废</el-button>
        <el-button type="danger">停止</el-button>
        <el-button>复制</el-button>
        <el-button>粘贴</el-button>
      </el-space>
    </div>
    <div class="operate-btns">
      <el-space>
        <el-radio-group v-model="searchForm.orderType">
          <el-radio-button label="全部" value="New York" />
          <el-radio-button label="长期" value="Washington" />
          <el-radio-button label="临时" value="Los Angeles" />
        </el-radio-group>
        <el-select v-model="searchForm.orderClassCode" placeholder="医嘱类型" style="width: 240px">
          <el-option
            v-for="item in typeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <el-select v-model="searchForm.orderStatus" placeholder="医嘱状态" style="width: 240px">
          <el-option
            v-for="item in statusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-space>
    </div>
    <div class="orderTableData-container">
      <el-table
        :data="orderTableData"
        border
        row-key="id"
        style="width: 100%; height: 100%"
        highlight-current-row
        :expand-row-keys="expandOrder"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="date" label="状态" width="180" sortable />
        <el-table-column prop="orderTypeName" label="类型" width="180" />
        <el-table-column prop="address" label="医嘱内容" />
        <el-table-column prop="address" label="时间" />
        <el-table-column prop="address" label="执行科室" />
        <el-table-column prop="address" label="停止、作废人/时间" width="180" />
        <el-table-column fixed="right" label="操作" width="120">
          <template #default="props">
            <el-button link type="primary" size="small" @click="editRow(props.row)">
              查看
            </el-button>
            <el-button link type="primary" size="small">编辑</el-button>
          </template>
        </el-table-column>
        <el-table-column type="expand" width="1" style="width: 0">
          <template #default="props">
            <div m="4">
              <p m="t-0 b-2">State: {{ props.row.state }}</p>
              <p m="t-0 b-2">City: {{ props.row.city }}</p>
              <p m="t-0 b-2">Address: {{ props.row.address }}</p>
              <p m="t-0 b-2">Zip: {{ props.row.zip }}</p>
              <h3>Family</h3>
              <el-button type="primary" @click="save">保存</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script setup >
import { getCurrentInstance, onBeforeMount, onMounted, reactive, ref } from 'vue'
// const { proxy } = getCurrentInstance()
// const emits = defineEmits([])
// const props = defineProps({})
import { orderTableData } from './useOrder'
import { ElMessage } from 'element-plus'
const state = reactive({})
onBeforeMount(() => {})
onMounted(() => {})
defineExpose({ state })

const typeOptions = [
  {
    value: 'Option1',
    label: '中药',
  },
  {
    value: 'Option2',
    label: '西药',
  },
  {
    value: 'Option3',
    label: '检查',
  },
  {
    value: 'Option4',
    label: '检验',
  },
  {
    value: 'Option5',
    label: '处置',
  },
]
const statusOptions = [
  {
    value: 'Option1',
    label: '暂存',
  },
  {
    value: 'Option2',
    label: '签署',
  },
  {
    value: 'Option3',
    label: '回退',
  },
  {
    value: 'Option4',
    label: '停止',
  },
  {
    value: 'Option5',
    label: '作废',
  },
]
const searchForm = reactive({
  orderType: '',
  orderClassCode: '',
  orderStatus: '',
})

const expandOrder = ref([]) //目前的展开行
const newId = ref('')
/* ==== 操作 */
// 新增
const addNew = () => {
  if (!newId.value) {
    newId.value = '1111112222'
    orderTableData.unshift({ id: '1111112222' })
    expandOrder.value = ['1111112222']
  } else {
    ElMessage({
      message: '请先操作当前编辑的医嘱！',
      type: 'warning',
    })
  }
}
// 新增
const editRow = (row) => {
  expandOrder.value = [row.id]
}
const save = (row) => {
  expandOrder.value = []
  newId.value = undefined
}
</script>
<style lang="scss" scoped>
.advice-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  .operate-btns {
    height: 44px;
    display: flex;
    align-items: center;
    flex: none;
  }
  .orderTableData-container {
    flex: auto;
    height: calc(100% - 88px);
  }
}
</style>
