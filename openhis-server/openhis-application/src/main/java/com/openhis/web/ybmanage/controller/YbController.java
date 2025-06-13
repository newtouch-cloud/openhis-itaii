/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.web.ybmanage.controller;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.core.common.utils.StringUtils;
import com.openhis.administration.domain.Encounter;
import com.openhis.administration.domain.Patient;
import com.openhis.administration.service.IEncounterService;
import com.openhis.clinical.domain.ConditionDefinition;
import com.openhis.clinical.service.IConditionDefinitionService;
import com.openhis.yb.domain.InfoPerson;
import com.openhis.yb.dto.Catalogue1312Output;
import com.openhis.yb.dto.Clearing3205AWebParma;
import com.openhis.yb.dto.Financial3203AWebParam;
import com.openhis.yb.dto.Info1101Output;
import com.openhis.yb.dto.PatientInfoDto;
import com.openhis.yb.dto.Result;
import com.openhis.yb.dto.Settlement3201WebParam;
import com.openhis.yb.dto.Settlement3202WebParam;
import com.openhis.yb.dto.Settlement3209AWebParam;
import com.openhis.yb.service.IPerinfoService;
import com.openhis.yb.service.YbDao;
import com.openhis.yb.service.YbHttpUtils;
import com.openhis.yb.util.CommonConstant;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.core.common.exception.ServiceException;
import com.core.common.utils.SecurityUtils;
import com.openhis.administration.service.IPatientService;
import com.openhis.common.constant.CommonConstants;
import com.openhis.yb.dto.*;

/**
 * 医保接口
 *
 * @author SunJQ
 * @date 2025-04-11
 */
@RestController
@RequestMapping("/yb-request")
public class YbController {

    @Autowired
    YbDao ybDao;
    @Autowired
    YbHttpUtils ybHttpUtils;
    @Autowired
    IPatientService patientService;
    @Autowired
    IPerinfoService iPerinfoService;
    @Autowired
    IEncounterService iEncounterService;
    @Autowired
    IConditionDefinitionService iConditionDefinitionService;
    @Autowired
    ResourceLoader resourceLoader;


    /**
     * 【9001】
     *
     * @param practitionerId 参与者Id
     * @param mac            加密后的mac地址
     * @return 结果
     */
    @PostMapping("/sign")
    public R<?> sign(String practitionerId, String mac, String ip) {
        Sign signParam = ybDao.getSignParam(practitionerId, mac, ip);
        if (signParam == null) {
            throw new ServiceException("未生成签到参数");
        }
        Sign9001Result signResult = ybHttpUtils.sign(signParam);
        if (signResult != null) {
            ybDao.saveSign(signParam, signResult);
            return R.ok(signResult);
        }
        return R.fail();
    }

    /**
     * 【1101】
     *
     * @param certType 就诊id
     * @param certNo   租户id
     * @return 结果
     */
    @PostMapping("/per-info")
    public R<?> getPerInfo(String certType, String certNo, String psnCertType) {
        Info1101ReadcardParam readcard = ybDao.getReadCard(certType, certNo);
        Info1101Output perInfo = ybHttpUtils.getPerInfo(readcard);
        if (perInfo != null) {
            ybDao.saveReadcardAndPerinfo(readcard, perInfo);
            PatientInfoDto patient = ybDao.getPatent(perInfo);
            return R.ok(patient);
        } else {
            return R.fail("未查询到患者信息");
        }
    }

    /**
     * 【5301】获取人员慢病
     *
     * @param encounterId 就诊id
     * @return 结果
     */
    @GetMapping("/getConditionDefinition")
    public R<?> getSpecialDia(Long encounterId) {

        Encounter encounter = iEncounterService.getById(encounterId);
        if (encounter == null) {
            throw new ServiceException("未获取到就诊信息");
        }
        Patient patient = patientService.getById(encounter.getPatientId());
        if (patient == null) {
            throw new ServiceException("未获取到患者信息");
        }
        InfoPerson perinfo = iPerinfoService.getPerInfoByIdCard(patient.getIdCard(), SecurityUtils.getLoginUser().getTenantId());
        if (perinfo == null) {
            throw new ServiceException("未连接医保获取患者信息");
        }
        Info1101Output info1101Output = JSON.parseObject(perinfo.getResult1101(), Info1101Output.class);
        List<Info5301SpecialConditionResult> feedetail = info1101Output.getFeedetail();

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Info5301SpecialConditionResult> currentRangeResults = new ArrayList<>();
        for (Info5301SpecialConditionResult info5301SpecialConditionResult : feedetail) {
            try {
                Date begndate = dateFormat.parse(info5301SpecialConditionResult.getBegndate());
                Date enddate = dateFormat.parse(info5301SpecialConditionResult.getEnddate());

                // 检查当前日期是否在 begndate 和 enddate 之间
                if (currentDate.compareTo(begndate) >= 0 && currentDate.compareTo(enddate) <= 0) {
                    currentRangeResults.add(info5301SpecialConditionResult);
                }
            } catch (ParseException e) {
                e.printStackTrace(); // 处理日期解析异常
            }
        }
        List<ConditionDefinition> conditionDefinitions = null;
        if (!currentRangeResults.isEmpty()) {
            conditionDefinitions = iConditionDefinitionService.list(new LambdaUpdateWrapper<ConditionDefinition>().in(ConditionDefinition::getYbNo, currentRangeResults.stream().map(Info5301SpecialConditionResult::getOpspDiseCode).collect(Collectors.toList())));
        }
        return R.ok(conditionDefinitions);
    }

    /**
     * 【3301】目录对照
     *
     * @param tableName 就诊id
     * @param id        付款id
     * @return 结果
     */
    @PostMapping("/directory-cross-check")
    public R<?> directoryCheck(String tableName, Long id) {
        MedicalDirectory3301Param medicalDirectory3301Param = ybDao.getMedicalDirectory3301Param(tableName, id);
        ArrayList<MedicalDirectory3301Param> list = new ArrayList<>();
        list.add(medicalDirectory3301Param);
        MedicalDirectory3301ListParam medicalDirectory3301ListParam = new MedicalDirectory3301ListParam();
        medicalDirectory3301ListParam.setData(list);
        Result resultInfo = ybHttpUtils.directoryCheck(medicalDirectory3301ListParam);
        if (resultInfo.getCode().equals(CommonConstant.SC_OK_200)) {
            ybDao.saveDirectoryHistory(tableName, id, 3301, medicalDirectory3301Param);
        }
        return R.ok();
    }

    /**
     * 【3302】目录对照撤销
     *
     * @param tableName 就诊id
     * @param id        付款id
     * @return 结果
     */
    @PostMapping("/directory-un-check")
    public R<?> directoryUnCheck(String tableName, Long id) {

        MedicalDirectory3302Param medicalDirectory3302Param = ybDao.getMedicalDirectory3302Param(tableName, id);
        Result resultInfo = ybHttpUtils.directoryUnCheck(medicalDirectory3302Param);
        if (resultInfo.getCode().equals(CommonConstant.SC_OK_200)) {
            ybDao.saveDirectoryHistory(tableName, id, 3302, medicalDirectory3302Param);
        }

        return R.ok();
    }

    /**
     * 【3201】对总账
     *
     * @param settlement3201WebParam 3201入参条件
     * @return 结果
     */
    @PostMapping("/reconcile")
    public R<?> reconcileGeneralLedger(@RequestBody Settlement3201WebParam settlement3201WebParam) {
        Financial3201Param financial3201Param = ybDao.getFinancial3201Param(settlement3201WebParam);
        Result result = ybHttpUtils.reconcileGeneralLedger(financial3201Param);
        if(result.getCode().equals(CommonConstant.SC_OK_200)){
            //System.out.println(JSON.parseObject(JSON.toJSONString(result.getResult())));
            Financial3201Output financial3201Output = JSON.parseObject(JSON.toJSONString(result.getResult()), Financial3201Output.class);
            ybDao.saveReconcileGeneralLedger(financial3201Output);
            if("0".equals(financial3201Output.getStmtRslt())){
                return R.ok("OK");
            }
            return R.ok(financial3201Output.getStmtRsltDscr());
        }
        return R.fail("医保app通讯失败");
    }

    /**
     * 【3201】对总账列表
     *
     * @param settlement3201WebParam 3201入参条件
     * @return 结果
     */
    @GetMapping("/reconcile-list")
    public R<?> reconcileGeneralLedgerList(Settlement3201WebParam settlement3201WebParam) {
        return R.ok(ybDao.reconcileGeneralLedgerDetail(settlement3201WebParam));

    }

    /**
     * 【3202】对明细账
     *
     * @param settlement3202WebParam 3202查询数据
     * @return 结果
     */
    @PostMapping("/reconcile-detail-list")
    public R<?> reconcileGeneralLedgerDetail(@RequestBody Settlement3202WebParam settlement3202WebParam) {
        List<Financial3202FileParam> financial3202FileParams = ybDao.paymentCompareYbSettle(settlement3202WebParam);

        //FinancialSettlement3202Param financial3202Param =
        //        ybDao.getFinancialSettlement3202Param(settlement3202WebParam);
        //FinancialSettlement3202Result resultInfo = ybHttpUtils.reconcileGeneralLedgerDetail(financial3202Param);

        return R.ok(financial3202FileParams);
    }

    /**
     * 【3202】生成txt文件
     *
     * @param settlementIdList 3202查询数据
     * @return 结果
     */
    @PostMapping("/reconcile-detail-txt")
    public R<?> reconcileGeneralLedgerDetailTxt(List<String> settlementIdList) throws IOException {
        List<Financial3202FileParam> financial3202FileParams = ybDao.paymentCompareYbSettle(settlementIdList);
        String filePath = SecurityUtils.getLoginUser().getOptionJson().getString("filePath")+new Date().getTime()+".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Financial3202FileParam item : financial3202FileParams) {
                // 假设每个实体都有toString()方法返回逗号分隔的属性
                // 或者你可以自定义获取属性的方式
                String line = item.toString().replace(",", "\t");
                writer.write(line);
                writer.newLine();
            }
        }
        return R.ok("生成txt文件成功，文件路径："+filePath);
    }

    /**
     * 触发3202接口
     * @param settlementIdList 3202查询数据
     * @return
     * @throws IOException
     */
    @PostMapping("/reconcile-general")
    public R<?> reconcileGeneral(List<String> settlementIdList, String filePath, String fileQuryNo, String setlOptins, String clrType) {
        //todo:这里需要考虑3302接口的参数是后台合计还是由前台传入，因为这个参数还要和txt文件对应（既需要txt中的数据还需要db表中的一些数据），
        //String filePath = SecurityUtils.getLoginUser().getOptionJson().getString("filePath")+new Date().getTime()+".txt";
        List<Financial3202FileParam> financial3202FileParamList = new ArrayList<>();
        Resource resource = resourceLoader.getResource(filePath); // 或使用 "file:" 读取绝对路径

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] fields = line.split("\t"); // 按 Tab 分割字段
                    if (fields.length == 7) {
                        BigDecimal medfeeSumamt = new BigDecimal(fields[3]);
                        BigDecimal fundPaySumamt = new BigDecimal(fields[4]);
                        BigDecimal acctPay = new BigDecimal(fields[5]);
                        Financial3202FileParam person = new Financial3202FileParam(fields[0], fields[1], fields[2], medfeeSumamt, fundPaySumamt, acctPay, fields[6]);
                        financial3202FileParamList.add(person);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("IO异常，异常信息："+e.getMessage());
        }

        BigDecimal medfeeSumamt = BigDecimal.ZERO;//医疗费总额
        BigDecimal fundPaySumamt = BigDecimal.ZERO;//基金支付
        //Integer fixmedinsSetlCnt = 0;//结算笔数

        if(financial3202FileParamList.isEmpty()){
            throw new ServiceException("未获取txt文件中实体");
        }
        for (Financial3202FileParam financial3202FileParam : financial3202FileParamList) {
            medfeeSumamt = medfeeSumamt.add(financial3202FileParam.getMedfeeSumamt());
            fundPaySumamt = fundPaySumamt.add(financial3202FileParam.getFundPaySumamt());
        }

        FinancialSettlement3202Param financialSettlement3202Param = ybDao.getFinancialSettlement3202Param(settlementIdList, medfeeSumamt, fundPaySumamt);
        if(financialSettlement3202Param==null){
            throw new ServiceException("未生成3202参数");
        }
        financialSettlement3202Param.setClrType(clrType).setFileQuryNo(fileQuryNo)
                .setRefdSetlFlag(financial3202FileParamList.get(0).getRefdSetlFlag()).setSetlOptins(setlOptins);

        FinancialSettlement3202Result financialSettlement3202Result = ybHttpUtils.reconcileGeneralLedgerDetail(financialSettlement3202Param);
        if(financialSettlement3202Result==null){
            throw new ServiceException("未收到3202参数");
        }

        return R.ok(financialSettlement3202Result);
    }



    /**
     * 【3209A】第三方数据查询
     *
     * @param settlement3209AWebParam 3209A入参条件
     * @return 结果
     */
    @PostMapping("/three-part-search-err")
    public R<?> threePartSearch(Settlement3209AWebParam settlement3209AWebParam) {

        FinancialSettlement3209AParam financialSettlement3209AParam =
                ybDao.getFinancialSettlement3209AParam(settlement3209AWebParam);
        List<FinancialSettlement3209AResult> list = ybHttpUtils.threePartSearch(financialSettlement3209AParam);

        return R.ok();
    }

    /**
     * 【3203A】清算申请(吉林省)
     *
     * @param financial3203AWebParam 3203A入参条件
     * @return 结果
     */
    @PostMapping("/apply-financial-clearing")
    public R<?> applyFinancialClearing(Financial3203AWebParam financial3203AWebParam) {
        Financial3203AParam financial3203AParam = ybDao.getFinancial3203AParam(financial3203AWebParam);
        String s = ybHttpUtils.applyFinancialClearing(financial3203AParam);
        if(!StringUtils.isEmpty(s)){
            Result<?> result = JSON.parseObject(s, Result.class);
            if (result != null) {
                if (result.getCode() == 200) {
                    ybDao.save3203AFinancialClearingApplycation(financial3203AParam, s);
                    return R.ok(result);
                }
            }
        }
        return R.fail(s);
    }

    /**
     * 【3204A】清算申请撤销(吉林省)
     *
     * @param clrAppyEvtId 3203A入参条件
     * @return 结果
     */
    @PostMapping("/apply-un-clearing")
    public R<?> cancelFinancialClearing(String clrAppyEvtId, String clrOptins) {

        Financial3204Param financial3204Param = new Financial3204Param();
        financial3204Param.setClrOptins(clrOptins).setClrAppyEvtId(clrAppyEvtId);
        Result resultInfo = ybHttpUtils.cancelFinancialClearing(financial3204Param);
        if (resultInfo.getCode().equals(CommonConstant.SC_OK_200)) {
            ybDao.save3204AFinancialClearingApplycation(clrAppyEvtId, clrOptins);
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 【3205A】清算申请状态查询
     *
     * @param clearing3205AWebParma 3205A入参条件
     * @return 结果
     */
    @PostMapping("/get-clearing-status")
    public R<?> getFinancialClearingStatus(Clearing3205AWebParma clearing3205AWebParma) {

        Clearing3205AParma clearing3205AParma = ybDao.getClearing3205AParma(clearing3205AWebParma);
        Clearing3205AResult clearing3205AResult = ybHttpUtils.getFinancialClearingStatus(clearing3205AParma);

        return R.ok(clearing3205AResult);
    }

    /**
     * 【3501】商品盘存上传
     *
     * @param id 供应申请id
     * @return 结果
     */
    @PostMapping("/upload-inventory-count")
    public R<?> uploadInventoryCount(Long id) {

        MedicalInventory3501Param medicalInventory3501Param =
                ybDao.getMedicalInventory3501Param(id, SecurityUtils.getLoginUser().getTenantId());
        Result resultInfo = ybHttpUtils.uploadInventoryCount(medicalInventory3501Param);
        if (resultInfo.getCode().equals(CommonConstant.SC_OK_200)) {
            ybDao.saveInventoryCheckRecord(medicalInventory3501Param, resultInfo.getMessage());
            return R.ok();
        }
        ybDao.saveInventoryCheckRecord(medicalInventory3501Param, resultInfo.getMessage());
        return R.fail();
    }

    /**
     * 【3502】商品信息变更
     *
     * @param id         供应申请id
     * @param invChgType 变更类型 参考枚举
     * @return 结果
     */
    @PostMapping("/upload-inventory")
    public R<?> updateInventoryCount(Long id, String invChgType) {

        MedicalInventory3502Param medicalInventory3502Param =
                ybDao.getMedicalInventory3502Param(id, invChgType, SecurityUtils.getLoginUser().getTenantId());
        Result resultInfo = ybHttpUtils.updateInventoryCount(medicalInventory3502Param);
        if (resultInfo.getCode().equals(CommonConstant.SC_OK_200)) {
            ybDao.saveInventoryChangeRecord(medicalInventory3502Param, resultInfo.getMessage());
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 【3503】商品采购
     *
     * @param id 供应申请id
     * @return 结果
     */
    @PostMapping("/procurement")
    public R<?> procurement(Long id) {

        Medical3503Param medical3503Param =
                ybDao.getMedical3503Param(id, SecurityUtils.getLoginUser().getTenantId());
        Result resultInfo = ybHttpUtils.procurement(medical3503Param);
        if (resultInfo.getCode().equals(CommonConstant.SC_OK_200)) {
            ybDao.saveInventoryPurchaseRecord(medical3503Param, resultInfo.getMessage());
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 【3504】商品采购退货
     *
     * @param id 供应申请id
     * @return 结果
     */
    @PostMapping("/procurement-cancel")
    public R<?> cancelProcurement(Long id) {

        MedicalPurchase3504Param medicalPurchase3504Param =
                ybDao.getMedicalPurchase3504Param(id, SecurityUtils.getLoginUser().getTenantId());
        Result resultInfo = ybHttpUtils.cancelProcurement(medicalPurchase3504Param);
        if (resultInfo.getCode().equals(CommonConstant.SC_OK_200)) {
            ybDao.saveInventoryPurchaseReturnRecord(medicalPurchase3504Param, resultInfo.getMessage());
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 【3505】商品销售
     *
     * @param id 供应申请id
     * @return 结果
     */
    @PostMapping("/merchandise")
    public R<?> merchandise(Long id, String ListType) {
        Medical3505Param medical3505Param =
                ybDao.getMedical3505Param(id, ListType, SecurityUtils.getLoginUser().getTenantId());
        Result result = ybHttpUtils.merchandise(medical3505Param);
        Medical3505Result medical3505Result =
                JSON.parseObject(JSON.toJSONString(result.getMessage()), Medical3505Result.class);
        if ("1".equals(medical3505Result.getRetRslt())) {
            ybDao.saveInventorySaleRecord(medical3505Param, medical3505Result.getMsgRslt());
            return R.ok();
        }
        ybDao.saveInventorySaleRecord(medical3505Param, medical3505Result.getMsgRslt());
        return R.fail();
    }

    /**
     * 【3506】商品销售退货
     *
     * @param id 供应申请id
     * @return 结果
     */
    @PostMapping("/cancel-merchandise")
    public R<?> cancelMerchandise(Long id, String ListType) {
        Medical3506Param medical3506Param =
                ybDao.getMedical3506Param(id, ListType, SecurityUtils.getLoginUser().getTenantId());
        Result result = ybHttpUtils.cancelMerchandise(medical3506Param);
        Medical3505Result medical3505Result =
                JSON.parseObject(JSON.toJSONString(result.getMessage()), Medical3505Result.class);
        if ("1".equals(medical3505Result.getRetRslt())) {
            ybDao.saveInventorySaleReturnRecord(medical3506Param, medical3505Result.getMsgRslt());
            return R.ok();
        }
        ybDao.saveInventorySaleReturnRecord(medical3506Param, medical3505Result.getMsgRslt());
        return R.fail();
    }

    /**
     * 【3507】商品信息删除
     *
     * @param id 供应申请id
     * @return 结果
     */
    @PostMapping("/del-goods")
    public R<?> deleteGoodsInfo(Long id, String ListType) {
        Medical3507Param medical3507Param = ybDao.getMedical3507Param(id, ListType);
        Result resultBody = ybHttpUtils.deleteGoodsInfo(medical3507Param);
        if (resultBody.getCode().equals(CommonConstant.SC_OK_200)) {
            ybDao.saveInventoryDelRecord(medical3507Param);
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 【1312】-医保目录信息查询
     *
     * @param hilistCode 医保目录编码
     * @param dateStr    更新时间
     * @return 结果
     */
    @PostMapping("/query-yb-catalogue")
    public R<?> queryYbCatalogue(@RequestParam String hilistCode, @RequestParam String dateStr) {
        Catalogue1312QueryParam catalogue1312QueryParam = new Catalogue1312QueryParam();
        catalogue1312QueryParam.setHilistCode(hilistCode);
        catalogue1312QueryParam.setInsuplcAdmdvs(
                SecurityUtils.getLoginUser().getOptionJson().getString(CommonConstants.Option.INSUPLC_ADMDVS));
        // 直接解析（默认 ISO 格式）
        LocalDate localDate = LocalDate.parse(dateStr);
        // 转换为 Date
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        catalogue1312QueryParam.setUpdtTime(date);
        catalogue1312QueryParam.setPageNum(1);
        catalogue1312QueryParam.setPageSize(10);
        catalogue1312QueryParam.setDecryptFlag("0"); // 不需要解密
        List<Catalogue1312Output> outputList = ybHttpUtils.queryYbCatalogue(catalogue1312QueryParam);
        return R.ok(outputList);
    }

}
