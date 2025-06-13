/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.service.impl;

import static com.alibaba.fastjson.JSON.parseObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.core.common.utils.DateUtils;
import com.core.common.utils.SecurityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openhis.component.HandlerIntDispatcher;
import com.openhis.config.InterfaceConfig;
import com.openhis.config.Tenant;
import com.openhis.constant.ClincStatusConst;
import com.openhis.constant.InputDataTypeConst;
import com.openhis.constant.InterFaceResultConst;
import com.openhis.domain.InfoPerson;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;
import com.openhis.service.YbService;
import com.openhis.utils.RedisUtil;
import com.openhis.vo.*;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;

/**
 * TODO:医保同一服务层
 *
 * @author SunJQ
 * @date 2025-03-19
 */
@Service
public class YbServiceImpl implements YbService {
    /**********************************************************************/
    /**
     * 接口分发器
     */
    @Autowired
    private HandlerIntDispatcher handlerIntDispatcher;
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

    /**********************************************************************/

    /**********************************************************************/
    /**
     * 签到
     *
     * @param baseParam
     * @param request
     * @return
     */
    public Result<?> signIn(BaseParam baseParam, HttpServletRequest request) {
        // BaseInfo baseInfo = baseParam.getBaseInfo();
        // baseInfo.setAdmvs(tenant.getAdmvs()).setFixmedinsCode(tenant.getFixmedinsCode())
        // .setFixmedinsName(tenant.getFixmedinsName());
        Sign1101Param sign = parseObject(JSON.toJSONString(baseParam.getData()), Sign1101Param.class);

        Console.error("AAA:" + sign.getMac() + "|" + sign.getIp() + "|" + sign.getOpterNo());
        // Sign1101Param sign = (Sign1101Param)baseParam.getData();
        // sign.setIp(request.getRemoteAddr());
        ResultBody result = handlerIntDispatcher.dispatcher(ServiceAdrEnum.P9001.toString(),
            new InputData(sign, null, InputDataTypeConst.SIGN_IN), baseParam);
        if ("0".equals(result.getInfcode())) {
            Sign param = parseObject(parseObject(result.getOutput()).getString("signinoutb"), Sign.class);
            param.setOpterNo(sign.getOpterNo());
            param.setStatus("1");
            redisUtil.set("USER_SIGNNO:USER_SIGNNO_" + baseParam.getBaseInfo().getUserId(), param.getSignNo(),
                24 * 3600);
            return Result.ok("签到成功！", param);
        }
        return Result.error(result.getErrMsg());
    }

    /**
     * 签退
     *
     * @param baseParam
     * @return
     */
    public Result<?> signOut(BaseParam baseParam) {
        // Sign signInfo = getById(sign.getId());
        // LoginUser sysUser = loginUserUtil.getLoginUser();
        Long userId = baseParam.getBaseInfo().getUserId();
        Sign signInfo = parseObject(String.valueOf(redisUtil.get("USER_SIGNNO:USER_SIGNNO_" + userId)), Sign.class);
        Sign inputData = new Sign();
        inputData.setOpterNo(signInfo.getOpterNo());
        inputData.setSignNo(signInfo.getSignNo());
        ResultBody result = handlerIntDispatcher.dispatcher(ServiceAdrEnum.P9002.toString(),
            new InputData(inputData, null, InputDataTypeConst.SIGN_OUT), baseParam);
        if ("0".equals(result.getInfcode())) {
            Sign param = parseObject(parseObject(result.getOutput()).getString("signoutoutb"), Sign.class);
            signInfo.setSignOutTime(param.getSignTime());
            signInfo.setStatus("2");
            // updateById(signInfo);
            // LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            redisUtil.del("USER_SIGNNO:USER_SIGNNO_" + userId);
            return Result.ok("签退成功！", signInfo);
        }
        return Result.error(result.getErrMsg());
    }

    /**
     * 读卡获取身份信息
     *
     * @param baseParam
     * @return
     */
    public Result<?> getPerInfo(BaseParam baseParam) {
        BaseInfo baseInfo = baseParam.getBaseInfo();
        // baseInfo.setAdmvs(tenant.getAdmvs()).setFixmedinsCode(tenant.getFixmedinsCode())
        // .setFixmedinsName(tenant.getFixmedinsName());
        ResultBody result = handlerIntDispatcher.dispatcher(ServiceAdrEnum.P1101.toString(),
            new InputData(baseParam.getData(), null, InputDataTypeConst.DATA), baseParam);
        if ("0".equals(result.getInfcode())) {
            JSONObject resultObj = parseObject(result.getOutput());
            InfoPerson perinfo = parseObject(resultObj.getString("baseinfo"), InfoPerson.class);
            JSONArray insuinfo = resultObj.getJSONArray("insuinfo");
            // if (insuinfo.size() == 1) {
            // JSONObject obj = insuinfo.getJSONObject(0);
            // perinfo.setInsuplcAdmdvs(obj.getString("insuplc_admdvs"));
            // perinfo.setInsutype(obj.getString("insutype"));
            // perinfo.setBalc(obj.getString("balc"));
            // return Result.OK("获取成功", perinfo);
            // }
            for (int i = 0, size = insuinfo.size(); i < size; i++) {
                JSONObject obj = insuinfo.getJSONObject(i);
                // 职工
                if ("310".equals(obj.getString("insutype")) && "1".equals(obj.getString("psn_insu_stas"))) {
                    perinfo.setInsuplcAdmdvs(obj.getString("insuplc_admdvs"));
                    perinfo.setInsutype(obj.getString("insutype"));
                    perinfo.setBalc(obj.getString("balc"));
                    perinfo.setCvlservFlag(obj.getString("cvlserv_flag"));
                    perinfo.setPsnType(obj.getString("psn_type"));
                    perinfo.setPsnInsuDate(obj.getString("psn_insu_date"));
                    perinfo.setEmpName(obj.getString("emp_name"));
                }
                // 居民
                if ("390".equals(obj.getString("insutype")) && "1".equals(obj.getString("psn_insu_stas"))) {
                    perinfo.setInsuplcAdmdvs(obj.getString("insuplc_admdvs"));
                    perinfo.setInsutype(obj.getString("insutype"));
                    perinfo.setBalc(obj.getString("balc"));
                    perinfo.setCvlservFlag(obj.getString("cvlserv_flag"));
                    perinfo.setPsnType(obj.getString("psn_type"));
                    perinfo.setPsnInsuDate(obj.getString("psn_insu_date"));
                    perinfo.setEmpName(obj.getString("emp_name"));
                }
            }

            // 查询慢特病信息
            baseParam.getBaseInfo().setInsuplcAdmdvs(perinfo.getInsuplcAdmdvs());
            ResultBody resultBody = handlerIntDispatcher.dispatcher(ServiceAdrEnum.G5301.toString(),
                new InputData(new Info5301Param().setPsnNo(perinfo.getPsnNo()), null, InputDataTypeConst.DATA),
                baseParam);
            if ("0".equals(resultBody.getInfcode())) {
                resultObj = parseObject(resultBody.getOutput());
                JSONArray feedetail = resultObj.getJSONArray("feedetail");

                List<Info5301SpecialConditionResult> list = new ArrayList<>();
                Info5301SpecialConditionResult info5301SpecialConditionResult;
                // 慢性病赋值
                for (int i = 0; i < feedetail.size(); i++) {
                    JSONObject obj = feedetail.getJSONObject(i);
                    info5301SpecialConditionResult = new Info5301SpecialConditionResult();
                    info5301SpecialConditionResult.setOpspDiseName(obj.getString("opsp_dise_name"))
                        .setOpspDiseCode(obj.getString("opsp_dise_code")).setBegndate(obj.getString("begndate"))
                        .setEnddate(obj.getString("enddate"));

                    list.add(info5301SpecialConditionResult);
                }
                perinfo.setFeedetail(list);
            }
            // redisUtil.set(String.format("%s:%s:%s", "PERSON-INFO", readcard.getMdtrtCertType(),
            // readcard.getMdtrtCertNo()), perinfo, 72000);
            System.out.println(JSON.toJSONString(perinfo));
            return Result.ok("获取成功", perinfo);
        }
        return Result.error(result.getErrMsg());
    }

    /**
     * 挂号
     *
     * @param baseParam
     * @return
     */
    public Result<?> reg(BaseParam baseParam) {
        Result result;
        try {
            BaseInfo baseInfo = baseParam.getBaseInfo();
            // baseInfo.setAdmvs(tenant.getAdmvs()).setFixmedinsCode(tenant.getFixmedinsCode())
            // .setFixmedinsName(tenant.getFixmedinsName());
            ClinicReg reg = parseObject(JSON.toJSONString(baseParam.getData()), ClinicReg.class);
            String mdtrtCertNo = reg.getMdtrtCertNo();
            reg.setMdtrtCertNo(reg.getMdtrtCertNo());
            reg.setBegntime(DateUtil.date());
            ResultBody resultBody = handlerIntDispatcher.dispatcher(ServiceAdrEnum.C2201.toString(),
                new InputData(reg, null, InputDataTypeConst.DATA), baseParam);
            if (InterFaceResultConst.SUCCESS.equals(resultBody.getInfcode())) {
                JSONObject resultObj = parseObject(resultBody.getOutput());
                reg.setStatus(ClincStatusConst.STATUS_0);
                reg.setMdtrtCertNo(mdtrtCertNo);
                reg.setMdtrtId(resultObj.getJSONObject("data").getString("mdtrt_id"));
                result = Result.ok(reg);
            } else {
                result = Result.error(resultBody.getErrMsg(), reg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("网络异常");
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> cancelReg(BaseParam baseParam) {
        Result result;
        try {
            BaseInfo baseInfo = baseParam.getBaseInfo();
            // baseInfo.setAdmvs(tenant.getAdmvs()).setFixmedinsCode(tenant.getFixmedinsCode())
            // .setFixmedinsName(tenant.getFixmedinsName());
            ClinicReg reg = parseObject(JSON.toJSONString(baseParam.getData()), ClinicReg.class);
            ClinicReg transReg = new ClinicReg();
            transReg.setPsnNo(reg.getPsnNo());
            transReg.setMdtrtId(reg.getMdtrtId());
            transReg.setIptOtpNo(reg.getIptOtpNo());
            ResultBody resultBody = handlerIntDispatcher.dispatcher(ServiceAdrEnum.C2202.toString(),
                new InputData(transReg, null, InputDataTypeConst.DATA), baseParam);
            if (InterFaceResultConst.SUCCESS.equals(resultBody.getInfcode())) {
                reg.setStatus(ClincStatusConst.CANCLE);
                // regService.updateById(reg);
                result = Result.ok(transReg);
            } else {
                result = Result.error(resultBody.getErrMsg(), reg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("网络异常");
        }
        return result;
    }

    /**
     * 门诊就诊信息上传
     *
     * @param baseParam
     * @return
     */
    @Override
    public Result<?> saveMedical(BaseParam baseParam) {
        Result result;
        BaseInfo baseInfo = baseParam.getBaseInfo();
        // baseInfo.setAdmvs(tenant.getAdmvs()).setFixmedinsCode(tenant.getFixmedinsCode())
        // .setFixmedinsName(tenant.getFixmedinsName());
        Clinic2203MedicalParam medical =
            parseObject(JSON.toJSONString(baseParam.getData()), Clinic2203MedicalParam.class);
        try {
            List<Clinic2203DiseInfoParam> diseinfoList = medical.getDiseinfoList();
            medical.setBegntime(DateUtil.date());
            if (diseinfoList != null) {
                diseinfoList.stream().forEach(diseInfo -> diseInfo.setDiagTime(DateUtil.date()));
            } else {
                // throw new ServiceException("诊断节点列表不能为空");
                result = Result.error("诊断节点列表不能为空");
            }
            ResultBody resultBody = handlerIntDispatcher.dispatcher(ServiceAdrEnum.C2203.toString(),
                new InputData(medical, diseinfoList, medical.getInsuplcAdmdvs(), InputDataTypeConst.MD_TRT_INFO),
                baseParam);
            if (InterFaceResultConst.SUCCESS.equals(resultBody.getInfcode())) {
                result = Result.ok(resultBody);
            } else {
                result = Result.error(resultBody.getErrMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("网络异常");
        }
        return result;
    }

    /**
     * 门诊费用明细上传
     *
     * @param baseParam
     * @return
     */
    public Result<?> upFeeDetail(BaseParam baseParam) {
        Result result;
        BaseInfo baseInfo = baseParam.getBaseInfo();
        // baseInfo.setAdmvs(tenant.getAdmvs()).setFixmedinsCode(tenant.getFixmedinsCode())
        // .setFixmedinsName(tenant.getFixmedinsName());
        Clinic2204OrderParam clinic2204OrderParam =
            parseObject(JSON.toJSONString(baseParam.getData()), Clinic2204OrderParam.class);
        try {
            ResultBody resultBody = handlerIntDispatcher.dispatcher(ServiceAdrEnum.C2204.toString(),
                new InputData(clinic2204OrderParam.getFeedetail(), clinic2204OrderParam.getInsuplcAdmdvs(),
                    InputDataTypeConst.FEE_DETAIL),
                baseParam);
            if (InterFaceResultConst.SUCCESS.equals(resultBody.getInfcode())) {
                // 将返回结果解析成JSON
                JSONObject resultObj = parseObject(resultBody.getOutput());
                List<Clinic2204FeeDetailResult> feedetails =
                    JSONObject.parseArray(resultObj.getString("result"), Clinic2204FeeDetailResult.class);
                result = Result.ok(new Clinic2204OrderResult().setResult(feedetails));
            } else {
                result = Result.error(resultBody.getErrMsg(), new Clinic2204OrderResult());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("网络异常");
        }
        return result;
    }

    /**
     * 门诊费用明细上传
     *
     * @return
     */
    public Result<?> cancelFeeDetail(BaseParam baseParam) {
        Result result;
        try {
            // 调用接口
            ResultBody resultBody = handlerIntDispatcher.dispatcher(ServiceAdrEnum.C2205.toString(),
                new InputData(baseParam.getData(), baseParam.getBaseInfo().getInsuplcAdmdvs(), InputDataTypeConst.DATA),
                baseParam);
            if (InterFaceResultConst.SUCCESS.equals(resultBody.getInfcode())) {
                result = Result.ok(resultBody);
            } else {
                result = Result.error(resultBody.getErrMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("网络异常");
        }
        return result;
    }

    /**
     * 预结算
     *
     * @param baseParam
     * @return
     */
    public Result<?> preSetl(BaseParam baseParam) {
        Result result;
        BaseInfo baseInfo = baseParam.getBaseInfo();
        // baseInfo.setAdmvs(tenant.getAdmvs()).setFixmedinsCode(tenant.getFixmedinsCode())
        // .setFixmedinsName(tenant.getFixmedinsName());
        Clinic2206OrderParam clinic2206OrderParam =
            parseObject(JSON.toJSONString(baseParam.getData()), Clinic2206OrderParam.class);
        Clinic2206OrderResult clinic2206OrderResult = new Clinic2206OrderResult();
        try {
            String mdtrtCertNo = clinic2206OrderParam.getMdtrtCertNo();
            clinic2206OrderResult.setMdtrtCertNo(clinic2206OrderParam.getMdtrtCertNoEncrypt());
            // 调用接口
            ResultBody resultBody = handlerIntDispatcher.dispatcher(ServiceAdrEnum.C2206.toString(),
                new InputData(clinic2206OrderParam, clinic2206OrderParam.getInsuplcAdmdvs(), InputDataTypeConst.DATA),
                baseParam);
            if (InterFaceResultConst.SUCCESS.equals(resultBody.getInfcode())) {
                // 将返回结果解析成JSON
                JSONObject resultObj = parseObject(resultBody.getOutput());
                // 解析结算信息
                clinic2206OrderResult = parseObject(resultObj.getString("setlinfo"), Clinic2206OrderResult.class);
                // 结算基金分项信息
                List<Clinic2206FundPaymentResult> clincSetldetailList =
                    JSONObject.parseArray(resultObj.getString("setldetail"), Clinic2206FundPaymentResult.class);

                clinic2206OrderResult.setMdtrtCertNo(mdtrtCertNo);

                clinic2206OrderResult.setSetldetail(clincSetldetailList);

                result = Result.ok(clinic2206OrderResult);
            } else {
                result = Result.error(resultBody.getErrMsg(), clinic2206OrderResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("网络异常");
        }
        return result;
    }

    /**
     * 门诊费用结算
     *
     * @param baseParam
     * @return
     */
    public Result<?> setl(BaseParam baseParam) {
        Result result;
        BaseInfo baseInfo = baseParam.getBaseInfo();
        // baseInfo.setAdmvs(tenant.getAdmvs()).setFixmedinsCode(tenant.getFixmedinsCode())
        // .setFixmedinsName(tenant.getFixmedinsName());
        Clinic2206OrderParam clincOrder =
            parseObject(JSON.toJSONString(baseParam.getData()), Clinic2206OrderParam.class);

        try {
            // 调用接口
            ResultBody resultBody = handlerIntDispatcher.dispatcher(ServiceAdrEnum.C2207.toString(),
                new InputData(clincOrder, clincOrder.getInsuplcAdmdvs(), InputDataTypeConst.DATA), baseParam);
            if (InterFaceResultConst.SUCCESS.equals(resultBody.getInfcode())) {
                // 将返回结果解析成JSON
                JSONObject resultObj = parseObject(resultBody.getOutput());
                // 解析结算信息
                Clinic2207OrderResult clincSetlinfo =
                    parseObject(resultObj.getString("setlinfo"), Clinic2207OrderResult.class);
                // 结算基金分项信息
                List<Clinic2206FundPaymentResult> clincSetldetailList =
                    JSONObject.parseArray(resultObj.getString("setldetail"), Clinic2206FundPaymentResult.class);
                clincSetlinfo.setSetldetail(clincSetldetailList);

                result = Result.ok(clincSetlinfo);
            } else {
                result = Result.error(resultBody.getErrMsg(), new Clinic2206OrderResult());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("网络异常");
        }
        return result;
    }

    /**
     * 门诊费用撤销
     *
     * @param baseParam
     * @return
     */
    public Result<?> cancelSetl(BaseParam baseParam) {
        Result result;
        BaseInfo baseInfo = baseParam.getBaseInfo();
        // baseInfo.setAdmvs(tenant.getAdmvs()).setFixmedinsCode(tenant.getFixmedinsCode())
        // .setFixmedinsName(tenant.getFixmedinsName());
        Clinic2208UnSetlInfoParam setlinfo =
            parseObject(JSON.toJSONString(baseParam.getData()), Clinic2208UnSetlInfoParam.class);

        try {
            // 调用接口
            ResultBody resultBody = handlerIntDispatcher.dispatcher(ServiceAdrEnum.C2208.toString(),
                new InputData(setlinfo, baseInfo.getInsuplcAdmdvs(), InputDataTypeConst.DATA), baseParam);
            if (InterFaceResultConst.SUCCESS.equals(resultBody.getInfcode())) {

                // 将返回结果解析成JSON
                JSONObject resultObj = parseObject(resultBody.getOutput());
                // 解析结算信息
                Clinic2208UnSetlInfoResult clincSetlinfo =
                    parseObject(resultObj.getString("setlinfo"), Clinic2208UnSetlInfoResult.class);
                // 结算基金分项信息
                List<Clinic2206FundPaymentResult> clincSetldetailList =
                    JSONObject.parseArray(resultObj.getString("setldetail"), Clinic2206FundPaymentResult.class);
                // 保存结算信息和结算基金分项信息
                clincSetlinfo.setSetldetail(clincSetldetailList);

                result = Result.ok(clincSetlinfo);
            } else {
                result = Result.error(resultBody.getErrMsg(), new Clinic2208UnSetlInfoResult());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("网络异常");
        }
        return result;
    }

    /**
     * 下载目录
     * 
     * @param fileDownload
     * @return
     */
    @Override
    public Result<?> download(BaseParam baseParam, FileDownload fileDownload) {
        Result result;
        try {
            ServiceAdrEnum anEnum = ServiceAdrEnum.getEnum(fileDownload.getCode());
            ResultBody resultBody = handlerIntDispatcher.dispatcher(anEnum.toString(),
                new InputData(fileDownload.getVersion(), null, InputDataTypeConst.DATA), baseParam);
            if (InterFaceResultConst.SUCCESS.equals(resultBody.getInfcode())) {
                result = Result.ok("下载成功");
            }
            result = Result.error("下载失败");
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("网络异常");
        }
        return result;
    }

    /**
     * 诊断及费用明细上传
     * 
     * @param baseParam
     * @return
     */
    @Override
    public Result<?> preSettle(BaseParam baseParam) {
        Result result;
        HashMap hashMap = new HashMap();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            hashMap = objectMapper.readValue(String.valueOf(baseParam.getData()), HashMap.class);
            // 处理obj
        } catch (IOException e) {
            System.err.println("JSON解析错误: " + e.getMessage());
            e.printStackTrace();
        }

        BaseInfo baseInfo = baseParam.getBaseInfo();
        // baseInfo.setAdmvs(tenant.getAdmvs()).setFixmedinsCode(tenant.getFixmedinsCode())
        // .setFixmedinsName(tenant.getFixmedinsName());
        Clinic2203MedicalParam p2203 =
            parseObject(JSON.toJSONString(String.valueOf(hashMap.get("P2203"))), Clinic2203MedicalParam.class);
        Clinic2204OrderParam p2204 =
            parseObject(JSON.toJSONString(String.valueOf(hashMap.get("P2204"))), Clinic2204OrderParam.class);
        try {
            List<Clinic2203DiseInfoParam> diseinfoList = p2203.getDiseinfoList();
            diseinfoList.stream().forEach(diseInfo -> diseInfo.setDiagTime(DateUtil.date()));
            ResultBody resultBody = handlerIntDispatcher.dispatcher(ServiceAdrEnum.C2203.toString(),
                new InputData(p2203, diseinfoList, p2203.getInsuplcAdmdvs(), InputDataTypeConst.MD_TRT_INFO),
                baseParam);
            if (InterFaceResultConst.SUCCESS.equals(resultBody.getInfcode())) {
                ResultBody result2204 = handlerIntDispatcher.dispatcher(ServiceAdrEnum.C2204.toString(),
                    new InputData(p2204, p2204.getInsuplcAdmdvs(), InputDataTypeConst.FEE_DETAIL), baseParam);
                if (InterFaceResultConst.SUCCESS.equals(resultBody.getInfcode())) {
                    // 将返回结果解析成JSON
                    JSONObject resultObj = parseObject(result2204.getOutput());
                    // 解析结算信息
                    Clinic2204OrderResult clinic2204OrderResult =
                        parseObject(resultObj.getString("result"), Clinic2204OrderResult.class);
                    result = Result.ok(clinic2204OrderResult);
                } else {
                    result = Result.error("诊断信息上传失败");
                }
            } else {
                result = Result.error("就诊信息上传失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("网络异常");
        }
        return result;
    }

    /**
     * 目录对照
     * 
     * @param baseParam
     * @return
     */
    @Override
    public Result<?> directoryCheck(BaseParam baseParam) {
        Result result;
        try {
            BaseInfo baseInfo = baseParam.getBaseInfo();
            // baseInfo.setAdmvs(tenant.getAdmvs()).setFixmedinsCode(tenant.getFixmedinsCode())
            // .setFixmedinsName(tenant.getFixmedinsName());

            MedicalDirectory3301ListParam medicalDirectory3301ListParam =
                JSON.parseObject(JSON.toJSONString(baseParam.getData()), MedicalDirectory3301ListParam.class);
            // MedicalDirectory3301Param medicalDirectory3301Param =
            // parseObject(JSON.toJSONString(baseParam.getData()), MedicalDirectory3301Param.class);
            ResultBody resultInfo = handlerIntDispatcher.dispatcher(ServiceAdrEnum.P3301.toString(),
                new InputData(medicalDirectory3301ListParam.getData(), null, InputDataTypeConst.DATA), baseParam);
            if (InterFaceResultConst.SUCCESS.equals(resultInfo.getInfcode())) {
                result = Result.ok(resultInfo);
            } else {
                result = Result.error(resultInfo.getErrMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("网络异常");
        }
        return result;
    }

    /**
     * 目录对照
     *
     * @param baseParam
     * @return
     */
    @Override
    public Result<?> directoryUnCheck(BaseParam baseParam) {
        Result result;
        try {
            BaseInfo baseInfo = baseParam.getBaseInfo();
            // baseInfo.setAdmvs(tenant.getAdmvs()).setFixmedinsCode(tenant.getFixmedinsCode())
            // .setFixmedinsName(tenant.getFixmedinsName());
            MedicalDirectory3302ParamDto medicalDirectory3302ParamDto =
                parseObject(JSON.toJSONString(baseParam.getData()), MedicalDirectory3302ParamDto.class);
            MedicalDirectory3302Param medicalDirectory3302Param = new MedicalDirectory3302Param();
            medicalDirectory3302Param.setFixmedinsCode(medicalDirectory3302ParamDto.getFixmedinsCode())
                    .setFixmedinsHilistId(medicalDirectory3302ParamDto.getFixmedinsHilistId())
                    .setListType(medicalDirectory3302ParamDto.getListType()).setMedListCodg(medicalDirectory3302ParamDto.getMedListCodg());
            ResultBody resultInfo = handlerIntDispatcher.dispatcher(ServiceAdrEnum.P3302.toString(),
                new InputData(medicalDirectory3302ParamDto, null, InputDataTypeConst.DATA), baseParam);
            if (InterFaceResultConst.SUCCESS.equals(resultInfo.getInfcode())) {
                result = Result.ok(resultInfo);
            } else {
                result = Result.error(resultInfo.getErrMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("网络异常");
        }
        return result;
    }

    /**
     * 结算对总账
     *
     * @param baseParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<?> reconcileGeneralLedger(BaseParam baseParam) {
        Result result;
        Financial3201Output financial3201Output = new Financial3201Output();
        try {
            //com.core.common.core.domain.model.LoginUser loginUser = SecurityUtils.getLoginUser();
            // LoginUser sysUser = loginUserUtil.getLoginUser();
            // reconciliation.setFixmedinsCode(sysUser.getFixmedinsCode());
            // reconciliation.setFixmedinsName(sysUser.getFixmedinsName());
            // reconciliation.setAdmvs(sysUser.getAdmvs());
            // reconciliation.setSetlOptins(sysUser.getAdmvs());
            Financial3201Param financial3201Param = parseObject(JSON.toJSONString(baseParam.getData()), Financial3201Param.class);
            // 调用接口
            ResultBody resultBody = handlerIntDispatcher.dispatcher(ServiceAdrEnum.R3201.toString(),
                new InputData(financial3201Param, financial3201Param.getAdmvs(), InputDataTypeConst.DATA), baseParam);
            if (InterFaceResultConst.SUCCESS.equals(resultBody.getInfcode())) {
                JSONObject resultObj = JSONObject.parseObject(resultBody.getOutput());
                //reconciliation.setCreateTime(new Date());
                financial3201Output.setSetlOptins(financial3201Param.getSetlOptins());
                financial3201Output.setStmtRslt(resultObj.getJSONObject("stmtinfo").getString("stmt_rslt"));
                financial3201Output.setStmtRsltDscr(resultObj.getJSONObject("stmtinfo").getString("stmt_rslt_dscr"));
                result = Result.ok(financial3201Output);
            } else {
                result = Result.error(resultBody.getErrMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("网络异常");
        }
        return result;
    }

    @Override
    public Result<?> reconcileGeneralLedgerDetail(BaseParam baseParam,
        FinancialSettlement3202Param financial3202Param) {
        Result result;
        try {
            ResultBody resultBody = handlerIntDispatcher.dispatcher(ServiceAdrEnum.R3202.toString(),
                new InputData(financial3202Param, financial3202Param.getSetlOptins(), InputDataTypeConst.DATA),
                baseParam);
            if (InterFaceResultConst.SUCCESS.equals(resultBody.getInfcode())) {
                JSONObject resultObj = JSONObject.parseObject(resultBody.getOutput());
                FinancialSettlement3202Result financialSettlement3202Result = new FinancialSettlement3202Result();
                financialSettlement3202Result.setFilename(resultObj.getJSONObject("fileinfo").getString("filename"));
                financialSettlement3202Result
                    .setFileQuryNo(resultObj.getJSONObject("fileinfo").getString("file_qury_no"));
                financialSettlement3202Result.setDldEndtime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss",
                    resultObj.getJSONObject("fileinfo").getString("dld_endtime")));

                result = Result.ok(financialSettlement3202Result);
            } else {
                result = Result.error(resultBody.getErrMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("网络异常");
        }
        return result;
    }

    @Override
    public Result<?> threePartSearch(BaseParam baseParam, FinancialSettlement3209AParam financialSettlement3209AParam) {
        Result result;
        try {
            ResultBody resultBody = handlerIntDispatcher.dispatcher(ServiceAdrEnum.R3202.toString(),
                new InputData(financialSettlement3209AParam, financialSettlement3209AParam.getClrOptins(),
                    InputDataTypeConst.DATA),
                baseParam);
            if (InterFaceResultConst.SUCCESS.equals(resultBody.getInfcode())) {
                JSONObject resultObj = JSONObject.parseObject(resultBody.getOutput());
                result = Result.ok(resultObj.getJSONObject("selt_list"));
            } else {
                result = Result.error(resultBody.getErrMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("网络异常");
        }
        return result;
    }

    @Override
    public Result<?> applyFinancialClearing(BaseParam baseParam, Financial3203AParam financial3203AParam) {
        Result result;
        try {
            ResultBody resultBody = handlerIntDispatcher.dispatcher(ServiceAdrEnum.R3203A.toString(),
                new InputData(financial3203AParam, financial3203AParam.getClrOptins(), InputDataTypeConst.DATA),
                baseParam);
            if (InterFaceResultConst.SUCCESS.equals(resultBody.getInfcode())) {
                JSONObject resultObj = JSONObject.parseObject(resultBody.getOutput());
                result = Result.ok(resultObj.getJSONObject("clr_appy_evt_id"));
            } else {
                result = Result.error(resultBody.getErrMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("网络异常");
        }
        return result;
    }

    @Override
    public Result<?> cancelFinancialClearing(BaseParam baseParam, Financial3204Param financial3204Param) {
        Result result;
        try {
            ResultBody resultBody = handlerIntDispatcher.dispatcher(ServiceAdrEnum.R3204A.toString(),
                new InputData(financial3204Param, financial3204Param.getClrOptins(), InputDataTypeConst.DATA),
                baseParam);
            if (InterFaceResultConst.SUCCESS.equals(resultBody.getInfcode())) {
                // JSONObject resultObj = JSONObject.parseObject(resultBody.getOutput());
                result = Result.ok(resultBody);
            } else {
                result = Result.error(resultBody.getErrMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("网络异常");
        }
        return result;
    }

    @Override
    public Result<?> getFinancialClearingStatus(BaseParam baseParam, Clearing3205AParma clearing3205AParma) {
        Result result;
        try {
            ResultBody resultBody = handlerIntDispatcher.dispatcher(ServiceAdrEnum.R3205A.toString(),
                new InputData(clearing3205AParma, clearing3205AParma.getClrOptins(), InputDataTypeConst.DATA),
                baseParam);
            if (InterFaceResultConst.SUCCESS.equals(resultBody.getInfcode())) {
                JSONObject resultObj = JSONObject.parseObject(resultBody.getOutput());
                result = Result.ok(resultObj.getJSONObject("clr_appys"));
            } else {
                result = Result.error(resultBody.getErrMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("网络异常");
        }
        return result;
    }

    @Override
    public Result<?> uploadInventoryCount(BaseParam baseParam) {
        Result result;
        try {
            ResultBody resultBody = handlerIntDispatcher.dispatcher(ServiceAdrEnum.I3501.toString(),
                new InputData(baseParam.getData(), null, InputDataTypeConst.INV_INFO), baseParam);
            if (InterFaceResultConst.SUCCESS.equals(resultBody.getInfcode())) {
                // JSONObject resultObj = JSONObject.parseObject(resultBody.getOutput());
                result = Result.ok(resultBody);
            } else {
                result = Result.error(resultBody.getErrMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("网络异常");
        }
        return result;
    }

    @Override
    public Result<?> updateInventoryCount(BaseParam baseParam) {
        Result result;
        try {
            ResultBody resultBody = handlerIntDispatcher.dispatcher(ServiceAdrEnum.I3502.toString(),
                new InputData(baseParam.getData(), null, InputDataTypeConst.INV_INFO), baseParam);
            if (InterFaceResultConst.SUCCESS.equals(resultBody.getInfcode())) {
                // JSONObject resultObj = JSONObject.parseObject(resultBody.getOutput());
                result = Result.ok(resultBody);
            } else {
                result = Result.error(resultBody.getErrMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("网络异常");
        }
        return result;
    }

    @Override
    public Result<?> procurement(BaseParam baseParam) {
        Result result;
        try {
            ResultBody resultBody = handlerIntDispatcher.dispatcher(ServiceAdrEnum.I3503.toString(),
                new InputData(baseParam.getData(), null, InputDataTypeConst.PURC_INFO), baseParam);
            if (InterFaceResultConst.SUCCESS.equals(resultBody.getInfcode())) {
                // JSONObject resultObj = JSONObject.parseObject(resultBody.getOutput());
                result = Result.ok(resultBody);
            } else {
                result = Result.error(resultBody.getErrMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("网络异常");
        }
        return result;
    }

    @Override
    public Result<?> cancelProcurement(BaseParam baseParam) {
        Result result;
        try {
            ResultBody resultBody = handlerIntDispatcher.dispatcher(ServiceAdrEnum.I3504.toString(),
                new InputData(baseParam.getData(), null, InputDataTypeConst.PURC_INFO), baseParam);
            if (InterFaceResultConst.SUCCESS.equals(resultBody.getInfcode())) {
                // JSONObject resultObj = JSONObject.parseObject(resultBody.getOutput());
                result = Result.ok(resultBody);
            } else {
                result = Result.error(resultBody.getErrMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("网络异常");
        }
        return result;
    }

    @Override
    public Result<?> merchandise(BaseParam baseParam) {
        Result result = null;
        try {
            ResultBody resultBody = handlerIntDispatcher.dispatcher(ServiceAdrEnum.I3505.toString(),
                new InputData(baseParam.getData(), null, InputDataTypeConst.SEL_INFO), baseParam);
            if (InterFaceResultConst.SUCCESS.equals(resultBody.getInfcode())) {
                JSONObject resultObj = JSONObject.parseObject(resultBody.getOutput());
                Medical3505Result medical3505Result = new Medical3505Result();
                if (resultObj.get("msgRslt") != null && resultObj.get("retRslt") != null) {
                    medical3505Result.setMsgRslt(resultObj.get("msgRslt").toString());
                    medical3505Result.setRetRslt(resultObj.get("retRslt").toString());
                } else {
                    medical3505Result.setMsgRslt("成功");
                    medical3505Result.setRetRslt("1");
                }
                result = Result.ok(medical3505Result);
            } else if (InterFaceResultConst.FAILED.equals(resultBody.getInfcode())) {
                Medical3505Result medical3505Result = new Medical3505Result();
                medical3505Result.setRetRslt("-1");
                medical3505Result.setMsgRslt(resultBody.getErrMsg());
                result = Result.ok(medical3505Result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("网络异常");
        }
        return result;
    }

    @Override
    public Result<?> cancelMerchandise(BaseParam baseParam) {
        Result result = null;
        try {
            ResultBody resultBody = handlerIntDispatcher.dispatcher(ServiceAdrEnum.I3506.toString(),
                new InputData(baseParam.getData(), null, InputDataTypeConst.SEL_INFO), baseParam);
            if (InterFaceResultConst.SUCCESS.equals(resultBody.getInfcode())) {
                JSONObject resultObj = JSONObject.parseObject(resultBody.getOutput());
                Medical3505Result medical3505Result = new Medical3505Result();
                if (resultObj.get("msgRslt") != null && resultObj.get("retRslt") != null) {
                    medical3505Result.setMsgRslt(resultObj.get("msgRslt").toString());
                    medical3505Result.setRetRslt(resultObj.get("retRslt").toString());
                } else {
                    medical3505Result.setMsgRslt("成功");
                    medical3505Result.setRetRslt("1");
                }
                result = Result.ok(medical3505Result);
            } else if (InterFaceResultConst.FAILED.equals(resultBody.getInfcode())) {
                Medical3505Result medical3505Result = new Medical3505Result();
                medical3505Result.setRetRslt("-1");
                medical3505Result.setMsgRslt(resultBody.getErrMsg());
                result = Result.ok(medical3505Result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("网络异常");
        }
        return result;
    }

    @Override
    public Result<?> deleteGoodsInfo(BaseParam baseParam, Medical3507Param medical3507Param) {
        Result result;
        try {
            ResultBody resultBody = handlerIntDispatcher.dispatcher(ServiceAdrEnum.I3507.toString(),
                new InputData(medical3507Param, null, InputDataTypeConst.DATA), baseParam);
            if (InterFaceResultConst.SUCCESS.equals(resultBody.getInfcode())) {
                result = Result.ok(resultBody);
            } else {
                result = Result.error(resultBody.getErrMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("网络异常");
        }
        return result;
    }

    @Override
    public Result<?> queryYbCatalogue(BaseParam baseParam) {
        BaseInfo baseInfo = baseParam.getBaseInfo();
        // baseInfo.setAdmvs(tenant.getAdmvs()).setFixmedinsCode(tenant.getFixmedinsCode())
        // .setFixmedinsName(tenant.getFixmedinsName());
        ResultBody result = handlerIntDispatcher.dispatcher(ServiceAdrEnum.D1312.toString(),
            new InputData(baseParam.getData(), null, InputDataTypeConst.DATA), baseParam);
        // System.out.println("---------1312result---------------" + result);
        String data = "";
        if ("0".equals(result.getInfcode())) {
            JSONObject resultObj = parseObject(result.getOutput());
            data = resultObj.getString("data");
        }
        return Result.ok("获取成功", data);
    }
}
