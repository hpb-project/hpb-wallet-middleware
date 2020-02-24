package com.hpb.bc.service.impl;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hpb.bc.entity.result.Result;
import com.hpb.bc.entity.result.ResultCode;
import com.hpb.bc.service.BaseService;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;

public abstract class AbstractBaseService implements BaseService {
	
	public Logger log = LoggerFactory.getLogger(this.getClass());
	
	public Result<?> execute(String methodName,List<String> reqStrList){
		
		Class<? extends AbstractBaseService> clazz = this.getClass(); 
		
        try {
			Method method = clazz.getDeclaredMethod(methodName);
			method.setAccessible(true);
			return (Result<?>) method.invoke(this, reqStrList);
		} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.error(e.getMessage(), e);
			return new Result<>(ResultCode.EXCEPTION, e.getMessage());
		}
	
	}

}
