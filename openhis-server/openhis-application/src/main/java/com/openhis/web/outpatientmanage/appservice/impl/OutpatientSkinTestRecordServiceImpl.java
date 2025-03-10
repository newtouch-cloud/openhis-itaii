package com.openhis.web.outpatientmanage.appservice.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.core.common.utils.DateUtils;
import com.core.common.utils.SecurityUtils;
import com.core.common.utils.StringUtils;
import com.openhis.administration.domain.Practitioner;
import com.openhis.administration.domain.PractitionerRole;
import com.openhis.administration.mapper.PractitionerMapper;
import com.openhis.administration.mapper.PractitionerRoleMapper;
import com.openhis.clinical.domain.AllergyIntolerance;
import com.openhis.workflow.domain.ServiceRequest;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openhis.clinical.mapper.AllergyIntoleranceMapper;
import com.openhis.common.enums.ClinicalStatus;
import com.openhis.common.enums.VerificationStatus;
import com.openhis.common.utils.EnumUtils;
import com.openhis.web.outpatientmanage.appservice.IOutpatientSkinTestRecordService;
import com.openhis.web.outpatientmanage.dto.OutpatientSkinTestRecordDto;
import com.openhis.web.outpatientmanage.dto.OutpatientSkinTestRecordSearchParam;
import com.openhis.web.outpatientmanage.mapper.OutpatientManageMapper;
import com.openhis.web.patientmanage.dto.PatientListDto;
import com.openhis.workflow.mapper.ServiceRequestMapper;

/**
 * 门诊管理 应用实现类
 *
 * @author liuhr
 * @date 2025/3/7
 */
@Service
public class OutpatientSkinTestRecordServiceImpl implements IOutpatientSkinTestRecordService {

    @Resource
    OutpatientManageMapper outpatientManageMapper;

    @Resource
    ServiceRequestMapper serviceRequestMapper;

    @Resource
    AllergyIntoleranceMapper allergyIntoleranceMapper;

    @Resource
    PractitionerMapper practitionerMapper;

    @Resource
    PractitionerRoleMapper practitionerRoleMapper;


    /**
     * 获取皮试项目检查状态列表
     */
    @Override
    public List<PatientListDto> getSkinTestStatus() {
        // 获取皮试状态列表
        List<VerificationStatus> statusList = Arrays.asList(VerificationStatus.values());
        List<PatientListDto> dtos = new ArrayList<>();
        // 取得更新值
        for (VerificationStatus status : statusList) {
            PatientListDto dto = new PatientListDto();
            dto.setValue(status.getValue());
            dto.setInfo(status.getInfo());
            dtos.add(dto);
        }
        return dtos;
    }

    /**
     * 获取皮试项目检查状态列表
     */
    @Override
    public List<PatientListDto> getSkinTestResult() {
        // 获取皮试状态列表
        List<ClinicalStatus> statusList = Arrays.asList(ClinicalStatus.values());
        List<PatientListDto> dtos = new ArrayList<>();
        // 取得更新值
        for (ClinicalStatus status : statusList) {
            PatientListDto dto = new PatientListDto();
            dto.setValue(status.getValue());
            dto.setInfo(status.getInfo());
            dtos.add(dto);
        }
        return dtos;
    }

    /**
     * 分页查询门诊皮试记录,可选条件
     *
     * @param outpatientSkinTestRecordSearchParam 查询条件
     * @param pageNo 页码（默认为1）
     * @param pageSize 每页大小（默认为10）
     */
    @Override
    public Page<OutpatientSkinTestRecordDto> getSkinTestRecords(
        OutpatientSkinTestRecordSearchParam outpatientSkinTestRecordSearchParam, Integer pageNo, Integer pageSize) {
        // 跳过的记录数
        Integer offset = (pageNo - 1) * pageSize;
        // 连表查询患者信息
        List<OutpatientSkinTestRecordDto> listOutpatientSkinTestRecords =
            outpatientManageMapper.getOutpatientSkinTestRecord(outpatientSkinTestRecordSearchParam, pageSize, offset);

        // 查询总记录数
        long total = outpatientManageMapper.countOutpatientSkinTestRecords(outpatientSkinTestRecordSearchParam);
        // 创建Page对象并设置属性
        Page<OutpatientSkinTestRecordDto> outpatientSkinTestRecordPage = new Page<>(pageNo, pageSize, total);
        outpatientSkinTestRecordPage.setRecords(listOutpatientSkinTestRecords);
        outpatientSkinTestRecordPage.getRecords().forEach(e -> {
            // 皮试结果状态枚举类回显赋值
            e.setClinicalStatusEnum_enumText(EnumUtils.getInfoByValue(ClinicalStatus.class, e.getClinicalStatusEnum()));
            // 皮试检查项目状态枚举类回显赋值
            e.setVerificationStatusEnum_enumText(
                EnumUtils.getInfoByValue(VerificationStatus.class, e.getVerificationStatusEnum()));
        });
        return outpatientSkinTestRecordPage;
    }

    /**
     * 获取门诊皮试记录列表
     *
     * @param outpatientSkinTestRecordSearchParam 门诊皮试记录查询参数
     * @param pageSize 页面大小
     * @param offset 跳过条数
     * @return 分页查询
     */
    @Override
    public List<OutpatientSkinTestRecordDto> getOutpatientSkinTestRecord(
        OutpatientSkinTestRecordSearchParam outpatientSkinTestRecordSearchParam, Integer pageSize, Integer offset) {
        return outpatientManageMapper.getOutpatientSkinTestRecord(outpatientSkinTestRecordSearchParam, pageSize,
            offset);
    }

    /**
     * 统计门诊皮试记录数的方法
     *
     * @param outpatientSkinTestRecordSearchParam 门诊皮试记录查询参数
     * @return 分页查询
     */
    @Override
    public long
        countOutpatientSkinTestRecords(OutpatientSkinTestRecordSearchParam outpatientSkinTestRecordSearchParam) {
        return outpatientManageMapper.countOutpatientSkinTestRecords(outpatientSkinTestRecordSearchParam);
    }

    /**
     * 护士确认执行皮试后，更新皮试记录信息（服务申请管理与过敏与不耐受的相关字段更新）
     *
     * @param outpatientSkinTestRecordDto 皮试记录信息
     */
    @Override
    public int editSkinTestRecord(OutpatientSkinTestRecordDto outpatientSkinTestRecordDto) {

        // 更新服务申请管理表
        ServiceRequest serviceRequest = new ServiceRequest();
        // 更新的条件
        serviceRequest.setId(outpatientSkinTestRecordDto.getId());

        // 判断开始时间为空，不允许更新表
        if (StringUtils.isEmpty(outpatientSkinTestRecordDto.getOccurrenceStartTime())) {
            return 0;
        }
        Date endTime;
        //判断结束时间，为空以开始时间基础加10分钟
        if (StringUtils.isEmpty(outpatientSkinTestRecordDto.getOccurrenceEndTime())) {
            // 结束时间为空，开始时间加10min设置
            endTime =
                DateUtils.addDateMinute(DateUtils.parseDate(outpatientSkinTestRecordDto.getOccurrenceEndTime()), 10);
        } else {
            endTime = DateUtils.parseDate(outpatientSkinTestRecordDto.getOccurrenceEndTime());
        }
        // 设置开始时间
        serviceRequest
            .setOccurrenceStartTime(DateUtils.parseDate(outpatientSkinTestRecordDto.getOccurrenceStartTime()));
        // 设置结束时间
        serviceRequest.setOccurrenceEndTime(endTime);

        // 获取系统登录的userId，找到practitionerId
        Practitioner practitioner;
        QueryWrapper<Practitioner> queryWrapperP = new QueryWrapper<>();
        queryWrapperP.eq("user_id", SecurityUtils.getLoginUser().getUserId()); // 设置查询条件为user_id等于指定值
        practitioner = practitionerMapper.selectOne(queryWrapperP);
        // 设置执行人ID
        serviceRequest.setPerformerId(practitioner.getId());

        // 以执行人ID，获取执行人的身份类别
        PractitionerRole practitionerRole;
        QueryWrapper<PractitionerRole> queryWrapperPR = new QueryWrapper<>();
        queryWrapperP.eq("practitioner_id", practitioner.getId());
        practitionerRole = practitionerRoleMapper.selectOne(queryWrapperPR);
        // 设置执行人身份类别
        serviceRequest.setPerformerTypeCode(practitionerRole.getRoleCode());

        // 以id为主条件更新服务申请管理表
        UpdateWrapper<ServiceRequest> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", serviceRequest.getId())
            .set("performer_type_code", serviceRequest.getPerformerTypeCode())
            .set("performer_id", serviceRequest.getPerformerId())
            .set("occurrence_start_time", serviceRequest.getOccurrenceStartTime())
            .set("occurrence_end_time", serviceRequest.getOccurrenceEndTime());

        int count = serviceRequestMapper.update(null, updateWrapper);

        //过敏与不耐受表更新
        AllergyIntolerance allergyIntolerance = new AllergyIntolerance();
        //设置服务申请ID
        allergyIntolerance.setRequestId(outpatientSkinTestRecordDto.getId());
        //设置临床状态（皮试结果）
        allergyIntolerance.setClinicalStatusEnum(outpatientSkinTestRecordDto.getClinicalStatusEnum());
        //设置验证状态（皮试检查的状态）
        allergyIntolerance.setVerificationStatusEnum(outpatientSkinTestRecordDto.getVerificationStatusEnum());
        //设置患者id
        allergyIntolerance.setPatientId(outpatientSkinTestRecordDto.getPatientId());
        //设置记录者id
        allergyIntolerance.setPractitionerId(practitioner.getId());
        //设置记录日期(当下日期)
        allergyIntolerance.setRecordedDate(DateUtils.getNowDate());
        //设置备注
        allergyIntolerance.setNote(outpatientSkinTestRecordDto.getNote());

        // 以服务申请ID为主条件更新过敏与不耐受表
        UpdateWrapper<AllergyIntolerance> updateWrapperAI = new UpdateWrapper<>();
        updateWrapperAI.eq("request_id",allergyIntolerance.getRequestId());
//        boolean result = allergyIntoleranceMapper.saveOrUpdate(allergyIntolerance, updateWrapper);


        return 1;
    }

}
