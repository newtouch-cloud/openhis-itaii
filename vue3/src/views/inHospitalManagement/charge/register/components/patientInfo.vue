<template>
  <div class="patientInfo-container">
    <div class="operate">
      <div>基础信息</div>
      <div v-if="props.registrationType" type="primary" @click="toggleEdit" style="margin-right: 0;cursor: pointer;margin-right: 12px;">
        {{ isEditing ? '取消' : '编辑' }}
      </div>
    </div>

    <!-- 展示模式 -->
    <div v-if="!isEditing">
      <el-row class="patientInfos">
        <el-col :span="2" class="patInfo-label">
          <el-text truncated>患者编码：</el-text>
        </el-col>
        <el-col :span="4" class="patInfo-value">
          <el-text truncated>{{ patientInfo?.patientBusNo }}</el-text>
        </el-col>
        <el-col :span="2" class="patInfo-label">
          <el-text truncated>患者姓名：</el-text>
        </el-col>
        <el-col :span="4" class="patInfo-value">
          <el-text truncated>{{ patientInfo?.name }}</el-text>
        </el-col>
        <el-col :span="2" class="patInfo-label">
          <el-text truncated>费别类型：</el-text>
        </el-col>
        <el-col :span="4" class="patInfo-value">
          <el-text truncated>{{ patientInfo?.feeTypeName }}</el-text>
          <!-- TODO -->
          <svg-icon size="20" icon-class="hipEdit" style="cursor: pointer" @click="changFeeType" /> 
        </el-col>
      </el-row>
      <el-row class="patientInfos">
        <el-col :span="2" class="patInfo-label">
          <el-text truncated>证件类型：</el-text>
        </el-col>
        <!-- TODO -->
        <el-col :span="4" class="patInfo-value">
          <el-text truncated>{{ patientInfo?.cardTypeName }}</el-text>
        </el-col>
        <el-col :span="2" class="patInfo-label">
          <el-text truncated>证件号：</el-text>
        </el-col>
        <el-col :span="4" class="patInfo-value">
          <el-text truncated>{{ patientInfo?.idCard }}</el-text>
        </el-col>
        <el-col :span="2" class="patInfo-label">
          <el-text truncated>出生日期：</el-text>
        </el-col>
        <el-col :span="4" class="patInfo-value">
          <el-text truncated>{{ patientInfo?.birthDate }}</el-text>
        </el-col>
        <el-col :span="2" class="patInfo-label">
          <el-text truncated>年龄：</el-text>
        </el-col>
        <el-col :span="4" class="patInfo-value">
          <el-text truncated>{{ patientInfo?.ageString }}</el-text>
        </el-col>
      </el-row>
      <el-row class="patientInfos">
        <el-col :span="2" class="patInfo-label">
          <el-text truncated>性别：</el-text>
        </el-col>
        <el-col :span="4" class="patInfo-value">
          <el-text truncated>{{ patientInfo?.genderEnum_enumText }}</el-text>
        </el-col>
        <el-col :span="2" class="patInfo-label">
          <el-text truncated>民族：</el-text>
        </el-col>
        <el-col :span="4" class="patInfo-value">
          <el-text truncated>{{ patientInfo?.nationName }}</el-text>
        </el-col>
        <el-col :span="2" class="patInfo-label">
          <el-text truncated>国籍：</el-text>
        </el-col>
        <el-col :span="4" class="patInfo-value">
          <el-text truncated>{{ patientInfo?.nationalityName }}</el-text>
        </el-col>
        <el-col :span="2" class="patInfo-label">
          <el-text truncated>婚姻：</el-text>
        </el-col>
        <el-col :span="4" class="patInfo-value">
          <el-text truncated>{{ patientInfo?.maritalStatusEnum }}</el-text>
        </el-col>
      </el-row>
      <el-row class="patientInfos">
        <el-col :span="2" class="patInfo-label">
          <el-text truncated>职业：</el-text>
        </el-col>
        <el-col :span="4" class="patInfo-value">
          <el-text :text="patientInfo?.workName" />
        </el-col>
        <el-col :span="2" class="patInfo-label">
          <el-text truncated>手机：</el-text>
        </el-col>
        <el-col :span="4" class="patInfo-value">
          <el-text :text="patientInfo?.tel" />
        </el-col>
        <el-col :span="2" class="patInfo-label">
          <el-text truncated>现住址：</el-text>
        </el-col>
        <el-col :span="4" class="patInfo-value">
          <el-text :text="patientInfo?.liveAddr" />
        </el-col>
      </el-row>
      <el-row class="patientInfos">
        <el-col :span="2" class="patInfo-label">
          <el-text truncated>户口地址：</el-text>
        </el-col>
        <el-col :span="4" class="patInfo-value">
          <el-text :text="patientInfo?.nativeExactAddr" />
        </el-col>
        <el-col :span="2" class="patInfo-label">
          <el-text truncated>籍贯：</el-text>
        </el-col>
        <el-col :span="4" class="patInfo-value">
          <el-text :text="patientInfo?.nativePlaceName" />
        </el-col>
        <el-col :span="2" class="patInfo-label">
          <el-text truncated>病人来源：</el-text>
        </el-col>
        <el-col :span="4" class="patInfo-value">
          <el-text :text="patientInfo?.sourceName" />
        </el-col>
        <el-col :span="2" class="patInfo-label">
          <el-text truncated>出生地：</el-text>
        </el-col>
        <el-col :span="4" class="patInfo-value">
          <el-text :text="patientInfo?.birthPlaceName" />
        </el-col>
      </el-row>
      <el-row class="patientInfos">
        <el-col :span="2" class="patInfo-label">
          <el-text truncated>单位名称：</el-text>
        </el-col>
        <el-col :span="4" class="patInfo-value">
          <el-text :text="patientInfo?.companyName" />
        </el-col>
        <el-col :span="2" class="patInfo-label">
          <el-text truncated>贫困患者：</el-text>
        </el-col>
        <el-col :span="4" class="patInfo-value">
          <el-text truncated>{{ patientInfo?.poorTypeName }}</el-text>
        </el-col>
      </el-row>
    </div>

    <!-- 编辑模式 -->
    <div v-else>
      <el-form :model="form" ref="registerRef" label-width="100px" :rules="rules" style="width: 95%">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="患者编码">
              <el-input v-model="form.patientBusNo" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="患者姓名" prop="name">
              <el-input v-model="form.name" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="费别类型" prop="feeType">
              <el-select v-model="form.feeType" placeholder="请选择" style="width: 100%">
                <el-option label="医保" value="1" />
                <el-option label="自费" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="证件类型">
              <el-select v-model="form.cardType" placeholder="请选择" style="width: 100%">
                <el-option
                  v-for="item in typeList.identityDocumentType"
                  :key="item.value"
                  :label="item.info"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="证件号" prop="idCard">
              <el-input v-model="form.idCard" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="出生日期">
              <el-date-picker v-model="form.birthDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD HH:mm:ss" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="年龄">
              <el-input v-model="form.age" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="性别">
              <el-select v-model="form.sex" placeholder="请选择" style="width: 100%">
                <el-option
                  v-for="item in typeList.sex"
                  :key="item.value"
                  :label="item.info"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="民族">
              <el-input v-model="form.nation" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="国籍">
              <el-input v-model="form.nationality" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="婚姻">
              <el-select v-model="form.maritalStatusEnum" placeholder="请选择" style="width: 100%">
                <el-option
                  v-for="item in typeList.maritalStatus"
                  :key="item.value"
                  :label="item.info"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="职业">
              <el-select v-model="form.workName" placeholder="请选择" style="width: 100%">
                <el-option
                  v-for="item in typeList.occupationType"
                  :key="item.value"
                  :label="item.info"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="手机号" prop="tel">
              <el-input v-model="form.tel" />
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item label="现住址">
              <el-input v-model="form.liveAddr" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="户口地址">
              <el-input v-model="form.nativeExactAddr" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="籍贯">
              <el-input v-model="form.nativePlaceName" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="病人来源">
              <el-input v-model="form.sourceName" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="出生地">
              <el-input v-model="form.birthPlaceName" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="单位名称">
              <el-input v-model="form.companyName" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="贫困患者">
              <el-select v-model="form.poorType" placeholder="请选择" style="width: 100%">
                <el-option label="是" value="1" />
                <el-option label="否" value="0" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import { patientlLists } from "./api";

const typeList = ref({})
const props = defineProps({
  patientInfo: {
    type: Object,
    require: true,
    default: () => ({ code: '', name: '', feeTypeName: '' })
  },
  registrationType: {
    type: [String, Boolean, Number], // 根据实际类型调整
    default: null // 或者 false、'' 等
  }
})

const rules = reactive({
  name: [
    {
      required: true,
      message: '患者姓名未填写',
      trigger: ['blur', 'change'],
    },
  ],
  feeType: [
    {
      required: true,
      message: '费别类型未填写',
      trigger: ['blur', 'change'],
    },
  ],
  cardType: [
    {
      required: true,
      message: '证件类型未填写',
      trigger: ['blur', 'change'],
    },
  ],
  idCard: [
    {
      required: true,
      message: '证件号未填写',
      trigger: ['blur', 'change'],
    },
  ],
  tel: [
    {
      required: true,
      message: '手机号未填写',
      trigger: ['blur', 'change'],
    },
  ]
})

const emits = defineEmits(['onChangFeeType'])
const registerRef = ref();

onMounted(() => {
  getInitOptions()
});

// 编辑状态
const isEditing = ref(false)

watch(
  () => props.registrationType,
  (newValue) => {
    isEditing.value = !newValue;
  },
  { immediate: true }
);


function getInitOptions() {
  patientlLists().then(res => {
    typeList.value = res.data;
  })
}


// 表单数据
const form = reactive({})

// 切换编辑状态
const toggleEdit = () => {
  if (!isEditing.value) {
    // 取消时恢复原值
    Object.assign(form, props.patientInfo)
  }
  isEditing.value = !isEditing.value
}

// 提交表单
const submitForm = async (callback) => {
  console.log('提交更新:', form)
  if (!registerRef.value) return false;
  registerRef.value.validate((valid) => {
    if (valid) {
      callback();
      return true;
    } else {
      return false;
    }
  });
}

// 患者费别变更
const changFeeType = () => {
  emits('onChangFeeType')
}
defineExpose({ submitForm, form });
</script>

<style lang="scss" scoped>
.patientInfo-container {
  .operate {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 32px;
    border-radius: 4px 4px 0px 0px;
    background: rgba(37, 109, 149, 0.05);
    padding-left: 16px;
    color: var(--hip-color-primary-light);
    font-weight: bold;
    margin-bottom: 8px;
  }

  > .patientInfos {
    margin-bottom: 16px;

    .patInfo-label {
      text-align: right;
    }

    &:not(.patientInfo-container :has(+ .patientInfos)) {
      margin-bottom: 0px;
    }
  }
}
</style>