<template>
  <div style="width: 100%">
    <div class="mb8">
      <el-button type="primary" plain @click="handleAddPrescription()" :disabled="buttonDisabled">
        添加绑定项目
      </el-button>
    </div>
    <el-form :model="form" :rules="rules" ref="formRef">
      <el-table
        ref="prescriptionRef"
        :data="form.consumablesList"
        row-key="patientId"
        border
        @selection-change="handleSelectionChange"
        v-loading="props.loading"
      >
        <el-table-column label="项目类型" align="center" prop="type" width="150">
          <template #default="scope">
            <span v-if="!scope.row.isEdit">{{ scope.row.type_dictText }}</span>
            <el-form-item v-else :prop="`consumablesList.${scope.$index}.type`" :rules="rules.type">
              <el-select v-model="scope.row.type" placeholder="" @change="handleTypeChange">
                <el-option v-if="props.tab == 1 || props.tab == 3" label="诊疗" value="1" />
                <el-option v-if="props.tab != 3" label="耗材" value="2" />
              </el-select>
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="项目名" align="center" prop="name">
          <template #default="scope">
            <span v-if="!scope.row.isEdit">{{ scope.row.name }}</span>
            <el-form-item v-else :prop="`consumablesList.${scope.$index}.name`" :rules="rules.name">
              <PopoverList @search="handleSearch" :width="800" :modelValue="scope.row.name">
                <template #popover-content="{}">
                  <DeviceList
                    v-if="scope.row.type == '2'"
                    @selectRow="(row) => selectRow(row, scope.$index)"
                    :searchKey="searchKey"
                  />
                  <ActivityList
                    v-else
                    @selectRow="(row) => selectRow(row, scope.$index)"
                    :searchKey="searchKey"
                  />
                </template>
              </PopoverList>
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="绑定数量" align="center" prop="quantity" width="250">
          <template #default="scope">
            <span v-if="!scope.row.isEdit">{{ scope.row.quantity }}</span>
            <el-form-item
              v-else
              :prop="`consumablesList.${scope.$index}.quantity`"
              :rules="rules.quantity"
            >
              <el-input v-model="scope.row.quantity" placeholder="" />
            </el-form-item>
          </template>
        </el-table-column>
        <!-- <el-table-column label="使用范围" align="center" prop="rangeCode" width="150">
          <template #default="scope">
            <span v-if="!scope.row.isEdit">{{ scope.row.rangeCode_dictText }}</span>
            <el-form-item
              v-else
              :prop="`consumablesList.${scope.$index}.rangeCode`"
              :rules="rules.rangeCode"
            >
              <el-input v-model="scope.row.rangeCode" placeholder="" />
            </el-form-item>
          </template>
        </el-table-column> -->
        <el-table-column label="范围" align="center" prop="rangeCode" width="250">
          <template #default="scope">
            <span v-if="!scope.row.isEdit">{{ scope.row.rangeCode_dictText }}</span>
            <el-form-item
              v-else
              :prop="`consumablesList.${scope.$index}.rangeCode`"
              :rules="rules.statusEnum"
            >
              <el-select v-model="scope.row.rangeCode" placeholder="">
                <el-option
                  v-for="dict in use_range"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="启用状态" align="center" prop="statusEnum" width="250">
          <template #default="scope">
            <span v-if="!scope.row.isEdit">{{ scope.row.statusEnum_dictText }}</span>
            <el-form-item
              v-else
              :prop="`consumablesList.${scope.$index}.statusEnum`"
              :rules="rules.statusEnum"
            >
              <el-select v-model="scope.row.statusEnum" placeholder="" @change="handleTypeChange">
                <el-option
                  v-for="item in statusOptions"
                  :key="item.value"
                  :label="item.info"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" fixed="right" width="250">
          <template #default="scope">
            <el-button
              v-if="!scope.row.isEdit"
              link
              type="primary"
              @click="handleEdit(scope.$index)"
            >
              编辑
            </el-button>
            <el-button
              v-if="scope.row.isEdit"
              link
              type="primary"
              @click="handleSave(scope.row, scope.$index)"
            >
              保存
            </el-button>
            <el-button
              v-if="scope.row.isEdit"
              link
              type="primary"
              @click="
                () => {
                  form.consumablesList[scope.$index].isEdit = false;
                  isAdd = true;
                  if (!scope.row.id) {
                    form.consumablesList.splice(scope.$index, 1);
                  }
                }
              "
            >
              取消
            </el-button>
            <el-button link type="primary" @click.stop="handleDelete(scope.row, scope.$index)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
  </div>
</template>
      
<script setup>
import { reactive, watch } from 'vue';
import { bind, deleteBind, init } from './api';
import PopoverList from '@/components/OpenHis/popoverList/index.vue';
import DeviceList from './deviceList.vue';
import ActivityList from './activityList.vue';

const emit = defineEmits(['refresh']);
const total = ref(0);
const queryParams = ref({});
const consumablesList = ref([]);
const searchKey = ref('');
const rowIndex = ref(-1);
const isAdd = ref(true);
const rules = ref({
  name: [{ required: true, message: '请选择耗材项', trigger: 'change' }],
  quantity: [{ required: true, message: '请输入绑定数量', trigger: 'blur' }],
  rangeCode: [{ required: true, message: '请选择使用范围', trigger: 'blur' }],
});
const form = reactive({
  consumablesList: [],
});
const statusOptions = ref([]);

const props = defineProps({
  bindList: {
    type: Object,
    required: false,
  },
  bindInfo: {
    type: Object,
    required: false,
  },
  tab: {
    type: String,
    required: false,
  },
  loading: {
    type: Boolean,
    required: false,
  },
});
const buttonDisabled = computed(() => {
  return !props.bindInfo.id;
});
watch(
  () => props.bindList,
  (newVal) => {
    form.consumablesList = newVal.map((item) => {
      return {
        id: item.id,
        name: item.deviceName ? item.deviceName : item.activityName,
        devActId: item.devActId,
        quantity: item.quantity,
        rangeCode: item.rangeCode,
        rangeCode_dictText: item.rangeCode_dictText,
        statusEnum: item.statusEnum,
        statusEnum_dictText: item.statusEnum_enumText,
        typeCode: item.typeCode,
        typeCode_dictText: item.typeCode_dictText,
        type: item.activityName ? '1' : '2',
        type_dictText: item.activityName ? '诊疗' : '耗材',
      };
    });
    isAdd.value = true;
  },
  { deep: true }
);
watch(
  () => props.tab,
  () => {
    form.consumablesList = [];
    isAdd.value = true;
  },
  { deep: true }
);

const { proxy } = getCurrentInstance();

const { use_range } = proxy.useDict('use_range');
function handleAddPrescription() {
  if (!isAdd.value) {
    proxy.$modal.msgWarning('请先保存当前行');
    return;
  }
  form.consumablesList.push({
    isEdit: true,
    // type: '2',
    rangeCode: '3',
    statusEnum: 2,
  });
  isAdd.value = false;
  console.log(form.consumablesList, 234567890);
}

function handleEdit(index) {
  form.consumablesList[index].isEdit = true;
}

function handleSave(row, index) {
  proxy.$refs['formRef'].validate((valid) => {
    if (valid) {
      console.log(row, 'row');
      bind({
        itemNo: props.bindInfo.id,
        devActId: row.devActId,
        typeCode: props.bindInfo.typeCode,
        rangeCode: row.rangeCode,
        quantity: row.quantity,
        statusEnum: row.statusEnum,
        id: row.id ? row.id : undefined,
        devActTable: row.type == '1' ? 'wor_activity_definition' : 'adm_device_definition',
      }).then((res) => {
        if (res.code === 200) {
          proxy.$modal.msgSuccess('操作成功');
          emit('refresh');
          isAdd.value = true;
        }
      });
      form.consumablesList[index].isEdit = false;
    }
  });
}

function handleDelete(row, index) {
  if (row.id) {
    proxy.$modal.confirm('确认删除当前绑定项目吗').then(() => {
      deleteBind(row.id).then((res) => {
        if (res.code == 200) {
          proxy.$modal.msgSuccess('删除成功');
          emit('refresh');
        }
      });
    });
  } else {
    form.consumablesList.splice(index, 1);
    isAdd.value = true;
  }
}

function initOptions() {
  init().then((res) => {
    statusOptions.value = res.data.statusOptions;
  });
}

function handleSearch(value) {
  searchKey.value = value;
}

initOptions();
function selectRow(row, index) {
  form.consumablesList[index].devActId = row.id;
  form.consumablesList[index].name = row.name;
}
</script>
    
<style scoped>
</style>