package com.openhis.service;

import com.openhis.pojo.InputData;
import com.openhis.pojo.ResultBody;
import com.openhis.vo.BaseParam;

import java.util.Map;
/**
 *
 */
public interface HandlerService {

	/**
	 * 发送数据
	 * @param inputData
	 * @return
	 */
	ResultBody handle(InputData inputData, BaseParam baseParam);

	/**
	 * 初始化接口报文
	 * @param intNum
	 * @param inputData
	 * @return
	 */
	String initTransBody(String intNum,InputData inputData,BaseParam baseParam);

	/**
	 * 发送数据
	 * @return
	 */
	Map<String,String> getHeader(BaseParam baseParam);

	/**
	 * 发送数据
	 * @param url
	 * @param transBody
	 * @return
	 */
	ResultBody sendDatas(String url, String transBody,BaseParam baseParam);

	/**
	 * 获取token
	 * @return
	 */
	String getToken(BaseParam baseParam);
}
