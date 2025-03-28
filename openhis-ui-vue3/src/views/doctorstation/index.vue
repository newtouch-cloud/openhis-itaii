<template>
  <div
    style="
      display: flex;
      justify-content: flex-start;
      align-items: flex-start;
      height: 85vh;
      width: 100%;
    "
  >
    <el-tabs
      type="border-card"
      v-model="activeTab"
      style="width: 30%; height: 100%; margin: 20px 10px 0px 20px"
    >
      <el-tab-pane label="待诊" name="wait">
        <patientlist
          @cellClick="cellClick"
          @toCurrent="toCurrent"
          :status="1"
        />
      </el-tab-pane>
      <el-tab-pane label="现诊" name="current">
        <patientlist
          @cellClick="cellClick"
          @toCurrent="toCurrent"
          :status="2"
        />
      </el-tab-pane>
      <el-tab-pane label="完诊" name="finish">
        <patientlist
          @cellClick="cellClick"
          @toCurrent="toCurrent"
          :status="2"
        />
      </el-tab-pane>
      <el-tab-pane label="暂离" name="leave">
        <patientlist
          @cellClick="cellClick"
          @toCurrent="toCurrent"
          :status="2"
        />
      </el-tab-pane>
    </el-tabs>
    <el-tabs
      type="border-card"
      addable
      style="width: 100%; height: 100%; margin: 20px 10px 0px 0px"
    >
      <el-tab-pane label="病历" v-loading="emrLoading">
        <div style="margin-bottom: 15px; padding-left: 40px">
          <el-row :gutter="24">
            <el-col :span="12">
              <el-descriptions>
                <el-descriptions-item label="门诊号"
                  >1234567890</el-descriptions-item
                >
                <el-descriptions-item label="就诊科室"
                  >内科</el-descriptions-item
                >
                <el-descriptions-item label="支付类型"
                  >全自费</el-descriptions-item
                >
              </el-descriptions>
            </el-col>
            <el-col :span="12">
              <el-button
                type="primary"
                @click="addEmr()"
                style="margin-left: 20px"
                :disabled="buttonDisabled"
                >保存病历</el-button
              >
              <el-button
                type="primary"
                @click="handleEmrTemplate()"
                style="margin-left: 20px"
                :disabled="buttonDisabled"
                >病历模板</el-button
              >
              <el-button
                type="primary"
                @click="handleSaveTemplate()"
                style="margin-left: 20px"
                :disabled="buttonDisabled"
                >另存模板</el-button
              >
              <el-button
                type="primary"
                @click="handleEmrHistory()"
                style="margin-left: 20px"
                :disabled="buttonDisabled"
                >历史病历</el-button
              >
              <el-button
                type="primary"
                @click="handleReceive()"
                style="margin-left: 20px"
                :disabled="buttonDisabled"
                >打印病历</el-button
              >
            </el-col>
          </el-row>
        </div>
        <el-form ref="emrRef" :model="form" :rules="rules" label-width="80px">
          <el-row :gutter="24">
            <el-col :span="6">
              <el-form-item label="身高" prop="height" style="width: 100%">
                <el-input
                  placeholder=""
                  v-model="form.height"
                  class="input-with-bottom-border"
                >
                  <template #suffix>cm</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="体重" prop="weight" style="width: 100%">
                <el-input
                  placeholder=""
                  v-model="form.weight"
                  class="input-with-bottom-border"
                >
                  <template #suffix>kg</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="体温" prop="temperature" style="width: 100%">
                <el-input
                  placeholder=""
                  v-model="form.temperature"
                  class="input-with-bottom-border"
                >
                  <template #suffix>℃</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="脉搏" prop="pulse" style="width: 100%">
                <el-input
                  placeholder=""
                  v-model="form.pulse"
                  class="input-with-bottom-border"
                >
                  <template #suffix>次/分</template>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="24">
            <!-- <el-col :span="6">
              <el-form-item
                label="血糖"
                prop="chiefComplaint"
                style="width: 100%"
              >
                <el-input placeholder="" class="input-with-bottom-border">
                  <template #suffix></template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item
                label="血糖"
                prop="chiefComplaint"
                style="width: 100%"
              >
                <el-input placeholder="" class="input-with-bottom-border">
                  <template #suffix>mmol/L</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item
                label="收缩压"
                prop="chiefComplaint"
                style="width: 100%"
              >
                <el-input placeholder="" class="input-with-bottom-border">
                  <template #suffix>mmHg</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item
                label="舒张压"
                prop="chiefComplaint"
                style="width: 100%"
              >
                <el-input placeholder="" class="input-with-bottom-border">
                  <template #suffix>mmHg</template>
                </el-input>
              </el-form-item>
            </el-col> -->
          </el-row>
          <el-row :gutter="24">
            <el-col :span="18">
              <el-form-item label="主诉" prop="chiefComplaint">
                <el-input
                  v-model="form.chiefComplaint"
                  type="textarea"
                  :rows="1"
                  placeholder=""
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="发病日期" prop="onsetDate">
                <el-date-picker
                  v-model="form.onsetDate"
                  type="date"
                  size="default"
                  style="width: 100% !important"
                  placement="bottom"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="现病史" prop="currentIllnessHistory">
            <el-input
              v-model="form.currentIllnessHistory"
              type="textarea"
              :rows="2"
              placeholder=""
            />
          </el-form-item>
          <el-form-item label="既往史" prop="pastMedicalHistory">
            <el-input
              v-model="form.pastMedicalHistory"
              type="textarea"
              :rows="2"
              placeholder=""
            />
          </el-form-item>
          <el-form-item label="月经史" prop="menstrualHistory">
            <el-input
              v-model="form.menstrualHistory"
              type="textarea"
              :rows="2"
              placeholder=""
            />
          </el-form-item>
          <el-form-item label="过敏史" prop="allergyHistory">
            <el-input
              v-model="form.allergyHistory"
              type="textarea"
              :rows="2"
              placeholder=""
            />
          </el-form-item>
          <el-form-item label="查体" prop="physicalExamination">
            <el-input
              v-model="form.physicalExamination"
              type="textarea"
              :rows="2"
              placeholder=""
            />
          </el-form-item>
          <el-form-item label="处理" prop="treatment">
            <el-input
              v-model="form.treatment"
              type="textarea"
              :rows="2"
              placeholder=""
            />
          </el-form-item>
          <el-form-item label="辅助检查" prop="auxiliaryExamination">
            <el-input
              v-model="form.auxiliaryExamination"
              type="textarea"
              :rows="2"
              placeholder=""
            />
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="诊断" v-loading="emrLoading">
        <el-row :gutter="24">
          <el-col :span="4" :xs="24">
            <el-input
              v-model="form.phone"
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
              :data="tree"
              :props="{ label: 'name', children: 'children' }"
              highlight-current
              default-expand-all
              @node-click="handleNodeClick"
            >
              <template #default="{ node, data }">
                <span class="custom-tree-node">
                  <span>{{ node.label }}</span>
                  <span class="tree-node-actions">
                    <el-button
                      style="color: #000000"
                      v-if="
                        node.level === 1 &&
                        data.name != '常用' &&
                        data.name != '历史'
                      "
                      type="text"
                      size="mini"
                      @click.stop="addChild(data)"
                    >
                      <el-icon><Plus /></el-icon>
                    </el-button>
                    <el-button
                      style="color: #000000"
                      v-if="
                        node.level === 2 &&
                        node.parent.data.name != '常用' &&
                        node.parent.data.name != '历史'
                      "
                      type="text"
                      size="mini"
                      @click.stop="deleteChild(data)"
                    >
                      <el-icon><Minus /></el-icon>
                    </el-button>
                  </span>
                </span>
              </template>
            </el-tree>
          </el-col>
          <el-col :span="20" :xs="24">
            <div style="margin-bottom: 10px">
              <el-button type="primary" plain @click="handleAddDiagnosis()">
                新增诊断
              </el-button>
              <el-button type="primary" plain @click="handleSaveDiagnosis()">
                保存诊断
              </el-button>
            </div>
            <el-table
              ref="diagnosisTableRef"
              :data="diagnosisList"
              @cell-click="clickRow"
              @selection-change="handleSelectionChange"
              height="650"
            >
              <el-table-column label="序号" type="index" width="50" />
              <el-table-column label="诊断名称" align="center" prop="name">
                <template #default="scope">
                  <el-popover
                    :popper-style="{ padding: '0' }"
                    placement="bottom-start"
                    :visible="scope.row.showPopover"
                    trigger="manual"
                    :width="500"
                  >
                    <diagnosislist
                      :diagnosisSearchkey="diagnosisSearchkey"
                      @selsectDiagnosis="handleSelsectDiagnosis"
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
                </template>
              </el-table-column>
              <el-table-column label="医保码" align="center" prop="ybNo" />
              <el-table-column
                label="诊断类型"
                align="center"
                prop="maindiseFlag"
              >
                <template #default="scope">
                  <el-checkbox
                    label="主诊断"
                    v-model:value="scope.row.maindiseFlag"
                    border
                    size="small"
                    @change="(value) => handleMaindise(value, scope.$index)"
                  />
                  <el-select
                    v-model="scope.row.verificationStatusEnum"
                    placeholder=" "
                    style="width: 40%; padding-bottom: 5px; padding-left: 10px"
                    size="small"
                    @change="handle"
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
              <el-table-column label="操作" align="center">
                <template #default="scope">
                  <el-button
                    link
                    type="primary"
                    @click="handleDeleteDiagnosis(scope.$index)"
                    >删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="处方" v-loading="emrLoading">
        <prescriptionlist :patientInfo="patientInfo" />
      </el-tab-pane>
      <template #addIcon>
        <!-- 特殊操作展示用户信息 -->
        <el-button class="plain-button">
          {{
            patientInfo
              ? patientInfo.patientName +
                " / " +
                patientInfo.age +
                " / " +
                patientInfo.genderEnum_enumText
              : ""
          }}
        </el-button>
      </template>
    </el-tabs>
    <el-dialog
      :title="emrTitle"
      v-model="openEmrTemplate"
      width="600px"
      append-to-body
      destroy-on-close
    >
      <emrtemplate
        v-if="showDialog == 'emrTemplate'"
        @selectRow="templateSelect"
      />
      <emrhistory v-if="showDialog == 'emrHistory'" />
      <div v-if="showDialog == 'saveTemplate'">
        <span>模板名称：<el-input v-model="templateName" /></span>
        <el-radio-group v-model="radio">
          <el-radio :label="1">个人</el-radio>
          <el-radio :label="2">科室</el-radio>
          <el-radio :label="3">全院</el-radio>
        </el-radio-group>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submit">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
    <diagnosisdialog
      :openDiagnosis="openDiagnosis"
      @close="closeDiagnosisDialog"
      :radio="orgOrUser"
    />
  </div>
</template>

<script setup name="doctorstation">
import patientlist from "./components/patientlist.vue";
import {
  saveEmr,
  getEmrDetail,
  saveEmrTemplate,
  getConditionDefinitionInfo,
  saveDiagnosis,
  diagnosisInit,
  deleteDiagnosisBind,
} from "./components/api";
import prescriptionlist from "./components/prescriptionlist.vue";
import emrtemplate from "./components/emrtemplate.vue";
import emrhistory from "./components/emrhistory.vue";
import diagnosisdialog from "./components/diagnosisdialog.vue";
import diagnosislist from "./components/diagnosislist.vue";
import { computed, ref } from "vue";
import { data } from "province-city-china/data";

const activeTab = ref("wait");
const form = ref({});
const patientInfo = ref();
const { proxy } = getCurrentInstance();
const openEmrTemplate = ref(false);
const templateName = ref("");
const showDialog = ref("");
const emrTitle = ref("");
const emrLoading = ref(false);
const diagnosisList = ref([]);
const openDiagnosis = ref(false);
const tree = ref([]);
const buttonDisabled = computed(() => {
  return !patientInfo.value;
});
const rowIndex = ref(-1);
const diagnosisSearchkey = ref("");
// const emrContext = ref({});
const radio = ref(1);
const rules = ref({
  chiefComplaint: [{ required: true, message: "请输入主诉", trigger: "blur" }],
});
const diagnosisOptions = ref([]);
const orgOrUser = ref();
/**
 * 患者列表行点击回调
 * @param column 患者信息
 */
function cellClick(column) {
  patientInfo.value = column;
  console.log(column);
  if (activeTab.value == "wait") {
    form.value = {};
  } else {
    emrLoading.value = true;
    getEmrDetail(column.encounterId).then((res) => {
      if (res.data) {
        form.value = JSON.parse(res.data.contextJson);
      } else {
        form.value = {};
      }
      setTimeout(() => {
        emrLoading.value = false;
      }, 100);
    });
    getTree();
  }
}

init();
function init() {
  diagnosisInit().then((res) => {
    if (res.code == 200) {
      diagnosisOptions.value = res.data.verificationStatusOptions;
    }
  });
}

function handleMaindise(value, index) {
  diagnosisList.value[index].maindiseFlag = value ? 1 : 0;
  console.log(diagnosisList.value);
}

/**
 * 保存病历
 */
function addEmr() {
  saveEmr({
    patientId: patientInfo.value.patientId,
    encounterId: patientInfo.value.encounterId,
    contextJson: form.value,
  }).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess("保存成功");
    }
  });
}

function handleChange(value) {
  diagnosisSearchkey.value = value;
}

function handleNodeClick(data) {
  diagnosisList.value.push({
    ybNo: data.ybNo,
    name: data.name,
    verificationStatusEnum: 4,
  });
}

/**
 * 病历模板
 */
function handleEmrTemplate() {
  emrTitle.value = "病历模板";
  showDialog.value = "emrTemplate";
  openEmrTemplate.value = true;
}
/**
 * 选择病历模板
 */
function templateSelect(row) {
  form.value = row;
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
  console.log(data);

  deleteDiagnosisBind(data.id).then((res) => {
    if (res.code == 200) {
      proxy.$modal.msgSuccess("删除成功");
      getTree();
    }
  });
}

function cancel() {
  openEmrTemplate.value = false;
  openDiagnosis.value = false;
}

/**
 * 历史病历
 */
function handleEmrHistory() {
  emrTitle.value = "历史病历";
  showDialog.value = "emrHistory";
  openEmrTemplate.value = true;
}

/**
 * 保存病历模板
 */
function handleSaveTemplate() {
  emrTitle.value = "保存模板";
  showDialog.value = "saveTemplate";
  openEmrTemplate.value = true;
}

/**
 * 弹窗确认操作，包括保存病历模板/选择病历模板/选择历史病历
 */
function submit() {
  switch (showDialog.value) {
    case "saveTemplate":
      saveEmrTemplate({
        templateName: templateName.value,
        useScopeCode: radio.value,
        contextJson: form.value,
      }).then((res) => {
        if (res.code == 200) {
          openEmrTemplate.value = false;
          proxy.$modal.msgSuccess("保存成功");
        }
      });
      break;
    case "emrTemplate":
      openEmrTemplate.value = false;
      break;
    case "emrHistory":
      break;
  }
}

/**
 * 获取诊断树列表
 */
function getTree() {
  getConditionDefinitionInfo(
    patientInfo.value ? patientInfo.value.patientId : ""
  ).then((res) => {
    if (res.code == 200) {
      // 手动构造树列表
      tree.value[0] = {
        name: "历史",
        children: res.data.patientHistoryList,
      };
      tree.value[1] = {
        name: "常用",
        children: res.data.doctorCommonUseList,
      };
      tree.value[2] = {
        name: "个人",
        children: res.data.userPersonalList,
      };
      tree.value[3] = {
        name: "科室",
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
  diagnosisList.value.push({
    showPopover: false,
    name: undefined,
    verificationStatusEnum: 4,
  });
}

/**
 * 删除诊断
 */
function handleDeleteDiagnosis(index) {
  diagnosisList.value.splice(index, 1);
}

/**
 * 保存诊断
 */
function handleSaveDiagnosis() {
  if (diagnosisList.value.length == 0) {
    proxy.$modal.msgWarning("诊断不能为空");
  } else if (1 != 1) {
    proxy.$modal.msgWarning("至少添加条主诊断");
  } else {
    saveDiagnosis({
      patientId: patientInfo.value.patientId,
      encounterId: patientInfo.value.encounterId,
      diagnosisChildList: diagnosisList.value,
    }).then((res) => {
      if (res.code == 200) {
        getTree();
        proxy.$modal.msgSuccess("保存成功");
      }
    });
  }
}

/**
 * 关闭诊断弹窗
 */
function closeDiagnosisDialog(str) {
  if (str === "success") {
    proxy.$modal.msgSuccess("操作成功");
  }
  openDiagnosis.value = false;
  getTree();
}

function queryDiagnosisUse(value) {}

/**
 * 选择诊断并赋值到列表
 */
function handleSelsectDiagnosis(row) {
  diagnosisList.value[rowIndex.value].ybNo = row.ybNo;
  diagnosisList.value[rowIndex.value].name = row.name;
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

function toCurrent() {
  activeTab.value = "current";
}
</script>
<style lang="scss" scoped>
// .el-tabs--card {
//   height: calc(100vh - 100px);
// }
// .el-tab-pane {
//   height: calc(100vh - 200px);
//   overflow-y: auto;
// }

/* ::v-deep .el-tabs__item.is-top:last-child {
  float: right;
} */
::v-deep .el-tabs__new-tab {
  /* position: relative;
  right: 10%; */
  border: none;
}

.plain-button {
  margin-right: 300px;
  border: none;
  background-color: transparent;
  color: #909399;
  font-size: 16px;
  padding: 0;
  cursor: pointer;
}
.el-popover.el-popper {
  padding: 0px !important;
}

::v-deep .el-table__row:hover > td {
  background-color: #cde5ff !important; /* 设置为透明或其他你想要的颜色 */
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