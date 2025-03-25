/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.datadictionary.appservice.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.core.common.core.domain.entity.SysDictData;
import com.core.common.utils.ChineseConvertUtils;
import com.core.system.service.ISysDictTypeService;
import com.openhis.administration.domain.Supplier;
import com.openhis.administration.service.ISupplierService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.enums.DeviceCategory;
import com.openhis.common.enums.Whether;
import com.openhis.web.datadictionary.appservice.IItemDefinitionService;
import com.openhis.web.datadictionary.dto.DeviceManageInitDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.SecurityUtils;
import com.core.common.utils.bean.BeanUtils;
import com.core.common.utils.poi.ExcelUtil;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.ApplicableScope;
import com.openhis.common.enums.PublicationStatus;
import com.openhis.common.utils.EnumUtils;
import com.openhis.medication.domain.Medication;
import com.openhis.medication.domain.MedicationDefinition;
import com.openhis.medication.domain.MedicationDetail;
import com.openhis.medication.service.IMedicationDefinitionService;
import com.openhis.medication.service.IMedicationService;
import com.openhis.web.datadictionary.appservice.IMedicationManageAppService;
import com.openhis.web.datadictionary.dto.MedicationManageDto;
import com.openhis.web.datadictionary.dto.MedicationManageInitDto;
import com.openhis.web.datadictionary.dto.MedicationManageUpDto;
import com.openhis.web.datadictionary.mapper.MedicationManageSearchMapper;

/**
 * 药品目录 impl
 *
 * @author
 * @date 2025-03-17
 */
@Service
public class MedicationManageAppServiceImpl implements IMedicationManageAppService {

    @Autowired
    private IMedicationService medicationService;

    @Autowired
    private IMedicationDefinitionService medicationDefinitionService;

    @Autowired
    private MedicationManageSearchMapper medicationManageSearchMapper;
    @Autowired
    private ISupplierService supplierService;

    @Autowired
    private ISysDictTypeService sysDictTypeService;

    @Autowired
    private IItemDefinitionService itemDefinitionServic;

    /**
     * 药品目录初始化
     *
     * @return
     */
    @Override
    public R<?> getMedicationInit() {

        MedicationManageInitDto medicationManageInitDto = new MedicationManageInitDto();
        // 获取状态
        List<MedicationManageInitDto.statusEnumOption> statusEnumOptions = Stream.of(PublicationStatus.values())
            .map(status -> new MedicationManageInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        // 获取适用范围
        List<MedicationManageInitDto.domainEnumOption> domainEnumOptions = Stream.of(ApplicableScope.values())
            .map(domain -> new MedicationManageInitDto.domainEnumOption(domain.getValue(), domain.getInfo()))
            .collect(Collectors.toList());
        // 查询供应商列表
        List<Supplier> supplierList = supplierService.getList();
        // 供应商信息
        List<MedicationManageInitDto.supplierListOption> supplierListOptions = supplierList.stream()
            .map(supplier -> new MedicationManageInitDto.supplierListOption(supplier.getId(), supplier.getName()))
            .collect(Collectors.toList());

        // 获取药品分类
        List<SysDictData> medicalList =
            sysDictTypeService.selectDictDataByType(CommonConstants.DictName.MED_CATEGORY_CODE);
        // 获取药品分类
        List<MedicationManageInitDto.dictCategoryCode> medicationCategories = Stream.of(DeviceCategory.values())
            .map(category -> new MedicationManageInitDto.dictCategoryCode(category.getValue(), category.getInfo()))
            .collect(Collectors.toList());

        //获取是/否 列表
        // 获取状态
        List<MedicationManageInitDto.statusEnumOption> statusWeatherOption = Stream.of(Whether.values())
            .map(status -> new MedicationManageInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());

        medicationManageInitDto.setStatusFlagOptions(statusEnumOptions);
        medicationManageInitDto.setDomainFlagOptions(domainEnumOptions);
        medicationManageInitDto.setSupplierListOptions(supplierListOptions);
        medicationManageInitDto.setMedicationCategoryCodeOptions(medicationCategories);
        medicationManageInitDto.setStatusWeatherOptions(statusWeatherOption);
        
        return R.ok(medicationManageInitDto);
    }

    /**
     * 药品目录查询
     *
     * @param
     * @param searchKey 查询条件-模糊查询
     * @param pageNo 查询条件
     * @param pageSize 查询条件
     * @return 药品目录查询结果
     */
    @Override
    public R<?> getMedicationList(@RequestParam(value = "searchKey", defaultValue = "") String searchKey,
        @RequestParam(value = "ybMatchFlag", defaultValue = "-1") Integer ybMatchFlag,
        @RequestParam(value = "statusEnum", defaultValue = "-1") Integer statusEnum,
        @RequestParam(value = "categoryCode", defaultValue = "") String categoryCode,
        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {

        // 分页设置
        Integer offset = (pageNo - 1) * pageSize;
        // 获取租户ID
        Integer tenantId = SecurityUtils.getLoginUser().getTenantId();
        // 查询药品目录列表
        List<MedicationManageDto> medicationDetailList = medicationManageSearchMapper.getPage(searchKey, ybMatchFlag,
            statusEnum, categoryCode, tenantId, pageSize, offset);
        // 查询总记录数
        long total =
            medicationManageSearchMapper.getPageCount(searchKey, ybMatchFlag, statusEnum, categoryCode, tenantId);
        // 创建Page对象并设置属性
        Page<MedicationManageDto> medicationManageDtoPage = new Page<>(pageNo, pageSize, total);
        medicationManageDtoPage.setRecords(medicationDetailList);

        // 枚举类回显赋值
        medicationManageDtoPage.getRecords().forEach(e -> {
            // 药品状态
            e.setStatusEnum_enumText(EnumUtils.getInfoByValue(PublicationStatus.class, e.getStatusEnum()));
            // 活动标记
            // e.setActiveFlag_enumText(EnumUtils.getInfoByValue(AccountStatus.class, e.getActiveFlag()));
            // 适用范围
            // e.setDomainEnum_enumText(EnumUtils.getInfoByValue(ApplicableScope.class, e.getDomainEnum()));
        });

        // 返回【药品录列表DTO】分页
        return R.ok(medicationManageDtoPage);
    }

    /**
     * 编辑药品目录信息
     *
     * @param medicationManageUpDto 药品目录信息
     */
    @Override
    public R<?> editMedication(@Validated @RequestBody MedicationManageUpDto medicationManageUpDto) {

        MedicationDefinition medicationDefinition = new MedicationDefinition();
        Medication medication = new Medication();
        BeanUtils.copyProperties(medicationManageUpDto, medication); // 子表信息
        BeanUtils.copyProperties(medicationManageUpDto, medicationDefinition);// 主表信息
        // 拼音码
        medicationDefinition.setPyStr(ChineseConvertUtils.toPinyinFirstLetter(medicationDefinition.getName()));
        // 五笔码
        medicationDefinition.setWbStr(ChineseConvertUtils.toWBFirstLetter(medicationDefinition.getName()));

        // 更新子表药品信息
        if (medicationService.updateById(medication)) {
            // 更新主表药品信息
            return medicationDefinitionService.updateById(medicationDefinition)
                ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"药品目录"}))
                : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        } else {
            return R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
        }
    }

    /**
     * 药品目录详细查询
     *
     * @param id 查询条件
     * @return 药品目录查询结果
     */
    @Override
    public R<?> getMedicationOne(@PathVariable("id") Long id) {

        // 获取租户ID
        Integer tenantId = SecurityUtils.getLoginUser().getTenantId();
        // 查询药品目录列表
        MedicationManageDto medicationManageDto = medicationManageSearchMapper.getOne(id, tenantId);
        // 返回【药品录列表DTO】列表
        return R.ok(medicationManageDto);
    }

    /**
     * 药品目录停用
     *
     * @param ids 药品目录ID列表
     * @return
     */
    @Override
    public R<?> editMedicationStop(List<Long> ids) {

        List<Medication> medicationList = new ArrayList<>();
        // 取得更新值
        for (Long detail : ids) {
            Medication medication = new Medication();
            medication.setId(detail);
            medication.setStatusEnum(PublicationStatus.RETIRED);
            medicationList.add(medication);
        }
        // 更新药品信息
        return medicationService.updateBatchById(medicationList)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"药品目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 药品目录启用
     *
     * @param ids 药品目录ID列表
     * @return
     */
    @Override
    public R<?> editMedicationStart(List<Long> ids) {

        List<Medication> medicationList = new ArrayList<>();
        // 取得更新值
        for (Long detail : ids) {
            Medication medication = new Medication();
            medication.setId(detail);
            medication.setStatusEnum(PublicationStatus.ACTIVE);
            medicationList.add(medication);
        }
        // 更新药品信息
        return medicationService.updateBatchById(medicationList)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"药品目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 添加药品目录信息
     *
     * @param medicationManageUpDto 药品目录信息
     */
    @Override
    public R<?> addMedication(@Validated @RequestBody MedicationManageUpDto medicationManageUpDto) {

        MedicationDetail medicationDetail = new MedicationDetail();
        BeanUtils.copyProperties(medicationManageUpDto, medicationDetail);
        // 拼音码
        medicationDetail.setPyStr(ChineseConvertUtils.toPinyinFirstLetter(medicationDetail.getName()));
        // 五笔码
        medicationDetail.setWbStr(ChineseConvertUtils.toWBFirstLetter(medicationDetail.getName()));

        // 新增主表外来药品目录
        if (medicationDefinitionService.addMedication(medicationDetail)) {

            // 新增子表外来药品目录
            boolean insertMedicationSuccess = medicationService.addMedication(medicationDetail);
            // 添加药品成功后，添加相应的条件价格表信息
            boolean insertItemDefinitionSuccess = itemDefinitionServic.addItem(medicationManageUpDto,medicationDetail);

            return (insertMedicationSuccess || insertItemDefinitionSuccess)
                ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"药品目录"}))
                : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00008, null));
        } else {
            return R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00008, null));
        }
    }

    @Override
    public R<?> exportMedication(String searchKey, Integer ybMatchFlag, Integer statusEnum, String categoryCode,
        HttpServletResponse response) {
        // 获取租户ID
        Integer tenantId = SecurityUtils.getLoginUser().getTenantId();
        List<MedicationManageDto> list =
            medicationManageSearchMapper.getList(searchKey, ybMatchFlag, statusEnum, categoryCode, tenantId);
        ExcelUtil<MedicationManageDto> util = new ExcelUtil<>(MedicationManageDto.class);
        util.exportExcel(response, list, "药品目录");
        return null;
    }
}
