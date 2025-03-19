<template>
  <div class="app-container">
    <el-dialog :title="title" v-model="visible" width="95%" append-to-body>
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="Plus"
            @click="handleAdd"
            v-hasPermi="['system:user:add']"
            >提交审核</el-button
          >
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="Printer"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['monitor:job:remove']"
            >打印</el-button
          >
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="EditPen"
            @click="getList"
            v-hasPermi="['system:user:import']"
            >计算金额</el-button
          >
        </el-col>
      </el-row>

      <el-form
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        v-show="showSearch"
        label-width="90px"
      >
        <el-form-item label="单据号" prop="busNo">
          <el-input
            v-model="queryParams.busNo"
            placeholder="单据号："
            clearable
            style="width: 150px"
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item
          label="采购部门："
          prop="categoryEnum"
          label-width="100px"
        >
          <el-select
            v-model="queryParams.categoryEnum"
            placeholder=""
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="dict in appointmentRequiredFlagOptions"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          label="部门经手人："
          prop="applicantId"
          label-width="120px"
        >
          <el-select
            v-model="queryParams.applicantId"
            placeholder=""
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="dict in activeFlagOptions"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="单据日期：">
          <el-date-picker
            v-model="queryParams.applyTimeStart"
            value-format="YYYY/MM/DD HH:mm:ss"
            type="datetime"
            start-placeholder="开始日期"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="供应商：" prop="supplierId">
          <el-input
            v-model="queryParams.supplierId"
            placeholder="回车查询"
            clearable
            style="width: 150px"
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="药品类型：" prop="categoryEnum">
          <el-select
            v-model="queryParams.categoryEnum"
            placeholder=""
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="dict in activeFlagOptions"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="仓库类型：" prop="purposeLocationId">
          <el-select
            v-model="queryParams.purposeLocationId"
            placeholder=""
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="dict in activeFlagOptions"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <el-tabs type="border-card">
        <el-tab-pane label="入库单明细">
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="Plus"
                @click="addNewRow"
                v-hasPermi="['system:user:add']"
                >添加行</el-button
              >
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="Delete"
                :disabled="multiple"
                @click="deleteSelectedRows"
                v-hasPermi="['monitor:job:remove']"
                >删除行</el-button
              >
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="Plus"
                @click="handleAdd"
                v-hasPermi="['system:user:add']"
                >确认当前行</el-button
              >
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="Delete"
                :disabled="multiple"
                @click="handleDelete"
                v-hasPermi="['monitor:job:remove']"
                >取消行编辑</el-button
              >
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="Delete"
                :disabled="multiple"
                @click="handleDelete"
                v-hasPermi="['monitor:job:remove']"
                >取消所有修改</el-button
              >
            </el-col>
            <!-- <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="Search"
                @click="getList"
                v-hasPermi="['system:user:import']"
                style="margin-left: 20px"
                >另存模板</el-button
              >
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="warning"
                plain
                icon="CircleClose"
                @click="handleClear"
                v-hasPermi="['system:user:export']"
                >选择模板</el-button
              >
            </el-col> -->
          </el-row>

          <el-table
            v-loading="loading"
            :data="purchaseinventoryList"
            @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column
              label="项目"
              align="center"
              key="itemTable"
              prop="itemTable"
              width="300"
            >
              <template #default="scope">
                <el-select
                  v-model="scope.row.itemTable"
                  placeholder="请选择计量单位"
                >
                  <el-option label="单位1" value="单位1" />
                  <el-option label="单位2" value="单位2" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column
              label="规格"
              align="center"
              key="statusEnum_enumText"
              prop="statusEnum_enumText"
              width="130"
            >
              <template #default="scope">
                <el-input v-model="scope.row.busNo" placeholder="" />
              </template>
            </el-table-column>
            <el-table-column
              label="厂家/产地"
              align="center"
              key="supplierId"
              prop="supplierId"
              :show-overflow-tooltip="true"
              width="130"
            >
              <template #default="scope">
                <el-input v-model="scope.row.busNo" placeholder="" />
              </template>
            </el-table-column>
            <el-table-column
              label="计量单位"
              align="center"
              key="unitCode"
              prop="unitCode"
              :show-overflow-tooltip="true"
              width="100"
            >
              <template #default="scope">
                <el-select
                  v-model="scope.row.unitCode"
                  placeholder="请选择计量单位"
                >
                  <el-option label="单位1" value="单位1" />
                  <el-option label="单位2" value="单位2" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column
              label="仓库"
              align="center"
              key="approverId"
              prop="approverId"
              :show-overflow-tooltip="true"
              width="130"
            >
              <template #default="scope">
                <el-select
                  v-model="scope.row.approverId"
                  placeholder="请选择仓库"
                >
                  <el-option label="仓库1" value="仓库1" />
                  <el-option label="仓库2" value="仓库2" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column
              label="货位"
              align="center"
              key="applicantId"
              prop="applicantId"
              width="130"
            >
              <template #default="scope">
                <el-select
                  v-model="scope.row.approverId"
                  placeholder="请选择货位"
                >
                  <el-option label="货位1" value="货位1" />
                  <el-option label="货位2" value="货位2" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column
              label="总库存数量"
              align="center"
              key="approverId"
              prop="approverId"
              width="130"
            >
              <template #default="scope">
                <el-input v-model="scope.row.busNo" placeholder="" />
              </template>
            </el-table-column>
            <el-table-column
              label="采购数量"
              align="center"
              key="applyTime"
              prop="applyTime"
              width="130"
            >
              <template #default="scope">
                <el-input v-model="scope.row.busNo" placeholder="" />
              </template>
            </el-table-column>
            <el-table-column
              label="采购单价 "
              align="center"
              key="approvalTime"
              prop="approvalTime"
              width="130"
            >
              <template #default="scope">
                <el-input v-model="scope.row.busNo" placeholder="" />
              </template>
            </el-table-column>
            <el-table-column
              label="合计金额 "
              align="center"
              key="totalPrice"
              prop="totalPrice"
              width="130"
            >
              <template #default="scope">
                <el-input v-model="scope.row.totalPrice" placeholder="" />
              </template>
            </el-table-column>
            <el-table-column
              label="产品批号"
              align="center"
              key="lotNumber"
              prop="lotNumber"
              width="160"
            >
              <template #default="scope">
                <el-input v-model="scope.row.lotNumber" placeholder="" />
              </template>
            </el-table-column>
            <el-table-column
              label="生产日期"
              align="center"
              key="startTime"
              prop="startTime"
              width="120"
            >
              <template #default="scope">
                <el-date-picker
                  v-model="scope.row.startTime"
                  type="date"
                  placeholder="选择日期"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
                />
              </template>
            </el-table-column>
            <el-table-column
              label="有效期至"
              align="center"
              key="endTime"
              prop="endTime"
              width="120"
            >
              <template #default="scope">
                <el-date-picker
                  v-model="scope.row.endTime"
                  type="date"
                  placeholder="选择日期"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
                />
              </template>
            </el-table-column>
            <el-table-column
              label="有效期(月)"
              align="center"
              key="approvalTime"
              prop="approvalTime"
              width="100"
            >
              <template #default="scope">
                <el-input v-model="scope.row.busNo" placeholder="" />
              </template>
            </el-table-column>
            <el-table-column
              label="发票号 "
              align="center"
              key="invoiceNo"
              prop="invoiceNo"
              width="130"
            >
              <template #default="scope">
                <el-input v-model="scope.row.invoiceNo" placeholder="" />
              </template>
            </el-table-column>
            <el-table-column
              label="药品追溯码"
              align="center"
              key="traceNo"
              prop="traceNo"
              width="130"
            >
              <template #default="scope">
                <el-input v-model="scope.row.traceNo" placeholder="" />
              </template>
            </el-table-column>
            <el-table-column
              label="追溯码包装"
              align="center"
              key="approvalTime"
              prop="approvalTime"
              width="130"
            >
              <template #default="scope">
                <el-input v-model="scope.row.busNo" placeholder="" />
              </template>
            </el-table-column>
            <el-table-column
              label="备注"
              align="center"
              key="approvalTime"
              prop="approvalTime"
              width="130"
            >
              <template #default="scope">
                <el-input v-model="scope.row.busNo" placeholder="" />
              </template>
            </el-table-column>
            <el-table-column
              label="操作"
              align="center"
              width="140"
              class-name="small-padding fixed-width"
              fixed="right"
            >
              <template #default="scope">
                <el-button
                  link
                  type="primary"
                  icon="Edit"
                  @click="handleUpdate(scope.row)"
                  v-hasPermi="['system:user:edit']"
                  >编辑</el-button
                >
                <el-button
                  link
                  type="primary"
                  icon="View"
                  @click="handleView(scope.row)"
                  v-hasPermi="['system:user:remove']"
                  >查看</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <pagination
            v-show="total > 0"
            :total="total"
            v-model:page="queryParams.pageNo"
            v-model:limit="queryParams.pageSize"
            @pagination="getList"
          />
        </el-tab-pane>
      </el-tabs>
      <el-row
        :gutter="10"
        class="mb8"
        style="
          margin-top: 15px;
          display: flex;
          align-items: center;
          justify-content: flex-start;
        "
      >
        <el-col :span="2">
          <span>制单人：</span>
        </el-col>
        <el-col :span="2">
          <span>审核人：</span>
        </el-col>
        <el-col :span="2">
          <span>单据状态：</span>
        </el-col>
        <el-col :span="1.5">
          <el-row
            :gutter="8"
            style="
              display: flex;
              align-items: center;
              justify-content: flex-start;
            "
          >
            <el-col :span="6">
              <span>合计：</span>
            </el-col>
            <el-col :span="18">
              <el-input v-model="heji" placeholder="" disabled />
            </el-col>
          </el-row>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script setup name="InventoryReceiptDialog">
// import {
//   getPurchaseinventoryList,
//   editPurchaseinventory,
//   addPurchaseinventory,
//   getPurchaseinventoryOne,
//   getInit,
//   deptTreeSelect,
//   locationTreeSelect,
//   delPurchaseinventory,
// } from "./components/purchaseinventory";

const router = useRouter();
const { proxy } = getCurrentInstance();
const purchaseinventoryRef = ref(null); // 初始化 ref
const {
  adm_location,
  category_code,
  service_type_code,
  specialty_code,
  med_chrgitm_type,
  financial_type_code,
} = proxy.useDict(
  "adm_location",
  "category_code",
  "service_type_code",
  "specialty_code",
  "med_chrgitm_type",
  "financial_type_code"
);

const purchaseinventoryList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const activeFlagOptions = ref(undefined);
const appointmentRequiredFlagOptions = ref(undefined);
const deptOptions = ref(undefined); // 部门树选项
const locationOptions = ref(undefined); // 地点树选项
const dateRange = ref([]);
const visible = ref(false);

// 是否停用
const statusFlagOptions = ref(undefined);

const data = reactive({
  form: {},
  queryParams: {
    pageNo: 1,
    pageSize: 10,
    searchKey: undefined, // 供应商名称
    busNo: undefined, // 编码
    statusEnum: undefined, // 状态（包括 1：预置，2：启用，3：停用）
    sourceEnum: undefined, // 来源（包括 1：厂商/产地目录分类，2：自定义）
  },
  rules: {
    offeredOrgId: [
      { required: true, message: "提供部门不能为空", trigger: "blur" },
    ],
    categoryCode: [
      { required: true, message: "服务分类不能为空", trigger: "blur" },
    ],
    fwTypeCode: [
      { required: true, message: "服务类型不能为空", trigger: "blur" },
    ],
    specialtyCode: [
      { required: true, message: "服务专业不能为空", trigger: "blur" },
    ],
    locationId: [{ required: true, message: "地点不能为空", trigger: "blur" }],
    name: [{ required: true, message: "服务名称不能为空", trigger: "blur" }],
    contact: [
      { required: true, message: "联系人电话不能为空", trigger: "blur" },
    ],
    appointmentRequiredFlag: [
      { required: true, message: "预约要求不能为空", trigger: "blur" },
    ],
    activeFlag: [
      { required: true, message: "活动标识不能为空", trigger: "blur" },
    ],
    chargeName: [{ required: true, message: "名称不能为空", trigger: "blur" }],
    description: [{ required: true, message: "描述不能为空", trigger: "blur" }],
    cwTypeCode: [
      { required: true, message: "财务类别不能为空", trigger: "blur" },
    ],
    ybType: [{ required: true, message: "医保类别不能为空", trigger: "blur" }],
    price: [{ required: true, message: "基础价格不能为空", trigger: "blur" }],
  },
});

const { queryParams, form, rules } = toRefs(data);

const selectedRows = ref([]); // 用于存储选中的行
const addNewRow = () => {
  purchaseinventoryList.value.push({
    busNo: "",
    statusEnum_enumText: "",
    supplierId: "",
    purposeLocationId: "",
    approverId: "",
    applicantId: "",
    applyTime: null,
    approvalTime: null,
    // 其他字段...
  });
};

/** 挂号收费查询下拉树结构 */
function getPurchaseinventoryTypeList() {
  getInit().then((response) => {
    console.log(response, "response");
    activeFlagOptions.value = response.data.activeFlagOptions; // 活动标记
    appointmentRequiredFlagOptions.value =
      response.data.appointmentRequiredFlagOptions; // 预约必填标记
  });
}

/** 查询部门下拉树结构 */
function getDeptTree() {
  deptTreeSelect().then((response) => {
    console.log(response, "response查询部门下拉树结构");

    deptOptions.value = response.data.records;
    console.log(deptOptions.value, "部门下拉树结构");
  });
}

/** 查询地点下拉树结构 */
function getLocationTree() {
  locationTreeSelect().then((response) => {
    console.log(response, "response查询部门下拉树结构");
    locationOptions.value = response.data.records;
    console.log(locationOptions.value, "部门下拉树结构");
  });
}

/** 查询挂号收费项目列表 */
function getList() {
  loading.value = true;
  // // queryParams.value.statusEnum = +queryParams.value.statusEnum
  // console.log(queryParams.value, "queryParams.value");
  // getPurchaseinventoryList(queryParams.value).then((res) => {
  loading.value = false;
  //   console.log(res, "res");
  //   purchaseinventoryList.value = res.data.records;
  //   total.value = res.data.total;
  //   console.log(total.value, "total.value");
  // });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.applyTimeStart = dateRange.value[0];
  queryParams.value.applyTimeEnd = dateRange.value[1];
  queryParams.value.pageNo = 1;
  getList();
}

/** 清空条件按钮操作 */
function handleClear() {
  // 清空查询条件
  proxy.resetForm("queryRef");
  getList();
}

/** 选择条数  */
function handleSelectionChange(selection) {
  console.log(selection, "selection");
  // selectedData.value = selection.map((item) => ({ ...item })); // 存储选择的行数据
  ids.value = selection.map((item) => item.id);
  selectedRows.value = selection;
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

const deleteSelectedRows = () => {
  if (selectedRows.value.length === 0) {
    alert("请先选择要删除的行");
    return;
  }

  purchaseinventoryList.value = purchaseinventoryList.value.filter(
    (row) => !selectedRows.value.includes(row)
  );

  selectedRows.value = []; // 清空选中行
};

/** 重置操作表单 */
function reset() {
  form.value = {
    id: undefined,
    name: undefined,
    categoryCode: undefined,
    cwTypeCode: undefined,
    fwTypeCode: undefined,
    specialtyCode: undefined,
    locationId: undefined,
    offeredOrgId: undefined,
    activeFlag: undefined,
    extraDetails: undefined,
    contact: undefined,
    appointmentRequiredFlag: undefined,
    chargeName: undefined,
    price: undefined,
    description: undefined,
    ybType: undefined,
    title: undefined,
    comment: undefined,
  };
  proxy.resetForm("purchaseinventoryRef");
}
// 显示弹框
function show() {
  reset();
  visible.value = true;
  getList();
}
/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}
/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "新增";
}
/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  form.value = JSON.parse(JSON.stringify(row));
  form.value.fwTypeCode = form.value.typeCode;
  open.value = true;
  title.value = "编辑";
}
/** 提交按钮 */
function submitForm() {
  if (form.value.id != undefined) {
    // 移除规则
    rules.value.chargeName = [];
    rules.value.description = [];
    rules.value.cwTypeCode = [];
    rules.value.ybType = [];
    rules.value.price = [];
  } else {
    // 恢复规则
    rules.value.cwTypeCode = [
      { required: true, message: "财务类别不能为空", trigger: "blur" },
    ];
    rules.value.ybType = [
      { required: true, message: "医保类别不能为空", trigger: "blur" },
    ];
    rules.value.price = [
      { required: true, message: "基础价格不能为空", trigger: "blur" },
    ];

    rules.value.chargeName = [
      { required: true, message: "名称不能为空", trigger: "blur" },
    ];
    rules.value.description = [
      { required: true, message: "描述不能为空", trigger: "blur" },
    ];
  }

  // const nameData = name || chargeName;
  // 服务名称
  form.value.name = getName();
  // 收费名称
  form.value.chargeName = getName();
  proxy.$refs["purchaseinventoryRef"].validate((valid) => {
    if (valid) {
      if (form.value.id != undefined) {
        // 调用转换函数
        const transformFormEditParam = transformFormEditData(form);
        console.log(transformFormEditData, "transformFormEditData");
        console.log(form.value, "editPurchaseinventory", form.value.statusEnum);
        editPurchaseinventory(transformFormEditParam).then((response) => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          reset();
          getList();
        });
      } else {
        // 调用转换函数
        const transformedData = transformFormData(form);
        console.log(transformedData, "transformedData");
        addPurchaseinventory(transformedData).then((response) => {
          reset();
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

// 获取完整地址字符串
function getName() {
  console.log(service_type_code.value, "service_type_code.value");
  // 服务类型
  const serviceTypeText = proxy.selectDictLabel(
    service_type_code.value,
    form.value.fwTypeCode
  );
  // 服务分类
  const categoryCodeText = proxy.selectDictLabel(
    category_code.value,
    form.value.categoryCode
  );
  // 服务专业
  const specialtyCodeText = proxy.selectDictLabel(
    specialty_code.value,
    form.value.specialtyCode
  );
  console.log(
    serviceTypeText,
    "serviceTypeText",
    categoryCodeText,
    specialtyCodeText
  );
  const nameParts = [
    serviceTypeText,
    form.value.addressCity,
    categoryCodeText,
    specialtyCodeText,
  ];

  // 使用 reduce 方法拼接地址，非空字段之间用 '-' 连接
  return nameParts.reduce((acc, part) => {
    if (part) {
      if (acc) {
        acc += " - "; // 在非空字段之间添加 '-'
      }
      acc += part;
    }
    return acc;
  }, "");
}
/** 详细按钮操作 */
function handleView(row) {
  reset();
  title.value = "查看";
  open.value = true;
  getPurchaseinventoryOne(row.id).then((response) => {
    console.log(response, "responsebbbb", row.id);
    form.value = response.data;
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const delId = row.id || ids.value;
  proxy.$modal
    .confirm("是否确认删除以上数据?")
    .then(function () {
      return delPurchaseinventory({ ids: delId.join(",") });
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}
// 转换insert参数函数
const transformFormData = (form) => {
  const {
    id,
    name,
    categoryCode,
    // typeCode,
    cwTypeCode,
    fwTypeCode,
    specialtyCode,
    locationId,
    offeredOrgId,
    activeFlag,
    extraDetails,
    contact,
    appointmentRequiredFlag,
    chargeName,
    price,
    description,
    ybType,
    title,
    comment,
  } = form.value;

  return {
    healthcareServiceFormData: {
      id,
      activeFlag,
      offeredOrgId,
      categoryCode,
      typeCode: fwTypeCode,
      specialtyCode,
      locationId,
      name,
      contact,
      appointmentRequiredFlag,
      extraDetails,
      comment,
    },
    chargeItemDefinitionFormData: {
      id,
      chargeName,
      title,
      orgId: offeredOrgId,
      description,
      typeCode: cwTypeCode,
      ybType,
      price,
    },
  };
};

// 转换insert参数函数
const transformFormEditData = (form) => {
  const {
    id,
    name,
    categoryCode,
    // typeCode,
    cwTypeCode,
    fwTypeCode,
    specialtyCode,
    locationId,
    offeredOrgId,
    activeFlag,
    extraDetails,
    contact,
    appointmentRequiredFlag,
    chargeName,
    price,
    description,
    ybType,
    title,
    comment,
  } = form.value;

  return {
    healthcareServiceFormData: {
      id,
      activeFlag,
      offeredOrgId,
      categoryCode,
      typeCode: fwTypeCode,
      specialtyCode,
      locationId,
      name,
      contact,
      appointmentRequiredFlag,
      extraDetails,
      comment,
    },
  };
};
defineExpose({
  show,
});
</script>
<style scoped>
.custom-tree-node {
  display: flex;
  align-items: center;
}

.title {
  font-weight: bold;
  font-size: large;
  margin-bottom: 10px;
}
</style>