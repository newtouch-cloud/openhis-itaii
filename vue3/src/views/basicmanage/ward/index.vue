<template>
  <div style="display: flex; justify-content: space-between" class="app-container">
    <el-card style="width: 30%">
      <template #header>
        <span style="vertical-align: middle">病区</span>
      </template>
      <div style="width: 100%">
        <el-button type="primary" @click="open = true" class="mb8"> 新增 </el-button>
        <el-button style="float: right" @click="getWardList()" class="mb8" icon="refresh" />
        <el-table
          max-height="630"
          :data="wardList"
          @cell-click="(row) => clickRow(row, 10)"
          highlight-current-row
        >
          <el-table-column label="病区" align="center" prop="name" />
          <el-table-column label="病区号" align="center" prop="startTime">
            <template #default="scope">
              {{ getLastPartOfString(scope.row.busNo) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template #default="scope">
              <el-button type="primary" link @click.stop="handleEdit(scope.row)"> 编辑 </el-button>
              <el-button type="danger" link @click="handleDelete(scope.row)"> 删除 </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
    <div style="width: 69%">
      <el-card style="margin-bottom: 20px">
        <template #header>
          <span style="vertical-align: middle">病房</span>
        </template>
        <el-table
          height="320"
          :data="houseList"
          @cell-click="(row) => clickRow(row, 11)"
          highlight-current-row
          v-loading="loading"
        >
          <el-table-column label="病房" align="center" prop="name" />
          <el-table-column label="病房号" align="center" prop="busNo">
            <template #default="scope">
              {{ getLastPartOfString(scope.row.busNo) }}
            </template>
          </el-table-column>
          <el-table-column label="状态" align="center" prop="statusEnum_enumText" />
          <el-table-column label="操作" align="center">
            <template #default="scope">
              <el-button type="primary" link @click.stop="handleEdit(scope.row, 4)">
                编辑
              </el-button>
              <el-button type="danger" link @click="handleDelete(scope.row)"> 删除 </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
      <el-card>
        <template #header>
          <span style="vertical-align: middle">床位</span>
        </template>
        <el-table height="280" :data="bedList" v-loading="loading" width="">
          <el-table-column label="病床" align="center" prop="name" />
          <el-table-column label="病床号" align="center" prop="busNo">
            <template #default="scope">
              {{ getLastPartOfString(scope.row.busNo) }}
            </template>
          </el-table-column>
          <el-table-column label="状态" align="center" prop="statusEnum_enumText" />
          <el-table-column label="操作" align="center">
            <template #default="scope">
              <el-button type="primary" link @click.stop="handleEdit(scope.row, 10)">
                编辑
              </el-button>
              <el-button type="danger" link @click="handleDelete(scope.row)"> 删除 </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
    <el-dialog :title="title" v-model="open" width="600px" @close="cancel" append-to-body>
      <el-form ref="orgRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="id" prop="id" v-show="false">
          <el-input v-model="form.id" placeholder="请输入科室编号" />
        </el-form-item>
        <el-form-item label="科室编号" prop="busNo" v-show="false">
          <el-input v-model="form.busNo" placeholder="请输入科室编号" />
        </el-form-item>
        <el-form-item :label="type + '分类'" prop="formEnum">
          <el-radio-group v-model="form.formEnum" @change="handleRadioChange" :disabled="isEdit">
            <el-radio :label="4">病区</el-radio>
            <el-radio :label="10">病房</el-radio>
            <el-radio :label="11">床位</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="type + '名称'" prop="name">
          <el-input v-model="form.name" placeholder="请输入科室名称" />
        </el-form-item>
        <el-col>
          <el-form-item :label="upLabel" prop="busNoParent">
            <template v-if="form.formEnum == 4">
              <el-tree-select
                clearable
                style="width: 100%"
                v-model="form.busNoParent"
                filterable
                :data="organization"
                :props="{
                  value: 'id',
                  label: 'name',
                  children: 'children',
                }"
                value-key="id"
                check-strictly
                placeholder="请选择上级科室/病区/床位"
              />
            </template>
            <template v-else>
              <el-select
                v-model="form.busNoParent"
                placeholder="请选择上级科室/病区/床位"
                clearable
                filterable
                style="width: 100%"
              >
                <el-option
                  v-for="item in wardListOption"
                  :key="item.busNo"
                  :label="item.name"
                  :value="item.busNo"
                />
              </el-select>
            </template>
          </el-form-item>
        </el-col>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
    
<script setup name="Ward">
import { getList, addLocation, getOrgList, deleteLocation, editLocation } from './components/api';
const { proxy } = getCurrentInstance();
const queryParams = ref({
  pageNum: 1,
  pageSize: 50,
  formEnum: 4,
  // locationFormList: [4],
});
const type = ref('病区');
const wardList = ref([]);
const bedList = ref([]);
const houseList = ref([]);
const wardListOption = ref([]);
const organization = ref([]);
const loading = ref(false);
const isEdit = ref(false);
const open = ref(false);
const form = reactive({
  formEnum: 4,
});
const upLabel = ref('关联科室');
const title = ref('新增');
const rules = ref({
  name: [
    { required: true, message: '请输入科室名称', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' },
  ],
  busNoParent: [
    {
      required: form.formEnum != 4,
      message: '请选择上级' + type.value,
      trigger: 'blur',
    },
  ],
});
/**
 * 患者列表
 */
function getWardList() {
  queryParams.value.formEnum = 4;
  getList(queryParams.value).then((res) => {
    wardList.value = res.data.records;
  });
}

function init() {
  getOrgList().then((res) => {
    organization.value = res.data.records;
  });
}

function handleRadioChange(val) {
  if (val == 4) {
    type.value = '病区';
    upLabel.value = '关联科室';
    return;
  } else if (val == 10) {
    type.value = '病房';
    upLabel.value = '所属病区';
    queryParams.value.formEnum = 4;
  } else {
    type.value = '床位';
    upLabel.value = '所属病房';
    queryParams.value.formEnum = 10;
  }
  getList(queryParams.value).then((res) => {
    wardListOption.value = res.data.records;
  });
}

/**
 * 点击患者列表行 获取处方列表
 */
function clickRow(row, val) {
  loading.value = true;
  queryParams.value.formEnum = val;
  queryParams.value.busNo = row.busNo;
  getList(queryParams.value).then((res) => {
    if (val == 10) {
      houseList.value = res.data.records;
    } else if (val == 11) {
      bedList.value = res.data.records;
    }
    setTimeout(() => {
      queryParams.value.busNo = undefined;
      loading.value = false;
    }, 100);
  });
}

function submitForm() {
  if (form.busNoParent) {
    if (form.formEnum == 4) {
      form.organizationId = form.busNoParent;
    } else {
      form.busNo = form.busNoParent;
    }
  }
  console.log(form);
  if (!isEdit.value) {
    addLocation(form).then((res) => {
      if (res.code == 200) {
        proxy.$modal.msgSuccess('操作成功');
        cancel();
        getWardList();
      }
    });
  } else {
    editLocation(form).then((res) => {
      if (res.code == 200) {
        proxy.$modal.msgSuccess('操作成功');
        cancel();
        getWardList();
      }
    });
  }
}

function handleDelete(row) {
  deleteLocation(row.busNo).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess('删除成功');
    }
  });
}

function getLastPartOfString(str) {
  const parts = str.split('.');
  return parts.pop();
}

function handleEdit(row, val) {
  form.id = row.id;
  form.name = row.name;
  form.formEnum = row.formEnum;
  form.busNo = row.busNo;
  if (row.organizationId) {
    form.busNoParent = row.organizationId;
  } else {
    form.busNoParent = row.busNo.split('.').slice(0, -1).join('.');
  }
  isEdit.value = true;
  title.value = '编辑';
  if (val) {
    queryParams.value.formEnum = val;
    getList(queryParams.value).then((res) => {
      wardListOption.value = res.data.records;
    });
  }
  open.value = true;
}

function cancel() {
  open.value = false;
  form.id = undefined;
  form.name = '';
  form.formEnum = 4;
  form.busNo = '';
  form.busNoParent = '';
  form.organizationId = '';
  isEdit.value = false;
  title.value = '新增';
}

init();
getWardList();
</script>

<style scoped>
</style>