package com.openhis.service.impl;

import static com.alibaba.fastjson.JSON.parseObject;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.openhis.vo.BaseParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.openhis.config.InterfaceConfig;
import com.openhis.config.Tenant;
import com.openhis.enums.ServiceAdrEnum;
import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;
import com.openhis.pojo.TransBody;
import com.openhis.service.HandlerService;
import com.openhis.utils.BusnessIdUtil;
import com.openhis.utils.EasyGmUtils;
import com.openhis.utils.HttpClientUtil;
import com.openhis.utils.RedisUtil;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 医保交易
 */
@Component
@Slf4j
public abstract class HandlerServiceImpl implements HandlerService {

    /**
     * 中心接口配置
     */
    @Autowired
    protected InterfaceConfig interfaceConfig;
    /**
     * redis工具
     */
    @Autowired
    private RedisUtil redisUtil;
    /**
     * 多租户服务
     */
    @Autowired
    private Tenant tenant;
    @Autowired
    private BusnessIdUtil busnessIdUtil;

    Logger logger = LoggerFactory.getLogger(HandlerServiceImpl.class);

    @Override
    public abstract ResultBody handle(InputData inputData, BaseParam baseParam);


    @Override
    public String initTransBody(String intNum, InputData inputData, BaseParam baseParam) {
        // LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        // com.openhis.vo.LoginUser sysUser = loginUserUtil.getLoginUser();
        // 从缓存中获取签到编号
        String signNo = "";
        if (redisUtil.hasKey("USER_SIGNNO:USER_SIGNNO_" + baseParam.getBaseInfo().getUserId())) {
            signNo = redisUtil.get("USER_SIGNNO:USER_SIGNNO_" + baseParam.getBaseInfo().getUserId()).toString();
        }
        if("9001".equals(intNum)){
            signNo = "";
        }
        logger.info("【" + intNum + "】加密前的input：====" + JSONObject.toJSONString(inputData));
        System.out.println("【" + intNum + "】加密前的input：====" + JSONObject.toJSONString(inputData));

        TransBody transBody = TransBody.builder().infno(intNum)
            .msgid(busnessIdUtil.getMsgid(baseParam.getBaseInfo().getFixmedinsCode()))
            .mdtrtareaAdmvs(baseParam.getBaseInfo().getAdmvs())
            .insuplcAdmdvs(intNum.equals("9001") || intNum.equals("1101") || intNum.equals("3301") || intNum.equals("3302")  ? baseParam.getBaseInfo().getAdmvs()
                : baseParam.getBaseInfo().getInsuplcAdmdvs())
            .recerSysCode("sys01").devNo("").devSafeInfo("")
            .cainfo(interfaceConfig.getIsEncrypt() ? sign(JSONObject.toJSONString(inputData)) : "")
            .signtype(interfaceConfig.getIsEncrypt() ? "SM2" : "").infver("V1.0").opterType("1").encType("SM4")
            .opter(baseParam.getBaseInfo().getUserId().toString()).opterName(baseParam.getBaseInfo().getRealname())
            .infTime(DateUtil.now()).fixmedinsCode(baseParam.getBaseInfo().getFixmedinsCode())
            .fixmedinsName(baseParam.getBaseInfo().getFixmedinsName()).signNo(signNo)
            // .fixmedinsName(baseParam.getBaseInfo().getFixmedinsName()).signNo("220000202505210958500117447359")
            .input(interfaceConfig.getIsEncrypt() ? encrypt(JSONObject.toJSONString(inputData)) : inputData).build();
        System.out.println("【" + intNum + "】" + " 整体入参：====" + JSONObject.toJSONString(transBody));
        logger.info("【" + intNum + "】 整体入参：====" + JSONObject.toJSONString(transBody));
        return JSONObject.toJSONString(transBody);
    }


    @Override
    public Map<String, String> getHeader(BaseParam baseParam) {
        Map<String, String> header = new HashMap();
        header.put("Authorization", "Bearer " + this.getToken(baseParam));
        header.put(HttpHeaders.CONTENT_TYPE, "application/json");
        return header;
    }


    @Override
    public ResultBody sendDatas(String url, String transBody, BaseParam baseParam) {
        String result =
            HttpRequest.post(url).addHeaders(getHeader(baseParam)).body(transBody).timeout(20000).execute().body();
        System.out.println("解密之前医保返回出参：====" + result);
        logger.info("解密之前医保返回出参：====" + result);
        ResultBody resultBody = JSONObject.parseObject(result, ResultBody.class);
        // 判断返回值是否需要解密
        String decryptFlag = baseParam.getBaseInfo().getDecryptFlag();
        if (interfaceConfig.getIsEncrypt()&&"1".equals(decryptFlag)) {
            decrypt(resultBody);
        }
        System.out.println("解密之后出参：====" + resultBody);
        logger.info("解密之后出参：====" + resultBody);
        return resultBody;
    }


    @Override
    public String getToken(BaseParam baseParam) {
        String accessToken = "";
        Long userId = baseParam.getBaseInfo().getUserId();
        if (redisUtil.hasKey("USER_TOKEN:USER_TOKEN_" + userId)) {
            // 获取缓存
            accessToken = String.valueOf(redisUtil.get("USER_TOKEN:USER_TOKEN_" + userId));
        } else {
            Map parmap = new HashMap<String, String>();
            //parmap.put("username", tenant.getUsername());
            //parmap.put("password", tenant.getPassword());
            //parmap.put("scope", tenant.getScope());
            //parmap.put("grant_type", tenant.getGrantType());
            parmap.put("username", baseParam.getBaseInfo().getYbUsername());
            parmap.put("password", baseParam.getBaseInfo().getYbPassword());
            parmap.put("scope", baseParam.getBaseInfo().getYbScope());
            parmap.put("grant_type", baseParam.getBaseInfo().getYbGrantType());
            System.out.println(parmap);
            Map headermap = new HashMap<String, String>();
            //headermap.put("Authorization", "Basic " + Base64.getEncoder().encodeToString(
            //    (tenant.getClientId() + ":" + tenant.getClientSecret()).getBytes(StandardCharsets.UTF_8)));
            headermap.put("Authorization", "Basic " + Base64.getEncoder().encodeToString(
                    (baseParam.getBaseInfo().getYbClientId() + ":" + baseParam.getBaseInfo().getYbClientSecret()).getBytes(StandardCharsets.UTF_8)));
            headermap.put(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
            System.out.println(interfaceConfig.getAddress() + ServiceAdrEnum.TOKEN.getAddress());
            String result = HttpClientUtil.sendIOPost(interfaceConfig.getAddress() + ServiceAdrEnum.TOKEN.getAddress(),
                parmap, headermap, "utf8", null);
            System.out.println(result);
            accessToken = JSON.parseObject(result).getString("access_token");
            // 数据插入缓存（set中的参数含义：key值，user对象，缓存存在时间10（long类型），时间单位）
            redisUtil.set("USER_TOKEN:USER_TOKEN_" + userId, accessToken, interfaceConfig.getTime());
            log.info("数据插入缓存" + accessToken);
        }
        return accessToken;
    }


    private String sign(String signBody) {
        byte[] prvKey = Base64.getDecoder().decode(tenant.getCliPrvKey());
        byte[] asig = EasyGmUtils.signSm3WithSm2(signBody.getBytes(StandardCharsets.UTF_8),
            tenant.getClientId().getBytes(StandardCharsets.UTF_8), prvKey);
        String signStr = new String(Base64.getEncoder().encode(asig));
        return signStr;
    }


    private String encrypt(String param) {
        byte[] encrypt = param.getBytes();
        byte[] bs = EasyGmUtils.sm4Encrypt(tenant.getClientId().getBytes(StandardCharsets.UTF_8), encrypt);
        String result = new String(Base64.getEncoder().encode(bs));
        return result;
    }


    private void decrypt(ResultBody resultBody) {
        System.out.println(JSON.toJSONString(resultBody));
        byte[] result = EasyGmUtils.sm4Decrypt(tenant.getClientId().getBytes(StandardCharsets.UTF_8),
            Base64.getDecoder().decode(resultBody.getOutput()));
        resultBody.setOutput(new String(result));
    }
}
