/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.service.impl;

import static com.alibaba.fastjson.JSON.parseObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.core.common.utils.SecurityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import com.openhis.common.constant.CommonConstants;
import com.openhis.component.ElepHandlerIntDispatcher;
import com.openhis.config.InterfaceConfig;
import com.openhis.config.Tenant;
import com.openhis.enums.ElepServiceAdrEnum;
import com.openhis.pojo.RequestData;
import com.openhis.utils.PDFTemplateFillerUtil;
import com.openhis.utils.RedisUtil;
import com.openhis.vo.BaseInfo;
import com.openhis.vo.BaseParam;
import com.openhis.web.ybmanage.dto.PDFInputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.openhis.domain.*;
import com.openhis.service.*;
import com.openhis.vo.Result;

/**
 * @author yuxj
 * @date 2025-04-17
 */
@Service
public class YbElepServiceImpl implements IYbElepService {

    /**
     * 接口分发器
     */
    @Autowired
    private ElepHandlerIntDispatcher elepHandlerIntDispatcher;

    /**
     * redis工具
     */
    @Autowired
    private RedisUtil redisUtil;
    /**
     * 中心接口配置
     */
    @Autowired
    protected InterfaceConfig interfaceConfig;

    @Autowired
    private Tenant tenant;

    /**
     * 电子处方上传预核验
     *
     * @param baseParam 处方信息
     * @return 返回值
     */
    public Result<?> preCheckPrescription(BaseParam baseParam) {
        BaseInfo baseInfo = baseParam.getBaseInfo();
        baseInfo.setAdmvs(baseInfo.getAdmvs()).setFixmedinsCode(baseInfo.getFixmedinsCode()).setFixmedinsName(baseInfo.getFixmedinsName());
        PreCheckPrescription prescriptioninfo =
                JSON.parseObject(JSON.toJSONString(baseParam.getData()), PreCheckPrescription.class);
        // 调用分发器处理预核验业务
        BaseResponse result = elepHandlerIntDispatcher.dispatcher(ElepServiceAdrEnum.A0001.toString(),
                new RequestData(prescriptioninfo), baseParam);
        // 检查服务调用是否成功
        if (result.getCode() == 0) {
            // 解析返回结果中的数据
            ElepVeriPrescriptionOutput output = null;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                output = objectMapper.readValue(result.getEncData(), ElepVeriPrescriptionOutput.class);
            } catch (Exception e) {
                Result.error("返回参数转换错误");
            }
            // 做成PDF
            List<PDFInputDto.MedDetail> medDetailList = new ArrayList<>();
            PDFInputDto.MedDetail medDetail = new PDFInputDto.MedDetail();
            for (ElepVeriPrescriptionDetail item : prescriptioninfo.getRxdrugdetail()) {
                // 药品名字
                medDetail.setMedName(item.getDrugGenname());
                // 药品规格
                medDetail.setDrugSpec(item.getDrugSpec());
                // 单次用量
                medDetail.setSinDoscnt(new BigDecimal(item.getSinDoscnt()));
                // 单次剂量单
                medDetail.setSinDosunt(item.getSinDosunt());
                // 使用频次编码
                medDetail.setUsedFrquCodg(item.getUsedFrquCodg());
                // 用药途径
                medDetail.setMedWay(item.getMedcWayCodg());
                medDetailList.add(medDetail);
            }
            // 主诊断名称
            String diagName = prescriptioninfo.getDiseinfo().getDiagName();

            // 做成原始PDF文件
            PDFInputDto pdfInputDto = new PDFInputDto()
                    // 医保电子处方追溯码
                    .setRxTraceCode(output.getRxTraceCode())
                    // 机构名
                    .setOrgName(prescriptioninfo.getMdtrtinfo().getFixmedinsName())
                    // 门诊/住院病历号
                    .setIptOtpNo(prescriptioninfo.getMdtrtinfo().getIptOtpNo())
                    // 院内处方编号
                    .setPrescriptionNo(prescriptioninfo.getHospRxno())
                    // 科别/病区和床位
                    .setLocationName(prescriptioninfo.getMdtrtinfo().getPrscDeptName())
                    // 姓名
                    .setPatnName(prescriptioninfo.getMdtrtinfo().getPatnName())
                    // 性别
                    .setGender(prescriptioninfo.getMdtrtinfo().getGend())
                    // 年龄
                    .setPatnAge(new BigDecimal(prescriptioninfo.getMdtrtinfo().getPatnAge()))
                    // 费别
                    .setHiFeesetlType(prescriptioninfo.getMdtrtinfo().getHiFeesetlName())
                    // 开具日期
                    .setPrscTime(prescriptioninfo.getPrscTime())
                    // 临床诊断
                    .setDiagName(diagName)
                    // 处方有效天数
                    .setValiDays(new BigDecimal(prescriptioninfo.getValiDays()))
                    // 开方医师
                    .setPrscDrName(prescriptioninfo.getMdtrtinfo().getPrscDrName())
                    // 审核药师
                    .setPharName(prescriptioninfo.getMdtrtinfo().getPrscDrName())
                    // 调配、复核药师
                    .setDisRevPharName(null)
                    // 核对、发药药师
                    .setCheckPharName(null)
                    // 延长处方用量原因
                    .setReason(null)
                    // 药品明细信息
                    .setMedDetailList(medDetailList);

            // 获取当前日期
            LocalDate currentDate = LocalDate.now();
            // 定义格式（yyyyMMdd）
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            // 格式化日期
            String formattedDate = currentDate.format(formatter);

            String templatePath = baseInfo.getTemplatePath();
            String outputPath = baseInfo.getOutputPath() + formattedDate + "\\" + prescriptioninfo.getHospRxno() + ".pdf";
            String hospitalSealPath = baseInfo.getHospitalSealPath();
            try {
                PDFTemplateFillerUtil.fillPdfTemplate(templatePath, outputPath, hospitalSealPath, pdfInputDto);
                System.out.println("PDF 填充完成，文件已保存到：" + outputPath);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                System.err.println("填充 PDF 时发生错误：" + e.getMessage());
            }
            // 将pdf的名字保存
            redisUtil.set("RxFileName", outputPath);

            // 返回成功响应
            return Result.ok("电子处方上传预核验成功！", output);
        }

        return Result.error(result.getMessage());
    }

    /**
     * 电子处方医保电子签名
     *
     * @param baseParam 电子签名信息
     * @return 返回值
     */
    public Result<?> signature(BaseParam baseParam) {
        BaseInfo baseInfo = baseParam.getBaseInfo();
        baseInfo.setAdmvs(baseInfo.getAdmvs()).setFixmedinsCode(baseInfo.getFixmedinsCode()).setFixmedinsName(baseInfo.getFixmedinsName());
        ElepSignatureInput signatureInput =
                JSON.parseObject(JSON.toJSONString(baseParam.getData()), ElepSignatureInput.class);
        String prescriptionNo = signatureInput.getPrescriptionNo();
        signatureInput.setPrescriptionNo(null);

        // 调用分发器处理电子签名业务
        BaseResponse result = elepHandlerIntDispatcher.dispatcher(ElepServiceAdrEnum.A0002.toString(),
                new RequestData(signatureInput), baseParam);
        // 检查服务调用是否成功
        if (result.getCode() == 0) {
            // 解析返回结果中的数据
            ElepSignatureOutput output =
                    parseObject(parseObject(result.getEncData()).toString(), ElepSignatureOutput.class);

            LocalDate currentDate = LocalDate.now();
            // 定义格式（yyyyMMdd）
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            // 格式化日期
            String formattedDate = currentDate.format(formatter);
            String folderPath = baseInfo.getOutputPath() + formattedDate + "\\"; // Windows 绝对路径，注意末尾的反斜杠
            String fileName = prescriptionNo + ".pdf"; // 文件名由处方号拼接而成
            // 拼接完整的文件路径
            String filePath = folderPath + fileName;
            // 做成PDF文件
            boolean rxFileBool = PDFTemplateFillerUtil.makePDF(output.getRxFile(), filePath);
            if (!rxFileBool) {
                return Result.error("PDF文件做成失败！");
            }

            // 返回成功响应
            return Result.ok("电子处方医保电子签名成功！", output);
        }

        return Result.error(result.getMessage());
    }

    /**
     * 电子处方上传
     *
     * @param baseParam 上传信息
     * @return 返回值
     */
    public Result<?> upload(BaseParam baseParam) {
        BaseInfo baseInfo = baseParam.getBaseInfo();
        baseInfo.setAdmvs(baseInfo.getAdmvs()).setFixmedinsCode(baseInfo.getFixmedinsCode()).setFixmedinsName(baseInfo.getFixmedinsName());

        ElepUploadInput uploadInput =
                JSON.parseObject(JSON.toJSONString(baseParam.getData()), ElepUploadInput.class);

        // 调用分发器处理上传业务
        BaseResponse result = elepHandlerIntDispatcher.dispatcher(ElepServiceAdrEnum.A0003.toString(),
                new RequestData(uploadInput), baseParam);
        // 检查服务调用是否成功
        if (result.getCode() == 0) {
            // 解析返回结果中的数据
            ElepUploadOutput output = parseObject(parseObject(result.getEncData()).toString(), ElepUploadOutput.class);
            // 返回成功响应
            return Result.ok("电子处方上传成功！", output);
        }

        return Result.error(result.getMessage());
    }

    /**
     * 电子处方撤销
     *
     * @param baseParam 撤销信息
     * @return 返回值
     */
    public Result<?> revoke(BaseParam baseParam) {
        BaseInfo baseInfo = baseParam.getBaseInfo();
        baseInfo.setAdmvs(baseInfo.getAdmvs()).setFixmedinsCode(baseInfo.getFixmedinsCode()).setFixmedinsName(baseInfo.getFixmedinsName());

        ElepRevokeInput revokeInput =
                JSON.parseObject(JSON.toJSONString(baseParam.getData()), ElepRevokeInput.class);
        // 调用分发器处理上传业务
        BaseResponse result = elepHandlerIntDispatcher.dispatcher(ElepServiceAdrEnum.A0004.toString(),
                new RequestData(revokeInput), baseParam);
        // 检查服务调用是否成功
        if (result.getCode() == 0) {
            // 解析返回结果中的数据
            ElepRevokeOutput output = parseObject(parseObject(result.getEncData()).toString(), ElepRevokeOutput.class);
            // 返回成功响应
            return Result.ok("电子处方撤销成功！", output);
        }

        return Result.error(result.getMessage());
    }

    /**
     * 电子处方信息查询
     *
     * @param baseParam 查询信息
     * @return 返回值
     */
    public Result<?> querPrescription(BaseParam baseParam) {
        BaseInfo baseInfo = baseParam.getBaseInfo();
        baseInfo.setAdmvs(baseInfo.getAdmvs()).setFixmedinsCode(baseInfo.getFixmedinsCode()).setFixmedinsName(baseInfo.getFixmedinsName());

        ElepQuerPrescriptionInput querPrescriptionInput =
                JSON.parseObject(JSON.toJSONString(baseParam.getData()), ElepQuerPrescriptionInput.class);
//        (ElepQuerPrescriptionInput)baseParam.getData();
        // 调用分发器处理信息查询业务
        BaseResponse result = elepHandlerIntDispatcher.dispatcher(ElepServiceAdrEnum.A0005.toString(),
                new RequestData(querPrescriptionInput), baseParam);
        // 检查服务调用是否成功
        if (result.getCode() == 0) {
            // 解析返回结果中的数据
            QueryPrescription output =
                    parseObject(parseObject(result.getEncData()).toString(), QueryPrescription.class);
            // 返回成功响应
            return Result.ok("电子处方信息查询成功！", output);
        }

        return Result.error(result.getMessage());
    }

    /**
     * 电子处方取药结果查询
     *
     * @param baseParam 撤销信息
     * @return 返回值
     */
    public Result<?> medresult(BaseParam baseParam) {
        BaseInfo baseInfo = baseParam.getBaseInfo();
        baseInfo.setAdmvs(baseInfo.getAdmvs()).setFixmedinsCode(baseInfo.getFixmedinsCode()).setFixmedinsName(baseInfo.getFixmedinsName());

        ElepMedresultInput medresultInput =
                JSON.parseObject(JSON.toJSONString(baseParam.getData()), ElepMedresultInput.class);

        // 调用分发器处理结果查询业务
        BaseResponse result = elepHandlerIntDispatcher.dispatcher(ElepServiceAdrEnum.A0006.toString(),
                new RequestData(medresultInput), baseParam);
        // 检查服务调用是否成功
        if (result.getCode() == 0) {
            // 解析返回结果中的数据
            MedicationResultInquiry output =
                    parseObject(parseObject(result.getEncData()).toString(), MedicationResultInquiry.class);
            // 返回成功响应
            return Result.ok("电子处方取药结果查询成功！", output);
        }

        return Result.error(result.getMessage());
    }

}
