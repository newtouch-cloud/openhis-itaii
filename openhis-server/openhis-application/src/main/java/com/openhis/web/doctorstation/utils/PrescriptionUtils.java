package com.openhis.web.doctorstation.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.core.common.utils.AssignSeqUtil;
import com.openhis.common.enums.AssignSeqEnum;
import com.openhis.web.doctorstation.dto.AdviceSaveDto;

/**
 * 处方号工具类
 */
@Component
public class PrescriptionUtils {

    @Resource
    private AssignSeqUtil assignSeqUtil;

    private static final Integer MAX_SINGLE_PRESCRIPTION_NUM = 5; // 单个处方药品种类上限
    private static final BigDecimal MAX_SINGLE_PRESCRIPTION_PRICE = new BigDecimal("500"); // 单个处方金额上限

    /**
     * 主入口方法：为药品请求生成处方号
     */
    public void generatePrescriptionNumbers(List<AdviceSaveDto> medicineList) {
        if (medicineList == null || medicineList.isEmpty()) {
            return;
        }
        // 1. 按诊断ID分组（不同诊断必须分开）
        Map<Long, List<AdviceSaveDto>> diagnosisGroups =
            medicineList.stream().collect(Collectors.groupingBy(AdviceSaveDto::getConditionDefinitionId));
        // 2. 处理每个诊断组
        diagnosisGroups.values().forEach(this::processDiagnosisGroup);
    }

    /**
     * 处理单个诊断组
     */
    private void processDiagnosisGroup(List<AdviceSaveDto> diagnosisGroup) {
        if (diagnosisGroup.isEmpty()) {
            return;
        }
        // 1. 按药品性质分组
        Map<String, List<AdviceSaveDto>> pharmacologyGroups =
            diagnosisGroup.stream().collect(Collectors.groupingBy(AdviceSaveDto::getPharmacologyCategoryCode));
        // 2. 处理每个药品性质组
        pharmacologyGroups.values().forEach(pharmaGroup -> {
            // 2.1 先处理有分组ID的药品（确保它们不会被拆分）
            Map<Long, List<AdviceSaveDto>> groupedMedicines = pharmaGroup.stream().filter(m -> m.getGroupId() != null)
                .collect(Collectors.groupingBy(AdviceSaveDto::getGroupId));
            // 2.2 无分组ID的药品单独处理
            List<AdviceSaveDto> ungroupedMedicines =
                pharmaGroup.stream().filter(m -> m.getGroupId() == null).collect(Collectors.toList());
            // 2.3 合并处理 - 每个分组ID的药品作为一个整体，与无分组ID的药品一起按限制拆分
            List<List<AdviceSaveDto>> allGroups = new ArrayList<>();
            // 添加有分组ID的药品（每个分组作为一个整体）
            groupedMedicines.values().forEach(allGroups::add);
            // 添加无分组ID的药品（按种类限制拆分）
            allGroups.addAll(splitByQuantityLimit(ungroupedMedicines));
            // 2.4 对合并后的所有组按金额限制进行最终拆分
            List<List<AdviceSaveDto>> finalGroups = new ArrayList<>();
            allGroups.forEach(group -> {
                finalGroups.addAll(splitByPriceLimit(group));
            });
            // 2.5 分配处方号
            assignPrescriptionNumbers(finalGroups, pharmaGroup.get(0).getPharmacologyCategoryCode());
        });
    }

    /**
     * 按药品种类上限拆分（仅用于无分组ID药品）
     */
    private List<List<AdviceSaveDto>> splitByQuantityLimit(List<AdviceSaveDto> medicines) {
        List<List<AdviceSaveDto>> result = new ArrayList<>();
        List<AdviceSaveDto> currentGroup = new ArrayList<>(MAX_SINGLE_PRESCRIPTION_NUM);
        for (AdviceSaveDto medicine : medicines) {
            if (currentGroup.size() >= MAX_SINGLE_PRESCRIPTION_NUM) {
                result.add(currentGroup);
                currentGroup = new ArrayList<>(MAX_SINGLE_PRESCRIPTION_NUM);
            }
            currentGroup.add(medicine);
        }
        if (!currentGroup.isEmpty()) {
            result.add(currentGroup);
        }
        return result;
    }

    /**
     * 按处方金额上限拆分（通用方法，处理所有药品）
     */
    private List<List<AdviceSaveDto>> splitByPriceLimit(List<AdviceSaveDto> medicines) {
        List<List<AdviceSaveDto>> result = new ArrayList<>();
        List<AdviceSaveDto> currentGroup = new ArrayList<>();
        BigDecimal currentTotal = BigDecimal.ZERO;
        for (AdviceSaveDto medicine : medicines) {
            // 计算单个药品总金额
            BigDecimal medicinePrice = medicine.getUnitPrice().multiply(new BigDecimal(medicine.getQuantity()));
            // 特殊处理：单药品金额超限
            if (medicinePrice.compareTo(MAX_SINGLE_PRESCRIPTION_PRICE) > 0) {
                // 先保存当前组（如果有药品）
                if (!currentGroup.isEmpty()) {
                    result.add(currentGroup);
                    currentGroup = new ArrayList<>();
                    currentTotal = BigDecimal.ZERO;
                }
                // 该药品单独成方
                result.add(Collections.singletonList(medicine));
                continue;
            }
            // 检查添加后是否超限
            if (currentTotal.add(medicinePrice).compareTo(MAX_SINGLE_PRESCRIPTION_PRICE) > 0) {
                result.add(currentGroup);
                currentGroup = new ArrayList<>();
                currentTotal = BigDecimal.ZERO;
            }
            currentGroup.add(medicine);
            currentTotal = currentTotal.add(medicinePrice);
        }
        if (!currentGroup.isEmpty()) {
            result.add(currentGroup);
        }
        return result;
    }

    /**
     * 为最终分组分配处方号
     */
    private void assignPrescriptionNumbers(List<List<AdviceSaveDto>> groups, String pharmacologyCode) {
        groups.forEach(group -> {
            String prescriptionNo = generatePrescriptionNo(pharmacologyCode);
            group.forEach(medicine -> medicine.setPrescriptionNo(prescriptionNo));
        });
    }

    /**
     * 根据药品性质生成处方号
     */
    private String generatePrescriptionNo(String pharmacologyCategoryCode) {
        switch (pharmacologyCategoryCode) {
            case "2": // 麻醉药品
                return assignSeqUtil.getSeq(AssignSeqEnum.PRESCRIPTION_NARCOTIC_NO.getPrefix(), 8);
            case "3": // 毒性药品
                return assignSeqUtil.getSeq(AssignSeqEnum.PRESCRIPTION_TOXIC_NO.getPrefix(), 8);
            case "4": // 一类精神药
                return assignSeqUtil.getSeq(AssignSeqEnum.PRESCRIPTION_A_PSYCHOTROPIC_NO.getPrefix(), 8);
            case "5": // 二类精神药
                return assignSeqUtil.getSeq(AssignSeqEnum.PRESCRIPTION_B_PSYCHOTROPIC_NO.getPrefix(), 8);
            default: // 普通药品
                return assignSeqUtil.getSeq(AssignSeqEnum.PRESCRIPTION_COMMON_NO.getPrefix(), 8);
        }
    }

}
