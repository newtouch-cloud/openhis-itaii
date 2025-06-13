<template>
  <div class="medicalOrderList-container">
    <div class="search-container">
      <el-space :size="4" direction="vertical" class="search-content" alignment="left
      ">
        <el-space :size="4">
          <el-radio-group v-model="searchForm.drugType">
            <el-radio-button label="西药" value="1" />
            <el-radio-button label="中药" value="2" />
            <el-radio-button label="整取药" value="3" />
          </el-radio-group>
          <el-radio-group v-model="searchForm.isDetails">
            <el-radio-button label="明细" value="1" />
            <el-radio-button label="汇总" value="2" />
          </el-radio-group>
          <el-select v-model="searchForm.orderClass" v-if="searchForm.drugType == '1'" placeholder="医嘱类型"
            style="width: 100px">
            <el-option label="全部" value="0" />
            <el-option label="长期" value="1" />
            <el-option label="临时" value="2" />
          </el-select>
          <el-select v-model="searchForm.department" placeholder="领药科室" style="width: 140px">
            <el-option label="全部" value="0" />
            <el-option label="儿科" value="1" />
            <el-option label="神经内科" :value="2" />
          </el-select>
          <el-date-picker v-model="searchForm.endTime" v-if="searchForm.drugType == '1'" type="date" placeholder="截止时间"
            style="width: 100px" />
          <el-input v-model="searchForm.searchVal" v-if="searchForm.drugType == '1'" style="width: 140px"
            placeholder="请输入名称" />
          <el-button type="primary">查询</el-button>
        </el-space>
        <el-space :size="4" alignment="right" style="align-items: center; ">
          <span>默认打印</span>
          <el-checkbox v-model="searchForm.printDetails" label="明细" />
          <el-checkbox v-model="searchForm.printSummary" label="汇总" />
          <el-button type="primary">领药发送</el-button>
        </el-space>
      </el-space>
    </div>
    <div class="medicalOrderList-table">
      <el-table :data="drugDistributionDetailData" v-if="searchForm.isDetails == '1'" row-key="id"
        style="width: 100%; height: 100%" border :span-method="arraySpanMethod" show-overflow-tooltip>
        <el-table-column type="selection" />
        <el-table-column label="类型" prop="name" min-width=" 60px">
          <template #default="{ row }">
            <span v-if="!row.children">长期</span>
            <template v-else>
              {{ row.name }} 11 床【000000001】 &nbsp; 林俊杰 女/24 重 &nbsp;1级 自费 主诊断 医生：医生甲&nbsp; 预交金：1990.3
            </template>
          </template>
        </el-table-column>

        <el-table-column label="医嘱内容" prop="content" min-width="220px">
          <template #default="{ row }">
            <span v-if="row.children">{{ row.content }}</span>
          </template>
        </el-table-column>
        <el-table-column label="用法/频次" prop="gender" min-width="120px"
          v-if="searchForm.drugType == '1' || searchForm.drugType == '3'">
          <template #default="{ row }">
            <span>注射/qd</span>
          </template>
        </el-table-column>
        <el-table-column label="领药数量" prop="gender" min-width="120px" v-if="searchForm.drugType == '3'">
          <template #default="{ row }">
            <el-input-number v-model="row.num" :min="1" width="80px" style="width: 100px;" />
          </template>
        </el-table-column>
        <el-table-column label="领药总量" prop="gender" min-width="100px"
          v-if="searchForm.drugType == '1' || searchForm.drugType == '2'">
          <template #default="{ row }">
            <span>2 支</span>
          </template>
        </el-table-column>
        <el-table-column label="参考金额" prop="gender" min-width="100px">
          <template #default="{ row }">
            <span>20.00</span>
          </template>
        </el-table-column>
        <el-table-column label="取药科室" prop="gender" min-width="140px">
          <template #default="{ row }">
            <span>儿科</span>
          </template>
        </el-table-column>
        <el-table-column label="领药次数" prop="gender" fixed="right" min-width="340px" v-if="searchForm.drugType == '1'">
          <template #default="{ row }">
            <template v-if="!row.children">
              <div v-for="item in row.dates" :key="item.id">
                <span>{{ item.date }}</span>
                <span v-for="time in item.times" :key="time.id">
                  <el-checkbox v-model="time.checked" :label="`${time.time}`" border />
                </span>
              </div>

            </template>
            <template v-else>
            </template>
          </template>
        </el-table-column>
      </el-table>
      <el-table :data="drugDistributionSummaryData" v-else row-key="id" style="width: 100%; height: 100%" border
        :span-method="arraySpanMethod" show-overflow-tooltip>
        <el-table-column type="selection" />
        <el-table-column label="医嘱内容" prop="content" min-width="220px">
          <template #default="{ row }">
            <span v-if="!row.children">{{ row.content }}</span>
            <template v-else>
              **药房
            </template>
          </template>
        </el-table-column>
        <el-table-column label="领药数量" prop="gender" min-width="120px">
          <template #default="{ row }">
            <span>2 支</span>
          </template>
        </el-table-column>
        <el-table-column label="单价" prop="gender" min-width="100px">
          <template #default="{ row }">
            <span>20.00</span>
          </template>
        </el-table-column>
        <el-table-column label="参考金额" prop="gender" min-width="140px">
          <template #default="{ row }">
            <span>40.00</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script setup >
import { ref } from 'vue';
const searchForm = ref({
  drugType: '1',
  isDetails: '1',
  orderClass: '0',
  department: '0',
  printDetails: false,
  printSummary: false,
  searchVal: '',
  endTime: '',
})
const drugDistributionDetailData = ref([
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
  // console.log(row, column, rowIndex, columnIndex);
  // 如果是父级行
  if (row.children && row.children.length > 0) {
    if (columnIndex === 0) {
      return [1, 1]
    }
    // 如果是患者列
    if (columnIndex === 1) {
      return [1, 16]
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
const drugDistributionSummaryData = ref([
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
      }, {
        name: '',
        age: 10,
        gender: '男',
        content: '维生素B12注射液【1ml：0.5mg*1】  ',
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
      }, {
        name: '',
        age: 10,
        gender: '男',
        content: '维生素B12注射液【1ml：0.5mg*1】  ',
      }]
  }
])
</script>
<style lang="scss" scoped>
.medicalOrderList-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  overflow: hidden;

  .search-container {
    flex: none;
    padding: 8px 8px;
    border-bottom: 1px solid #e4e7ed;

    :deep(.search-content) {
      width: 100%;

      .el-space__item:nth-child(2) {
        align-self: end;
      }

    }
  }

  .medicalOrderList-table {
    flex: 1;

    :deep(.el-table__indent) {
      display: none !important;
    }

    :deep(.el-table__placeholder) {
      display: none !important;
    }
  }
}
</style>