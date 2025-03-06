package com.openhis.web.outpatientservice.appservice.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.utils.AgeCalculatorUtil;
import com.core.common.utils.bean.BeanUtils;
import com.openhis.administration.domain.Patient;
import com.openhis.administration.mapper.PatientMapper;
import com.openhis.clinical.domain.ConditionDefinition;
import com.openhis.clinical.mapper.ConditionDefinitionMapper;
import com.openhis.common.enums.AdministrativeGender;
import com.openhis.common.enums.PublicationStatus;
import com.openhis.common.enums.WhetherContainUnknown;
import com.openhis.common.utils.EnumUtils;
import com.openhis.common.utils.HisPageUtils;
import com.openhis.common.utils.HisQueryUtils;
import com.openhis.financial.domain.Contract;
import com.openhis.financial.mapper.ContractMapper;
import com.openhis.web.outpatientservice.appservice.IOutpatientRegistrationAppService;
import com.openhis.web.outpatientservice.dto.ConditionDefinitionMetadata;
import com.openhis.web.outpatientservice.dto.ContractMetadata;
import com.openhis.web.outpatientservice.dto.PatientMetadata;

/**
 * 门诊挂号 实现类
 */
@Service
public class IOutpatientRegistrationAppServiceImpl implements IOutpatientRegistrationAppService {

    @Resource
    PatientMapper patientMapper;

    @Resource
    ContractMapper contractMapper;

    @Resource
    ConditionDefinitionMapper conditionDefinitionMapper;

    /**
     * 门诊挂号 - 查询患者信息
     *
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 患者信息
     */
    @Override
    public Page<PatientMetadata> getPatientMetadataBySearchKey(String searchKey, Integer pageNo, Integer pageSize) {
        // 构建查询条件
        QueryWrapper<Patient> queryWrapper = HisQueryUtils.buildQueryWrapper(null, searchKey,
            new HashSet<>(Arrays.asList("name", "py_str", "wb_str")), null);
        // 设置排序
        queryWrapper.orderByDesc("update_time");
        // 患者信息
        Page<PatientMetadata> patientMetadataPage =
            HisPageUtils.selectPage(patientMapper, queryWrapper, pageNo, pageSize, PatientMetadata.class);

        patientMetadataPage.getRecords().forEach(e -> {
            // 性别枚举
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 计算年龄
            e.setAge(AgeCalculatorUtil.getAge(e.getBirthDate()));
        });
        return patientMetadataPage;
    }

    /**
     * 查询费用性质
     *
     * @return 费用性质
     */
    @Override
    public List<ContractMetadata> getContractMetadata() {
        // TODO: Contract表的基础数据维护还没做,具体不知道状态字段的取值是什么,先查询默认值为0的数据
        List<Contract> ContractList =
            contractMapper.selectList(new LambdaQueryWrapper<Contract>().eq(Contract::getStatusEnum, 0));
        // 复制同名字段并 return
        return ContractList.stream().map(contract -> {
            ContractMetadata metadata = new ContractMetadata();
            try {
                BeanUtils.copyProperties(contract, metadata);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return metadata;
        }).collect(Collectors.toList());
    }

    /**
     * 查询诊断信息
     *
     * @param searchKey 模糊查询关键字
     * @param pageNo 当前页
     * @param pageSize 每页多少条
     * @return 诊断信息
     */
    @Override
    public Page<ConditionDefinitionMetadata> getConditionDefinitionMetadataSearchKey(String searchKey, Integer pageNo,
        Integer pageSize) {
        // 构建查询条件
        ConditionDefinition conditionDefinition = new ConditionDefinition();
        // 查询状态是有效的
        conditionDefinition.setStatusEnum(PublicationStatus.ACTIVE.getValue());
        QueryWrapper<ConditionDefinition> queryWrapper = HisQueryUtils.buildQueryWrapper(conditionDefinition, searchKey,
            new HashSet<>(Arrays.asList("name", "py_str", "wb_str")), null);
        // 设置排序
        queryWrapper.orderByDesc("update_time");
        // 诊断信息
        Page<ConditionDefinitionMetadata> conditionDefinitionMetadataPage = HisPageUtils
            .selectPage(conditionDefinitionMapper, queryWrapper, pageNo, pageSize, ConditionDefinitionMetadata.class);
        conditionDefinitionMetadataPage.getRecords().forEach(e -> {
            // 医保标记
            e.setYbFlag_enumText(EnumUtils.getInfoByValue(WhetherContainUnknown.class, e.getYbFlag()));
            // 医保对码标记
            e.setYbMatchFlag_enumText(EnumUtils.getInfoByValue(WhetherContainUnknown.class, e.getYbMatchFlag()));
        });
        return conditionDefinitionMetadataPage;
    }

}
