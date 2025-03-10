package com.openhis.web.doctorstation.appservice.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.core.common.core.domain.R;
import com.openhis.document.domain.Emr;
import com.openhis.document.domain.EmrDetail;
import com.openhis.document.domain.EmrDict;
import com.openhis.document.domain.EmrTemplate;
import com.openhis.document.service.IEmrDetailService;
import com.openhis.document.service.IEmrDictService;
import com.openhis.document.service.IEmrService;
import com.openhis.document.service.IEmrTemplateService;
import com.openhis.web.doctorstation.appservice.IDoctorStationEmrAppService;
import com.openhis.web.doctorstation.dto.EmrTemplateDto;
import com.openhis.web.doctorstation.dto.PatientEmrDto;

/**
 * 医生站-电子病历 应用实现类
 */
@Service
public class DoctorStationEmrAppServiceImpl implements IDoctorStationEmrAppService {

    @Resource
    IEmrService emrService;

    @Resource
    IEmrTemplateService emrTemplateService;

    @Resource
    IEmrDetailService emrDetailService;

    @Resource
    IEmrDictService emrDictService;

    /**
     * 添加病人病历信息
     *
     * @param patientEmrDto 电子病历信息dto
     * @return 操作结果
     */
    @Override
    public R<?> addPatientEmr(PatientEmrDto patientEmrDto) {
        Emr emr = new Emr();
        BeanUtils.copyProperties(patientEmrDto, emr);
        String contextStr = patientEmrDto.getContextJson().toString();
        boolean saveSuccess = emrService.save(emr.setContextJson(contextStr));
        if (!saveSuccess) {
            return R.fail();
        }
        // 获取电子病历字典表中全部key，用来判断病历JSON串中是否有需要加入到病历详情表的字段
        List<String> emrDictList = emrDictService.list(new LambdaQueryWrapper<EmrDict>().select(EmrDict::getEmrKey))
            .stream().map(EmrDict::getEmrKey).collect(Collectors.toList());
        Map<String, String> emrContextMap =
            JSONObject.parseObject(contextStr, new TypeReference<Map<String, String>>() {});
        List<EmrDetail> emrDetailList = new ArrayList<>();
        // 遍历病历内容map
        for (Map.Entry<String, String> entry : emrContextMap.entrySet()) {
            EmrDetail emrDetail = new EmrDetail();
            emrDetail.setEmrId(emr.getId());
            if (!emrDictList.isEmpty() && emrDictList.contains(entry.getKey())) {
                emrDetail.setEmrKey(entry.getKey());
                emrDetail.setEmrValue(entry.getValue());
            }
            emrDetailList.add(emrDetail);
        }
        boolean save = emrDetailService.saveBatch(emrDetailList);
        return save ? R.ok() : R.fail();
    }

    /**
     * 获取患者历史病历
     *
     * @param patientEmrDto 查询条件
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return 分页数据列表
     */
    @Override
    public R<?> getPatientEmrHistory(PatientEmrDto patientEmrDto, Integer pageNo, Integer pageSize) {
        Page<Emr> page = emrService.page(new Page<>(pageNo, pageSize),
            new LambdaQueryWrapper<Emr>().eq(Emr::getPatientId, patientEmrDto.getPatientId()));
        return R.ok(page);
    }

    /**
     * 保存病历模板
     *
     * @param emrTemplateDto 病历模板信息
     * @return 操作结果
     */
    @Override
    public R<?> addEmrTemplate(EmrTemplateDto emrTemplateDto) {
        EmrTemplate emrTemplate = new EmrTemplate();
        BeanUtils.copyProperties(emrTemplateDto, emrTemplate);
        return emrTemplateService.save(emrTemplate) ? R.ok() : R.fail();
    }

    /**
     * 获取电子病历模板列表
     *
     * @param emrTemplateDto 查询参数
     * @param pageNo 当前页码
     * @param pageSize 查询条数
     * @return
     */
    @Override
    public R<?> getEmrTemplate(EmrTemplateDto emrTemplateDto, Integer pageNo, Integer pageSize) {
        LambdaQueryWrapper<EmrTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(EmrTemplate::getTemplateName, emrTemplateDto.getTemplateName())
            .eq(EmrTemplate::getUseScopeCode, emrTemplateDto.getUseScopeCode())
            .eq(EmrTemplate::getUserId, emrTemplateDto.getUserId());
        Page<EmrTemplate> emrTemplatePage = emrTemplateService.page(new Page<>(pageNo, pageSize), queryWrapper);
        return R.ok(emrTemplatePage);
    }

    /**
     * 删除病历模板
     *
     * @param id 模板id
     * @return 操作结果
     */
    @Override
    public R<?> deleteEmrTemplate(Long id) {
        return emrTemplateService.removeById(id) ? R.ok() : R.fail();
    }

}
