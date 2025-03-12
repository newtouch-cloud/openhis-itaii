<template>
  <div class="prescription">
    <div class="prescription-header">
        <el-row class="row-spacing">
            <el-button type="primary" plain icon="Printer" @click="printPrescription">打印处方</el-button>
		    <el-button type="primary" icon="Printer" plain @click="printInfusionRecordPrescription">打印输液处方</el-button>
        </el-row>
        <el-row class="row-spacing">
            <el-col :span="6">
                <span>门诊号</span>
                <span class="fixed-width">12345689789123456</span>
            </el-col>
            <el-col :span="6">
                <span>姓名</span>
                <span class="fixed-width">小红</span>
            </el-col>
            <el-col :span="4">
                <span>性别</span>
                <span style="display: inline-block;width: 80px;margin-left: 10px;border-bottom: 1px solid black;text-align: center;">男</span>
            </el-col>
            <el-col :span="7">
                <span>年龄</span>
                <span class="fixed-width">25岁10月10天</span>
            </el-col>
        </el-row>
        <el-row class="row-spacing">
            <el-col :span="6">
                <span>处方号</span>
                <span class="fixed-width">12345689789123456</span>
            </el-col>
            <el-col :span="6">
                <span>医生</span>
                <span class="fixed-width">张三</span>
            </el-col>
            <el-col :span="4">
                <span>类型</span>
                <span style="display: inline-block;width: 80px;margin-left: 10px;border-bottom: 1px solid black;text-align: center;">自自费费</span>
            </el-col>
            <el-col :span="7">
                <span>登记时间</span>
                <span class="fixed-width">2025年1月1日 11:11:11</span>
            </el-col>
        </el-row>
        <el-row class="row-spacing">
            <el-col :span="6">
                <span>病人ID</span>
                <span class="fixed-width">12345683456</span>
            </el-col>
            <el-col :span="6">
                <span>状态</span>
                <span class="fixed-width" style="color: salmon;">门诊就诊|不分发药|已审核</span>
            </el-col>
        </el-row>
    </div>
    <div style="margin-top: 15px;">
      <el-tabs type="border-card">
        <el-tab-pane label="西药处方">
            <el-table :data="xiyaoList" border style="width: 100%;height: 58vh" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" align="center" />
                <el-table-column prop="name" label="分组" width="60" />
                <el-table-column prop="idCard" label="药品" width="180" />
                <el-table-column prop="description" label="规格" width="160" />
                <el-table-column prop="patientBusNo" label="用药途径" width="80" />
                <el-table-column prop="encounterBusNo" label="用药频次" width="80" />
                <el-table-column prop="phone" label="单次剂量" width="80" />
                <el-table-column prop="genderEnum_enumText" label="剂量单位" width="80" />
                <el-table-column prop="phone" label="用药天数" width="60" />
                <el-table-column prop="encounterTime" label="总量" width="40" />
                <el-table-column prop="encounterTime" label="单位" width="40" />
                <el-table-column prop="subjectStatusEnum_enumText" label="单价（元）" width="100" />
                <el-table-column prop="organizationName" label="组数" width="60" />
                <el-table-column prop="doctorName" label="主嘱" width="40" />
                <el-table-column prop="doctorName" label="用法说明" width="180" />
                <el-table-column prop="doctorName" label="皮试" width="40" />
                <el-table-column prop="doctorName" label="皮试状态" width="60" />
                <el-table-column prop="doctorName" label="皮试结果" width="60" />
                <el-table-column prop="doctorName" label="是否计价" width="60" />
		    </el-table>
        </el-tab-pane>
        <el-tab-pane label="中药处方">
            <p style="color: blue;font-size: 17px;margin-top: 0;font-weight: bold;">中药方付数:1&nbsp;&nbsp;&nbsp;用法:</p>
            <el-table :data="zhongyaoList" border style="width: 100%;height: 58vh" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" align="center" />
                <el-table-column prop="idCard" label="药品" width="180" />
                <el-table-column prop="encounterTime" label="单位" width="60" />
                <el-table-column prop="encounterTime" label="数量" width="60" />
                <el-table-column prop="subjectStatusEnum_enumText" label="单价" width="100" />
                <el-table-column prop="organizationName" label="煎用方法" width="140" />
                <el-table-column prop="doctorName" label="用药频次" width="160" />
                <el-table-column prop="doctorName" label="规格" width="180" />
                <el-table-column prop="doctorName" label="是否计价" width="80" />
		    </el-table>
        </el-tab-pane>
        <el-tab-pane label="辅助检查">
            <el-table :data="fuzhuList" border style="width: 100%;height: 65vh" @selection-change="handleSelectionChange">
                <el-table-column  type="selection" width="80" align="center">
                    <template #header>
                        <span>是否执行</span>
                    </template>
                </el-table-column>
                <el-table-column prop="idCard" label="检验检查" width="160" />
                <el-table-column prop="encounterTime" label="说明" width="180" />
                <el-table-column prop="encounterTime" label="部位" width="60" />
                <el-table-column prop="subjectStatusEnum_enumText" label="单位" width="60" />
                <el-table-column prop="organizationName" label="数量" width="60" />
                <el-table-column prop="doctorName" label="单价" width="60" />
                <el-table-column prop="doctorName" label="执行科室" width="140" />
                <el-table-column prop="doctorName" label="执行时间" width="140" />
                <el-table-column prop="doctorName" label="执行医生" width="100" />
                <el-table-column prop="doctorName" label="皮试" width="60" />
                <el-table-column prop="doctorName" label="皮试状态" width="80" />
                <el-table-column prop="doctorName" label="皮试结果" width="80" />
                <el-table-column label="操作" align="center" width="90" fixed="right" >
                    <template #default="scope">
                            <el-button link type="primary" icon="Edit" @click="handlePrescription(scope.row)" v-hasPermi="['system:menu:edit']">打印样本号</el-button>
                    </template>
			    </el-table-column>
                <el-table-column prop="doctorName" label="是否计价" width="80" />
		    </el-table>
        </el-tab-pane>
        <el-tab-pane label="治疗与手术记录">
            <el-table :data="shoushuList" border style="width: 100%;height: 58vh" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" align="center" />
                <el-table-column prop="idCard" label="手术治疗" width="180" />
                <el-table-column prop="encounterTime" label="疗程项目" width="180" />
                <el-table-column prop="encounterTime" label="数量" width="60" />
                <el-table-column prop="subjectStatusEnum_enumText" label="单位" width="60" />
                <el-table-column prop="organizationName" label="单价" width="80" />
                <el-table-column prop="doctorName" label="治疗次数" width="100" />
                <el-table-column prop="doctorName" label="用法说明" width="180" />
                <el-table-column prop="doctorName" label="是否理疗" width="120" />
                <el-table-column prop="doctorName" label="执行科室" width="120" />
		    </el-table>
        </el-tab-pane>
        <el-tab-pane label="耗材处方">
            <div style="align-items: center;">
                <span style="color: blue;">耗材药房：</span>
                <el-select v-model="value" placeholder="请选择">
                    <el-option
                    v-for="item in hcyaofangList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                    >
                    </el-option>
                </el-select>&nbsp;
                <el-button type="primary" plain @click="handleSelectionChange">更改耗材药房</el-button>
            </div>
            <br>
            <el-table :data="haocaiList" border style="width: 100%;height: 58vh;" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" align="center" />
                <el-table-column prop="idCard" label="医用耗材" width="180" />
                <el-table-column prop="encounterTime" label="单位" width="60" />
                <el-table-column prop="encounterTime" label="数量" width="60" />
                <el-table-column prop="subjectStatusEnum_enumText" label="单价" width="100" />
		    </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>
    <div></div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
// import { listOutpatienRecords, listDoctorNames } from "./component/api"

const xiyaoList = ref([]);
const zhongyaoList = ref([]);
const fuzhuList = ref([]);
const shoushuList = ref([]);
const haocaiList = ref([]);
const hcyaofangList = ref([]);

const { proxy } = getCurrentInstance();

const data = reactive({
	form: {},
	queryParams: {
		pageNo: 1,
		pageSize: 10,
		doctorName: undefined,
		searchKey: undefined,
		phone: undefined,
		patientid: undefined
	},
});
const { queryParams } = toRefs(data);

function handleSelectionChange(selection) {
//   ids.value = selection.map(item => item.dictId);
//   single.value = selection.length != 1;
//   multiple.value = !selection.length;
}
//打印输液处方
function printInfusionRecordPrescription() {

}
//打印处方
function printPrescription() {

}

</script>

<style>
.prescription-header{
    font-size: 15px;
    margin-top: -20px;
    /* background-color: #813030; */
}
.fixed-width {
  display: inline-block;
  width: 190px;
  margin-left: 10px;
  border-bottom: 1px solid black;
  text-align: center;
}
.row-spacing {
  margin-bottom: 20px; /* 设置每行的底部间距 */
}
</style>