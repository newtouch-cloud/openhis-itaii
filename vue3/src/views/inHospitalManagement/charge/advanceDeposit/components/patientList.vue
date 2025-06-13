<template>
  <el-drawer v-model="drawerVisible" :title="title" :before-close="handleClose" size="70%">
    <div class="drawer-content">
      <!-- 筛选区域 -->
      <el-form :inline="true" :model="queryParams" class="filter-form">
        <el-form-item label="在院病区" prop="wardLocationId">
          <el-select v-model="queryParams.wardLocationId" placeholder="请选择" clearable style="width:160px">
            <el-option
              v-for="ward in wardListOptions"
              :key="ward.value"
              :label="ward.label"
              :value="ward.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="searchKey">
          <el-input
            v-model="queryParams.searchKey"
            placeholder="请输入流水号/住院号/患者姓名"
            style="width: 230px"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getList">查询</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格区域 -->
      <el-table
        :data="patientData"
        style="width: 100%"
        class="patient-table"
        @row-click="handleRowClick"
        :row-class-name="tableRowClassName"
      >
        <el-table-column prop="wardLocationId_dictText" label="病区" min-width="180" />
        <el-table-column prop="bedLocationId_dictText" label="床号" min-width="100" />
        <el-table-column prop="patientName" label="姓名" min-width="120" />
        <el-table-column prop="admissionNo" label="住院号" min-width="120" />
        <!-- <el-table-column prop="statusEnum_enumText" label="票据状态" min-width="120" /> -->
        <el-table-column label="操作" min-width="100">
          <template #default="scope">
            <el-button link type="primary" size="small" @click.stop="viewPatient(scope.row)"
              >查看</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页区域 -->
      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNo"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </div>
  </el-drawer>
</template>

<script setup >
import { defineModel, defineProps, defineEmits, reactive, ref } from 'vue';
import { getPatientInfoPage } from './api';

// Props
const props = defineProps({
  title: {
    type: String,
    default: '患者列表',
  },
  drawerVisible: {
    type: Boolean,
    default: false,
  },
  wardListOptions: {
    type: Array,
    default: () => [],
  },
});

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    searchKey: undefined,
    wardLocationId: undefined,
  },
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);
// Emits
const emits = defineEmits(['confirm', 'cancel', 'patientSelected']);
const total = ref(0);
// // v-model for drawer visibility
// const drawerVisible = defineModel('drawerVisible', {
//   type: Boolean,
//   default: false,
// })
const drawerVisible = ref(false);
// 筛选表单
const filterForm = reactive({
  ward: '综合外科病区',
  searchText: '',
});

// 表格数据
const allPatientData = reactive([
  {
    id: 1,
    ward: '综合外科病区',
    bedNumber: '18床',
    name: '张明月',
    admissionNumber: '100034',
    masterIndex: '100034',
  },
  {
    id: 2,
    ward: '综合外科病区',
    bedNumber: '+2床',
    name: '王敏',
    admissionNumber: '109872',
    masterIndex: '109872',
  },
  {
    id: 3,
    ward: '综合外科病区',
    bedNumber: '9床',
    name: '王丽芬',
    admissionNumber: '109872',
    masterIndex: '109872',
  },
  {
    id: 4,
    ward: '综合外科病区',
    bedNumber: '10床',
    name: '古力娜扎',
    admissionNumber: '109872',
    masterIndex: '109872',
  },
  {
    id: 5,
    ward: '综合外科病区',
    bedNumber: '3床',
    name: '王新月',
    admissionNumber: '109872',
    masterIndex: '109872',
  },
  {
    id: 6,
    ward: '综合外科病区',
    bedNumber: '+1床',
    name: '张明敏',
    admissionNumber: '109872',
    masterIndex: '109872',
  },
  {
    id: 7,
    ward: '综合外科病区',
    bedNumber: '走10床',
    name: '胡欣妮',
    admissionNumber: '109872',
    masterIndex: '109872',
  },
  {
    id: 8,
    ward: '综合外科病区',
    bedNumber: '11床',
    name: '张明红',
    admissionNumber: '109872',
    masterIndex: '109872',
  },
  {
    id: 9,
    ward: '综合外科病区',
    bedNumber: '12床',
    name: '刘邦',
    admissionNumber: '109872',
    masterIndex: '109872',
  },
  {
    id: 10,
    ward: '综合外科病区',
    bedNumber: '13床',
    name: '诸葛亮',
    admissionNumber: '109872',
    masterIndex: '109872',
  },
  {
    id: 11,
    ward: '综合外科病区',
    bedNumber: '14床',
    name: '曹操',
    admissionNumber: '109872',
    masterIndex: '109872',
  },
  {
    id: 12,
    ward: '综合外科病区',
    bedNumber: '15床',
    name: '司马懿',
    admissionNumber: '109872',
    masterIndex: '109872',
  },
  {
    id: 13,
    ward: '综合外科病区',
    bedNumber: '16床',
    name: '孙权',
    admissionNumber: '109872',
    masterIndex: '109872',
  },
  {
    id: 14,
    ward: '综合外科病区',
    bedNumber: '20床',
    name: '周瑜',
    admissionNumber: '109872',
    masterIndex: '109872',
  },
  // 添加更多数据以测试分页
  {
    id: 15,
    ward: '内科病区',
    bedNumber: '1床',
    name: '患者A',
    admissionNumber: '200001',
    masterIndex: '200001',
  },
  {
    id: 16,
    ward: '内科病区',
    bedNumber: '2床',
    name: '患者B',
    admissionNumber: '200002',
    masterIndex: '200002',
  },
]);

const patientData = ref([...allPatientData.slice(0, 10)]); // 当前页显示的数据
const totalPatients = ref(allPatientData.length);
const currentPage = ref(1);
const pageSize = ref(10); // 和图片中每页大约10-15条类似，这里设为10
const wardListOptions = ref([]);
const selectedPatient = ref(null); // 用于存储选中的患者

// 行高亮相关
const tableRowClassName = ({ row }) => {
  if (selectedPatient.value && row.id === selectedPatient.value.id) {
    return 'selected-row';
  }
  return '';
};

const handleRowClick = (row) => {
  selectedPatient.value = row;
  // console.log('Selected patient:', row);
};

const handlePageChange = (page) => {
  currentPage.value = page;
  const start = (page - 1) * pageSize.value;
  const end = start + pageSize.value;
  // 重新从筛选后的或全部数据中获取当前页
  const sourceData = allPatientData.filter((p) => {
    const wardMatch = filterForm.ward ? p.ward === filterForm.ward : true;
    const textMatch = filterForm.searchText
      ? p.patientName.includes(filterForm.searchText) ||
        p.admissionNumber.includes(filterForm.searchText) ||
        p.masterIndex.includes(filterForm.searchText)
      : true;
    return wardMatch && textMatch;
  });
  patientData.value = sourceData.slice(start, end);
};

/**
 * 查看患者信息
 *
 * @param patient 患者对象
 */
function viewPatient(patient) {
  console.log('View patient:', patient);
  selectedPatient.value = patient; // 选中患者
  emits('patientSelected', selectedPatient.value); // 发送选中的患者数据
  drawerVisible.value = false;
  reset(); // 重置筛选条件
  // 可以在这里做一些操作，比如高亮行，或者如果“查看”是选择并关闭，则触发confirm
}

// Drawer methods
function handleClose() {
  drawerVisible.value = false;
  reset();
}

const cancelClick = () => {
  drawerVisible.value = false;
  emits('cancel');
};

const confirmClick = () => {
  if (selectedPatient.value) {
    emits('patientSelected', selectedPatient.value); // 发送选中的患者数据
  }
  emits('confirm'); // 触发通用的confirm事件
  drawerVisible.value = false;
};

/**
 * 打开抽屉
 */
function show() {
  console.log('show', props);
  wardListOptions.value = props.wardListOptions;
  drawerVisible.value = props.drawerVisible;
  getList();
}

/**
 * 取得病人列表
 */
function getList() {
  console.log('queryParams', queryParams.value);
  getPatientInfoPage(queryParams.value).then((res) => {
    if (res.data && res.data.records && res.data.records.length > 0) {
      // form.value = JSON.parse(JSON.stringify(res.data.records[0]));
    }
    console.log(res, '=================================');
    patientData.value = res.data.records;
    total.value = res.data.total;
    console.log(res, '================================= ');
  });
}
function reset() {
  queryParams.value = {
    pageNo: 1,
    pageSize: 10,
    searchKey: undefined,
    wardLocationId: undefined,
  };
}
defineExpose({
  show,
});
</script>

<style lang="scss" scoped>
.drawer-content {
  // padding: 0px 20px 20px 20px; // 顶部不需要padding，让筛选栏贴近
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
}

.filter-form {
  padding: 15px 0;
  :deep(.el-form-item) {
    margin-bottom: 0; // 减少筛选表单项的下边距
  }
}

.patient-table {
  flex-grow: 1; // 让表格占据剩余空间
  margin-bottom: 20px;
  :deep(.el-table__header th) {
    background-color: #f0f2f5; // 表头背景色，更接近图片
    color: #333;
    font-weight: normal;
  }
  :deep(.el-table__row) {
    cursor: pointer;
  }
  :deep(.selected-row) {
    background-color: #ecf5ff !important; // Element Plus 选中行高亮颜色
  }
}

.pagination-container {
  padding: 0; // 移除分页组件的默认padding
  margin-top: auto; // 将分页组件推到底部
}
</style>
