package com.openhis.web.pharmacymanage.appservice.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import com.openhis.yb.service.YbManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.*;
import com.openhis.administration.domain.Organization;
import com.openhis.administration.service.IOrganizationService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.medication.domain.MedicationDispense;
import com.openhis.medication.service.IMedicationDispenseService;
import com.openhis.web.pharmacymanage.appservice.IChineseMedicineDispenseAppService;
import com.openhis.web.pharmacymanage.dto.*;
import com.openhis.web.pharmacymanage.mapper.ChineseMedicineDispenseMapper;
import com.openhis.workflow.domain.InventoryItem;
import com.openhis.workflow.service.IInventoryItemService;

/**
 * 中药发放 应用实现类
 *
 * @author wangyang
 * @date 2025/3/14
 */
@Service
public class IChineseMedicineDispenseAppServiceImpl implements IChineseMedicineDispenseAppService {

    @Autowired
    private IOrganizationService iOrganizationService;

    @Autowired
    IMedicationDispenseService iMedicationDispenseService;

    @Autowired
    IInventoryItemService iInventoryItemService;

    @Autowired
    ChineseMedicineDispenseMapper chineseMedicineDispenseMapper;

    @Autowired
    YbManager ybService;

    /**
     * 获取页面初始化信息
     *
     * @return 初始化信息
     */
    @Override
    public R<?> init() {

        DispenseInitDto initDto = new DispenseInitDto();

        // 获取科室下拉选列表
        List<Organization> organizationList =
            iOrganizationService.getList(OrganizationType.DEPARTMENT.getValue(), OrganizationClass.CLINIC.getValue());
        List<DispenseInitDto.DepartmentOption> organizationOptions = organizationList.stream()
            .map(organization -> new DispenseInitDto.DepartmentOption(organization.getId(), organization.getName()))
            .collect(Collectors.toList());

        // 未发药原因下拉选列表
        List<DispenseInitDto.NotPerformedReasonOption> notPerformedReasonOptions =
            Stream.of(NotPerformedReasonEnum.values())
                .map(notPerformedReason -> new DispenseInitDto.NotPerformedReasonOption(notPerformedReason.getValue(),
                    notPerformedReason.getInfo()))
                .collect(Collectors.toList());

        initDto.setDepartmentOptions(organizationOptions).setNotPerformedReasonOptions(notPerformedReasonOptions);
        return R.ok(initDto);
    }

    /**
     * 分页查询就诊病人列表
     *
     * @param encounterInfoSearchParam 查询条件
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 就诊病人列表
     */
    @Override
    public R<?> getEncounterInfoListPage(EncounterInfoSearchParam encounterInfoSearchParam, String searchKey,
        Integer pageNo, Integer pageSize, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<EncounterInfoSearchParam> queryWrapper =
            HisQueryUtils.buildQueryWrapper(encounterInfoSearchParam, searchKey,
                new HashSet<>(Arrays.asList(CommonConstants.FieldName.PatientName, CommonConstants.FieldName.idCard,
                    CommonConstants.FieldName.PatientPyStr, CommonConstants.FieldName.PatientWbStr)),
                request);

        // 查询就诊病人列表
        Page<EncounterInfoPageDto> encounterInfoPage =
            chineseMedicineDispenseMapper.selectEncounterInfoListPage(new Page<>(pageNo, pageSize), queryWrapper);

        encounterInfoPage.getRecords().forEach(encounterInfo -> {
            // 性别
            encounterInfo.setGenderEnum_enumText(
                EnumUtils.getInfoByValue(AdministrativeGender.class, encounterInfo.getGenderEnum()));
            // 性别
            encounterInfo
                .setStatusEnum_enumText(EnumUtils.getInfoByValue(DispenseStatus.class, encounterInfo.getStatusEnum()));
        });
        return R.ok(encounterInfoPage);
    }

    /**
     * 查询处方单列表
     *
     * @param encounterId 就诊号
     * @return 处方单列表
     */
    @Override
    public R<?> getPrescriptionInfo(Long encounterId) {

        // 患者基本信息查询
        PrescriptionPatientInfoDto prescriptionPatientInfoDto =
            chineseMedicineDispenseMapper.selectPrescriptionPatientInfo(encounterId);
        // 年龄
        prescriptionPatientInfoDto.setAge(AgeCalculatorUtil.getAge(prescriptionPatientInfoDto.getBirthDate()));
        // 性别
        prescriptionPatientInfoDto.setGenderEnum_enumText(
            EnumUtils.getInfoByValue(AdministrativeGender.class, prescriptionPatientInfoDto.getGenderEnum()));
        // 合同类型
        if (StringUtils.isNotNull(prescriptionPatientInfoDto.getCategoryEnum())) {
            prescriptionPatientInfoDto.setCategoryEnum_enumText(
                EnumUtils.getInfoByValue(FinCategory.class, prescriptionPatientInfoDto.getCategoryEnum()));
        }
        // 处方单信息查询
        List<PrescriptionMedicineInfoDto> prescriptionMedicineInfoList =
            chineseMedicineDispenseMapper.selectPrescriptionMedicineInfoList(encounterId);
        // 计算合计金额
        if (!prescriptionMedicineInfoList.isEmpty()) {
            double totalPrice = 0d;
            for (PrescriptionMedicineInfoDto item : prescriptionMedicineInfoList) {
                totalPrice += item.getTotalPrice().doubleValue();
            }
            prescriptionPatientInfoDto.setTotalPrice(totalPrice);
        }
        PrescriptionInfoDto prescriptionInfoDto = new PrescriptionInfoDto();
        prescriptionInfoDto.setPrescriptionPatientInfoDto(prescriptionPatientInfoDto);
        prescriptionInfoDto.setPrescriptionMedicineInfoDtoList(prescriptionMedicineInfoList);
        return R.ok(prescriptionInfoDto);
    }

    /**
     * 配药
     *
     * @param prescriptionNo 处方号
     * @param preparerId 配药人
     * @return 处理结果
     */
    @Override
    public R<?> prepare(String prescriptionNo, Long preparerId) {

        List<MedicationDispense> medicationDispenseList = new ArrayList<>();
        boolean iMedicationDispenseUpdFlg = true;

        // 配药人检查
        if (!preparerId.equals(SecurityUtils.getLoginUser().getPractitionerId())) {
            // 获取药品待发放信息
            List<DispenseInventoryDto> dispenseInventoryList =
                chineseMedicineDispenseMapper.selectDispenseInventoryInfoByPrescriptionNo(prescriptionNo,
                    CommonConstants.TableName.MED_MEDICATION_DEFINITION);
            if (!dispenseInventoryList.isEmpty()) {

                MedicationDispense medicationDispense;

                for (DispenseInventoryDto dispenseInventoryDto : dispenseInventoryList) {

                    // 当前库存数量(拆零单位)=当前库存数量(拆零单位)-请求数量
                    BigDecimal quantity = dispenseInventoryDto.getInventoryQuantity()
                        .subtract(new BigDecimal(dispenseInventoryDto.getDispenseQuantity()));
                    // 库存数量判定
                    if (quantity.compareTo(BigDecimal.ZERO) < 0) {
                        // 库存数量不足
                        return R.fail(MessageUtils.createMessage(PromptMsgConstant.Inventory.M00002, null));
                    }

                    // 药品发放表项目设定
                    medicationDispense = new MedicationDispense();
                    // id
                    medicationDispense.setId(dispenseInventoryDto.getDispenseId());
                    // 药品发放状态
                    medicationDispense.setStatusEnum(DispenseStatus.PREPARED.getValue());
                    // 状态变更时间
                    medicationDispense.setStatusChangedTime(DateUtils.getNowDate());
                    // 配药人
                    medicationDispense.setPreparerId(preparerId);
                    // 配药时间
                    medicationDispense.setPrepareTime(DateUtils.getNowDate());
                    medicationDispenseList.add(medicationDispense);

                }
                // 药品发放更新
                iMedicationDispenseUpdFlg = iMedicationDispenseService.updateBatchById(medicationDispenseList);

                return iMedicationDispenseUpdFlg
                    ? R.ok(MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null))
                    : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00011, null));
            }
            return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00010, null));
        }

        return R.fail(MessageUtils.createMessage(PromptMsgConstant.MedicationDispense.M00001, null));
    }

    /**
     * 处方单核对发药
     *
     * @param prescriptionNo 处方号
     * @return 处理结果
     */
    @Override
    public R<?> medicineDispense(String prescriptionNo) {

        List<MedicationDispense> medicationDispenseList = new ArrayList<>();
        List<InventoryItem> inventoryItemList = new ArrayList<>();
        boolean iMedicationDispenseUpdFlg = true;
        boolean iInventoryItemUpdFlg = true;

        // 获取药品待发放和库存信息
        List<DispenseInventoryDto> dispenseInventoryList =
            chineseMedicineDispenseMapper.selectDispenseInventoryInfoByPrescriptionNo(prescriptionNo,
                CommonConstants.TableName.MED_MEDICATION_DEFINITION);
        if (!dispenseInventoryList.isEmpty()) {

            MedicationDispense medicationDispense;
            InventoryItem inventoryItem;

            for (DispenseInventoryDto dispenseInventoryDto : dispenseInventoryList) {

                // 药品发放表项目设定
                medicationDispense = new MedicationDispense();
                // id
                medicationDispense.setId(dispenseInventoryDto.getDispenseId());
                // 药品发放状态
                medicationDispense.setStatusEnum(DispenseStatus.COMPLETED.getValue());
                // 状态变更时间
                medicationDispense.setStatusChangedTime(DateUtils.getNowDate());
                // 发药人
                medicationDispense.setPractitionerId(SecurityUtils.getLoginUser().getPractitionerId());
                // 已发药数量
                medicationDispense.setDispenseQuantity(dispenseInventoryDto.getDispenseQuantity());
                // 发药时间
                medicationDispense.setDispenseTime(DateUtils.getNowDate());
                medicationDispenseList.add(medicationDispense);

                // 库存表项目设定
                inventoryItem = new InventoryItem();

                inventoryItem.setId(dispenseInventoryDto.getInventoryId());
                // 当前库存数量(拆零单位)=当前库存数量(拆零单位)-请求数量
                inventoryItem.setQuantity(dispenseInventoryDto.getInventoryQuantity()
                    .subtract(new BigDecimal(dispenseInventoryDto.getDispenseQuantity())));
                inventoryItemList.add(inventoryItem);
            }
            // 药品发放更新
            iMedicationDispenseUpdFlg = iMedicationDispenseService.updateBatchById(medicationDispenseList);
            // 库存更新
            iInventoryItemUpdFlg = iInventoryItemService.updateBatchById(inventoryItemList);

            return iMedicationDispenseUpdFlg && iInventoryItemUpdFlg
                ? R.ok(MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null))
                : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00011, null));
        }

        return R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00010, null));
    }

    /**
     * 作废
     *
     * @param prescriptionNo 处方号
     * @param notPerformedReasonEnum 未发药原因
     * @return 处理结果
     */
    @Override
    public R<?> medicineCancel(String prescriptionNo, Integer notPerformedReasonEnum) {

        List<MedicationDispense> medicationDispenseList = new ArrayList<>();
        boolean iMedicationDispenseUpdFlg = true;

        // 获取药品待发放记录
        List<DispenseInventoryDto> dispenseInventoryList =
            chineseMedicineDispenseMapper.selectDispenseInventoryInfoByPrescriptionNo(prescriptionNo,
                CommonConstants.TableName.MED_MEDICATION_DEFINITION);
        if (!dispenseInventoryList.isEmpty()) {
            MedicationDispense medicationDispense;
            for (DispenseInventoryDto dispenseInventoryDto : dispenseInventoryList) {

                // 药品发放表项目设定
                medicationDispense = new MedicationDispense();
                // id
                medicationDispense.setId(dispenseInventoryDto.getDispenseId());
                // 药品发放状态
                medicationDispense.setStatusEnum(DispenseStatus.DECLINED.getValue());
                // 未发药原因
                medicationDispense.setNotPerformedReasonEnum(notPerformedReasonEnum);
                // 状态变更时间
                medicationDispense.setStatusChangedTime(DateUtils.getNowDate());
                // 发药人
                medicationDispense.setPractitionerId(SecurityUtils.getLoginUser().getPractitionerId());
                medicationDispenseList.add(medicationDispense);
            }
            // 药品发放更新
            iMedicationDispenseUpdFlg = iMedicationDispenseService.updateBatchById(medicationDispenseList);
        }
        return iMedicationDispenseUpdFlg ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null))
            : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00010, null));
    }
}
