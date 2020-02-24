package com.hpb.bc.entity.result;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import io.hpb.web3.protocol.core.Response.Error;

public class Result<T> implements Serializable{

	private static final long serialVersionUID = -5548534177186829713L;
	
	public static final String RESULT_SUCCESS_1 = "1";
	
	public static final String RESULT_FAIL_0 = "0";
	
	private String code;
	private String msg;
	private T data;
	
	public Result() {}
	
	public Result(ResultCode resultCode) {
		this.code = resultCode.code();
		this.msg = resultCode.msg();
	}
	
	public Result(ResultCode respCode, T data) {
		this(respCode);
		this.data = data;
	}
	
	public Result(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public Result(T data) {
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<Object> SUCCESS(){
		this.code = ResultCode.SUCCESS.code();
		this.msg = ResultCode.SUCCESS.msg();
		return result(this.code,this.msg,this.data);
	}
	
	public List<Object> FAIL(){
		this.code = ResultCode.FAIL.code();
		this.msg = ResultCode.FAIL.msg();
		return result(this.code,this.msg,null);
	}
	
	public List<Object> error(Error error){
		this.code = ResultCode.FAIL.code();
		this.msg = error.getMessage();
		return result(this.code,this.msg,null);
	}
	
	public List<Object> exception(Exception exception){
		this.code = ResultCode.FAIL.code();
		this.msg = ResultCode.ERROR_EXCEPTION.code();
		return result(this.code,this.msg,null);
	}
	
	public List<Object> result(String code,String msg,T data){
		this.code = code;
		this.msg = msg;
		this.data = data;
		return Arrays.asList(this.code,this.msg,this.data);
	}
	
	public List<Object> result(String code,String msg){
		return result(code,msg,this.data);
	}
	
	public List<Object> result(){
		return result(this.code,this.msg,this.data);
	}
	
}
