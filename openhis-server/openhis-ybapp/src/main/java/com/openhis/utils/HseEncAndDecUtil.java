package com.openhis.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.bouncycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Iterator;
import java.util.Map;

import static java.util.Base64.getEncoder;


/**
 * 电子凭证加解密和.
 */
public class HseEncAndDecUtil {


    /**
     * sm2签名
     * @param message 未加密报文
     * @param sm4key 渠道sm4密钥
     * @param prvKey 渠道私钥
     * @return 签名串 String
     * @throws Exception
     */
    public static String signature(String message,String sm4key,String prvKey,String appId){
        byte[] messageByte;
        try {
            JSONObject jsonObject = JSON.parseObject(message);
            removeEmpty(jsonObject);
            messageByte = SignUtil.getSignText(jsonObject, sm4key).getBytes("UTF-8");
        }catch (Exception e){
            messageByte = message.getBytes();
        }
        System.out.println("待签名的参数："+ messageByte);
        //byte[] prvKeyBite = Base64.getDecoder().decode(prvKey);
        //byte[] asig = EasyGmUtils.signSm3WithSm2(message.getBytes(StandardCharsets.UTF_8),
                //appId.getBytes(StandardCharsets.UTF_8), prvKeyBite);
        //String signStr = new String(Base64.getEncoder().encode(secKey));
        //return signStr;
        byte[] chnlSecretByte = sm4key.getBytes();
        byte[] prvkey = Base64.getDecoder().decode(prvKey);
        return Base64.getEncoder().encodeToString(EasyGmUtils.signSm3WithSm2(messageByte, chnlSecretByte, prvkey));
    }

    /**
     * sm2验签
     * @param msg sm4解密后报文
     * @param source 原始响应报文
     * @param signData 签名串
     * @param sm4key 渠道密钥
     * @param pubKey 平台公钥
     * @return 验证是否通过 boolean
     * @throws Exception
     */
    public static boolean verify(String msg,String source, String signData,String sm4key,String pubKey){
        byte[] msgByte;
        try {
            JSONObject jsonObject = JSON.parseObject(msg);
            JSONObject jsonObjects = JSON.parseObject(source);
            jsonObjects.remove("signData");
            jsonObjects.remove("encData");
            jsonObjects.put("data",jsonObject);
            removeEmpty(jsonObject);
            String str = SignUtil.getSignText(jsonObjects, sm4key);
            msgByte = str.getBytes("UTF-8");
        }catch (Exception e){
            msgByte = msg.getBytes();
        }
        byte[] signatureByte = Base64.getDecoder().decode(signData),
                chnlSecretByte = sm4key.getBytes(),
                pubKeyByte = Base64.getDecoder().decode(pubKey);

        return EasyGmUtils.verifySm3WithSm2(msgByte, chnlSecretByte, signatureByte, pubKeyByte);
    }


    /**
     * sm4加密
     * @param chnlId 渠道id
     * @param sm4key 渠道sm4密钥
     * @param message 待加密报文
     * @return 加密后的报文内容 String
     * @throws Exception
     */
    public static String sm4Encrypt(String chnlId,String sm4key,String message) throws Exception {
        //用appId加密appSecret获取新秘钥
        byte[] appSecretEncData = EasyGmUtils.sm4Encrypt(chnlId.substring(0, 16).getBytes("UTF-8"), sm4key.getBytes("UTF-8"));
        //新秘钥串
        byte[] secKey = Hex.toHexString(appSecretEncData).toUpperCase().substring(0, 16).getBytes("UTF-8");
        //加密数据
        return Hex.toHexString(EasyGmUtils.sm4Encrypt(secKey, message.getBytes("UTF-8"))).toUpperCase();
    }

    /**
     * sm4解密
     * @param chnlId 渠道id
     * @param sm4key 渠道sm4密钥
     * @param message 待解密报文
     * @return 解密后的报文 String
     * @throws Exception
     */
    public static String sm4Decrypt (String chnlId,String sm4key,String message) throws Exception{
        //生产解密key
        byte[] appSecretEncDataDecode = EasyGmUtils.sm4Encrypt(chnlId.substring(0, 16).getBytes("UTF-8"), sm4key.getBytes("UTF-8"));
        byte[] secKeyDecode = Hex.toHexString(appSecretEncDataDecode).toUpperCase().substring(0, 16).getBytes("UTF-8");
        return new String(EasyGmUtils.sm4Decrypt(secKeyDecode, Hex.decode(message)));
    }

    private final static String version = "1.0.0";
    private final static String encType = "sm4";
    private final static String signType = "sm2";
    /**
     * 创建请求报文
     * @param chnlId 渠道id
     * @param encData 加密的报文
     * @param signData 签名的报文
     * @param transType 请求接口名
     * @return
     */
    public static JSONObject buildMsg(String chnlId,String encData,String signData,String transType){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appId",chnlId);
        jsonObject.put("encData",encData);
        jsonObject.put("encType",encType);
        jsonObject.put("signData",signData);
        jsonObject.put("signType",signType);
        jsonObject.put("timestamp", System.currentTimeMillis());
        jsonObject.put("transType",transType);
        jsonObject.put("version",version);
        return jsonObject;
    }

    /**
     * 创建和加密请求报文
     * @param chnlId 渠道id
     * @param sm4key 渠道sm4密钥
     * @param prvkey 渠道私钥
     * @param transType 请求接口名
     * @param body 原始未加密的请求报文体
     * @return
     * @throws Exception
     */
    public static JSONObject encryptMsg(String chnlId,String sm4key,String prvkey,String transType,JSONObject body) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appId",chnlId);
        jsonObject.put("encType",encType);
        jsonObject.put("data",body);
        jsonObject.put("signType",signType);
        jsonObject.put("timestamp", System.currentTimeMillis());
        jsonObject.put("transType",transType);
        jsonObject.put("version",version);
        //加密后的报文
        String encData = sm4Encrypt(chnlId,sm4key, body.toJSONString());
        //签名
        String signData = signature(jsonObject.toJSONString(), sm4key, prvkey,chnlId);
        jsonObject.remove("data");
        jsonObject.put("encData",encData);
        jsonObject.put("signData",signData);
        return jsonObject;
    }

    /**
     * 解密报文
     * @param jsonObject 医保电子凭证响应的原始加密报文
     * @param sm4key 渠道sm4密钥
     * @param pubKey 平台公钥
     * @param chnlIdSrc 渠道id（兼容旧中台报文返回参数无渠道id参数情况）
     * @return
     * @throws Exception
     */
    public static String decryptMsg(JSONObject jsonObject,String sm4key,String pubKey,String chnlIdSrc) throws Exception {
        String chnlId = (String) jsonObject.get("appId");
        chnlId = StringUtil.isEmpty(chnlId)?chnlIdSrc:chnlId;
        String msg = (String) jsonObject.get("encData");
        String message = (String) jsonObject.get("message");
        String code = (String) jsonObject.get("code");
        if (!"0".equals(code)){
            throw new RuntimeException(message);
        }
        //解密
        String msgS = sm4Decrypt(chnlId,sm4key,msg);
        //验签
        String signData = (String) jsonObject.get("signData");
        boolean flag = verify(msgS,jsonObject.toJSONString(),signData,sm4key,pubKey);
        if (!flag){
            throw new RuntimeException("验签失败！！！");
        }
        return msgS;
    }

    /**
     * 移除json中空值的键值对
     * @param jsonObject
     */
    private static void removeEmpty(JSONObject jsonObject){
        Iterator<Map.Entry<String, Object>> it = jsonObject.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, Object> entry = it.next();
            Object value = entry.getValue();
            if (value instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) value;
                // 数组长度为0时将其处理,防止Gson转换异常
                if (jsonArray.size() == 0) {
                    it.remove();
                } else {
                    for (Object o : jsonArray) {
                        JSONObject asJsonObject = (JSONObject) o;
                        removeEmpty(asJsonObject);
                    }
                }
            }
            if (value instanceof JSONObject) {
                JSONObject asJsonObject = (JSONObject) value;
                removeEmpty(asJsonObject);
            }
            if (value == null){
                it.remove();
            }
            if (value instanceof String &&StringUtil.isEmpty(value)){
                it.remove();
            }
        }
    }

    public static void main(String[] args) throws Exception{

        /**
         * 生成sm2公私钥
         */
//        System.out.println(SM2Util.getKeyPairStr());


        /**
         * 渠道id
         */
        String chnlId = "";
        /**
         * 渠道私钥
         */
        String prvkey = "TcuVE2p/=";
        /**
         * 渠道密钥
         */
        String sm4key = "";
        /**
         * 平台公钥
         */
        String pubKey = "+++Vvnrxdp8CnkYFXBdu6c=";

        /**
         * 报文体
         */
        JSONObject body = new JSONObject();
        body.put("appUserId","ohNH9sgKsmJC3tR_spm9jcQx_bh");
        body.put("appId",chnlId);
        body.put("idNo","");
        body.put("idType","01");
        body.put("userName","");
        body.put("authCode","111111");
        body.put("redirectURL","www.baidu.com");
        body.put("phone","18050860136");
        body.put("certificateStatus","asdas");


        JSONObject s1 = encryptMsg(chnlId,sm4key,prvkey,"ec.gen.link",body);
        System.out.println("加密后的报文:"+s1);

        String s2 = decryptMsg(s1,sm4key,pubKey,chnlId);
        System.out.println("解密后的报文:"+s2);

    }

}
