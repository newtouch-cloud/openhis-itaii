package com.openhis.web.pharmacymanage.appservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.*;
import com.openhis.administration.domain.Organization;
import com.openhis.administration.service.IOrganizationService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.medication.domain.MedicationDispense;
import com.openhis.medication.service.IMedicationDispenseService;
import com.openhis.web.pharmacymanage.appservice.IWesternMedicineDispenseAppService;
import com.openhis.web.pharmacymanage.dto.*;
import com.openhis.web.pharmacymanage.mapper.WesternMedicineDispenseMapper;
import com.openhis.workflow.domain.InventoryItem;
import com.openhis.workflow.service.IInventoryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 西药发放 应用实现类
 *
 * @author wangyang
 * @date 2025/3/14
 */
@Service
public class IWesternMedicineDispenseAppServiceImpl implements IWesternMedicineDispenseAppService {

    @Autowired
    private IOrganizationService iOrganizationService;

    @Autowired
    IMedicationDispenseService iMedicationDispenseService;

    @Autowired
    IInventoryItemService iInventoryItemService;

    @Autowired
    WesternMedicineDispenseMapper westernMedicineDispenseMapper;

    /**
     * 获取页面初始化信息
     *
     * @return 初始化信息
     */
    @Override
    public R<?> init() {

        PageInitDto initDto = new PageInitDto();

        // 获取科室下拉选列表
        List<Organization> organizationList = iOrganizationService.getList(OrganizationClass.CLINIC.getValue());
        List<PageInitDto.DepartmentOption> organizationOptions = organizationList.stream()
                .map(organization -> new PageInitDto.DepartmentOption(organization.getId(),
                        organization.getName())).collect(Collectors.toList());

        // 未发药原因下拉选列表
        List<PageInitDto.NotPerformedReasonOption> notPerformedReasonOptions =
                Stream.of(NotPerformedReasonEnum.values()).map(notPerformedReason ->
                        new PageInitDto.NotPerformedReasonOption(notPerformedReason.getValue(),
                                notPerformedReason.getInfo())).collect(Collectors.toList());

        initDto.setDepartmentOptions(organizationOptions).setNotPerformedReasonOptions(notPerformedReasonOptions);
        return R.ok(initDto);
    }

    /**
     * 分页查询就诊病人列表
     *
     * @param encounterInfoSearchParam 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @param request 请求数据
     * @return 就诊病人列表
     */
    @Override
    public R<?> getEncounterInfoListPage(EncounterInfoSearchParam encounterInfoSearchParam,
                                         Integer pageNo,
                                         Integer pageSize,
                                         HttpServletRequest request) {

        // 查询条件设定
        String condition = encounterInfoSearchParam.getCondition();
        encounterInfoSearchParam.setCondition(null);
        if (StringUtils.isNotEmpty(condition)){
            Pattern pattern = Pattern.compile(".*\\d.*");
            Matcher matcher = pattern.matcher(condition);
            if (matcher.find()){
                encounterInfoSearchParam.setIdCard(condition);
            } else{
                encounterInfoSearchParam.setPatientName(condition);
            }
        }

        // 构建查询条件
        QueryWrapper<EncounterInfoSearchParam> queryWrapper =
                HisQueryUtils.buildQueryWrapper(encounterInfoSearchParam,null,null, request);

        // 查询就诊病人列表
        Page<EncounterInfoPageDto> encounterInfoPageDto = westernMedicineDispenseMapper.selectEncounterInfoListPage(
                new Page<>(pageNo, pageSize), queryWrapper);

        // 个别项目设定
        encounterInfoPageDto.getRecords().forEach(prescriptionPatientInfoDto -> {
            // 性别
            prescriptionPatientInfoDto.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class,
                prescriptionPatientInfoDto.getGenderEnum()));
        });
        return R.ok(encounterInfoPageDto);
    }

    /**
     * 查询处方单列表
     * @param encounterId 就诊号
     * @return 处方单列表
     */
    @Override
    public R<?> getPrescriptionInfo(Long encounterId) {

        // 患者基本信息查询
        PrescriptionPatientInfoDto prescriptionPatientInfoDto = westernMedicineDispenseMapper.
                selectPrescriptionPatientInfo(encounterId);
        // 年龄
        prescriptionPatientInfoDto.setAge(AgeCalculatorUtil.getAge(prescriptionPatientInfoDto.getBirthDate()));
        // 性别
        prescriptionPatientInfoDto.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class,
                prescriptionPatientInfoDto.getGenderEnum()));
        // 合同类型
        if (StringUtils.isNotNull(prescriptionPatientInfoDto.getCategoryEnum())){
            prescriptionPatientInfoDto.setCategoryEnum_enumText(EnumUtils.getInfoByValue(FinCategory.class,
                    prescriptionPatientInfoDto.getCategoryEnum()));
        }
        // 处方单信息查询
        List<PrescriptionMedicineInfoDto> prescriptionMedicineInfoList = westernMedicineDispenseMapper.
                selectPrescriptionMedicineInfoList(encounterId);
        // 计算合计金额
        if(!prescriptionMedicineInfoList.isEmpty()) {
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
     * 处方单核对发药
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
        List<DispenseInventoryDto> dispenseInventoryList = westernMedicineDispenseMapper.
                selectDispenseInventoryInfoByPrescriptionNo(prescriptionNo);
        if(!dispenseInventoryList.isEmpty()){
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
                // 配药时间
                medicationDispense.setPrepareTime(DateUtils.getNowDate());
                // 发药时间
                medicationDispense.setDispenseTime(DateUtils.getNowDate());
                medicationDispenseList.add(medicationDispense);

                // 库存表项目设定
                inventoryItem = new InventoryItem();
                // 库存数量
                if (dispenseInventoryDto.getDispenseUnitCode().equals(dispenseInventoryDto.
                        getInventoryBaseUnitCode())) {
                    // id
                    inventoryItem.setId(dispenseInventoryDto.getInventoryId());
                    // 包装数量
                    inventoryItem.setBaseQuantity(dispenseInventoryDto.getInventoryBaseQuantity().
                            min(new BigDecimal(dispenseInventoryDto.getDispenseQuantity())));
                    // 拆零数量（拆零比×包装数量）
                    inventoryItem.setMinQuantity(dispenseInventoryDto.getInventoryMinQuantity().
                            min(dispenseInventoryDto.getPartPercent().multiply(
                                    new BigDecimal(dispenseInventoryDto.getDispenseQuantity()))));
                } else if (dispenseInventoryDto.getDispenseUnitCode().equals(dispenseInventoryDto.
                        getInventoryMinUnitCode())) {
                    // id
                    inventoryItem.setId(dispenseInventoryDto.getInventoryId());
                    // 拆零数量
                    inventoryItem.setMinQuantity(dispenseInventoryDto.getInventoryMinQuantity().min(
                            new BigDecimal(dispenseInventoryDto.getDispenseQuantity())));
                    // 包装数量（拆零数量÷拆零比）
                    inventoryItem.setBaseQuantity(dispenseInventoryDto.getInventoryBaseQuantity().min(
                            new BigDecimal(dispenseInventoryDto.getDispenseQuantity()).
                                    divide(dispenseInventoryDto.getPartPercent(),RoundingMode.HALF_UP)));
                }
                inventoryItemList.add(inventoryItem);
            }
            // 药品发放更新
            iMedicationDispenseUpdFlg = iMedicationDispenseService.updateBatchById(medicationDispenseList);

            // 库存更新
            iInventoryItemUpdFlg = iInventoryItemService.updateBatchById(inventoryItemList);
        }
        return iMedicationDispenseUpdFlg && iInventoryItemUpdFlg ?
                R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null))
                : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00010, null));
    }

    /**
     * 作废
     * @param prescriptionNo 处方号
     * @param notPerformedReasonEnum 未发药原因
     * @return 处理结果
     */
    @Override
    public R<?> medicineCancel(String prescriptionNo,
                               Integer notPerformedReasonEnum) {

        List<MedicationDispense> medicationDispenseList = new ArrayList<>();
        boolean iMedicationDispenseUpdFlg = true;

        // 获取药品待发放记录
        List<DispenseInventoryDto> dispenseInventoryList = westernMedicineDispenseMapper.
                selectDispenseInventoryInfoByPrescriptionNo(prescriptionNo);
        if(!dispenseInventoryList.isEmpty()){
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
        return iMedicationDispenseUpdFlg?
                R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00004, null))
                : R.fail(MessageUtils.createMessage(PromptMsgConstant.Common.M00010, null));
    }
}
