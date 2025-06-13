<template>
  <div>
    <el-row :gutter="24">
      <el-col :span="4" :xs="24">
        <el-input
          v-model="diagnosis"
          placeholder="诊断名称"
          clearable
          style="width: 100%; margin-bottom: 10px"
          @keyup.enter="queryDiagnosisUse"
        >
          <template #append>
            <el-button icon="Search" @click="queryDiagnosisUse" />
          </template>
        </el-input>
        <el-tree
          ref="treeRef"
          :data="tree"
          node-key="id"
          :props="{ label: 'name', children: 'children' }"
          highlight-current
          default-expand-all
          :filter-node-method="filterNode"
          @node-click="handleNodeClick"
        >
          <template #default="{ node, data }">
            <div class="custom-tree-node">
              <span>{{ node.label }}</span>
              <span class="tree-node-actions">
                <template v-if="node.level === 1 && data.name != '常用' && data.name != '历史'">
                  <el-button
                    style="color: #000000"
                    type="text"
                    size="small"
                    @click.stop="addChild(data)"
                  >
                    <el-icon><Plus /></el-icon>
                  </el-button>
                </template>
                <el-popconfirm
                  width="200"
                  :hide-after="10"
                  title="确认删除此常用诊断吗"
                  placement="top-start"
                  @confirm="deleteChild(data)"
                >
                  <template #reference>
                    <el-button
                      style="color: #000000"
                      v-if="
                        node.level === 2 &&
                        node.parent.data.name != '常用' &&
                        node.parent.data.name != '历史'
                      "
                      type="text"
                      size="small"
                      @click.stop=""
                    >
                      <el-icon><Minus /></el-icon>
                    </el-button>
                  </template>
                </el-popconfirm>
              </span>
            </div>
          </template>
        </el-tree>
      </el-col>
      <el-col :span="20" :xs="24">
        <div style="margin-bottom: 10px">
          <el-button type="primary" plain @click="handleAddDiagnosis()"> 新增诊断 </el-button>
          <el-button type="primary" plain @click="handleSaveDiagnosis()"> 保存诊断 </el-button>
          <el-button type="primary" plain @click="handleImport()"> 导入慢性病诊断 </el-button>
        </div>

        <el-form :model="form" :rules="rules" ref="formRef">
          <el-table ref="diagnosisTableRef" :data="form.diagnosisList" height="650">
            <el-table-column label="序号" type="index" width="50" />
            <el-table-column label="诊断排序" align="center" prop="diagSrtNo" width="180">
              <template #default="scope">
                <el-form-item
                  :prop="`diagnosisList.${scope.$index}.diagSrtNo`"
                  :rules="rules.diagSrtNo"
                >
                  <el-input-number
                    v-model="scope.row.diagSrtNo"
                    controls-position="right"
                    :controls="false"
                    style="width: 80px"
                  />
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="诊断类别" align="center" prop="diagSrtNo" width="180">
              <template #default="scope">
                <el-form-item
                  :prop="`diagnosisList.${scope.$index}.medTypeCode`"
                  :rules="rules.medTypeCode"
                >
                  <el-select v-model="scope.row.medTypeCode" placeholder=" " style="width: 150px">
                    <el-option
                      v-for="item in med_type"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="诊断名称" align="center" prop="name">
              <template #default="scope">
                <el-form-item :prop="`diagnosisList.${scope.$index}.name`" :rules="rules.name">
                  <el-popover
                    :popper-style="{ padding: '0' }"
                    placement="bottom-start"
                    :visible="scope.row.showPopover"
                    trigger="manual"
                    :width="800"
                  >
                    <diagnosislist
                      :diagnosisSearchkey="diagnosisSearchkey"
                      @selectDiagnosis="handleSelsectDiagnosis"
                    />
                    <template #reference>
                      <el-input
                        v-model="scope.row.name"
                        placeholder="请选择诊断"
                        @input="handleChange"
                        @focus="handleFocus(scope.row, scope.$index)"
                        @blur="handleBlur(scope.row)"
                      />
                    </template>
                  </el-popover>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="医保码" align="center" prop="ybNo" />
            <el-table-column label="诊断类型" align="center" prop="maindiseFlag">
              <template #default="scope">
                <el-checkbox
                  label="主诊断"
                  :trueLabel="1"
                  :falseLabel="0"
                  v-model="scope.row.maindiseFlag"
                  border
                  size="small"
                  @change="(value) => handleMaindise(value, scope.$index)"
                />
                <el-select
                  v-model="scope.row.verificationStatusEnum"
                  placeholder=" "
                  style="width: 40%; padding-bottom: 5px; padding-left: 10px"
                  size="small"
                >
                  <el-option
                    v-for="item in diagnosisOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="180">
              <template #default="scope">
                <el-button link type="primary" @click="handleDeleteDiagnosis(scope.$index)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form>
      </el-col>
    </el-row>
    <diagnosisdialog
      :openDiagnosis="openDiagnosis"
      @close="closeDiagnosisDialog"
      :radio="orgOrUser"
    />
  </div>
</template>

<script setup>
import { getCurrentInstance } from 'vue';
import {
  getConditionDefinitionInfo,
  saveDiagnosis,
  diagnosisInit,
  deleteDiagnosisBind,
  getEncounterDiagnosis,
  getEmrDetail,
  getChronicDisease,
} from './api';
import diagnosisdialog from './diagnosisdialog.vue';
import diagnosislist from './diagnosislist.vue';
// const diagnosisList = ref([]);
const allowAdd = ref(false);
const tree = ref([]);
const openDiagnosis = ref(false);
const diagnosisSearchkey = ref('');
const diagnosisOptions = ref([]);
const rowIndex = ref();
const diagnosis = ref();
const orgOrUser = ref();
const form = ref({
  diagnosisList: [],
});
const props = defineProps({
  patientInfo: {
    type: Object,
    required: true,
  },
});
const emits = defineEmits(['diagnosisSave']);
const { proxy } = getCurrentInstance();
const { med_type } = proxy.useDict('med_type');
const rules = ref({
  name: [{ required: true, message: '请选择诊断', trigger: 'change' }],
  medTypeCode: [{ required: true, message: '请选择诊断类型', trigger: 'change' }],
  diagSrtNo: [{ required: true, message: '请输入诊断序号', trigger: 'change' }],
});

watch(
  () => form.value.diagnosisList,
  () => {
    emits('diagnosisSave', false);
  },
  { deep: true }
);

function getDetail(encounterId) {
  getEmrDetail(encounterId).then((res) => {
    allowAdd.value = res.data ? true : false;
  });
}

function getList() {
  getEncounterDiagnosis(props.patientInfo.encounterId).then((res) => {
    if (res.code == 200) {
      form.value.diagnosisList = res.data;
      emits('diagnosisSave', false);
      console.log(form.value.diagnosisList);
    }
  });

  getTree();
}

init();
function init() {
  diagnosisInit().then((res) => {
    if (res.code == 200) {
      diagnosisOptions.value = res.data.verificationStatusOptions;
    }
  });
}

function handleImport() {
  if (props.patientInfo.contractName != '自费') {
    // 获取患者慢性病信息
    getChronicDisease({ encounterId: props.patientInfo.encounterId }).then((res) => {
      if (res.data.length > 0) {
        res.data.forEach((item, index) => {
          form.value.diagnosisList.push({
            ...item,
            ...{
              medTypeCode: '140104',
              verificationStatusEnum: 4,
              definitionId: item.id,
              diagSrtNo: form.value.diagnosisList.length + 1,
            },
          });
        });
      }
    });
  }
}

/**
 * 添加子节点
 */
function addChild(data) {
  orgOrUser.value = data.name;
  openDiagnosis.value = true;
}

/**
 * 删除子节点
 */
function deleteChild(data) {
  deleteDiagnosisBind(data.id).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess('删除成功');
      getTree();
    }
  });
}
watch(diagnosis, (val) => {
  proxy.$refs['treeRef'].filter(val);
});

/** 通过条件过滤节点  */
const filterNode = (value, data) => {
  console.log('filterNode', value, data);

  if (!value) return true;
  return data.name.indexOf(value) !== -1;
};

/**
 * 获取诊断树列表
 */
function getTree() {
  getConditionDefinitionInfo(props.patientInfo ? props.patientInfo.patientId : '').then((res) => {
    if (res.code == 200) {
      let list = [];
      list = res.data.patientHistoryList;
      list.children = [];
      // 手动构造树列表;
      tree.value[0] = {
        id: '1',
        name: '历史',
        children: list,
      };
      tree.value[1] = {
        id: '2',
        name: '常用',
        children: res.data.doctorCommonUseList,
      };
      tree.value[2] = {
        id: '3',
        name: '个人',
        children: res.data.userPersonalList,
      };
      tree.value[3] = {
        id: '4',
        name: '科室',
        children: res.data.organizationList,
      };
      console.log(tree.value);
    }
  });
}

/**
 * 添加诊断
 */
function handleAddDiagnosis() {
  proxy.$refs.formRef.validate((valid) => {
    if (valid) {
      if (!allowAdd.value) {
        proxy.$modal.msgWarning('请先填写病历');
        return;
      }
      form.value.diagnosisList.push({
        showPopover: false,
        name: undefined,
        verificationStatusEnum: 4,
        diagSrtNo: form.value.diagnosisList.length + 1,
      });
      if (form.value.diagnosisList.length == 1) {
        form.value.diagnosisList[0].maindiseFlag = 1;
      }
    }
  });
}

/**
 * 删除诊断
 */
function handleDeleteDiagnosis(index) {
  form.value.diagnosisList.splice(index, 1);
}

function handleMaindise(value, index) {
  if (value == 1) {
    let flag = 0;
    form.value.diagnosisList.forEach((item) => {
      console.log(item);
      if (item.maindiseFlag == 1) {
        flag++;
      }
    });
    if (flag > 1) {
      form.value.diagnosisList[index].maindiseFlag = 0;
      proxy.$modal.msgWarning('只能有一条主诊断');
    }
  }
}

/**
 * 保存诊断
 */
function handleSaveDiagnosis() {
  proxy.$refs.formRef.validate((valid) => {
    if (valid) {
      if (form.value.diagnosisList.length == 0) {
        proxy.$modal.msgWarning('诊断不能为空');
        return false;
      } else if (!form.value.diagnosisList.some((diagnosis) => diagnosis.maindiseFlag === 1)) {
        proxy.$modal.msgWarning('至少添加一条主诊断');
      } else {
        saveDiagnosis({
          patientId: props.patientInfo.patientId,
          encounterId: props.patientInfo.encounterId,
          diagnosisChildList: form.value.diagnosisList,
        }).then((res) => {
          if (res.code == 200) {
            getTree();
            emits('diagnosisSave', false);
            proxy.$modal.msgSuccess('诊断已保存');
          }
        });
      }
    }
  });
}

/**
 * 关闭诊断弹窗
 */
function closeDiagnosisDialog(str) {
  if (str === 'success') {
    proxy.$modal.msgSuccess('操作成功');
  }
  openDiagnosis.value = false;
  getTree();
}

function queryDiagnosisUse(value) {}

function handleChange(value) {
  diagnosisSearchkey.value = value;
}
/**
 * 选择诊断并赋值到列表
 */
function handleSelsectDiagnosis(row) {
  console.log(row);
  form.value.diagnosisList[rowIndex.value].ybNo = row.ybNo;
  form.value.diagnosisList[rowIndex.value].name = row.name;
  form.value.diagnosisList[rowIndex.value].definitionId = row.id;
}
/**获取焦点时 打开列表 */
function handleFocus(row, index) {
  rowIndex.value = index;
  row.showPopover = true;
}
/**失去焦点时 关闭列表 */
function handleBlur(row) {
  row.showPopover = false;
}

function handleNodeClick(data) {
  console.log(data.children);
  // 检查节点是否为根节点
  if (data.children != undefined) {
    // 如果是根节点，不执行任何操作
    return;
  }
  if (!allowAdd.value) {
    proxy.$modal.msgWarning('请先填写病历');
    return;
  }
  const isDuplicate = form.value.diagnosisList.some(
    (diagnosis) => diagnosis.ybNo === data.ybNo || diagnosis.name === data.name
  );
  if (isDuplicate) {
    proxy.$modal.msgWarning('该诊断项已存在');
    return;
  }
  form.value.diagnosisList.push({
    ybNo: data.ybNo,
    name: data.name,
    verificationStatusEnum: 4,
    definitionId: data.id,
  });
  if (form.value.diagnosisList.length == 1) {
    form.value.diagnosisList[0].maindiseFlag = 1;
  }
}

defineExpose({ getList, getDetail, handleSaveDiagnosis });
</script>

<style lang="scss" scoped>
.el-checkbox.is-bordered.el-checkbox--small {
  background-color: #ffffff;
}
.custom-tree-node {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.tree-node-actions {
  display: flex;
  align-items: center;
}
</style>