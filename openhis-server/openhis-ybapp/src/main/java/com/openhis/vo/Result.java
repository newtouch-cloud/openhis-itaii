package com.openhis.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel(value="接口返回对象", description="接口返回对象")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 成功标志
	 */
	@ApiModelProperty(value = "成功标志")
	private boolean success = true;

	/**
	 * 返回处理消息
	 */
	@ApiModelProperty(value = "返回处理消息")
	private String message = "操作成功！";

	/**
	 * 返回代码
	 */
	@ApiModelProperty(value = "返回代码")
	private Integer code = 0;
	
	/**
	 * 返回数据对象 data
	 */
	@ApiModelProperty(value = "返回数据对象")
	private T result;
	
	/**
	 * 时间戳
	 */
	@ApiModelProperty(value = "时间戳")
	private long timestamp ;

	/**
	 * 创建一个错误响应
	 * @param message 错误消息
	 * @return Result 对象
	 */
	public static <T> Result<T> error(String message) {
		Result<T> result = new Result<>();
		result.setSuccess(false);
		result.setMessage(message);
		result.setCode(500); // 通常用非零表示错误
		result.setTimestamp(System.currentTimeMillis());
		return result;
	}
	/**
	 * 创建一个错误响应
	 * @param message 错误消息
	 * @return Result 对象
	 */
	public static <T> Result<T> error(String message,T data) {
		Result<T> result = new Result<>();
		result.result = data;
		result.setSuccess(false);
		result.setMessage(message);
		result.setCode(500); // 通常用非零表示错误
		result.setTimestamp(System.currentTimeMillis());
		return result;
	}

	public static <T> Result<T> okMsg(String message) {
		Result<T> result = new Result<>();
		result.setSuccess(true);
		result.setMessage(message);
		result.setCode(200); // 通常用非零表示错误
		result.setTimestamp(System.currentTimeMillis());
		return result;
	}

	/**
	 * 创建一个成功响应
	 * @param result 数据对象
	 * @return Result 对象
	 */
	public static <T> Result<T> success(T result) {
		Result<T> resultObj = new Result<>();
		resultObj.result = result;
		resultObj.setSuccess(true);
		resultObj.setMessage("操作成功！");
		resultObj.setCode(200);
		resultObj.setResult(result);
		resultObj.setTimestamp(System.currentTimeMillis());
		return resultObj;
	}

	/**
	 * 创建一个成功响应
	 * @param result 数据对象
	 * @return Result 对象
	 */
	public static <T> Result<T> ok(T result) {
		Result<T> resultObj = new Result<>();
		resultObj.result = result;
		resultObj.setSuccess(true);
		resultObj.setMessage("操作成功！");
		resultObj.setCode(200);
		resultObj.setResult(result);
		resultObj.setTimestamp(System.currentTimeMillis());
		return resultObj;
	}

	/**
	 * 创建一个成功响应
	 * @param result 数据对象
	 * @return Result 对象
	 */
	public static <T> Result<T> ok(String msg, T result) {
		Result<T> resultObj = new Result<>();
		resultObj.setSuccess(true);
		resultObj.setMessage(msg);
		resultObj.setCode(200);
		resultObj.setResult(result);
		resultObj.setTimestamp(System.currentTimeMillis());
		return resultObj;
	}

}