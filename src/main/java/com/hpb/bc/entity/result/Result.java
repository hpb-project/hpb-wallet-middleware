/*
 * Copyright 2020 HPB Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
