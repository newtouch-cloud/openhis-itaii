package com.openhis.service;

import java.util.Map;

import com.alibaba.fastjson2.JSONObject;
import com.openhis.domain.BaseResponse;
import com.openhis.pojo.RequestData;
import com.openhis.vo.BaseParam;

/**
 *
 */
public interface IElepHandlerService {

	/**
	 * 发送数据
	 * @param requestData 请求数据
	 * @return 请求数据
	 */
	BaseResponse elepHandle(RequestData requestData, BaseParam baseParam);

	/**
	 * 初始化接口报文
	 * @param intNum 类型
	 * @param requestData 请求数据
	 * @return
	 */
	JSONObject initTransBody(String intNum, RequestData requestData, BaseParam baseParam);


	/**
	 * 发送数据
	 * @param url url
	 * @param request 请求数据
	 * @return
	 */
	BaseResponse sendDatas(String url, JSONObject request,BaseParam baseParam);


}
