package com.openhis.web.reportmanage.appservice.impl;

import java.util.List;

import com.openhis.web.reportmanage.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.common.core.domain.R;
import com.core.common.utils.AgeCalculatorUtil;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.SecurityUtils;
import com.openhis.common.constant.PromptMsgConstant;
import com.openhis.common.enums.AccountType;
import com.openhis.common.enums.AdministrativeGender;
import com.openhis.common.enums.ClinicalStatus;
import com.openhis.common.enums.Whether;
import com.openhis.common.enums.ybenums.YbRxItemTypeCode;
import com.openhis.common.utils.EnumUtils;
import com.openhis.web.reportmanage.appservice.IPrintReportAppService;
import com.openhis.web.reportmanage.mapper.PrintReportMapper;
import com.openhis.workflow.service.IServiceRequestService;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 院内处方和医嘱报告打印 接口实现类
 *
 * @author liuhr
 * @date 2025/5/6
 */
@Service
public class IPrintReportAppServiceImpl implements IPrintReportAppService {

    @Autowired
    private PrintReportMapper printReportMapper;
    @Autowired
    private IServiceRequestService serviceRequestService;

    /**
     * 打印——处置单
     *
     * @param encounterId 患者就诊Id
     * @return 处置单信息
     */
    @Override
    public R<?> disposalPrint(Long encounterId) {

        List<DisposalDto> disposalList = printReportMapper.getDisposalList(encounterId);

        // 获取所属医院id
        Long hospitalId = SecurityUtils.getLoginUser().getHospitalId();

        disposalList.forEach(e -> {
            // 医院名
            e.setOrgId(hospitalId);
            // 回显性别
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 计算年龄
            e.setAge(AgeCalculatorUtil.getAge(e.getBirthDate()));
        });

        return R.ok(disposalList);
    }

    /**
     * 打印——检查申请单
     *
     * @param encounterId 患者就诊Id
     * @return 检查申请单信息
     */
    @Override
    public R<?> checkApplicationPrint(Long encounterId) {

        List<CkInspAppDto> checkList =
                printReportMapper.getCheckInspectionList(encounterId, YbRxItemTypeCode.MEDICAL_IMAGING.getValue());

        // 获取所属医院id
        Long hospitalId = SecurityUtils.getLoginUser().getHospitalId();

        checkList.forEach(e -> {
            // 医院名
            e.setOrgId(hospitalId);
            // 回显性别
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 计算年龄
            e.setAge(AgeCalculatorUtil.getAge(e.getBirthDate()));
        });

        return R.ok(checkList);
    }

    /**
     * 打印——检验申请单
     *
     * @param encounterId 患者就诊Id
     * @return 检验申请单信息
     */
    @Override
    public R<?> inspectionApplicationPrint(Long encounterId) {
        List<CkInspAppDto> inspectionList =
                printReportMapper.getCheckInspectionList(encounterId, YbRxItemTypeCode.LAB_TEST.getValue());

        // 获取所属医院id
        Long hospitalId = SecurityUtils.getLoginUser().getHospitalId();

        inspectionList.forEach(e -> {
            // 医院名
            e.setOrgId(hospitalId);
            // 回显性别
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 计算年龄
            e.setAge(AgeCalculatorUtil.getAge(e.getBirthDate()));
        });

        return R.ok(inspectionList);
    }

    /**
     * 打印——处方单
     *
     * @param prescriptionNo 院内处方编号
     * @param encounterId    患者就诊Id
     * @return 处方单信息
     */
    @Override
    public R<?> prescriptionPrint(String prescriptionNo, Long encounterId) {

        List<PrescriptionPrintDto> list = printReportMapper.getPrescriptionList(prescriptionNo, encounterId);
        // 获取所属医院id
        Long hospitalId = SecurityUtils.getLoginUser().getHospitalId();

        list.forEach(e -> {
            // 医院名
            e.setOrgId(hospitalId);
            // 性别枚举类回显赋值
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 计算年龄
            e.setAge(AgeCalculatorUtil.getAge(e.getBirthDate()));
            // 是否慢病
            e.setNcdsFlag_enumText(EnumUtils.getInfoByValue(Whether.class, e.getNcdsFlag()));
            // 费别
            e.setTypeCode_enumText(EnumUtils.getInfoByCode(AccountType.class, e.getTypeCode()));
            // 皮试结果
            e.setPsResult(EnumUtils.getInfoByValue(ClinicalStatus.class, e.getClinicalStatusEnum()));

            switch (e.getPharmacologyCategoryCode()) {
                case "2": // 麻醉药品
                    e.setPharmacologyCategoryCode_Text("麻醉");
                case "3": // 毒性药品
                    e.setPharmacologyCategoryCode_Text("毒性");
                case "4": // 一类精神药
                    e.setPharmacologyCategoryCode_Text("精一");
                case "5": // 二类精神药
                    e.setPharmacologyCategoryCode_Text("精二");
                default: // 普通药品
                    e.setPharmacologyCategoryCode_Text("普通");
            }
        });

        return R.ok(list);
    }

    /**
     * 打印——护士输液的瓶签
     *
     * @param serviceId   服务请求id
     * @param encounterId 就诊id
     * @param groupId     药品组id
     * @return 输液的瓶签
     */
    public R<?> bottleLabelPrint(Long serviceId, Long encounterId, Long groupId) {

        List<BottleLabelDto> bottleLabelList = printReportMapper.getBottleLabelList(serviceId, encounterId, groupId);
        // 获取所属医院id
        Long hospitalId = SecurityUtils.getLoginUser().getHospitalId();

        bottleLabelList.forEach(e -> {
            // 医院名
            e.setOrgId(hospitalId);
            // 回显性别
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 计算年龄
            e.setAge(AgeCalculatorUtil.getAge(e.getBirthDate()));
        });

        return R.ok(bottleLabelList);
    }

    /**
     * 打印——护士输液的瓶签 批量
     *
     * @param @return 输液的瓶签
     */
    @Override
    public R<?> bottleLabelBatchPrint(List<Long> serviceIds,
                                      List<Long> encounterIds) {
        List<BottleLabelDto> bottleLabelList = printReportMapper.getBottleLabelBatchList(serviceIds, encounterIds);
        // 获取所属医院id
        Long hospitalId = SecurityUtils.getLoginUser().getHospitalId();

        bottleLabelList.forEach(e -> {
            // 医院名
            e.setOrgId(hospitalId);
            // 回显性别
            e.setGenderEnum_enumText(EnumUtils.getInfoByValue(AdministrativeGender.class, e.getGenderEnum()));
            // 计算年龄
            e.setAge(AgeCalculatorUtil.getAge(e.getBirthDate()));
        });

        return R.ok(bottleLabelList);
    }

    /**
     * 更新打印次数——护士输液的瓶签的打印次数
     *
     * @param serviceId 服务请求id
     * @param num       打印次数
     * @return 更新结果
     */
    public R<?> updatePrintCount(Long serviceId, Integer num) {

        serviceRequestService.updateCountPint(serviceId, num);
        return R.ok(null, MessageUtils.createMessage(PromptMsgConstant.Common.M00002, new Object[]{"打印次数"}));
    }

}
