package com.openhis.web.datadictionary.appservice.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.openhis.administration.domain.Supplier;
import com.openhis.administration.service.ISupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.core.domain.entity.SysDictData;
import com.core.common.utils.*;
import com.core.common.utils.bean.BeanUtils;
import com.core.system.service.ISysDictTypeService;
import com.openhis.administration.domain.ChargeItemDefinition;
import com.openhis.administration.domain.Organization;
import com.openhis.administration.service.IOrganizationService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.*;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.datadictionary.appservice.IDiagnosisTreatmentManageAppService;
import com.openhis.web.datadictionary.appservice.IItemDefinitionService;
import com.openhis.web.datadictionary.dto.*;
import com.openhis.web.datadictionary.mapper.ActivityDefinitionManageMapper;
import com.openhis.workflow.domain.ActivityDefinition;
import com.openhis.workflow.mapper.ActivityDefinitionMapper;
import com.openhis.workflow.service.IActivityDefinitionService;

/**
 * 诊疗实现类
 *
 * @author Wuser
 * @date 2025/3/28
 */
@Service
public class DiagnosisTreatmentManageAppServiceImpl implements IDiagnosisTreatmentManageAppService {

    @Autowired
    private IActivityDefinitionService iActivityDefinitionService;
    @Autowired
    private ActivityDefinitionMapper activityDefinitionMapper;
    @Autowired
    private IOrganizationService iOrganizationService;
    @Autowired
    private ISysDictTypeService iSysDictTypeService;
    @Resource
    private ActivityDefinitionManageMapper activityDefinitionManageMapper;
    @Autowired
    private IItemDefinitionService itemDefinitionServic;
    @Autowired
    private ISysDictTypeService sysDictTypeService;

    @Autowired(required = false)
    AssignSeqUtil assignSeqUtil;

    /**
     * 诊疗目录初期查询
     *
     * @return
     */
    @Override
    public R<?> getDiseaseTreatmentInit() {
        DiagnosisTreatmentInitDto diagnosisTreatmentInitDto = new DiagnosisTreatmentInitDto();
        // 获取状态
        List<DiagnosisTreatmentInitDto.statusEnumOption> statusEnumOptions = Stream.of(PublicationStatus.values())
            .map(status -> new DiagnosisTreatmentInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        diagnosisTreatmentInitDto.setStatusFlagOptions(statusEnumOptions);
        // 获取执行科室
        LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Organization::getTypeEnum, OrganizationType.HOSPITAL_DEPARTMENT);
        List<Organization> organizations = iOrganizationService.list(queryWrapper);
        List<DiagnosisTreatmentInitDto.exeOrganization> exeOrganizations = organizations.stream()
            .map(exeOrg -> new DiagnosisTreatmentInitDto.exeOrganization(exeOrg.getId(), exeOrg.getName()))
            .collect(Collectors.toList());
        diagnosisTreatmentInitDto.setExeOrganizations(exeOrganizations);

        // 获取诊目录疗分类
        List<SysDictData> diagnosisList =
            sysDictTypeService.selectDictDataByType(CommonConstants.DictName.DIAGNOSIS_CATEGORY_CODE);
        // 获取诊疗录疗分类
        List<DiagnosisTreatmentInitDto.dictCategoryCode> diagnosisCategories = diagnosisList.stream().map(
                category -> new DiagnosisTreatmentInitDto.dictCategoryCode(category.getDictValue(), category.getDictLabel()))
            .collect(Collectors.toList());
        diagnosisTreatmentInitDto.setDiagnosisCategoryOptions(diagnosisCategories);

        // 查询医疗服务项类型
        List<SysDictData> medical_service_items =
            iSysDictTypeService.selectDictDataByType(ActivityDefCategory.MEDICAL_SERVICE_ITEM.getCode());
        // 获取医疗服务项List
        List<DiagnosisTreatmentInitDto.diseaseTreatmentType> diseaseTreatmentCategoryList = medical_service_items
            .stream().map(status -> new DiagnosisTreatmentInitDto.diseaseTreatmentType(status.getDictValue(),
                status.getDictLabel()))
            .collect(Collectors.toList());
        List<DiagnosisTreatmentInitDto.diseaseTreatmentCategory> diseaseTreatmentCategories = new ArrayList<>();
//
//        //获取目录分类
//        DiagnosisTreatmentInitDto.diseaseTreatmentCategory diseaseTreatmentCategory =
//            new DiagnosisTreatmentInitDto.diseaseTreatmentCategory(ActivityDefCategory.MEDICAL_SERVICE_ITEM.getValue(),
//                ActivityDefCategory.MEDICAL_SERVICE_ITEM.getInfo());
//        diseaseTreatmentCategory.setChildren(diseaseTreatmentCategoryList);
//        diseaseTreatmentCategories.add(diseaseTreatmentCategory);
//        diagnosisTreatmentInitDto.setDiseaseTreatmentCategoryList(diseaseTreatmentCategories);
//
//        // 查询手术与治疗类型
//        List<SysDictData> medical_service_items2 =
//            iSysDictTypeService.selectDictDataByType(ActivityDefCategory.TREATMENT_SURGERY.getCode());
//        // 获取手术与治疗List
//        List<DiagnosisTreatmentInitDto.diseaseTreatmentType> diseaseTreatmentCategoryList2 = medical_service_items2
//            .stream().map(status -> new DiagnosisTreatmentInitDto.diseaseTreatmentType(status.getDictValue(),
//                status.getDictLabel()))
//            .collect(Collectors.toList());
//
//        DiagnosisTreatmentInitDto.diseaseTreatmentCategory diseaseTreatmentCategory2 =
//            new DiagnosisTreatmentInitDto.diseaseTreatmentCategory(ActivityDefCategory.TREATMENT_SURGERY.getValue(),
//                ActivityDefCategory.TREATMENT_SURGERY.getInfo());
//        diseaseTreatmentCategory2.setChildren(diseaseTreatmentCategoryList2);
//        diseaseTreatmentCategories.add(diseaseTreatmentCategory2);
//
//        diagnosisTreatmentInitDto.setDiseaseTreatmentCategoryList(diseaseTreatmentCategories);

        // 获取类型
        List<DiagnosisTreatmentInitDto.statusEnumOption> typeEnumOptions = Stream.of(ActivityType.values())
            .map(status -> new DiagnosisTreatmentInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        diagnosisTreatmentInitDto.setTypeEnumOptions(typeEnumOptions);

        return R.ok(diagnosisTreatmentInitDto);
    }

    /**
     * 查询诊疗目录分页列表
     *
     * @param DiagnosisTreatmentSelParam 查询条件
     * @param searchKey 查询条件-模糊查询
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return
     */
    @Override
    public R<?> getDiseaseTreatmentPage(DiagnosisTreatmentSelParam DiagnosisTreatmentSelParam, String searchKey,
        Integer pageNo, Integer pageSize, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<DiagnosisTreatmentDto> queryWrapper = HisQueryUtils.buildQueryWrapper(DiagnosisTreatmentSelParam,
            searchKey, new HashSet<>(Arrays.asList("bus_no", "name", "py_str", "wb_str")), request);

        // 分页查询
        IPage<DiagnosisTreatmentDto> diseaseTreatmentPage =
            activityDefinitionManageMapper.getDiseaseTreatmentPage(new Page<>(pageNo, pageSize), queryWrapper);

        diseaseTreatmentPage.getRecords().forEach(e -> {
            // 医保标记枚举类回显赋值
            e.setYbFlag_enumText(EnumUtils.getInfoByValue(Whether.class, e.getYbFlag()));
            // 医保对码标记枚举类回显赋值
            e.setYbMatchFlag_enumText(EnumUtils.getInfoByValue(Whether.class, e.getYbMatchFlag()));
            // 类型举类回显赋值
            e.setTypeEnum_enumText(EnumUtils.getInfoByValue(ActivityType.class, e.getTypeEnum()));
            // 状态举类回显赋值
            e.setStatusEnum_enumText(EnumUtils.getInfoByValue(PublicationStatus.class, e.getStatusEnum()));
        });

        // 返回【诊疗目录列表DTO】分页
        return R.ok(diseaseTreatmentPage);
    }

    /**
     * 根据id查询诊疗详情
     *
     * @param id 诊疗ID
     * @return
     */
    @Override
    public R<?> getDiseaseTreatmentOne(Long id) {

        // 获取租户ID
        Integer tenantId = SecurityUtils.getLoginUser().getTenantId();
        // 根据ID查询【诊疗目录】
        return R.ok(activityDefinitionManageMapper.getDiseaseTreatmentOne(id, tenantId));

    }

    /**
     * 诊疗目录编辑
     *
     * @param diagnosisTreatmentUpDto 诊疗目录列表
     * @return
     */
    @Override
    public R<?> editDiseaseTreatment(DiagnosisTreatmentUpDto diagnosisTreatmentUpDto) {

        ActivityDefinition activityDefinition = new ActivityDefinition();
        BeanUtils.copyProperties(diagnosisTreatmentUpDto, activityDefinition);

        // 使用10位数基础采番
        String code = assignSeqUtil.getSeq(AssignSeqEnum.DEVICE_NUM.getPrefix(), 10);
        activityDefinition.setBusNo(code);
        // 拼音码
        activityDefinition.setPyStr(ChineseConvertUtils.toPinyinFirstLetter(activityDefinition.getName()));
        // 五笔码
        activityDefinition.setWbStr(ChineseConvertUtils.toWBFirstLetter(activityDefinition.getName()));

        // 更新诊疗信息
        if (iActivityDefinitionService.updateById(activityDefinition)) {
            ChargeItemDefinition chargeItemDefinition = new ChargeItemDefinition();
            chargeItemDefinition.setYbType(diagnosisTreatmentUpDto.getYbType())
                .setTypeCode(diagnosisTreatmentUpDto.getItemTypeCode())
                .setInstanceTable(CommonConstants.TableName.WOR_ACTIVITY_DEFINITION)
                .setInstanceId(diagnosisTreatmentUpDto.getId());

            // 更新价格表
            return itemDefinitionServic.updateItem(chargeItemDefinition)
                ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"诊疗目录"}))
                : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));

        }
        return R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 诊疗目录停用
     *
     * @param ids 诊疗目录ID列表
     * @return
     */
    @Override
    public R<?> editDiseaseTreatmentStop(List<Long> ids) {

        List<ActivityDefinition> ActivityDefinitionList = new CopyOnWriteArrayList<>();

        // 取得更新值
        for (Long detail : ids) {
            ActivityDefinition ActivityDefinition = new ActivityDefinition();
            ActivityDefinition.setId(detail);
            ActivityDefinition.setStatusEnum(PublicationStatus.RETIRED.getValue());
            ActivityDefinitionList.add(ActivityDefinition);
        }
        // 更新诊疗信息
        return iActivityDefinitionService.updateBatchById(ActivityDefinitionList)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"诊疗目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));

    }

    /**
     * 诊疗目录启用
     *
     * @param ids 诊疗目录ID列表
     * @return
     */
    @Override
    public R<?> editDiseaseTreatmentStart(List<Long> ids) {

        List<ActivityDefinition> ActivityDefinitionList = new CopyOnWriteArrayList<>();

        // 取得更新值
        for (Long detail : ids) {
            ActivityDefinition ActivityDefinition = new ActivityDefinition();
            ActivityDefinition.setId(detail);
            ActivityDefinition.setStatusEnum(PublicationStatus.ACTIVE.getValue());
            ActivityDefinitionList.add(ActivityDefinition);
        }
        // 更新诊疗信息
        return iActivityDefinitionService.updateBatchById(ActivityDefinitionList)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"诊疗目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));

    }

    /**
     * 新增外来诊疗目录
     *
     * @param diagnosisTreatmentUpDto 诊疗目录
     * @return
     */
    @Override
    public R<?> addDiseaseTreatment(DiagnosisTreatmentUpDto diagnosisTreatmentUpDto) {

        ActivityDefinition activityDefinition = new ActivityDefinition();
        BeanUtils.copyProperties(diagnosisTreatmentUpDto, activityDefinition);
        // 拼音码
        activityDefinition.setPyStr(ChineseConvertUtils.toPinyinFirstLetter(activityDefinition.getName()));
        // 五笔码
        activityDefinition.setWbStr(ChineseConvertUtils.toWBFirstLetter(activityDefinition.getName()));

        // 新增外来诊疗目录
        activityDefinition.setStatusEnum(PublicationStatus.DRAFT.getValue());
        if (iActivityDefinitionService.addDiagnosisTreatment(activityDefinition)) {

            ItemUpFromDirectoryDto itemUpFromDirectoryDto = new ItemUpFromDirectoryDto();
            BeanUtils.copyProperties(diagnosisTreatmentUpDto, itemUpFromDirectoryDto);
            itemUpFromDirectoryDto.setTypeCode(diagnosisTreatmentUpDto.getItemTypeCode())
                .setInstanceTable(CommonConstants.TableName.WOR_ACTIVITY_DEFINITION)
                .setEffectiveStart(DateUtils.getNowDate()).setStatusEnum(PublicationStatus.ACTIVE.getValue())
                .setConditionFlag(Whether.YES.getValue()).setChargeName(diagnosisTreatmentUpDto.getName())
                .setInstanceId(activityDefinition.getId()).setPrice(diagnosisTreatmentUpDto.getRetailPrice());

            return itemDefinitionServic.addItem(itemUpFromDirectoryDto)
                ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"诊疗目录"}))
                : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00008, null));

        }
        return R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00008, null));

    }
}
