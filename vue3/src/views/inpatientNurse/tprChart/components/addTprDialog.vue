<template>
  <el-dialog
    title="患者详情"
    v-model="props.open"
    width="1700px"
    append-to-body
    destroy-on-close
    @close="close"
  >
    <div style="display: flex; justify-content: space-between" class="app-container">
      <div style="width: 50%">
        <el-card style="margin-bottom: 20px">
          <template #header>
            <span style="vertical-align: middle">基本信息</span>
          </template>

          <el-row :gutter="24">
            <el-col :span="8">
              <span>姓名:{{ patientInfo.patientName }}</span>
            </el-col>
            <el-col :span="8">
              <span>性别:{{ patientInfo.genderEnum_enumText }}</span>
            </el-col>
            <el-col :span="8">
              <span>年龄:{{ patientInfo.ageString }}</span>
            </el-col>
          </el-row>
        </el-card>
        <el-card>
          <template #header>
            <span style="vertical-align: middle">体征信息</span>
          </template>
          <div style="width: 100%">
            <div style="width: 100%">
              <el-date-picker
                v-model="receptionTime"
                type="daterange"
                range-separator="~"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                placement="bottom"
                value-format="YYYY-MM-DD"
                style="width: 84%; margin-bottom: 10px; margin-right: 10px"
              />
              <el-button type="primary" style="margin-bottom: 10px" @click="getPatientList">
                搜索
              </el-button>
            </div>
            <div style="margin-bottom: 10px">
              <el-button type="primary" @click="confirmCharge()" :disabled="buttonDisabled">
                保存
              </el-button>
              <el-button type="primary" plain @click="handleReadCard('01')" style="width: 65px">
                修改
              </el-button>
              <el-button
                type="primary"
                plain
                @click="handleReadCard('02')"
                style="width: 65px"
                :disabled="true"
              >
                删除
              </el-button>
            </div>
            <el-table
              ref="patientListRef"
              max-height="650"
              :data="patientList"
              row-key="encounterId"
              @cell-click="clickRow"
              highlight-current-row
            >
              <el-table-column type="selection" :selectable="checkSelectable" width="55" />
              <el-table-column prop="busNo" label="体温" width="80" />
              <el-table-column prop="statusEnum_enumText" label="收缩压" min-width="120" />
              <el-table-column prop="busNo" label="舒张压" width="80" />
              <el-table-column prop="statusEnum_enumText" label="心率" min-width="120" />
              <el-table-column prop="busNo" label="脉搏" width="80" />
              <el-table-column prop="statusEnum_enumText" label="呼吸" min-width="120" />
              <el-table-column prop="busNo" label="血氧" width="80" />
              <el-table-column prop="statusEnum_enumText" label="动脉压" min-width="120" />
              <el-table-column prop="busNo" label="血糖" width="80" />
              <el-table-column prop="statusEnum_enumText" label="物理降温" min-width="120" />
              <el-table-column prop="busNo" label="左瞳孔(光)" width="80" />
              <el-table-column prop="statusEnum_enumText" label="右瞳孔(光)" min-width="120" />
              <el-table-column prop="busNo" label="CCU心率" width="280" />
              <el-table-column prop="statusEnum_enumText" label="左瞳孔(大小)" min-width="120" />
              <el-table-column prop="busNo" label="右瞳孔(大小)" width="280" />
              <el-table-column prop="statusEnum_enumText" label="意识" min-width="120" />
              <el-table-column prop="busNo" label="新生儿箱温" width="280" />
              <el-table-column prop="statusEnum_enumText" label="血酮" min-width="120" />
              <el-table-column prop="busNo" label="体重" width="280" />
              <el-table-column prop="statusEnum_enumText" label="身高" min-width="120" />
              <el-table-column prop="busNo" label="腹围" width="280" />
              <el-table-column prop="statusEnum_enumText" label="大便次数" min-width="120" />
              <el-table-column prop="busNo" label="灌肠次数" width="280" />
              <el-table-column prop="statusEnum_enumText" label="灌肠后大便次数" min-width="120" />
              <el-table-column prop="busNo" label="出量" width="280" />
              <el-table-column prop="statusEnum_enumText" label="入量" min-width="120" />
              <el-table-column prop="statusEnum_enumText" label="尿量" min-width="120" />
              <el-table-column prop="busNo" label="大便量" width="280" />
              <el-table-column label="操作" min-width="150" fixed="right">
                <!-- <template #default="scope"> -->
                <template>
                  <el-button link type="primary" size="small">保存</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-card>
      </div>

      <div style="width: 49%">
        <el-card>
          <template #header>
            <span style="vertical-align: middle">体征录入</span>
          </template>
          <div style="margin-bottom: 10px">
            <el-button type="primary" @click="confirmCharge()" :disabled="buttonDisabled">
              保存
            </el-button>
          </div>
          <el-form ref="dynamicForm" :model="formData" label-width="100px" :rules="formRules">
            <div class="page-bottom">
              <el-row :gutter="24">
                <el-col :span="8">
                  <el-form-item style="margin-top: 15px" label="录入时间">
                    <div class="input-time-inline">
                      <el-select
                        v-model="formData.timePoint"
                        clearable
                        size="small"
                        placeholder=""
                      >
                        <el-option
                          v-for="item in InputOptions"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value"
                        />
                      </el-select>
                    </div>
                  </el-form-item>
                </el-col>
                <!-- <el-col :span="3">
                  <el-form-item style="margin-top: 15px">
                    <div v-if="formData.id === '' || formData.id === undefined">
                      <el-button
                        size="small"
                        type="primary"
                        style="margin-left: 20px"
                        @click="handleAdd"
                        :disabled="buttonDisabled"
                        >新增</el-button
                      >
                    </div>
                    <div v-else>
                      <el-button
                        size="small"
                        type="primary"
                        style="margin-left: 20px"
                        @click="handleUpdate"
                        :disabled="buttonDisabled"
                        >保存</el-button
                      >
                    </div>
                  </el-form-item>
                </el-col> -->
              </el-row>
              <el-row :gutter="24">
                <el-col :span="8">
                  <el-form-item prop="temperature" label="体温">
                    <div class="input-select-container">
                      <el-input
                        v-model="formData.temperature"
                        size="small"
                        @keydown="onKeyDown('0', '0', $event)"
                        :ref="`input-0-0`"
                        style="width: 80%"
                      />
                      <el-select
                        v-model="formData.column133"
                        clearable
                        size="small"
                        placeholder=""
                        @change="
                          handlechangedict(bodyTemperatureList, formData.column133, 'column009')
                        "
                      >
                        <el-option
                          v-for="item in bodyTemperatureList"
                          :key="item.code"
                          :label="item.display"
                          :value="item.code"
                        />
                      </el-select>
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="14">
                  <div class="flex-container">
                    <el-form-item prop="bloodPressure" label="血压">
                      <div class="flex-container">
                        <el-input
                          prop="lowBloodPressure"
                          v-model="formData.date"
                          style="width: 90px"
                          size="small"
                          @keydown="onKeyDown('1', '1', $event)"
                          :ref="`input-1-1`"
                        />
                        <label>/</label>
                        <el-input
                          prop="HighBloodPressure"
                          v-model="formData.times"
                          style="width: 90px"
                          size="small"
                          @keydown="onKeyDown('1', '2', $event)"
                          :ref="`input-1-2`"
                        />
                      </div>
                      <el-select
                        v-model="formData.column134"
                        size="small"
                        style="width: 100px"
                        clearable
                        placeholder=""
                        @change="
                          handlechangedict(
                            bloodPressureList,
                            formData.column134,
                            'column012',
                            'column013'
                          )
                        "
                      >
                        <el-option
                          v-for="item in bloodPressureList"
                          :key="item.code"
                          :label="item.display"
                          :value="item.code"
                        />
                      </el-select>
                    </el-form-item>
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="24">
                <el-col :span="8">
                  <el-form-item prop="heartRate" label="心率">
                    <el-input
                      v-model="formData.heartRate"
                      size="small"
                      @keydown="onKeyDown('1', '0', $event)"
                      :ref="`input-1-0`"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item prop="pulseRate" label="脉搏">
                    <el-input
                      v-model="formData.pulseRate"
                      size="small"
                      @keydown="onKeyDown('0', '1', $event)"
                      :ref="`input-0-1`"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item prop="respirationRate" label="呼吸">
                    <el-input
                      v-model="formData.respirationRate"
                      size="small"
                      @keydown="onKeyDown('0', '2', $event)"
                      :ref="`input-0-2`"
                    />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="24">
                <el-col :span="8">
                  <div class="layui-form-item">
                    <el-form-item prop="bloodOxygen" label="血氧">
                      <el-input
                        v-model="formData.bloodOxygen"
                        size="small"
                        @keydown="onKeyDown('1', '3', $event)"
                        :ref="`input-1-3`"
                      />
                    </el-form-item>
                  </div>
                </el-col>
                <!-- <el-col :span="8">
                  <el-form-item prop="column089" label="动脉压">
                    <el-input
                      v-model="formData.column089"
                      size="small"
                      @keydown="onKeyDown('2', '0', $event)"
                      :ref="`input-2-0`"
                    />
                  </el-form-item>
                </el-col> -->
                <el-col :span="8">
                  <el-form-item prop="bloodGlucose" label="血糖">
                    <div class="input-select-container">
                      <el-input
                        v-model="formData.bloodGlucose"
                        size="small"
                        @keydown="onKeyDown('2', '1', $event)"
                        :ref="`input-2-1`"
                      />
                      <el-select
                        v-model="formData.column135"
                        clearable
                        placeholder=""
                        size="small"
                        @change="handlechangedict(bloodSugarList, formData.column135, 'column091')"
                      >
                        <el-option
                          v-for="item in bloodSugarList"
                          :key="item.code"
                          :label="item.display"
                          :value="item.code"
                        />
                      </el-select>
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item prop="physicalCooling" label="物理降温">
                    <el-input
                      v-model="formData.physicalCooling"
                      size="small"
                      @keydown="onKeyDown('2', '2', $event)"
                      :ref="`input-2-2`"
                    />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="24">

                <!-- <el-col :span="8">
                  <el-form-item label="左瞳孔(光)">
                    <el-select v-model="formData.column092" clearable placeholder="" size="small">
                      <el-option
                        v-for="item in pupilList"
                        :key="item.code"
                        :label="item.display"
                        :value="item.code"
                      />
                    </el-select>
                  </el-form-item>
                </el-col> -->
                <!-- <el-col :span="8">
                  <el-form-item label="右瞳孔(光)">
                    <el-select v-model="formData.column093" clearable placeholder="" size="small">
                      <el-option
                        v-for="item in pupilList"
                        :key="item.code"
                        :label="item.display"
                        :value="item.code"
                      />
                    </el-select>
                  </el-form-item>
                </el-col> -->
              </el-row>
              <el-row :gutter="24">
                <!-- <el-col :span="8">
                  <el-form-item label="左瞳孔(大小)">
                    <el-input
                      v-model="formData.column051"
                      size="small"
                      @keydown="onKeyDown('3', '0', $event)"
                      :ref="`input-3-0`"
                    /> </el-form-item
                ></el-col> -->
                <!-- <el-col :span="8">
                  <el-form-item label="右瞳孔(大小)">
                    <el-input
                      v-model="formData.column052"
                      size="small"
                      @keydown="onKeyDown('3', '1', $event)"
                      :ref="`input-3-1`"
                    />
                  </el-form-item>
                </el-col> -->
              </el-row>

              <el-row :gutter="24">
                <!-- <el-col :span="8">
                  <el-form-item label="意识">
                    <el-select v-model="formData.column138" clearable placeholder="" size="small">
                      <el-option
                        v-for="item in sanityList"
                        :key="item.code"
                        :label="item.display"
                        :value="item.code"
                      />
                    </el-select>
                  </el-form-item>
                </el-col> -->
                <el-col :span="8">
                  <el-form-item label="CCU心率">
                    <el-select v-model="formData.ccuHeartRate" clearable placeholder="" size="small">
                      <el-option
                        v-for="item in CCUHeartRateList"
                        :key="item.code"
                        :label="item.display"
                        :value="item.code"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="新生儿箱温">
                    <el-input
                      v-model="formData.newbornsIncubator"
                      size="small"
                      @keydown="onKeyDown('4', '0', $event)"
                      :ref="`input-4-0`"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="血酮">
                    <div class="flex-container">
                      <el-input
                        v-model="formData.bloodKetone"
                        size="small"
                        @keydown="onKeyDown('4', '1', $event)"
                        :ref="`input-4-1`"
                      />
                      <template>
                        <el-select
                          v-model="formData.column141"
                          clearable
                          placeholder=""
                          size="small"
                          @change="
                            handlechangedict(bloodKetoneList, formData.column141, 'column140')
                          "
                        >
                          <el-option
                            v-for="item in bloodKetoneList"
                            :key="item.code"
                            :label="item.display"
                            :value="item.code"
                          />
                        </el-select>
                      </template>
                    </div> </el-form-item
                ></el-col>
              </el-row>
              <el-row :gutter="24">
                <!-- <el-col :span="8">
                  <el-form-item prop="column009" label="体重(kg)">
                    <div class="input-select-container">
                      <el-input
                        v-model="formData.column009"
                        size="small"
                        @keydown="onKeyDown('0', '0', $event)"
                        :ref="`input-0-0`"
                      />
                      <el-select
                        v-model="formData.column133"
                        clearable
                        size="small"
                        placeholder=""
                        @change="
                          handlechangedict(bodyTemperatureList, formData.column133, 'column009')
                        "
                      >
                        <el-option
                          v-for="item in bodyTemperatureList"
                          :key="item.code"
                          :label="item.display"
                          :value="item.code"
                        />
                      </el-select>
                    </div>
                  </el-form-item>
                </el-col> -->
                <el-col :span="8">
                  <el-form-item prop="height" label="身高(cm)">
                    <div class="input-select-container">
                      <el-input
                        v-model="formData.height"
                        size="small"
                        @keydown="onKeyDown('0', '0', $event)"
                        :ref="`input-0-0`"
                      />
                      <el-select
                        v-model="formData.column133"
                        clearable
                        size="small"
                        placeholder=""
                        @change="
                          handlechangedict(bodyTemperatureList, formData.column133, 'column009')
                        "
                      >
                        <el-option
                          v-for="item in bodyTemperatureList"
                          :key="item.code"
                          :label="item.display"
                          :value="item.code"
                        />
                      </el-select>
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item prop="waistCircumference" label="腹围(cm)">
                    <div class="input-select-container">
                      <el-input
                        v-model="formData.waistCircumference"
                        size="small"
                        @keydown="onKeyDown('0', '0', $event)"
                        :ref="`input-0-0`"
                      />
                      <el-select
                        v-model="formData.column133"
                        clearable
                        size="small"
                        placeholder=""
                        @change="
                          handlechangedict(bodyTemperatureList, formData.column133, 'column009')
                        "
                      >
                        <el-option
                          v-for="item in bodyTemperatureList"
                          :key="item.code"
                          :label="item.display"
                          :value="item.code"
                        />
                      </el-select>
                    </div>
                  </el-form-item>
                </el-col>
              </el-row>
              <!-- </el-form-item> -->
              <el-row :gutter="24">
                <el-col :span="8">
                  <div class="flex-container">
                    <el-form-item prop="stoolFrequency" label="大便次数(次)">
                      <div class="input-select-container">
                        <el-input
                          v-model="formData.stoolFrequency"
                          size="small"
                          @keydown="onKeyDown('0', '0', $event)"
                          :ref="`input-0-0`"
                        />
                        <el-select
                          v-model="formData.column133"
                          clearable
                          size="small"
                          placeholder=""
                          @change="
                            handlechangedict(bodyTemperatureList, formData.column133, 'column009')
                          "
                        >
                          <el-option
                            v-for="item in bodyTemperatureList"
                            :key="item.code"
                            :label="item.display"
                            :value="item.code"
                          />
                        </el-select>
                      </div>
                    </el-form-item>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="flex-container">
                    <el-form-item prop="enemaFrequency" label="灌肠次数(次)">
                      <el-input
                        v-model="formData.enemaFrequency"
                        size="small"
                        @keydown="onKeyDown('0', '0', $event)"
                        :ref="`input-0-0`"
                      />
                    </el-form-item>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="flex-container">
                    <el-form-item prop="sfAfterEnema" label="灌肠后大便次数(次)">
                      <el-input
                        v-model="formData.sfAfterEnema"
                        size="small"
                        @keydown="onKeyDown('0', '0', $event)"
                        :ref="`input-0-0`"
                      />
                    </el-form-item>
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="24">
                <el-col :span="8">
                  <div class="flex-container">
                    <el-form-item prop="output" label="出量（ml）">
                      <el-input
                        v-model="formData.output"
                        size="small"
                        @keydown="onKeyDown('0', '0', $event)"
                        :ref="`input-0-0`"
                      />
                    </el-form-item>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="flex-container">
                    <el-form-item prop="input" label="入量（ml）">
                      <el-input
                        v-model="formData.input"
                        size="small"
                        @keydown="onKeyDown('0', '0', $event)"
                        :ref="`input-0-0`"
                      />
                    </el-form-item>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="flex-container">
                    <el-form-item prop="urineVolume" label="尿量（ml）">
                      <el-input
                        v-model="formData.urineVolume"
                        size="small"
                        @keydown="onKeyDown('0', '0', $event)"
                        :ref="`input-0-0`"
                      />
                    </el-form-item>
                  </div>
                </el-col>
              </el-row>

              <el-row :gutter="24">
                <el-col :span="8">
                  <div class="flex-container">
                    <el-form-item prop="stoolVolume" label="大便量（ml）">
                      <el-input
                        v-model="formData.stoolVolume"
                        size="small"
                        @keydown="onKeyDown('0', '0', $event)"
                        :ref="`input-0-0`"
                      />
                    </el-form-item>
                  </div>
                </el-col>
              </el-row>
            </div>
          </el-form>
        </el-card>
      </div>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="close">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, nextTick } from 'vue';
import { listPatient, addVitalSigns } from './api';
import moment from "moment";

const { proxy } = getCurrentInstance();
const props = defineProps({
  open: {
    type: Boolean,
    default: false,
  },
  patientId: {
    type: String,
    default: '',
  },
  patientInfo: {
    type: Object,
    default: () => {},
  },
});
const { method_code, unit_code, rate_code, distribution_category_code } = proxy.useDict(
  'method_code',
  'unit_code',
  'rate_code',
  'distribution_category_code'
);

const emit = defineEmits(['close']);
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  patientId: undefined, // 患者id
});
// const patientInfo = ref({
//   patientName: '张三',
// });
const patientInfo = ref({});
const form = ref({});
const title = ref('');
const rowRules = ref({
  conditionDefinitionId: [{ required: true, message: '请选择诊断', trigger: 'change' }],
  dose: [{ required: true, message: '请输入单次剂量', trigger: 'change' }],
  doseQuantity: [{ required: true, message: '请输入单次剂量', trigger: 'change' }],
  quantity: [{ required: true, message: '请输入数量', trigger: 'change' }],
  dispensePerDuration: [{ required: true, message: '请输入用药天数', trigger: 'change' }],
});

// 表单配置数据
const formConfig = ref([
  {
    type: 'input', // 表单项类型
    label: '用户名', // 标签文本
    prop: 'username', // 表单字段名
    rules: [{ required: true, message: '请输入用户名', trigger: 'blur' }], // 验证规则
    attrs: {
      // el-input 的属性
      placeholder: '请输入用户名',
      clearable: true,
    },
  },
  {
    type: 'select',
    label: '性别',
    prop: 'gender',
    options: [
      // select 的选项
      { value: 'male', label: '男' },
      { value: 'female', label: '女' },
    ],
    attrs: {
      placeholder: '请选择性别',
    },
  },
  // 更多表单项配置...
]);

const InputOptions = ref([
  {
    value: '0200', label: '2点',
  },
  {
    value: '0600', label: '6点',
  },
  {
    value: '1000', label: '10点',
  },
  {
    value: '1400', label: '14点',
  },
  {
    value: '1800', label: '18点',
  },
  {
    value: '2200', label: '22点',
  },
])

// 表单数据
const formData = ref({
  username: '',
  gender: '',
});
// 表格列配置数据
const tableColumns = ref([
  {
    type: 'selection', // 特殊列类型
    width: '55',
    // selectable: checkSelectable, // 可选函数
  },
  {
    prop: 'encounterBusNo', // 字段名
    label: '病历号', // 列标题
    align: 'center',
    width: '120',
  },
  {
    prop: 'patientName',
    label: '姓名',
    align: 'center',
  },
  {
    prop: 'statusEnum_enumText',
    label: '收费状态',
    align: 'center',
    formatter: (row) => formatStatus(row.statusEnum_enumText), // 格式化函数
  },
  // 更多列配置...
]);

function getPatientList() {}
/**
 * 取得患者信息详细
 */
function getPatientDetial() {
  patientInfo.value = props.patientInfo;
  console.log(props, 'props', props.patientInfo);
  reset();
  title.value = '';
  title.value = props.title;
  console.log(props, 'props', title.value);
  console.log(queryParams.value, 'queryParams.value', props.patientId);
  queryParams.value.patientId = props.patientId;
  listPatient(queryParams.value).then((res) => {
    if (res.data && res.data.records && res.data.records.length > 0) {
      form.value = res.data.records[0];
    }
    form.value.addressInfo =
      form.value.addressProvince +
      '' +
      form.value.addressCity +
      '' +
      form.value.addressDistrict +
      '' +
      form.value.addressStreet +
      '' +
      form.value.address;

    // loading.value = false;
    console.log(res, 'resqqqqqqqqqqqqqqqqqqqqqqq', form.value);
  });
}

function close() {
  reset();
  emit('close');
}
function confirmCharge() {
  let params = {}
  params = formData.value
  params.patientId = props.patientInfo.patientId
  params.encounterId = props.patientInfo.encounterId
  params.recordingDate = moment(new Date()).format('YYYY-MM-DD');
  params.timePoint = formData.value.timePoint
  params.vitalSignsCode = ['003']
  params.vitalSignsValues = ['38']
  addVitalSigns(formData.value).then(res => {
    console.log(res)
  })
}
/** 重置操作表单 */
function reset() {
  form.value = {
    id: undefined,
    activeFlag: undefined, // 活动标记
    tempFlag: undefined, // 临时标识
    name: undefined, // 患者姓名
    nameJson: undefined, // 患者其他名称
    busNo: undefined, // 病历号
    genderEnum: undefined, // 性别
    genderEnum_enumText: undefined, // 性别
    birthDate: undefined, // 生日
    deceasedDate: undefined, // 死亡时间
    maritalStatusEnum: undefined, // 婚姻状态
    maritalStatusEnum_enumText: undefined, // 婚姻状态
    prfsEnum: undefined, // 职业编码
    prfsEnum_enumText: undefined, // 职业编码
    phone: undefined, // 电话
    address: undefined, // 地址
    addressProvince: undefined, // 地址省
    addressCity: undefined, // 地址市
    addressDistrict: undefined, // 地址区
    addressStreet: undefined, // 地址街道
    addressJson: undefined, // 患者其他地址
    nationalityCode: undefined, // 民族
    idCard: undefined, // 身份证号
    pyStr: undefined, //拼音码
    wbStr: undefined, // 五笔码
    bloodAbo: undefined, // 血型ABO
    bloodAbo_enumText: undefined, // 血型ABO
    bloodRh: undefined, // 血型RH
    bloodRh_enumText: undefined, // 血型RH
    workCompany: undefined, // 工作单位
    nativePlace: undefined, // 籍贯
    countryCode: undefined, // 国家编码
    linkName: undefined, // 联系人
    linkRelationCode: undefined, // 联系人关系
    linkRelationCode_codeText: undefined, // 联系人关系
    linkTelcom: undefined, // 联系人电话
    linkJsons: undefined, // 其他联系人
    tenanid: undefined, // 租户ID
    ageString: undefined, // 病人年龄
    priorityEnum: undefined, // 护理级别
    priorityEnum_enumText: undefined, // 护理级别
    statusEnum: undefined, // 患者状态
    statusEnum_enumText: undefined, // 患者状态
    organizationId: undefined, // 入院科室
    organizationId_dictText: undefined, // 入院科室
    startTime: undefined, // 入院日期
    endTime: undefined, // 出院日期
    responsibleDoctor: undefined, // 责任医生
    responsibleNurse: undefined, // 责任护士
    iptDiseTypeCode: undefined, // 主要诊断
    typeCode: undefined, // 费别
    typeCode_dictText: undefined, // 费别
    hospitalizationDays: undefined, //住院天数
    classEnum: undefined, // 就诊类别
    classEnum_enumText: undefined, // 就诊类别
    postoperativeDays: undefined, // 术后天数
    surgeryStartTime: undefined, // 手术开始日期
    surgeryEndTime: undefined, // 手术结束日期
    surgeryStatusEnum: undefined, // 手术状态
    surgeryStatusEnum_enumText: undefined, // 手术状态
    categoryCode: undefined, // 过敏源类别
    categoryCode_dictText: undefined, // 过敏源类别
    caty: undefined, // 入院科室名称
    locationId: undefined, // 床位号
    locationId_dictText: undefined, // 床位号
    encounterId: undefined, // 就诊流水号
    encounterLocationId: undefined, // 就诊地点流水号
  };
  proxy.resetForm('formRef');
}

defineExpose({
  getPatientDetial,
});
</script>

<style scoped>
:deep(.pagination-container .el-pagination) {
  right: 20px !important;
}

.input-select-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.input-select-container .el-input__inner,
.input-select-container .el-select {
  width: 100%;
}

.flex-container {
  display: flex;
  align-items: center; /* 垂直居中对齐 */
  justify-content: flex-start; /* 水平起始对齐 */
  gap: 8px; /* 两个输入框之间的间距 */
}

.flex-container label {
  margin: 0 8px; /* 标签的间距 */
}
</style>