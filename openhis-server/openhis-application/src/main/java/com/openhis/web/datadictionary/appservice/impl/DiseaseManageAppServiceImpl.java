package com.openhis.web.datadictionary.appservice.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.core.common.utils.AssignSeqUtil;
import com.core.common.utils.ChineseConvertUtils;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.clinical.domain.ConditionDefinition;
import com.openhis.clinical.mapper.ConditionDefinitionMapper;
import com.openhis.clinical.service.IConditionDefinitionService;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.AssignSeqEnum;
import com.openhis.common.enums.ConditionDefinitionSource;
import com.openhis.common.enums.PublicationStatus;
import com.openhis.common.enums.Whether;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.web.datadictionary.appservice.IDiseaseManageAppService;
import com.openhis.web.datadictionary.dto.DiseaseManageDto;
import com.openhis.web.datadictionary.dto.DiseaseManageInitDto;
import com.openhis.web.datadictionary.dto.DiseaseManageSelParam;
import com.openhis.web.datadictionary.dto.DiseaseManageUpDto;

/**
 * 病种目录 实现
 *
 * @author liuhr
 * @date 2025/3/30
 */
@Service
public class DiseaseManageAppServiceImpl implements IDiseaseManageAppService {

    @Autowired
    private ConditionDefinitionMapper conditionDefinitionMapper;
    @Autowired
    private IConditionDefinitionService ConditionDefinitionService;
    @Autowired(required = false)
    AssignSeqUtil assignSeqUtil;

    /**
     * 病种目录初始化
     *
     * @return
     */
    @Override
    public R<?> getDiseaseInit() {

        DiseaseManageInitDto diseaseManageInitDto = new DiseaseManageInitDto();
        // 获取疾病目录种类
        List<DiseaseManageInitDto.diseaseCategory> diseaseCategoryList = Stream.of(ConditionDefinitionSource.values())
            .map(status -> new DiseaseManageInitDto.diseaseCategory(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        diseaseManageInitDto.setDiseaseCategoryList(diseaseCategoryList);
        // 获取状态
        List<DiseaseManageInitDto.statusEnumOption> statusEnumOptions = Stream.of(PublicationStatus.values())
            .map(status -> new DiseaseManageInitDto.statusEnumOption(status.getValue(), status.getInfo()))
            .collect(Collectors.toList());
        diseaseManageInitDto.setStatusFlagOptions(statusEnumOptions);

        return R.ok(diseaseManageInitDto);
    }

    /**
     * 查询病种目录分页列表
     *
     * @param diseaseManageSelParam 查询条件
     * @param searchKey 查询条件-模糊查询
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return
     */
    public R<?> getDiseaseList(DiseaseManageSelParam diseaseManageSelParam, String searchKey, Integer pageNo,
        Integer pageSize, HttpServletRequest request) {

        // 构建查询条件
        QueryWrapper<ConditionDefinition> queryWrapper = HisQueryUtils.buildQueryWrapper(diseaseManageSelParam,
            searchKey, new HashSet<>(Arrays.asList("condition_code", "name", "py_str", "wb_str")), request);
        // 设置排序
        queryWrapper.orderByAsc("condition_code");
        // 分页查询
        Page<DiseaseManageDto> diseasePage =
            HisPageUtils.selectPage(conditionDefinitionMapper, queryWrapper, pageNo, pageSize, DiseaseManageDto.class);

        diseasePage.getRecords().forEach(e -> {
            // 医保对码枚举类回显赋值
            e.setYbMatchFlag_enumText(EnumUtils.getInfoByValue(Whether.class, e.getYbMatchFlag()));
            // 状态
            e.setStatusEnum_enumText(EnumUtils.getInfoByValue(PublicationStatus.class, e.getStatusEnum()));
            // 所属分类
            e.setSourceEnum_enumText(EnumUtils.getInfoByValue(ConditionDefinitionSource.class, e.getSourceEnum()));
            // 医保标记
            e.setYbFlag_enumText(EnumUtils.getInfoByValue(Whether.class, e.getYbFlag()));
        });

        // 返回【病种目录列表DTO】分页
        return R.ok(diseasePage);
    }

    /**
     * 根据id查询疾病详情
     *
     * @param id 疾病ID
     * @return
     */
    public R<?> getDiseaseOne(Long id) {
        DiseaseManageDto diseaseManageDto = new DiseaseManageDto();
        // 根据ID查询【病种目录】
        ConditionDefinition conditionDefinition = ConditionDefinitionService.getById(id);
        BeanUtils.copyProperties(conditionDefinition, diseaseManageDto);
        return R.ok(diseaseManageDto);
    }

    /**
     * 病种目录编辑
     *
     * @param diseaseManageDto 病种目录列表
     * @return
     */
    public R<?> editDisease(DiseaseManageUpDto diseaseManageDto) {

        ConditionDefinition conditionDefinition = new ConditionDefinition();
        BeanUtils.copyProperties(diseaseManageDto, conditionDefinition);
        // 拼音码
        conditionDefinition.setPyStr(ChineseConvertUtils.toPinyinFirstLetter(conditionDefinition.getName()));
        // 五笔码
        conditionDefinition.setWbStr(ChineseConvertUtils.toWBFirstLetter(conditionDefinition.getName()));

        // 更新病种信息
        return ConditionDefinitionService.updateById(conditionDefinition)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"疾病目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 病种目录停用
     *
     * @param ids 病种目录ID列表
     * @return
     */
    public R<?> editDiseaseStop(List<Long> ids) {
        List<ConditionDefinition> conditionDefinitionList = new CopyOnWriteArrayList<>();

        // 取得更新值
        for (Long detail : ids) {
            ConditionDefinition conditionDefinition = new ConditionDefinition();
            conditionDefinition.setId(detail);
            conditionDefinition.setStatusEnum(PublicationStatus.ACTIVE.getValue());
            conditionDefinitionList.add(conditionDefinition);
        }
        // 更新病种信息
        return ConditionDefinitionService.updateBatchById(conditionDefinitionList)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"疾病目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 病种目录启用
     *
     * @param ids 病种目录ID列表
     * @return
     */
    public R<?> editDiseaseStart(List<Long> ids) {
        List<ConditionDefinition> conditionDefinitionList = new CopyOnWriteArrayList<>();

        // 取得更新值
        for (Long detail : ids) {
            ConditionDefinition conditionDefinition = new ConditionDefinition();
            conditionDefinition.setId(detail);
            conditionDefinition.setStatusEnum(PublicationStatus.ACTIVE.getValue());
            conditionDefinitionList.add(conditionDefinition);
        }
        // 更新病种信息
        return ConditionDefinitionService.updateBatchById(conditionDefinitionList)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"疾病目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00007, null));
    }

    /**
     * 新增外来病种目录
     *
     * @param diseaseManageUpDto 病种目录
     * @return
     */
    public R<?> addDisease(DiseaseManageUpDto diseaseManageUpDto) {
        ConditionDefinition conditionDefinition = new ConditionDefinition();
        BeanUtils.copyProperties(diseaseManageUpDto, conditionDefinition);
        // 使用10位数基础采番
        String code = assignSeqUtil.getSeq(AssignSeqEnum.CONDITION_DEFINITION_NUM.getPrefix(), 10);
        conditionDefinition.setConditionCode(code);

        // 新增外来病种目录
        conditionDefinition.setStatusEnum(PublicationStatus.ACTIVE.getValue());
        // 拼音码
        conditionDefinition.setPyStr(ChineseConvertUtils.toPinyinFirstLetter(conditionDefinition.getName()));
        // 五笔码
        conditionDefinition.setWbStr(ChineseConvertUtils.toWBFirstLetter(conditionDefinition.getName()));
        return ConditionDefinitionService.addDisease(conditionDefinition)
            ? R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[] {"疾病目录"}))
            : R.fail(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00008, null));
    }

}
