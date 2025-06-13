<template>
  <div class="app-container">
    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="电子处方查询结果" v-model="visible" width="1800px" append-to-body>
      <div style="width: 100%">
        <div class="title">处方信息</div>
        <el-table max-height="650" :data="prescriptionInfoList" border>
          <el-table-column label="医保处方编号" align="center" prop="hiRxno" width="280" sortable />
          <el-table-column label="就诊凭证类型" align="center" prop="mdtrtCertType" width="110" />
          <el-table-column label="就诊凭证编号" align="center" prop="mdtrtCertNo" width="150" />
          <el-table-column label="卡识别码" align="center" prop="cardSn" />
          <el-table-column label="业务类型" align="center" prop="bizTypeCode" width="130" />
          <el-table-column label="处方附加属性" align="center" prop="rxExraAttrCode" width="130" />
          <el-table-column label="电子凭证令牌" align="center" prop="ecToken" width="130" />
          <el-table-column
            label="电子凭证线上身份核验流水号"
            align="center"
            prop="authNo"
            width="200"
          />
          <el-table-column label="参保地编号" align="center" prop="insuPlcNo" width="100" />
          <el-table-column label="就医地编号" align="center" prop="mdtrtareaNo" width="100" />
          <el-table-column
            label="定点医疗机构处方编号"
            align="center"
            prop="hospRxno"
            width="160"
          />
          <el-table-column label="续方的原处方编号" align="center" prop="initRxno" width="160" />
          <el-table-column
            label="处方类别代码"
            align="center"
            prop="rxTypeCode"
            width="200"
            sortable
          />
          <el-table-column label="开方时间" align="center" prop="prscTime" width="180">
            <template #default="scope">
              {{ formatDate(scope.row.prscTime) }}
            </template>
          </el-table-column>
          <el-table-column label="药品类目数" align="center" prop="rxDrugCnt" width="110" />
          <el-table-column
            label="处方整剂用法编号"
            align="center"
            prop="rxUsedWayCodg"
            width="150"
          />
          <el-table-column
            label="处方整剂用法名称"
            align="center"
            prop="rxUsedWayName"
            width="130"
          />
          <el-table-column label="处方整剂频次编号" align="center" prop="rxFrquCodg" width="130" />
          <el-table-column label="处方整剂频次名称" align="center" prop="rxFrquName" width="130" />
          <el-table-column label="处方整剂剂量单位" align="center" prop="rxDosunt" width="130" />
          <el-table-column label="处方整剂单次剂量数" align="center" prop="rxDoscnt" width="150" />
          <el-table-column label="处方整剂医嘱说明" align="center" prop="rxDrordDscr" width="130" />
          <el-table-column label="处方有效天数" align="center" prop="valiDays" width="130" />

          <el-table-column label="有效截止时间" align="center" prop="valiEndTime" width="180">
            <template #default="scope">
              {{ formatDate(scope.row.valiEndTime) }}
            </template>
          </el-table-column>
          <el-table-column label="复用" align="center" prop="reptFlag" />
          <el-table-column label="最大使用次数" align="center" prop="maxReptCnt" width="110" />
          <el-table-column
            label="使用最小间隔(天数)"
            align="center"
            prop="minInvDays"
            width="150"
          />
          <el-table-column label="续方" align="center" prop="rxCotnFlag" width="80" />
          <el-table-column label="长期处方" align="center" prop="longRxFlag" />
        </el-table>
        <!-- <pagination
          v-show="total > 0"
          :total="total"
          v-model:page="queryParams.pageNo"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
        /> -->
      </div>
      <div style="width: 100%">
        <div class="title">处方明细信息</div>
        <el-table max-height="650" :data="rxdrugdetailList" border>
          <el-table-column
            label="医疗目录编码"
            align="center"
            prop="medListCodg"
            width="200"
            sortable
          />
          <el-table-column
            label="定点医药机构目录编号"
            align="center"
            prop="fixmedinsHilistId"
            width="90"
          />
          <el-table-column label="医疗机构制剂标志" align="center" prop="hospPrepFlag" width="60" />
          <el-table-column label="处方项目分类代码" align="center" prop="rxItemTypeCode" />
          <el-table-column
            label="处方项目分类名称"
            align="center"
            prop="rxItemTypeName"
            width="130"
          />
          <el-table-column label="中药类别代码" align="center" prop="tcmdrugTypeCode" width="80" />
          <el-table-column label="中药类别名称" align="center" prop="tcmdrugTypeName" />
          <el-table-column label="草药脚注" align="center" prop="tcmherbFoote" />
          <el-table-column label="药物类型代码" align="center" prop="mednTypeCode" />
          <el-table-column label="药物类型名称" align="center" prop="mednTypeName" />
          <el-table-column label="主要用药标志" align="center" prop="mainMedcFlag" />
          <el-table-column label="加急标志" align="center" prop="urgtFlag" width="200" sortable />
          <el-table-column label="基本药物标志" align="center" prop="basMednFlag" width="90" />
          <el-table-column label="是否进口药品" align="center" prop="impDrugFlag" width="60" />
          <el-table-column label="是否OTC药品" align="center" prop="otcFlag" />
          <el-table-column label="药品通用名" align="center" prop="drugGenname" width="130" />
          <el-table-column label="药品剂型" align="center" prop="drugDosform" width="80" />
          <el-table-column label="药品规格" align="center" prop="drugSpec" />
          <el-table-column label="药品商品名" align="center" prop="drugProdname" />
          <el-table-column label="生产厂家" align="center" prop="prdrName" />
          <el-table-column label="用药途径代码" align="center" prop="medcWayCodg" />
          <el-table-column label="用药途径描述" align="center" prop="medcWayDscr" />
          <el-table-column label="用药开始时间" align="center" prop="medcBegntime" width="90">
            <template #default="scope">
              {{ formatDate(scope.row.medcBegntime) }}
            </template>
          </el-table-column>
          <el-table-column label="用药结束时间" align="center" prop="medcEndtime" width="90">
            <template #default="scope">
              {{ formatDate(scope.row.medcEndtime) }}
            </template>
          </el-table-column>
          <el-table-column label="用药天数" align="center" prop="medcDays" width="200" sortable />
          <el-table-column label="单次剂量单位" align="center" prop="sinDosunt" width="90" />
          <el-table-column label="单次用量" align="center" prop="sinDoscnt" width="60" />
          <el-table-column label="使用频次编码" align="center" prop="usedFrquCodg" />
          <el-table-column label="使用频次名称" align="center" prop="usedFrquName" width="130" />
          <el-table-column label="药品总用药量单位" align="center" prop="drugDosunt" width="80" />
          <el-table-column label="药品总用药量" align="center" prop="drugCnt" />
          <el-table-column label="药品单价" align="center" prop="drugPric" />
          <el-table-column label="药品总金额" align="center" prop="drugSumamt" />
          <el-table-column label="医院审批标志" align="center" prop="hospApprFlag" />
          <el-table-column label="自费原因类型" align="center" prop="selfPayRea" />
          <el-table-column
            label="自费原因描述"
            align="center"
            prop="realDscr"
            width="200"
            sortable
          />
          <el-table-column label="扩展数据" align="center" prop="extras" width="90" />
          <el-table-column label="院内内部处方号" align="center" prop="prescriptionNo" width="60" />
          <el-table-column label="医保处方编号" align="center" prop="hiRxno" />
        </el-table>
      </div>
      <div style="width: 100%">
        <div class="title">就诊信息</div>
        <el-table max-height="650" :data="mdtrtinfoList" border>
          <el-table-column
            label="定点医疗机构名称"
            align="center"
            prop="fixmedinsName"
            width="200"
            sortable
          />
          <el-table-column
            label="定点医疗机构编号"
            align="center"
            prop="fixmedinsCode"
            width="90"
          />
          <el-table-column label="医保就诊ID" align="center" prop="mdtrtId" width="70" />
          <el-table-column label="医疗类别" align="center" prop="medType" />
          <el-table-column label="门诊/住院号" align="center" prop="iptOtpNo" width="130" />
          <el-table-column label="门诊住院标识" align="center" prop="otpIptFlag" width="80" />
          <el-table-column label="医保人员编号" align="center" prop="psnNo" />
          <el-table-column label="患者姓名" align="center" prop="patnName" />
          <el-table-column label="人员证件类型" align="center" prop="psnCertType" />
          <el-table-column label="证件号码" align="center" prop="certno" />
          <el-table-column label="年龄" align="center" prop="patnAge" />
          <el-table-column
            label="患者身高(cm)"
            align="center"
            prop="patnHgt"
            width="200"
            sortable
          />
          <el-table-column label="患者体重(kg)" align="center" prop="patnWt" width="90" />
          <el-table-column label="性别" align="center" prop="gend" width="60" />
          <el-table-column label="计划生育手术类别" align="center" prop="birctrlType" />
          <el-table-column
            label="计划生育手术或生育日期"
            align="center"
            prop="birctrlMatnDate"
            width="130"
          />
          <el-table-column label="生育类别" align="center" prop="matnType" width="80" />
          <el-table-column label="妊娠(孕周)" align="center" prop="gesoVal" />
          <el-table-column label="新生儿标志" align="center" prop="nwbFlag" />
          <el-table-column label="新生儿日、月龄" align="center" prop="nwbAge" />
          <el-table-column label="哺乳期标志" align="center" prop="suckPrdFlag" />
          <el-table-column label="过敏史" align="center" prop="algsHis" />
          <el-table-column
            label="开方科室名称"
            align="center"
            prop="prscDeptName"
            width="200"
            sortable
          />
          <el-table-column label="开方科室编号" align="center" prop="prscDeptCode" width="90" />
          <el-table-column label="开方医保医师代码" align="center" prop="drCode" width="60" />
          <el-table-column label="开方医师姓名" align="center" prop="prscDrName" />
          <el-table-column
            label="开方医师证件类型"
            align="center"
            prop="prscDrCertType"
            width="130"
          />
          <el-table-column label="开方医师证件号码" align="center" prop="prscDrCertno" width="80" />
          <el-table-column label="医生职称编码" align="center" prop="drProfttlCodg" />
          <el-table-column label="医生职称名称" align="center" prop="drProfttlName" />
          <el-table-column label="医生科室编码" align="center" prop="drDeptCode" />
          <el-table-column label="医生科室名称" align="center" prop="drDeptName" />
          <el-table-column label="科别" align="center" prop="caty" />
          <el-table-column label="就诊时间" align="center" prop="mdtrtTime" width="90">
            <template #default="scope">
              {{ formatDate(scope.row.mdtrtTime) }}
            </template>
          </el-table-column>
          <el-table-column label="病种编码" align="center" prop="diseCodg" width="200" sortable />
          <el-table-column label="病种名称" align="center" prop="diseName" width="90" />
          <el-table-column label="特殊病种标志" align="center" prop="spDiseFlag" width="60" />
          <el-table-column label="主诊断代码" align="center" prop="maindiagCode" />
          <el-table-column label="主诊断名称" align="center" prop="maindiagName" width="130" />
          <el-table-column label="疾病病情描述" align="center" prop="diseCondDscr" width="80" />
          <el-table-column label="医保费用结算类型" align="center" prop="hiFeesetlType" />
          <el-table-column label="医保费用类别名称" align="center" prop="hiFeesetlName" />
          <el-table-column label="挂号费" align="center" prop="rgstFee" />
          <el-table-column label="医疗费总额" align="center" prop="medfeeSumamt" />
          <el-table-column label="是否初诊" align="center" prop="fstdiagFlag" />
          <el-table-column label="扩展数据" align="center" prop="extras" />
          <el-table-column label="院内内部处方号" align="center" prop="prescriptionNo" />
          <el-table-column label="医保处方编号" align="center" prop="hiRxno" />
        </el-table>
      </div>
      <div style="width: 100%">
        <div class="title">诊断信息</div>
        <el-table max-height="650" :data="discinfoList" border>
          <el-table-column label="诊断类别" align="center" prop="diagType" width="200" sortable />
          <el-table-column label="主诊断标志" align="center" prop="maindiagFlag" width="90" />
          <el-table-column label="诊断排序号" align="center" prop="diagSrtNo" width="60" />
          <el-table-column label="诊断代码" align="center" prop="diagCode" />
          <el-table-column label="诊断名称" align="center" prop="diagName" width="130" />
          <el-table-column label="诊断科室名称" align="center" prop="diagDept" width="80" />
          <el-table-column label="诊断科室代码" align="center" prop="diagDeptCode" />
          <el-table-column label="诊断医生编码" align="center" prop="diagDrNo" />
          <el-table-column label="诊断医生姓名" align="center" prop="diagDrName" />
          <el-table-column label="诊断时间" align="center" prop="diagTime" width="90">
            <template #default="scope">
              {{ formatDate(scope.row.diagTime) }}
            </template>
          </el-table-column>
          <el-table-column label="中医病名代码" align="center" prop="tcmDiseCode" />
          <el-table-column label="中医病名" align="center" prop="tcmDiseName" />
          <el-table-column
            label="中医证候代码"
            align="center"
            prop="tcmsympCode"
            width="200"
            sortable
          />
          <el-table-column label="中医证候" align="center" prop="tcmsymp" width="90" />
          <el-table-column label="院内内部处方号" align="center" prop="prescriptionNo" width="60" />
          <el-table-column label="医保处方编号" align="center" prop="hiRxno" />
        </el-table>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="PrescriptionQueryDialog">
import { formatDate, formatDateStr } from '@/utils/index';
const { proxy } = getCurrentInstance();

const title = ref('');
const visible = ref(false);
const emits = defineEmits(['submit']); // 声明自定义事件
const prescriptionQuery = ref(undefined);
const prescriptionInfoList = ref([]); // 初始化为空数组; // 处方信息
const rxdrugdetailList = ref(undefined); // 处方明细信息
const mdtrtinfoList = ref([]); // 就诊信息
const discinfoList = ref(undefined); // 诊断信息

const data = reactive({
  form: {},
  rules: {},
});

const { queryParams, form, rules } = toRefs(data);

const props = defineProps({
  prescriptionQuery: {
    type: Object,
    required: false,
  },
});

// 显示弹框
function show() {
  reset();
  prescriptionQuery.value = props.prescriptionQuery;
  console.log(prescriptionQuery.value, '处方查询prescriptionQuery.value');
  prescriptionInfoList.value.push(prescriptionQuery.value); // 处方信息
  rxdrugdetailList.value = prescriptionQuery.value.rxDetlList
    ? prescriptionQuery.value.rxDetlList
    : []; // 处方明细信息

  mdtrtinfoList.value.push(prescriptionQuery.value.rxOtpinfo); // 就诊信息
  discinfoList.value = prescriptionQuery.value.rxDiseList ? prescriptionQuery.value.rxDiseList : []; // 诊断信息
  visible.value = true;
}

/** 重置操作表单 */
function reset() {
  prescriptionInfoList.value = []; // 初始化为空数组; // 处方信息
  rxdrugdetailList.value = []; // 处方明细信息
  mdtrtinfoList.value = []; // 就诊信息
  discinfoList.value = []; // 诊断信息
}

/** 取消按钮 */
function cancel() {
  visible.value = false;
  reset();
}
defineExpose({
  show,
});
</script>
<style scoped>
.el-form--inline .el-form-item {
  display: inline-flex;
  vertical-align: middle;
  margin-right: 10px !important;
}

.title {
  font-weight: bold;
  font-size: large;
  margin-bottom: 10px;
}
</style>
