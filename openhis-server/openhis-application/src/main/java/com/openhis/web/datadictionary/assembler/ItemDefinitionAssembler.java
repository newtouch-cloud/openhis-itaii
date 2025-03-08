/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.datadictionary.assembler;

/**
 * 费用定价dto转换器
 *
 * @author zxy
 * @date 2025-02-21
 */
public class ItemDefinitionAssembler {
//    /**
//     * 装配【费用定价分页列表DTO】分页 药品
//     *
//     * @param chargeItemDefinitionPage 【费用定价管理Entity实体】分页
//     * @param chargeItemDefAppList 【费用定价管理子表Entity实体】列表
//     * @param medicationDefinitionList【药品定义管理Entity实体】列表
//     * @return 【费用定价分页列表DTO】分页
//     */
//    public static Page<ChargeItemDefPageDto> assembleMedDefinitionDto(
//        Page<ChargeItemDefinition> chargeItemDefinitionPage, List<ChargeItemDefDetail> chargeItemDefAppList,
//        List<MedicationDefinition> medicationDefinitionList, ItemDefSearchParam itemDefSearchParam) {
//
//        // 将查询到的【药品基本信息管理】列表，作成以ID为Key的Map
//        Map<Long, MedicationDefinition> medicationDefinitionMap = medicationDefinitionList.stream()
//            .collect(Collectors.toMap(MedicationDefinition::getId, Function.identity()));
//
//        // 将查询到的【患者管理】列表，作成以ID为Key的Map
//        Map<Long, ChargeItemDefDetail> chargeItemDefAppMap =
//            chargeItemDefAppList.stream().collect(Collectors.toMap(ChargeItemDefDetail::getId, Function.identity()));
//
//        // 定义【入库单据分页列表DTO】的分页，传入【页码】、【行数】、及上面分页的【总数】
//        Page<ChargeItemDefPageDto> returnPage = new Page<>(chargeItemDefinitionPage.getCurrent(),
//            chargeItemDefinitionPage.getSize(), chargeItemDefinitionPage.getTotal());
//
//        // 将【供应申请管理】的分页转化为返回【入库单据分页列表DTO】的分页
//        returnPage.setRecords(chargeItemDefinitionPage.getRecords().stream().map(entity -> {
//            // 定义【入库单据分页列表DTO】
//            ChargeItemDefPageDto dto = new ChargeItemDefPageDto();
//            ChargeItemDefDetail chargeItemDefApp =
//                chargeItemDefAppMap.getOrDefault(entity.getInstanceId(), new ChargeItemDefDetail());
//            MedicationDefinition medicationDefinition =
//                medicationDefinitionMap.getOrDefault(entity.getInstanceId(), new MedicationDefinition());
//            // 从主表COPY需要的字段
//            dto.setId(entity.getId());
//            dto.setConditionLotnumber(chargeItemDefApp.getConditionLotnumber());
//            dto.setPyCode(medicationDefinition.getPyCode());
//            dto.setTypeEnum(medicationDefinition.getCategoryCode());
//            return dto;
//        }).collect(Collectors.toList()));
//        // 模糊查询项目名称/项目编码/助记码
//        if (itemDefSearchParam.getSearchKey() != null) {
//            returnPage.setRecords(returnPage.getRecords().stream()
//                .filter(e -> e.getChargeName().contains(itemDefSearchParam.getSearchKey())
//                    || e.getTitle().contains(itemDefSearchParam.getSearchKey())
//                    || e.getPyCode().contains(itemDefSearchParam.getSearchKey()))
//                .collect(Collectors.toList()));
//        }
//        // 精确查询收费项目
//        if (itemDefSearchParam.getChargeItem() != null) {
//            returnPage.setRecords(returnPage.getRecords().stream()
//                .filter(e -> e.getTypeEnum().equals(itemDefSearchParam.getChargeItem())).collect(Collectors.toList()));
//        }
//
//        // 返回【入库单据分页列表DTO】分页
//        return returnPage;
//    }
//
//    /**
//     * 装配【费用定价分页列表DTO】分页 器具
//     *
//     * @param chargeItemDefinitionPage 【费用定价管理Entity实体】分页
//     * @param chargeItemDefAppList 【费用定价管理子表Entity实体】列表
//     * @param medicationDefinitionList【药品定义管理Entity实体】列表
//     * @return 【费用定价分页列表DTO】分页
//     */
//    public static Page<ChargeItemDefPageDto> assembleDevDefinitionDto(
//        Page<ChargeItemDefinition> chargeItemDefinitionPage, List<ChargeItemDefDetail> chargeItemDefAppList,
//        List<DeviceDefinition> medicationDefinitionList, ItemDefSearchParam itemDefSearchParam) {
//
//        // 将查询到的【药品基本信息管理】列表，作成以ID为Key的Map
//        Map<Long, DeviceDefinition> deviceDefinitionMap =
//            medicationDefinitionList.stream().collect(Collectors.toMap(DeviceDefinition::getId, Function.identity()));
//
//        // 将查询到的【患者管理】列表，作成以ID为Key的Map
//        Map<Long, ChargeItemDefDetail> chargeItemDefAppMap =
//            chargeItemDefAppList.stream().collect(Collectors.toMap(ChargeItemDefDetail::getId, Function.identity()));
//
//        // 定义【入库单据分页列表DTO】的分页，传入【页码】、【行数】、及上面分页的【总数】
//        Page<ChargeItemDefPageDto> returnPage = new Page<>(chargeItemDefinitionPage.getCurrent(),
//            chargeItemDefinitionPage.getSize(), chargeItemDefinitionPage.getTotal());
//
//        // 将【供应申请管理】的分页转化为返回【入库单据分页列表DTO】的分页
//        returnPage.setRecords(chargeItemDefinitionPage.getRecords().stream().map(entity -> {
//            // 定义【入库单据分页列表DTO】
//            ChargeItemDefPageDto dto = new ChargeItemDefPageDto();
//            ChargeItemDefDetail chargeItemDefApp =
//                chargeItemDefAppMap.getOrDefault(entity.getInstanceId(), new ChargeItemDefDetail());
//            DeviceDefinition deviceDefinition =
//                deviceDefinitionMap.getOrDefault(entity.getInstanceId(), new DeviceDefinition());
//            // 从主表COPY需要的字段
//            dto.setId(entity.getId());
//            dto.setConditionLotnumber(chargeItemDefApp.getConditionLotnumber());
//            dto.setPyCode(deviceDefinition.getPyCode());
//            dto.setTypeEnum(deviceDefinition.getDeviceClass());
//            return dto;
//        }).collect(Collectors.toList()));
//        // 模糊查询项目名称/项目编码/助记码
//        if (itemDefSearchParam.getSearchKey() != null) {
//            returnPage.setRecords(returnPage.getRecords().stream()
//                .filter(e -> e.getChargeName().contains(itemDefSearchParam.getSearchKey())
//                    || e.getTitle().contains(itemDefSearchParam.getSearchKey())
//                    || e.getPyCode().contains(itemDefSearchParam.getSearchKey()))
//                .collect(Collectors.toList()));
//        }
//        // 精确查询收费项目
//        if (itemDefSearchParam.getChargeItem() != null) {
//            returnPage.setRecords(returnPage.getRecords().stream()
//                .filter(e -> e.getTypeEnum().equals(itemDefSearchParam.getChargeItem())).collect(Collectors.toList()));
//        }
//
//        // 返回【入库单据分页列表DTO】分页
//        return returnPage;
//    }
//
//    /**
//     * 装配【费用定价分页列表DTO】分页 诊疗
//     *
//     * @param chargeItemDefinitionPage 【费用定价管理Entity实体】分页
//     * @param chargeItemDefAppList 【费用定价管理子表Entity实体】列表
//     * @param medicationDefinitionList【药品定义管理Entity实体】列表
//     * @return 【费用定价分页列表DTO】分页
//     */
//    public static Page<ChargeItemDefPageDto> assembleProDefinitionDto(
//        Page<ChargeItemDefinition> chargeItemDefinitionPage, List<ChargeItemDefDetail> chargeItemDefAppList,
//        List<ActivityDefinition> medicationDefinitionList, ItemDefSearchParam itemDefSearchParam) {
//
//        // 将查询到的【药品基本信息管理】列表，作成以ID为Key的Map
//        Map<Long, ActivityDefinition> activityDefinitionMap =
//            medicationDefinitionList.stream().collect(Collectors.toMap(ActivityDefinition::getId, Function.identity()));
//
//        // 将查询到的【患者管理】列表，作成以ID为Key的Map
//        Map<Long, ChargeItemDefDetail> chargeItemDefAppMap =
//            chargeItemDefAppList.stream().collect(Collectors.toMap(ChargeItemDefDetail::getId, Function.identity()));
//
//        // 定义【入库单据分页列表DTO】的分页，传入【页码】、【行数】、及上面分页的【总数】
//        Page<ChargeItemDefPageDto> returnPage = new Page<>(chargeItemDefinitionPage.getCurrent(),
//            chargeItemDefinitionPage.getSize(), chargeItemDefinitionPage.getTotal());
//
//        // 将【供应申请管理】的分页转化为返回【入库单据分页列表DTO】的分页
//        returnPage.setRecords(chargeItemDefinitionPage.getRecords().stream().map(entity -> {
//            // 定义【入库单据分页列表DTO】
//            ChargeItemDefPageDto dto = new ChargeItemDefPageDto();
//            ChargeItemDefDetail chargeItemDefApp =
//                chargeItemDefAppMap.getOrDefault(entity.getInstanceId(), new ChargeItemDefDetail());
//            ActivityDefinition deviceDefinition =
//                activityDefinitionMap.getOrDefault(entity.getInstanceId(), new ActivityDefinition());
//            // 从主表COPY需要的字段
//            dto.setId(entity.getId());
//            dto.setConditionLotnumber(chargeItemDefApp.getConditionLotnumber());
//            dto.setPyCode(deviceDefinition.getPyCode());
//            dto.setTypeEnum(deviceDefinition.getTypeEnum().toString());
//            return dto;
//        }).collect(Collectors.toList()));
//        // 模糊查询项目名称/项目编码/助记码
//        if (itemDefSearchParam.getSearchKey() != null) {
//            returnPage.setRecords(returnPage.getRecords().stream()
//                .filter(e -> e.getChargeName().contains(itemDefSearchParam.getSearchKey())
//                    || e.getTitle().contains(itemDefSearchParam.getSearchKey())
//                    || e.getPyCode().contains(itemDefSearchParam.getSearchKey()))
//                .collect(Collectors.toList()));
//        }
//        // 精确查询收费项目
//        if (itemDefSearchParam.getChargeItem() != null) {
//            returnPage.setRecords(returnPage.getRecords().stream()
//                .filter(e -> e.getTypeEnum().equals(itemDefSearchParam.getChargeItem())).collect(Collectors.toList()));
//        }
//
//        // 返回【入库单据分页列表DTO】分页
//        return returnPage;
//    }
}
